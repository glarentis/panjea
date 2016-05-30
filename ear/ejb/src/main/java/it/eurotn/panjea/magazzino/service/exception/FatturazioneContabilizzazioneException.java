package it.eurotn.panjea.magazzino.service.exception;

import it.eurotn.panjea.magazzino.domain.DatiGenerazione;

public class FatturazioneContabilizzazioneException extends Exception {

	private static final long serialVersionUID = -8910028053805977500L;

	private DatiGenerazione datiGenerazione;

	private ContabilizzazioneException contabilizzazioneException;

	/**
	 * Costruttore.
	 * 
	 * @param datiGenerazione
	 *            dati di generazione della fatturazione
	 * @param contabilizzazioneException
	 *            errore di contabilizzazione
	 */
	public FatturazioneContabilizzazioneException(final DatiGenerazione datiGenerazione,
			final ContabilizzazioneException contabilizzazioneException) {
		super();
		this.datiGenerazione = datiGenerazione;
		this.contabilizzazioneException = contabilizzazioneException;
	}

	/**
	 * @return Returns the contabilizzazioneException.
	 */
	public ContabilizzazioneException getContabilizzazioneException() {
		return contabilizzazioneException;
	}

	/**
	 * @return Returns the datiGenerazione.
	 */
	public DatiGenerazione getDatiGenerazione() {
		return datiGenerazione;
	}

}
