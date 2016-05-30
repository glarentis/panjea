/**
 * 
 */
package it.eurotn.panjea.anagrafica.rich.tabelle;

import it.eurotn.panjea.anagrafica.domain.CategoriaEntita;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaBD;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import org.springframework.richclient.command.AbstractCommand;

/**
 * @author Leonardo
 * 
 */
public class CategoriaEntitaPage extends FormBackedDialogPageEditor {

	private IAnagraficaBD anagraficaBD;

	/**
	 * Costruttore.
	 * 
	 * @param pageID
	 *            id della pagina
	 */
	public CategoriaEntitaPage(final String pageID) {
		super(pageID, new CategoriaEntitaForm(new CategoriaEntita()));
	}

	@Override
	protected Object doDelete() {
		anagraficaBD.cancellaCategoriaEntita((CategoriaEntita) getBackingFormPage().getFormObject());
		return getBackingFormPage().getFormObject();
	}

	@Override
	protected Object doSave() {
		CategoriaEntita categoriaEntita = (CategoriaEntita) this.getForm().getFormObject();
		return anagraficaBD.salvaCategoriaEntita(categoriaEntita);
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
	 *            The anagraficaBD to set.
	 */
	public void setAnagraficaBD(IAnagraficaBD anagraficaBD) {
		this.anagraficaBD = anagraficaBD;
	}

}
