package it.eurotn.panjea.manutenzioni.rich.editors.areeinstallazioni.ricerca;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.ActionCommand;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

import it.eurotn.panjea.manutenzioni.domain.documento.AreaInstallazione;
import it.eurotn.panjea.manutenzioni.manager.areeinstallazioni.ParametriRicercaAreeInstallazione;
import it.eurotn.panjea.manutenzioni.rich.bd.IManutenzioniBD;
import it.eurotn.rich.editors.AbstractTablePageEditor;

public class RisultatiRicercaAreaInstallazioneTablePage extends AbstractTablePageEditor<AreaInstallazione> {

    private class OpenAreaCommand extends ActionCommand {

        @Override
        protected void doExecuteCommand() {
            AreaInstallazione area = RisultatiRicercaAreaInstallazioneTablePage.this.getTable().getSelectedObject();
            LifecycleApplicationEvent event = new OpenEditorEvent(
                    manutenzioniBD.caricaAreaInstallazioneById(area.getId()));
            Application.instance().getApplicationContext().publishEvent(event);
        }
    }

    private ParametriRicercaAreeInstallazione parametri;

    private IManutenzioniBD manutenzioniBD;

    /**
     * Costruttore
     */
    public RisultatiRicercaAreaInstallazioneTablePage() {
        super("risultatiRicercaAreaInstallazioneTablePage", new String[] { "documento.tipoDocumento",
                "documento.codice", "documento.dataDocumento", "documento.entitaDocumento", "documento.sedeEntita" },
                AreaInstallazione.class);
        getTable().setPropertyCommandExecutor(new OpenAreaCommand());
    }

    @Override
    public Collection<AreaInstallazione> loadTableData() {
        List<AreaInstallazione> result = Collections.emptyList();
        if (parametri.isEffettuaRicerca()) {
            result = manutenzioniBD.ricercaAreeInstallazioni(parametri);
        }
        return result;
    }

    @Override
    public void onPostPageOpen() {
        // nu
    }

    @Override
    public Collection<AreaInstallazione> refreshTableData() {
        return loadTableData();
    }

    @Override
    public void setFormObject(Object object) {
        parametri = (ParametriRicercaAreeInstallazione) object;
    }

    /**
     * @param manutenzioniBD
     *            The manutenzioniBD to set.
     */
    public void setManutenzioniBD(IManutenzioniBD manutenzioniBD) {
        this.manutenzioniBD = manutenzioniBD;
    }
}
