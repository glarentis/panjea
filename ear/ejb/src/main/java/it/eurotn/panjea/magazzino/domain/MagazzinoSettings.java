package it.eurotn.panjea.magazzino.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.QueryHint;
import javax.persistence.Table;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.ParamDef;
import org.hibernate.envers.Audited;

import it.eurotn.entity.EntityBase;

/**
 * Magazzino settings rappresenta le impostazioni di base necessarie al corretto funzionamento della gestione magazzino.
 *
 * @author Leonardo
 */
@NamedQueries({
        @NamedQuery(name = "MagazzinoSettings.caricaAll", query = "from MagazzinoSettings ms where ms.codiceAzienda = :codiceAzienda", hints = {
                @QueryHint(name = "org.hibernate.cacheable", value = "true"),
                @QueryHint(name = "org.hibernate.cacheRegion", value = "magazzinoSettings") }) })
@Entity
@Audited
@Table(name = "maga_settings")
@FilterDef(name = MagazzinoSettings.ADDEBITO_DICHIARAZIONE_INTENTO_IN_VIGORE, parameters = @ParamDef(name = "paramDataRegistrazione", type = "date") )
public class MagazzinoSettings extends EntityBase implements java.io.Serializable {

    private static final long serialVersionUID = 8187242114521832671L;
    public static final String ADDEBITO_DICHIARAZIONE_INTENTO_IN_VIGORE = "addebitoDichiarazioneInVigore";

    @Column(length = 10, nullable = false)
    private String codiceAzienda;

    /**
     * se True su nuova riga articolo creo un lotto con stesso codice e datadell'ultimo inserito.
     */
    private boolean nuovoDaUltimoLotto;

    @Embedded
    private OrdinamentoFatturazione ordinamentoFatturazione;

    private boolean riportaTestateOrdineInFatturazione;

    private Integer annoCompetenza;

    @Column(length = 9, nullable = true)
    private String codiceGS1;

    @Column(length = 5, nullable = true)
    private String numeratoreBarCode;

    private boolean gestioneLottiAutomatici;

