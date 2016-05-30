package it.eurotn.panjea.anagrafica.manager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.mail.Message.RecipientType;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.persistence.Query;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.ejb.TransactionTimeout;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.anagrafica.domain.Mail;
import it.eurotn.panjea.anagrafica.manager.interfaces.ImportazioneMailManager;
import it.eurotn.panjea.anagrafica.manager.interfaces.MailManager;
import it.eurotn.panjea.anagrafica.service.exception.PreferenceNotFoundException;
import it.eurotn.panjea.anagrafica.service.interfaces.PreferenceService;
import it.eurotn.panjea.exception.GenericException;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.panjea.service.interfaces.PanjeaMessage;

@Stateless(name = "Panjea.ImportazioneMailManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.ImportazioneMailManager")
public class ImportazioneMailManagerBean implements ImportazioneMailManager {

    private static final Logger LOGGER = Logger.getLogger(ImportazioneMailManagerBean.class);

    public static final String IMPORTA_MAIL_MESSAGE_SELECTOR = "importaMailMessageSelector";

    @Resource
    private SessionContext context;

    @EJB
    private PanjeaDAO panjeaDAO;

    @EJB
    private PanjeaMessage panjeaMessage;

    @EJB
    private PreferenceService preferenceService;

    @EJB
    private MailManager mailManager;

    private String caricaCartellaAllegati() {
        String cartellaAllegatiPath;
        try {
            cartellaAllegatiPath = preferenceService.caricaPreference("dirAllegati").getValore();
        } catch (PreferenceNotFoundException e1) {
            LOGGER.error("--> Directory degli allegati non impostata", e1);
            throw new GenericException("Directory degli allegati non impostata", e1);
        }

        File cartellaAllegati = new File(cartellaAllegatiPath);
        if (!cartellaAllegati.isDirectory() || !cartellaAllegati.exists()) {
            throw new GenericException(
                    "la cartella degli allegati impostata non esiste. Cartella impostata " + cartellaAllegatiPath);
        }

        return cartellaAllegatiPath;
    }

    @SuppressWarnings("unchecked")
    private List<Mail> caricaMail() {
        List<Mail> mails;
        Query query = panjeaDAO
                .prepareQuery("select m from Mail m left join fetch m.destinatario.entita order by m.timeStamp desc");
        try {
            mails = panjeaDAO.getResultList(query);
        } catch (Exception e) {
            LOGGER.error("--> errore durante il caricamento delle mail.", e);
            throw new GenericException("errore durante il caricamento delle mail.", e);
        }
        return mails;
    }

    @Override
    public Integer caricaNumeroMailDaImportare() {
        LOGGER.debug("--> Enter caricaNumeroMailDaImportare");

        Long numeroMail = 0L;

        Query query = panjeaDAO.prepareQuery("select count(m.id) from Mail m ");

        try {
            numeroMail = (Long) panjeaDAO.getSingleResult(query);
        } catch (Exception e) {
            LOGGER.error("--> errore durante il recupero del numero mail da importare", e);
            throw new GenericException("errore durante il recupero del numero mail da importare", e);
        }

        LOGGER.debug("--> Exit caricaNumeroMailDaImportare");
        return numeroMail.intValue();
    }

    private String getPathAllegato(Mail mail, String nomeAllegato, String dirAllegati) {

        String pathFile = new StringBuilder(dirAllegati).append(mail.getId().toString()).append(".")
                .append(FilenameUtils.getName(nomeAllegato)).toString();
        File attachmentFile = new File(pathFile);
        if (!Files.exists(Paths.get(pathFile))) {
            return null;
        }

        return attachmentFile.getPath();
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    @TransactionTimeout(value = 14400)
    @Override
    public void importaMail() {

        String dirAllegatiPath = caricaCartellaAllegati();

        List<Mail> mails = caricaMail();

        int currentIdx = 1;
        for (Mail mail : mails) {
            ByteArrayOutputStream emlFile = new ByteArrayOutputStream();
            String username = "";
            MimeMessage message;
            try {
                message = new MimeMessage((Session) null);
                message.addRecipient(RecipientType.TO, new InternetAddress(mail.getDestinatario().getEmail()));
                message.setSubject(mail.getOggetto());
                message.setSentDate(mail.getData());
                // testo
                MimeBodyPart textPart = new MimeBodyPart();
                textPart.setContent(mail.getTesto(), "text/html; charset=utf-8");

                InternetAddress fromAddr = new InternetAddress();
                if (mail.getUtenteDiSpedizione() != null) {
                    username = mail.getUtenteDiSpedizione().getUserName();
                    if (mail.getUtenteDiSpedizione().getDatiMailPredefiniti() != null) {
                        fromAddr = new InternetAddress(
                                mail.getUtenteDiSpedizione().getDatiMailPredefiniti().getEmail());
                    }
                }
                message.setFrom(fromAddr);

                Multipart multipart = new MimeMultipart("mixed");
                multipart.addBodyPart(textPart);

                if (!StringUtils.isBlank(mail.getNomeAllegati())) {
                    String[] nomiAllegati = StringUtils.split(mail.getNomeAllegati(), "#");
                    for (String nome : nomiAllegati) {
                        String filePath = getPathAllegato(mail, nome, dirAllegatiPath);
                        if (filePath != null) {
                            MimeBodyPart messageBodyPart = new MimeBodyPart();
                            messageBodyPart = new MimeBodyPart();
                            String fileName = FilenameUtils.getName(filePath);
                            DataSource source = new FileDataSource(filePath);
                            messageBodyPart.setDataHandler(new DataHandler(source));
                            messageBodyPart.setFileName(fileName);
                            multipart.addBodyPart(messageBodyPart);
                        }
                    }
                }
                message.setContent(multipart);

                message.writeTo(emlFile);
            } catch (Exception e) {
                LOGGER.error("--> errore durante la creazione del messaggio", e);
                message = null;
            }

            if (message != null) {
                mailManager.salvaMail(emlFile.toByteArray(), mail.getDestinatario().getEntita(), mail.isSuccesso(),
                        username);

                mailManager.cancellaMail(mail.getId());
            }

            if (currentIdx % 10 == 0) {
                panjeaMessage.send(new ImportazioneMailStateDescriptor(mails.size(), currentIdx),
                        IMPORTA_MAIL_MESSAGE_SELECTOR);
            }
            currentIdx++;
        }
        panjeaMessage.send(new ImportazioneMailStateDescriptor(mails.size(), mails.size()),
                IMPORTA_MAIL_MESSAGE_SELECTOR);
    }

}
