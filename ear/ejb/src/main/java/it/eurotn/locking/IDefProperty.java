package it.eurotn.locking;

public interface IDefProperty {

	/**
	 * @return nome della classe di dominio
	 */
	String getDomainClassName();

	/**
	 * 
	 * @return id dell'oggetto
	 */
	Integer getId();

	/**
	 * 
	 * @return versione dell'oggetto
	 */
	Integer getVersion();

	/**
	 * 
	 * @return true se l'entità è nuova
	 */
	boolean isNew();

}
