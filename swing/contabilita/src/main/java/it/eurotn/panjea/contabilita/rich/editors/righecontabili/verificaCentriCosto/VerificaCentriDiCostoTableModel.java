package it.eurotn.panjea.contabilita.rich.editors.righecontabili.verificaCentriCosto;

import it.eurotn.panjea.contabilita.util.AreaContabileDTO;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

import com.jidesoft.grid.HierarchicalTableModel;

public class VerificaCentriDiCostoTableModel extends DefaultBeanTableModel<AreaContabileDTO> implements
		HierarchicalTableModel {
	/**
	 * Costruttore.
	 */
	public VerificaCentriDiCostoTableModel() {
		super("verificaCentriDiCostoTableModel", new String[] { "dataDocumento", "dataRegistrazione",
				"numeroDocumento", "numeroProtocollo", "tipoDocumento", "entitaDocumento", "totale" },
				AreaContabileDTO.class);
	}

	@Override
	public Object getChildValueAt(int paramInt) {
		return null;
	}

	@Override
	public boolean hasChild(int paramInt) {
		return true;
	}

	@Override
	public boolean isExpandable(int paramInt) {
		return true;
	}

	@Override
	public boolean isHierarchical(int paramInt) {
		return true;
	}

}
