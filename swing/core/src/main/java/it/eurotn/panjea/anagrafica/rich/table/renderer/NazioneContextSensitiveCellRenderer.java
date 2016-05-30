/**
 *
 */
package it.eurotn.panjea.anagrafica.rich.table.renderer;

import it.eurotn.panjea.anagrafica.domain.datigeografici.Nazione;
import it.eurotn.rich.control.table.renderer.IconContextSensitiveCellRenderer;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTable;

/**
 * @author fattazzo
 * 
 */
public class NazioneContextSensitiveCellRenderer extends IconContextSensitiveCellRenderer {

	private static final long serialVersionUID = -1096088802724307450L;

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		Icon icon = null;
		try {
			Nazione nazione = (Nazione) value;
			if (nazione != null && !nazione.isNew()) {
				icon = getIconSource().getIcon(nazione.getCodice().toLowerCase());
			}
		} catch (Exception e) {
			icon = null;
		}
		label.setIcon(icon);
		return label;
	}
}
