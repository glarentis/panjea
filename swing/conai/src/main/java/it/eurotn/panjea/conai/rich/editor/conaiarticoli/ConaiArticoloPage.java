package it.eurotn.panjea.conai.rich.editor.conaiarticoli;

import it.eurotn.panjea.conai.domain.ConaiArticolo;
import it.eurotn.panjea.conai.rich.bd.IConaiBD;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import org.springframework.richclient.command.AbstractCommand;

public class ConaiArticoloPage extends FormBackedDialogPageEditor {

	public static final String PAGE_ID = "conaiArticoloPage";

	private IConaiBD conaiBD;

	/**
	 * Costruttore.
	 */
	public ConaiArticoloPage() {
		super(PAGE_ID, new ConaiArticoloForm());
	}

	@Override
	protected Object doSave() {
		ConaiArticolo conaiArticolo = (ConaiArticolo) getForm().getFormObject();
		return conaiBD.salvaArticoloConai(conaiArticolo);
	}

	@Override
	protected AbstractCommand[] getCommand() {
		return new AbstractCommand[] { toolbarPageEditor.getLockCommand(), toolbarPageEditor.getSaveCommand(),
				toolbarPageEditor.getUndoCommand() };
	}

	/**
	 * @return Returns the conaiBD.
	 */
	public IConaiBD getConaiBD() {
		return conaiBD;
	}

	@Override
	public void loadData() {
	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public void refreshData() {
	}

	/**
	 * @param conaiBD
	 *            The conaiBD to set.
	 */
	public void setConaiBD(IConaiBD conaiBD) {
		this.conaiBD = conaiBD;
	}

	@Override
	public void setFormObject(Object object) {
		super.setFormObject(object);
	}

}
