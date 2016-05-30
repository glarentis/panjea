package it.eurotn.panjea.intra.rich.editors.dichiarazione.sezione1;

import it.eurotn.panjea.intra.domain.DichiarazioneIntra;
import it.eurotn.panjea.intra.domain.RigaSezione1Intra;
import it.eurotn.panjea.intra.rich.bd.IIntraBD;
import it.eurotn.rich.editors.AbstractTablePageEditor;
import it.eurotn.rich.editors.table.EditFrame;

import java.util.Collection;

public class Sezione1BeniTablePage extends AbstractTablePageEditor<RigaSezione1Intra> {

	private DichiarazioneIntra dichiarazioneIntra;
	private IIntraBD intraBD;

	/**
	 * Costruttore.
	 */
	public Sezione1BeniTablePage() {
		super("sezione1BeniTablePage", new String[] { "progressivo", "fornitoreStato", "fornitorepiva", "importo",
				"importoInValuta", "naturaTransazione", "nomenclatura", "massaNetta", "um", "massaSupplementare",
				"nomenclatura.umsupplementare", "valoreStatisticoEuro", "gruppoCondizioneConsegna",
				"modalitaTrasporto", "paese", "provincia", "paeseOrigineArticolo" }, RigaSezione1Intra.class);
	}

	/**
	 * @return Returns the intraBD.
	 */
	public IIntraBD getIntraBD() {
		return intraBD;
	}

	@Override
	public Collection<RigaSezione1Intra> loadTableData() {
		return intraBD.caricaRigheSezioniDichiarazione(dichiarazioneIntra, RigaSezione1Intra.class);
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
	public Collection<RigaSezione1Intra> refreshTableData() {
		return loadTableData();
	}

	@Override
	public void setFormObject(Object object) {
		dichiarazioneIntra = (DichiarazioneIntra) object;
		Sezione1BeniPage sezione1AcquistiBenePage = (Sezione1BeniPage) getEditPages().get(
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
