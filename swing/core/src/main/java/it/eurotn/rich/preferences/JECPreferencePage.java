package it.eurotn.rich.preferences;

import org.springframework.richclient.preference.PreferencePage;

public abstract class JECPreferencePage extends PreferencePage {

    public JECPreferencePage(String id) {
        super(id);
    }

    @Override
    public boolean onFinish() {
        return super.onFinish();
    }

}
