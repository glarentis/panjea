package it.eurotn.panjea.giroclienti.rich.editors.entita;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.dialog.MessageDialog;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.grid.TableModelWrapperUtils;

import it.eurotn.panjea.anagrafica.domain.Entita;
import it.eurotn.panjea.giroclienti.domain.GiroSedeCliente;
import it.eurotn.panjea.giroclienti.rich.bd.IGiroClientiAnagraficaBD;
import it.eurotn.rich.editors.AbstractTablePageEditor;

public class GiroClientiEntitaTablePage extends AbstractTablePageEditor<GiroSedeCliente> {

    private class SalvaGiriCommand extends ActionCommand {

        /**
         * Costruttore.
         */
        public SalvaGiriCommand() {
            super("saveCommand");
            RcpSupport.configure(this);
        }

        @Override
        protected void doExecuteCommand() {

            for (GiroSedeCliente giroSedeCliente : getTable().getRows()) {
                if (!((GiroClientiEntitaTableModel) TableModelWrapperUtils
                        .getActualTableModel(getTable().getTable().getModel(), GiroClientiEntitaTableModel.class))
                                .isObjectValid(giroSedeCliente)) {
                    new MessageDialog("ATTENZIONE",
                            "Non tutti i giri presenti sono validi. Correggere i giri non corretti per procedere con il salvataggio")
                                    .showDialog();
                    return;
                }
            }

            giroClientiAnagraficaBD.cancellaGiroSedeCliente(entita.getId());
            giroClientiAnagraficaBD.salvaGiroSedeCliente(getTable().getRows());
        }

    }

    private IGiroClientiAnagraficaBD giroClientiAnagraficaBD;

    private Entita entita;

    private SalvaGiriCommand salvaGiriCommand;

    /**
     * Costruttore.
     */
    public GiroClientiEntitaTablePage() {
        super("giroClientiEntitaTablePage", new GiroClientiEntitaTableModel());
    }

    @Override
    public AbstractCommand[] getCommands() {
        return new AbstractCommand[] { getSalvaGiriCommand(), getRefreshCommand() };
    }

    /**
     * @return the salvaGiriCommand
     */
    private SalvaGiriCommand getSalvaGiriCommand() {
        if (salvaGiriCommand == null) {
            salvaGiriCommand = new SalvaGiriCommand();
        }

        return salvaGiriCommand;
    }

    @Override
    public Collection<GiroSedeCliente> loadTableData() {

        List<GiroSedeCliente> giroSediEclienti = new ArrayList<GiroSedeCliente>();

        if (entita != null && entita.getId() != null) {
            giroSediEclienti = giroClientiAnagraficaBD.caricaGiroSedeCliente(entita.getId());
        }

        return giroSediEclienti;
    }

    @Override
    public void onPostPageOpen() {
        // non gestito
    }

    @Override
    public boolean onPrePageOpen() {
        boolean initializePage = true;
        if (entita.isNew()) {
            initializePage = false;
            MessageSourceAccessor messageSourceAccessor = (MessageSourceAccessor) ApplicationServicesLocator.services()
                    .getService(MessageSourceAccessor.class);
            String titolo = messageSourceAccessor.getMessage("entita.null.messageDialog.title", new Object[] {},
                    Locale.getDefault());
            String messaggio = messageSourceAccessor.getMessage(
                    "entita.null.messageDialog.message", new Object[] { messageSourceAccessor
                            .getMessage(entita.getDomainClassName(), new Object[] {}, Locale.getDefault()) },
                    Locale.getDefault());
            new MessageDialog(titolo, messaggio).showDialog();
        }
        return initializePage;
    }

    @Override
    public Collection<GiroSedeCliente> refreshTableData() {
        return loadTableData();
    }

    @Override
    public void setFormObject(Object object) {
        this.entita = (Entita) object;
        ((GiroClientiEntitaTableModel) TableModelWrapperUtils.getActualTableModel(getTable().getTable().getModel(),
                GiroClientiEntitaTableModel.class)).setEntita(entita);
    }

    /**
     * @param giroClientiAnagraficaBD
     *            the giroClientiAnagraficaBD to set
     */
    public void setGiroClientiAnagraficaBD(IGiroClientiAnagraficaBD giroClientiAnagraficaBD) {
        this.giroClientiAnagraficaBD = giroClientiAnagraficaBD;
    }

}
