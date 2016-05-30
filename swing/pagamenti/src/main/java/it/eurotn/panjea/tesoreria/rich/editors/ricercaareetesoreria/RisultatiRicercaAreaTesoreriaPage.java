package it.eurotn.panjea.tesoreria.rich.editors.ricercaareetesoreria;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationEvent;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.ApplicationPage;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.ActionCommandInterceptor;
import org.springframework.richclient.core.Guarded;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.exception.GenericException;
import it.eurotn.panjea.rich.IEditorListener;
import it.eurotn.panjea.rich.PanjeaLifecycleApplicationEvent;
import it.eurotn.panjea.rich.pages.PanjeaDockingApplicationPage;
import it.eurotn.panjea.tesoreria.domain.AreaTesoreria;
import it.eurotn.panjea.tesoreria.domain.Effetto;
import it.eurotn.panjea.tesoreria.rich.bd.ITesoreriaBD;
import it.eurotn.panjea.tesoreria.rich.commands.EliminaAreaTesoreriaCommand;
import it.eurotn.panjea.tesoreria.service.exception.RapportoBancarioPerFlussoAssenteException;
import it.eurotn.panjea.tesoreria.util.ParametriRicercaAreeTesoreria;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.command.JECCommandGroup;
import it.eurotn.rich.command.ParametriRicercaCommand;
import it.eurotn.rich.editors.AbstractTablePageEditor;
import it.eurotn.rich.editors.IPageEditor;
import it.eurotn.rich.report.StampaCommand;

