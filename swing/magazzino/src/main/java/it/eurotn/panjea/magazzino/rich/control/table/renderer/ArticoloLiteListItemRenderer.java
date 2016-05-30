package it.eurotn.panjea.magazzino.rich.control.table.renderer;

import it.eurotn.panjea.magazzino.domain.ArticoloLite;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JList;

import org.apache.commons.lang3.StringUtils;
import org.springframework.richclient.util.RcpSupport;

public class ArticoloLiteListItemRenderer extends DefaultListCellRenderer {

	private static final long serialVersionUID = -1480573955301891260L;

	@SuppressWarnings("rawtypes")
	@Override
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
			boolean cellHasFocus) {
		JLabel label = (JLabel) super.getListCellRendererComponent(list, "", index, isSelected, cellHasFocus);
		label.setText(new StringBuilder(((ArticoloLite) value).getCodice()).append(" - ")
				.append(StringUtils.abbreviate(((ArticoloLite) value).getDescrizione(), 40)).toString());
		Icon icon = RcpSupport.getIcon(ArticoloLite.class.getName());
		label.setIcon(icon);
		return label;
	}

}
