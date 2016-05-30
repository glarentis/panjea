package it.eurotn.panjea.auvend.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * Descrive un documento generico di AuVend.
 * 
 * @author giangi
 * 
 */
public class Documento implements Serializable, Comparable<Documento> {
	private static final long serialVersionUID = -8500498392180570498L;
	private Integer numero;
	private Date data;
	private int id;// se presente in Panjea indica l'id della classe DOCUMENTO

	@Override
	public int compareTo(Documento o) {
		return this.getNumero().compareTo(o.getNumero());
	}

	/**
	 * @return data
	 */
	public Date getData() {
		return data;
	}

	/**
	 * @return id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return numero
	 */
	public Integer getNumero() {
		return numero;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(Date data) {
		this.data = data;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @param numero
	 *            the numero to set
	 */
	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	/**
	 * Constructs a <code>String</code> with all attributes in name = value format.
	 * 
	 * @return a <code>String</code> representation of this object.
	 */
	@Override
	public String toString() {
		final String tab = "    ";

		String retValue = "";

		retValue = "Documento ( " + super.toString() + tab + "numero = " + this.numero + tab + "data = " + this.data
				+ tab + "id = " + this.id + tab + " )";

		return retValue;
	}

}
