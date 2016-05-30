package it.eurotn.panjea.magazzino.rich.editors.articolo;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.dialog.AbstractDialogPage;
import org.springframework.richclient.factory.ComponentFactory;
import org.springframework.richclient.settings.Settings;

import com.jidesoft.grid.HierarchicalTable;
import com.jidesoft.swing.JideScrollPane;
import com.jidesoft.swing.JideTabbedPane;

import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoDocumentoBD;
import it.eurotn.panjea.magazzino.util.StatisticaArticolo;
import it.eurotn.panjea.magazzino.util.StatisticheArticolo;
import it.eurotn.panjea.rich.factory.PanjeaComponentFactory;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;

public class StatisticheArticoloPage extends AbstractDialogPage implements IPageLifecycleAdvisor {

    private class RefreshChangeListener implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent changeevent) {
            refreshData();
        }
    }

    public static final String PAGE_ID = "statisticheArticoloPage";
    public static final String CARD_DATI_KEY = "DATI";
    public static final String CARD_WAITING_KEY = "WAIT";
    private StatisticheArticolo statisticheArticolo;
    private Articolo articolo;
    private JideTabbedPane tabbedPane;
    private IMagazzinoDocumentoBD magazzinoDocumentoBD;
    private JPanel rootPanel;
    private CardLayout cardLayout;
    private JSpinner annoControl;
    private RefreshChangeListener refreshChangeListener;

    /**
     * Costruttore.
     */
    protected StatisticheArticoloPage() {
        super(PAGE_ID);
    }

    @Override
    protected JComponent createControl() {
        PanjeaComponentFactory componentFactory = (PanjeaComponentFactory) Application.services()
                .getService(ComponentFactory.class);
        annoControl = componentFactory.createSpinner();
        annoControl.setValue(Calendar.getInstance().get(Calendar.YEAR));
        refreshChangeListener = new RefreshChangeListener();
        annoControl.addChangeListener(refreshChangeListener);

        cardLayout = new CardLayout();
        rootPanel = componentFactory.createPanel(cardLayout);
        tabbedPane = (JideTabbedPane) componentFactory.createTabbedPane();
        tabbedPane.setTabShape(JideTabbedPane.SHAPE_BOX);
        tabbedPane.setTabPlacement(SwingConstants.LEFT);
        tabbedPane.setBorder(BorderFactory.createEmptyBorder());

        JPanel statistichePanel = componentFactory.createPanel(new BorderLayout());

        rootPanel.add(tabbedPane, CARD_DATI_KEY);

        JLabel waitingLabel = componentFactory.createLabel("CARICAMENTO IN CORSO");
        waitingLabel.setHorizontalAlignment(SwingConstants.CENTER);
        JPanel waitingPanel = componentFactory.createPanel(new BorderLayout());
        waitingPanel.add(waitingLabel, BorderLayout.CENTER);
        rootPanel.add(waitingPanel, CARD_WAITING_KEY);

        statistichePanel.add(annoControl, BorderLayout.NORTH);
        statistichePanel.add(rootPanel, BorderLayout.CENTER);

        return statistichePanel;
    }

    @Override
    public void dispose() {
        if (isControlCreated()) {
            tabbedPane.removeChangeListener(refreshChangeListener);
        }
    }

    /**
     * @return Returns the magazzinoDocumentoBD.
     */
    public IMagazzinoDocumentoBD getMagazzinoDocumentoBD() {
        return magazzinoDocumentoBD;
    }

    @Override
    public void loadData() {
        cardLayout.show(rootPanel, CARD_WAITING_KEY);
        tabbedPane.removeAll();

        new SwingWorker<StatisticheArticolo, Void>() {

            @Override
            protected StatisticheArticolo doInBackground() throws Exception {
                return magazzinoDocumentoBD.caricaStatisticheArticolo(articolo,
                        new Integer(annoControl.getValue().toString()));
            }

            @Override
            protected void done() {
                try {
                    statisticheArticolo = get();
                    if (statisticheArticolo.getIdArticolo() != articolo.getId()) {
                        // mentre stavo creando le statistiche è stato selezionato un nuovo
                        // articolo...non faccio nulla.
                        return;
                    }
                } catch (Exception e) {
                    logger.warn("--> errore nel visualizzare le statistiche articolo.", e);
                }
                int depositoPrincipaleTab = 0;
                tabbedPane.removeAll();
                Map<String, List<StatisticaArticolo>> statisticheTipoDeposito = statisticheArticolo
                        .getStatistichePerTipologiaDeposito();
                for (Entry<String, List<StatisticaArticolo>> entry : statisticheTipoDeposito.entrySet()) {
                    // Se ho solamente due elementi allora non mi serve la statistica totale..la
                    // rimuovo dalla lista.
                    if (entry.getValue().size() == 2) {
                        entry.getValue().remove(0);
                    }

                    StatisticheArticoloTable statisticheTable = new StatisticheArticoloTable(magazzinoDocumentoBD,
                            articolo);
                    statisticheTable.setRows(entry.getValue());
                    statisticheTable.getTable().setRowHeight(26);
                    if (entry.getValue() != null && !entry.getValue().isEmpty()) {
                        int idx = -1;
                        for (StatisticaArticolo statisticaArticolo : statisticheTable.getVisibleObjects()) {
                            idx++;
                            // setto come expanded la riga del deposito principale e salvo l'indice
                            // del tab per poi alla
                            // fine selezionarlo
                            if (statisticaArticolo.getDepositoLite().isPrincipale()) {
                                ((HierarchicalTable) statisticheTable.getTable()).expandRow(idx);
                                depositoPrincipaleTab = tabbedPane.getTabCount();
                                break;
                            }
                        }
                    }

                    JideScrollPane simpleScrollPane = new JideScrollPane(statisticheTable.getComponent());
                    simpleScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                    simpleScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
                    simpleScrollPane.setWheelScrollingEnabled(true);
                    simpleScrollPane.setBorder(BorderFactory.createEmptyBorder());

                    tabbedPane.add(entry.getKey(), simpleScrollPane);
                }
                cardLayout.show(rootPanel, CARD_DATI_KEY);
                if (tabbedPane.getTabCount() > 0) {
                    tabbedPane.setSelectedIndex(depositoPrincipaleTab);
                }
            };
        }.execute();

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
        loadData();
    }

    @Override
    public void restoreState(Settings arg0) {
    }

    @Override
    public void saveState(Settings arg0) {
    }

    @Override
    public void setFormObject(Object object) {
        articolo = (Articolo) object;
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
