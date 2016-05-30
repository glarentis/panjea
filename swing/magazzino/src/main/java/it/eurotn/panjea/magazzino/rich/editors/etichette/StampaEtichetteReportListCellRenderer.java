package it.eurotn.panjea.magazzino.rich.editors.etichette;

import it.eurotn.panjea.magazzino.domain.etichetta.LayoutStampaEtichette;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

public class StampaEtichetteReportListCellRenderer extends DefaultListCellRenderer {

	
	private static final long serialVersionUID = -5245246241059029611L;

	@Override
	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
		
		Component component = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

		if (value instanceof LayoutStampaEtichette) {
			LayoutStampaEtichette report = (LayoutStampaEtichette) value;
			setText(report.getDescrizione());
		}

		return component;
	}

}
