package it.eurotn.panjea.beniammortizzabili2.util;

import it.eurotn.panjea.beniammortizzabili2.domain.BeneAmmortizzabileLite;
import it.eurotn.panjea.beniammortizzabili2.domain.SottoSpecie;
import it.eurotn.panjea.beniammortizzabili2.domain.Specie;
import it.eurotn.panjea.beniammortizzabili2.domain.Ubicazione;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SituazioneBene implements Serializable {

	private static final long serialVersionUID = -7083571582605520046L;

	private Integer id;
	private Integer codice;
	private String descrizione;

	private Integer idUbicazione;
	private String codiceUbicazione;
	private String descrizioneUbicazione;

	private Integer idSpecie;
	private String codiceSpecie;
	private String descrizioneSpecie;

	private Integer idSottoSpecie;
	private String codiceSottoSpecie;
	private String descrizioneSottoSpecie;

	private Date dataInizioAmmortamento;

	private BigDecimal importoVariazioniBene;
	private BigDecimal importoVenditeBene;

	private BigDecimal importoSoggettoAdAmmortamento = BigDecimal.ZERO;
	private BigDecimal importoAmmortamentoOrdinario = BigDecimal.ZERO;
	private BigDecimal importoAmmortamentoAnticipato = BigDecimal.ZERO;

	private BigDecimal importoVariazioniFondo = BigDecimal.ZERO;
	private BigDecimal importoVenditeFondo = BigDecimal.ZERO;

	private BigDecimal importoResiduo;

	private BeneAmmortizzabileLite bene;

	private List<SituazioneBene> beniFigli = new ArrayList<SituazioneBene>();

	private Integer benePadreId;

	/**
	 * @return the bene
	 */
	public BeneAmmortizzabileLite getBene() {

		if (bene == null) {
			Ubicazione ubicazione = new Ubicazione();
			ubicazione.setId((this.idUbicazione != null) ? this.idUbicazione : -1);
			ubicazione.setCodice(this.codiceUbicazione);
			ubicazione.setDescrizione(this.descrizioneUbicazione);

			Specie specie = new Specie();
			specie.setId(this.idSpecie);
			specie.setCodice(this.codiceSpecie);
			specie.setDescrizione(this.descrizioneSpecie);

			SottoSpecie sottoSpecie = new SottoSpecie();
			sottoSpecie.setSpecie(specie);
			sottoSpecie.setId(this.idSottoSpecie);
			sottoSpecie.setCodice(this.codiceSottoSpecie);
			sottoSpecie.setDescrizione(this.descrizioneSottoSpecie);

			bene = new BeneAmmortizzabileLite();
			bene.setId(this.id);
			bene.setCodice(this.codice);
			bene.setDescrizione(this.descrizione);
			bene.setSottoSpecie(sottoSpecie);
			bene.setUbicazione(ubicazione);
			bene.setDataInizioAmmortamento(this.dataInizioAmmortamento);
		}

		return bene;
	}

	/**
	 * @return the benePadreId
	 */
	public Integer getBenePadreId() {
		return benePadreId;
	}

	/**
	 * @return the beniFigli
	 */
	public List<SituazioneBene> getBeniFigli() {
		return beniFigli;
	}

	/**
	 * @return the importoAmmortamentoAnticipato
	 */
	public BigDecimal getImportoAmmortamentoAnticipato() {
		return importoAmmortamentoAnticipato;
	}

	/**
	 * @return the importoAmmortamentoOrdinario
	 */
	public BigDecimal getImportoAmmortamentoOrdinario() {
		return importoAmmortamentoOrdinario;
	}

	/**
	 * @return the importoFondo
	 */
	public BigDecimal getImportoFondo() {
		return getImportoVariazioniFondo().subtract(getImportoVenditeFondo());
	}

	/**
	 * @return the importoResiduo
	 */
	public BigDecimal getImportoResiduo() {

		if (importoResiduo == null) {
			BigDecimal importoAmmortizzato = getImportoAmmortamentoAnticipato().add(
					getImportoAmmortamentoOrdinario().add(
							getImportoVariazioniFondo().subtract(getImportoVenditeFondo())));
			importoResiduo = getImportoSoggettoAdAmmortamento().subtract(importoAmmortizzato);
		}

		return importoResiduo;
	}

	/**
	 * @return the importoSoggettoAdAmmortamento
	 */
	public BigDecimal getImportoSoggettoAdAmmortamento() {
		return importoSoggettoAdAmmortamento.add(getImportoVariazioniBene().subtract(getImportoVenditeBene()));
	}

	/**
	 * @return the importoVariazioniBene
	 */
	public BigDecimal getImportoVariazioniBene() {
		return importoVariazioniBene;
	}

	/**
	 * @return the importoVariazioniFondo
	 */
	public BigDecimal getImportoVariazioniFondo() {
		return importoVariazioniFondo;
	}

	/**
	 * @return the importoVenditeBene
	 */
	public BigDecimal getImportoVenditeBene() {
		return importoVenditeBene;
	}

	/**
	 * @return the importoVenditeFondo
	 */
	public BigDecimal getImportoVenditeFondo() {
		return importoVenditeFondo;
	}

	/**
	 * @param benePadreId the benePadreId to set
	 */
	public void setBenePadreId(Integer benePadreId) {
		this.benePadreId = benePadreId;
	}

	/**
	 * @param beniFigli the beniFigli to set
	 */
	public void setBeniFigli(List<SituazioneBene> beniFigli) {
		this.beniFigli = beniFigli;
	}

	/**
	 * @param codice the codice to set
	 */
	public void setCodice(Integer codice) {
		this.codice = codice;
	}

	/**
	 * @param codiceSottoSpecie the codiceSottoSpecie to set
	 */
	public void setCodiceSottoSpecie(String codiceSottoSpecie) {
		this.codiceSottoSpecie = codiceSottoSpecie;
	}

	/**
	 * @param codiceSpecie the codiceSpecie to set
	 */
	public void setCodiceSpecie(String codiceSpecie) {
		this.codiceSpecie = codiceSpecie;
	}

	/**
	 * @param codiceUbicazione the codiceUbicazione to set
	 */
	public void setCodiceUbicazione(String codiceUbicazione) {
		this.codiceUbicazione = codiceUbicazione;
	}

	/**
	 * @param dataInizioAmmortamento the dataInizioAmmortamento to set
	 */
	public void setDataInizioAmmortamento(Date dataInizioAmmortamento) {
		this.dataInizioAmmortamento = dataInizioAmmortamento;
	}

	/**
	 * @param descrizione the descrizione to set
	 */
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	/**
	 * @param descrizioneSottoSpecie the descrizioneSottoSpecie to set
	 */
	public void setDescrizioneSottoSpecie(String descrizioneSottoSpecie) {
		this.descrizioneSottoSpecie = descrizioneSottoSpecie;
	}

	/**
	 * @param descrizioneSpecie the descrizioneSpecie to set
	 */
	public void setDescrizioneSpecie(String descrizioneSpecie) {
		this.descrizioneSpecie = descrizioneSpecie;
	}

	/**
	 * @param descrizioneUbicazione the descrizioneUbicazione to set
	 */
	public void setDescrizioneUbicazione(String descrizioneUbicazione) {
		this.descrizioneUbicazione = descrizioneUbicazione;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @param idSottoSpecie the idSottoSpecie to set
	 */
	public void setIdSottoSpecie(Integer idSottoSpecie) {
		this.idSottoSpecie = idSottoSpecie;
	}

	/**
	 * @param idSpecie the idSpecie to set
	 */
	public void setIdSpecie(Integer idSpecie) {
		this.idSpecie = idSpecie;
	}

	/**
	 * @param idUbicazione the idUbicazione to set
	 */
	public void setIdUbicazione(Integer idUbicazione) {
		this.idUbicazione = idUbicazione;
	}

	/**
	 * @param importoAmmortamentoAnticipato the importoAmmortamentoAnticipato to set
	 */
	public void setImportoAmmortamentoAnticipato(BigDecimal importoAmmortamentoAnticipato) {
		this.importoAmmortamentoAnticipato = importoAmmortamentoAnticipato;
	}

	/**
	 * @param importoAmmortamentoOrdinario the importoAmmortamentoOrdinario to set
	 */
	public void setImportoAmmortamentoOrdinario(BigDecimal importoAmmortamentoOrdinario) {
		this.importoAmmortamentoOrdinario = importoAmmortamentoOrdinario;
	}

	/**
	 * @param importoSoggettoAdAmmortamento the importoSoggettoAdAmmortamento to set
	 */
	public void setImportoSoggettoAdAmmortamento(BigDecimal importoSoggettoAdAmmortamento) {
		this.importoSoggettoAdAmmortamento = importoSoggettoAdAmmortamento;
	}

	/**
	 * @param importoVariazioniBene the importoVariazioniBene to set
	 */
	public void setImportoVariazioniBene(BigDecimal importoVariazioniBene) {
		this.importoVariazioniBene = importoVariazioniBene;
	}

	/**
	 * @param importoVariazioniFondo the importoVariazioniFondo to set
	 */
	public void setImportoVariazioniFondo(BigDecimal importoVariazioniFondo) {
		this.importoVariazioniFondo = importoVariazioniFondo;
	}

	/**
	 * @param importoVenditeBene the importoVenditeBene to set
	 */
	public void setImportoVenditeBene(BigDecimal importoVenditeBene) {
		this.importoVenditeBene = importoVenditeBene;
	}

	/**
	 * @param importoVenditeFondo the importoVenditeFondo to set
	 */
	public void setImportoVenditeFondo(BigDecimal importoVenditeFondo) {
		this.importoVenditeFondo = importoVenditeFondo;
	}
}
