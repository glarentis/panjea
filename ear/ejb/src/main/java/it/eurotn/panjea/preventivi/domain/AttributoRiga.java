package it.eurotn.panjea.preventivi.domain;

import it.eurotn.panjea.magazzino.domain.AttributoRigaArticolo;
import it.eurotn.util.PanjeaEJBUtil;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

@Entity(name = "it.eurotn.panjea.preventivi.domain.AttributoRiga")
@Audited
@Table(name = "prev_attributi_riga")
public class AttributoRiga extends AttributoRigaArticolo {
	private static final long serialVersionUID = 1L;

	@ManyToOne(optional = false)
	private RigaArticolo rigaArticolo;

	@Override
	public Object clone() {
		AttributoRiga attributoRiga = new AttributoRiga();
		PanjeaEJBUtil.copyProperties(attributoRiga, this);
		return attributoRiga;
	}

	/**
	 * 
	 * @return attributoRigaMagazzino copiata da attributoRigaOrdine
	 */
	public it.eurotn.panjea.ordini.domain.AttributoRiga creaAttributoRigaOrdine() {
		it.eurotn.panjea.ordini.domain.AttributoRiga attributoOrdine = new it.eurotn.panjea.ordini.domain.AttributoRiga();
		attributoOrdine.setFormula(getFormula());
		attributoOrdine.setObbligatorio(getObbligatorio());
		attributoOrdine.setUpdatable(isUpdatable());
		attributoOrdine.setOrdine(getOrdine());
		attributoOrdine.setStampa(isStampa());
		attributoOrdine.setTipoAttributo(getTipoAttributo());
		attributoOrdine.setValore(getValore());
		attributoOrdine.setRiga(getRiga());
		attributoOrdine.setRicalcolaInEvasione(isRicalcolaInEvasione());
		return attributoOrdine;
	}

	/**
	 * @return the rigaArticolo
	 */
	public RigaArticolo getRigaArticolo() {
		return rigaArticolo;
	}

	/**
	 * @param rigaArticolo
	 *            the rigaArticolo to set
	 */
	public void setRigaArticolo(RigaArticolo rigaArticolo) {
		this.rigaArticolo = rigaArticolo;
	}

	/**
	 * Costruisce una <code>String</code> con tutti gli attributi accodati con questo formato nome attributo = valore.
	 * 
	 * @return a <code>String</code> come risultato di questo oggetto
	 */
	@Override
	public String toString() {

		StringBuffer retValue = new StringBuffer();

		retValue.append("AttributoRiga[ ").append(super.toString()).append(" valore = ").append(this.getValore())
				.append(" updatable = ").append(this.isUpdatable()).append(" ]");
		return retValue.toString();
	}
}
