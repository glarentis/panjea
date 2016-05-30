/**
 *
 */
package it.eurotn.panjea.beniammortizzabili2.domain;

import java.math.BigDecimal;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 * @author Leonardo
 *
 */
@Entity
@DiscriminatorValue("F")
@NamedQueries({
	@NamedQuery(name = "QuotaAmmortamentoFiscale.caricaImportiFiscaliConsolidati", query = "select new it.eurotn.panjea.beniammortizzabili.util.QuotaAmmortamentoFiscaleImporto(b.id, sum(q.impQuotaAmmortamentoOrdinario), sum(q.impQuotaAmmortamentoAnticipato) ) "
			+ " from QuotaAmmortamentoFiscale q join q.beneAmmortizzabile b "
			+ " where b.codiceAzienda = :paramCodiceAzienda and " + " q.consolidata = true  " + " group by b.id  "),
			@NamedQuery(name = "QuotaAmmortamentoFiscale.caricaImportiFiscaliConsolidatiPerBene", query = "select new it.eurotn.panjea.beniammortizzabili.util.QuotaAmmortamentoFiscaleImporto(b.id, sum(q.impQuotaAmmortamentoOrdinario), sum(q.impQuotaAmmortamentoAnticipato) ) "
					+ " from QuotaAmmortamentoFiscale q join q.beneAmmortizzabile b "
					+ " where b.id = :paramIdBeneAmmortizzabile and " + " q.consolidata = true  " + " group by b.id  ")

})
public class QuotaAmmortamentoFiscale extends QuotaAmmortamento {

	private static final long serialVersionUID = 6868062310080109776L;

	public static final String PROP_PERC_QUOTA_AMMORTAMENTO_ANTICIPATO = "percQuotaAmmortamentoAnticipato";
	public static final String PROP_IMP_QUOTA_AMMORTAMENTO_ANTICIPATO = "impQuotaAmmortamentoAnticipato";
	public static final String PROP_PERC_QUOTA_AMMORTAMENTO_ACCELERATO = "percQuotaAmmortamentoAccelerato";
	public static final String PROP_IMP_QUOTA_AMMORTAMENTO_ACCELERATO = "impQuotaAmmortamentoAccelerato";
	public static final String PROP_PERC_QUOTA_AMMORTAMENTO_RIDOTTO = "percQuotaAmmortamentoRidotto";
	public static final String PROP_IMP_QUOTA_AMMORTAMENTO_RIDOTTO = "impQuotaAmmortamentoRidotto";

	private double percQuotaAmmortamentoAnticipato;
	private double percQuotaAmmortamentoAccelerato;
	private double percQuotaAmmortamentoRidotto;

	private BigDecimal impQuotaAmmortamentoAnticipato;
	private BigDecimal impQuotaAmmortamentoAccelerato;
	private BigDecimal impQuotaAmmortamentoRidotto;

	/**
	 * Costruttore di default.
	 */
	public QuotaAmmortamentoFiscale() {
		super();
		initialize();
	}

	/**
	 * @return the impQuotaAmmortamentoAccelerato
	 */
	public BigDecimal getImpQuotaAmmortamentoAccelerato() {
		return impQuotaAmmortamentoAccelerato;
	}

	/**
	 * @return the impQuotaAmmortamentoAnticipato
	 */
	public BigDecimal getImpQuotaAmmortamentoAnticipato() {
		return impQuotaAmmortamentoAnticipato;
	}

	/**
	 * @return the impQuotaAmmortamentoRidotto
	 */
	public BigDecimal getImpQuotaAmmortamentoRidotto() {
		return impQuotaAmmortamentoRidotto;
	}

	/**
	 * @return the percQuotaAmmortamentoAccelerato
	 */
	public double getPercQuotaAmmortamentoAccelerato() {
		return percQuotaAmmortamentoAccelerato;
	}

	/**
	 * @return the percQuotaAmmortamentoAnticipato
	 */
	public double getPercQuotaAmmortamentoAnticipato() {
		return percQuotaAmmortamentoAnticipato;
	}

	/**
	 * @return the percQuotaAmmortamentoAnticipatoApplicata
	 */
	public Double getPercQuotaAmmortamentoAnticipatoApplicata() {
		if (isPercPrimoAnnoApplicata()) {
			return new BigDecimal(percQuotaAmmortamentoAnticipato).setScale(6).divide(new BigDecimal(2), 2)
					.doubleValue();
		}
		return percQuotaAmmortamentoAnticipato;
	}

