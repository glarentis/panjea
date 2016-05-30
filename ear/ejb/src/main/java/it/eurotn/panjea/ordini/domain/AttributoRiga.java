package it.eurotn.panjea.ordini.domain;

import it.eurotn.panjea.magazzino.domain.AttributoArticolo;
import it.eurotn.panjea.magazzino.domain.AttributoRigaArticolo;
import it.eurotn.panjea.magazzino.domain.TipoAttributo;
import it.eurotn.util.PanjeaEJBUtil;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

/**
 * Contiene i valori dinamici per una riga di magazzino.<br>
 * I valori servono per calcolare la qta della riga<br>
 * Vengono creati quando si sceglie un articolo e si inserisce la sua formula<br>
 * di trasformazione. Nella formula ci sono {@link TipoAttributo} e se il valore di {@link AttributoArticolo} è null il
 * valore può essere modificato
 * 
 * @author giangi
 */
@Entity(name = "it.eurotn.panjea.ordini.domain.AttributoRiga")
@Audited
@Table(name = "ordi_attributi_riga")
public class AttributoRiga extends AttributoRigaArticolo implements Cloneable {
	private static final long serialVersionUID = 1L;

	@ManyToOne(optional = false)
	private RigaArticolo rigaArticolo;

	@Override
	public Object clone() {
		it.eurotn.panjea.ordini.domain.AttributoRiga attributoRiga = new it.eurotn.panjea.ordini.domain.AttributoRiga();
		PanjeaEJBUtil.copyProperties(attributoRiga, this);
		return attributoRiga;
	}

	/**
	 * 
	 * @return attributoRigaMagazzino copiata da attributoRigaOrdine
	 */
	public it.eurotn.panjea.magazzino.domain.AttributoRiga creaAttributoRigaMagazzino() {
		it.eurotn.panjea.magazzino.domain.AttributoRiga attributoMagazzino = new it.eurotn.panjea.magazzino.domain.AttributoRiga();
		attributoMagazzino.setFormula(getFormula());
		attributoMagazzino.setObbligatorio(getObbligatorio());
		attributoMagazzino.setUpdatable(isUpdatable());
		attributoMagazzino.setOrdine(getOrdine());
		attributoMagazzino.setStampa(isStampa());
		attributoMagazzino.setTipoAttributo(getTipoAttributo());
		attributoMagazzino.setValore(getValore());
		attributoMagazzino.setRiga(getRiga());
		attributoMagazzino.setRicalcolaInEvasione(isRicalcolaInEvasione());
		return attributoMagazzino;
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
