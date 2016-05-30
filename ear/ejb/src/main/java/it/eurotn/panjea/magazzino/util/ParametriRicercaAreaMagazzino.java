package it.eurotn.panjea.magazzino.util;

import java.util.ArrayList;
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
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.CollectionOfElements;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import it.eurotn.panjea.anagrafica.documenti.domain.CodiceDocumento;
import it.eurotn.panjea.anagrafica.domain.Deposito;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.domain.lite.VettoreLite;
import it.eurotn.panjea.anagrafica.util.parametriricerca.ITableHeader;
import it.eurotn.panjea.anagrafica.util.parametriricerca.TableHeaderObject;
import it.eurotn.panjea.magazzino.domain.DatiGenerazione.TipoGenerazione;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino.StatoAreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino;
import it.eurotn.panjea.magazzino.domain.rendicontazione.TipoEsportazione;
import it.eurotn.panjea.parametriricerca.domain.AbstractParametriRicerca;
import it.eurotn.panjea.parametriricerca.domain.Periodo;
import it.eurotn.panjea.sicurezza.domain.Utente;

/**
 * Classe Util che descrive i parametri di ricerca di {@link AreaMagazzino} .
 *
 * @author adriano
 * @version 1.0, 30/set/2008
 */
@Entity
@Table(name = "para_ricerca_area_magazzino")
public class ParametriRicercaAreaMagazzino extends AbstractParametriRicerca implements ITableHeader {

    private static final long serialVersionUID = 1L;

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

    @ManyToOne(optional = true)
    private Utente utente = null;

