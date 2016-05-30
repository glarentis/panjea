package it.eurotn.panjea.anagrafica.rich.tabelle;

import it.eurotn.panjea.anagrafica.domain.Lingua;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaTabelleBD;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import org.springframework.richclient.command.AbstractCommand;

public class LinguaPage extends FormBackedDialogPageEditor {

	private IAnagraficaTabelleBD anagraficaTabelleBD;
	private static final String PAGE_ID = "linguaPage";

	/**
	 * Costruttore.
	 */
	public LinguaPage() {
		super(PAGE_ID, new LinguaForm(new Lingua()));
	}

	@Override
	protected Object doDelete() {
		anagraficaTabelleBD.cancellaLingua((Lingua) getBackingFormPage().getFormObject());
		return getBackingFormPage().getFormObject();
	}

	@Override
	protected Object doSave() {
		Lingua lingua = (Lingua) getForm().getFormObject();
		// Rimuovo la descrizione dal codice
		String[] descLingua = lingua.getCodice().split("-");
		lingua.setCodice(descLingua[0].trim());
		return anagraficaTabelleBD.salvaLingua(lingua);
	}

	@Override
	public AbstractCommand[] getCommand() {
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
	 *            anagraficaTabelleBD to set
	 */
	public void setAnagraficaTabelleBD(IAnagraficaTabelleBD anagraficaTabelleBD) {
		this.anagraficaTabelleBD = anagraficaTabelleBD;
	}
}
