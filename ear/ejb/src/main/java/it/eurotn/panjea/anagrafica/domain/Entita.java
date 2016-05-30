package it.eurotn.panjea.anagrafica.domain;

import java.math.BigDecimal;
import java.util.Set;

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
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Index;
import org.hibernate.envers.Audited;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.agenti.domain.Agente;
import it.eurotn.panjea.agenti.domain.lite.AgenteLite;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.domain.lite.ClienteLite;
import it.eurotn.panjea.anagrafica.domain.lite.ClientePotenzialeLite;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.domain.lite.FornitoreLite;
import it.eurotn.panjea.anagrafica.domain.lite.VettoreLite;
import it.eurotn.panjea.audit.envers.AuditableProperties;
import it.eurotn.panjea.magazzino.domain.BloccoSede;

@Entity
@Audited
@Table(name = "anag_entita", uniqueConstraints = @UniqueConstraint(columnNames = { "TIPO_ANAGRAFICA", "codice" }) )
@org.hibernate.annotations.Table(appliesTo = "anag_entita", indexes = {
        @Index(name = "idxTipoCodice", columnNames = { "TIPO_ANAGRAFICA", "codice" }),
        @Index(name = "idxTipo", columnNames = { "TIPO_ANAGRAFICA" }) })
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TIPO_ANAGRAFICA", discriminatorType = DiscriminatorType.STRING, length = 2)
@DiscriminatorValue("E")
@AuditableProperties(properties = { "anagrafica" })
@NamedQueries({
        @NamedQuery(name = "Entita.caricaSpitPaymentFlag", query = "select e.splitPayment from Entita e where e.id=:idEntita"),
        @NamedQuery(name = "Entita.countEntita", query = "select count(e.id) from Entita e where e.anagrafica.codiceAzienda = :paramCodiceAzienda") })
public class Entita extends EntityBase {

    private static final long serialVersionUID = -163810612499426050L;
    public static final String REF = "Entita";
    public static final String PROP_CODICE = "codice";
    public static final String PROP_ABILITATO = "abilitato";
    public static final String PROP_ANAGRAFICA = "anagrafica";

    public static final String PROP_ID = "id";

    private Integer codice;

    @Column(name = "TIPO_ANAGRAFICA", insertable = false, updatable = false)
    private String tipoAnagrafica;

    @Column(length = 15)
    private String codiceEsterno;

    @Column(name = "abilitato")
    private boolean abilitato;

