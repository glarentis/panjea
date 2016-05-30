package it.eurotn.panjea.contabilita.rich.converter;

import com.jidesoft.converter.ConverterContext;

import it.eurotn.panjea.centricosto.domain.CentroCosto;
import it.eurotn.rich.converter.PanjeaConverter;

public class CentroCostoConverter extends PanjeaConverter {

    @Override
    public Object fromString(String arg0, ConverterContext arg1) {
        return null;
    }

    @Override
    public Class<?> getClasse() {
        return CentroCosto.class;
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
        if (arg0 instanceof CentroCosto) {
            CentroCosto centroCosto = (CentroCosto) arg0;
            String codice = centroCosto.getCodice();
            String descrizione = centroCosto.getDescrizione();
            if (codice != null) {
                result = codice + " - " + descrizione;
            }
        }
        return result;
    }
}
