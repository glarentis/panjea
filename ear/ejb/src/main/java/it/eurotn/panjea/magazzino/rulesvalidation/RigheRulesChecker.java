package it.eurotn.panjea.magazzino.rulesvalidation;

import it.eurotn.panjea.magazzino.domain.RigaArticolo;
import it.eurotn.panjea.magazzino.domain.RigaArticoloLite;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Verifica se le righe di un documento siano valide rispetto a delle regole configurabili.
 * 
 * @author giangi
 * 
 */
public class RigheRulesChecker implements Serializable {
	private static final long serialVersionUID = 4237067958757117968L;

	private final Set<AbstractRigaArticoloRulesValidation> regole;

	/**
	 * Costruttore.
	 */
	public RigheRulesChecker() {
		regole = new HashSet<AbstractRigaArticoloRulesValidation>();
	}

	/**
	 * Aggiunge una regola di verifica.
	 * 
	 * @param rule
	 *            regola di verifica da aggiungere alle regole da validare
	 */
	public void addRules(AbstractRigaArticoloRulesValidation rule) {
		regole.add(rule);
	}

	/**
	 * Verifica se la riga passa tutte le regole presenti.<br/>
	 * 
	 * @param rigaArticolo
	 *            rigaArticolo da verificare
	 * @return true se la riga è valida
	 */
	public boolean check(RigaArticolo rigaArticolo) {
		for (AbstractRigaArticoloRulesValidation regola : regole) {
			if (!regola.valida(rigaArticolo)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Verifica se la riga passa tutte le regole presenti.<br/>
	 * Se la riga contiene regole non valida viene settato il valore <code>rulesValidationError</code> della riga
	 * 
	 * @param rigaArticoloLite
	 *            rigaArticolo da verificare
	 * @return true se la riga è valida
	 */
	public boolean check(RigaArticoloLite rigaArticoloLite) {
		boolean result = true;
		for (AbstractRigaArticoloRulesValidation regola : regole) {
			if (!regola.valida(rigaArticoloLite)) {
				rigaArticoloLite.addToRulesValidationError(regola);
				result = false;
			}
		}
		return result;
	}

	/**
	 * getter og rules.
	 * 
	 * @return rules
	 */
	public Set<AbstractRigaArticoloRulesValidation> getRules() {
		return regole;
	}

	/**
	 * Rimuove una regola di verifica.
	 * 
	 * @param rule
	 *            regola da rimuovere
	 */
	public void removeRules(AbstractRigaArticoloRulesValidation rule) {
		regole.remove(rule);
	}

}
