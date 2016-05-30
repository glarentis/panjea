package it.eurotn.panjea.auvend.rich.editors.importazioni.fatturazionerifornimenti;

import it.eurotn.panjea.auvend.rich.bd.IAuVendBD;
import it.eurotn.panjea.auvend.rich.forms.ImportazioneFatturazioneRifornimentiForm;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

public class ImportazioneFatturazioneRifornimentiPage extends FormBackedDialogPageEditor {
	public static final String PAGE_ID = "importazioneFatturazioneRifornimentiPage";

	private IAuVendBD auVendBD;

	/**
	 * 
	 * Costruttore.
	 */
	public ImportazioneFatturazioneRifornimentiPage() {
		super(PAGE_ID, new ImportazioneFatturazioneRifornimentiForm());
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
