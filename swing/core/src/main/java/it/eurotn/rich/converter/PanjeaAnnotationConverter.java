package it.eurotn.rich.converter;

import java.util.Comparator;

import org.apache.commons.beanutils.PropertyUtilsBean;

import com.jidesoft.comparator.ObjectComparatorManager;
import com.jidesoft.converter.ConverterContext;
import com.jidesoft.converter.ObjectConverterManager;

import it.eurotn.entity.annotation.EntityConverter;
import it.eurotn.panjea.exception.GenericException;

@SuppressWarnings("rawtypes")
public class PanjeaAnnotationConverter extends PanjeaCompositeConverter {

    private class ConverterComparator implements Comparator<Object> {
        @Override
        public int compare(Object o1, Object o2) {
            PanjeaAnnotationConverter converter = PanjeaAnnotationConverter.this;
            return converter.toString(o1, null).compareToIgnoreCase(converter.toString(o2, null));
        }
    }

    private String propertiesString;
    private String[] properties;

    private Class<?> clazz;

    private ConverterComparator comparator;

    private PropertyUtilsBean beanUtil = new PropertyUtilsBean();

    /**
     * @param clazz
     *            classe
     */
    public PanjeaAnnotationConverter(final Class<?> clazz) {
        this.propertiesString = clazz.getAnnotation(EntityConverter.class).properties();
        this.clazz = clazz;
        comparator = new ConverterComparator();
    }

    @Override
    public Object fromString(String paramString, ConverterContext paramConverterContext) {
        return null;
    }

    @Override
    protected String getCampo1(Object value) {
        initProperties();
        try {
            return ObjectConverterManager.toString(beanUtil.getProperty(value, properties[0]));
        } catch (Exception e) {
            // rilancio l'eccezione in quanto questo non deve accadere, le proprietà devono essere giuste
            throw new GenericException(
                    "Proprietà " + properties[0] + " non trovata per " + value.getClass().getSimpleName());
        }
    }

    @Override
    protected String getCampo2(Object value) {
        initProperties();
        if (properties.length == 1) {
            return "";
        }
        try {
            return ObjectConverterManager.toString(beanUtil.getProperty(value, properties[1]));
        } catch (Exception e) {
            // rilancio l'eccezione in quanto questo non deve accadere, le proprietà devono essere giuste
            throw new GenericException(
                    "Proprietà " + properties[1] + " non trovata per " + value.getClass().getSimpleName());
        }
    }

    @Override
    public Class<?> getClasse() {
        return clazz;
    }

    @Override
    protected Comparator<Object> getComparatorCampo1() {
        return comparator;
    }

    @Override
    protected Comparator<Object> getComparatorCampo2() {
        return comparator;
    }

    /**
     * inizializza l'array delle proprietà
     */
    private void initProperties() {
        if (properties != null) {
            return;
        }
        if (!propertiesString.contains(",")) {
            properties = new String[] { propertiesString };
        } else {
            properties = propertiesString.split(",");
        }
    }

    /**
     * Registra il converter
     */
    @Override
    public void registra() {
        ObjectComparatorManager.registerComparator(clazz, new ConverterComparator(), null);
        ObjectConverterManager.registerConverter(clazz, this);
    }

    @Override
    public boolean supportFromString(String paramString, ConverterContext paramConverterContext) {
        return false;
    }

    @Override
    public boolean supportToString(Object paramObject, ConverterContext paramConverterContext) {
        return true;
    }

}
