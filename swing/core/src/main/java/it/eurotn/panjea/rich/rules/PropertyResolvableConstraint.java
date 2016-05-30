/**
 * 
 */
package it.eurotn.panjea.rich.rules;

import org.springframework.rules.constraint.TypeResolvableConstraint;
import org.springframework.rules.constraint.property.PropertyConstraint;

/**
 * Specializzazione di TypeResolvableConstraint per estendere i messaggi i18n alle PropertyConstraint
 * 
 * @author adriano
 * @version 1.0, 07/ago/07
 * 
 */
public class PropertyResolvableConstraint extends TypeResolvableConstraint implements PropertyConstraint {

    private PropertyConstraint propertyConstraint;

    /**
     * Costruttore.
     * 
     * @param propertyConstraint
     *            constraint
     */
    public PropertyResolvableConstraint(final PropertyConstraint propertyConstraint) {
        super();
        this.propertyConstraint = propertyConstraint;
    }

    @Override
    public String getPropertyName() {
        return propertyConstraint.getPropertyName();
    }

    @Override
    public boolean isCompoundRule() {
        return propertyConstraint.isCompoundRule();
    }

    @Override
    public boolean isDependentOn(String arg0) {
        return propertyConstraint.isDependentOn(arg0);
    }

    @Override
    public boolean test(Object arg0) {
        return propertyConstraint.test(arg0);
    }

}
