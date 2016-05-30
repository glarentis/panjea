/**
 * 
 */
package it.eurotn.panjea.anagrafica.rich.tabelle;

import it.eurotn.panjea.anagrafica.domain.Mansione;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaTabelleBD;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import org.springframework.richclient.command.AbstractCommand;

/**
 * 
 * @author Aracno
 * @version 1.0, 16/ott/06
 * 
 */
public class MansionePage extends FormBackedDialogPageEditor {

	private IAnagraficaTabelleBD anagraficaTabelleBD;

	/**
	 * Costruttore.
	 * 
	 * @param pageID
	 *            id della papgina
	 */
	public MansionePage(final String pageID) {
		super(pageID, new MansioneForm(new Mansione()));
	}

	@Override
	protected Object doDelete() {
		anagraficaTabelleBD.cancellaMansione((Mansione) getBackingFormPage().getFormObject());
		return getBackingFormPage().getFormObject();
	}

	@Override
	protected Object doSave() {
		Mansione mansione = (Mansione) this.getForm().getFormObject();
		return anagraficaTabelleBD.salvaMansione(mansione);
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
	 *            The anagraficaTabelleBD to set.
	 */
	public void setAnagraficaTabelleBD(IAnagraficaTabelleBD anagraficaTabelleBD) {
		this.anagraficaTabelleBD = anagraficaTabelleBD;
	}
}
