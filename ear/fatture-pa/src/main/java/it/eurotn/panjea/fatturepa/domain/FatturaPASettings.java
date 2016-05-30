package it.eurotn.panjea.fatturepa.domain;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.QueryHint;
import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

import it.eurotn.entity.EntityBase;
import it.gov.fatturapa.sdi.fatturapa.FormatoTrasmissioneType;

/**
 * @author fattazzo
 *
 */
@Entity
@Audited
@Table(name = "ftpa_settings")
@NamedQueries({
        @NamedQuery(name = "FatturaPASettings.caricaAll", query = "from FatturaPASettings ftpas where ftpas.codiceAzienda = :codiceAzienda", hints = {
                @QueryHint(name = "org.hibernate.cacheable", value = "true"),
                @QueryHint(name = "org.hibernate.cacheRegion", value = "magazzinoSettings") }) })
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "magazzinoSettings")
public class FatturaPASettings extends EntityBase {

    public enum SoftwareFirmaElettronica {
        FIRMA_CERTA
    }

    private static final long serialVersionUID = -4542682030671200L;

    @Column(length = 10, nullable = false)
    private String codiceAzienda;

    @Column(length = 10)
    private String registroProtocollo;

    private boolean gestioneFirmaElettronica;

    private SoftwareFirmaElettronica softwareFirmaElettronica;

    private String softwareFirmaPath;

    private FormatoTrasmissioneType formatoTrasmissione;

    private String emailSpedizioneSdI;

    @Embedded
    private DatiMailInvioSdI datiMailInvioSdI;

    @Embedded
    @AttributeOverrides({ @AttributeOverride(name = "server", column = @Column(name = "serverRicezione") ),
            @AttributeOverride(name = "email", column = @Column(name = "emailRicezione") ),
            @AttributeOverride(name = "utenteMail", column = @Column(name = "utenteMailRicezione") ),
            @AttributeOverride(name = "passwordMail", column = @Column(name = "passwordMailRicezione") ),
            @AttributeOverride(name = "port", column = @Column(name = "portRicezione") ),
            @AttributeOverride(name = "auth", column = @Column(name = "authRicezione") ) })
    private DatiMailRicezioneSdI datiMailRicezioneSdI;

    private boolean controlloNotificheSdiAbilitato;

    private String emailRicezioneSdI;

