package it.eurotn.panjea.magazzino.rich.editors.statistiche.movimentazione;

import it.eurotn.panjea.magazzino.util.RigaMovimentazione;
import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

import com.jidesoft.converter.ConverterContext;

public class MovimentazioneBeanTableModel extends DefaultBeanTableModel<RigaMovimentazione> {

	private static final long serialVersionUID = 1124813834613364885L;

	private static final ConverterContext NUMBERQTACONTEXT = new NumberWithDecimalConverterContext();
	private static final ConverterContext NUMBERPREZZOCONTEXT = new NumberWithDecimalConverterContext();

	{
		NUMBERPREZZOCONTEXT.setUserObject(6);
		NUMBERQTACONTEXT.setUserObject(6);
	}

	/**
	 * Costruttore.
	 * 
	 * @param id
	 *            id del tableModel
	 */
	public MovimentazioneBeanTableModel(final String id) {
		super(id, new String[] { "categoriaLite", "articoloLite", "depositoLite", "dataRegistrazione",
				"documento.dataDocumento", "documento.tipoDocumento", "documento.codice", "entitaDocumento",
				"qtaInventario", "qtaMagazzinoCaricoTotale", "qtaMagazzinoScaricoTotale", "qtaMovimentata",
				"prezzoMedio", "prezzoUnitario", "prezzoNetto", "importoCarico", "importoScarico", "variazione",
				"importoMovimentatoCostoMedio", "noteRiga", "importoProvvigione", "sedeEntita",
				"articoloLite.unitaMisura", "descrizioneRiga", "tipoOmaggio" }, RigaMovimentazione.class);
	}

	@Override
	public ConverterContext getConverterContextAt(int row, int column) {
		switch (column) {
		case 7:
		case 8:
		case 9:
		case 10:
		case 11:
			return NUMBERQTACONTEXT;
		case 12:
		case 13:
		case 14:
		case 15:
		case 16:
		case 18:
		case 20:
			return NUMBERPREZZOCONTEXT;
		default:
			return null;
		}
	}

	/**
	 * @param numeroDecimaliPrezzo
	 *            the numeroDecimaliPrezzo to set
	 */
	public void setNumeroDecimaliPrezzo(int numeroDecimaliPrezzo) {
		// NUMBERPREZZOCONTEXT.setUserObject(new Integer(numeroDecimaliPrezzo));
	}

	/**
	 * @param numeroDecimaliQta
	 *            the numeroDecimaliQta to set
	 */
	public void setNumeroDecimaliQta(int numeroDecimaliQta) {
		// NUMBERQTACONTEXT.setUserObject(new Integer(numeroDecimaliQta));
	}
}
