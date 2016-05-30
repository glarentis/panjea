/**
 * 
 */
package it.eurotn.panjea.magazzino.rich.rules;

import it.eurotn.panjea.lotti.domain.RigaLotto;

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
public class RigheLottiConstraint extends TypeResolvableConstraint {

	private Logger logger = Logger.getLogger(RigheLottiConstraint.class);

	/**
	 * Costruttore.
	 * 
	 */
	public RigheLottiConstraint() {
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
			Set<RigaLotto> righeLotto = (Set<RigaLotto>) object;
			for (RigaLotto rigaLotto : righeLotto) {
				isValid = rigaLotto.getLotto() != null && !rigaLotto.getLotto().isNew();
				isValid = isValid && (rigaLotto.getQuantita() != null && rigaLotto.getQuantita().compareTo(0.0) != 0);
				if (!isValid) {
					break;
				}
			}
		}
		logger.debug("--> Exit validationCollectionRequired");
		return isValid;
	}
}
