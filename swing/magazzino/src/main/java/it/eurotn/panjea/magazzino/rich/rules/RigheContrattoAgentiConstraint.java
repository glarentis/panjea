/**
 * 
 */
package it.eurotn.panjea.magazzino.rich.rules;

import it.eurotn.panjea.magazzino.domain.RigaContrattoAgente;

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
public class RigheContrattoAgentiConstraint extends TypeResolvableConstraint {

	private Logger logger = Logger.getLogger(RigheContrattoAgentiConstraint.class);

	/**
	 * Costruttore.
	 * 
	 */
	public RigheContrattoAgentiConstraint() {
		super("righeContrattoAgentiConstraint");
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
			if (((Collection<?>) object).size() >= 0) {
				@SuppressWarnings("unchecked")
				List<RigaContrattoAgente> righe = (List<RigaContrattoAgente>) object;

				for (RigaContrattoAgente riga : righe) {

					isValid = riga.getValoreProvvigione() != null;

					if (!isValid) {
						break;
					}
				}
				return isValid;
			}
		} else {
			isValid = object == null;
		}
		logger.debug("--> Exit validationCollectionRequired");
		return isValid;
	}

}
