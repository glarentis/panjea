package it.eurotn.panjea.magazzino.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.FilterDefs;
import org.hibernate.annotations.Filters;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.ParamDef;
import org.hibernate.envers.AuditJoinTable;
import org.hibernate.envers.Audited;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.anagrafica.domain.CodiceIva;
import it.eurotn.panjea.anagrafica.domain.FaseLavorazioneArticolo;
import it.eurotn.panjea.anagrafica.domain.UnitaMisura;
import it.eurotn.panjea.conai.domain.ConaiComponente;
import it.eurotn.panjea.intra.domain.DatiArticoloIntra;
import it.eurotn.panjea.lotti.domain.Lotto;
import it.eurotn.panjea.magazzino.domain.descrizionilingua.DescrizioneLinguaArticolo;
import it.eurotn.panjea.magazzino.domain.descrizionilingua.DescrizioneLinguaArticoloEstesa;
import it.eurotn.panjea.magazzino.domain.descrizionilingua.IDescrizioneLingua;
import it.eurotn.panjea.magazzino.util.ArticoloRicerca;
import it.eurotn.panjea.ordini.domain.documento.TipoAreaOrdine;
import it.eurotn.util.PanjeaEJBUtil;

/**
 * Classe di dominio Articolo.<br>
 *
 * @author adriano
 * @version 1.0, 16/set/2008
 */
@Entity
@Audited
@Table(name = "maga_articoli", uniqueConstraints = @UniqueConstraint(columnNames = { "codiceazienda", "codice",
        "TIPO" }) )
@org.hibernate.annotations.Table(appliesTo = "maga_articoli", indexes = {
        @Index(name = "IdxCodice", columnNames = { "codiceazienda", "codice" }),
        @Index(name = "IdxBarCode", columnNames = { "barCode" }),
        @Index(name = "IdxDescrizione", columnNames = { "descrizioneLinguaAziendale" }) })
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TIPO", discriminatorType = DiscriminatorType.STRING, length = 2)
@DiscriminatorValue("A")
@NamedQueries({
        @NamedQuery(name = "Articolo.caricaUltimoCosto", query = "select r.prezzoNetto.importoInValutaAzienda,r.areaMagazzino.id,r.areaMagazzino.documento.codice,r.areaMagazzino.documento.dataDocumento,r.numeroDecimaliPrezzo from RigaArticolo r where r.prezzoNetto.importoInValutaAzienda<>0 and r.areaMagazzino.tipoAreaMagazzino.aggiornaCostoUltimo=true and r.articolo.id= :articolo_id and  r.areaMagazzino.dataRegistrazione<= :dataRegistrazione order by  r.areaMagazzino.dataRegistrazione desc"),
        @NamedQuery(name = "Articolo.caricaUltimoCostoDeposito", query = "select r.prezzoNetto.importoInValutaAzienda,r.areaMagazzino.id,r.areaMagazzino.documento.codice,r.areaMagazzino.documento.dataDocumento,r.numeroDecimaliPrezzo from RigaArticolo r where r.prezzoNetto.importoInValutaAzienda<>0 and r.areaMagazzino.tipoAreaMagazzino.aggiornaCostoUltimo=true and r.articolo.id= :articolo_id and r.areaMagazzino.depositoOrigine.id = :deposito_id and  r.areaMagazzino.dataRegistrazione<= :dataRegistrazione order by  r.areaMagazzino.dataRegistrazione desc"),
        @NamedQuery(name = "Articolo.caricaByCodice", query = "select art from Articolo art where art.codice=:codice"),
        @NamedQuery(name = "Articolo.numeroComponenti", query = "select count(*) from Componente c where c.distinta=:articolo"),
        @NamedQuery(name = "Articolo.caricaCodiceDescrizioneById", query = "select a.codice,a.descrizioneLinguaAziendale from Articolo a where a.id=:idArticolo"),
        @NamedQuery(name = "Articolo.caricaCodiceDescrizione", query = "select a.codice,a.descrizioneLinguaAziendale,id from Articolo a where a.abilitato=true"),
        @NamedQuery(name = "Articolo.aggiornaArticoliAlternativiFiltro", query = "update Articolo a set a.articoliAlternativiFiltro=:filtro where a.id=:articolo") })
@FilterDefs({ @FilterDef(name = "configurazioneDistintaBase"),
        @FilterDef(name = "configurazioneDistinta", parameters = @ParamDef(name = "idConfigurazione", type = "integer") ) })
public class Articolo extends EntityBase implements IDescrizioneFactory, IDescrizioneEstesaFactory, Cloneable {

    public enum ETipoArticolo {
        FISICO, SERVIZI, ACCESSORI, SPESE_TRASPORTO, SPESE_ALTRE
    }

    public enum TipoLotto {
        NESSUNO, LOTTO, LOTTO_INTERNO
    }

    public static final String ATTRIBUTO_TRASMETTI_ATTRIBUTI = "TRASATTR";

