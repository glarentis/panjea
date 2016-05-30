package it.eurotn.panjea.magazzino.rich.editors.statistiche.indicerotazione;

import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoDocumentoBD;
import it.eurotn.panjea.magazzino.util.IndiceGiacenzaArticolo;
import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriCalcoloIndiciRotazioneGiacenza;
import it.eurotn.rich.editors.AbstractTablePageEditor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RisultatiIndiceRotazioneTablePage extends
		AbstractTablePageEditor<IndiceGiacenzaArticolo> {

	private ParametriCalcoloIndiciRotazioneGiacenza parametri;

	private IMagazzinoDocumentoBD magazzinoDocumentoBD;

	protected RisultatiIndiceRotazioneTablePage() {
		super("risultatiIndiceRotazioneTablePage",
				new RisultatiIndiceRotazioneTableModel());
	}

	/**
	 * @return Returns the magazzinoDocumentoBD.
	 */
	public IMagazzinoDocumentoBD getMagazzinoDocumentoBD() {
		return magazzinoDocumentoBD;
	}

	@Override
	public Collection<IndiceGiacenzaArticolo> loadTableData() {
		List<IndiceGiacenzaArticolo> result = new ArrayList<IndiceGiacenzaArticolo>();
		if (parametri.isEffettuaRicerca()) {
			result = magazzinoDocumentoBD.calcolaIndiciRotazione(parametri);
		}
		return result;
	}

	@Override
	public void onPostPageOpen() {

	}

	@Override
	public Collection<IndiceGiacenzaArticolo> refreshTableData() {
		return loadTableData();
	}

	@Override
	public void setFormObject(Object object) {
		parametri = (ParametriCalcoloIndiciRotazioneGiacenza) object;
	}

	/**
	 * @param magazzinoDocumentoBD
	 *            The magazzinoDocumentoBD to set.
	 */
	public void setMagazzinoDocumentoBD(
			IMagazzinoDocumentoBD magazzinoDocumentoBD) {
		this.magazzinoDocumentoBD = magazzinoDocumentoBD;
	}
}
