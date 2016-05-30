package it.eurotn.panjea.rich.converter;

import java.math.BigDecimal;

import org.apache.log4j.Logger;
import org.springframework.binding.convert.ConversionContext;
import org.springframework.binding.convert.support.AbstractConverter;

import com.jidesoft.converter.ConverterContext;

public class BigDecimalConverter extends NumberConverter {

    private class BigDecimalToDoubleConverter extends AbstractConverter {

        @Override
        protected Object doConvert(Object arg0, @SuppressWarnings("rawtypes") Class arg1, ConversionContext arg2)
                throws Exception {

            Double doubleVal = null;
            if (arg0 != null) {
                doubleVal = ((BigDecimal) arg0).doubleValue();
            }
            return doubleVal;
        }

        @Override
        public Class<?>[] getSourceClasses() {
            return new Class[] { BigDecimalConverter.this.getClasse() };
        }

        @Override
        public Class<?>[] getTargetClasses() {
            return new Class[] { Double.class };
        }
    }

    private static Logger logger = Logger.getLogger(BigDecimalConverter.class);

    /**
     * Costruttore.
     */
    public BigDecimalConverter() {
        super();
        addSpringConverter(new BigDecimalToDoubleConverter());
    }

    @Override
    public Object fromString(String arg0, ConverterContext arg1) {
        BigDecimal result = BigDecimal.ZERO;
        try {
            result = new BigDecimal(arg0.replace(",", "."));
        } catch (NumberFormatException e) {
            logger.warn("-->errore nel trasformare la stringa in BigDecimal. Stringa da trasformare" + arg0);
        }
        return result;
    }

    @Override
    public Class<?> getClasse() {
        return BigDecimal.class;
    }

    @Override
    public boolean supportFromString(String arg0, ConverterContext arg1) {
        return true;
    }

}
