package it.eurotn.rich.command;

import java.awt.Dimension;
import java.util.Collection;

import javax.swing.AbstractButton;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.dialog.CloseAction;
import org.springframework.richclient.dialog.ConfirmationDialog;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.rich.PanjeaLifecycleApplicationEvent;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;

public abstract class AbstractDeleteCommand extends ActionCommand {

    private final class DeleteConfirmationDialog extends ConfirmationDialog {
        private boolean result;

        /**
         * Costruttore.
         * 
         * @param title
         *            titolo del messaggio
         * @param message
         *            messaggio
         */
        public DeleteConfirmationDialog(final String title, final String message) {
            super(title, message);
        }

        /**
         * @return true se conferma
         */
        public boolean getConfirm() {
            return result;
        }

        @Override
        protected void onCancel() {
            super.onCancel();
            result = false;
        }

        @Override
        protected void onConfirm() {
            result = true;
        }
    }

    public static final String DELETE_COMMAND = "deleteCommand";

    private Object sourceContainer = null;
    private boolean isExecuted = false;

    /**
     * Costruttore di default.
     * 
     * @param securityControllerId
     *            chiave per la sicurezza
     */
    public AbstractDeleteCommand(final String securityControllerId) {
        super(DELETE_COMMAND);
        this.setSecurityControllerId(securityControllerId);
        RcpSupport.configure(this);
    }

    /**
     * @return true or false
     */
    protected boolean confirmOperation() {
        // Richiesta conferma
        String message = RcpSupport.getMessage(getConfirmMessageKey());
        String title = RcpSupport.getMessage(getConfirmTitleKey());

        DeleteConfirmationDialog confirmationDialog = new DeleteConfirmationDialog(title, message);
        confirmationDialog.setPreferredSize(new Dimension(250, 50));
        confirmationDialog.setCloseAction(CloseAction.HIDE);
        confirmationDialog.setResizable(false);
        confirmationDialog.showDialog();

        isExecuted = confirmationDialog.getConfirm();

        confirmationDialog = null;
        return isExecuted;
    }

    @Override
    protected void doExecuteCommand() {
        if (!confirmOperation()) {
            return;
        }

        Object deletedObject = null;

        deletedObject = onDelete();

        if (deletedObject != null) {
            if (sourceContainer == null) {
                sourceContainer = this;
            }

            if (deletedObject instanceof Collection<?>) {
                Collection<?> resultList = (Collection<?>) deletedObject;
                for (Object object : resultList) {
                    PanjeaLifecycleApplicationEvent deleteEvent = new PanjeaLifecycleApplicationEvent(
                            LifecycleApplicationEvent.DELETED, object, sourceContainer);
                    Application.instance().getApplicationContext().publishEvent(deleteEvent);

                    // HACK come source object lancio -1 per distinguere la change object delle cancellazione da quella
                    // di update/insert
                    if (sourceContainer instanceof FormBackedDialogPageEditor) {
                        ((FormBackedDialogPageEditor) sourceContainer).firePropertyChangeNew(
                                IPageLifecycleAdvisor.OBJECT_CHANGED, new Integer(-1), deletedObject);
                    }
                }
            } else {
                PanjeaLifecycleApplicationEvent deleteEvent = new PanjeaLifecycleApplicationEvent(
                        LifecycleApplicationEvent.DELETED, deletedObject, sourceContainer);
                Application.instance().getApplicationContext().publishEvent(deleteEvent);

                // HACK come source object lancio -1 per distinguere la change object delle cancellazione da quella di
                // update/insert
                if (sourceContainer instanceof FormBackedDialogPageEditor) {
                    ((FormBackedDialogPageEditor) sourceContainer).firePropertyChangeNew(
                            IPageLifecycleAdvisor.OBJECT_CHANGED, new Integer(-1), deletedObject);
                }
            }
        }
    }

    /**
     * @return chiave del messaggio di conferma.
     */
    protected String getConfirmMessageKey() {
        return "panjea.message.confirm.delete";
    }

    /**
     * @return chiave per il titolo della finestra di conferma
     */
    protected String getConfirmTitleKey() {
        return "panjea.title.confirm.delete";
    }

    /**
     * @return command executed or not
     */
    public boolean isExecuted() {
        return isExecuted;
    }

    @Override
    protected void onButtonAttached(AbstractButton button) {
        super.onButtonAttached(button);
        button.setName(getSecurityControllerId() + DELETE_COMMAND);
    }

    /**
     * Metodo per implementare la cancellazione.
     * 
     * @return l'oggetto cancellato
     */
    public abstract Object onDelete();

    /**
     * @param paramSourceContainer
     *            sorgente dell'evento
     */
    public void setSourceContainer(Object paramSourceContainer) {
        this.sourceContainer = paramSourceContainer;
    }

}