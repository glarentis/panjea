/**
 *
 */
package it.eurotn.panjea.sicurezza.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import it.eurotn.entity.EntityBase;

/**
 * @author angelo
 */
@Entity
@Table(name = "anag_dati_mail")
public class DatiMail extends EntityBase implements Cloneable {

    public enum TipoConnessione {
        NESSUNA(25), SSL(465), STARTTSL(587);

        private int porta;

        /**
         *
         * Costruttore.
         *
         * @param porta
         *            di porta di default per la connessione
         */
        private TipoConnessione(final int porta) {
            this.porta = porta;
        }

        /**
         * @return Returns the porta.
         */
        public int getPorta() {
            return porta;
        }
    }

    public static final String ID_DIV_FIRMA = "<div id=\"firma\">";

    public static final String PROP_PASSWORD = "passwordMail";

    public static final String PROP_EMAIL = "email";

    private static final long serialVersionUID = 1554225696011611298L;

    @Column(name = "nomeAccount", length = 60)
    private String nomeAccount;

    @ManyToOne(optional = false)
    private Utente utente;

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

    private boolean pec;

    @Column(name = "emailDiRisposta", length = 60)
    private String emailDiRisposta;

    @Lob
    private String firma;

    private boolean predefinito;

    private boolean notificaLettura;

    {
        tipoConnessione = TipoConnessione.NESSUNA;
        predefinito = false;
        notificaLettura = false;
    }

    /**
     * Constructor.
     */
    public DatiMail() {
        super();
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
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
     * @return the firma
     */
    public String getFirma() {
        return firma != null ? firma : "";
    }

    /**
     * @return the nomeAccount
     */
    public String getNomeAccount() {
        return nomeAccount;
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
     * @return the utente
     */
    public Utente getUtente() {
        return utente;
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
     * @return the notificaLettura
     */
    public boolean isNotificaLettura() {
        return notificaLettura;
    }

    /**
     * @return the pec
     */
    public boolean isPec() {
        return pec;
    }

    /**
     * @return the predefinito
     */
    public boolean isPredefinito() {
        return predefinito;
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
     * @param firma
     *            the firma to set
     */
    public void setFirma(String firma) {
        this.firma = firma;
    }

    /**
     * @param nomeAccount
     *            the nomeAccount to set
     */
    public void setNomeAccount(String nomeAccount) {
        this.nomeAccount = nomeAccount;
    }

    /**
     * @param notificaLettura
     *            the notificaLettura to set
     */
    public void setNotificaLettura(boolean notificaLettura) {
        this.notificaLettura = notificaLettura;
    }

    /**
     * @param passwordMail
     *            the passwordMail to set
     */
    public void setPasswordMail(String passwordMail) {
        this.passwordMail = passwordMail;
    }

    /**
     * @param pec
     *            the pec to set
     */
    public void setPec(boolean pec) {
        this.pec = pec;
    }

    /**
     * @param port
     *            the port to set
     */
    public void setPort(Integer port) {
        this.port = port;
    }

    /**
     * @param predefinito
     *            the predefinito to set
     */
    public void setPredefinito(boolean predefinito) {
        this.predefinito = predefinito;
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
     * @param utente
     *            the utente to set
     */
    public void setUtente(Utente utente) {
        this.utente = utente;
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
        StringBuffer retValue = new StringBuffer();
        retValue.append("DatiMail[ ").append(spazio).append("nomeAccount = ").append(this.nomeAccount).append(spazio)
                .append("email = ").append(this.email).append(spazio).append("]");
        return retValue.toString();
    }
}
