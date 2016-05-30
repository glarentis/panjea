package it.eurotn.panjea.anagrafica.rich.factory.table;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.rich.factory.table.AbstractCustomTableCellRenderer;

import javax.swing.SwingConstants;

import com.jidesoft.grid.EditorContext;

public class CustomTipoDocumentoCellRenderer extends AbstractCustomTableCellRenderer {

	private static final long serialVersionUID = -1999005858279820552L;

	public static final EditorContext CONTEXT = new EditorContext("tipoDocumento");

	/**
	 * Costruttore.
	 * 
	 */
	public CustomTipoDocumentoCellRenderer() {
		super(SwingConstants.LEFT);
	}

	@Override
	public String getIconKey(Object value, boolean isSelected, boolean hasFocus) {
		return TipoDocumento.class.getName();
	}

	@Override
	public String getRendererText(Object value, boolean isSelected, boolean hasFocus) {

		String descrizione = "";

		if (value != null && value.getClass().getName().equals(TipoDocumento.class.getName())) {
			TipoDocumento tipoDocumento = (TipoDocumento) value;
			descrizione = tipoDocumento.getCodice() + " - " + tipoDocumento.getDescrizione();
		} else {
			if (value != null && value.getClass().getName().equals(String.class.getName())) {
				descrizione = (String) value;
			}
		}

		return descrizione;
	}
}
