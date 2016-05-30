package it.eurotn.panjea.preventivi.rich.editors.righepreventivo;

import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.ActionCommandInterceptor;

import it.eurotn.panjea.preventivi.domain.interfaces.IRigaDocumento;
import it.eurotn.panjea.preventivi.util.interfaces.IRigaDTO;
import it.eurotn.rich.editors.AbstractTablePageEditor;
import it.eurotn.rich.editors.IPageEditor;
import it.eurotn.rich.editors.table.EEditPageMode;
import it.eurotn.rich.editors.table.EditFrame;

public class RigheDocumentoEditFrame<E extends IRigaDocumento, T extends IRigaDTO> extends EditFrame<T> {

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

    private static Logger logger = Logger.getLogger(RigheDocumentoEditFrame.class);

    private static final long serialVersionUID = 1L;

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
    public RigheDocumentoEditFrame(final EEditPageMode editView, final AbstractTablePageEditor<T> pageEditor,
            final String startQuickAction) {
        super(editView, pageEditor, startQuickAction);

        for (AbstractCommand command : getDetailNewCommands().values()) {
            ((ActionCommand) command).addCommandInterceptor(new NewCommandInterceptor());
        }

        for (Entry<String, IPageEditor> entry : pageEditor.getEditPages().entrySet()) {
            ((ActionCommand) entry.getValue().getEditorUndoCommand())
                    .addCommandInterceptor(new UndoDeleteCommandInterceptor());

            ((ActionCommand) entry.getValue().getEditorDeleteCommand())
                    .addCommandInterceptor(new UndoCommandInterceptor());
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public T getTableManagedObject(Object object) {
        logger.debug("--> Enter getTableManagedObject");
        E rigaPreventivo = (E) object;
        logger.debug("--> Exit getTableManagedObject");
        T result = (T) rigaPreventivo.creaRigaDTO();
        return result;
    }
}
