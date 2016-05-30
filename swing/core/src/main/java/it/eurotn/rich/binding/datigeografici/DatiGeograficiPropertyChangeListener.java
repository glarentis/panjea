package it.eurotn.rich.binding.datigeografici;

import java.beans.PropertyChangeListener;
import java.util.Map;

import org.springframework.binding.form.FormModel;

public abstract class DatiGeograficiPropertyChangeListener implements PropertyChangeListener {

    protected String formPropertyPath = null;
    protected FormModel formModel = null;
    protected Map<String, PropertyChangeListener> listeners = null;
    protected SuddivisioniAmministrativeControlController suddivisioniAmministrativeControlController = null;

    /**
     * Costruttore.
     * 
     * @param formPropertyPath
     *            formPropertyPath
     * @param formModel
     *            formModel
     */
    public DatiGeograficiPropertyChangeListener(final String formPropertyPath, final FormModel formModel) {
        super();
        this.formPropertyPath = formPropertyPath;
        this.formModel = formModel;
    }

    /**
     * @param listeners
     *            the listeners to set
     */
    public void setListeners(Map<String, PropertyChangeListener> listeners) {
        this.listeners = listeners;
    }

    /**
     * @param suddivisioniAmministrativeControlController
     *            the suddivisioniAmministrativeControlController to set
     */
    public void setSuddivisioniAmministrativeControlController(
            SuddivisioniAmministrativeControlController suddivisioniAmministrativeControlController) {
        this.suddivisioniAmministrativeControlController = suddivisioniAmministrativeControlController;
    }
}
