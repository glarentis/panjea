/**
 *
 */
package it.eurotn.rich.control.table;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JTable;

import com.jidesoft.converter.ObjectConverterManager;
import com.jidesoft.swing.TableSearchable;

/**
 * Table searchable estesa per controllare la conversione dell'oggetto a String nella ricerca.<br>
 * 
 * Corretto il comportamento del popup di ricerca contestuale:<br>
 * se, una volta iniziata la ricerca, cambio la riga selezionata con il mouse, il popup di ricerca contestuale viene
 * nascosto, ma la ricerca non viene azzerata.<br>
 * 
 * Installo un listener sul popup di ricerca, sull'evento visible (a false), chiamo il metodo hidePopup della searchable
 * in modo da azzerare la ricerca.
 * 
 * @author leonardo
 */
public class JideTableSearchable extends TableSearchable {

    private class SearchPopupChangeListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            Object newValue = evt.getNewValue();
            if (newValue != null && newValue instanceof Boolean && !((Boolean) newValue).booleanValue()) {
                JideTableSearchable.this.hidePopup();
            }
        }
    }

    private SearchPopupChangeListener searchPopupChangeListener = null;
    private SearchPopup searchPopup;

    /**
     * @param paramJTable
     *            paramJTable
     */
    public JideTableSearchable(final JTable paramJTable) {
        super(paramJTable);
    }

    @Override
    protected String convertElementToString(Object obj) {
        String value = ObjectConverterManager.toString(obj);
        if (value == null) {
            value = super.convertElementToString(obj);
        }
        return value;
    }

    @Override
    protected SearchPopup createSearchPopup(String paramString) {
        searchPopup = super.createSearchPopup(paramString);

        // aggiungo un property change sulla proprietà visible.
        // se clicco con il mouse su una riga della tabella cambiando la selezione, il popup di ricerca viene chiuso, ma
        // non viene chiamata la hide (e quindi la ricerca non viene azzerata)
        // chiamo la hide se visible=false
        searchPopup.addPropertyChangeListener("visible", getSearchPopupChangeListener());
        return searchPopup;
    }

    /**
     * @return SearchPopupChangeListener
     */
    private SearchPopupChangeListener getSearchPopupChangeListener() {
        if (searchPopupChangeListener == null) {
            searchPopupChangeListener = new SearchPopupChangeListener();
        }
        return searchPopupChangeListener;
    }

    @Override
    public void hidePopup() {
        // rimuovo il property cahnge sulla hide perchè il popup viene creato ogni volta che eseguo una ricerca
        if (searchPopup != null) {
            searchPopup.removePropertyChangeListener(getSearchPopupChangeListener());
        }
        super.hidePopup();
    }
}