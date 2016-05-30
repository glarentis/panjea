package it.eurotn.panjea.magazzino.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CollectionOfElements;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import it.eurotn.panjea.agenti.domain.lite.AgenteLite;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.util.parametriricerca.ITableHeader;
import it.eurotn.panjea.anagrafica.util.parametriricerca.TableHeaderObject;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino.ESezioneTipoMovimento;
import it.eurotn.panjea.parametriricerca.domain.AbstractParametriRicerca;
import it.eurotn.panjea.parametriricerca.domain.Periodo;

/**
 * @author Leonardo
 */
@Entity
@Table(name = "para_ricerca_movimentazione")
public class ParametriRicercaMovimentazione extends AbstractParametriRicerca implements Serializable, ITableHeader {

    private static final long serialVersionUID = -3890020027848927944L;

    @ManyToOne(optional = true)
    private ArticoloLite articoloLite = null;

    @ManyToOne(optional = true)
    private DepositoLite depositoLite = null;

    @ManyToOne(optional = true)
    private EntitaLite entitaLite = null;

    @ManyToOne(optional = true)
    private SedeEntita sedeEntita = null;

    private Periodo periodo;

    private boolean righeOmaggio = false;

    @CollectionOfElements(targetElement = ESezioneTipoMovimento.class, fetch = FetchType.EAGER)
    @Fetch(FetchMode.JOIN)
    @JoinTable(name = "para_ricerca_movimentazione_sezione_tipo_movimento", joinColumns = @JoinColumn(name = "id") )
    @Column(name = "sezione", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private Set<ESezioneTipoMovimento> sezioniTipoMovimento = null;

    @ManyToMany(fetch = FetchType.EAGER)
    @Fetch(FetchMode.JOIN)
    private Set<TipoAreaMagazzino> tipiAreaMagazzino = null;

    @Column(length = 400)
    private String noteRiga = null;

    @Column(length = 255)
    private String descrizioneRiga = null;

    @ManyToOne(optional = true)
    private AgenteLite agenteLite;

    {
        if (tipiAreaMagazzino == null) {
            this.tipiAreaMagazzino = new HashSet<TipoAreaMagazzino>();
        }
        if (sezioniTipoMovimento == null) {
            this.sezioniTipoMovimento = new HashSet<ESezioneTipoMovimento>();
        }
        sezioniTipoMovimento.add(ESezioneTipoMovimento.ACQUISTATOVENDUTO);
        sezioniTipoMovimento.add(ESezioneTipoMovimento.GENERICO);

        if (periodo == null) {
            periodo = new Periodo();
        }
    }

    /**
     * Costruttore,inizializza i valori degli attributi.
     */
    public ParametriRicercaMovimentazione() {
        super();
    }

    /**
     * @return the agenteLite
     */
    public AgenteLite getAgenteLite() {
        return agenteLite;
    }

    /**
     * @return the articoloLite
     */
    public ArticoloLite getArticoloLite() {
        return articoloLite;
    }

    /**
     * @return the deposito
     */
    public DepositoLite getDepositoLite() {
        return depositoLite;
    }

    /**
     * @return Returns the descrizioneRiga.
     */
    public String getDescrizioneRiga() {
        return descrizioneRiga;
    }

    /**
     * @return the entitaLite
     */
    public EntitaLite getEntitaLite() {
        return entitaLite;
    }

    @Override
    public List<TableHeaderObject> getHeaderValues() {
        List<TableHeaderObject> values = new ArrayList<TableHeaderObject>();

        if (periodo.getDataIniziale() != null) {
            values.add(new TableHeaderObject("dataIniziale", periodo.getDataIniziale()));
        }
        if (periodo.getDataFinale() != null) {
            values.add(new TableHeaderObject("dataFinale", periodo.getDataFinale()));
        }
        if (articoloLite != null && articoloLite.getId() != null) {
            values.add(new TableHeaderObject("articoloLite", articoloLite));
        }
        if (depositoLite != null && depositoLite.getId() != null) {
            values.add(new TableHeaderObject("depositoLite", depositoLite));
        }
        if (entitaLite != null && entitaLite.getId() != null) {
            values.add(new TableHeaderObject("entitaLite", entitaLite));
        }
        if (agenteLite != null && agenteLite.getId() != null) {
            values.add(new TableHeaderObject("agenteLite", agenteLite));
        }
        if (noteRiga != null && !noteRiga.isEmpty()) {
            values.add(new TableHeaderObject("noteRiga", noteRiga));
        }
        if (sezioniTipoMovimento != null && !sezioniTipoMovimento.isEmpty()
                && sezioniTipoMovimento.size() != ESezioneTipoMovimento.values().length) {
            values.add(new TableHeaderObject("sezioniTipoMovimento", sezioniTipoMovimento));
        }
        if (tipiAreaMagazzino != null && !tipiAreaMagazzino.isEmpty()) {
            values.add(new TableHeaderObject("tipiDocumento", tipiAreaMagazzino));
        }

        if (values.isEmpty()) {
            return null;
        } else {
            return values;
        }
    }

    /**
     * @return the noteRiga
     */
    public String getNoteRiga() {
        return noteRiga;
    }

    /**
     * @return Returns the periodo.
     */
    public Periodo getPeriodo() {
        return periodo;
    }

    /**
     * @return the sedeEntita
     */
    public SedeEntita getSedeEntita() {
        return sedeEntita;
    }

    /**
     * @return the sezioneTipoMovimento
     * @uml.property name="sezioniTipoMovimento"
     */
    public Set<ESezioneTipoMovimento> getSezioniTipoMovimento() {
        return sezioniTipoMovimento;
    }

    /**
     * @return the tipiAreaMagazzino
     * @uml.property name="tipiAreaMagazzino"
     */
    public Set<TipoAreaMagazzino> getTipiAreaMagazzino() {
        return tipiAreaMagazzino;
    }

    /**
     * @return Returns the righeOmaggio.
     */
    public boolean isRigheOmaggio() {
        return righeOmaggio;
    }

    /**
     * @param agenteLite
     *            the agenteLite to set
     */
    public void setAgenteLite(AgenteLite agenteLite) {
        this.agenteLite = agenteLite;
    }

    /**
     * @param articoloLite
     *            the articoloLite to set
     */
    public void setArticoloLite(ArticoloLite articoloLite) {
        this.articoloLite = articoloLite;
    }

    /**
     * @param depositoLite
     *            the deposito to set
     */
    public void setDepositoLite(DepositoLite depositoLite) {
        this.depositoLite = depositoLite;
    }

    /**
     * @param descrizioneRiga
     *            The descrizioneRiga to set.
     */
    public void setDescrizioneRiga(String descrizioneRiga) {
        this.descrizioneRiga = descrizioneRiga;
    }

    /**
     * @param entitaLite
     *            the entitaLite to set
     */
    public void setEntitaLite(EntitaLite entitaLite) {
        this.entitaLite = entitaLite;
    }

    /**
     * @param noteRiga
     *            the noteRiga to set
     */
    public void setNoteRiga(String noteRiga) {
        this.noteRiga = noteRiga;
    }

    /**
     * @param periodo
     *            The periodo to set.
     */
    public void setPeriodo(Periodo periodo) {
        this.periodo = periodo;
    }

    /**
     * @param righeOmaggio
     *            The righeOmaggio to set.
     */
    public void setRigheOmaggio(boolean righeOmaggio) {
        this.righeOmaggio = righeOmaggio;
    }

    /**
     * @param sedeEntita
     *            the sedeEntita to set
     */
    public void setSedeEntita(SedeEntita sedeEntita) {
        this.sedeEntita = sedeEntita;
    }

    /**
     * @param paramSezioniTipoMovimento
     *            the sezioniTipoMovimento to set
     */
    public void setSezioniTipoMovimento(Set<ESezioneTipoMovimento> paramSezioniTipoMovimento) {
        this.sezioniTipoMovimento = paramSezioniTipoMovimento;
    }

    /**
     * @param tipiAreaMagazzino
     *            the tipiAreaMagazzino to set
     */
    public void setTipiAreaMagazzino(Set<TipoAreaMagazzino> tipiAreaMagazzino) {
        this.tipiAreaMagazzino = tipiAreaMagazzino;
    }

}
