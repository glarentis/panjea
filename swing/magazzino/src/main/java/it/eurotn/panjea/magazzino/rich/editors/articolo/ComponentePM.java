/**
 *
 */
package it.eurotn.panjea.magazzino.rich.editors.articolo;

import it.eurotn.locking.IDefProperty;
import it.eurotn.panjea.anagrafica.domain.UnitaMisura;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.Componente;

import java.io.Serializable;

/**
 * @author leonardo
 */
@Deprecated
public class ComponentePM implements Serializable, IDefProperty {

	private static final long serialVersionUID = -5739571028760142674L;

	private Componente componente;
	private boolean distinta;

	/**
	 * Costruttore.
	 */
	public ComponentePM() {
		super();
	}

	/**
	 * Costruttore.<br>
	 * NOTA che se considero di creare il componente dall'articolo corrente dell'editor articoli, se è distinta vorrà
	 * dire che vorrò creare i suoi componenti e quindi dovrò passare !articolo.isDistinta() e non
	 * articolo.isDistinta().
	 * 
	 * @param componente
	 *            componente
	 * @param distinta
	 *            se il componente deve essere distinta o componente
	 */
	public ComponentePM(final Componente componente, final boolean distinta) {
		super();
		this.componente = componente;
		this.distinta = distinta;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		return componente.equals(((ComponentePM) obj).getComponente());
	}

	/**
	 * @return the articolo
	 */
	public ArticoloLite getArticolo() {
		if (distinta) {
			return componente.getDistinta();
		} else {
			return componente.getArticolo();
		}
	}

	/**
	 * @return the codice
	 */
	public String getCodice() {
		if (distinta) {
			return componente.getDistinta().getCodice();
		} else {
			return componente.getArticolo().getCodice();
		}
	}

	/**
	 * @return the componente
	 */
	public Componente getComponente() {
		return componente;
	}

	/**
	 * @return the descrizione
	 */
	public String getDescrizione() {
		if (distinta) {
			return componente.getDistinta().getDescrizione();
		} else {
			return componente.getArticolo().getDescrizione();
		}
	}

	@Override
	public String getDomainClassName() {
		return componente.getDomainClassName();
	}

	/**
	 * @return the formula
	 */
	public String getFormula() {
		return componente.getFormula();
	}

	@Override
	public Integer getId() {
		return componente.getId();
	}

	/**
	 * @return the unitaMisura
	 */
	public UnitaMisura getUnitaMisura() {
		if (distinta) {
			return componente.getDistinta().getUnitaMisura();
		} else {
			return componente.getArticolo().getUnitaMisura();
		}
	}

	@Override
	public Integer getVersion() {
		return componente.getVersion();
	}

	@Override
	public int hashCode() {
		return componente.hashCode();
	}

	@Override
	public boolean isNew() {
		return false;
	}

	/**
	 * @param componente
	 *            the componente to set
	 */
	public void setComponente(Componente componente) {
		this.componente = componente;
	}

}
