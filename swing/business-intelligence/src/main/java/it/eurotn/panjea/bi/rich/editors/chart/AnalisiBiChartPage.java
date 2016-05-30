/**
 *
 */
package it.eurotn.panjea.bi.rich.editors.chart;

import java.awt.BorderLayout;
import java.awt.Insets;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

import org.apache.commons.lang3.StringUtils;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.config.CommandButtonConfigurer;
import org.springframework.richclient.command.config.CommandFaceDescriptor;
import org.springframework.richclient.core.DefaultMessage;
import org.springframework.richclient.dialog.AbstractDialogPage;
import org.springframework.richclient.dialog.MessageDialog;
import org.springframework.richclient.factory.ButtonFactory;
import org.springframework.richclient.settings.Settings;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.pivot.IPivotDataModel;
import com.steema.teechart.Chart;
import com.steema.teechart.Commander;
import com.steema.teechart.TChart;
import com.steema.teechart.drawing.Color;
import com.steema.teechart.events.ITeeEventListener;
import com.steema.teechart.events.SeriesMouseAdapter;
import com.steema.teechart.events.SeriesMouseEvent;
import com.steema.teechart.events.TeeEvent;
import com.steema.teechart.styles.MarksStyle;
import com.steema.teechart.styles.SHPMap;
import com.steema.teechart.styles.Series;
import com.steema.teechart.styles.SeriesCollection;
import com.steema.teechart.styles.SeriesEvent;
import com.steema.teechart.styles.SeriesEventStyle;
import com.steema.teechart.themes.ColorPalettes;
import com.steema.teechart.themes.Theme;
import com.steema.teechart.tools.MarksTip;

import it.eurotn.panjea.bi.domain.analisi.AnalisiBi;
import it.eurotn.panjea.bi.rich.editors.analisi.AnalisiBiEditorController;
import it.eurotn.panjea.bi.rich.editors.chart.modelbuilder.ModelBuilder;
import it.eurotn.panjea.bi.rich.editors.chart.modelbuilder.SerieDimensioniAggregateModelBuilder;
import it.eurotn.panjea.bi.rich.editors.chart.modelbuilder.SerieMapModelBuilder;
import it.eurotn.panjea.bi.rich.editors.chart.modelbuilder.SerieMisureModelBuilder;
import it.eurotn.rich.command.JideToggleCommand;
import it.eurotn.rich.editors.IEditorCommands;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;

/**
 * Gestisce la visualizzazione del grafico della query di business.
 *
 * @author fattazzo
 */
public class AnalisiBiChartPage extends AbstractDialogPage implements IPageLifecycleAdvisor, IEditorCommands {

    private class ChangeModelCommand extends JideToggleCommand {

        public static final String COMMAND_ID = "changeModelCommand";
        private CommandFaceDescriptor selectedFaceDescriptor;

        private CommandFaceDescriptor deselectedFaceDescriptor;

        /**
         * Costruttore di default.
         *
         * @param analisiBiEditorController
         *            controller
         */
        public ChangeModelCommand() {
            super(COMMAND_ID);
            RcpSupport.configure(this);

            selectedFaceDescriptor = new CommandFaceDescriptor("", RcpSupport.getIcon(COMMAND_ID + ".selection.icon"),
                    RcpSupport.getMessage(COMMAND_ID + ".selection.caption"));
            deselectedFaceDescriptor = new CommandFaceDescriptor("",
                    RcpSupport.getIcon(COMMAND_ID + ".deselection.icon"),
                    RcpSupport.getMessage(COMMAND_ID + ".deselection.caption"));

            setFaceDescriptor(selectedFaceDescriptor);
        }

        @Override
        public AbstractButton createButton(String faceDescriptorId, ButtonFactory buttonFactory,
                CommandButtonConfigurer buttonConfigurer) {
            AbstractButton button = super.createButton(faceDescriptorId, buttonFactory, buttonConfigurer);
            button.setContentAreaFilled(false);
            button.setMargin(new Insets(2, 2, 2, 2));
            return button;
        }

