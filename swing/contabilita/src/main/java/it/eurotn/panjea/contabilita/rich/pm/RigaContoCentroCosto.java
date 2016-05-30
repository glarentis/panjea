package it.eurotn.panjea.contabilita.rich.pm;

import it.eurotn.panjea.contabilita.domain.Conto.SottotipoConto;
import it.eurotn.panjea.contabilita.domain.Conto.TipoConto;
import it.eurotn.panjea.contabilita.util.SaldoConto;

import java.math.BigDecimal;

/**
 * Classe derivata da SottoContoDTO per presentare solo le informazioni del centro costo.
 * 
 * @author Leonardo
 */
public class RigaContoCentroCosto extends SaldoConto {

	private static final long serialVersionUID = -4456062065520004741L;

	/**
	 * Costruttore.
	 * 
	 * @param centroCostoCodice
	 *            centroCostoCodice
	 * @param centroCostoDescrizione
	 *            centroCostoDescrizione
	 * @param centroCostoId
	 *            centroCostoId
	 * @param tipoConto
	 *            tipoConto
	 * @param sottoTipoConto
	 *            sottoTipoConto
	 * @param importoDare
	 *            importoDare
	 * @param importoAvere
	 *            importoAvere
	 */
	public RigaContoCentroCosto(final String centroCostoCodice, final String centroCostoDescrizione,
			final Integer centroCostoId, final TipoConto tipoConto, final SottotipoConto sottoTipoConto,
			final BigDecimal importoDare, final BigDecimal importoAvere) {
		super();
		setMastroCodice(null);
		setMastroDescrizione(null);
		setMastroId(-1);
		setContoCodice(null);
		setContoDescrizione(null);
		setContoId(-1);
		setSottoContoCodice(null);
		setSottoContoDescrizione(null);
		setSottoContoId(-1);
		setCentroCostoCodice(centroCostoCodice);
		setCentroCostoDescrizione(centroCostoDescrizione);
		setCentroCostoId(centroCostoId);
		setTipoConto(tipoConto);
		setSottoTipoConto(sottoTipoConto);
		setImportoAvere(importoAvere);
		setImportoDare(importoDare);
	}
}