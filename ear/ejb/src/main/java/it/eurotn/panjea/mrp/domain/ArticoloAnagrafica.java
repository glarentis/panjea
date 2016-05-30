package it.eurotn.panjea.mrp.domain;

import java.io.Serializable;

import org.apache.commons.lang3.ObjectUtils;

/**
 * Dati di anagrafica dell'articolo utilizzati nell'mrp.
 *
 * @author giangi
 * @version 1.0, 20/dic/2013
 */
public class ArticoloAnagrafica implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer leadTime;
	private Integer ggSicurezza;
	private Integer idFornitore;

	private Integer idTipoDocumento;

	private int numDecimali;
	private Double minOrdinabile;
	private Double lottoRiordino;
	private int idArticolo;
	private boolean distinta;

	/**
	 * Dati di anagrafica dell'articolo incrociato con il fornitore abituale se presente.
	 */
	public ArticoloAnagrafica() {
	}

	/**
	 * @return Returns the ggSicurezza.
	 */
	public Integer getGgSicurezza() {
		return ObjectUtils.defaultIfNull(ggSicurezza, 0);
	}

	/**
	 * @return Returns the idArticolo.
	 */
	public int getIdArticolo() {
		return idArticolo;
	}

	/**
	 * @return Returns the idFornitore.
	 */
	public Integer getIdFornitore() {
		return idFornitore;
	}

	/**
	 * @return Returns the idTipoDocumento.
	 */
	public Integer getIdTipoDocumento() {
		return idTipoDocumento;
	}

	/**
	 * @return Returns the leadTime.
	 */
	public Integer getLeadTime() {
		return leadTime;
	}

	/**
	 * @return Returns the lottoRiordino.
	 */
	public Double getLottoRiordino() {
		return lottoRiordino;
	}

	/**
	 * @return Returns the minOrdinabile.
	 */
	public Double getMinOrdinabile() {
		return minOrdinabile;
	}

	/**
	 * @return Returns the numDecimali.
	 */
	public int getNumDecimali() {
		return numDecimali;
	}

	/**
	 * @return the distinta
	 */
	public boolean isDistinta() {
		return distinta;
	}

	/**
	 * @param distinta
	 *            the distinta to set
	 */
	public void setDistinta(boolean distinta) {
		this.distinta = distinta;
	}

	/**
	 * @param ggSicurezza
	 *            The ggSicurezza to set.
	 */
	public void setGgSicurezza(Integer ggSicurezza) {
		this.ggSicurezza = ggSicurezza;
	}

	/**
	 * @param idArticolo
	 *            The idArticolo to set.
	 */
	public void setIdArticolo(int idArticolo) {
		this.idArticolo = idArticolo;
	}

	/**
	 * @param idFornitore
	 *            The idFornitore to set.
	 */
	public void setIdFornitore(Integer idFornitore) {
		this.idFornitore = idFornitore;
	}

	/**
	 * @param idTipoDocumento
	 *            The idTipoDocumento to set.
	 */
	public void setIdTipoDocumento(Integer idTipoDocumento) {
		this.idTipoDocumento = idTipoDocumento;
	}

	/**
	 * @param leadTime
	 *            The leadTime to set.
	 */
	public void setLeadTime(Integer leadTime) {
		this.leadTime = ObjectUtils.defaultIfNull(leadTime, 0);
	}

	/**
	 * @param lottoRiordino
	 *            The lottoRiordino to set.
	 */
	public void setLottoRiordino(Double lottoRiordino) {
		this.lottoRiordino = ObjectUtils.defaultIfNull(lottoRiordino, 0.0);
	}

	/**
	 * @param minOrdinabile
	 *            The minOrdinabile to set.
	 */
	public void setMinOrdinabile(Double minOrdinabile) {
		this.minOrdinabile = ObjectUtils.defaultIfNull(minOrdinabile, 0.0);
	}

	/**
	 * @param numDecimali
	 *            The numDecimali to set.
	 */
	public void setNumDecimali(int numDecimali) {
		this.numDecimali = numDecimali;
	}

	@Override
	public String toString() {
		return "ArticoloAnagrafica [idArticolo=" + idArticolo + ", idFornitore=" + idFornitore + ", idTipoDocumento="
				+ idTipoDocumento + ", numDecimali=" + numDecimali + ", minOrdinabile=" + minOrdinabile
				+ ", lottoRiordino=" + lottoRiordino + ", leadTime=" + leadTime + ", distinta=" + distinta + "]";
	}

}
