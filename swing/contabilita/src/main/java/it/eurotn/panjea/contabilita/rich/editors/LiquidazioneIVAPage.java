/**
 *
 */
package it.eurotn.panjea.contabilita.rich.editors;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationEvent;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.ActionCommandExecutor;
import org.springframework.richclient.command.ActionCommandInterceptor;
import org.springframework.richclient.command.CommandGroup;
import org.springframework.richclient.dialog.AbstractDialogPage;
import org.springframework.richclient.dialog.MessageDialog;
import org.springframework.richclient.settings.Settings;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.grid.SortableTable;
import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.contabilita.domain.ContabilitaSettings;
import it.eurotn.panjea.contabilita.domain.TipoAreaContabile;
import it.eurotn.panjea.contabilita.domain.TipoDocumentoBase.TipoOperazioneTipoDocumento;
import it.eurotn.panjea.contabilita.rich.bd.IContabilitaAnagraficaBD;
import it.eurotn.panjea.contabilita.rich.bd.IContabilitaBD;
import it.eurotn.panjea.contabilita.rich.commands.GeneraDocumentoLiquidazioneCommand;
import it.eurotn.panjea.contabilita.rich.commands.ReportLiquidazioneCommand;
import it.eurotn.panjea.contabilita.rich.pm.LiquidazionePM;
import it.eurotn.panjea.contabilita.util.AreaContabileDTO;
import it.eurotn.panjea.contabilita.util.AreaContabileFullDTO;
import it.eurotn.panjea.contabilita.util.RegistroLiquidazioneDTO;
import it.eurotn.panjea.rich.IEditorListener;
import it.eurotn.panjea.rich.PanjeaLifecycleApplicationEvent;
import it.eurotn.rich.control.table.JideTableWidget;
import it.eurotn.rich.editors.IEditorCommands;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;

/**
 * Consente la selezione del periodo della liquidazione iva in base alla tipologia scelta nelle
 * preferenze della contabilità.
 *
 * Non derivata da una formBacked perchè non mi serve il form (i controlli variano da
 * tipoPeriodicità)
 *
 * @author fattazzo
 * @version 1.0, 18/dic/07
 *
 */
