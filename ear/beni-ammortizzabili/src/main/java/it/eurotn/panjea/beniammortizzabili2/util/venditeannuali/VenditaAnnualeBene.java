package it.eurotn.panjea.beniammortizzabili2.util.venditeannuali;

import it.eurotn.panjea.beniammortizzabili2.domain.BeneAmmortizzabile;
import it.eurotn.panjea.beniammortizzabili2.domain.SottoSpecie;
import it.eurotn.panjea.beniammortizzabili2.domain.Specie;

import java.io.Serializable;
import java.math.BigDecimal;

public class VenditaAnnualeBene implements Serializable {

	private static final long serialVersionUID = 5936406736686859462L;

	private Integer idBene;
	private Integer codiceBene;
	private String descrizioneBene;
	private Integer annoAcquisto;
	private BeneAmmortizzabile beneAmmortizzabile;

	private Integer idSpecie;
	private String codiceSpecie;
	private String descrizioneSpecie;
	private Specie specie;

	private Integer idSottoSpecie;
	private String codiceSottoSpecie;
	private String descrizioneSottoSpecie;
	private SottoSpecie sottoSpecie;

	private BigDecimal importoSoggettoAdAmmortamentoSingolo;
	private BigDecimal importoSoggettoAdAmmortamentoFigli;

	private BigDecimal valoreQuoteFondo;
	private BigDecimal valoreValutazioniFondo;
	private BigDecimal valoreVenditeFondo;
	private BigDecimal rivalutazioniFondoFigli;
	private BigDecimal venditeFondoFigli;

	private BigDecimal venditeAnnualiBene;
	private BigDecimal venditeAnnualiFigli;

	private BigDecimal plusMinusValoreAnnualiBene;
	private BigDecimal plusMinusValoreAnnualiFigli;

	private String tipologiaEliminazione;

	/**
	 * Costruttore.
	 *
	 */
	public VenditaAnnualeBene() {
		super();
	}

	/**
	 * @return the beneAmmortizzabile
	 */
	public BeneAmmortizzabile getBene() {
		if (beneAmmortizzabile == null) {
			beneAmmortizzabile = new BeneAmmortizzabile();
			beneAmmortizzabile.setId(idBene);
			beneAmmortizzabile.setCodice(codiceBene);
			beneAmmortizzabile.setDescrizione(descrizioneBene);
			beneAmmortizzabile.setSottoSpecie(getSottoSpecie());
			beneAmmortizzabile.setAnnoAcquisto(annoAcquisto);
		}

		return beneAmmortizzabile;
	}

	/**
	 * @return importoSoggettoAdAmmortamento
	 */
	public BigDecimal getimportoSoggettoAdAmmortamento() {
		return importoSoggettoAdAmmortamentoSingolo.add(importoSoggettoAdAmmortamentoFigli);
	}

	/**
	 * @return totaleVenditeAnno
	 */
	public BigDecimal getPlusMinusValoreAnno() {
		return plusMinusValoreAnnualiBene.add(plusMinusValoreAnnualiFigli);
	}

	/**
	 * @return the sottoSpecie
	 */
	private SottoSpecie getSottoSpecie() {
		if (sottoSpecie == null) {
			sottoSpecie = new SottoSpecie();
			sottoSpecie.setId(idSottoSpecie);
			sottoSpecie.setCodice(codiceSottoSpecie);
			sottoSpecie.setDescrizione(descrizioneSottoSpecie);
			sottoSpecie.setSpecie(getSpecie());
		}

		return sottoSpecie;
	}

	/**
	 * @return the specie
	 */
	private Specie getSpecie() {
		if (specie == null) {
			specie = new Specie();
			specie.setId(idSpecie);
			specie.setCodice(codiceSpecie);
			specie.setDescrizione(descrizioneSpecie);
		}

		return specie;
	}

	/**
	 * @return Returns the tipologiaEliminazione.
	 */
	public String getTipologiaEliminazione() {
		return tipologiaEliminazione;
	}

	/**
	 * @return totaleVenditeAnno
	 */
	public BigDecimal getTotaleVenditeAnno() {
		return venditeAnnualiBene.add(venditeAnnualiFigli);
	}

	/**
	 * @return the valoreFondo
	 */
	public BigDecimal getValoreFondo() {
		return valoreQuoteFondo.add(valoreValutazioniFondo).subtract(valoreVenditeFondo).add(rivalutazioniFondoFigli)
				.subtract(venditeFondoFigli);
	}

	/**
	 * @param annoAcquisto
	 *            the annoAcquisto to set
	 */
	public void setAnnoAcquisto(Integer annoAcquisto) {
		this.annoAcquisto = annoAcquisto;
	}

	/**
	 * @param codiceBene
	 *            the codiceBene to set
	 */
	public void setCodiceBene(Integer codiceBene) {
		this.codiceBene = codiceBene;
	}

	/**
	 * @param codiceSottoSpecie
	 *            the codiceSottoSpecie to set
	 */
	public void setCodiceSottoSpecie(String codiceSottoSpecie) {
		this.codiceSottoSpecie = codiceSottoSpecie;
	}

	/**
	 * @param codiceSpecie
	 *            the codiceSpecie to set
	 */
	public void setCodiceSpecie(String codiceSpecie) {
		this.codiceSpecie = codiceSpecie;
	}

	/**
	 * @param descrizioneBene
	 *            the descrizioneBene to set
	 */
	public void setDescrizioneBene(String descrizioneBene) {
		this.descrizioneBene = descrizioneBene;
	}

