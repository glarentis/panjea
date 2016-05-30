package it.eurotn.panjea.preventivi.rich.bd.interfaces;

import it.eurotn.panjea.preventivi.domain.documento.AreaPreventivo;
import it.eurotn.panjea.preventivi.domain.documento.interfaces.IAreaDocumentoTestata;
import it.eurotn.panjea.preventivi.util.interfaces.IAreaDTO;
import it.eurotn.panjea.rate.domain.AreaRate;

import java.util.List;

public interface IAreaDocumentoBD<E extends IAreaDocumentoTestata, T extends IAreaDTO<E>> {

	/**
	 *
	 * @param areaDocumento
	 *            areaDocumento da cancellare
	 */
	void cancellaArea(E areaDocumento);

	/**
	 *
	 * @param areeDocumento
	 *            aree documento da cancellare.
	 */
	void cancellaAree(List<AreaPreventivo> areeDocumento);

	/**
	 *
	 * @param areaDocumento
	 *            area di cui si richiede il fullDTO
	 * @return area fullDTO salvata
	 */
	T caricaAreaFullDTO(E areaDocumento);

	/**
	 *
	 * @param areaDocumento
	 *            areaDocumento
	 * @param areaRate
	 *            areaRate
	 * @return area fullDTO salvata
	 */
	T salvaAreaDocumento(E areaDocumento, AreaRate areaRate);

}
