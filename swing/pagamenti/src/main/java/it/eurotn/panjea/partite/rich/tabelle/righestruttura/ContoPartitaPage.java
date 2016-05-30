package it.eurotn.panjea.partite.rich.tabelle.righestruttura;

import it.eurotn.panjea.partite.domain.ContoPartita;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import org.springframework.richclient.command.AbstractCommand;

public class ContoPartitaPage extends FormBackedDialogPageEditor {

	private static final String PAGE_ID = "contoPartitaPage";

	/**
	 * Costruttore.
	 * 
	 */
	public ContoPartitaPage() {
		super(PAGE_ID, new ContoPartitaForm(new ContoPartita()));
	}

	@Override
	protected Object doDelete() {
		return super.doDelete();
	}

	@Override
	protected Object doSave() {
		return getForm().getFormObject();
	}

	@Override
	protected AbstractCommand[] getCommand() {
		return toolbarPageEditor.getDefaultCommand(true);
	}

	@Override
	public void loadData() {
	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public boolean onPrePageOpen() {
		return false;
	}

	@Override
	public void refreshData() {
	}

}
