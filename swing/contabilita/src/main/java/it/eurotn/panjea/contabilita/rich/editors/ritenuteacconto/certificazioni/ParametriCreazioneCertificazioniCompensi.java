/**
 * 
 */
package it.eurotn.panjea.contabilita.rich.editors.ritenuteacconto.certificazioni;

import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;

import java.util.Calendar;
import java.util.Date;

/**
 * @author fattazzo
 * 
 */
public class ParametriCreazioneCertificazioniCompensi {

	private Integer anno;

	private EntitaLite entita;

	private Date dataCertificazione;

	{
		Calendar calendar = Calendar.getInstance();
		anno = calendar.get(Calendar.YEAR);
		entita = null;
		calendar.set(Calendar.DAY_OF_MONTH, 28);
		calendar.set(Calendar.MONTH, 1);
		calendar.set(Calendar.YEAR, anno + 1);
		dataCertificazione = calendar.getTime();
	}

	/**
	 * @return the anno
	 */
	public Integer getAnno() {
		return anno;
	}

	/**
	 * @return the dataCertificazione
	 */
	public Date getDataCertificazione() {
		return dataCertificazione;
	}

	/**
	 * @return the entita
	 */
	public EntitaLite getEntita() {
		return entita;
	}

	/**
	 * @param anno
	 *            the anno to set
	 */
	public void setAnno(Integer anno) {
		this.anno = anno;
	}

	/**
	 * @param dataCertificazione
	 *            the dataCertificazione to set
	 */
	public void setDataCertificazione(Date dataCertificazione) {
		this.dataCertificazione = dataCertificazione;
	}

	/**
	 * @param entita
	 *            the entita to set
	 */
	public void setEntita(EntitaLite entita) {
		this.entita = entita;
	}

}
