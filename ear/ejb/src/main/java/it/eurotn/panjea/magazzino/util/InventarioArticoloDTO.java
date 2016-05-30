package it.eurotn.panjea.magazzino.util;

import java.io.Serializable;
import java.util.Date;

import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;

public class InventarioArticoloDTO implements Serializable {

    private static final long serialVersionUID = 2415072666770826099L;

    private Date data;

    private DepositoLite deposito;

    /**
     * Costruttore.
     * 
     */
    public InventarioArticoloDTO() {
        super();
        this.deposito = new DepositoLite();
    }

    /**
     * Costruttore.
     * 
     * @param data
     *            data inventario
     * @param deposito
     *            deposito inventario
     */
    public InventarioArticoloDTO(final Date data, final DepositoLite deposito) {
        super();
        this.data = data;
        this.deposito = deposito;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        InventarioArticoloDTO other = (InventarioArticoloDTO) obj;
        if (data == null) {
            if (other.data != null) {
                return false;
            }
        } else if (!data.equals(other.data)) {
            return false;
        }
        if (deposito == null) {
            if (other.deposito != null) {
                return false;
            }
        } else if (!deposito.equals(other.deposito)) {
            return false;
        }
        return true;
    }

    /**
     * @return the data
     */
    public Date getData() {
        return data;
    }

    /**
     * @return the deposito
     */
    public DepositoLite getDeposito() {
        return deposito;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((data == null) ? 0 : data.hashCode());
        result = prime * result + ((deposito == null) ? 0 : deposito.hashCode());
        return result;
    }
}
