package it.eurotn.panjea.cauzioni.util.parametriricerca;

import it.eurotn.panjea.anagrafica.documenti.domain.CodiceDocumento;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.util.EntitaDocumento;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class MovimentazioneCauzioneDTO implements Serializable {

	private static final long serialVersionUID = 6348977177670580303L;

	private Integer idAreaMagazzino;

	private Date dataRegistrazione;

	private Date dataDocumento;

	private TipoDocumento tipoDocumento;
	private CodiceDocumento numeroDocumento;

	private EntitaDocumento entitaDocumento;

	private SedeEntita sedeEntita;

	private Double dati;
	private Double resi;

	private Integer numeroDecimaliQta;

	private BigDecimal importoDati;

	private BigDecimal importoResi;

	/**
	 * Costruttore.
	 * 
	 */
	public MovimentazioneCauzioneDTO() {
		super();
		initialize();
	}

	/**
	 * @return the dataDocumento
	 */
	public Date getDataDocumento() {
		return dataDocumento;
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
	public EntitaDocumento getEntita() {
		return entitaDocumento;
	}

	/**
	 * @return Returns the idAreaMagazzino.
	 */
	public Integer getIdAreaMagazzino() {
		return idAreaMagazzino;
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
	 * @return the numeroDocumento
	 */
	public CodiceDocumento getNumeroDocumento() {
		return numeroDocumento;
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
		return dati - resi;
	}

	/**
	 * @return the sedeEntita
	 */
	public SedeEntita getSedeEntita() {
		return sedeEntita;
	}

	/**
	 * @return the tipoDocumento
	 */
	public TipoDocumento getTipoDocumento() {
		return tipoDocumento;
	}

	/**
	 * Init degli oggetti di this.
	 */
	public void initialize() {
		this.tipoDocumento = new TipoDocumento();
		this.entitaDocumento = new EntitaDocumento();
		this.sedeEntita = new SedeEntita();
		this.numeroDocumento = new CodiceDocumento();
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
	 * @param codiceTipoDocumento
	 *            the codiceTipoDocumento to set
	 */
	public void setCodiceTipoDocumento(String codiceTipoDocumento) {
		this.tipoDocumento.setCodice(codiceTipoDocumento);
	}

	/**
	 * @param dataDocumento
	 *            the dataDocumento to set
	 */
	public void setDataDocumento(Date dataDocumento) {
		this.dataDocumento = dataDocumento;
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
	 * @param descrizioneTipoDocumento
	 *            the descrizioneTipoDocumento to set
	 */
	public void setDescrizioneTipoDocumento(String descrizioneTipoDocumento) {
		this.tipoDocumento.setDescrizione(descrizioneTipoDocumento);
	}

	/**
	 * @param idAreaMagazzino
	 *            The idAreaMagazzino to set.
	 */
	public void setIdAreaMagazzino(Integer idAreaMagazzino) {
		this.idAreaMagazzino = idAreaMagazzino;
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
	 * @param idTipoDocumento
	 *            the idTipoDocumento to set
	 */
	public void setIdTipoDocumento(Integer idTipoDocumento) {
		this.tipoDocumento.setId(idTipoDocumento);
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
	 * @param numeroDocumento
	 *            the numeroDocumento to set
	 */
	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento.setCodice(numeroDocumento);
	}

	/**
	 * @param resi
	 *            The resi to set.
	 */
	public void setResi(Double resi) {
		this.resi = resi;
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
