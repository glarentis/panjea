package it.eurotn.panjea.magazzino.util.parametriricerca;

import java.io.Serializable;

import it.eurotn.panjea.magazzino.domain.Listino;
import it.eurotn.panjea.magazzino.domain.VersioneListino;

/**
 *
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
public class ParametriAggiornaManutenzioneListino implements Serializable {

    private static final long serialVersionUID = -8339888161235649164L;

    private VersioneListino versioneListino = null;

    /**
     * Default constructor.
     */
    public ParametriAggiornaManutenzioneListino() {
        super();
        init();
    }

    /**
     * @return the versioneListino
     */
    public VersioneListino getVersioneListino() {
        return versioneListino;
    }

    /**
     * Inizializza i valori di default.
     */
    private void init() {
        versioneListino = new VersioneListino();
        versioneListino.setListino(new Listino());
    }

    /**
     * @param versioneListino
     *            the versioneListino to set
     */
    public void setVersioneListino(VersioneListino versioneListino) {
        this.versioneListino = versioneListino;
    }

}
