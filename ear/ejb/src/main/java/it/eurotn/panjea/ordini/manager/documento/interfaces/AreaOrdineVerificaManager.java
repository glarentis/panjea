package it.eurotn.panjea.ordini.manager.documento.interfaces;

import it.eurotn.panjea.ordini.domain.documento.AreaOrdine;
import it.eurotn.panjea.rate.domain.AreaRate;

import javax.ejb.Local;

@Local
public interface AreaOrdineVerificaManager {

	/**
	 * Verifica se l'area ordine deve cambiare stato.
	 * 
	 * @param areaOrdine
	 *            area ordine
	 * @param areaRate
	 *            areaRate
	 * @return <code>true</code> se deve cambiare lo stato, <code>false</code> altrimenti
	 */
	boolean checkCambioStato(AreaOrdine areaOrdine, AreaRate areaRate);
}
