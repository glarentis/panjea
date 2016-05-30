package it.eurotn.panjea.auvend.rich.editors.importazioni.riparazionecontoterzi;

import it.eurotn.panjea.auvend.rich.bd.IAuVendBD;
import it.eurotn.panjea.auvend.rich.forms.ImportazioneFatturazioneRifornimentiForm;
import it.eurotn.panjea.auvend.rich.forms.ImportazioneRiparazioniContoTerziForm;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

public class ImportazioneRiparazioniContoTerziPage extends FormBackedDialogPageEditor {

	public static final String PAGE_ID = "importazioneRiparazioniContoTerziPage";

	private IAuVendBD auVendBD;

	/**
	 * 
	 * Costruttore.
	 */
	public ImportazioneRiparazioniContoTerziPage() {
		super(PAGE_ID, new ImportazioneRiparazioniContoTerziForm());
	}

	/**
	 * @return Returns the auVendBD.
	 */
	public IAuVendBD getAuVendBD() {
		return auVendBD;
	}

	@Override
	public boolean isDirty() {
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

	/**
	 * @param auVendBD
	 *            The auVendBD to set.
	 */
	public void setAuVendBD(IAuVendBD auVendBD) {
		this.auVendBD = auVendBD;
	}
}
