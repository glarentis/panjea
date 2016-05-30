package it.eurotn.panjea.manutenzioni.rich.editors.causaliinstallazione;

import java.util.Collection;

import it.eurotn.panjea.manutenzioni.domain.CausaleInstallazione;
import it.eurotn.panjea.manutenzioni.rich.bd.IManutenzioniBD;
import it.eurotn.rich.editors.AbstractTablePageEditor;

public class CausaliInstallazioneTablePage extends AbstractTablePageEditor<CausaleInstallazione> {

    public static final String PAGE_ID = "causaliInstallazioneTablePage";

    private IManutenzioniBD manutenzioniBD = null;

    /**
     * Costruttore di default.
     */
    public CausaliInstallazioneTablePage() {
        super(PAGE_ID, new String[] { "codice", "descrizione", "tipoInstallazione" }, CausaleInstallazione.class);
    }

    @Override
    public Collection<CausaleInstallazione> loadTableData() {
        return manutenzioniBD.caricaCausaliInstallazione();
    }

    @Override
    public void onPostPageOpen() {
        // Non utilizzato
    }

    @Override
    public boolean onPrePageOpen() {
        return true;
    }

    @Override
    public Collection<CausaleInstallazione> refreshTableData() {
        return loadTableData();
    }

    @Override
    public void setFormObject(Object object) {
        // Non utilizzato
    }

    /**
     * @param manutenzioniBD
     *            the manutenzioniBD to set
     */
    public void setManutenzioniBD(IManutenzioniBD manutenzioniBD) {
        this.manutenzioniBD = manutenzioniBD;
    }

}
