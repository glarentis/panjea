package it.eurotn.panjea.onroad.importer.manager.interfaces;

import it.eurotn.panjea.magazzino.util.AreaMagazzinoFullDTO;
import it.eurotn.panjea.onroad.domain.DocumentoOnRoad;

import java.util.Map;

import javax.ejb.Local;

@Local
public interface OnroadDocumentoTransformer {

	/**
	 * Trasforma un documento onRoad in una areaMagazzinoFullDTO.
	 * 
	 * @param documentoOnRoad
	 *            documentoOnRoad da trasformare
	 * @param conversioniTipiDocumentoOnRoadToPanjea
	 *            la mappa di tipi documento onRoad --> panjea
	 * @return AreaMagazzinoFullDTO
	 */
	AreaMagazzinoFullDTO trasforma(DocumentoOnRoad documentoOnRoad,
			Map<String, String> conversioniTipiDocumentoOnRoadToPanjea);

}
