package it.eurotn.panjea.rich.editors.documentograph;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.documenti.graph.node.DocumentoNode;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;

import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JList;

import org.springframework.richclient.util.RcpSupport;

public class BreadcrumbDocumentoListRenderer extends DefaultListCellRenderer {

	private static final long serialVersionUID = -8868202753267173254L;

	private Icon tipoDocumentoIcon = null;

	{
		tipoDocumentoIcon = RcpSupport.getIcon(TipoDocumento.class.getName());
	}

	@Override
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
			boolean cellHasFocus) {
		JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

		DocumentoNode node = (DocumentoNode) value;

		StringBuilder sb = new StringBuilder();
		sb.append("<html><b>");
		sb.append(node.getDescrizioneDocumento());
		sb.append("</b></html>");
		label.setText(sb.toString());
		label.setBackground(Color.decode(node.getColore()));
		label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		label.setIcon(tipoDocumentoIcon);

		return label;
	}
}
