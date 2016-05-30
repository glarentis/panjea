package it.eurotn.panjea.magazzino.domain;

import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.apache.commons.lang3.ObjectUtils;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.envers.Audited;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.anagrafica.domain.FaseLavorazioneArticolo;
import it.eurotn.util.PanjeaEJBUtil;

/**
 * Lega un articolo ad una distinta (anch'essa un articolo).<br>
 *
 * @author adriano
 * @version 1.0, 16/set/2008
 */
@Entity
@Audited
@Table(name = "maga_componente")
@NamedQueries({
        @NamedQuery(name = "Componente.caricaByDistinta", query = "select articolo from Componente c where c.distinta=:distinta"),
        @NamedQuery(name = "Componente.caricaByConfigurazioneDistinta", query = "select c from Componente c where c.configurazioneDistinta.id=:idConfigurazioneDistinta and c.articolo.id = :idArticolo") })
public class Componente extends EntityBase implements Cloneable {

    private static final long serialVersionUID = 4567744598846012780L;

    {
        formula = TipoAttributo.ATTRIBUTO_QTA_CODICE_FORMULA;
        this.fasiLavorazioneArticolo = new TreeSet<FaseLavorazioneArticolo>();
        qtaAttrezzaggio = 0.0;
    }

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, mappedBy = "componente")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @OrderBy(value = "ordinamento")
    private Set<FaseLavorazioneArticolo> fasiLavorazioneArticolo;

    private Double qtaAttrezzaggio;

    @ManyToOne(fetch = FetchType.LAZY)
    private ConfigurazioneDistinta configurazioneDistinta;

    // per ogni componente salvo la configurazione che uso.
    // La configurazione però non viene aggiornata al cambiamento della conf. del componente.
    @ManyToOne(fetch = FetchType.LAZY)
    private ConfigurazioneDistinta configurazioneComponente;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Componente componenteConfigurazione;

    @ManyToOne
    @JoinColumn(name = "articolo_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ArticoloLite articolo;

    @ManyToOne
    @JoinColumn(name = "distinta_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ArticoloLite distinta;

    @Column(nullable = false)
    private String formula;

    private Integer ordinamento;

    {
        ordinamento = 0;
    }

    @Override
    public Object clone() {
        Componente componenteClone = new Componente();
        PanjeaEJBUtil.copyProperties(componenteClone, this);
        componenteClone.setVersion(0);
        componenteClone.setId(null);

        return componenteClone;
    }

    /**
     * @return Returns the articolo.
     */
    public ArticoloLite getArticolo() {
        return articolo;
    }

    /**
     * @return Returns the componenteConfigurazione.
     */
    public Componente getComponenteConfigurazione() {
        return componenteConfigurazione;
    }

    /**
     * @return Returns the configurazioneComponente.
     */
    public ConfigurazioneDistinta getConfigurazioneComponente() {
        return configurazioneComponente;
    }

    /**
     * @return Returns the configurazioneDistinta.
     */
    public ConfigurazioneDistinta getConfigurazioneDistinta() {
        return configurazioneDistinta;
    }

    /**
     * @return Returns the distinta.
     */
    public ArticoloLite getDistinta() {
        return distinta;
    }

    /**
     * @return Returns the fasiLavorazioneArticolo.
     */
    public Set<FaseLavorazioneArticolo> getFasiLavorazioneArticolo() {
        return fasiLavorazioneArticolo;
    }

    /**
     * @return the formula
     */
    public String getFormula() {
        return formula;
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
     * @param componenteConfigurazione
     *            The componenteConfigurazione to set.
     */
    public void setComponenteConfigurazione(Componente componenteConfigurazione) {
        this.componenteConfigurazione = componenteConfigurazione;
    }

    /**
     * @param configurazioneComponente
     *            The configurazioneComponente to set.
     */
    public void setConfigurazioneComponente(ConfigurazioneDistinta configurazioneComponente) {
        this.configurazioneComponente = configurazioneComponente;
    }

    /**
     * @param configurazioneDistinta
     *            The configurazioneDistinta to set.
     */
    public void setConfigurazioneDistinta(ConfigurazioneDistinta configurazioneDistinta) {
        this.configurazioneDistinta = configurazioneDistinta;
    }

    /**
     * @param distinta
     *            The distinta to set.
     */
    public void setDistinta(ArticoloLite distinta) {
        this.distinta = distinta;
    }

    /**
     * @param fasiLavorazioneArticolo
     *            The fasiLavorazioneArticolo to set.
     */
    public void setFasiLavorazioneArticolo(Set<FaseLavorazioneArticolo> fasiLavorazioneArticolo) {
        this.fasiLavorazioneArticolo = fasiLavorazioneArticolo;
    }

    /**
     * @param formula
     *            the formula to set
     */
    public void setFormula(String formula) {
        this.formula = formula;
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
        this.qtaAttrezzaggio = ObjectUtils.defaultIfNull(qtaAttrezzaggio, 0.0);
    }

}
