package it.eurotn.panjea.magazzino.rich.editors.righemagazzino;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.ActionCommandInterceptor;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.magazzino.domain.RigaArticolo;
import it.eurotn.panjea.magazzino.domain.RigaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoDocumentoBD;
import it.eurotn.panjea.magazzino.util.RigaArticoloDTO;
import it.eurotn.panjea.magazzino.util.RigaMagazzinoDTO;
import it.eurotn.panjea.magazzino.util.RigaNotaDTO;
import it.eurotn.panjea.magazzino.util.RigaTestataDTO;
import it.eurotn.panjea.rich.PanjeaLifecycleApplicationEvent;
import it.eurotn.rich.command.AbstractDeleteCommand;
import it.eurotn.rich.editors.AbstractTablePageEditor;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;
import it.eurotn.rich.editors.IPageEditor;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;
import it.eurotn.rich.editors.table.EEditPageMode;
import it.eurotn.rich.editors.table.EditFrame;
import it.eurotn.rich.editors.table.EditFrameChangeSelectionConstraint;

public class RigheMagazzinoEditFrame extends EditFrame<RigaMagazzinoDTO> {

    private class DeleteCommand extends AbstractDeleteCommand {
        /**
         *
         * Costruttore.
         *
         */
        public DeleteCommand() {
            super("RIGHESECURITYCONTROLLER");
        }

        @Override
        public void addCommandInterceptor(ActionCommandInterceptor l) {
            // L'editFrame aggiunge listener al pulsante di default dell'editor.
            // Nel caso specifico non voglio nessun listener.
        }

        @Override
        public Object onDelete() {
            List<RigaMagazzinoDTO> righeSelezionate = tableWidget.getSelectedObjects();
            List<RigaMagazzino> righeDaCancellare = new ArrayList<RigaMagazzino>();
            for (RigaMagazzinoDTO rigaMagazzinoDTO : righeSelezionate) {
                righeDaCancellare.add(rigaMagazzinoDTO.getRigaMagazzino());
            }
            if (righeDaCancellare.size() == 0) {
                return null;
            }
            AreaMagazzino areaMagazzino = getMagazzinoDocumentoBD().cancellaRigheMagazzino(righeDaCancellare);
            tableWidget.removeRowsObject(righeSelezionate);
            for (RigaMagazzino rigaMagazzino : righeDaCancellare) {
                rigaMagazzino.setAreaMagazzino(areaMagazzino);
            }
            RigaMagazzino rigaMagazzino = righeDaCancellare.get(0);
            rigaMagazzino.setAreaMagazzino(areaMagazzino);

            PanjeaLifecycleApplicationEvent deleteEvent = new PanjeaLifecycleApplicationEvent(
                    LifecycleApplicationEvent.DELETED, rigaMagazzino, getCurrentEditPage());
            Application.instance().getApplicationContext().publishEvent(deleteEvent);

            // HACK come source object lancio -1 per distinguere la change object delle
            // cancellazione da quella di
            // update/insert
            if (getCurrentEditPage() instanceof FormBackedDialogPageEditor) {
                ((FormBackedDialogPageEditor) getCurrentEditPage())
                        .firePropertyChangeNew(IPageLifecycleAdvisor.OBJECT_CHANGED, new Integer(-1), rigaMagazzino);
            }

            if (tableWidget.isEmpty()) {
                ((FormBackedDialogPageEditor) pageEditor.getEditPages().get(RigaArticoloDTO.class.getName()))
                        .getNewCommand().execute();
            }

            return righeDaCancellare;
        }

    }

    private final class NewCommandInterceptor implements ActionCommandInterceptor {
        @Override
        public void postExecution(ActionCommand actioncommand) {
            getQuickInsertCommand().setSelected(true);
        }

        @Override
        public boolean preExecution(ActionCommand actioncommand) {
            return true;
        }
    }

    private final class SaveCommandInterceptor implements ActionCommandInterceptor {
        @Override
        public void postExecution(ActionCommand arg0) {
            if (getQuickInsertCommand().isSelected()) {
                setCurrentPage(getTableManagedObject(new RigaArticolo()));
            }
        }

