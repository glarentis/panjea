package it.eurotn.panjea.magazzino.rich.forms.rigamagazzino;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.springframework.binding.form.FormModel;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

import it.eurotn.locking.IDefProperty;
import it.eurotn.panjea.anagrafica.documenti.domain.IAreaDocumento;
import it.eurotn.panjea.magazzino.domain.RigaMagazzino;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoDocumentoBD;
import it.eurotn.panjea.magazzino.rich.bd.MagazzinoDocumentoBD;
import it.eurotn.panjea.magazzino.util.RigaDestinazione;
import it.eurotn.rich.control.table.JideTableWidget;
import it.eurotn.rich.control.table.JideTableWidget.TableType;
import it.eurotn.rich.form.PanjeaAbstractForm;

public class RigheCollegateForm extends PanjeaAbstractForm {

    private class OpenAreaCollegataCommand extends ApplicationWindowAwareCommand {

        @Override
        protected void doExecuteCommand() {
            if (righeCollegateTableWidget.getSelectedObject() != null) {
                IAreaDocumento areaDocumento = righeCollegateTableWidget.getSelectedObject().getAreaDocumento();
                LifecycleApplicationEvent event = new OpenEditorEvent(areaDocumento);
                Application.instance().getApplicationContext().publishEvent(event);
            }
        }

    }

    public static final String FORM_ID = "righeCollegateForm";

    protected JideTableWidget<RigaDestinazione> righeCollegateTableWidget;

    private final IMagazzinoDocumentoBD magazzinoDocumentoBD;

    /**
     * Costruttore.
     *
     * @param formModel
     *            formmodel da utilizzare
     *
     */
    public RigheCollegateForm(final FormModel formModel) {
        super(formModel, FORM_ID);
        this.magazzinoDocumentoBD = RcpSupport.getBean(MagazzinoDocumentoBD.BEAN_ID);
    }

    @Override
    protected JComponent createFormControl() {

        righeCollegateTableWidget = new JideTableWidget<RigaDestinazione>("righeCollegateTableWidget",
                new RigheCollegateTableModel());
        righeCollegateTableWidget.setTableType(TableType.AGGREGATE);
        righeCollegateTableWidget.setPropertyCommandExecutor(new OpenAreaCollegataCommand());

        JPanel rootPanel = getComponentFactory().createPanel(new BorderLayout());
        rootPanel.add(righeCollegateTableWidget.getComponent(), BorderLayout.CENTER);

        addFormObjectChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                refreshData();
            }
        });

        return rootPanel;
    }

    /**
     * Azzera le righe nella tabella
     */
    public void refreshData() {
        reloadData();
    }

    /**
     * Carica le righe collegate
     */
    public void reloadData() {
        if (((IDefProperty) getFormObject()).isNew()) {
            righeCollegateTableWidget.setRows(new ArrayList<RigaDestinazione>());
            return;
        }

        try {
            righeCollegateTableWidget.getOverlayTable().startSearch();
            List<RigaDestinazione> righe = magazzinoDocumentoBD
                    .caricaRigheMagazzinoCollegate((RigaMagazzino) getFormObject());
            righeCollegateTableWidget.setRows(righe);
        } finally {
            righeCollegateTableWidget.getOverlayTable().stopSearch();
        }
    }

    @Override
    public void setFormObject(Object formObject) {
        super.setFormObject(formObject);
        refreshData();
    }
}
