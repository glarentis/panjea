package it.eurotn.rich.binding.convert.support;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.binding.convert.ConversionContext;
import org.springframework.binding.convert.Converter;
import org.springframework.binding.convert.support.AbstractConverter;
import org.springframework.core.ReflectiveVisitorHelper;

public class CollectionConverter extends AbstractConverter implements Converter {

    protected static class ValuesVisitor {
        /**
         * 
         * @param values
         *            valori da trasformare
         * @return lista contenente i valori
         */
        List<?> visit(Collection<?> values) {
            return new ArrayList<Object>(values);
        }

        /**
         * 
         * @param values
         *            valori da trasformare
         * @return lista contenente i valori
         */
        List<?> visit(List<?> values) {
            return values;
        }

        /**
         * 
         * @param value
         *            valore da trasformare
         * @return lista contenente i valori
         */
        List<?> visit(Object value) {
            return visit(new Object[] { value });
        }

        /**
         * 
         * @param values
         *            valori da trasformare
         * @return lista contenente i valori
         */
        List<?> visit(Object[] values) {
            return Arrays.asList(values);
        }

        /**
         * @return lista vuota se i valori sono null.
         */
        List<?> visitNull() {
            return new ArrayList<Object>();
        }
    }

    private static final Class<?>[] SOURCE_CLASSES = new Class[] { Object.class, Collection.class, List.class,
            Object[].class };

    private static final Class<?>[] TARGET_CLASSES = new Class[] { Collection.class, List.class, Object[].class };

    private final ReflectiveVisitorHelper visitorHelper = new ReflectiveVisitorHelper();

    private Object visitor = new ValuesVisitor();

    @SuppressWarnings("rawtypes")
    @Override
    protected Object doConvert(Object sourceValue, Class targetClass, ConversionContext context) throws Exception {
        Collection<?> values = (Collection) visitorHelper.invokeVisit(visitor, sourceValue);
        if (Object[].class == targetClass) {
            return values.toArray();
        } else if (Set.class == targetClass) {
            return new HashSet<Object>(values);
        }
        return values;
    }

    @Override
    public Class<?>[] getSourceClasses() {
        return SOURCE_CLASSES;
    }

    @Override
    public Class<?>[] getTargetClasses() {
        return TARGET_CLASSES;
    }

}
