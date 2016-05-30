package it.eurotn.panjea.auvend.rich.editors.importazioni;

import it.eurotn.panjea.auvend.domain.Documento;
import it.eurotn.panjea.rich.factory.table.AbstractCustomTableCellRenderer;

import java.util.List;

import javax.swing.SwingConstants;

public class MovimentiDaAggiornareCellRenderer extends AbstractCustomTableCellRenderer {

	private static final long serialVersionUID = -5614247835229762003L;

	/**
	 * Costruttore.
	 * 
	 */
	public MovimentiDaAggiornareCellRenderer() {
		super(SwingConstants.CENTER);
	}

	@Override
	public String getIconKey(Object value, boolean isSelected, boolean hasFocus) {

		if (value == null) {
			return null;
		} else {
			@SuppressWarnings("unchecked")
			List<Documento> list = (List<Documento>) value;

			if (list.isEmpty()) {
				return "ok.icon";
			} else {
				return "attention.icon";
			}
		}
	}

	@Override
	public String getRendererText(Object value, boolean isSelected, boolean hasFocus) {

		return "";
	}

}
