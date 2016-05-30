/**
 *
 */
package it.eurotn.panjea.magazzino.rich.editors.contratto.sedi;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;
import javax.swing.SwingConstants;

import org.springframework.richclient.dialog.AbstractDialogPage;
import org.springframework.richclient.settings.Settings;

import com.jidesoft.swing.JideTabbedPane;

import it.eurotn.panjea.magazzino.domain.Contratto;
import it.eurotn.panjea.magazzino.rich.bd.IContrattoBD;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;

/**
 * @author fattazzo
 *
 */
public class ContrattoSediPage extends AbstractDialogPage implements IPageLifecycleAdvisor, PropertyChangeListener {

    public static final String PAGE_ID = "contrattoSediPage";

    private static final String SEDI_CONTRATTO_TITLE_PANE = "sediContrattoTitlePane";
    private static final String CATEGORIE_SEDI_CONTRATTO_TITLE_PANE = "categorieSediContrattoTitlePane";

    private Contratto contratto;
    private IMagazzinoAnagraficaBD magazzinoAnagraficaBD;
    private IContrattoBD contrattoBD;
    private SediContrattoComponent sediContrattoComponent;
    private CategorieSediContrattoComponent categorieSediContrattoComponent;

    /**
     * Costruttore privato.
     */
    protected ContrattoSediPage() {
        super(PAGE_ID, true);
    }

    @Override
    protected JComponent createControl() {
        logger.debug("--> Enter createControl");
        JideTabbedPane tabbedPane = new JideTabbedPane();

        // tab di sedi contratto
        sediContrattoComponent = new SediContrattoComponent(magazzinoAnagraficaBD, contrattoBD);
        sediContrattoComponent.getContrattoChangedPublisher()
                .addPropertyChangeListener(SediContrattoComponent.PAGE_COMPONENT_ID, this);
        tabbedPane.addTab(getMessage(SEDI_CONTRATTO_TITLE_PANE), sediContrattoComponent.createControl());

        // tab di categorie sedi contratto
        categorieSediContrattoComponent = new CategorieSediContrattoComponent(magazzinoAnagraficaBD, contrattoBD);
        categorieSediContrattoComponent.getContrattoChangedPublisher()
                .addPropertyChangeListener(CategorieSediContrattoComponent.PAGE_COMPONENT_ID, this);
        tabbedPane.addTab(getMessage(CATEGORIE_SEDI_CONTRATTO_TITLE_PANE),
                categorieSediContrattoComponent.createControl());

        tabbedPane.setTabPlacement(SwingConstants.BOTTOM);
        logger.debug("--> Exit createControl");
        return tabbedPane;
    }

    @Override
    public void dispose() {
    }

    /**
     * 
     * @return Contratto corrente della pagina
     */
    public Contratto getContratto() {
        return contratto;
    }

    @Override
    public void loadData() {
        sediContrattoComponent.loadData(this.contratto);
        categorieSediContrattoComponent.loadData(this.contratto);
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
    public void propertyChange(PropertyChangeEvent evt) {
        String pageComponentId = evt.getPropertyName();
        Contratto nuovoContratto = (Contratto) evt.getNewValue();
        // notifico alle altre pages dell'editor il nuovo contratto
        firePropertyChange(IPageLifecycleAdvisor.OBJECT_CHANGED, null, nuovoContratto);
        this.contratto = nuovoContratto;
        // devo aggiornare la seconda tab oltre che le altre page contenute nell'editor
        if (pageComponentId.equals(SediContrattoComponent.PAGE_COMPONENT_ID)) {
            categorieSediContrattoComponent.loadData(this.contratto);
        } else if (pageComponentId.equals(CategorieSediContrattoComponent.PAGE_COMPONENT_ID)) {
            sediContrattoComponent.loadData(this.contratto);
        }
    }

    @Override
    public void refreshData() {
        loadData();
    }

    @Override
    public void restoreState(Settings settings) {
    }

    @Override
    public void saveState(Settings settings) {
    }

    public void setContrattoBD(IContrattoBD contrattoBD) {
        this.contrattoBD = contrattoBD;
    }

    @Override
    public void setFormObject(Object object) {
        this.contratto = (Contratto) object;
    }

    public void setMagazzinoAnagraficaBD(IMagazzinoAnagraficaBD magazzinoAnagraficaBD) {
        this.magazzinoAnagraficaBD = magazzinoAnagraficaBD;
    }

    @Override
    public void setReadOnly(boolean readOnly) {

    }

}
