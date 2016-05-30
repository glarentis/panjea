package it.eurotn.panjea.vending.rich.editors.rifornimento.ricerca;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

import it.eurotn.panjea.vending.domain.documento.AreaRifornimento;
import it.eurotn.panjea.vending.manager.arearifornimento.ParametriRicercaAreeRifornimento;
import it.eurotn.panjea.vending.rich.bd.IVendingDocumentoBD;
import it.eurotn.rich.editors.AbstractTablePageEditor;

public class RisultatiRicercaAreaRifornimentoTablePage extends AbstractTablePageEditor<AreaRifornimento> {

    private class OpenAreaRifornimentoEditorCommand extends ActionCommand {

        /**
         * Costruttore.
         */
        public OpenAreaRifornimentoEditorCommand() {
            super("openAreaRifornimentoEditorCommand");
            RcpSupport.configure(this);
        }

        @Override
        protected void doExecuteCommand() {

            AreaRifornimento areaRifornimento = getTable().getSelectedObject();
            if (areaRifornimento != null) {
                areaRifornimento = vendingDocumentoBD.caricaAreaRifornimentoById(areaRifornimento.getId());

                Object object = null;
                if (areaRifornimento.getAreaMagazzino() != null) {
                    object = areaRifornimento.getAreaMagazzino();
                } else if (areaRifornimento.getAreaOrdine() != null) {
                    object = areaRifornimento.getAreaOrdine();
                }

                if (object != null) {
                    LifecycleApplicationEvent event = new OpenEditorEvent(object);
                    Application.instance().getApplicationContext().publishEvent(event);
                }
            }
        }

    }

    private ParametriRicercaAreeRifornimento parametriRicerca = null;

    private IVendingDocumentoBD vendingDocumentoBD;

    /**
     * Costruttore.
     */
    public RisultatiRicercaAreaRifornimentoTablePage() {
        super("risultatiRicercaAreaRifornimentoTablePage", new String[] { "documento.dataDocumento", "documento",
                "documento.entitaDocumento", "documento.sedeEntita", "operatore", "distributore", "installazione" },
                AreaRifornimento.class);
        getTable().setPropertyCommandExecutor(new OpenAreaRifornimentoEditorCommand());
    }

    @Override
    public Collection<AreaRifornimento> loadTableData() {
        List<AreaRifornimento> areerifornimento = new ArrayList<>();

        if (parametriRicerca != null && parametriRicerca.isEffettuaRicerca()) {
            areerifornimento = vendingDocumentoBD.ricercaAreeRifornimento(parametriRicerca);
        }

        return areerifornimento;
    }

    @Override
    public void onPostPageOpen() {
        // nothing to do
    }

    @Override
    public Collection<AreaRifornimento> refreshTableData() {
        return loadTableData();
    }

    @Override
    public void setFormObject(Object object) {
        if (object instanceof ParametriRicercaAreeRifornimento) {
            this.parametriRicerca = (ParametriRicercaAreeRifornimento) object;
        } else {
            this.parametriRicerca = new ParametriRicercaAreeRifornimento();
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
