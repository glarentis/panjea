package it.eurotn.panjea.contabilita.rich.pm;

import it.eurotn.panjea.contabilita.domain.Conto.SottotipoConto;
import it.eurotn.panjea.contabilita.domain.Conto.TipoConto;
import it.eurotn.panjea.contabilita.util.SaldoConto;

import java.math.BigDecimal;

/**
 * Classe derivata da SottoContoDTO per presentare solo le informazioni del conto.
 * 
 * @author Leonardo
 */
public class RigaConto extends SaldoConto {

	private static final long serialVersionUID = -4456062065520004741L;

	/**
	 * Costruttore.
	 * 
	 * @param codiceConto
	 *            codiceConto
	 * @param descrizioneConto
	 *            descrizioneConto
	 * @param contoId
	 *            contoId
	 * @param tipoConto
	 *            tipoConto
	 * @param sottoTipoConto
	 *            sottoTipoConto
	 * @param importoDare
	 *            importoDare
	 * @param importoAvere
	 *            importoAvere
	 */
	public RigaConto(final String codiceConto, final String descrizioneConto, final int contoId,
			final TipoConto tipoConto, final SottotipoConto sottoTipoConto, final BigDecimal importoDare,
			final BigDecimal importoAvere) {
		super();
		setMastroCodice(null);
		setMastroDescrizione(null);
		setMastroId(-1);
		setContoCodice(codiceConto);
		setContoDescrizione(descrizioneConto);
		setContoId(contoId);
		setSottoContoCodice(null);
		setSottoContoDescrizione(null);
		setSottoContoId(-1);
		setTipoConto(tipoConto);
		setSottoTipoConto(sottoTipoConto);
		setImportoAvere(importoAvere);
		setImportoDare(importoDare);
	}
}