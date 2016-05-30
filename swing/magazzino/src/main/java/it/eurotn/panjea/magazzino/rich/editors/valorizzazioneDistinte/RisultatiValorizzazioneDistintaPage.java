package it.eurotn.panjea.magazzino.rich.editors.valorizzazioneDistinte;

import java.util.Map;

import javax.swing.JComponent;

import org.springframework.richclient.dialog.AbstractDialogPage;
import org.springframework.richclient.settings.Settings;

import com.jidesoft.grid.TreeTable;
import com.jidesoft.swing.JideScrollPane;

import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoDocumentoBD;
import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriValorizzazioneDistinte;
import it.eurotn.panjea.mrp.domain.Bom;
import it.eurotn.panjea.mrp.util.ArticoloConfigurazioneKey;
import it.eurotn.rich.control.table.JideTableSearchable;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;

public class RisultatiValorizzazioneDistintaPage extends AbstractDialogPage implements IPageLifecycleAdvisor {

    private ParametriValorizzazioneDistinte parametri;
    private TreeTable tree;
    private IMagazzinoDocumentoBD magazzinoDocumentoBD;

    /**
     * Costruttore.
     */
    public RisultatiValorizzazioneDistintaPage() {
        super("risultatiValorizzazioneDistintaPage");
    }

    @Override
    protected JComponent createControl() {
        tree = new TreeTable();
        tree.setRowHeight(25);
        tree.sortColumn(0);
        new JideTableSearchable(tree);
        return new JideScrollPane(tree);
    }

    @Override
    public void dispose() {
    }

    /**
     * @return Returns the magazzinoDocumentoBD.
     */
    public IMagazzinoDocumentoBD getMagazzinoDocumentoBD() {
        return magazzinoDocumentoBD;
    }

    @Override
    public void loadData() {
    }

    @Override
    public void onPostPageOpen() {
    }

    @Override
    public boolean onPrePageOpen() {
        return true;
    }

    @Override
    public void postSetFormObject(Object object) {
    }

    @Override
    public void preSetFormObject(Object object) {

    }

    @Override
    public void refreshData() {
        if (parametri.isEffettuaRicerca()) {
            Map<ArticoloConfigurazioneKey, Bom> result = magazzinoDocumentoBD.valorizzaDistinte(parametri);
            ValorizzazioneDistinteTreeTableModel model = new ValorizzazioneDistinteTreeTableModel(result.values());
            if (!isControlCreated()) {
                createControl();
            }
            tree.setModel(model);
        }
    }

    @Override
    public void restoreState(Settings arg0) {
    }

    @Override
    public void saveState(Settings arg0) {
    }

    @Override
    public void setFormObject(Object object) {
        parametri = (ParametriValorizzazioneDistinte) object;
    }

    /**
     * @param magazzinoDocumentoBD
     *            The magazzinoDocumentoBD to set.
     */
    public void setMagazzinoDocumentoBD(IMagazzinoDocumentoBD magazzinoDocumentoBD) {
        this.magazzinoDocumentoBD = magazzinoDocumentoBD;
    }

    @Override
    public void setReadOnly(boolean readOnly) {

    }

}
