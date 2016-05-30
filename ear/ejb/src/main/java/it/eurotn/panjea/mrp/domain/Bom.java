package it.eurotn.panjea.mrp.domain;

import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.mrp.util.ArticoloConfigurazioneKey;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.ObjectUtils;

/**
 * @author leonardo
 *
 */
public class Bom implements Serializable {

	private static final long serialVersionUID = -7794269882407080295L;

	private Set<Bom> figli;
	private Set<Bom> padri;

	protected BigDecimal costo;
	private Integer idDistinta;
	private String formulaMolt;
	private BigDecimal moltiplicatore; // risultato ottenuto dalla formula.
	private Integer idArticolo;
	private Integer idComponente;
	private Integer idComponentePadre;
	private Integer idConfigurazione;
	private String codiciAttributi;
	private String valoriAttributi;
	private boolean trasmettiAttributi = false;

	private ArticoloConfigurazioneKey key;
	private ArticoloLite articolo;

	private Double qtaAttrezzaggioDistinta;
	private Double qtaAttrezzaggioArticolo;

	{
		figli = new HashSet<>();
		padri = new HashSet<>();
		trasmettiAttributi = false;
	}

	/**
	 * Costruttore.
	 *
	 * @param articolo
	 *            articolo
	 * @param costo
	 *            costo
	 * @param formulaMolt
	 *            formulaMolt
	 */
	public Bom(final ArticoloLite articolo, final BigDecimal costo, final String formulaMolt) {
		this.articolo = articolo;
		this.costo = costo;
		this.formulaMolt = formulaMolt;
	}

	/**
	 * Costruttore.
	 *
	 * @param formulaMolt
	 *            formulaMolt
	 * @param idArticolo
	 *            idArticolo
	 * @param idConfigurazione
	 *            idConfigurazione
	 * @param qtaAttrezzaggioDistinta
	 *            qta attrezzaggio componente
	 * @param qtaAttrezaggioArticolo
	 *            qta attrezzaggio fasi componente
	 */
	public Bom(final Integer idConfigurazione, final int idArticolo, final String formulaMolt,
			final Double qtaAttrezzaggioDistinta, final Double qtaAttrezaggioArticolo) {
		super();
		this.formulaMolt = formulaMolt;
		this.idArticolo = idArticolo;
		this.idConfigurazione = idConfigurazione;
		this.qtaAttrezzaggioDistinta = qtaAttrezzaggioDistinta;
		this.qtaAttrezzaggioArticolo = qtaAttrezaggioArticolo;
	}

	/**
	 * Costruttore.
	 *
	 * @param idDistinta
	 *            bom per id articolo
	 * @param idConfigurazione
	 *            idConfigurazione
	 * @param qtaAttrezzaggioDistinta
	 *            qta attrezzaggio distinta
	 * @param qtaAttrezaggioArticolo
	 *            qta attrezzaggio articolo
	 */
	public Bom(final Integer idDistinta, final Integer idConfigurazione, final Double qtaAttrezzaggioDistinta,
			final Double qtaAttrezaggioArticolo) {
		super();
		this.idDistinta = idDistinta;
		this.idConfigurazione = idConfigurazione;
		this.qtaAttrezzaggioDistinta = qtaAttrezzaggioDistinta;
		this.qtaAttrezzaggioArticolo = qtaAttrezaggioArticolo;
		this.figli = new HashSet<>(15);
		this.padri = Collections.synchronizedSet(new HashSet<Bom>(15));
		this.costo = BigDecimal.ZERO;
	}

	/**
	 * Costruttore.
	 *
	 * @param idComponente
	 *            idComponente
	 * @param idDistinta
	 *            idDistinta
	 * @param formulaMolt
	 *            formulaMolt
	 * @param qtaAttrezzaggioDistinta
	 *            qta attrezzaggio distinta
	 * @param qtaAttrezaggioArticolo
	 *            qta attrezzaggio articolo
	 * @param idArticolo
	 *            idArticolo
	 * @param idComponentePadre
	 *            idComponentePadre
	 * @param idConfigurazione
	 *            idConfigurazione
	 */
	public Bom(final Integer idComponente, final Integer idDistinta, final String formulaMolt,
			final Double qtaAttrezzaggioDistinta, final Double qtaAttrezaggioArticolo, final Integer idArticolo,
			final Integer idComponentePadre, final Integer idConfigurazione) {
		this.idDistinta = idDistinta;
		this.formulaMolt = formulaMolt;
		this.qtaAttrezzaggioDistinta = qtaAttrezzaggioDistinta;
		this.qtaAttrezzaggioArticolo = qtaAttrezaggioArticolo;
		this.idArticolo = idArticolo;
		if (idConfigurazione != null) {
			this.idComponente = idComponente;
			this.idComponentePadre = idComponentePadre;
			this.idConfigurazione = idConfigurazione;
		}
	}

