/**
 *
 */
package it.eurotn.panjea.rich.forms;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.springframework.binding.form.FormModel;
import org.springframework.binding.form.ValidatingFormModel;
import org.springframework.richclient.core.Guarded;
import org.springframework.richclient.form.FormGuard;

/**
 * FormGuard che abilita o disabilita i Guarded al suo interno basandosi sulla proprietà <br>
 * READONLY_PROPERTY di {@link ValidatingFormModel}. <br>
 * 
 * Attravero questa classe viene creato e gestito il {@link FormGuard} di default di spring che abilita/disabilita i
 * <br>
 * guarded sulle variazioni del FormModel: ENABLED_PROPERTY, HAS_ERRORS_PROPERTY e DIRTY_PROPERTY
 * 
 * 
 * @author adriano
 * @version 1.0, 15/set/2008
 */
public class PanjeaFormGuard implements PropertyChangeListener {

    public static final int ON_EDITABLE = FormGuard.ON_ENABLED;

    private ValidatingFormModel formModel;

    private FormGuard formGuard;

    private Map<Guarded, Integer> guardedEntries = Collections.synchronizedMap(new HashMap<Guarded, Integer>());

    /**
     * @param formModel
     */
    public PanjeaFormGuard(ValidatingFormModel formModel) {
        this.formModel = formModel;
        this.formGuard = new FormGuard(formModel);
        this.formModel.addPropertyChangeListener(FormModel.READONLY_PROPERTY, this);
    }

    /**
     * @param formModel
     * @param guarded
     */
    public PanjeaFormGuard(ValidatingFormModel formModel, Guarded guarded) {
        this(formModel, guarded, FormGuard.FORMERROR_GUARDED);
    }

    /**
     * @param formModel
     * @param guarded
     * @param mask
     */
    public PanjeaFormGuard(ValidatingFormModel formModel, Guarded guarded, int mask) {
        this(formModel);
        addGuarded(guarded, mask);
    }

    /**
     * aggiunge un Guarded da gestire.
     * 
     * @param newGuarded
     * @param mask
     */
    public void addGuarded(Guarded newGuarded, int mask) {
        this.guardedEntries.put(newGuarded, new Integer(mask));
        newGuarded.setEnabled(stateMatchesMask(getFormModelState(), mask));
        formGuard.addGuarded(newGuarded, mask);
    }

    public void clear() {

        for (Guarded guarded : guardedEntries.keySet()) {
            removeGuarded(guarded);
        }

        guardedEntries.clear();
        guardedEntries = null;

        formGuard = null;
        formModel = null;
    }

    /**
     * 
     * @return di formState
     */
    private int getFormModelState() {
        int formState = 0;
        if (formModel != null) {
            if (!formModel.getHasErrors()) {
                formState += FormGuard.ON_NOERRORS;
            }
            if (formModel.isDirty()) {
                formState += FormGuard.ON_ISDIRTY;
            }
            if (!formModel.isReadOnly()) {
                formState += ON_EDITABLE;
            }
        }
        return formState;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        updateAllGuarded();
    }

    /**
     * rimuove il {@link Guarded} toRemove dalla gestione di questo {@link PanjeaFormGuard}.
     * 
     * @param toRemove
     *            guarded to remove
     * @return <code>false</code> se toRemove non è presente
     */
    public boolean removeGuarded(Guarded toRemove) {
        Object mask = this.guardedEntries.remove(toRemove);
        formGuard.removeGuarded(toRemove);
        return mask != null;

    }

    /**
     * 
     * @param formState
     * @param mask
     * @return
     */
    private boolean stateMatchesMask(int formState, int mask) {
        return ((mask & formState) == mask);
    }

    /**
     * aggiorna tutti i {@link Guarded}.
     */
    private void updateAllGuarded() {
        int formState = getFormModelState();

        Iterator guardedIter = this.guardedEntries.keySet().iterator();
        while (guardedIter.hasNext()) {
            Guarded guarded = (Guarded) guardedIter.next();
            int mask = (this.guardedEntries.get(guarded)).intValue();

            boolean b = stateMatchesMask(formState, mask);
            guarded.setEnabled(b);
        }
    }

}
