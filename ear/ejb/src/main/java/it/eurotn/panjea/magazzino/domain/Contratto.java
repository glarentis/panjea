package it.eurotn.panjea.magazzino.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Index;
import org.hibernate.envers.Audited;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;

/**
 * Classe per la rappresentazione di un Contratto. Gli attributi data inizio e data fine definiscono la validita del
 * contratto. <br>
 * Valuta specifica in quale valuta sono definiti i prezzi all'interno di {@link RigaContratto} <br>
 * Numero decimali definisce il numero decimali predefinito da inserire sulla {@link RigaContratto} <br>
 * L'attributo tuttiClienti specifica che il contratto è generalizzato per tutti i clienti, la sua valorizzazione a true
 * invaliderà la collection sediMagazzino e categorieListino <br>
 * L'attributo tuttiArticoli specifica che il contratto è generalizzato per tutti gli articoli, la sua valorizzazione a
 * true dovrà invalidare tutte le righe contratto. <br>
 *
 * @author adriano
 * @version 1.0, 17/giu/08
 */
@Entity
@Audited
@Table(name = "maga_contratti", uniqueConstraints = @UniqueConstraint(columnNames = { "codiceAzienda", "codice" }) )
@NamedQueries({
        @NamedQuery(name = "Contratto.caricaContratti", query = "from Contratto c where c.codiceAzienda = :paramCodiceAzienda ") })
public class Contratto extends EntityBase {

    private static final long serialVersionUID = 1L;

    /**
     * @uml.property name="codiceAzienda"
     */
    @Column(length = 10, nullable = false)
    private String codiceAzienda;

    /**
     * @uml.property name="codice"
     */
    @Column(length = 15, nullable = false)
    private String codice;

    /**
     * @uml.property name="descrizione"
     */
    @Column(length = 100)
    private String descrizione;

    /**
     * @uml.property name="dataInizio"
     */
    @Column
    @Temporal(TemporalType.DATE)
    @Index(name = "index_dataInizio")
    private Date dataInizio;

    /**
     * @uml.property name="dataFine"
     */
    @Column
    @Temporal(TemporalType.DATE)
    @Index(name = "index_dataFine")
    private Date dataFine;

    /**
     * @uml.property name="codiceValuta"
     */
    @Column(length = 3)
    private String codiceValuta;

    /**
     * numero decimali proposti sul contratto.
     * 
     * @uml.property name="numeroDecimali"
     */
    @Column
    private Integer numeroDecimali;

    /**
     * @uml.property name="categorieSediMagazzino"
     */
    @ManyToMany(fetch = FetchType.LAZY)
    private List<CategoriaSedeMagazzino> categorieSediMagazzino;

    /**
     * @uml.property name="sediMagazzino"
     */
    @ManyToMany(fetch = FetchType.LAZY)
    private List<SedeMagazzinoLite> sediMagazzino;

    /**
     * @uml.property name="entita"
     */
    @ManyToMany(fetch = FetchType.LAZY)
    private List<EntitaLite> entita;

    /**
     * @uml.property name="righeContratto"
     */
    @OneToMany(mappedBy = "contratto", cascade = { CascadeType.REMOVE })
    private List<RigaContratto> righeContratto;

    /**
     * @uml.property name="tutteCategorieSedeMagazzino"
     */
    private boolean tutteCategorieSedeMagazzino;

    /**
     * Costruttore.
     */
    public Contratto() {
        super();
        initialize();
    }

    /**
     * @return categorieSediMagazzino
     * @uml.property name="categorieSediMagazzino"
     */
    public List<CategoriaSedeMagazzino> getCategorieSediMagazzino() {
        return categorieSediMagazzino;
    }

    /**
     * @return Returns the codice.
     * @uml.property name="codice"
     */
    public String getCodice() {
        return codice;
    }

    /**
     * @return Returns the codiceAzienda.
     * @uml.property name="codiceAzienda"
     */
    public String getCodiceAzienda() {
        return codiceAzienda;
    }

    /**
     * @return Returns the codiceValuta.
     * @uml.property name="codiceValuta"
     */
    public String getCodiceValuta() {
        return codiceValuta;
    }

    /**
     * @return Returns the dataFine.
     * @uml.property name="dataFine"
     */
    public Date getDataFine() {
        return dataFine;
    }

    /**
     * @return Returns the dataInizio.
     * @uml.property name="dataInizio"
     */
    public Date getDataInizio() {
        return dataInizio;
    }

    /**
     * @return Returns the descrizione.
     * @uml.property name="descrizione"
     */
    public String getDescrizione() {
        return descrizione;
    }

    /**
     * @return the entita
     * @uml.property name="entita"
     */
    public List<EntitaLite> getEntita() {
        return entita;
    }

