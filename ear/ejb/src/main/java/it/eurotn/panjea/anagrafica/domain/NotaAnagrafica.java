package it.eurotn.panjea.anagrafica.domain;

import it.eurotn.entity.EntityBase;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.envers.Audited;

@Entity
@Audited
@Table(name = "anag_note_anagrafica", uniqueConstraints = @UniqueConstraint(columnNames = { "codice" }))
@NamedQueries({ @NamedQuery(name = "NotaAnagrafica.caricaAll", query = "from NotaAnagrafica") })
public class NotaAnagrafica extends EntityBase {

	private static final long serialVersionUID = -4075742652361002708L;

	private String codice;

	@Lob
	private String descrizione;

	/**
	 * @return the codice
	 */
	public String getCodice() {
		return codice;
	}

	/**
	 * @return the descrizione
	 */
	public String getDescrizione() {
		return descrizione;
	}

	/**
	 * @param codice
	 *            the codice to set
	 */
	public void setCodice(String codice) {
		this.codice = codice;
	}

	/**
	 * @param descrizione
	 *            the descrizione to set
	 */
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
}
