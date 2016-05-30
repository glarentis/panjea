package it.eurotn.rich.control.table;

import it.eurotn.rich.converter.PanjeaCompositeConverter;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JMenuItem;

import com.jidesoft.converter.ObjectConverter;
import com.jidesoft.converter.ObjectConverterManager;

@SuppressWarnings("serial")
public class SwitchConverterMenuItem extends JMenuItem {
    private class SwitchAction extends AbstractAction {
        private Class<?> columnClass;

        /**
         * 
         * Costruttore.
         * 
         * @param columnClass
         *            classe della colonna selezionata
         */
        public SwitchAction(final Class<?> columnClass) {
            super();
            this.columnClass = columnClass;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            ObjectConverter converter = ObjectConverterManager.getConverter(columnClass);
            if (converter instanceof PanjeaCompositeConverter) {
                ((PanjeaCompositeConverter<?>) converter).scambiaVisualizzazione();
            }
        }
    }

    /**
     * 
     * Costruttore.
     * 
     * @param columnClass
     *            classe della colonna selezionata
     */
    public SwitchConverterMenuItem(final Class<?> columnClass) {
        setAction(new SwitchAction(columnClass));
    }
}
