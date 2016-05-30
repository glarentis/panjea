package it.eurotn.rich.converter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.binding.convert.ConversionContext;
import org.springframework.binding.convert.support.AbstractConverter;

import com.jidesoft.comparator.ComparatorContext;
import com.jidesoft.comparator.ObjectComparatorManager;
import com.jidesoft.converter.ConverterContext;
import com.jidesoft.converter.ObjectConverter;
import com.jidesoft.converter.ObjectConverterManager;

/**
 *
 * @author fattazzo
 *
 */
public abstract class PanjeaConverter<T> implements ObjectConverter {

    private class ObjectFromStringConverter extends AbstractConverter {

        @SuppressWarnings("rawtypes")
        @Override
        protected Object doConvert(Object arg0, Class arg1, ConversionContext arg2) throws Exception {
            return PanjeaConverter.this.fromString((String) arg0, null);
        }

        @Override
        public Class<?>[] getSourceClasses() {
            return new Class[] { String.class };
        }

        @Override
        public Class<?>[] getTargetClasses() {
            return new Class[] { PanjeaConverter.this.getClasse() };
        }
    }

    private class ObjectToStringConverter extends AbstractConverter {

        @Override
        protected Object doConvert(Object arg0, @SuppressWarnings("rawtypes") Class arg1, ConversionContext arg2)
                throws Exception {
            return PanjeaConverter.this.toString(arg0, null);
        }

        @Override
        public Class<?>[] getSourceClasses() {
            return new Class[] { PanjeaConverter.this.getClasse() };
        }

        @Override
        public Class<?>[] getTargetClasses() {
            return new Class[] { String.class };
        }
    }

    private static final Logger LOGGER = Logger.getLogger(PanjeaConverter.class);

    private final List<AbstractConverter> abstractConverters;

    private ConverterContext converterContext = null;

    private ComparatorContext comparatorContext = null;

    /**
     * Costruttore.
     */
    public PanjeaConverter() {
        this(true);
    }

    /**
     * Costruttore.
     *
     * @param registerDefaultConverter
     *            registra il converter di default per la classe (il converter di spring di default
     *            non ha un context, quindi se registro più converter sulla stessa classe non saprò
     *            quale verrà usato, non devo registrarlo)
     */
    public PanjeaConverter(final boolean registerDefaultConverter) {
        super();
        abstractConverters = new ArrayList<AbstractConverter>();

        if (registerDefaultConverter) {
            // se il converter supporta il toString aggiungo il converter ( spring ) di default
            if (supportToString(null, null)) {
                abstractConverters.add(new ObjectToStringConverter());
            }

            // se il converter supporta il fromString aggiungo il converter ( spring ) di default
            if (supportFromString("", null)) {
                abstractConverters.add(new ObjectFromStringConverter());
            }
        }
    }

    /**
     * Aggiunge un converter.
     *
     * @param abstractConverter
     *            da aggiungere
     */
    public void addSpringConverter(AbstractConverter abstractConverter) {
        abstractConverters.add(abstractConverter);
    }

    /**
     * Classe gestita dal converter.
     *
     * @return class
     */
    public abstract Class<T> getClasse();

    /**
     * @return comparator per i converter
     */
    public Comparator<T> getComparator() {
        return new Comparator<T>() {

            @Override
            public int compare(T o1, T o2) {
                PanjeaConverter<T> converter = PanjeaConverter.this;
                return converter.toString(o1, converter.getConverterContext())
                        .compareToIgnoreCase(converter.toString(o2, converter.getConverterContext()));
            }
        };
    }

    /**
     * @return comparator context legato al comparator
     */
    public ComparatorContext getComparatorContext() {
        return comparatorContext;
    }

    /**
     * @return Contesto legato al converter.
     */
    public ConverterContext getConverterContext() {
        return converterContext;
    }

    /**
     * Restituisce tutti i converter di spring configurati.
     *
     * @return array di converter
     */
    public AbstractConverter[] getSpringConverters() {
        AbstractConverter[] convertArray = new AbstractConverter[abstractConverters.size()];

        int idx = 0;
        for (AbstractConverter abstractConverter : abstractConverters) {
            convertArray[idx] = abstractConverter;
            idx++;
        }

        return convertArray;
    }

    /***
     * registra i converter ed i comparator nei manager di jide.
     */
    public void registra() {
        if (getComparator() != null) {
            ObjectComparatorManager.registerComparator(getClasse(), getComparator(), getComparatorContext());
        }

        if (getConverterContext() != null) {
            ObjectConverterManager.registerConverter(getClasse(), this, getConverterContext());
        } else {
            ObjectConverterManager.registerConverter(getClasse(), this);
        }
    }

    /**
     * @param converterContext
     *            setter of converterContext
     */
    public void setConverterContext(ConverterContext converterContext) {
        this.converterContext = converterContext;
    }
}
