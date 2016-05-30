package it.eurotn.panjea.aton.importer.manager.interfaces;

import it.eurotn.panjea.exporter.exception.ImportException;
import it.eurotn.panjea.ordini.domain.OrdineImportato;

import java.util.List;

import javax.ejb.Local;

@Local
public interface ImportaOrdini {

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
	 * @return lista di ordini importati.
	 * @throws ImportException
	 *             rilanciata se viene riscontrato un errore durante l'importazione
	 */
	List<OrdineImportato> importa(String pathFile) throws ImportException;
}
