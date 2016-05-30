/**
 *
 */
package it.eurotn.panjea.rich.rules;

import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.rules.Rules;
import org.springframework.rules.RulesSource;
import org.springframework.rules.constraint.Constraint;
import org.springframework.rules.constraint.TypeResolvableConstraint;
import org.springframework.rules.constraint.property.PropertyConstraint;
import org.springframework.rules.reporting.BeanValidationResultsCollector;
import org.springframework.rules.reporting.PropertyResults;

/**
 * {@link Constraint} per la validazione che una {@link Collection} non sia vuota.
 * 
 * @author gfattarsi
 * 
 */
public class CollectionValuesRequired extends TypeResolvableConstraint {

    private Logger logger = Logger.getLogger(CollectionValuesRequired.class);
    private RulesSource rulesSource;

    /**
     * Costruttore.
     */
    public CollectionValuesRequired() {
        super("collectionValuesRequired");
    }

    /**
     * @return RulesSource
     */
    private RulesSource getRulesSource() {
        if (rulesSource == null) {
            rulesSource = (RulesSource) ApplicationServicesLocator.services().getService(RulesSource.class);
        }
        return rulesSource;
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
        if (object instanceof Collection) {
            for (Object value : ((Collection<?>) object)) {
                Rules rules = getRulesSource().getRules(value.getClass(), null);
                if (rules != null) {
                    for (Iterator<?> i = rules.iterator(); i.hasNext();) {
                        PropertyConstraint validationRule = (PropertyConstraint) i.next();
                        BeanValidationResultsCollector resultsCollector = new BeanValidationResultsCollector(value);
                        PropertyResults results = resultsCollector.collectPropertyResults(validationRule);
                        if (results != null && results.getViolatedCount() > 0) {
                            return false;
                        }
                    }
                }
            }
        }
        logger.debug("--> Exit validationCollectionRequired");
        return true;
    }

}
