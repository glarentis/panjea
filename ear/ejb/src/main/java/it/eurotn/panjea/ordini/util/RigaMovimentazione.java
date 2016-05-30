package it.eurotn.panjea.ordini.util;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.anagrafica.util.EntitaDocumento;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.Sconto;
import it.eurotn.panjea.magazzino.domain.omaggio.TipoOmaggio;
import it.eurotn.panjea.ordini.domain.documento.evasione.RiferimentiOrdine.ModalitaRicezione;

/**
 * @author fattazzo
 */
public class RigaMovimentazione implements Serializable {

	private static final long serialVersionUID = 2025768563802667686L;

	private Integer areaOrdineId;
	private Integer rigaOrdineId;
	private TipoOmaggio tipoOmaggio;
	private Date dataRegistrazione;
	private Date dataConsegna;
	private Documento documento;
	private EntitaDocumento entitaDocumento;
	private Integer numeroDecimaliPrezzo;
	private Integer numeroDecimaliQuantita;
	private BigDecimal prezzoUnitario;
	private BigDecimal prezzoNetto;
	private BigDecimal prezzoTotale;
	private ArticoloLite articoloLite = null;
	private DepositoLite depositoLite = null;
	private Sconto variazione;
	private String noteRiga;
	private Double quantita = 0.0;
	private Double quantitaEvasa = 0.0;
	private boolean evasioneForzata;
	private Date dataOrdine;
	private String numeroOrdineRicezione;
	private ModalitaRicezione modalitaRicezione;

	/**
	 * Costruttore.
	 */
	public RigaMovimentazione() {
		super();
		initialize();
	}

	/**
	 * @return the areaOrdineId
	 */
	public Integer getAreaOrdineId() {
		return areaOrdineId;
	}

	/**
	 * @return the articoloLite
	 */
	public ArticoloLite getArticoloLite() {
		return articoloLite;
	}

	/**
	 * @return Returns the dataConsegna.
	 */
	public Date getDataConsegna() {
		return dataConsegna;
	}

	/**
	 * @return the dataRegistrazione
	 */
	public Date getDataRegistrazione() {
		return dataRegistrazione;
	}

	/**
	 * @return the depositoLite
	 */
	public DepositoLite getDepositoLite() {
		return depositoLite;
	}

	/**
	 * @return the documento
	 */
	public Documento getDocumento() {
		return documento;
	}

	/**
	 * @return the entitaDocumento
	 */
	public EntitaDocumento getEntitaDocumento() {
		return entitaDocumento;
	}

