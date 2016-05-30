package it.eurotn.panjea.cosaro.importazione;

import java.io.File;

import javax.ejb.Local;

@Local
public interface ImportazioneCosaroService {

	/**
	 * importa i file di ordini trovati nella cartella configurata.
	 * 
	 * @param fileToImport
	 *            file da importare
	 */
	void importa(File fileToImport);

}
