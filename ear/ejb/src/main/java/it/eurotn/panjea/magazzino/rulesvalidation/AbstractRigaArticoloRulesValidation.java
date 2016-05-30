package it.eurotn.panjea.magazzino.rulesvalidation;

import it.eurotn.panjea.magazzino.domain.RigaArticolo;
import it.eurotn.panjea.magazzino.domain.RigaArticoloLite;

import java.io.Serializable;

/**
 * 
 * @author  giangi
 * @version  1.0, 10/nov/2010
 */
public abstract class AbstractRigaArticoloRulesValidation implements Serializable {

	private static final long serialVersionUID = -2408161870941346941L;

	/**
	 * @uml.property  name="nome"
	 */
	private final String nome;

	/**
	 * Costruttore di default.
	 * 
	 * @param nome
	 *            nome della regola
	 */
	public AbstractRigaArticoloRulesValidation(final String nome) {
		super();
		this.nome = nome;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		AbstractRigaArticoloRulesValidation other = (AbstractRigaArticoloRulesValidation) obj;
		if (nome == null) {
			if (other.nome != null) {
				return false;
			}
		} else if (!nome.equals(other.nome)) {
			return false;
		}
		return true;
	}

	/**
	 * @return  the nome
	 * @uml.property  name="nome"
	 */
	public String getNome() {
		return nome;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		return result;
	}

	/**
	 * Esegue la validazione sulla riga articolo.
	 * 
	 * @param rigaArticolo
	 *            Riga articolo da validare
	 * @return <code>true</code> se la validazione ha avuto successo, <code>false</code> altrimenti.
	 */
	public abstract boolean valida(RigaArticolo rigaArticolo);

	/**
	 * Esegue la validazione sulla riga articolo lite.
	 * 
	 * @param rigaArticoloLite
	 *            Riga articolo da validare
	 * @return <code>true</code> se la validazione ha avuto successo, <code>false</code> altrimenti.
	 */
	public abstract boolean valida(RigaArticoloLite rigaArticoloLite);
}
