package it.eurotn.panjea.magazzino.util;

import java.io.Serializable;
import java.util.List;

import it.eurotn.panjea.magazzino.domain.InventarioArticolo;

public class InventarioArticoloStampa implements Serializable {

    private static final long serialVersionUID = -2324003130029165858L;

    private List<InventarioArticolo> inventariArticoli;

    /**
     * Costruttore.
     * 
     * @param inventariArticoli
     *            inventariArticoli
     */
    public InventarioArticoloStampa(final List<InventarioArticolo> inventariArticoli) {
        super();
        this.inventariArticoli = inventariArticoli;
    }

    /**
     * @return the inventariArticoli
     */
    public List<InventarioArticolo> getInventariArticoli() {
        return inventariArticoli;
    }
}
