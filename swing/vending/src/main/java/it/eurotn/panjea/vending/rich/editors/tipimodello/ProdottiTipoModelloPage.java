package it.eurotn.panjea.vending.rich.editors.tipimodello;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;

import org.springframework.richclient.dialog.AbstractDialogPage;
import org.springframework.richclient.settings.Settings;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import it.eurotn.panjea.manutenzioni.domain.ProdottoCollegato;
import it.eurotn.panjea.vending.domain.TipoModello;
import it.eurotn.panjea.vending.rich.bd.IVendingAnagraficaBD;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;

public class ProdottiTipoModelloPage extends AbstractDialogPage implements IPageLifecycleAdvisor {

    private TipoModello tipoModello;

    private IVendingAnagraficaBD vendingAnagraficaBD;

    private ProdottiTipoModelloAggiuntiComponent prodottiTipoModelloAggiuntiComponent = new ProdottiTipoModelloAggiuntiComponent();

    protected ProdottiTipoModelloPage() {
        super("prodottiTipoModelloPage");
    }

    @Override
    protected JComponent createControl() {

        FormLayout layout = new FormLayout("4dlu,fill:300dlu", "4dlu,default");
        JPanel rootPanel = getComponentFactory().createPanel(layout);
        rootPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        CellConstraints cc = new CellConstraints();
        rootPanel.add(prodottiTipoModelloAggiuntiComponent, cc.xy(2, 2));

        return rootPanel;
    }

    @Override
    public void dispose() {
        prodottiTipoModelloAggiuntiComponent.dispose();
    }

    @Override
    public void loadData() {
        List<ProdottoCollegato> prodotti = new ArrayList<>();

        if (tipoModello != null && !tipoModello.isNew()) {
            prodotti = vendingAnagraficaBD.caricaProdottiCollegatiByTipoModello(tipoModello.getId());
        }

        prodottiTipoModelloAggiuntiComponent.getTableWidget().setRows(prodotti);
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
    public void restoreState(Settings settings) {
        // Non utilizzato
    }

    @Override
    public void saveState(Settings settings) {
        // Non utilizzato
    }

    @Override
    public void setFormObject(Object object) {
        this.tipoModello = (TipoModello) object;

        prodottiTipoModelloAggiuntiComponent.setTipoModello(tipoModello);
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
