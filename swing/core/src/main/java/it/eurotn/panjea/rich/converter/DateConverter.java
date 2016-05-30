package it.eurotn.panjea.rich.converter;

import it.eurotn.rich.converter.PanjeaConverter;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

import com.jidesoft.comparator.DateComparator;
import com.jidesoft.converter.ConverterContext;

public class DateConverter extends PanjeaConverter {

    private static final String DEFAULT_DATE_PATTERN = "dd/MM/yyyy";

    public static final ConverterContext ORA_CONTEXT = new ConverterContext("oraContext", "HH:mm");

    private SimpleDateFormat dateFormat = new SimpleDateFormat();

    @Override
    public Object fromString(String arg0, ConverterContext arg1) {
        return null;
    }

    @Override
    public Class<?> getClasse() {
        return Date.class;
    }

    @Override
    public Comparator<Object> getComparator() {
        return DateComparator.getInstance();
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
    public String toString(Object value, ConverterContext context) {

        String result = "";

        if (value != null && value instanceof Date) {
            dateFormat.applyPattern(DEFAULT_DATE_PATTERN);
            if (ORA_CONTEXT.equals(context)) {
                dateFormat.applyPattern((String) ORA_CONTEXT.getUserObject());
            }
            if (context != null && context.getUserObject() != null) {
                dateFormat.applyPattern((String) context.getUserObject());
            }
            result = dateFormat.format(value);
        }

        return result;
    }

}
