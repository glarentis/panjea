package it.eurotn.panjea.fatturepa.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class DatiConservazioneSostitutiva implements Serializable {

    private static final long serialVersionUID = -8876092429909571698L;

    private String indirizzoWeb;

    @Column(length = 30)
    private String utente;

    private String password;

    /**
     * @return the password
     */
    public byte[] getEncryptedPassword() {
        return password.getBytes();
    }

    /**
     * @return the indirizzoWeb
     */
    public String getIndirizzoWeb() {
        return indirizzoWeb;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @return the utente
     */
    public String getUtente() {
        return utente;
    }

    /**
     * @param indirizzoWeb
     *            the indirizzoWeb to set
     */
    public void setIndirizzoWeb(String indirizzoWeb) {
        this.indirizzoWeb = indirizzoWeb;
    }

    /**
     * @param password
     *            the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @param utente
     *            the utente to set
     */
    public void setUtente(String utente) {
        this.utente = utente;
    }
}
