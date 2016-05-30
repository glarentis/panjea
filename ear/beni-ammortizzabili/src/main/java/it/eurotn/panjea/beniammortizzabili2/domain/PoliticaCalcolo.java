/**
 *
 */
package it.eurotn.panjea.beniammortizzabili2.domain;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.contabilita.domain.AreaContabileLite;

import java.math.BigDecimal;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @author Leonardo
 *
 */
@Entity
@Table(name = "bamm_politica_calcolo")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TIPO_POLITICA", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("PC")
@NamedQueries({ @NamedQuery(name = "PoliticaCalcolo.caricaPoliticheSimulazione", query = " from PoliticaCalcolo pc where pc.simulazione.id = :paramIdSimulazione ") })
public abstract class PoliticaCalcolo extends EntityBase {

	private static final long serialVersionUID = -6603308725342647053L;

	public static final int DEEP_GRUPPO = 0;
	public static final int DEEP_SPECIE = 1;
	public static final int DEEP_SOTTOSPECIE = 2;
	public static final int DEEP_BENE = 3;

	public static final String REF = "PoliticaCalcolo";
	public static final String PROP_ID = "id";
	public static final String PROP_USER_INSERT = "userInsert";
	public static final String PROP_DATE_INSERT = "dateInsert";
	public static final String PROP_USER_UPDATE = "userUpdate";
	public static final String PROP_DATE_UPDATE = "dateUpdate";
	public static final String PROP_TOTALE_ORDINARIO = "totaleOrdinario";
	public static final String PROP_TOTALE_ANTICIPATO = "totaleAnticipato";
	public static final String PROP_INDICE_POLITICA = "indicePolitica";
	public static final String PROP_AMMORTAMENTO_ANTICIPATO = "ammortamentoAnticipato";
	public static final String PROP_PERC_AMMORTAMENTO_ORDINARIO = "percAmmortamentoOrdinario";
	public static final String PROP_SIMULAZIONE = "simulazione";
	public static final String PROP_PERC_AMMORTAMENTO_ACCELERATO = "percAmmortamentoAccelerato";
	public static final String PROP_MAGGIORE_UTILIZZO = "maggioreUtilizzo";
	public static final String PROP_AMMORTAMENTO_RIDOTTO = "ammortamentoRidotto";
	public static final String PROP_MINORE_UTILIZZO = "minoreUtilizzo";
	public static final String PROP_PERC_AMMORTAMENTO_ANTICIPATO = "percAmmortamentoAnticipato";
	public static final String PROP_PERC_MAGGIORE_UTILIZZO = "percMaggioreUtilizzo";
	public static final String PROP_PERC_MINORE_UTILIZZO = "percMinoreUtilizzo";
	public static final String PROP_AMMORTAMENTO_ORDINARIO = "ammortamentoOrdinario";
	public static final String PROP_AMMORTAMENTO_ACCELERATO = "ammortamentoAccelerato";
	public static final String PROP_PERC_AMMORTAMENTO_RIDOTTO = "percAmmortamentoRidotto";

	private Integer indicePolitica = null;
	@ManyToOne
	private Simulazione simulazione;

	@Embedded
	private PoliticaCalcoloFiscale politicaCalcoloFiscale;

	@Embedded
	private PoliticaCalcoloCivilistica politicaCalcoloCivilistica;

	@Transient
	private boolean dirty;
	@Transient
	private BigDecimal totaleOrdinario;
	@Transient
	private BigDecimal totaleAnticipato;

	@ManyToOne
	private AreaContabileLite areaContabile;

	/**
	 * Costruttore di default.
	 */
	public PoliticaCalcolo() {
		super();
		initialize();
	}

	/**
	 * @return the areaContabile
	 */
	public AreaContabileLite getAreaContabile() {
		return areaContabile;
	}

	/**
	 * Codice dell'entità della politica di calcolo.
	 *
	 * @return codice dell'entità
	 */
	public abstract String getCodiceEntitaPoliticaCalcolo();

