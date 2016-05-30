package it.eurotn.panjea.anagrafica.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.PostLoad;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.envers.Audited;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.agenti.domain.lite.AgenteLite;
import it.eurotn.panjea.anagrafica.domain.lite.SedeEntitaLite;
import it.eurotn.panjea.anagrafica.domain.lite.VettoreLite;
import it.eurotn.panjea.audit.envers.AuditableProperties;
import it.eurotn.panjea.magazzino.domain.BloccoSede;
import it.eurotn.panjea.magazzino.domain.SedeMagazzino;
import it.eurotn.panjea.ordini.domain.SedeOrdine;
import it.eurotn.panjea.pagamenti.domain.SedePagamento;

/**
 * This is an object that contains data related to the ANAG_SEDI_ENTITA table. Do not modify this
 * class because it will be overwritten if the configuration file related to this class is modified.
 *
 * @hibernate.class table="ANAG_SEDI_ENTITA"
 */
@Entity
@Audited
@Table(name = "anag_sedi_entita", uniqueConstraints = @UniqueConstraint(columnNames = { "codice", "entita_id" }) )
@NamedQueries({
        @NamedQuery(name = "SedeEntita.caricaBySedeAnagrafica", query = " from SedeEntita s where s.sede.id = :paramIdSedeAnagrafica "),
        @NamedQuery(name = "SedeEntita.caricaSediEntitaNonEreditaDatiComerciali", query = " from SedeEntita s where s.entita.id = :paramIdEntita and s.ereditaDatiCommerciali =false"),
        @NamedQuery(name = "SedeEntita.caricaNoteStampa", query = "select s.noteStampa from SedeEntita s where s.id=:paramIdSedeEntita"),
        @NamedQuery(name = "SedeEntita.caricaPredefinita", query = "select s from SedeEntita s where s.entita=:entitaParam and s.predefinita=true"),
        @NamedQuery(name = "SedeEntita.caricaByCodice", query = "select s from SedeEntita s where s.entita.id=:idEntitaParam and s.codice = :paramCodice") })
@AuditableProperties(properties = { "sede" })
public class SedeEntita extends EntityBase {

    private static final long serialVersionUID = -6554242869522197098L;
    public static final String REF = "SedeEntita";
    public static final String PROP_SEDE = "sede";
    public static final String PROP_ENTITA = "entita";
    public static final String PROP_TIPO_SEDE = "tipoSede";
    public static final String PROP_ABILITATO = "abilitato";
    public static final String PROP_ID = "id";

    @Column(length = 20)
    private String codice;

    /**
     * Devo fare una OneToMany perchè la OneToOne inversa non può essere lazy a causa di un bug di
     * hibernate.
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "sedeEntita")
    private Set<SedePagamento> sedePagamento;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "sedeEntita")
    private Set<SedeMagazzino> sedeMagazzino;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "sedeEntita")
    private Set<SedeOrdine> sedeOrdine;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<CategoriaEntita> categoriaEntita;

    @Column(name = "abilitato")
    private boolean abilitato;

    private boolean predefinita;

    @ManyToOne
    @JoinColumn(name = "entita_id")
    private Entita entita;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "sede_anagrafica_id")
    private SedeAnagrafica sede;

    @ManyToOne
    private TipoSedeEntita tipoSede;

    @Column(length = 10)
    private String codiceImportazione;

    @Column(length = 3)
    private String codiceValuta;

    @ManyToOne
    private ZonaGeografica zonaGeografica;

    @OneToMany(mappedBy = "sedeEntita", fetch = FetchType.LAZY)
    private Set<ContattoSedeEntita> contatti;

    /**
     * se true indica che deve ereditare i rapporti bancari dalla sede principale.
     */
    private boolean ereditaRapportiBancari;

    private boolean ereditaDatiCommerciali;

    @Lob
    private String note;

    @Lob
    private String noteStampa;

    @ManyToOne(fetch = FetchType.LAZY)
    private SedeEntita sedeCollegata;

    @ManyToOne(fetch = FetchType.LAZY)
    private SedeEntita sedeSpedizione;

    @Column(length = 15)
    private String cig;

    @Column(length = 15)
    private String cup;

    @Column(length = 2)
    private String lingua;

    @ManyToOne
    private AgenteLite agente;

    @Embedded
    private BloccoSede bloccoSede;

    @ManyToOne
    private VettoreLite vettore;

    @Column(length = 40)
    private String ordinamento;

    @Column(length = 10)
    private String codiceUfficioPA;

    @ManyToOne
    private RapportoBancarioAzienda rapportoBancarioAzienda;

    /**
     * Inizializza il bean.
     */
    {
        this.abilitato = true;
        this.ereditaRapportiBancari = false;
        this.ereditaDatiCommerciali = false;
        this.lingua = "it";
        this.codiceValuta = "EUR";
        this.categoriaEntita = new ArrayList<CategoriaEntita>();
    }

