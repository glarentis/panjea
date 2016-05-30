package it.eurotn.panjea.centricosto.rich.bd;

import it.eurotn.panjea.centricosto.domain.CentroCosto;
import it.eurotn.panjea.rich.bd.AsyncMethodInvocation;

import java.util.List;

public interface ICentriCostoBD {
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
	@AsyncMethodInvocation
	List<CentroCosto> caricaCentriCosto(String codice);

	/**
	 * Salva un centro di costo.
	 * 
	 * @param centroCosto
	 *            centro di costo da salvare
	 * @return Centro di costo salvato.
	 */
	CentroCosto salvaCentroCosto(CentroCosto centroCosto);
}
