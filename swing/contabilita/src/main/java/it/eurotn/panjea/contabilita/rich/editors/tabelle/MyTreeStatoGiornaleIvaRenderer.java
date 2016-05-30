package it.eurotn.panjea.contabilita.rich.editors.tabelle;

import it.eurotn.panjea.contabilita.domain.GiornaleIva;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.swing.CheckBoxTreeCellRenderer;

/**
 * Aggiungo l'icona per visualizzare lo stato stampato del registro iva.
 * <ul>
 * <li>verde=stampato</li>
 * <li>giallo=non stampato</li>
 * <li>rosso=stampato non valido</li>
 * </ul>
 * 
 * @author Leonardo
 */
public class MyTreeStatoGiornaleIvaRenderer extends CheckBoxTreeCellRenderer {

	private static final long serialVersionUID = 2572619892391440400L;

	public static final String STATO_STAMPATO = "giornaleIvaPage.table.giornaleIva.stato.stampato";
	public static final String STATO_NON_STAMPATO = "giornaleIvaPage.table.giornaleIva.stato.nonStampato";
	public static final String STATO_NON_VALIDO = "giornaleIvaPage.table.giornaleIva.stato.nonValido";

	/**
	 * Costruttore di default.
	 */
	public MyTreeStatoGiornaleIvaRenderer() {
	}

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded,
			boolean leaf, int row, boolean hasFocus) {
		JComponent component = (JComponent) super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row,
				hasFocus);
		CheckBoxTreeCellRenderer checkBoxTreeCellRenderer = (CheckBoxTreeCellRenderer) component;

		JLabel label = (JLabel) ((DefaultTreeCellRenderer) checkBoxTreeCellRenderer.getComponent(1))
				.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
		label.setOpaque(true);
		setOpaque(true);
		Color fg;
		if (sel) {
			fg = UIManager.getColor("TextField.selectionBackground");
		} else {
			fg = UIManager.getColor("Panel.background");
		}
		setBackground(fg);

		DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;

		// se di tipoRegistro sono su un nodo padre che raggruppa per tipo
		// registro Acquisto, vendita, corrispettivo
		if (node.getUserObject() instanceof TipoRegistroPM) {
			TipoRegistroPM tipoRegistroPM = (TipoRegistroPM) node.getUserObject();
			label.setText(tipoRegistroPM.getTipoRegistro().name());
			int stato = tipoRegistroPM.getStato();
			switch (stato) {
			case 0:
				label.setIcon(RcpSupport.getIcon(STATO_STAMPATO));
				label.setToolTipText(RcpSupport.getMessage(STATO_STAMPATO));
				break;
			case 1:
				label.setIcon(RcpSupport.getIcon(STATO_NON_STAMPATO));
				label.setToolTipText(RcpSupport.getMessage(STATO_NON_STAMPATO));
				break;
			case 2:
				label.setIcon(RcpSupport.getIcon(STATO_NON_VALIDO));
				label.setToolTipText(RcpSupport.getMessage(STATO_NON_VALIDO));
				break;
			default:
				break;
			}
		} else if (node.getUserObject() instanceof GiornaleIva) {
			GiornaleIva giornaleIva = (GiornaleIva) node.getUserObject();
			// come testo la descrizione del registro iva associato al
			// giornale iva
			// e come icona

			String descr = giornaleIva.getRegistroIva().getDescrizione();
			Integer num = giornaleIva.getRegistroIva().getNumero();
			label.setText(num + " - " + descr);
			int stato = giornaleIva.getStato();
			switch (stato) {
			case 0:
				label.setIcon(RcpSupport.getIcon(STATO_STAMPATO));
				label.setToolTipText(RcpSupport.getMessage(STATO_STAMPATO));
				break;
			case 1:
				label.setIcon(RcpSupport.getIcon(STATO_NON_STAMPATO));
				label.setToolTipText(RcpSupport.getMessage(STATO_NON_STAMPATO));
				break;
			case 2:
				label.setIcon(RcpSupport.getIcon(STATO_NON_VALIDO));
				label.setToolTipText(RcpSupport.getMessage(STATO_NON_VALIDO));
				break;
			default:
				break;
			}
		}

		return component;
	}
}