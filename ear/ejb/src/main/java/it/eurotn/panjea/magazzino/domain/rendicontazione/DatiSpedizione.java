package it.eurotn.panjea.magazzino.domain.rendicontazione;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;

@Embeddable
public class DatiSpedizione implements Serializable {

    private static final long serialVersionUID = 236684896192611035L;

    @Column(length = 50)
    private String nomeFile;

    private SuffissoNomeFile suffissoNomeFile;

    /**
     * Indica se il suffisso deve essere applicato anche al primo file generato.
     */
    private boolean applicaSuffissoAlPrimoFile;

    // dati mail
    private String indirizzoMail;

    @ManyToOne(optional = true)
    private EntitaLite entita;

    @ManyToOne(optional = true)
    private SedeEntita sedeEntita;

    // dati ftp
    private String indirizzoFTP;

    // directory remota ftp
    private String remoteDirFTP;

    private String userName;

    private String password;

    /**
     * @return the entita
     */
    public EntitaLite getEntita() {
        return entita;
    }

    /**
     * @return the indirizzoFTP
     */
    public String getIndirizzoFTP() {
        return indirizzoFTP;
    }

    /**
     * @return the indirizzoMail
     */
    public String getIndirizzoMail() {
        return indirizzoMail;
    }

    /**
     * @return the nomeFile
     */
    public String getNomeFile() {
        return nomeFile;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @return the remoteDirFTP
     */
    public String getRemoteDirFTP() {
        return remoteDirFTP;
    }

    /**
     * @return the sedeEntita
     */
    public SedeEntita getSedeEntita() {
        return sedeEntita;
    }

    /**
     * @return the suffissoNomeFile
     */
    public SuffissoNomeFile getSuffissoNomeFile() {
        return suffissoNomeFile;
    }

    /**
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @return the applicaSuffissoAlPrimoFile
     */
    public boolean isApplicaSuffissoAlPrimoFile() {
        return applicaSuffissoAlPrimoFile;
    }

    /**
     * @param applicaSuffissoAlPrimoFile
     *            the applicaSuffissoAlPrimoFile to set
     */
    public void setApplicaSuffissoAlPrimoFile(boolean applicaSuffissoAlPrimoFile) {
        this.applicaSuffissoAlPrimoFile = applicaSuffissoAlPrimoFile;
    }

    /**
     * @param entita
     *            the entita to set
     */
    public void setEntita(EntitaLite entita) {
        this.entita = entita;
    }

    /**
     * @param indirizzoFTP
     *            the indirizzoFTP to set
     */
    public void setIndirizzoFTP(String indirizzoFTP) {
        this.indirizzoFTP = indirizzoFTP;
    }

    /**
     * @param indirizzoMail
     *            the indirizzoMail to set
     */
    public void setIndirizzoMail(String indirizzoMail) {
        this.indirizzoMail = indirizzoMail;
    }

    /**
     * @param nomeFile
     *            the nomeFile to set
     */
    public void setNomeFile(String nomeFile) {
        this.nomeFile = nomeFile;
    }

    /**
     * @param password
     *            the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @param remoteDirFTP
     *            the remoteDirFTP to set
     */
    public void setRemoteDirFTP(String remoteDirFTP) {
        this.remoteDirFTP = remoteDirFTP;
    }

    /**
     * @param sedeEntita
     *            the sedeEntita to set
     */
    public void setSedeEntita(SedeEntita sedeEntita) {
        this.sedeEntita = sedeEntita;
    }

    /**
     * @param suffissoNomeFile
     *            the suffissoNomeFile to set
     */
    public void setSuffissoNomeFile(SuffissoNomeFile suffissoNomeFile) {
        this.suffissoNomeFile = suffissoNomeFile;
    }

    /**
     * @param userName
     *            the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }
}