    /**
     * @return Returns the numeroDecimali.
     * @uml.property name="numeroDecimali"
     */
    public Integer getNumeroDecimali() {
        return numeroDecimali;
    }

    /**
     * @return Returns the righeContratto.
     * @uml.property name="righeContratto"
     */
    public List<RigaContratto> getRigheContratto() {
        return righeContratto;
    }

    /**
     * @return sediMagazzino
     * @uml.property name="sediMagazzino"
     */
    public List<SedeMagazzinoLite> getSediMagazzino() {
        return sediMagazzino;
    }

    /**
     * Inizializza i valori di default del contratto.
     */
    private void initialize() {
        tutteCategorieSedeMagazzino = false;
        categorieSediMagazzino = new ArrayList<CategoriaSedeMagazzino>();
        sediMagazzino = new ArrayList<SedeMagazzinoLite>();
        entita = new ArrayList<EntitaLite>();
    }

    /**
     * @return the tutteCategorieSedeMagazzino
     * @uml.property name="tutteCategorieSedeMagazzino"
     */
    public boolean isTutteCategorieSedeMagazzino() {
        return tutteCategorieSedeMagazzino;
    }

    /**
     * @param categorieSediMagazzino
     *            the categorieSediMagazzino to set
     * @uml.property name="categorieSediMagazzino"
     */
    public void setCategorieSediMagazzino(List<CategoriaSedeMagazzino> categorieSediMagazzino) {
        this.categorieSediMagazzino = categorieSediMagazzino;
    }

    /**
     * @param codice
     *            The codice to set.
     * @uml.property name="codice"
     */
    public void setCodice(String codice) {
        this.codice = codice;
    }

    /**
     * @param codiceAzienda
     *            The codiceAzienda to set.
     * @uml.property name="codiceAzienda"
     */
    public void setCodiceAzienda(String codiceAzienda) {
        this.codiceAzienda = codiceAzienda;
    }

    /**
     * @param codiceValuta
     *            The codiceValuta to set.
     * @uml.property name="codiceValuta"
     */
    public void setCodiceValuta(String codiceValuta) {
        this.codiceValuta = codiceValuta;
    }

    /**
     * @param dataFine
     *            The dataFine to set.
     * @uml.property name="dataFine"
     */
    public void setDataFine(Date dataFine) {
        this.dataFine = dataFine;
    }

    /**
     * @param dataInizio
     *            The dataInizio to set.
     * @uml.property name="dataInizio"
     */
    public void setDataInizio(Date dataInizio) {
        this.dataInizio = dataInizio;
    }

    /**
     * @param descrizione
     *            The descrizione to set.
     * @uml.property name="descrizione"
     */
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    /**
     * @param entita
     *            the entita to set
     * @uml.property name="entita"
     */
    public void setEntita(List<EntitaLite> entita) {
        this.entita = entita;
    }

    /**
     * @param numeroDecimali
     *            The numeroDecimali to set.
     * @uml.property name="numeroDecimali"
     */
    public void setNumeroDecimali(Integer numeroDecimali) {
        this.numeroDecimali = numeroDecimali;
    }

    /**
     * @param righeContratto
     *            The righeContratto to set.
     * @uml.property name="righeContratto"
     */
    public void setRigheContratto(List<RigaContratto> righeContratto) {
        this.righeContratto = righeContratto;
    }

    /**
     * @param sediMagazzino
     *            the sediMagazzino to set
     * @uml.property name="sediMagazzino"
     */
    public void setSediMagazzino(List<SedeMagazzinoLite> sediMagazzino) {
        this.sediMagazzino = sediMagazzino;
    }

    /**
     * @param tutteCategorieSedeMagazzino
     *            the tutteCategorieSedeMagazzino to set
     * @uml.property name="tutteCategorieSedeMagazzino"
     */
    public void setTutteCategorieSedeMagazzino(boolean tutteCategorieSedeMagazzino) {
        this.tutteCategorieSedeMagazzino = tutteCategorieSedeMagazzino;
    }

    /**
     * Costruisce una <code>String</code> con tutti gli attributi accodati con questo formato nome attributo = valore.
     * 
     * @return a <code>String</code> come risultato di questo oggetto
     */
    @Override
    public String toString() {

        StringBuilder retValue = new StringBuilder();

        retValue.append("Contratto[ ").append(super.toString()).append("codiceAzienda = ").append(this.codiceAzienda)
                .append("codice = ").append(this.codice).append("descrizione = ").append(this.descrizione)
                .append("dataInizio = ").append(this.dataInizio).append("dataFine = ").append(this.dataFine)
                .append("codiceValuta = ").append(this.codiceValuta).append("numeroDecimali = ")
                .append(this.numeroDecimali).append("]");

        return retValue.toString();
    }

}
