package it.eurotn.panjea.anagrafica.documenti.domain;

import it.eurotn.entity.EntityBase;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

@Entity
@Audited
@Table(name = "docu_link_documento")
public class LinkDocumento extends EntityBase {
	public enum TIPO_COLLEGAMENTO {
		GENERICO, STORNO
	}

	private static final long serialVersionUID = -4518865257116154699L;

	@ManyToOne()
	@JoinColumn(name = "DOCUMENTO_DESTINAZIONE_ID")
	private Documento documentoOrigine;

	@ManyToOne()
	@JoinColumn(name = "DOCUMENTO_ORIGINE_ID")
	private Documento documentoDestinazione;

	private TIPO_COLLEGAMENTO tipoCollegamento;

	/**
	 * 
	 * Costruttore.
	 * 
	 */
	public LinkDocumento() {
		tipoCollegamento = TIPO_COLLEGAMENTO.GENERICO;
	}

	/**
	 * 
	 * Costruttore.
	 * 
	 * @param documentoOrigine
	 *            documento di origine del link
	 * @param documentoDestinazione
	 *            documento di destinazione del link
	 */
	public LinkDocumento(final Documento documentoOrigine, final Documento documentoDestinazione) {
		super();
		this.documentoOrigine = documentoOrigine;
		this.documentoDestinazione = documentoDestinazione;
	}

	/**
	 * @return Returns the documentoDestinazione.
	 */
	public Documento getDocumentoDestinazione() {
		return documentoDestinazione;
	}

	/**
	 * @return Returns the documentoOrigine.
	 */
	public Documento getDocumentoOrigine() {
		return documentoOrigine;
	}

	/**
	 * @return Returns the tipoCollegamento.
	 */
	public TIPO_COLLEGAMENTO getTipoCollegamento() {
		return tipoCollegamento;
	}

	/**
	 * @param documentoDestinazione
	 *            The documentoDestinazione to set.
	 */
	public void setDocumentoDestinazione(Documento documentoDestinazione) {
		this.documentoDestinazione = documentoDestinazione;
	}

	/**
	 * @param documentoOrigine
	 *            The documentoOrigine to set.
	 */
	public void setDocumentoOrigine(Documento documentoOrigine) {
		this.documentoOrigine = documentoOrigine;
	}

	/**
	 * @param tipoCollegamento
	 *            The tipoCollegamento to set.
	 */
	public void setTipoCollegamento(TIPO_COLLEGAMENTO tipoCollegamento) {
		this.tipoCollegamento = tipoCollegamento;
	}

}
