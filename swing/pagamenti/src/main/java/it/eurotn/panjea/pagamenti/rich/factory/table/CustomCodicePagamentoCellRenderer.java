package it.eurotn.panjea.pagamenti.rich.factory.table;

import it.eurotn.locking.IDefProperty;
import it.eurotn.panjea.pagamenti.domain.CodicePagamento;
import it.eurotn.panjea.rich.factory.table.CustomTableCellRenderer;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JTable;
import javax.swing.SwingConstants;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.image.IconSource;

public class CustomCodicePagamentoCellRenderer extends CustomTableCellRenderer {

	private static final long serialVersionUID = 4338241774355389749L;

	private IconSource iconSource;

	@Override
	public IconSource getIconSource() {
		if (iconSource == null) {
			iconSource = (IconSource) Application.services().getService(IconSource.class);
		}

		return iconSource;
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

		// verifico se il codice pagamento che devo renderizzare in tabella e' avvalorato
		boolean codicePagPresente = value != null && value instanceof IDefProperty
				&& ((IDefProperty) value).getId() != null;

		if (codicePagPresente) {
			setHorizontalAlignment(SwingConstants.LEFT);

			CodicePagamento codicePagamento = (CodicePagamento) value;
			Icon entityIcon = getIconSource().getIcon(value.getClass().getName());
			setIcon(entityIcon);
			String descrizione = codicePagamento.getCodicePagamento() + " - " + codicePagamento.getDescrizione();
			setValue(descrizione);
		}

		return cell;
	}
}