/**
 *
 */
package it.eurotn.panjea.beniammortizzabili2.domain;

import java.math.BigDecimal;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 * @author Leonardo
 *
 */
@Entity
@DiscriminatorValue("PCB")
@NamedQueries({
		@NamedQuery(name = "PoliticaCalcoloBene.caricaCivilisticaByQuota", query = " select pcb from PoliticaCalcoloBene pcb where pcb.quotaCivilistica.id = :paramIdQuota "),
		@NamedQuery(name = "PoliticaCalcoloBene.caricaFiscaleByQuota", query = " select pcb from PoliticaCalcoloBene pcb where pcb.quotaFiscale.id = :paramIdQuota "),
		@NamedQuery(name = "PoliticaCalcoloBene.caricaByIdSimulazione", query = " from PoliticaCalcoloBene pcb where pcb.simulazione.id = :paramIdSimulazione "),
		@NamedQuery(name = "PoliticaCalcoloBene.caricaImportoAnticipatoSimulazioniCollegate", query = " select new it.eurotn.panjea.beniammortizzabili.util.QuotaAmmortamentoFiscaleImporto(pc.bene.id, sum(pc.quotaFiscale.impQuotaAmmortamentoOrdinario), sum(pc.quotaFiscale.impQuotaAmmortamentoAnticipato) ) "
				+ " from PoliticaCalcoloBene pc "
				+ " where pc.simulazione.id = :paramIdSimulazione and "
				+ "   pc.bene.id = :paramIdBeneAmmortizzabile and "
				+ "   pc.quotaFiscale.consolidata = false "
				+ " group by pc.bene.id ") })
public class PoliticaCalcoloBene extends PoliticaCalcolo {

	private static final long serialVersionUID = 2748155040812553036L;

	public static final String PROP_BENE = "bene";
	public static final String PROP_QUOTA_CIVILISTICA = "quotaCivilistica";
	public static final String PROP_QUOTA_FISCALE = "quotaFiscale";

	@ManyToOne
	private BeneAmmortizzabileLite bene;

	@ManyToOne(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
	private QuotaAmmortamentoCivilistico quotaCivilistica;

	@ManyToOne(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
	private QuotaAmmortamentoFiscale quotaFiscale;

	// rappresenta l'importo soggetto da ammortamento del bene utilizzato per il
	// calcolo della politica
	@Column(precision = 19, scale = 6)
	private BigDecimal importoSoggettoAmmortamento;

	/**
	 * Costruttore di default.
	 */
	public PoliticaCalcoloBene() {
		super();
		initialize();
	}

	/**
	 * @return the bene
	 */
	public BeneAmmortizzabileLite getBene() {
		return bene;
	}

	@Override
	public String getCodiceEntitaPoliticaCalcolo() {
		return getBene() != null ? (getBene().getCodice() != null ? getBene().getCodice().toString() : null) : null;
	}

	@Override
	public int getDeep() {
		return DEEP_BENE;
	}

	@Override
	public String getDescrizioneEntitaPoliticaCalcolo() {
		return getBene() != null ? getBene().getDescrizione() : null;
	}

	/**
	 * @return the importoSoggettoAmmortamento
	 */
	public BigDecimal getImportoSoggettoAmmortamento() {
		return importoSoggettoAmmortamento;
	}

	/**
	 * @return the quotaCivilistica
	 */
	public QuotaAmmortamentoCivilistico getQuotaCivilistica() {
		return quotaCivilistica;
	}

	/**
	 * @return the quotaFiscale
	 */
	public QuotaAmmortamentoFiscale getQuotaFiscale() {
		return quotaFiscale;
	}

	/**
	 * Inizializza i valori delle variabili dell'istanza.
	 */
	private void initialize() {
		bene = new BeneAmmortizzabileLite();
		quotaCivilistica = new QuotaAmmortamentoCivilistico();
		quotaFiscale = new QuotaAmmortamentoFiscale();
	}

	/**
	 * @param bene
	 *            the bene to set
	 */
	public void setBene(BeneAmmortizzabileLite bene) {
		this.bene = bene;
	}

	/**
	 * @param importoSoggettoAmmortamento
	 *            the importoSoggettoAmmortamento to set
	 */
	public void setImportoSoggettoAmmortamento(BigDecimal importoSoggettoAmmortamento) {
		this.importoSoggettoAmmortamento = importoSoggettoAmmortamento;
	}

	/**
	 * @param quotaCivilistica
	 *            the quotaCivilistica to set
	 */
	public void setQuotaCivilistica(QuotaAmmortamentoCivilistico quotaCivilistica) {
		this.quotaCivilistica = quotaCivilistica;
	}

	/**
	 * @param quotaFiscale
	 *            the quotaFiscale to set
	 */
	public void setQuotaFiscale(QuotaAmmortamentoFiscale quotaFiscale) {
		this.quotaFiscale = quotaFiscale;
	}

}