	/**
	 * @param idArticoliFiglio
	 *            add all set idArticoliFiglio
	 */
	public void addFigli(Set<Bom> idArticoliFiglio) {
		figli.addAll(idArticoliFiglio);
	}

	/**
	 * @param figlio
	 *            add figlio
	 */
	public void addFiglio(Bom figlio) {
		figli.add(figlio);
	}

	/**
	 * @param padre
	 *            the padre to add
	 */
	public synchronized void addPadre(Bom padre) {
		padri.add(padre);
	}

	/**
	 * Aggiunge un padre con i parametri per la definizione di un padre.
	 *
	 * @param idComponenteParam
	 *            idComponente
	 * @param idDistintaPadre
	 *            idDistintaPadre
	 * @param formulaMoltParam
	 *            formulaMolt
	 * @param qtaParamAttrezzaggioDistinta
	 *            qta attrezzaggio distinta
	 * @param qtaParamAttrezaggioArticolo
	 *            qta attrezzaggio articolo
	 *
	 * @param idArticoloParam
	 *            idArticolo
	 * @param idComponentePadreParam
	 *            idComponentePadre
	 * @param idConfig
	 *            idConfigurazione
	 */
	public void addPadre(Integer idComponenteParam, Integer idDistintaPadre, String formulaMoltParam,
			final Double qtaParamAttrezzaggioDistinta, final Double qtaParamAttrezaggioArticolo,
			Integer idArticoloParam, Integer idComponentePadreParam, Integer idConfig) {
		padri.add(new Bom(idComponenteParam, idDistintaPadre, formulaMoltParam, qtaParamAttrezzaggioDistinta,
				qtaParamAttrezaggioArticolo, idArticoloParam, idComponentePadreParam, idConfig));
	}

