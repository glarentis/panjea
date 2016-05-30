package it.eurotn.rich.binding;

import it.eurotn.panjea.anagrafica.domain.datigeografici.DatiGeografici;

import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.springframework.binding.form.FormModel;
import org.springframework.binding.form.ValidatingFormModel;
import org.springframework.richclient.form.binding.support.CustomBinding;

public class DatiGeograficiBinding extends CustomBinding {

    private ValidatingFormModel datiGeograficiFormModel = null;
    private DatiGeograficiBindingForm form = null;
    private String formPropertyPath = null;
    private String firstColumnDef = null;
    private boolean showNazioneControls = true;

    /**
     * Costruttore.
     *
     * @param datiGeograficiPanel
     *            datiGeograficiPanel
     * @param formModel
     *            formModel
     * @param formPropertyPath
     *            formPropertyPath
     * @param context
     *            context
     */
    public DatiGeograficiBinding(final JPanel datiGeograficiPanel, final FormModel formModel,
            final String formPropertyPath, final Map<String, Object> context) {
        super(formModel, formPropertyPath, DatiGeografici.class);
        this.datiGeograficiFormModel = (ValidatingFormModel) formModel;
        this.formPropertyPath = formPropertyPath;
        this.firstColumnDef = (String) context.get(DatiGeograficiBinder.FORM_FIRST_COLUMN_DEF);
        this.showNazioneControls = (boolean) context.get(DatiGeograficiBinder.SHOW_NAZIONE_CONTROLS);
    }

    /**
     * Attiva i change listeners per i dati geografici.
     */
    public void activateChangeListeners() {
        form.activateChangeListeners();
    }

    /**
     * Disattiva i change listeners per i dati geografici.
     */
    public void deactivateChangeListeners() {
        form.deactivateChangeListeners();
    }

    @Override
    protected JComponent doBindControl() {
        form = new DatiGeograficiBindingForm(datiGeograficiFormModel, formPropertyPath, firstColumnDef,
                showNazioneControls);
        return form.getControl();
    }

    @Override
    protected void enabledChanged() {
    }

    @Override
    protected void readOnlyChanged() {
    }

    @Override
    protected void valueModelChanged(Object obj) {
    }

}
