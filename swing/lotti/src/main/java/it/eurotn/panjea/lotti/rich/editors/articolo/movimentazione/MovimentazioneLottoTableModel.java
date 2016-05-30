package it.eurotn.panjea.lotti.rich.editors.articolo.movimentazione;

import it.eurotn.panjea.lotti.util.MovimentazioneLotto;
import it.eurotn.panjea.magazzino.domain.Articolo.TipoLotto;
import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

import com.jidesoft.converter.ConverterContext;

public class MovimentazioneLottoTableModel extends DefaultBeanTableModel<MovimentazioneLotto> {

	private static final long serialVersionUID = 2695988091024565866L;

	private TipoLotto tipoLotto;

	// gestisco 2 visualizzazioni diverse ( dipende dal flag di gestione lotti
	// interni, se è abilitato visualizzo il lotto interno, altrimenti lo tolgo
	// )
	private static String[] statisticheLottiColumns = new String[] { "documento.dataDocumento", "dataRegistrazione",
			"documento.tipoDocumento", "documento.codice", "documento.entitaDocumento", "tipoMovimento", "lotto",
			"quantitaCarico", "quantitaScarico", "deposito", "depositoDestinazione" };
	private static String[] statisticheLottiInterniColumns = new String[] { "documento.dataDocumento",
			"dataRegistrazione", "documento.tipoDocumento", "documento.codice", "documento.entitaDocumento",
			"tipoMovimento", "lotto", "lottoInterno", "quantitaCarico", "quantitaScarico", "deposito",
			"depositoDestinazione" };

	private static ConverterContext qtaContext;

	{
		qtaContext = new NumberWithDecimalConverterContext();
		qtaContext.setUserObject(2);
	}

	/**
	 * Costruttore.
	 * 
	 * @param tipoLotto
	 *            tipo lotto
	 */
	public MovimentazioneLottoTableModel(final TipoLotto tipoLotto) {
		super("movimentazioneLottoTableModel", (tipoLotto == TipoLotto.LOTTO_INTERNO) ? statisticheLottiInterniColumns
				: statisticheLottiColumns, MovimentazioneLotto.class);
		this.tipoLotto = tipoLotto;
	}

	@Override
	public ConverterContext getConverterContextAt(int row, int column) {

		if (tipoLotto == TipoLotto.LOTTO_INTERNO) {
			switch (column) {
			case 8:
			case 9:
				return qtaContext;
			default:
				return null;
			}
		} else {
			switch (column) {
			case 7:
			case 8:
				return qtaContext;
			default:
				return null;
			}
		}
	}

	/**
	 * 
	 * @param numeroDecimali
	 *            numeroDecimali
	 */
	public void setNumeroDecimaliQta(int numeroDecimali) {
		qtaContext.setUserObject(numeroDecimali);
	}

}
