package it.eurotn.panjea.lotti.exception;

import it.eurotn.panjea.ordini.domain.documento.evasione.RigaDistintaCarico;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * Rilanciata quando si cerca di evadere una rigaDistintaCarico senza righeDistintaCaricoLotto (se sono obbligatorie).
 * 
 * @author giangi
 * @version 1.0, 03/lug/2013
 * 
 */
public class EvasioneLottiException extends Exception {

	private static final long serialVersionUID = -6313093713887083588L;

	private Map<RigaDistintaCarico, LottiException> eccezioni;

	{
		eccezioni = new HashMap<RigaDistintaCarico, LottiException>();
	}

	/**
	 * Costruttore.
	 */
	public EvasioneLottiException() {
		super();
	}

	/**
	 * 
	 * @param rigaDistintaCarico
	 *            riga distinta carico che ha generato l'eccezione
	 * @param lottiException
	 *            eccezione generata.
	 */
	public void addException(RigaDistintaCarico rigaDistintaCarico, LottiException lottiException) {
		eccezioni.put(rigaDistintaCarico, lottiException);
	}

	/**
	 * 
	 * @return lista di eccezioni
	 */
	public Map<RigaDistintaCarico, LottiException> getEccezioni() {
		return eccezioni;
	}

	/**
	 * 
	 * @return true se non ho eccezioni
	 */
	public boolean isEmpty() {
		return eccezioni.isEmpty();
	}
}
