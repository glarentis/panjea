package it.eurotn.panjea.magazzino.rich.editors.listinotipomezzozonageografica;

import it.eurotn.panjea.magazzino.domain.moduloprezzo.ListinoTipoMezzoZonaGeografica;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.panjea.magazzino.rich.forms.listinotipomezzozonageografica.ListinoTipoMezzoZonaGeograficaForm;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import org.springframework.richclient.command.AbstractCommand;

public class ListinoTipoMezzoZonaGeograficaPage extends FormBackedDialogPageEditor {

	private static final String PAGE_ID = "listinoTipoMezzoZonaGeograficaPage";
	private IMagazzinoAnagraficaBD magazzinoAnagraficaBD = null;

	/**
	 * Costruttore di default,inizializza un nuovo form.
	 */
	public ListinoTipoMezzoZonaGeograficaPage() {
		super(PAGE_ID, new ListinoTipoMezzoZonaGeograficaForm());
	}

	@Override
	protected Object doDelete() {
		magazzinoAnagraficaBD
				.cancellaListinoTipoMezzoZonaGeografica((ListinoTipoMezzoZonaGeografica) getBackingFormPage()
						.getFormObject());
		return getBackingFormPage().getFormObject();
	}

	@Override
	protected Object doSave() {
		ListinoTipoMezzoZonaGeografica listinoTipoMezzoZonaGeografica = (ListinoTipoMezzoZonaGeografica) this.getForm()
				.getFormObject();
		return magazzinoAnagraficaBD.salvaListinoTipoMezzoZonaGeografica(listinoTipoMezzoZonaGeografica);
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
	 * Set magazzinoAnagraficaBD.
	 * 
	 * @param magazzinoAnagraficaBD
	 *            il bd da utilizzare per accedere alle operazioni sui tipi mezzo trasporto
	 */
	public void setMagazzinoAnagraficaBD(IMagazzinoAnagraficaBD magazzinoAnagraficaBD) {
		this.magazzinoAnagraficaBD = magazzinoAnagraficaBD;
	}

}
