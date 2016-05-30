package it.eurotn.panjea.magazzino.domain;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.anagrafica.documenti.domain.IAreaDocumento;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.util.RigaMagazzinoDTO;
import it.eurotn.panjea.ordini.domain.RigaOrdine;
import it.eurotn.panjea.ordini.domain.documento.AreaOrdine;
import it.eurotn.util.PanjeaEJBUtil;

/**
 * Classe astratta che descrive una riga per l'area magazzino.
 *
 * @author giangi
 */
@Entity
@Audited
@Table(name = "maga_righe_magazzino")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TIPO_RIGA", discriminatorType = DiscriminatorType.STRING, length = 2)
@DiscriminatorValue("R")
@NamedQueries({
        @NamedQuery(name = "RigaMagazzino.caricaByAreaByArticolo", query = "select distinct r from RigaMagazzino r left join fetch r.articolo art left join fetch r.attributi att left join fetch att.tipoAttributo tip left join fetch tip.unitaMisura left join fetch r.categoriaContabileArticolo where r.areaMagazzino.id =:paramIdAreaMagazzino and r.articolo.id=:paramIdArticolo order by r.ordinamento "),
        @NamedQuery(name = "RigaMagazzino.cancellaByAreaMagazzino", query = "delete from RigaMagazzino r where r.areaMagazzino.id = :paramAreaMagazzino"),
        @NamedQuery(name = "RigaMagazzino.cancellaRigheComponentiByAreaMagazzino", query = "delete from RigaArticoloComponente r where r.areaMagazzino.id = :paramAreaMagazzino"),
        @NamedQuery(name = "RigaMagazzino.azzeraPadriRigheComponentiByAreaMagazzino", query = "update from RigaArticoloComponente r set r.rigaPadre=null where r.areaMagazzino.id = :paramAreaMagazzino"),
        @NamedQuery(name = "RigaMagazzino.caricaByRigaCollegata", query = "select r from RigaMagazzino r where r.rigaMagazzinoCollegata.id = :paramRigaMagazzinoCollegataId") })
public abstract class RigaMagazzino extends EntityBase implements Cloneable {
    private static final long serialVersionUID = 1L;

    @ManyToOne(fetch = FetchType.LAZY)
    protected AreaMagazzino areaMagazzino;

    private boolean somministrazione;

    private int numeroRiga; // Non usato per adesso

    private double ordinamento;

    /**
     * Se ho una riga testata la riga è legata ad essa.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    private RigaTestata rigaTestataMagazzinoCollegata;

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

    /**
     * Punta alla riga che la collega ad un altro documento.<br/>
     * Se rigaMagazzinoCollegata <> null allora questa riga è una copia dell'originale
     */
    @ManyToOne(fetch = FetchType.LAZY)
    private RigaMagazzino rigaMagazzinoCollegata;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotAudited
    private RigaOrdine rigaOrdineCollegata;

    @ManyToOne(fetch = FetchType.LAZY)
    protected AreaOrdine areaOrdineCollegata;

