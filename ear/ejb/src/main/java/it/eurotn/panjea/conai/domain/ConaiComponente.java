/**
 *
 */
package it.eurotn.panjea.conai.domain;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.conai.domain.ConaiArticolo.ConaiMateriale;
import it.eurotn.panjea.conai.domain.ConaiArticolo.ConaiTipoImballo;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;

import java.math.BigDecimal;
import java.math.MathContext;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.envers.Audited;

/**
 * Componente conai, definisce il peso di ogni tipo imballo per l'articolo corrente.
 *
 * @author leonardo
 */
@Entity
@Audited
@Table(name = "maga_conai_componenti")
@NamedQueries({ @NamedQuery(name = "ConaiComponente.caricaByArticolo", query = "select cc from ConaiComponente cc where cc.articolo.id=:paramIdArticolo") })
public class ConaiComponente extends EntityBase {

	private static final long serialVersionUID = 3292292557985080004L;

	@ManyToOne
	@JoinColumn(name = "articolo_id", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private ArticoloLite articolo;

	@Enumerated
	private ConaiMateriale materiale;

	@Enumerated
	private ConaiTipoImballo tipoImballo;

	@Column(precision = 10, scale = 6)
	private BigDecimal pesoUnitario;

	/**
	 * Costruttore.
	 */
	public ConaiComponente() {
		super();
	}

	/**
	 * @return the articolo
	 */
	public ArticoloLite getArticolo() {
		return articolo;
	}

	/**
	 * @return the materiale
	 */
	public ConaiMateriale getMateriale() {
		return materiale;
	}

	/**
	 * @return Returns the pesoUnitario.
	 */
	public BigDecimal getPesoUnitario() {
		return pesoUnitario;
	}

	/**
	 *
	 * @return peso unitario in KG
	 */
	public BigDecimal getPesoUnitarioKG() {
		if (pesoUnitario == null) {
			return null;
		}
		MathContext mc = new MathContext(3);
		return pesoUnitario.multiply(new BigDecimal(1000), mc);
	}

	/**
	 * @return the tipoImballo
	 */
	public ConaiTipoImballo getTipoImballo() {
		return tipoImballo;
	}

	/**
	 * @param articolo
	 *            the articolo to set
	 */
	public void setArticolo(ArticoloLite articolo) {
		this.articolo = articolo;
	}

	/**
	 * @param materiale
	 *            the materiale to set
	 */
	public void setMateriale(ConaiMateriale materiale) {
		this.materiale = materiale;
	}

	/**
	 * @param pesoUnitario
	 *            The pesoUnitario to set.
	 */
	public void setPesoUnitario(BigDecimal pesoUnitario) {
		this.pesoUnitario = pesoUnitario;
	}

	/**
	 *
	 * @param pesoUnitarioKG
	 *            peso unitario del conai in kg
	 */
	public void setPesoUnitarioKG(BigDecimal pesoUnitarioKG) {
		if (pesoUnitarioKG != null) {
			MathContext mc = new MathContext(3);
			pesoUnitario = pesoUnitarioKG.divide(new BigDecimal(1000), mc);
		}
	}

	/**
	 * @param tipoImballo
	 *            the tipoImballo to set
	 */
	public void setTipoImballo(ConaiTipoImballo tipoImballo) {
		this.tipoImballo = tipoImballo;
	}

}
