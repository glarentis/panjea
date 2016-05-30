package it.eurotn.panjea.conai.domain;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.conai.domain.ConaiArticolo.ConaiMateriale;
import it.eurotn.panjea.conai.domain.ConaiArticolo.ConaiTipoImballo;
import it.eurotn.panjea.magazzino.domain.RigaArticolo;
import it.eurotn.util.PanjeaEJBUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

@Entity
@Audited
@Table(name = "maga_conai_righe_componente")
public class RigaConaiComponente extends EntityBase implements Cloneable {

	private static final long serialVersionUID = 8013241945713777159L;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	private RigaArticolo rigaArticolo;

	@Enumerated
	private ConaiMateriale materiale;

	@Enumerated
	private ConaiTipoImballo tipoImballo;

	@Column(precision = 19, scale = 6)
	private BigDecimal pesoUnitario;

	@Column(precision = 19, scale = 6)
	private BigDecimal pesoEsenzione;

	@Column(precision = 5, scale = 2)
	private BigDecimal percentualeEsenzione;

	private Integer numeroDecimaliQta;

	/**
	 * Calcola il peso esenzione con il peso totale e la percentuale esenzione salvata.
	 */
	public void calcolaEsenzione() {
		pesoEsenzione = getPesoTotale().multiply(percentualeEsenzione).divide(BigDecimal.valueOf(100.000000))
				.setScale(numeroDecimaliQta, RoundingMode.HALF_UP);
	}

	@Override
	public Object clone() {
		RigaConaiComponente rigaConaiComponente = (RigaConaiComponente) PanjeaEJBUtil.cloneObject(this);
		rigaConaiComponente.setId(null);
		rigaConaiComponente.setVersion(null);
		rigaConaiComponente.setTimeStamp(Calendar.getInstance().getTimeInMillis());
		return rigaConaiComponente;
	}

	/**
	 * @return the materiale
	 */
	public ConaiMateriale getMateriale() {
		return materiale;
	}

	/**
	 * @return Returns the numeroDecimaliQta.
	 */
	public Integer getNumeroDecimaliQta() {
		return numeroDecimaliQta;
	}

	/**
	 * @return the percentualeEsenzione
	 */
	public BigDecimal getPercentualeEsenzione() {
		return percentualeEsenzione;
	}

	/**
	 * @return the peso
	 */
	public BigDecimal getPeso() {
		BigDecimal pesoTotale = (BigDecimal) PanjeaEJBUtil.cloneObject(getPesoTotale());
		if (percentualeEsenzione != null) {
			pesoTotale = getPesoTotale().subtract(getPesoEsenzione());
		}
		return pesoTotale;
	}

	/**
	 * @return the peso esenzione
	 */
	public BigDecimal getPesoEsenzione() {
		return pesoEsenzione;
	}

	/**
	 * 
	 * @return peso totale della riga conai
	 */
	public BigDecimal getPesoTotale() {
		return pesoUnitario.multiply(BigDecimal.valueOf(rigaArticolo.getQta())).setScale(numeroDecimaliQta,
				RoundingMode.HALF_UP);
	}

	/**
	 * @return the pesoUnitario
	 */
	public BigDecimal getPesoUnitario() {
		return pesoUnitario;
	}

	/**
	 * @return the rigaArticolo
	 */
	public RigaArticolo getRigaArticolo() {
		return rigaArticolo;
	}

	/**
	 * @return the tipoImballo
	 */
	public ConaiTipoImballo getTipoImballo() {
		return tipoImballo;
	}

	/**
	 * Inizializza i valori materiale, tipoImballo e peso da ConaiComponente.
	 * 
	 * @param conaiComponente
	 *            il componente conai da cui copiare i dati
	 * @param paramNumeroDecimaliQta
	 *            numero decimali della quantità
	 */
	public void initialize(ConaiComponente conaiComponente, Integer paramNumeroDecimaliQta) {
		PanjeaEJBUtil.copyProperties(this, conaiComponente);
		setId(null);
		setVersion(null);

		setNumeroDecimaliQta(paramNumeroDecimaliQta);
		setPesoUnitario(getPesoUnitario().setScale(getNumeroDecimaliQta(), RoundingMode.HALF_UP));
	}

	/**
	 * @param materiale
	 *            the materiale to set
	 */
	public void setMateriale(ConaiMateriale materiale) {
		this.materiale = materiale;
	}

	/**
	 * @param numeroDecimaliQta
	 *            The numeroDecimaliQta to set.
	 */
	public void setNumeroDecimaliQta(Integer numeroDecimaliQta) {
		this.numeroDecimaliQta = numeroDecimaliQta;
	}

	/**
	 * @param percentualeEsenzione
	 *            the percentualeEsenzione to set
	 */
	public void setPercentualeEsenzione(BigDecimal percentualeEsenzione) {
		this.percentualeEsenzione = percentualeEsenzione;
	}

	/**
	 * @param pesoEsenzione
	 *            the pesoEsenzione to set
	 */
	public void setPesoEsenzione(BigDecimal pesoEsenzione) {
		this.pesoEsenzione = pesoEsenzione;
	}

	/**
	 * @param pesoUnitario
	 *            The pesoUnitario to set.
	 */
	public void setPesoUnitario(BigDecimal pesoUnitario) {
		this.pesoUnitario = pesoUnitario;
	}

	/**
	 * @param rigaArticolo
	 *            the rigaArticolo to set
	 */
	public void setRigaArticolo(RigaArticolo rigaArticolo) {
		this.rigaArticolo = rigaArticolo;
	}

	/**
	 * @param tipoImballo
	 *            the tipoImballo to set
	 */
	public void setTipoImballo(ConaiTipoImballo tipoImballo) {
		this.tipoImballo = tipoImballo;
	}
}
