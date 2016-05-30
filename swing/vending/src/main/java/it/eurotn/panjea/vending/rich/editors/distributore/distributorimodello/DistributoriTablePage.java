package it.eurotn.panjea.vending.rich.editors.distributore.distributorimodello;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.Observable;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationEvent;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;

import com.jidesoft.utils.SwingWorker;

import it.eurotn.panjea.rich.PanjeaLifecycleApplicationEvent;
import it.eurotn.panjea.vending.domain.Distributore;
import it.eurotn.panjea.vending.domain.Modello;
import it.eurotn.panjea.vending.manager.distributore.ParametriRicercaDistributore;
import it.eurotn.panjea.vending.rich.bd.IVendingAnagraficaBD;
import it.eurotn.panjea.vending.rich.editors.distributore.DistributorePM;
import it.eurotn.panjea.vending.rich.editors.distributore.modelli.ModelliTreeTablePage;
import it.eurotn.rich.editors.AbstractTablePageEditor;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;

public class DistributoriTablePage extends AbstractTablePageEditor<Distributore> {

    private static final Logger LOGGER = Logger.getLogger(DistributoriTablePage.class);

    public static final String PAGE_ID = "distributoriTablePage";

    private IVendingAnagraficaBD vendingAnagraficaBD;

    private DistributorePM distributorePM;

    private boolean isDeleteObject;

    /**
     * Costruttore.
     */
    protected DistributoriTablePage() {
        super(PAGE_ID, new String[] { "codice", "descrizione", "proprietaCliente" }, Distributore.class);
        isDeleteObject = false;
    }

    /**
     * @return Modello corrente
     */
    public Modello getModelloCorrente() {
        Modello modello = null;
        if (distributorePM != null) {
            modello = distributorePM.getModello();
        }
        return modello;
    }

    @Override
    public Collection<Distributore> loadTableData() {
        if (distributorePM == null) {
            return new ArrayList<>();
        }
        if (!Objects.equals(ModelliTreeTablePage.PAGE_ID, distributorePM.getSource())) {
            return getTable().getRows();
        }

        ParametriRicercaDistributore parametri = new ParametriRicercaDistributore();
        if (distributorePM.getTipoModello() != null) {
            parametri.setIdTipoModello(distributorePM.getTipoModello().getId());
        }
        if (distributorePM.getModello() != null) {
            parametri.setIdModello(distributorePM.getModello().getId());
        }

        return vendingAnagraficaBD.ricercaDistributori(parametri);

    }

    @Override
    public void onEditorEvent(ApplicationEvent event) {
        // Se è stato cancellato l'ultimo articolo devo rispedire un messaggio
        // all'editor con un articolo a null
        PanjeaLifecycleApplicationEvent panjeaEvent = (PanjeaLifecycleApplicationEvent) event;
        if (panjeaEvent.getEventType().equals(LifecycleApplicationEvent.DELETED)
                && panjeaEvent.getObject() instanceof Distributore) {
            isDeleteObject = true;
            getTable().removeRowObject((Distributore) panjeaEvent.getObject());
        }
    }

    @Override
    public void onPostPageOpen() {
        // Non utilizzato
    }

    @Override
    public void processTableData(Collection<Distributore> results) {
        if (getTable() == null) {
            return;
        }
        super.processTableData(results);
    }

    @Override
    public void refreshData() {
        if (distributorePM == null) {
            return;
        }
        if (Objects.equals(ModelliTreeTablePage.PAGE_ID, distributorePM.getSource())) {
            update(null, null);
            super.refreshData();
        }
    }

    @Override
    public Collection<Distributore> refreshTableData() {
        return loadTableData();
    }

    @Override
    public void setFormObject(Object object) {
        if (object instanceof DistributorePM) {
            this.distributorePM = (DistributorePM) object;
            if (distributorePM.getDistributore() != null && distributorePM.getSource() == null) {
                getTable().selectRowObject(distributorePM.getDistributore(), null);
            }
        } else if (object instanceof Distributore) {
            if (!((Distributore) object).isNew() && !isDeleteObject) {
                getTable().replaceOrAddRowObject((Distributore) object, (Distributore) object, this);
            }
        }
        isDeleteObject = false;
    }

    /**
     * @param vendingAnagraficaBD
     *            the vendingAnagraficaBD to set
     */
    public void setVendingAnagraficaBD(IVendingAnagraficaBD vendingAnagraficaBD) {
        this.vendingAnagraficaBD = vendingAnagraficaBD;
    }

    @Override
    public void update(Observable obs, Object arg) {
        super.update(obs, arg);

        // Rilancio il distributore selezionato
        if (getTable().getSelectedObject() != null) {

            new SwingWorker<Distributore, Void>() {

                @Override
                protected Distributore doInBackground() throws Exception {
                    if (getTable().getSelectedObject() != null) {
                        return vendingAnagraficaBD.caricaDistributore(getTable().getSelectedObject().getId());
                    }
                    return null;
                }

                @Override
                protected void done() {
                    try {
                        DistributoriTablePage.this.firePropertyChange(IPageLifecycleAdvisor.OBJECT_CHANGED, null,
                                new DistributorePM(null, null, get(), PAGE_ID));
                    } catch (Exception e) {
                        LOGGER.error("--> errore durante il caricamento del distributore.", e);
                    }
                }
            }.execute();
        }
    }

}
