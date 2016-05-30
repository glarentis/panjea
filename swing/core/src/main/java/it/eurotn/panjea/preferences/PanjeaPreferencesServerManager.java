/**
 * 
 */
package it.eurotn.panjea.preferences;

import it.eurotn.rich.preferences.ButtonPreferenceDialog;

import java.awt.Dimension;
import java.util.*;

import org.springframework.context.MessageSource;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.core.*;
import org.springframework.richclient.dialog.*;
import org.springframework.richclient.preference.PreferencePage;
import org.springframework.richclient.settings.*;

/**
 * @author Leonardo
 * 
 */
public class PanjeaPreferencesServerManager {

    private ButtonPreferenceDialog dialog;
    private List preferencePages = new ArrayList();
    private SettingsManager settingsManager;

    public void showDialog() {
        if (dialog == null) {
            dialog = new ButtonPreferenceDialog();

            for (Iterator iter = preferencePages.iterator(); iter.hasNext();) {
                PreferencePage page = (PreferencePage) iter.next();
                dialog.addPreferencePage(page);
            }

            MessageSource messageSource = (MessageSource) ApplicationServicesLocator.services()
                    .getService(MessageSource.class);
            dialog.setTitle(messageSource.getMessage("preferenceServerDialog.title", null, Locale.getDefault()));
            String messaggio = messageSource.getMessage("preferenceServerDialog.message", null, Locale.getDefault());
            dialog.setMessage(new DefaultMessage(messaggio, Severity.INFO));

            dialog.setCloseAction(CloseAction.DISPOSE);
            dialog.setPreferredSize(new Dimension(600, 280));

            try {
                dialog.setSettings(settingsManager.getInternalSettings());
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

    public void setPreferencePages(List pages) {
        preferencePages = pages;
    }

    public SettingsManager getSettingsManager() {
        return settingsManager;
    }

    public void setSettingsManager(SettingsManager settingsManager) {
        this.settingsManager = settingsManager;
    }

}
