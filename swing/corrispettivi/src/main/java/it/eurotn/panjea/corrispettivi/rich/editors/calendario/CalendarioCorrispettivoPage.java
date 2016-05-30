package it.eurotn.panjea.corrispettivi.rich.editors.calendario;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.dialog.AbstractDialogPage;
import org.springframework.richclient.settings.Settings;
import org.springframework.richclient.util.GuiStandardUtils;
import org.springframework.richclient.util.RcpSupport;
import org.springframework.rules.closure.Closure;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.rich.factory.list.TipoDocumentoCellRenderer;
import it.eurotn.panjea.corrispettivi.domain.CalendarioCorrispettivo;
import it.eurotn.panjea.corrispettivi.rich.bd.ICorrispettiviBD;
import it.eurotn.panjea.plugin.PluginManager;
import it.eurotn.panjea.rich.dialogs.LoadingDialog;
import it.eurotn.rich.editors.IEditorCommands;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;

/**
 *
 * @author Fattazzo <g.fattarsi@eurotn.it>
 */
public class CalendarioCorrispettivoPage extends AbstractDialogPage implements IPageLifecycleAdvisor, IEditorCommands {

    /**
     * Carica il calendario in base ai parametri selezionati nella pagina.
     */
    private class CaricaCalendarioActionCommand extends ActionCommand {

        private static final String COMMAND_ID = CalendarioCorrispettivoPage.PAGE_ID + ".caricaCalendarioActionCommand";

        /**
         * Costruttore.
         */
        public CaricaCalendarioActionCommand() {
            super(COMMAND_ID);
            setSecurityControllerId(COMMAND_ID);
            RcpSupport.configure(this);
        }

        @Override
        protected void doExecuteCommand() {
            // aggiorno il pannello del calendario.
            CalendarioCorrispettivo calendarioCorrispettivo = corrispettiviBD.caricaCalendarioCorrispettivo(
                    (Integer) spinnerAnno.getValue(), comboBoxMesi.getSelectedIndex(),
                    (TipoDocumento) comboBoxTipoDocumento.getSelectedItem());

            CalendarioCorrispettivoPage.this.calendarioCorrispettiviPanel
                    .setCalendarioCorrispettivo(calendarioCorrispettivo);
            CalendarioCorrispettivoPage.this.tabellaTotaliCodiciIva.setCalendarioCorrispettivo(calendarioCorrispettivo);

            // firepropertyChanged per notificare a calendarioCorrispettiviPanel dell'aggiornamento
            // del calendario
            CalendarioCorrispettivoPage.this.firePropertyChange(CALENDARIO_CHANGED, null,
                    CalendarioCorrispettivoPage.this.calendarioCorrispettiviPanel.getCalendarioCorrispettivo());
        }
    }

    /**
     * Crea tutti i documenti dei corrispettivi inseriti nel calendario.
     */
    private class CreaDocumentiActionCommand extends ActionCommand {

        private static final String COMMAND_ID = CalendarioCorrispettivoPage.PAGE_ID + ".creaDocumentiActionCommand";

        /**
         * Costruttore.
         */
        public CreaDocumentiActionCommand() {
            super(COMMAND_ID);
            setSecurityControllerId(COMMAND_ID);
            RcpSupport.configure(this);
        }

        @Override
        protected void doExecuteCommand() {
            // non serve che passo nessun parametro alla call() della closure
            LoadingDialog loadingDialog = new LoadingDialog(getCreaDocumentiClosure(), null);
            loadingDialog.showDialog();
        }
    }

    /**
     * Closure che crea i documenti dal calendario corrispettivo.
     *
     * @author Leonardo
     */
    private class CreaDocumentiClosure implements Closure {

        @Override
        public Object call(Object arg0) {
            CalendarioCorrispettivo calendarioCorrispettivo = CalendarioCorrispettivoPage.this.calendarioCorrispettiviPanel
                    .getCalendarioCorrispettivo();

            CalendarioCorrispettivo calendarioCorrispettivoNew = CalendarioCorrispettivoPage.this.corrispettiviBD
                    .creaDocumenti(calendarioCorrispettivo);

            CalendarioCorrispettivoPage.this.calendarioCorrispettiviPanel
                    .setCalendarioCorrispettivo(calendarioCorrispettivoNew);
            CalendarioCorrispettivoPage.this.tabellaTotaliCodiciIva
                    .setCalendarioCorrispettivo(calendarioCorrispettivoNew);
            return null;
        }

    }

