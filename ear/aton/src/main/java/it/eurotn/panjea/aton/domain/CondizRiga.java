package it.eurotn.panjea.aton.domain;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Riga del file condiz da esportare.<br/>
 * Non posso farla astartta perchè il framework BeanIO non accetta classi astratte.
 * 
 * @author giangi
 * @version 1.0, 27/dic/2011
 */
public class CondizRiga {

	/**
	 * @return chiave di ricerca per il condiz aton
	 */
	public String getChiave() {
		throw new UnsupportedOperationException("Utilizzare una delle classi derivate");
	}

	/**
	 * @return codice di ricerca per il condiz aton
	 */
	public String getCodiceRicerca() {
		throw new UnsupportedOperationException("Utilizzare una delle classi derivate");
	}

	/**
	 * @return codice sconto
	 */
	public String getCodiceSconto() {
		throw new UnsupportedOperationException("Utilizzare una delle classi derivate");
	}

	/**
	 * @return data fine
	 */
	public Date getDataFine() {
		throw new UnsupportedOperationException("Utilizzare una delle classi derivate");
	}

	/**
	 * @return data inizio
	 */
	public Date getDataInizio() {
		throw new UnsupportedOperationException("Utilizzare una delle classi derivate");
	}

	/**
	 * @return prezzo
	 */
	public BigDecimal getPrezzo() {
		throw new UnsupportedOperationException("Utilizzare una delle classi derivate");
	}

	/**
	 * @return scontoPercentuale
	 */
	public BigDecimal getScontoPercentuale() {
		throw new UnsupportedOperationException("Utilizzare una delle classi derivate");
	}

	/**
	 * @param chiave
	 *            chiave
	 */
	public void setChiave(String chiave) {
		throw new UnsupportedOperationException("Utilizzare una delle classi derivate");
	}

	/**
	 * @param codiceRicerca
	 *            codiceRicerca
	 */
	public void setCodiceRicerca(String codiceRicerca) {
		throw new UnsupportedOperationException("Utilizzare una delle classi derivate");
	}

	/**
	 * @param codiceSconto
	 *            codiceSconto
	 */
	public void setCodiceSconto(String codiceSconto) {
		throw new UnsupportedOperationException("Utilizzare una delle classi derivate");
	}

	/**
	 * @param data
	 *            dataFine
	 */
	public void setDataFine(Date data) {
		throw new UnsupportedOperationException("Utilizzare una delle classi derivate");
	}

	/**
	 * @param data
	 *            dataInizio
	 */
	public void setDataInizio(Date data) {
		throw new UnsupportedOperationException("Utilizzare una delle classi derivate");
	}

	/**
	 * @param prezzo
	 *            prezzo
	 */
	public void setPrezzo(BigDecimal prezzo) {
		throw new UnsupportedOperationException("Utilizzare una delle classi derivate");
	}

	/**
	 * @param scontoPercentuale
	 *            the scontoPercentuale to set
	 */
	public void setScontoPercentuale(BigDecimal scontoPercentuale) {
		throw new UnsupportedOperationException("Utilizzare una delle classi derivate");
	}

}
