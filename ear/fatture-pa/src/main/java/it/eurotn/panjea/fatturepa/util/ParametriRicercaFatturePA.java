package it.eurotn.panjea.fatturepa.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.CollectionOfElements;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import it.eurotn.panjea.anagrafica.documenti.domain.CodiceDocumento;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.fatturepa.domain.StatoFatturaPA;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino.StatoAreaMagazzino;
import it.eurotn.panjea.parametriricerca.domain.AbstractParametriRicerca;
import it.eurotn.panjea.parametriricerca.domain.Periodo;

@Entity
@Table(name = "para_ricerca_fatture_pa")
public class ParametriRicercaFatturePA extends AbstractParametriRicerca {

    private static final long serialVersionUID = -4307726043562994469L;

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

    @ManyToOne(optional = true)
    private EntitaLite entita;

    private Integer annoCompetenza;

    @CollectionOfElements(targetElement = StatoAreaMagazzino.class, fetch = FetchType.EAGER)
    @Fetch(FetchMode.JOIN)
    @JoinTable(name = "para_ricerca_fatture_pa_stati", joinColumns = @JoinColumn(name = "id") )
    @Column(name = "stato", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private Set<AreaMagazzino.StatoAreaMagazzino> statiAreaMagazzino = null;

    @CollectionOfElements(targetElement = StatoFatturaPA.class, fetch = FetchType.EAGER)
    @JoinTable(name = "para_ricerca_fatture_pa_stati_pa", joinColumns = @JoinColumn(name = "id") )
    @Column(name = "statipa", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private Set<StatoFatturaPA> statiFatturaPA;

    @Column(length = 5)
    private String progressivoInvio;

    @Transient
    private List<Integer> idAreeDaRicercare;

    {
        this.numeroDocumentoIniziale = new CodiceDocumento();
        this.numeroDocumentoFinale = new CodiceDocumento();

        this.idAreeDaRicercare = new ArrayList<Integer>();

        if (statiAreaMagazzino == null) {
            statiAreaMagazzino = new HashSet<AreaMagazzino.StatoAreaMagazzino>();
        }
    }

    /**
     * Costruttore.
     */
    public ParametriRicercaFatturePA() {
        super();
    }

    /**
     * @return the annoCompetenza
     */
    public Integer getAnnoCompetenza() {
        return annoCompetenza;
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
     * @return the entita
     */
    public EntitaLite getEntita() {
        return entita;
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
        return numeroDocumentoFinale;
    }

    /**
     * @return the numeroDocumentoIniziale
     */
    public CodiceDocumento getNumeroDocumentoIniziale() {
        return numeroDocumentoIniziale;
    }

    /**
     * @return the progressivoInvio
     */
    public String getProgressivoInvio() {
        return progressivoInvio;
    }

    /**
     * @return the statiAreaMagazzino
     */
    public Set<AreaMagazzino.StatoAreaMagazzino> getStatiAreaMagazzino() {
        return statiAreaMagazzino;
    }

    /**
     * @return the statiFatturaPA
     */
    public Set<StatoFatturaPA> getStatiFatturaPA() {
        return statiFatturaPA;
    }

    /**
     * @param annoCompetenza
     *            the annoCompetenza to set
     */
    public void setAnnoCompetenza(Integer annoCompetenza) {
        this.annoCompetenza = annoCompetenza;
    }

    /**
     * @param dataDocumento
     *            the dataDocumento to set
     */
    public void setDataDocumento(Periodo dataDocumento) {
        this.dataDocumento = dataDocumento;
    }

    /**
     * @param dataRegistrazione
     *            the dataRegistrazione to set
     */
    public void setDataRegistrazione(Periodo dataRegistrazione) {
        this.dataRegistrazione = dataRegistrazione;
    }

    /**
     * @param entita
     *            the entita to set
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
     * @param progressivoInvio
     *            the progressivoInvio to set
     */
    public void setProgressivoInvio(String progressivoInvio) {
        this.progressivoInvio = progressivoInvio;
    }

    /**
     * @param statiAreaMagazzino
     *            the statiAreaMagazzino to set
     */
    public void setStatiAreaMagazzino(Set<AreaMagazzino.StatoAreaMagazzino> statiAreaMagazzino) {
        this.statiAreaMagazzino = statiAreaMagazzino;
    }

    /**
     * @param statiFatturaPA
     *            the statiFatturaPA to set
     */
    public void setStatiFatturaPA(Set<StatoFatturaPA> statiFatturaPA) {
        this.statiFatturaPA = statiFatturaPA;
    }

}
