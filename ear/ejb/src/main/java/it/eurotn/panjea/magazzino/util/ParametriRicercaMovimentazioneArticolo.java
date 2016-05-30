package it.eurotn.panjea.magazzino.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.CollectionOfElements;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino.ESezioneTipoMovimento;
import it.eurotn.panjea.parametriricerca.domain.AbstractParametriRicerca;
import it.eurotn.panjea.parametriricerca.domain.Periodo;

/**
 * @author fattazzo
 */
@Entity
@Table(name = "para_ricerca_movimentazione_articolo")
public class ParametriRicercaMovimentazioneArticolo extends AbstractParametriRicerca implements Serializable {

    private static final long serialVersionUID = -3890020027848927944L;

    @ManyToOne(optional = true)
    private ArticoloLite articoloLite;

    @ManyToOne(optional = true)
    private DepositoLite depositoLite;

    private Periodo periodo;

    @Transient
    private boolean effettuaRicerca;

    @CollectionOfElements(targetElement = ESezioneTipoMovimento.class, fetch = FetchType.EAGER)
    @Fetch(FetchMode.JOIN)
    @JoinTable(name = "para_movimentazione_articolo_sezione_tipo_movimento", joinColumns = @JoinColumn(name = "id") )
    @Column(name = "sezione", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private List<ESezioneTipoMovimento> sezioniTipoMovimento = null;

    @ManyToOne(optional = true)
    private EntitaLite entitaLite = null;

    @ManyToOne(optional = true)
    private SedeEntita sedeEntita = null;

    private boolean giacenzaProgressivaInizialeAZero;

    {
        this.depositoLite = null;
        this.effettuaRicerca = false;
        if (sezioniTipoMovimento == null) {
            this.sezioniTipoMovimento = new ArrayList<ESezioneTipoMovimento>();
            sezioniTipoMovimento.add(ESezioneTipoMovimento.ACQUISTATOVENDUTO);
            sezioniTipoMovimento.add(ESezioneTipoMovimento.GENERICO);
        }
        this.periodo = new Periodo();
        this.giacenzaProgressivaInizialeAZero = false;
    }

    /**
     * Costruttore,inizializza i valori degli attributi.
     */
    public ParametriRicercaMovimentazioneArticolo() {
        super();
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
     * @return the entitaLite
     */
    public EntitaLite getEntitaLite() {
        return entitaLite;
    }

    /**
     * @return Returns the periodo.
     */
    public Periodo getPeriodo() {
        if (periodo == null) {
            this.periodo = new Periodo();
        }
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
     */
    public List<ESezioneTipoMovimento> getSezioniTipoMovimento() {
        return sezioniTipoMovimento;
    }

    /**
     * @return the effettuaRicerca
     */
    @Override
    public boolean isEffettuaRicerca() {
        return effettuaRicerca;
    }

    /**
     * @return the giacenzaProgressivaInizialeAZero
     */
    public boolean isGiacenzaProgressivaInizialeAZero() {
        return giacenzaProgressivaInizialeAZero;
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
     * @param effettuaRicerca
     *            the effettuaRicerca to set
     */
    @Override
    public void setEffettuaRicerca(boolean effettuaRicerca) {
        this.effettuaRicerca = effettuaRicerca;
    }

    /**
     * @param entitaLite
     *            the entitaLite to set
     */
    public void setEntitaLite(EntitaLite entitaLite) {
        this.entitaLite = entitaLite;
    }

    /**
     * @param giacenzaProgressivaInizialeAZero
     *            the giacenzaProgressivaInizialeAZero to set
     */
    public void setGiacenzaProgressivaInizialeAZero(boolean giacenzaProgressivaInizialeAZero) {
        this.giacenzaProgressivaInizialeAZero = giacenzaProgressivaInizialeAZero;
    }

    /**
     * @param periodo
     *            The periodo to set.
     */
    public void setPeriodo(Periodo periodo) {
        this.periodo = periodo;
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
    public void setSezioniTipoMovimento(List<ESezioneTipoMovimento> paramSezioniTipoMovimento) {
        this.sezioniTipoMovimento = paramSezioniTipoMovimento;
    }

}
