package it.eurotn.panjea.magazzino.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.envers.Audited;

import it.eurotn.entity.EntityBase;

/**
 * Classe per la definizione della classe astratta RigaContratto.<br/>
 * Le classi che la ereditano determinano se {@link RigaContratto} è associata a {@link Categoria} o ad {@link Articolo}
 *
 * @author adriano
 * @version 1.0, 17/giu/08
 */
@Entity
@Audited
@Table(name = "maga_righe_contratto")
@NamedQueries({
        @NamedQuery(name = "RigaContratto.caricaByContratto", query = "from RigaContratto c left join fetch c.articolo a where c.contratto.id = :paramIdContratto") })
public class RigaContratto extends EntityBase {

    /**
     * @author giangi
     */
    public enum Azione {
        SOSTITUZIONE, VARIAZIONE
    }

    /**
     * @author giangi
     */
    public enum TipoRiga {
        ARTICOLO, CATEGORIA, TUTTI
    }

    private static final long serialVersionUID = -8235610161718466288L;

    @ManyToOne
    private Contratto contratto;

    @ManyToOne
    private ArticoloLite articolo;

    @ManyToOne
    private CategoriaCommercialeArticolo categoriaCommercialeArticolo;

    private Boolean tutteLeCategorie;

    @Embedded
    private RigaContrattoStrategiaPrezzo strategiaPrezzo;

    @Embedded
    private RigaContrattoStrategiaSconto strategiaSconto;

    private boolean strategiaPrezzoAbilitata;

    private boolean strategiaScontoAbilitata;

    /**
     * Decimali per il prezzo.
     */
    private Integer numeroDecimaliPrezzo;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "rigaContratto", cascade = { CascadeType.ALL })
    @org.hibernate.annotations.Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @javax.persistence.OrderBy("agente")
    private List<RigaContrattoAgente> righeContrattoAgente;

    /**
     * Inizializzano i valori di default.
     */
    {
        numeroDecimaliPrezzo = 2;
        tutteLeCategorie = true;
        strategiaPrezzoAbilitata = true;
        strategiaScontoAbilitata = true;
        strategiaPrezzo = new RigaContrattoStrategiaPrezzo();
        strategiaSconto = new RigaContrattoStrategiaSconto();
    }

    /**
     * Costruttore.
     */
    public RigaContratto() {
        super();
    }

    /**
     * @return the articolo
     */
    public ArticoloLite getArticolo() {
        return articolo;
    }

    /**
     * @return Returns the categoriaCommercialeArticolo.
     */
    public CategoriaCommercialeArticolo getCategoriaCommercialeArticolo() {
        return categoriaCommercialeArticolo;
    }

    /**
     * @return the contratto
     */
    public Contratto getContratto() {
        return contratto;
    }

    /**
     * @return the numeroDecimaliPrezzo
     */
    public Integer getNumeroDecimaliPrezzo() {
        return numeroDecimaliPrezzo;
    }

    /**
     * @return the righeContrattoAgente
     */
    public List<RigaContrattoAgente> getRigheContrattoAgente() {
        return righeContrattoAgente;
    }

    /**
     * @return the strategiaPrezzo
     */
    public RigaContrattoStrategiaPrezzo getStrategiaPrezzo() {
        if (strategiaPrezzo == null) {
            strategiaPrezzo = new RigaContrattoStrategiaPrezzo();
        }
        return strategiaPrezzo;
    }

    /**
     * @return the strategiaSconto
     */
    public RigaContrattoStrategiaSconto getStrategiaSconto() {
        if (strategiaSconto == null) {
            strategiaSconto = new RigaContrattoStrategiaSconto();
        }
        return strategiaSconto;
    }

    /**
     * @return the tutteLeCategorie
     */
    public Boolean getTutteLeCategorie() {
        return tutteLeCategorie;
    }

    /**
     * @return the strategiaPrezzoAbilitata
     */
    public boolean isStrategiaPrezzoAbilitata() {
        return strategiaPrezzoAbilitata;
    }

    /**
     * @return the strategiaScontoAbilitata
     */
    public boolean isStrategiaScontoAbilitata() {
        return strategiaScontoAbilitata;
    }

    /**
     * @param articolo
     *            the articolo to set
     */
    public void setArticolo(ArticoloLite articolo) {
        if (articolo != null) {
            tutteLeCategorie = false;
        }
        this.articolo = articolo;
    }

    /**
     * @param categoriaCommercialeArticolo
     *            the categoria to set
     */
    public void setCategoriaCommercialeArticolo(CategoriaCommercialeArticolo categoriaCommercialeArticolo) {
        if (categoriaCommercialeArticolo != null) {
            tutteLeCategorie = false;
        }
        this.categoriaCommercialeArticolo = categoriaCommercialeArticolo;
    }

    /**
     * @param contratto
     *            the contratto to set
     */
    public void setContratto(Contratto contratto) {
        this.contratto = contratto;
    }

    /**
     * @param numeroDecimaliPrezzo
     *            the numeroDecimaliPrezzo to set
     */
    public void setNumeroDecimaliPrezzo(Integer numeroDecimaliPrezzo) {
        this.numeroDecimaliPrezzo = numeroDecimaliPrezzo;
    }

    /**
     * @param righeContrattoAgente
     *            the righeContrattoAgente to set
     */
    public void setRigheContrattoAgente(List<RigaContrattoAgente> righeContrattoAgente) {
        this.righeContrattoAgente = righeContrattoAgente;
    }

    /**
     * @param strategiaPrezzo
     *            the strategiaPrezzo to set
     */
    public void setStrategiaPrezzo(RigaContrattoStrategiaPrezzo strategiaPrezzo) {
        this.strategiaPrezzo = strategiaPrezzo;
    }

    /**
     * @param strategiaPrezzoAbilitata
     *            the strategiaPrezzoAbilitata to set
     */
    public void setStrategiaPrezzoAbilitata(boolean strategiaPrezzoAbilitata) {
        this.strategiaPrezzoAbilitata = strategiaPrezzoAbilitata;
    }

    /**
     * @param strategiaSconto
     *            the strategiaSconto to set
     */
    public void setStrategiaSconto(RigaContrattoStrategiaSconto strategiaSconto) {
        this.strategiaSconto = strategiaSconto;
    }

    /**
     * @param strategiaScontoAbilitata
     *            the strategiaScontoAbilitata to set
     */
    public void setStrategiaScontoAbilitata(boolean strategiaScontoAbilitata) {
        this.strategiaScontoAbilitata = strategiaScontoAbilitata;
    }

    /**
     * @param tutteLeCategorie
     *            the tutteLeCategorie to set
     */
    public void setTutteLeCategorie(Boolean tutteLeCategorie) {
        this.tutteLeCategorie = tutteLeCategorie;
    }

}
