package it.eurotn.panjea.anagrafica.rich.factory.list;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;

/**
 * Cell renderer per combobox che visualizza nella il codice e la descrizione del tipo documento.
 */
public class TipoDocumentoCellRenderer extends DefaultListCellRenderer {

	private static final long serialVersionUID = 956056490164173003L;

	@Override
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
			boolean cellHasFocus) {
		JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		if (value instanceof TipoDocumento) {
			TipoDocumento td = (TipoDocumento) value;
			if (!td.getCodice().isEmpty()) {
				label.setText(td.getCodice() + " - " + td.getDescrizione());
			}
		}
		return label;
	}
}