package it.eurotn.panjea.mrp.rich.editors.risultato;

import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import org.springframework.richclient.command.AbstractCommand;

public class RisultatoMrpFlatPage extends FormBackedDialogPageEditor {

	private static final String PAGE_ID = "risultatoMrpFlatPage";


	/**
	 * Costruttore.
	 */
	public RisultatoMrpFlatPage() {
		super(PAGE_ID, new RisultatoMrpFlatForm());
	}

	@Override
	protected Object doDelete() {
		return getBackingFormPage().getFormObject();
	}

	@Override
	protected Object doSave() {
		return getBackingFormPage().getFormObject();
	}

	@Override
	protected AbstractCommand[] getCommand() {
		return toolbarPageEditor.getDefaultCommand(true);
	}

	@Override
	public void loadData() {
	}

	@Override
	public void onNew() {
	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public void refreshData() {
	}

}
