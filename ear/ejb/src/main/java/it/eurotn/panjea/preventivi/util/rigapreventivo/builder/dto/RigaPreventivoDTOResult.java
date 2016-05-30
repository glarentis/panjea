package it.eurotn.panjea.preventivi.util.rigapreventivo.builder.dto;

import java.math.BigDecimal;

public class RigaPreventivoDTOResult {
	private Integer id;
	private Integer idAreaPreventivo;
	private String tipoRiga;
	private Integer idArticolo;
	private String codiceArticolo;
	private String codiceArticoloEntita;
	private String descrizioneArticolo;
	private Integer idArticoloPadre;
	private String codiceArticoloPadre;
	private String codiceArticoloPadreEntita;
	private String descrizioneArticoloPadre;
	private String descrizioneRiga;
	private Integer numeroDecimaliPrezzo;
	private String codiceValutaPrezzoUnitario;
	private BigDecimal importoInValutaPrezzoUnitario;
	private BigDecimal importoInValutaAziendaPrezzoUnitario;
	private BigDecimal tassoDiCambioPrezzoUnitario;
	private Double qtaRiga;
	private String codiceValutaPrezzoNetto;
	private BigDecimal importoInValutaPrezzoNetto;
	private BigDecimal importoInValutaAziendaPrezzoNetto;
	private BigDecimal tassoDiCambioPrezzoNetto;
	private BigDecimal variazione1;
	private BigDecimal variazione2;
	private BigDecimal variazione3;
	private BigDecimal variazione4;
	private String codiceValutaPrezzoTotale;
	private BigDecimal importoInValutaPrezzoTotale;
	private BigDecimal importoInValutaAziendaPrezzoTotale;
	private BigDecimal tassoDiCambioPrezzoTotale;
	private Double qtaChiusa;
	private String rigaNota;
	private String noteRiga;
	private Integer livello;
	private boolean rigaAutomatica;

	/**
	 * @return the codiceArticolo
	 */
	public String getCodiceArticolo() {
		return codiceArticolo;
	}

	/**
	 * @return the codiceArticoloEntita
	 */
	public String getCodiceArticoloEntita() {
		return codiceArticoloEntita;
	}

	/**
	 * @return the codiceArticoloPadre
	 */
	public String getCodiceArticoloPadre() {
		return codiceArticoloPadre;
	}

	/**
	 * @return the codiceArticoloPadreEntita
	 */
	public String getCodiceArticoloPadreEntita() {
		return codiceArticoloPadreEntita;
	}

	/**
	 * @return the codiceValutaPrezzoNetto
	 */
	public String getCodiceValutaPrezzoNetto() {
		return codiceValutaPrezzoNetto;
	}

	/**
	 * @return the codiceValutaPrezzoTotale
	 */
	public String getCodiceValutaPrezzoTotale() {
		return codiceValutaPrezzoTotale;
	}

	/**
	 * @return the codiceValutaPrezzoUnitario
	 */
	public String getCodiceValutaPrezzoUnitario() {
		return codiceValutaPrezzoUnitario;
	}

	/**
	 * @return the descrizioneArticolo
	 */
	public String getDescrizioneArticolo() {
		return descrizioneArticolo;
	}

	/**
	 * @return the descrizioneArticoloPadre
	 */
	public String getDescrizioneArticoloPadre() {
		return descrizioneArticoloPadre;
	}

	/**
	 * @return the descrizioneRiga
	 */
	public String getDescrizioneRiga() {
		return descrizioneRiga;
	}

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @return the idAreaPreventivo
	 */
	public Integer getIdAreaPreventivo() {
		return idAreaPreventivo;
	}

	/**
	 * @return the idArticolo
	 */
	public Integer getIdArticolo() {
		return idArticolo;
	}

	/**
	 * @return the idArticoloPadre
	 */
	public Integer getIdArticoloPadre() {
		return idArticoloPadre;
	}

	/**
	 * @return the importoInValutaAziendaPrezzoNetto
	 */
	public BigDecimal getImportoInValutaAziendaPrezzoNetto() {
		return importoInValutaAziendaPrezzoNetto;
	}

