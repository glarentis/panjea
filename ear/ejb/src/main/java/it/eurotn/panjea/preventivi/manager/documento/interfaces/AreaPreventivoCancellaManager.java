package it.eurotn.panjea.preventivi.manager.documento.interfaces;

import it.eurotn.panjea.preventivi.domain.RigaPreventivo;
import it.eurotn.panjea.preventivi.domain.documento.AreaPreventivo;

public interface AreaPreventivoCancellaManager {
	/**
	 * Cancella un'area ordine, il documento e l'eventuale area rate collegata.
	 * 
	 * @param areaPreventivo
	 *            areaPreventivo da cancellare
	 */
	void cancellaAreaPreventivo(AreaPreventivo areaPreventivo);

	/**
	 * Cancella una {@link RigaPreventivo} e restituisce l'area ordine a cui era legata.
	 * 
	 * @param rigaPreventivo
	 *            riga da cancellare
	 * @return area ordine legata alla riga cancellata
	 */
	AreaPreventivo cancellaRigaPreventivo(RigaPreventivo rigaPreventivo);
}
