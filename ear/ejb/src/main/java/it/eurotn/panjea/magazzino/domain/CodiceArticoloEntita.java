package it.eurotn.panjea.magazzino.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.envers.Audited;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;

/**
 *
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
@Entity
@Audited
@Table(name = "maga_codici_articolo_entita", uniqueConstraints = @UniqueConstraint(columnNames = { "codice",
        "entita_id", "articolo_id" }) )
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "datiCommerciali")
@NamedQueries({
        @NamedQuery(name = "CodiceArticoloEntita.caricaByArticolo", query = "select cae from CodiceArticoloEntita cae where cae.articolo.id=:paramIdArticolo"),
        @NamedQuery(name = "CodiceArticoloEntita.caricaByEntita", query = "select cae from CodiceArticoloEntita cae where cae.entita.id = :paramIdEntita"),
        @NamedQuery(name = "CodiceArticoloEntita.caricaByArticoloEEntita", query = "select cae from CodiceArticoloEntita cae where cae.articolo.id=:paramIdArticolo and cae.entita.id = :paramIdEntita"),
        @NamedQuery(name = "CodiceArticoloEntita.caricaConsegnaContoTerziByArticolo", query = "select cae from CodiceArticoloEntita cae where cae.consegnaContoTerzi = true and cae.articolo.id = :paramIdArticolo and cae.entita.class = FornitoreLite "),
        @NamedQuery(name = "CodiceArticoloEntita.caricaByArticoloEEntitaPrincipale", query = "select cae from CodiceArticoloEntita cae where cae.entitaPrincipale = true and cae.articolo.id = :paramIdArticolo ") })
public class CodiceArticoloEntita extends EntityBase {

    private static final long serialVersionUID = 3809569654617929024L;

    @Column(length = 30, nullable = false)
    private String codice;

    @Column(length = 13)
    private String barCode;

    @Column(length = 30)
    private String barCode2;

    @Column(length = 100)
    private String descrizione;

    private Integer leadTime;

    private Integer ggSicurezza;

    private Double qtaMinimaOrdinabile;

    private Double lottoRiordino;

    @ManyToOne
    @JoinColumn(name = "entita_id", nullable = false)
    private EntitaLite entita;

    @ManyToOne
    @JoinColumn(name = "articolo_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Articolo articolo;

    private boolean consegnaContoTerzi;

    /**
     * Indica se l'entità e l'entità principale per il quale acquisto/vendo.
     */
    private boolean entitaPrincipale;

    /**
     * Costruttore.
     */
    public CodiceArticoloEntita() {
        super();
        initialize();
    }

    /**
     * @return the articolo
     */
    public Articolo getArticolo() {
        return articolo;
    }

    /**
     * @return the barCode
     */
    public String getBarCode() {
        return barCode;
    }

    /**
     * @return the barCode2
     */
    public String getBarCode2() {
        return barCode2;
    }

    /**
     * @return the codice
     */
    public String getCodice() {
        return codice;
    }

    /**
     * @return the consegnaContoTerzi
     */
    public Boolean getConsegnaContoTerzi() {
        return consegnaContoTerzi;
    }

    /**
     * @return the descrizione
     */
    public String getDescrizione() {
        return descrizione;
    }

    /**
     * @return the entita
     */
    public EntitaLite getEntita() {
        return entita;
    }

    /**
     * @return Returns the ggSicurezza.
     */
    public Integer getGgSicurezza() {
        return ggSicurezza;
    }

    /**
     * @return Returns the leadTime.
     */
    public Integer getLeadTime() {
        return leadTime;
    }

    /**
     * @return Returns the lottoRiordino.
     */
    public Double getLottoRiordino() {
        return lottoRiordino;
    }

    /**
     * @return Returns the qtaMinimaOrdinabile.
     */
    public Double getQtaMinimaOrdinabile() {
        return qtaMinimaOrdinabile;
    }

    /**
     * Init degli attributi di this.
     */
    private void initialize() {
        this.consegnaContoTerzi = Boolean.FALSE;
        this.leadTime = 0;
        this.lottoRiordino = 0.0;
        this.qtaMinimaOrdinabile = 0.0;
    }

    /**
     * @return Returns the entitaPrincipale.
     */
    public boolean isEntitaPrincipale() {
        return entitaPrincipale;
    }

    /**
     * @param articolo
     *            the articolo to set
     */
    public void setArticolo(Articolo articolo) {
        this.articolo = articolo;
    }

    /**
     * @param barCode
     *            the barCode to set
     */
    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    /**
     * @param barCode2
     *            the barCode2 to set
     */
    public void setBarCode2(String barCode2) {
        this.barCode2 = barCode2;
    }

    /**
     * @param codice
     *            the codice to set
     */
    public void setCodice(String codice) {
        this.codice = codice;
    }

    /**
     * @param consegnaContoTerzi
     *            the consegnaContoTerzi to set
     */
    public void setConsegnaContoTerzi(Boolean consegnaContoTerzi) {
        this.consegnaContoTerzi = consegnaContoTerzi;
    }

    /**
     * @param descrizione
     *            the descrizione to set
     */
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    /**
     * @param entita
     *            the entita to set
     */
    public void setEntita(EntitaLite entita) {
        this.entita = entita;
    }

    /**
     * @param entitaPrincipale
     *            The entitaPrincipale to set.
     */
    public void setEntitaPrincipale(boolean entitaPrincipale) {
        this.entitaPrincipale = entitaPrincipale;
    }

    /**
     * @param ggSicurezza
     *            The ggSicurezza to set.
     */
    public void setGgSicurezza(Integer ggSicurezza) {
        this.ggSicurezza = ggSicurezza;
    }

    /**
     * @param leadTime
     *            The leadTime to set.
     */
    public void setLeadTime(Integer leadTime) {
        this.leadTime = leadTime;
    }

    /**
     * @param lottoRiordino
     *            The lottoRiordino to set.
     */
    public void setLottoRiordino(Double lottoRiordino) {
        this.lottoRiordino = lottoRiordino;
    }

    /**
     * @param qtaMinimaOrdinabile
     *            The qtaMinimaOrdinabile to set.
     */
    public void setQtaMinimaOrdinabile(Double qtaMinimaOrdinabile) {
        this.qtaMinimaOrdinabile = qtaMinimaOrdinabile;
    }
}