	/**
	 * @return the importoInValutaAziendaPrezzoTotale
	 */
	public BigDecimal getImportoInValutaAziendaPrezzoTotale() {
		return importoInValutaAziendaPrezzoTotale;
	}

	/**
	 * @return the importoInValutaAziendaPrezzoUnitario
	 */
	public BigDecimal getImportoInValutaAziendaPrezzoUnitario() {
		return importoInValutaAziendaPrezzoUnitario;
	}

	/**
	 * @return the importoInValutaPrezzoNetto
	 */
	public BigDecimal getImportoInValutaPrezzoNetto() {
		return importoInValutaPrezzoNetto;
	}

	/**
	 * @return the importoInValutaPrezzoTotale
	 */
	public BigDecimal getImportoInValutaPrezzoTotale() {
		return importoInValutaPrezzoTotale;
	}

	/**
	 * @return the importoInValutaPrezzoUnitario
	 */
	public BigDecimal getImportoInValutaPrezzoUnitario() {
		return importoInValutaPrezzoUnitario;
	}

	/**
	 * @return the livello
	 */
	public Integer getLivello() {
		return livello;
	}

	/**
	 * @return the noteRiga
	 */
	public String getNoteRiga() {
		return noteRiga;
	}

	/**
	 * @return the numeroDecimaliPrezzo
	 */
	public Integer getNumeroDecimaliPrezzo() {
		return numeroDecimaliPrezzo;
	}

	/**
	 * @return the qtaChiusa
	 */
	public Double getQtaChiusa() {
		return qtaChiusa;
	}

	/**
	 * @return the qtaRiga
	 */
	public Double getQtaRiga() {
		return qtaRiga;
	}

	/**
	 * @return the rigaNota
	 */
	public String getRigaNota() {
		return rigaNota;
	}

	/**
	 * @return the tassoDiCambioPrezzoNetto
	 */
	public BigDecimal getTassoDiCambioPrezzoNetto() {
		return tassoDiCambioPrezzoNetto;
	}

	/**
	 * @return the tassoDiCambioPrezzoTotale
	 */
	public BigDecimal getTassoDiCambioPrezzoTotale() {
		return tassoDiCambioPrezzoTotale;
	}

	/**
	 * @return the tassoDiCambioPrezzoUnitario
	 */
	public BigDecimal getTassoDiCambioPrezzoUnitario() {
		return tassoDiCambioPrezzoUnitario;
	}

	/**
	 * @return the tipoRiga
	 */
	public String getTipoRiga() {
		return tipoRiga;
	}

	/**
	 * @return the variazione1
	 */
	public BigDecimal getVariazione1() {
		return variazione1;
	}

	/**
	 * @return the variazione2
	 */
	public BigDecimal getVariazione2() {
		return variazione2;
	}

	/**
	 * @return the variazione3
	 */
	public BigDecimal getVariazione3() {
		return variazione3;
	}

	/**
	 * @return the variazione4
	 */
	public BigDecimal getVariazione4() {
		return variazione4;
	}

	/**
	 * @return the rigaAutomatica
	 */
	public boolean isRigaAutomatica() {
		return rigaAutomatica;
	}

	/**
	 * @param codiceArticolo
	 *            the codiceArticolo to set
	 */
	public void setCodiceArticolo(String codiceArticolo) {
		this.codiceArticolo = codiceArticolo;
	}

	/**
	 * @param codiceArticoloEntita
	 *            the codiceArticoloEntita to set
	 */
	public void setCodiceArticoloEntita(String codiceArticoloEntita) {
		this.codiceArticoloEntita = codiceArticoloEntita;
	}

	/**
	 * @param codiceArticoloPadre
	 *            the codiceArticoloPadre to set
	 */
	public void setCodiceArticoloPadre(String codiceArticoloPadre) {
		this.codiceArticoloPadre = codiceArticoloPadre;
	}