    private Boolean calcolaGiacenzeInCreazioneRiga;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "magazzinoSettings", cascade = CascadeType.ALL)
    @org.hibernate.annotations.Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Filter(name = ADDEBITO_DICHIARAZIONE_INTENTO_IN_VIGORE, condition = "dataVigore = (select max(addebitiDI.dataVigore) from maga_settings_addebito_dichiarazione_intento addebitiDI where addebitiDI.dataVigore<=:paramDataRegistrazione)")
    @OrderBy("dataVigore")
    private List<AddebitoDichiarazioneIntentoSettings> addebitiDichiarazioneIntento;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "magazzinoSettings", cascade = CascadeType.ALL)
    @org.hibernate.annotations.Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Filter(name = ADDEBITO_DICHIARAZIONE_INTENTO_IN_VIGORE, condition = "dataVigore = (select max(addebitiSoglie.dataVigore) from maga_settings_soglie_addebito_dichiarazione_intento addebitiSoglie where addebitiSoglie.dataVigore<=:paramDataRegistrazione)")
    @OrderBy("dataVigore")
    private List<SogliaAddebitoDichiarazioneSettings> sogliaAddebitoDichiarazione;

    private boolean aggiornaAutomaticamenteListiniCollegati;

    @Column(length = 2000)
    private String descrizioneNotaAutomaticaOmaggio;

    @Column(length = 2000)
    private String descrizioneNotaAutomaticaSomministrazione;

    {
        this.gestioneLottiAutomatici = false;
        this.addebitiDichiarazioneIntento = new ArrayList<AddebitoDichiarazioneIntentoSettings>();
        this.sogliaAddebitoDichiarazione = new ArrayList<SogliaAddebitoDichiarazioneSettings>();
        this.aggiornaAutomaticamenteListiniCollegati = true;
        this.riportaTestateOrdineInFatturazione = false;
    }

    /**
     * Costruttore.
     */
    public MagazzinoSettings() {
        super();
    }

    /**
     * @return the addebitiDichiarazioneIntento
     */
    public List<AddebitoDichiarazioneIntentoSettings> getAddebitiDichiarazioneIntento() {
        return addebitiDichiarazioneIntento;
    }

    /**
     * @return AddebitoDichiarazioneIntentoSettings
     */
    public AddebitoDichiarazioneIntentoSettings getAddebitoDichiarazioneIntentoInVigore() {
        return (addebitiDichiarazioneIntento != null && !addebitiDichiarazioneIntento.isEmpty())
                ? addebitiDichiarazioneIntento.get(0) : null;
    }

    /**
     * @return the annoCompetenza
     */
    public Integer getAnnoCompetenza() {
        return annoCompetenza;
    }

    /**
     * @return Returns the calcolaGiacenzeInCreazioneRiga.
     */
    public Boolean getCalcolaGiacenzeInCreazioneRiga() {
        if (calcolaGiacenzeInCreazioneRiga == null) {
            return false;
        }
        return calcolaGiacenzeInCreazioneRiga;
    }

    /**
     * @return the codiceAzienda
     */
    public String getCodiceAzienda() {
        return codiceAzienda;
    }

    /**
     * @return the codiceGS1
     */
    public String getCodiceGS1() {
        return codiceGS1;
    }

    /**
     * @return the descrizioneNotaAutomaticaOmaggio
     */
    public String getDescrizioneNotaAutomaticaOmaggio() {
        return descrizioneNotaAutomaticaOmaggio;
    }

    /**
     * @return the descrizioneNotaAutomaticaSomministrazione
     */
    public String getDescrizioneNotaAutomaticaSomministrazione() {
        return descrizioneNotaAutomaticaSomministrazione;
    }

    /**
     * @return the numeratoreBarCode
     */
    public String getNumeratoreBarCode() {
        return numeratoreBarCode;
    }

    /**
     * @return Returns the ordinamentoFatturazione.
     */
    public OrdinamentoFatturazione getOrdinamentoFatturazione() {
        if (ordinamentoFatturazione == null) {
            ordinamentoFatturazione = new OrdinamentoFatturazione();
        }
        return ordinamentoFatturazione;
    }

    /**
     * @return the sogliaAddebitoDichiarazione
     */
    public List<SogliaAddebitoDichiarazioneSettings> getSogliaAddebitoDichiarazione() {
        return sogliaAddebitoDichiarazione;
    }

    /**
     * @return the sogliaAddebitoDichiarazione
     */
    public SogliaAddebitoDichiarazioneSettings getSogliaAddebitoDichiarazioneInVigore() {
        return (sogliaAddebitoDichiarazione != null && !sogliaAddebitoDichiarazione.isEmpty())
                ? sogliaAddebitoDichiarazione.get(0) : null;
    }

    /**
     * @return the aggiornaAutomaticamenteListiniCollegati
     */
    public boolean isAggiornaAutomaticamenteListiniCollegati() {
        return aggiornaAutomaticamenteListiniCollegati;
    }

    /**
     * @return the gestioneLottiAutomatici
     */
    public boolean isGestioneLottiAutomatici() {
        return gestioneLottiAutomatici;
    }

    /**
     * @return Returns the nuovoDaUltimoLotto.
     */
    public boolean isNuovoDaUltimoLotto() {
        return nuovoDaUltimoLotto;
    }

    /**
     * @return the riportaTestateOrdineInFatturazione
     */
    public boolean isRiportaTestateOrdineInFatturazione() {
        return riportaTestateOrdineInFatturazione;
    }

    /**
     * @param addebitiDichiarazioneIntento
     *            the addebitiDichiarazioneIntento to set
     */
    public void setAddebitiDichiarazioneIntento(
            List<AddebitoDichiarazioneIntentoSettings> addebitiDichiarazioneIntento) {
        this.addebitiDichiarazioneIntento = addebitiDichiarazioneIntento;
    }

    /**
     * @param aggiornaAutomaticamenteListiniCollegati
     *            the aggiornaAutomaticamenteListiniCollegati to set
     */
    public void setAggiornaAutomaticamenteListiniCollegati(boolean aggiornaAutomaticamenteListiniCollegati) {
        this.aggiornaAutomaticamenteListiniCollegati = aggiornaAutomaticamenteListiniCollegati;
    }

    /**
     * @param annoCompetenza
     *            the annoCompetenza to set
     */
    public void setAnnoCompetenza(Integer annoCompetenza) {
        this.annoCompetenza = annoCompetenza;
    }

    /**
     * @param calcolaGiacenzeInCreazioneRiga
     *            The calcolaGiacenzeInCreazioneRiga to set.
     */
    public void setCalcolaGiacenzeInCreazioneRiga(Boolean calcolaGiacenzeInCreazioneRiga) {
        this.calcolaGiacenzeInCreazioneRiga = calcolaGiacenzeInCreazioneRiga;
    }

    /**
     * @param codiceAzienda
     *            the codiceAzienda to set
     */
    public void setCodiceAzienda(String codiceAzienda) {
        this.codiceAzienda = codiceAzienda;
    }

    /**
     * @param codiceGS1
     *            the codiceGS1 to set
     */
    public void setCodiceGS1(String codiceGS1) {
        this.codiceGS1 = codiceGS1;
    }

    /**
     * @param descrizioneNotaAutomaticaOmaggio
     *            the descrizioneNotaAutomaticaOmaggio to set
     */
    public void setDescrizioneNotaAutomaticaOmaggio(String descrizioneNotaAutomaticaOmaggio) {
        this.descrizioneNotaAutomaticaOmaggio = descrizioneNotaAutomaticaOmaggio;
    }

    /**
     * @param descrizioneNotaAutomaticaSomministrazione
     *            the descrizioneNotaAutomaticaSomministrazione to set
     */
    public void setDescrizioneNotaAutomaticaSomministrazione(String descrizioneNotaAutomaticaSomministrazione) {
        this.descrizioneNotaAutomaticaSomministrazione = descrizioneNotaAutomaticaSomministrazione;
    }

    /**
     * @param gestioneLottiAutomatici
     *            the gestioneLottiAutomatici to set
     */
    public void setGestioneLottiAutomatici(boolean gestioneLottiAutomatici) {
        this.gestioneLottiAutomatici = gestioneLottiAutomatici;
    }

    /**
     * @param numeratoreBarCode
     *            the numeratoreBarCode to set
     */
    public void setNumeratoreBarCode(String numeratoreBarCode) {
        this.numeratoreBarCode = numeratoreBarCode;
    }

    /**
     * @param nuovoDaUltimoLotto
     *            The nuovoDaUltimoLotto to set.
     */
    public void setNuovoDaUltimoLotto(boolean nuovoDaUltimoLotto) {
        this.nuovoDaUltimoLotto = nuovoDaUltimoLotto;
    }

    /**
     * @param ordinamentoFatturazione
     *            The ordinamentoFatturazione to set.
     */
    public void setOrdinamentoFatturazione(OrdinamentoFatturazione ordinamentoFatturazione) {
        this.ordinamentoFatturazione = ordinamentoFatturazione;
    }

    /**
     * @param riportaTestateOrdineInFatturazione
     *            the riportaTestateOrdineInFatturazione to set
     */
    public void setRiportaTestateOrdineInFatturazione(boolean riportaTestateOrdineInFatturazione) {
        this.riportaTestateOrdineInFatturazione = riportaTestateOrdineInFatturazione;
    }

    /**
     * @param sogliaAddebitoDichiarazione
     *            the sogliaAddebitoDichiarazione to set
     */
    public void setSogliaAddebitoDichiarazione(List<SogliaAddebitoDichiarazioneSettings> sogliaAddebitoDichiarazione) {
        this.sogliaAddebitoDichiarazione = sogliaAddebitoDichiarazione;
    }

}
