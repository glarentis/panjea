package it.eurotn.panjea.intra.rich.editors.dichiarazione.sezione3;

import it.eurotn.panjea.intra.domain.DichiarazioneIntra;
import it.eurotn.panjea.intra.domain.RigaSezione3Intra;
import it.eurotn.panjea.intra.rich.bd.IIntraBD;
import it.eurotn.rich.editors.AbstractTablePageEditor;
import it.eurotn.rich.editors.table.EditFrame;

import java.util.Collection;

public class Sezione3ServiziTablePage extends AbstractTablePageEditor<RigaSezione3Intra> {

	private DichiarazioneIntra dichiarazioneIntra;
	private IIntraBD intraBD;

	/**
	 * Costruttore.
	 */
	public Sezione3ServiziTablePage() {
		super("sezione3ServiziTablePage", new String[] { "progressivo", "fornitoreStato", "fornitorepiva", "importo",
				"importoInValuta", "numeroFattura", "dataFattura", "servizio", "modalitaErogazione", "modalitaIncasso",
				"paesePagamento" }, RigaSezione3Intra.class);
	}

	/**
	 * @return Returns the intraBD.
	 */
	public IIntraBD getIntraBD() {
		return intraBD;
	}

	@Override
	public Collection<RigaSezione3Intra> loadTableData() {
		return intraBD.caricaRigheSezioniDichiarazione(dichiarazioneIntra, RigaSezione3Intra.class);
	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public boolean onPrePageOpen() {
		boolean canOpen = true;
		if (dichiarazioneIntra != null) {
			canOpen = !dichiarazioneIntra.isNew();
		}
		return canOpen;
	}

	@Override
	public Collection<RigaSezione3Intra> refreshTableData() {
		return loadTableData();
	}

	@Override
	public void setFormObject(Object object) {
		dichiarazioneIntra = (DichiarazioneIntra) object;
		Sezione3ServiziPage sezione3AcquistiBenePage = (Sezione3ServiziPage) getEditPages().get(
				EditFrame.DEFAULT_OBJECT_CLASS_NAME);
		sezione3AcquistiBenePage.setDichiarazioneIntra(dichiarazioneIntra);
	}

	/**
	 * @param intraBD
	 *            The intraBD to set.
	 */
	public void setIntraBD(IIntraBD intraBD) {
		this.intraBD = intraBD;
	}

}
