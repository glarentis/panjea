/**
 *
 */
package it.eurotn.panjea.contabilita.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * @author fattazzo
 *
 */
@Embeddable
public class DatiGenerazione implements Serializable {

	public enum TipoGenerazione {
		SIMULAZIONE_BENI
	}

	private static final long serialVersionUID = -6568960274322048195L;

	private String utente;

	@Temporal(TemporalType.TIMESTAMP)
	private Date dataGenerazione;

	private TipoGenerazione tipoGenerazione;

	/**
	 * Costruttore.
	 */
	public DatiGenerazione() {
		this(null, null, null);
	}

	/**
	 * Costruttore.
	 *
	 * @param utente
	 *            utente di generazione
	 * @param dataGenerazione
	 *            data
	 * @param tipoGenerazione
	 *            tipo di generazione
	 */
	public DatiGenerazione(final String utente, final Date dataGenerazione, final TipoGenerazione tipoGenerazione) {
		super();
		this.utente = utente;
		this.dataGenerazione = dataGenerazione;
		this.tipoGenerazione = tipoGenerazione;
	}

	/**
	 * @return the dataGenerazione
	 */
	public Date getDataGenerazione() {
		return dataGenerazione;
	}

	/**
	 * @return the tipoGenerazione
	 */
	public TipoGenerazione getTipoGenerazione() {
		return tipoGenerazione;
	}

	/**
	 * @return the utente
	 */
	public String getUtente() {
		return utente;
	}

	/**
	 * @param dataGenerazione
	 *            the dataGenerazione to set
	 */
	public void setDataGenerazione(Date dataGenerazione) {
		this.dataGenerazione = dataGenerazione;
	}

	/**
	 * @param tipoGenerazione
	 *            the tipoGenerazione to set
	 */
	public void setTipoGenerazione(TipoGenerazione tipoGenerazione) {
		this.tipoGenerazione = tipoGenerazione;
	}

	/**
	 * @param utente
	 *            the utente to set
	 */
	public void setUtente(String utente) {
		this.utente = utente;
	}
}