	/**
	 * @return the importoRimanenza
	 */
	public BigDecimal getImportoRimanenza() {
		return getPrezzoNetto().multiply(new BigDecimal(getRimanenza())).setScale(2, RoundingMode.HALF_UP);
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
	 * @return the numeroDecimaliQuantita
	 */
	public Integer getNumeroDecimaliQuantita() {
		return numeroDecimaliQuantita;
	}

	/**
	 * @return the prezzoNetto
	 */
	public BigDecimal getPrezzoNetto() {
		return prezzoNetto;
	}

	/**
	 * @return the prezzoTotale
	 */
	public BigDecimal getPrezzoTotale() {
		return prezzoTotale;
	}

	/**
	 * @return the prezzoUnitario
	 */
	public BigDecimal getPrezzoUnitario() {
		return prezzoUnitario;
	}

	/**
	 * @return the quantita
	 */
	public Double getQuantita() {
		return quantita;
	}

	/**
	 * @return the quantitaEvasa
	 */
	public Double getQuantitaEvasa() {
		return quantitaEvasa;
	}

	/**
	 * @return the rigaOrdineId
	 */
	public Integer getRigaOrdineId() {
		return rigaOrdineId;
	}

	/**
	 * @return the rimanenza
	 */
	public Double getRimanenza() {
		Double qta = quantita == null || evasioneForzata ? 0.0 : quantita;
		Double qtaEvasa = quantitaEvasa == null || evasioneForzata ? 0.0 : quantitaEvasa;
		return qta - qtaEvasa;
	}

	/**
	 * @return Returns the tipoOmaggio.
	 */
	public TipoOmaggio getTipoOmaggio() {
		return tipoOmaggio;
	}

	/**
	 * @return the variazione
	 */
	public Sconto getVariazione() {
		return variazione;
	}

	/**
	 * Inizializza i valori.
	 */
	private void initialize() {
		this.documento = new Documento();
		entitaDocumento = new EntitaDocumento();
		this.articoloLite = new ArticoloLite();
		this.depositoLite = new DepositoLite();
		this.variazione = new Sconto();
	}

	/**
	 * @return the evasioneForzata
	 */
	public boolean isEvasioneForzata() {
		return evasioneForzata;
	}

	/**
	 * @param areaOrdineId
	 *            the areaOrdineId to set
	 */
	public void setAreaOrdineId(Integer areaOrdineId) {
		this.areaOrdineId = areaOrdineId;
	}

	/**
	 * @param codiceArticolo
	 *            the codiceArticolo to set
	 */
	public void setCodiceArticolo(String codiceArticolo) {
		this.articoloLite.setCodice(codiceArticolo);
	}

	/**
	 * @param codiceDeposito
	 *            the codiceDeposito to set
	 */
	public void setCodiceDeposito(String codiceDeposito) {
		this.depositoLite.setCodice(codiceDeposito);
	}

	/**
	 * @param codiceEntita
	 *            the codiceEntita to set
	 */
	public void setCodiceEntita(Integer codiceEntita) {
		this.entitaDocumento.setCodice(codiceEntita);
	}

	/**
	 * @param codiceTipoDocumento
	 *            the codiceTipoDocumento to set
	 */
	public void setCodiceTipoDocumento(String codiceTipoDocumento) {
		this.documento.getTipoDocumento().setCodice(codiceTipoDocumento);
	}

	/**
	 * @param dataConsegna
	 *            The dataConsegna to set.
	 */
	public void setDataConsegna(Date dataConsegna) {
		this.dataConsegna = dataConsegna;
	}

	/**
	 * @param dataDocumento
	 *            the dataDocumento to set
	 */
	public void setDataDocumento(Date dataDocumento) {
		this.documento.setDataDocumento(dataDocumento);
	}

	/**
	 * @param dataRegistrazione
	 *            the dataRegistrazione to set
	 */
	public void setDataRegistrazione(Date dataRegistrazione) {
		this.dataRegistrazione = dataRegistrazione;
	}

	/**
	 * @param descrizioneArticolo
	 *            the descrizioneArticolo to set
	 */
	public void setDescrizioneArticolo(String descrizioneArticolo) {
		this.articoloLite.setDescrizione(descrizioneArticolo);
	}

	/**
	 * @param descrizioneDeposito
	 *            the descrizioneDeposito to set
	 */
	public void setDescrizioneDeposito(String descrizioneDeposito) {
		this.depositoLite.setDescrizione(descrizioneDeposito);
	}

	/**
	 * @param descrizioneEntita
	 *            the descrizioneEntita to set
	 */
	public void setDescrizioneEntita(String descrizioneEntita) {
		this.entitaDocumento.setDescrizione(descrizioneEntita);
	}

	/**
	 * @param descrizioneTipoDocumento
	 *            the descrizioneTipoDocumento to set
	 */
	public void setDescrizioneTipoDocumento(String descrizioneTipoDocumento) {
		this.documento.getTipoDocumento().setDescrizione(descrizioneTipoDocumento);
	}

	/**
	 * @param entitaDocumento
	 *            the entitaDocumento to set
	 */
	public void setEntitaDocumento(EntitaDocumento entitaDocumento) {
		this.entitaDocumento = entitaDocumento;
	}

	/**
	 * @param evasioneForzata
	 *            the evasioneForzata to set
	 */
	public void setEvasioneForzata(boolean evasioneForzata) {
		this.evasioneForzata = evasioneForzata;
	}

	/**
	 * @param idArticolo
	 *            the idArticolo to set
	 */
	public void setIdArticolo(Integer idArticolo) {
		this.articoloLite.setId(idArticolo);
	}

	/**
	 * @param idDeposito
	 *            the idDeposito to set
	 */
	public void setIdDeposito(Integer idDeposito) {
		this.depositoLite.setId(idDeposito);
	}

	/**
	 * @param idDocumento
	 *            the idDocumento to set
	 */
	public void setIdDocumento(Integer idDocumento) {
		this.documento.setId(idDocumento);
	}

	/**
	 * @param idEntita
	 *            the idEntita to set
	 */
	public void setIdEntita(Integer idEntita) {
		this.entitaDocumento.setId(idEntita);
	}

	/**
	 * @param idTipoDocumento
	 *            the idTipoDocumento to set
	 */
	public void setIdTipoDocumento(Integer idTipoDocumento) {
		this.documento.getTipoDocumento().setId(idTipoDocumento);
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
	 * @param numeroDecimaliQuantita
	 *            the numeroDecimaliQuantita to set
	 */
	public void setNumeroDecimaliQuantita(Integer numeroDecimaliQuantita) {
		this.numeroDecimaliQuantita = numeroDecimaliQuantita;
	}

	/**
	 * @param numeroDocumento
	 *            the numeroDocumento to set
	 */
	public void setNumeroDocumento(String numeroDocumento) {
		this.documento.getCodice().setCodice(numeroDocumento);
	}

	/**
	 * @param numeroDocumentoOrder
	 *            the numeroDocumentoOrder to set
	 */
	public void setNumeroDocumentoOrder(String numeroDocumentoOrder) {
		this.documento.getCodice().setCodiceOrder(numeroDocumentoOrder);
	}

	/**
	 * @param prezzoNetto
	 *            the prezzoNetto to set
	 */
	public void setPrezzoNetto(BigDecimal prezzoNetto) {
		this.prezzoNetto = prezzoNetto;
	}

	/**
	 * @param prezzoTotale
	 *            the prezzoTotale to set
	 */
	public void setPrezzoTotale(BigDecimal prezzoTotale) {
		this.prezzoTotale = prezzoTotale;
	}

	/**
	 * @param prezzoUnitario
	 *            the prezzoUnitario to set
	 */
	public void setPrezzoUnitario(BigDecimal prezzoUnitario) {
		this.prezzoUnitario = prezzoUnitario;
	}

	/**
	 * @param quantita
	 *            the quantita to set
	 */
	public void setQuantita(Double quantita) {
		this.quantita = quantita;
	}

	/**
	 * @param quantitaEvasa
	 *            the quantitaEvasa to set
	 */
	public void setQuantitaEvasa(Double quantitaEvasa) {
		this.quantitaEvasa = quantitaEvasa;
	}

	/**
	 * @param rigaOrdineId
	 *            the rigaOrdineId to set
	 */
	public void setRigaOrdineId(Integer rigaOrdineId) {
		this.rigaOrdineId = rigaOrdineId;
	}

	/**
	 * @param tipoEntita
	 *            the tipoEntita to set
	 */
	public void setTipoEntita(String tipoEntita) {
		if (tipoEntita != null) {
			TipoEntita te = null;
			if ("C".equals(tipoEntita)) {
				te = TipoEntita.CLIENTE;
			} else if ("F".equals(tipoEntita)) {
				te = TipoEntita.FORNITORE;
			}
			this.entitaDocumento.setTipoEntita(te);
		}
	}

	/**
	 * @param tipoOmaggio
	 *            The tipoOmaggio to set.
	 */
	public void setTipoOmaggio(TipoOmaggio tipoOmaggio) {
		this.tipoOmaggio = tipoOmaggio;
	}

	/**
	 * @param variazione1
	 *            the variazione1 to set
	 */
	public void setVariazione1(BigDecimal variazione1) {
		this.variazione.setSconto1(variazione1);

	}

	/**
	 * @param variazione2
	 *            the variazione2 to set
	 */
	public void setVariazione2(BigDecimal variazione2) {
		this.variazione.setSconto2(variazione2);
	}

	/**
	 * @param variazione3
	 *            the variazione3 to set
	 */
	public void setVariazione3(BigDecimal variazione3) {
		this.variazione.setSconto3(variazione3);
	}

	/**
	 * @param variazione4
	 *            the variazione4 to set
	 */
	public void setVariazione4(BigDecimal variazione4) {
		this.variazione.setSconto4(variazione4);
	}

	public Date getDataOrdine() {
		return dataOrdine;
	}

	public void setDataOrdine(Date dataOrdine) {
		this.dataOrdine = dataOrdine;
	}

	public String getNumeroOrdineRicezione() {
		return numeroOrdineRicezione;
	}

	public void setNumeroOrdineRicezione(String numeroOrdineRicezione) {
		this.numeroOrdineRicezione = numeroOrdineRicezione;
	}

	public ModalitaRicezione getModalitaRicezione() {
		return modalitaRicezione;
	}

	public void setModalitaRicezione(ModalitaRicezione modalitaRicezione) {
		this.modalitaRicezione = modalitaRicezione;
	}



}
