package it.eurotn.panjea.lotti.rich.editors.articolo.situazione;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSplitPane;
import javax.swing.SwingConstants;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.support.ActionCommandInterceptorAdapter;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.settings.Settings;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.filter.Filter;
import com.jidesoft.grid.AbstractTableFilter;
import com.jidesoft.grid.FilterableTableModel;
import com.jidesoft.grid.TableColumnChooser;
import com.jidesoft.grid.TableModelWrapperUtils;
import com.jidesoft.pivot.AggregateTable;
import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.lotti.domain.Lotto;
import it.eurotn.panjea.lotti.rich.bd.ILottiBD;
import it.eurotn.panjea.lotti.rich.editors.LottoPage;
import it.eurotn.panjea.lotti.rich.editors.articolo.movimentazione.MovimentazioneLottoPage;
import it.eurotn.panjea.lotti.util.ParametriRicercaLotti;
import it.eurotn.panjea.lotti.util.StatisticaLotto;
import it.eurotn.panjea.lotti.util.StatisticaLotto.StatoLotto;
import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.panjea.magazzino.domain.Articolo.TipoLotto;
import it.eurotn.panjea.magazzino.domain.etichetta.EtichettaArticolo;
import it.eurotn.panjea.magazzino.domain.etichetta.ParametriStampaEtichetteArticolo;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.dialog.DefaultTitledPageApplicationDialog;
import it.eurotn.rich.editors.AbstractTablePageEditor;
import it.eurotn.rich.editors.IPageEditor;
import it.eurotn.rich.report.StampaCommand;

public class StatisticheLottiArticoloPage extends AbstractTablePageEditor<StatisticaLotto> {

    private class EditLottoCommand extends ActionCommand {

        @Override
        protected void doExecuteCommand() {

            final StatisticaLotto statisticaLotto = getTable().getSelectedObject();

            if (statisticaLotto != null) {
                Lotto lottoTmp = null;
                switch (articolo.getTipoLotto()) {
                case LOTTO:
                    lottoTmp = statisticaLotto.getLotto();
                    break;
                case LOTTO_INTERNO:
                    lottoTmp = statisticaLotto.getLottoInterno();
                    break;
                default:
                    throw new UnsupportedOperationException("L'articolo non prevede la gestione lotti!");
                }

                lottoTmp = lottiBD.caricaLotto(lottoTmp);

                final IPageEditor dialogPage = new LottoPage(lottoTmp);
                final DefaultTitledPageApplicationDialog dialog = new DefaultTitledPageApplicationDialog(lottoTmp, null,
                        dialogPage) {
                    @Override
                    protected boolean onFinish() {
                        final boolean finish = super.onFinish();

                        if (finish) {
                            refreshData();

                            // posso usare la statistica lotto precedente in quanto l'equals si basa solo su id e non su
                            // proprietà che posso modificare del lotto. In questo modo non devo crearmi property change
                            // da aggiungere sulla lottopage.
                            getTable().selectRowObject(statisticaLotto, StatisticheLottiArticoloPage.this);
                            movimentazioneLottoPage.update(null, statisticaLotto);
                        }
                        return finish;
                    }
                };
                dialog.showDialog();
            }
        }
    }

    private class StampaEtichetteCommand extends ApplicationWindowAwareCommand {

        public static final String COMMAND_ID = "stampaEtichetteCommand";

        /**
         * Costruttore.
         */
        public StampaEtichetteCommand() {
            super(COMMAND_ID);
            RcpSupport.configure(this);
        }

        @Override
        protected void doExecuteCommand() {

            final ParametriStampaEtichetteArticolo parametriEtichette = new ParametriStampaEtichetteArticolo();
            parametriEtichette.setGestioneLotti(Boolean.TRUE);

            final List<StatisticaLotto> righe = getTable().getSelectedObjects();

            for (final StatisticaLotto statisticaLotto : righe) {
                final EtichettaArticolo etichettaArticolo = new EtichettaArticolo();
                etichettaArticolo.setArticolo(statisticaLotto.getLotto().getArticolo());
                etichettaArticolo.setNumeroCopiePerStampa(1);
                etichettaArticolo.setNumeroDecimali(articolo.getNumeroDecimaliPrezzo());
                etichettaArticolo.setPercApplicazioneCodiceIva(articolo.getCodiceIva().getPercApplicazione());
                etichettaArticolo.setLotto(statisticaLotto.getLotto());

                parametriEtichette.getEtichetteArticolo().add(etichettaArticolo);
            }

            if (!parametriEtichette.getEtichetteArticolo().isEmpty()) {
                final LifecycleApplicationEvent event = new OpenEditorEvent(parametriEtichette);
                Application.instance().getApplicationContext().publishEvent(event);
            }
        }
    }

