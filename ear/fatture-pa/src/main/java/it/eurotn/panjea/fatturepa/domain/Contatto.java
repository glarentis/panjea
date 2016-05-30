package it.eurotn.panjea.fatturepa.domain;

import java.io.Serializable;

import javax.persistence.Embeddable;

/**
 * @author fattazzo
 *
 */
@Embeddable
public class Contatto implements Serializable {

    private static final long serialVersionUID = 1320174117741471433L;

    private String telefono;

    private String fax;

    private String email;

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @return the fax
     */
    public String getFax() {
        return fax;
    }

    /**
     * @return the telefono
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * @param email
     *            the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @param fax
     *            the fax to set
     */
    public void setFax(String fax) {
        this.fax = fax;
    }

    /**
     * @param telefono
     *            the telefono to set
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
