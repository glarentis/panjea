package it.eurotn.panjea.ordini.domain;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.envers.AuditJoinTable;
import org.hibernate.envers.Audited;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.ordini.domain.documento.AreaOrdine;
import it.eurotn.panjea.ordini.util.RigaOrdineDTO;
import it.eurotn.panjea.preventivi.domain.RigaPreventivo;
import it.eurotn.panjea.preventivi.domain.documento.AreaPreventivo;
import it.eurotn.util.PanjeaEJBUtil;

/**
 * @author fattazzo
 */
@Entity
@Table(name = "ordi_righe_ordine")
@Audited
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TIPO_RIGA", discriminatorType = DiscriminatorType.STRING, length = 1)
@DiscriminatorValue("R")
@NamedQueries({
        @NamedQuery(name = "RigaOrdine.caricaByAreaOrdine", query = "select distinct r, (select sum(rc.qta*rc.moltQtaOrdine) from RigaMagazzino rc where rc.rigaOrdineCollegata = r) from RigaOrdine r left join fetch r.articolo art left join fetch r.attributi att left join fetch att.tipoAttributo tip left join fetch tip.unitaMisura left join fetch r.categoriaContabileArticolo where r.areaOrdine.id =:paramAreaOrdine order by r.ordinamento "),
        @NamedQuery(name = "RigaOrdine.cancellaByAreaOrdine", query = "delete from RigaOrdine r where r.areaOrdine.id = :paramAreaOrdine"),
        @NamedQuery(name = "RigaOrdine.cancellaRigheComponentiByAreaOrdine", query = "delete from RigaArticoloComponenteOrdine r where r.areaOrdine.id = :paramAreaOrdine"), })
public abstract class RigaOrdine extends EntityBase implements Cloneable {
    private static final long serialVersionUID = 1L;

    @ManyToOne(fetch = FetchType.LAZY)
    protected AreaOrdine areaOrdine;

    private int numeroRiga; // Non usato per adesso

    private double ordinamento;

    /**
     * Campo utilizzato per salvare le informazioni delle gerarchia dei documenti che si riferiscono alla riga. Il
     * formato con il quale verranno salvati sarà<br>
     * [id dell'area]|[id dell'area]|[id dell'area]|[id dell'area]|.....
     */
    private String chiave;

    /**
     * Rappresenta il livello della riga all'interno del documento. Le righe del documento saranno di livello 0 mentre
     * in quelle collegate il livello sarà in base alle righe testata.
     */
    private int livello;

    private boolean evasioneForzata;

