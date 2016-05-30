package it.eurotn.panjea.magazzino.rich.editors.documenti.spedizione;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Set;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;
import javax.swing.text.NumberFormatter;

import org.apache.commons.lang3.StringUtils;
import org.jdesktop.swingx.HorizontalLayout;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.support.ActionCommandInterceptorAdapter;
import org.springframework.richclient.settings.SettingsException;
import org.springframework.richclient.settings.SettingsManager;
import org.springframework.richclient.util.GuiStandardUtils;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.grid.CellRendererManager;

import it.eurotn.panjea.anagrafica.documenti.domain.IAreaDocumento;
import it.eurotn.panjea.anagrafica.domain.SedeAnagrafica.TipoSpedizioneDocumenti;
import it.eurotn.panjea.documenti.util.MovimentoSpedizioneDTO;
import it.eurotn.panjea.exceptions.PanjeaRuntimeException;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoDocumentoBD;
import it.eurotn.panjea.magazzino.rich.editors.documenti.spedizione.renderer.DatiMailCellRenderer;
import it.eurotn.panjea.magazzino.rich.editors.documenti.spedizione.renderer.EmailSpedizioneCellRenderer;
import it.eurotn.panjea.magazzino.rich.editors.documenti.spedizione.renderer.EsitoSpedizioneCellRenderer;
import it.eurotn.panjea.magazzino.rich.editors.documenti.spedizione.renderer.TipoLayoutRenderer;
import it.eurotn.panjea.rich.bd.ISicurezzaBD;
import it.eurotn.panjea.rich.bd.SicurezzaBD;
import it.eurotn.panjea.sicurezza.domain.DatiMail;
import it.eurotn.panjea.sicurezza.domain.Utente;
import it.eurotn.panjea.stampe.domain.LayoutStampa;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.editors.AbstractTablePageEditor;
import it.eurotn.rich.report.JecReport;
import it.eurotn.rich.report.ReportManager;
import it.eurotn.rich.report.StampaCommand;

public class SpedizioneMovimentiTablePage extends AbstractTablePageEditor<MovimentoSpedizioneDTO> {

    private class SpedizioneDocumentiCommandInterceptor extends ActionCommandInterceptorAdapter {

        @Override
        public void postExecution(ActionCommand command) {
            if (stampaEtichetteCheckBox.isSelected()) {
                getStampaEtichetteDocumentiCommand().execute();
            }
        }

        @Override
        public boolean preExecution(ActionCommand command) {
            command.addParameter(SpedisciMovimentiCommand.PARAM_TIPO_LAYOUT, getTipoLayout());
            try {
                settingsManager.getUserSettings().setInt("spedizioneSelezionata",
                        tipoLayoutComboBox.getSelectedIndex());
                settingsManager.getUserSettings().save();
            } catch (Exception e) {
                logger.error("-->errore nel salvare le settings per il tipoSpedizione", e);
                throw new PanjeaRuntimeException("-->errore nel salvare le settings per il tipoSpedizione", e);
            }
            return true;
        }
    }

    private class StampaEtichetteDocumentiCommand extends StampaCommand {

        public StampaEtichetteDocumentiCommand() {
            super("stampaEtichetteDocumentiCommand", null);
        }

        @Override
        protected void doExecuteCommand() {

            Map<Object, Object> parametri = getParametri();

            // eseguo il report solo se ci sono documenti di cui stampare le etichette
            if (!StringUtils.isBlank((CharSequence) stampantiComboBox.getSelectedItem())
                    && parametri.containsKey("idDocumenti")) {

                LayoutStampa layoutStampa = new LayoutStampa();
                layoutStampa.setBatch(false);
                layoutStampa.setFormulaNumeroCopie("1");
                layoutStampa.setPreview(false);
                layoutStampa.setReportName(getReportName());
                layoutStampa.setStampante((String) stampantiComboBox.getSelectedItem());

                JecReport report = new JecReport(getReportPath(), parametri);
                report.setReportName(getReportName());
                report.setParametriMail(getParametriMail());
                report.setLayoutStampa(layoutStampa);
                report.execute();
            }
        }