	/**
	 * Profondità della politica di calcolo.
	 *
	 * @return profondità
	 */
	public abstract int getDeep();

	/**
	 * Descrizione dell'entita della politica di calcolo.
	 *
	 * @return descrizione dell'entità
	 */
	public abstract String getDescrizioneEntitaPoliticaCalcolo();

	/**
	 * @return the indicePolitica
	 */
	public Integer getIndicePolitica() {
		return indicePolitica;
	}

	/**
	 * @return the politicaCalcoloCivilistica
	 */
	public PoliticaCalcoloCivilistica getPoliticaCalcoloCivilistica() {
		return politicaCalcoloCivilistica;
	}

	/**
	 * @return the politicaCalcoloFiscale
	 */
	public PoliticaCalcoloFiscale getPoliticaCalcoloFiscale() {
		return politicaCalcoloFiscale;
	}

	/**
	 * @return the simulazione
	 */
	public Simulazione getSimulazione() {
		return simulazione;
	}

	/**
	 * @return the totaleAnticipato
	 */
	public BigDecimal getTotaleAnticipato() {
		return totaleAnticipato;
	}

	/**
	 * @return the totaleOrdinario
	 */
	public BigDecimal getTotaleOrdinario() {
		return totaleOrdinario;
	}

	/**
	 * Inizializza i valori delle variabili dell'istanza.
	 */
	private void initialize() {
		politicaCalcoloFiscale = new PoliticaCalcoloFiscale();
		politicaCalcoloCivilistica = new PoliticaCalcoloCivilistica();
		simulazione = new Simulazione();
	}

	/**
	 * @return the dirty
	 */
	public boolean isDirty() {
		return dirty;
	}

	/**
	 * @param areaContabile
	 *            the areaContabile to set
	 */
	public void setAreaContabile(AreaContabileLite areaContabile) {
		this.areaContabile = areaContabile;
	}

	/**
	 * @param dirty
	 *            the dirty to set
	 */
	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}

	/**
	 * @param indicePolitica
	 *            the indicePolitica to set
	 */
	public void setIndicePolitica(Integer indicePolitica) {
		this.indicePolitica = indicePolitica;
	}

	/**
	 * @param politicaCalcoloCivilistica
	 *            the politicaCalcoloCivilistica to set
	 */
	public void setPoliticaCalcoloCivilistica(PoliticaCalcoloCivilistica politicaCalcoloCivilistica) {
		this.politicaCalcoloCivilistica = politicaCalcoloCivilistica;
	}

	/**
	 * @param politicaCalcoloFiscale
	 *            the politicaCalcoloFiscale to set
	 */
	public void setPoliticaCalcoloFiscale(PoliticaCalcoloFiscale politicaCalcoloFiscale) {
		this.politicaCalcoloFiscale = politicaCalcoloFiscale;
	}

	/**
	 * @param simulazione
	 *            the simulazione to set
	 */
	public void setSimulazione(Simulazione simulazione) {
		this.simulazione = simulazione;
	}

	/**
	 * @param totaleAnticipato
	 *            the totaleAnticipato to set
	 */
	public void setTotaleAnticipato(BigDecimal totaleAnticipato) {
		this.totaleAnticipato = totaleAnticipato;
	}

	/**
	 * @param totaleOrdinario
	 *            the totaleOrdinario to set
	 */
	public void setTotaleOrdinario(BigDecimal totaleOrdinario) {
		this.totaleOrdinario = totaleOrdinario;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("PoliticaCalcolo[");
		buffer.append("indicePolitica = ").append(indicePolitica);
		buffer.append(" politicaCalcoloCivilistica = ").append(politicaCalcoloCivilistica);
		buffer.append(" politicaCalcoloFiscale = ").append(politicaCalcoloFiscale);
		buffer.append(" simulazione = ").append(simulazione != null ? simulazione.getId() : null);
		buffer.append("]");
		return buffer.toString();
	}

}
