package it.eurotn.panjea.preventivi.util;

import it.eurotn.panjea.preventivi.domain.documento.interfaces.IAreaDocumentoTestata;

public abstract class RegoleCambioStatoAreaDocumento<E extends IAreaDocumentoTestata, T extends Enum<T>> {

	/**
	 * 
	 * @param statoAttuale
	 *            stato attuale
	 * @param statoNuovo
	 *            stato da impostare
	 * @return true se è possibile passare dallo stato attuale a quello nuovo.
	 */
	public abstract boolean isCambioStatoPossibile(T statoAttuale, T statoNuovo);

	/**
	 * 
	 * @param areaDocumento
	 *            area documento
	 * @return true se è possibile modificare lo stato dell'area documento, indipendentemente dallo stato di
	 *         destinazione voluto.
	 */
	public boolean isCambioStatoPossibileSuDocumento(E areaDocumento) {
		return areaDocumento != null && !areaDocumento.isNew();
	}

	/**
	 * Applica ulteriori modifiche all'area documento dopo il cambio stato.
	 * 
	 * @param areaDocumento
	 *            areaDocumento cambiata
	 * @param statoPrecedente
	 *            stato precedente dell'area documento.
	 */
	public void postCambioStato(E areaDocumento, T statoPrecedente) {

	}
}
