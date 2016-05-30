package it.eurotn.panjea.partite.domain;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.anagrafica.domain.TipoPagamento;
import it.eurotn.panjea.partite.domain.StrutturaPartita.TipoStrategiaDataScadenza;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

/**
 * 
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
@Entity
@Audited
@Table(name = "part_struttura_partita")
@NamedQueries({ @NamedQuery(name = "StrutturaPartitaLite.caricaAll", query = "select a from StrutturaPartitaLite a where a.codiceAzienda = :paramCodiceAzienda") })
public class StrutturaPartitaLite extends EntityBase {
	private static final long serialVersionUID = 3847123402823835703L;

	@Column(length = 10, nullable = false)
	private String codiceAzienda;

	@Column(length = 60)
	private String descrizione;

	@Column
	private Integer giornoFisso;

	@Enumerated
	private TipoStrategiaDataScadenza tipoStrategiaDataScadenza;

	@ManyToOne
	private CategoriaRata categoriaRata;

	@Enumerated
	private TipoPagamento tipoPagamento;

	@Column
	private Integer ggPostScadenza;

	/**
	 * @return the categoriaRata
	 */
	public CategoriaRata getCategoriaRata() {
		return categoriaRata;
	}

	/**
	 * @return the codiceAzienda
	 */
	public String getCodiceAzienda() {
		return codiceAzienda;
	}

	/**
	 * @return the descrizione
	 */
	public String getDescrizione() {
		return descrizione;
	}

	/**
	 * @return the ggPostScadenza
	 */
	public Integer getGgPostScadenza() {
		return ggPostScadenza;
	}

	/**
	 * @return the giornoFisso
	 */
	public Integer getGiornoFisso() {
		return giornoFisso;
	}

	/**
	 * @return the tipoPagamento
	 */
	public TipoPagamento getTipoPagamento() {
		return tipoPagamento;
	}

	/**
	 * @return the tipoStrategiaDataScadenza
	 */
	public TipoStrategiaDataScadenza getTipoStrategiaDataScadenza() {
		return tipoStrategiaDataScadenza;
	}

	/**
	 * @param categoriaRata
	 *            the categoriaRata to set
	 */
	public void setCategoriaRata(CategoriaRata categoriaRata) {
		this.categoriaRata = categoriaRata;
	}

	/**
	 * @param codiceAzienda
	 *            the codiceAzienda to set
	 */
	public void setCodiceAzienda(String codiceAzienda) {
		this.codiceAzienda = codiceAzienda;
	}

	/**
	 * @param descrizione
	 *            the descrizione to set
	 */
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	/**
	 * @param ggPostScadenza
	 *            the ggPostScadenza to set
	 */
	public void setGgPostScadenza(Integer ggPostScadenza) {
		this.ggPostScadenza = ggPostScadenza;
	}

	/**
	 * @param giornoFisso
	 *            the giornoFisso to set
	 */
	public void setGiornoFisso(Integer giornoFisso) {
		this.giornoFisso = giornoFisso;
	}

	/**
	 * @param tipoPagamento
	 *            the tipoPagamento to set
	 */
	public void setTipoPagamento(TipoPagamento tipoPagamento) {
		this.tipoPagamento = tipoPagamento;
	}

	/**
	 * @param tipoStrategiaDataScadenza
	 *            the tipoStrategiaDataScadenza to set
	 */
	public void setTipoStrategiaDataScadenza(TipoStrategiaDataScadenza tipoStrategiaDataScadenza) {
		this.tipoStrategiaDataScadenza = tipoStrategiaDataScadenza;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("StrutturaPartitaLite[");
		buffer.append("codiceAzienda = ").append(codiceAzienda);
		buffer.append("categoriaRata = ").append(categoriaRata);
		buffer.append(" descrizione = ").append(descrizione);
		buffer.append(" ggPostScadenza = ").append(ggPostScadenza);
		buffer.append(" giornoFisso = ").append(giornoFisso);
		buffer.append(" tipoPagamento = ").append(tipoPagamento);
		buffer.append("]");
		return buffer.toString();
	}
}
