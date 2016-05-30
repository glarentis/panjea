package it.eurotn.panjea.magazzino.domain;

import java.util.Set;
import java.util.TreeSet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.CollectionOfElements;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Index;

import it.eurotn.entity.EntityBase;

/**
 * @author fattazzo
 *
 */
@Entity(name = "SchedaArticolo")
@Table(name = "maga_schede_articolo")
@NamedQueries({
        @NamedQuery(name = "SchedaArticolo.caricaSchedaArticolo", query = "select sa from SchedaArticolo sa where sa.articolo.id = :idArticolo and sa.articolo.codiceAzienda = :codiceAzienda and sa.anno = :anno and sa.mese = :mese"),
        @NamedQuery(name = "SchedaArticolo.caricaArticoliStampati", query = "select a.id as id,a.codice as codice, a.descrizioneLinguaAziendale as descrizione,a.barCode as barCode,a.abilitato as abilitato from SchedaArticolo sa inner join sa.articolo a where a.codiceAzienda = :codiceAzienda and sa.anno = :anno and sa.mese = :mese and sa.stato = 1"),
        @NamedQuery(name = "SchedaArticolo.caricaArticoliNonValidi", query = "select a.id as id,a.codice as codice, a.descrizioneLinguaAziendale as descrizione,a.barCode as barCode,a.abilitato as abilitato from SchedaArticolo sa inner join sa.articolo a where a.codiceAzienda = :codiceAzienda and sa.anno = :anno and sa.mese = :mese and sa.stato = 2"),
        @NamedQuery(name = "SchedaArticolo.caricaArticoliInElaborazione", query = "select a.id as id,a.codice as codice, a.descrizioneLinguaAziendale as descrizione,a.barCode as barCode,a.abilitato as abilitato from SchedaArticolo sa inner join sa.articolo a where a.codiceAzienda = :codiceAzienda and sa.anno = :anno and sa.mese = :mese and sa.stato = 0"),
        @NamedQuery(name = "SchedaArticolo.caricaArticoliDaStampare", query = "select a.id as id,a.codice as codice, a.descrizioneLinguaAziendale as descrizione,a.barCode as barCode,a.abilitato as abilitato from Articolo a where a.gestioneSchedaArticolo=true and (a.codiceAzienda = :codiceAzienda and a.gestioneSchedaArticoloAnno < :anno or ( a.gestioneSchedaArticoloAnno = :anno and  a.gestioneSchedaArticoloMese <= :mese)) and a.id not in (select sa.articolo.id from SchedaArticolo sa where sa.anno=:anno and sa.mese=:mese)") })
public class SchedaArticolo extends EntityBase {

    public enum StatoScheda {
        IN_ELABORAZIONE, STAMPATO, NON_VALIDO
    }

    private static final long serialVersionUID = 1879666384212297272L;

    @Column(length = 10, nullable = false)
    @Index(name = "index_codiceAzienda")
    private String codiceAzienda = null;

    @ManyToOne
    private Articolo articolo;

    private Integer anno;

    private Integer mese;

    private StatoScheda stato;

    @CollectionOfElements(targetElement = String.class, fetch = FetchType.EAGER)
    @Fetch(FetchMode.JOIN)
    @JoinTable(name = "maga_schede_articolo_note", joinColumns = @JoinColumn(name = "id") )
    @Column(name = "note")
    private Set<String> note;

    {
        stato = StatoScheda.IN_ELABORAZIONE;
        note = new TreeSet<String>();
    }

    /**
     * @return the anno
     */
    public Integer getAnno() {
        return anno;
    }

    /**
     * @return the articolo
     */
    public Articolo getArticolo() {
        return articolo;
    }

    /**
     * @return the codiceAzienda
     */
    public String getCodiceAzienda() {
        return codiceAzienda;
    }

    /**
     * @return the mese
     */
    public Integer getMese() {
        return mese;
    }

    /**
     * @return the note
     */
    public Set<String> getNote() {
        return note;
    }

    /**
     * @return the stato
     */
    public StatoScheda getStato() {
        return stato;
    }

    /**
     * @param anno
     *            the anno to set
     */
    public void setAnno(Integer anno) {
        this.anno = anno;
    }

    /**
     * @param articolo
     *            the articolo to set
     */
    public void setArticolo(Articolo articolo) {
        this.articolo = articolo;
    }

    /**
     * @param codiceAzienda
     *            the codiceAzienda to set
     */
    public void setCodiceAzienda(String codiceAzienda) {
        this.codiceAzienda = codiceAzienda;
    }

    /**
     * @param mese
     *            the mese to set
     */
    public void setMese(Integer mese) {
        this.mese = mese;
    }

    /**
     * @param note
     *            the note to set
     */
    public void setNote(Set<String> note) {
        this.note = note;
    }

    /**
     * @param stato
     *            the stato to set
     */
    public void setStato(StatoScheda stato) {
        this.stato = stato;
    }

    @Override
    public String toString() {
        return "SchedaArticolo [articolo=" + articolo + ", anno=" + anno + ", mese=" + mese + ", stato=" + stato + "]";
    }
}
