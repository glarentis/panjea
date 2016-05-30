/**
 * 
 */
package it.eurotn.panjea.rich.rules;

import org.apache.commons.lang3.StringUtils;
import org.springframework.rules.constraint.Constraint;

/**
 * @author fattazzo
 * 
 */
public class PropertyContainConstraint implements Constraint {

    private String containValue = null;

    /**
     * Costruttore.
     * 
     * @param containValue
     *            valore di cui verificare l'esistenza
     */
    public PropertyContainConstraint(final String containValue) {
        super();
        this.containValue = containValue;
    }

    @Override
    public boolean test(Object paramObject) {

        if (paramObject != null && !(paramObject instanceof String)) {
            return false;
        }

        String objectString = (String) paramObject;

        return StringUtils.contains(objectString, containValue);
    }

}
