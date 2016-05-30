/**
 * 
 */
package it.eurotn.panjea.contabilita.domain;

import it.eurotn.panjea.anagrafica.domain.DatiRitenutaAccontoEntita;
import it.eurotn.util.PanjeaEJBUtil;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

import org.apache.commons.lang3.StringUtils;

/**
 * @author fattazzo
 * 
 */
@Embeddable
public class DatiRitenutaAccontoAreaContabile implements Serializable {

	private static final long serialVersionUID = -7970434374550215311L;

	private BigDecimal imponibileRiferimento;
	private BigDecimal imponibileSoggettoRitenuta;
	private BigDecimal imponibileNonSoggettoRitenuta;
	private BigDecimal speseRimborso;

	@ManyToOne
	private Prestazione prestazione;

	private boolean causaleRitenutaPresente;
	private Double percentualeAliquota;
	private Double percentualeImponibile;
	private String tributo;
	private String sezione;

	private boolean fondoProfessionistiPresente;
	private Double percFondoProfessionisti;

	private boolean contributoPrevidenzialePresente;
	private ETipoContoBase contoBasePrevidenziale;
	private Double percContributivaINPS;
	private Double percContributivaEnasarco;
	private Double percPrevidenzialeCaricoLavoratore;
	private Double percPrevidenzialeCaricoAzienda;

	{
		imponibileRiferimento = BigDecimal.ZERO;
		imponibileSoggettoRitenuta = BigDecimal.ZERO;
		imponibileNonSoggettoRitenuta = BigDecimal.ZERO;
		speseRimborso = BigDecimal.ZERO;

		causaleRitenutaPresente = false;
		percentualeAliquota = 0.0;
		percentualeImponibile = 0.0;

		fondoProfessionistiPresente = false;
		percFondoProfessionisti = 0.0;

		contributoPrevidenzialePresente = false;
		contoBasePrevidenziale = null;
		percContributivaINPS = 0.0;
		percContributivaEnasarco = 0.0;
		percPrevidenzialeCaricoLavoratore = 0.0;
		percPrevidenzialeCaricoAzienda = 0.0;
	}

	/**
	 * Costruttore.
	 */
	public DatiRitenutaAccontoAreaContabile() {
		super();
	}

	/**
	 * Costruttore.
	 * 
	 * @param datiRitenutaAccontoEntita
	 *            dati ritenuta acconto dell'entità utilizzati per avvalorare quelli dell'area contabile
	 * @param tipoAreaContabile
	 *            tipo area contabile
	 * @param imponibileDocumento
	 *            imponibnibile del documento
	 */
	public DatiRitenutaAccontoAreaContabile(final DatiRitenutaAccontoEntita datiRitenutaAccontoEntita,
			final TipoAreaContabile tipoAreaContabile, final BigDecimal imponibileDocumento) {
		super();

		imponibileRiferimento = imponibileDocumento;
		imponibileSoggettoRitenuta = imponibileDocumento;

		if (tipoAreaContabile.getTipoRitenutaAcconto() != null) {
			this.contoBasePrevidenziale = tipoAreaContabile.getTipoRitenutaAcconto().getContoBasePrevidenziale();
		}

		if (datiRitenutaAccontoEntita != null) {
			this.prestazione = datiRitenutaAccontoEntita.getPrestazione();

			this.causaleRitenutaPresente = datiRitenutaAccontoEntita.getCausaleRitenutaAcconto() != null;
			if (this.causaleRitenutaPresente) {
				PanjeaEJBUtil.copyProperties(this, datiRitenutaAccontoEntita.getCausaleRitenutaAcconto());
			}

			this.fondoProfessionistiPresente = datiRitenutaAccontoEntita.getPercFondoProfessionisti().compareTo(0.0) != 0;
			percFondoProfessionisti = datiRitenutaAccontoEntita.getPercFondoProfessionisti();

			ContributoPrevidenziale contributo = datiRitenutaAccontoEntita.getContributoPrevidenziale();
			this.contributoPrevidenzialePresente = contributo != null;
			if (this.contributoPrevidenzialePresente) {
				if (this.contoBasePrevidenziale == ETipoContoBase.QUOTA_INPS) {
					this.percContributivaINPS = datiRitenutaAccontoEntita.getContributoPrevidenziale()
							.getPercContributiva();
				} else {
					this.percContributivaEnasarco = datiRitenutaAccontoEntita.getContributoPrevidenziale()
							.getPercContributiva();
				}
				this.percPrevidenzialeCaricoLavoratore = contributo.getPercCaricoLavoratore();
				this.percPrevidenzialeCaricoAzienda = contributo.getPercCaricoAzienda();
			}
		}
	}

	/**
	 * @return the contoBasePrevidenziale
	 */
	public ETipoContoBase getContoBasePrevidenziale() {
		return contoBasePrevidenziale;
	}

	/**
	 * @return the imponibileNonSoggettoRitenuta
	 */
	public BigDecimal getImponibileNonSoggettoRitenuta() {
		return imponibileNonSoggettoRitenuta;
	}

