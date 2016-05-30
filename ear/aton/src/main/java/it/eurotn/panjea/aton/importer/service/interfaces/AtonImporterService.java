package it.eurotn.panjea.aton.importer.service.interfaces;

import it.eurotn.panjea.anagrafica.domain.Preference;
import it.eurotn.panjea.exporter.exception.ImportException;
import it.eurotn.panjea.ordini.domain.OrdineImportato;

import javax.ejb.Remote;

@Remote
public interface AtonImporterService {
	/**
	 * Importa gli ordini di atom utilizzando l'entity {@link OrdineImportato}. La cartella di importazione viene
	 * prelevata dalle {@link Preference}
	 * 
	 * @throws ImportException
	 *             rilanciata se viene riscontrato un errore durante l'importazione
	 */
	void importa() throws ImportException;

	/**
	 * @return il numero di file da importare
	 */
	int[] verifica();
}
