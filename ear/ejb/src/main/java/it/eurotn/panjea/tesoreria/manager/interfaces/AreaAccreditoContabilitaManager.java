package it.eurotn.panjea.tesoreria.manager.interfaces;

import it.eurotn.panjea.tesoreria.domain.AreaAccredito;
import it.eurotn.panjea.tesoreria.domain.Effetto;

import java.util.List;

import javax.ejb.Local;

@Local
public interface AreaAccreditoContabilitaManager {

	/**
	 * Esegue la creazione dei documenti contabili degli accrediti.
	 * 
	 * @param areaAccredito
	 *            area accredito
	 * @param effetti
	 *            lista degli effetti di cui si compone l'area accredito
	 * @param scritturaPosticipata
	 *            scrittura posticipata
	 */
	void creaAreaContabileAccredito(AreaAccredito areaAccredito, List<Effetto> effetti, boolean scritturaPosticipata);

	/**
	 * Verifica l'effetto ritornando true se la scrittura risulta essere posticipata, false altrimenti.<br>
	 * La scrittura è posticipata se il tipo documento dell'area effetti non prevede tipo area contabile.
	 * 
	 * @param effetto
	 *            l'effetto da verificare
	 * @return true o false
	 */
	boolean isDopoIncasso(Effetto effetto);
}
