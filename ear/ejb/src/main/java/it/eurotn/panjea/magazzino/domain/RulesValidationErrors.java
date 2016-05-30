package it.eurotn.panjea.magazzino.domain;

import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;

import it.eurotn.panjea.magazzino.rulesvalidation.AbstractRigaArticoloRulesValidation;

/**
 *
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
public class RulesValidationErrors implements Serializable {

    private static final long serialVersionUID = -5715748764567019254L;

    /**
     * @uml.property name="rules"
     */
    private final Set<String> rules;

    /**
     * Costruttore.
     */
    public RulesValidationErrors() {
        super();
        this.rules = new TreeSet<String>();
    }

    /**
     * Aggiunge un errore di validazione.
     * 
     * @param rule
     *            errore di validazione
     */
    public void addToRules(AbstractRigaArticoloRulesValidation rule) {
        this.rules.add(rule.getClass().getName());
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        RulesValidationErrors other = (RulesValidationErrors) obj;
        if (rules == null) {
            if (other.rules != null) {
                return false;
            }
        } else if (!rules.equals(other.rules)) {
            return false;
        }
        return true;
    }

    /**
     * @return the rules
     * @uml.property name="rules"
     */
    public Set<String> getRules() {
        return rules;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((rules == null) ? 0 : rules.hashCode());
        return result;
    }

}
