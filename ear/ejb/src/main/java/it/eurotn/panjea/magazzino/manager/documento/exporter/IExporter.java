package it.eurotn.panjea.magazzino.manager.documento.exporter;

import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.exception.EsportaDocumentoCassaException;

import javax.ejb.Local;

@Local
public interface IExporter {

	/**
	 * Esporta i dati del documento.
	 * 
	 * @param areaMagazzino
	 *            area magazzino di riferimento
	 * @throws EsportaDocumentoCassaException
	 *             eccezione generica
	 */
	void esporta(AreaMagazzino areaMagazzino) throws EsportaDocumentoCassaException;
}
