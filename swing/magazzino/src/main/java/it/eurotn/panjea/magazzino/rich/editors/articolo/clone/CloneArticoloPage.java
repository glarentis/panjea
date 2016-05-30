package it.eurotn.panjea.magazzino.rich.editors.articolo.clone;

import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import org.springframework.richclient.command.AbstractCommand;

public class CloneArticoloPage extends FormBackedDialogPageEditor {

	public static final String PAGE_ID = "CloneArticoloPage";

	/**
	 * Costruttore.
	 *
	 * @param parameter
	 *            articolo da gestire
	 *
	 */
	public CloneArticoloPage(final CloneArticoloParameter parameter) {
		super(PAGE_ID, new CloneArticoloForm(parameter));
	}

	@Override
	protected Object doSave() {
		return null;
	}

	@Override
	protected AbstractCommand[] getCommand() {
		return new AbstractCommand[] {};
	}

	@Override
	public void loadData() {

	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	@Override
	public void refreshData() {
	}

	@Override
	public void setFormObject(Object object) {
		super.setFormObject(object);
	}
}
