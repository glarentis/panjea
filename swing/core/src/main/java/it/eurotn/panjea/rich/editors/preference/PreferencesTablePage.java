package it.eurotn.panjea.rich.editors.preference;

import it.eurotn.panjea.anagrafica.domain.Preference;
import it.eurotn.panjea.rich.bd.IPreferenceBD;
import it.eurotn.rich.editors.AbstractTablePageEditor;

import java.util.List;

public class PreferencesTablePage extends AbstractTablePageEditor<Preference> {

    public static final String PAGE_ID = "preferencesTablePage";

    private IPreferenceBD preferenceBD;

    /**
     * 
     * Costruttore.
     * 
     */
    protected PreferencesTablePage() {
        super(PAGE_ID, new String[] { "chiave", "valore", "nomeUtente" }, Preference.class);
    }

    @Override
    public List<Preference> loadTableData() {
        return preferenceBD.caricaPreferences();
    }

    @Override
    public void onPostPageOpen() {
    }

    @Override
    public boolean onPrePageOpen() {
        return true;
    }

    @Override
    public List<Preference> refreshTableData() {
        return loadTableData();
    }

    @Override
    public void setFormObject(Object object) {
        // TODO Auto-generated method stub

    }

    /**
     * @param preferenceBD
     *            the preferenceBD to set
     */
    public void setPreferenceBD(IPreferenceBD preferenceBD) {
        this.preferenceBD = preferenceBD;
    }

}
