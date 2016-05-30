package it.eurotn.panjea.preventivi.domain.documento.interfaces;

import it.eurotn.panjea.anagrafica.documenti.domain.ITipoAreaDocumento;

public interface ITipoAreaStampabile extends ITipoAreaDocumento {

	/**
	 * 
	 * @return reportPath
	 */
	String getReportPath();

	/**
	 * 
	 * @param id
	 *            id
	 */
	void setId(Integer id);

	/**
	 * 
	 * @param versionTipoArea
	 *            versione
	 */
	void setVersion(Integer versionTipoArea);
}
