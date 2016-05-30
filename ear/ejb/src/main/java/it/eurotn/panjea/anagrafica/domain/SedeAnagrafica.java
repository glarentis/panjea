package it.eurotn.panjea.anagrafica.domain;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PostLoad;
import javax.persistence.Table;

import org.apache.log4j.Logger;
import org.hibernate.envers.Audited;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.anagrafica.domain.datigeografici.DatiGeografici;

@Entity
@Audited
@Table(name = "anag_sedi_anagrafica")
@NamedQueries({
        @NamedQuery(name = "SedeAnagrafica.eliminaAnagraficheOrfane", query = " delete SedeAnagrafica s "
                + " where s not in ( select sede.sede from SedeEntita as sede ) and "
                + "       s not in ( select sedeaz.sede from SedeAzienda as sedeaz) and "
                + "       s not in ( select anag.sedeAnagrafica from Anagrafica as anag)  "),
        @NamedQuery(name = "SedeAnagrafica.caricaSediAnagrafica", query = "select sedeEntita.sede "
                + "from Entita as entita inner join entita.sedi as sedeEntita "
                + "where entita.anagrafica.id = :paramIdAnagrafica and " + " sedeEntita.abilitato = :paramAbilitato "
                + "group by sedeEntita.sede") })
public class SedeAnagrafica extends EntityBase {

    public enum TipoSpedizioneDocumenti {
        EMAIL, STAMPA
    }

    private static Logger logger = Logger.getLogger(SedeAnagrafica.class);

    private static final long serialVersionUID = -2642580467436367395L;

    public static final String REF = "SedeAnagrafica";
    public static final String PROP_INDIRIZZO = "indirizzo";
    public static final String PROP_ABILITATO = "abilitato";
    public static final String PROP_ID = "id";
    public static final String PROP_NUMERO_CIVICO = "numeroCivico";
    public static final String PROP_DESCRIZIONE = "descrizione";
    public static final String PROP_TELEFONO = "telefono";
    public static final String PROP_FAX = "fax";
    public static final String PROP_INDIRIZZO_MAIL = "indirizzoMail";
    public static final String PROP_WEB = "web";

    @Embedded
    private DatiGeografici datiGeografici;

    @Column(name = "descrizione", length = 60)
    private java.lang.String descrizione;

    @Column(name = "indirizzo", length = 60)
    private java.lang.String indirizzo;

    @Column(name = "numero_civico", length = 10)
    private java.lang.String numeroCivico;

    @Column(name = "abilitato")
    private boolean abilitato;

    @Column(name = "telefono", length = 20)
    private java.lang.String telefono;

    @Column(name = "fax", length = 20)
    private java.lang.String fax;

    @Column(name = "indirizzo_mail", length = 100)
    private java.lang.String indirizzoMail;

    @Column(name = "indirizzo_pec", length = 100)
    private java.lang.String indirizzoPEC;

    @Column(name = "indirizzo_mail_spedizione", length = 100)
    private java.lang.String indirizzoMailSpedizione;

    @Column(name = "web", length = 150)
    private java.lang.String web;

    private TipoSpedizioneDocumenti tipoSpedizioneDocumenti;

    private boolean spedizioneDocumentiViaPEC;

    {
        spedizioneDocumentiViaPEC = false;
        tipoSpedizioneDocumenti = TipoSpedizioneDocumenti.STAMPA;
    }

    /**
     * Costruttore.
     */
    public SedeAnagrafica() {
        initialize();
    }

    /**
     * @return the datiGeografici
     */
    public DatiGeografici getDatiGeografici() {
        if (datiGeografici == null) {
            datiGeografici = new DatiGeografici();
        }
        return datiGeografici;
    }

    /**
     * @return descrizione
     * @uml.property name="descrizione"
     */
    public java.lang.String getDescrizione() {
        return descrizione;
    }

    /**
     * @return descrizione estesa
     */
    public java.lang.String getDescrizioneEstesa() {
        StringBuilder sb = new StringBuilder();
        if (getDescrizione() != null) {
            sb.append(getDescrizione());
        }

        if (getIndirizzo() != null) {
            sb.append(" ");
            sb.append(getIndirizzo());
        }

        String localita = getDatiGeografici().getDescrizioneLocalita();
        if (localita != null && !localita.isEmpty()) {
            sb.append(" ");
            sb.append(localita);
        }
        return sb.toString();
    }

    /**
     * @return fax
     * @uml.property name="fax"
     */
    public java.lang.String getFax() {
        return fax;
    }

    /**
     * @return indirizzo
     * @uml.property name="indirizzo"
     */
    public java.lang.String getIndirizzo() {
        return indirizzo;
    }

    /**
     * @return indirizzoMail
     * @uml.property name="indirizzoMail"
     */
    public java.lang.String getIndirizzoMail() {
        if (indirizzoMail == null) {
            indirizzoMail = "";
        }
        return indirizzoMail;
    }

    /**
     * @return the indirizzoMailSpedizione
     */
    public java.lang.String getIndirizzoMailSpedizione() {
        return indirizzoMailSpedizione;
    }

    /**
     * @return the indirizzoPEC
     */
    public java.lang.String getIndirizzoPEC() {
        if (indirizzoPEC == null) {
            indirizzoPEC = "";
        }
        return indirizzoPEC;
    }

