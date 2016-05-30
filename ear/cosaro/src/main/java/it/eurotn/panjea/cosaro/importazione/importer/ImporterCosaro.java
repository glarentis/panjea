package it.eurotn.panjea.cosaro.importazione.importer;

import java.io.File;

import javax.ejb.Local;

@Local
public interface ImporterCosaro {
	/**
	 * 
	 * @param file
	 *            file da importare
	 * @return true se il file è stato importato
	 */
	boolean importa(File file);

	/**
	 * Testa per controllare se il file può essere importato con l'importer.
	 * 
	 * @param file
	 *            file da testare
	 * @return true se il file è compatibile con il tipo di importazione
	 */
	boolean test(File file);
}
