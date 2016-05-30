package it.eurotn.panjea.contabilita.rich.editors.righecontabili.verificaCentriCosto;

import it.eurotn.panjea.contabilita.rich.bd.IContabilitaBD;
import it.eurotn.panjea.contabilita.util.AreaContabileDTO;
import it.eurotn.rich.control.table.JideTableWidget.TableType;
import it.eurotn.rich.editors.AbstractTablePageEditor;

import java.util.Collection;

import com.jidesoft.grid.HierarchicalTable;

public class VerificaCentriDiCostoPage extends AbstractTablePageEditor<AreaContabileDTO> {

	private IContabilitaBD contabilitaBD;

	/**
	 * Costruttore.
	 */
	public VerificaCentriDiCostoPage() {
		super("verificaCentriDiCostoPage", new VerificaCentriDiCostoTableModel());
		getTable().setTableType(TableType.HIERARCHICAL);
		getTable().setHierarchicalTableComponentFactory(
				new VerificaCentridiCostoRigheContabiliHierarchicalTableComponentFactory());
	}

	@Override
	public void dispose() {
		((HierarchicalTable) getTable().getTable()).collapseAllRows();
		super.dispose();
	}

	/**
	 * @return Returns the contabilitaBD.
	 */
	public IContabilitaBD getContabilitaBD() {
		return contabilitaBD;
	}

	@Override
	public Collection<AreaContabileDTO> loadTableData() {
		return contabilitaBD.verificaRigheSenzaCentriDiCosto();
	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public Collection<AreaContabileDTO> refreshTableData() {
		return null;
	}

	/**
	 * @param contabilitaBD
	 *            The contabilitaBD to set.
	 */
	public void setContabilitaBD(IContabilitaBD contabilitaBD) {
		this.contabilitaBD = contabilitaBD;
	}

	@Override
	public void setFormObject(Object object) {

	}

}
