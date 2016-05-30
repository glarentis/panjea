package it.eurotn.panjea.preventivi.domain.documento.interfaces;

import it.eurotn.panjea.preventivi.domain.documento.AreaPreventivo;
import it.eurotn.panjea.rate.domain.AreaRate;

public interface AreaPreventivoVerificaManager {

	/**
	 * Verifica se l'area ordine deve cambiare stato.
	 * 
	 * @param areaPrevetivo
	 *            area preventivo
	 * @param areaRate
	 *            areaRate
	 * @return <code>true</code> se deve cambiare lo stato, <code>false</code> altrimenti
	 */
	boolean checkCambioStato(AreaPreventivo areaPrevetivo, AreaRate areaRate);
}
