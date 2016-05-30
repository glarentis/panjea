package it.eurotn.panjea.tesoreria.manager.interfaces;

import it.eurotn.panjea.contabilita.service.exception.ContoRapportoBancarioAssenteException;
import it.eurotn.panjea.tesoreria.domain.AreaDistintaBancaria;

import javax.ejb.Local;

@Local
public interface AreaDistintaBancariaContabilitaManager {

	/**
	 * esegue la conferma della distinta bancaria creandone la registrazione contabile.
	 * 
	 * @param areaDistintaBancaria distinta bancaria
	 * @throws ContoRapportoBancarioAssenteException eccezione
	 */
	void creaAreaContabileDistintaBancaria(AreaDistintaBancaria areaDistintaBancaria)
			throws ContoRapportoBancarioAssenteException;

}
