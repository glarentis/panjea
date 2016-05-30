/**
 * 
 */
package it.eurotn.panjea.anagrafica.rich.tabelle;

import it.eurotn.panjea.anagrafica.domain.Banca;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaBD;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import org.springframework.richclient.command.AbstractCommand;

/**
 * @author Leonardo
 * 
 */
public class BancaPage extends FormBackedDialogPageEditor {

	private IAnagraficaBD anagraficaBD;

	/**
	 * Costruttore.
	 * 
	 * @param pageID
	 *            id della pagina
	 */
	public BancaPage(final String pageID) {
		super(pageID, new BancaForm(new Banca()));
	}

	@Override
	protected Object doDelete() {
		anagraficaBD.cancellaBanca((Banca) getBackingFormPage().getFormObject());
		return getBackingFormPage().getFormObject();
	}

	@Override
	protected Object doSave() {
		Banca banca = (Banca) this.getForm().getFormObject();
		return anagraficaBD.salvaBanca(banca);
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
	 * @param anagraficaBD
	 *            anagraficaBD to set
	 */
	public void setAnagraficaBD(IAnagraficaBD anagraficaBD) {
		this.anagraficaBD = anagraficaBD;
	}

}
