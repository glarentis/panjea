/**
 *
 */
package it.eurotn.panjea.anagrafica.mail;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.richclient.core.DefaultMessage;
import org.springframework.richclient.core.Message;
import org.springframework.richclient.core.Severity;
import org.springframework.richclient.dialog.FormBackedDialogPage;
import org.springframework.richclient.dialog.MessageDialog;
import org.springframework.richclient.form.Form;
import org.springframework.richclient.util.RcpSupport;
import org.springframework.rules.closure.Closure;

import it.eurotn.panjea.anagrafica.domain.Entita;
import it.eurotn.panjea.anagrafica.domain.ParametriMail;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.rich.bd.AnagraficaBD;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaBD;
import it.eurotn.panjea.sicurezza.domain.DatiMail;
import it.eurotn.panjea.sicurezza.domain.Utente;
import it.eurotn.rich.control.mail.IPanjeaMailClient;
import it.eurotn.rich.dialog.MessageAlert;
import it.eurotn.rich.editors.PanjeaTitledPageApplicationDialog;
import it.eurotn.rich.report.editor.export.AbstractExportCommand;
import it.eurotn.rich.report.editor.export.ChooseEmailTypeDialog;
import it.eurotn.rich.services.IMailService;
import it.eurotn.rich.services.MailLocalService;

/**
 * @author fattazzo
 *
 */
public class PanjeaMailClient implements IPanjeaMailClient {

    private static Logger logger = Logger.getLogger(PanjeaMailClient.class);

    private List<AbstractExportCommand> exportCommandsSelected;

    private boolean abort;

    private ParametriMail parametriMail;

    private IMailService mailService = RcpSupport.getBean(MailLocalService.BEAN_ID);

    protected String emailValida;

    private IAnagraficaBD anagraficaBD;

    /**
     * Costruttore.
     */
    public PanjeaMailClient() {
        super();
        this.anagraficaBD = RcpSupport.getBean(AnagraficaBD.BEAN_ID);
    }

    /**
     * Restituisce l'email corretta da utilizzare.
     *
     * @param entita
     *            entità
     * @return mail
     */
    public String getEMailValida(Entita entita) {

        SedeEntita sedePrincipale = anagraficaBD.caricaSedePrincipaleEntita(entita);

        return getEMailValida(sedePrincipale.getSede().getIndirizzoMail(), sedePrincipale.getSede().getIndirizzoPEC());
    }

    /**
     * Restituisce l'email corretta da utilizzare.
     *
     * @param indirizzoEMail
     *            indirizzo email
     * @param indirizzoPEC
     *            indirizzo PEC
     * @return mail
     */
    public String getEMailValida(String indirizzoEMail, String indirizzoPEC) {

        if (!indirizzoEMail.isEmpty() && !indirizzoPEC.isEmpty()) {
            new ChooseEmailTypeDialog(indirizzoEMail, indirizzoPEC, new Closure() {

                @Override
                public Object call(Object argument) {
                    emailValida = (String) argument;
                    return null;
                }
            }).showDialog();
        } else {
            emailValida = indirizzoEMail.isEmpty() ? indirizzoPEC : indirizzoEMail;
        }

        return emailValida;
    }

    @Override
    public void send(ParametriMail parametri, AbstractExportCommand exportCommand) {
        send(parametri, exportCommand, false);
    }

    @Override
    public void send(ParametriMail parametri, AbstractExportCommand exportCommand, boolean throwException) {
        parametriMail = parametri;
        exportCommandsSelected = new ArrayList<AbstractExportCommand>();
        if (exportCommand != null) {
            exportCommandsSelected.add(exportCommand);
        }

        sendMail(true, throwException);
    }

    /**
     * Crea gli eventuali allegati selezionati ed Invia la mail.
     *
     * @param throwException
     *            <code>true</code> per non gestire l'eccezione all'interno del metodo ma rilanciarla
     */
    private void sendMail(boolean aggiungiFirma, boolean throwException) {
        try {
            for (AbstractExportCommand command : exportCommandsSelected) {
                File fileWithExt = new File(System.getProperty("java.io.tmpdir") + File.separator
                        + parametriMail.getNomeAllegato() + "." + command.getFileExtension());
                command.export(fileWithExt);
                parametriMail.addAttachments(fileWithExt.getAbsolutePath(),
                        parametriMail.getNomeAllegato() + "." + command.getFileExtension());
            }
            mailService.send(parametriMail, aggiungiFirma, throwException);
        } catch (Exception e) {
            if (throwException) {
                throw e;
            }
            MessageDialog messageDialog = new MessageDialog("Errore invio mail",
                    new DefaultMessage(
                            "Errore nella spedizione della mail. Errore ricevuto dal server\n " + e.getMessage(),
                            Severity.ERROR));
            messageDialog.showDialog();
            logger.error("-->errore nel spedire il report tramite e-mail", e);

        }
    }

    /**
     * Visualizza il dialogo per la gestione dei parametri di invio.
     *
     * @param paramParametriMail
     *            parametri email
     * @param exportCommands
     *            exportCommands
     */
    @Override
    public void show(ParametriMail paramParametriMail, AbstractExportCommand[] exportCommands) {

        Utente utente = mailService.caricaUtenteCorrente();

        parametriMail = paramParametriMail;
        parametriMail.setDatiMail(utente.getDatiMailPredefiniti());
        String testo = StringUtils.defaultString(parametriMail.getOggetto());
        parametriMail.setTesto(testo.concat("<br/><br/><br/>".concat(DatiMail.ID_DIV_FIRMA)
                .concat(utente.getDatiMailPredefiniti().getFirma()).concat("</div>")));

        Message message = new DefaultMessage("Spedizione mail...", Severity.INFO);
        MessageAlert alert = new MessageAlert(message);
        abort = false;

        PanjeaTitledPageApplicationDialog dialog = new PanjeaTitledPageApplicationDialog(
                new ParametriMailForm(parametriMail, exportCommands, utente), null) {

            @Override
            protected String getTitle() {
                return "Dati spedizione mail";
            }

            @Override
            protected void onCancel() {
                abort = true;
                super.onCancel();
            }

            @Override
            protected boolean onFinish() {
                Form form = ((FormBackedDialogPage) getDialogPage()).getBackingFormPage();
                form.commit();
                parametriMail = (ParametriMail) form.getFormObject();
                exportCommandsSelected = ((ParametriMailForm) ((FormBackedDialogPage) getDialogPage())
                        .getBackingFormPage()).getSelectedExportCommands();

                if (!parametriMail.hasAttachment() && exportCommandsSelected.isEmpty()) {
                    MessageDialog messageDialog = new MessageDialog("ATTENZIONE",
                            "Selezionare almeno un tipo di allegato.");
                    messageDialog.showDialog();
                }

                return !form.hasErrors() && (!exportCommandsSelected.isEmpty() || parametriMail.hasAttachment());
            }

        };
        dialog.setTitlePaneTitle("Dati per la spedizione del report tramite email");
        dialog.showDialog();

        if (abort) {
            return;
        }

        alert.showAlert();

        sendMail(false, false);
    }

}
