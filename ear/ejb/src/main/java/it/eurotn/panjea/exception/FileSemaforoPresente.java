package it.eurotn.panjea.exception;

/**
 * Rilanciata se esiste un file semaforo.
 * 
 * @author giangi
 * @version 1.0, 11/dic/2012
 * 
 */
public class FileSemaforoPresente extends Exception {
	private static final long serialVersionUID = 1200492391998225026L;
	private String filePath;

	/**
	 * 
	 * Costruttore.
	 * 
	 * @param filePath
	 *            path del file semaforo
	 */
	public FileSemaforoPresente(final String filePath) {
		super();
		this.filePath = filePath;
	}

	/**
	 * @return Returns the filePath.
	 */
	public String getFilePath() {
		return filePath;
	}

}
