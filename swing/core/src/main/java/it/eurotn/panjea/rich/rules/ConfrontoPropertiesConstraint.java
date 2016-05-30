/**
 * 
 */
package it.eurotn.panjea.rich.rules;

import org.apache.log4j.Logger;
import org.springframework.binding.PropertyAccessStrategy;
import org.springframework.rules.closure.BinaryConstraint;
import org.springframework.rules.constraint.Constraint;
import org.springframework.rules.constraint.property.PropertiesConstraint;
import org.springframework.rules.constraint.property.PropertyConstraint;

/**
 * Estende propertiesConstraint per verificare due proprieta' non obbligatorie secondo un operatore binario (ad es.
 * LessThanEqualTo,GreaterThan,ecc.). (property1 BinaryConstraint property2) utile nel caso di controllo date se dataA
 * minore di dataB, verificata solo nel caso in cui esiste sia dataA che dataB.
 * 
 * @author Leonardo
 * @see org.springframework.rules.closure.BinaryConstraint
 * @see org.springframework.rules.factory.Constraints
 */
public class ConfrontoPropertiesConstraint extends PropertiesConstraint {

    private static Logger logger = Logger.getLogger(ConfrontoPropertiesConstraint.class);
    private String propertyName;
    private String otherPropertyName;
    private BinaryConstraint beanPropertyExpression;
    private PropertyConstraint ifConstraint;
    private boolean negaConfronto;

    /**
     * Classe per confrontare due valori secondo una BinaryConstraint (=,>,<).<br>
     * 
     * @param propertyName
     *            il nome della proprietà 1 da verificare
     * @param binaryConstraint
     *            la binary constraint (=,>,<), per diverso devo usare il costruttore con tutti i parametri
     * @param otherPropertyName
     *            il nome della proprietà 2 da verificare
     */
    public ConfrontoPropertiesConstraint(final String propertyName, final BinaryConstraint binaryConstraint,
            final String otherPropertyName) {
        this(propertyName, binaryConstraint, otherPropertyName, null, false);
    }

    /**
     * Classe per confrontare due valori secondo una BinaryConstraint (=,>,<,!=) se la ifConstraint risulta vera.
     * 
     * @param propertyName
     *            il nome della proprietà 1 da verificare
     * @param binaryConstraint
     *            la binary constraint (=,>,<)
     * @param otherPropertyName
     *            il nome della proprietà 2 da verificare
     * @param ifConstraint
     *            una constraint che decide se continuare con il test di confronto, può essere null ritornando solo il
     *            risultato del confronto
     * @param negaConfronto
     *            permette di negare la binaryConstraint
     */
    public ConfrontoPropertiesConstraint(final String propertyName, final BinaryConstraint binaryConstraint,
            final String otherPropertyName, final PropertyConstraint ifConstraint, final boolean negaConfronto) {
        super(propertyName, binaryConstraint, otherPropertyName);
        this.propertyName = propertyName;
        this.otherPropertyName = otherPropertyName;
        this.beanPropertyExpression = binaryConstraint;
        this.ifConstraint = ifConstraint;
        this.negaConfronto = negaConfronto;
    }

    @Override
    public BinaryConstraint getConstraint() {
        return beanPropertyExpression;
    }

    /**
     * @return the ifConstraint
     */
    public Constraint getIfConstraint() {
        return ifConstraint;
    }

    @Override
    public String getOtherPropertyName() {
        return otherPropertyName;
    }

    @Override
    public String getPropertyName() {
        return propertyName;
    }

    /**
     * @param ifConstraint
     *            the ifConstraint to set
     */
    public void setIfConstraint(PropertyConstraint ifConstraint) {
        this.ifConstraint = ifConstraint;
    }

    @Override
    protected boolean test(PropertyAccessStrategy domainObjectAccessStrategy) {
        logger.debug("--> domainObjectAccessStrategy " + domainObjectAccessStrategy);
        if (ifConstraint != null) {
            if (!ifConstraint.test(domainObjectAccessStrategy)) {
                return true;
            }
        }
        Object da = domainObjectAccessStrategy.getPropertyValue(getPropertyName());
        Object a = domainObjectAccessStrategy.getPropertyValue(getOtherPropertyName());

        logger.debug("--> da " + da);
        logger.debug("--> a " + a);

        boolean testResult = false;
        if (da != null && a != null) {
            testResult = getConstraint().test(da, a);
            if (negaConfronto) {
                testResult = !testResult;
            }
        } else {
            // questa parte mi serve per garantire la validita' del test nel caso in cui una delle due proprieta'
            // risulti null
            testResult = true;
        }
        return testResult;
    }
}
