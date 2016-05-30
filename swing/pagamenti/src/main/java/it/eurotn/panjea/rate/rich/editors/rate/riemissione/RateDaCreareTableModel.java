package it.eurotn.panjea.rate.rich.editors.rate.riemissione;

import it.eurotn.panjea.rate.domain.Rata;
import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.panjea.tesoreria.util.RataRiemessa;
import it.eurotn.rich.control.table.DefaultBeanEditableTableModel;

import java.math.BigDecimal;

import com.jidesoft.converter.ConverterContext;
import com.jidesoft.grid.EditorContext;

public class RateDaCreareTableModel extends DefaultBeanEditableTableModel<Rata> {

	private static final long serialVersionUID = -7287802795957973191L;

	private RataRiemessa rataDaRiemettere;

	private static ConverterContext numberImportoContext;
	private static EditorContext numberImportoEditorContext;

	static {
		numberImportoContext = new NumberWithDecimalConverterContext();
		numberImportoContext.setUserObject(new Integer(2));
		numberImportoEditorContext = new EditorContext("DecimalNumberEditorContext", 2);
	}

	/**
	 * Costruttore.
	 *
	 */
	public RateDaCreareTableModel() {
		super("rateDaCreareTableModel", new String[] { "importo.importoInValuta", "dataScadenza", "tipoPagamento" },
				Rata.class);
		// cancello e inserisco di nuovo la riga per avere una rata con l'importo residuo della rata di partenza
	}

	@Override
	protected Rata createNewObject() {
		Rata newRata = new Rata();
		if (rataDaRiemettere != null) {
			// Calcolo l'importo con i dati che ho in tabella
			BigDecimal importoNuovaRata = rataDaRiemettere.getImporto().getImportoInValuta();
			importoNuovaRata = importoNuovaRata.subtract(rataDaRiemettere.getImportoRateRiemesse());
			for (Rata rata : source) {
				importoNuovaRata = importoNuovaRata.subtract(rata.getImporto().getImportoInValuta());
			}
			newRata.getImporto().setImportoInValuta(importoNuovaRata);
			newRata.getImporto().setImportoInValutaAzienda(importoNuovaRata);
			newRata.setTipoPagamento(rataDaRiemettere.getTipoPagamento());
		}
		return newRata;
	}

	@Override
	public ConverterContext getConverterContextAt(int row, int column) {
		switch (column) {
		case 0:
			return numberImportoContext;
		default:
			return super.getConverterContextAt(row, column);
		}
	}

	@Override
	public EditorContext getEditorContextAt(int row, int column) {
		switch (column) {
		case 0:
			return numberImportoEditorContext;
		default:
			return null;
		}
	}

	/**
	 *
	 * @return totale delle rate nel table model
	 */
	public BigDecimal getTotaleRate() {
		BigDecimal result = BigDecimal.ZERO;
		for (Rata rata : getObjects()) {
			if (rata.getImporto().getImportoInValuta() != null) {
				result = result.add(rata.getImporto().getImportoInValuta());
			}
		}
		return result;
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return true;
	}

	/**
	 *
	 * @param rata
	 *            rata da riemettere
	 */
	public void setRataDaRiemettere(RataRiemessa rata) {
		rataDaRiemettere = rata;
		setRows(rata.getRateDaCreare());
	}

	@Override
	public void setValueAt(Object value, int row, int column) {
		super.setValueAt(value, row, column);
		rataDaRiemettere.setRateDaCreare(getObjects());
		// se viene modificato un importo devo andare a ricalcolare quello proposto nella nuova riga
		BigDecimal importoRata = rataDaRiemettere.getImportoDaRiemettere();
		getElementAt(getObjects().size()).getImporto().setImportoInValutaAzienda(importoRata);
		getElementAt(getObjects().size()).getImporto().setImportoInValuta(importoRata);
	}
}