	/**
	 * @param padriToAdd
	 *            add padri to set
	 */
	public void addPadri(Set<Bom> padriToAdd) {
		for (Bom padre : padriToAdd) {
			padri.add(padre);
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Bom other = (Bom) obj;
		if (idConfigurazione == null) {
			if (other.idConfigurazione != null) {
				return false;
			}
		} else if (!idConfigurazione.equals(other.idConfigurazione)) {
			return false;
		}
		if (idDistinta == null) {
			if (other.idDistinta != null) {
				return false;
			}
		} else if (!idDistinta.equals(other.idDistinta)) {
			return false;
		}
		if (idArticolo == null) {
			if (other.idArticolo != null) {
				return false;
			}
		} else if (!idArticolo.equals(other.idArticolo)) {
			return false;
		}
		if (idComponente == null) {
			if (other.idComponente != null) {
				return false;
			}
		} else if (!idComponente.equals(other.idComponente)) {
			return false;
		}

		return true;
	}

	/**
	 * @return Returns the articolo.
	 */
	public ArticoloLite getArticolo() {
		return articolo;
	}

	/**
	 * @return the codiciAttributi
	 */
	public String getCodiciAttributi() {
		return codiciAttributi;
	}

	/**
	 * @return Returns the costo.
	 */
	public BigDecimal getCosto() {
		return costo;
	}

	/**
	 * @return Returns the costo * moltiplicatore.
	 */
	public BigDecimal getCostoCopia() {
		BigDecimal costoSafe = ObjectUtils.defaultIfNull(costo, BigDecimal.ZERO);
		BigDecimal moltiplicatoreSafe = ObjectUtils.defaultIfNull(moltiplicatore, BigDecimal.ZERO);
		return costoSafe.multiply(moltiplicatoreSafe);
	}

	/**
	 * @return Returns the componentiFigli.
	 */
	public Set<Bom> getFigli() {
		return Collections.unmodifiableSet(figli);
	}

	/**
	 * @return Returns the formulaMolt.
	 */
	public String getFormulaMolt() {
		return formulaMolt;
	}

	/**
	 * @return Returns the idArticolo.
	 */
	public Integer getIdArticolo() {
		return idArticolo;
	}

	/**
	 * @return Returns the idComponente.
	 */
	public Integer getIdComponente() {
		return idComponente;
	}

	/**
	 * @return Returns the idComponentePadre.
	 */
	public Integer getIdComponentePadre() {
		return idComponentePadre;
	}

	/**
	 * @return Returns the idConfigurazione.
	 */
	public Integer getIdConfigurazione() {
		return idConfigurazione;
	}

	/**
	 * @return Returns the idDistinta.
	 */
	public Integer getIdDistinta() {
		return idDistinta;
	}

	/**
	 *
	 * @return chiave configurazione
	 */
	public ArticoloConfigurazioneKey getKeyArticoloConfigurazione() {
		return new ArticoloConfigurazioneKey(idArticolo, idConfigurazione);
	}

	/**
	 * @return la chiave del padre
	 */
	public ArticoloConfigurazioneKey getKeyArticoloPadre() {
		if (key == null) {
			key = new ArticoloConfigurazioneKey(idComponentePadre, idConfigurazione);
		}
		return key;
	}

	/**
	 * @return Returns the moltiplicatore.
	 */
	public BigDecimal getMoltiplicatore() {
		return moltiplicatore;
	}

	/**
	 * @return Returns the padri.
	 */
	public Set<Bom> getPadri() {
		return padri;
	}

	/**
	 * @return the qtaAttrezzaggioArticolo
	 */
	public Double getQtaAttrezzaggioArticolo() {
		return ObjectUtils.defaultIfNull(qtaAttrezzaggioArticolo, 0.0);
	}

	/**
	 * @return the qtaAttrezzaggioDistinta
	 */
	public Double getQtaAttrezzaggioDistinta() {
		return ObjectUtils.defaultIfNull(qtaAttrezzaggioDistinta, 0.0);
	}

	/**
	 * @return the valoriAttributi
	 */
	public String getValoriAttributi() {
		return valoriAttributi;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idConfigurazione == null) ? 0 : idConfigurazione.hashCode());
		result = prime * result + ((idDistinta == null) ? 0 : idDistinta.hashCode());
		result = prime * result + ((idComponente == null) ? 0 : idComponente.hashCode());
		result = prime * result + ((idArticolo == null) ? 0 : idArticolo.hashCode());
		return result;
	}

	/**
	 * @return is padre
	 */
	public boolean isPadre() {
		return padri.isEmpty();
	}

	/**
	 * @return the trasmettiAttributi
	 */
	public boolean isTrasmettiAttributi() {
		return trasmettiAttributi;
	}

	/**
	 * @param articolo
	 *            The articolo to set.
	 */
	public void setArticolo(ArticoloLite articolo) {
		this.articolo = articolo;
	}

	/**
	 * @param codiciAttributi
	 *            the codiciAttributi to set
	 */
	public void setCodiciAttributi(String codiciAttributi) {
		this.codiciAttributi = codiciAttributi;
	}

	/**
	 * @param costo
	 *            The costo to set.
	 */
	public void setCosto(BigDecimal costo) {
		this.costo = costo;
	}

	/**
	 * @param formulaMolt
	 *            the formulaMolt to set
	 */
	public void setFormulaMolt(String formulaMolt) {
		this.formulaMolt = formulaMolt;
	}

	/**
	 * @param moltiplicatore
	 *            The moltiplicatore to set.
	 */
	public void setMoltiplicatore(BigDecimal moltiplicatore) {
		this.moltiplicatore = moltiplicatore;
	}

	/**
	 * @param trasmettiAttributi
	 *            the trasmettiAttributi to set
	 */
	public void setTrasmettiAttributi(boolean trasmettiAttributi) {
		this.trasmettiAttributi = trasmettiAttributi;
	}

	/**
	 * @param valoriAttributi
	 *            the valoriAttributi to set
	 */
	public void setValoriAttributi(String valoriAttributi) {
		this.valoriAttributi = valoriAttributi;
	}

	@Override
	public String toString() {
		return "Bom [componenti num=" + figli.size() + ", idDistinta=" + idDistinta + ", idConfigurazione="
				+ idConfigurazione + "]";
	}

}
