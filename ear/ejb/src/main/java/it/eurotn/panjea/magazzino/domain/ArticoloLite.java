package it.eurotn.panjea.magazzino.domain;

import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.Filters;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.envers.AuditJoinTable;
import org.hibernate.envers.Audited;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.anagrafica.domain.CodiceIva;
import it.eurotn.panjea.anagrafica.domain.FaseLavorazioneArticolo;
import it.eurotn.panjea.anagrafica.domain.UnitaMisura;
import it.eurotn.panjea.conai.domain.ConaiComponente;
import it.eurotn.panjea.magazzino.domain.Articolo.ETipoArticolo;
import it.eurotn.panjea.magazzino.domain.Articolo.TipoLotto;
import it.eurotn.panjea.magazzino.util.ArticoloRicerca;
import it.eurotn.util.PanjeaEJBUtil;

/**
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
@Entity
@Audited
@Table(name = "maga_articoli")
@NamedQueries({
        @NamedQuery(name = "ArticoloLite.caricaByCategoriaCommerciale", query = "select DISTINCT a from ArticoloLite a inner join fetch a.codiceIva codIva where a.categoriaCommercialeArticolo.id in (:paramIdCategorie)"),
        @NamedQuery(name = "ArticoloLite.caricaByCodice", query = "select a from ArticoloLite a where a.codiceAzienda = :paramCodiceAzienda and a.codice = :paramCodice ") })
public class ArticoloLite extends EntityBase {

    private static final long serialVersionUID = -2166537896523666678L;

    @Transient
    private String campoLibero;

    /**
     * indica se il lotto può essere tralasciato.
     */
    private boolean lottoFacoltativo;

    private boolean distinta;

    @Column(length = 10, nullable = false)
    @Index(name = "index_codiceAzienda")
    private String codiceAzienda = null;

    @Column(length = 30)
    private String codice;

    @Column(length = 30)
    private String codiceInterno;

    @Transient
    private String codiceEntita;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, mappedBy = "articolo")
    private Set<CodiceArticoloEntita> codiciEntita;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, mappedBy = "distinta")
    @Filters({ @Filter(name = "configurazioneDistintaBase", condition = "configurazioneDistinta_id is null") })
    @OrderBy(value = "ordinamento")
    private Set<Componente> componenti;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, mappedBy = "articolo")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @OrderBy(value = "ordinamento")
    private Set<FaseLavorazioneArticolo> fasiLavorazioneArticolo;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, mappedBy = "articolo")
    private Set<ConaiComponente> componentiConai;

    @Column(length = 100)
    private String descrizioneLinguaAziendale;

    private boolean abilitato;

    private boolean articoloLibero;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "articolo_id")
    @AuditJoinTable(name = "articolo_attributoarticolo_aud")
    private List<AttributoArticolo> attributiArticolo;

    @Column(length = 13)
    private String barCode;

    private String mascheraInput;

    @ManyToOne
    private UnitaMisura unitaMisura;

    private ETipoArticolo tipoArticolo;

    @ManyToOne(fetch = FetchType.LAZY)
    @Fetch(FetchMode.SELECT)
    private Categoria categoria;

    @ManyToOne(fetch = FetchType.LAZY)
    @Fetch(FetchMode.SELECT)
    private CategoriaCommercialeArticolo categoriaCommercialeArticolo;

    @ManyToOne(fetch = FetchType.LAZY)
    @Fetch(FetchMode.SELECT)
    private CategoriaCommercialeArticolo categoriaCommercialeArticolo2;

    @Column(nullable = false)
    private Integer numeroDecimaliQta;

    private Integer numeroDecimaliPrezzo;

    private ProvenienzaPrezzoArticolo provenienzaPrezzoArticolo;

    private TipoLotto tipoLotto;

    private boolean stampaLotti;

    @ManyToOne(fetch = FetchType.LAZY)
    private CodiceIva codiceIva;

    @Column(length = 50)
    private String posizione;

    private boolean gestioneQuantitaZero;

    private boolean produzione;

    private boolean somministrazione;

    private Double resa; // per il vending

    /**
     * Costruttore.
     */
    public ArticoloLite() {
    }

    /**
     * Costruttore.
     *
     * @param articolo
     *            articolo
     */
    public ArticoloLite(final Articolo articolo) {
        this.setId(articolo.getId());
        this.setVersion(articolo.getVersion());
        this.setAbilitato(articolo.isAbilitato());
        this.setBarCode(articolo.getBarCode());
        this.setCategoria(articolo.getCategoria());
        this.setCodice(articolo.getCodice());
        this.setCodiceAzienda(articolo.getCodiceAzienda());
        this.setDescrizione(articolo.getDescrizione());
        this.setMascheraInput(articolo.getMascheraInput());
        this.setNumeroDecimaliPrezzo(articolo.getNumeroDecimaliPrezzo());
        this.setNumeroDecimaliQta(articolo.getNumeroDecimaliQta());
        this.setProvenienzaPrezzoArticolo(articolo.getProvenienzaPrezzoArticolo());
        this.setTipoArticolo(articolo.getTipoArticolo());
        this.setUnitaMisura(articolo.getUnitaMisura());
        this.setTipoLotto(articolo.getTipoLotto());
        this.setStampaLotti(articolo.isStampaLotti());
        this.setDistinta(articolo.isDistinta());
    }

    /**
     * @return un "proxy" di articolo. Setta solamente i campi utili per poter caricare <br/>
     *         il reale articolo persistente.
     */
    public Articolo creaProxyArticolo() {
        Articolo articolo = new Articolo();
        articolo.setId(getId());
        articolo.setVersion(getVersion());
        articolo.setCodice(getCodice());
        articolo.setDescrizione(getDescrizione());
        articolo.setNumeroDecimaliQta(getNumeroDecimaliQta());
        return articolo;
    }

    /**
     * Crea un articolo ricerca.
     *
     * @return articolo creato
     */
    public ArticoloRicerca createArticoloRicerca() {
        ArticoloRicerca articoloRicerca = new ArticoloRicerca();
        PanjeaEJBUtil.copyProperties(articoloRicerca, this);
        return articoloRicerca;
    }

    /**
     * @return Returns the attributiArticolo.
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
     * @return the barCode
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
     * @return the codiceEntita
     */
    public String getCodiceEntita() {
        return codiceEntita;
    }

    /**
     * @return the codiceInterno
     */
    public String getCodiceInterno() {
        return codiceInterno;
    }

    /**
     * @return Returns the codiceIva.
     */
    public CodiceIva getCodiceIva() {
        return codiceIva;
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
     * @return descrizione
     */
    public String getDescrizione() {
        return descrizioneLinguaAziendale;
    }

    /**
     * @return the fasiLavorazioneArticolo
     */
    public Set<FaseLavorazioneArticolo> getFasiLavorazioneArticolo() {
        return fasiLavorazioneArticolo;
    }

    /**
     * @return mascheraInput
     */
    public String getMascheraInput() {
        return mascheraInput;
    }

    /**
     * @return Returns the numeroDecimaliPrezzo.
     */
    public Integer getNumeroDecimaliPrezzo() {
        if (numeroDecimaliPrezzo == null) {
            return 0;
        } else {
            return numeroDecimaliPrezzo;
        }
    }

    /**
     * @return numeroDecimaliQta
     */
    public Integer getNumeroDecimaliQta() {
        if (numeroDecimaliQta == null) {
            return 0;
        } else {
            return numeroDecimaliQta;
        }
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
     * @return the resa
     */
    public Double getResa() {
        return resa;
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
     * @return the unitaMisura
     */
    public UnitaMisura getUnitaMisura() {
        return unitaMisura;
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
     * @return Returns the distinta.
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
     * @return Returns the lottoFacoltativo.
     */
    public boolean isLottoFacoltativo() {
        return lottoFacoltativo;
    }

    /**
     * @return the produzione
     */
    public boolean isProduzione() {
        return produzione;
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
     * @param articoloLibero
     *            the articoloLibero to set
     */
    public void setArticoloLibero(boolean articoloLibero) {
        this.articoloLibero = articoloLibero;
    }

    /**
     * @param attributiArticolo
     *            The attributiArticolo to set.
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
     * @param codiceCategoria
     *            the codiceCategoria to set
     */
    public void setCodiceCategoria(String codiceCategoria) {
        if (categoria == null) {
            this.categoria = new Categoria();
        }
        this.categoria.setCodice(codiceCategoria);
    }

    /**
     * @param codiceEntita
     *            the codiceEntita to set
     */
    public void setCodiceEntita(String codiceEntita) {
        this.codiceEntita = codiceEntita;
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
     *            The codiceIva to set.
     */
    public void setCodiceIva(CodiceIva codiceIva) {
        this.codiceIva = codiceIva;
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
     * @param componentiConai
     *            the componentiConai to set
     */
    public void setComponentiConai(Set<ConaiComponente> componentiConai) {
        this.componentiConai = componentiConai;
    }

    /**
     * @param descrizione
     *            the descrizione to set
     */
    public void setDescrizione(String descrizione) {
        this.descrizioneLinguaAziendale = descrizione;
    }

    /**
     * @param descrizioneCategoria
     *            the descrizioneCategoria to set
     */
    public void setDescrizioneCategoria(String descrizioneCategoria) {
        if (categoria == null) {
            this.categoria = new Categoria();
        }
        this.categoria.setDescrizione(descrizioneCategoria);
    }

    /**
     * @param distinta
     *            The distinta to set.
     */
    public void setDistinta(boolean distinta) {
        this.distinta = distinta;
    }

    /**
     * @param fasiLavorazioneArticolo
     *            the fasiLavorazioneArticolo to set
     */
    public void setFasiLavorazioneArticolo(Set<FaseLavorazioneArticolo> fasiLavorazioneArticolo) {
        this.fasiLavorazioneArticolo = fasiLavorazioneArticolo;
    }

    /**
     * @param gestioneQuantitaZero
     *            the gestioneQuantitaZero to set
     */
    public void setGestioneQuantitaZero(boolean gestioneQuantitaZero) {
        this.gestioneQuantitaZero = gestioneQuantitaZero;
    }

    /**
     * @param idCategoria
     *            the idCategoria to set
     */
    public void setIdCategoria(Integer idCategoria) {
        if (categoria == null) {
            this.categoria = new Categoria();
        }
        this.categoria.setId(idCategoria);
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
     * @param numeroDecimaliPrezzo
     *            The numeroDecimaliPrezzo to set.
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
     *            the resa to set
     */
    public void setResa(Double resa) {
        this.resa = resa;
    }

    /**
     * @param stampaLotti
     *            the stampaLotti to set
     */
    public void setStampaLotti(boolean stampaLotti) {
        this.stampaLotti = stampaLotti;
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
     * Constructs a <code>String</code> with all attributes in name = value format.
     *
     * @return a <code>String</code> representation of this object.
     */
    @Override
    public String toString() {
        StringBuilder retValue = new StringBuilder();
        retValue.append("ArticoloLite[ ").append(" codice = ").append(this.codice);
        retValue.append(" descrizione = ").append(this.descrizioneLinguaAziendale);
        retValue.append(" ]");
        return retValue.toString();
    }
}
