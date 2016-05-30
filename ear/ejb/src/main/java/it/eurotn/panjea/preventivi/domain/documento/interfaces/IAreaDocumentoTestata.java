package it.eurotn.panjea.preventivi.domain.documento.interfaces;

import it.eurotn.panjea.anagrafica.documenti.domain.IAreaDocumento;
import it.eurotn.panjea.anagrafica.documenti.domain.ITipoAreaDocumento;

import java.util.Date;

public interface IAreaDocumentoTestata extends IAreaDocumento {

	/**
	 *
	 * @return note
	 */
	IAreaDocumentoNote getAreaDocumentoNote();

	/**
	 *
	 * @return dataRegestrazione
	 */
	Date getDataRegistrazione();

	/**
	 *
	 * @return TipoAreaDocumento
	 */
	ITipoAreaDocumento getTipoArea();

	/**
	 *
	 * @param id
	 *            id
	 */
	void setId(Integer id);

	/**
	 * @param tipoArea
	 *            tipoArea
	 */
	void setTipoArea(ITipoAreaDocumento tipoArea);
}