        @Override
        protected Map<Object, Object> getParametri() {

            // seleziono tutto perchè così la getSelectedObjects() mi restituisce i movimenti come
            // ordinati in griglia
            getTable().selectAll();
            List<MovimentoSpedizioneDTO> movimentiDaStampare = getTable().getSelectedObjects();
            getTable().unSelectAll();

            StringBuilder idDocs = new StringBuilder();
            String separator = "";
            for (MovimentoSpedizioneDTO movimento : movimentiDaStampare) {
                // creo le etichette dei soli documenti che vanno in stampa
                if (movimento.getTipoSpedizioneDocumenti() == TipoSpedizioneDocumenti.STAMPA) {
                    idDocs.append(separator);
                    idDocs.append(movimento.getAreaDocumento().getDocumento().getId());
                    separator = ",";
                }
            }

            HashMap<Object, Object> parametri = new HashMap<Object, Object>();
            parametri.put("numeroEtichettaIniziale", numeroPrimaEtichettaTextField.getValue());
            if (!idDocs.toString().isEmpty()) {
                parametri.put("idDocumenti", idDocs.toString());
            }
            parametri.put("utente", PanjeaSwingUtil.getUtenteCorrente().getUserName());
            return parametri;
        }

        @Override
        protected String getReportName() {
            return "Stampa etichette di spedizione documenti";
        }

        @Override
        protected String getReportPath() {
            return "Documenti/EtichetteSpedizione/" + layoutComboBox.getSelectedItem();
        }

    }

    private class TerminaSpedizioneCommand extends ActionCommand {

        /**
         * Costruttore.
         */
        public TerminaSpedizioneCommand() {
            super("terminaSpedizioneCommand");
            RcpSupport.configure(this);
        }

        @Override
        protected void doExecuteCommand() {
            getSpedisciMovimentiCommand().terminaSpedizione();
        }

    }

    private SettingsManager settingsManager;

    private SpedizioneMovimenti spedizioneMovimenti;

    private EditTemplateSpedizioneMovimentiCommand editTemplateSpedizioneMovimentiCommand;

    private SpedisciMovimentiCommand spedisciMovimentiCommand;
    private StampaEtichetteDocumentiCommand stampaEtichetteDocumentiCommand;
    private SpedizioneDocumentiCommandInterceptor spedizioneDocumentiCommandInterceptor;

    private TerminaSpedizioneCommand terminaSpedizioneCommand;
    private JComboBox<DatiMail> datiMailCombo = new JComboBox<DatiMail>();

    private JCheckBox stampaEtichetteCheckBox;

    private JPanel parametriStampaEtichettePanel;
    private JComboBox<String> stampantiComboBox;
    private JFormattedTextField numeroPrimaEtichettaTextField;
    private JComboBox<String> layoutComboBox;
    private IMagazzinoDocumentoBD magazzinoDocumentoBD;

    private ISicurezzaBD sicurezzaBD;
    private ReportManager reportManager;

    private JComboBox<TipoLayout> tipoLayoutComboBox;

    protected SpedizioneMovimentiTablePage() {
        super("spedizioneMovimentiTablePage", new SpedizioneMovimentiTableModel());

        // registro qui il renderer sull'esito di spedizione senza appesantire l'xml
        CellRendererManager.registerRenderer(String.class, new EsitoSpedizioneCellRenderer(),
                EsitoSpedizioneCellRenderer.ESITO_SPEDIZIONE_CONTEXT);
        CellRendererManager.registerRenderer(String.class, new EmailSpedizioneCellRenderer(),
                EmailSpedizioneCellRenderer.EMAIL_SPEDIZIONE_CONTEXT);
        datiMailCombo.setRenderer(new DatiMailCellRenderer());

        getTable().getTable().setDefaultEditor(DatiMail.class, new DefaultCellEditor(datiMailCombo));

        new EmailSpedizioneController(this);

        this.sicurezzaBD = RcpSupport.getBean(SicurezzaBD.BEAN_ID);
        reportManager = RcpSupport.getBean("reportManager");
    }

