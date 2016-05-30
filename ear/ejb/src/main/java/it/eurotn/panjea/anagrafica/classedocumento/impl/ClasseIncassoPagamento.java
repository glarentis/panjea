package it.eurotn.panjea.anagrafica.classedocumento.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import it.eurotn.panjea.anagrafica.classedocumento.manager.AbstractClasseTipoDocumento;

/**
 * Classe che descrive le aree per i documenti di incasso pagamenti, non ha area iva.
 *
 * @author adriano
 * @version 1.0, 28/nov/2008
 *
 */
public class ClasseIncassoPagamento extends AbstractClasseTipoDocumento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    public List<String> getTipiAree() {
        List<String> list = new ArrayList<String>();
        list.add("it.eurotn.panjea.contabilita.domain.TipoAreaContabile");
        list.add("it.eurotn.panjea.partite.domain.TipoAreaPartita");
        return list;
    }

    @Override
    public List<String> getTipiCaratteristiche() {
        List<String> list = new ArrayList<String>();
        return list;
    }

}
