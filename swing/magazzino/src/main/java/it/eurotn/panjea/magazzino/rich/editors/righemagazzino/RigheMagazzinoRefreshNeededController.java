package it.eurotn.panjea.magazzino.rich.editors.righemagazzino;

import it.eurotn.panjea.magazzino.util.AreaMagazzinoFullDTO;
import it.eurotn.panjea.pagamenti.domain.CodicePagamento;

import java.math.BigDecimal;

public class RigheMagazzinoRefreshNeededController {
	private AreaMagazzinoFullDTO attuale;
	private AreaMagazzinoFullDTO precedente;

	/**
	 * Esegue il confronto tra due bigdecimal, utilizzanzo compareTo (ignora la precisione), trattando in maniera sicura
	 * valori null.
	 * 
	 * @param first
	 *            Primo bigdecimal
	 * @param second
	 *            Secondo bigdecimal
	 * @return vero se entrambi sono null oppure se equivalenti
	 */
	private boolean bigDecimalsComparesToEqualsZero(BigDecimal first, BigDecimal second) {
		if (first == null && second == null) {
			return true;
		}

		if (first != null && second != null) {
			return first.compareTo(second) == 0;
		}

		return false;
	}

	/**
	 * 
	 * @param nuova
	 *            L'areaMagazzinoFullDTO attualmente impostata come form model.
	 */
	public void cambiaAreaAttuale(AreaMagazzinoFullDTO nuova) {
		if (attuale == null || nuova.isNew() || !(nuova.getId().equals(attuale.getId()))) {
			precedente = null;
		} else {
			precedente = attuale;
		}

		attuale = nuova;
	}

	/**
	 * Controlla se le modifiche al codice di pagamento richiedono l'aggiornamento delle righe.
	 * 
	 * @return vero se le modifiche al codice pagamento richiedono l'aggiornamento delle righe.
	 */
	private boolean codicePagamentoRichiedeAggiornamentoRigheMagazzino() {
		if (attuale.getAreaRate() == null) {
			return false;
		}

		CodicePagamento codiceAttuale = attuale.getAreaRate().getCodicePagamento();
		CodicePagamento codicePrecedente = precedente.getAreaRate().getCodicePagamento();
		if (objectsEquals(codiceAttuale, codicePrecedente)) {
			return false;
		}

		BigDecimal percentualeScontoAttuale = codiceAttuale.getPercentualeScontoCommerciale();
		BigDecimal percentualeScontoPrecedente = codicePrecedente.getPercentualeScontoCommerciale();
		return !bigDecimalsComparesToEqualsZero(percentualeScontoAttuale, percentualeScontoPrecedente);
	}

	/**
	 * Esegue il confronto tra due oggetti, trattando in maniera sicura valori null. Non usare con i BigDecimal perché
	 * l'equals verifica, oltre al valore, anche la precisione (scale).
	 * 
	 * @param first
	 *            Primo oggetto
	 * @param second
	 *            Secondo oggetto
	 * @return vero se entrambi sono null oppure se equivalenti
	 */
	private boolean objectsEquals(Object first, Object second) {
		if (first == null && second == null) {
			return true;
		}

		if (first != null && second != null) {
			return first.equals(second);
		}

		return false;
	}

	/**
	 * Confronta l'areaMagazzinoFullDTO usata come form model e la versione precedente alla ricerca di modifiche che
	 * richiedano di aggiornare le righe visualizzate.
	 * 
	 * @return vero se le righe devono essere ricaricate.
	 */
	public boolean righeMagazzinoSonoDaRicaricare() {
		if (precedente == null) {
			return false;
		}

		return codicePagamentoRichiedeAggiornamentoRigheMagazzino();
	}

}
