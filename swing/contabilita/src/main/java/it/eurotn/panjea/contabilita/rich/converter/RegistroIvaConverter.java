package it.eurotn.panjea.contabilita.rich.converter;

import com.jidesoft.converter.ConverterContext;

import it.eurotn.panjea.contabilita.domain.RegistroIva;
import it.eurotn.rich.converter.PanjeaConverter;

/**
 * @author fattazzo
 *
 */
public class RegistroIvaConverter extends PanjeaConverter<RegistroIva> {

    @Override
    public Object fromString(String arg0, ConverterContext arg1) {
        return null;
    }

    @Override
    public Class<RegistroIva> getClasse() {
        return RegistroIva.class;
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

        if (arg0 != null && arg0 instanceof RegistroIva) {
            result = ((RegistroIva) arg0).getNumero() + " - " + ((RegistroIva) arg0).getDescrizione();
        }

        return result;
    }

}
