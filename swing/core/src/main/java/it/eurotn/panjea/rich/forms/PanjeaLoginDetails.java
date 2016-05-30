/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.eurotn.panjea.rich.forms;

import java.util.Locale;

import org.springframework.richclient.security.LoginDetails;
import org.springframework.rules.Rules;
import org.springframework.rules.constraint.Constraint;
import org.springframework.rules.constraint.property.PropertyConstraint;

/**
 * 
 * @author giangi
 */
public class PanjeaLoginDetails extends LoginDetails {

    public static final String PROPERTY_LINGUA = "lingua";
    public static final String PROPERTY_AZIENDA = "azienda";

    private Locale locale;
    private String azienda;
    private Rules validationRules;

    /**
     * @return Returns the ultimaAziendaLoggata.
     */
    public String getAzienda() {
        return azienda;
    }

    /**
     * @return Returns the locale.
     */
    public Locale getLocale() {
        return locale;
    }

    /*
     * Return the property constraints.
     * 
     * @see org.springframework.rules.PropertyConstraintProvider#getPropertyConstraint(java.lang.String)
     */
    @Override
    public PropertyConstraint getPropertyConstraint(String propertyName) {
        return validationRules.getPropertyConstraint(propertyName);
    }

    @Override
    protected void initRules() {
        this.validationRules = new Rules(getClass()) {
            protected int getPasswordMinLength() {
                return 2;
            }

            protected int getUsernameMinLength() {
                return 2;
            }

            @Override
            protected void initRules() {
                add(PROPERTY_USERNAME, all(new Constraint[] { required(), minLength(getUsernameMinLength()) }));
                add(PROPERTY_PASSWORD, all(new Constraint[] { required(), minLength(getPasswordMinLength()) }));
                add(PROPERTY_AZIENDA, all(new Constraint[] { required() }));
            }

        };
    }

    /**
     * @param ultimaAziendaLoggata
     *            The ultimaAziendaLoggata to set.
     */
    public void setAzienda(String ultimaAziendaLoggata) {
        this.azienda = ultimaAziendaLoggata;
    }

    /**
     * @param locale
     *            The locale to set.
     */
    public void setLocale(Locale locale) {
        this.locale = locale;
    }
}
