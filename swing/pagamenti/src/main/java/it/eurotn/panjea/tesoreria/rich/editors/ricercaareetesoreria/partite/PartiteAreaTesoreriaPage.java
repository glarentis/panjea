package it.eurotn.panjea.tesoreria.rich.editors.ricercaareetesoreria.partite;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.springframework.richclient.dialog.AbstractDialogPage;
import org.springframework.richclient.settings.Settings;

import it.eurotn.panjea.tesoreria.domain.AreaTesoreria;
import it.eurotn.panjea.tesoreria.rich.bd.ITesoreriaBD;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;

public class PartiteAreaTesoreriaPage extends AbstractDialogPage implements IPageLifecycleAdvisor {

    public static final String PAGE_ID = "partiteAreaTesoreriaPage";

    private AreaTesoreria areaTesoreria;
    private ITesoreriaBD tesoreriaBD;

    private final JLabel waitLabel;

    private final JPanel partitePanel = getComponentFactory().createPanel(new BorderLayout());

    /**
     * Costruttore.
     */
    protected PartiteAreaTesoreriaPage() {
        super(PAGE_ID);
        waitLabel = new JLabel("Caricamento in corso....");
        waitLabel.setHorizontalAlignment(SwingConstants.CENTER);
    }

    @Override
    protected JComponent createControl() {

        JPanel rootPanel = getComponentFactory().createPanel(new BorderLayout());
        rootPanel.add(partitePanel, BorderLayout.CENTER);
        rootPanel.add(createFilterPanelComponent(), BorderLayout.NORTH);
        return rootPanel;
    }

    /**
     * Crea il componente per i filtri e i raggruppamenti.
     * 
     * @return componente creato
     */
    private JComponent createFilterPanelComponent() {
        JPanel rootPanel = getComponentFactory().createPanel(new BorderLayout());
        return rootPanel;
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
        partitePanel.removeAll();

        if (this.areaTesoreria != null) {
            partitePanel.add(waitLabel, BorderLayout.CENTER);
            partitePanel.repaint();
        }
        if (areaTesoreria != null && this.isEnabled()) {
            AreaTesoreria areaTesoreriaFull = tesoreriaBD.caricaAreaTesoreria(areaTesoreria);

            if (areaTesoreriaFull.equals(areaTesoreria)) {
                JComponent componentBuilder = PartiteComponentBuilderFactory.createComponent(areaTesoreriaFull,
                        tesoreriaBD);

                partitePanel.removeAll();
                partitePanel.add(getComponentFactory().createScrollPane(componentBuilder), BorderLayout.CENTER);
            }
        } else {
            partitePanel.removeAll();
            partitePanel.repaint();
        }

    }

    @Override
    public void restoreState(Settings arg0) {
    }

    @Override
    public void saveState(Settings arg0) {
    }

    @Override
    public void setEnabled(boolean enabled) {
        boolean previousEnable = this.isEnabled();
        super.setEnabled(enabled);

        if (!previousEnable && enabled) {
            loadData();
        }
    }

    @Override
    public void setFormObject(Object object) {
        if (object instanceof AreaTesoreria) {
            this.areaTesoreria = (AreaTesoreria) object;
        } else {
            this.areaTesoreria = null;
        }
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
