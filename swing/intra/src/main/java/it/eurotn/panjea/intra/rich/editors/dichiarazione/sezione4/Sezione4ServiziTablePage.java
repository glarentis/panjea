package it.eurotn.panjea.intra.rich.editors.dichiarazione.sezione4;

import it.eurotn.panjea.intra.domain.DichiarazioneIntra;
import it.eurotn.panjea.intra.domain.RigaSezione4Intra;
import it.eurotn.panjea.intra.rich.bd.IIntraBD;
import it.eurotn.rich.editors.AbstractTablePageEditor;
import it.eurotn.rich.editors.table.EditFrame;

import java.util.Collection;

public class Sezione4ServiziTablePage extends AbstractTablePageEditor<RigaSezione4Intra> {

	private DichiarazioneIntra dichiarazioneIntra;
	private IIntraBD intraBD;

	/**
	 * Costruttore.
	 */
	public Sezione4ServiziTablePage() {
		super("sezione4ServiziTablePage", new String[] { "progressivo", "fornitoreStato", "fornitorepiva", "importo",
				"importoInValuta", "numeroFattura", "dataFattura", "servizio", "modalitaErogazione", "modalitaIncasso",
				"paesePagamento" }, RigaSezione4Intra.class);
	}

	/**
	 * @return Returns the intraBD.
	 */
	public IIntraBD getIntraBD() {
		return intraBD;
	}

	@Override
	public Collection<RigaSezione4Intra> loadTableData() {
		return intraBD.caricaRigheSezioniDichiarazione(dichiarazioneIntra, RigaSezione4Intra.class);
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
	public Collection<RigaSezione4Intra> refreshTableData() {
		return loadTableData();
	}

	@Override
	public void setFormObject(Object object) {
		dichiarazioneIntra = (DichiarazioneIntra) object;
		Sezione4ServiziPage sezione1AcquistiBenePage = (Sezione4ServiziPage) getEditPages().get(
				EditFrame.DEFAULT_OBJECT_CLASS_NAME);
		sezione1AcquistiBenePage.setDichiarazioneIntra(dichiarazioneIntra);
	}

	/**
	 * @param intraBD
	 *            The intraBD to set.
	 */
	public void setIntraBD(IIntraBD intraBD) {
		this.intraBD = intraBD;
	}

}
