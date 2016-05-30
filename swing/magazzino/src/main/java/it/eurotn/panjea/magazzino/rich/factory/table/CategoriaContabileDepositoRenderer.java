package it.eurotn.panjea.magazzino.rich.factory.table;

import it.eurotn.panjea.magazzino.domain.CategoriaContabileDeposito;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

public class CategoriaContabileDepositoRenderer extends DefaultTableCellRenderer {
	private static final long serialVersionUID = 157896810319506129L;

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

		// verifico se il codice pagamento che devo renderizzare in tabella e' avvalorato
		boolean categoriaContabileDepositoPresente = (value != null && value instanceof CategoriaContabileDeposito);

		if (categoriaContabileDepositoPresente) {
			setHorizontalAlignment(SwingConstants.LEFT);
			CategoriaContabileDeposito categoriaContabileDeposito = (CategoriaContabileDeposito) value;
			// Icon entityIcon = getIconSource().getIcon(value.getClass().getName());
			// setIcon(entityIcon);
			String descrizione = categoriaContabileDeposito.getCodice();
			setValue(descrizione);
		}

		return cell;
	}
}
