/**
 * 
 */
package it.eurotn.panjea.protocolli.domain;

import it.eurotn.codice.generator.interfaces.IProtocolloValore;
import it.eurotn.entity.EntityBase;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Classe di dominio che rappresenta il valore dei progressivi per anno.
 * 
 * @author adriano
 * @version 1.0, 05/mag/07
 */
@Entity
@Table(name = "code_protocolli_anno", uniqueConstraints = { @UniqueConstraint(columnNames = { "protocollo_id", "anno" }) })
@NamedQueries({
		@NamedQuery(name = "ProtocolloAnno.caricaByCodiceProtocolloAnno", query = "select pa from ProtocolloAnno pa inner join pa.protocollo p where p.codiceAzienda = :paramCodiceAzienda and p.codice = :paramCodice and pa.anno = :paramAnno "),
		@NamedQuery(name = "ProtocolloAnno.caricaByAnno", query = " select pa from ProtocolloAnno pa inner join pa.protocollo p where p.codiceAzienda = :paramCodiceAzienda and pa.anno = :paramAnno "),
		@NamedQuery(name = "ProtocolloAnno.caricaByProtocollo", query = " select pa.protocollo from ProtocolloAnno pa inner join pa.protocollo p where p.id = :paramId "),
		@NamedQuery(name = "ProtocolloAnno.caricaAll", query = " select distinct p.protocollo from ProtocolloAnno as p where p.protocollo.codiceAzienda = :paramCodiceAzienda and p.anno = :paramAnno") })
public class ProtocolloAnno extends EntityBase implements IProtocolloValore {

	public static final String REF = "ProtocolloAnno";
	public static final String PROP_PROTOCOLLO = "protocollo";
	public static final String PROP_ID = "id";
	public static final String PROP_ANNO = "anno";
	public static final String PROP_VALORE = "valore";

	private static final long serialVersionUID = -8502431909340424927L;

	@Column(name = "anno")
	private Integer anno;
	@Column
	private Integer valore;
	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "protocollo_id")
	private Protocollo protocollo;

	/**
	 * Costruttore.
	 * 
	 */
	public ProtocolloAnno() {
		this.protocollo = new Protocollo();
	}

	/**
	 * @return Returns the anno.
	 */
	public Integer getAnno() {
		return anno;
	}

	/**
	 * @return Returns the protocolloAnnuale.
	 */
	public Protocollo getProtocollo() {
		return protocollo;
	}

	/**
	 * @return Returns the valore.
	 */
	@Override
	public Integer getValore() {
		return valore;
	}

	/**
	 * @param anno
	 *            The anno to set.
	 */
	public void setAnno(Integer anno) {
		this.anno = anno;
	}

	/**
	 * @param protocollo
	 *            The protocollo to set.
	 */
	public void setProtocollo(Protocollo protocollo) {
		this.protocollo = protocollo;
	}

	/**
	 * @param valore
	 *            The valore to set.
	 */
	@Override
	public void setValore(Integer valore) {
		this.valore = valore;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("ProtocolloAnno[");
		buffer.append(super.toString());
		buffer.append(" anno = ").append(anno);
		buffer.append(" valore = ").append(valore);
		buffer.append(" protocollo = ").append(protocollo != null ? protocollo.getId() : null);
		buffer.append("]");
		return buffer.toString();
	}
}