	/**
	 * @param descrizioneSottoSpecie
	 *            the descrizioneSottoSpecie to set
	 */
	public void setDescrizioneSottoSpecie(String descrizioneSottoSpecie) {
		this.descrizioneSottoSpecie = descrizioneSottoSpecie;
	}

	/**
	 * @param descrizioneSpecie
	 *            the descrizioneSpecie to set
	 */
	public void setDescrizioneSpecie(String descrizioneSpecie) {
		this.descrizioneSpecie = descrizioneSpecie;
	}

	/**
	 * @param idBene
	 *            the idBene to set
	 */
	public void setIdBene(Integer idBene) {
		this.idBene = idBene;
	}

	/**
	 * @param idSottoSpecie
	 *            the idSottoSpecie to set
	 */
	public void setIdSottoSpecie(Integer idSottoSpecie) {
		this.idSottoSpecie = idSottoSpecie;
	}

	/**
	 * @param idSpecie
	 *            the idSpecie to set
	 */
	public void setIdSpecie(Integer idSpecie) {
		this.idSpecie = idSpecie;
	}

	/**
	 * @param importoSoggettoAdAmmortamentoFigli
	 *            the importoSoggettoAdAmmortamentoFigli to set
	 */
	public void setImportoSoggettoAdAmmortamentoFigli(BigDecimal importoSoggettoAdAmmortamentoFigli) {
		this.importoSoggettoAdAmmortamentoFigli = importoSoggettoAdAmmortamentoFigli;
	}

	/**
	 * @param importoSoggettoAdAmmortamentoSingolo
	 *            the importoSoggettoAdAmmortamentoSingolo to set
	 */
	public void setImportoSoggettoAdAmmortamentoSingolo(BigDecimal importoSoggettoAdAmmortamentoSingolo) {
		this.importoSoggettoAdAmmortamentoSingolo = importoSoggettoAdAmmortamentoSingolo;
	}

	/**
	 * @param plusMinusValoreAnnualiBene
	 *            The plusMinusValoreAnnualiBene to set.
	 */
	public void setPlusMinusValoreAnnualiBene(BigDecimal plusMinusValoreAnnualiBene) {
		this.plusMinusValoreAnnualiBene = plusMinusValoreAnnualiBene;
	}

	/**
	 * @param plusMinusValoreAnnualiFigli
	 *            The plusMinusValoreAnnualiFigli to set.
	 */
	public void setPlusMinusValoreAnnualiFigli(BigDecimal plusMinusValoreAnnualiFigli) {
		this.plusMinusValoreAnnualiFigli = plusMinusValoreAnnualiFigli;
	}

	/**
	 * @param rivalutazioni
	 *            the rivalutazioni to set
	 */
	public void setRivalutazioni(BigDecimal rivalutazioni) {
	}

	/**
	 * @param rivalutazioniFigli
	 *            the rivalutazioniFigli to set
	 */
	public void setRivalutazioniFigli(BigDecimal rivalutazioniFigli) {
	}

	/**
	 * @param rivalutazioniFondoFigli
	 *            the rivalutazioniFondoFigli to set
	 */
	public void setRivalutazioniFondoFigli(BigDecimal rivalutazioniFondoFigli) {
		this.rivalutazioniFondoFigli = rivalutazioniFondoFigli;
	}

	/**
	 * @param tipologiaEliminazione
	 *            The tipologiaEliminazione to set.
	 */
	public void setTipologiaEliminazione(String tipologiaEliminazione) {
		this.tipologiaEliminazione = tipologiaEliminazione;
	}

	/**
	 * @param valoreQuoteFondo
	 *            the valoreQuoteFondo to set
	 */
	public void setValoreQuoteFondo(BigDecimal valoreQuoteFondo) {
		this.valoreQuoteFondo = valoreQuoteFondo;
	}

	/**
	 * @param valoreValutazioniFondo
	 *            the valoreValutazioniFondo to set
	 */
	public void setValoreValutazioniFondo(BigDecimal valoreValutazioniFondo) {
		this.valoreValutazioniFondo = valoreValutazioniFondo;
	}

	/**
	 * @param valoreVenditeFondo
	 *            the valoreVenditeFondo to set
	 */
	public void setValoreVenditeFondo(BigDecimal valoreVenditeFondo) {
		this.valoreVenditeFondo = valoreVenditeFondo;
	}

	/**
	 * @param vendite
	 *            the vendite to set
	 */
	public void setVendite(BigDecimal vendite) {
	}

	/**
	 * @param venditeAnnualiBene
	 *            the venditeAnnualiBene to set
	 */
	public void setVenditeAnnualiBene(BigDecimal venditeAnnualiBene) {
		this.venditeAnnualiBene = venditeAnnualiBene;
	}

	/**
	 * @param venditeAnnualiFigli
	 *            the venditeAnnualiFigli to set
	 */
	public void setVenditeAnnualiFigli(BigDecimal venditeAnnualiFigli) {
		this.venditeAnnualiFigli = venditeAnnualiFigli;
	}

	/**
	 * @param venditeFigli
	 *            the venditeFigli to set
	 */
	public void setVenditeFigli(BigDecimal venditeFigli) {
	}

	/**
	 * @param venditeFondoFigli
	 *            the venditeFondoFigli to set
	 */
	public void setVenditeFondoFigli(BigDecimal venditeFondoFigli) {
		this.venditeFondoFigli = venditeFondoFigli;
	}

}
