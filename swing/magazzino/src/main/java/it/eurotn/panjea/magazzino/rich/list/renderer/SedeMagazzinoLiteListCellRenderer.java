package it.eurotn.panjea.magazzino.rich.list.renderer;

import it.eurotn.panjea.magazzino.domain.SedeMagazzinoLite;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;

import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.plaf.UIDefaultsLookup;

public class SedeMagazzinoLiteListCellRenderer extends DefaultListCellRenderer {

	private static final long serialVersionUID = 8574802386606008196L;

	public static final String SEDE_DI_RIFATTURAZIONE_ICON = "sedeDiRifatturazione32.icon";

	@Override
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
			boolean cellHasFocus) {
		JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

		label.setIcon(RcpSupport.getIcon(SEDE_DI_RIFATTURAZIONE_ICON));

		SedeMagazzinoLite sedeMagazzino = (SedeMagazzinoLite) value;

		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("<html>");
		stringBuffer.append("<b>");
		stringBuffer.append(sedeMagazzino.getSedeEntita().getEntita().getCodice());
		stringBuffer.append(" - ");
		stringBuffer.append(sedeMagazzino.getSedeEntita().getEntita().getAnagrafica().getDenominazione());
		stringBuffer.append("</b>");
		stringBuffer.append("<br>");
		stringBuffer.append(sedeMagazzino.getSedeEntita().getSede().getDescrizione());
		if (sedeMagazzino.getSedeEntita().getSede().getLocalita() != null) {
			stringBuffer.append(" - ");
			stringBuffer.append(sedeMagazzino.getSedeEntita().getSede().getLocalita().getDescrizione());
		}
		String indirizzo = sedeMagazzino.getSedeEntita().getSede().getIndirizzo();
		if (indirizzo != null && !indirizzo.isEmpty()) {
			stringBuffer.append(" - ");
			stringBuffer.append(indirizzo);
		}
		stringBuffer.append("</html>");

		label.setText(stringBuffer.toString());

		if (isSelected) {
			label.setBackground(UIDefaultsLookup.getColor("Table.selectionBackground"));
		} else {
			label.setBackground(UIDefaultsLookup.getColor("Panel.background"));
		}

		return label;
	}
}