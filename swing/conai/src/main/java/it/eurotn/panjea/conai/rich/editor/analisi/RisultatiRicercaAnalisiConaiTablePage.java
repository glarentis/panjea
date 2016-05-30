package it.eurotn.panjea.conai.rich.editor.analisi;

import it.eurotn.panjea.conai.rich.bd.IConaiBD;
import it.eurotn.panjea.conai.util.AnalisiConaiDTO;
import it.eurotn.panjea.conai.util.parametriricerca.ParametriRicercaAnalisi;
import it.eurotn.rich.editors.AbstractTablePageEditor;

import java.util.ArrayList;
import java.util.List;

public class RisultatiRicercaAnalisiConaiTablePage extends AbstractTablePageEditor<AnalisiConaiDTO> {

	public static final String PAGE_ID = "risultatiRicercaAnalisiConaiTablePage";

	private IConaiBD conaiBD;

	private ParametriRicercaAnalisi parametriRicerca;

	/**
	 * Costruttore.
	 */
	protected RisultatiRicercaAnalisiConaiTablePage() {
		super(PAGE_ID, new RisultatiRicercaAnalisiConaiTableModel());
	}

	@Override
	public List<AnalisiConaiDTO> loadTableData() {
		return null;
	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public List<AnalisiConaiDTO> refreshTableData() {
		List<AnalisiConaiDTO> analisi = new ArrayList<AnalisiConaiDTO>();

		if (parametriRicerca.isEffettuaRicerca()) {
			analisi = conaiBD.caricaAnalisiConali(parametriRicerca);
		}

		return analisi;
	}

	/**
	 * @param conaiBD
	 *            The conaiBD to set.
	 */
	public void setConaiBD(IConaiBD conaiBD) {
		this.conaiBD = conaiBD;
	}

	@Override
	public void setFormObject(Object object) {
		if (object instanceof ParametriRicercaAnalisi) {
			this.parametriRicerca = (ParametriRicercaAnalisi) object;
		} else {
			this.parametriRicerca = new ParametriRicercaAnalisi();
		}
		// NPE da mail. controllo il null
		if (getTable() != null) {
			getTable().setTableHeader(parametriRicerca);
		}
	}

}
