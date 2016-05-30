package it.eurotn.panjea.tesoreria.solleciti;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.rate.domain.Rata;

import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;

@Entity
@Audited
@Table(name = "part_righe_solleciti")
public class RigaSollecito extends EntityBase {

	private static final long serialVersionUID = -8625471727333873816L;

	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	private Sollecito sollecito;

	@ManyToOne(fetch = FetchType.EAGER)
	private Rata rata;

	@Temporal(TemporalType.DATE)
	private Date dataScadenza;

	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "importoInValuta", column = @Column(name = "importoInValutaResiduo", precision = 19, scale = 6)),
			@AttributeOverride(name = "importoInValutaAzienda", column = @Column(name = "importoInValutaAziendaResiduo", precision = 19, scale = 6)),
			@AttributeOverride(name = "tassoDiCambio", column = @Column(name = "tassoDiCambioResiduo", precision = 12, scale = 6)),
			@AttributeOverride(name = "codiceValuta", column = @Column(name = "codiceValutaResiduo", length = 3)) })
	private Importo residuo;

	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "importoInValuta", column = @Column(name = "importoInValuta", precision = 19, scale = 6)),
			@AttributeOverride(name = "importoInValutaAzienda", column = @Column(name = "importoInValutaAzienda", precision = 19, scale = 6)),
			@AttributeOverride(name = "tassoDiCambio", column = @Column(name = "tassoDiCambio", precision = 12, scale = 6)),
			@AttributeOverride(name = "codiceValuta", column = @Column(name = "codiceValuta", length = 3)) })
	private Importo importo;

	/**
	 * @return the dataScadenza
	 */
	public Date getDataScadenza() {
		return dataScadenza;
	}

	/**
	 * @return the importo
	 */
	public Importo getImporto() {
		return importo;
	}

	/**
	 * @return the rata
	 */
	public Rata getRata() {
		return rata;
	}

	/**
	 * @return the residuo
	 */
	public Importo getResiduo() {
		return residuo;
	}

	/**
	 * @return the sollecito
	 */
	public Sollecito getSollecito() {
		return sollecito;
	}

	/**
	 * @param dataScadenza
	 *            the dataScadenza to set
	 */
	public void setDataScadenza(Date dataScadenza) {
		this.dataScadenza = dataScadenza;
	}

	/**
	 * @param importo
	 *            the importo to set
	 */
	public void setImporto(Importo importo) {
		this.importo = importo;
	}

	/**
	 * @param rata
	 *            the rata to set
	 */
	public void setRata(Rata rata) {
		this.rata = rata;
	}

	/**
	 * @param residuo
	 *            the residuo to set
	 */
	public void setResiduo(Importo residuo) {
		this.residuo = residuo;
	}

	/**
	 * @param sollecito
	 *            the sollecito to set
	 */
	public void setSollecito(Sollecito sollecito) {
		this.sollecito = sollecito;
	}
}
