/**
 * 
 */
package it.eurotn.panjea.contabilita.domain;

import it.eurotn.entity.EntityBase;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

/**
 * Registro Iva contabilità.
 * 
 * @author Leonardo
 */
@Entity
@Audited
@Table(name = "cont_registri_iva", uniqueConstraints = @UniqueConstraint(columnNames = { "codiceAzienda", "numero",
		"tipoRegistro" }))
@NamedQueries({ @NamedQuery(name = "RegistroIva.caricaByNumeroETipoRegistro", query = "from RegistroIva ri where ri.codiceAzienda = :paramCodiceAzienda and ri.tipoRegistro = :paramTipoRegistro and ri.numero = :paramNumero ") })
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "registroIva")
public class RegistroIva extends EntityBase implements java.io.Serializable {

	/**
	 * 
	 * @author giangi
	 * @version 1.0, 10/nov/2010
	 */
	public enum TipoRegistro {
		/**
		 * @uml.property name="aCQUISTO"
		 * @uml.associationEnd
		 */
		ACQUISTO, /**
		 * @uml.property name="vENDITA"
		 * @uml.associationEnd
		 */
		VENDITA, /**
		 * @uml.property name="cORRISPETTIVO"
		 * @uml.associationEnd
		 */
		CORRISPETTIVO, /**
		 * @uml.property name="rIEPILOGATIVO"
		 * @uml.associationEnd
		 */
		RIEPILOGATIVO
	}

	private static final long serialVersionUID = -4459717148905268102L;

	/**
	 * @uml.property name="codiceAzienda"
	 */
	@Column(length = 10, nullable = false)
	private String codiceAzienda;

	/**
	 * @uml.property name="descrizione"
	 */
	@Column(length = 60)
	private String descrizione = null;

	/**
	 * @uml.property name="numero"
	 */
	private Integer numero = null;

	/**
	 * @uml.property name="tipoRegistro"
	 * @uml.associationEnd
	 */
	@Enumerated
	private TipoRegistro tipoRegistro;

	/**
	 * @uml.property name="nome"
	 */
	private String nome;

	@Transient
	private String descrizioneRegistroProRata;

	/**
	 * @return the codiceAzienda
	 * @uml.property name="codiceAzienda"
	 */
	public String getCodiceAzienda() {
		return codiceAzienda;
	}

	/**
	 * @return the descrizione
	 * @uml.property name="descrizione"
	 */
	public String getDescrizione() {
		return descrizione;
	}

	/**
	 * @return Returns the descrizioneRegistroProRata.
	 */
	public String getDescrizioneRegistroProRata() {
		return descrizioneRegistroProRata;
	}

	/**
	 * @return the nome
	 * @uml.property name="nome"
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * @return the numero
	 * @uml.property name="numero"
	 */
	public Integer getNumero() {
		return numero;
	}

	/**
	 * @return the tipoRegistro
	 * @uml.property name="tipoRegistro"
	 */
	public TipoRegistro getTipoRegistro() {
		return tipoRegistro;
	}

	/**
	 * 
	 * @return true se è il registro per il pro-rata
	 */
	public boolean isProRata() {
		return getDescrizione().startsWith("Pro rata");
	}

	/**
	 * @param codiceAzienda
	 *            the codiceAzienda to set
	 * @uml.property name="codiceAzienda"
	 */
	public void setCodiceAzienda(String codiceAzienda) {
		this.codiceAzienda = codiceAzienda;
	}

	/**
	 * @param descrizione
	 *            the descrizione to set
	 * @uml.property name="descrizione"
	 */
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	/**
	 * @param descrizioneRegistroProRata
	 *            The descrizioneRegistroProRata to set.
	 */
	public void setDescrizioneRegistroProRata(String descrizioneRegistroProRata) {
		this.descrizioneRegistroProRata = descrizioneRegistroProRata;
	}

	/**
	 * @param nome
	 *            the nome to set
	 * @uml.property name="nome"
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}

	/**
	 * @param numero
	 *            the numero to set
	 * @uml.property name="numero"
	 */
	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	/**
	 * @param tipoRegistro
	 *            the tipoRegistro to set
	 * @uml.property name="tipoRegistro"
	 */
	public void setTipoRegistro(TipoRegistro tipoRegistro) {
		this.tipoRegistro = tipoRegistro;
	}

}
