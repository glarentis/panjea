package it.eurotn.panjea.anagrafica.rich.editors.tabelle.noteanagrafica;

import it.eurotn.panjea.anagrafica.domain.NotaAnagrafica;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaTabelleBD;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import org.springframework.richclient.command.AbstractCommand;

public class NotaAnagraficaPage extends FormBackedDialogPageEditor {

	public static final String PAGE_ID = "notaAnagraficaPage";

	private IAnagraficaTabelleBD anagraficaTabelleBD;

	/**
	 * Costruttore.
	 * 
	 */
	public NotaAnagraficaPage() {
		super(PAGE_ID, new NotaAnagraficaForm());
	}

	@Override
	protected Object doDelete() {
		anagraficaTabelleBD.cancellaNotaAnagrafica((NotaAnagrafica) getBackingFormPage().getFormObject());
		return getBackingFormPage().getFormObject();
	}

	@Override
	protected Object doSave() {
		NotaAnagrafica notaAnagrafica = (NotaAnagrafica) getBackingFormPage().getFormObject();
		return anagraficaTabelleBD.salvaNotaAnagrafica(notaAnagrafica);
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
		return true;
	}

	@Override
	public void refreshData() {

	}

	/**
	 * @param anagraficaTabelleBD
	 *            the anagraficaTabelleBD to set
	 */
	public void setAnagraficaTabelleBD(IAnagraficaTabelleBD anagraficaTabelleBD) {
		this.anagraficaTabelleBD = anagraficaTabelleBD;
	}

}
