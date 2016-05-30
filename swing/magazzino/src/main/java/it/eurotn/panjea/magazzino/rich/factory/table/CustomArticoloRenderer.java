package it.eurotn.panjea.magazzino.rich.factory.table;

import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.rich.factory.table.AbstractCustomTableCellRenderer;

import javax.swing.SwingConstants;

import com.jidesoft.grid.EditorContext;

public class CustomArticoloRenderer extends AbstractCustomTableCellRenderer {

	public static final EditorContext CONTEXT = new EditorContext("articolo");

	private static final long serialVersionUID = -3449678962743357013L;

	public static final String KEY_ICON_ARTICOLO_DISTINTA = "disinta.icon";
	public static final String KEY_ICON_ARTICOLO_PADRE = "rigaArticoloPage.tab.rigaArticoloPage.padre.icon";

	/**
	 * Costruttore.
	 *
	 */
	public CustomArticoloRenderer() {
		super(SwingConstants.LEFT);
	}

	@Override
	public String getIconKey(Object value, boolean isSelected, boolean hasFocus) {
		boolean isDistinta = false;
		if (value instanceof ArticoloLite) {
			isDistinta = ((ArticoloLite) value).isDistinta();
		} else if (value instanceof Articolo) {
			isDistinta = ((Articolo) value).isDistinta();
		}
		if (isDistinta) {
			return KEY_ICON_ARTICOLO_DISTINTA;
		}
		return Articolo.class.getName();
	}

	@Override
	public String getRendererText(Object value, boolean isSelected, boolean hasFocus) {

		String descrizione = "";

		if (value != null) {
			if (value.getClass().getName().equals(Articolo.class.getName())) {
				Articolo articolo = (Articolo) value;
				descrizione = articolo.getCodice() + " - " + articolo.getDescrizione();
			} else {
				if (value.getClass().getName().equals(ArticoloLite.class.getName())) {
					ArticoloLite articolo = (ArticoloLite) value;
					descrizione = articolo.getCodice() + " - " + articolo.getDescrizione();
				} else {
					if (value.getClass().getName().equals(String.class.getName())) {
						descrizione = (String) value;
					}
				}
			}
		}

		return descrizione;
	}

}