	/**
	 * @param codiceArticoloPadreEntita
	 *            the codiceArticoloPadreEntita to set
	 */
	public void setCodiceArticoloPadreEntita(String codiceArticoloPadreEntita) {
		this.codiceArticoloPadreEntita = codiceArticoloPadreEntita;
	}

	/**
	 * @param codiceValutaPrezzoNetto
	 *            the codiceValutaPrezzoNetto to set
	 */
	public void setCodiceValutaPrezzoNetto(String codiceValutaPrezzoNetto) {
		this.codiceValutaPrezzoNetto = codiceValutaPrezzoNetto;
	}

	/**
	 * @param codiceValutaPrezzoTotale
	 *            the codiceValutaPrezzoTotale to set
	 */
	public void setCodiceValutaPrezzoTotale(String codiceValutaPrezzoTotale) {
		this.codiceValutaPrezzoTotale = codiceValutaPrezzoTotale;
	}

	/**
	 * @param codiceValutaPrezzoUnitario
	 *            the codiceValutaPrezzoUnitario to set
	 */
	public void setCodiceValutaPrezzoUnitario(String codiceValutaPrezzoUnitario) {
		this.codiceValutaPrezzoUnitario = codiceValutaPrezzoUnitario;
	}

	/**
	 * @param descrizioneArticolo
	 *            the descrizioneArticolo to set
	 */
	public void setDescrizioneArticolo(String descrizioneArticolo) {
		this.descrizioneArticolo = descrizioneArticolo;
	}

	/**
	 * @param descrizioneArticoloPadre
	 *            the descrizioneArticoloPadre to set
	 */
	public void setDescrizioneArticoloPadre(String descrizioneArticoloPadre) {
		this.descrizioneArticoloPadre = descrizioneArticoloPadre;
	}

