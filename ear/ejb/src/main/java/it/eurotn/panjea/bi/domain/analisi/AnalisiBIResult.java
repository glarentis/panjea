package it.eurotn.panjea.bi.domain.analisi;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class AnalisiBIResult implements Serializable {
	private static final long serialVersionUID = 1L;

	private List<Object[]> dati;

	private int numDecimaliQtaMax;

	private Map<Integer, Integer> modelIndexToResultColumn;

	/**
	 *
	 * @param dati
	 *            dati con il risultato dell'analisi
	 * @param numDecimaliQtaMax
	 *            numDecimali da applicare alla qta.
	 * @param modelIndexToResultColumn
	 *            in chiave ho l'indice della colonna nel modello di business come valore è la posizione enll'array dei
	 *            risultati
	 */
	public AnalisiBIResult(final List<Object[]> dati, final Map<Integer, Integer> modelIndexToResultColumn,
			final int numDecimaliQtaMax) {
		super();
		this.dati = dati;
		this.modelIndexToResultColumn = modelIndexToResultColumn;
		this.numDecimaliQtaMax = numDecimaliQtaMax;
	}

	/**
	 * @return Returns the numDecimaliQtaMax.
	 */
	public int getNumDecimaliQtaMax() {
		return numDecimaliQtaMax;
	}

	/**
	 *
	 * @return numero di record presenti nel modello
	 */
	public int getNumRecord() {
		return dati.size();
	}

	/**
	 *
	 * @return righe contenenti i risultati dell'interrogazione
	 */
	public List<Object[]> getRows() {
		return dati;
	}

	/**
	 * Valore contenuto nel modello per la colonna e l'indice<br/>
	 * .
	 *
	 * @param rowIndex
	 *            indice riga
	 * @param columnIndex
	 *            indica colonna
	 * @return valore nella "cella"
	 */
	public Object getValueAt(int rowIndex, int columnIndex) {
		Object result = null;
		try {
			Integer datiIndex = modelIndexToResultColumn.get(columnIndex);
			if (datiIndex == null) {
				return "";
			}
			result = dati.get(rowIndex)[datiIndex];
		} catch (Exception e) {
			// Per problemi di thread posso avere il datamodel "sporco", ad esempio la lista dei dati vuota ed il numero
			// di righe con
			// il data model vecchio. per adesso non me ne curo
			System.err.println(e);
			System.out.println("DEBUG:AnalisiBIResult->getValueAt: " + rowIndex + "," + dati.size());
		}
		return result;
	}
}
