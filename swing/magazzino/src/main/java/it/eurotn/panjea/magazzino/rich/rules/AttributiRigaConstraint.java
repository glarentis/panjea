/**
 * 
 */
package it.eurotn.panjea.magazzino.rich.rules;

import it.eurotn.panjea.magazzino.domain.AttributoRigaArticolo;

import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.rules.constraint.Constraint;
import org.springframework.rules.constraint.TypeResolvableConstraint;

/**
 * {@link Constraint} per la validazione che una {@link Collection} non sia vuota.
 * 
 * @author gfattarsi
 * 
 */
public class AttributiRigaConstraint extends TypeResolvableConstraint {

	private Logger logger = Logger.getLogger(AttributiRigaConstraint.class);

	/**
	 * Costruttore.
	 * 
	 */
	public AttributiRigaConstraint() {
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
			List<AttributoRigaArticolo> attributi = (List<AttributoRigaArticolo>) object;
			for (AttributoRigaArticolo attributoRiga : attributi) {

				boolean valorePresente = attributoRiga.getValore() != null && !attributoRiga.getValore().isEmpty();
				isValid = attributoRiga.getFormula() != null;
				isValid = isValid || (attributoRiga.getObbligatorio() && valorePresente);
				isValid = isValid || !attributoRiga.getObbligatorio();
				if (!isValid) {
					break;
				}
			}
		}
		logger.debug("--> Exit validationCollectionRequired");
		return isValid;
	}
}
