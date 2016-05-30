package it.eurotn.panjea.centricosto.manager.interfaces;

import it.eurotn.panjea.centricosto.domain.CentroCosto;

import java.util.List;

import javax.ejb.Local;

@Local
public interface CentriCostoManager {

	/**
	 * Cancella un centro di costo. Se esistono dei sottoconti non viene cancellato.
	 * 
	 * @param centroCosto
	 *            centro di costo da cancellare.
	 */
	void cancellaCentroCosto(CentroCosto centroCosto);

	/**
	 * @param codice
	 *            codice del centro di costo
	 * @return centri di costo per l'azienda loggata
	 */
	List<CentroCosto> caricaCentriCosto(String codice);

	/**
	 * Carica un centro di costo.
	 * 
	 * @param centroCosto
	 *            centro da caricare
	 * @return centro caricato
	 */
	CentroCosto caricaCentroCosto(CentroCosto centroCosto);

	/**
	 * Salva un centro di costo.
	 * 
	 * @param centroCosto
	 *            centro di costo da salvare
	 * @return Centro di costo salvato.
	 */
	CentroCosto salvaCentroCosto(CentroCosto centroCosto);
}
