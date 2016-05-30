package it.eurotn.panjea.preventivi.rich.editors.statistiche.movimentazione;

import it.eurotn.panjea.preventivi.util.RigaMovimentazione;
import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

import com.jidesoft.converter.ConverterContext;

public class MovimentazionePreventivoBeanTableModel extends DefaultBeanTableModel<RigaMovimentazione> {

	private static final long serialVersionUID = 1124813834613364885L;

	private static final ConverterContext NUMBERQTACONTEXT = new NumberWithDecimalConverterContext();
	private static final ConverterContext NUMBERPREZZOCONTEXT = new NumberWithDecimalConverterContext();
	private static final ConverterContext NUMBERPREZZOTOTALECONTEXT = new NumberWithDecimalConverterContext(2);

	/**
	 * Costruttore.
	 * 
	 * @param id
	 *            id del tableModel
	 */
	public MovimentazionePreventivoBeanTableModel(final String id) {
		super(id, new String[] { "articoloLite", "dataRegistrazione", "documento.dataDocumento",
				"documento.tipoDocumento", "documento.codice", "entitaDocumento", "quantita", "quantitaEvasa",
				"rimanenza", "prezzoUnitario", "prezzoNetto", "prezzoTotale", "importoRimanenza", "variazione",
				"noteRiga", "tipoOmaggio" }, RigaMovimentazione.class);
	}

	@Override
	public ConverterContext getConverterContextAt(int row, int column) {
		switch (column) {
		case 6:
		case 7:
		case 8:
			return NUMBERQTACONTEXT;
		case 9:
		case 10:
			return NUMBERPREZZOCONTEXT;
		case 11:
		case 12:
			return NUMBERPREZZOTOTALECONTEXT;
		default:
			return null;
		}
	}

	/**
	 * @param numeroDecimaliPrezzo
	 *            the numeroDecimaliPrezzo to set
	 */
	public void setNumeroDecimaliPrezzo(int numeroDecimaliPrezzo) {
		NUMBERPREZZOCONTEXT.setUserObject(new Integer(numeroDecimaliPrezzo));
	}

	/**
	 * @param numeroDecimaliQta
	 *            the numeroDecimaliQta to set
	 */
	public void setNumeroDecimaliQta(int numeroDecimaliQta) {
		NUMBERQTACONTEXT.setUserObject(new Integer(numeroDecimaliQta));
	}
}
