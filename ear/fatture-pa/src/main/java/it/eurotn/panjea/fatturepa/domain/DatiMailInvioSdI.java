package it.eurotn.panjea.fatturepa.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.log4j.Logger;

import it.eurotn.panjea.sicurezza.domain.DatiMail;
import it.eurotn.panjea.sicurezza.domain.DatiMail.TipoConnessione;
import it.eurotn.util.PanjeaEJBUtil;

@Embeddable
public class DatiMailInvioSdI implements Serializable, Cloneable {

    private static final Logger LOGGER = Logger.getLogger(DatiMailInvioSdI.class);

    private static final long serialVersionUID = -6125536070983302640L;

    @Column(name = "server", length = 30)
    private String server;

    @Column(name = "email", length = 60)
    private String email;

    @Column(name = "utenteMail", length = 60)
    private String utenteMail;

    @Column(name = "passwordMail", length = 60)
    private String passwordMail;

    private Integer port;

    private boolean auth;

    private TipoConnessione tipoConnessione;

    @Column(name = "emailDiRisposta", length = 60)
    private String emailDiRisposta;

    {
        tipoConnessione = TipoConnessione.NESSUNA;
    }

    /**
     * Costruttore.
     */
    public DatiMailInvioSdI() {
        super();
    }

    /**
     * Costruttore.
     *
     * @param datiMail
     *            {@link DatiMail}
     */
    public DatiMailInvioSdI(final DatiMail datiMail) {
        super();
        PanjeaEJBUtil.copyProperties(this, datiMail);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("--> CLONE");
        }
        return super.clone();
    }

    /**
     * @return {@link DatiMail}
     */
    public DatiMail getDatiMail() {
        DatiMail datiMail = new DatiMail();
        PanjeaEJBUtil.copyProperties(datiMail, this);

        return datiMail;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @return the mailDiRisposta
     */
    public String getEmailDiRisposta() {
        return emailDiRisposta;
    }

    /**
     * @return the passwordMail
     */
    public String getPasswordMail() {
        return passwordMail;
    }

    /**
     * @return the port
     */
    public Integer getPort() {
        return port;
    }

    /**
     * @return the server
     */
    public String getServer() {
        return server;
    }

    /**
     * @return Returns the tipoConnessione.
     */
    public TipoConnessione getTipoConnessione() {
        return tipoConnessione;
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
        result = result && port != null;
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
     * @param emailDiRisposta
     *            the emailDiRisposta to set
     */
    public void setEmailDiRisposta(String emailDiRisposta) {
        this.emailDiRisposta = emailDiRisposta;
    }

    /**
     * @param passwordMail
     *            the passwordMail to set
     */
    public void setPasswordMail(String passwordMail) {
        this.passwordMail = passwordMail;
    }

    /**
     * @param port
     *            the port to set
     */
    public void setPort(Integer port) {
        this.port = port;
    }

    /**
     * @param server
     *            the server to set
     */
    public void setServer(String server) {
        this.server = server;
    }

    /**
     * @param tipoConnessione
     *            The tipoConnessione to set.
     */
    public void setTipoConnessione(TipoConnessione tipoConnessione) {
        this.tipoConnessione = tipoConnessione;
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
