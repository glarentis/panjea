package it.eurotn.panjea.onroad.importer.service.interfaces;

import it.eurotn.panjea.anagrafica.domain.Preference;
import it.eurotn.panjea.exporter.exception.ImportException;
import it.eurotn.panjea.onroad.domain.wrapper.ClientiOnRoad;
import it.eurotn.panjea.onroad.domain.wrapper.DocumentiOnRoad;

import javax.ejb.Remote;

@Remote
public interface OnRoadImporterService {

	/**
	 * Importa i clienti onRoad in Panjea, restituisce le entità importate e a quelle non importate. La cartella di
	 * importazione viene prelevata dalle {@link Preference}.
	 * 
	 * @return ClientiOnRoad
	 * @throws ImportException
	 *             rilanciata se viene riscontrato un errore durante l'importazione
	 */
	ClientiOnRoad importaClienti() throws ImportException;

	/**
	 * Importa i documenti di aton OnRoad. La cartella di importazione viene prelevata dalle {@link Preference}.
	 * 
	 * @return DocumentiOnRoad
	 * @throws ImportException
	 *             rilanciata se viene riscontrato un errore durante l'importazione
	 */
	DocumentiOnRoad importaDocumenti() throws ImportException;
}
