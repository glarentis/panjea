package it.eurotn.panjea.preferences;

import javax.swing.JComponent;

import org.springframework.binding.form.FormModel;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.form.Form;
import org.springframework.richclient.form.FormModelHelper;
import org.springframework.richclient.form.binding.swing.SwingBindingFactory;
import org.springframework.richclient.form.builder.TableFormBuilder;
import org.springframework.richclient.preference.FormBackedPreferencePage;
import org.springframework.richclient.settings.Settings;
import org.springframework.richclient.settings.SettingsException;
import org.springframework.richclient.settings.SettingsManager;

import it.eurotn.rich.editors.IEditorCommands;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;
import it.eurotn.rich.form.PanjeaAbstractForm;

public class GeneralSettingsPage extends FormBackedPreferencePage implements IEditorCommands, IPageLifecycleAdvisor {

    private static final String PAGE_ID = "generalSettingsPage";

    private SettingsManager settingsManager = null;

    /**
    *
    */
    public GeneralSettingsPage() {
        super(PAGE_ID);
    }

    @Override
    protected Form createForm() {
        GeneralSettingsPM generalSettings = null;
        try {
            generalSettings = new GeneralSettingsPM(getSettingsManagerLocal().getUserSettings());
        } catch (SettingsException e) {
            e.printStackTrace();
        }

        FormModel lookAndFeelFormModel = FormModelHelper.createCompoundFormModel(generalSettings,
                "generalSettingsFormModel");
        PanjeaAbstractForm lookAndFeelForm = new PanjeaAbstractForm(lookAndFeelFormModel, "generalSettingsForm") {

            @Override
            protected JComponent createFormControl() {
                final SwingBindingFactory bf = (SwingBindingFactory) getBindingFactory();
                TableFormBuilder builder = new TableFormBuilder(bf);
                builder.setLabelAttributes("colGrId=label colSpec=right:pref");
                builder.add("multipleInstance");
                builder.row();
                return builder.getForm();
            }
        };
        return lookAndFeelForm;
    }

    /**
    *
    */
    @Override
    public void dispose() {
    }

    /**
     * @return AbstractCommand.
     */
    @Override
    public AbstractCommand getEditorDeleteCommand() {
        return null;
    }

    /**
     * @return AbstractCommand.
     */
    @Override
    public AbstractCommand getEditorLockCommand() {
        return null;
    }

    /**
     * @return AbstractCommand.
     */
    @Override
    public AbstractCommand getEditorNewCommand() {
        return null;
    }

    /**
     * @return AbstractCommand non utilizato.
     */
    @Override
    public AbstractCommand getEditorSaveCommand() {
        return null;
    }

    /**
     * @return AbstractCommand.
     */
    @Override
    public AbstractCommand getEditorUndoCommand() {
        return null;
    }

    /**
     *
     * @return SettingManager
     */
    private SettingsManager getSettingsManagerLocal() {
        if (settingsManager == null) {
            PanjeaPreferencesManager panjeaPreferencesManager = (PanjeaPreferencesManager) getApplication()
                    .getApplicationContext().getBean("preferenceManager", PanjeaPreferencesManager.class);
            settingsManager = panjeaPreferencesManager.getSettingsManagerLocal();
        }
        return settingsManager;
    }

    /**
    *
    */
    @Override
    public void loadData() {
    }

    @Override
    protected void onDefaults() {
        GeneralSettingsPM generalSettings = null;
        try {
            generalSettings = new GeneralSettingsPM(getSettingsManagerLocal().getUserSettings());
        } catch (SettingsException e) {
            e.printStackTrace();
        }
        getForm().setFormObject(generalSettings);
    }

    @Override
    public boolean onFinish() {
        if (getForm() == null) {
            // il form potrebbe essere nullo dato il caricamento lazy delle
            // pagine
            return true;
        }
        getForm().commit();
        GeneralSettingsPM generalSettings = (GeneralSettingsPM) getForm().getFormObject();
        try {
            getSettingsManagerLocal().getUserSettings().setBoolean(GeneralSettingsPM.MULTIPE_INSTANCE_SETTINGS_KEY,
                    generalSettings.isMultipleInstance());
            getSettingsManagerLocal().getUserSettings().save();
        } catch (Exception e) {
            logger.debug("--> Errore nel salvataggio dei settings", e);
            return false;
        }
        return true;
    }

    @Override
    public void onPostPageOpen() {
    }

    @Override
    public boolean onPrePageOpen() {
        return true;
    }

    @Override
    public void postSetFormObject(Object object) {
    }

    @Override
    public void preSetFormObject(Object object) {
    }

    @Override
    public void refreshData() {
    }

    @Override
    public void restoreState(Settings settings) {
    }

    @Override
    public void saveState(Settings settings) {
    }

    @Override
    public void setFormObject(Object object) {
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
