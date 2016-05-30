package it.eurotn.panjea.spedizioni.domain;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.spedizioni.util.AreaMagazzinoSpedizione;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

@Entity
@Audited
@Table(name = "maga_segnacolli")
public class Segnacollo extends EntityBase {

	private static final long serialVersionUID = -6523180539517106951L;

	private Integer numeroSegnacollo;

	private boolean consegnato;

	@Column(length = 35)
	private String descrizionePuntoOperativoPartenza;

	@Column(length = 3)
	private String terminalArrivo;

	@Column(length = 3)
	private String puntoOperativoArrivoCodice;

	@Column(length = 35)
	private String puntoOperativoArrivoDescrizione;

	@Column(length = 2)
	private String zonaSegnacollo;

	/**
	 * Costruttore.
	 * 
	 */
	public Segnacollo() {
		super();
	}

	/**
	 * 
	 * Crea un segnacollo da un'area magazzino spedizione.
	 * 
	 * @param areaMagazzinoSpedizione
	 *            area magazzino di riferimento
	 */
	public Segnacollo(final AreaMagazzinoSpedizione areaMagazzinoSpedizione) {
		super();

		setConsegnato(false);
		setNumeroSegnacollo(areaMagazzinoSpedizione.getNumeroSegnacollo());

		setDescrizionePuntoOperativoPartenza(areaMagazzinoSpedizione.getDescrizionePuntoOperativoPartenza());
		setTerminalArrivo(areaMagazzinoSpedizione.getTerminalArrivo());
		setPuntoOperativoArrivoCodice(areaMagazzinoSpedizione.getPuntoOperativoArrivoCodice());
		setPuntoOperativoArrivoDescrizione(areaMagazzinoSpedizione.getPuntoOperativoArrivoDescrizione());
		setZonaSegnacollo(areaMagazzinoSpedizione.getZonaSegnacollo());
	}

	/**
	 * @return the descrizionePuntoOperativoPartenza
	 */
	public String getDescrizionePuntoOperativoPartenza() {
		return descrizionePuntoOperativoPartenza;
	}

	/**
	 * @return the numeroSegnacollo
	 */
	public Integer getNumeroSegnacollo() {
		return numeroSegnacollo;
	}

	/**
	 * @return the puntoOperativoArrivoCodice
	 */
	public String getPuntoOperativoArrivoCodice() {
		return puntoOperativoArrivoCodice;
	}

	/**
	 * @return the puntoOperativoArrivoDescrizione
	 */
	public String getPuntoOperativoArrivoDescrizione() {
		return puntoOperativoArrivoDescrizione;
	}

	/**
	 * @return the terminalArrivo
	 */
	public String getTerminalArrivo() {
		return terminalArrivo;
	}

	/**
	 * @return the zonaSegnacollo
	 */
	public String getZonaSegnacollo() {
		return zonaSegnacollo;
	}

	/**
	 * @return the consegnato
	 */
	public boolean isConsegnato() {
		return consegnato;
	}

	/**
	 * @param consegnato
	 *            the consegnato to set
	 */
	public void setConsegnato(boolean consegnato) {
		this.consegnato = consegnato;
	}

	/**
	 * @param descrizionePuntoOperativoPartenza
	 *            the descrizionePuntoOperativoPartenza to set
	 */
	public void setDescrizionePuntoOperativoPartenza(String descrizionePuntoOperativoPartenza) {
		this.descrizionePuntoOperativoPartenza = descrizionePuntoOperativoPartenza;
	}

	/**
	 * @param numeroSegnacollo
	 *            the numeroSegnacollo to set
	 */
	public void setNumeroSegnacollo(Integer numeroSegnacollo) {
		this.numeroSegnacollo = numeroSegnacollo;
	}

	/**
	 * @param puntoOperativoArrivoCodice
	 *            the puntoOperativoArrivoCodice to set
	 */
	public void setPuntoOperativoArrivoCodice(String puntoOperativoArrivoCodice) {
		this.puntoOperativoArrivoCodice = puntoOperativoArrivoCodice;
	}

	/**
	 * @param puntoOperativoArrivoDescrizione
	 *            the puntoOperativoArrivoDescrizione to set
	 */
	public void setPuntoOperativoArrivoDescrizione(String puntoOperativoArrivoDescrizione) {
		this.puntoOperativoArrivoDescrizione = puntoOperativoArrivoDescrizione;
	}

	/**
	 * @param terminalArrivo
	 *            the terminalArrivo to set
	 */
	public void setTerminalArrivo(String terminalArrivo) {
		this.terminalArrivo = terminalArrivo;
	}

	/**
	 * @param zonaSegnacollo
	 *            the zonaSegnacollo to set
	 */
	public void setZonaSegnacollo(String zonaSegnacollo) {
		this.zonaSegnacollo = zonaSegnacollo;
	}
}
