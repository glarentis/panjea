package it.eurotn.panjea.manutenzioni.rich.editors.operatore.sostituzione;

import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.manutenzioni.domain.Operatore;

public class SostituisciOperatoreCommand extends ActionCommand {

    public static final String PARAM_OPERATORE = "paramOperatore";

    private SostituzioneDialog sostituzioneDialog = new SostituzioneDialog();

    /**
     * Costruttore.
     */
    public SostituisciOperatoreCommand() {
        super("sostituisciOperatoreCommand");
        RcpSupport.configure(this);
    }

    @Override
    protected void doExecuteCommand() {

        Operatore operatore = (Operatore) getParameter(PARAM_OPERATORE, null);
        if (operatore == null || operatore.isNew()) {
            return;
        }

        SostituzioneOperatorePM pm = new SostituzioneOperatorePM(operatore);
        sostituzioneDialog.setSostituzioneOperatorePM(pm);
        sostituzioneDialog.showDialog();

    }

}
