package it.eurotn.panjea.rich.editors.preference;

import it.eurotn.panjea.anagrafica.domain.Preference;
import it.eurotn.panjea.rich.bd.IPreferenceBD;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import org.springframework.richclient.command.AbstractCommand;

public class PreferencePage extends FormBackedDialogPageEditor {

    public static final String PAGE_ID = "preferencePage";

    private IPreferenceBD preferenceBD;

    /**
     * Costruttore.
     * 
     */
    public PreferencePage() {
        super(PAGE_ID, new PreferenceForm());
    }

    @Override
    protected Object doDelete() {
        Preference preference = (Preference) getBackingFormPage().getFormObject();

        preferenceBD.cancellaPreference(preference);

        return preference;
    }

    @Override
    protected Object doSave() {
        return preferenceBD.salvaPreference((Preference) getBackingFormPage().getFormObject());
    }

    @Override
    protected AbstractCommand[] getCommand() {
        return toolbarPageEditor.getDefaultCommand(true);
    }

    @Override
    public void loadData() {
    }

    @Override
    public void onPostPageOpen() {
    }

    @Override
    public boolean onPrePageOpen() {
        return true;
    }

    @Override
    public void refreshData() {
    }

    /**
     * @param preferenceBD
     *            the preferenceBD to set
     */
    public void setPreferenceBD(IPreferenceBD preferenceBD) {
        this.preferenceBD = preferenceBD;
    }

}
