package it.eurotn.panjea.fatturepa.rich.manager;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.richclient.core.DefaultMessage;
import org.springframework.richclient.core.Message;
import org.springframework.richclient.core.Severity;
import org.springframework.richclient.dialog.MessageDialog;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.anagrafica.domain.Destinatario;
import it.eurotn.panjea.anagrafica.domain.ParametriMail;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.exception.GenericException;
import it.eurotn.panjea.fatturepa.domain.AreaMagazzinoFatturaPA;
import it.eurotn.panjea.fatturepa.domain.FatturaPASettings;
import it.eurotn.panjea.fatturepa.rich.bd.FatturePAAnagraficaBD;
import it.eurotn.panjea.fatturepa.rich.bd.FatturePABD;
import it.eurotn.panjea.fatturepa.rich.bd.IFatturePAAnagraficaBD;
import it.eurotn.panjea.fatturepa.rich.bd.IFatturePABD;
import it.eurotn.panjea.sicurezza.domain.DatiMail;
import it.eurotn.rich.dialog.MessageAlert;
import it.eurotn.rich.services.IMailService;
import it.eurotn.rich.services.MailLocalService;

public class InvioFatturaPAManager {

    private static final Logger LOGGER = Logger.getLogger(InvioFatturaPAManager.class);

    private IFatturePABD fatturePABD;
    private IFatturePAAnagraficaBD fatturePAAnagraficaBD;

    /**
     * Costruttore.
     */
    public InvioFatturaPAManager() {
        super();

        this.fatturePABD = RcpSupport.getBean(FatturePABD.BEAN_ID);
        this.fatturePAAnagraficaBD = RcpSupport.getBean(FatturePAAnagraficaBD.BEAN_ID);
    }

    private ParametriMail creaParametriMail(String xmlFileName, EntitaLite entita) {

        FatturaPASettings fatturaPASettings = fatturePAAnagraficaBD.caricaFatturaPASettings();

        // controllo subito che ci siano dei dati configurati per l'invio al SdI
        DatiMail datiMailPredefinitiPec = fatturaPASettings.getDatiMailInvioSdI().getDatiMail();
        if (!datiMailPredefinitiPec.isValid()) {
            throw new GenericException("Non è stato configurato nessun indirizzo PEC per l'invio della mail al SdI.");
        }
        ParametriMail parametri = new ParametriMail();
        // setto l'id a 0 altrimenti i mail service di Panjea setta i dati mail predefiniti dell'utente se non parametri
        // non ci sono dati mail o sono nuovi
        datiMailPredefinitiPec.setId(0);
        parametri.setDatiMail(datiMailPredefinitiPec);

        Destinatario destinatario = new Destinatario();
        destinatario.setEmail(fatturaPASettings.getEmailSpedizioneSdI());
        destinatario.setEntita(entita);
        Set<Destinatario> destinatari = new TreeSet<Destinatario>();
        destinatari.add(destinatario);
        parametri.setDestinatari(destinatari);
        parametri.setOggetto("Invio fattura elettronica " + xmlFileName);
        parametri.setTesto("In allegato il file xml " + xmlFileName);

        return parametri;
    }

    /**
     * Esegue l'invio della fattura PA al SdI.
     *
     * @param idAreaMagazzino
     *            id area magazzino
     */
    public void inviaFattura(Integer idAreaMagazzino) {

        if (idAreaMagazzino == null) {
            return;
        }

        FatturaPASettings fatturaPASettings = fatturePAAnagraficaBD.caricaFatturaPASettings();

        // se c'è la gestione della firma elettronica significa che questa viene fatta "in Panjea" e quindi l'invio
        // dell'XML firmato viene effettuato tramite PEC
        if (fatturaPASettings.isGestioneFirmaElettronica()) {
            inviaFatturaConPEC(idAreaMagazzino);
        } else {
            fatturePABD.invioSdiFatturaPA(idAreaMagazzino);
        }
    }

    private void inviaFatturaConPEC(Integer idAreaMagazzino) {

        AreaMagazzinoFatturaPA areaMagazzinoFatturaPA = fatturePABD.caricaAreaMagazzinoFatturaPA(idAreaMagazzino);
        if (areaMagazzinoFatturaPA.getXmlFattura().isXmlFirmato()) {

            Message message = new DefaultMessage("Invio fattura elettronica in corso...", Severity.INFO);
            final MessageAlert messageAlert = new MessageAlert(message, 0);
            messageAlert.showAlert();

            // salvo il file XML firmato in locale
            byte[] xmlFirmato = fatturePABD
                    .downloadXMLFirmato(areaMagazzinoFatturaPA.getXmlFattura().getXmlFileNameFirmato());
            Path xmlPath = null;
            try {
                // salvo tutti i file da firmare nella directory temporanea
                Path tempSignDir = Files.createTempDirectory("invioXMLFirmato");
                FileUtils.cleanDirectory(tempSignDir.toFile());

                xmlPath = Paths.get(tempSignDir.toFile() + File.separator
                        + areaMagazzinoFatturaPA.getXmlFattura().getXmlFileNameFirmato());
                Files.write(xmlPath, xmlFirmato);

            } catch (Exception e) {
                throw new RuntimeException("Errore durante il salvataggio del file xml", e);
            }

            ParametriMail parametriMail = creaParametriMail(
                    areaMagazzinoFatturaPA.getXmlFattura().getXmlFileNameFirmato(),
                    areaMagazzinoFatturaPA.getAreaMagazzino().getDocumento().getEntita());
            parametriMail.addAttachments(xmlPath.toString(), null);

            try {
                IMailService mailLocalService = RcpSupport.getBean(MailLocalService.BEAN_ID);
                mailLocalService.send(parametriMail, false, true);

                fatturePABD.salvaFatturaPAComeInviata(idAreaMagazzino);
            } catch (Exception e) {
                LOGGER.error("--> errore durante l'invio della mail.", e);
                new MessageDialog("ERRORE INVIO MAIL", e.getMessage()).showDialog();
            } finally {
                messageAlert.closeAlert();
            }
        }
    }

}