    public static final String PAGE_ID = "calendarioCorrispettivoPage";
    private static final String LABEL_ANNO = PAGE_ID + ".anno.label";
    private static final String LABEL_MESE = PAGE_ID + ".mese.label";
    private static final String LABEL_TIPO_DOC = PAGE_ID + ".tipoDocumento.label";
    private static final String CALENDARIO_CHANGED = "calendarioCorrispettiviChanged";
    private ICorrispettiviBD corrispettiviBD = null;
    private int currentMonth = -1;
    private int currentYear = -1;
    private JSpinner spinnerAnno = null;
    @SuppressWarnings("rawtypes")
    private JComboBox comboBoxMesi = null;
    private JComboBox<TipoDocumento> comboBoxTipoDocumento = null;
    private JLabel labelAnno = null;
    private JLabel labelMese = null;
    private JLabel labelTipoDoc = null;
    private CalendarioCorrispettiviPanel calendarioCorrispettiviPanel = null;
    private ActionCommand creaDocumentiActionCommand = null;
    private TabellaTotaliCodiciIva tabellaTotaliCodiciIva = null;
    private CreaDocumentiClosure creaDocumentiClosure = null;
    private CaricaCalendarioActionCommand caricaCalendarioActionCommand = null;

    private PluginManager pluginManager;

    /**
     * Costruttore.
     */
    public CalendarioCorrispettivoPage() {
        super(PAGE_ID);

        this.pluginManager = RcpSupport.getBean(PluginManager.BEAN_ID);

        Calendar calendar = Calendar.getInstance();
        this.currentYear = calendar.get(Calendar.YEAR);
        this.currentMonth = calendar.get(Calendar.MONTH);
    }

