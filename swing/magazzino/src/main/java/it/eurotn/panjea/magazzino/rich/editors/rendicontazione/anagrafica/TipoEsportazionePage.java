package it.eurotn.panjea.magazzino.rich.editors.rendicontazione.anagrafica;

import it.eurotn.panjea.magazzino.domain.rendicontazione.TipoEsportazione;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import org.springframework.richclient.command.AbstractCommand;

public class TipoEsportazionePage extends FormBackedDialogPageEditor {

	public static final String PAGE_ID = "tipoEsportazionePage";

	private IMagazzinoAnagraficaBD magazzinoAnagraficaBD;

	/**
	 * Costruttore.
	 * 
	 */
	public TipoEsportazionePage() {
		super(PAGE_ID, new TipoEsportazioneForm());
	}

	@Override
	protected Object doDelete() {
		TipoEsportazione tipoEsportazione = (TipoEsportazione) getBackingFormPage().getFormObject();

		magazzinoAnagraficaBD.cancellaTipoEsportazione(tipoEsportazione);
		return tipoEsportazione;
	}

	@Override
	protected Object doSave() {
		TipoEsportazione tipoEsportazione = (TipoEsportazione) getBackingFormPage().getFormObject();

		tipoEsportazione = magazzinoAnagraficaBD.salvaTipoEsportazione(tipoEsportazione);
		return tipoEsportazione;
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

	@Override
	public void setFormObject(Object object) {

		TipoEsportazione tipoEsportazione = (TipoEsportazione) object;

		if (tipoEsportazione.getId() != null) {
			tipoEsportazione = magazzinoAnagraficaBD.caricaTipoEsportazione(tipoEsportazione.getId(), false);
		}

		super.setFormObject(tipoEsportazione);
	}

	/**
	 * @param magazzinoAnagraficaBD
	 *            the magazzinoAnagraficaBD to set
	 */
	public void setMagazzinoAnagraficaBD(IMagazzinoAnagraficaBD magazzinoAnagraficaBD) {
		this.magazzinoAnagraficaBD = magazzinoAnagraficaBD;
	}

}
