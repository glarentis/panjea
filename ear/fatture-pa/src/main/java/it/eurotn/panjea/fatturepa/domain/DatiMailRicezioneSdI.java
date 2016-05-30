package it.eurotn.panjea.fatturepa.domain;

import java.io.Serializable;

import javax.persistence.Column;

import org.apache.log4j.Logger;

import it.eurotn.panjea.sicurezza.domain.DatiMail;
import it.eurotn.util.PanjeaEJBUtil;

public class DatiMailRicezioneSdI implements Serializable, Cloneable {

    private static final Logger LOGGER = Logger.getLogger(DatiMailRicezioneSdI.class);

    private static final long serialVersionUID = -6125536070983302640L;

    @Column(name = "server", length = 30)
    private String server;

    @Column(name = "email", length = 60)
    private String email;

    @Column(name = "utenteMail", length = 60)
    private String utenteMail;

    @Column(name = "passwordMail", length = 60)
    private String passwordMail;

    private boolean auth;

    /**
     * Costruttore.
     */
    public DatiMailRicezioneSdI() {
        super();
    }

    /**
     * Costruttore.
     *
     * @param datiMail
     *            {@link DatiMail}
     */
    public DatiMailRicezioneSdI(final DatiMail datiMail) {
        super();
        PanjeaEJBUtil.copyProperties(this, datiMail);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("--> Clone");
        }
        return super.clone();
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @return the passwordMail
     */
    public String getPasswordMail() {
        return passwordMail;
    }

    /**
     * @return the server
     */
    public String getServer() {
        return server;
    }

    /**
     * @return the utenteMail
     */
    public String getUtenteMail() {
        return utenteMail;
    }

    /**
     * @return Returns the auth.
     */
    public boolean isAuth() {
        return auth;
    }

    /**
     * @return true se i dati sono configurati correttamente per l'invio delle mail.
     */
    public boolean isValid() {
        boolean result;
        result = server != null && server.length() > 0;
        result = result && email != null && email.length() > 0;
        if (auth) {
            result = result && passwordMail != null && passwordMail.length() > 0;
            result = result && utenteMail != null && utenteMail.length() > 0;
        }
        return result;
    }

    /**
     * @param auth
     *            The auth to set.
     */
    public void setAuth(boolean auth) {
        this.auth = auth;
    }

    /**
     * @param email
     *            the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @param passwordMail
     *            the passwordMail to set
     */
    public void setPasswordMail(String passwordMail) {
        this.passwordMail = passwordMail;
    }

    /**
     * @param server
     *            the server to set
     */
    public void setServer(String server) {
        this.server = server;
    }

    /**
     * @param utenteMail
     *            the utenteMail to set
     */
    public void setUtenteMail(String utenteMail) {
        this.utenteMail = utenteMail;
    }

    /**
     * Costruisce una <code>String</code> con tutti gli attributi accodati con questo formato nome attributo = valore.
     *
     * @return toString
     */
    @Override
    public String toString() {
        final String spazio = " ";
        StringBuilder retValue = new StringBuilder();
        retValue.append("DatiMail[ ").append(spazio).append("email = ").append(this.email).append(spazio).append("]");
        return retValue.toString();
    }

}
