package it.eurotn.rich.converter;

import java.util.Comparator;

import com.jidesoft.converter.ObjectConverterManager;

public class ConverterComparator implements Comparator<Object> {
    @Override
    public int compare(Object o1, Object o2) {
        String o1String = ObjectConverterManager.toString(o1);
        String o2String = ObjectConverterManager.toString(o2);
        return o1String.compareToIgnoreCase(o2String);
    }
}