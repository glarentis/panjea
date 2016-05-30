package it.eurotn.rich.preferences;

import it.eurotn.rich.dialog.ButtonCompositeDialogPage;
import it.eurotn.rich.editors.PanjeaTitledPageApplicationDialog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.richclient.dialog.CompositeDialogPage;
import org.springframework.richclient.preference.PreferencePage;
import org.springframework.richclient.settings.Settings;
import org.springframework.util.Assert;

public class ButtonPreferenceDialog extends PanjeaTitledPageApplicationDialog {

    private List<PreferencePage> preferencePages = new ArrayList<PreferencePage>();

    private Settings settings = null;
    private Settings settingsLocal = null;

    public ButtonPreferenceDialog() {
        super(new ButtonCompositeDialogPage("preferenceDialog"));
    }

    private void addPage(PreferencePage page) {
        Assert.isTrue(!isControlCreated(), "Add pages before control is created.");
        preferencePages.add(page);
        // page.setPreferenceDialog(this);
    }

    public void addPreferencePage(PreferencePage page) {
        addPage(page);
        getPageContainer().addPage(page);
    }

    // public void addPreferencePage(PreferencePage parent, PreferencePage page)
    // {
    // addPage(page);
    // getPageContainer().addPage(parent, page);
    // }

    private CompositeDialogPage getPageContainer() {
        return (CompositeDialogPage) getDialogPage();
    }

    public Settings getSettings() {
        return settings;
    }

    public Settings getSettingsLocal() {
        return settingsLocal;
    }

    @Override
    public boolean onFinish() {
        for (PreferencePage page : preferencePages) {
        }

        if (settingsLocal != null) {
            try {
                settingsLocal.save();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        if (settings != null) {
            try {
                settings.save();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        return true;
    }

    public void setSettings(Settings settings) {
        Assert.notNull(settings, "Settings cannot be null.");
        this.settings = settings;
    }

    public void setSettingsLocal(Settings settingsLocal) {
        Assert.notNull(settingsLocal, "SettingsLocal cannot be null.");
        this.settingsLocal = settingsLocal;
    }

}