	/**
	 * @return the imponibileRiferimento
	 */
	public BigDecimal getImponibileRiferimento() {
		return imponibileRiferimento;
	}

	/**
	 * @return the imponibileSoggettoRitenuta
	 */
	public BigDecimal getImponibileSoggettoRitenuta() {
		return imponibileSoggettoRitenuta;
	}

	/**
	 * @return the percContributiva
	 */
	public Double getPercContributiva() {
		Double result = 0.0;
		if (contoBasePrevidenziale != null) {
			switch (contoBasePrevidenziale) {
			case QUOTA_INPS:
				result = getPercContributivaINPS();
				break;
			case QUOTA_ENASARCO:
				result = getPercContributivaEnasarco();
				break;
			default:
				result = 0.0;
				break;
			}
		}
		return result;
	}

	/**
	 * @return the percContributivaEnasarco
	 */
	public Double getPercContributivaEnasarco() {
		return percContributivaEnasarco;
	}

	/**
	 * @return the percContributivaINPS
	 */
	public Double getPercContributivaINPS() {
		return percContributivaINPS;
	}

	/**
	 * @return the percentualeAliquota
	 */
	public Double getPercentualeAliquota() {
		return percentualeAliquota;
	}

	/**
	 * @return the percentualeImponibile
	 */
	public Double getPercentualeImponibile() {
		return percentualeImponibile;
	}

	/**
	 * @return the percFondoProfessionisti
	 */
	public Double getPercFondoProfessionisti() {
		return percFondoProfessionisti;
	}

	/**
	 * @return the percPrevidenzialeCaricoAzienda
	 */
	public Double getPercPrevidenzialeCaricoAzienda() {
		return percPrevidenzialeCaricoAzienda;
	}

	/**
	 * @return the percPrevidenzialeCaricoLavoratore
	 */
	public Double getPercPrevidenzialeCaricoLavoratore() {
		return percPrevidenzialeCaricoLavoratore;
	}

	/**
	 * @return the prestazione
	 */
	public Prestazione getPrestazione() {
		return prestazione;
	}

	/**
	 * @return the sezione
	 */
	public String getSezione() {
		return sezione;
	}

	/**
	 * @return the speseRimborso
	 */
	public BigDecimal getSpeseRimborso() {
		return speseRimborso;
	}

	/**
	 * @return the tributo
	 */
	public String getTributo() {
		return tributo;
	}

	/**
	 * @return the causaleRitenutaPresente
	 */
	public boolean isCausaleRitenutaPresente() {
		return causaleRitenutaPresente;
	}

	/**
	 * Verifica se i dati della ritenuta sono diversi.
	 * 
	 * @param datiConfronto
	 *            dati di confronto
	 * @return <code>true</code> se almeno un valore è diverso
	 */
	public boolean isChanged(DatiRitenutaAccontoAreaContabile datiConfronto) {
		boolean result = false;

		result = result
				|| getImponibileSoggettoRitenuta().compareTo(datiConfronto.getImponibileSoggettoRitenuta()) != 0;

		result = result || isFondoProfessionistiPresente() != datiConfronto.isFondoProfessionistiPresente();
		result = result || getPercFondoProfessionisti().compareTo(datiConfronto.getPercFondoProfessionisti()) != 0;

		result = result || isCausaleRitenutaPresente() != datiConfronto.isCausaleRitenutaPresente();
		result = result || StringUtils.equals(getTributo(), datiConfronto.getTributo());
		result = result || StringUtils.equals(getSezione(), datiConfronto.getSezione());
		result = result || getPercentualeImponibile().compareTo(datiConfronto.getPercentualeImponibile()) != 0;
		result = result || getPercentualeAliquota().compareTo(datiConfronto.getPercentualeAliquota()) != 0;

		result = result || isContributoPrevidenzialePresente() != datiConfronto.isContributoPrevidenzialePresente();
		result = result || getPercContributiva().compareTo(datiConfronto.getPercContributiva()) != 0;
		result = result
				|| getPercPrevidenzialeCaricoLavoratore().compareTo(
						datiConfronto.getPercPrevidenzialeCaricoLavoratore()) != 0;
		result = result
				|| getPercPrevidenzialeCaricoAzienda().compareTo(datiConfronto.getPercPrevidenzialeCaricoAzienda()) != 0;

		return result;
	}

	/**
	 * @return the contributoPrevidenzialePresente
	 */
	public boolean isContributoPrevidenzialePresente() {
		return contributoPrevidenzialePresente;
	}

	/**
	 * @return the fondoProfessionistiPresente
	 */
	public boolean isFondoProfessionistiPresente() {
		return fondoProfessionistiPresente;
	}

	/**
	 * @param causaleRitenutaPresente
	 *            the causaleRitenutaPresente to set
	 */
	public void setCausaleRitenutaPresente(boolean causaleRitenutaPresente) {
		this.causaleRitenutaPresente = causaleRitenutaPresente;
	}

