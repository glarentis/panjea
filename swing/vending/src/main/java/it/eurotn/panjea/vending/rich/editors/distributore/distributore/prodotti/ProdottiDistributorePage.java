package it.eurotn.panjea.vending.rich.editors.distributore.distributore.prodotti;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.springframework.richclient.dialog.AbstractDialogPage;
import org.springframework.richclient.settings.Settings;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.manutenzioni.domain.ProdottoCollegato;
import it.eurotn.panjea.manutenzioni.domain.TipoProdottoCollegato;
import it.eurotn.panjea.vending.domain.Distributore;
import it.eurotn.panjea.vending.domain.ProdottoDistributore;
import it.eurotn.panjea.vending.rich.bd.IVendingAnagraficaBD;
import it.eurotn.panjea.vending.rich.editors.distributore.DistributorePM;
import it.eurotn.rich.command.JideToggleCommand;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;

public class ProdottiDistributorePage extends AbstractDialogPage implements IPageLifecycleAdvisor {

    private class ListaDefinitivaCommand extends JideToggleCommand {

        /**
         * Costruttore.
         */
        public ListaDefinitivaCommand() {
            super("listaDefinitivaCommand");
            RcpSupport.configure(this);
        }

        @Override
        protected void onDeselection() {
            List<ProdottoCollegato> prodotti = new ArrayList<>();
            if (distributore != null && !distributore.isNew()) {
                prodotti = vendingAnagraficaBD.caricaProdottiCollegatiByDistributore(distributore.getId());
            }
            prodottiComponent.getTableWidget().setRows(prodotti);
        }

        @Override
        protected void onSelection() {
            if (distributore != null && !distributore.isNew()) {
                List<ProdottoCollegato> prodotti = vendingAnagraficaBD
                        .caricaProdottiCollegatiByDistributore(distributore.getId());

                // rimuovo tutti i prodotti rimossi dal distributore
                prodotti.removeAll(prodottiDistributoreRimossiComponent.getTableWidget().getRows());

                // rimuovo tutti gli articoli uguali a quelli rimossi dal distributore
                List<Integer> articoliRimossi = new ArrayList<>();
                for (ProdottoCollegato prodottoRimosso : prodottiDistributoreRimossiComponent.getTableWidget()
                        .getRows()) {
                    articoliRimossi.add(prodottoRimosso.getArticolo().getId());
                }
                for (Iterator<ProdottoCollegato> iterator = prodotti.iterator(); iterator.hasNext();) {
                    ProdottoCollegato prodotto = iterator.next();
                    if (articoliRimossi.contains(prodotto.getArticolo().getId())) {
                        iterator.remove();
                    }
                }
                prodottiComponent.getTableWidget().setRows(prodotti);
            }
        }

    }

    private class ProdottiTableListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if (listaDefinitivaCommand.isSelected()) {
                listaDefinitivaCommand.onSelection();
            } else {
                listaDefinitivaCommand.onDeselection();
            }
        }

    }

    private Distributore distributore;

    private IVendingAnagraficaBD vendingAnagraficaBD;

    private ProdottiComponent prodottiComponent = new ProdottiComponent();
    private ProdottiDistributoreAggiuntiComponent prodottiDistributoreAggiuntiComponent = new ProdottiDistributoreAggiuntiComponent();
    private ProdottiDistributoreRimossiComponent prodottiDistributoreRimossiComponent = new ProdottiDistributoreRimossiComponent();

    private ProdottiTableListener prodottiTableListener = new ProdottiTableListener();

    private ListaDefinitivaCommand listaDefinitivaCommand;

    protected ProdottiDistributorePage() {
        super("prodottiDistributorePage");
    }

    @Override
    protected JComponent createControl() {
        JPanel leftPanel = getComponentFactory().createPanel(new BorderLayout());
        leftPanel.add(prodottiComponent, BorderLayout.CENTER);
        listaDefinitivaCommand = new ListaDefinitivaCommand();
        leftPanel.add(listaDefinitivaCommand.createButton(), BorderLayout.SOUTH);

        JPanel rightPanel = getComponentFactory().createPanel(new GridLayout(2, 1));
        rightPanel.add(prodottiDistributoreAggiuntiComponent);
        rightPanel.add(prodottiDistributoreRimossiComponent);

        JPanel rootPanel = getComponentFactory().createPanel(new BorderLayout(10, 0));
        rootPanel.add(leftPanel, BorderLayout.WEST);
        rootPanel.add(rightPanel, BorderLayout.CENTER);

        prodottiDistributoreAggiuntiComponent.addProdottiListener(prodottiTableListener);
        prodottiDistributoreRimossiComponent.addProdottiListener(prodottiTableListener);

        return rootPanel;
    }

    @Override
    public void dispose() {
        prodottiComponent.dispose();
        prodottiDistributoreAggiuntiComponent.dispose();
        prodottiDistributoreRimossiComponent.dispose();
    }

    @Override
    public void loadData() {
        List<ProdottoCollegato> prodottiList = new ArrayList<>();
        List<ProdottoCollegato> prodottiDistributoreAggiuntiList = new ArrayList<>();
        List<ProdottoCollegato> prodottiDistributoreRimossiList = new ArrayList<>();

        List<ProdottoCollegato> prodotti = new ArrayList<>();
        if (distributore != null && !distributore.isNew()) {
            prodotti = vendingAnagraficaBD.caricaProdottiCollegatiByDistributore(distributore.getId());
        }

        for (ProdottoCollegato prodotto : prodotti) {
            prodottiList.add(prodotto);
            if (prodotto instanceof ProdottoDistributore) {
                if (prodotto.getTipo() == TipoProdottoCollegato.AGGIUNTO) {
                    prodottiDistributoreAggiuntiList.add(prodotto);
                } else {
                    prodottiDistributoreRimossiList.add(prodotto);
                }
            }
        }

        prodottiComponent.getTableWidget().setRows(prodottiList);
        prodottiDistributoreAggiuntiComponent.getTableWidget().setRows(prodottiDistributoreAggiuntiList);
        prodottiDistributoreRimossiComponent.getTableWidget().setRows(prodottiDistributoreRimossiList);
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
    public void postSetFormObject(Object object) {
        // Non utilizzato
    }

    @Override
    public void preSetFormObject(Object object) {
        // Non utilizzato
    }

    @Override
    public void refreshData() {
        loadData();
    }

    @Override
    public void restoreState(Settings arg0) {
        // Non utilizzato
    }

    @Override
    public void saveState(Settings arg0) {
        // Non utilizzato
    }

    @Override
    public void setFormObject(Object object) {
        if (object instanceof DistributorePM) {
            DistributorePM distributorePM = (DistributorePM) object;

            this.distributore = distributorePM.getDistributore();
        } else {
            this.distributore = (Distributore) object;
        }

        prodottiDistributoreAggiuntiComponent.setDistributore(distributore);
        prodottiDistributoreRimossiComponent.setDistributore(distributore);
        listaDefinitivaCommand.setEnabled(distributore != null && !distributore.isNew());
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        // Non utilizzato
    }

    /**
     * @param vendingAnagraficaBD
     *            the vendingAnagraficaBD to set
     */
    public final void setVendingAnagraficaBD(IVendingAnagraficaBD vendingAnagraficaBD) {
        this.vendingAnagraficaBD = vendingAnagraficaBD;
    }

}
