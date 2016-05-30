package it.eurotn.panjea.magazzino.rich.editors.areamagazzino;

import it.eurotn.rich.dialog.DockingCompositeDialogPage;

import org.springframework.richclient.dialog.DialogPage;
import org.springframework.richclient.settings.SettingsManager;

public class AreaMagazzinoDockingCompositeDialogPage extends DockingCompositeDialogPage {

	private final AreaMagazzinoEditorController areaMagazzinoEditorController;

	/**
	 * Costruttore.
	 * 
	 * @param pageId
	 *            id della pagina
	 * @param settingsManager
	 *            {@link SettingsManager}
	 */
	public AreaMagazzinoDockingCompositeDialogPage(final String pageId, final SettingsManager settingsManager) {
		super(pageId);
		areaMagazzinoEditorController = new AreaMagazzinoEditorController(this, settingsManager);
	}

	@Override
	public void addPage(DialogPage page) {
		super.addPage(page);
		areaMagazzinoEditorController.addPage(page);
	}

	@Override
	public void dispose() {
		areaMagazzinoEditorController.dispose();
		super.dispose();
	}
}