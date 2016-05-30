package it.eurotn.panjea.ordini.rich.editors.righeordine;

import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.ActionCommandInterceptor;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.ordini.domain.RigaOrdine;
import it.eurotn.panjea.ordini.rich.bd.IOrdiniDocumentoBD;
import it.eurotn.panjea.ordini.util.RigaOrdineDTO;
import it.eurotn.rich.editors.AbstractTablePageEditor;
import it.eurotn.rich.editors.IPageEditor;
import it.eurotn.rich.editors.table.EEditPageMode;
import it.eurotn.rich.editors.table.EditFrame;

public class RigheOrdineEditFrame extends EditFrame<RigaOrdineDTO> {
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

    private static Logger logger = Logger.getLogger(RigheOrdineEditFrame.class);

    private static final long serialVersionUID = 7522587816392725939L;

    private IOrdiniDocumentoBD ordiniDocumentoBD;

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
    public RigheOrdineEditFrame(final EEditPageMode editView, final AbstractTablePageEditor<RigaOrdineDTO> pageEditor,
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

    /**
     * @return the magazzinoDocumentoBD
     */
    public IOrdiniDocumentoBD getOrdiniDocumentoBD() {
        if (ordiniDocumentoBD == null) {
            ordiniDocumentoBD = RcpSupport.getBean("ordiniDocumentoBD");
        }

        return ordiniDocumentoBD;
    }

    @Override
    public RigaOrdineDTO getTableManagedObject(Object object) {
        logger.debug("--> Enter getTableManagedObject");
        RigaOrdine rigaOrdine = (RigaOrdine) object;
        logger.debug("--> Exit getTableManagedObject");
        RigaOrdineDTO result = rigaOrdine.creaRigaOrdineDTO();
        return result;
    }

}
