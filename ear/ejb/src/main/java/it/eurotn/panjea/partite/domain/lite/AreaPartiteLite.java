package it.eurotn.panjea.partite.domain.lite;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.pagamenti.domain.CodicePagamento;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

/**
 * 
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
@Entity
@Audited
@Table(name = "part_area_partite")
public class AreaPartiteLite extends EntityBase {

	private static final long serialVersionUID = -8783003949411853786L;

	@OneToOne
	private Documento documento;

	@ManyToOne
	private CodicePagamento codicePagamento;

	/**
	 * @return the codicePagamento
	 */
	public CodicePagamento getCodicePagamento() {
		return codicePagamento;
	}

	/**
	 * @return the documento
	 */
	public Documento getDocumento() {
		return documento;
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
	public void setDocumento(Documento documento) {
		this.documento = documento;
	}
}
