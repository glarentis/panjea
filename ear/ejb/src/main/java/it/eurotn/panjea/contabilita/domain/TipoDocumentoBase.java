/**
 *
 */
package it.eurotn.panjea.contabilita.domain;

import it.eurotn.entity.EntityBase;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.QueryHint;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

/**
 * Classe di dominio che definisce il tipo documento per la generezione dei documenti di apertura e chiusura.
 *
 * @author adriano
 * @version 1.0, 27/ago/07
 */
@Entity
@Audited
@Table(name = "cont_tipi_documento_base", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "codiceAzienda", "tipoOperazione" }),
		@UniqueConstraint(columnNames = { "codiceAzienda", "tipoAreaContabile_id" }) })
@NamedQueries({
	@NamedQuery(name = "TipoDocumentoBase.caricaByAzienda", query = "from TipoDocumentoBase t where t.codiceAzienda = :paramCodiceAzienda ", hints = {
			@QueryHint(name = "org.hibernate.cacheable", value = "true"),
			@QueryHint(name = "org.hibernate.cacheRegion", value = "tipiDocumentoContabiliBase") }),
			@NamedQuery(name = "TipoDocumentoBase.caricaByTipoOperazione", query = "from TipoDocumentoBase t where t.codiceAzienda = :paramCodiceAzienda and t.tipoOperazione = :paramTipoOperazione ") })
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "tipiDocumentoContabiliBase")
public class TipoDocumentoBase extends EntityBase implements java.io.Serializable {

	/**
	 * @author giangi
	 * @version 1.0, 10/nov/2010
	 */
	public enum TipoOperazioneTipoDocumento {
		CHIUSURA_CONTO_ECONOMICO, CHIUSURA_CONTO_PATRIMONIALE, CHIUSURA_CONTO_ORDINE, APERTURA_CONTO_PATRIMONIALE, APERTURA_CONTO_ORDINE, LIQUIDAZIONE_IVA, AC_SIMULAZIONE_BENI, CHIUSURA_RISCONTI, CHIUSURA_RATEI;
	}

	private static final long serialVersionUID = 7968830913412954438L;

	@Column(length = 10, nullable = false)
	private String codiceAzienda;

	@ManyToOne
	private TipoAreaContabile tipoAreaContabile;

	@Enumerated
	private TipoOperazioneTipoDocumento tipoOperazione;

	/**
	 * Costruttore.
	 */
	public TipoDocumentoBase() {
		initialize();
	}

	/**
	 * @return Returns the codiceAzienda.
	 */
	public String getCodiceAzienda() {
		return codiceAzienda;
	}

	/**
	 * @return Returns the tipoAreaContabile.
	 */
	public TipoAreaContabile getTipoAreaContabile() {
		return tipoAreaContabile;
	}

	/**
	 * @return Returns the tipoOperazione.
	 */
	public TipoOperazioneTipoDocumento getTipoOperazione() {
		return tipoOperazione;
	}

	/**
	 * Inizializza i valori di default.
	 */
	private void initialize() {
		this.tipoAreaContabile = new TipoAreaContabile();
	}

	/**
	 * @param codiceAzienda
	 *            The codiceAzienda to set.
	 */
	public void setCodiceAzienda(String codiceAzienda) {
		this.codiceAzienda = codiceAzienda;
	}

	/**
	 * @param tipoAreaContabile
	 *            The tipoAreaContabile to set.
	 */
	public void setTipoAreaContabile(TipoAreaContabile tipoAreaContabile) {
		this.tipoAreaContabile = tipoAreaContabile;
	}

	/**
	 * @param tipoOperazione
	 *            The tipoOperazione to set.
	 */
	public void setTipoOperazione(TipoOperazioneTipoDocumento tipoOperazione) {
		this.tipoOperazione = tipoOperazione;
	}

}
