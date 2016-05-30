package it.eurotn.panjea.vending.rich.editors.rilevazionievadts.ricerca;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

import it.eurotn.panjea.vending.domain.evadts.RilevazioneEvaDts;
import it.eurotn.panjea.vending.manager.evadts.rilevazioni.ParametriRicercaRilevazioniEvaDts;
import it.eurotn.panjea.vending.rich.bd.IVendingDocumentoBD;
import it.eurotn.rich.editors.AbstractTablePageEditor;

public class RisultatiRicercaRilevazioneEvaDtsTablePage extends AbstractTablePageEditor<RilevazioneEvaDts> {

    private class OpenRilevazioneEvaDtsEditorCommand extends ActionCommand {

        /**
         * Costruttore.
         */
        public OpenRilevazioneEvaDtsEditorCommand() {
            super("openRilevazioneEvaDtsEditorCommand");
            RcpSupport.configure(this);
        }

        @Override
        protected void doExecuteCommand() {

            RilevazioneEvaDts rilevazioneEvaDts = getTable().getSelectedObject();
            if (rilevazioneEvaDts != null) {
                rilevazioneEvaDts = vendingDocumentoBD.caricaRilevazioneEvaDtsById(rilevazioneEvaDts.getId());
                LifecycleApplicationEvent event = new OpenEditorEvent(rilevazioneEvaDts);
                Application.instance().getApplicationContext().publishEvent(event);
            }
        }

    }

    private ParametriRicercaRilevazioniEvaDts parametriRicerca = null;

    private IVendingDocumentoBD vendingDocumentoBD;

    /**
     * Costruttore.
     *
     */
    public RisultatiRicercaRilevazioneEvaDtsTablePage() {
        super("risultatiRicercaRilevazioneEvaDtsTablePage",
                new String[] { "areaRifornimento.documento.dataDocumento", "areaRifornimento.documento",
                        "areaRifornimento.documento.entitaDocumento", "areaRifornimento.documento.sedeEntita",
                        "areaRifornimento.operatore", "areaRifornimento.distributore",
                        "areaRifornimento.installazione" },
                RilevazioneEvaDts.class);
        getTable().setPropertyCommandExecutor(new OpenRilevazioneEvaDtsEditorCommand());
    }

    @Override
    public Collection<RilevazioneEvaDts> loadTableData() {
        List<RilevazioneEvaDts> rilevazioni = new ArrayList<>();

        if (parametriRicerca != null && parametriRicerca.isEffettuaRicerca()) {
            rilevazioni = vendingDocumentoBD.ricercaRilevazioniEvaDts(parametriRicerca);
        }

        return rilevazioni;
    }

    @Override
    public void onPostPageOpen() {
        // nothing to do
    }

    @Override
    public Collection<RilevazioneEvaDts> refreshTableData() {
        return loadTableData();
    }

    @Override
    public void setFormObject(Object object) {
        if (object instanceof ParametriRicercaRilevazioniEvaDts) {
            this.parametriRicerca = (ParametriRicercaRilevazioniEvaDts) object;
        } else {
            this.parametriRicerca = new ParametriRicercaRilevazioniEvaDts();
        }
    }

    /**
     * @param vendingDocumentoBD
     *            the vendingDocumentoBD to set
     */
    public void setVendingDocumentoBD(IVendingDocumentoBD vendingDocumentoBD) {
        this.vendingDocumentoBD = vendingDocumentoBD;
    }

}
