package it.eurotn.panjea.preferences;

import it.eurotn.rich.preferences.ButtonPreferenceDialog;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.richclient.core.DefaultMessage;
import org.springframework.richclient.core.Severity;
import org.springframework.richclient.dialog.MessageDialog;
import org.springframework.richclient.preference.PreferencePage;
import org.springframework.richclient.settings.SettingsException;
import org.springframework.richclient.settings.SettingsManager;

public class PanjeaPreferencesManager {

    private ButtonPreferenceDialog dialog;

    private List preferencePages = new ArrayList();

    private SettingsManager settingsManagerLocal;

    private SettingsManager settingsManager;

    public SettingsManager getSettingsManager() {
        return settingsManager;
    }

    public SettingsManager getSettingsManagerLocal() {
        return settingsManagerLocal;
    }

    public void setPreferencePages(List pages) {
        preferencePages = pages;
    }

    public void setSettingsManager(SettingsManager settingsManager) {
        this.settingsManager = settingsManager;
    }

    public void setSettingsManagerLocal(SettingsManager settingsManagerLocal) {
        this.settingsManagerLocal = settingsManagerLocal;
    }

    public void showDialog() {
        if (dialog == null) {
            dialog = new ButtonPreferenceDialog();

            for (Iterator iter = preferencePages.iterator(); iter.hasNext();) {
                PreferencePage page = (PreferencePage) iter.next();
                // if (page.getParent() == null) {
                dialog.addPreferencePage(page);
                // } else {
                // dialog.addPreferencePage(page.getParent(), page);
                // }
            }

            dialog.setTitle("Preferences");
            try {
                dialog.setSettingsLocal(settingsManagerLocal.getUserSettings());
                dialog.setSettings(settingsManager.getUserSettings());
            } catch (SettingsException e) {
                new MessageDialog("Error", new DefaultMessage(e.getMessage(), Severity.ERROR)).showDialog();
                e.printStackTrace();
            }
        }

        // dialog creation can fail
        if (dialog != null) {
            dialog.showDialog();
        }
    }

}
