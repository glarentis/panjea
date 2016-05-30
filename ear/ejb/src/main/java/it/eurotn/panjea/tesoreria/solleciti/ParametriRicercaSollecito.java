/**
 * 
 */
package it.eurotn.panjea.tesoreria.solleciti;

import it.eurotn.panjea.anagrafica.domain.lite.ClienteLite;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;

import java.io.Serializable;

/**
 * Parametri per la ricerca degli effetti.
 * 
 * @author vittorio
 * @version 1.0, 26/nov/2009
 */
public class ParametriRicercaSollecito implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5038681740356177985L;

	// flag utilizzato per sapere se effettuare la ricerca o no
	/**
	 * @uml.property name="effettuaRicerca"
	 */
	private boolean effettuaRicerca = false;

	/**
	 * @uml.property name="entita"
	 * @uml.associationEnd
	 */
	private EntitaLite entita = null;

	/**
	 * Costruttore di default.
	 */
	public ParametriRicercaSollecito() {
		initailize();
	}

	/**
	 * @return the entita
	 * @uml.property name="entita"
	 */
	public EntitaLite getEntita() {
		return entita;
	}

	/**
	 * Init delle propriet� che non devono essere null.
	 */
	private void initailize() {
		removeNullValue();
	}

	/**
	 * @return the effettuaRicerca
	 * @uml.property name="effettuaRicerca"
	 */
	public boolean isEffettuaRicerca() {
		return effettuaRicerca;
	}

	/**
	 * Istanzia gli oggetti a null del server per il client.
	 */
	private void removeNullValue() {
		this.entita = new ClienteLite();

	}

	/**
	 * @param effettuaRicerca
	 *            the effettuaRicerca to set
	 * @uml.property name="effettuaRicerca"
	 */
	public void setEffettuaRicerca(boolean effettuaRicerca) {
		this.effettuaRicerca = effettuaRicerca;
	}

	/**
	 * @param entita
	 *            the entita to set
	 * @uml.property name="entita"
	 */
	public void setEntita(EntitaLite entita) {
		this.entita = entita;
	}
}
