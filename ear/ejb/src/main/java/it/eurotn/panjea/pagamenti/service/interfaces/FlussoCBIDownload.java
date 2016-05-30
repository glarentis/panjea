package it.eurotn.panjea.pagamenti.service.interfaces;

import javax.ejb.Remote;

@Remote
public interface FlussoCBIDownload {

	/**
	 * Apre il file specificato e lo restituisce come array di byte.
	 * 
	 * @param filePath
	 *            percorso del file
	 * @return contenuto del file
	 */
	byte[] getData(String filePath);
}
