package it.eurotn.panjea.magazzino.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import it.eurotn.entity.EntityBase;

@Entity
@Audited
@Table(name = "maga_righe_raggruppamento_articoli")
public class RigaRaggruppamentoArticoli extends EntityBase {
    private static final long serialVersionUID = -5808954949960855607L;
    @ManyToOne()
    private ArticoloLite articolo;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private RaggruppamentoArticoli raggruppamento;
    private Double qta;

    /**
     * Costruttore.
     */
    public RigaRaggruppamentoArticoli() {
        qta = 1.0;
    }

    /**
     * @return Returns the articolo.
     */
    public ArticoloLite getArticolo() {
        return articolo;
    }

    /**
     * @return Returns the qta.
     */
    public Double getQta() {
        return qta;
    }

    /**
     * @return Returns the raggruppamento.
     */
    public RaggruppamentoArticoli getRaggruppamento() {
        return raggruppamento;
    }

    /**
     * @param articolo
     *            The articolo to set.
     */
    public void setArticolo(ArticoloLite articolo) {
        this.articolo = articolo;
    }

    /**
     * @param qta
     *            The qta to set.
     */
    public void setQta(Double qta) {
        this.qta = qta;
    }

    /**
     * @param raggruppamento
     *            The raggruppamento to set.
     */
    public void setRaggruppamento(RaggruppamentoArticoli raggruppamento) {
        this.raggruppamento = raggruppamento;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "RigaRaggruppamentoArticoli [articolo=" + articolo + ", qta=" + qta + "]";
    }
}