    /**
     * @return the agente
     */
    public AgenteLite getAgente() {
        return agente;
    }

    /**
     * @return the bloccoSede
     */
    public BloccoSede getBloccoSede() {
        if (bloccoSede == null) {
            bloccoSede = new BloccoSede();
        }
        return bloccoSede;
    }

    /**
     * @return Returns the categoriaEntita.
     */
    public List<CategoriaEntita> getCategoriaEntita() {
        return categoriaEntita;
    }

    /**
     * @return the cig
     */
    public String getCig() {
        return cig;
    }

    /**
     * @return the codice
     */
    public String getCodice() {
        return codice == null ? "" : codice;
    }

    /**
     * @return codice utilizzanto durante le importazioni
     */
    public String getCodiceImportazione() {
        return codiceImportazione;
    }

    /**
     * @return the codiceUfficioPA
     */
    public String getCodiceUfficioPA() {
        return codiceUfficioPA;
    }

    /**
     * @return Returns the codiceValuta.
     */
    public String getCodiceValuta() {
        return codiceValuta;
    }

    /**
     * @return Returns the contatti.
     */
    public Set<ContattoSedeEntita> getContatti() {
        return contatti;
    }

    /**
     * @return the cup
     */
    public String getCup() {
        return cup;
    }

    /**
     * @return entità della sede
     */
    public Entita getEntita() {
        return entita;
    }

    /**
     * @return the lingua
     */
    public String getLingua() {
        return lingua;
    }

    /**
     * @return note associate alla sede
     */
    public String getNote() {
        return note;
    }

    /**
     * @return the noteStampa
     */
    public String getNoteStampa() {
        return noteStampa;
    }

    /**
     * @return the ordinamento
     */
    public String getOrdinamento() {
        return ordinamento;
    }

    /**
     * @return the rapportoBancarioAzienda
     */
    public RapportoBancarioAzienda getRapportoBancarioAzienda() {
        return rapportoBancarioAzienda;
    }

    /**
     * @return anagrafica della sede
     */
    public SedeAnagrafica getSede() {
        if (sede == null) {
            sede = new SedeAnagrafica();
        }
        return sede;
    }

    /**
     * @return the sedePerSpedizione
     */
    public SedeEntita getSedeCollegata() {
        return sedeCollegata;
    }

    /**
     * @return {@link SedeEntitaLite} creata dalla {@link SedeEntita}
     */
    public SedeEntitaLite getSedeEntitaLite() {
        SedeEntitaLite sedeEntitaLite = new SedeEntitaLite();
        sedeEntitaLite.setId(getId());
        return sedeEntitaLite;
    }

    /**
     * @return Returns the sedeMagazzino.
     */
    public SedeMagazzino getSedeMagazzino() {
        if (!sedeMagazzino.isEmpty()) {
            return sedeMagazzino.iterator().next();
        }
        return null;
    }

    /**
     * @return Returns the sedeMagazzino.
     */
    public SedeOrdine getSedeOrdine() {
        if (!sedeOrdine.isEmpty()) {
            return sedeOrdine.iterator().next();
        }
        return null;
    }

    /**
     * @return Returns the sedePagamento.
     */
    public SedePagamento getSedePagamento() {
        if (!sedePagamento.isEmpty()) {
            return sedePagamento.iterator().next();
        }
        return null;
    }

    /**
     * @return the sedeSpedizione
     */
    public SedeEntita getSedeSpedizione() {
        return sedeSpedizione;
    }

    /**
     * @return tipo della sede
     */
    public TipoSedeEntita getTipoSede() {
        return tipoSede;
    }

    /**
     * @return the vettore
     */
    public VettoreLite getVettore() {
        return vettore;
    }

    /**
     * @return the zonaGeografica
     */
    public ZonaGeografica getZonaGeografica() {
        return zonaGeografica;
    }

    /**
     * @return sede abilitatoa o no.
     */
    public boolean isAbilitato() {
        return abilitato;
    }

    /**
     * @return the ereditaDatiCommerciali
     */
    public boolean isEreditaDatiCommerciali() {
        return ereditaDatiCommerciali;
    }

    /**
     * @return true se i rapporti bancari sono ereditati
     */
    public boolean isEreditaRapportiBancari() {
        return ereditaRapportiBancari;
    }

    /**
     * @return Returns the predefinita.
     */
    public boolean isPredefinita() {
        return predefinita;
    }

    /**
     * Inizializza i valori di default. TODO Una volta eliminati remove e restore mantenere solo
     * questo metodo eliminando initialize().
     */
    @PostLoad
    protected void postLoad() {
    }

    /**
     * @param abilitato
     *            set abilitato
     */
    public void setAbilitato(boolean abilitato) {
        this.abilitato = abilitato;
    }

    /**
     * @param agente
     *            the agente to set
     */
    public void setAgente(AgenteLite agente) {
        this.agente = agente;
    }

