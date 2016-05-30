package it.eurotn.panjea.magazzino.rich.editors.statistiche.movimentazione;

import it.eurotn.panjea.magazzino.util.RigaMovimentazione;
import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

import com.jidesoft.converter.ConverterContext;

public class MovimentazioneArticoloBeanTableModel extends DefaultBeanTableModel<RigaMovimentazione> {

	private static final long serialVersionUID = 1124813834613364885L;

	private static final ConverterContext NUMBERQTACONTEXT = new NumberWithDecimalConverterContext();
	private static final ConverterContext NUMBERPREZZOCONTEXT = new NumberWithDecimalConverterContext();

	/**
	 * Costruttore.
	 * 
	 * @param id
	 *            id del tableModel
	 */
	public MovimentazioneArticoloBeanTableModel(final String id) {
		super(id, new String[] { "dataRegistrazione", "documento.dataDocumento", "documento.tipoDocumento",
				"documento.codice", "entitaDocumento", "qtaMagazzinoCaricoTotale", "qtaMagazzinoScaricoTotale",
				"prezzoUnitario", "prezzoNetto", "prezzoTotale", "giacenzaProgressiva", "sedeEntita" },
				RigaMovimentazione.class);
	}

	@Override
	public ConverterContext getConverterContextAt(int row, int column) {
		switch (column) {
		case 5:
		case 6:
			return NUMBERQTACONTEXT;
		case 7:
		case 8:
		case 9:
			return NUMBERPREZZOCONTEXT;
		case 10:
			return NUMBERQTACONTEXT;
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
