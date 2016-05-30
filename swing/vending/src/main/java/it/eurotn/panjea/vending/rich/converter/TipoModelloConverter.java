package it.eurotn.panjea.vending.rich.converter;

import java.util.Comparator;

import com.jidesoft.converter.ConverterContext;

import it.eurotn.panjea.vending.domain.TipoModello;
import it.eurotn.rich.converter.PanjeaCompositeConverter;

public class TipoModelloConverter extends PanjeaCompositeConverter<TipoModello> {

    @Override
    public Object fromString(String arg0, ConverterContext arg1) {
        return null;
    }

    @Override
    protected String getCampo1(TipoModello value) {
        return value.getDescrizione();
    }

    @Override
    protected String getCampo2(TipoModello value) {
        return value.getCodice();
    }

    @Override
    public Class<TipoModello> getClasse() {
        return TipoModello.class;
    }

    @Override
    protected Comparator<TipoModello> getComparatorCampo1() {
        return new Comparator<TipoModello>() {

            @Override
            public int compare(TipoModello o1, TipoModello o2) {
                return o1.getDescrizione().compareTo(o2.getDescrizione());
            }
        };
    }

    @Override
    protected Comparator<TipoModello> getComparatorCampo2() {
        return new Comparator<TipoModello>() {

            @Override
            public int compare(TipoModello o1, TipoModello o2) {
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