        @Override
        protected void onDeselection() {
            raggruppaGrafico = false;
            createModel();
            this.setFaceDescriptor(selectedFaceDescriptor);
        }

        @Override
        protected void onSelection() {
            raggruppaGrafico = true;
            createModel();
            this.setFaceDescriptor(deselectedFaceDescriptor);
        }

    }

    private class SeriesMouseListener extends SeriesMouseAdapter {

        @Override
        public void seriesClicked(SeriesMouseEvent e) {
            System.out.println(e.getSeries().toString() + e.getValueIndex());
            Series s = (Series) e.getSeries();

            Object object = s.getTitle() + e.getValueIndex();
            if (s instanceof SHPMap) {
                object = s.getTitle() + s.getMarkText(e.getValueIndex()).toLowerCase();
            }

            int[] righeColonne = rowColumnByValueIndex.get(object);
            if (righeColonne != null) {
                firePropertyChange(AnalisiBiEditorController.SELECTION_EVENT, null, righeColonne);
            }
        };
    }

    private class ShowMapCommand extends ActionCommand {

        private MapSelectionDialog mapSelectionDialog;

        /**
         * Costruttore.
         */
        public ShowMapCommand() {
            super("showMapCommand");
            RcpSupport.configure(this);
        }

        @Override
        public AbstractButton createButton(String faceDescriptorId, ButtonFactory buttonFactory,
                CommandButtonConfigurer buttonConfigurer) {
            AbstractButton button = super.createButton(faceDescriptorId, buttonFactory, buttonConfigurer);
            button.setContentAreaFilled(false);
            button.setMargin(new Insets(2, 2, 2, 2));
            return button;
        }

        @Override
        protected void doExecuteCommand() {
            getMapSelectionDialog().showDialog();
        }

        /**
         * @return the mapSelectionDialog
         */
        public MapSelectionDialog getMapSelectionDialog() {
            if (mapSelectionDialog == null) {
                mapSelectionDialog = new MapSelectionDialog(pivotDataModel) {

                    @Override
                    protected void onConfirm() {
                        if (!getFileMappeSelezionate().isEmpty()) {
                            nomeMappe = getFileMappeSelezionate();
                            visualizzaMappa = true;
                            createModel(new SerieMapModelBuilder(nomeMappe));
                        }
                    }
                };
            }

            return mapSelectionDialog;
        }
    }

    public static final String PAGE_ID = "analisiBiChartPage";

    public static final Color[] CUSTOM_PALETTE = { Color.fromArgb(0xFF4266A3), // blue
            Color.fromArgb(0xFFF39C35), // orange
            Color.fromArgb(0xFFF14C14), // red
            Color.fromArgb(0xFF4E97A8), // blue
            Color.fromArgb(0xFF2B406B), // blue
            Color.fromArgb(0xFF1D7B63), // blue-green dark
            Color.fromArgb(0xFFB3080E), // dark red
            Color.fromArgb(0xFFF2C05D), // pale orange
            Color.fromArgb(0xFF5DB79E), // blue-green
            Color.fromArgb(0xFF707070), // grey
            Color.fromArgb(0xFFF3EA8D), // yellow
            Color.fromArgb(0xFFB4B4B4), // grey
            Color.fromArgb(0xFFFF9999), Color.fromArgb(0xFF663399), Color.fromArgb(0xFFCCFFFF),
            Color.fromArgb(0xFFFFFFCC), Color.fromArgb(0xFF660066), Color.fromArgb(0xFF8080FF),
            Color.fromArgb(0xFFCC6600), Color.fromArgb(0xFFFFCCCC), Color.fromArgb(0xFF800000),
            Color.fromArgb(0xFFFF00FF), Color.fromArgb(0xFF00FFFF), Color.fromArgb(0xFFFFFF00),
            Color.fromArgb(0xFF800080), Color.fromArgb(0xFF000080), Color.fromArgb(0xFF808000),
            Color.fromArgb(0xFFFF0000), Color.fromArgb(0xFFFFCC00), Color.fromArgb(0xFFFFFFCC),
            Color.fromArgb(0xFFCCFFCC), Color.fromArgb(0xFF00FFFF), Color.fromArgb(0xFFFFCC99),
            Color.fromArgb(0xFFCC99FF) };
    private boolean raggruppaGrafico;
    private TChart chartPanel;
    private Chart chart;

