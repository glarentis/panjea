package it.eurotn.panjea.vending.rich.converter;

import java.util.Comparator;

import com.jidesoft.converter.ConverterContext;

import it.eurotn.panjea.vending.domain.Modello;
import it.eurotn.rich.converter.PanjeaCompositeConverter;

public class ModelloConverter extends PanjeaCompositeConverter<Modello> {

    @Override
    public Object fromString(String arg0, ConverterContext arg1) {
        return null;
    }

    @Override
    protected String getCampo1(Modello value) {
        return value.getDescrizione();
    }

    @Override
    protected String getCampo2(Modello value) {
        return value.getCodice();
    }

    @Override
    public Class<Modello> getClasse() {
        return Modello.class;
    }

    @Override
    protected Comparator<Modello> getComparatorCampo1() {
        return new Comparator<Modello>() {

            @Override
            public int compare(Modello o1, Modello o2) {
                return o1.getDescrizione().compareTo(o2.getDescrizione());
            }
        };
    }

    @Override
    protected Comparator<Modello> getComparatorCampo2() {
        return new Comparator<Modello>() {

            @Override
            public int compare(Modello o1, Modello o2) {
                return o1.getCodice().compareTo(o2.getCodice());
            }
        };
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