	/**
	 * @param contoBasePrevidenziale
	 *            the contoBasePrevidenziale to set
	 */
	public void setContoBasePrevidenziale(ETipoContoBase contoBasePrevidenziale) {
		this.contoBasePrevidenziale = contoBasePrevidenziale;
	}

	/**
	 * @param contributoPrevidenzialePresente
	 *            the contributoPrevidenzialePresente to set
	 */
	public void setContributoPrevidenzialePresente(boolean contributoPrevidenzialePresente) {
		this.contributoPrevidenzialePresente = contributoPrevidenzialePresente;
	}

	/**
	 * @param fondoProfessionistiPresente
	 *            the fondoProfessionistiPresente to set
	 */
	public void setFondoProfessionistiPresente(boolean fondoProfessionistiPresente) {
		this.fondoProfessionistiPresente = fondoProfessionistiPresente;
	}

	/**
	 * @param imponibileNonSoggettoRitenuta
	 *            the imponibileNonSoggettoRitenuta to set
	 */
	public void setImponibileNonSoggettoRitenuta(BigDecimal imponibileNonSoggettoRitenuta) {
		this.imponibileNonSoggettoRitenuta = imponibileNonSoggettoRitenuta;
	}

	/**
	 * @param imponibileRiferimento
	 *            the imponibileRiferimento to set
	 */
	public void setImponibileRiferimento(BigDecimal imponibileRiferimento) {
		this.imponibileRiferimento = imponibileRiferimento;
	}

	/**
	 * @param imponibileSoggettoRitenuta
	 *            the imponibileSoggettoRitenuta to set
	 */
	public void setImponibileSoggettoRitenuta(BigDecimal imponibileSoggettoRitenuta) {
		this.imponibileSoggettoRitenuta = imponibileSoggettoRitenuta;
	}

	/**
	 * @param percContributiva
	 *            the percContributiva to set
	 */
	public void setPercContributiva(Double percContributiva) {
		setPercContributivaINPS(0.0);
		setPercContributivaEnasarco(0.0);
		if (contoBasePrevidenziale != null) {
			switch (contoBasePrevidenziale) {
			case QUOTA_INPS:
				setPercContributivaINPS(percContributiva);
				break;
			case QUOTA_ENASARCO:
				setPercContributivaEnasarco(percContributiva);
				break;
			default:
				break;
			}
		}
	}

	/**
	 * @param percContributivaEnasarco
	 *            the percContributivaEnasarco to set
	 */
	public void setPercContributivaEnasarco(Double percContributivaEnasarco) {
		this.percContributivaEnasarco = percContributivaEnasarco;
	}

	/**
	 * @param percContributivaINPS
	 *            the percContributivaINPS to set
	 */
	public void setPercContributivaINPS(Double percContributivaINPS) {
		this.percContributivaINPS = percContributivaINPS;
	}

	/**
	 * @param percentualeAliquota
	 *            the percentualeAliquota to set
	 */
	public void setPercentualeAliquota(Double percentualeAliquota) {
		this.percentualeAliquota = percentualeAliquota;
	}

	/**
	 * @param percentualeImponibile
	 *            the percentualeImponibile to set
	 */
	public void setPercentualeImponibile(Double percentualeImponibile) {
		this.percentualeImponibile = percentualeImponibile;
	}

	/**
	 * @param percFondoProfessionisti
	 *            the percFondoProfessionisti to set
	 */
	public void setPercFondoProfessionisti(Double percFondoProfessionisti) {
		this.percFondoProfessionisti = percFondoProfessionisti;
	}

	/**
	 * @param percPrevidenzialeCaricoAzienda
	 *            the percPrevidenzialeCaricoAzienda to set
	 */
	public void setPercPrevidenzialeCaricoAzienda(Double percPrevidenzialeCaricoAzienda) {
		this.percPrevidenzialeCaricoAzienda = percPrevidenzialeCaricoAzienda;
	}

	/**
	 * @param percPrevidenzialeCaricoLavoratore
	 *            the percPrevidenzialeCaricoLavoratore to set
	 */
	public void setPercPrevidenzialeCaricoLavoratore(Double percPrevidenzialeCaricoLavoratore) {
		this.percPrevidenzialeCaricoLavoratore = percPrevidenzialeCaricoLavoratore;
	}

	/**
	 * @param prestazione
	 *            the prestazione to set
	 */
	public void setPrestazione(Prestazione prestazione) {
		this.prestazione = prestazione;
	}

	/**
	 * @param sezione
	 *            the sezione to set
	 */
	public void setSezione(String sezione) {
		this.sezione = sezione;
	}

	/**
	 * @param speseRimborso
	 *            the speseRimborso to set
	 */
	public void setSpeseRimborso(BigDecimal speseRimborso) {
		this.speseRimborso = speseRimborso;
	}

	/**
	 * @param tributo
	 *            the tributo to set
	 */
	public void setTributo(String tributo) {
		this.tributo = tributo;
	}

}
