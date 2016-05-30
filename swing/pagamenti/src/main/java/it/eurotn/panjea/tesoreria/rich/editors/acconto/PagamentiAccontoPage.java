package it.eurotn.panjea.tesoreria.rich.editors.acconto;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.springframework.richclient.dialog.AbstractDialogPage;
import org.springframework.richclient.settings.Settings;

import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.partite.rich.editors.ricercarate.PagamentiSituazioneRataComponent;
import it.eurotn.panjea.tesoreria.domain.AreaAcconto;
import it.eurotn.panjea.tesoreria.domain.Pagamento;
import it.eurotn.panjea.tesoreria.rich.bd.ITesoreriaBD;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;

public class PagamentiAccontoPage extends AbstractDialogPage implements IPageLifecycleAdvisor {

    public static final String PAGE_ID = "pagamentiAccontoPage";

    private AreaAcconto areaAcconto;

    private JPanel rootPanel;

    private ITesoreriaBD tesoreriaBD;
    private AziendaCorrente aziendaCorrente;

    /**
     * Costruttore.
     */
    protected PagamentiAccontoPage() {
        super(PAGE_ID);
    }

    @Override
    protected JComponent createControl() {
        rootPanel = new JPanel(new BorderLayout());
        return rootPanel;
    }

    /**
     * 
     * @return componente con i pagamenti effettuati per l'acconto.
     */
    private JComponent createPagamentiComponent() {

        JPanel pagamentiPanel = getComponentFactory().createPanel(new BorderLayout());

        List<Pagamento> pagamenti = tesoreriaBD.caricaPagamentiByAreaAcconto(areaAcconto);

        PagamentiSituazioneRataComponent component = new PagamentiSituazioneRataComponent(1);
        component.setAziendaCorrente(aziendaCorrente);
        component.setTesoreriaBD(tesoreriaBD);
        pagamentiPanel.add(component.getControl(), BorderLayout.CENTER);
        component.updateControl(pagamenti);

        return new JScrollPane(pagamentiPanel);
    }

    @Override
    public void dispose() {
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
        rootPanel.removeAll();
        rootPanel.add(createPagamentiComponent(), BorderLayout.CENTER);
    }

    @Override
    public void restoreState(Settings arg0) {
    }

    @Override
    public void saveState(Settings arg0) {
    }

    /**
     * @param aziendaCorrente
     *            the aziendaCorrente to set
     */
    public void setAziendaCorrente(AziendaCorrente aziendaCorrente) {
        this.aziendaCorrente = aziendaCorrente;
    }

    @Override
    public void setFormObject(Object object) {
        this.areaAcconto = ((AreaAccontoWrapper) object).getAreaAcconto();
    }

    @Override
    public void setReadOnly(boolean readOnly) {
    }

    /**
     * @param tesoreriaBD
     *            the tesoreriaBD to set
     */
    public void setTesoreriaBD(ITesoreriaBD tesoreriaBD) {
        this.tesoreriaBD = tesoreriaBD;
    }

}
