package it.eurotn.panjea.contabilita.rich.editors.righecontabili.verificaCentriCosto;

import it.eurotn.panjea.contabilita.domain.RigaContabile;
import it.eurotn.panjea.contabilita.rich.bd.ContabilitaBD;
import it.eurotn.panjea.contabilita.rich.bd.IContabilitaBD;
import it.eurotn.panjea.contabilita.util.AreaContabileDTO;
import it.eurotn.rich.control.table.JecHierarchicalTable;
import it.eurotn.rich.control.table.JideTableWidget;
import it.eurotn.rich.control.table.JideTableWidget.TableType;

import java.awt.Component;
import java.util.List;

import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.grid.HierarchicalTable;
import com.jidesoft.grid.HierarchicalTableComponentFactory;
import com.jidesoft.grid.ListSelectionModelGroup;
import com.jidesoft.grid.TableModelWrapperUtils;
import com.jidesoft.grid.TreeLikeHierarchicalPanel;
import com.jidesoft.swing.JideSwingUtilities;

public class VerificaCentridiCostoRigheContabiliHierarchicalTableComponentFactory implements
HierarchicalTableComponentFactory {

	private ListSelectionModelGroup group = null;

	@Override
	public Component createChildComponent(HierarchicalTable paramHierarchicalTable, Object paramObject, int paramInt) {
		IContabilitaBD contabilitaBD = RcpSupport.getBean(ContabilitaBD.BEAN_ID);
		if (group == null) {
			group = new ListSelectionModelGroup();
		}
		group.add(paramHierarchicalTable.getSelectionModel());
		VerificaCentriDiCostoTableModel model = (VerificaCentriDiCostoTableModel) TableModelWrapperUtils
				.getActualTableModel(paramHierarchicalTable.getModel());
		AreaContabileDTO areaContabileDTO = model.getElementAt(paramInt);
		List<RigaContabile> righe = contabilitaBD.caricaRigheContabili(areaContabileDTO.getId());
		JideTableWidget<RigaContabile> table = new JideTableWidget<RigaContabile>("verificaCentriDiCostoPage",
				new RigheContabiliCentriDiCostoTableModel());
		table.setTableType(TableType.HIERARCHICAL);
		table.setHierarchicalTableComponentFactory(new CentriDiCostoHierarchicalTableComponentFactory(group));
		table.setRows(righe);

		group.add(table.getTable().getSelectionModel());
		return new TreeLikeHierarchicalPanel(new FitScrollPane(table.getTable()));
	}

	@Override
	public void destroyChildComponent(HierarchicalTable paramHierarchicalTable, Component paramComponent, int row) {
		HierarchicalTable tableRighe = (HierarchicalTable) JideSwingUtilities.findFirstComponentByClass(
				(TreeLikeHierarchicalPanel) paramComponent, JecHierarchicalTable.class);

		// collasso tutte el child così se ci sono modifiche vengono salvate
		tableRighe.collapseAllRows();
		group.remove(paramHierarchicalTable.getSelectionModel());

		RigheContabiliCentriDiCostoTableModel righeContabiliCentriDiCostoTableModel = (RigheContabiliCentriDiCostoTableModel) TableModelWrapperUtils
				.getActualTableModel(tableRighe.getModel());

		VerificaCentriDiCostoTableModel areaContabileTableModel = (VerificaCentriDiCostoTableModel) TableModelWrapperUtils
				.getActualTableModel(paramHierarchicalTable.getModel());

		if (righeContabiliCentriDiCostoTableModel.isModificato()) {
			IContabilitaBD contabilitaBD = RcpSupport.getBean(ContabilitaBD.BEAN_ID);
			AreaContabileDTO elementSelected = areaContabileTableModel.getElementAt(row);
			contabilitaBD.validaAreaContabile(elementSelected.getId());
		}
	}
}
