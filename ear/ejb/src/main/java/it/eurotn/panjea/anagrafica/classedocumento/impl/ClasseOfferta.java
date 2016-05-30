package it.eurotn.panjea.anagrafica.classedocumento.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import it.eurotn.panjea.anagrafica.classedocumento.manager.AbstractClasseTipoDocumento;

/**
 * @author Leonardo
 *
 */
public class ClasseOfferta extends AbstractClasseTipoDocumento implements Serializable {

    private static final long serialVersionUID = 3645704001435411430L;

    @Override
    public List<String> getTipiAree() {
        List<String> list = new ArrayList<String>();
        list.add("it.eurotn.panjea.offerte.domain.TipoAreaOfferta");
        return list;
    }

    @Override
    public List<String> getTipiCaratteristiche() {
        List<String> list = new ArrayList<String>();
        return list;
    }

}
