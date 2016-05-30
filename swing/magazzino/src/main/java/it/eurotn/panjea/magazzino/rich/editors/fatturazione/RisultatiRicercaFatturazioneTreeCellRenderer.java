package it.eurotn.panjea.magazzino.rich.editors.fatturazione;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.magazzino.domain.RigaArticoloLite;
import it.eurotn.panjea.magazzino.rich.editors.fatturazione.AreaMagazzinoLitePM.StatoRigaAreaMagazzinoLitePM;

import java.awt.BorderLayout;
import java.awt.Component;
import java.text.SimpleDateFormat;

import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.image.IconSource;

import com.jidesoft.converter.ObjectConverterManager;

public class RisultatiRicercaFatturazioneTreeCellRenderer extends DefaultTreeCellRenderer {

	/**
	 * CheckBox che visualizza lo stato selected basandosi sulla proprieta'
	 * selected di {@link RataPartitaPM}, se la rata non e' selezionabile la
	 * checkbox e' disabilitata.<br>
	 * <br>
	 * TODO usare lo stesso {@link DefaultTreeCellRenderer} per le tre classi
	 * {@link RisultatiRicercaAreePartitePage},
	 * 
	 * @author Leonardo
	 */
	private class CheckBoxRataSelezionata extends JCheckBox {

		private static final long serialVersionUID = 754325169697832533L;

		/**
		 * Costruttore.
		 * 
		 * @param areaMagazzinoLitePM
		 *            areaMagazzinoLitePM
		 */
		public CheckBoxRataSelezionata(final AreaMagazzinoLitePM areaMagazzinoLitePM) {
			super();
			if (areaMagazzinoLitePM.getStatoRigaAreaMagazzinoLitePM() == StatoRigaAreaMagazzinoLitePM.SELEZIONABILE) {
				this.setSelected(areaMagazzinoLitePM.isSelected());
			} else {
				this.setEnabled(false);
			}
		}
	}

	private static final long serialVersionUID = 3171282695842930298L;

	private final IconSource iconSource = (IconSource) ApplicationServicesLocator.services().getService(
			IconSource.class);

	public static final String KEY_ICON_NON_SELEZIONABILE = "nonSelezionabile.icon";
	public static final String KEY_ICON_SELEZIONABILE = "selezionabile.icon";
	public static final String KEY_ICON_AGGIUNTA = "aggiuntoCarrello.icon";
	private final Icon iconSelezionabile = iconSource.getIcon(KEY_ICON_SELEZIONABILE);

	private final Icon iconAggiuntoCarrello = iconSource.getIcon(KEY_ICON_AGGIUNTA);
	private final Icon iconNonSelezionabile = iconSource.getIcon(KEY_ICON_NON_SELEZIONABILE);

	/**
	 * Costruttore.
	 */
	public RisultatiRicercaFatturazioneTreeCellRenderer() {
		super();
	}

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded,
			boolean leaf, int row, boolean hasFocus) {

		// il component per aggiungere la checkbox di selezione
		JComponent component = null;

		JLabel label = (JLabel) super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
		DefaultMutableTreeTableNode node = (DefaultMutableTreeTableNode) value;

		if (node.getUserObject() instanceof TipoDocumento) {
			TipoDocumento tipoDocumento = (TipoDocumento) node.getUserObject();

			label.setText(tipoDocumento.getCodice() + " - " + tipoDocumento.getDescrizione());
			label.setIcon(iconSource.getIcon(TipoDocumento.class.getName()));

		} else {
			if (node.getUserObject() instanceof AreaMagazzinoLitePM) {
				AreaMagazzinoLitePM areaMagazzinoLitePM = (AreaMagazzinoLitePM) node.getUserObject();

				SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
				label.setText(formatter.format(areaMagazzinoLitePM.getAreaMagazzinoLite().getDataRegistrazione()));

				component = new JPanel(new BorderLayout());
				component.setBackground(UIManager.getColor("JPanel.background"));
				component.add(label, BorderLayout.CENTER);
				CheckBoxRataSelezionata checkBoxRataSelezionata = new CheckBoxRataSelezionata(areaMagazzinoLitePM);
				checkBoxRataSelezionata.setBackground(UIManager.getColor("JPanel.background"));
				component.add(checkBoxRataSelezionata, BorderLayout.LINE_START);

				// se non e' selezionabile mostro la riga in grigio
				if (areaMagazzinoLitePM.getStatoRigaAreaMagazzinoLitePM().equals(
						StatoRigaAreaMagazzinoLitePM.SELEZIONABILE)) {
					label.setIcon(iconSelezionabile);
				} else if (areaMagazzinoLitePM.getStatoRigaAreaMagazzinoLitePM().equals(
						StatoRigaAreaMagazzinoLitePM.AGGIUNTO_CARRELLO)) {
					label.setIcon(iconAggiuntoCarrello);
				} else if (areaMagazzinoLitePM.getStatoRigaAreaMagazzinoLitePM().equals(
						StatoRigaAreaMagazzinoLitePM.NON_SELEZIONABILE)) {
					label.setIcon(iconNonSelezionabile);
				}

				if (!areaMagazzinoLitePM.getAreaMagazzinoLite().getRigheNonValide().isEmpty()) {
					StringBuilder sb = new StringBuilder("<html><ul>");
					for (RigaArticoloLite rigaArticoloLite : areaMagazzinoLitePM.getAreaMagazzinoLite()
							.getRigheNonValide()) {
						sb.append("<li>" + rigaArticoloLite.getArticolo().getDescrizione() + ": <b> - </b>");
						sb.append(ObjectConverterManager.toString(rigaArticoloLite.getRulesValidationError())
								.replaceAll(",", "<b> - </b>"));
						sb.append("</li>");
					}

					sb.append("</ul></html>");
					component.setToolTipText(sb.toString());
				}

				return component;
			}
		}

		return label;
	}
}
