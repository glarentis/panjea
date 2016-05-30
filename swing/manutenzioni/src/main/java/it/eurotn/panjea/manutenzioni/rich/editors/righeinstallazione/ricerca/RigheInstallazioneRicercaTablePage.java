package it.eurotn.panjea.manutenzioni.rich.editors.righeinstallazione.ricerca;

import java.util.Collection;
import java.util.Collections;

import it.eurotn.locking.IDefProperty;
import it.eurotn.panjea.manutenzioni.domain.Installazione;
import it.eurotn.panjea.manutenzioni.domain.documento.RigaInstallazione;
import it.eurotn.panjea.manutenzioni.rich.bd.IManutenzioniBD;
import it.eurotn.rich.editors.AbstractTablePageEditor;

public class RigheInstallazioneRicercaTablePage extends AbstractTablePageEditor<RigaInstallazione> {

    private Installazione installazione;
    private Integer idArticolo;
    private IManutenzioniBD manutenzioniBD;

    /**
     * Costruttore
     * 
     * @param idTableWidget
     *            id della tablewisget
     * @param colonne
     *            colonne da visualizzare
     *
     */
    public RigheInstallazioneRicercaTablePage(final String idTableWidget, final String[] colonne) {
        super(idTableWidget, colonne, RigaInstallazione.class);
    }

    /**
     * @return Returns the manutenzioniBD.
     */
    public IManutenzioniBD getManutenzioniBD() {
        return manutenzioniBD;
    }

    @Override
    public Collection<RigaInstallazione> loadTableData() {
        if (installazione != null) {
            return manutenzioniBD.caricaRigheInstallazioneByInstallazione(installazione.getId());
        }
        if (idArticolo != null) {
            return manutenzioniBD.caricaRigheInstallazioneByArticolo(idArticolo);
        }
        return Collections.emptyList();
    }

    @Override
    public void onPostPageOpen() {

    }

    @Override
    public Collection<RigaInstallazione> refreshTableData() {
        return null;
    }

    @Override
    public void setFormObject(Object object) {
        if (object instanceof Installazione) {

            installazione = (Installazione) object;
        } else {
            idArticolo = ((IDefProperty) object).getId();
        }
    }

    /**
     * @param manutenzioniBD
     *            The manutenzioniBD to set.
     */
    public void setManutenzioniBD(IManutenzioniBD manutenzioniBD) {
        this.manutenzioniBD = manutenzioniBD;
    }

}