    private IPivotDataModel pivotDataModel;
    private SeriesMouseListener seriesMouseListener;
    private java.util.Map<String, int[]> rowColumnByValueIndex;
    private ModelBuilder modelBuilder;

    private JPanel mainPanel;

    private byte[] configurazioneGrafico;

    private boolean readOnly;

    private boolean visualizzaMappa;
    private Set<String> nomeMappe;

    /**
     * Costruttore.
     */
    public AnalisiBiChartPage() {
        super(PAGE_ID);
        seriesMouseListener = new SeriesMouseListener();
        raggruppaGrafico = false;
        readOnly = false;
        visualizzaMappa = false;
    }

    /**
     *
     * @param analisiBiParam
     *            analisi con il layout da applicare
     */
    public void applyLayout(AnalisiBi analisiBiParam) {
        raggruppaGrafico = analisiBiParam.isRaggruppaGrafico();
        String[] tmpMappe = StringUtils.split(analisiBiParam.getNomeFileMappa(), ",");
        nomeMappe = new TreeSet<String>();
        if (tmpMappe != null && tmpMappe.length > 0) {
            nomeMappe.addAll(Arrays.asList(tmpMappe));
            visualizzaMappa = true;
        }
        configurazioneGrafico = analisiBiParam.getChartConfiguration();
        if (chartPanel != null) {
            chartPanel.dispose();
            chart = null;
            chartPanel = null;
        }
        initChart();
    }

    @Override
    protected JComponent createControl() {
        mainPanel = new JPanel(new BorderLayout());
        return mainPanel;
    }

    private void createModel() {
        if (visualizzaMappa) {
            modelBuilder = new SerieMapModelBuilder(nomeMappe);
        } else {
            nomeMappe = new TreeSet<String>();
            if (raggruppaGrafico) {
                modelBuilder = new SerieDimensioniAggregateModelBuilder();
            } else {
                modelBuilder = new SerieMisureModelBuilder();
            }
        }
        createModel(modelBuilder);
    }

    private void createModel(ModelBuilder paramModelBuilder) {
        modelBuilder = paramModelBuilder;
        rowColumnByValueIndex = modelBuilder.createModel(chartPanel, pivotDataModel);
    }

    @Override
    public void dispose() {
    }

    /**
     *
     * @return chart sulla pagina
     */
    public TChart getChart() {
        initChart();
        return chartPanel;
    }

    @Override
    public AbstractCommand getEditorDeleteCommand() {
        return null;
    }

    @Override
    public AbstractCommand getEditorLockCommand() {
        return null;
    }

    @Override
    public AbstractCommand getEditorNewCommand() {
        return null;
    }

    @Override
    public AbstractCommand getEditorSaveCommand() {
        return null;
    }

    @Override
    public AbstractCommand getEditorUndoCommand() {
        return null;
    }

