package it.eurotn.panjea.cauzioni.util.parametriricerca;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.util.EntitaDocumento;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.Categoria;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class SituazioneCauzioniDTO implements Serializable {

	private static final long serialVersionUID = 5058604066642895066L;

	private Date dataRegistrazione;

	private EntitaDocumento entitaDocumento;

	private SedeEntita sedeEntita;

	private Categoria categoria;

	private Double dati;
	private Double resi;

	private Double saldo;

	private Double saldoEntita;

	private Integer numeroDecimaliQta;

	private BigDecimal importoDati;

	private BigDecimal importoResi;

	private BigDecimal saldoImporto;

	private ArticoloLite articolo;

	/**
	 * Costruttore.
	 *
	 */
	public SituazioneCauzioniDTO() {
		super();
		initialize();
	}

	/**
	 * @return Returns the articolo.
	 */
	public ArticoloLite getArticolo() {
		return articolo;
	}

	/**
	 * @return the categoria
	 */
	public Categoria getCategoria() {
		return categoria;
	}

	/**
	 * @return the dataRegistrazione
	 */
	public Date getDataRegistrazione() {
		return dataRegistrazione;
	}

	/**
	 * @return Returns the dati.
	 */
	public Double getDati() {
		return dati;
	}

	/**
	 * @return the entitaDocumento
	 */
	public EntitaDocumento getEntitaDocumento() {
		return entitaDocumento;
	}

	/**
	 * @return Returns the importoDati.
	 */
	public BigDecimal getImportoDati() {
		return importoDati;
	}

	/**
	 * @return Returns the importoResi.
	 */
	public BigDecimal getImportoResi() {
		return importoResi;
	}

	/**
	 * @return the numeroDecimaliQta
	 */
	public Integer getNumeroDecimaliQta() {
		return numeroDecimaliQta;
	}

	/**
	 * @return Returns the resi.
	 */
	public Double getResi() {
		return resi;
	}

	/**
	 * @return Returns the saldo.
	 */
	public Double getSaldo() {
		return saldo;
	}

	public Double getSaldoEntita() {
		return saldoEntita;
	}

	/**
	 * @return the saldoImporto
	 */
	public BigDecimal getSaldoImporto() {
		return saldoImporto;
	}

	/**
	 * @return the sedeEntita
	 */
	public SedeEntita getSedeEntita() {
		return sedeEntita;
	}

	/**
	 * Init degli oggetti di this.
	 */
	public void initialize() {
		entitaDocumento = new EntitaDocumento();
		sedeEntita = new SedeEntita();
		categoria = new Categoria();
		articolo = new ArticoloLite();
		numeroDecimaliQta = 6;
	}

	/**
	 * @param codiceArticolo
	 *            The codiceArticolo to set.
	 */
	public void setCodiceArticolo(String codiceArticolo) {
		this.articolo.setCodice(codiceArticolo);
	}

	/**
	 * @param codiceCategoria
	 *            the codiceCategoria to set
	 */
	public void setCodiceCategoria(String codiceCategoria) {
		this.categoria.setCodice(codiceCategoria);
	}

	/**
	 * @param codiceEntita
	 *            the codiceEntita to set
	 */
	public void setCodiceEntita(Integer codiceEntita) {
		this.entitaDocumento.setCodice(codiceEntita);
	}

	/**
	 * @param codiceSedeEntita
	 *            the codiceSedeEntita to set
	 */
	public void setCodiceSedeEntita(String codiceSedeEntita) {
		this.sedeEntita.setCodice(codiceSedeEntita);
	}

	/**
	 * @param dataRegistrazione
	 *            the dataRegistrazione to set
	 */
	public void setDataRegistrazione(Date dataRegistrazione) {
		this.dataRegistrazione = dataRegistrazione;
	}

	/**
	 * @param dati
	 *            The dati to set.
	 */
	public void setDati(Double dati) {
		this.dati = dati;
	}

	/**
	 * @param descrizioneArticolo
	 *            The descrizioneArticolo to set.
	 */
	public void setDescrizioneArticolo(String descrizioneArticolo) {
		this.articolo.setDescrizione(descrizioneArticolo);
	}

	/**
	 * @param descrizioneCategoria
	 *            the descrizioneCategoria to set
	 */
	public void setDescrizioneCategoria(String descrizioneCategoria) {
		this.categoria.setDescrizione(descrizioneCategoria);
	}

	/**
	 * @param descrizioneEntita
	 *            the descrizioneEntita to set
	 */
	public void setDescrizioneEntita(String descrizioneEntita) {
		this.entitaDocumento.setDescrizione(descrizioneEntita);
	}

	/**
	 * @param descrizioneSedeEntita
	 *            the descrizioneSedeEntita to set
	 */
	public void setDescrizioneSedeEntita(String descrizioneSedeEntita) {
		this.sedeEntita.getSede().setDescrizione(descrizioneSedeEntita);
	}

	/**
	 * @param idArticolo
	 *            The idArticolo to set.
	 */
	public void setIdArticolo(Integer idArticolo) {
		this.articolo.setId(idArticolo);
	}

	/**
	 * @param idCategoria
	 *            the idCategoria to set
	 */
	public void setIdCategoria(Integer idCategoria) {
		this.categoria.setId(idCategoria);
	}

	/**
	 * @param idEntita
	 *            the idEntita to set
	 */
	public void setIdEntita(Integer idEntita) {
		this.entitaDocumento.setId(idEntita);
	}

	/**
	 * @param idSedeEntita
	 *            the idSedeEntita to set
	 */
	public void setIdSedeEntita(Integer idSedeEntita) {
		this.sedeEntita.setId(idSedeEntita);
	}

	/**
	 * @param importoDati
	 *            The importoDati to set.
	 */
	public void setImportoDati(BigDecimal importoDati) {
		this.importoDati = importoDati;
	}

	/**
	 * @param importoResi
	 *            The importoResi to set.
	 */
	public void setImportoResi(BigDecimal importoResi) {
		this.importoResi = importoResi;
	}

	/**
	 * @param numeroDecimaliQta
	 *            the numeroDecimaliQta to set
	 */
	public void setNumeroDecimaliQta(Integer numeroDecimaliQta) {
		this.numeroDecimaliQta = numeroDecimaliQta;
	}

	/**
	 * @param resi
	 *            The resi to set.
	 */
	public void setResi(Double resi) {
		this.resi = resi;
	}

	/**
	 * @param saldo
	 *            The saldo to set.
	 */
	public void setSaldo(Double saldo) {
		this.saldo = saldo;
	}

	public void setSaldoEntita(Double saldoEntita) {
		this.saldoEntita = saldoEntita;
	}

	/**
	 * @param saldoImporto
	 *            the saldoImporto to set
	 */
	public void setSaldoImporto(BigDecimal saldoImporto) {
		this.saldoImporto = saldoImporto;
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
}
