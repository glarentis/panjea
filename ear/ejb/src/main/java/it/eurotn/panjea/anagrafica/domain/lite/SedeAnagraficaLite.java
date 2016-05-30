package it.eurotn.panjea.anagrafica.domain.lite;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.anagrafica.domain.datigeografici.Localita;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.envers.Audited;

/**
 * 
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
@Entity
@Audited
@Table(name = "anag_sedi_anagrafica")
public class SedeAnagraficaLite extends EntityBase {

	private static final long serialVersionUID = -4345032612963772381L;

	@Column(name = "descrizione", length = 60)
	private java.lang.String descrizione;

	@Column(name = "indirizzo", length = 60)
	private java.lang.String indirizzo;

	@ManyToOne(optional = true)
	@Fetch(FetchMode.JOIN)
	@JoinColumn(name = "localita_id")
	private Localita localita;

	/**
	 * @return descrizione
	 */
	public java.lang.String getDescrizione() {
		return descrizione;
	}

	/**
	 * @return the indirizzo
	 */
	public java.lang.String getIndirizzo() {
		return indirizzo;
	}

	/**
	 * @return localita
	 */
	public Localita getLocalita() {
		return localita;
	}

	/**
	 * @param descrizione
	 *            the descrizione to set
	 */
	public void setDescrizione(java.lang.String descrizione) {
		this.descrizione = descrizione;
	}

	/**
	 * @param indirizzo
	 *            the indirizzo to set
	 */
	public void setIndirizzo(java.lang.String indirizzo) {
		this.indirizzo = indirizzo;
	}

	/**
	 * @param localita
	 *            the localita to set
	 */
	public void setLocalita(Localita localita) {
		this.localita = localita;
	}
}