    private void initChart() {
        if (chartPanel == null) {
            mainPanel.removeAll();
            chartPanel = new TChart();
            chart = null;
            if (configurazioneGrafico != null) {
                try {
                    chart = chartPanel.getImport().getTemplate()
                            .fromStream(new ByteArrayInputStream(configurazioneGrafico));
                    chart.setZoom(null);
                    chartPanel.refreshControl();
                    Iterator<?> iteratorSeries = chart.getSeries().iterator();
                    while (iteratorSeries.hasNext()) {
                        Series series = (Series) iteratorSeries.next();
                        series.addSeriesMouseListener(seriesMouseListener);
                    }

                } catch (Exception e) {
                    logger.error("-->errore nel caricare il format del grafico", e);
                    MessageDialog dialog = new MessageDialog("Errore",
                            new DefaultMessage("Errore nel caricare il formato del grafico.\nCarico il default"));
                    dialog.showDialog();
                }
            }

            if (chart == null) {
                chart = chartPanel.getChart();
                chart.getAspect().setChart3DPercent(7);
                chart.getAspect().setOrthogonal(false);
                chart.getAspect().setSmoothingMode(true);
                chart.getAspect().setView3D(false);
                chart.getTitle().setVisible(false);
                Theme theme = new com.steema.teechart.themes.BlackIsBackTheme(chart);
                theme.apply();
                ColorPalettes.applyPalette(chart, CUSTOM_PALETTE);

                MarksTip toolsTipTool = new MarksTip(chart);
                toolsTipTool.setSeries(null);
                toolsTipTool.setMouseDelay(200);
                toolsTipTool.setStyle(MarksStyle.LABELVALUE);
            }
            chartPanel.setChart(chart);
            chart.getListeners().add(new ITeeEventListener() {

                @Override
                public void invokeEvent(TeeEvent event) {
                    if (event instanceof SeriesEvent) {
                        SeriesEvent se = (SeriesEvent) event;
                        if (se.event == SeriesEventStyle.ADD) {
                            if (se.series instanceof Series) {
                                // La add la può richiamare se cambio gli assi o meno quindi
                                // per sicurezza rimuovo il listener (se non c'era va bene) e poi lo
                                // riaggiungo
                                Series serie = (Series) se.series;
                                serie.removeSeriesMouseListener(seriesMouseListener);
                                serie.addSeriesMouseListener(seriesMouseListener);
                            }
                        }
                        if (se.event == SeriesEventStyle.REMOVE) {
                            if (se.series instanceof Series) {
                                Series serie = (Series) se.series;
                                serie.removeSeriesMouseListener(seriesMouseListener);

                                if (serie instanceof SHPMap) {
                                    visualizzaMappa = false;
                                }
                            }
                        }
                    }

                }
            });

            if (!readOnly) {
                Commander toolbar = new Commander(chartPanel.getChart());
                toolbar.addSeparator();
                toolbar.add(new ChangeModelCommand().createButton());
                toolbar.add(new ShowMapCommand().createButton());
                toolbar.setVisible(true);
                toolbar.setOrientation(1);
                mainPanel.add(toolbar, BorderLayout.WEST);
            }
            mainPanel.add(chartPanel, BorderLayout.CENTER);
        }
    }

    @Override
    public void loadData() {
    }

    @Override
    public void onPostPageOpen() {
    }

    @Override
    public boolean onPrePageOpen() {
        return false;
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
    public void setFormObject(Object object) {
        if (object instanceof IPivotDataModel) {
            pivotDataModel = ((IPivotDataModel) object);
            if (isVisible()) {
                createModel();
            }
        }
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
            initChart();
            createModel();
        }
    }

    public AnalisiBi syncLayout(AnalisiBi analisiBi) {
        analisiBi.setRaggruppaGrafico(raggruppaGrafico);
        analisiBi.setNomeFileMappa(visualizzaMappa ? StringUtils.join(nomeMappe.iterator(), ",") : "");
        ByteArrayOutputStream m = new ByteArrayOutputStream();
        try {
            // se il grafico non è di tipo mappa devo pulire le serie dai loro valori prima di
            // ottenere lo stream del
            // template perchè per un bug di teechart non si riesce ad avere il template senza dati.
            // Nel caso della
            // mappa poi se pulisco e ricreo le serie vado incontro ad alcuni bug come ad esempio
            // artefatti sulla
            // visualizzazione ( solo alcuni shape vengono disegnati e altri no ) errore sulla
            // generazione dei colori
            // della legenda ( valori rgb non in range 0-255). Lo stream generato da una serie di
            // tipo mappa non
            // contiene gli shape quindi posso usarlo visto che la dimensione risulta essere più
            // piccola di un template
            // di una serie non di tipo mappa.
            if (!visualizzaMappa) {
                SeriesCollection serieList = chart.getSeries();
                for (Object serie : serieList) {
                    ((Series) serie).clear();
                }
                chartPanel.getExport().getTemplate().toStream(m);
                analisiBi.setChartConfiguration(m.toByteArray());
                applyLayout(analisiBi);
                createModel();
            }
        } catch (Exception e) {
            System.err.println(e);
            logger.error("-->errore ", e);
        }
        return analisiBi;
    }
}