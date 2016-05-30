package it.eurotn.panjea.preventivi.util.interfaces;

import java.io.Serializable;

public interface IRigaDTO extends Serializable {

	/**
	 * 
	 * @return descrizione
	 */
	String getDescrizione();

	/**
	 * 
	 * @return id
	 */
	Integer getId();

	/**
	 * 
	 * @return livello
	 */
	Integer getLivello();

	/**
	 * 
	 * @param descrizione
	 *            descrizione
	 */
	void setDescrizione(String descrizione);

	/**
	 * 
	 * @param id
	 *            id
	 */
	void setId(Integer id);

	/**
	 * 
	 * @param livello
	 *            livello
	 */
	void setLivello(Integer livello);
}
