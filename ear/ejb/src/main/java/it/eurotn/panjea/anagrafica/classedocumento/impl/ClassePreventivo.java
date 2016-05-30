package it.eurotn.panjea.anagrafica.classedocumento.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import it.eurotn.panjea.anagrafica.classedocumento.manager.AbstractClasseTipoDocumento;

/**
 * @author mattia
 *
 */
public class ClassePreventivo extends AbstractClasseTipoDocumento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    public List<String> getTipiAree() {
        List<String> list = new ArrayList<String>();
        list.add("it.eurotn.panjea.preventivi.domain.documento.TipoAreaPreventivo");
        return list;
    }

    @Override
    public List<String> getTipiCaratteristiche() {
        return Collections.<String> emptyList();
    }
}
