package it.eurotn.panjea.manutenzioni.rich.editors.ubicazioniinstallazione;

import java.util.Collection;

import it.eurotn.panjea.manutenzioni.domain.UbicazioneInstallazione;
import it.eurotn.panjea.manutenzioni.rich.bd.IManutenzioniBD;
import it.eurotn.rich.editors.AbstractTablePageEditor;

public class UbicazioniInstallazioneTablePage extends AbstractTablePageEditor<UbicazioneInstallazione> {

    public static final String PAGE_ID = "ubicazioniInstallazioneTablePage";

    private IManutenzioniBD manutenzioniBD = null;

    /**
     * Costruttore di default.
     */
    public UbicazioniInstallazioneTablePage() {
        super(PAGE_ID, new String[] { "descrizione" }, UbicazioneInstallazione.class);
    }

    @Override
    public Collection<UbicazioneInstallazione> loadTableData() {
        return manutenzioniBD.caricaUbicazioniInstallazione();
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
    public Collection<UbicazioneInstallazione> refreshTableData() {
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
