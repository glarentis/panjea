package it.eurotn.panjea.vending.rich.editors.modelli;

import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;

import org.springframework.richclient.dialog.AbstractDialogPage;
import org.springframework.richclient.settings.Settings;
import org.springframework.richclient.util.RcpSupport;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import it.eurotn.panjea.manutenzioni.domain.ProdottoCollegato;
import it.eurotn.panjea.manutenzioni.domain.TipoProdottoCollegato;
import it.eurotn.panjea.vending.domain.Modello;
import it.eurotn.panjea.vending.domain.ProdottoModello;
import it.eurotn.panjea.vending.rich.bd.IVendingAnagraficaBD;
import it.eurotn.rich.command.JideToggleCommand;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;

public class ProdottiModelloPage extends AbstractDialogPage implements IPageLifecycleAdvisor {

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
            if (modello != null && !modello.isNew()) {
                prodotti = vendingAnagraficaBD.caricaProdottiCollegatiByModello(modello.getId());
            }
            prodottiComponent.getTableWidget().setRows(prodotti);
        }

        @Override
        protected void onSelection() {
            if (modello != null && !modello.isNew()) {
                List<ProdottoCollegato> prodotti = vendingAnagraficaBD
                        .caricaProdottiCollegatiByModello(modello.getId());

                // rimuovo tutti i prodotti rimossi dal modello
                prodotti.removeAll(prodottiModelloRimossiComponent.getTableWidget().getRows());

                // rimuovo tutti gli articoli uguali a quelli rimossi dal modello
                List<Integer> articoliRimossi = new ArrayList<>();
                for (ProdottoCollegato prodottoRimosso : prodottiModelloRimossiComponent.getTableWidget().getRows()) {
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

    private Modello modello;

    private IVendingAnagraficaBD vendingAnagraficaBD;

    private ProdottiComponent prodottiComponent = new ProdottiComponent();
    private ProdottiModelloAggiuntiComponent prodottiModelloAggiuntiComponent = new ProdottiModelloAggiuntiComponent();
    private ProdottiModelloRimossiComponent prodottiModelloRimossiComponent = new ProdottiModelloRimossiComponent();

    private ProdottiTableListener prodottiTableListener = new ProdottiTableListener();

    private ListaDefinitivaCommand listaDefinitivaCommand;

    protected ProdottiModelloPage() {
        super("prodottiModelloPage");
    }

    @Override
    protected JComponent createControl() {
        FormLayout layout = new FormLayout("4dlu,fill:pref,10dlu,fill:pref", "4dlu,145dlu,10dlu,145dlu,4dlu,default");
        JPanel rootPanel = getComponentFactory().createPanel(layout);
        rootPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        CellConstraints cc = new CellConstraints();

        rootPanel.add(prodottiComponent, cc.xywh(2, 2, 1, 3));
        rootPanel.add(prodottiModelloAggiuntiComponent, cc.xy(4, 2));
        rootPanel.add(prodottiModelloRimossiComponent, cc.xy(4, 4));

        listaDefinitivaCommand = new ListaDefinitivaCommand();
        rootPanel.add(listaDefinitivaCommand.createButton(), cc.xy(2, 6));

        prodottiModelloAggiuntiComponent.addProdottiListener(prodottiTableListener);
        prodottiModelloRimossiComponent.addProdottiListener(prodottiTableListener);

        return rootPanel;
    }

    @Override
    public void dispose() {
        prodottiComponent.dispose();
        prodottiModelloAggiuntiComponent.dispose();
        prodottiModelloRimossiComponent.dispose();
    }

    @Override
    public void loadData() {
        List<ProdottoCollegato> prodottiList = new ArrayList<>();
        List<ProdottoCollegato> prodottiModelloAggiuntiList = new ArrayList<>();
        List<ProdottoCollegato> prodottiModelloRimossiList = new ArrayList<>();

        List<ProdottoCollegato> prodotti = new ArrayList<>();
        if (modello != null && !modello.isNew()) {
            prodotti = vendingAnagraficaBD.caricaProdottiCollegatiByModello(modello.getId());
        }

        for (ProdottoCollegato prodotto : prodotti) {
            prodottiList.add(prodotto);
            if (prodotto instanceof ProdottoModello) {
                if (prodotto.getTipo() == TipoProdottoCollegato.AGGIUNTO) {
                    prodottiModelloAggiuntiList.add(prodotto);
                } else {
                    prodottiModelloRimossiList.add(prodotto);
                }
            }
        }

        prodottiComponent.getTableWidget().setRows(prodottiList);
        prodottiModelloAggiuntiComponent.getTableWidget().setRows(prodottiModelloAggiuntiList);
        prodottiModelloRimossiComponent.getTableWidget().setRows(prodottiModelloRimossiList);
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
        this.modello = (Modello) object;

        prodottiModelloAggiuntiComponent.setModello(modello);
        prodottiModelloRimossiComponent.setModello(modello);
        listaDefinitivaCommand.setEnabled(modello != null && !modello.isNew());
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
