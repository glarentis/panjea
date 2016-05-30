/**
 * 
 */
package it.eurotn.panjea.pos.domain;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.Listino;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

/**
 * @author fattazzo
 * 
 */
@Entity
@Audited
@Table(name = "pos_settings")
@NamedQueries({ @NamedQuery(name = "PosSettings.caricaAll", query = "from PosSettings pos where pos.codiceAzienda = :codiceAzienda") })
public class PosSettings extends EntityBase {

	private static final long serialVersionUID = -4542682030671200L;

	@Column(length = 10, nullable = false)
	private String codiceAzienda;

	@ManyToOne
	private Listino listino;

	@ManyToOne
	private ArticoloLite articoloArrotondamenti;

	@ManyToOne
	private ArticoloLite articoloBuono;

	@ManyToOne
	private ArticoloLite articoloVeloce1;
	@ManyToOne
	private ArticoloLite articoloVeloce2;
	@ManyToOne
	private ArticoloLite articoloVeloce3;
	@ManyToOne
	private ArticoloLite articoloVeloce4;
	@ManyToOne
	private ArticoloLite articoloVeloce5;
	@ManyToOne
	private ArticoloLite articoloVeloce6;
	@ManyToOne
	private ArticoloLite articoloVeloce7;
	@ManyToOne
	private ArticoloLite articoloVeloce8;

	/**
	 * Costruttore.
	 */
	public PosSettings() {
		super();
	}

	/**
	 * @return the articoloArrotondamenti
	 */
	public ArticoloLite getArticoloArrotondamenti() {
		return articoloArrotondamenti;
	}

	/**
	 * @return the articoloBuono
	 */
	public ArticoloLite getArticoloBuono() {
		return articoloBuono;
	}

	/**
	 * @return the articoloVeloce1
	 */
	public ArticoloLite getArticoloVeloce1() {
		return articoloVeloce1;
	}

	/**
	 * @return the articoloVeloce2
	 */
	public ArticoloLite getArticoloVeloce2() {
		return articoloVeloce2;
	}

	/**
	 * @return the articoloVeloce3
	 */
	public ArticoloLite getArticoloVeloce3() {
		return articoloVeloce3;
	}

	/**
	 * @return the articoloVeloce4
	 */
	public ArticoloLite getArticoloVeloce4() {
		return articoloVeloce4;
	}

	/**
	 * @return the articoloVeloce5
	 */
	public ArticoloLite getArticoloVeloce5() {
		return articoloVeloce5;
	}

	/**
	 * @return the articoloVeloce6
	 */
	public ArticoloLite getArticoloVeloce6() {
		return articoloVeloce6;
	}

	/**
	 * @return the articoloVeloce7
	 */
	public ArticoloLite getArticoloVeloce7() {
		return articoloVeloce7;
	}

	/**
	 * @return the articoloVeloce8
	 */
	public ArticoloLite getArticoloVeloce8() {
		return articoloVeloce8;
	}

	/**
	 * @return the codiceAzienda
	 */
	public String getCodiceAzienda() {
		return codiceAzienda;
	}

	/**
	 * @return the listino
	 */
	public Listino getListino() {
		return listino;
	}

	/**
	 * @param articoloArrotondamenti
	 *            the articoloArrotondamenti to set
	 */
	public void setArticoloArrotondamenti(ArticoloLite articoloArrotondamenti) {
		this.articoloArrotondamenti = articoloArrotondamenti;
	}

	/**
	 * @param articoloBuono
	 *            the articoloBuono to set
	 */
	public void setArticoloBuono(ArticoloLite articoloBuono) {
		this.articoloBuono = articoloBuono;
	}

	/**
	 * @param articoloVeloce1
	 *            the articoloVeloce1 to set
	 */
	public void setArticoloVeloce1(ArticoloLite articoloVeloce1) {
		this.articoloVeloce1 = articoloVeloce1;
	}

	/**
	 * @param articoloVeloce2
	 *            the articoloVeloce2 to set
	 */
	public void setArticoloVeloce2(ArticoloLite articoloVeloce2) {
		this.articoloVeloce2 = articoloVeloce2;
	}

	/**
	 * @param articoloVeloce3
	 *            the articoloVeloce3 to set
	 */
	public void setArticoloVeloce3(ArticoloLite articoloVeloce3) {
		this.articoloVeloce3 = articoloVeloce3;
	}

	/**
	 * @param articoloVeloce4
	 *            the articoloVeloce4 to set
	 */
	public void setArticoloVeloce4(ArticoloLite articoloVeloce4) {
		this.articoloVeloce4 = articoloVeloce4;
	}

	/**
	 * @param articoloVeloce5
	 *            the articoloVeloce5 to set
	 */
	public void setArticoloVeloce5(ArticoloLite articoloVeloce5) {
		this.articoloVeloce5 = articoloVeloce5;
	}

	/**
	 * @param articoloVeloce6
	 *            the articoloVeloce6 to set
	 */
	public void setArticoloVeloce6(ArticoloLite articoloVeloce6) {
		this.articoloVeloce6 = articoloVeloce6;
	}

	/**
	 * @param articoloVeloce7
	 *            the articoloVeloce7 to set
	 */
	public void setArticoloVeloce7(ArticoloLite articoloVeloce7) {
		this.articoloVeloce7 = articoloVeloce7;
	}

	/**
	 * @param articoloVeloce8
	 *            the articoloVeloce8 to set
	 */
	public void setArticoloVeloce8(ArticoloLite articoloVeloce8) {
		this.articoloVeloce8 = articoloVeloce8;
	}

	/**
	 * @param codiceAzienda
	 *            the codiceAzienda to set
	 */
	public void setCodiceAzienda(String codiceAzienda) {
		this.codiceAzienda = codiceAzienda;
	}

	/**
	 * @param listino
	 *            the listino to set
	 */
	public void setListino(Listino listino) {
		this.listino = listino;
	}

}
