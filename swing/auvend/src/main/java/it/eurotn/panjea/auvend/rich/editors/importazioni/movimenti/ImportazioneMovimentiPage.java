package it.eurotn.panjea.auvend.rich.editors.importazioni.movimenti;

import it.eurotn.panjea.auvend.rich.bd.IAuVendBD;
import it.eurotn.panjea.auvend.rich.forms.ImportazioneMovimentiForm;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

public class ImportazioneMovimentiPage extends FormBackedDialogPageEditor {
	public static final String PAGE_ID = "importazioneMovimentiPage";

	private IAuVendBD auVendBD;

	/**
	 * 
	 * Costruttore.
	 */
	public ImportazioneMovimentiPage() {
		super(PAGE_ID, new ImportazioneMovimentiForm());
	}

	/**
	 * @return Returns the auVendBD.
	 */
	public IAuVendBD getAuVendBD() {
		return auVendBD;
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
