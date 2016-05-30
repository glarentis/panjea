package it.eurotn.panjea.mrp.rich.editors.risultato.command;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.core.DefaultMessage;
import org.springframework.richclient.core.Message;
import org.springframework.richclient.core.Severity;
import org.springframework.richclient.dialog.MessageDialog;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

import it.eurotn.panjea.magazzino.exception.FormulaMrpCalcoloArticoloException;
import it.eurotn.panjea.mrp.rich.bd.IMrpBD;
import it.eurotn.panjea.mrp.util.ParametriMrpRisultato;
import it.eurotn.rich.dialog.MessageAlert;

public class CalcolaMrpCommand extends ActionCommand {

    public static final String PARAMETRI_MRP = "parametriMrp";

    /**
     * Costruttore.
     */
    public CalcolaMrpCommand() {
        super("calcoloMrpCommand");
        RcpSupport.configure(this);
    }

    @Override
    protected void doExecuteCommand() {
        ParametriMrpRisultato parametriMrpRisultato = (ParametriMrpRisultato) getParameter(
                PARAMETRI_MRP);
        if (parametriMrpRisultato != null) {
            Message messageMrp = new DefaultMessage("Calcolo mrp in corso...", Severity.INFO);
            MessageAlert mrpAlert = new MessageAlert(messageMrp);
            mrpAlert.showAlert();
            try {
                IMrpBD mrpBD = RcpSupport.getBean(IMrpBD.BEAN_ID);
                mrpBD.calcolaMrp(parametriMrpRisultato.getNumBucket(),
                        parametriMrpRisultato.getDataInizio(),
                        parametriMrpRisultato.getAreaOrdine().getId());
                OpenEditorEvent event = new OpenEditorEvent(parametriMrpRisultato);
                Application.instance().getApplicationContext().publishEvent(event);
            } catch (FormulaMrpCalcoloArticoloException e) {
                Message message = new DefaultMessage(e.getMessageComplete(), Severity.ERROR);
                MessageDialog ma = new MessageDialog("ERRORE CALCOLO FORMULA", message);
                ma.showDialog();
            } catch (Exception e) {
                logger.error("-->errore nel calcolo mrp ", e);
                String errorMessage = "Errore nel calcolo mrp...\n" + e.getMessage();
                if (e.getCause() != null
                        && e.getCause().getCause() instanceof ArrayIndexOutOfBoundsException) {
                    errorMessage = "Numero di giorni dell'orizzonte temporale insufficiente!";
                }
                Message messageErrorMrp = new DefaultMessage(errorMessage, Severity.ERROR);
                MessageAlert mrpErrorAlert = new MessageAlert(messageErrorMrp, 2000);
                mrpErrorAlert.addCloseCommandVisible();
                mrpErrorAlert.showAlert();
            } finally {
                mrpAlert.closeAlert();
            }
        }
    }

}
