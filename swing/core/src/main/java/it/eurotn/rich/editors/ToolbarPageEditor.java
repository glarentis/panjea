package it.eurotn.rich.editors;

import javax.swing.AbstractButton;

import org.apache.log4j.Logger;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.core.DefaultMessage;
import org.springframework.richclient.core.Guarded;
import org.springframework.richclient.core.Severity;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.locking.IDefProperty;
import it.eurotn.locking.ILock;
import it.eurotn.locking.exception.LockNotFoundException;
import it.eurotn.panjea.rich.bd.ILockingBD;
import it.eurotn.rich.command.AbstractDeleteCommand;

/**
 * Toolbar di default per editors che implementano <code>IPageEditor</code> predisposta con quattro funzioni di base:
 * <ul>
 * <li>nuovo</li>
 * <li>lock</li>
 * <li>salva</li>
 * <li>undo</li>
 * </ul>
 * Nell' esecuzione dei rispettivi command viene chiamato il metodo associato da implementare nella classe che
 * implementa l'interfaccia <code>IPageEditor</code>.
 *
 * @author Leonardo
 * @see it.eurotn.rich.editors.IPageEditor
 */
public class ToolbarPageEditor {

    private class DeleteCommand extends AbstractDeleteCommand {

        /**
         *
         * Costruttore.
         *
         */
        public DeleteCommand() {
            super(DELETE_COMMAND);
            setSecurityControllerId(getPageEditor().getPageSecurityEditorId() + ".controller");
            setSourceContainer(getPageEditor());

            RcpSupport.configure(this);
        }

        @Override
        protected void onButtonAttached(AbstractButton button) {
            super.onButtonAttached(button);
            button.setName(getPageEditor().getPageEditorId() + DELETE_COMMAND);
        }

        @Override
        public Object onDelete() {
            return getPageEditor().onDelete();
        }
    }

    private class LockCommand extends ActionCommand {

        /**
         *
         * Costruttore.
         *
         */
        public LockCommand() {
            super(LOCK_COMMAND);

            this.setSecurityControllerId(getPageEditor().getPageSecurityEditorId() + ".controller");
            RcpSupport.configure(this);
        }

        @Override
        protected void doExecuteCommand() {
            logger.debug("-->ENTER doExecuteCommand. Richiamo la OnLock");
            if (this.isAuthorized()) {
                getPageEditor().onLock();
            } else {
                getPageEditor();
            }
        }

        @Override
        protected void onButtonAttached(AbstractButton button) {
            super.onButtonAttached(button);
            button.setName(getPageEditor().getPageEditorId() + LOCK_COMMAND);

        }
    }

    private class NewCommand extends ActionCommand {

        /**
         *
         * Costruttore.
         *
         */
        public NewCommand() {
            super(NEW_COMMAND);
            setSecurityControllerId(getPageEditor().getPageSecurityEditorId() + ".controller");
            RcpSupport.configure(this);
        }

        @Override
        protected void doExecuteCommand() {
            getPageEditor().onNew();
        }

        @Override
        protected void onButtonAttached(AbstractButton button) {
            super.onButtonAttached(button);
            button.setName(getPageEditor().getPageEditorId() + NEW_COMMAND);
        }
    }

    public class SaveCommand extends ActionCommand implements Guarded {
        /**
         *
         * Costruttore.
         *
         */
        public SaveCommand() {
            super(SAVE_COMMAND);
            this.setSecurityControllerId(getPageEditor().getPageSecurityEditorId() + ".controller");
            RcpSupport.configure(this);
        }

        @Override
        protected void doExecuteCommand() {
            getPageEditor().onSave();
        }

        /**
         *
         * @return pageEditor associata alla toolbar.
         */
        public IFormPageEditor getPageEditor() {
            return formPageEditor;
        }

        @Override
        protected void onButtonAttached(AbstractButton button) {
            super.onButtonAttached(button);
            button.setName(getPageEditor().getPageEditorId() + SAVE_COMMAND);
        }
    }

    private class UndoCommand extends ActionCommand {
        /**
         *
         * Costruttore.
         *
         */
        public UndoCommand() {
            super(UNDO_COMMAND);
            this.setSecurityControllerId(getPageEditor().getPageSecurityEditorId() + ".controller");
            RcpSupport.configure(this);
        }

        @Override
        protected void doExecuteCommand() {
            getPageEditor().onUndo();
        }

        @Override
        protected void onButtonAttached(AbstractButton button) {
            super.onButtonAttached(button);
            button.setName(getPageEditor().getPageEditorId() + UNDO_COMMAND);
        }
    }

    private static final Logger LOGGER = Logger.getLogger(ToolbarPageEditor.class);

    public static final String UNDO_COMMAND = "undoModelCommand";
    public static final String NEW_COMMAND = "newCommand";
    public static final String SAVE_COMMAND = "saveCommand";
    public static final String LOCK_COMMAND = "lockCommand";

    public static final String LOCK_MESSAGE = "LOCKED";
    public static final String RELEASE_MESSAGE = "RELEASED";

    private IFormPageEditor formPageEditor;

    private AbstractCommand undoCommand;
    private AbstractCommand newCommand;
    private SaveCommand saveCommand;
    private ActionCommand lockCommand;
    private AbstractCommand deleteCommand;

    private ILock lock = null;

    /**
     * Crea una nuova toolbar.
     *
     * @param formPageEditor
     *            l'editor che implementa l'interfaccia <code>IPageEditor</code> a cui associare la toolbar.
     */
    public ToolbarPageEditor(final IFormPageEditor formPageEditor) {
        this.formPageEditor = formPageEditor;
    }