        @Override
        public boolean preExecution(ActionCommand arg0) {
            return true;
        }
    }

    private final class UndoDeleteCommandInterceptor implements ActionCommandInterceptor {
        @Override
        public void postExecution(ActionCommand arg0) {
            getQuickDefaultCommand().setSelected(true);
        }

        @Override
        public boolean preExecution(ActionCommand arg0) {
            return true;
        }
    }

    private static Logger logger = Logger.getLogger(RigheMagazzinoEditFrame.class);

    private static final long serialVersionUID = 6209304295848835850L;

    private IMagazzinoDocumentoBD magazzinoDocumentoBD;

    /**
     * Costruttore.
     *
     * @param editView
     *            editView
     * @param pageEditor
     *            pageEditor
     * @param startQuickAction
     *            quick action
     */
    public RigheMagazzinoEditFrame(final EEditPageMode editView,
            final AbstractTablePageEditor<RigaMagazzinoDTO> pageEditor, final String startQuickAction) {
        super(editView, pageEditor, startQuickAction);

        for (AbstractCommand command : getDetailNewCommands().values()) {
            ((ActionCommand) command).addCommandInterceptor(new NewCommandInterceptor());
        }
    }

    @Override
    public EditFrameChangeSelectionConstraint createEditFrameChangeSelectionConstraint() {
        return new RigheMagazzinoEditFrameChangeSelectionConstraint();
    }

    /**
     * @return the magazzinoDocumentoBD
     */
    public IMagazzinoDocumentoBD getMagazzinoDocumentoBD() {
        if (magazzinoDocumentoBD == null) {
            magazzinoDocumentoBD = RcpSupport.getBean("magazzinoDocumentoBD");

        }

        return magazzinoDocumentoBD;
    }

    @Override
    public RigaMagazzinoDTO getTableManagedObject(Object object) {
        logger.debug("--> Enter getTableManagedObject");
        RigaMagazzino rigaMagazzino = (RigaMagazzino) object;
        logger.debug("--> Exit getTableManagedObject");
        RigaMagazzinoDTO result = rigaMagazzino.creaRigaMagazzinoDTO();
        return result;
    }

    @Override
    protected void init() {
        UndoDeleteCommandInterceptor undoDeleteCommandInterceptor = new UndoDeleteCommandInterceptor();

        AbstractCommand insertCommand = ((FormBackedDialogPageEditor) pageEditor.getEditPages()
                .get(RigaArticoloDTO.class.getName())).getNewCommand();

        DeleteCommand deleteCommand = new DeleteCommand();
        for (Entry<String, IPageEditor> entry : pageEditor.getEditPages().entrySet()) {
            ((FormBackedDialogPageEditor) entry.getValue()).setDeleteCommand(deleteCommand);
            ((ActionCommand) entry.getValue().getEditorUndoCommand())
                    .addCommandInterceptor(undoDeleteCommandInterceptor);

            if (!entry.getKey().equals(RigaNotaDTO.class.getName())
                    && !entry.getKey().equals(RigaArticoloDTO.class.getName())) {
                ((FormBackedDialogPageEditor) entry.getValue()).setNewCommand(insertCommand);
            }
        }

        SaveCommandInterceptor saveCommandInterceptor = new SaveCommandInterceptor();

        ActionCommand saveRitaTestataCommand = (ActionCommand) ((FormBackedDialogPageEditor) pageEditor.getEditPages()
                .get(RigaTestataDTO.class.getName())).getEditorSaveCommand();
        saveRitaTestataCommand.addCommandInterceptor(saveCommandInterceptor);

        ActionCommand saveRitaNotaCommand = (ActionCommand) ((FormBackedDialogPageEditor) pageEditor.getEditPages()
                .get(RigaNotaDTO.class.getName())).getEditorSaveCommand();
        saveRitaNotaCommand.addCommandInterceptor(saveCommandInterceptor);

        super.init();
    }
}
