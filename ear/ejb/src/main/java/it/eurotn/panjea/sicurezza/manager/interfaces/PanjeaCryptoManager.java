package it.eurotn.panjea.sicurezza.manager.interfaces;

import javax.ejb.Local;

@Local
public interface PanjeaCryptoManager {

	/**
	 * Decrypt.
	 *
	 * @param encryptedData
	 *            .
	 * @return .
	 */
	String decrypt(String encryptedData);

	/**
	 * Encrypt.
	 *
	 * @param text
	 *            .
	 * @return .
	 */
	String encrypt(String text);
}
