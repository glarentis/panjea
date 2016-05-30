package it.eurotn.panjea.magazzino.rich.factory.table;

import it.eurotn.panjea.magazzino.domain.CategoriaContabileArticolo;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

public class CategoriaContabileArticoloRenderer extends DefaultTableCellRenderer {
	private static final long serialVersionUID = 157896810319506129L;

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

		// verifico se il codice pagamento che devo renderizzare in tabella e' avvalorato
		boolean categoriaContabileArticoloPresente = (value != null && value instanceof CategoriaContabileArticolo);

		if (categoriaContabileArticoloPresente) {
			setHorizontalAlignment(SwingConstants.LEFT);
			CategoriaContabileArticolo categoriaContabileArticolo = (CategoriaContabileArticolo) value;
			String descrizione = categoriaContabileArticolo.getCodice();
			setValue(descrizione);
		}

		return cell;
	}
}