    public class StampaMovimentazioneLottiCommand extends StampaCommand {
        private static final String CONTROLLER_ID = "printMovimentazioneLottiCommand";

        private final AziendaCorrente aziendaCorrente;

        /**
         * Costruttore.
         *
         */
        public StampaMovimentazioneLottiCommand() {
            super("printMovimentazioneLottiCommand", CONTROLLER_ID);
            this.aziendaCorrente = RcpSupport.getBean(AziendaCorrente.BEAN_ID);
        }

        @Override
        protected Map<Object, Object> getParametri() {
            final HashMap<Object, Object> parametri = new HashMap<>();
            parametri.put("descAzienda", aziendaCorrente.getDenominazione());
            parametri.put("utente", PanjeaSwingUtil.getUtenteCorrente().getUserName());

            final StringBuilder lotti = new StringBuilder();
            for (final StatisticaLotto statisticaLotto : getTable().getSelectedObjects()) {
                if (!lotti.toString().isEmpty()) {
                    lotti.append(",");
                }
                switch (articolo.getTipoLotto()) {
                case LOTTO:
                    lotti.append(statisticaLotto.getLotto().getId());
                    break;
                case LOTTO_INTERNO:
                    lotti.append(statisticaLotto.getLottoInterno().getId());
                    break;
                default:
                    break;
                }
            }
            parametri.put("idLotti", lotti.toString());
            return parametri;
        }

        @Override
        protected String getReportName() {
            return "Movimentazione lotti";
        }

        @Override
        protected String getReportPath() {
            return "Lotti/movimentazione";
        }
    }

    private class StampaMovimentazioneLottiCommandInterceptor extends ActionCommandInterceptorAdapter {

        @Override
        public boolean preExecution(ActionCommand command) {
            final List<StatisticaLotto> selectedObjects = getTable().getSelectedObjects();
            return selectedObjects != null && !selectedObjects.isEmpty();
        }
    }

    private static final String CARD_NESSUNA_GESTIONE_LOTTI = "noLotti";

    private static final String CARD_RIEPILOGO_LOTTI = "riepilogoLotti";

    public static final String PAGE_ID = "statisticheLottiArticoloPage";
    private Articolo articolo;

    private ILottiBD lottiBD;

    private Lotto lotto;
    private MovimentazioneLottoPage movimentazioneLottoPage;
    private JPanel rootPanel;

    private StampaEtichetteCommand stampaEtichetteCommand;

    private StampaMovimentazioneLottiCommand stampaMovimentazioneLottiCommand;
    private Map<TipoLotto, String> tipiLottoCardLayout;

    /**
     *
     * Costruttore.
     *
     */
    protected StatisticheLottiArticoloPage() {
        super(PAGE_ID, new StatisticheLottiArticoloTableModel());
        getTable().setAggregatedColumns(new String[] { "deposito" });
        getTable().setPropertyCommandExecutor(new EditLottoCommand());
        getTable().getTable().getTableHeader().setReorderingAllowed(false);

        applyStatoFilter(new StatoLotto[] { StatoLotto.APERTO });
    }

    /**
     * Applica il filtro allo stato del lotto con il valore indicato.
     *
     * @param statiLotto
     *            stati da filtrare
     */
    private void applyStatoFilter(final StatoLotto[] statiLotto) {
        final FilterableTableModel filterableTableModel = (FilterableTableModel) TableModelWrapperUtils
                .getActualTableModel(getTable().getTable().getModel(), FilterableTableModel.class);
        filterableTableModel.clearFilters();
        filterableTableModel.setFiltersApplied(true);
        final Filter<StatoLotto> filter = new AbstractTableFilter<StatoLotto>() {

            private static final long serialVersionUID = 7620269785197603043L;

            @Override
            public boolean isValueFiltered(StatoLotto paramT) {
                final int idx = ArrayUtils.indexOf(statiLotto, paramT);
                return idx == -1;
            }
        };
        filterableTableModel.addFilter(1, filter);
        filterableTableModel.refresh();

        getTable().getTable().getSelectionModel().clearSelection();
        getTable().selectRow(0, null);
        if (movimentazioneLottoPage != null) {
            movimentazioneLottoPage.update(null, getTable().getSelectedObject());
        }
    }

