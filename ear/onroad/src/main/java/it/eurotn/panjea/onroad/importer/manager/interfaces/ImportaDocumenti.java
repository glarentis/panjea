package it.eurotn.panjea.onroad.importer.manager.interfaces;

import it.eurotn.panjea.exporter.exception.ImportException;
import it.eurotn.panjea.onroad.domain.wrapper.DocumentiOnRoad;

import javax.ejb.Local;

@Local
public interface ImportaDocumenti {

	/**
	 * 
	 * @return cartella contenente i file da importare per Aton
	 */
	String getAtonImportDir();

	/**
	 * 
	 * @return prefisso utilizzato nel node dei file per l'importazione
	 */
	String getAtonPrefixImport();

	/**
	 * Riceve un file di testate e salva l'ordine compreso di righe e note (recupera anche gli altri file necessari).
	 * 
	 * @param pathFile
	 *            path del file da importare
	 * @return documenti onRoad importati.
	 * @throws ImportException
	 *             rilanciata se viene riscontrato un errore durante l'importazione
	 */
	DocumentiOnRoad importa(String pathFile) throws ImportException;
}
