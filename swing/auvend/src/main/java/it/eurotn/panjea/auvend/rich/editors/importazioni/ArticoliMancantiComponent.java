package it.eurotn.panjea.auvend.rich.editors.importazioni;

import it.eurotn.panjea.auvend.domain.Articolo;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.Collection;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;

import org.springframework.richclient.factory.AbstractControlFactory;

public class ArticoliMancantiComponent extends AbstractControlFactory {

	private JList listArticoliMancanti;

	/**
	 * @param articoli
	 *            articoli mancanti da aggiungere
	 */
	public void aggiungiArticoliMancanti(Collection<Articolo> articoli) {
		((DefaultListModel) this.listArticoliMancanti.getModel()).removeAllElements();
		for (Articolo articolo : articoli) {
			((DefaultListModel) this.listArticoliMancanti.getModel()).addElement(articolo);
		}
	}

	@Override
	protected JComponent createControl() {
		JPanel artPanel = new JPanel(new BorderLayout());
		artPanel.setBorder(BorderFactory.createTitledBorder("Articoli mancanti"));
		artPanel.setPreferredSize(new Dimension(300, 300));

		this.listArticoliMancanti = new JList(new DefaultListModel());
		this.listArticoliMancanti.setCellRenderer(new DefaultListCellRenderer() {

			private static final long serialVersionUID = -4065052882517619876L;

			@Override
			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				JLabel label = (JLabel) super
						.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

				Articolo articolo = (Articolo) value;

				label.setText(articolo.getCodice() + " - " + articolo.getDescrizione());
				label.setIcon(getIconSource().getIcon(it.eurotn.panjea.magazzino.domain.Articolo.class.getName()));

				return label;
			}
		});
		artPanel.add(getComponentFactory().createScrollPane(this.listArticoliMancanti), BorderLayout.CENTER);

		return artPanel;
	}

}