	/**
	 * @return the percQuotaAmmortamentoRidotto
	 */
	public double getPercQuotaAmmortamentoRidotto() {
		return percQuotaAmmortamentoRidotto;
	}

	@Override
	public BigDecimal getTotaleAnno() {
		BigDecimal impOrdinario = BigDecimal.ZERO;
		BigDecimal impAnticipato = BigDecimal.ZERO;
		BigDecimal impAccelerato = BigDecimal.ZERO;

		if (getImpQuotaAmmortamentoOrdinario() != null) {
			impOrdinario = getImpQuotaAmmortamentoOrdinario();
		}

		if (getImpQuotaAmmortamentoAnticipato() != null) {
			impAnticipato = getImpQuotaAmmortamentoAnticipato();
		}

		if (getImpQuotaAmmortamentoAccelerato() != null) {
			impAccelerato = getImpQuotaAmmortamentoAccelerato();
		}
		return impOrdinario.add(impAnticipato).add(impAccelerato);
	}

	/**
	 * Inizializza i valori delle variabili dell'istanza.
	 */
	private void initialize() {
		percQuotaAmmortamentoAnticipato = 0;
		percQuotaAmmortamentoAccelerato = 0;
		percQuotaAmmortamentoRidotto = 0;
		impQuotaAmmortamentoAnticipato = BigDecimal.ZERO;
		impQuotaAmmortamentoAccelerato = BigDecimal.ZERO;
		impQuotaAmmortamentoRidotto = BigDecimal.ZERO;
	}

	/**
	 * @param impQuotaAmmortamentoAccelerato
	 *            the impQuotaAmmortamentoAccelerato to set
	 */
	public void setImpQuotaAmmortamentoAccelerato(BigDecimal impQuotaAmmortamentoAccelerato) {
		this.impQuotaAmmortamentoAccelerato = impQuotaAmmortamentoAccelerato;
	}

	/**
	 * @param impQuotaAmmortamentoAnticipato
	 *            the impQuotaAmmortamentoAnticipato to set
	 */
	public void setImpQuotaAmmortamentoAnticipato(BigDecimal impQuotaAmmortamentoAnticipato) {
		this.impQuotaAmmortamentoAnticipato = impQuotaAmmortamentoAnticipato;
	}

	/**
	 * @param impQuotaAmmortamentoRidotto
	 *            the impQuotaAmmortamentoRidotto to set
	 */
	public void setImpQuotaAmmortamentoRidotto(BigDecimal impQuotaAmmortamentoRidotto) {
		this.impQuotaAmmortamentoRidotto = impQuotaAmmortamentoRidotto;
	}

	/**
	 * @param percQuotaAmmortamentoAccelerato
	 *            the percQuotaAmmortamentoAccelerato to set
	 */
	public void setPercQuotaAmmortamentoAccelerato(double percQuotaAmmortamentoAccelerato) {
		this.percQuotaAmmortamentoAccelerato = percQuotaAmmortamentoAccelerato;
	}

	/**
	 * @param percQuotaAmmortamentoAnticipato
	 *            the percQuotaAmmortamentoAnticipato to set
	 */
	public void setPercQuotaAmmortamentoAnticipato(double percQuotaAmmortamentoAnticipato) {
		this.percQuotaAmmortamentoAnticipato = percQuotaAmmortamentoAnticipato;
	}

	/**
	 * @param percQuotaAmmortamentoRidotto
	 *            the percQuotaAmmortamentoRidotto to set
	 */
	public void setPercQuotaAmmortamentoRidotto(double percQuotaAmmortamentoRidotto) {
		this.percQuotaAmmortamentoRidotto = percQuotaAmmortamentoRidotto;
	}

	@Override
	public String toString() {
		String superString = super.toString();
		StringBuffer buffer = new StringBuffer();
		buffer.append("QuotaAmmortamentoFiscale[");
		buffer.append("impQuotaAmmortamentoAccelerato = ").append(impQuotaAmmortamentoAccelerato);
		buffer.append(" impQuotaAmmortamentoAnticipato = ").append(impQuotaAmmortamentoAnticipato);
		buffer.append(" impQuotaAmmortamentoRidotto = ").append(impQuotaAmmortamentoRidotto);
		buffer.append(" percQuotaAmmortamentoAccelerato = ").append(percQuotaAmmortamentoAccelerato);
		buffer.append(" percQuotaAmmortamentoAnticipato = ").append(percQuotaAmmortamentoAnticipato);
		buffer.append(" percQuotaAmmortamentoRidotto = ").append(percQuotaAmmortamentoRidotto);
		buffer.append("]");
		return superString + buffer.toString();
	}

}
