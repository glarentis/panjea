package it.eurotn.panjea.partite.domain;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.contabilita.domain.ContoBase;
import it.eurotn.panjea.contabilita.domain.SottoConto;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

/**
 * 
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
@Entity
@Audited
@Table(name = "part_conto_partite")
public class ContoPartita extends EntityBase {
	/**
	 * 
	 * @author giangi
	 * @version 1.0, 10/nov/2010
	 */
	public enum DareAvere {
		DARE, AVERE
	}

	/**
	 * 
	 * @author giangi
	 * @version 1.0, 10/nov/2010
	 */
	public enum TipoConto {
		CONTO_BASE, CONTO
	}

	/**
	 * 
	 * @author giangi
	 * @version 1.0, 10/nov/2010
	 */
	public enum TipoOperazione {
		SOMMA, SOTTRAI
	}

	private static final long serialVersionUID = 4218463841807054186L;

	@ManyToOne
	private ContoBase contoBase;

	@ManyToOne
	private SottoConto sottoConto;

	@Enumerated
	private TipoConto tipoConto;

	@Enumerated
	private TipoOperazione tipoOperazione;

	@Enumerated
	private DareAvere dareAvere;

	/**
	 * Costruttore.
	 */
	public ContoPartita() {
		tipoOperazione = TipoOperazione.SOMMA;
		dareAvere = DareAvere.DARE;
		sottoConto = new SottoConto();
		contoBase = new ContoBase();
		tipoConto = TipoConto.CONTO_BASE;
	}

	/**
	 * @return the codiceConto
	 */
	public String getCodiceConto() {
		switch (tipoConto.ordinal()) {
		case 0:
			return contoBase.getDescrizione();
		case 1:
			return sottoConto.getDescrizione();

		default:
			throw new UnsupportedOperationException();
		}
	}

	/**
	 * @return the contoBase
	 */
	public ContoBase getContoBase() {
		return contoBase;
	}

	/**
	 * @return the sottoConto
	 */
	public SottoConto getContoPartita() {
		if (sottoConto != null) {
			return sottoConto;
		} else {
			return contoBase.getSottoConto();
		}

	}

	/**
	 * @return the dareAvere
	 */
	public DareAvere getDareAvere() {
		return dareAvere;
	}

	/**
	 * @return the sottoConto
	 */
	public SottoConto getSottoConto() {
		return sottoConto;
	};

	/**
	 * @return the tipoConto
	 */
	public TipoConto getTipoConto() {
		return tipoConto;
	}

	/**
	 * @return the tipoOperazione
	 */
	public TipoOperazione getTipoOperazione() {
		return tipoOperazione;
	}

	/**
	 * Vuota , utilizzata perchè il binding vuole i metodi set/get.
	 */
	public void setCodiceConto() {
		// Vuota , utilizzata perchè il binding vuole i metodi set/get
	}

	/**
	 * @param contoBase
	 *            the contoBase to set
	 */
	public void setContoBase(ContoBase contoBase) {
		this.contoBase = contoBase;
	}

	/**
	 * @param dareAvere
	 *            the dareAvere to set
	 */
	public void setDareAvere(DareAvere dareAvere) {
		this.dareAvere = dareAvere;
	}

	/**
	 * @param sottoConto
	 *            the sottoConto to set
	 */
	public void setSottoConto(SottoConto sottoConto) {
		this.sottoConto = sottoConto;
	}

	/**
	 * @param tipoConto
	 *            the tipoConto to set
	 */
	public void setTipoConto(TipoConto tipoConto) {
		this.tipoConto = tipoConto;
	}

	/**
	 * @param tipoOperazione
	 *            the tipoOperazione to set
	 */
	public void setTipoOperazione(TipoOperazione tipoOperazione) {
		this.tipoOperazione = tipoOperazione;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("ContoPartita[");
		buffer.append("contoBase = ").append(contoBase);
		buffer.append(" dareAvere = ").append(dareAvere);
		buffer.append(" sottoConto = ").append(sottoConto);
		buffer.append(" tipoOperazione = ").append(tipoOperazione);
		buffer.append("]");
		return buffer.toString();
	};

}
