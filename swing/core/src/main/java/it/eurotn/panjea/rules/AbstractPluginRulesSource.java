/**
 * 
 */
package it.eurotn.panjea.rules;

import it.eurotn.panjea.rich.rules.CodiceFiscaleOrPartitaIVAConstraint;
import it.eurotn.panjea.rich.rules.CollectionRequired;
import it.eurotn.panjea.rich.rules.DomainAttributeRequired;
import it.eurotn.panjea.rich.rules.ImportoRequired;
import it.eurotn.panjea.rich.rules.PartitaIVAConstraint;
import it.eurotn.panjea.rich.rules.PeriodoConstraint;

import java.util.List;

import org.springframework.rules.Rules;
import org.springframework.rules.constraint.Constraint;
import org.springframework.rules.constraint.RelationalOperator;
import org.springframework.rules.constraint.StringLengthConstraint;
import org.springframework.rules.support.DefaultRulesSource;

/**
 * Classe astratta che verrà implementata ne vari plugins che restituisce una lista di Rules
 * 
 * @author fattazzo
 * @version 1.0, 08/mag/07
 * 
 */
public abstract class AbstractPluginRulesSource extends DefaultRulesSource {

    public static final String REGEXP_FISCALCODE = "(^[A-Za-z]{6}[0-9LMNPQRSTUV]{2}[A-Za-z]{1}[0-9LMNPQRSTUV]{2}[A-Za-z]{1}[0-9LMNPQRSTUV]{3}[A -Za-z]{1}$)?";

    private Constraint alphabeticConstraint;
    private Constraint alphanumericConstraint;
    private Constraint numericConstraint;

    private Constraint internetAddressConstraint;

    private Constraint requiredConstraint;

    private Constraint emailConstraint;

    private Constraint domainAttributeRequired;
    private Constraint collectionRequiredRequired;

    {
        alphabeticConstraint = all(new Constraint[] { regexp("([a-zA-Z]*)?", "alphabeticConstraint") });
        alphanumericConstraint = all(new Constraint[] { regexp("([a-zA-Z0-9]*)?", "alphaNumericConstraint") });
        numericConstraint = all(new Constraint[] { regexp("([0-9]*)?", "numericConstraint") });

        requiredConstraint = all(new Constraint[] { required() });

        emailConstraint = all(new Constraint[] { regexp("([-a-zA-Z0-9._]+@[-a-zA-Z0-9.]+)?", "emailConstraint") });
        internetAddressConstraint = all(new Constraint[] {
                regexp("(((http\\://|https\\://|ftp\\://)|(www.))+(([a-zA-Z0-9\\.-]+\\.[a-zA-Z]{2,4})|([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}))(/[a-zA-Z0-9%:/-_\\?\\.'~]*)?)?",
                        "internetAddressConstraint") });

        domainAttributeRequired = new DomainAttributeRequired("domainAttributeConstraint");

        collectionRequiredRequired = new CollectionRequired("collectionRequiredConstraint");
    }

    /**
     * @return the alphabeticConstraint
     */
    public Constraint getAlphabeticConstraint() {
        return alphabeticConstraint;
    }

    /**
     * @return the alphanumericConstraint
     */
    public Constraint getAlphanumericConstraint() {
        return alphanumericConstraint;
    }

    /**
     * @return the collectionRequiredRequired
     */
    public Constraint getCollectionRequiredRequired() {
        return collectionRequiredRequired;
    }

    /**
     * @return the domainAttributeRequired
     */
    public Constraint getDomainAttributeRequiredConstraint() {
        return domainAttributeRequired;
    }

    /**
     * @return the emailConstraint per la validazione di una email
     */
    public Constraint getEmailConstraint() {
        return emailConstraint;
    }

    /**
     * @return the fiscalCodeOrPartitaIVAConstraint
     */
    public Constraint getFiscalCodeOrPartitaIVAConstraint() {
        CodiceFiscaleOrPartitaIVAConstraint constraint = new CodiceFiscaleOrPartitaIVAConstraint(REGEXP_FISCALCODE);
        constraint.setType("codiceFiscaleOrPartitaIVAConstraint");
        return constraint;
    }

    /**
     * @return the importoConstraint
     */
    public Constraint getImportoConstraint() {
        ImportoRequired importoRequired = new ImportoRequired();
        importoRequired.setType("");
        return importoRequired;
    }

    /**
     * @return the internetAddressConstraint
     */
    public Constraint getInternetAddressConstraint() {
        return internetAddressConstraint;
    }

    /**
     * @param fixedLength
     *            length
     * @return the lengthConstraint
     */
    public Constraint getLengthConstraint(int fixedLength) {
        StringLengthConstraint constraint = new StringLengthConstraint(RelationalOperator.EQUAL_TO, fixedLength);
        return constraint;
    }

    /**
     * @param maxLength
     *            length
     * @return the maxCharConstraint
     */
    public Constraint getMaxCharConstraint(int maxLength) {
        return all(new Constraint[] { maxLength(maxLength) });
    }

    /**
     * @param minLength
     *            length
     * @return the minCharConstraint
     */
    public Constraint getMinCharConstraint(int minLength) {
        return all(new Constraint[] { minLength(minLength) });
    }

    /**
     * @return the numericConstraint
     */
    public Constraint getNumericConstraint() {
        return numericConstraint;
    }

    /**
     * @return the partitaIVAConstraint
     */
    public Constraint getPartitaIVAConstraint() {
        PartitaIVAConstraint partitaIVAConstraint = new PartitaIVAConstraint();
        // per avere il messaggio di validazione i18n
        partitaIVAConstraint.setType("partitaIVAConstraint");
        return partitaIVAConstraint;
    }

    /**
     * @return the partitaIVARequiredConstraint
     */
    public Constraint getPartitaIVARequiredConstraint() {
        return all(new Constraint[] { getPartitaIVAConstraint(), getRequiredConstraint() });
    }

    /**
     * @param periodoRequired
     *            true for required
     * @return the periodoConstraint
     */
    public Constraint getPeriodoConstraint(boolean periodoRequired) {
        PeriodoConstraint periodoConstraint = new PeriodoConstraint(periodoRequired);
        return periodoConstraint;
    }

    /**
     * @return constraint per la definizione di un campo richiesto.
     */
    public Constraint getRequiredConstraint() {
        return requiredConstraint;
    }

    /**
     * @return lista di rule da applicare
     */
    public abstract List<Rules> getRules();
}