    @ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE, CascadeType.PERSIST })
    @JoinColumn(name = "anagrafica_id")
    private Anagrafica anagrafica;

    @OneToMany(mappedBy = "entita", fetch = FetchType.LAZY)
    @OrderBy("abilitato")
    private Set<SedeEntita> sedi;

    @Column(precision = 12, scale = 6)
    private BigDecimal fido;

    private boolean raggruppaEffetti;

    @Transient
    private Integer codicePrecedente = null;

    @Lob
    private String note;

    @Lob
    private String noteMagazzino;

    @Lob
    private String noteContabilita;

    private boolean assortimentoArticoli;

    private boolean escludiSpesometro;

    private boolean riepilogativo;

    private boolean splitPayment;

    @Embedded
    private BloccoSede bloccoSede;

    // campi necessari per la fatturazione alle pubbliche amministrazioni
    @Column(length = 17)
    private String codiceEori;

    @Column(length = 50)
    private String codiceIdentificativoFiscale;

    private boolean fatturazionePA;

    @Lob
    private String noteFatturaPA;

    @Transient
    private EntitaLite entitaLite;

    /**
     * Costruttore.
     */
    public Entita() {
        super();
        this.initialize();
    }

    /**
     * @return Returns the anagrafica.
     */
    public Anagrafica getAnagrafica() {
        return anagrafica;
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
     * @return the codice
     */
    public Integer getCodice() {
        return codice;
    }

    /**
     * @return the codiceEori
     */
    public String getCodiceEori() {
        return codiceEori;
    }

    /**
     *
     * @return codice esterno dell'entità
     */
    public String getCodiceEsterno() {
        return codiceEsterno;
    }

    /**
     * @return the codiceIdentificativoFiscale
     */
    public String getCodiceIdentificativoFiscale() {
        return codiceIdentificativoFiscale;
    }

    /**
     * @return Returns the codicePrecedente
     */
    public Integer getCodicePrecedente() {
        return codicePrecedente;
    }

    /**
     * @return {@link EntitaLite}
     */
    public EntitaLite getEntitaLite() {
        if (entitaLite != null) {
            return entitaLite;
        }

        if (getClass().getName().equals(Cliente.class.getName())) {
            entitaLite = new ClienteLite();
        } else if (getClass().getName().equals(Fornitore.class.getName())) {
            entitaLite = new FornitoreLite();
        } else if (getClass().getName().equals(Vettore.class.getName())) {
            entitaLite = new VettoreLite();
        } else if (getClass().getName().equals(ClientePotenziale.class.getName())) {
            entitaLite = new ClientePotenzialeLite();
        } else if (getClass().getName().equals(Agente.class.getName())) {
            entitaLite = new AgenteLite();
        } else if ("C".equals(tipoAnagrafica)) {
            entitaLite = new ClienteLite();
        } else if ("F".equals(tipoAnagrafica)) {
            entitaLite = new FornitoreLite();
        } else if ("V".equals(tipoAnagrafica)) {
            entitaLite = new VettoreLite();
        } else if ("CP".equals(tipoAnagrafica)) {
            entitaLite = new ClientePotenzialeLite();
        } else if ("A".equals(tipoAnagrafica)) {
            entitaLite = new AgenteLite();
        }
        if (entitaLite != null) {
            entitaLite.setId(getId());
            entitaLite.setVersion(getVersion());
            entitaLite.setCodice(getCodice());
            entitaLite.getAnagrafica().setId(getAnagrafica().getId());
            entitaLite.getAnagrafica().setVersion(getAnagrafica().getVersion());
            entitaLite.getAnagrafica().setDenominazione(getAnagrafica().getDenominazione());
        }
        return entitaLite;
    }

    /**
     * @return the fido
     */
    public BigDecimal getFido() {
        return fido;
    }

    /**
     * @return the note
     */
    public String getNote() {
        return note;
    }

    /**
     * @return the noteContabilita
     */
    public String getNoteContabilita() {
        return noteContabilita;
    }

    /**
     * @return the noteFatturaPA
     */
    public String getNoteFatturaPA() {
        return noteFatturaPA;
    }

    /**
     * @return the noteMagazzino
     */
    public String getNoteMagazzino() {
        return noteMagazzino;
    }

    /**
     * implementato nelle classi ereditate ne definisce il valore per l'ordinamento.
     *
     * @return ordine
     */
    public int getOrdine() {
        return 0;
    }

    /**
     * @return the sedi
     */
    public java.util.Set<SedeEntita> getSedi() {
        return sedi;
    }

    /**
     * Implementato nelle classi ereditate ne definisce il valore per il tipo.
     *
     * @return Tipo dell'entità (discriminatore)
     */
    public TipoEntita getTipo() {
        return TipoEntita.CLIENTE;
    }

    /**
     * @return the tipoAnagrafica
     */
    public final String getTipoAnagrafica() {
        return tipoAnagrafica;
    }

    /**
     * Inizializza i valori.
     */
    private void initialize() {
        this.entitaLite = null;
        this.anagrafica = new Anagrafica();
        this.abilitato = true;
        this.fido = BigDecimal.ZERO;
        this.raggruppaEffetti = true;
        this.assortimentoArticoli = false;
        this.riepilogativo = false;
        this.fatturazionePA = false;
    }

    /**
     * @return the abilitato
     */
    public boolean isAbilitato() {
        return abilitato;
    }

    /**
     * @return the assortimentoArticoli
     */
    public boolean isAssortimentoArticoli() {
        return assortimentoArticoli;
    }

    /**
     * @return Returns the escludiSpesometro.
     */
    public boolean isEscludiSpesometro() {
        return escludiSpesometro;
    }

    /**
     * @return the fatturazionePA
     */
    public boolean isFatturazionePA() {
        return fatturazionePA;
    }

    /**
     * @return the raggruppaEffetti
     */
    public boolean isRaggruppaEffetti() {
        return raggruppaEffetti;
    }

    /**
     * @return the riepilogativo
     */
    public boolean isRiepilogativo() {
        return riepilogativo;
    }

    /**
     * @return Returns the splitPayment.
     */
    public boolean isSplitPayment() {
        return splitPayment;
    }

    /**
     * @param abilitato
     *            the abilitato to set.
     */
    public void setAbilitato(boolean abilitato) {
        this.abilitato = abilitato;
    }

    /**
     * @param anagrafica
     *            The anagrafica to set.
     */
    public void setAnagrafica(Anagrafica anagrafica) {
        this.anagrafica = anagrafica;
    }

    /**
     * @param assortimentoArticoli
     *            the assortimentoArticoli to set
     */
    public void setAssortimentoArticoli(boolean assortimentoArticoli) {
        this.assortimentoArticoli = assortimentoArticoli;
    }

    /**
     * @param bloccoSede
     *            the bloccoSede to set
     */
    public void setBloccoSede(BloccoSede bloccoSede) {
        this.bloccoSede = bloccoSede;
    }

    /**
     * @param codice
     *            the codice to set
     */
    public void setCodice(Integer codice) {
        this.codicePrecedente = this.getCodice();
        this.codice = codice;
    }

    /**
     * @param codiceEori
     *            the codiceEori to set
     */
    public void setCodiceEori(String codiceEori) {
        this.codiceEori = codiceEori;
    }

    /**
     *
     * @param codiceEsterno
     *            setta il codice esterno per
     */
    public void setCodiceEsterno(String codiceEsterno) {
        this.codiceEsterno = codiceEsterno;
    }

    /**
     * @param codiceIdentificativoFiscale
     *            the codiceIdentificativoFiscale to set
     */
    public void setCodiceIdentificativoFiscale(String codiceIdentificativoFiscale) {
        this.codiceIdentificativoFiscale = codiceIdentificativoFiscale;
    }

    /**
     * @param codicePrecedente
     *            the codicePrecedente to set
     */
    public void setCodicePrecedente(Integer codicePrecedente) {
        this.codicePrecedente = codicePrecedente;
    }

    /**
     * @param escludiSpesometro
     *            The escludiSpesometro to set.
     */
    public void setEscludiSpesometro(boolean escludiSpesometro) {
        this.escludiSpesometro = escludiSpesometro;
    }

    /**
     * @param fatturazionePA
     *            the fatturazionePA to set
     */
    public void setFatturazionePA(boolean fatturazionePA) {
        this.fatturazionePA = fatturazionePA;
    }

    /**
     * @param fido
     *            the fido to set
     */
    public void setFido(BigDecimal fido) {
        this.fido = fido;
    }

    /**
     * @param note
     *            the note to set.
     */
    public void setNote(String note) {
        this.note = note;
    }

    /**
     * @param noteContabilita
     *            the noteContabilita to set.
     */
    public void setNoteContabilita(String noteContabilita) {
        this.noteContabilita = noteContabilita;
    }

    /**
     * @param noteFatturaPA
     *            the noteFatturaPA to set
     */
    public void setNoteFatturaPA(String noteFatturaPA) {
        this.noteFatturaPA = noteFatturaPA;
    }

    /**
     * @param noteMagazzino
     *            the noteMagazzino to set.
     */
    public void setNoteMagazzino(String noteMagazzino) {
        this.noteMagazzino = noteMagazzino;
    }

    /**
     * @param raggruppaEffetti
     *            the raggruppaEffetti to set
     */
    public void setRaggruppaEffetti(boolean raggruppaEffetti) {
        this.raggruppaEffetti = raggruppaEffetti;
    }

    /**
     * @param riepilogativo
     *            the riepilogativo to set
     */
    public void setRiepilogativo(boolean riepilogativo) {
        this.riepilogativo = riepilogativo;
    }

    /**
     * @param sedi
     *            the sedi to set.
     */
    public void setSedi(java.util.Set<SedeEntita> sedi) {
        this.sedi = sedi;
    }

    /**
     * @param splitPayment
     *            The splitPayment to set.
     */
    public void setSplitPayment(boolean splitPayment) {
        this.splitPayment = splitPayment;
    }

    /**
     * @param tipoAnagrafica
     *            the tipoAnagrafica to set
     */
    public final void setTipoAnagrafica(String tipoAnagrafica) {
        this.tipoAnagrafica = tipoAnagrafica;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("Entita[");
        buffer.append(super.toString());
        buffer.append("abilitato = ").append(abilitato);
        buffer.append(" anagrafica = ").append(anagrafica != null ? anagrafica.getId() : null);
        buffer.append("]");
        return buffer.toString();
    }
}