	/**
	 * @param descrizioneRiga
	 *            the descrizioneRiga to set
	 */
	public void setDescrizioneRiga(String descrizioneRiga) {
		this.descrizioneRiga = descrizioneRiga;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @param idAreaPreventivo
	 *            the idAreaPreventivo to set
	 */
	public void setIdAreaPreventivo(Integer idAreaPreventivo) {
		this.idAreaPreventivo = idAreaPreventivo;
	}

	/**
	 * @param idArticolo
	 *            the idArticolo to set
	 */
	public void setIdArticolo(Integer idArticolo) {
		this.idArticolo = idArticolo;
	}

	/**
	 * @param idArticoloPadre
	 *            the idArticoloPadre to set
	 */
	public void setIdArticoloPadre(Integer idArticoloPadre) {
		this.idArticoloPadre = idArticoloPadre;
	}

	/**
	 * @param importoInValutaAziendaPrezzoNetto
	 *            the importoInValutaAziendaPrezzoNetto to set
	 */
	public void setImportoInValutaAziendaPrezzoNetto(BigDecimal importoInValutaAziendaPrezzoNetto) {
		this.importoInValutaAziendaPrezzoNetto = importoInValutaAziendaPrezzoNetto;
	}

	/**
	 * @param importoInValutaAziendaPrezzoTotale
	 *            the importoInValutaAziendaPrezzoTotale to set
	 */
	public void setImportoInValutaAziendaPrezzoTotale(BigDecimal importoInValutaAziendaPrezzoTotale) {
		this.importoInValutaAziendaPrezzoTotale = importoInValutaAziendaPrezzoTotale;
	}

	/**
	 * @param importoInValutaAziendaPrezzoUnitario
	 *            the importoInValutaAziendaPrezzoUnitario to set
	 */
	public void setImportoInValutaAziendaPrezzoUnitario(BigDecimal importoInValutaAziendaPrezzoUnitario) {
		this.importoInValutaAziendaPrezzoUnitario = importoInValutaAziendaPrezzoUnitario;
	}

	/**
	 * @param importoInValutaPrezzoNetto
	 *            the importoInValutaPrezzoNetto to set
	 */
	public void setImportoInValutaPrezzoNetto(BigDecimal importoInValutaPrezzoNetto) {
		this.importoInValutaPrezzoNetto = importoInValutaPrezzoNetto;
	}

	/**
	 * @param importoInValutaPrezzoTotale
	 *            the importoInValutaPrezzoTotale to set
	 */
	public void setImportoInValutaPrezzoTotale(BigDecimal importoInValutaPrezzoTotale) {
		this.importoInValutaPrezzoTotale = importoInValutaPrezzoTotale;
	}

	/**
	 * @param importoInValutaPrezzoUnitario
	 *            the importoInValutaPrezzoUnitario to set
	 */
	public void setImportoInValutaPrezzoUnitario(BigDecimal importoInValutaPrezzoUnitario) {
		this.importoInValutaPrezzoUnitario = importoInValutaPrezzoUnitario;
	}

	/**
	 * @param livello
	 *            the livello to set
	 */
	public void setLivello(Integer livello) {
		this.livello = livello;
	}

	/**
	 * @param noteRiga
	 *            the noteRiga to set
	 */
	public void setNoteRiga(String noteRiga) {
		this.noteRiga = noteRiga;
	}

	/**
	 * @param numeroDecimaliPrezzo
	 *            the numeroDecimaliPrezzo to set
	 */
	public void setNumeroDecimaliPrezzo(Integer numeroDecimaliPrezzo) {
		this.numeroDecimaliPrezzo = numeroDecimaliPrezzo;
	}

	/**
	 * @param qtaChiusa
	 *            the qtaChiusa to set
	 */
	public void setQtaChiusa(Double qtaChiusa) {
		this.qtaChiusa = qtaChiusa;
	}

	/**
	 * @param qtaRiga
	 *            the qtaRiga to set
	 */
	public void setQtaRiga(Double qtaRiga) {
		this.qtaRiga = qtaRiga;
	}

	/**
	 * @param rigaAutomatica
	 *            the rigaAutomatica to set
	 */
	public void setRigaAutomatica(boolean rigaAutomatica) {
		this.rigaAutomatica = rigaAutomatica;
	}

	/**
	 * @param rigaNota
	 *            the rigaNota to set
	 */
	public void setRigaNota(String rigaNota) {
		this.rigaNota = rigaNota;
	}

	/**
	 * @param tassoDiCambioPrezzoNetto
	 *            the tassoDiCambioPrezzoNetto to set
	 */
	public void setTassoDiCambioPrezzoNetto(BigDecimal tassoDiCambioPrezzoNetto) {
		this.tassoDiCambioPrezzoNetto = tassoDiCambioPrezzoNetto;
	}

	/**
	 * @param tassoDiCambioPrezzoTotale
	 *            the tassoDiCambioPrezzoTotale to set
	 */
	public void setTassoDiCambioPrezzoTotale(BigDecimal tassoDiCambioPrezzoTotale) {
		this.tassoDiCambioPrezzoTotale = tassoDiCambioPrezzoTotale;
	}

	/**
	 * @param tassoDiCambioPrezzoUnitario
	 *            the tassoDiCambioPrezzoUnitario to set
	 */
	public void setTassoDiCambioPrezzoUnitario(BigDecimal tassoDiCambioPrezzoUnitario) {
		this.tassoDiCambioPrezzoUnitario = tassoDiCambioPrezzoUnitario;
	}

	/**
	 * @param tipoRiga
	 *            the tipoRiga to set
	 */
	public void setTipoRiga(String tipoRiga) {
		this.tipoRiga = tipoRiga;
	}

	/**
	 * @param variazione1
	 *            the variazione1 to set
	 */
	public void setVariazione1(BigDecimal variazione1) {
		this.variazione1 = variazione1;
	}

	/**
	 * @param variazione2
	 *            the variazione2 to set
	 */
	public void setVariazione2(BigDecimal variazione2) {
		this.variazione2 = variazione2;
	}

	/**
	 * @param variazione3
	 *            the variazione3 to set
	 */
	public void setVariazione3(BigDecimal variazione3) {
		this.variazione3 = variazione3;
	}

	/**
	 * @param variazione4
	 *            the variazione4 to set
	 */
	public void setVariazione4(BigDecimal variazione4) {
		this.variazione4 = variazione4;
	}
}
