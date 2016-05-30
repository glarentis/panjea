package it.eurotn.panjea.anagrafica.manager;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;
import org.jboss.annotation.IgnoreDependency;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;
import org.joda.time.DateTime;

import com.logicaldoc.webservice.document.WSDocument;

import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.manager.interfaces.MailManager;
import it.eurotn.panjea.anagrafica.util.EntitaDocumento;
import it.eurotn.panjea.anagrafica.util.MailDTO;
import it.eurotn.panjea.anagrafica.util.parametriricerca.ParametriRicercaMail;
import it.eurotn.panjea.dms.domain.DmsSettings;
import it.eurotn.panjea.dms.exception.DMSLoginException;
import it.eurotn.panjea.dms.exception.DmsException;
import it.eurotn.panjea.dms.manager.allegati.AllegatoEmail;
import it.eurotn.panjea.dms.manager.interfaces.DMSAllegatoManager;
import it.eurotn.panjea.dms.manager.interfaces.DMSSettingsManager;
import it.eurotn.panjea.dms.manager.transfer.LogicalDocEjbTransferFile;
import it.eurotn.panjea.exception.GenericException;
import it.eurotn.panjea.parametriricerca.domain.Periodo;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.security.JecPrincipal;

/**
 * Manager per classi anagrafiche di anagrafica.
 *
 * @author Leonardo
 * @version 1.0, 28/dic/07
 */
@Stateless(name = "Panjea.MailManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.MailManager")
public class MailManagerBean implements MailManager {

    private static final Logger LOGGER = Logger.getLogger(MailManagerBean.class);

    @Resource
    private SessionContext context;

    @EJB(beanName = "DMSEmailAllegatoManagerBean")
    private DMSAllegatoManager dmsAllegatoManager;

    @EJB
    private DMSSettingsManager dmsSettingsManager;

    @EJB
    @IgnoreDependency
    private LogicalDocEjbTransferFile logicalDocEjbTransferFile;

    @EJB
    private PanjeaDAO panjeaDAO;

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    @Override
    public void cancellaMail(Integer id) {
        LOGGER.debug("--> Enter cancellaMail");

        Query query = panjeaDAO.prepareNamedQuery("Mail.deleteMailById");
        query.setParameter("paramId", id);
        try {
            panjeaDAO.executeQuery(query);
        } catch (Exception e) {
            LOGGER.error("--> Errore durante la cancellazione della mail", e);
            throw new GenericException("Errore durante la cancellazione della mail", e);
        }

        LOGGER.debug("--> Exit cancellaMail");
    }

    @Override
    public List<MailDTO> caricaMails(ParametriRicercaMail parametriRicercaMail) {
        Periodo periodo = parametriRicercaMail.getPeriodo();

        Date dataInizio = new DateTime(1900, 1, 1, 0, 0).toDate();
        Date dataFine = new DateTime(2900, 1, 1, 0, 0).toDate();
        if (periodo != null) {
            dataInizio = periodo.getDataIniziale() != null ? periodo.getDataIniziale() : dataInizio;
            // inizio a cercare dall'inizio del giorno
            dataInizio = DateUtils.truncate(dataInizio, Calendar.DAY_OF_MONTH);

            // cerco fino alla fine del giorno indicato
            Calendar calDataFine = Calendar.getInstance();
            calDataFine.setTime(periodo.getDataFinale() != null ? periodo.getDataFinale() : dataFine);
            calDataFine.set(Calendar.MILLISECOND, 0);
            calDataFine.set(Calendar.SECOND, 59);
            calDataFine.set(Calendar.MINUTE, 59);
            calDataFine.set(Calendar.HOUR_OF_DAY, 23);
            dataFine = calDataFine.getTime();
        }

        EntitaDocumento entitaDocumento = null;
        if (parametriRicercaMail.getEntita() != null) {
            entitaDocumento = parametriRicercaMail.getEntita().creaEntitaDocumento();
        }
        AllegatoEmail allegatoEmail = new AllegatoEmail(entitaDocumento, getCodiceAzienda(), dataInizio, dataFine,
                parametriRicercaMail.getTesto());
        List<WSDocument> allegatiTrovati = null;
        try {
            allegatiTrovati = dmsAllegatoManager.getAllegati(allegatoEmail);
        } catch (DMSLoginException e) {
            throw new DmsException(e);
        }

        List<MailDTO> mail = new ArrayList<>();
        for (WSDocument wsDocument : allegatiTrovati) {
            mail.add(new MailDTO(wsDocument));
        }
        return mail;
    }

    /**
     *
     * @return codiceAzienda
     */
    private String getCodiceAzienda() {
        return ((JecPrincipal) context.getCallerPrincipal()).getCodiceAzienda();
    }

    /**
     * Restituisce il principal corrente.
     *
     * @return JecPrincipal
     */
    private JecPrincipal getCurrentPrincipal() {
        return (JecPrincipal) context.getCallerPrincipal();
    }

    @Override
    public void salvaMail(byte[] emlFile, EntitaLite entita, boolean inviata) {
        LOGGER.debug("--> Enter salvaMail");
        salvaMail(emlFile, entita, inviata, getCurrentPrincipal().getUserName());
        LOGGER.debug("--> Exit salvaMail");
    }

    @Override
    public void salvaMail(byte[] emlFile, EntitaLite entita, boolean inviata, String utente) {
        try (InputStream emlInputStream = new ByteArrayInputStream(emlFile)) {
            MimeMessage mimeMessage = new MimeMessage(null, emlInputStream);

            DmsSettings dmsSettings = dmsSettingsManager.caricaDmsSettings();

            String nomeFile = mimeMessage.getSubject();

            WSDocument document = new WSDocument();
            document.setPath(dmsSettings.getFolder(mimeMessage, entita, getCodiceAzienda()));
            document.setTitle(nomeFile);
            document.setFileName(nomeFile + ".eml");
            document.setObject(mimeMessage.getSubject());
            InternetAddress fromAddress = (InternetAddress) mimeMessage.getFrom()[0];
            document.setSource(StringUtils.defaultString(fromAddress.getAddress()));
            InternetAddress toAddress = (InternetAddress) mimeMessage.getRecipients(RecipientType.TO)[0];
            document.setRecipient(toAddress.getAddress());
            document.setSourceAuthor(utente);
            document.setSourceType(inviata ? "Inviata" : "Non inviata");

            String uuid = logicalDocEjbTransferFile.putContent(nomeFile + ".eml", emlFile);

            EntitaDocumento entitaDocumento = entita != null ? entita.creaEntitaDocumento() : null;
            AllegatoEmail allegatoEmail = new AllegatoEmail(entitaDocumento, getCodiceAzienda());
            allegatoEmail.setData(mimeMessage.getSentDate());
            dmsAllegatoManager.addAllegato(uuid, document, allegatoEmail);

        } catch (Exception e) {
            LOGGER.error("--> errore durante la pubblicazione della mail nella gestione documentale.", e);
            throw new RuntimeException("errore durante la pubblicazione della mail nella gestione documentale.", e);
        }
    }
}
