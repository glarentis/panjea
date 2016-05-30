/**
 *
 */
package it.eurotn.panjea.offerte.domain;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.domain.Contatto;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.sicurezza.domain.Utente;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * @author Leonardo
 */
@Entity
@Table(name = "offe_area_offerta")
@NamedQueries({ @NamedQuery(name = "AreaOfferta.ricercaByDocumento", query = "select a from AreaOfferta a inner join a.documento d where d.id = :paramIdDocumento") })
public class AreaOfferta extends EntityBase {

	private static final long serialVersionUID = 3549110064678845282L;

	@ManyToOne
	private TipoAreaOfferta tipoAreaOfferta;

	@OneToOne
	private Documento documento;

	@ManyToOne
	private SedeEntita sedeEntita;

	@ManyToOne
	private Contatto contatto;

	@ManyToOne
	private Utente utente;

	@Embedded
	private CondizioneFornitura condizioneFornitura;

	@Lob
	private String descrizione;

	@Lob
	private String note;

	private boolean stampaTotale;

	{
		documento = new Documento();
	}

	/**
	 * Costruttore.
	 */
	public AreaOfferta() {
		super();
	}

	/**
	 * @return the condizioneFornitura
	 */
	public CondizioneFornitura getCondizioneFornitura() {
		return condizioneFornitura;
	}

	/**
	 * @return the contatto
	 */
	public Contatto getContatto() {
		return contatto;
	}

	/**
	 * @return the descrizione
	 */
	public String getDescrizione() {
		return descrizione;
	}

	/**
	 * @return the documento
	 */
	public Documento getDocumento() {
		return documento;
	}

	/**
	 * @return the note
	 */
	public String getNote() {
		return note;
	}

	/**
	 * @return the sedeEntita
	 */
	public SedeEntita getSedeEntita() {
		return sedeEntita;
	}

	/**
	 * @return the tipoAreaOfferta
	 */
	public TipoAreaOfferta getTipoAreaOfferta() {
		return tipoAreaOfferta;
	}

	/**
	 * @return the utente
	 */
	public Utente getUtente() {
		return utente;
	}

	/**
	 * @return the stampaTotale
	 */
	public boolean isStampaTotale() {
		return stampaTotale;
	}

	/**
	 * @param condizioneFornitura
	 *            the condizioneFornitura to set
	 */
	public void setCondizioneFornitura(CondizioneFornitura condizioneFornitura) {
		this.condizioneFornitura = condizioneFornitura;
	}

	/**
	 * @param contatto
	 *            the contatto to set
	 */
	public void setContatto(Contatto contatto) {
		this.contatto = contatto;
	}

	/**
	 * @param descrizione
	 *            the descrizione to set
	 */
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	/**
	 * @param documento
	 *            the documento to set
	 */
	public void setDocumento(Documento documento) {
		this.documento = documento;
	}

	/**
	 * @param note
	 *            the note to set
	 */
	public void setNote(String note) {
		this.note = note;
	}

	/**
	 * @param sedeEntita
	 *            the sedeEntita to set
	 */
	public void setSedeEntita(SedeEntita sedeEntita) {
		this.sedeEntita = sedeEntita;
	}

	/**
	 * @param stampaTotale
	 *            the stampaTotale to set
	 */
	public void setStampaTotale(boolean stampaTotale) {
		this.stampaTotale = stampaTotale;
	}

	/**
	 * @param tipoAreaOfferta
	 *            the tipoAreaOfferta to set
	 */
	public void setTipoAreaOfferta(TipoAreaOfferta tipoAreaOfferta) {
		this.tipoAreaOfferta = tipoAreaOfferta;
	}

	/**
	 * @param utente
	 *            the utente to set
	 */
	public void setUtente(Utente utente) {
		this.utente = utente;
	}

}
