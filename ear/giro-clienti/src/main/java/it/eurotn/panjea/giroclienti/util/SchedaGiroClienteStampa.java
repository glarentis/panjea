package it.eurotn.panjea.giroclienti.util;

import java.io.Serializable;

import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.ordini.domain.AttributoRiga;
import it.eurotn.panjea.ordini.domain.RigaArticolo;

public class SchedaGiroClienteStampa implements Serializable {

    private static final long serialVersionUID = -4311594045152421404L;

    private EntitaLite entita;

    private RigaArticolo rigaArticolo;

    /**
     * @return the articolo
     */
    public ArticoloLite getArticolo() {
        return rigaArticolo.getArticolo();
    }

    /**
     * @return the attributo1
     */
    public AttributoRiga getAttributo1() {
        return rigaArticolo.getAttributo("conf");
    }

    /**
     * @return the attributo2
     */
    public AttributoRiga getAttributo2() {
        return rigaArticolo.getAttributo("peso");
    }

    /**
     * @return the entita
     */
    public EntitaLite getEntita() {
        return entita;
    }

    /**
     * @param entita
     *            the entita to set
     */
    public void setEntita(EntitaLite entita) {
        this.entita = entita;
    }

}
