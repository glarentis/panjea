package it.eurotn.panjea.rich.editors.lock;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.dialog.ConfirmationDialog;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.locking.DefaultLock;
import it.eurotn.locking.ILock;
import it.eurotn.locking.exception.LockNotFoundException;
import it.eurotn.panjea.rich.bd.ILockingBD;
import it.eurotn.panjea.rich.lock.pm.LockPM;
import it.eurotn.rich.editors.AbstractTablePageEditor;

/**
 * @author Aracno
 * @version 1.0, 16/ott/06
 */
public class LockTablePage extends AbstractTablePageEditor<LockPM> {

    private class DeleteCommand extends ApplicationWindowAwareCommand {

        public static final String COMMAND_ID = "deleteCommand";

        /**
         * Costruttore.
         */
        public DeleteCommand() {
            super(COMMAND_ID);
            RcpSupport.configure(this);
        }

        @Override
        protected void doExecuteCommand() {
            if (getTable().getSelectedObject() != null) {
                ConfirmationDialog confirmationDialog = new ConfirmationDialog("Conferma cancellazione",
                        "Cancellare tutti i blocchi degli oggetti selezionati?") {

                    @Override
                    protected void onConfirm() {
                        List<LockPM> locks = getTable().getSelectedObjects();
                        for (LockPM lock : locks) {
                            try {
                                lockBD.release(lock.getLock());
                            } catch (LockNotFoundException e) {
                                LOGGER.error("-->errore nei rimuovere tutti i record bloccati", e);
                            }
                            getTable().removeRowObject(lock);
                        }
                    }
                };
                confirmationDialog.setPreferredSize(new Dimension(250, 50));
                confirmationDialog.setResizable(false);
                confirmationDialog.showDialog();
            }
        }
    }

    private static final Logger LOGGER = Logger.getLogger(LockTablePage.class);

    private static final String PAGE_ID = "lockTablePage";

    private ILockingBD lockBD;

    private DeleteCommand deleteCommand;

    /**
     * Costruttore.
     *
     */
    protected LockTablePage() {
        super(PAGE_ID, new String[] { "classLock", "lock.version", "lock.userName", "lock.timeStart" }, LockPM.class);
        lockBD = (ILockingBD) ApplicationServicesLocator.services().getService(ILockingBD.class);
    }

    @Override
    public AbstractCommand[] getCommands() {
        return new AbstractCommand[] { getDeleteCommand(), getRefreshCommand() };
    }

    /**
     * @return the deleteCommand
     */
    public DeleteCommand getDeleteCommand() {
        if (deleteCommand == null) {
            deleteCommand = new DeleteCommand();
        }

        return deleteCommand;
    }

    @Override
    public List<LockPM> loadTableData() {
        List<ILock> listLock = lockBD.getLockAll();
        List<LockPM> locks = new ArrayList<LockPM>();
        for (ILock lock : listLock) {
            LockPM lockPM = new LockPM((DefaultLock) lock);
            locks.add(lockPM);
        }
        return locks;
    }

    @Override
    public void onPostPageOpen() {
        LOGGER.debug("--> Enter onPostPageOpen");
    }

    @Override
    public List<LockPM> refreshTableData() {
        return loadTableData();
    }

    @Override
    public void setFormObject(Object object) {
        LOGGER.debug("--> Enter setFormObject");
    }
}
