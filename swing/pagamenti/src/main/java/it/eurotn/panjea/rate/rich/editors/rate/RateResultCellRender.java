/**
 * 
 */
package it.eurotn.panjea.rate.rich.editors.rate;

import it.eurotn.panjea.rate.domain.Rata;
import it.eurotn.panjea.rate.domain.Rata.StatoRata;
import it.eurotn.panjea.tesoreria.domain.Pagamento;

import java.awt.Component;
import java.util.Locale;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.springframework.context.MessageSource;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.image.IconSource;

/**
 * @author Leonardo
 * 
 */
public class RateResultCellRender extends DefaultTreeCellRenderer {

	private static final long serialVersionUID = -526094759588706608L;
	private IconSource iconSource = null;
	private MessageSource messageSource = null;

	/**
	 * 
	 * @return iconsource
	 */
	private IconSource getIconSource() {
		if (iconSource == null) {
			iconSource = (IconSource) Application.services().getService(IconSource.class);
		}
		return iconSource;
	}

	/**
	 * 
	 * @param key
	 *            chiave del messaggio
	 * @return messaggio I18N
	 */
	private String getMessage(String key) {
		if (messageSource == null) {
			messageSource = (MessageSource) Application.services().getService(MessageSource.class);
		}
		return messageSource.getMessage(key, null, Locale.getDefault());
	}

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded,
			boolean leaf, int row, boolean hasFocus) {
		JLabel label = (JLabel) super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
		DefaultMutableTreeTableNode node = (DefaultMutableTreeTableNode) value;
		label.setToolTipText(null);

		if (node.getUserObject() instanceof Pagamento) {
			label.setIcon(getIconSource().getIcon(Pagamento.class.getName()));

			Pagamento pagamento = (Pagamento) node.getUserObject();
			if (pagamento.isInsoluto()) {
				setText("Insoluto");
			} else {
				setText("");
			}
		} else if (node.getUserObject() instanceof Rata) {

			Rata rata = (Rata) node.getUserObject();

			Integer numeroRata = rata.getNumeroRata();
			StatoRata statoRata = rata.getStatoRata();

			String className = statoRata.getClass().getName();
			className = className + "." + statoRata.name();

			Icon icon = getIconSource().getIcon(className);
			String tooltip = getMessage(className);

			StringBuilder sb = new StringBuilder();
			sb.append(numeroRata);
			sb.append("");
			if (rata.getRataRiemessa() != null && rata.getRataRiemessa().getId() != null) {
				sb.append(" (");
				sb.append(rata.getRataRiemessa().getNumeroRata());
				sb.append(")");
			}

			label.setIcon(icon);
			setText(sb.toString());
			label.setToolTipText(tooltip);
		}
		return label;
	}

}
