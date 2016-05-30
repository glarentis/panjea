/**
 * 
 */
package it.eurotn.panjea.rich.dialogs;

import java.awt.Window;

import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.dialog.ConfirmationDialog;
import org.springframework.richclient.util.RcpSupport;

/**
 * Dialogo di conferma SI,NO,ANNULLA.<br>
 * Mantengo finishCommand per il SI, aggiungo negateCommand per NO e mantengo cancelCommand per ANNULLA (chiudendo il
 * dialog con i controlli di sistema del dialog viene chiamato il cancel command).<br>
 * 
 * @author Leonardo
 */
public abstract class AskDialog extends ConfirmationDialog {

    private class NegateDialogCommand extends ActionCommand {

        /**
         * Costruttore.
         */
        public NegateDialogCommand() {
            super(getNegateCommandId());
            RcpSupport.configure(this);
        }

        @Override
        protected void doExecuteCommand() {
            onNegate();
        }

    }

    private static final String DEFAULT_NEGATE_COMMAND_ID = "noCommand";
    private static final String DISCARD_COMMAND_ID = "discardCommand";

    private ActionCommand negateCommand = null;

    /**
     * Costruttore.
     */
    public AskDialog() {
        super();
    }

    /**
     * Costruttore.
     * 
     * @param title
     *            title
     * @param message
     *            message
     */
    public AskDialog(final String title, final String message) {
        super(title, message);
    }

    /**
     * Costruttore.
     * 
     * @param title
     *            title
     * @param parent
     *            parent window che apre il dialog
     * @param message
     *            message
     */
    public AskDialog(final String title, final Window parent, final String message) {
        super(title, parent, message);
    }

    @Override
    protected String getCancelCommandId() {
        return DISCARD_COMMAND_ID;
    }

    @Override
    protected Object[] getCommandGroupMembers() {
        return new AbstractCommand[] { getFinishCommand(), getNegateCommand(), getCancelCommand() };
    }

    /**
     * @return the discardCommand to get
     */
    protected ActionCommand getNegateCommand() {
        if (negateCommand == null) {
            negateCommand = new NegateDialogCommand();
        }
        return negateCommand;
    }

    /**
     * Returns the id for the cancel command.
     * 
     * @return discard command id
     */
    protected String getNegateCommandId() {
        return DEFAULT_NEGATE_COMMAND_ID;
    }

    /**
     * Metodo chiamato sull'annulla.
     */
    protected abstract void onNegate();

}