    @Override
    protected JComponent createControl() {

        rootPanel = getComponentFactory().createPanel(new CardLayout());

        rootPanel.add(createRiepilogoLottiPanel(), CARD_RIEPILOGO_LOTTI);
        rootPanel.add(createNessunaGestioneLottiPanel(), CARD_NESSUNA_GESTIONE_LOTTI);

        tipiLottoCardLayout = new HashMap<>();
        tipiLottoCardLayout.put(TipoLotto.NESSUNO, CARD_NESSUNA_GESTIONE_LOTTI);
        tipiLottoCardLayout.put(TipoLotto.LOTTO, CARD_RIEPILOGO_LOTTI);
        tipiLottoCardLayout.put(TipoLotto.LOTTO_INTERNO, CARD_RIEPILOGO_LOTTI);

        return rootPanel;
    }

    /**
     * Crea il pannello che informa della gestione lotti non prevista per l'articolo corrente.
     *
     * @return pannello creato
     */
    private JPanel createNessunaGestioneLottiPanel() {

        final JPanel panel = getComponentFactory().createPanel(new BorderLayout());

        final JLabel label = new JLabel("GESTIONE LOTTI NON PREVISTA PER L'ARTICOLO");
        label.setIcon(RcpSupport.getIcon("attention.icon"));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(label, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Crea il pannello che contiene le tabelle della situazione e movimentazione dei lotti dell'articolo.
     *
     * @return pannello creato
     */
    private JPanel createRiepilogoLottiPanel() {

        final JPanel panel = getComponentFactory().createPanel(new BorderLayout());

        movimentazioneLottoPage = new MovimentazioneLottoPage();
        getTable().addSelectionObserver(movimentazioneLottoPage);

        final JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, super.createControl(),
                movimentazioneLottoPage.getControl());
        panel.add(splitPane, BorderLayout.CENTER);

        return panel;
    }

    @Override
    public AbstractCommand[] getCommands() {
        return new AbstractCommand[] { getStampaMovimentazioneLottiCommand(), getStampaEtichetteCommand(),
                getRefreshCommand() };
    }

    @Override
    public JComponent getHeaderControl() {
        final JPanel panel = getComponentFactory().createPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));

