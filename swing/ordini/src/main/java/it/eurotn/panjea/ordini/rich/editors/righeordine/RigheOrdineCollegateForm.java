package it.eurotn.panjea.ordini.rich.editors.righeordine;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.springframework.binding.form.FormModel;
import org.springframework.richclient.command.CommandGroup;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.locking.IDefProperty;
import it.eurotn.panjea.magazzino.util.RigaDestinazione;
import it.eurotn.panjea.ordini.domain.RigaOrdine;
import it.eurotn.panjea.ordini.rich.bd.IOrdiniDocumentoBD;
import it.eurotn.panjea.ordini.rich.bd.OrdiniDocumentoBD;
import it.eurotn.rich.command.AbstractDeleteCommand;

public class RigheOrdineCollegateForm extends it.eurotn.panjea.magazzino.rich.forms.rigamagazzino.RigheCollegateForm
        implements Observer {

    private class DeleteRigaCollegataCommand extends AbstractDeleteCommand {

        public DeleteRigaCollegataCommand() {
            super(DELETE_COMMAND);
        }

        @Override
        public Object onDelete() {
            int idRigaOrdine = ((RigaOrdine) getFormObject()).getId();
            int idRigaDestinazione = righeCollegateTableWidget.getSelectedObject().getIdRiga();
            ordiniDocumentoBD.cancellaRigheCollegate(idRigaOrdine, idRigaDestinazione);
            righeCollegateTableWidget.removeRowObject(righeCollegateTableWidget.getSelectedObject());
            return null;
        }

    }

    private IOrdiniDocumentoBD ordiniDocumentoBD;
    private DeleteRigaCollegataCommand deleteCommand;

    /**
     * Costruttore.
     *
     * @param formModel
     *            form model
     */
    public RigheOrdineCollegateForm(final FormModel formModel) {
        super(formModel);
        ordiniDocumentoBD = RcpSupport.getBean(OrdiniDocumentoBD.BEAN_ID);
    }

    @Override
    protected JComponent createFormControl() {
        deleteCommand = new DeleteRigaCollegataCommand();
        JPanel rootPanel = new JPanel(new BorderLayout());
        JComponent table = super.createFormControl();
        rootPanel.add(table, BorderLayout.CENTER);
        CommandGroup group = new CommandGroup();
        group.add(deleteCommand);
        rootPanel.add(group.createButtonStack(), BorderLayout.SOUTH);
        righeCollegateTableWidget.addSelectionObserver(this);
        return rootPanel;
    }

    @Override
    public void dispose() {
        super.dispose();
        righeCollegateTableWidget.removeSelectionObserver(this);
    }

    /**
     * Azzera le righe nella tabella
     */
    @Override
    public void refreshData() {
        reloadData();
    }

    /**
     * Carica le righe collegate
     */
    @Override
    public void reloadData() {
        if (((IDefProperty) getFormObject()).isNew()) {
            righeCollegateTableWidget.setRows(new ArrayList<RigaDestinazione>());
            return;
        }
        try {
            righeCollegateTableWidget.getOverlayTable().startSearch();
            righeCollegateTableWidget.setRows(ordiniDocumentoBD.caricaRigheCollegate((RigaOrdine) getFormObject()));
        } catch (Exception e) {
            logger.error("-->errore nel ricaricare i dati nelle righe collegate", e);
            throw new RuntimeException(e);
        } finally {
            righeCollegateTableWidget.getOverlayTable().stopSearch();
        }
    }

    @Override
    public void update(Observable obs, Object object) {
        if (righeCollegateTableWidget.getSelectedObject() != null) {
            deleteCommand.setEnabled(righeCollegateTableWidget.getSelectedObject().isCanDelete());
        }
    }
}
