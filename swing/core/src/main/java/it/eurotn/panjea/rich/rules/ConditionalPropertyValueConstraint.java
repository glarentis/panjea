package it.eurotn.panjea.rich.rules;

import org.springframework.binding.PropertyAccessStrategy;
import org.springframework.rules.constraint.Constraint;
import org.springframework.rules.constraint.property.AbstractPropertyConstraint;
import org.springframework.rules.constraint.property.PropertyValueConstraint;
import org.springframework.rules.reporting.TypeResolvable;

/**
 * 
 * @author adriano
 */
public class ConditionalPropertyValueConstraint extends AbstractPropertyConstraint implements TypeResolvable {

    private Constraint thenConstraint;

    private Constraint elseConstraint;

    private PropertyValueConstraint ifPropertyValueConstraint;

    private String type;

    /**
     * Costruttore.
     * 
     * @param propertyName
     *            .
     * @param ifPropertyValueConstraint
     *            .
     * @param thenConstraint
     *            .
     * @param elseConstraint
     *            .
     * @param type
     *            .
     */
    public ConditionalPropertyValueConstraint(final String propertyName,
            final PropertyValueConstraint ifPropertyValueConstraint, final Constraint thenConstraint,
            final Constraint elseConstraint, final String type) {
        super(propertyName);
        this.ifPropertyValueConstraint = ifPropertyValueConstraint;
        this.thenConstraint = thenConstraint;
        this.elseConstraint = elseConstraint;
        this.type = type;
    }

    /**
     * 
     * Costruttore.
     * 
     * @param propertyName
     *            .
     * @param ifPropertyValueConstraint
     *            .
     * @param thenConstraint
     *            .
     * @param type
     *            .
     */
    public ConditionalPropertyValueConstraint(final String propertyName,
            final PropertyValueConstraint ifPropertyValueConstraint, final Constraint thenConstraint,
            final String type) {
        this(propertyName, ifPropertyValueConstraint, thenConstraint, null, type);
    }

    @Override
    public String getType() {
        return type;
    }

    /**
     * @param type
     *            the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    @Override
    protected boolean test(PropertyAccessStrategy domainObjectAccessStrategy) {
        Object object = domainObjectAccessStrategy.getPropertyValue(getPropertyName());
        if (ifPropertyValueConstraint.test(domainObjectAccessStrategy)) {
            return thenConstraint.test(object);
        } else if (elseConstraint != null) {
            return elseConstraint.test(object);
        }
        return true;
    }

}