public class RisultatiRicercaAreaTesoreriaPage extends AbstractTablePageEditor<AreaTesoreria>
        implements InitializingBean, IEditorListener {

    /**
     * Command per lanciare la ricerca aree partite lanciando solo firePropertyChange(OBJECT_CHANGED,formObj).
     *
     * @author Leonardo
     */
    private class CercaAreeTesoreriaCommand extends ActionCommand implements Guarded {

        /**
         * Costruttore.
         */
        public CercaAreeTesoreriaCommand() {
            super("searchCommand");
            this.setSecurityControllerId(CERCA_AREE_TESORERIA_COMMAND);
            RcpSupport.configure(this);
        }

        @Override
        protected void doExecuteCommand() {
            parametriRicercaAreeTesoreria = (ParametriRicercaAreeTesoreria) form.getFormObject();
            parametriRicercaAreeTesoreria.setAreeTesoreria(null);
            parametriRicercaAreeTesoreria.setEffettuaRicerca(true);
            refreshData();
        }

    }

    private class EliminaAreaTesoreriaCommandInterceptor implements ActionCommandInterceptor {

        @Override
        public void postExecution(ActionCommand command) {

        }

        @Override
        public boolean preExecution(ActionCommand command) {
            List<AreaTesoreria> areeDaCancellare = RisultatiRicercaAreaTesoreriaPage.this.getTable()
                    .getSelectedObjects();
            ((EliminaAreaTesoreriaCommand) command).setAreeDaCancellare(areeDaCancellare);
            return true;
        }

    }

    private class ResetRicercaAreeTesorieriaCommand extends ActionCommand {

        /**
         * Costruttore.
         */
        public ResetRicercaAreeTesorieriaCommand() {
            super("resetParametriRicercaCommand");
            setSecurityControllerId(RESET_PARAMETRI_RICERCA_COMMAND);
            RcpSupport.configure(this);
        }

        @Override
        protected void doExecuteCommand() {
            parametriRicercaAreeTesoreria = new ParametriRicercaAreeTesoreria();
            form.getNewFormObjectCommand().execute();
            refreshData();
        }

    }

    private class SelectionObserver implements Observer {
        @Override
        public void update(Observable observable, Object obj) {
            // disabilito il cancella finchè non viene caricato tutto il documento
            boolean toggleEnableEditPage = obj != null;
            try {
                if (toggleEnableEditPage) {
                    getEditPages().get(obj.getClass().getName()).getEditorDeleteCommand().setEnabled(false);
                }

                RisultatiRicercaAreaTesoreriaPage.this
                        .firePropertyChange(RisultatiRicercaAreaTesoreriaPage.AREA_TESORERIA_SELEZIONATA, -1, obj);
            } finally {
                if (toggleEnableEditPage) {
                    getEditPages().get(obj.getClass().getName()).getEditorDeleteCommand().setEnabled(true);
                }
            }
        }
    }

    private class StampaDistinteSelezionateCommand extends StampaCommand {

        /**
         * Costruttore.
         */
        public StampaDistinteSelezionateCommand() {
            super(STAMPA_DISTINTE_SELEZIONATECOMMAND, STAMPA_DISTINTE_SELEZIONATECOMMAND);
        }

        @Override
        protected void doExecuteCommand() {
            try {
                tesoreriaBD.caricaEffettiDistintePerStampa(getParametri());
            } catch (RapportoBancarioPerFlussoAssenteException e1) {
                List<EntitaLite> entities = e1.getErrors();
                // devo lanciare l'apertura della search result entita' riempiendo la tabella con le entita' contenute
                // nell'exception
                if (!CollectionUtils.isEmpty(entities)) {
                    try {
                        ApplicationPage applicationPage = Application.instance().getActiveWindow().getPage();
                        ((PanjeaDockingApplicationPage) applicationPage)
                                .openResultView(entities.get(0).getClass().getName(), entities);
                    } catch (Exception e) {
                        logger.error("--> errore in createExceptionContent", e);
                    }
                }
                throw new GenericException(
                        "Rapporti bancari assenti. Controllare le entità presenti nei risultati della ricerca");
            }

            super.doExecuteCommand();
        }

        @Override
        protected Map<Object, Object> getParametri() {
            HashMap<Object, Object> parametri = new HashMap<Object, Object>();
            parametri.put("descAzienda", aziendaCorrente.getDenominazione());
            parametri.put("utente", PanjeaSwingUtil.getUtenteCorrente().getUserName());

            StringBuilder idAree = new StringBuilder();
            for (AreaTesoreria areaTesoreria : getTable().getSelectedObjects()) {
                if (!idAree.toString().isEmpty()) {
                    idAree.append(",");
                }

                idAree.append(areaTesoreria.getId());
            }

            parametri.put("idAree", idAree.toString());

            return parametri;
        }

        @Override
        protected String getReportName() {
            return "Stampa distinte effetti presentati";
        }

        @Override
        protected String getReportPath() {
            return "Tesoreria/DistinteEffettiPresentati";
        }

    }

    public static final String PAGE_ID = "risultatiRicercaAreaTesoreriaPage";

    private static final String CERCA_AREE_TESORERIA_COMMAND = PAGE_ID + ".cercaAreeTesoreriaCommand";
    private static final String RESET_PARAMETRI_RICERCA_COMMAND = PAGE_ID + ".resetParametriRicercaCommand";
    public static final String STAMPA_DISTINTE_SELEZIONATECOMMAND = "stampaDistinteSelezionateCommand";
    public static final String AREA_TESORERIA_SELEZIONATA = "areaTesoreriaSelezionata";
    private ParametriRicercaAreeTesoreria parametriRicercaAreeTesoreria;

    private AziendaCorrente aziendaCorrente;
    private ITesoreriaBD tesoreriaBD;
    private EliminaAreaTesoreriaCommand eliminaAreaTesoreriaCommand = null;

    private EliminaAreaTesoreriaCommandInterceptor eliminaAreaTesoreriaCommandInterceptor = null;
    private CercaAreeTesoreriaCommand cercaAreeTesoreriaCommand;
    private ResetRicercaAreeTesorieriaCommand resetRicercaAreeTesorieriaCommand;
    private StampaDistinteSelezionateCommand stampaDistinteSelezionateCommand;
    private ParametriRicercaCommand parametriRicercaCommand;
    private ParametriRicercaAreeTesoreriaForm form;

    private SelectionObserver selectionObserver;

    /**
     * Costruttore.
     */
    protected RisultatiRicercaAreaTesoreriaPage() {
        super(PAGE_ID, new RisultatiRicercaAreeTesoreriaTableModel(PAGE_ID));
        setShowTitlePane(false);
        getTable().setDelayForSelection(500);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        org.springframework.util.Assert.notNull(tesoreriaBD, "tesoreriaBD cannot be null!");

        selectionObserver = new SelectionObserver();
        getTable().addSelectionObserver(selectionObserver);
    }

    /**
     * Carica le aree tesoreria in base ai parametri specificati.
     *
     * @return lista di aree tesoreria trovate
     */
    private List<AreaTesoreria> getAreeTesoreria() {
        logger.debug("--> Enter getAreeTesoreria");
        List<AreaTesoreria> areeTesoreria = new ArrayList<AreaTesoreria>();

        if (this.parametriRicercaAreeTesoreria != null && this.parametriRicercaAreeTesoreria.isEffettuaRicerca()) {
            if (parametriRicercaAreeTesoreria.getAreeTesoreria() != null
                    && !parametriRicercaAreeTesoreria.getAreeTesoreria().isEmpty()) {
                form.getNewFormObjectCommand().execute();
                areeTesoreria = parametriRicercaAreeTesoreria.getAreeTesoreria();
            } else {
                areeTesoreria = tesoreriaBD.ricercaAreeTesorerie(parametriRicercaAreeTesoreria);
            }
        }

        logger.debug("--> Exit getAreeTesoreria");
        return areeTesoreria;
    }

    /**
     * @return the cercaAreeTesoreriaCommand
     */
    public CercaAreeTesoreriaCommand getCercaAreeTesoreriaCommand() {
        if (cercaAreeTesoreriaCommand == null) {
            cercaAreeTesoreriaCommand = new CercaAreeTesoreriaCommand();
        }

        return cercaAreeTesoreriaCommand;
    }

    @Override
    public AbstractCommand[] getCommands() {
        return new AbstractCommand[] { getStampaDistinteSelezionateCommand(), getRefreshCommand() };
    }

    /**
     * @return the deleteAreeTesoreriaCommand
     */
    @Override
    public ActionCommand getEditorDeleteCommand() {
        if (eliminaAreaTesoreriaCommand == null) {
            eliminaAreaTesoreriaCommand = new EliminaAreaTesoreriaCommand();
            eliminaAreaTesoreriaCommand.setTesoreriaBD(tesoreriaBD);
            eliminaAreaTesoreriaCommand.addCommandInterceptor(getEliminaAreaTesoreriaCommandInterceptor());
        }
        return eliminaAreaTesoreriaCommand;
    }

    /**
     * @return the EliminaAreaTesoreriaCommandInterceptor to get
     */
    public EliminaAreaTesoreriaCommandInterceptor getEliminaAreaTesoreriaCommandInterceptor() {
        if (eliminaAreaTesoreriaCommandInterceptor == null) {
            eliminaAreaTesoreriaCommandInterceptor = new EliminaAreaTesoreriaCommandInterceptor();
        }
        return eliminaAreaTesoreriaCommandInterceptor;
    }

    @Override
    public JComponent getHeaderControl() {
        JPanel rootPanel = getComponentFactory().createPanel(new BorderLayout(0, 10));

        form = new ParametriRicercaAreeTesoreriaForm(aziendaCorrente);
        rootPanel.add(form.getControl(), BorderLayout.CENTER);

        JECCommandGroup commandGroup = new JECCommandGroup();
        commandGroup.add(getParametriRicercaCommand());
        commandGroup.add(getResetRicercaAreeTesorieriaCommand());
        commandGroup.add(getCercaAreeTesoreriaCommand());
        rootPanel.add(commandGroup.createToolBar(), BorderLayout.NORTH);

        return rootPanel;
    }

    /**
     *
     * @return resetParametriRicercaCommand
     */
    protected ParametriRicercaCommand getParametriRicercaCommand() {
        if (parametriRicercaCommand == null) {
            parametriRicercaCommand = new ParametriRicercaCommand(form.getFormModel(), this);
        }
        return parametriRicercaCommand;
    }

    /**
     * @return the resetRicercaAreeTesorieriaCommand
     */
    public ResetRicercaAreeTesorieriaCommand getResetRicercaAreeTesorieriaCommand() {
        if (resetRicercaAreeTesorieriaCommand == null) {
            resetRicercaAreeTesorieriaCommand = new ResetRicercaAreeTesorieriaCommand();
        }
        return resetRicercaAreeTesorieriaCommand;
    }

    /**
     * @return Returns the stampaDistinteSelezionateCommand.
     */
    public StampaDistinteSelezionateCommand getStampaDistinteSelezionateCommand() {
        if (stampaDistinteSelezionateCommand == null) {
            stampaDistinteSelezionateCommand = new StampaDistinteSelezionateCommand();
        }

        return stampaDistinteSelezionateCommand;
    }

    @Override
    public void grabFocus() {
        form.grabFocus();
    }

    @Override
    public Collection<AreaTesoreria> loadTableData() {
        return getAreeTesoreria();
    }

    @Override
    public void onEditorEvent(ApplicationEvent event) {

        // sovrascrivo la onEditorEvent perchè altrimenti la super mi cancella
        // l'area solo se la classe è AreaTesoreria.
        Object object = event.getSource();

        if (object instanceof AreaTesoreria) {
            AreaTesoreria areaTesoreria = new AreaTesoreria();
            areaTesoreria.setId(((AreaTesoreria) object).getId());
            areaTesoreria.setVersion(((AreaTesoreria) object).getVersion());

            ApplicationEvent event2 = new PanjeaLifecycleApplicationEvent(LifecycleApplicationEvent.DELETED,
                    areaTesoreria);
            super.onEditorEvent(event2);
        } else if (object instanceof Effetto) {

            Effetto effetto = (Effetto) object;

            if (effetto.getAreaEffetti().getEffetti().size() > 1) {
                AreaTesoreria areaTesoreria = tesoreriaBD.caricaAreaTesoreria(effetto.getAreaEffetti());
                getTable().replaceOrAddRowObject(effetto.getAreaEffetti(), areaTesoreria, null);
                getTable().selectRowObject(areaTesoreria, null);
            } else {
                getTable().removeRowObject(effetto.getAreaEffetti());
            }
        } else {
            super.onEditorEvent(event);
        }
    }

    @Override
    public void onPostPageOpen() {

    }

    @Override
    public boolean onPrePageOpen() {
        return true;
    }

    @Override
    public Collection<AreaTesoreria> refreshTableData() {
        return loadTableData();
    }

    /**
     * @param aziendaCorrente
     *            the aziendaCorrente to set
     */
    public void setAziendaCorrente(AziendaCorrente aziendaCorrente) {
        this.aziendaCorrente = aziendaCorrente;
    }

    @Override
    public void setEditPages(Map<String, IPageEditor> editPages) {
        super.setEditPages(editPages);
        // setto a null il property command executor perchè non voglio che con il doppio click o
        // l'invio venga portata
        // in modifica la page dell'area selezionata in tabella.
        getTable().setPropertyCommandExecutor(null);
    }

    @Override
    public void setFormObject(Object object) {
        this.parametriRicercaAreeTesoreria = new ParametriRicercaAreeTesoreria();

        if (object instanceof ParametriRicercaAreeTesoreria) {
            this.parametriRicercaAreeTesoreria = (ParametriRicercaAreeTesoreria) object;
        } else if (object instanceof AreaTesoreria) {
            this.parametriRicercaAreeTesoreria.setAreeTesoreria(new ArrayList<AreaTesoreria>());
            this.parametriRicercaAreeTesoreria.getAreeTesoreria().add((AreaTesoreria) object);
            this.parametriRicercaAreeTesoreria.setEffettuaRicerca(true);
        }
    }

    /**
     * @param tesoreriaBD
     *            the tesoreriaBD to set
     */
    public void setTesoreriaBD(ITesoreriaBD tesoreriaBD) {
        this.tesoreriaBD = tesoreriaBD;
    }
}
