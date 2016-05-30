package it.eurotn.panjea.manutenzioni.rich.editors.installazioni;

import java.util.Collection;

import it.eurotn.panjea.anagrafica.domain.Entita;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.manutenzioni.domain.Installazione;
import it.eurotn.panjea.manutenzioni.rich.bd.IManutenzioniBD;
import it.eurotn.rich.editors.AbstractTablePageEditor;
import it.eurotn.rich.editors.PagesTabbedDialogPageEditor;
import it.eurotn.rich.editors.table.EditFrame;

public class InstallazioniTablePage extends AbstractTablePageEditor<Installazione> {

    public static final String PAGE_ID = "installazioniTablePage";

    private IManutenzioniBD manutenzioniBD = null;

    private Entita entita;

    private SedeEntita sedeEntita;

    /**
     * Costruttore di default.
     */
    public InstallazioniTablePage() {
        super(PAGE_ID, new String[] { "codice", "descrizione", "articolo", "deposito.sedeEntita", "ubicazione",
                "listino", "datiInstallazione.caricatore", "datiInstallazione.tecnico" }, Installazione.class);
    }

    @Override
    public Collection<Installazione> loadTableData() {
        if (sedeEntita != null) {
            return manutenzioniBD.ricercaInstallazioniBySede(sedeEntita.getId());
        } else {
            return manutenzioniBD.ricercaInstallazioniByEntita(entita.getId());
        }
    }

    @Override
    public void onNew() {
        super.onNew();
        // PagesTabbedDialogPageEditor tabbedPages = (PagesTabbedDialogPageEditor) getEditPages()
        // .get(EditFrame.DEFAULT_OBJECT_CLASS_NAME);
        // InstallazioneForm form = (InstallazioneForm) ((InstallazionePage) tabbedPages.getDialogPages().get(0))
        // .getForm();
        // form.setEntita(entita);
        // form.setSedeEntita(sedeEntita);
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
    public Collection<Installazione> refreshTableData() {
        return loadTableData();
    }

    @Override
    public void setFormObject(Object object) {

        if (object instanceof SedeEntita) {
            sedeEntita = (SedeEntita) object;
            entita = sedeEntita.getEntita();
        } else {
            sedeEntita = null;
            entita = (Entita) object;
        }
        PagesTabbedDialogPageEditor tabbedPages = (PagesTabbedDialogPageEditor) getEditPages()
                .get(EditFrame.DEFAULT_OBJECT_CLASS_NAME);
        InstallazioneForm form = (InstallazioneForm) ((InstallazionePage) tabbedPages.getDialogPages().get(0))
                .getForm();
        form.setEntita(entita);
        form.setSedeEntita(sedeEntita);
    }

    /**
     * @param manutenzioniBD
     *            the iManutenzioniBD to set
     */
    public void setManutenzioniBD(IManutenzioniBD manutenzioniBD) {
        this.manutenzioniBD = manutenzioniBD;
    }

}
