package it.eurotn.panjea.magazzino.rich.editors.righemagazzino;

import java.util.Map.Entry;

import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.ActionCommandInterceptor;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.magazzino.domain.TipoAttributo;
import it.eurotn.panjea.magazzino.domain.TipoVariante;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.rich.editors.AbstractTablePageEditor;
import it.eurotn.rich.editors.IPageEditor;
import it.eurotn.rich.editors.table.EEditPageMode;
import it.eurotn.rich.editors.table.EditFrame;

public class TipoAttributoEditFrame extends EditFrame<TipoAttributo> {

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

    /**
     * 
     */
    private static final long serialVersionUID = -4313869201183273961L;

    private IMagazzinoAnagraficaBD magazzinoAnagraficaBD;

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
    public TipoAttributoEditFrame(final EEditPageMode editView, final AbstractTablePageEditor<TipoAttributo> pageEditor,
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
    public IMagazzinoAnagraficaBD getmagazzinoAnagraficaBD() {
        if (magazzinoAnagraficaBD == null) {
            magazzinoAnagraficaBD = RcpSupport.getBean("magazzinoAnagraficaBD");

        }

        return magazzinoAnagraficaBD;
    }

    @Override
    public TipoAttributo getTableManagedObject(Object object) {

        TipoAttributo tipoAttributo = new TipoAttributo();

        if (object instanceof TipoVariante) {

            if (((TipoVariante) object).getId() != null) {
                tipoAttributo = getmagazzinoAnagraficaBD().caricaTipoVariante((TipoVariante) object);
                // se la riga è = a null vuol dire che l'ho cancellata
                // quindi ne creo una solamente
                // con l'id settato per fare in modo che la tabella si
                // cancelli la riga
                if (tipoAttributo == null) {
                    tipoAttributo = new TipoAttributo();
                    tipoAttributo.setId(((TipoAttributo) object).getId());
                }
            } else {
                tipoAttributo = new TipoVariante();
            }

        } else {
            if (object instanceof TipoAttributo) {
                tipoAttributo = (TipoAttributo) object;
            }
        }

        return tipoAttributo;
    }
}