    @ManyToOne(fetch = FetchType.LAZY)
    protected AreaMagazzino areaMagazzinoCollegata;

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
        this.rigaAutomatica = Boolean.FALSE;
        this.somministrazione = false;
    }

    /**
     *
     * @return nuova istanza di una riga magazzino
     */
    protected abstract RigaMagazzinoDTO creaIstanzaRigaMagazzinoDTO();

    /**
     * Crea e associa una nuova riga collegata sull'area magazzino passata come parametro.
     *
     * @param areaMagazzino
     *            {@link AreaMagazzino}
     * @param ordinamentoRigaCollagata
     *            ordinamento per la nuova riga collegata
     * @return riga collegata
     */
    public abstract RigaMagazzino creaRigaCollegata(AreaMagazzino areaMagazzino, double ordinamentoRigaCollagata);

    /**
     *
     * @return riga magazzino creata partendo dalla classe di dominio
     */
    public RigaMagazzinoDTO creaRigaMagazzinoDTO() {
        RigaMagazzinoDTO riga = creaIstanzaRigaMagazzinoDTO();
        PanjeaEJBUtil.copyProperties(riga, this);
        riga.setRigaCollegata(this.getAreaCollegata() != null);
        return riga;
    }

    /**
     * @return the areaOrdineCollegata
     */
    public IAreaDocumento getAreaCollegata() {

        if (areaMagazzinoCollegata != null) {
            return areaMagazzinoCollegata;
        } else {
            return areaOrdineCollegata;
        }
    }

    /**
     * @return the areaMagazzino
     */
    public AreaMagazzino getAreaMagazzino() {
        return areaMagazzino;
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
     *            indica se stampare gli attributi della riga
     * @param stampaNote
     *            indica se stampare le note
     * @return descrizione della riga
     */
    public final String getDescrizioneRiga(boolean stampaAttributi, boolean stampaNote) {
        return getDescrizioneRiga(stampaAttributi, stampaNote, null, false);
    }

    /**
     * Ritorna la descrizione della riga con vari elementi collegati dipendente dai prametri.
     *
     * @param stampaAttributi
     *            indica se stampare gli attributi della riga
     * @param stampaNote
     *            indica se stampare le note
     * @param lingua
     *            lingua per il report
     * @return descrizione della riga
     */
    public String getDescrizioneRiga(boolean stampaAttributi, boolean stampaNote, String lingua) {
        return getDescrizioneRiga(stampaAttributi, stampaNote, lingua, false);
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
        String linguaEntita = (areaMagazzino.getDocumento().getSedeEntita() != null)
                ? areaMagazzino.getDocumento().getSedeEntita().getLingua() : null;

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
        String linguaEntita = (areaMagazzino.getDocumento().getSedeEntita() != null)
                ? areaMagazzino.getDocumento().getSedeEntita().getLingua() : null;

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
        String linguaEntita = (areaMagazzino.getDocumento().getSedeEntita() != null)
                ? areaMagazzino.getDocumento().getSedeEntita().getLingua() : null;

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
     * @return the ordinamento
     */
    public double getOrdinamento() {
        return ordinamento;
    }

    /**
     * @return the rigaMagazzinoCollegata
     */
    public RigaMagazzino getRigaMagazzinoCollegata() {
        return rigaMagazzinoCollegata;
    }

    /**
     * @return Returns the rigaOrdineCollegata.
     */
    public RigaOrdine getRigaOrdineCollegata() {
        return rigaOrdineCollegata;
    }

    /**
     * @return Returns the rigaTestataMagazzinoCollegata.
     */
    public RigaTestata getRigaTestataMagazzinoCollegata() {
        return rigaTestataMagazzinoCollegata;
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
     * @return Returns the somministrazione.
     */
    public boolean isSomministrazione() {
        return somministrazione;
    }

    /**
     * @param areaDocumento
     *            the areaDocumento to set
     */
    public void setAreaCollegata(IAreaDocumento areaDocumento) {
        if (areaDocumento instanceof AreaMagazzino) {
            this.areaMagazzinoCollegata = (AreaMagazzino) areaDocumento;
        } else {
            this.areaOrdineCollegata = (AreaOrdine) areaDocumento;
        }
    }

    /**
     * @param areaMagazzino
     *            the areaMagazzino to set
     */
    public void setAreaMagazzino(AreaMagazzino areaMagazzino) {
        this.areaMagazzino = areaMagazzino;
    }

    /**
     * @param areaMagazzinoCollegata
     *            the areaMagazzinoCollegata to set
     */
    public void setAreaMagazzinoCollegata(AreaMagazzino areaMagazzinoCollegata) {
        this.areaMagazzinoCollegata = areaMagazzinoCollegata;
    }

    /**
     * @param areaOrdineCollegata
     *            the areaOrdineCollegata to set
     */
    public void setAreaOrdineCollegata(AreaOrdine areaOrdineCollegata) {
        this.areaOrdineCollegata = areaOrdineCollegata;
    }

    /**
     * @param chiave
     *            the chiave to set
     */
    public void setChiave(String chiave) {
        this.chiave = chiave;
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
     * @param rigaMagazzinoCollegata
     *            the rigaMagazzinoCollegata to set
     */
    public void setRigaMagazzinoCollegata(RigaMagazzino rigaMagazzinoCollegata) {
        this.rigaMagazzinoCollegata = rigaMagazzinoCollegata;
    }

    /**
     * @param rigaOrdineCollegata
     *            The rigaOrdineCollegata to set.
     */
    public void setRigaOrdineCollegata(RigaOrdine rigaOrdineCollegata) {
        this.rigaOrdineCollegata = rigaOrdineCollegata;
    }

    /**
     * @param rigaTestataMagazzinoCollegata
     *            The rigaTestataMagazzinoCollegata to set.
     */
    public void setRigaTestataMagazzinoCollegata(RigaTestata rigaTestataMagazzinoCollegata) {
        this.rigaTestataMagazzinoCollegata = rigaTestataMagazzinoCollegata;
    }

    /**
     * @param somministrazione
     *            The somministrazione to set.
     */
    public void setSomministrazione(boolean somministrazione) {
        this.somministrazione = somministrazione;
    }

    /**
     * Costruisce una <code>String</code> con tutti gli attributi accodati con questo formato nome attributo = valore.
     *
     * @return a <code>String</code> come risultato di questo oggetto
     */
    @Override
    public String toString() {

        StringBuilder retValue = new StringBuilder();

        retValue.append("RigaMagazzino[ ").append(super.toString()).append(" numeroRiga = ").append(this.numeroRiga)
                .append(" ordinamento = ").append(this.ordinamento).append(" ]");

        return retValue.toString();
    }

}
