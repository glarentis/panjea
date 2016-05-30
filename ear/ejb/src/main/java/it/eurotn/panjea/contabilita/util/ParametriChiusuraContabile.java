/**
 * 
 */
package it.eurotn.panjea.contabilita.util;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * Parametri per esecuzione dell'operazione di chiusura contabile.
 * 
 * @author adriano
 * @version 1.0, 29/ago/07
 */
public class ParametriChiusuraContabile implements Serializable {

	private static final long serialVersionUID = 6148796129595524952L;

	/**
	 * @uml.property name="anno"
	 */
	private Integer anno;

	/**
	 * @uml.property name="dataMovimento"
	 */
	private Date dataMovimento;

	/**
	 * @return Returns the anno.
	 * @uml.property name="anno"
	 */
	public Integer getAnno() {
		return anno;
	}

	/**
	 * Restituisce l'anno dell'attributo dataMovimento.
	 * 
	 * @return
	 */
	public int getAnnoMovimento() {
		if (dataMovimento != null) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(dataMovimento);
			return calendar.get(Calendar.YEAR);
		}
		return -1;
	}

	public int getAnnoMovimentoPrecedente() {
		int annoMovimento = getAnnoMovimento();
		if (annoMovimento != 0) {
			return annoMovimento - 1;
		}
		return annoMovimento;
	}

	/**
	 * @return Returns the dataMovimento.
	 * @uml.property name="dataMovimento"
	 */
	public Date getDataMovimento() {
		return dataMovimento;
	}

	/**
	 * @param anno
	 *            The anno to set.
	 * @uml.property name="anno"
	 */
	public void setAnno(Integer anno) {
		this.anno = anno;
	}

	/**
	 * @param dataMovimento
	 *            The dataMovimento to set.
	 * @uml.property name="dataMovimento"
	 */
	public void setDataMovimento(Date dataMovimento) {
		this.dataMovimento = dataMovimento;
	}

}
