/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.eurotn.panjea.contabilita.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Index;
import org.hibernate.envers.Audited;

/**
 * Classe incaricata di mantenere l'informazione del codice di importazione del SottoConto proveniente da una procedura.
 * esterna
 * 
 * @author adriano
 */
@Entity
@Audited
@Table(name = "cont_codici_importazione_sottoconto")
public class CodiceImportazioneSottoConto implements Serializable {

	public static final String REF = "CodiceImportazioneSottoConto";
	public static final String PROP_CODICEIMPORTAZIONE = "codiceImportazione";
	public static final String PROP_ID = "id";

	private static final long serialVersionUID = 1L;

	/**
	 * @uml.property name="id"
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	/**
	 * @uml.property name="codiceImportazione"
	 */
	@Column(length = 6, nullable = false)
	@Index(name = "codiceImportazione")
	private String codiceImportazione;

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof CodiceImportazioneSottoConto)) {
			return false;
		}
		CodiceImportazioneSottoConto other = (CodiceImportazioneSottoConto) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	/**
	 * @return codiceImportazione
	 * @uml.property name="codiceImportazione"
	 */
	public String getCodiceImportazione() {
		return codiceImportazione;
	}

	/**
	 * @return id
	 * @uml.property name="id"
	 */
	public Integer getId() {
		return id;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	/**
	 * @param codiceImportazione
	 *            the codiceImportazione to set
	 * @uml.property name="codiceImportazione"
	 */
	public void setCodiceImportazione(String codiceImportazione) {
		this.codiceImportazione = codiceImportazione;
	}

	/**
	 * @param id
	 *            the id to set
	 * @uml.property name="id"
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "CodiceImportazioneSottoConto[id=" + id + "]";
	}

}
