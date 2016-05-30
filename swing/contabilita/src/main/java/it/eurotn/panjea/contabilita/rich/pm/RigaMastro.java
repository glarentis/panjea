package it.eurotn.panjea.contabilita.rich.pm;

import it.eurotn.panjea.contabilita.domain.Conto.SottotipoConto;
import it.eurotn.panjea.contabilita.domain.Conto.TipoConto;
import it.eurotn.panjea.contabilita.util.SaldoConto;

import java.math.BigDecimal;

/**
 * Classe derivata da SottoContoDTO per presentare solo le informazioni del mastro.
 *
 * @author Leonardo
 */
public class RigaMastro extends SaldoConto {

	private static final long serialVersionUID = -8656752912800657124L;

	/**
	 * Costruttore.
	 *
	 * @param codiceMastro
	 *            codiceMastro
	 * @param descrizioneMastro
	 *            descrizioneMastro
	 * @param idMastro
	 *            idMastro
	 * @param tipoConto
	 *            tipoConto
	 * @param sottoTipoConto
	 *            sottoTipoConto
	 * @param importoDare
	 *            importoDare
	 * @param importoAvere
	 *            importoAvere
	 */
	public RigaMastro(final String codiceMastro, final String descrizioneMastro, final int idMastro,
			final TipoConto tipoConto, final SottotipoConto sottoTipoConto, final BigDecimal importoDare,
			final BigDecimal importoAvere) {
		super();
		setMastroCodice(codiceMastro);
		setMastroDescrizione(descrizioneMastro);
		setMastroId(idMastro);
		setContoCodice(null);
		setContoDescrizione(null);
		setContoId(-1);
		setSottoContoCodice(null);
		setSottoContoDescrizione(null);
		setSottoContoId(-1);
		setTipoConto(tipoConto);
		setSottoTipoConto(sottoTipoConto);
		setImportoDare(importoDare);
		setImportoAvere(importoAvere);
	}
}