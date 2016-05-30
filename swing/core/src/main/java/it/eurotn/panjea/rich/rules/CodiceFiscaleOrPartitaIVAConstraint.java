/**
 * 
 */
package it.eurotn.panjea.rich.rules;

import org.apache.log4j.Logger;
import org.springframework.rules.constraint.Constraint;
import org.springframework.rules.constraint.RegexpConstraint;
import org.springframework.rules.reporting.TypeResolvableSupport;

/**
 * Constraint che unisce due validazioni con una OR codiceFiscaleConstraint OR partitaIVAConstraint La regular
 * expression è richiesta come parametro sul costruttore per la validazione del codice fiscale.
 * 
 * @author Leonardo
 */
public class CodiceFiscaleOrPartitaIVAConstraint extends TypeResolvableSupport implements Constraint {

    private static Logger logger = Logger.getLogger(CodiceFiscaleOrPartitaIVAConstraint.class);

    private String regexp;

    /**
     * Costruttore.
     * 
     * @param regexp
     *            regexp
     */
    public CodiceFiscaleOrPartitaIVAConstraint(final String regexp) {
        super();
        org.springframework.util.Assert.hasText(regexp);
        this.regexp = regexp;
    }

    @Override
    public boolean test(Object argument) {
        logger.debug("--> argument partita ivo o codice fiscale " + argument);
        RegexpConstraint c = new RegexpConstraint(regexp);
        boolean isRegExpValid = c.test(argument);
        logger.debug("--> isRegExpValid " + isRegExpValid);
        PartitaIVAConstraint partitaIVAConstraint = new PartitaIVAConstraint();
        boolean isPIVAValid = partitaIVAConstraint.test(argument);
        logger.debug("--> isPIVAValid " + isPIVAValid);

        return isRegExpValid || isPIVAValid;
    }

}
