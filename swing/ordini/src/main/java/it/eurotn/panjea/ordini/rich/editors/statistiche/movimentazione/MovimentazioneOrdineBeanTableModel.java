package it.eurotn.panjea.ordini.rich.editors.statistiche.movimentazione;

import it.eurotn.panjea.ordini.util.RigaMovimentazione;
import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

import com.jidesoft.converter.ConverterContext;

public class MovimentazioneOrdineBeanTableModel extends DefaultBeanTableModel<RigaMovimentazione> {

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
	public MovimentazioneOrdineBeanTableModel(final String id) {
		super(id, new String[] { "articoloLite", "depositoLite", "dataRegistrazione", "documento.dataDocumento",
				"documento.tipoDocumento", "documento.codice", "entitaDocumento", "quantita", "quantitaEvasa",
				"rimanenza", "prezzoUnitario", "prezzoNetto", "prezzoTotale", "importoRimanenza", "variazione",
				"noteRiga", "evasioneForzata", "tipoOmaggio", "dataConsegna", "dataOrdine", "modalitaRicezione", "numeroOrdineRicezione" }, RigaMovimentazione.class);
	}

	@Override
	public ConverterContext getConverterContextAt(int row, int column) {
		switch (column) {
		case 7:
		case 8:
		case 9:
			return NUMBERQTACONTEXT;
		case 10:
		case 11:
			return NUMBERPREZZOCONTEXT;
		case 12:
		case 13:
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