        final JRadioButton radioAperti = getComponentFactory().createRadioButton("Aperti");
        radioAperti.setSelected(true);
        radioAperti.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                applyStatoFilter(new StatoLotto[] { StatoLotto.APERTO });
            }
        });
        final JRadioButton radioChiusi = getComponentFactory().createRadioButton("Chiusi");
        radioChiusi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                applyStatoFilter(new StatoLotto[] { StatoLotto.CHIUSO });
            }
        });
        final JRadioButton radioTutti = getComponentFactory().createRadioButton("Tutti");
        radioTutti.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                applyStatoFilter(StatoLotto.values());
            }
        });

        final ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(radioAperti);
        buttonGroup.add(radioChiusi);
        buttonGroup.add(radioTutti);

        panel.add(new JLabel("Stati visualizzati:"));
        panel.add(radioAperti);
        panel.add(radioChiusi);
        panel.add(radioTutti);

        return panel;
    }

    /**
     * @return Returns the stampaEtichetteCommand.
     */
    public StampaEtichetteCommand getStampaEtichetteCommand() {
        if (stampaEtichetteCommand == null) {
            stampaEtichetteCommand = new StampaEtichetteCommand();
        }

        return stampaEtichetteCommand;
    }

    /**
     * @return Returns the stampaMovimentazioneLottiCommand.
     */
    private StampaMovimentazioneLottiCommand getStampaMovimentazioneLottiCommand() {
        if (stampaMovimentazioneLottiCommand == null) {
            stampaMovimentazioneLottiCommand = new StampaMovimentazioneLottiCommand();
            stampaMovimentazioneLottiCommand.addCommandInterceptor(new StampaMovimentazioneLottiCommandInterceptor());
        }

        return stampaMovimentazioneLottiCommand;
    }

    /**
     *
     * @return tableModel della pagina
     */
    StatisticheLottiArticoloTableModel getTableModel() {
        final StatisticheLottiArticoloTableModel tableModel = (StatisticheLottiArticoloTableModel) TableModelWrapperUtils
                .getActualTableModel(getTable().getTable().getModel());
        return tableModel;
    }

    @Override
    public List<StatisticaLotto> loadTableData() {
        List<StatisticaLotto> list = Collections.emptyList();

        if (articolo != null && articolo.getId() != null) {
            list = lottiBD.caricaSituazioneLotti(articolo.getArticoloLite());
        }

        // se impostato un lotto filtro i risultati per il codice lotto
        if (lotto != null) {
            CollectionUtils.filter(list, new Predicate() {

                @Override
                public boolean evaluate(Object arg0) {
                    return lotto.getCodice().equals(((StatisticaLotto) arg0).getLotto().getCodice());
                }
            });
        }

        return list;
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
        super.postSetFormObject(object);

    }

    @Override
    public void processTableData(Collection<StatisticaLotto> results) {
        super.processTableData(results);
        // npe da mail su riga 479.L'unica possibilità è che getTableModel sia null
        // forse sta chiudendo il form.
        if (articolo != null && getTableModel() != null) {
            final CardLayout cardLayout = (CardLayout) rootPanel.getLayout();
            cardLayout.show(rootPanel, tipiLottoCardLayout.get(articolo.getTipoLotto()));

            movimentazioneLottoPage.setTipoLotto(articolo.getTipoLotto());

            int[] aggCols = new int[] {};
            if (((AggregateTable) getTable().getTable()).getAggregateTableModel().hasAggregateColumns()) {
                aggCols = ((AggregateTable) getTable().getTable()).getAggregateTableModel().getAggregatedColumns();
            }

            ((AggregateTable) getTable().getTable()).getAggregateTableModel().setAggregatedColumns(new int[] {});
            ((AggregateTable) getTable().getTable()).getAggregateTableModel().aggregate();

            TableColumnChooser.showAllColumns(getTable().getTable());

            ((AggregateTable) getTable().getTable()).getAggregateTableModel().setAggregatedColumns(aggCols);

            ((AggregateTable) getTable().getTable()).getAggregateTableModel().aggregate();

            if (articolo != null) {
                if (articolo.getTipoLotto() != TipoLotto.LOTTO_INTERNO) {
                    TableColumnChooser.hideColumn(getTable().getTable(), 4);
                    ((AggregateTable) getTable().getTable()).getAggregateTableModel().removeAggregatedColumn(4);
                }
                getTableModel().setNumeroDecimaliQta(articolo.getNumeroDecimaliQta());
                movimentazioneLottoPage.setNumeroDecimaliQuantita(articolo.getNumeroDecimaliQta());
            } else {
                movimentazioneLottoPage.setNumeroDecimaliQuantita(2);
            }
        }
    }

    @Override
    public List<StatisticaLotto> refreshTableData() {
        return loadTableData();
    }

    @Override
    public void restoreState(Settings settings) {
        super.restoreState(settings);
        movimentazioneLottoPage.restoreState(settings);
    }

    @Override
    public void saveState(Settings settings) {
        super.saveState(settings);
        movimentazioneLottoPage.saveState(settings);
    }

    @Override
    public void setFormObject(Object object) {
        lotto = null;
        articolo = null;
        if (object instanceof Articolo) {
            articolo = (Articolo) object;
        } else if (object instanceof ParametriRicercaLotti) {
            if (((ParametriRicercaLotti) object).getArticolo() != null) {
                articolo = new Articolo();
                PanjeaSwingUtil.copyProperties(articolo, ((ParametriRicercaLotti) object).getArticolo());
            }
            lotto = ((ParametriRicercaLotti) object).getLotto();
        }
    }

    /**
     * @param lottiBD
     *            the lottiBD to set
     */
    public void setLottiBD(ILottiBD lottiBD) {
        this.lottiBD = lottiBD;
    }

}
