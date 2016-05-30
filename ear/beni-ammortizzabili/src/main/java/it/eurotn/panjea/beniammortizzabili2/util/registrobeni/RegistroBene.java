package it.eurotn.panjea.beniammortizzabili2.util.registrobeni;

import it.eurotn.panjea.beniammortizzabili2.domain.BeneAmmortizzabileLite;
import it.eurotn.panjea.beniammortizzabili2.domain.Gruppo;
import it.eurotn.panjea.beniammortizzabili2.domain.SottoSpecie;
import it.eurotn.panjea.beniammortizzabili2.domain.Specie;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class RegistroBene implements Serializable {

	private static final long serialVersionUID = -6067499730315237368L;

	private BeneAmmortizzabileLite bene;
	private Integer idBene;
	private Integer codiceBene;
	private String descrizioneBene;
	private Integer annoAcquistoBene;
	private BigDecimal importoSoggettoAdAmmortamentoSingolo;
	private BigDecimal importoSoggettoAdAmmortamentoFigli;

	private List<BeneAmmortizzabileLite> beniFigli;

	private Gruppo gruppo;
	private Integer idGruppo;
	private String codiceGruppo;
	private String descrizioneGruppo;

	private Specie specie;
	private Integer idSpecie;
	private String codiceSpecie;
	private String descrizioneSpecie;

	private SottoSpecie sottoSpecie;
	private Integer idSottoSpecie;
	private String codiceSottoSpecie;
	private String descrizioneSottoSpecie;

	private BigDecimal importoDaAmmortizzare;
	private BigDecimal residuo;

	private BigDecimal rivalutazioni;
	private BigDecimal vendite;
	private BigDecimal minusPlusValenze;

	private BigDecimal valoreQuoteFondo;
	private BigDecimal valoreValutazioniFondo;
	private BigDecimal valoreVenditeFondo;

	private Double percOrdinario;
	private Double percAnticipato;
	private boolean percPrimoAnnoApplicata;
	private BigDecimal impOrdinario;
	private BigDecimal impAnticipato;

	private BigDecimal rivalutazioniFigli;
	private BigDecimal venditeFigli;
	private BigDecimal rivalutazioniFondoFigli;
	private BigDecimal venditeFondoFigli;
	private BigDecimal minusPlusValenzeFigli;

	/**
	 * Costruttore.
	 */
	public RegistroBene() {
		super();
		initialize();
	}

	/**
	 * @return the bene
	 */
	public BeneAmmortizzabileLite getBene() {
		if (bene == null) {
			bene = new BeneAmmortizzabileLite();
			bene.setId(idBene);
			bene.setCodice(codiceBene);
			bene.setDescrizione(descrizioneBene);
			bene.setAnnoAcquisto(annoAcquistoBene);
		}

		return bene;
	}

	/**
	 * @return the beniFigli
	 */
	public List<BeneAmmortizzabileLite> getBeniFigli() {
		return beniFigli;
	}

	/**
	 * @return the gruppo
	 */
	public Gruppo getGruppo() {
		if (gruppo == null) {
			gruppo = new Gruppo();
			gruppo.setId(idGruppo);
			gruppo.setCodice(codiceGruppo);
			gruppo.setDescrizione(descrizioneGruppo);
		}

		return gruppo;
	}

	/**
	 * @return the impAnticipato
	 */
	public BigDecimal getImpAnticipato() {
		return impAnticipato;
	}

	/**
	 * @return the impOrdinario
	 */
	public BigDecimal getImpOrdinario() {
		return impOrdinario;
	}

	/**
	 * @return the importoDaAmmortizzare
	 */
	public BigDecimal getImportoDaAmmortizzare() {
		if (importoDaAmmortizzare == null) {
			importoDaAmmortizzare = getimportoSoggettoAdAmmortamento().add(rivalutazioni).subtract(vendite)
					.add(rivalutazioniFigli).subtract(venditeFigli);
		}

		return importoDaAmmortizzare;
	}

	/**
	 * @return importoSoggettoAdAmmortamento
	 */
	public BigDecimal getimportoSoggettoAdAmmortamento() {
		return importoSoggettoAdAmmortamentoSingolo.add(importoSoggettoAdAmmortamentoFigli);
	}

	/**
	 * @return the minusPlusValenze
	 */
	public BigDecimal getMinusPlusValenze() {
		return minusPlusValenze.add(minusPlusValenzeFigli);
	}

	/**
	 * @return the percAnticipato
	 */
	public Double getPercAnticipato() {
		return percAnticipato;
	}

	/**
	 * @return the percOrdinario
	 */
	public Double getPercOrdinario() {
		return percOrdinario;
	}

	/**
	 * @return the residuo
	 */
	public BigDecimal getResiduo() {
		if (residuo == null) {
			// residuo = BigDecimal.ZERO;
			// if (getImportoDaAmmortizzare().compareTo(BigDecimal.ZERO) != 0) {
			residuo = getImportoDaAmmortizzare().subtract(getValoreFondo()).subtract(getImpOrdinario())
					.subtract(getImpAnticipato());
			// }
		}

		return residuo;
	}

	/**
	 * @return the rivalutazioni
	 */
	public BigDecimal getRivalutazioni() {
		return rivalutazioni.add(rivalutazioniFigli);
	}

	/**
	 * @return the sottoSpecie
	 */
	public SottoSpecie getSottoSpecie() {
		if (sottoSpecie == null) {
			sottoSpecie = new SottoSpecie();
			sottoSpecie.setId(idSottoSpecie);
			sottoSpecie.setCodice(codiceSottoSpecie);
			sottoSpecie.setDescrizione(descrizioneSottoSpecie);
		}

		return sottoSpecie;
	}

	/**
	 * @return the specie
	 */
	public Specie getSpecie() {
		if (specie == null) {
			specie = new Specie();
			specie.setId(idSpecie);
			specie.setCodice(codiceSpecie);
			specie.setDescrizione(descrizioneSpecie);
		}

		return specie;
	}

	/**
	 * @return the valoreFondo
	 */
	public BigDecimal getValoreFondo() {
		return valoreQuoteFondo.add(valoreValutazioniFondo).subtract(valoreVenditeFondo).add(rivalutazioniFondoFigli)
				.subtract(venditeFondoFigli);
	}

	/**
	 * @return the vendite
	 */
	public BigDecimal getVendite() {
		return vendite.add(venditeFigli);
	}

	/**
	 * Inizializza i valori.
	 */
	private void initialize() {
		this.bene = null;
		this.gruppo = null;
		this.specie = null;
		this.sottoSpecie = null;
		this.importoDaAmmortizzare = null;
		this.residuo = null;
	}

	/**
	 * @return the percPrimoAnnoApplicata
	 */
	public boolean isPercPrimoAnnoApplicata() {
		return percPrimoAnnoApplicata;
	}

	/**
	 * @param annoAcquistoBene
	 *            the annoAcquistoBene to set
	 */
	public void setAnnoAcquistoBene(Integer annoAcquistoBene) {
		this.annoAcquistoBene = annoAcquistoBene;
	}

	/**
	 * @param beniFigli
	 *            the beniFigli to set
	 */
	public void setBeniFigli(List<BeneAmmortizzabileLite> beniFigli) {
		this.beniFigli = beniFigli;
	}

	/**
	 * @param codiceBene
	 *            the codiceBene to set
	 */
	public void setCodiceBene(Integer codiceBene) {
		this.codiceBene = codiceBene;
	}

	/**
	 * @param codiceGruppo
	 *            the codiceGruppo to set
	 */
	public void setCodiceGruppo(String codiceGruppo) {
		this.codiceGruppo = codiceGruppo;
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
	 * @param descrizioneGruppo
	 *            the descrizioneGruppo to set
	 */
	public void setDescrizioneGruppo(String descrizioneGruppo) {
		this.descrizioneGruppo = descrizioneGruppo;
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
	 * @param idGruppo
	 *            the idGruppo to set
	 */
	public void setIdGruppo(Integer idGruppo) {
		this.idGruppo = idGruppo;
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
	 * @param impAnticipato
	 *            the impAnticipato to set
	 */
	public void setImpAnticipato(BigDecimal impAnticipato) {
		this.impAnticipato = impAnticipato;
	}

	/**
	 * @param impOrdinario
	 *            the impOrdinario to set
	 */
	public void setImpOrdinario(BigDecimal impOrdinario) {
		this.impOrdinario = impOrdinario;
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
	 * @param minusPlusValenze
	 *            the minusPlusValenze to set
	 */
	public void setMinusPlusValenze(BigDecimal minusPlusValenze) {
		this.minusPlusValenze = minusPlusValenze;
	}

	/**
	 * @param minusPlusValenzeFigli
	 *            the minusPlusValenzeFigli to set
	 */
	public void setMinusPlusValenzeFigli(BigDecimal minusPlusValenzeFigli) {
		this.minusPlusValenzeFigli = minusPlusValenzeFigli;
	}

	/**
	 * @param percAnticipato
	 *            the percAnticipato to set
	 */
	public void setPercAnticipato(Double percAnticipato) {
		this.percAnticipato = percAnticipato;
	}

	/**
	 * @param percOrdinario
	 *            the percOrdinario to set
	 */
	public void setPercOrdinario(Double percOrdinario) {
		this.percOrdinario = percOrdinario;
	}

	/**
	 * @param percPrimoAnnoApplicata
	 *            the percPrimoAnnoApplicata to set
	 */
	public void setPercPrimoAnnoApplicata(boolean percPrimoAnnoApplicata) {
		this.percPrimoAnnoApplicata = percPrimoAnnoApplicata;
	}

	/**
	 * @param rivalutazioni
	 *            the rivalutazioni to set
	 */
	public void setRivalutazioni(BigDecimal rivalutazioni) {
		this.rivalutazioni = rivalutazioni;
	}

	/**
	 * @param rivalutazioniFigli
	 *            the rivalutazioniFigli to set
	 */
	public void setRivalutazioniFigli(BigDecimal rivalutazioniFigli) {
		this.rivalutazioniFigli = rivalutazioniFigli;
	}

	/**
	 * @param rivalutazioniFondoFigli
	 *            the rivalutazioniFondoFigli to set
	 */
	public void setRivalutazioniFondoFigli(BigDecimal rivalutazioniFondoFigli) {
		this.rivalutazioniFondoFigli = rivalutazioniFondoFigli;
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
		this.vendite = vendite;
	}

	/**
	 * @param venditeFigli
	 *            the venditeFigli to set
	 */
	public void setVenditeFigli(BigDecimal venditeFigli) {
		this.venditeFigli = venditeFigli;
	}

	/**
	 * @param venditeFondoFigli
	 *            the venditeFondoFigli to set
	 */
	public void setVenditeFondoFigli(BigDecimal venditeFondoFigli) {
		this.venditeFondoFigli = venditeFondoFigli;
	}
}
