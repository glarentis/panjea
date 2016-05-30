package it.eurotn.panjea.tesoreria.rich.editors.ricercaeffetti;

import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.panjea.tesoreria.rich.editors.ricercaeffetti.EffettiLayoutManager.Mode;
import it.eurotn.panjea.tesoreria.util.SituazioneEffetto;
import it.eurotn.panjea.tesoreria.util.SituazioneEffetto.StatoCarrello;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

import com.jidesoft.converter.ConverterContext;
import com.jidesoft.grid.EditorContext;

public class RisultatiRicercaEffettiTableModel extends DefaultBeanTableModel<SituazioneEffetto> {

	private static final long serialVersionUID = 778750647980049579L;

	private Mode mode;

	private static ConverterContext numberPrezzoContext;

	static {
		numberPrezzoContext = new NumberWithDecimalConverterContext();
		numberPrezzoContext.setUserObject(new Integer(2));
	}

	/**
	 * 
	 * @param modelId
	 *            id per il modello
	 */
	public RisultatiRicercaEffettiTableModel(final String modelId) {
		super(modelId, new String[] { "dataValutaEffetto", "rapportoBancario", "areaEffetto.documento", "entita",
				"effetto.statoEffetto", "dataScadenza", "effetto.importo.importoInValutaAzienda" },
				SituazioneEffetto.class);
		mode = Mode.GENERALE;
	}

	@Override
	public ConverterContext getConverterContextAt(int row, int column) {
		switch (column) {
		case 6:
			return numberPrezzoContext;
		default:
			return null;
		}
	}

	@Override
	public EditorContext getEditorContextAt(int row, int column) {
		if (row == -1) {
			return null;
		}
		switch (column) {
		case 0:
			return null;
		case 4:
			switch (mode) {
			case INSOLUTI:
			case ACCREDITI:
			case ANTICIPI:
				SituazioneEffetto situazioneEffetto = getObject(row);
				return new EditorContext("dataValutaEditorContext", situazioneEffetto);
			default:
				return null;
			}
		default:
			return super.getEditorContextAt(row, column);
		}
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}

	@Override
	protected Object setColumnValue(Object row, Object value, int column) {
		// se la colonna è quella dello stato effetto ritorno null perchè viene usata per la
		// selezione del carrello ma non deve modificare lo stato
		if (column == 4) {
			return null;
		} else {
			return super.setColumnValue(row, value, column);
		}
	}

	/**
	 * @param mode
	 *            the mode to set
	 */
	public void setMode(Mode mode) {
		this.mode = mode;

		// resetto gli stati carrello degli effetti che ho
		for (SituazioneEffetto situazioneEffetto : getObjects()) {
			situazioneEffetto.setStatoCarrello(StatoCarrello.SELEZIONABILE);
		}
	}
}
