package it.eurotn.panjea.contabilita.rich.factory.table;

import it.eurotn.panjea.contabilita.domain.SottoConto;
import it.eurotn.panjea.rich.factory.table.AbstractCustomTableCellRenderer;

import javax.swing.SwingConstants;

public class CustomSottoContoCellRenderer extends AbstractCustomTableCellRenderer {

	private static final long serialVersionUID = 6578316303621859286L;

	public CustomSottoContoCellRenderer() {
		super(SwingConstants.LEFT);
	}

	@Override
	public String getIconKey(Object value, boolean isSelected, boolean hasFocus) {
		return SottoConto.class.getName();
	}

	@Override
	public String getRendererText(Object value, boolean isSelected, boolean hasFocus) {

		String rendererText = "";

		if (value != null && !value.toString().isEmpty()) {
			SottoConto sottoConto = (SottoConto) value;

			rendererText = sottoConto.getSottoContoCodice() + " - " + sottoConto.getDescrizione();
		}
		return rendererText;
	}

}
