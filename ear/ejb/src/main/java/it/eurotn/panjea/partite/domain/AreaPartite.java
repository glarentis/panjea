package it.eurotn.panjea.partite.domain;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.documenti.domain.IAreaDocumento;
import it.eurotn.panjea.anagrafica.documenti.domain.IStatoDocumento;
import it.eurotn.panjea.anagrafica.documenti.domain.ITipoAreaDocumento;
import it.eurotn.panjea.anagrafica.util.EntitaDocumento;
import it.eurotn.panjea.documenti.domain.StatoSpedizione;
import it.eurotn.panjea.pagamenti.domain.CodicePagamento;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.envers.Audited;

/**
 *
 *
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
@Entity
@Audited
@Table(name = "part_area_partite")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TIPO_PARTITA", discriminatorType = DiscriminatorType.STRING, length = 2)
@DiscriminatorValue("AP")
@NamedQueries({ @NamedQuery(name = "AreaPartite.ricercaByDocumento", query = "select a from AreaPartite a inner join a.documento d where d.id = :paramIdDocumento and a.documento.codiceAzienda=:paramCodiceAzienda ") })
// @NamedQuery(name = "AreaPartite.ricercaIdAreeCollegate", query =
// " select ap.id from AreaPartite ap where ap.areaPartiteCollegata.id=:paramIdAreaCollegata and ap.documento.codiceAzienda=:paramCodiceAzienda and ap.tipoOperazione in (:paramTipiOperazione) order by ap.documento.dataDocumento ")
public abstract class AreaPartite extends EntityBase implements IAreaDocumento {
	private static final long serialVersionUID = 4246742134073759650L;

	@OneToOne
	protected Documento documento;

	@Transient
	private EntitaDocumento entitaDocumento;

	@ManyToOne
	protected TipoAreaPartita tipoAreaPartita;

	@Column(precision = 19, scale = 2)
	protected BigDecimal speseIncasso;

	@ManyToOne
	protected CodicePagamento codicePagamento;

	{
		this.documento = new Documento();
		this.tipoAreaPartita = new TipoAreaPartita();
		this.speseIncasso = BigDecimal.ZERO;
	}

	/**
	 * @return the codicePagamento
	 */
	public CodicePagamento getCodicePagamento() {
		return codicePagamento;
	}

	/**
	 * @return the documento
	 */
	@Override
	public Documento getDocumento() {
		return documento;
	}

	/**
	 * @return the entitaDocumento
	 */
	public EntitaDocumento getEntitaDocumento() {
		entitaDocumento = new EntitaDocumento(this.documento);

		return entitaDocumento;
	}

	/**
	 * @return the speseIncasso
	 */
	public BigDecimal getSpeseIncasso() {
		return speseIncasso;
	}

	@Override
	public IStatoDocumento getStato() {
		return null;
	}

	@Override
	public StatoSpedizione getStatoSpedizione() {
		return null;
	}

	@Override
	public ITipoAreaDocumento getTipoAreaDocumento() {
		return tipoAreaPartita;
	}

	/**
	 * @return Returns the tipoAreaPartita.
	 */
	public TipoAreaPartita getTipoAreaPartita() {
		return tipoAreaPartita;
	}

	/**
	 * @param codicePagamento
	 *            the codicePagamento to set
	 */
	public void setCodicePagamento(CodicePagamento codicePagamento) {
		this.codicePagamento = codicePagamento;
	}

	/**
	 * @param documento
	 *            the documento to set
	 */
	@Override
	public void setDocumento(Documento documento) {
		this.documento = documento;
	}

	/**
	 * @param speseIncasso
	 *            the speseIncasso to set
	 */
	public void setSpeseIncasso(BigDecimal speseIncasso) {
		this.speseIncasso = speseIncasso;
	}

	@Override
	public void setStatoSpedizione(StatoSpedizione statoSpedizione) {
	}

	@Override
	public void setTipoAreaDocumento(ITipoAreaDocumento tipoAreaDocumento) {
		tipoAreaPartita = (TipoAreaPartita) tipoAreaDocumento;
	}

	/**
	 * @param tipoAreaPartita
	 *            The tipoAreaPartita to set.
	 */
	public void setTipoAreaPartita(TipoAreaPartita tipoAreaPartita) {
		this.tipoAreaPartita = tipoAreaPartita;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AreaPartite [");
		if (codicePagamento != null) {
			builder.append("codicePagamento=");
			builder.append(codicePagamento);
			builder.append(", ");
		}
		if (documento != null) {
			builder.append("documento=");
			builder.append(documento.getId());
			builder.append(", ");
		}
		if (speseIncasso != null) {
			builder.append("speseIncasso=");
			builder.append(speseIncasso);
			builder.append(", ");
		}
		if (tipoAreaPartita != null) {
			builder.append("tipoAreaPartita=");
			builder.append(tipoAreaPartita);
			builder.append(", ");
		}

		builder.append("]");
		return builder.toString();
	}
}
