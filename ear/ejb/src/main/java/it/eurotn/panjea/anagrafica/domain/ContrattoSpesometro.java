/**
 * 
 */
package it.eurotn.panjea.anagrafica.domain;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Index;
import org.hibernate.envers.Audited;

/**
 * @author leonardo
 */
@Entity
@Audited
@Table(name = "anag_contratti_spesometro", uniqueConstraints = @UniqueConstraint(columnNames = { "codiceAzienda",
		"codice" }))
@NamedQueries({
		@NamedQuery(name = "ContrattoSpesometro.caricaContratti", query = "from ContrattoSpesometro c where c.codiceAzienda = :paramCodiceAzienda "),
		@NamedQuery(name = "ContrattoSpesometro.caricaContrattiEntita", query = "from ContrattoSpesometro c where c.codiceAzienda = :paramCodiceAzienda and c.entita.id=:paramIdEntita ") })
public class ContrattoSpesometro extends EntityBase {

	private static final long serialVersionUID = -4995834794339194719L;

	@Column(length = 15, nullable = false)
	private String codice;

	@Column(length = 10, nullable = false)
	private String codiceAzienda;

	@Column
	@Temporal(TemporalType.DATE)
	@Index(name = "index_dataInizio")
	private Date dataInizio;

	@Column
	@Temporal(TemporalType.DATE)
	@Index(name = "index_dataFine")
	private Date dataFine;

	@ManyToOne
	private EntitaLite entita;

	private String note;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "contrattoSpesometro")
	private Set<Documento> documenti;

	@Transient
	private Documento documento;

	/**
	 * @return the codice
	 */
	public String getCodice() {
		return codice;
	}

	/**
	 * @return the codiceAzienda
	 */
	public String getCodiceAzienda() {
		return codiceAzienda;
	}

	/**
	 * @return the dataFine
	 */
	public Date getDataFine() {
		return dataFine;
	}

	/**
	 * @return the dataInizio
	 */
	public Date getDataInizio() {
		return dataInizio;
	}

	/**
	 * @return the documenti
	 */
	public Set<Documento> getDocumenti() {
		return documenti;
	}

	/**
	 * @return the documento
	 */
	public Documento getDocumento() {
		return documento;
	}

	/**
	 * @return the entita
	 */
	public EntitaLite getEntita() {
		return entita;
	}

	/**
	 * @return the note
	 */
	public String getNote() {
		return note;
	}

	/**
	 * @param codice
	 *            the codice to set
	 */
	public void setCodice(String codice) {
		this.codice = codice;
	}

	/**
	 * @param codiceAzienda
	 *            the codiceAzienda to set
	 */
	public void setCodiceAzienda(String codiceAzienda) {
		this.codiceAzienda = codiceAzienda;
	}

	/**
	 * @param dataFine
	 *            the dataFine to set
	 */
	public void setDataFine(Date dataFine) {
		this.dataFine = dataFine;
	}

	/**
	 * @param dataInizio
	 *            the dataInizio to set
	 */
	public void setDataInizio(Date dataInizio) {
		this.dataInizio = dataInizio;
	}

	/**
	 * @param documenti
	 *            the documenti to set
	 */
	public void setDocumenti(Set<Documento> documenti) {
		this.documenti = documenti;
	}

	/**
	 * @param documento
	 *            the documento to set
	 */
	public void setDocumento(Documento documento) {
		this.documento = documento;
	}

	/**
	 * @param entita
	 *            the entita to set
	 */
	public void setEntita(EntitaLite entita) {
		this.entita = entita;
	}

	/**
	 * @param note
	 *            the note to set
	 */
	public void setNote(String note) {
		this.note = note;
	}

}
