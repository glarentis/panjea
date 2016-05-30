package it.eurotn.panjea.ordini.rich.forms.righeordine.componenti;

import java.awt.BorderLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.springframework.richclient.dialog.AbstractDialogPage;
import org.springframework.richclient.settings.Settings;

import com.jidesoft.grid.TableUtils;
import com.jidesoft.grid.TreeTable;

import it.eurotn.panjea.magazzino.domain.IRigaArticoloDocumento;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;

public class RigheArticoliComponentiDistintaTreeTablePage extends AbstractDialogPage
        implements IPageLifecycleAdvisor, Observer {

    public static final String PAGE_ID = "righeArticoliComponentiDistintaTreeTablePage";
    private IRigaArticoloDocumento rigaArticoloDocumento = null;
    private JPanel rootPanel;
    private TreeTable table;

    /**
     * Costruttore.
     */
    protected RigheArticoliComponentiDistintaTreeTablePage() {
        super(PAGE_ID);
        // getObjectConfigurer().configure(this, "righeArticoliComponentiTablePage");
        // ((JideTable) getTable().getTable()).addValidator(new Validator() {
        //
        // @Override
        // public ValidationResult validating(ValidationObject validationObject) {
        // TableValidationObject tableValidationObject = (TableValidationObject) validationObject;
        // JTable table = (JTable) validationObject.getSource();
        //
        // ValidationResult result = ValidationResult.OK;
        //
        // if (table.getColumnClass(tableValidationObject.getColumn()).equals(Date.class)) {
        //
        // Date data = (Date) validationObject.getNewValue();
        //
        // if (data == null) {
        // result = new ValidationResult(1, false, ValidationResult.FAIL_BEHAVIOR_PERSIST,
        // "Inserire una data valida.");
        // }
        //
        // }
        // return result;
        // }
        // });
    }

    @Override
    protected JComponent createControl() {
        if (rootPanel == null) {
            rootPanel = new JPanel(new BorderLayout());
            table = new TreeTable();
            table.expandAll();
            table.setRowHeight(25);
            table.sortColumn(0);
            rootPanel.add(new JScrollPane(table), BorderLayout.CENTER);
        }
        return rootPanel;
    }

    @Override
    public void dispose() {

    }

    /**
     * @return the rigaArticoloDocumento to get
     */
    public IRigaArticoloDocumento getRigaArticoloDocumento() {
        return rigaArticoloDocumento;
    }

    @Override
    public void loadData() {

    }

    @Override
    public void onPostPageOpen() {

    }

    @Override
    public boolean onPrePageOpen() {

        return false;
    }

    @Override
    public void postSetFormObject(Object object) {

    }

    @Override
    public void preSetFormObject(Object object) {

    }

    @Override
    public void refreshData() {

    }

    @Override
    public void restoreState(Settings arg0) {

    }

    @Override
    public void saveState(Settings arg0) {

    }

    @Override
    public void setFormObject(Object object) {
        rigaArticoloDocumento = (IRigaArticoloDocumento) object;
        RigheArticoliComponentiDistintaTreeTableModel righeArticoliComponentiDistintaTreeTableModel = new RigheArticoliComponentiDistintaTreeTableModel(
                rigaArticoloDocumento.getComponenti());
        table.setModel(righeArticoliComponentiDistintaTreeTableModel);
        table.expandAll();
        TableUtils.autoResizeAllColumns(table);
        // List<RigaArticolo> righeArticolo = new ArrayList<RigaArticolo>();
        // if (rigaArticoloDocumento != null && rigaArticoloDocumento.getComponenti() != null) {
        // Set<IRigaArticoloDocumento> righe = rigaArticoloDocumento.getComponenti();
        // for (IRigaArticoloDocumento rigaArticolo : righe) {
        // righeArticolo.add((RigaArticolo) rigaArticolo);
        // }
        // }
    }

    @Override
    public void setReadOnly(boolean readOnly) {

    }

    @Override
    public void update(Observable o, Object arg) {
    }

}
