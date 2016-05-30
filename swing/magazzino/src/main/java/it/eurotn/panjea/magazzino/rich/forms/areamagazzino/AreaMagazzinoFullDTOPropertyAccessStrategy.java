package it.eurotn.panjea.magazzino.rich.forms.areamagazzino;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.PropertyAccessor;
import org.springframework.binding.support.ClassPropertyAccessStrategy;
import org.springframework.binding.value.ValueModel;
import org.springframework.binding.value.support.ValueHolder;
import org.springframework.richclient.beans.DefaultMemberPropertyAccessor;

import it.eurotn.panjea.magazzino.util.AreaMagazzinoFullDTO;

public final class AreaMagazzinoFullDTOPropertyAccessStrategy extends ClassPropertyAccessStrategy {

    private class AreaMagazzinoFullDTOPropertyAccessor extends DefaultMemberPropertyAccessor {

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
        public AreaMagazzinoFullDTOPropertyAccessor(final ValueModel target, final boolean fieldAccessEnabled,
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

    public static final String BEAN_ID = "areaMagazzinoFullDTOPropertyAccessStrategy";

    private Map<String, Class<?>> propertyAccessorMap;

    {
        propertyAccessorMap = new HashMap<>();
    }

    /**
     * Costruttore.
     */
    public AreaMagazzinoFullDTOPropertyAccessStrategy() {
        super(new ValueHolder(new AreaMagazzinoFullDTO()), false, false);
        new AreaMagazzinoFullDTOPropertyAccessor(getDomainObjectHolder(), false, false);
    }

    @Override
    protected PropertyAccessor getPropertyAccessor() {
        return new AreaMagazzinoFullDTOPropertyAccessor(getDomainObjectHolder(), false, false);
    }

    /**
     * @param propertyAccessorMap
     *            the propertyAccessorMap to set
     */
    public final void setPropertyAccessorMap(Map<String, Class<?>> propertyAccessorMap) {
        this.propertyAccessorMap = propertyAccessorMap;
    }

}