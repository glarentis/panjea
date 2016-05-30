package it.eurotn.panjea.cauzioni.rich.editors.entita.situazionecauzioni;

import it.eurotn.panjea.cauzioni.util.parametriricerca.MovimentazioneCauzioneDTO;
import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

import com.jidesoft.converter.ConverterContext;

public class MovimentazioneCauzioniTableModel extends DefaultBeanTableModel<MovimentazioneCauzioneDTO> {

	private static final long serialVersionUID = 3573772139677406763L;

	private final ConverterContext totaleContext = new NumberWithDecimalConverterContext();

	/**
	 * Costruttore.
	 */
	public MovimentazioneCauzioniTableModel() {
		this("movimentazioneCauzioniTableModel");
	}

	/**
	 * Costruttore.
	 * 
	 * @param modelId
	 *            id modello
	 */
	public MovimentazioneCauzioniTableModel(final String modelId) {
		super(modelId, new String[] { "sedeEntita", "dataRegistrazione", "dataDocumento", "tipoDocumento",
				"numeroDocumento", "dati", "resi", "importoDati", "importoResi" }, MovimentazioneCauzioneDTO.class);
	}

	@Override
	public ConverterContext getConverterContextAt(int row, int j) {
		MovimentazioneCauzioneDTO movimentazioneDTO = getElementAt(row);

		switch (j) {
		case 5:
		case 6:
			totaleContext.setUserObject(movimentazioneDTO.getNumeroDecimaliQta());
			return totaleContext;
		case 7:
		case 8:
			totaleContext.setUserObject(6);
			return totaleContext;
		default:
			return null;
		}
	}

}