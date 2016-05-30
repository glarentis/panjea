package it.eurotn.panjea.anagrafica.rich.factory.table;

import it.eurotn.panjea.anagrafica.domain.Deposito;
import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.rich.factory.table.AbstractCustomTableCellRenderer;

import javax.swing.SwingConstants;

public class CustomDepositoCellRenderer extends AbstractCustomTableCellRenderer {

	private static final long serialVersionUID = -6095416133851386196L;

	/**
	 * Costruttore.
	 * 
	 */
	public CustomDepositoCellRenderer() {
		super(SwingConstants.LEFT);
	}

	@Override
	public String getIconKey(Object value, boolean isSelected, boolean hasFocus) {
		if (value != null) {
			return Deposito.class.getName();
		} else {
			return "";
		}
	}

	@Override
	public String getRendererText(Object value, boolean isSelected, boolean hasFocus) {

		if (value != null) {

			if (value instanceof Deposito) {
				Deposito deposito = (Deposito) value;
				return deposito.getCodice() + " - " + deposito.getDescrizione();
			} else {
				DepositoLite deposito = (DepositoLite) value;
				return deposito.getCodice() + " - " + deposito.getDescrizione();
			}
		} else {
			return "";
		}
	}

}
