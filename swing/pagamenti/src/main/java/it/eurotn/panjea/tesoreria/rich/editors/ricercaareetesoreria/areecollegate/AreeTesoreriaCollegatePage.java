package it.eurotn.panjea.tesoreria.rich.editors.ricercaareetesoreria.areecollegate;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import org.springframework.richclient.dialog.AbstractDialogPage;
import org.springframework.richclient.settings.Settings;

import it.eurotn.panjea.tesoreria.domain.AreaTesoreria;
import it.eurotn.panjea.tesoreria.rich.bd.ITesoreriaBD;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;

public class AreeTesoreriaCollegatePage extends AbstractDialogPage implements IPageLifecycleAdvisor {

    private class LoadDataActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (areaTesoreria != null && AreeTesoreriaCollegatePage.this.isEnabled()) {

                List<AreaTesoreria> areeCollegate = tesoreriaBD.caricaAreeCollegate(areaTesoreria);

                rootPanel.removeAll();
                for (AreaTesoreria areaTesoreriaTmp : areeCollegate) {
                    rootPanel.add(new JLabel("Area: " + areaTesoreriaTmp.getClass().getName() + " - "
                            + areaTesoreriaTmp.getDocumento().getTipoDocumento().getCodice() + " - "
                            + areaTesoreriaTmp.getDocumento().getCodice()));
                }
            }
        }
    }

    public static final String PAGE_ID = "areeTesoreriaCollegatePage";

    private AreaTesoreria areaTesoreria;
    private ITesoreriaBD tesoreriaBD;

    private JPanel rootPanel;

    private final JLabel waitLabel;
    private Timer timer;
    private final LoadDataActionListener loadDataActionListener;

    /**
     * Costruttore.
     */
    protected AreeTesoreriaCollegatePage() {
        super(PAGE_ID);

        loadDataActionListener = new LoadDataActionListener();
        timer = new Timer(500, loadDataActionListener);
        timer.setRepeats(false);

        waitLabel = new JLabel("Caricamento in corso....");
        waitLabel.setHorizontalAlignment(SwingConstants.CENTER);
    }

    @Override
    protected JComponent createControl() {
        rootPanel = getComponentFactory().createPanel(new FlowLayout(FlowLayout.LEADING));
        return rootPanel;
    }

    @Override
    public void dispose() {
        this.timer.removeActionListener(loadDataActionListener);
        this.timer = null;
    }

    @Override
    public void loadData() {
        rootPanel.removeAll();

        if (this.areaTesoreria != null) {
            rootPanel.add(waitLabel, BorderLayout.CENTER);
            rootPanel.repaint();
        }

        timer.restart();
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