    private JPanel createParametriStampaEtichettePanel() {
        JPanel panel = getComponentFactory().createPanel(new HorizontalLayout(4));
        panel.setVisible(false);

        panel.add(new JLabel("Layout"));
        Set<String> etichetteReport = reportManager.listReport("Documenti/EtichetteSpedizione");
        layoutComboBox = new JComboBox<String>(etichetteReport.toArray(new String[etichetteReport.size()]));
        panel.add(layoutComboBox);

        panel.add(new JLabel("Numero posizione prima etichetta"));
        NumberFormat format = NumberFormat.getInstance();
        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setValueClass(Integer.class);
        formatter.setMinimum(0);
        formatter.setAllowsInvalid(false);
        formatter.setMaximum(100);
        // If you want the value to be committed on each keystroke instead of focus lost
        formatter.setCommitsOnValidEdit(true);
        numeroPrimaEtichettaTextField = new JFormattedTextField(formatter);
        numeroPrimaEtichettaTextField.setColumns(5);
        numeroPrimaEtichettaTextField.setHorizontalAlignment(JTextField.RIGHT);
        numeroPrimaEtichettaTextField.setValue(new Integer(1));
        panel.add(numeroPrimaEtichettaTextField);

        panel.add(new JLabel("Stampante"));
        List<String> printersName = getPrintersName();
        stampantiComboBox = new JComboBox<String>(printersName.toArray(new String[printersName.size()]));
        panel.add(stampantiComboBox);

        return panel;
    }

    @Override
    public void dispose() {
        editTemplateSpedizioneMovimentiCommand = null;
        stampaEtichetteDocumentiCommand = null;

        getSpedisciMovimentiCommand().removeCommandInterceptor(spedizioneDocumentiCommandInterceptor);
        spedizioneDocumentiCommandInterceptor = null;
        spedisciMovimentiCommand = null;

        super.dispose();
    }

    @Override
    public AbstractCommand[] getCommands() {
        return new AbstractCommand[] { getEditTemplateSpedizioneMovimentiCommand(), getRefreshCommand(),
                getSpedisciMovimentiCommand(), getTerminaSpedizioneCommand() };
    }

    /**
     * @return the editTemplateSpedizioneMovimentiCommand
     */
    private EditTemplateSpedizioneMovimentiCommand getEditTemplateSpedizioneMovimentiCommand() {
        if (editTemplateSpedizioneMovimentiCommand == null) {
            editTemplateSpedizioneMovimentiCommand = new EditTemplateSpedizioneMovimentiCommand();
        }

        return editTemplateSpedizioneMovimentiCommand;
    }

