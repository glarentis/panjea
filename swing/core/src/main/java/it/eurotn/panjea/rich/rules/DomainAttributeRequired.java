/**
 * 
 */
package it.eurotn.panjea.rich.rules;

import it.eurotn.locking.IDefProperty;

import org.apache.log4j.Logger;
import org.springframework.rules.constraint.Constraint;
import org.springframework.rules.constraint.TypeResolvableConstraint;

/**
 * Implementazione di <code>Constraint</code> per effettuare la validazione degli attributi come classi di dominio
 * 
 * @author adriano
 * @version 1.0, 09/nov/06
 * 
 */
public class DomainAttributeRequired extends TypeResolvableConstraint implements Constraint {

    private static Logger logger = Logger.getLogger(DomainAttributeRequired.class);

    /**
     * @param type
     *            type
     */
    public DomainAttributeRequired(final String type) {
        super(type);
    }

    @Override
    public Object[] getArguments() {
        return super.getArguments();
    }

    @Override
    public String[] getCodes() {
        return super.getCodes();
    }

    @Override
    public String getDefaultMessage() {
        return super.getDefaultMessage();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.core.closure.Constraint#test(java.lang.Object)
     */
    @Override
    public boolean test(Object object) {
        logger.debug("--> Enter test");
        return validaAttributeId(object);
    }

    /**
     * esegue la validazione di <code>Object</code> eseguendo il cast del parametro ad <code>IDefProperty</code> e
     * verificandone l'attributo id
     * 
     * @param object
     * @return boolean
     */
    private boolean validaAttributeId(Object object) {
        logger.debug("--> Enter validaAttributeId");
        if (object instanceof IDefProperty) {
            IDefProperty domainObject = (IDefProperty) object;
            boolean isOk = domainObject.getId() != null && domainObject.getId().intValue() != -1;
            logger.debug("--> Exit validaAttributeId");
            return isOk;
        } else {
            logger.debug("--> Exit validaAttributeId");
            return false;
        }
    }

}
