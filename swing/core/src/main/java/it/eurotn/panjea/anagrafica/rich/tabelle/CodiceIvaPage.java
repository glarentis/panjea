package it.eurotn.panjea.anagrafica.rich.tabelle;

import it.eurotn.panjea.anagrafica.domain.CodiceIva;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaTabelleBD;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import org.springframework.richclient.command.AbstractCommand;

public class CodiceIvaPage extends FormBackedDialogPageEditor {

	private final IAnagraficaTabelleBD anagraficaTabelleBD;

	/**
	 * Costruttore.
	 * 
	 * @param pageID
	 *            id della pagina
	 * @param anagraficaTabelleBD
	 *            anagraficaTabelleBD
	 */
	public CodiceIvaPage(final String pageID, final IAnagraficaTabelleBD anagraficaTabelleBD) {
		super(pageID, new CodiceIvaForm(new CodiceIva()));
		org.springframework.util.Assert.notNull(anagraficaTabelleBD, "anagraficaTabelleBD must be not null");
		this.anagraficaTabelleBD = anagraficaTabelleBD;
	}

	@Override
	protected Object doDelete() {
		anagraficaTabelleBD.cancellaCodiceIva((CodiceIva) getBackingFormPage().getFormObject());
		return getBackingFormPage().getFormObject();
	}

	@Override
	protected Object doSave() {
		CodiceIva codiceIva = (CodiceIva) this.getForm().getFormObject();
		codiceIva = anagraficaTabelleBD.salvaCodiceIva(codiceIva);
		return codiceIva;
	}

	@Override
	public AbstractCommand[] getCommand() {
		return toolbarPageEditor.getDefaultCommand(true);
	}

	@Override
	protected boolean insertControlInScrollPane() {
		return false;
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
		CodiceIva codiceIva = (CodiceIva) object;
		super.setFormObject(codiceIva);
	}

}
