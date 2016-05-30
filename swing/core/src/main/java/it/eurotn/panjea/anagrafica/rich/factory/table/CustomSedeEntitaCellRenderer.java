package it.eurotn.panjea.anagrafica.rich.factory.table;

import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.lite.SedeEntitaLite;
import it.eurotn.panjea.rich.factory.table.AbstractCustomTableCellRenderer;

import javax.swing.SwingConstants;

import com.jidesoft.grid.EditorContext;

public class CustomSedeEntitaCellRenderer extends AbstractCustomTableCellRenderer {

	public static final EditorContext CONTEXT = new EditorContext("sedeEntita");

	private static final long serialVersionUID = -1999005858279820552L;

	/**
	 * Costruttore.
	 * 
	 */
	public CustomSedeEntitaCellRenderer() {
		super(SwingConstants.LEFT);
	}

	@Override
	public String getIconKey(Object value, boolean isSelected, boolean hasFocus) {
		return SedeEntita.class.getName();
	}

	@Override
	public String getRendererText(Object value, boolean isSelected, boolean hasFocus) {

		String descrizione = "";

		if (value != null) {
			if (value.getClass().getName().equals(SedeEntita.class.getName())) {
				SedeEntita sedeEntita = (SedeEntita) value;
				descrizione = sedeEntita.getSede().getDescrizione();
			} else if (value.getClass().getName().equals(SedeEntitaLite.class.getName())) {
				SedeEntitaLite sedeEntitaLite = (SedeEntitaLite) value;
				descrizione = sedeEntitaLite.getSede().getDescrizione();
			} else {
				if (value.getClass().getName().equals(String.class.getName())) {
					descrizione = (String) value;
				}
			}
		}

		return descrizione;
	}
}
