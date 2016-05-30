package it.eurotn.panjea.magazzino.rich.forms.categoriaarticolo.attributi;

import it.eurotn.panjea.magazzino.domain.TipoAttributo;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;

public class TipoAttributoListCellRender extends DefaultListCellRenderer {

	private static final long serialVersionUID = -2717948574707490360L;

	@Override
	public Component getListCellRendererComponent(JList<?> jlist, Object value, int index, boolean isSelected,
			boolean cellHasFocus) {
		JLabel label = (JLabel) super.getListCellRendererComponent(jlist, value, index, isSelected, cellHasFocus);

		TipoAttributo tipoAttributo = (TipoAttributo) value;
		label.setText(tipoAttributo.getCodice());
		label.setToolTipText(tipoAttributo.getNome());
		label.setOpaque(false);
		return label;
	}

}