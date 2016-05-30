/**
 * 
 */
package it.eurotn.panjea.lotti.util;

import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.magazzino.util.CategoriaLite;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author fattazzo
 * 
 */
public class ParametriRicercaScadenzaLotti implements Serializable {

	private static final long serialVersionUID = -5342340878362759663L;

	private DepositoLite deposito;

	private Date dataScadenza;

	private List<CategoriaLite> categorie;

	private boolean tutteCategorie;

	private boolean effettuaRicerca;

	{
		this.effettuaRicerca = false;
		this.tutteCategorie = true;

		this.dataScadenza = Calendar.getInstance().getTime();
	}

	/**
	 * Costruttore.
	 */
	public ParametriRicercaScadenzaLotti() {
		super();
	}

	/**
	 * @return the categorie
	 */
	public List<CategoriaLite> getCategorie() {
		return categorie;
	}

	/**
	 * @return the dataScadenza
	 */
	public Date getDataScadenza() {
		return dataScadenza;
	}

	/**
	 * @return the deposito
	 */
	public DepositoLite getDeposito() {
		return deposito;
	}

	/**
	 * @return the effettuaRicerca
	 */
	public boolean isEffettuaRicerca() {
		return effettuaRicerca;
	}

	/**
	 * @return the tutteCategorie
	 */
	public boolean isTutteCategorie() {
		return tutteCategorie;
	}

	/**
	 * @param categorie
	 *            the categorie to set
	 */
	public void setCategorie(List<CategoriaLite> categorie) {
		this.categorie = categorie;
	}

	/**
	 * @param dataScadenza
	 *            the dataScadenza to set
	 */
	public void setDataScadenza(Date dataScadenza) {
		this.dataScadenza = dataScadenza;
	}

	/**
	 * @param deposito
	 *            the deposito to set
	 */
	public void setDeposito(DepositoLite deposito) {
		this.deposito = deposito;
	}

	/**
	 * @param effettuaRicerca
	 *            the effettuaRicerca to set
	 */
	public void setEffettuaRicerca(boolean effettuaRicerca) {
		this.effettuaRicerca = effettuaRicerca;
	}

	/**
	 * @param tutteCategorie
	 *            the tutteCategorie to set
	 */
	public void setTutteCategorie(boolean tutteCategorie) {
		this.tutteCategorie = tutteCategorie;
	}
}
