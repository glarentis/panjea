package it.eurotn.panjea.intra.rich.editors.dichiarazione.sezione2;

import it.eurotn.panjea.intra.domain.DichiarazioneIntra;
import it.eurotn.panjea.intra.domain.RigaSezione2Intra;
import it.eurotn.panjea.intra.rich.bd.IIntraBD;
import it.eurotn.rich.editors.AbstractTablePageEditor;
import it.eurotn.rich.editors.table.EditFrame;

import java.util.Collection;

public class Sezione2BeniTablePage extends AbstractTablePageEditor<RigaSezione2Intra> {

	private DichiarazioneIntra dichiarazioneIntra;
	private IIntraBD intraBD;

	/**
	 * Costruttore.
	 */
	public Sezione2BeniTablePage() {
		super("sezione2BeniTablePage", new String[] { "progressivo", "fornitoreStato", "fornitorepiva", "importo",
				"importoInValuta", "naturaTransazione", "mese", "trimestre", "anno", "segno", "nomenclatura",
				"valoreStatisticoEuro" }, RigaSezione2Intra.class);
	}

	/**
	 * @return Returns the intraBD.
	 */
	public IIntraBD getIntraBD() {
		return intraBD;
	}

	@Override
	public Collection<RigaSezione2Intra> loadTableData() {
		return intraBD.caricaRigheSezioniDichiarazione(dichiarazioneIntra, RigaSezione2Intra.class);
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
	public Collection<RigaSezione2Intra> refreshTableData() {
		return loadTableData();
	}

	@Override
	public void setFormObject(Object object) {
		dichiarazioneIntra = (DichiarazioneIntra) object;
		Sezione2BeniPage sezione2AcquistiBenePage = (Sezione2BeniPage) getEditPages().get(
				EditFrame.DEFAULT_OBJECT_CLASS_NAME);
		sezione2AcquistiBenePage.setDichiarazioneIntra(dichiarazioneIntra);
	}

	/**
	 * @param intraBD
	 *            The intraBD to set.
	 */
	public void setIntraBD(IIntraBD intraBD) {
		this.intraBD = intraBD;
	}

}
