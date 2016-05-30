package it.eurotn.rich.binding.value.support;

import org.springframework.binding.value.support.DefaultValueChangeDetector;

import it.eurotn.entity.EntityBase;
import it.eurotn.entity.EntityBaseCodice;

/**
 * Confronta gi oggetti per capire se sono cambiati.<br>
 * Rispetto al {@link DefaultValueChangeDetector} anche la classe che ereditano da {@link EntityBase} o da
 * {@link EntityBaseCodice} vengono <br>
 * <br>
 *
 *
 * @author giangi
 *
 */
public class PanjeaValueChangeDetector extends DefaultValueChangeDetector {

    @Override
    public boolean hasValueChanged(Object oldValue, Object newValue) {
        if (oldValue != null && classesWithSafeEquals.contains(oldValue.getClass())) {
            return !oldValue.equals(newValue);
        }

        return oldValue != newValue;
    }
}