    /**
     * dispose della toolbar.
     */
    public void dispose() {
        this.formPageEditor = null;
        this.lock = null;
        this.lockCommand = null;
        this.newCommand = null;
        this.saveCommand = null;
        this.undoCommand = null;
        this.deleteCommand = null;
    }

    /**
     * Toolbar con tre funzioni base quali.
     * <ul>
     * <li>nuovo</li>
     * <li>salva</li>
     * <li>undo</li>
     * </ul>
     *
     * @return comandi da aggiungere di default
     */
    public AbstractCommand[] getDefaultCommand() {
        return getDefaultCommand(false);
    }

    /**
     * Restituisce la toolbar di default con quattro funzioni
     * <ul>
     * <li>nuovo</li>
     * <li>lock</li>
     * <li>salva</li>
     * <li>undo</li>
     * <li>delete</li>
     * </ul>
     * oppure con tre, escludendo il lock.
     *
     * @param insertLockCommand
     *            permette di scegliere se inserire il lock command nella toolbar
     * @return comandi da aggiungere di default
     */
    public AbstractCommand[] getDefaultCommand(boolean insertLockCommand) {
        if (insertLockCommand) {
            return new AbstractCommand[] { getNewCommand(), getLockCommand(), getSaveCommand(), getUndoCommand(),
                    getDeleteCommand() };
        } else {
            return new AbstractCommand[] { getNewCommand(), getSaveCommand(), getUndoCommand(), getDeleteCommand() };
        }
    }

    /**
     * @return Returns the deleteCommand.
     */
    public AbstractCommand getDeleteCommand() {
        if (deleteCommand == null) {
            deleteCommand = new DeleteCommand();
        }

        return deleteCommand;
    }

    /**
     *
     * @return lock aquisito tramite il lockcommand
     */
    public ILock getLock() {
        return lock;
    }

    /**
     * @return Returns the lockCommand.
     */
    public ActionCommand getLockCommand() {
        if (lockCommand == null) {
            lockCommand = new LockCommand();
        }
        return lockCommand;
    }

    /**
     * @return Returns the newCommand.
     */
    public AbstractCommand getNewCommand() {
        if (newCommand == null) {
            newCommand = new NewCommand();
        }
        return newCommand;
    }

    /**
     * @return Returns the pageEditor.
     */
    public IPageEditor getPageEditor() {
        return formPageEditor;
    }

    /**
     * @return Returns the saveCommand.
     */
    public SaveCommand getSaveCommand() {
        if (saveCommand == null) {
            saveCommand = new SaveCommand();
        }
        return saveCommand;
    }

    /**
     * @return Returns the undoCommand.
     */
    public AbstractCommand getUndoCommand() {
        if (undoCommand == null) {
            undoCommand = new UndoCommand();
        }
        return undoCommand;
    }

    /**
     * Metodo per il lock dell'oggetto scelto contenuto nel form model.
     */
    public void lock() {
        LOGGER.debug("--> Enter lock");
        Object lockObject = formPageEditor.getForm().getFormModel().getFormObject();
        // Se l'oggetto e' nuovo non lo blocco
        if (lockObject instanceof IDefProperty && ((IDefProperty) lockObject).isNew()) {
            return;
        }

        if (lock == null) {

            if (lockObject instanceof IDefProperty) {
                LOGGER.debug("--> lock di " + lockObject);
                ILockingBD lockBD = (ILockingBD) ApplicationServicesLocator.services().getService(ILockingBD.class);

                // viene lockato l'oggetto del form model
                lock = lockBD.lock(lockObject);

            }
            // abilito il form model per permettere la modifica
            formPageEditor.getForm().getFormModel().setReadOnly(false);

            // notifico all'utente che l'oggetto in modifica e' bloccato
            formPageEditor.setMessage(new DefaultMessage(LOCK_MESSAGE, Severity.INFO));
        }
        LOGGER.debug("--> Exit lock");
    }

    /**
     * Metodo per il release del lock adottato sull'oggetto scelto.
     *
     * @return true or false
     */
    public boolean releaseLock() {
        boolean released = true;
        if (lock != null) {
            ILockingBD lockBD = (ILockingBD) ApplicationServicesLocator.services().getService(ILockingBD.class);
            try {
                released = lockBD.release(lock);
                lock = null;
            } catch (LockNotFoundException e) {
                // se ci sono problemi nel rilascio (l'amministratore ha
                // rilasciato il lock)
                LOGGER.warn("---> Attenzione release del lock non riuscito. Lock che ho provato a rilasciare " + lock);
                // ripristino il form allo stato iniziale non abilitato
                (formPageEditor.getForm()).getFormModel().setReadOnly(true);
                formPageEditor.getForm().revert();

                // e devo ripristinare i commands allo stato iniziale
                getNewCommand().setEnabled(true);
                getLockCommand().setEnabled(true);
                getUndoCommand().setEnabled(false);

                lock = null;
                formPageEditor.setMessage(null);

                // rilancio l'exc per il messaggio al client
                throw new RuntimeException(e);
            }
        }
        (formPageEditor.getForm()).getFormModel().setReadOnly(true);
        // notifico all'utente che l'oggetto in modifica e' sbloccato
        formPageEditor.setMessage(new DefaultMessage(RELEASE_MESSAGE, Severity.INFO));
        return released;
    }

    /**
     *
     * @param deleteCommand
     *            deleteCommand da utilizzare al posto del comando standard
     */
    public void setDeleteCommand(AbstractCommand deleteCommand) {
        this.deleteCommand = deleteCommand;
    }

    /**
     * @param newCommand
     *            The newCommand to set.
     */
    public void setNewCommand(AbstractCommand newCommand) {
        this.newCommand = newCommand;
    }
}
