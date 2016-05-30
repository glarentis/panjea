package it.eurotn.panjea.magazzino.rich.editors.statistiche.valorizzazione;

import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaBD;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoDocumentoBD;
import it.eurotn.panjea.magazzino.util.ParametriRicercaValorizzazione;
import it.eurotn.panjea.magazzino.util.ValorizzazioneArticolo;
import it.eurotn.rich.editors.AbstractTablePageEditor;

import java.util.List;

public class RisultatiRicercaValorizzazioneMagazzinoTablePage extends AbstractTablePageEditor<ValorizzazioneArticolo> {

	public static final String PAGE_ID = "risultatiRicercaValorizzazioneMagazzinoTablePage";

	private ParametriRicercaValorizzazione parametriRicercaValorizzazione;

	private IMagazzinoDocumentoBD magazzinoDocumentoBD;
	private IAnagraficaBD anagraficaBD;

	private final String[] categoriaAggregateColumns = new String[] { "categoria", "deposito" };

	/**
	 * Costruttore.
	 */
	protected RisultatiRicercaValorizzazioneMagazzinoTablePage() {
		super(PAGE_ID, new ValorizzazioneTableModel());
		getTable().setAggregatedColumns(categoriaAggregateColumns);
	}

	@Override
	public List<ValorizzazioneArticolo> loadTableData() {
		List<ValorizzazioneArticolo> righe = null;
		if (parametriRicercaValorizzazione.isEffettuaRicerca()) {
			setRows(magazzinoDocumentoBD.caricaValorizzazione(parametriRicercaValorizzazione));
		}
		return righe;
	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	@Override
	public List<ValorizzazioneArticolo> refreshTableData() {
		return loadTableData();
	}

	/**
	 * @param anagraficaBD
	 *            the anagraficaBD to set
	 */
	public void setAnagraficaBD(IAnagraficaBD anagraficaBD) {
		this.anagraficaBD = anagraficaBD;
	}

	@Override
	public void setFormObject(Object object) {
		this.parametriRicercaValorizzazione = (ParametriRicercaValorizzazione) object;
	}

	/**
	 * @param magazzinoDocumentoBD
	 *            the magazzinoDocumentoBD to set
	 */
	public void setMagazzinoDocumentoBD(IMagazzinoDocumentoBD magazzinoDocumentoBD) {
		this.magazzinoDocumentoBD = magazzinoDocumentoBD;
	}

}
