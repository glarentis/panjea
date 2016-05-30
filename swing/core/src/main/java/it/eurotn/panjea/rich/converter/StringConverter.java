package it.eurotn.panjea.rich.converter;

import it.eurotn.rich.converter.PanjeaConverter;

import java.text.Collator;
import java.util.Comparator;

import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.converter.ConverterContext;

public class StringConverter extends PanjeaConverter {

    @Override
    public Object fromString(String arg0, ConverterContext arg1) {
        return arg0;
    }

    @Override
    public Class<?> getClasse() {
        return String.class;
    }

    @Override
    public Comparator<Object> getComparator() {
        return Collator.getInstance();
    }

    @Override
    public boolean supportFromString(String arg0, ConverterContext arg1) {
        return true;
    }

    @Override
    public boolean supportToString(Object arg0, ConverterContext arg1) {
        return true;
    }

    @Override
    public String toString(Object value, ConverterContext context) {
        String result = "";

        if (value != null) {
            result = value.toString();
        }

        if (context != null && context instanceof I18NConverterContext) {
            result = RcpSupport.getMessage(result);
        }

        return result;
    }

}
