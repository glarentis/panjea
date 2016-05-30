package it.eurotn.panjea.anagrafica.domain.datigeografici;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;
import org.hibernate.envers.Audited;

import it.eurotn.entity.EntityBase;
import it.eurotn.entity.annotation.EntityConverter;

@Entity
@Audited
@Table(name = "geog_nazioni", uniqueConstraints = @UniqueConstraint(columnNames = "codice") )
@NamedQueries({
        @NamedQuery(name = "Nazione.caricaAll", query = "from Nazione n where n.codice like :codice order by n.codice ") })
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "datiGeografici")
@EntityConverter(properties = "codice,descrizione")
public class Nazione extends EntityBase {

    public static final String LIVELLO1_PLACEHOLDER = "lv1";
    public static final String LIVELLO2_PLACEHOLDER = "lv2";
    public static final String LIVELLO2_SIGLA_PLACEHOLDER = "lv2s";
    public static final String LIVELLO3_PLACEHOLDER = "lv3";
    public static final String LIVELLO4_PLACEHOLDER = "lv4";
    public static final String CAP_PLACEHOLDER = "cap";
    public static final String LOCALITA_PLACEHOLDER = "loc";

    private static final long serialVersionUID = 4350273407819612174L;

    @Column(length = 3, nullable = true)
    private String codiceValuta;

    @Column(length = 6, nullable = false)
    @Index(name = "nazioneCodice")
    private String codice;

    @Column(name = "descrizione", length = 60)
    private String descrizione;

    @Column(length = 60)
    private String livelloAmministrativo1;

    @Column(length = 60)
    private String livelloAmministrativo2;

    @Column(length = 60)
    private String livelloAmministrativo3;

    @Column(length = 60)
    private String livelloAmministrativo4;

    private boolean intra;

    private boolean blacklist;

    @Column(length = 70)
    private String mascheraIndirizzo;

    private String codiceNazioneVettore;

    private Integer codiceNazioneUIC;

    {
        blacklist = false;
    }

    /**
     * Costruttore.
     */
    public Nazione() {
        super();
    }

    /**
     * @return the codice
     */
    public String getCodice() {
        return codice;
    }

    /**
     * @return the codiceNazioneUIC
     */
    public Integer getCodiceNazioneUIC() {
        return codiceNazioneUIC;
    }

    /**
     * @return the codiceNazioneVettore
     */
    public String getCodiceNazioneVettore() {
        return codiceNazioneVettore;
    }

    /**
     * @return Returns the codiceValuta.
     */
    public String getCodiceValuta() {
        return codiceValuta;
    }

    /**
     * @return the descrizione
     */
    public String getDescrizione() {
        return descrizione;
    }

    /**
     * @return the livelloAmministrativo1
     */
    public String getLivelloAmministrativo1() {
        return livelloAmministrativo1;
    }

    /**
     * @return the livelloAmministrativo2
     */
    public String getLivelloAmministrativo2() {
        return livelloAmministrativo2;
    }

    /**
     * @return the livelloAmministrativo3
     */
    public String getLivelloAmministrativo3() {
        return livelloAmministrativo3;
    }

    /**
     * @return the livelloAmministrativo4
     */
    public String getLivelloAmministrativo4() {
        return livelloAmministrativo4;
    }

    /**
     * @return Returns the mascheraIndirizzo.
     */
    public String getMascheraIndirizzo() {
        return mascheraIndirizzo;
    }

    /**
     * @return restituisce il numero di livelli disponibili per la Nazione.
     */
    public int getNumeroLivelli() {
        int numeroLivelli = 0;
        if (livelloAmministrativo1 != null) {
            numeroLivelli += 1;
        }
        if (livelloAmministrativo2 != null) {
            numeroLivelli += 1;
        }
        if (livelloAmministrativo3 != null) {
            numeroLivelli += 1;
        }
        if (livelloAmministrativo4 != null) {
            numeroLivelli += 1;
        }
        return numeroLivelli;
    }

    /**
     * @return definisce se per la nazione corrente è disponibile il livello amministrativo 1
     */
    public boolean hasLivelloAmministrativo1() {
        return livelloAmministrativo1 != null;
    }

    /**
     * @return definisce se per la nazione corrente è disponibile il livello amministrativo 2
     */
    public boolean hasLivelloAmministrativo2() {
        return livelloAmministrativo2 != null;
    }

    /**
     * @return definisce se per la nazione corrente è disponibile il livello amministrativo 3
     */
    public boolean hasLivelloAmministrativo3() {
        return livelloAmministrativo3 != null;
    }

    /**
     * @return definisce se per la nazione corrente è disponibile il livello amministrativo 4
     */
    public boolean hasLivelloAmministrativo4() {
        return livelloAmministrativo4 != null;
    }

    /**
     * @return the blacklist
     */
    public boolean isBlacklist() {
        return blacklist;
    }

    /**
     * @return the intra
     */
    public boolean isIntra() {
        return intra;
    }

    /**
     * @param blacklist
     *            the blacklist to set
     */
    public void setBlacklist(boolean blacklist) {
        this.blacklist = blacklist;
    }

    /**
     * @param codice
     *            the codice to set
     */
    public void setCodice(String codice) {
        this.codice = codice;
    }

    /**
     * @param codiceNazioneUIC
     *            the codiceNazioneUIC to set
     */
    public void setCodiceNazioneUIC(Integer codiceNazioneUIC) {
        this.codiceNazioneUIC = codiceNazioneUIC;
    }

    /**
     * @param codiceNazioneVettore
     *            the codiceNazioneVettore to set
     */
    public void setCodiceNazioneVettore(String codiceNazioneVettore) {
        this.codiceNazioneVettore = codiceNazioneVettore;
    }

    /**
     * @param codiceValuta
     *            The codiceValuta to set.
     */
    public void setCodiceValuta(String codiceValuta) {
        this.codiceValuta = codiceValuta;
    }

    /**
     * @param descrizione
     *            the descrizione to set
     */
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    /**
     * @param intra
     *            the intra to set
     */
    public void setIntra(boolean intra) {
        this.intra = intra;
    }

    /**
     * @param livelloAmministrativo1
     *            the livelloAmministrativo1 to set
     */
    public void setLivelloAmministrativo1(String livelloAmministrativo1) {
        this.livelloAmministrativo1 = livelloAmministrativo1;
    }

    /**
     * @param livelloAmministrativo2
     *            the livelloAmministrativo2 to set
     */
    public void setLivelloAmministrativo2(String livelloAmministrativo2) {
        this.livelloAmministrativo2 = livelloAmministrativo2;
    }

    /**
     * @param livelloAmministrativo3
     *            the livelloAmministrativo3 to set
     */
    public void setLivelloAmministrativo3(String livelloAmministrativo3) {
        this.livelloAmministrativo3 = livelloAmministrativo3;
    }

    /**
     * @param livelloAmministrativo4
     *            the livelloAmministrativo4 to set
     */
    public void setLivelloAmministrativo4(String livelloAmministrativo4) {
        this.livelloAmministrativo4 = livelloAmministrativo4;
    }

    /**
     * @param mascheraIndirizzo
     *            The mascheraIndirizzo to set.
     */
    public void setMascheraIndirizzo(String mascheraIndirizzo) {
        this.mascheraIndirizzo = mascheraIndirizzo;
    }

}