    @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE })
    @AuditJoinTable(name = "ordi_righe_ordine_ordi_righe_ordine_aud")
    private Set<RigaOrdine> righeOrdineCollegate;
    /**
     * Punta alla riga che la collega ad un altro documento.<br/>
     * Se rigaOrdineCollegata <> null allora questa riga è una copia dell'originale
     */
    // @ManyToOne(fetch = FetchType.LAZY)
    // private RigaOrdine rigaOrdineCollegata;

    /**
     * Se ho una riga testata la riga è legata ad essa.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    private RigaTestata rigaTestataCollegata;

    @ManyToOne(fetch = FetchType.LAZY)
    protected AreaPreventivo areaPreventivoCollegata;

    @ManyToOne(fetch = FetchType.LAZY)
    private RigaPreventivo rigaPreventivoCollegata;

    @Column(length = 4000)
    private String noteRiga;

    @Column(length = 4000)
    private String noteLinguaRiga;

    /**
     * Indica se le note della riga dovranno essere riportate sul documento di destinazione.
     */
    private boolean noteSuDestinazione;

    private boolean rigaAutomatica;

    /**
     * Inizializza i valori della riga magazzino.
     */
    {
        this.livello = 0;
        this.ordinamento = Calendar.getInstance().getTimeInMillis();
        this.noteSuDestinazione = true;
        this.areaOrdine = new AreaOrdine();
        this.rigaAutomatica = Boolean.FALSE;
        this.righeOrdineCollegate = new HashSet<RigaOrdine>();
    }

    /**
     * @param rigaOrdineCollegata
     *            rigaOrdineCollegata to add
     */
    public void addRigaOrdineCollegata(RigaOrdine rigaOrdineCollegata) {
        if (this.righeOrdineCollegate == null) {
            this.righeOrdineCollegate = new HashSet<RigaOrdine>();
        }
        if (!righeOrdineCollegate.contains(rigaOrdineCollegata)) {
            this.righeOrdineCollegate.add(rigaOrdineCollegata);
        }
    }

    /**
     * @return nuova istanza di una riga magazzino
     */
    protected abstract RigaOrdineDTO creaIstanzaRigaOrdineDTO();

    /**
     * @return riga ordine DTO
     */
    public RigaOrdineDTO creaRigaOrdineDTO() {
        RigaOrdineDTO riga = creaIstanzaRigaOrdineDTO();
        PanjeaEJBUtil.copyProperties(riga, this);
        riga.setRigaCollegata(this.getAreaPreventivoCollegata() != null);
        return riga;
    }

    /**
     * @return the areaOrdine
     */
    public AreaOrdine getAreaOrdine() {
        return areaOrdine;
    }

    /**
     * @return the areaPreventivoCollegata
     */
    public AreaPreventivo getAreaPreventivoCollegata() {
        return areaPreventivoCollegata;
    }

    /**
     * @return the chiave
     */
    public String getChiave() {
        return chiave;
    }

    /**
     * Metodo che restituisce la stringa che rappresenta la rigaMagazzino riportando descrizione,attributi e note.
     *
     * @param stampaAttributi
     *            indica se stampare gli attributi
     * @param stampaNote
     *            indica se stampare le note
     * @return descrizione delle riga
     */
    public final String getDescrizioneRiga(boolean stampaAttributi, boolean stampaNote) {
        return getDescrizioneRiga(stampaAttributi, stampaNote, null);
    }

    /**
     * Metodo che restituisce la stringa che rappresenta la rigaMagazzino riportando descrizione,attributi e note.
     *
     * @param stampaAttributi
     *            indica se stampare gli attributi della riga
     * @param stampaNote
     *            indica se stampare le note
     * @param lingua
     *            lingua da utilizzare
     * @return descrizione della riga
     */
    public String getDescrizioneRiga(boolean stampaAttributi, boolean stampaNote, String lingua) {
        String linguaEntita = (areaOrdine.getDocumento().getSedeEntita() != null)
                ? areaOrdine.getDocumento().getSedeEntita().getLingua() : null;

        if (linguaEntita != null && linguaEntita.equals(lingua)) {
            return getNoteLinguaRiga();
        } else {
            return getNoteRiga();
        }
    }

    /**
     * Metodo che restituisce la stringa che rappresenta la rigaMagazzino riportando descrizione,attributi e note.
     *
     * @param stampaAttributi
     *            indica se stampare gli attributi della riga
     * @param stampaNote
     *            indica se stampare le note
     * @param lingua
     *            lingua da utilizzare
     * @param escludiTagHtml
     *            se true disabilita l'inserimento dei tag html, che non vanno messi nelle stampe su stampante solo
     *            testo.
     * @return descrizione della riga
     */
    public String getDescrizioneRiga(boolean stampaAttributi, boolean stampaNote, String lingua,
            boolean escludiTagHtml) {
        String linguaEntita = (areaOrdine.getDocumento().getSedeEntita() != null)
                ? areaOrdine.getDocumento().getSedeEntita().getLingua() : null;

        if (linguaEntita != null && linguaEntita.equals(lingua)) {
            return getNoteLinguaRiga();
        } else {
            return getNoteRiga();
        }
    }

    /**
     * Metodo che restituisce la stringa che rappresenta la rigaMagazzino riportando descrizione,attributi e note.
     *
     * @param stampaAttributi
     *            indica se stampare gli attributi della riga
     * @param stampaNote
     *            indica se stampare le note
     * @param lingua
     *            lingua da utilizzare
     * @param escludiTagHtml
     *            se true disabilita l'inserimento dei tag html, che non vanno messi nelle stampe su stampante solo
     *            testo.
     * @param stampaConai
     *            se <code>true</code> stampa il dettaglio della gestione conai
     * @return descrizione della riga
     */
    public String getDescrizioneRiga(boolean stampaAttributi, boolean stampaNote, String lingua, boolean escludiTagHtml,
            boolean stampaConai) {
        String linguaEntita = (areaOrdine.getDocumento().getSedeEntita() != null)
                ? areaOrdine.getDocumento().getSedeEntita().getLingua() : null;

        if (linguaEntita != null && linguaEntita.equals(lingua)) {
            return getNoteLinguaRiga();
        } else {
            return getNoteRiga();
        }
    }

    /**
     * @return the livello
     */
    public int getLivello() {
        return livello;
    }

    /**
     * @return the noteLinguaRiga
     */
    public String getNoteLinguaRiga() {
        return noteLinguaRiga;
    }

    /**
     * @param lingua
     *            lingua da utilizzare, se null viene utilizzata quella aziendale
     * @return restituisce le note formattate per la stampa o null se non contiene testo utile (ad es. nel caso di soli
     *         tag html senza contenuto)
     */
    public String getNotePerStampa(String lingua) {
        String linguaEntita = (areaOrdine.getDocumento().getSedeEntita() != null)
                ? areaOrdine.getDocumento().getSedeEntita().getLingua() : null;

        if (linguaEntita != null && linguaEntita.equals(lingua)) {
            return getNoteLinguaRiga();
        } else {
            return getNoteRiga();
        }
    }

    /**
     * @return the noteRiga
     */
    public String getNoteRiga() {
        return noteRiga;
    }

    /**
     * @return Returns the numeroRiga.
     */
    public int getNumeroRiga() {
        return numeroRiga;
    }

    /**
     * @return the ordinamento
     */
    public double getOrdinamento() {
        return ordinamento;
    }

    /**
     * @return the rigaPreventivoCollegata
     */
    public RigaPreventivo getRigaPreventivoCollegata() {
        return rigaPreventivoCollegata;
    }

    /**
     *
     * @return riga testata al quale è collegata la riga.
     */
    public RigaTestata getRigaTestataCollegata() {
        return rigaTestataCollegata;
    }

    /**
     * @return the rigaOrdineCollegata
     */
    // public RigaOrdine getRigaOrdineCollegata() {
    // return rigaOrdineCollegata;
    // }

    /**
     * @return the righeOrdineCollegate
     */
    public Set<RigaOrdine> getRigheOrdineCollegate() {
        return righeOrdineCollegate;
    }

    /**
     * @return the evasioneForzata
     */
    public boolean isEvasioneForzata() {
        return evasioneForzata;
    }

    /**
     * @return the noteSuDestinazione
     */
    public boolean isNoteSuDestinazione() {
        return noteSuDestinazione;
    }

    /**
     * @return the rigaAutomatica
     */
    public boolean isRigaAutomatica() {
        return rigaAutomatica;
    }

    /**
     * @param areaOrdine
     *            the areaOrdine to set
     */
    public void setAreaOrdine(AreaOrdine areaOrdine) {
        this.areaOrdine = areaOrdine;
    }

    /**
     * @param areaPreventivoCollegata
     *            the areaPreventivoCollegata to set
     */
    public void setAreaPreventivoCollegata(AreaPreventivo areaPreventivoCollegata) {
        this.areaPreventivoCollegata = areaPreventivoCollegata;
    }

    /**
     * @param chiave
     *            the chiave to set
     */
    public void setChiave(String chiave) {
        this.chiave = chiave;
    }

    /**
     * @param evasioneForzata
     *            the evasioneForzata to set
     */
    public void setEvasioneForzata(boolean evasioneForzata) {
        this.evasioneForzata = evasioneForzata;
    }

    /**
     * @param livello
     *            the livello to set
     */
    public void setLivello(int livello) {
        this.livello = livello;
    }

    /**
     * @param noteLinguaRiga
     *            the noteLinguaRiga to set
     */
    public void setNoteLinguaRiga(String noteLinguaRiga) {
        this.noteLinguaRiga = noteLinguaRiga;
    }

    /**
     * @param noteRiga
     *            the noteRiga to set
     */
    public void setNoteRiga(String noteRiga) {
        if (noteRiga != null && noteRiga.length() > 4000) {
            noteRiga = noteRiga.substring(0, 4000);
        }
        this.noteRiga = noteRiga;
    }

    /**
     * @param noteSuDestinazione
     *            the noteSuDestinazione to set
     */
    public void setNoteSuDestinazione(boolean noteSuDestinazione) {
        this.noteSuDestinazione = noteSuDestinazione;
    }

    /**
     * @param numeroRiga
     *            The numeroRiga to set.
     */
    public void setNumeroRiga(int numeroRiga) {
        this.numeroRiga = numeroRiga;
    }

    /**
     * @param ordinamento
     *            the ordinamento to set
     */
    public void setOrdinamento(double ordinamento) {
        this.ordinamento = ordinamento;
    }

    /**
     * @param rigaAutomatica
     *            the rigaAutomatica to set
     */
    public void setRigaAutomatica(boolean rigaAutomatica) {
        this.rigaAutomatica = rigaAutomatica;
    }

    /**
     * @param rigaPreventivoCollegata
     *            the rigaPreventivoCollegata to set
     */
    public void setRigaPreventivoCollegata(RigaPreventivo rigaPreventivoCollegata) {
        this.rigaPreventivoCollegata = rigaPreventivoCollegata;
    }

    /**
     * @param rigaOrdineCollegata
     *            the rigaOrdineCollegata to set
     */
    // public void setRigaOrdineCollegata(RigaOrdine rigaOrdineCollegata) {
    // this.rigaOrdineCollegata = rigaOrdineCollegata;
    // }

    /**
     *
     * @param rigaTestataCollegata
     *            setta la riga ordine (nello stesso ordine) alla quale fa riferimento questa riga.
     */
    public void setRigaTestataCollegata(RigaTestata rigaTestataCollegata) {
        this.rigaTestataCollegata = rigaTestataCollegata;
    }

    /**
     * @param righeOrdineCollegate
     *            the righeOrdineCollegate to set
     */
    public void setRigheOrdineCollegate(Set<RigaOrdine> righeOrdineCollegate) {
        this.righeOrdineCollegate = righeOrdineCollegate;
    }

    /**
     * Costruisce una <code>String</code> con tutti gli attributi accodati con questo formato nome attributo = valore.
     *
     * @return a <code>String</code> come risultato di questo oggetto
     */
    @Override
    public String toString() {

        StringBuffer retValue = new StringBuffer();

        retValue.append("RigaOrdine[ ").append(super.toString()).append(" numeroRiga = ").append(this.numeroRiga)
                .append(" ordinamento = ").append(this.ordinamento).append(" ]");

        return retValue.toString();
    }

}