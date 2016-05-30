package it.eurotn.panjea.ordini.exception;

import it.eurotn.panjea.anagrafica.domain.Entita;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Eccezione che contiene la lista delle {@link Entita} sulle quali non è stato definito nessun tipo documento per
 * l'evasione ordini.
 * 
 * @author fattazzo
 * 
 */
public class EntitaSenzaTipoDocumentoEvasioneException extends Exception {

	private static final long serialVersionUID = -4472347686043017638L;

	private Set<EntitaLite> entita;

	/**
	 * Costruttore.
	 * 
	 * @param entita
	 *            entita senza tipo documento di evasione
	 * 
	 */
	public EntitaSenzaTipoDocumentoEvasioneException(final Set<EntitaLite> entita) {
		super();
		this.entita = new HashSet<EntitaLite>();
		if (entita != null) {
			this.entita.addAll(entita);
		}
	}

	/**
	 * Aggiunge la collection delle entità.
	 * 
	 * @param entitas
	 *            entità da aggiungere
	 */
	public void addEntita(Collection<EntitaLite> entitas) {
		this.entita.addAll(entitas);
	}

	/**
	 * @return the entita
	 */
	public Set<EntitaLite> getEntita() {
		return entita;
	}

}
