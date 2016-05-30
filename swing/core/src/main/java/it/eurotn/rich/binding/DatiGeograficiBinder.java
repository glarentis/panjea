package it.eurotn.rich.binding;

import it.eurotn.panjea.anagrafica.domain.datigeografici.DatiGeografici;

import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.springframework.binding.form.FormModel;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.binding.support.AbstractBinder;

public class DatiGeograficiBinder extends AbstractBinder {

    public static final String FORM_LAYOUT = "layout";
    public static final String FORM_BUILDER = "builder";
    public static final String FORM_FIRST_COLUMN_DEF = "firstColumnDef";
    public static final String SHOW_NAZIONE_CONTROLS = "showNazioneControls";

    private String firstColumnDef = null;
    private boolean showNazioneControls = true;

    /**
     * Costruttore.
     */
    public DatiGeograficiBinder() {
        super(DatiGeografici.class);
    }

    @SuppressWarnings("rawtypes")
    @Override
    protected JComponent createControl(Map context) {
        this.firstColumnDef = (String) context.get(DatiGeograficiBinder.FORM_FIRST_COLUMN_DEF);
        this.showNazioneControls = (boolean) context.get(DatiGeograficiBinder.SHOW_NAZIONE_CONTROLS);

        return new JPanel();
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    protected Binding doBind(JComponent control, FormModel formModel, String formPropertyPath, Map context) {
        context.put(DatiGeograficiBinder.FORM_FIRST_COLUMN_DEF, firstColumnDef);
        context.put(DatiGeograficiBinder.SHOW_NAZIONE_CONTROLS, showNazioneControls);
        DatiGeograficiBinding datiGeograficiBinding = new DatiGeograficiBinding((JPanel) control, formModel,
                formPropertyPath, context);

        return datiGeograficiBinding;
    }

}
