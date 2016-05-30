package it.eurotn.panjea.cosaro.produzione;

import java.io.File;

import javax.ejb.Local;

@Local
public interface ProduzioneCosaroService {

	/**
	 * Importa i carichi produzione da GAMMAMEAT.
	 * 
	 * @param fileProduzione
	 *            file di produzione, il nome è la data del documento di produzione.
	 */
	void importa(File fileProduzione);
}
