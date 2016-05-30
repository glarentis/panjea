package it.eurotn.panjea.ordini.rich.forms.areaordine;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.PropertyAccessor;
import org.springframework.binding.support.ClassPropertyAccessStrategy;
import org.springframework.binding.value.ValueModel;
import org.springframework.binding.value.support.ValueHolder;
import org.springframework.richclient.beans.DefaultMemberPropertyAccessor;

import it.eurotn.panjea.ordini.util.AreaOrdineFullDTO;

public final class AreaOrdineFullDTOPropertyAccessStrategy extends ClassPropertyAccessStrategy {

    private class AreaOrdineFullDTOPropertyAccessor extends DefaultMemberPropertyAccessor {

        /**
         * Costruttore.
         *
         * @param target
         *            object
         * @param fieldAccessEnabled
         *            fieldAccessEnabled
         * @param strictNullHandlingEnabled
         *            strictNullHandlingEnabled
         */
        public AreaOrdineFullDTOPropertyAccessor(final ValueModel target, final boolean fieldAccessEnabled,
                final boolean strictNullHandlingEnabled) {
            super(target.getValue().getClass(), target.getValue(), fieldAccessEnabled, strictNullHandlingEnabled);
        }

        @Override
        public Class<?> getPropertyType(String propertyPath) {
            Class<?> result = ObjectUtils.defaultIfNull(propertyAccessorMap.get(propertyPath),
                    super.getPropertyType(propertyPath));
            return result;
        }

        @Override
        public Object getPropertyValue(String propertyPath) {
            Object object = null;
            try {
                object = super.getPropertyValue(propertyPath);
            } catch (Exception e) {
                object = null;
            }
            return object;
        }
    }

    public static final String BEAN_ID = "areaOrdineFullDTOPropertyAccessStrategy";

    private Map<String, Class<?>> propertyAccessorMap;

    {
        propertyAccessorMap = new HashMap<>();
    }

    /**
     * Costruttore.
     */
    public AreaOrdineFullDTOPropertyAccessStrategy() {
        super(new ValueHolder(new AreaOrdineFullDTO()), false, false);
        new AreaOrdineFullDTOPropertyAccessor(getDomainObjectHolder(), false, false);
    }

    @Override
    protected PropertyAccessor getPropertyAccessor() {
        return new AreaOrdineFullDTOPropertyAccessor(getDomainObjectHolder(), false, false);
    }

    /**
     * @param propertyAccessorMap
     *            the propertyAccessorMap to set
     */
    public final void setPropertyAccessorMap(Map<String, Class<?>> propertyAccessorMap) {
        this.propertyAccessorMap = propertyAccessorMap;
    }

}