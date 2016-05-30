package it.eurotn.panjea.tesoreria.manager.interfaces;

import it.eurotn.panjea.tesoreria.domain.AreaEffetti;
import it.eurotn.panjea.tesoreria.util.ParametriCreazioneAreaChiusure;

import javax.ejb.Local;

@Local
public interface AreaEffettiContabilitaManager {

	/**
	 * Esegue la creazione dei documenti contabili per gli effetti.
	 * 
	 * @param areaEffetti
	 *            area effetti
	 * @param parametriCreazioneAreaChiusure
	 *            .
	 */
	void creaAreaContabileEffetti(AreaEffetti areaEffetti, ParametriCreazioneAreaChiusure parametriCreazioneAreaChiusure);
}
