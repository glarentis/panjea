package it.eurotn.panjea.manutenzioni.domain.documento;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.envers.Audited;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.manutenzioni.domain.ArticoloMI;
import it.eurotn.panjea.manutenzioni.domain.CausaleInstallazione;
import it.eurotn.panjea.manutenzioni.domain.Installazione;

@SuppressWarnings("serial")
@Entity
@Audited
@Table(name = "manu_righe_installazione")
public class RigaInstallazione extends EntityBase {

    public enum TipoMovimento {
        INSTALLAZIONE, SOSTITUZIONE, RITIRO
    }

    @ManyToOne(fetch = FetchType.LAZY)
    private AreaInstallazione areaInstallazione;

    @ManyToOne(fetch = FetchType.LAZY)
    private ArticoloMI articoloRitiro;

    @ManyToOne(fetch = FetchType.LAZY)
    private Installazione installazione;

    @ManyToOne(fetch = FetchType.LAZY)
    private ArticoloMI articoloInstallazione;

    @ManyToOne(fetch = FetchType.LAZY)
    @Fetch(FetchMode.JOIN)
    private CausaleInstallazione causaleInstallazione;

    @ManyToOne(fetch = FetchType.LAZY)
    @Fetch(FetchMode.JOIN)
    private CausaleInstallazione causaleRitiro;

    private double ordinamento;

    private TipoMovimento tipoMovimento;

    /**
     * Costruttore.
     */
    public RigaInstallazione() {
        super();
    }

    /**
     *
     * @param installazioneParam
     *            crea una rigaInstallazione legata all'installazione.
     */
    public RigaInstallazione(final Installazione installazioneParam) {
        super();
        installazione = installazioneParam;
    }

    /**
     * @return Returns the areaInstallazione.
     */
    public AreaInstallazione getAreaInstallazione() {
        return areaInstallazione;
    }

    /**
     * @return Returns the articoloInstallazione.
     */
    public ArticoloMI getArticoloInstallazione() {
        return articoloInstallazione;
    }

    /**
     * @return Returns the articoloRitiro.
     */
    public ArticoloMI getArticoloRitiro() {
        return articoloRitiro;
    }

    /**
     * @return Returns the causaleInstallazione.
     */
    public CausaleInstallazione getCausaleInstallazione() {
        return causaleInstallazione;
    }

    /**
     * @return Returns the causaleRitiro.
     */
    public CausaleInstallazione getCausaleRitiro() {
        return causaleRitiro;
    }

    /**
     * @return Returns the installazione.
     */
    public Installazione getInstallazione() {
        return installazione;
    }

    /**
     * @return Returns the ordinamento.
     */
    public double getOrdinamento() {
        return ordinamento;
    }

    /**
     * @return Returns the tipoMovimento.
     */
    public TipoMovimento getTipoMovimento() {
        return tipoMovimento;
    }

    /**
     * @param areaInstallazione
     *            The areaInstallazione to set.
     */
    public void setAreaInstallazione(AreaInstallazione areaInstallazione) {
        this.areaInstallazione = areaInstallazione;
    }

    /**
     * @param articoloInstallazione
     *            The articoloInstallazione to set.
     */
    public void setArticoloInstallazione(ArticoloMI articoloInstallazione) {
        this.articoloInstallazione = articoloInstallazione;
    }

    /**
     * @param articoloRitiro
     *            the articoloRitiro to set
     */
    public void setArticoloRitiro(ArticoloMI articoloRitiro) {
        this.articoloRitiro = articoloRitiro;
    }

    /**
     * @param causaleInstallazione
     *            The causaleInstallazione to set.
     */
    public void setCausaleInstallazione(CausaleInstallazione causaleInstallazione) {
        this.causaleInstallazione = causaleInstallazione;
    }

    /**
     * @param causaleRitiro
     *            The causaleRitiro to set.
     */
    public void setCausaleRitiro(CausaleInstallazione causaleRitiro) {
        this.causaleRitiro = causaleRitiro;
        if (this.causaleRitiro != null) {
            if (!causaleRitiro.getTipoInstallazione().isRitiro()) {
                throw new IllegalArgumentException("la causale deve avere il flag ritiro");
            }
            this.articoloRitiro = installazione.getArticolo();
        } else {
            this.articoloRitiro = null;
        }
    }

    /**
     * @param installazione
     *            The installazione to set.
     */
    public void setInstallazione(Installazione installazione) {
        this.installazione = installazione;
    }

    /**
     * @param ordinamento
     *            The ordinamento to set.
     */
    public void setOrdinamento(double ordinamento) {
        this.ordinamento = ordinamento;
    }

    /**
     * @param tipoMovimento
     *            The tipoMovimento to set.
     */
    public void setTipoMovimento(TipoMovimento tipoMovimento) {
        this.tipoMovimento = tipoMovimento;
    }
}