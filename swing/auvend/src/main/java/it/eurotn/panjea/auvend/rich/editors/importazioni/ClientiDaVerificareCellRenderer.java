package it.eurotn.panjea.auvend.rich.editors.importazioni;

import it.eurotn.panjea.auvend.domain.Cliente;
import it.eurotn.panjea.rich.factory.table.AbstractCustomTableCellRenderer;

import java.util.List;

import javax.swing.SwingConstants;

public class ClientiDaVerificareCellRenderer extends AbstractCustomTableCellRenderer {

	private static final long serialVersionUID = -4755708872385071079L;

	/**
	 * Costruttore.
	 * 
	 */
	public ClientiDaVerificareCellRenderer() {
		super(SwingConstants.CENTER);
	}

	@Override
	public String getIconKey(Object value, boolean isSelected, boolean hasFocus) {
		if (value == null) {
			return null;
		} else {
			@SuppressWarnings("unchecked")
			List<Cliente> list = (List<Cliente>) value;

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
