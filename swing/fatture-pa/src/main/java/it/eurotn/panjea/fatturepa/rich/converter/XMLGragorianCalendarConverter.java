package it.eurotn.panjea.fatturepa.rich.converter;

import javax.xml.datatype.XMLGregorianCalendar;

import com.jidesoft.converter.ConverterContext;
import com.jidesoft.converter.ObjectConverterManager;

import it.eurotn.rich.converter.PanjeaConverter;

public class XMLGragorianCalendarConverter extends PanjeaConverter<XMLGregorianCalendar> {

    @Override
    public Object fromString(String arg0, ConverterContext arg1) {
        return null;
    }

    @Override
    public Class<XMLGregorianCalendar> getClasse() {
        return XMLGregorianCalendar.class;
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

        if (value != null && value instanceof XMLGregorianCalendar) {
            result = ObjectConverterManager.toString(((XMLGregorianCalendar) value).toGregorianCalendar().getTime());
        }

        return result;
    }

}
