package it.eurotn.panjea.magazzino.rich.editors.documenti.spedizione.renderer;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;

import com.jidesoft.converter.ObjectConverterManager;

public class DatiMailCellRenderer extends DefaultListCellRenderer {

	private static final long serialVersionUID = 8505351723356791252L;

	@Override
	public Component getListCellRendererComponent(JList<? extends Object> list, Object value, int index,
			boolean isSelected, boolean cellHasFocus) {

		JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		label.setText(ObjectConverterManager.toString(value));
		return label;
	}

}
