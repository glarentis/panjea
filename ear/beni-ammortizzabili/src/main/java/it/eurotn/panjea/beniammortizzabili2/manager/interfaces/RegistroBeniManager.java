package it.eurotn.panjea.beniammortizzabili2.manager.interfaces;

import it.eurotn.panjea.beniammortizzabili2.util.registrobeni.RegistroBene;

import java.util.List;
import java.util.Map;

import javax.ejb.Local;

@Local
public interface RegistroBeniManager {

	/**
	 * Carica il registro dei beni.
	 * 
	 * @param parameters parametri
	 * @return beni caricati
	 */
	List<RegistroBene> caricaRegistroBeni(Map<String, Object> parameters);
}
