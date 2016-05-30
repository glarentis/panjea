package it.eurotn.panjea.contabilita.rich.converter;

import com.jidesoft.converter.ConverterContext;

import it.eurotn.panjea.contabilita.domain.Conto;
import it.eurotn.rich.converter.PanjeaConverter;

public class ContoConverter extends PanjeaConverter {

    @Override
    public Object fromString(String arg0, ConverterContext arg1) {
        return null;
    }

    @Override
    public Class<?> getClasse() {
        return Conto.class;
    }

    @Override
    public boolean supportFromString(String arg0, ConverterContext arg1) {
        return false;
    }

    @Override
    public boolean supportToString(Object arg0, ConverterContext arg1) {
        return true;
    }

    @Override
    public String toString(Object arg0, ConverterContext arg1) {
        String result = "";
        if (arg0 instanceof Conto) {
            result = ((Conto) arg0).getCodice();
        }
        return result;
    }

}
