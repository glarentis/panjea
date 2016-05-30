package it.eurotn.panjea.magazzino.util.parametriricerca;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import it.eurotn.panjea.agenti.domain.lite.AgenteLite;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.domain.CategoriaEntita;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.ZonaGeografica;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.magazzino.domain.SedeMagazzinoLite;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzinoLite;
import it.eurotn.panjea.pagamenti.domain.CodicePagamento;
import it.eurotn.panjea.parametriricerca.domain.AbstractParametriRicerca;
import it.eurotn.panjea.parametriricerca.domain.Periodo;

/**
 * Parametri per la ricerca dei documenti per la fatturazione.
 *
 * @author fattazzo
 */
@Entity
@Table(name = "para_ricerca_fatturazione")
public class ParametriRicercaFatturazione extends AbstractParametriRicerca implements Serializable {

    private static final long serialVersionUID = -1215315836576093308L;

    @Transient
    private boolean effettuaRicerca = false;

    private Periodo periodo;

    @ManyToOne(optional = true)
    private TipoDocumento tipoDocumentoDestinazione;

    @ManyToOne(optional = true)
    private SedeMagazzinoLite sedePerRifatturazione;

    @Transient
    private List<TipoDocumento> tipiDocumentoDaFatturare;

    @ManyToOne(optional = true)
    private EntitaLite entitaLite;

    @ManyToOne(optional = true)
    private SedeEntita sedeEntita;

    @ManyToOne(optional = true)
    private CodicePagamento codicePagamento;

    private String codiceValuta;

    @ManyToOne(optional = true)
    private AgenteLite agente;

    @ManyToOne(optional = true)
    private CategoriaEntita categoriaEntita;

    @ManyToOne(optional = true)
    private ZonaGeografica zonaGeografica;

    /**
     * Se la lista non è vuota indica di caricare le aree magazzino della lista.<br/>
     * Viene utilizzata quando "sfatturo" una serie di documenti e voglio tenere traccia dei documenti collegati per
     * poter rifare la fatturazione con gli stessi documenti
     */
    @Transient
    private List<AreaMagazzinoLite> areeMagazzino;

    {
        if (areeMagazzino == null) {
            areeMagazzino = new ArrayList<AreaMagazzinoLite>();
        }
    }

    /**
     * Costruttore di default.
     */
    public ParametriRicercaFatturazione() {
        super();
    }

    /**
     * Aggiunge un'area magazzino alle aree da caricare.
     * 
     * @param areaMagazzinoLite
     *            area Magazzino da aggiungere
     */
    public void addToAreeMagazzino(AreaMagazzinoLite areaMagazzinoLite) {
        areeMagazzino.add(areaMagazzinoLite);
    }

    /**
     * @return Returns the agente.
     */
    public AgenteLite getAgente() {
        return agente;
    }

    /**
     * Se la lista non è vuota la ricerca non considera gli altri parametri ma carica solamente le aree presenti nella
     * lista.
     * 
     * @return lista non modificabile di areeMagazzino da caricare.
     */
    public List<AreaMagazzinoLite> getAreeMagazzino() {
        return Collections.unmodifiableList(areeMagazzino);
    }

    /**
     * @return Returns the categoriaEntita.
     */
    public CategoriaEntita getCategoriaEntita() {
        return categoriaEntita;
    }

    /**
     * @return the codicePagamento
     */
    public CodicePagamento getCodicePagamento() {
        return codicePagamento;
    }

    /**
     * @return the codiceValuta
     */
    public String getCodiceValuta() {
        return codiceValuta;
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
            periodo = new Periodo();
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
     * @return the sedePerRifatturazione
     */
    public SedeMagazzinoLite getSedePerRifatturazione() {
        return sedePerRifatturazione;
    }

    /**
     * @return the tipiDocumentoDaFatturare
     */
    public List<TipoDocumento> getTipiDocumentoDaFatturare() {
        return tipiDocumentoDaFatturare;
    }

    /**
     * @return the tipoDocumentoDestinazione
     */
    public TipoDocumento getTipoDocumentoDestinazione() {
        return tipoDocumentoDestinazione;
    }

    /**
     * @return Returns the zonaGeografica.
     */
    public ZonaGeografica getZonaGeografica() {
        return zonaGeografica;
    }

    /**
     * @return true significa che ho i parametri già impostati e quindi devo effettuare subito la ricerca.
     */
    @Override
    public boolean isEffettuaRicerca() {
        return effettuaRicerca;
    }

    /**
     * @param agente
     *            The agente to set.
     */
    public void setAgente(AgenteLite agente) {
        this.agente = agente;
    }

    /**
     * @param areeMagazzino
     *            The areeMagazzino to set.
     */
    public void setAreeMagazzino(List<AreaMagazzinoLite> areeMagazzino) {
        this.areeMagazzino = areeMagazzino;
    }

    /**
     * @param categoriaEntita
     *            The categoriaEntita to set.
     */
    public void setCategoriaEntita(CategoriaEntita categoriaEntita) {
        this.categoriaEntita = categoriaEntita;
    }

    /**
     * @param codicePagamento
     *            the codicePagamento to set
     */
    public void setCodicePagamento(CodicePagamento codicePagamento) {
        this.codicePagamento = codicePagamento;
    }

    /**
     * @param codiceValuta
     *            the codiceValuta to set
     */
    public void setCodiceValuta(String codiceValuta) {
        this.codiceValuta = codiceValuta;
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
     * @param sedePerRifatturazione
     *            the sedePerRifatturazione to set
     */
    public void setSedePerRifatturazione(SedeMagazzinoLite sedePerRifatturazione) {
        this.sedePerRifatturazione = sedePerRifatturazione;
    }

    /**
     * @param tipiDocumentoDaFatturare
     *            the tipiDocumentoDaFatturare to set
     */
    public void setTipiDocumentoDaFatturare(List<TipoDocumento> tipiDocumentoDaFatturare) {
        this.tipiDocumentoDaFatturare = tipiDocumentoDaFatturare;
    }

    /**
     * @param tipoDocumentoDestinazione
     *            The tipoDocumentoDestinazione to set.
     */
    public void setTipoDocumentoDestinazione(TipoDocumento tipoDocumentoDestinazione) {
        this.tipoDocumentoDestinazione = tipoDocumentoDestinazione;
    }

    /**
     * @param zonaGeografica
     *            The zonaGeografica to set.
     */
    public void setZonaGeografica(ZonaGeografica zonaGeografica) {
        this.zonaGeografica = zonaGeografica;
    }
}
