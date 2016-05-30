package it.eurotn.panjea.magazzino.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import it.eurotn.entity.EntityBase;

@Entity
@Table(name = "maga_righe_inventario_articolo")
public class RigaInventarioArticolo extends EntityBase {

    private static final long serialVersionUID = 1620807007807353002L;

    @ManyToOne
    private InventarioArticolo inventarioArticolo;

    @Column(precision = 12, scale = 6)
    private BigDecimal quantita;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date data;

    /**
     * @return the data
     */
    public Date getData() {
        return data;
    }

    /**
     * @return the inventarioArticolo
     */
    public InventarioArticolo getInventarioArticolo() {
        return inventarioArticolo;
    }

    /**
     * @return the quantita
     */
    public BigDecimal getQuantita() {
        return quantita;
    }

    /**
     * @param data
     *            the data to set
     */
    public void setData(Date data) {
        this.data = data;
    }

    /**
     * @param inventarioArticolo
     *            the inventarioArticolo to set
     */
    public void setInventarioArticolo(InventarioArticolo inventarioArticolo) {
        this.inventarioArticolo = inventarioArticolo;
    }

    /**
     * @param quantita
     *            the quantita to set
     */
    public void setQuantita(BigDecimal quantita) {
        this.quantita = quantita;
    }

}
