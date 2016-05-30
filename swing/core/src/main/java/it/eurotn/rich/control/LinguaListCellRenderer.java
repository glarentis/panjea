package it.eurotn.rich.control;

import java.awt.Component;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

public class LinguaListCellRenderer extends DefaultListCellRenderer {

    private static final long serialVersionUID = 4244618831138503153L;
    private static Map<String, String> lingue = null;

    static {
        lingue = new HashMap<String, String>();

        for (Locale locale : Locale.getAvailableLocales()) {
            lingue.put(locale.getLanguage(), locale.getDisplayLanguage());
        }
    }

    /**
     * @return Map<String, String>
     */
    @SuppressWarnings("unchecked")
    public Set<String> getLingue() {
        return (Set<String>) (lingue != null ? lingue.keySet() : Collections.emptySet());
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
            boolean cellHasFocus) {
        Component comp = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (lingue.containsKey(value)) {
            setText(value + " - " + lingue.get(value));
        }
        return comp;
    }

}
