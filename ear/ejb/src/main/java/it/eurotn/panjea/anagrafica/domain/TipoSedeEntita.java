package it.eurotn.panjea.anagrafica.domain;

import it.eurotn.entity.EntityBase;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Index;
import org.hibernate.envers.Audited;

/**
 * 
 * 
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
@Entity
@Audited
@Table(name = "anag_tipo_sede_entita", uniqueConstraints = { @UniqueConstraint(columnNames = "codice") })
@NamedQueries({
		@NamedQuery(name = "TipoSedeEntita.caricaTipiSedeByCodice", query = " from TipoSedeEntita where codice like :codice"),
		@NamedQuery(name = "TipoSedeEntita.caricaTipiSedeSecondarie", query = " from TipoSedeEntita t where t.sedePrincipale = false "),
		@NamedQuery(name = "TipoSedeEntita.caricaTipoSedePrincipale", query = " from TipoSedeEntita t where t.sedePrincipale = true "),
		@NamedQuery(name = "TipoSedeEntita.caricaTipoSedeByTipoSede", query = " select t from TipoSedeEntita t where t.tipoSede=:paramTipoSede") })
public class TipoSedeEntita extends EntityBase {

	/**
	 * La tipologia del tipo sede entita'.<br/>
	 * <ul>
	 * <li>NORMALE: tipo standard</li>
	 * <li>DA_DOCUMENTO: tipo sede utilizzato per la creazione di una sede on the fly</li>
	 * </ul>
	 * 
	 * @author Leonardo
	 */
	public enum TipoSede {
		NORMALE, DA_DOCUMENTO, INDIRIZZO_SPEDIZIONE, SERVIZIO
	}

	private static final long serialVersionUID = 1969767618628157463L;

	public static final String REF = "TipoSedeEntita";
	public static final String PROP_CODICE = "codice";
	public static final String PROP_USER_UPDATE = "userUpdate";
	public static final String PROP_DATE_INSERT = "dateInsert";
	public static final String PROP_USER_INSERT = "userInsert";
	public static final String PROP_ABILITATO = "abilitato";
	public static final String PROP_SEDE_PRINCIPALE = "sedePrincipale";
	public static final String PROP_DATE_UPDATE = "dateUpdate";
	public static final String PROP_ID = "id";
	public static final String PROP_TIPO_SEDE = "tipoSede";
	public static final String PROP_DESCRIZIONE = "descrizione";

	/**
	 * @uml.property name="codice"
	 */
	@Column(name = "codice", length = 10)
	@Index(name = "tiposedeEntitaCodice")
	private String codice;
	/**
	 * @uml.property name="descrizione"
	 */
	@Column(name = "descrizione", length = 60)
	private java.lang.String descrizione;
	/**
	 * @uml.property name="sedePrincipale"
	 */
	@Column(name = "sede_principale")
	private boolean sedePrincipale;
	/**
	 * @uml.property name="abilitato"
	 */
	@Column(name = "abilitato")
	private boolean abilitato;

	/**
	 * @uml.property name="tipoSede"
	 * @uml.associationEnd
	 */
	@Enumerated
	private TipoSede tipoSede;

	/**
	 * Costruttore.
	 * 
	 */
	public TipoSedeEntita() {
		super();
		tipoSede = TipoSede.NORMALE;
	}

	/**
	 * @return codice
	 * @uml.property name="codice"
	 */
	public String getCodice() {
		return codice;
	}

	/**
	 * @return descrizione
	 * @uml.property name="descrizione"
	 */
	public java.lang.String getDescrizione() {
		return descrizione;
	}

	/**
	 * @return tipoSede
	 * @uml.property name="tipoSede"
	 */
	public TipoSede getTipoSede() {
		return tipoSede;
	}

	/**
	 * @return abilitato
	 * @uml.property name="abilitato"
	 */
	public boolean isAbilitato() {
		return abilitato;
	}

	/**
	 * @return sedePrincipale
	 * @uml.property name="sedePrincipale"
	 */
	public boolean isSedePrincipale() {
		return sedePrincipale;
	}

	/**
	 * @param abilitato
	 *            the abilitato to set
	 * @uml.property name="abilitato"
	 */
	public void setAbilitato(boolean abilitato) {
		this.abilitato = abilitato;
	}

	/**
	 * @param codice
	 *            the codice to set
	 * @uml.property name="codice"
	 */
	public void setCodice(String codice) {
		this.codice = codice;
	}

	/**
	 * @param descrizione
	 *            the descrizione to set
	 * @uml.property name="descrizione"
	 */
	public void setDescrizione(java.lang.String descrizione) {
		this.descrizione = descrizione;
	}

	/**
	 * @param sedePrincipale
	 *            the sedePrincipale to set
	 * @uml.property name="sedePrincipale"
	 */
	public void setSedePrincipale(boolean sedePrincipale) {
		this.sedePrincipale = sedePrincipale;
	}

	/**
	 * @param tipoSede
	 *            the tipoSede to set
	 * @uml.property name="tipoSede"
	 */
	public void setTipoSede(TipoSede tipoSede) {
		this.tipoSede = tipoSede;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("TipoSedeEntita[");
		buffer.append(super.toString());
		buffer.append(" abilitato = ").append(abilitato);
		buffer.append(" codice = ").append(codice);
		buffer.append(" descrizione = ").append(descrizione);
		buffer.append(" sedePrincipale = ").append(sedePrincipale);
		buffer.append(" tipoSede = ").append(tipoSede);
		buffer.append("]");
		return buffer.toString();
	}

}