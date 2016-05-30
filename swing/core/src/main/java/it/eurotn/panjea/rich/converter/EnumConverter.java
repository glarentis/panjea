package it.eurotn.panjea.rich.converter;

import it.eurotn.rich.converter.PanjeaConverter;

import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.converter.ConverterContext;

public class EnumConverter extends PanjeaConverter {

    @Override
    public Object fromString(String arg0, ConverterContext arg1) {
        return null;
    }

    @Override
    public Class<?> getClasse() {
        return Enum.class;
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
        if (arg0 != null && arg0 instanceof Enum) {
            return RcpSupport.getMessage(arg0.getClass().getName() + "." + ((Enum<?>) arg0).name());
        } else {
            return "";
        }
    }
}