    private boolean attivaConservazioneSostitutiva;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "indirizzoWeb", column = @Column(name = "indirizzoWebConservazioneSostitutiva") ),
            @AttributeOverride(name = "utente", column = @Column(name = "utenteConservazioneSostitutiva") ),
            @AttributeOverride(name = "password", column = @Column(name = "passwordConservazioneSostitutiva") ) })
    private DatiConservazioneSostitutiva datiConservazioneSostitutiva;

    private Integer numeroGiorniControlloNotifiche;

    {
        gestioneFirmaElettronica = false;
        softwareFirmaElettronica = SoftwareFirmaElettronica.FIRMA_CERTA;
        formatoTrasmissione = FormatoTrasmissioneType.SDI_10;
        emailSpedizioneSdI = "sdi01@pec.fatturapa.it";
        datiMailInvioSdI = new DatiMailInvioSdI();
        datiMailRicezioneSdI = new DatiMailRicezioneSdI();
        controlloNotificheSdiAbilitato = false;
        emailRicezioneSdI = "@pec.fatturapa.it";
        attivaConservazioneSostitutiva = false;
        datiConservazioneSostitutiva = new DatiConservazioneSostitutiva();

        numeroGiorniControlloNotifiche = 6;
    }

    /**
     * Costruttore.
     */
    public FatturaPASettings() {
        super();
    }

    /**
     * @return the codiceAzienda
     */
    public String getCodiceAzienda() {
        return codiceAzienda;
    }

    /**
     * @return the datiConservazioneSostitutiva
     */
    public DatiConservazioneSostitutiva getDatiConservazioneSostitutiva() {
        if (datiConservazioneSostitutiva == null) {
            datiConservazioneSostitutiva = new DatiConservazioneSostitutiva();
        }

        return datiConservazioneSostitutiva;
    }

    /**
     * @return the datiMailInvioSdI
     */
    public DatiMailInvioSdI getDatiMailInvioSdI() {
        if (datiMailInvioSdI == null) {
            datiMailInvioSdI = new DatiMailInvioSdI();
        }
        return datiMailInvioSdI;
    }

    /**
     * @return the datiMailRicezioneSdI
     */
    public DatiMailRicezioneSdI getDatiMailRicezioneSdI() {
        if (datiMailRicezioneSdI == null) {
            datiMailRicezioneSdI = new DatiMailRicezioneSdI();
        }
        return datiMailRicezioneSdI;
    }

    /**
     * @return the emailRicezioneSdI
     */
    public String getEmailRicezioneSdI() {
        return emailRicezioneSdI;
    }

    /**
     * @return the emailSpedizioneSdI
     */
    public String getEmailSpedizioneSdI() {
        return emailSpedizioneSdI;
    }

    /**
     * @return the formatoTrasmissione
     */
    public FormatoTrasmissioneType getFormatoTrasmissione() {
        return formatoTrasmissione;
    }

    /**
     * @return the numeroGiorniControlloNotifiche
     */
    public Integer getNumeroGiorniControlloNotifiche() {
        return numeroGiorniControlloNotifiche;
    }

    /**
     * @return the registroProtocollo
     */
    public String getRegistroProtocollo() {
        return registroProtocollo;
    }

    /**
     * @return the softwareFirmaElettronica
     */
    public SoftwareFirmaElettronica getSoftwareFirmaElettronica() {
        return softwareFirmaElettronica;
    }

    /**
     * @return the softwareFirmaPath
     */
    public String getSoftwareFirmaPath() {
        return softwareFirmaPath;
    }

    /**
     * @return the attivaConservazioneSostitutiva
     */
    public boolean isAttivaConservazioneSostitutiva() {
        return attivaConservazioneSostitutiva;
    }

    /**
     * @return the controlloNotificheSdiAbilitato
     */
    public boolean isControlloNotificheSdiAbilitato() {
        return controlloNotificheSdiAbilitato;
    }

    /**
     * @return the gestioneFirmaElettronica
     */
    public boolean isGestioneFirmaElettronica() {
        return gestioneFirmaElettronica;
    }

    /**
     * @return <code>true</code> se i dati obbligatori sono tutti presenti
     */
    public boolean isValid() {
        return !StringUtils.isBlank(registroProtocollo) && !StringUtils.isBlank(softwareFirmaPath)
                && softwareFirmaElettronica != null;
    }

    /**
     * @param attivaConservazioneSostitutiva
     *            the attivaConservazioneSostitutiva to set
     */
    public void setAttivaConservazioneSostitutiva(boolean attivaConservazioneSostitutiva) {
        this.attivaConservazioneSostitutiva = attivaConservazioneSostitutiva;
    }

    /**
     * @param codiceAzienda
     *            the codiceAzienda to set
     */
    public void setCodiceAzienda(String codiceAzienda) {
        this.codiceAzienda = codiceAzienda;
    }

    /**
     * @param controlloNotificheSdiAbilitato
     *            the controlloNotificheSdiAbilitato to set
     */
    public void setControlloNotificheSdiAbilitato(boolean controlloNotificheSdiAbilitato) {
        this.controlloNotificheSdiAbilitato = controlloNotificheSdiAbilitato;
    }

    /**
     * @param datiConservazioneSostitutiva
     *            the datiConservazioneSostitutiva to set
     */
    public void setDatiConservazioneSostitutiva(DatiConservazioneSostitutiva datiConservazioneSostitutiva) {
        this.datiConservazioneSostitutiva = datiConservazioneSostitutiva;
    }

    /**
     * @param datiMailInvioSdI
     *            the datiMailInvioSdI to set
     */
    public void setDatiMailInvioSdI(DatiMailInvioSdI datiMailInvioSdI) {
        this.datiMailInvioSdI = datiMailInvioSdI;
    }

    /**
     * @param datiMailRicezioneSdI
     *            the datiMailRicezioneSdI to set
     */
    public void setDatiMailRicezioneSdI(DatiMailRicezioneSdI datiMailRicezioneSdI) {
        this.datiMailRicezioneSdI = datiMailRicezioneSdI;
    }

    /**
     * @param emailRicezioneSdI
     *            the emailRicezioneSdI to set
     */
    public void setEmailRicezioneSdI(String emailRicezioneSdI) {
        this.emailRicezioneSdI = emailRicezioneSdI;
    }

    /**
     * @param emailSpedizioneSdI
     *            the emailSpedizioneSdI to set
     */
    public void setEmailSpedizioneSdI(String emailSpedizioneSdI) {
        this.emailSpedizioneSdI = emailSpedizioneSdI;
    }

    /**
     * @param formatoTrasmissione
     *            the formatoTrasmissione to set
     */
    public void setFormatoTrasmissione(FormatoTrasmissioneType formatoTrasmissione) {
        this.formatoTrasmissione = formatoTrasmissione;
    }

    /**
     * @param gestioneFirmaElettronica
     *            the gestioneFirmaElettronica to set
     */
    public void setGestioneFirmaElettronica(boolean gestioneFirmaElettronica) {
        this.gestioneFirmaElettronica = gestioneFirmaElettronica;
    }

    /**
     * @param numeroGiorniControlloNotifiche
     *            the numeroGiorniControlloNotifiche to set
     */
    public void setNumeroGiorniControlloNotifiche(Integer numeroGiorniControlloNotifiche) {
        this.numeroGiorniControlloNotifiche = numeroGiorniControlloNotifiche;
    }

    /**
     * @param registroProtocollo
     *            the registroProtocollo to set
     */
    public void setRegistroProtocollo(String registroProtocollo) {
        this.registroProtocollo = registroProtocollo;
    }

    /**
     * @param softwareFirmaElettronica
     *            the softwareFirmaElettronica to set
     */
    public void setSoftwareFirmaElettronica(SoftwareFirmaElettronica softwareFirmaElettronica) {
        this.softwareFirmaElettronica = softwareFirmaElettronica;
    }

    /**
     * @param softwareFirmaPath
     *            the softwareFirmaPath to set
     */
    public void setSoftwareFirmaPath(String softwareFirmaPath) {
        this.softwareFirmaPath = softwareFirmaPath;
    }

}