public class LiquidazioneIVAPage extends AbstractDialogPage
        implements IPageLifecycleAdvisor, IEditorCommands, InitializingBean, IEditorListener {

    private class AnnoChangeListener implements ChangeListener {

        @Override
        public void stateChanged(ChangeEvent e) {
            updateTable();
            generaDocumentoLiquidazioneCommand.setAnno((Integer) spinnerAnno.getValue());
        }
    }

    private class GeneraDocumentoLiquidazioneCommandInterceptor implements ActionCommandInterceptor {

        @Override
        public void postExecution(ActionCommand arg0) {
            updateTable();
        }

        @Override
        public boolean preExecution(ActionCommand arg0) {
            generaDocumentoLiquidazioneCommand.setAnno((Integer) spinnerAnno.getValue());
            if (table.getSelectedObject() != null) {
                if ((table.getSelectedObject()).getRegistroLiquidazioneDTO().getAreaContabileDTO()
                        .getNumeroProtocollo() != null) {
                    // Ho gia un documento quindi non faccio nulla. Inserire
                    // un messaggio di avviso
                    return false;
                } else {
                    generaDocumentoLiquidazioneCommand.setDataDocumento(getDataFinePeriodo());
                }
            } else {
                return false;
            }
            return true;
        }
    }

    private class LiquidazioneTableCommandExecutor implements ActionCommandExecutor {

        @Override
        public void execute() {
            AreaContabileDTO areaContabileDTO = (table.getSelectedObject()).getRegistroLiquidazioneDTO()
                    .getAreaContabileDTO();
            if (areaContabileDTO.getNumeroDocumento() != null) {
                AreaContabileFullDTO areaContabileFullDTO = contabilitaBD
                        .caricaAreaContabileFullDTO(areaContabileDTO.getId());
                LifecycleApplicationEvent event = new OpenEditorEvent(areaContabileFullDTO);
                Application.instance().getApplicationContext().publishEvent(event);
            }
        }
    }

    private class LiquidazioneTableSelectionObserver implements Observer {

        @Override
        public void update(Observable observable, Object obj) {

            if (obj != null) {
                LiquidazionePM liquidazionePM = (LiquidazionePM) obj;
                AreaContabileDTO areaContabileDTO = liquidazionePM.getRegistroLiquidazioneDTO().getAreaContabileDTO();
                boolean isGenerazioneDocumentoEnabled = areaContabileDTO.getNumeroDocumento() == null;
                boolean isAnnuale = liquidazionePM.getPeriodo().equals(LiquidazioneIVAPage.LABEL_ANNUALE);
                generaDocumentoLiquidazioneCommand.setEnabled(isGenerazioneDocumentoEnabled && !isAnnuale);
            }
        }
    }

    /**
     * RefrshCommand.
     */
    private class RefreshCommand extends ActionCommand {

        /**
         * Costruttore.
         */
        private RefreshCommand() {
            super("refreshCommand");
            RcpSupport.configure(this);
        }

        @Override
        protected void doExecuteCommand() {
            updateTable();
        }

        @Override
        protected void onButtonAttached(AbstractButton button) {
            super.onButtonAttached(button);
            button.setName(PAGE_ID + "." + "refreshCommand");
        }
    }

    private class ReportLiquidazioneCommandInterceptor implements ActionCommandInterceptor {

        @Override
        public void postExecution(ActionCommand arg0) {
        }

        @Override
        public boolean preExecution(ActionCommand arg0) {
            reportLiquidazioneCommand.setAnno((Integer) spinnerAnno.getValue());
            if (table.getSelectedObject() != null) {
                String periodo = (table.getSelectedObject()).getPeriodo();
                reportLiquidazioneCommand.setPeriodo(periodo);
                reportLiquidazioneCommand.setDataFine(getDataFinePeriodo());
                reportLiquidazioneCommand.setDataInizio(getDataInizioPeriodo());
            } else {
                return false;
            }
            return true;
        }
    }

    private class ReportLiquidazioneDefinitivaCommandInterceptor implements ActionCommandInterceptor {

        @Override
        public void postExecution(ActionCommand command) {
            // Aggiorno per visualizzare il registro iva creato
            updateTable();
        }

        @Override
        public boolean preExecution(ActionCommand command) {
            ReportLiquidazioneCommand reportCommand = (ReportLiquidazioneCommand) command;
            reportCommand.setAnno((Integer) spinnerAnno.getValue());
            if (table.getSelectedObject() != null) {
                String periodo = (table.getSelectedObject()).getPeriodo();
                reportCommand.setPeriodo(periodo);
                reportCommand.setDataFine(getDataFinePeriodo());
                reportCommand.setDataInizio(getDataInizioPeriodo());
                reportCommand.setGiornaleIva((table.getSelectedObject()).getRegistroLiquidazioneDTO().getGiornaleIva());
            }
            return true;
        }
    }

    private static Logger logger = Logger.getLogger(LiquidazioneIVAPage.class);
    public static final String PAGE_ID = "liquidazioneIVAPage";
    public static final String PANEL_TABLE_TITLE = PAGE_ID + ".table.title";
    public static final String ANNO = "anno.label";
    public static final String LABEL_ANNUALE = "Annuale";

    private ContabilitaSettings contabilitaSettings = null;
    private JSpinner spinnerAnno;
    private JideTableWidget<LiquidazionePM> table;

    private IContabilitaBD contabilitaBD;
    private IContabilitaAnagraficaBD contabilitaAnagraficaBD;
    private AziendaCorrente aziendaCorrente;
    private ReportLiquidazioneCommand reportLiquidazioneCommand = null;
    private ReportLiquidazioneCommand reportLiquidazioneDefinitivaCommand = null;
    private GeneraDocumentoLiquidazioneCommand generaDocumentoLiquidazioneCommand = null;

    private GeneraDocumentoLiquidazioneCommandInterceptor generaDocumentoLiquidazioneCommandInterceptor = null;

    private ReportLiquidazioneCommandInterceptor reportLiquidazioneCommandInterceptor = null;

    private ReportLiquidazioneDefinitivaCommandInterceptor reportLiquidazioneDefinitivaCommandInterceptor = null;

    /*
     * public String getPeriodicitaLabel() { switch (contabilitaSettings.getTipoPeriodicita()) {
     * case ANNUALE: return getAnnoPeriodicitaLabel(); case MENSILE: return
     * comboBoxMensile.getSelectedItem().toString() + " " + getAnnoPeriodicitaLabel(); case
     * TRIMESTRALE: return comboBoxTrimestrale.getSelectedItem().toString() + " " +
     * getAnnoPeriodicitaLabel(); }
     * 
     * return ""; }
     */

    private AnnoChangeListener annoChangeListener = null;

    private LiquidazioneTableSelectionObserver liquidazioneTableSelectionObserver = null;

    private LiquidazioneTableCommandExecutor liquidazioneTableCommandExecutor = null;

    /**
     * Default constructor.
     */
    public LiquidazioneIVAPage() {
        super(PAGE_ID);
        creaReportLiquidazioneCommand();
        creaReportLiquidazioneDefinitivaCommand();
        creaDocumentoLiquidazioneCommand();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        org.springframework.util.Assert.notNull(contabilitaBD);
        org.springframework.util.Assert.notNull(aziendaCorrente);
        org.springframework.util.Assert.notNull(contabilitaAnagraficaBD);
        contabilitaSettings = contabilitaAnagraficaBD.caricaContabilitaSettings();
    }

    /**
     * Creo il command e aggiunge il listener sulla execute per imposta i parametri per
     * l'esecuzione.
     */
    private void creaDocumentoLiquidazioneCommand() {
        generaDocumentoLiquidazioneCommand = (GeneraDocumentoLiquidazioneCommand) getActiveWindow().getCommandManager()
                .getCommand("generaDocumentoLiquidazioneCommand");
        generaDocumentoLiquidazioneCommandInterceptor = new GeneraDocumentoLiquidazioneCommandInterceptor();
        generaDocumentoLiquidazioneCommand.addCommandInterceptor(generaDocumentoLiquidazioneCommandInterceptor);
    }

    /**
     * Creo il command e aggiunge il listener sulla execute per imposta i parametri per
     * l'esecuzione.
     */
    private void creaReportLiquidazioneCommand() {
        reportLiquidazioneCommand = (ReportLiquidazioneCommand) getActiveWindow().getCommandManager()
                .getCommand("reportLiquidazioneCommand");
        reportLiquidazioneCommandInterceptor = new ReportLiquidazioneCommandInterceptor();
        reportLiquidazioneCommand.addCommandInterceptor(reportLiquidazioneCommandInterceptor);
    }

    /**
     * Crea command per la stampa definitiva della liquidazione.
     */
    private void creaReportLiquidazioneDefinitivaCommand() {
        reportLiquidazioneDefinitivaCommand = (ReportLiquidazioneCommand) getActiveWindow().getCommandManager()
                .getCommand("reportLiquidazioneDefinitivaCommand");
        reportLiquidazioneDefinitivaCommandInterceptor = new ReportLiquidazioneDefinitivaCommandInterceptor();
        reportLiquidazioneDefinitivaCommand.addCommandInterceptor(reportLiquidazioneDefinitivaCommandInterceptor);
    }

    /**
     * Crea la button bar con i commands di stampa porvvisoria e definitiva e il command per la
     * generazione del documento di liquidazione.
     * 
     * @return JComponent
     */
    private JComponent createButtonBar() {
        JPanel panel = new JPanel(new BorderLayout());

        CommandGroup commandGroup = new CommandGroup();

        commandGroup.add(reportLiquidazioneCommand);
        commandGroup.add(reportLiquidazioneDefinitivaCommand);
        commandGroup.add(generaDocumentoLiquidazioneCommand);

        RefreshCommand refreshCommand = new RefreshCommand();
        commandGroup.add(refreshCommand);

        panel.add(commandGroup.createButtonBar(), BorderLayout.EAST);

        return panel;
    }

    @Override
    protected JComponent createControl() {
        logger.debug("--> Enter createControl");

        JPanel panel = new JPanel(new BorderLayout());
        JPanel panelData = new JPanel(new BorderLayout());
        panel.add(panelData, BorderLayout.NORTH);
        panelData.add(createHeaderControl(), BorderLayout.NORTH);
        panelData.add(createButtonBar(), BorderLayout.SOUTH);
        JPanel panelTable = new JPanel(new BorderLayout());
        panelTable.add(createTable(), BorderLayout.CENTER);
        panelData.add(panelTable, BorderLayout.CENTER);
        Border padding = BorderFactory.createEmptyBorder(20, 20, 5, 20);
        panel.setBorder(padding);

        logger.debug("--> Exit createControl");
        return panel;
    }

    /**
     * Crea i controlli per la scelta della liquidazione da stampare.
     * 
     * @return JComponent
     */
    private JComponent createHeaderControl() {
        JPanel panel = new JPanel(new BorderLayout());

        panel.add(new JLabel(getMessageSource().getMessage(ANNO, new Object[] {}, Locale.getDefault())),
                BorderLayout.WEST);

        SpinnerNumberModel model = new SpinnerNumberModel(new Integer(aziendaCorrente.getAnnoContabile()),
                new Integer(0), new Integer(aziendaCorrente.getAnnoContabile() + 100), new Integer(1));
        spinnerAnno = new JSpinner(model);
        annoChangeListener = new AnnoChangeListener();
        spinnerAnno.addChangeListener(annoChangeListener);

        JPanel panelSpinner = new JPanel(new BorderLayout());
        panelSpinner.add(spinnerAnno, BorderLayout.WEST);

        panel.add(panelSpinner, BorderLayout.CENTER);

        Border padding = BorderFactory.createEmptyBorder(0, 0, 5, 0);
        panel.setBorder(padding);

        return panel;
    }

    /**
     * Crea la tabella di scelta del mese da stampare.
     * 
     * @return Component
     */
    private Component createTable() {
        // Inserisco nelle colonne registroLiquidazioneDTO.areaContabileDTO
        // perche' installo il rendere sulla colonna
        // per visualizzarne il numero ed un pulsante per aprirla (se presente)
        table = new JideTableWidget<LiquidazionePM>(PAGE_ID + ".Table",
                new String[] { "periodo", "registroLiquidazioneDTO.giornaleIva.numeroPagina",
                        "registroLiquidazioneDTO.areaContabileDTO.numeroDocumento",
                        "registroLiquidazioneDTO.areaContabileDTO.dataDocumento", "statoGiornale" },
                LiquidazionePM.class);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(getMessage(PANEL_TABLE_TITLE)));
        panel.add(table.getComponent(), BorderLayout.CENTER);

        // blocco il sort, filter e ReorderingAllowed
        ((SortableTable) table.getTable()).setSortable(false);
        table.getTable().getTableHeader().setReorderingAllowed(false);
        liquidazioneTableSelectionObserver = new LiquidazioneTableSelectionObserver();
        table.addSelectionObserver(liquidazioneTableSelectionObserver);
        liquidazioneTableCommandExecutor = new LiquidazioneTableCommandExecutor();
        table.setPropertyCommandExecutor(liquidazioneTableCommandExecutor);
        return panel;
    }

    @Override
    public void dispose() {
        if (generaDocumentoLiquidazioneCommand != null) {
            generaDocumentoLiquidazioneCommand.removeCommandInterceptor(generaDocumentoLiquidazioneCommandInterceptor);
        }
        generaDocumentoLiquidazioneCommand = null;
        if (reportLiquidazioneCommand != null) {
            reportLiquidazioneCommand.removeCommandInterceptor(reportLiquidazioneCommandInterceptor);
        }
        reportLiquidazioneCommand = null;
        if (reportLiquidazioneDefinitivaCommand != null) {
            reportLiquidazioneDefinitivaCommand.addCommandInterceptor(reportLiquidazioneDefinitivaCommandInterceptor);
        }
        reportLiquidazioneDefinitivaCommand = null;
        if (spinnerAnno != null) {
            spinnerAnno.removeChangeListener(annoChangeListener);
        }
        table.removeSelectionObserver(liquidazioneTableSelectionObserver);
        table.dispose();
        table = null;
    }

    /**
     * @return the aziendaCorrente
     */
    public AziendaCorrente getAziendaCorrente() {
        return aziendaCorrente;
    }

    /**
     * @return the contabilitaAnagraficaBD
     */
    public IContabilitaAnagraficaBD getContabilitaAnagraficaBD() {
        return contabilitaAnagraficaBD;
    }

    /**
     * @return the contabilitaBD
     */
    public IContabilitaBD getContabilitaBD() {
        return contabilitaBD;
    }

    /**
     * Restituisce in base al periodo selezionato la data di fine.
     * 
     * @return data fine periodo di selezione
     */
    public Date getDataFinePeriodo() {
        logger.debug("--> Enter getDataFinePeriodo");

        int anno = (Integer) spinnerAnno.getValue();
        int mese = -1;
        int selectdRow = table.getTable().getSelectedRow();
        switch (contabilitaSettings.getTipoPeriodicita()) {
        case MENSILE:
            if (selectdRow < 12) {
                mese = table.getTable().getSelectedRow();
            }
            break;
        case TRIMESTRALE:
            switch (selectdRow) {
            case 0:
                mese = 2;
                break;
            case 1:
                mese = 5;
                break;
            case 2:
                mese = 8;
                break;
            case 3:
                mese = 11;
                break;
            }
            break;
        }

        // i mesi sono da 0 a 11;ma nella lista ho un elemento in più che è il riepilogo annuale
        // esso non rientra nelle condizioni che precedono e quindi associo 11 (dicembre) in modo da
        // associare l'ultimo mese dell'anno alla data di fine periodo
        if (mese == -1) {
            mese = 11;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.set(anno, mese, 1);

        int lastDayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        calendar.set(Calendar.DAY_OF_MONTH, lastDayOfMonth);

        logger.debug("--> Exit getDataFinePeriodo");
        return calendar.getTime();
    }

    /**
     * Restituisce in base al periodo selezionato la data di inizio.
     * 
     * @return data inizio periodo di selezione
     */
    public Date getDataInizioPeriodo() {
        logger.debug("--> Enter getDataInizioPeriodo");

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, (Integer) spinnerAnno.getValue());
        calendar.set(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        // i mesi sono da 0 a 11;ma nella lista ho un elemento in più che è il riepilogo annuale
        // esso non rientra nelle condizioni che seguono e quindi rimane con valore di default (1
        // gennaio)
        int selectedRow = table.getTable().getSelectedRow();

        switch (contabilitaSettings.getTipoPeriodicita()) {
        case MENSILE:
            if (selectedRow < 12) {
                calendar.set(Calendar.MONTH, selectedRow);
            }
            break;
        case TRIMESTRALE:
            switch (selectedRow) {
            case 0:
                calendar.set(Calendar.MONTH, 0);
                break;
            case 1:
                calendar.set(Calendar.MONTH, 3);
                break;
            case 2:
                calendar.set(Calendar.MONTH, 6);
                break;
            case 3:
                calendar.set(Calendar.MONTH, 9);
                break;
            }

            break;
        }

        logger.debug("--> Exit getDataInizioPeriodo");
        return calendar.getTime();
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

    @Override
    public void loadData() {
    }

    @Override
    public void onEditorEvent(ApplicationEvent event) {
        if (event instanceof PanjeaLifecycleApplicationEvent) {
            String evtName = ((PanjeaLifecycleApplicationEvent) event).getEventType();
            if (LifecycleApplicationEvent.DELETED.equals(evtName)) {
                Object object = event.getSource();
                if (object instanceof AreaContabileFullDTO) {
                    updateTable();
                }
            }
        }
    }

    @Override
    public void onPostPageOpen() {
        updateTable();
    }

    @Override
    public boolean onPrePageOpen() {
        // HACK nella onpostPageOpen vengono caricati i Registri Liquidazione,
        // se non configurato
        // il tipoDocumentoBase per liquidazione
        // viene rilanciata la TipoDocumentoBaseException che viene intercettato
        // dal try catch del createControl
        // di AbstractEditorDialogPage bloccando quindi la normale gestione
        // delle eccezioni, espongo all'utente
        // il messaggio
        Map<TipoOperazioneTipoDocumento, TipoAreaContabile> tipiAreaTipoOperaz = contabilitaAnagraficaBD
                .caricaTipiOperazione();
        if (tipiAreaTipoOperaz.get(TipoOperazioneTipoDocumento.LIQUIDAZIONE_IVA) == null) {
            new MessageDialog("ATTENZIONE", "TipoDocumentoBase per liquidazione non configurato").showDialog();
            return false;
        }
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

    /**
     * @param aziendaCorrente
     *            the aziendaCorrente to set
     */
    public void setAziendaCorrente(AziendaCorrente aziendaCorrente) {
        this.aziendaCorrente = aziendaCorrente;
    }

    /**
     * @param contabilitaAnagraficaBD
     *            the contabilitaAnagraficaBD to set
     */
    public void setContabilitaAnagraficaBD(IContabilitaAnagraficaBD contabilitaAnagraficaBD) {
        this.contabilitaAnagraficaBD = contabilitaAnagraficaBD;
    }

    /**
     * @param contabilitaBD
     *            the contabilitaBD to set
     */
    public void setContabilitaBD(IContabilitaBD contabilitaBD) {
        this.contabilitaBD = contabilitaBD;
    }

    @Override
    public void setFormObject(Object object) {
    }

    @Override
    public void setReadOnly(boolean readOnly) {
    }

    /**
     * Aggiorna la visualizzazione della tabella per la selezione del periodo per cui stampare la
     * liquidazione.
     */
    private void updateTable() {
        List<RegistroLiquidazioneDTO> registriLiquidazioniDTO = contabilitaBD
                .caricaRegistriLiquidazione((Integer) spinnerAnno.getValue());
        List<LiquidazionePM> liquidazioniPM = new ArrayList<LiquidazionePM>();

        switch (contabilitaSettings.getTipoPeriodicita()) {
        case MENSILE:

            for (int i = 0; i < 12; i++) {
                String mese = getMessageSource().getMessage("mese." + (i + 1), new Object[] {}, Locale.getDefault());
                LiquidazionePM liquidazionePM = new LiquidazionePM(mese, registriLiquidazioniDTO.get(i));
                liquidazioniPM.add(liquidazionePM);
            }
            break;
        case TRIMESTRALE:
            for (int i = 0; i < 12; i += 3) {
                String meseIniziale = getMessageSource().getMessage("mese." + (i + 1), new Object[] {},
                        Locale.getDefault());
                String meseFinale = getMessageSource().getMessage("mese." + (i + 3), new Object[] {},
                        Locale.getDefault());

                LiquidazionePM liquidazionePM = new LiquidazionePM(meseIniziale + " - " + meseFinale,
                        registriLiquidazioniDTO.get(i));
                liquidazioniPM.add(liquidazionePM);
            }
            break;
        }
        LiquidazionePM liquidazionePM = new LiquidazionePM(LiquidazioneIVAPage.LABEL_ANNUALE,
                registriLiquidazioniDTO.get(12));
        liquidazioniPM.add(liquidazionePM);
        table.setRows(liquidazioniPM);
    }

}
