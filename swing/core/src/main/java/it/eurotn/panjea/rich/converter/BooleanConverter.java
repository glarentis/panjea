package it.eurotn.panjea.rich.converter;

import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.converter.ConverterContext;

import it.eurotn.rich.converter.PanjeaConverter;

/**
 * @author fattazzo
 *
 */
public class BooleanConverter extends PanjeaConverter<Boolean> {

    @Override
    public Object fromString(String arg0, ConverterContext arg1) {
        return null;
    }

    @Override
    public Class<Boolean> getClasse() {
        return Boolean.class;
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

        if (arg0 != null) {
            Boolean value = (Boolean) arg0;
            result = RcpSupport.getMessage(value.toString());
        }

        return result;
    }

}
