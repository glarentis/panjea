package it.eurotn.panjea.vending.rich.editors.prodottiinstallazione;

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

import it.eurotn.panjea.manutenzioni.domain.Installazione;
import it.eurotn.panjea.manutenzioni.domain.ProdottoCollegato;
import it.eurotn.panjea.manutenzioni.domain.ProdottoInstallazione;
import it.eurotn.panjea.manutenzioni.domain.TipoProdottoCollegato;
import it.eurotn.panjea.vending.rich.bd.IVendingAnagraficaBD;
import it.eurotn.rich.command.JideToggleCommand;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;

public class ProdottiInstallazionePage extends AbstractDialogPage implements IPageLifecycleAdvisor {

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
            if (installazione != null && !installazione.isNew()) {
                prodotti = vendingAnagraficaBD.caricaProdottiCollegatiByInstallazione(installazione.getId());
            }
            prodottiComponent.getTableWidget().setRows(prodotti);
        }

        @Override
        protected void onSelection() {
            if (installazione != null && !installazione.isNew()) {
                List<ProdottoCollegato> prodotti = vendingAnagraficaBD
                        .caricaProdottiCollegatiByInstallazione(installazione.getId());

                // rimuovo tutti i prodotti rimossi dall'installazione
                prodotti.removeAll(prodottiInstallazioneRimossiComponent.getTableWidget().getRows());

                // rimuovo tutti gli articoli uguali a quelli rimossi dall'installazione
                List<Integer> articoliRimossi = new ArrayList<>();
                for (ProdottoCollegato prodottoRimosso : prodottiInstallazioneRimossiComponent.getTableWidget()
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

    private Installazione installazione;

    private IVendingAnagraficaBD vendingAnagraficaBD;

    private ProdottiComponent prodottiComponent = new ProdottiComponent();
    private ProdottiInstallazioneAggiuntiComponent prodottiInstallazioneAggiuntiComponent = new ProdottiInstallazioneAggiuntiComponent();
    private ProdottiInstallazioneRimossiComponent prodottiInstallazioneRimossiComponent = new ProdottiInstallazioneRimossiComponent();

    private ProdottiTableListener prodottiTableListener = new ProdottiTableListener();

    private ListaDefinitivaCommand listaDefinitivaCommand;

    protected ProdottiInstallazionePage() {
        super("prodottiInstallazionePage");
    }

    @Override
    protected JComponent createControl() {
        JPanel leftPanel = getComponentFactory().createPanel(new BorderLayout());
        leftPanel.add(prodottiComponent, BorderLayout.CENTER);
        listaDefinitivaCommand = new ListaDefinitivaCommand();
        leftPanel.add(listaDefinitivaCommand.createButton(), BorderLayout.SOUTH);

        JPanel rightPanel = getComponentFactory().createPanel(new GridLayout(2, 1));
        rightPanel.add(prodottiInstallazioneAggiuntiComponent);
        rightPanel.add(prodottiInstallazioneRimossiComponent);

        JPanel rootPanel = getComponentFactory().createPanel(new BorderLayout(10, 0));
        rootPanel.add(leftPanel, BorderLayout.WEST);
        rootPanel.add(rightPanel, BorderLayout.CENTER);

        prodottiInstallazioneAggiuntiComponent.addProdottiListener(prodottiTableListener);
        prodottiInstallazioneRimossiComponent.addProdottiListener(prodottiTableListener);

        return rootPanel;
    }

    @Override
    public void dispose() {
        prodottiComponent.dispose();
        prodottiInstallazioneAggiuntiComponent.dispose();
        prodottiInstallazioneRimossiComponent.dispose();
    }

    @Override
    public void loadData() {
        List<ProdottoCollegato> prodottiList = new ArrayList<>();
        List<ProdottoCollegato> prodottiInstallazioneAggiuntiList = new ArrayList<>();
        List<ProdottoCollegato> prodottiInstallazioneRimossiList = new ArrayList<>();

        List<ProdottoCollegato> prodotti = new ArrayList<>();
        if (installazione != null && !installazione.isNew()) {
            prodotti = vendingAnagraficaBD.caricaProdottiCollegatiByInstallazione(installazione.getId());
        }

        for (ProdottoCollegato prodotto : prodotti) {
            prodottiList.add(prodotto);
            if (prodotto instanceof ProdottoInstallazione) {
                if (prodotto.getTipo() == TipoProdottoCollegato.AGGIUNTO) {
                    prodottiInstallazioneAggiuntiList.add(prodotto);
                } else {
                    prodottiInstallazioneRimossiList.add(prodotto);
                }
            }
        }

        prodottiComponent.getTableWidget().setRows(prodottiList);
        prodottiInstallazioneAggiuntiComponent.getTableWidget().setRows(prodottiInstallazioneAggiuntiList);
        prodottiInstallazioneRimossiComponent.getTableWidget().setRows(prodottiInstallazioneRimossiList);
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
        this.installazione = (Installazione) object;

        prodottiInstallazioneAggiuntiComponent.setInstallazione(installazione);
        prodottiInstallazioneRimossiComponent.setInstallazione(installazione);
        listaDefinitivaCommand.setEnabled(installazione != null && !installazione.isNew());
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
