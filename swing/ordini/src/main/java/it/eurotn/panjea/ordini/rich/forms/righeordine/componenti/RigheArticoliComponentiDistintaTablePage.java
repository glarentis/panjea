package it.eurotn.panjea.ordini.rich.forms.righeordine.componenti;

import it.eurotn.panjea.magazzino.domain.IRigaArticoloDocumento;
import it.eurotn.panjea.ordini.domain.RigaArticolo;
import it.eurotn.rich.control.table.JideTableWidget;
import it.eurotn.rich.control.table.JideTableWidget.TableType;
import it.eurotn.rich.editors.AbstractTablePageEditor;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Observer;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;

import org.jdesktop.swingx.icon.EmptyIcon;

import com.jidesoft.grid.HierarchicalTable;
import com.jidesoft.grid.HierarchicalTableComponentFactory;
import com.jidesoft.grid.JideTable;
import com.jidesoft.validation.TableValidationObject;
import com.jidesoft.validation.ValidationObject;
import com.jidesoft.validation.ValidationResult;
import com.jidesoft.validation.Validator;

public class RigheArticoliComponentiDistintaTablePage extends AbstractTablePageEditor<RigaArticolo> implements Observer {

	private class ComponentiHierarchicalTableComponentFactory implements HierarchicalTableComponentFactory {

		private JideTableWidget<RigaArticolo> tableWidget;

		@Override
		public Component createChildComponent(HierarchicalTable arg0, Object arg1, int arg2) {
			tableWidget = new JideTableWidget<RigaArticolo>("tableComponentiWidget",
					new RigheArticoliComponentiDistintaTableModel());
			tableWidget.setTableType(TableType.HIERARCHICAL);
			@SuppressWarnings("unchecked")
			Set<IRigaArticoloDocumento> componenti = (Set<IRigaArticoloDocumento>) arg1;
			List<RigaArticolo> righeArticolo = new ArrayList<RigaArticolo>();
			if (componenti != null) {
				for (IRigaArticoloDocumento rigaArticolo : componenti) {
					righeArticolo.add((RigaArticolo) rigaArticolo);
				}
			}
			tableWidget.setHierarchicalTableComponentFactory(new ComponentiHierarchicalTableComponentFactory());
			tableWidget.setRows(righeArticolo);
			((HierarchicalTable) tableWidget.getTable()).setSingleExpansion(false);
			JPanel panel = new JPanel(new BorderLayout());
			panel.add(new JLabel(new EmptyIcon(16, 16)), BorderLayout.WEST);
			panel.add(tableWidget.getTable(), BorderLayout.CENTER);
			((HierarchicalTable) tableWidget.getTable()).expandAllRows();
			// tableWidget.getTable().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.DARK_GRAY));
			// panel.setBorder(BorderFactory.createLoweredSoftBevelBorder());
			return panel;
		}

		@Override
		public void destroyChildComponent(HierarchicalTable arg0, Component arg1, int arg2) {
			tableWidget.dispose();
		}
	}

	public static final String PAGE_ID = "righeArticoliOrdiniComponentiDistintaTablePage";
	private IRigaArticoloDocumento rigaArticoloDocumento = null;

	/**
	 * Costruttore.
	 */
	protected RigheArticoliComponentiDistintaTablePage() {
		super(PAGE_ID, new RigheArticoliComponentiDistintaTableModel());
		getTable().setTableType(TableType.HIERARCHICAL);
		getTable().setHierarchicalTableComponentFactory(new ComponentiHierarchicalTableComponentFactory());
		((HierarchicalTable) getTable().getTable()).setSingleExpansion(false);
		((HierarchicalTable) getTable().getTable()).expandAllRows();

		((JideTable) getTable().getTable()).addValidator(new Validator() {

			@Override
			public ValidationResult validating(ValidationObject validationObject) {
				TableValidationObject tableValidationObject = (TableValidationObject) validationObject;
				JTable table = (JTable) validationObject.getSource();

				ValidationResult result = ValidationResult.OK;

				if (table.getColumnClass(tableValidationObject.getColumn()).equals(Date.class)) {

					Date data = (Date) validationObject.getNewValue();

					if (data == null) {
						result = new ValidationResult(1, false, ValidationResult.FAIL_BEHAVIOR_PERSIST,
								"Inserire una data valida.");
					}

				}
				return result;
			}
		});
	}

	@Override
	public JComponent createToolbar() {
		return null;
	}

	@Override
	public Object getManagedObject(Object pageObject) {
		return rigaArticoloDocumento;
	}

	/**
	 * @return the rigaArticoloDocumento to get
	 */
	public IRigaArticoloDocumento getRigaArticoloDocumento() {
		return rigaArticoloDocumento;
	}

	@Override
	public Collection<RigaArticolo> loadTableData() {
		List<RigaArticolo> righeArticolo = new ArrayList<RigaArticolo>();
		if (rigaArticoloDocumento != null && rigaArticoloDocumento.getComponenti() != null) {
			Set<IRigaArticoloDocumento> righe = rigaArticoloDocumento.getComponenti();
			for (IRigaArticoloDocumento rigaArticolo : righe) {
				righeArticolo.add((RigaArticolo) rigaArticolo);
			}
		}
		return righeArticolo;
	}

	@Override
	public void onPostPageOpen() {

	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	@Override
	public Collection<RigaArticolo> refreshTableData() {
		return loadTableData();
	}

	@Override
	public void setFormObject(Object object) {
		rigaArticoloDocumento = (IRigaArticoloDocumento) object;
		getObjectConfigurer().configure(this, "righeArticoliComponentiTablePage");
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		super.setReadOnly(readOnly);
	}

}
