/**
 * 
 */
package it.eurotn.panjea.rich.rules;

import org.springframework.binding.PropertyAccessStrategy;
import org.springframework.rules.constraint.And;
import org.springframework.rules.constraint.Constraint;
import org.springframework.rules.constraint.Required;
import org.springframework.rules.constraint.property.RequiredIfTrue;

/**
 * @author fattazzo
 * 
 */
public class PatternCodiceDocumentoValueConstraint extends RequiredIfTrue {

    /**
     * Costruttore.
     * 
     * @param propertyName
     *            property name
     * @param predicate
     *            predicate
     */
    public PatternCodiceDocumentoValueConstraint(final String propertyName, final Constraint predicate) {
        super(propertyName, predicate);
    }

    @Override
    protected boolean test(PropertyAccessStrategy domainObjectAccessStrategy) {
        if (this.getConstraint().test(domainObjectAccessStrategy)) {
            return new And(Required.instance(), new PropertyContainConstraint("$valoreProt$"))
                    .test(domainObjectAccessStrategy.getPropertyValue(getPropertyName()));
        }

        return true;
    }
}
