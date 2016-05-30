package it.eurotn.panjea.rich.factory;

import java.util.HashSet;
import java.util.Iterator;

import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * Estende la JList per aggiungere funzionalità in più. 1)impostando setSelectionMode(4) seleziona elementi multipli con
 * un solo click
 */
public class JecList extends JList implements ListSelectionListener {
    private static final long serialVersionUID = -1940904637907290659L;
    private HashSet<Integer> selectionCache = new HashSet<Integer>();
    private boolean listenerAdded = false;

    @Override
    /*
     * con i==3 seleziono gli elementi multipli con un click.
     */
    public void setSelectionMode(int i) {
        if (i == 3 && !listenerAdded) {
            // Imposto il listener
            addListSelectionListener(this);
            // setto il selectionMode a multiplo
            i = ListSelectionModel.MULTIPLE_INTERVAL_SELECTION;
            listenerAdded = true;
        }
        super.setSelectionMode(i);
    }

    @Override
    public void valueChanged(ListSelectionEvent lse) {
        // se questo evento e' parte di una serie, associati alla stessa modifica
        // non devo eseguire nessuna operazione
        if (lse != null && lse.getValueIsAdjusting()) {
            return;
        }
        removeListSelectionListener(this);
        // remember everything selected as a result of this action
        HashSet<Integer> newSelections = new HashSet<Integer>();
        int size = getModel().getSize();
        for (int i = 0; i < size; i++) {
            if (getSelectionModel().isSelectedIndex(i)) {
                newSelections.add(new Integer(i));
            }
        }

        // turn on everything that was previously selected
        Iterator<Integer> it = selectionCache.iterator();
        while (it.hasNext()) {
            int index = (it.next()).intValue();
            getSelectionModel().addSelectionInterval(index, index);
        }

        // add or remove the delta
        it = newSelections.iterator();
        while (it.hasNext()) {
            Integer nextInt = it.next();
            int index = nextInt.intValue();
            if (selectionCache.contains(nextInt)) {
                getSelectionModel().removeSelectionInterval(index, index);
            } else {
                getSelectionModel().addSelectionInterval(index, index);
            }
        }

        // save selections for next time
        selectionCache.clear();
        for (int i = 0; i < size; i++) {
            if (getSelectionModel().isSelectedIndex(i)) {
                selectionCache.add(new Integer(i));
            }
        }
        addListSelectionListener(this);

    }
}