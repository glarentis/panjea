package it.eurotn.panjea.onroad.importer.manager.interfaces;

import it.eurotn.panjea.exporter.exception.ImportException;
import it.eurotn.panjea.onroad.domain.wrapper.ClientiOnRoad;

import javax.ejb.Local;

@Local
public interface ImportaClienti {

	/**
	 * @return cartella contenente i file da importare per Aton
	 */
	String getAtonImportDir();

	/**
	 * @return prefisso utilizzato nel node dei file per l'importazione
	 */
	String getAtonPrefixImport();

	/**
	 * Riceve un file di clienti e li salva.
	 * 
	 * @param pathFile
	 *            path del file da importare
	 * @return lista di clienti importati.
	 * @throws ImportException
	 *             rilanciata se viene riscontrato un errore durante l'importazione
	 */
	ClientiOnRoad importa(String pathFile) throws ImportException;
}
