/**
 * 
 */
package it.eurotn.panjea.auvend.util;

import java.util.Date;

/**
 * Classe di utilita' che rappresenta la testata di carico dei caricatori di AuVend.
 * 
 * @author adriano
 * @version 1.0, 24/dic/2008
 * 
 */
public class BollaUscitaTestata {

	private int progressivo;

	private String caricatore;

	private String depositoBase;

	private String targa;

	private String giro;

	private Date dataBolla;

	private String numeroBolla;

	private boolean bollaChiusa;

	private Date ultimaModifica;

	/**
	 * @return Returns the caricatore.
	 */
	public String getCaricatore() {
		return caricatore;
	}

	/**
	 * @return Returns the dataBolla.
	 */
	public Date getDataBolla() {
		return dataBolla;
	}

	/**
	 * @return Returns the depositoBase.
	 */
	public String getDepositoBase() {
		return depositoBase;
	}

	/**
	 * @return Returns the giro.
	 */
	public String getGiro() {
		return giro;
	}

	/**
	 * @return Returns the numeroBolla.
	 */
	public String getNumeroBolla() {
		return numeroBolla;
	}

	/**
	 * @return Returns the progressivo.
	 */
	public int getProgressivo() {
		return progressivo;
	}

	/**
	 * @return Returns the targa.
	 */
	public String getTarga() {
		return targa;
	}

	/**
	 * @return Returns the ultimaModifica.
	 */
	public Date getUltimaModifica() {
		return ultimaModifica;
	}

	/**
	 * @return Returns the bollaChiusa.
	 */
	public boolean isBollaChiusa() {
		return bollaChiusa;
	}

	/**
	 * @param bollaChiusa
	 *            The bollaChiusa to set.
	 */
	public void setBollaChiusa(boolean bollaChiusa) {
		this.bollaChiusa = bollaChiusa;
	}

	/**
	 * @param caricatore
	 *            The caricatore to set.
	 */
	public void setCaricatore(String caricatore) {
		this.caricatore = caricatore;
	}

	/**
	 * @param dataBolla
	 *            The dataBolla to set.
	 */
	public void setDataBolla(Date dataBolla) {
		this.dataBolla = dataBolla;
	}

	/**
	 * @param depositoBase
	 *            The depositoBase to set.
	 */
	public void setDepositoBase(String depositoBase) {
		this.depositoBase = depositoBase;
	}

	/**
	 * @param giro
	 *            The giro to set.
	 */
	public void setGiro(String giro) {
		this.giro = giro;
	}

	/**
	 * @param numeroBolla
	 *            The numeroBolla to set.
	 */
	public void setNumeroBolla(String numeroBolla) {
		this.numeroBolla = numeroBolla;
	}

	/**
	 * @param progressivo
	 *            The progressivo to set.
	 */
	public void setProgressivo(int progressivo) {
		this.progressivo = progressivo;
	}

	/**
	 * @param targa
	 *            The targa to set.
	 */
	public void setTarga(String targa) {
		this.targa = targa;
	}

	/**
	 * @param ultimaModifica
	 *            The ultimaModifica to set.
	 */
	public void setUltimaModifica(Date ultimaModifica) {
		this.ultimaModifica = ultimaModifica;
	}

}
