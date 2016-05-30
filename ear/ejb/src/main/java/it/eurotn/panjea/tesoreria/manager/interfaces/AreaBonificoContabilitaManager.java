package it.eurotn.panjea.tesoreria.manager.interfaces;

import it.eurotn.panjea.contabilita.service.exception.ContoRapportoBancarioAssenteException;
import it.eurotn.panjea.tesoreria.domain.AreaBonifico;

import javax.ejb.Local;

@Local
public interface AreaBonificoContabilitaManager {

	/**
	 * Esegue la conferma del bonifico creandone la registrazione contabile.
	 * 
	 * @param areaBonifico
	 *            bonifico
	 * @throws ContoRapportoBancarioAssenteException
	 *             eccezione
	 */
	void creaAreaContabileBonifico(AreaBonifico areaBonifico) throws ContoRapportoBancarioAssenteException;

}
