package it.eurotn.panjea.anagrafica.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.Componente;
import it.eurotn.panjea.magazzino.domain.ConfigurazioneDistinta;
import it.eurotn.util.PanjeaEJBUtil;

@Entity
@Audited
@Table(name = "maga_fasi_lavorazione_articolo")
@NamedQueries({
        @NamedQuery(name = "FaseLavorazioneArticolo.loadByConfigurazioneBase", query = "select f from FaseLavorazioneArticolo f where f.articolo=:articolo and configurazioneDistinta is null order by f.ordinamento"),
        @NamedQuery(name = "FaseLavorazioneArticolo.loadByConfigurazioneComponente", query = "select f from FaseLavorazioneArticolo f where f.componente=:componente order by f.ordinamento"),
        @NamedQuery(name = "FaseLavorazioneArticolo.loadByConfigurazione", query = "select f from FaseLavorazioneArticolo f where configurazioneDistinta=:configurazione and f.componente is null order by f.ordinamento") })
public class FaseLavorazioneArticolo extends EntityBase implements Cloneable {

    private static final long serialVersionUID = -1485164892624067992L;

    private Double qtaAttrezzaggio;

    private Integer ordinamento;

    @ManyToOne
    private ArticoloLite articolo;

    @ManyToOne
    private Componente componente;

    @ManyToOne
    private ConfigurazioneDistinta configurazioneDistinta;

    @ManyToOne
    private FaseLavorazione faseLavorazione;

    @Column(length = 200)
    private String descrizione;

    {
        qtaAttrezzaggio = 0.0;
        ordinamento = 0;
    }

    /**
     * Costruttore.
     */
    public FaseLavorazioneArticolo() {
        super();
    }

    /**
     * Avvalora articolo configurazione.
     */
    public void avvaloraArticoloConfigurazione() {
        if (configurazioneDistinta != null) {
            articolo = configurazioneDistinta.getDistinta().getArticoloLite();
        } else if (componente != null) {
            articolo = componente.getDistinta();
            configurazioneDistinta = componente.getConfigurazioneDistinta();
        }
    }

    @Override
    public Object clone() {
        FaseLavorazioneArticolo nuovaFase = new FaseLavorazioneArticolo();
        PanjeaEJBUtil.copyProperties(nuovaFase, this);
        nuovaFase.setId(null);
        nuovaFase.setVersion(0);

        return nuovaFase;
    }

    /**
     * @return Returns the articolo.
     */
    public ArticoloLite getArticolo() {
        return articolo;
    }

    /**
     * @return Returns the componente.
     */
    public Componente getComponente() {
        return componente;
    }

    /**
     * @return Returns the configurazioneDistinta.
     */
    public ConfigurazioneDistinta getConfigurazioneDistinta() {
        return configurazioneDistinta;
    }

    /**
     * @return the descrizione
     */
    public String getDescrizione() {
        return descrizione;
    }

    /**
     * @return Returns the faseLavorazione.
     */
    public FaseLavorazione getFaseLavorazione() {
        return faseLavorazione;
    }

    /**
     * @return the ordinamento
     */
    public Integer getOrdinamento() {
        return ordinamento;
    }

    /**
     * @return Returns the qtaAttrezzaggio.
     */
    public Double getQtaAttrezzaggio() {
        return qtaAttrezzaggio;
    }

    /**
     * @param articolo
     *            The articolo to set.
     */
    public void setArticolo(ArticoloLite articolo) {
        this.articolo = articolo;
    }

    /**
     * @param componente
     *            The componente to set.
     */
    public void setComponente(Componente componente) {
        this.componente = componente;
    }

    /**
     * @param configurazioneDistinta
     *            The configurazioneDistinta to set.
     */
    public void setConfigurazioneDistinta(ConfigurazioneDistinta configurazioneDistinta) {
        this.configurazioneDistinta = configurazioneDistinta;
    }

    /**
     * @param descrizione
     *            the descrizione to set
     */
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    /**
     * @param faseLavorazione
     *            The faseLavorazione to set.
     */
    public void setFaseLavorazione(FaseLavorazione faseLavorazione) {
        this.faseLavorazione = faseLavorazione;
        if (faseLavorazione != null) {
            setOrdinamento(faseLavorazione.getOrdinamento());
        }
    }

    /**
     * @param ordinamento
     *            the ordinamento to set
     */
    public void setOrdinamento(Integer ordinamento) {
        this.ordinamento = ordinamento;
    }

    /**
     * @param qtaAttrezzaggio
     *            The qtaAttrezzaggio to set.
     */
    public void setQtaAttrezzaggio(Double qtaAttrezzaggio) {
        this.qtaAttrezzaggio = qtaAttrezzaggio;
    }
}
