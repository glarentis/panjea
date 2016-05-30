/**
 * 
 */
package it.eurotn.panjea.iva.manager.interfaces;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.contabilita.domain.AreaContabile;
import it.eurotn.panjea.iva.domain.AreaIva;
import it.eurotn.panjea.iva.domain.RigaIva;

import javax.ejb.Local;

/**
 * @author Leonardo
 * 
 */
@Local
public interface AreaIvaCancellaManager {

	/**
	 * Esegue la cancellazione dell'Area Iva se l'area non ha un link verso il magazzino. Altrimenti rimuove solamente
	 * il link verso l'area contabile.
	 * 
	 * @deprecated usare il metodo cancellaAreaIva(Documento)
	 * @param areaContabile
	 *            area contabile
	 */
	@Deprecated
	void cancellaAreaIva(AreaContabile areaContabile);

	/**
	 * Cancella l'{@link AreaIva} scelta cancellando le righe iva collegate.
	 * 
	 * @param areaIva
	 *            area iva
	 */
	void cancellaAreaIva(AreaIva areaIva);

	/**
	 * Cancella l'area iva collegata al documento passato come parametro.
	 * 
	 * @param documento
	 *            documento di riferimento
	 */
	void cancellaAreaIva(Documento documento);

	/**
	 * Cancella l'{@link AreaIva} scelta.
	 * 
	 * @param areaIva
	 *            area iva
	 */
	void cancellaAreaIvaNoCheck(AreaIva areaIva);

	/**
	 * metodo che cancella {@link RigaIva}.
	 * 
	 * @param rigaIva
	 *            riga iva da cancellare
	 */
	void cancellaRigaIva(RigaIva rigaIva);

	/**
	 * Cancella la lista di {@link RigaIva} associate all'{@link AreaIva}.
	 * 
	 * @param areaIva
	 *            area iva
	 */
	void cancellaRigheIva(AreaIva areaIva);

}
