package it.eurotn.panjea.manutenzioni.rich.converter;

import java.util.Comparator;

import com.jidesoft.converter.ConverterContext;

import it.eurotn.panjea.manutenzioni.domain.ArticoloMI;
import it.eurotn.rich.converter.PanjeaCompositeConverter;

public class ArticoloMIConverter extends PanjeaCompositeConverter<ArticoloMI>implements Comparator<ArticoloMI> {

    @Override
    public int compare(ArticoloMI o1, ArticoloMI o2) {
        return ArticoloMIConverter.this.toString(o1, null).compareTo(ArticoloMIConverter.this.toString(o2, null));
    }

    @Override
    public Object fromString(String arg0, ConverterContext arg1) {
        return null;
    }

    @Override
    protected String getCampo1(ArticoloMI value) {
        return value.getCodice();
    }

    @Override
    protected String getCampo2(ArticoloMI value) {
        return value.getDescrizione();
    }

    @Override
    public Class<ArticoloMI> getClasse() {
        return ArticoloMI.class;
    }

    @Override
    protected Comparator<ArticoloMI> getComparatorCampo1() {
        return this;
    }

    @Override
    protected Comparator<ArticoloMI> getComparatorCampo2() {
        return this;
    }

    @Override
    public boolean supportFromString(String arg0, ConverterContext arg1) {
        return false;
    }

    @Override
    public boolean supportToString(Object arg0, ConverterContext arg1) {
        return true;
    }

}
