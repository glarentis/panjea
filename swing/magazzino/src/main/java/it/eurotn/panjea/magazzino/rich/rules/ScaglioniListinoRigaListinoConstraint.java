package it.eurotn.panjea.magazzino.rich.rules;

import it.eurotn.panjea.magazzino.domain.ScaglioneListino;

import java.util.Collection;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.rules.constraint.Constraint;
import org.springframework.rules.constraint.TypeResolvableConstraint;

/**
 * {@link Constraint} per la validazione che una {@link Collection} non sia vuota.
 * 
 * @author gfattarsi
 * 
 */
public class ScaglioniListinoRigaListinoConstraint extends TypeResolvableConstraint {

	private Logger logger = Logger.getLogger(ScaglioniListinoRigaListinoConstraint.class);

	/**
	 * Costruttore.
	 * 
	 */
	public ScaglioniListinoRigaListinoConstraint() {
		super("collectionValuesRequired");
	}

	@Override
	public boolean test(Object object) {
		return validationCollectionRequired(object);
	}

	/**
	 * Esegue il test di validazione.
	 * 
	 * @param object
	 *            collection da validare
	 * @return <code>true</code> se valida
	 */
	private boolean validationCollectionRequired(Object object) {
		logger.debug("--> Enter validationCollectionRequired");
		boolean isValid = true;
		if (object instanceof Collection) {
			@SuppressWarnings("unchecked")
			Set<ScaglioneListino> scaglioni = (Set<ScaglioneListino>) object;
			for (ScaglioneListino scaglioneListino : scaglioni) {
				isValid = scaglioneListino.getQuantita() != null && scaglioneListino.getPrezzo() != null;
				if (!isValid) {
					break;
				}
			}
		}
		logger.debug("--> Exit validationCollectionRequired");
		return isValid;
	}
}
