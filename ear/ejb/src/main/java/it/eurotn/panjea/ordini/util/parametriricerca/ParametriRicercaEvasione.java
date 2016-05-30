package it.eurotn.panjea.ordini.util.parametriricerca;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import it.eurotn.panjea.agenti.domain.lite.AgenteLite;
import it.eurotn.panjea.anagrafica.documenti.domain.CodiceDocumento;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.util.parametriricerca.ITableHeader;
import it.eurotn.panjea.anagrafica.util.parametriricerca.TableHeaderObject;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.ordini.domain.documento.AreaOrdine;
import it.eurotn.panjea.ordini.domain.documento.TipoAreaOrdine;
import it.eurotn.panjea.parametriricerca.domain.AbstractParametriRicerca;
import it.eurotn.panjea.parametriricerca.domain.Periodo;
import it.eurotn.panjea.parametriricerca.domain.Periodo.TipoPeriodo;

/**
 * @author fattazzo
 */
@Entity
@Table(name = "para_ricerca_evasione_ordine")
public class ParametriRicercaEvasione extends AbstractParametriRicerca implements ITableHeader {

    private static final long serialVersionUID = -7465626052231535142L;

    @ManyToOne(optional = true)
    private AgenteLite agente;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "dataIniziale", column = @Column(name = "dataRegistrazioneIniziale") ),
            @AttributeOverride(name = "dataFinale", column = @Column(name = "dataRegistrazioneFinale") ),
            @AttributeOverride(name = "tipoPeriodo", column = @Column(name = "dataRegistrazioneTipoPeriodo") ),
            @AttributeOverride(name = "dataInizialeNull", column = @Column(name = "dataRegistrazioneDataInizialeNull") ),
            @AttributeOverride(name = "numeroGiorni", column = @Column(name = "dataRegistrazioneNumeroGiorni") ) })
    private Periodo dataRegistrazione;

    private Date dataInizioTrasporto;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "codice", column = @Column(name = "numeroDocumentoIniziale", length = 30) ),
            @AttributeOverride(name = "codiceOrder", column = @Column(name = "numeroDocumentoInizialeOrder", length = 60) ) })
    private CodiceDocumento numeroDocumentoIniziale = null;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "codice", column = @Column(name = "numeroDocumentoFinale", length = 30) ),
            @AttributeOverride(name = "codiceOrder", column = @Column(name = "numeroDocumentoFinaleOrder", length = 60) ) })
    private CodiceDocumento numeroDocumentoFinale = null;

    @Embedded
    @AttributeOverrides({ @AttributeOverride(name = "dataIniziale", column = @Column(name = "dataConsegnaIniziale") ),
            @AttributeOverride(name = "dataFinale", column = @Column(name = "dataConsegnaFinale") ),
            @AttributeOverride(name = "tipoPeriodo", column = @Column(name = "dataConsegnaTipoPeriodo") ),
            @AttributeOverride(name = "dataInizialeNull", column = @Column(name = "dataConsegnaDataInizialeNull") ),
            @AttributeOverride(name = "numeroGiorni", column = @Column(name = "dataConsegnaNumeroGiorni") ) })
    private Periodo dataConsegna;

    @ManyToMany(fetch = FetchType.EAGER)
    @Fetch(FetchMode.JOIN)
    private Set<TipoAreaOrdine> tipiAreaOrdine = null;

    @ManyToOne(optional = true)
    private EntitaLite entita;

    private Boolean trasportoCuraMittente;

    @ManyToOne(optional = true)
    private SedeEntita sedeEntita;

    @ManyToOne(optional = true)
    private DepositoLite deposito;

    @ManyToOne(optional = true)
    private ArticoloLite articolo;

    @Enumerated
    private TipoEntita tipoEntita;

    @Transient
    private List<AreaOrdine> areeOrdine;

    @Transient
    private Long dataCreazioneOrdini;

    @Transient
    private boolean evadiOrdini;

    /**
     * Costruttore.
     */
    public ParametriRicercaEvasione() {
        if (dataRegistrazione == null) {
            dataRegistrazione = new Periodo();
            dataRegistrazione.setTipoPeriodo(TipoPeriodo.ANNO_CORRENTE);
            dataRegistrazione.setTipoPeriodo(TipoPeriodo.DATE);
        }
        if (dataConsegna == null) {
            dataConsegna = new Periodo();
            dataConsegna.setTipoPeriodo(TipoPeriodo.NESSUNO);
        }

        if (tipiAreaOrdine == null) {
            tipiAreaOrdine = new HashSet<TipoAreaOrdine>();
        }

        this.tipoEntita = TipoEntita.CLIENTE;

        this.dataCreazioneOrdini = null;
        this.evadiOrdini = false;

        this.numeroDocumentoIniziale = new CodiceDocumento();
        this.numeroDocumentoFinale = new CodiceDocumento();
    }

    /**
     * Crea i parametri per la ricerca da un'area magazzino.
     *
     * @param areaMagazzino
     *            area magazzino
     *
     * @return parametri impostati
     */
    public static ParametriRicercaEvasione creaParametriRicercaEvasione(AreaMagazzino areaMagazzino) {
        ParametriRicercaEvasione parametriRicerca = new ParametriRicercaEvasione();
        parametriRicerca.getDataRegistrazione().setDataIniziale(areaMagazzino.getDataRegistrazione());
        parametriRicerca.getDataRegistrazione().setDataFinale(areaMagazzino.getDataRegistrazione());

        parametriRicerca.setNumeroDocumentoIniziale(areaMagazzino.getDocumento().getCodice());
        parametriRicerca.setNumeroDocumentoFinale(areaMagazzino.getDocumento().getCodice());

        parametriRicerca.setEntita(areaMagazzino.getDocumento().getEntita());
        parametriRicerca.setSedeEntita(areaMagazzino.getDocumento().getSedeEntita());

        parametriRicerca.setDeposito(areaMagazzino.getDepositoOrigine());

        parametriRicerca.setEffettuaRicerca(true);

        return parametriRicerca;
    }

    /**
     * Crea i parametri per la ricerca da un'area ordine.
     *
     * @param areaOrdine
     *            area ordine
     *
     * @return parametri impostati
     */
    public static ParametriRicercaEvasione creaParametriRicercaEvasione(AreaOrdine areaOrdine) {
        ParametriRicercaEvasione parametriRicerca = new ParametriRicercaEvasione();
        parametriRicerca.getDataRegistrazione().setDataIniziale(areaOrdine.getDataRegistrazione());
        parametriRicerca.getDataRegistrazione().setDataFinale(areaOrdine.getDataRegistrazione());

        parametriRicerca.setNumeroDocumentoIniziale(areaOrdine.getDocumento().getCodice());
        parametriRicerca.setNumeroDocumentoFinale(areaOrdine.getDocumento().getCodice());

        Set<TipoAreaOrdine> tipiAreaOrdine = new TreeSet<TipoAreaOrdine>();
        tipiAreaOrdine.add(areaOrdine.getTipoAreaOrdine());
        parametriRicerca.setTipiAreaOrdine(tipiAreaOrdine);

        parametriRicerca.setEntita(areaOrdine.getDocumento().getEntita());
        parametriRicerca.setSedeEntita(areaOrdine.getDocumento().getSedeEntita());

        parametriRicerca.setDeposito(areaOrdine.getDepositoOrigine());

        parametriRicerca.setEffettuaRicerca(true);

        return parametriRicerca;
    }

    /**
     * @return Returns the agente.
     */
    public AgenteLite getAgente() {
        return agente;
    }

    /**
     * @return the areeOrdine
     */
    public List<AreaOrdine> getAreeOrdine() {
        return areeOrdine;
    }

    /**
     * @return the articolo
     */
    public ArticoloLite getArticolo() {
        return articolo;
    }

    /**
     * @return the dataConsegna
     */
    public Periodo getDataConsegna() {
        return dataConsegna;
    }

    /**
     *
     * @return data consegna finale
     */
    public Date getDataConsegnaFinale() {
        return dataConsegna.getDataFinale();
    }

    /**
     * @return data consegna iniziale
     */
    public Date getDataConsegnaIniziale() {
        return dataConsegna.getDataIniziale();
    }

    /**
     * @return Returns the dataCreazioneOrdini.
     */
    public Long getDataCreazioneOrdini() {
        return dataCreazioneOrdini;
    }

    /**
     * @return Returns the dataInizioTrasporto.
     */
    public Date getDataInizioTrasporto() {
        return dataInizioTrasporto;
    }

    /**
     * @return the dataRegistrazione
     */
    public Periodo getDataRegistrazione() {
        return dataRegistrazione;
    }

    /**
     * @return data registrazione finale
     */
    public Date getDataRegistrazioneFinale() {
        return dataRegistrazione.getDataFinale();
    }

    /**
     * @return data registrazione iniziale
     */
    public Date getDataRegistrazioneIniziale() {
        return dataRegistrazione.getDataIniziale();
    }

    /**
     * @return the deposito
     */
    public DepositoLite getDeposito() {
        return deposito;
    }

    /**
     * @return the entita
     */
    public EntitaLite getEntita() {
        return entita;
    }

    @Override
    public List<TableHeaderObject> getHeaderValues() {
        // List<TableHeaderObject> values = new ArrayList<TableHeaderObject>();
        //
        // values.add(new TableHeaderObject("annoCompetenza", annoCompetenza));
        //
        // if (getDataDocumentoIniziale() != null) {
        // values.add(new TableHeaderObject("dataDocumentoIniziale", getDataDocumentoIniziale()));
        // }
        //
        // if (getDataDocumentoFinale() != null) {
        // values.add(new TableHeaderObject("dataDocumentoFinale", getDataDocumentoFinale()));
        // }
        //
        // if (numeroDocumentoIniziale != null) {
        // values.add(new TableHeaderObject("numeroDocumentoIniziale", numeroDocumentoIniziale));
        // }
        // if (numeroDocumentoFinale != null) {
        // values.add(new TableHeaderObject("numeroDocumentoFinale", numeroDocumentoFinale));
        // }
        // if (entita != null && entita.getId() != null) {
        // values.add(new TableHeaderObject("entita", entita));
        // }
        // if (agente != null && agente.getId() != null) {
        // values.add(new TableHeaderObject("agente", agente));
        // }
        // if (utente != null && utente.getId() != null) {
        // values.add(new TableHeaderObject("utente", utente));
        // }
        // if (statiAreaOrdine != null && !statiAreaOrdine.isEmpty()
        // && statiAreaOrdine.size() != StatoAreaOrdine.values().length) {
        // values.add(new TableHeaderObject("statiAreaOrdine", statiAreaOrdine));
        // }
        // values.add(new TableHeaderObject("statoOrdine", statoOrdine));
        //
        // if (tipiAreaOrdine != null && !tipiAreaOrdine.isEmpty()) {
        // values.add(new TableHeaderObject("tipiDocumento", tipiAreaOrdine));
        // }
        //
        // if (values.isEmpty()) {
        // return null;
        // } else {
        // return values;
        // }
        return null;
    }

    /**
     * @return the numeroDocumentoFinale
     */
    public CodiceDocumento getNumeroDocumentoFinale() {
        if (this.numeroDocumentoFinale == null) {
            return new CodiceDocumento();
        }
        return numeroDocumentoFinale;
    }

    /**
     * @return the numeroDocumentoIniziale
     */
    public CodiceDocumento getNumeroDocumentoIniziale() {
        if (this.numeroDocumentoIniziale == null) {
            return new CodiceDocumento();
        }
        return numeroDocumentoIniziale;
    }

    /**
     * @return the sedeEntita
     */
    public SedeEntita getSedeEntita() {
        return sedeEntita;
    }

    /**
     * @return the tipiAreaOrdine
     */
    public Set<TipoAreaOrdine> getTipiAreaOrdine() {
        return tipiAreaOrdine;
    }

    /**
     * @return the tipoEntita
     */
    public TipoEntita getTipoEntita() {
        return tipoEntita;
    }

    /**
     * @return Returns the trasportoCuraMittente.
     */
    public Boolean getTrasportoCuraMittente() {
        return trasportoCuraMittente == null ? false : trasportoCuraMittente;
    }

    /**
     * @return Returns the evadiOrdini.
     */
    public boolean isEvadiOrdini() {
        return evadiOrdini;
    }

    /**
     * @param agente
     *            The agente to set.
     */
    public void setAgente(AgenteLite agente) {
        this.agente = agente;
    }

    /**
     * @param areeOrdine
     *            the areeOrdine to set
     */
    public void setAreeOrdine(List<AreaOrdine> areeOrdine) {
        this.areeOrdine = areeOrdine;
    }

    /**
     * @param articolo
     *            the articolo to set
     */
    public void setArticolo(ArticoloLite articolo) {
        this.articolo = articolo;
    }

    /**
     * @param dataConsegna
     *            the dataConsegna to set
     */
    public void setDataConsegna(Periodo dataConsegna) {
        this.dataConsegna = dataConsegna;
    }

    /**
     * @param dataCreazioneOrdini
     *            The dataCreazioneOrdini to set.
     */
    public void setDataCreazioneOrdini(Long dataCreazioneOrdini) {
        this.dataCreazioneOrdini = dataCreazioneOrdini;
    }

    /**
     * @param dataInizioTrasporto
     *            The dataInizioTrasporto to set.
     */
    public void setDataInizioTrasporto(Date dataInizioTrasporto) {
        this.dataInizioTrasporto = dataInizioTrasporto;
    }

    /**
     * @param dataRegistrazione
     *            the dataRegistrazione to set
     */
    public void setDataRegistrazione(Periodo dataRegistrazione) {
        this.dataRegistrazione = dataRegistrazione;
    }

    /**
     * @param deposito
     *            the deposito to set
     */
    public void setDeposito(DepositoLite deposito) {
        this.deposito = deposito;
    }

    /**
     * @param entita
     *            the entita to set
     */
    public void setEntita(EntitaLite entita) {
        this.entita = entita;
    }

    /**
     * @param evadiOrdini
     *            The evadiOrdini to set.
     */
    public void setEvadiOrdini(boolean evadiOrdini) {
        this.evadiOrdini = evadiOrdini;
    }

    /**
     * @param numeroDocumentoFinale
     *            the numeroDocumentoFinale to set
     */
    public void setNumeroDocumentoFinale(CodiceDocumento numeroDocumentoFinale) {
        this.numeroDocumentoFinale = numeroDocumentoFinale;
    }

    /**
     * @param numeroDocumentoIniziale
     *            the numeroDocumentoIniziale to set
     */
    public void setNumeroDocumentoIniziale(CodiceDocumento numeroDocumentoIniziale) {
        this.numeroDocumentoIniziale = numeroDocumentoIniziale;
    }

    /**
     * @param sedeEntita
     *            the sedeEntita to set
     */
    public void setSedeEntita(SedeEntita sedeEntita) {
        this.sedeEntita = sedeEntita;
    }

    /**
     * @param tipiAreaOrdine
     *            the tipiAreaOrdine to set
     */
    public void setTipiAreaOrdine(Set<TipoAreaOrdine> tipiAreaOrdine) {
        this.tipiAreaOrdine = tipiAreaOrdine;
    }

    /**
     * @param tipoEntita
     *            the tipoEntita to set
     */
    public void setTipoEntita(TipoEntita tipoEntita) {
        this.tipoEntita = tipoEntita;
    }

    /**
     * @param trasportoCuraMittente
     *            The trasportoCuraMittente to set.
     */
    public void setTrasportoCuraMittente(Boolean trasportoCuraMittente) {
        this.trasportoCuraMittente = trasportoCuraMittente;
    }
}
