package it.eurotn.panjea.rich.converter;

import java.math.BigDecimal;

import org.springframework.binding.convert.ConversionContext;
import org.springframework.binding.convert.support.AbstractConverter;

public class DoubleConverter extends NumberConverter {

    private class DoubleToBigDecimalConverter extends AbstractConverter {

        @Override
        protected Object doConvert(Object arg0, @SuppressWarnings("rawtypes") Class arg1, ConversionContext arg2)
                throws Exception {
            BigDecimal bigDecimal = null;
            if (arg0 != null) {
                bigDecimal = BigDecimal.valueOf(((Double) arg0));
            }
            return bigDecimal;
        }

        @Override
        public Class<?>[] getSourceClasses() {
            return new Class[] { DoubleConverter.this.getClasse() };
        }

        @Override
        public Class<?>[] getTargetClasses() {
            return new Class[] { BigDecimal.class };
        }
    }

    /**
     * Costruttore.
     */
    public DoubleConverter() {
        super();
        addSpringConverter(new DoubleToBigDecimalConverter());
    }

    @Override
    public Class<?> getClasse() {
        return Double.class;
    }

}
