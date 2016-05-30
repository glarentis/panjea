package it.eurotn.panjea.ordini.manager.documento.interfaces;

import it.eurotn.panjea.ordini.domain.RigaOrdine;
import it.eurotn.panjea.ordini.domain.documento.AreaOrdine;

import javax.ejb.Local;

@Local
public interface AreaOrdineCancellaManager {

	/**
	 * Cancella un'area ordine, il documento e l'eventuale area rate collegata.
	 * 
	 * @param areaOrdine
	 *            areaOrdine da cancellare
	 */
	void cancellaAreaOrdine(AreaOrdine areaOrdine);

	/**
	 * Cancella una {@link RigaOrdine} e restituisce l'area ordine a cui era legata.
	 * 
	 * @param rigaOrdine
	 *            riga da cancellare
	 * @return area ordine legata alla riga cancellata
	 */
	AreaOrdine cancellaRigaOrdine(RigaOrdine rigaOrdine);

}
