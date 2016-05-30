package it.eurotn.rich.services;

import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;
import org.springframework.richclient.core.DefaultMessage;
import org.springframework.richclient.core.Severity;
import org.springframework.richclient.dialog.MessageDialog;

import it.eurotn.panjea.anagrafica.domain.Destinatario;
import it.eurotn.panjea.anagrafica.domain.ParametriMail;
import it.eurotn.panjea.exception.GenericException;
import it.eurotn.panjea.sicurezza.domain.DatiMail;
import it.eurotn.panjea.sicurezza.domain.Utente;
import it.eurotn.panjea.sicurezza.service.interfaces.SicurezzaService;
import it.eurotn.panjea.utils.PanjeaSwingUtil;

/**
 * Sevizio per la spedizione di mail.
 */
public class MailLocalService implements IMailService {

    private static final Logger LOGGER = Logger.getLogger(MailLocalService.class);

    public static final String BEAN_ID = "mailLocalService";

    private SicurezzaService sicurezzaService;

    /**
     * @return carica l'utente corrente
     */
    @Override
    public Utente caricaUtenteCorrente() {
        Utente utente = null;
        try {
            utente = sicurezzaService.caricaUtente(PanjeaSwingUtil.getUtenteCorrente().getUserName());
        } catch (Exception e1) {
            LOGGER.error("--> errore impossibile ricuperare il utente ", e1);
            throw new RuntimeException(e1);
        }
        return utente;
    }

    @Override
    public boolean send(ParametriMail parametri, boolean aggiungiFirma, final boolean throwException) {
        LOGGER.debug("--> Enter sendMail");

        // recupero l' utente attuale del sitema
        Utente utente = caricaUtenteCorrente();

        if (parametri.getDatiMail() == null || parametri.getDatiMail().isNew()) {
            parametri.setDatiMail(utente.getDatiMailPredefiniti());
        }

        if (!parametri.getDatiMail().isValid()) {
            if (throwException) {
                throw new GenericException("Configurazione non corretta per l'invio della mail");
            }
            MessageDialog message = new MessageDialog("Configurazione mancante",
                    new DefaultMessage("Configurazione non corretta per l'invio della mail", Severity.ERROR));
            message.showDialog();
            return false;
        }

        SendMailTask mailTask = new SendMailTask(parametri, utente, aggiungiFirma);

        // per il catch delle exceptions del thread
        Thread.UncaughtExceptionHandler handler = new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread th, final Throwable ex) {
                MessageDialog messageDialog = new MessageDialog("Errore invio mail",
                        new DefaultMessage(
                                "Errore nella spedizione della mail. Errore ricevuto dal server\n " + ex.getMessage(),
                                Severity.ERROR));
                messageDialog.showDialog();
            }
        };
        try {
            if (SwingUtilities.isEventDispatchThread()) {
                Thread thread = new Thread(mailTask);
                thread.setUncaughtExceptionHandler(handler);
                thread.start();
            } else {
                mailTask.run();
            }

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return true;
    }

    /**
     * @param sicurezzaService
     *            the sicurezzaService to set
     */
    public void setSicurezzaService(SicurezzaService sicurezzaService) {
        this.sicurezzaService = sicurezzaService;
    }

    @Override
    public boolean testConnection(Utente utente, DatiMail datiMail) {
        if (utente == null) {
            utente = caricaUtenteCorrente();
        }

        if (!datiMail.isValid()) {
            MessageDialog message = new MessageDialog("Configurazione mancante",
                    new DefaultMessage("Configurazione non corretta per l'invio della mail", Severity.ERROR));
            message.showDialog();
            return false;
        }

        try {
            ParametriMail parametriMail = new ParametriMail();
            parametriMail.setOggetto("Test Mail");
            parametriMail.setTesto("Test Mail");
            Destinatario destinatario = new Destinatario();
            destinatario.setEmail("panjea@eurotn.it");
            parametriMail.getDestinatari().add(destinatario);
            parametriMail.setDatiMail(datiMail);

            SendMailTask mailTask = new SendMailTask(parametriMail, utente, false, false);
            mailTask.run();
        } catch (Exception e) {
            LOGGER.error("--> errore impossibile spedire la Email ", e);
            throw new RuntimeException(e.getMessage(), e);
        }

        return true;
    }

}
