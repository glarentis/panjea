/**
 * 
 */
package it.eurotn.panjea.anagrafica.rich.tabelle;

import it.eurotn.panjea.anagrafica.domain.TipoSedeEntita;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaTabelleBD;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import org.springframework.richclient.command.AbstractCommand;

/**
 * 
 * @author Aracno
 * @version 1.0, 16/ott/06
 * 
 */
public class TipoSedeEntitaPage extends FormBackedDialogPageEditor {

	private IAnagraficaTabelleBD anagraficaTabelleBD;

	/**
	 * Costruttore.
	 * 
	 * @param pageID
	 *            id della pagina
	 */
	public TipoSedeEntitaPage(final String pageID) {
		super(pageID, new TipoSedeEntitaForm(new TipoSedeEntita()));
	}

	@Override
	protected Object doDelete() {
		anagraficaTabelleBD.cancellaTipoSedeEntita((TipoSedeEntita) getBackingFormPage().getFormObject());
		return getBackingFormPage().getFormObject();
	}

	@Override
	protected Object doSave() {
		TipoSedeEntita tipoSedeEntita = (TipoSedeEntita) this.getForm().getFormObject();
		return anagraficaTabelleBD.salvaTipoSedeEntita(tipoSedeEntita);
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
