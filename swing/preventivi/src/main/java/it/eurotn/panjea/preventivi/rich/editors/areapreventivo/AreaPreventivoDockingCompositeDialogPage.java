package it.eurotn.panjea.preventivi.rich.editors.areapreventivo;

import it.eurotn.rich.dialog.DockingCompositeDialogPage;

import org.springframework.richclient.dialog.DialogPage;

public class AreaPreventivoDockingCompositeDialogPage extends DockingCompositeDialogPage {

	private final AreaPreventivoEditorController areaPreventivoEditorController;

	/**
	 * 
	 * @param pageId
	 *            pageid
	 */
	public AreaPreventivoDockingCompositeDialogPage(final String pageId) {
		super(pageId);
		areaPreventivoEditorController = new AreaPreventivoEditorController(this);
	}

	@Override
	public void addPage(DialogPage page) {
		super.addPage(page);
		areaPreventivoEditorController.addPage(page);
	}

	@Override
	public void dispose() {
		areaPreventivoEditorController.dispose();
		super.dispose();
	}

}
