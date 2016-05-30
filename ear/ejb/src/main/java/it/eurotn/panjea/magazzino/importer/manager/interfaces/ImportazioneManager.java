/**
 * 
 */
package it.eurotn.panjea.magazzino.importer.manager.interfaces;

import it.eurotn.panjea.magazzino.importer.util.DocumentoImport;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoRicerca;

import java.util.Collection;
import java.util.List;

import javax.ejb.Local;

/**
 * @author fattazzo
 * 
 */
@Local
public interface ImportazioneManager {

	/**
	 * Carica tutti i documenti in base all'importer specificato.
	 * 
	 * @param codiceImporter
	 *            codice dell'importer importer
	 * @param fileImport
	 *            file da importare
	 * @return documenti caricati
	 */
	Collection<DocumentoImport> caricaDocumenti(String codiceImporter, byte[] fileImport);

	/**
	 * Carica tutti i codice gli importer presenti.
	 * 
	 * @return importer caricati
	 */
	List<String> caricaImporter();

	/**
	 * Importa i documenti.
	 * 
	 * @param documenti
	 *            documenti da importare
	 * @param codiceImporter
	 *            codice importer associato
	 * @return aree create
	 */
	List<AreaMagazzinoRicerca> importaDocumenti(Collection<DocumentoImport> documenti, String codiceImporter);
}
