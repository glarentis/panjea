/**
 *
 */
package it.eurotn.panjea.contabilita.domain;

import it.eurotn.entity.EntityBase;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.QueryHint;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;
import org.hibernate.envers.Audited;

/**
 * @author fattazzo
 * @version 1.0, 27/ago/07
 */

@NamedQueries({
	@NamedQuery(name = "ContoBase.caricaAll", query = "from ContoBase c join fetch c.sottoConto s join fetch s.conto conto join fetch conto.mastro where c.codiceAzienda = :paramCodiceAzienda", hints = {
			@QueryHint(name = "org.hibernate.cacheable", value = "true"),
			@QueryHint(name = "org.hibernate.cacheRegion", value = "contiBase") }),
			@NamedQuery(name = "ContoBase.caricaByDescrizione", query = "from ContoBase c where c.codiceAzienda = :paramCodiceAzienda and c.descrizione = :paramDescrizione") })
@Entity
@Audited
@Table(name = "cont_conti_base", uniqueConstraints = @UniqueConstraint(columnNames = { "descrizione" }))
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "contiBase")
public class ContoBase extends EntityBase implements java.io.Serializable {

	private static final long serialVersionUID = -3630026577351736129L;

	/**
	 * @uml.property name="codiceAzienda"
	 */
	@Column(length = 10, nullable = false)
	@Index(name = "index_codiceAzienda")
	private String codiceAzienda;

	/**
	 * @uml.property name="descrizione"
	 */
	@Column(length = 50)
	private String descrizione;

	/**
	 * @uml.property name="sottoConto"
	 * @uml.associationEnd
	 */
	@ManyToOne
	private SottoConto sottoConto;

	/**
	 * @uml.property name="tipoContoBase"
	 * @uml.associationEnd
	 */
	private ETipoContoBase tipoContoBase;

	/**
	 * Costruttore.
	 *
	 */
	public ContoBase() {
		initialize();
	}

	/**
	 * @return Returns the codiceAzienda.
	 * @uml.property name="codiceAzienda"
	 */
	public String getCodiceAzienda() {
		return codiceAzienda;
	}

	/**
	 * @return Returns the descrizione.
	 * @uml.property name="descrizione"
	 */
	public String getDescrizione() {
		return descrizione;
	}

	/**
	 * @return Returns the sottoConto.
	 * @uml.property name="sottoConto"
	 */
	public SottoConto getSottoConto() {
		return sottoConto;
	}

	/**
	 * @return Returns the tipoContoBase.
	 * @uml.property name="tipoContoBase"
	 */
	public ETipoContoBase getTipoContoBase() {
		return tipoContoBase;
	}

	/**
	 * Inizializza i valori di default.
	 */
	private void initialize() {
		this.sottoConto = new SottoConto();
	}

	/**
	 * @param codiceAzienda
	 *            The codiceAzienda to set.
	 * @uml.property name="codiceAzienda"
	 */
	public void setCodiceAzienda(String codiceAzienda) {
		this.codiceAzienda = codiceAzienda;
	}

	/**
	 * @param descrizione
	 *            The descrizione to set.
	 * @uml.property name="descrizione"
	 */
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	/**
	 * @param sottoConto
	 *            The sottoConto to set.
	 * @uml.property name="sottoConto"
	 */
	public void setSottoConto(SottoConto sottoConto) {
		this.sottoConto = sottoConto;
	}

	/**
	 * @param tipoContoBase
	 *            The tipoContoBase to set.
	 * @uml.property name="tipoContoBase"
	 */
	public void setTipoContoBase(ETipoContoBase tipoContoBase) {
		this.tipoContoBase = tipoContoBase;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("ContoBase[");
		buffer.append(super.toString());
		buffer.append("codiceAzienda = ").append(codiceAzienda);
		buffer.append(" descrizione = ").append(descrizione);
		buffer.append(" sottoConto = ").append(sottoConto);
		buffer.append(" tipoContoBase = ").append(tipoContoBase);
		buffer.append("]");
		return buffer.toString();
	}

}
