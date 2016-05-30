/**
 *
 */
package it.eurotn.panjea.rich.rules;

import java.util.Collection;

import org.apache.log4j.Logger;
import org.springframework.rules.constraint.Constraint;
import org.springframework.rules.constraint.TypeResolvableConstraint;

/**
 * {@link Constraint} per la validazione che una {@link Collection} non sia vuota.
 * 
 * @author adriano
 * @version 1.0, 05/gen/2009
 * 
 */
public class CollectionRequired extends TypeResolvableConstraint {

    private static Logger logger = Logger.getLogger(CollectionRequired.class);

    /**
     * Costruttore.
     * 
     * @param type
     *            type
     */
    public CollectionRequired(final String type) {
        super(type);
    }

    @Override
    public boolean test(Object object) {
        return validationCollectionRequired(object);
    }

    /**
     * Verifica che la collection non sia vuota.
     * 
     * @param object
     *            la collection
     * @return true o false
     */
    private boolean validationCollectionRequired(Object object) {
        logger.debug("--> Enter validationCollectionRequired");
        if (object instanceof Collection) {
            return ((Collection<?>) object).size() != 0;
        }
        logger.debug("--> Exit validationCollectionRequired");
        return false;
    }

}
