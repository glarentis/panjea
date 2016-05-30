package it.eurotn.panjea.magazzino.rich.editors.rendicontazione.entita;

import it.eurotn.panjea.magazzino.domain.rendicontazione.EntitaTipoEsportazione;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import org.springframework.richclient.command.AbstractCommand;

public class EntitaTipoEsportazionePage extends FormBackedDialogPageEditor {

	public static final String PAGE_ID = "entitaTipoEsportazionePage";

	private IMagazzinoAnagraficaBD magazzinoAnagraficaBD;

	/**
	 * Costruttore.
	 */
	public EntitaTipoEsportazionePage() {
		super(PAGE_ID, new EntitaTipoEsportazioneForm());
	}

	@Override
	protected Object doDelete() {
		EntitaTipoEsportazione entitaTipoEsportazione = (EntitaTipoEsportazione) getBackingFormPage().getFormObject();

		magazzinoAnagraficaBD.cancellaEntitaTipoEsportazione(entitaTipoEsportazione);

		return entitaTipoEsportazione;
	}

	@Override
	protected Object doSave() {
		EntitaTipoEsportazione entitaTipoEsportazione = (EntitaTipoEsportazione) getBackingFormPage().getFormObject();

		entitaTipoEsportazione = magazzinoAnagraficaBD.salvaEntitaTipoEsportazione(entitaTipoEsportazione);

		return entitaTipoEsportazione;
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
	 * @param magazzinoAnagraficaBD
	 *            The magazzinoAnagraficaBD to set.
	 */
	public void setMagazzinoAnagraficaBD(IMagazzinoAnagraficaBD magazzinoAnagraficaBD) {
		this.magazzinoAnagraficaBD = magazzinoAnagraficaBD;
	}

}