    @Override
    public JComponent getFooterControl() {
        JPanel footerPanel = getComponentFactory().createPanel(new BorderLayout(10, 5));

        parametriStampaEtichettePanel = createParametriStampaEtichettePanel();
        footerPanel.add(parametriStampaEtichettePanel, BorderLayout.CENTER);

        stampaEtichetteCheckBox = new JCheckBox("Stampa etichette");
        stampaEtichetteCheckBox.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent event) {
                parametriStampaEtichettePanel.setVisible(((JCheckBox) event.getSource()).isSelected());
            }
        });

        footerPanel.add(stampaEtichetteCheckBox, BorderLayout.WEST);

        GuiStandardUtils.attachBorder(footerPanel);

        return footerPanel;
    }

    @Override
    public JComponent getHeaderControl() {
        JPanel panel = getComponentFactory().createPanel(new FlowLayout(FlowLayout.RIGHT, 4, 0));
        panel.add(new JLabel("Layout"));
        tipoLayoutComboBox = new JComboBox<TipoLayout>(TipoLayout.values());
        tipoLayoutComboBox.setRenderer(new TipoLayoutRenderer());
        panel.add(tipoLayoutComboBox);
        panel.setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 10));
        return panel;
    }

    /**
     * Carica i nomi delle stampanti di sistema.
     *
     * @return nomi delle stampanti caricati
     */
    private List<String> getPrintersName() {

        PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);

        List<String> printers = new ArrayList<String>();
        printers.add("");
        for (PrintService printer : printServices) {
            printers.add(printer.getName());
        }

        return printers;
    }

    /**
     * @return the spedisciMovimentiCommand
     */
    private SpedisciMovimentiCommand getSpedisciMovimentiCommand() {
        if (spedisciMovimentiCommand == null) {
            spedisciMovimentiCommand = new SpedisciMovimentiCommand(getTable());
            spedizioneDocumentiCommandInterceptor = new SpedizioneDocumentiCommandInterceptor();
            spedisciMovimentiCommand.addCommandInterceptor(spedizioneDocumentiCommandInterceptor);
        }

        return spedisciMovimentiCommand;
    }

    /**
     * @return the stampaEtichetteDocumentiCommand
     */
    private StampaEtichetteDocumentiCommand getStampaEtichetteDocumentiCommand() {
        if (stampaEtichetteDocumentiCommand == null) {
            stampaEtichetteDocumentiCommand = new StampaEtichetteDocumentiCommand();
        }

        return stampaEtichetteDocumentiCommand;
    }

    /**
     * @return the terminaSpedizioneCommand
     */
    private TerminaSpedizioneCommand getTerminaSpedizioneCommand() {
        if (terminaSpedizioneCommand == null) {
            terminaSpedizioneCommand = new TerminaSpedizioneCommand();
        }

        return terminaSpedizioneCommand;
    }

    /**
     * @return the tipoLayout
     */
    public TipoLayout getTipoLayout() {
        return (TipoLayout) tipoLayoutComboBox.getSelectedItem();
    }

    @Override
    public Collection<MovimentoSpedizioneDTO> loadTableData() {
        return spedizioneMovimenti.getMovimenti();
    }

    @Override
    public void onPostPageOpen() {
    }

    @Override
    protected void onRefresh() {

        // devo forzare il commit altrimenti se una colonna è in modifica e non sono uscito prende
        // il valore vecchio
        TableCellEditor editor = getTable().getTable().getCellEditor();
        if (editor != null) {
            editor.stopCellEditing();
        }

        refreshData();
    }

    @Override
    public Collection<MovimentoSpedizioneDTO> refreshTableData() {
        Class<? extends IAreaDocumento> areaClass = null;
        List<Integer> idDocumenti = new ArrayList<Integer>();
        for (MovimentoSpedizioneDTO movimento : spedizioneMovimenti.getMovimenti()) {
            idDocumenti.add(movimento.getAreaDocumento().getDocumento().getId());
            areaClass = movimento.getAreaDocumento().getClass();
        }

        List<MovimentoSpedizioneDTO> movimentiPerSpedizione = magazzinoDocumentoBD
                .caricaMovimentiPerSpedizione(areaClass, idDocumenti);
        Utente utente = sicurezzaBD.caricaUtente(PanjeaSwingUtil.getUtenteCorrente().getUserName());

        spedizioneMovimenti.setUtente(utente);
        spedizioneMovimenti.setMovimenti(movimentiPerSpedizione);

        return loadTableData();
    }

    @Override
    public void setFormObject(Object object) {
        this.spedizioneMovimenti = (SpedizioneMovimenti) object;

        datiMailCombo.removeAllItems();
        // aggiungo tutti gli account email dell'utente per poterli scegliere nella colonna della
        // tabella
        for (DatiMail dati : spedizioneMovimenti.getUtente().getDatiMail()) {
            datiMailCombo.addItem(dati);
        }
        if (spedizioneMovimenti.getTipoLayout() == null) {
            try {
                tipoLayoutComboBox.setSelectedIndex(settingsManager.getUserSettings().getInt("spedizioneSelezionata"));
                System.out.println(settingsManager.getUserSettings().getInt("spedizioneSelezionata"));
            } catch (SettingsException e) {
                logger.warn("Errore nel ripristinare la scelta del tipo layout", e);
            }
        } else {
            tipoLayoutComboBox.setSelectedItem(spedizioneMovimenti.getTipoLayout());
        }
    }

    /**
     * @param magazzinoDocumentoBD
     *            the magazzinoDocumentoBD to set
     */
    public void setMagazzinoDocumentoBD(IMagazzinoDocumentoBD magazzinoDocumentoBD) {
        this.magazzinoDocumentoBD = magazzinoDocumentoBD;
    }

    /**
     * @param settingsManager
     *            settings manager
     */
    public void setSettingsManager(SettingsManager settingsManager) {
        this.settingsManager = settingsManager;
    }

    @Override
    public void update(Observable observable, Object obj) {
        super.update(observable, obj);
    }

}