    /**
     *
     * @return url per aprire le mappe google in un browser
     */
    public String getMapUrl() {
        StringBuilder sb = new StringBuilder("https://www.google.it/maps/place/");
        String datiGeogStr = getIndirizzo() + " " + getDatiGeografici().getUrlMap();
        try {
            datiGeogStr = URLEncoder.encode(datiGeogStr, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // se non si riesce a fare il parse utilizzo l'url così com'è
            if (logger.isDebugEnabled()) {
                logger.debug("--> url dati geografici: " + datiGeogStr);
            }
        }

        sb.append(datiGeogStr);
        return sb.toString();
    }

    /**
     * @return numeroCivico
     * @uml.property name="numeroCivico"
     */
    public java.lang.String getNumeroCivico() {
        return numeroCivico;
    }

    /**
     * @return telefono
     * @uml.property name="telefono"
     */
    public java.lang.String getTelefono() {
        return telefono;
    }

    /**
     * @return the tipoSpedizioneDocumenti
     */
    public TipoSpedizioneDocumenti getTipoSpedizioneDocumenti() {
        return tipoSpedizioneDocumenti;
    }

    /**
     * @return web
     * @uml.property name="web"
     */
    public java.lang.String getWeb() {
        return web;
    }

    /**
     * Inizializza i valori di default.
     */
    @PostLoad
    public void initialize() {
    }

    /**
     * @return isAbilitato
     * @uml.property name="abilitato"
     */
    public boolean isAbilitato() {
        return abilitato;
    }

    /**
     * @return the spedizioneDocumentiViaPEC
     */
    public boolean isSpedizioneDocumentiViaPEC() {
        return spedizioneDocumentiViaPEC;
    }

    /**
     * @param abilitato
     *            the abilitato to set
     * @uml.property name="abilitato"
     */
    public void setAbilitato(boolean abilitato) {
        this.abilitato = abilitato;
    }

    /**
     * @param datiGeografici
     *            the datiGeografici to set
     */
    public void setDatiGeografici(DatiGeografici datiGeografici) {
        this.datiGeografici = datiGeografici;
    }

    /**
     * @param descrizione
     *            the descrizione to set
     * @uml.property name="descrizione"
     */
    public void setDescrizione(java.lang.String descrizione) {
        this.descrizione = descrizione;
    }

    /**
     * @param fax
     *            the fax to set
     * @uml.property name="fax"
     */
    public void setFax(java.lang.String fax) {
        this.fax = fax;
    }

    /**
     * @param indirizzo
     *            the indirizzo to set
     * @uml.property name="indirizzo"
     */
    public void setIndirizzo(java.lang.String indirizzo) {
        this.indirizzo = indirizzo;
    }

    /**
     * @param indirizzoMail
     *            the indirizzoMail to set
     * @uml.property name="indirizzoMail"
     */
    public void setIndirizzoMail(java.lang.String indirizzoMail) {
        this.indirizzoMail = indirizzoMail;
    }

    /**
     * @param indirizzoMailSpedizione
     *            the indirizzoMailSpedizione to set
     */
    public void setIndirizzoMailSpedizione(java.lang.String indirizzoMailSpedizione) {
        this.indirizzoMailSpedizione = indirizzoMailSpedizione;
    }

    /**
     * @param indirizzoPEC
     *            the indirizzoPEC to set
     */
    public void setIndirizzoPEC(java.lang.String indirizzoPEC) {
        this.indirizzoPEC = indirizzoPEC;
    }

    /**
     * @param numeroCivico
     *            the numeroCivico to set
     * @uml.property name="numeroCivico"
     */
    public void setNumeroCivico(java.lang.String numeroCivico) {
        this.numeroCivico = numeroCivico;
    }

    /**
     * @param spedizioneDocumentiViaPEC
     *            the spedizioneDocumentiViaPEC to set
     */
    public void setSpedizioneDocumentiViaPEC(boolean spedizioneDocumentiViaPEC) {
        this.spedizioneDocumentiViaPEC = spedizioneDocumentiViaPEC;
    }

    /**
     * @param telefono
     *            the telefono to set
     * @uml.property name="telefono"
     */
    public void setTelefono(java.lang.String telefono) {
        this.telefono = telefono;
    }

    /**
     * @param tipoSpedizioneDocumenti
     *            the tipoSpedizioneDocumenti to set
     */
    public void setTipoSpedizioneDocumenti(TipoSpedizioneDocumenti tipoSpedizioneDocumenti) {
        this.tipoSpedizioneDocumenti = tipoSpedizioneDocumenti;
    }

    /**
     * @param web
     *            the web to set
     * @uml.property name="web"
     */
    public void setWeb(java.lang.String web) {
        this.web = web;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("SedeAnagrafica[");
        buffer.append(super.toString());
        buffer.append("abilitato = ").append(abilitato);
        buffer.append(" descrizione = ").append(descrizione);
        buffer.append(" fax = ").append(fax);
        buffer.append(" indirizzo = ").append(indirizzo);
        buffer.append(" numeroCivico = ").append(numeroCivico);
        buffer.append(" telefono = ").append(telefono);
        buffer.append(" web = ").append(web);
        buffer.append("]");
        return buffer.toString();
    }

}