    /**
     * @param bloccoSede
     *            the bloccoSede to set
     */
    public void setBloccoSede(BloccoSede bloccoSede) {
        this.bloccoSede = bloccoSede;
    }

    /**
     * @param categoriaEntita
     *            The categoriaEntita to set.
     */
    public void setCategoriaEntita(List<CategoriaEntita> categoriaEntita) {
        this.categoriaEntita = categoriaEntita;
    }

    /**
     * @param cig
     *            the cig to set
     */
    public void setCig(String cig) {
        this.cig = cig;
    }

    /**
     * @param codice
     *            the codice to set
     */
    public void setCodice(String codice) {
        this.codice = codice;
    }

    /**
     * @param codiceImportazione
     *            codiceImportazione to set
     */
    public void setCodiceImportazione(String codiceImportazione) {
        this.codiceImportazione = codiceImportazione;
    }

    /**
     * @param codiceUfficioPA
     *            the codiceUfficioPA to set
     */
    public void setCodiceUfficioPA(String codiceUfficioPA) {
        this.codiceUfficioPA = codiceUfficioPA;
    }

    /**
     * @param codiceValuta
     *            The codiceValuta to set.
     */
    public void setCodiceValuta(String codiceValuta) {
        this.codiceValuta = codiceValuta;
    }

    /**
     * @param contatti
     *            The contatti to set.
     */
    public void setContatti(Set<ContattoSedeEntita> contatti) {
        this.contatti = contatti;
    }

    /**
     * @param cup
     *            the cup to set
     */
    public void setCup(String cup) {
        this.cup = cup;
    }

    /**
     * @param entita
     *            entità to set
     */
    public void setEntita(Entita entita) {
        this.entita = entita;
    }

    /**
     * @param ereditaDatiCommerciali
     *            the ereditaDatiCommerciali to set
     */
    public void setEreditaDatiCommerciali(boolean ereditaDatiCommerciali) {
        this.ereditaDatiCommerciali = ereditaDatiCommerciali;
    }

    /**
     * @param ereditaRapportiBancari
     *            the ereditaRapportiBancari to set
     */
    public void setEreditaRapportiBancari(boolean ereditaRapportiBancari) {
        this.ereditaRapportiBancari = ereditaRapportiBancari;
    }

    /**
     * @param lingua
     *            the lingua to set
     */
    public void setLingua(String lingua) {
        if (lingua != null && lingua.length() > 2) {
            lingua = lingua.substring(0, 2);
        }
        this.lingua = lingua;
    }

    /**
     * @param note
     *            the note to set
     */
    public void setNote(String note) {
        this.note = note;
    }

    /**
     * @param noteStampa
     *            the noteStampa to set
     */
    public void setNoteStampa(String noteStampa) {
        this.noteStampa = noteStampa;
    }

    /**
     * @param ordinamento
     *            the ordinamento to set
     */
    public void setOrdinamento(String ordinamento) {
        this.ordinamento = ordinamento;
    }

    /**
     * @param predefinita
     *            The predefinita to set.
     */
    public void setPredefinita(boolean predefinita) {
        this.predefinita = predefinita;
    }

    /**
     * @param rapportoBancarioAzienda
     *            the rapportoBancarioAzienda to set
     */
    public void setRapportoBancarioAzienda(RapportoBancarioAzienda rapportoBancarioAzienda) {
        this.rapportoBancarioAzienda = rapportoBancarioAzienda;
    }

    /**
     * @param sede
     *            the sede to set
     */
    public void setSede(SedeAnagrafica sede) {
        this.sede = sede;
    }

    /**
     * @param sedeCollegata
     *            the sedeCollegata to set
     */
    public void setSedeCollegata(SedeEntita sedeCollegata) {
        this.sedeCollegata = sedeCollegata;
    }

    /**
     * @param sedeSpedizione
     *            the sedeSpedizione to set
     */
    public void setSedeSpedizione(SedeEntita sedeSpedizione) {
        this.sedeSpedizione = sedeSpedizione;
    }

    /**
     * @param tipoSede
     *            the tipoSede to set
     */
    public void setTipoSede(TipoSedeEntita tipoSede) {
        this.tipoSede = tipoSede;
    }

    /**
     * @param vettore
     *            the vettore to set
     */
    public void setVettore(VettoreLite vettore) {
        this.vettore = vettore;
    }

    /**
     * @param zonaGeografica
     *            the zonaGeografica to set
     */
    public void setZonaGeografica(ZonaGeografica zonaGeografica) {
        this.zonaGeografica = zonaGeografica;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("SedeEntita[");
        buffer.append(super.toString());
        buffer.append("abilitato = ").append(abilitato);
        buffer.append(" entita = ").append(entita != null ? entita.getId() : null);
        buffer.append(" sede = ").append(sede != null ? sede.getId() : null);
        buffer.append(" tipoSede = ").append(tipoSede != null ? tipoSede.getId() : null);
        buffer.append("]");
        return buffer.toString();
    }
}
