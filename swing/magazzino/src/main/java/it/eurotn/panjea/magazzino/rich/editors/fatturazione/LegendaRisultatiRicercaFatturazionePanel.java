/**
 * 
 */
package it.eurotn.panjea.magazzino.rich.editors.fatturazione;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.Locale;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.springframework.context.MessageSource;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.factory.ComponentFactory;
import org.springframework.richclient.image.IconSource;

/**
 * Pannello che visualizza la legenda dei risultati ricerca della pagina {@link RisultatiRicercaFatturazioneTablePage}.
 * 
 * @author fattazzo
 * 
 */
public class LegendaRisultatiRicercaFatturazionePanel extends JPanel {

	private static final long serialVersionUID = 6744726660254595556L;

	/**
	 * Costruttore pannello.
	 */
	public LegendaRisultatiRicercaFatturazionePanel() {
		super();
		initialize();
	}

	/**
	 * Aggiunge al pannello i le icone e le label per la legenda.
	 */
	private void initialize() {
		ComponentFactory componentFactory = (ComponentFactory) Application.services()
				.getService(ComponentFactory.class);

		IconSource iconSource = (IconSource) Application.services().getService(IconSource.class);
		MessageSource messageSource = (MessageSource) Application.services().getService(MessageSource.class);

		setLayout(new BorderLayout());

		JPanel leftPanel = componentFactory.createPanel(new FlowLayout(FlowLayout.LEFT));

		Icon iconAggiuntoCarrello = iconSource.getIcon(RisultatiRicercaFatturazioneTablePage.KEY_ICON_AGGIUNTA);
		Icon iconNonSelezionabile = iconSource
				.getIcon(RisultatiRicercaFatturazioneTablePage.KEY_ICON_NON_SELEZIONABILE);
		Icon iconSelezionabile = iconSource.getIcon(RisultatiRicercaFatturazioneTablePage.KEY_ICON_SELEZIONABILE);

		String msgAggiuntoCarrello = messageSource.getMessage(RisultatiRicercaFatturazioneTablePage.KEY_MSG_AGGIUNTA,
				new String[] {}, Locale.getDefault());
		String msgNonSelezionabile = messageSource.getMessage(
				RisultatiRicercaFatturazioneTablePage.KEY_MSG_NON_SELEZIONABILE, new String[] {}, Locale.getDefault());
		String msgSelezionabile = messageSource.getMessage(RisultatiRicercaFatturazioneTablePage.KEY_MSG_SELEZIONABILE,
				new String[] {}, Locale.getDefault());

		JLabel labelSelezionabile = componentFactory.createLabel(msgSelezionabile);
		labelSelezionabile.setIcon(iconSelezionabile);
		JLabel labelNonSelezionabile = componentFactory.createLabel(msgNonSelezionabile);
		labelNonSelezionabile.setIcon(iconNonSelezionabile);
		JLabel labelAggiuntoCarrello = componentFactory.createLabel(msgAggiuntoCarrello);
		labelAggiuntoCarrello.setIcon(iconAggiuntoCarrello);

		leftPanel.add(labelSelezionabile);
		leftPanel.add(labelAggiuntoCarrello);
		leftPanel.add(labelNonSelezionabile);
		add(leftPanel, BorderLayout.LINE_START);
	}
}
