package it.eurotn.panjea.tesoreria.rich.editors.areaTesoreria.bonifico;

import it.eurotn.panjea.tesoreria.domain.Pagamento;
import it.eurotn.rich.control.table.DefaultBeanEditableTableModel;

public class PagamentiBonificoFornitoreTableModel extends DefaultBeanEditableTableModel<Pagamento> {

	private static final long serialVersionUID = -6950659266093394303L;

	/**
	 * Costruttore.
	 */
	public PagamentiBonificoFornitoreTableModel() {
		super("lottoTableModel", new String[] { "rata.numeroRata", "rata.dataScadenza",
				"rata.areaRate.documento.entitaDocumento", "importo" }, Pagamento.class);
	}

	@Override
	protected boolean isAllowInsert() {
		return false;
	}

	@Override
	protected boolean isAllowRemove() {
		return true;
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}

}