    @Override
    protected JComponent createControl() {
        JPanel panel = getComponentFactory().createPanel(new BorderLayout());
        panel.add(createHeaderPanel(), BorderLayout.NORTH);

        // pannello del calendario Guard che prende sul costruttore un guarded che è il nostro
        // command
        // per la creazione dei documenti dato un calendarioCorrispettivo
        calendarioCorrispettiviPanel = new CalendarioCorrispettiviPanel(getCreaDocumentiActionCommand());
        // controllo tramite il propertyChange (CalendarioCorrispettiviPanel) lo stato del suo
        // Guarded
        // enabled/disabled; il command è un Guarded quindi non serve implementarlo
        // lancio da this il firePropertyChange quando eseguo il caricaCalendarioCommand
        this.addPropertyChangeListener(CALENDARIO_CHANGED, calendarioCorrispettiviPanel);
        calendarioCorrispettiviPanel.addPropertyChangeListener(GiornoCorrispettivoPanel.UPDATE_CORRISPETTIVO_PROPERTY,
                new PropertyChangeListener() {

                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        getCaricaCalendarioActionCommand().execute();
                    }
                });
        panel.add(calendarioCorrispettiviPanel, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Crea il pannello che contiene i controlli del mese, dell'anno, dei parametri e della tabella riepilogativa.
     *
     * @return HeaderPanel
     */
    private JComponent createHeaderPanel() {

        JPanel rootPanel = getComponentFactory().createPanel(new BorderLayout());
        rootPanel.add(createParameterHeaderPanel(), BorderLayout.WEST);
        tabellaTotaliCodiciIva = new TabellaTotaliCodiciIva();
        tabellaTotaliCodiciIva.setCorrispettiviBD(corrispettiviBD);
        GuiStandardUtils.attachBorder(tabellaTotaliCodiciIva);
        rootPanel.add(tabellaTotaliCodiciIva, BorderLayout.EAST);

        return rootPanel;
    }

    /**
     * Crea il pannello che contiene tutti i controlli che servono per inserire i parametri del calendario da caricare.
     *
     * @return ParameterHeaderPanel
     */
    private JComponent createParameterHeaderPanel() {

        initializeAnnoControl();
        initializeComboBoxMeseControl();
        initializeComboBoxTipoDocumentoControl();

        FormLayout layout = new FormLayout("right:pref,4dlu,left:pref,10dlu,right:pref,4dlu,left:pref",
                "pref,2dlu,pref,2dlu,pref,2dlu,pref,2dlu,pref,2dlu");
        JPanel panel = getComponentFactory().createPanel(layout);

        CellConstraints cc = new CellConstraints();

        panel.add(labelAnno, cc.xy(1, 1));
        panel.add(spinnerAnno, cc.xy(3, 1));
        panel.add(labelMese, cc.xy(5, 1));
        panel.add(comboBoxMesi, cc.xy(7, 1));
        panel.add(labelTipoDoc, cc.xy(1, 3));
        panel.add(comboBoxTipoDocumento, cc.xyw(3, 3, 5));
        panel.add(getCaricaCalendarioActionCommand().createButton(), cc.xyw(3, 5, 5));

        if (pluginManager.isPresente(PluginManager.PLUGIN_CONTABILITA)) {
            panel.add(getCreaDocumentiActionCommand().createButton(),
                    cc.xy(1, 5, CellConstraints.FILL, CellConstraints.FILL));
        }

        GuiStandardUtils.attachBorder(panel);
        return panel;
    }

    @Override
    public void dispose() {
        // non faccio niente
    }

    /**
     * @return CaricaCalendarioActionCommand
     */
    private CaricaCalendarioActionCommand getCaricaCalendarioActionCommand() {
        if (caricaCalendarioActionCommand == null) {
            caricaCalendarioActionCommand = new CaricaCalendarioActionCommand();
        }
        return caricaCalendarioActionCommand;
    }

    /**
     * @return CreaDocumentiActionCommand
     */
    public ActionCommand getCreaDocumentiActionCommand() {
        if (creaDocumentiActionCommand == null) {
            creaDocumentiActionCommand = new CreaDocumentiActionCommand();
        }
        return creaDocumentiActionCommand;
    }

    /**
     * Crea la closure per la creazione dei documenti.
     *
     * @return Closure
     */
    private Closure getCreaDocumentiClosure() {
        if (creaDocumentiClosure == null) {
            creaDocumentiClosure = new CreaDocumentiClosure();
        }
        return creaDocumentiClosure;
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

    /**
     * Inizializza i controlli per la selezione dell'anno.
     */
    private void initializeAnnoControl() {
        spinnerAnno = new JSpinner(new SpinnerNumberModel(currentYear, 1900, 4000, 1));

        labelAnno = getComponentFactory().createLabel(LABEL_ANNO);
    }

    /**
     * Inizializza i controlli per la selezione del mese.
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void initializeComboBoxMeseControl() {

        DateFormatSymbols dateFormatSymbols = new DateFormatSymbols(Locale.getDefault());
        String[] months = dateFormatSymbols.getMonths();
        String[] mesi = new String[12];
        System.arraycopy(months, 0, mesi, 0, 12);

        comboBoxMesi = new JComboBox(mesi);
        comboBoxMesi.setSelectedIndex(currentMonth);

        labelMese = getComponentFactory().createLabel(LABEL_MESE);
    }

    /**
     * Inizializza i controlli per la selezione dli tioi documento corrispettivo.
     */
    private void initializeComboBoxTipoDocumentoControl() {
        List<TipoDocumento> tipiDocumento = corrispettiviBD.caricaTipiDocumentoCorrispettivi();

        comboBoxTipoDocumento = new JComboBox<>(tipiDocumento.toArray(new TipoDocumento[tipiDocumento.size()]));
        comboBoxTipoDocumento.setRenderer(new TipoDocumentoCellRenderer());
        labelTipoDoc = getComponentFactory().createLabel(LABEL_TIPO_DOC);
    }

    @Override
    public void loadData() {
        // non faccio niente
    }

    @Override
    public void onPostPageOpen() {
        // non faccio niente
    }

    @Override
    public boolean onPrePageOpen() {
        return true;
    }

    @Override
    public void postSetFormObject(Object object) {
        // non faccio niente
    }

    @Override
    public void preSetFormObject(Object object) {
        // non faccio niente
    }

    @Override
    public void refreshData() {
        // non faccio niente
    }

    @Override
    public void restoreState(Settings arg0) {
        // non faccio niente
    }

    @Override
    public void saveState(Settings arg0) {
        // non faccio niente
    }

    /**
     * @param corrispettiviBD
     *            the corrispettiviBD to set
     */
    public void setCorrispettiviBD(ICorrispettiviBD corrispettiviBD) {
        this.corrispettiviBD = corrispettiviBD;
    }

    @Override
    public void setFormObject(Object object) {
        // non faccio niente
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