    @Embedded
    @AttributeOverrides({ @AttributeOverride(name = "dataIniziale", column = @Column(name = "dataDocumentoIniziale") ),
            @AttributeOverride(name = "dataFinale", column = @Column(name = "dataDocumentoFinale") ),
            @AttributeOverride(name = "tipoPeriodo", column = @Column(name = "dataDocumentoTipoPeriodo") ),
            @AttributeOverride(name = "dataInizialeNull", column = @Column(name = "dataDocumentoDataInizialeNull") ),
            @AttributeOverride(name = "numeroGiorni", column = @Column(name = "dataDocumentoNumeroGiorni") ) })
    private Periodo dataDocumento;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "dataIniziale", column = @Column(name = "dataRegistrazioneIniziale") ),
            @AttributeOverride(name = "dataFinale", column = @Column(name = "dataRegistrazioneFinale") ),
            @AttributeOverride(name = "tipoPeriodo", column = @Column(name = "dataRegistrazioneTipoPeriodo") ),
            @AttributeOverride(name = "dataInizialeNull", column = @Column(name = "dataRegistrazioneDataInizialeNull") ),
            @AttributeOverride(name = "numeroGiorni", column = @Column(name = "dataRegistrazioneNumeroGiorni") ) })
    private Periodo dataRegistrazione;

    @ManyToMany(fetch = FetchType.EAGER)
    @Fetch(FetchMode.JOIN)
    private Set<TipoAreaMagazzino> tipiAreaMagazzino = null;

    @CollectionOfElements(targetElement = StatoAreaMagazzino.class, fetch = FetchType.EAGER)
    @Fetch(FetchMode.JOIN)
    @JoinTable(name = "para_ricerca_area_magazzino_stati", joinColumns = @JoinColumn(name = "id") )
    @Column(name = "stato", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private Set<AreaMagazzino.StatoAreaMagazzino> statiAreaMagazzino = null;

    @ManyToOne(optional = true)
    private EntitaLite entita;

    @Transient
    private List<Deposito> depositiDestinazione;

    @Transient
    private List<Deposito> depositiSorgente;

    private Integer annoCompetenza;

    @CollectionOfElements(targetElement = TipoGenerazione.class, fetch = FetchType.EAGER)
    @JoinTable(name = "para_ricerca_area_magazzino_tipi_generazione", joinColumns = @JoinColumn(name = "id") )
    @Column(name = "tipo", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private Set<TipoGenerazione> tipiGenerazione;

    @Transient
    private TipoEsportazione tipoEsportazione;
    @Transient
    private Date dataGenerazione;

    @Transient
    private List<AreaMagazzinoRicerca> areeMagazzino;

    @ManyToOne(optional = true)
    private VettoreLite vettore;

    private Boolean speditiAlVettore;

    private Date dataInizioTrasporto;

    private Boolean soloNonRendicontati;

    @Transient
    private List<Integer> idAreeDaRicercare;

    {
        areeMagazzino = new ArrayList<AreaMagazzinoRicerca>();
        if (tipiGenerazione == null) {
            tipiGenerazione = new TreeSet<TipoGenerazione>();
        }
        if (tipiAreaMagazzino == null) {
            tipiAreaMagazzino = new HashSet<TipoAreaMagazzino>();
        }
        if (statiAreaMagazzino == null) {
            statiAreaMagazzino = new HashSet<AreaMagazzino.StatoAreaMagazzino>();
        }

        if (speditiAlVettore == null) {
            speditiAlVettore = Boolean.FALSE;
        }
        if (tipoEsportazione == null) {
            tipoEsportazione = new TipoEsportazione();
        }

        this.soloNonRendicontati = false;
        this.speditiAlVettore = null;
        this.numeroDocumentoIniziale = new CodiceDocumento();
        this.numeroDocumentoFinale = new CodiceDocumento();
        this.idAreeDaRicercare = new ArrayList<Integer>();
    }

    /**
     * Costruttore.
     */
    public ParametriRicercaAreaMagazzino() {
        super();
    }

    /**
     * @return the annoCompetenza
     */
    public Integer getAnnoCompetenza() {
        return annoCompetenza;
    }

    /**
     * @return the areeMagazzino
     */
    public List<AreaMagazzinoRicerca> getAreeMagazzino() {
        return areeMagazzino;
    }

    /**
     * @return Returns the dataDocumento.
     */
    public Periodo getDataDocumento() {
        if (dataDocumento == null) {
            dataDocumento = new Periodo();
        }
        return dataDocumento;
    }

    /**
     * @return dataDocumento finale
     */
    public Date getDataDocumentoFinale() {
        if (dataDocumento == null) {
            dataDocumento = new Periodo();
        }
        return dataDocumento.getDataFinale();
    }

    /**
     * @return dataDocumento iniziale
     */
    public Date getDataDocumentoIniziale() {
        if (dataDocumento == null) {
            dataDocumento = new Periodo();
        }
        return dataDocumento.getDataIniziale();
    }

    /**
     * @return Returns the dataGenerazione.
     */
    public Date getDataGenerazione() {
        return dataGenerazione;
    }

    /**
     * @return the dataInizioTrasporto
     */
    public Date getDataInizioTrasporto() {
        return dataInizioTrasporto;
    }

    /**
     * @return Returns the dataRegistrazione.
     */
    public Periodo getDataRegistrazione() {
        if (dataRegistrazione == null) {
            dataRegistrazione = new Periodo();
        }
        return dataRegistrazione;
    }

    /**
     * @return dataRegistrazione finale
     */
    public Date getDataRegistrazioneFinale() {
        if (dataRegistrazione == null) {
            dataRegistrazione = new Periodo();
        }
        return dataRegistrazione.getDataFinale();
    }

    /**
     * @return dataregistrazione iniziale
     */
    public Date getDataRegistrazioneIniziale() {
        if (dataRegistrazione == null) {
            dataRegistrazione = new Periodo();
        }
        return dataRegistrazione.getDataIniziale();
    }

    /**
     * @return Returns the depositiDestinazione.
     */
    public List<Deposito> getDepositiDestinazione() {
        return depositiDestinazione;
    }

    /**
     * @return Returns the depositiSorgente.
     */
    public List<Deposito> getDepositiSorgente() {
        return depositiSorgente;
    }

    /**
     * @return Returns the entita.
     */
    public EntitaLite getEntita() {
        return entita;
    }

    @Override
    public List<TableHeaderObject> getHeaderValues() {
        List<TableHeaderObject> values = new ArrayList<TableHeaderObject>();

        values.add(new TableHeaderObject("annoCompetenza", annoCompetenza));

        if (getDataRegistrazioneIniziale() != null) {
            values.add(new TableHeaderObject("dataRegistrazioneIniziale", getDataRegistrazioneIniziale()));
        }

        if (getDataRegistrazioneFinale() != null) {
            values.add(new TableHeaderObject("dataRegistrazioneFinale", getDataRegistrazioneFinale()));
        }

        if (getDataDocumentoIniziale() != null) {
            values.add(new TableHeaderObject("dataDocumentoIniziale", getDataDocumentoIniziale()));
        }

        if (getDataDocumentoFinale() != null) {
            values.add(new TableHeaderObject("dataDocumentoFinale", getDataDocumentoFinale()));
        }

        if (!numeroDocumentoIniziale.isEmpty()) {
            values.add(new TableHeaderObject("numeroDocumentoIniziale", numeroDocumentoIniziale.getCodice()));
        }
        if (!numeroDocumentoFinale.isEmpty()) {
            values.add(new TableHeaderObject("numeroDocumentoFinale", numeroDocumentoFinale.getCodice()));
        }
        if (entita != null && entita.getId() != null) {
            values.add(new TableHeaderObject("entita", entita));
        }
        if (utente != null && utente.getId() != null) {
            values.add(new TableHeaderObject("utente", utente));
        }
        if (statiAreaMagazzino != null && !statiAreaMagazzino.isEmpty()
                && statiAreaMagazzino.size() != StatoAreaMagazzino.values().length) {
            values.add(new TableHeaderObject("statiAreaMagazzino", statiAreaMagazzino));
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
     * @return the idAreeDaRicercare
     */
    public List<Integer> getIdAreeDaRicercare() {
        return idAreeDaRicercare;
    }

    /**
     * @return the numeroDocumentoFinale
     */
    public CodiceDocumento getNumeroDocumentoFinale() {
        if (numeroDocumentoFinale == null) {
            return new CodiceDocumento();
        }
        return numeroDocumentoFinale;
    }

    /**
     * @return the numeroDocumentoIniziale
     */
    public CodiceDocumento getNumeroDocumentoIniziale() {
        if (numeroDocumentoIniziale == null) {
            return new CodiceDocumento();
        }
        return numeroDocumentoIniziale;
    }

    /**
     * @return the soloNonRendicontati
     */
    public Boolean getSoloNonRendicontati() {
        return soloNonRendicontati;
    }

    /**
     * @return the speditiAlVettore
     */
    public Boolean getSpeditiAlVettore() {
        return speditiAlVettore;
    }

    /**
     * @return Returns the statiAreaMagazzino.
     */
    public Set<AreaMagazzino.StatoAreaMagazzino> getStatiAreaMagazzino() {
        return statiAreaMagazzino;
    }

    /**
     * @return Returns the tipiAreaMagazzino.
     */
    public Set<TipoAreaMagazzino> getTipiAreaMagazzino() {
        return tipiAreaMagazzino;
    }

    /**
     * @return the tipiGenerazione
     */
    public Set<TipoGenerazione> getTipiGenerazione() {
        return tipiGenerazione;
    }

    /**
     * @return the tipoEsportazione
     */
    public TipoEsportazione getTipoEsportazione() {
        return tipoEsportazione;
    }

    /**
     * @return userName
     */
    public String getUserName() {
        return getUtente().getUserName();
    }

    /**
     * @return Returns the utente.
     */
    public Utente getUtente() {
        return utente;
    }

    /**
     * @return the vettore
     */
    public VettoreLite getVettore() {
        return vettore;
    }

    /**
     * @param annoCompetenza
     *            the annoCompetenza to set
     */
    public void setAnnoCompetenza(Integer annoCompetenza) {
        this.annoCompetenza = annoCompetenza;
    }

    /**
     * @param areeMagazzino
     *            the areeMagazzino to set
     */
    public void setAreeMagazzino(List<AreaMagazzinoRicerca> areeMagazzino) {
        this.areeMagazzino = areeMagazzino;
    }

    /**
     * @param dataDocumento
     *            The dataDocumento to set.
     */
    public void setDataDocumento(Periodo dataDocumento) {
        this.dataDocumento = dataDocumento;
    }

    /**
     * @param dataGenerazione
     *            The dataGenerazione to set.
     */
    public void setDataGenerazione(Date dataGenerazione) {
        this.dataGenerazione = dataGenerazione;
    }

    /**
     * @param dataInizioTrasporto
     *            the dataInizioTrasporto to set
     */
    public void setDataInizioTrasporto(Date dataInizioTrasporto) {
        this.dataInizioTrasporto = dataInizioTrasporto;
    }

    /**
     * @param dataRegistrazione
     *            The dataRegistrazione to set.
     */
    public void setDataRegistrazione(Periodo dataRegistrazione) {
        this.dataRegistrazione = dataRegistrazione;
    }

    /**
     * @param depositiDestinazione
     *            The depositiDestinazione to set.
     */
    public void setDepositiDestinazione(List<Deposito> depositiDestinazione) {
        this.depositiDestinazione = depositiDestinazione;
    }

    /**
     * @param depositiSorgente
     *            The depositiSorgente to set.
     */
    public void setDepositiSorgente(List<Deposito> depositiSorgente) {
        this.depositiSorgente = depositiSorgente;
    }

    /**
     * @param entita
     *            The entita to set.
     */
    public void setEntita(EntitaLite entita) {
        this.entita = entita;
    }

    /**
     * @param idAreeDaRicercare
     *            the idAreeDaRicercare to set
     */
    public void setIdAreeDaRicercare(List<Integer> idAreeDaRicercare) {
        this.idAreeDaRicercare = idAreeDaRicercare;
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
     * @param soloNonRendicontati
     *            the soloNonRendicontati to set
     */
    public void setSoloNonRendicontati(Boolean soloNonRendicontati) {
        this.soloNonRendicontati = soloNonRendicontati;
    }

    /**
     * @param speditiAlVettore
     *            the speditiAlVettore to set
     */
    public void setSpeditiAlVettore(Boolean speditiAlVettore) {
        this.speditiAlVettore = speditiAlVettore;
    }

    /**
     * @param statiAreaMagazzino
     *            The statiAreaMagazzino to set.
     */
    public void setStatiAreaMagazzino(Set<AreaMagazzino.StatoAreaMagazzino> statiAreaMagazzino) {
        this.statiAreaMagazzino = statiAreaMagazzino;
    }

    /**
     * @param tipiAreaMagazzino
     *            The tipiAreaMagazzino to set.
     */
    public void setTipiAreaMagazzino(Set<TipoAreaMagazzino> tipiAreaMagazzino) {
        this.tipiAreaMagazzino = tipiAreaMagazzino;
    }

    /**
     * @param tipiGenerazione
     *            the tipiGenerazione to set
     */
    public void setTipiGenerazione(Set<TipoGenerazione> tipiGenerazione) {
        this.tipiGenerazione = tipiGenerazione;
    }

    /**
     * @param tipoEsportazione
     *            the tipoEsportazione to set
     */
    public void setTipoEsportazione(TipoEsportazione tipoEsportazione) {
        this.tipoEsportazione = tipoEsportazione;
    }

    /**
     * @param utente
     *            The utente to set.
     */
    public void setUtente(Utente utente) {
        this.utente = utente;
    }

    /**
     * @param vettore
     *            the vettore to set
     */
    public void setVettore(VettoreLite vettore) {
        this.vettore = vettore;
    }

}
