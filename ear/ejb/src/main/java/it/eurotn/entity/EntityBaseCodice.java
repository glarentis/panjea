package it.eurotn.entity;

import javax.persistence.MappedSuperclass;

/**
 * Ereditata da tutti gli oggetti persistenti che devono avere un campo codice generato.
 * 
 * @author giangi
 * 
 */
@MappedSuperclass
public class EntityBaseCodice extends EntityBase {

	private static final long serialVersionUID = 7517438619165897252L;
	private Integer codice;
	private String prefisso;

	/**
	 * @return Returns the codice.
	 */
	public Integer getCodice() {
		return codice;
	}

	/**
	 * @return Returns the prefisso.
	 */
	public String getPrefisso() {
		return prefisso;
	}

	/**
	 * @param codice
	 *            The codice to set.
	 */
	public void setCodice(Integer codice) {
		this.codice = codice;
	}

	/**
	 * @param prefisso
	 *            The prefisso to set.
	 */
	public void setPrefisso(String prefisso) {
		this.prefisso = prefisso;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(super.toString());
		buffer.append(" codice = ").append(codice);
		buffer.append(" prefisso = ").append(prefisso);
		return buffer.toString();
	}
}
