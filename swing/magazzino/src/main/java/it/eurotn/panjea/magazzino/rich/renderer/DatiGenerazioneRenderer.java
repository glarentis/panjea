package it.eurotn.panjea.magazzino.rich.renderer;

import it.eurotn.panjea.magazzino.domain.DatiGenerazione;
import it.eurotn.panjea.magazzino.domain.DatiGenerazione.TipoGenerazione;
import it.eurotn.rich.control.table.renderer.IconContextSensitiveCellRenderer;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;

public class DatiGenerazioneRenderer extends IconContextSensitiveCellRenderer {

	private static final long serialVersionUID = -1872327209319672288L;

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {

		JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

		label.setIcon(null);
		if (value instanceof DatiGenerazione && ((DatiGenerazione) value).getTipoGenerazione() != null) {

			TipoGenerazione tipoGenerazione = ((DatiGenerazione) value).getTipoGenerazione();
			label.setIcon(getIconSource().getIcon(tipoGenerazione.getClass().getName() + "#" + tipoGenerazione.name()));
		}

		return label;
	}

}
