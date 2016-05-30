/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.eurotn.panjea.rich.rules;

import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.binding.PropertyAccessStrategy;
import org.springframework.rules.closure.BinaryConstraint;

/**
 * 
 * @author Leonardo
 */
public class ConfrontoDateConstraint extends ConfrontoPropertiesConstraint {

    private static Logger logger = Logger.getLogger(ConfrontoDateConstraint.class);

    /**
     * Costruttore.
     * 
     * @param propertyName
     *            property
     * @param beanPropertyExpression
     *            expression
     * @param otherPropertyName
     *            other property
     */
    public ConfrontoDateConstraint(final String propertyName, final BinaryConstraint beanPropertyExpression,
            final String otherPropertyName) {
        super(propertyName, beanPropertyExpression, otherPropertyName);
    }

    @Override
    protected boolean test(PropertyAccessStrategy domainObjectAccessStrategy) {
        logger.debug("--> domainObjectAccessStrategy " + domainObjectAccessStrategy);

        Date da = (Date) domainObjectAccessStrategy.getPropertyValue(getPropertyName());
        Date a = (Date) domainObjectAccessStrategy.getPropertyValue(getOtherPropertyName());
        logger.debug("--> da " + da);
        logger.debug("--> a " + a);
        boolean testResult = false;
        if (da != null && a != null) {
            // creo due calendari per azzerare ore,minuti e secondi per fare in
            // modo di non avere problemi sulla validazione confronto delle date
            Calendar calendarDa = Calendar.getInstance();
            calendarDa.setTime(da);
            calendarDa.set(Calendar.HOUR, 0);
            calendarDa.set(Calendar.HOUR_OF_DAY, 0);
            calendarDa.set(Calendar.MINUTE, 0);
            calendarDa.set(Calendar.SECOND, 0);
            calendarDa.set(Calendar.MILLISECOND, 0);
            Calendar calendarA = Calendar.getInstance();
            calendarA.setTime(a);
            calendarA.set(Calendar.HOUR, 0);
            calendarA.set(Calendar.HOUR_OF_DAY, 0);
            calendarA.set(Calendar.MINUTE, 0);
            calendarA.set(Calendar.SECOND, 0);
            calendarA.set(Calendar.MILLISECOND, 0);

            logger.debug("--> calendarDa " + calendarDa);
            logger.debug("--> calendarA " + calendarA);
            Date dateDa = calendarDa.getTime();
            Date dateA = calendarA.getTime();
            testResult = getConstraint().test(dateDa, dateA);
        } else {
            testResult = true;
        }
        logger.debug("--> testResult per la verifica di date " + testResult);
        return testResult;
    }

}
