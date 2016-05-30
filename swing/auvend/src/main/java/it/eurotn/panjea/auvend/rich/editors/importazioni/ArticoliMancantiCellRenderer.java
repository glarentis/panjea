package it.eurotn.panjea.auvend.rich.editors.importazioni;

import it.eurotn.panjea.auvend.domain.Articolo;
import it.eurotn.panjea.rich.factory.table.AbstractCustomTableCellRenderer;

import java.util.List;

import javax.swing.SwingConstants;

public class ArticoliMancantiCellRenderer extends AbstractCustomTableCellRenderer {

	private static final long serialVersionUID = -8770518925194408519L;

	/**
	 * Costruttore.
	 * 
	 */
	public ArticoliMancantiCellRenderer() {
		super(SwingConstants.CENTER);
	}

	@Override
	public String getIconKey(Object value, boolean isSelected, boolean hasFocus) {

		if (value == null) {
			return null;
		} else {
			@SuppressWarnings("unchecked")
			List<Articolo> list = (List<Articolo>) value;

			if (list.isEmpty()) {
				return "ok.icon";
			} else {
				return "no.icon";
			}
		}
	}

	@Override
	public String getRendererText(Object value, boolean isSelected, boolean hasFocus) {

		return "";
	}

}
