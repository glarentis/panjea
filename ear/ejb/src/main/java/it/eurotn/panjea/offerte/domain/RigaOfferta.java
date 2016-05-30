package it.eurotn.panjea.offerte.domain;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * 
 * @author Leonardo
 */
@Entity
@Table(name = "offe_righe_offerta")
@NamedQueries({
		@NamedQuery(name = "RigaOfferta.caricaByAreaOfferta", query = "from RigaOfferta r where r.areaOfferta.id =:paramIdAreaOfferta "),
		@NamedQuery(name = "RigaOfferta.cancellaByAreaOfferta", query = "delete from RigaOfferta r where r.areaOfferta.id = :paramIdAreaOfferta ") })
public class RigaOfferta extends EntityBase {

	private static final long serialVersionUID = -8346069191179938809L;

	@ManyToOne
	private ArticoloLite articolo;

	private String descrizione;

	@Lob
	private String descrizioneEstesa;

	private double qta;

	@Column
	private boolean accettata;

	@Embedded
	private Importo prezzoUnitario;

	@ManyToOne(fetch = FetchType.LAZY)
	private AreaOfferta areaOfferta;

	/**
	 *
	 */
	public RigaOfferta() {
		super();
		initialize();
	}

	/**
	 * @return the areaOfferta
	 */
	public AreaOfferta getAreaOfferta() {
		return areaOfferta;
	}

	/**
	 * @return the articolo
	 */
	public ArticoloLite getArticolo() {
		return articolo;
	}

	/**
	 * @return the descrizione
	 */
	public String getDescrizione() {
		return descrizione;
	}

	/**
	 * @return the descrizioneEstesa
	 */
	public String getDescrizioneEstesa() {
		return descrizioneEstesa;
	}

	/**
	 * @return the prezzoUnitario
	 */
	public Importo getPrezzoTotale() {
		return getPrezzoUnitario().multiply(BigDecimal.valueOf(qta), 2);
	}

	/**
	 * @return the prezzoUnitario
	 */
	public Importo getPrezzoUnitario() {
		return prezzoUnitario;
	}

	/**
	 * @return the qta
	 */
	public double getQta() {
		return qta;
	}

	/**
	 * Inizializza i valori di default.
	 */
	private void initialize() {
		this.accettata = false;
		this.prezzoUnitario = new Importo();
		this.prezzoUnitario.setCodiceValuta("EUR");
		this.prezzoUnitario.setTassoDiCambio(BigDecimal.ONE);
	}

	/**
	 * @return the accettata
	 */
	public boolean isAccettata() {
		return accettata;
	}

	/**
	 * @param accettata
	 *            the accettata to set
	 */
	public void setAccettata(boolean accettata) {
		this.accettata = accettata;
	}

	/**
	 * @param areaOfferta
	 *            the areaOfferta to set
	 */
	public void setAreaOfferta(AreaOfferta areaOfferta) {
		this.areaOfferta = areaOfferta;
	}

	/**
	 * @param articolo
	 *            the articolo to set
	 */
	public void setArticolo(ArticoloLite articolo) {
		this.articolo = articolo;
	}

	/**
	 * @param descrizione
	 *            the descrizione to set
	 */
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	/**
	 * @param descrizioneEstesa
	 *            the descrizioneEstesa to set
	 */
	public void setDescrizioneEstesa(String descrizioneEstesa) {
		this.descrizioneEstesa = descrizioneEstesa;
	}

	public void setPrezzoTotale(Importo prezzoTotale) {
		// do nothing
	}

	/**
	 * @param prezzoUnitario
	 *            the prezzoUnitario to set
	 */
	public void setPrezzoUnitario(Importo prezzoUnitario) {
		this.prezzoUnitario = prezzoUnitario;
	}

	/**
	 * @param qta
	 *            the qta to set
	 */
	public void setQta(double qta) {
		this.qta = qta;
	}

}
