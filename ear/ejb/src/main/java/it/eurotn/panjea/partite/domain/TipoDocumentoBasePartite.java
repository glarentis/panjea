package it.eurotn.panjea.partite.domain;

import it.eurotn.entity.EntityBase;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.envers.Audited;

/**
 * Classe di dominio che definisce il tipo documento per la generezione dei documenti di generazione area partite per
 * distinte.
 * 
 * @author vittorio
 * @version 1.0, 05/dic/2008
 */
@Entity
@Audited
@Table(name = "part_tipi_documento_base", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "codiceAzienda", "tipoOperazione" }),
		@UniqueConstraint(columnNames = { "codiceAzienda", "tipoAreaPartita_id" }) })
@NamedQueries({
		@NamedQuery(name = "TipoDocumentoBasePartite.caricaByAzienda", query = "from TipoDocumentoBasePartite t where t.codiceAzienda = :paramCodiceAzienda "),
		@NamedQuery(name = "TipoDocumentoBasePartite.caricaByTipoOperazione", query = "from TipoDocumentoBasePartite t where t.codiceAzienda = :paramCodiceAzienda and t.tipoOperazione = :paramTipoOperazione ") })
public class TipoDocumentoBasePartite extends EntityBase {

	/**
	 * TipoOperazione per {@link TipoDocumentoBasePartite}.
	 * <ul>
	 * <li>DISTINTA_BANCARIA
	 * <li>VALUTA_PAGAMENTI
	 * <li>ALTRO
	 * <li>INSOLUTO
	 * <li>ACCONTO_FORNITORE
	 * <li>ACCONTO_CLIENTE
	 * <li>UTILIZZO_ACCONTO_CLIENTE
	 * <li>UTILIZZO_ACCONTO_FORNITORE
	 * <li>ANTICIPO
	 * <li>ACCONTO_IVA
	 * <li>ACCREDITO_ASSEGNO
	 * <li>BONIFICO_FORNITORE
	 * </ul>
	 * 
	 * @author Leonardo
	 */
	public enum TipoOperazione {
		DISTINTA_BANCARIA, VALUTA_PAGAMENTI, ALTRO, INSOLUTO, ACCONTO_FORNITORE, ACCONTO_CLIENTE, UTILIZZO_ACCONTO_CLIENTE, UTILIZZO_ACCONTO_FORNITORE, ANTICIPO, ACCONTO_IVA, ACCREDITO_ASSEGNO, BONIFICO_FORNITORE
	}

	private static final long serialVersionUID = 1249390881803948755L;

	@Column(length = 10, nullable = false)
	private String codiceAzienda;

	@ManyToOne
	private TipoAreaPartita tipoAreaPartita;

	@Enumerated
	private TipoOperazione tipoOperazione;

	/**
	 * Costruttore.
	 */
	public TipoDocumentoBasePartite() {
		super();
		initialize();
	}

	/**
	 * @return codiceAzienda
	 */
	public String getCodiceAzienda() {
		return codiceAzienda;
	}

	/**
	 * @return tipoAreaPartita
	 */
	public TipoAreaPartita getTipoAreaPartita() {
		return tipoAreaPartita;
	}

	/**
	 * @return tipoOperazione
	 */
	public TipoOperazione getTipoOperazione() {
		return tipoOperazione;
	}

	/**
	 * Init degli oggetti.
	 */
	private void initialize() {
		this.tipoAreaPartita = new TipoAreaPartita();
	}

	/**
	 * @param codiceAzienda
	 *            the codiceAzienda to set
	 */
	public void setCodiceAzienda(String codiceAzienda) {
		this.codiceAzienda = codiceAzienda;
	}

	/**
	 * @param tipoAreaPartita
	 *            the tipoAreaPartita to set
	 */
	public void setTipoAreaPartita(TipoAreaPartita tipoAreaPartita) {
		this.tipoAreaPartita = tipoAreaPartita;
	}

	/**
	 * @param tipoOperazione
	 *            the tipoOperazione to set
	 */
	public void setTipoOperazione(TipoOperazione tipoOperazione) {
		this.tipoOperazione = tipoOperazione;
	}
}
