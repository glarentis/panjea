package it.eurotn.panjea.contabilita.rich.editors.righecontabili.verificaCentriCosto;

import it.eurotn.panjea.contabilita.domain.RigaCentroCosto;
import it.eurotn.panjea.contabilita.domain.RigaContabile;
import it.eurotn.panjea.contabilita.rich.bd.ContabilitaBD;
import it.eurotn.panjea.contabilita.rich.bd.IContabilitaBD;
import it.eurotn.rich.control.table.JecAggregateTable;
import it.eurotn.rich.control.table.JideTableWidget;

import java.awt.Component;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JTable;

import org.springframework.richclient.core.DefaultMessage;
import org.springframework.richclient.dialog.MessageDialog;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.grid.HierarchicalTable;
import com.jidesoft.grid.HierarchicalTableComponentFactory;
import com.jidesoft.grid.ListSelectionModelGroup;
import com.jidesoft.grid.SortableTableModel;
import com.jidesoft.grid.TableModelWrapperUtils;
import com.jidesoft.grid.TreeLikeHierarchicalPanel;
import com.jidesoft.swing.JideSwingUtilities;

public class CentriDiCostoHierarchicalTableComponentFactory implements HierarchicalTableComponentFactory {
	private ListSelectionModelGroup group;

	/**
	 * Costruttore.
	 *
	 * @param group
	 *            group per unire il selection model
	 */
	public CentriDiCostoHierarchicalTableComponentFactory(final ListSelectionModelGroup group) {
		super();
		this.group = group;
	}

	@Override
	public Component createChildComponent(final HierarchicalTable paramHierarchicalTable, Object paramObject,
			final int row) {
		@SuppressWarnings("unchecked")
		Set<RigaCentroCosto> righeCentriCosto = (Set<RigaCentroCosto>) paramObject;

		final RigheContabiliCentriDiCostoTableModel righeContabiliTableModel = (RigheContabiliCentriDiCostoTableModel) TableModelWrapperUtils
				.getActualTableModel(paramHierarchicalTable.getModel());

		VerificaRigheCentroCostoTableModel centroDiCostoTableModel = new VerificaRigheCentroCostoTableModel(
				righeContabiliTableModel.getElementAt(row));

		final JideTableWidget<RigaCentroCosto> tableWidgetCentriDiCosti = new JideTableWidget<RigaCentroCosto>(
				"centriCostoTable", centroDiCostoTableModel);
		tableWidgetCentriDiCosti.setRows(righeCentriCosto);

		SortableTableModel sortableTableModel = (SortableTableModel) TableModelWrapperUtils.getActualTableModel(
				tableWidgetCentriDiCosti.getTable().getModel(), SortableTableModel.class);
		sortableTableModel.setSortable(false);

		group.add(tableWidgetCentriDiCosti.getTable().getSelectionModel());
		return new TreeLikeHierarchicalPanel(new FitScrollPane(tableWidgetCentriDiCosti.getTable()));
	}

	@Override
	public void destroyChildComponent(HierarchicalTable paramHierarchicalTable, Component paramComponent, int paramInt) {
		RigheContabiliCentriDiCostoTableModel righeContabiliTableModel = (RigheContabiliCentriDiCostoTableModel) TableModelWrapperUtils
				.getActualTableModel(paramHierarchicalTable.getModel());

		JTable tableCentriDiCosto = (JTable) JideSwingUtilities.findFirstComponentByClass(
				(TreeLikeHierarchicalPanel) paramComponent, JecAggregateTable.class);
		VerificaRigheCentroCostoTableModel centroDiCostoTableModel = (VerificaRigheCentroCostoTableModel) TableModelWrapperUtils
				.getActualTableModel(tableCentriDiCosto.getModel());
		group.remove(tableCentriDiCosto.getSelectionModel());

		// Salvo le modifiche
		if (centroDiCostoTableModel.isModificato()) {
			IContabilitaBD contabilitaBD = RcpSupport.getBean(ContabilitaBD.BEAN_ID);
			HashSet<RigaCentroCosto> centriDiCosto = new HashSet<RigaCentroCosto>(centroDiCostoTableModel.getObjects());
			boolean righeValide = true;
			for (RigaCentroCosto rigaCentroCosto : centriDiCosto) {
				if (rigaCentroCosto.getCentroCosto() == null || rigaCentroCosto.getImporto() == null) {
					MessageDialog dialog = new MessageDialog("Riga non valida", new DefaultMessage(
							"Impossibile salvare la riga,verificare i centri di costo"));
					dialog.showDialog();
					righeValide = false;
					break;
				}
			}

			if (righeValide) {
				RigaContabile rigaContabile = righeContabiliTableModel.getElementAt(paramInt);
				rigaContabile.setRigheCentroCosto(centriDiCosto);
				rigaContabile = contabilitaBD.salvaRigaContabile(rigaContabile);
				righeContabiliTableModel.setObject(rigaContabile);
			}
		}
	}
}
