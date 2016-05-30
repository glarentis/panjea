package it.eurotn.panjea.preventivi.util.interfaces;

public interface IRigaNotaDTO {

	/**
	 * 
	 * @return nota
	 */
	String getNota();

	/**
	 * 
	 * @return true se si tratta di una riga automatica.
	 */
	boolean isRigaAutomatica();

	/**
	 * 
	 * @param nota
	 *            nota
	 */
	void setNota(String nota);

	/**
	 * 
	 * @param rigaAutomatica
	 *            riga automatica
	 */
	void setRigaAutomatica(boolean rigaAutomatica);
}
