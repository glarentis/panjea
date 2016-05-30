package it.eurotn.panjea.mrp.rich.editors.risultato;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;

import it.eurotn.panjea.mrp.util.ParametriMrpRisultato;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

public class ParametriMrpRisultatoPage extends FormBackedDialogPageEditor {

    /**
     * ParametriMrpRisultatoDirtyChangeListener.
     */
    private class ParametriMrpRisultatoDirtyChangeListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            getForm().commit();
            ParametriMrpRisultato parametriMrpRisultato = (ParametriMrpRisultato) getForm()
                    .getFormObject();
            firePropertyChange(OBJECT_CHANGED, null, parametriMrpRisultato);
        }
    }

    private static final String PAGE_ID = "parametriMrpRisultatoPage";

    private ParametriMrpRisultatoDirtyChangeListener parametriMrpRisultatoDirtyChangeListener = null;

    /**
     * Costruttore.
     */
    public ParametriMrpRisultatoPage() {
        super(PAGE_ID, new ParametriMrpRisultatoForm(new ParametriMrpRisultato()));
        getForm().getValueModel("evidenziaOrdine")
                .addValueChangeListener(getParametriMrpRisultatoDirtyChangeListener());
        getForm().getValueModel("areaOrdine")
                .addValueChangeListener(getParametriMrpRisultatoDirtyChangeListener());
        getForm().getValueModel("escludiOrdinati")
                .addValueChangeListener(getParametriMrpRisultatoDirtyChangeListener());
    }

    @Override
    protected JComponent createToobar() {
        return null;
    }

    @Override
    public void dispose() {
        getForm().getFormModel()
                .removePropertyChangeListener(getParametriMrpRisultatoDirtyChangeListener());
        super.dispose();
    }

    /**
     * @return ParametriMrpRisultatoDirtyChangeListener
     */
    private ParametriMrpRisultatoDirtyChangeListener getParametriMrpRisultatoDirtyChangeListener() {
        if (parametriMrpRisultatoDirtyChangeListener == null) {
            parametriMrpRisultatoDirtyChangeListener = new ParametriMrpRisultatoDirtyChangeListener();
        }
        return parametriMrpRisultatoDirtyChangeListener;
    }

    @Override
    public boolean isDirty() {
        return false;
    }

    @Override
    public void loadData() {
    }

    @Override
    public void onPostPageOpen() {
    }

    @Override
    public void refreshData() {
    }

}
