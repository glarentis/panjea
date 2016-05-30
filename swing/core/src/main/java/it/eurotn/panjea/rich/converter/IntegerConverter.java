package it.eurotn.panjea.rich.converter;

import java.text.DecimalFormat;
import java.text.Format;

import org.apache.log4j.Logger;

import com.jidesoft.converter.ConverterContext;

public class IntegerConverter extends NumberConverter {

    private static Logger logger = Logger.getLogger(IntegerConverter.class);

    private static final Format INTEGER_FORMAT = new DecimalFormat("0");

    /**
     * Costruttore.
     */
    public IntegerConverter() {
        super();
        setCustomFormat(INTEGER_FORMAT);
    }

    @Override
    public Object fromString(String s, ConverterContext convertercontext) {
        Integer result = null;
        s = s.trim();
        if (s != null && !s.isEmpty()) {
            try {
                result = Integer.parseInt(s);
            } catch (NumberFormatException e) {
                logger.warn("-->errore durante la conversione di '" + s + "' a int", e);
                result = null;
            }
        }
        return result;
    }

    @Override
    public Class<?> getClasse() {
        return Integer.class;
    }

    @Override
    public boolean supportFromString(String s, ConverterContext convertercontext) {
        return true;
    }

}
