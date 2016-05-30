/**
 *
 */
package it.eurotn.panjea.magazzino.manager.documento.interfaces;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.documenti.service.exception.AreeCollegatePresentiException;
import it.eurotn.panjea.anagrafica.documenti.service.exception.DocumentiCollegatiPresentiException;
import it.eurotn.panjea.anagrafica.service.exception.TipoDocumentoBaseException;

import javax.ejb.Local;

/*
 * @author Leonardo
 */
@Local
public interface AreaMagazzinoDocumentoCancellaManager {

	/**
	 * 
	 * @param documento
	 *            documento di riferimento
	 * @throws AreeCollegatePresentiException
	 *             sollevata se esistono altre aree per il documento
	 * @throws TipoDocumentoBaseException
	 *             sollevatase non esite un tipoDocumentoBasePartite
	 * @throws DocumentiCollegatiPresentiException
	 *             sollevata se esistono documenti collegati
	 */
	void cancellaAreaMagazzino(Documento documento) throws DocumentiCollegatiPresentiException,
			TipoDocumentoBaseException, AreeCollegatePresentiException;

	/**
	 * 
	 * @param documento
	 *            documento di riferimento
	 * @param forceDeleteAreaCollegata
	 *            foza la cancellazione delle aree collegate
	 * @throws AreeCollegatePresentiException
	 *             sollevata se esistono altre aree per il documento
	 * @throws TipoDocumentoBaseException
	 *             sollevatase non esite un tipoDocumentoBasePartite
	 * @throws DocumentiCollegatiPresentiException
	 *             sollevata se esistono documenti collegati
	 */
	void cancellaAreaMagazzino(Documento documento, boolean forceDeleteAreaCollegata)
			throws DocumentiCollegatiPresentiException, TipoDocumentoBaseException, AreeCollegatePresentiException;

}
