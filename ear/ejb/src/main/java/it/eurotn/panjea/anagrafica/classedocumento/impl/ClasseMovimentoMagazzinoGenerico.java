package it.eurotn.panjea.anagrafica.classedocumento.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import it.eurotn.panjea.anagrafica.classedocumento.IClasseTipoDocumento;
import it.eurotn.panjea.anagrafica.classedocumento.manager.AbstractClasseTipoDocumento;

public class ClasseMovimentoMagazzinoGenerico extends AbstractClasseTipoDocumento implements Serializable {

    private static final long serialVersionUID = 4571752646931559019L;

    @Override
    public List<String> getTipiAree() {
        List<String> list = new ArrayList<String>();
        list.add("it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino");
        list.add("it.eurotn.panjea.partite.domain.TipoAreaPartita");
        list.add("it.eurotn.panjea.manutenzioni.domain.documento.TipoAreaInstallazione");
        list.add(IClasseTipoDocumento.TIPO_AREA_IVA);
        return list;
    }

    @Override
    public List<String> getTipiCaratteristiche() {
        List<String> list = new ArrayList<String>();
        list.add(IClasseTipoDocumento.TIPO_CARATTERISTICA_NOTA_CREDITO);
        list.add(IClasseTipoDocumento.TIPO_CARATTERISTICA_LOTTI);
        return list;
    }
}
