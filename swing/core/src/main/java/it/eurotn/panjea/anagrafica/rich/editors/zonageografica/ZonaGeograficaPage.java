package it.eurotn.panjea.anagrafica.rich.editors.zonageografica;

import it.eurotn.panjea.anagrafica.domain.ZonaGeografica;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaTabelleBD;
import it.eurotn.panjea.anagrafica.rich.forms.zonageografica.ZonaGeograficaForm;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import org.springframework.richclient.command.AbstractCommand;

/**
 * 
 * @author Leonardo
 */
public class ZonaGeograficaPage extends FormBackedDialogPageEditor {

	private static final String PAGE_ID = "zonaGeograficaPage";
	private IAnagraficaTabelleBD anagraficaTabelleBD = null;

	/**
	 * Costruttore di default,inizializza un nuovo form.
	 */
	public ZonaGeograficaPage() {
		super(PAGE_ID, new ZonaGeograficaForm());
	}

	@Override
	protected Object doDelete() {
		anagraficaTabelleBD.cancellaZonaGeografica((ZonaGeografica) getBackingFormPage().getFormObject());
		return getBackingFormPage().getFormObject();
	}

	@Override
	protected Object doSave() {
		ZonaGeografica zonaGeografica = (ZonaGeografica) this.getForm().getFormObject();
		return anagraficaTabelleBD.salvaZonaGeografica(zonaGeografica);
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
