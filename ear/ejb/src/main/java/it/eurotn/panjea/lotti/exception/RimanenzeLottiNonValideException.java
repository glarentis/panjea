package it.eurotn.panjea.lotti.exception;

import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.lotti.domain.Lotto;
import it.eurotn.panjea.lotti.domain.RigaLotto;
import it.eurotn.panjea.magazzino.domain.RigaArticolo;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;

import java.util.ArrayList;
import java.util.List;

public class RimanenzeLottiNonValideException extends RuntimeException {

	private static final long serialVersionUID = -5683287835961456258L;

	private List<RimanenzaLottiNonValidaException> rimanenzaLottiNonValidaExceptions;

	{
		rimanenzaLottiNonValidaExceptions = new ArrayList<RimanenzaLottiNonValidaException>();
	}

	/**
	 * Costruttore.
	 */
	public RimanenzeLottiNonValideException() {
		super();
	}

	/**
	 * Aggiunge una rimanenza non valida alla lista.
	 * 
	 * @param exception
	 *            eccezione da aggiungere
	 */
	public void addRimanenzaNonValida(RimanenzaLottiNonValidaException exception) {
		rimanenzaLottiNonValidaExceptions.add(exception);
	}

	/**
	 * @return Returns the rimanenzeLottoNonValide.
	 */
	public List<RimanenzaLottoNonValida> getRimanenzeLottoNonValide() {

		List<RimanenzaLottoNonValida> rimanenzeLottoNonValide = new ArrayList<RimanenzaLottoNonValida>();

		RimanenzaLottoNonValida rimanenzaLottoNonValida = null;
		for (RimanenzaLottiNonValidaException rimanenzaException : rimanenzaLottiNonValidaExceptions) {
			Double qta = null;
			DepositoLite depositoOrigine = null;
			RigaArticolo rigaArticolo = rimanenzaException.getRigaArticolo();

			if (rigaArticolo != null) {
				qta = rigaArticolo.getQta();
				AreaMagazzino areaMagazzino = rigaArticolo.getAreaMagazzino();
				if (areaMagazzino != null) {
					depositoOrigine = areaMagazzino.getDepositoOrigine();
				}
			}
			if (!rimanenzaException.getLotti().isEmpty()) {
				for (Lotto lotto : rimanenzaException.getLotti()) {
					rimanenzaLottoNonValida = new RimanenzaLottoNonValida(lotto.getArticolo(), depositoOrigine, lotto,
							lotto.getRimanenza(), qta);
					rimanenzeLottoNonValide.add(rimanenzaLottoNonValida);
				}
			} else if (!rimanenzaException.getRigheLotto().isEmpty()) {
				for (RigaLotto rigaLotto : rimanenzaException.getRigheLotto()) {
					rimanenzaLottoNonValida = new RimanenzaLottoNonValida(rigaLotto.getLotto().getArticolo(),
							depositoOrigine, rigaLotto.getLotto(), rigaLotto.getQuantita(), qta);
					rimanenzeLottoNonValide.add(rimanenzaLottoNonValida);
				}
			} else {
				rimanenzaLottoNonValida = new RimanenzaLottoNonValida(rimanenzaException.getRigaArticolo()
						.getArticolo(), depositoOrigine, null, null, qta);
				rimanenzeLottoNonValide.add(rimanenzaLottoNonValida);
			}

		}

		return rimanenzeLottoNonValide;
	}

	/**
	 * @return <code>true</code> ee la lista delle rimanenze lotti è vuota.
	 */
	public boolean isEmpty() {
		return rimanenzaLottiNonValidaExceptions.isEmpty();
	}
}