    private static final long serialVersionUID = -2975227463669533696L;

    /**
     * indica se il lotto può essere tralasciato.
     */
    private boolean lottoFacoltativo;

    private Integer leadTime;

    @Column(length = 10, nullable = false)
    @Index(name = "index_codiceAzienda")
    private String codiceAzienda = null;

    private ETipoArticolo tipoArticolo;

    private ProvenienzaPrezzoArticolo provenienzaPrezzoArticolo;

    private boolean articoloLibero;

    private boolean distinta;

    private String articoliAlternativiFiltro;

    /**
     * campo utilizzato per gestioni particolari.(che schifo).
     */
    @Column(length = 10)
    private String campoLibero;

    private Double resa; // per il vending
    private boolean somministrazione;

    @Embedded
    private DatiArticoloIntra datiIntra;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, mappedBy = "articolo")
    private Set<CodiceArticoloEntita> codiciEntita;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, mappedBy = "articolo")
    private Set<ArticoloAlternativo> articoliAlternativi;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, mappedBy = "distinta")
    @Filters({ @Filter(name = "configurazioneDistintaBase", condition = "configurazioneDistinta_id is null"),
            @Filter(name = "configurazioneDistinta", condition = "configurazioneDistinta_id =:idConfigurazione") })
    @OrderBy(value = "ordinamento")
    private Set<Componente> componenti;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, mappedBy = "articolo")
    private Set<ConaiComponente> componentiConai;

    @ManyToOne
    private CategoriaContabileArticolo categoriaContabileArticolo;

    @ManyToOne
    private CategoriaCommercialeArticolo categoriaCommercialeArticolo;

    @ManyToOne
    private CategoriaCommercialeArticolo categoriaCommercialeArticolo2;

    @Column(length = 30)
    private String codice;

    @Column(length = 20)
    private String barCode;

    @Column(length = 30)
    private String codiceInterno;

    /*
     * definisce la mask edit per l'input della quantità
     */
    @Column(length = 30)
    private String mascheraInput;

    private boolean abilitato;

    private Integer numeroDecimaliPrezzo;

    private boolean ivaAlternativa;

    /**
     * descrizione in lingua aziendale.
     */
    @Column(length = 100)
    private String descrizioneLinguaAziendale;

    @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
    @MapKey(name = "codiceLingua")
    @Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    @Fetch(FetchMode.JOIN)
    private Map<String, DescrizioneLinguaArticolo> descrizioniLingua;

    @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
    @MapKey(name = "codiceLingua")
    @Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    @Fetch(FetchMode.JOIN)
    private Map<String, DescrizioneLinguaArticoloEstesa> descrizioniLinguaEstesa;

    @ManyToOne(fetch = FetchType.LAZY)
    private FormulaTrasformazione formulaTrasformazioneQta;

    @ManyToOne(fetch = FetchType.LAZY)
    private FormulaTrasformazione formulaTrasformazioneQtaMagazzino;

    @Column(length = 1000)
    private String note;

    @ManyToOne
    private UnitaMisura unitaMisura;

    /**
     * unità di misura per la qta di magazzino.
     */
    @ManyToOne
    private UnitaMisura unitaMisuraQtaMagazzino;

    @ManyToOne(fetch = FetchType.LAZY)
    private CodiceIva codiceIva;

    @ManyToOne(fetch = FetchType.LAZY)
    @Fetch(FetchMode.SELECT)
    private Categoria categoria;

    @ManyToOne(fetch = FetchType.LAZY)
    private CategoriaScontoArticolo categoriaScontoArticolo;

    @ManyToOne(fetch = FetchType.LAZY)
    private TipoAreaOrdine tipoAreaOrdine;

    @OneToMany(cascade = { CascadeType.ALL }, mappedBy = "articolo")
    @org.hibernate.annotations.Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    @OnDelete(action = OnDeleteAction.CASCADE)
    // @JoinColumn(name = "articolo_id")
    @OrderBy("riga,ordine")
    @AuditJoinTable(name = "articolo_attributoarticolo_aud")
    private List<AttributoArticolo> attributiArticolo;

    private boolean mrp;

    @Column(nullable = false)
    protected Integer numeroDecimaliQta;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "articolo")
    @Fetch(FetchMode.SELECT)
    private Set<TipoMezzoTrasporto> tipiMezzoTrasporto;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "articolo")
    @Fetch(FetchMode.SELECT)
    private Set<CodiceArticoloEntita> codiciArticoloEntita;

    private String codiceLinguaAzienda;

    @OneToMany(mappedBy = "articolo")
    private List<Lotto> lotti;

    private TipoLotto tipoLotto;

    private boolean stampaLotti;

    @Column(length = 50)
    private String posizione;

    /**
     * Indica se l'articolo deve essere considerato per la gestione delle schede articolo.
     */
    private boolean gestioneSchedaArticolo;

    private Integer gestioneSchedaArticoloAnno;

    private Integer gestioneSchedaArticoloMese;

    private BigDecimal costoStandard;

    private boolean gestioneQuantitaZero;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, mappedBy = "articolo")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @OrderBy(value = "ordinamento")
    private Set<FaseLavorazioneArticolo> fasiLavorazioneArticolo;

    private boolean produzione;

    {
        this.abilitato = true;
        this.codice = "";
        this.descrizioneLinguaAziendale = "";
        this.descrizioniLingua = new TreeMap<String, DescrizioneLinguaArticolo>();
        this.descrizioniLinguaEstesa = new TreeMap<String, DescrizioneLinguaArticoloEstesa>();
        this.attributiArticolo = new ArrayList<AttributoArticolo>();
        this.tipiMezzoTrasporto = new TreeSet<TipoMezzoTrasporto>();
        this.codiciArticoloEntita = new TreeSet<CodiceArticoloEntita>();
        this.tipoArticolo = ETipoArticolo.FISICO;
        this.provenienzaPrezzoArticolo = ProvenienzaPrezzoArticolo.DOCUMENTO;
        this.numeroDecimaliPrezzo = 0;
        this.distinta = false;
        this.componenti = new TreeSet<Componente>();
        this.articoliAlternativi = new TreeSet<ArticoloAlternativo>();
        this.fasiLavorazioneArticolo = new TreeSet<FaseLavorazioneArticolo>();
        this.tipoLotto = TipoLotto.NESSUNO;
        this.stampaLotti = true;
        this.gestioneSchedaArticolo = false;
        this.gestioneQuantitaZero = false;
        this.produzione = false;
        this.somministrazione = false;
    }

    /**
     * Costruttore.
     */
    public Articolo() {
        super();
    }

    /**
     * Agguinge un attributo all'articolo.
     *
     * @param attributoArticolo
     *            attributo da aggiungere
     */
    public void addAttributoArticolo(AttributoArticolo attributoArticolo) {
        if (attributiArticolo == null) {
            attributiArticolo = new ArrayList<AttributoArticolo>();
        }
        attributiArticolo.add(attributoArticolo);
    }

    /**
     * Aggiunge una descrizione all'articolo.
     *
     * @param lingua
     *            descrizione da aggiungere
     */
    public void addDescrizione(DescrizioneLinguaArticolo lingua) {
        if (descrizioniLingua == null) {
            descrizioniLingua = new HashMap<String, DescrizioneLinguaArticolo>();
        }
        descrizioniLingua.put(lingua.getCodiceLingua(), lingua);
    }

    /**
     * Aggiunge una descrizione estesa all'articolo.
     *
     * @param lingua
     *            descrizione da aggiungere
     */
    public void addDescrizioneEstesa(DescrizioneLinguaArticoloEstesa lingua) {
        if (descrizioniLinguaEstesa == null) {
            descrizioniLinguaEstesa = new HashMap<String, DescrizioneLinguaArticoloEstesa>();
        }
        descrizioniLinguaEstesa.put(lingua.getCodiceLingua(), lingua);
    }

    @Override
    public Object clone() {
        Articolo cloneArticolo = PanjeaEJBUtil.cloneObject(this);
        cloneArticolo.setId(null);
        cloneArticolo.setBarCode(null);
        cloneArticolo.setCodice(cloneArticolo.getCodice() + "copia");
        cloneArticolo.setVersion(null);
        for (AttributoArticolo attributo : cloneArticolo.attributiArticolo) {
            attributo.setId(null);
            attributo.setVersion(null);
        }
        for (Entry<String, DescrizioneLinguaArticolo> item : cloneArticolo.getDescrizioniLingua().entrySet()) {
            item.getValue().setId(null);
        }

        for (Entry<String, DescrizioneLinguaArticoloEstesa> item : cloneArticolo.getDescrizioniLinguaEstesa()
                .entrySet()) {
            item.getValue().setId(null);
        }
        cloneArticolo.setCodiciArticoloEntita(new HashSet<CodiceArticoloEntita>());
        cloneArticolo.setCodiciEntita(new HashSet<CodiceArticoloEntita>());
        cloneArticolo.setLotti(new ArrayList<Lotto>());
        cloneArticolo.setTipiMezzoTrasporto(new HashSet<TipoMezzoTrasporto>());
        return cloneArticolo;
    }

    @Override
    public IDescrizioneLingua createDescrizioneLingua() {
        return new DescrizioneLinguaArticolo();
    }

    @Override
    public IDescrizioneLingua createDescrizioneLinguaEstesa() {
        return new DescrizioneLinguaArticoloEstesa();
    }

    /**
     * @return the articoliAlternativi
     */
    public Set<ArticoloAlternativo> getArticoliAlternativi() {
        return articoliAlternativi;
    }

    /**
     * @return Returns the articoliAlternativiFiltro.
     */
    public String getArticoliAlternativiFiltro() {
        return articoliAlternativiFiltro;
    }

    /**
     * Crea un {@link ArticoloLite} partendo dall'articolo.
     *
     * @return {@link ArticoloLite}
     */
    public ArticoloLite getArticoloLite() {
        ArticoloLite articoloLite = new ArticoloLite();
        articoloLite.setId(getId());
        articoloLite.setCodice(codice);
        articoloLite.setAbilitato(abilitato);
        articoloLite.setCodiceAzienda(codiceAzienda);
        articoloLite.setVersion(getVersion());
        articoloLite.setDescrizione(descrizioneLinguaAziendale);
        articoloLite.setTipoLotto(tipoLotto);
        articoloLite.setStampaLotti(stampaLotti);
        articoloLite.setArticoloLibero(isArticoloLibero());
        articoloLite.setBarCode(barCode);
        articoloLite.setNumeroDecimaliPrezzo(numeroDecimaliPrezzo);
        articoloLite.setNumeroDecimaliQta(numeroDecimaliQta);
        articoloLite.setLottoFacoltativo(isLottoFacoltativo());
        articoloLite.setUnitaMisura(unitaMisura);
        articoloLite.setDistinta(distinta);
        return articoloLite;
    }

    /**
     * @return l'articoloRicerca che rappresenta this
     */
    public ArticoloRicerca getArticoloRicerca() {
        ArticoloRicerca articoloRicerca = new ArticoloRicerca();
        articoloRicerca.setId(getId());
        articoloRicerca.setVersion(getVersion());
        articoloRicerca.setCodice(codice);
        articoloRicerca.setAbilitato(abilitato);
        articoloRicerca.setBarCode(barCode);
        articoloRicerca.setDescrizione(descrizioneLinguaAziendale);
        articoloRicerca.setNumeroDecimaliPrezzo(numeroDecimaliPrezzo);
        articoloRicerca.setNumeroDecimaliQta(numeroDecimaliQta);
        articoloRicerca.setProvenienzaPrezzoArticolo(provenienzaPrezzoArticolo);
        articoloRicerca.setTipoLotto(tipoLotto);
        articoloRicerca.setUnitaMisura(unitaMisura);
        articoloRicerca.setDistinta(distinta);
        return articoloRicerca;
    }

    /**
     * @return attributiArticolo
     */
    public List<AttributoArticolo> getAttributiArticolo() {
        return attributiArticolo;
    }

    /**
     *
     * @param codiceAttributo
     *            codice dell'attributo trovato
     * @return attributo o null se l'lattributo non è stato trovato
     */
    public AttributoArticolo getAttributo(String codiceAttributo) {
        for (AttributoArticolo attributo : attributiArticolo) {
            if (attributo.getTipoAttributo().getCodice().equals(codiceAttributo)) {
                return attributo;
            }
        }
        return null;
    }

    /**
     * @return barCode
     */
    public String getBarCode() {
        return barCode;
    }

    /**
     * @return Returns the campoLibero.
     */
    public String getCampoLibero() {
        return campoLibero;
    }

    /**
     * @return categoria
     */
    public Categoria getCategoria() {
        return categoria;
    }

    /**
     * @return Returns the categoriaCommercialeArticolo.
     */
    public CategoriaCommercialeArticolo getCategoriaCommercialeArticolo() {
        return categoriaCommercialeArticolo;
    }

    /**
     * @return the categoriaCommercialeArticolo2
     */
    public CategoriaCommercialeArticolo getCategoriaCommercialeArticolo2() {
        return categoriaCommercialeArticolo2;
    }

    /**
     * @return categoriaContabileArticolo
     */
    public CategoriaContabileArticolo getCategoriaContabileArticolo() {
        return categoriaContabileArticolo;
    }

    /**
     * @return categoriaScontoArticolo
     */
    public CategoriaScontoArticolo getCategoriaScontoArticolo() {
        return categoriaScontoArticolo;
    }

    /**
     * @return codice
     */
    public String getCodice() {
        return codice;
    }

    /**
     * @return codiceAzienda
     */
    public String getCodiceAzienda() {
        return codiceAzienda;
    }

    /**
     * @return the codiceInterno
     */
    public String getCodiceInterno() {
        return codiceInterno;
    }

    /**
     * @return codiceIva
     */
    public CodiceIva getCodiceIva() {
        return codiceIva;
    }

    /**
     * @return Returns the codiceLinguaAzienda.
     */
    public String getCodiceLinguaAzienda() {
        return codiceLinguaAzienda;
    }

    /**
     * @return the codiciArticoloEntita
     */
    public Set<CodiceArticoloEntita> getCodiciArticoloEntita() {
        return codiciArticoloEntita;
    }

    /**
     * @return Returns the codiciEntita.
     */
    public Set<CodiceArticoloEntita> getCodiciEntita() {
        return codiciEntita;
    }

    /**
     * @return Returns the componenti.
     */
    public Set<Componente> getComponenti() {
        return componenti;
    }

    /**
     * @return the componentiConai
     */
    public Set<ConaiComponente> getComponentiConai() {
        return componentiConai;
    }

    /**
     * @return the costoStandard
     */
    public BigDecimal getCostoStandard() {
        return costoStandard;
    }

    /**
     * @return Returns the datiIntra.
     */
    public DatiArticoloIntra getDatiIntra() {
        if (datiIntra == null) {
            datiIntra = new DatiArticoloIntra();
        }
        return datiIntra;
    }

    /**
     * @return descrizioneLinguaAziendale
     */
    public String getDescrizione() {
        return descrizioneLinguaAziendale;
    }

    /**
     * Restituisce la descrizione della lingua richiesta, se non esiste restituisce quella
     * aziendale.
     *
     * @param codiceLingua
     *            codice della lingua
     * @return descrizione
     */
    public String getDescrizione(String codiceLingua) {
        DescrizioneLinguaArticolo desc = descrizioniLingua.get(codiceLingua);

        if (desc == null) {
            return getDescrizione();
        } else {
            return desc.getDescrizione();
        }
    }

    /**
     * @return the descrizioneLinguaAziendale
     */
    public String getDescrizioneLinguaAziendale() {
        return descrizioneLinguaAziendale;
    }

    /**
     * @return descrizioniLingua
     */
    public Map<String, DescrizioneLinguaArticolo> getDescrizioniLingua() {
        return descrizioniLingua;
    }

    /**
     * @return descrizioniLinguaEstesa
     */
    public Map<String, DescrizioneLinguaArticoloEstesa> getDescrizioniLinguaEstesa() {
        return descrizioniLinguaEstesa;
    }

    /**
     * @return Returns the fasiLavorazioneArticolo.
     */
    public Set<FaseLavorazioneArticolo> getFasiLavorazioneArticolo() {
        return fasiLavorazioneArticolo;
    }

    /**
     * @return formulaTrasformazioneQta
     */
    public FormulaTrasformazione getFormulaTrasformazioneQta() {
        return formulaTrasformazioneQta;
    }

    /**
     * @return formulaTrasformazioneQtaMagazzino
     */
    public FormulaTrasformazione getFormulaTrasformazioneQtaMagazzino() {
        return formulaTrasformazioneQtaMagazzino;
    }

    /**
     * @return the gestioneSchedaArticoloAnno
     */
    public Integer getGestioneSchedaArticoloAnno() {
        return gestioneSchedaArticoloAnno;
    }

    /**
     * @return the gestioneSchedaArticoloMese
     */
    public Integer getGestioneSchedaArticoloMese() {
        return gestioneSchedaArticoloMese;
    }

    /**
     * @return Returns the leadTime.
     */
    public Integer getLeadTime() {
        return leadTime;
    }

    /**
     * @return the lotti
     */
    public List<Lotto> getLotti() {
        return lotti;
    }

    /**
     * @return mascheraInput
     */
    public String getMascheraInput() {
        return mascheraInput;
    }

    /**
     * @return note
     */
    public String getNote() {
        return note;
    }

    /**
     * @return the numeroDecimaliPrezzo
     */
    public Integer getNumeroDecimaliPrezzo() {
        return numeroDecimaliPrezzo;
    }

    /**
     * @return numeroDecimaliQta
     */
    public Integer getNumeroDecimaliQta() {
        return numeroDecimaliQta;
    }

    /**
     * @return Returns the posizione.
     */
    public String getPosizione() {
        return posizione;
    }

    /**
     * @return the provenienzaPrezzoArticolo
     */
    public ProvenienzaPrezzoArticolo getProvenienzaPrezzoArticolo() {
        return provenienzaPrezzoArticolo;
    }

    /**
     * @return Returns the resa.
     */
    public Double getResa() {
        return resa;
    }

    /**
     * @return the tipiMezzoTrasporto
     */
    public Set<TipoMezzoTrasporto> getTipiMezzoTrasporto() {
        return tipiMezzoTrasporto;
    }

    /**
     * @return Returns the tipoAreaOrdine.
     */
    public TipoAreaOrdine getTipoAreaOrdine() {
        return tipoAreaOrdine;
    }

    /**
     * @return tipoArticolo
     */
    public ETipoArticolo getTipoArticolo() {
        return tipoArticolo;
    }

    /**
     * @return the tipoLotto
     */
    public TipoLotto getTipoLotto() {
        return tipoLotto;
    }

    /**
     * @return unitaMisura
     */
    public UnitaMisura getUnitaMisura() {
        return unitaMisura;
    }

    /**
     * @return unitaMisuraQtaMagazzino
     */
    public UnitaMisura getUnitaMisuraQtaMagazzino() {
        return unitaMisuraQtaMagazzino;
    }

    /**
     *
     * @param codiceAttributo
     *            codice dell.attributo.
     * @return valore dell'attributo
     */
    public String getValoreAttributo(String codiceAttributo) {
        return getValoreAttributoTipizzato(codiceAttributo, String.class);
    }

    /**
     * @param codiceAttributo
     *            il codice dell'attributo da recuperare
     * @param returnType
     *            tipo richiesto
     * @param <T>
     *            tipo
     * @return the valore tipizzato
     */
    public <T> T getValoreAttributoTipizzato(String codiceAttributo, Class<T> returnType) {
        T valoreAttr = null;
        for (AttributoArticolo attributo : attributiArticolo) {
            if (codiceAttributo.equals(attributo.getTipoAttributo().getCodice())) {
                valoreAttr = attributo.getValoreTipizzato(returnType);
            }
        }
        return valoreAttr;
    }

    /**
     * @return true se l'articolo ha dei componenti conai, false altrimenti
     */
    public boolean hasComponentiConai() {
        return componentiConai != null && !componentiConai.isEmpty();
    }

    /**
     * @return abilitato
     */
    public boolean isAbilitato() {
        return abilitato;
    }

    /**
     * @return articoloLibero
     */
    public boolean isArticoloLibero() {
        return articoloLibero;
    }

    /**
     * @return Returns the disinta.
     */
    public boolean isDistinta() {
        return distinta;
    }

    /**
     * @return the gestioneQuantitaZero
     */
    public boolean isGestioneQuantitaZero() {
        return gestioneQuantitaZero;
    }

    /**
     * @return the gestioneSchedaArticolo
     */
    public boolean isGestioneSchedaArticolo() {
        return gestioneSchedaArticolo;
    }

    /**
     * @return ivaAlternativa
     */
    public boolean isIvaAlternativa() {
        return ivaAlternativa;
    }

    /**
     * @return Returns the lottoFacoltativo.
     */
    public boolean isLottoFacoltativo() {
        return lottoFacoltativo;
    }

    /**
     * @return Returns the mrp.
     */
    public boolean isMrp() {
        return mrp;
    }

    /**
     * @return the produzione
     */
    public boolean isProduzione() {
        return produzione;
    }

    /**
     * @return the somministrazione
     */
    public final boolean isSomministrazione() {
        return somministrazione;
    }

    /**
     * @return the stampaLotti
     */
    public boolean isStampaLotti() {
        return stampaLotti;
    }

    /**
     * @param abilitato
     *            the abilitato to set
     */
    public void setAbilitato(boolean abilitato) {
        this.abilitato = abilitato;
    }

    /**
     * @param articoliAlternativiFiltro
     *            The articoliAlternativiFiltro to set.
     */
    public void setArticoliAlternativiFiltro(String articoliAlternativiFiltro) {
        this.articoliAlternativiFiltro = articoliAlternativiFiltro;
    }

    /**
     * @param articoloLibero
     *            the articoloLibero to set
     */
    public void setArticoloLibero(boolean articoloLibero) {
        this.articoloLibero = articoloLibero;
    }

    /**
     * @param attributiArticolo
     *            the attributiArticolo to set
     */
    public void setAttributiArticolo(List<AttributoArticolo> attributiArticolo) {
        this.attributiArticolo = attributiArticolo;
    }

    /**
     * @param barCode
     *            the barCode to set
     */
    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    /**
     * @param campoLibero
     *            The campoLibero to set.
     */
    public void setCampoLibero(String campoLibero) {
        this.campoLibero = campoLibero;
    }

    /**
     * @param categoria
     *            the categoria to set
     */
    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
        if (codice.isEmpty() && categoria != null) {
            codice = categoria.getGenerazioneCodiceArticoloData().getMascheraCodiceArticolo();
        }
        if (descrizioneLinguaAziendale.isEmpty() && categoria != null) {
            descrizioneLinguaAziendale = categoria.getGenerazioneCodiceArticoloData().getMascheraDescrizioneArticolo();
        }

    }

    /**
     * @param categoriaCommercialeArticolo
     *            The categoriaCommercialeArticolo to set.
     */
    public void setCategoriaCommercialeArticolo(CategoriaCommercialeArticolo categoriaCommercialeArticolo) {
        this.categoriaCommercialeArticolo = categoriaCommercialeArticolo;
    }

    /**
     * @param categoriaCommercialeArticolo2
     *            the categoriaCommercialeArticolo2 to set
     */
    public void setCategoriaCommercialeArticolo2(CategoriaCommercialeArticolo categoriaCommercialeArticolo2) {
        this.categoriaCommercialeArticolo2 = categoriaCommercialeArticolo2;
    }

    /**
     * @param categoriaContabileArticolo
     *            the categoriaContabileArticolo to set
     */
    public void setCategoriaContabileArticolo(CategoriaContabileArticolo categoriaContabileArticolo) {
        this.categoriaContabileArticolo = categoriaContabileArticolo;
    }

    /**
     * @param categoriaScontoArticolo
     *            the categoriaScontoArticolo to set
     */
    public void setCategoriaScontoArticolo(CategoriaScontoArticolo categoriaScontoArticolo) {
        this.categoriaScontoArticolo = categoriaScontoArticolo;
    }

    /**
     * @param codice
     *            the codice to set
     */
    public void setCodice(String codice) {
        this.codice = codice;
    }

    /**
     * @param codiceAzienda
     *            the codiceAzienda to set
     */
    public void setCodiceAzienda(String codiceAzienda) {
        this.codiceAzienda = codiceAzienda;
    }

    /**
     * @param codiceInterno
     *            the codiceInterno to set
     */
    public void setCodiceInterno(String codiceInterno) {
        this.codiceInterno = codiceInterno;
    }

    /**
     * @param codiceIva
     *            the codiceIva to set
     */
    public void setCodiceIva(CodiceIva codiceIva) {
        this.codiceIva = codiceIva;
    }

    /**
     * @param codiceLinguaAzienda
     *            codice della lingua selezionata dall'utente. <br/>
     *            se settata lavora correttamente {@link Articolo#getDescrizione()} e
     *            {@link Articolo#setDescrizione()}
     */
    public void setCodiceLinguaAzienda(String codiceLinguaAzienda) {
        this.codiceLinguaAzienda = codiceLinguaAzienda;
    }

    /**
     * @param codiciArticoloEntita
     *            the codiciArticoloEntita to set
     */
    public void setCodiciArticoloEntita(Set<CodiceArticoloEntita> codiciArticoloEntita) {
        this.codiciArticoloEntita = codiciArticoloEntita;
    }

    /**
     * @param codiciEntita
     *            The codiciEntita to set.
     */
    public void setCodiciEntita(Set<CodiceArticoloEntita> codiciEntita) {
        this.codiciEntita = codiciEntita;
    }

    /**
     * @param componenti
     *            The componenti to set.
     */
    public void setComponenti(Set<Componente> componenti) {
        this.componenti = componenti;
    }

    /**
     * @param costoStandard
     *            the costoStandard to set
     */
    public void setCostoStandard(BigDecimal costoStandard) {
        this.costoStandard = costoStandard;
    }

    /**
     * @param datiIntra
     *            The datiIntra to set.
     */
    public void setDatiIntra(DatiArticoloIntra datiIntra) {
        this.datiIntra = datiIntra;
    }

    /**
     * @param descrizione
     *            the descrizione to set
     */
    public void setDescrizione(String descrizione) {
        this.descrizioneLinguaAziendale = descrizione;
    }

    /**
     * @param descrizioniLingua
     *            the descrizioniLingua to set
     */
    public void setDescrizioniLingua(Map<String, DescrizioneLinguaArticolo> descrizioniLingua) {
        this.descrizioniLingua = descrizioniLingua;
    }

    /**
     * @param descrizioniLinguaEstesa
     *            the descrizioniLinguaEstesa to set
     */
    public void setDescrizioniLinguaEstesa(Map<String, DescrizioneLinguaArticoloEstesa> descrizioniLinguaEstesa) {
        this.descrizioniLinguaEstesa = descrizioniLinguaEstesa;
    }

    /**
     * @param distinta
     *            The disinta to set.
     */
    public void setDistinta(boolean distinta) {
        this.distinta = distinta;
    }

    /**
     * @param fasiLavorazioneArticolo
     *            The fasiLavorazioneArticolo to set.
     */
    public void setFasiLavorazioneArticolo(Set<FaseLavorazioneArticolo> fasiLavorazioneArticolo) {
        this.fasiLavorazioneArticolo = fasiLavorazioneArticolo;
    }

    /**
     * @param formulaTrasformazioneQta
     *            the formulaTrasformazioneQta to set
     */
    public void setFormulaTrasformazioneQta(FormulaTrasformazione formulaTrasformazioneQta) {
        this.formulaTrasformazioneQta = formulaTrasformazioneQta;
    }

    /**
     * @param formulaTrasformazioneQtaMagazzino
     *            the formulaTrasformazioneQtaMagazzino to set
     */
    public void setFormulaTrasformazioneQtaMagazzino(FormulaTrasformazione formulaTrasformazioneQtaMagazzino) {
        this.formulaTrasformazioneQtaMagazzino = formulaTrasformazioneQtaMagazzino;
    }

    /**
     * @param gestioneQuantitaZero
     *            the gestioneQuantitaZero to set
     */
    public void setGestioneQuantitaZero(boolean gestioneQuantitaZero) {
        this.gestioneQuantitaZero = gestioneQuantitaZero;
    }

    /**
     * @param gestioneSchedaArticolo
     *            the gestioneSchedaArticolo to set
     */
    public void setGestioneSchedaArticolo(boolean gestioneSchedaArticolo) {
        this.gestioneSchedaArticolo = gestioneSchedaArticolo;
    }

    /**
     * @param gestioneSchedaArticoloAnno
     *            the gestioneSchedaArticoloAnno to set
     */
    public void setGestioneSchedaArticoloAnno(Integer gestioneSchedaArticoloAnno) {
        this.gestioneSchedaArticoloAnno = gestioneSchedaArticoloAnno;
    }

    /**
     * @param gestioneSchedaArticoloMese
     *            the gestioneSchedaArticoloMese to set
     */
    public void setGestioneSchedaArticoloMese(Integer gestioneSchedaArticoloMese) {
        this.gestioneSchedaArticoloMese = gestioneSchedaArticoloMese;
    }

    /**
     * @param ivaAlternativa
     *            the ivaAlternativa to set
     */
    public void setIvaAlternativa(boolean ivaAlternativa) {
        this.ivaAlternativa = ivaAlternativa;
    }

    /**
     * @param leadTime
     *            The leadTime to set.
     */
    public void setLeadTime(Integer leadTime) {
        this.leadTime = leadTime;
    }

    /**
     * @param lotti
     *            the lotti to set
     */
    public void setLotti(List<Lotto> lotti) {
        this.lotti = lotti;
    }

    /**
     * @param lottoFacoltativo
     *            The lottoFacoltativo to set.
     */
    public void setLottoFacoltativo(boolean lottoFacoltativo) {
        this.lottoFacoltativo = lottoFacoltativo;
    }

    /**
     * @param mascheraInput
     *            the mascheraInput to set
     */
    public void setMascheraInput(String mascheraInput) {
        this.mascheraInput = mascheraInput;
    }

    /**
     * @param mrp
     *            The mrp to set.
     */
    public void setMrp(boolean mrp) {
        this.mrp = mrp;
    }

    /**
     * @param note
     *            the note to set
     */
    public void setNote(String note) {
        this.note = note;
    }

    /**
     * @param numeroDecimaliPrezzo
     *            the numeroDecimaliPrezzo to set
     */
    public void setNumeroDecimaliPrezzo(Integer numeroDecimaliPrezzo) {
        this.numeroDecimaliPrezzo = numeroDecimaliPrezzo;
    }

    /**
     * @param numeroDecimaliQta
     *            the numeroDecimaliQta to set
     */
    public void setNumeroDecimaliQta(Integer numeroDecimaliQta) {
        this.numeroDecimaliQta = numeroDecimaliQta;
    }

    /**
     * @param posizione
     *            The posizione to set.
     */
    public void setPosizione(String posizione) {
        this.posizione = posizione;
    }

    /**
     * @param produzione
     *            the produzione to set
     */
    public void setProduzione(boolean produzione) {
        this.produzione = produzione;
    }

    /**
     * @param provenienzaPrezzoArticolo
     *            the provenienzaPrezzoArticolo to set
     */
    public void setProvenienzaPrezzoArticolo(ProvenienzaPrezzoArticolo provenienzaPrezzoArticolo) {
        this.provenienzaPrezzoArticolo = provenienzaPrezzoArticolo;
    }

    /**
     * @param resa
     *            The resa to set.
     */
    public void setResa(Double resa) {
        this.resa = resa;
    }

    /**
     * @param somministrazione
     *            the somministrazione to set
     */
    public final void setSomministrazione(boolean somministrazione) {
        this.somministrazione = somministrazione;
    }

    /**
     * @param stampaLotti
     *            the stampaLotti to set
     */
    public void setStampaLotti(boolean stampaLotti) {
        this.stampaLotti = stampaLotti;
    }

    /**
     * @param tipiMezzoTrasporto
     *            the tipiMezzoTrasporto to set
     */
    public void setTipiMezzoTrasporto(Set<TipoMezzoTrasporto> tipiMezzoTrasporto) {
        this.tipiMezzoTrasporto = tipiMezzoTrasporto;
    }

    /**
     * @param tipoAreaOrdine
     *            The tipoAreaOrdine to set.
     */
    public void setTipoAreaOrdine(TipoAreaOrdine tipoAreaOrdine) {
        this.tipoAreaOrdine = tipoAreaOrdine;
    }

    /**
     * @param tipoArticolo
     *            the tipoArticolo to set
     */
    public void setTipoArticolo(ETipoArticolo tipoArticolo) {
        this.tipoArticolo = tipoArticolo;
    }

    /**
     * @param tipoLotto
     *            the tipoLotto to set
     */
    public void setTipoLotto(TipoLotto tipoLotto) {
        this.tipoLotto = tipoLotto;
    }

    /**
     * @param unitaMisura
     *            the unitaMisura to set
     */
    public void setUnitaMisura(UnitaMisura unitaMisura) {
        this.unitaMisura = unitaMisura;
    }

    /**
     * @param unitaMisuraQtaMagazzino
     *            the unitaMisuraQtaMagazzino to set
     */
    public void setUnitaMisuraQtaMagazzino(UnitaMisura unitaMisuraQtaMagazzino) {
        this.unitaMisuraQtaMagazzino = unitaMisuraQtaMagazzino;
    }

    @Override
    public String toString() {
        return "Articolo [codice=" + codice + ", abilitato=" + abilitato + ", descrizioneLinguaAziendale="
                + descrizioneLinguaAziendale + "]";
    }

}
