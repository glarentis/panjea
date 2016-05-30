/**
 *
 */
package it.eurotn.panjea.contabilita.rich.editors.tabelle;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.Format;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import org.apache.commons.lang.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.CommandGroup;
import org.springframework.richclient.command.config.CommandConfigurer;
import org.springframework.richclient.dialog.AbstractDialogPage;
import org.springframework.richclient.settings.Settings;
import org.springframework.richclient.util.RcpSupport;
import org.springframework.rules.closure.Closure;

import com.jidesoft.grid.TableModelWrapperUtils;

import it.eurotn.locking.ILock;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.domain.ValutaAzienda;
import it.eurotn.panjea.anagrafica.rich.bd.IDocumentiBD;
import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.contabilita.domain.AreaContabile.StatoAreaContabile;
import it.eurotn.panjea.contabilita.domain.Giornale;
import it.eurotn.panjea.contabilita.domain.NotaGiornale;
import it.eurotn.panjea.contabilita.domain.RigaContabile.EContoInsert;
import it.eurotn.panjea.contabilita.rich.bd.IContabilitaBD;
import it.eurotn.panjea.contabilita.rich.pm.RigaStampaGiornale;
import it.eurotn.panjea.contabilita.rich.scriptlet.StampaGiornaleScriptlet;
import it.eurotn.panjea.contabilita.util.ParametriRicercaMovimentiContabili;
import it.eurotn.panjea.contabilita.util.RigaContabileDTO;
import it.eurotn.panjea.rich.factory.PanjeaComponentFactory;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.control.table.DefaultBeanTableModel;
import it.eurotn.rich.control.table.JideTableWidget;
import it.eurotn.rich.editors.IPageEditor;
import it.eurotn.rich.report.FooterBean;
import it.eurotn.rich.report.HeaderBean;
import it.eurotn.rich.report.JecLocalReport;

/**
 * Gestisce la visualizzazione e stampa dei libri giornale dell'azienda.
 *
 * @author fattazzo
 * @version 1.0, 26/set/07
 */
public class LibriGiornalePage extends AbstractDialogPage implements IPageEditor, Observer {

    /**
     * Renderer che visualizza il nome del mese nella cella.
     *
     * @author fattazzo
     * @version 1.0, 04/ott/07
     */
    private class MeseTableCellRenderer extends DefaultTableCellRenderer {

        private static final long serialVersionUID = -3141273136692479793L;

        /**
         * Costruttore.
         *
         */
        public MeseTableCellRenderer() {
            super();
        }

        @Override
        public Component getTableCellRendererComponent(JTable arg0, Object arg1, boolean arg2, boolean arg3, int row,
                int arg5) {
            JLabel label = (JLabel) super.getTableCellRendererComponent(arg0, arg1, arg2, arg3, row, arg5);

            String mese = getMessageSource().getMessage("mese." + label.getText(), new Object[] {},
                    Locale.getDefault());
            label.setText(mese);

            @SuppressWarnings("unchecked")
            DefaultBeanTableModel<Giornale> tableModel = (DefaultBeanTableModel<Giornale>) TableModelWrapperUtils
                    .getActualTableModel(mesiGiornaleTableWidget.getTable().getModel());

            Giornale giornale = tableModel.getObject(row);

            if (giornale.getId() != null) {
                if (giornale.getValido()) {
                    label.setIcon(getIconSource().getIcon("stato.valido"));
                } else {
                    label.setIcon(getIconSource().getIcon("stato.nonValido"));
                }
            } else {
                label.setIcon(getIconSource().getIcon("stato.nonStampato"));
            }

            return label;
        }
    }

    private class RefreshCommand extends ActionCommand {

        /**
         * Costruttore.
         *
         */
        public RefreshCommand() {
            super(PAGE_ID + REFRESH_COMMAND);
            CommandConfigurer c = (CommandConfigurer) ApplicationServicesLocator.services()
                    .getService(CommandConfigurer.class);
            c.configure(this);
        }

        @Override
        protected void doExecuteCommand() {
            List<Giornale> list = contabilitaBD.caricaGiornali((Integer) spinnerAnno.getValue());
            mesiGiornaleTableWidget.setRows(list);
        }

        @Override
        protected void onButtonAttached(AbstractButton button) {
            super.onButtonAttached(button);
            button.setName(PAGE_ID + REFRESH_COMMAND);
        }
    }

    /**
     * Stampa definitiva command, il loading dialog prende una closure e il parametro da passare alla call della closure
     * che e' il valore true o false per definire se la stampa deve essere definitiva o provvisoria.
     *
     * @author Leonardo
     *
     */
    private class StampaLibroGiornaleCommand extends ActionCommand {

        /**
         * Costruttore.
         *
         */
        public StampaLibroGiornaleCommand() {
            super(PAGE_ID + STAMPA_GIORNALE_COMMAND);
            RcpSupport.configure(this);
        }

        @Override
        protected void doExecuteCommand() {
            if (mesiGiornaleTableWidget.getSelectedObject() == null) {
                return;
            }
            stampaGiornale(true);
        }
    }

    /**
     * Stampa provvisoria command, il loading dialog prende una closure e il parametro da passare alla call della
     * closure che e' il valore true o false per definire se la stampa deve essere definitiva o provvisoria.
     *
     * @author Leonardo
     *
     */
    private class StampaLibroGiornaleProvvisorioCommand extends ActionCommand {

        /**
         * Costruttore.
         *
         */
        public StampaLibroGiornaleProvvisorioCommand() {
            super(PAGE_ID + STAMPA_GIORNALE_PROVVISORIO_COMMAND);
            RcpSupport.configure(this);
        }

        @Override
        protected void doExecuteCommand() {
            if (mesiGiornaleTableWidget.getSelectedObject() == null) {
                return;
            }
            stampaGiornale(false);
        }
    }

    public static final String PAGE_ID = "libriGiornalePage";

    public static final String STAMPA_GIORNALE_COMMAND = ".stampaGiornaleCommand";
    public static final String STAMPA_GIORNALE_PROVVISORIO_COMMAND = ".stampaGiornaleProvvisorioCommand";
    public static final String REFRESH_COMMAND = ".refreshCommand";
    public static final String ANNO = ".anno";
    public static final String PANEL_TABLE_TITLE = PAGE_ID + ".table.title";
    public static final String PANEL_NOTE_TITLE = PAGE_ID + ".note.title";
    public static final String NOTE_SALDO_AVERE = "libriGiornalePage.note.saldoAvere.label";
    public static final String NOTE_SALDO_DARE = "libriGiornalePage.note.saldoDare.label";
    public static final String NOTE_NUMERO_MOVIMENTO = "libriGiornalePage.note.numeroMovimento.label";
    public static final String NOTE_ULTIMA_PAGINA = "libriGiornalePage.note.numeroPagina.label";
    public static final String NOTE_DATA_ULTIMO_MOVIMENTO = "libriGiornalePage.note.dataUltimoDocumento.label";
    public static final String INVALIDAZIONE_GIORNALE = "libriGiornalePage.note.invalidazione.";
    public static final String DIALOG_NO_MOVIMENTI_TITLE = PAGE_ID + ".dialogNoMovimenti.title";
    public static final String DIALOG_NO_MOVIMENTI_MESSAGE = PAGE_ID + ".dialogNoMovimenti.message";

    private IContabilitaBD contabilitaBD;

    private AziendaCorrente aziendaCorrente;

    private JideTableWidget<Giornale> mesiGiornaleTableWidget;

    private JTextPane noteTextArea;

    private JSpinner spinnerAnno;

    private StampaLibroGiornaleCommand stampaLibroGiornaleCommand;

    private StampaLibroGiornaleProvvisorioCommand stampaLibroGiornaleProvvisorioCommand;

    private RefreshCommand refreshCommand;

    /**
     * Costruttore.
     */
    public LibriGiornalePage() {
        super(PAGE_ID);
    }

    /**
     * @param doc
     *            {@link StyledDocument}
     */
    private void addStylesToDocument(StyledDocument doc) {
        Style def = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);

        Style regular = doc.addStyle("regular", def);

        Style s = doc.addStyle("italic", regular);
        StyleConstants.setItalic(s, true);

        s = doc.addStyle("bold", regular);
        StyleConstants.setBold(s, true);

        s = doc.addStyle("small", regular);
        StyleConstants.setFontSize(s, 10);

        s = doc.addStyle("large", regular);
        StyleConstants.setFontSize(s, 16);

        s = doc.addStyle("iconNote", regular);
        StyleConstants.setAlignment(s, StyleConstants.ALIGN_LEFT);

        Icon noteIcon = getIconSource().getIcon("libriGiornalePage.note.icon");

        if (noteIcon != null) {
            StyleConstants.setIcon(s, noteIcon);
        }
    }

    /**
     * @param giornale
     *            giornale da aggiornare
     */
    private void aggiornaValoriGiornale(Giornale giornale) {
        logger.debug("--> Enter aggiornaValoriGiornale");

        noteTextArea.setText("");

        if (giornale.isNew()) {
            return;
        }

        StyledDocument doc = noteTextArea.getStyledDocument();
        addStylesToDocument(doc);

        try {
            doc.insertString(doc.getLength(), getMessage(NOTE_DATA_ULTIMO_MOVIMENTO) + " ", doc.getStyle("bold"));
            doc.insertString(doc.getLength(), giornale.getDataUltimoDocumento() + "\n", doc.getStyle("regular"));

            doc.insertString(doc.getLength(), getMessage(NOTE_NUMERO_MOVIMENTO) + " ", doc.getStyle("bold"));
            doc.insertString(doc.getLength(), giornale.getNumeroMovimento() + "\n", doc.getStyle("regular"));

            doc.insertString(doc.getLength(), getMessage(NOTE_ULTIMA_PAGINA) + " ", doc.getStyle("bold"));
            doc.insertString(doc.getLength(), giornale.getNumeroPagina() + "\n", doc.getStyle("regular"));

            doc.insertString(doc.getLength(), getMessage(NOTE_SALDO_AVERE) + " ", doc.getStyle("bold"));
            Format format = new DecimalFormat(ValutaAzienda.MASCHERA_VALUTA_GENERICA);
            doc.insertString(doc.getLength(), format.format(giornale.getSaldoAvere()) + "\n", doc.getStyle("regular"));

            doc.insertString(doc.getLength(), getMessage(NOTE_SALDO_DARE) + " ", doc.getStyle("bold"));
            doc.insertString(doc.getLength(), format.format(giornale.getSaldoDare()) + "\n\n\n",
                    doc.getStyle("regular"));

            for (NotaGiornale notaGiornale : giornale.getNoteGiornale()) {
                doc.insertString(doc.getLength(), " ", doc.getStyle("iconNote"));

                switch (notaGiornale.getTipoNotaGiornale()) {
                case GIORNALE_PRECEDENTE_INVALIDATO:
                    doc.insertString(doc.getLength(),
                            getMessage(INVALIDAZIONE_GIORNALE + notaGiornale.getTipoNotaGiornale().name()) + "\n",
                            doc.getStyle("regular"));
                    break;
                default:
                    String[] noteInterne = notaGiornale.getNoteInterne().split("[|]");
                    String message = getMessage(INVALIDAZIONE_GIORNALE + notaGiornale.getTipoNotaGiornale().name(),
                            new Object[] { noteInterne[0], noteInterne[1] });
                    doc.insertString(doc.getLength(), message + "\n", doc.getStyle("regular"));
                    break;
                }

            }
        } catch (BadLocationException e) {
            e.printStackTrace();
        }

        logger.debug("--> Exit aggiornaValoriGiornale");
    }

    /**
     * Avvolora i parametri per la ricerca in base al giornale di riferimento.
     *
     * @param giornale
     *            giornale di riferimento
     * @return {@link ParametriRicercaMovimentiContabili}
     */
    private ParametriRicercaMovimentiContabili avvaloraParametriRicerca(Giornale giornale) {
        IDocumentiBD documentiBD = (IDocumentiBD) Application.instance().getApplicationContext().getBean("documentiBD");

        Calendar calendar = new GregorianCalendar(giornale.getAnno(), giornale.getMese() - 1, 1);

        ParametriRicercaMovimentiContabili parametriRicercaMovimentiContabili = new ParametriRicercaMovimentiContabili();
        parametriRicercaMovimentiContabili.setAnnoCompetenza(((Integer) spinnerAnno.getValue()).toString());

        parametriRicercaMovimentiContabili.getDataRegistrazione().setDataIniziale(calendar.getTime());

        calendar.set(giornale.getAnno(), giornale.getMese() - 1, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        parametriRicercaMovimentiContabili.getDataRegistrazione().setDataFinale(calendar.getTime());

        parametriRicercaMovimentiContabili.setEscludiMovimentiStampati(false);
        parametriRicercaMovimentiContabili.getStatiAreaContabile().add(StatoAreaContabile.CONFERMATO);
        parametriRicercaMovimentiContabili.getStatiAreaContabile().add(StatoAreaContabile.VERIFICATO);

        List<TipoDocumento> tipiList = documentiBD.caricaTipiDocumento("codice", null, true);
        Set<TipoDocumento> tipiSet = new HashSet<TipoDocumento>();
        tipiSet.addAll(tipiList);

        parametriRicercaMovimentiContabili.setTipiDocumento(tipiSet);

        parametriRicercaMovimentiContabili.setFiltraAbilitatiStampaGiornale(true);

        return parametriRicercaMovimentiContabili;
    }

    /**
     * Crea le {@link RigaStampaGiornale} dalla lista di {@link RigaContabileDTO}.
     *
     * @param listRigheContabili
     *            righe contabili
     * @param giornalePrecedente
     *            giornale
     * @return righe stampa giornale
     * @param giornaleAttuale
     *            giornale attuale
     * @param dataEsercizioCal
     *            date inizio esercizio dell'azienda
     */
    private List<RigaStampaGiornale> creaRigheStampaGiornale(List<RigaContabileDTO> listRigheContabili,
            Giornale giornalePrecedente, Giornale giornaleAttuale, Calendar dataEsercizioCal) {
        logger.debug("--> Enter creaRigheStampaGiornale");

        List<RigaStampaGiornale> listRigheStampaGiornale = new ArrayList<RigaStampaGiornale>();

        Integer idAreaContabileCorrente = null;
        Integer idRigaContabileCorrente = null;
        Integer numeroMovimento;

        // se non ho un giornale precedente o se il mese del giornale corrisponde al mese di inizio
        // esercizio
        // dell'azienda setto il numero movimento a 0
        if (giornalePrecedente == null
                || (giornaleAttuale.getMese().equals(dataEsercizioCal.get(Calendar.MONTH) + 1))) {
            numeroMovimento = new Integer(0);
        } else {
            numeroMovimento = giornalePrecedente.getNumeroMovimento();
        }

        String noteSplitRegEx = "(?<=\\G.{" + 40 + "})";

        for (RigaContabileDTO rigaContabileDTO : listRigheContabili) {

            // escludo le righe che non sono in dare e in avere ( righe generate automaticamente non
            // da input utente )
            if (rigaContabileDTO.getContoInsert() == EContoInsert.NONE) {
                continue;
            }

            if (idAreaContabileCorrente == null || (rigaContabileDTO.getAreaContabileDTO().getId()
                    .intValue() != idAreaContabileCorrente.intValue())) {
                idAreaContabileCorrente = rigaContabileDTO.getAreaContabileDTO().getId();

                numeroMovimento++;
                listRigheStampaGiornale
                        .add(new RigaStampaGiornale(false, true, false, rigaContabileDTO, numeroMovimento));

                // note area contabile
                String note = StringUtils.defaultString(rigaContabileDTO.getAreaContabileDTO().getNote());
                String[] noteSplit = note.split("\n");

                for (String nota : noteSplit) {
                    if (nota.length() > 0) {
                        String[] notaSplit = nota.split(noteSplitRegEx);
                        for (String string : notaSplit) {
                            RigaContabileDTO rigaContabileNoteArea = new RigaContabileDTO();
                            rigaContabileNoteArea.setDescrizioneSottoConto(string);
                            listRigheStampaGiornale
                                    .add(new RigaStampaGiornale(false, false, false, rigaContabileNoteArea));
                        }
                    }
                }
            }

            if (rigaContabileDTO.getSottoContoCodice() != null && (idRigaContabileCorrente == null
                    || (rigaContabileDTO.getId().intValue() != idRigaContabileCorrente.intValue()))) {
                idRigaContabileCorrente = rigaContabileDTO.getId();

                listRigheStampaGiornale.add(new RigaStampaGiornale(true, false, false, rigaContabileDTO));

                // note riga contabile
                String noteRiga = StringUtils.defaultString(rigaContabileDTO.getNote());
                String[] notaSplit = noteRiga.split(noteSplitRegEx);
                for (String nota : notaSplit) {
                    RigaContabileDTO rigaContabileNote = new RigaContabileDTO();
                    rigaContabileNote.setDescrizioneSottoConto(nota);

                    listRigheStampaGiornale.add(new RigaStampaGiornale(false, false, false, rigaContabileNote));
                }
            }
        }

        // creo le righe filler
        int righeFiller = listRigheStampaGiornale.size() % 60;

        for (int i = 0; i < 60 - righeFiller; i++) {
            RigaContabileDTO rigaContabile = new RigaContabileDTO();
            listRigheStampaGiornale.add(new RigaStampaGiornale(false, false, true, rigaContabile));
        }

        logger.debug("--> Exit creaRigheStampaGiornale");
        return listRigheStampaGiornale;
    }

    /**
     * @return crea i controlli per i button.
     */
    private JComponent createButtonBar() {
        JPanel panel = new JPanel(new BorderLayout());

        CommandGroup commandGroup = new CommandGroup();

        stampaLibroGiornaleProvvisorioCommand = new StampaLibroGiornaleProvvisorioCommand();
        commandGroup.add(stampaLibroGiornaleProvvisorioCommand);

        stampaLibroGiornaleCommand = new StampaLibroGiornaleCommand();
        commandGroup.add(stampaLibroGiornaleCommand);

        refreshCommand = new RefreshCommand();
        commandGroup.add(refreshCommand);

        panel.add(commandGroup.createButtonBar(), BorderLayout.EAST);

        return panel;
    }

    /*
     * @see org.springframework.richclient.dialog.AbstractDialogPage#createControl()
     */
    @Override
    protected JComponent createControl() {
        JPanel panel = getComponentFactory().createPanel(new BorderLayout());
        JPanel panelData = getComponentFactory().createPanel(new BorderLayout());
        panel.add(panelData, BorderLayout.CENTER);
        panelData.add(createHeaderControl(), BorderLayout.NORTH);
        panelData.add(createButtonBar(), BorderLayout.SOUTH);
        JPanel panelTable = getComponentFactory().createPanel(new BorderLayout());
        panelTable.add(createTable(), BorderLayout.WEST);
        panelTable.add(createNote(), BorderLayout.CENTER);
        panelData.add(panelTable, BorderLayout.CENTER);
        Border padding = BorderFactory.createEmptyBorder(20, 20, 5, 20);
        panel.setBorder(padding);
        return panel;
    }

    /**
     * @return crea i controlli che verranno riportati in alto nella pagina
     */
    private JComponent createHeaderControl() {
        JPanel panel = getComponentFactory().createPanel(new BorderLayout());

        panel.add(new JLabel(getMessageSource().getMessage(PAGE_ID + ANNO, new Object[] {}, Locale.getDefault())),
                BorderLayout.WEST);

        SpinnerNumberModel model = new SpinnerNumberModel(new Integer(aziendaCorrente.getAnnoContabile()),
                new Integer(0), new Integer(aziendaCorrente.getAnnoContabile() + 100), new Integer(1));
        spinnerAnno = new JSpinner(model);
        spinnerAnno.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                List<Giornale> list = contabilitaBD.caricaGiornali((Integer) spinnerAnno.getValue());
                mesiGiornaleTableWidget.setRows(list);
            }
        });
        JPanel panelSpinner = getComponentFactory().createPanel(new BorderLayout());
        panelSpinner.add(spinnerAnno, BorderLayout.WEST);

        panel.add(panelSpinner, BorderLayout.CENTER);

        Border padding = BorderFactory.createEmptyBorder(0, 0, 5, 0);
        panel.setBorder(padding);

        return panel;
    }

    /**
     * Crea il componente per la visualizzazione delle note del giornale.
     *
     * @return componente creato
     */
    private JComponent createNote() {
        noteTextArea = new JTextPane();
        noteTextArea.setBackground(UIManager.getDefaults().getColor("Panel.background"));
        noteTextArea.setEditable(false);

        JScrollPane scrollPane = getComponentFactory().createScrollPane(noteTextArea);
        ((PanjeaComponentFactory) getComponentFactory()).createTitledBorderFor(getMessage(PANEL_NOTE_TITLE),
                scrollPane);

        // aggiorno le note del giornale selezionato alla prima
        // visualizzazione della tabella
        Giornale giornaleSel = mesiGiornaleTableWidget.getSelectedObject();
        if (giornaleSel != null) {
            aggiornaValoriGiornale(giornaleSel);
        }

        return scrollPane;
    }

    /**
     * Crea la tabella dei mesi.
     *
     * @return tabella
     */
    private JComponent createTable() {

        mesiGiornaleTableWidget = new JideTableWidget<Giornale>(PAGE_ID + ".table", new String[] { "mese" },
                Giornale.class);
        mesiGiornaleTableWidget.getComponent();
        mesiGiornaleTableWidget.getTable().getColumnModel().getColumn(0).setCellRenderer(new MeseTableCellRenderer());
        mesiGiornaleTableWidget.addSelectionObserver(this);
        JPanel panel = getComponentFactory().createPanel(new BorderLayout());
        ((PanjeaComponentFactory) getComponentFactory()).createTitledBorderFor(getMessage(PANEL_TABLE_TITLE), panel);
        panel.add(mesiGiornaleTableWidget.getTable(), BorderLayout.CENTER);
        panel.setPreferredSize(new Dimension(200, 215));

        List<Giornale> list = contabilitaBD.caricaGiornali((Integer) spinnerAnno.getValue());
        mesiGiornaleTableWidget.setRows(list);

        return panel;
    }

    @Override
    public void dispose() {
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
    public String getPageEditorId() {
        return PAGE_ID;
    }

    @Override
    public Object getPageObject() {
        return null;
    }

    @Override
    public String getPageSecurityEditorId() {
        return null;
    }

    @Override
    public void grabFocus() {
        getControl().requestFocusInWindow();
    }

    @Override
    public boolean isCommittable() {
        return true;
    }

    @Override
    public boolean isDirty() {
        return false;
    }

    @Override
    public boolean isLocked() {
        return false;
    }

    @Override
    public void loadData() {
    }

    @Override
    public Object onDelete() {
        return null;
    }

    @Override
    public ILock onLock() {
        return null;
    }

    @Override
    public void onNew() {
    }

    @Override
    public void onPostPageOpen() {
    }

    @Override
    public boolean onPrePageOpen() {
        return true;
    }

    @Override
    public boolean onSave() {
        return false;
    }

    @Override
    public boolean onUndo() {
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
    public void restoreState(Settings settings) {
    }

    @Override
    public void saveState(Settings settings) {
    }

    /**
     * @param aziendaCorrente
     *            the aziendaCorrente to set
     */
    public void setAziendaCorrente(AziendaCorrente aziendaCorrente) {
        this.aziendaCorrente = aziendaCorrente;
    }

    /**
     *
     * @param contabilitaBD
     *            the contabilitaBD to set
     */
    public void setContabilitaBD(IContabilitaBD contabilitaBD) {
        this.contabilitaBD = contabilitaBD;
    }

    @Override
    public void setFormObject(Object arg0) {
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Genera il report di stampa del giornale.
     *
     * @param definitivo
     *            <code>true</code> se la stampa deve essere definitiva e quindi aggiornare righe, areeContaibli e
     *            giornale, <code>false</code> se la stampa deve essere provvisoria
     */
    private void stampaGiornale(final boolean definitivo) {
        final Giornale giornale = mesiGiornaleTableWidget.getSelectedObject();

        Calendar dataEsercizioCal = Calendar.getInstance();
        dataEsercizioCal.setTime(aziendaCorrente.getDataInizioEsercizio());

        // HACK refactor necessario per tutto il report per farlo migrare verso
        // RigaContabileDTO
        ParametriRicercaMovimentiContabili parametri = avvaloraParametriRicerca(giornale);
        final List<RigaContabileDTO> list = contabilitaBD.ricercaControlloAreeContabili(parametri);

        // riordino la lista perchè se ci fossero delel aree senza righe verrebbero messe per prime
        // e non in ordine di
        // data
        Collections.sort(list, new Comparator<RigaContabileDTO>() {

            @Override
            public int compare(RigaContabileDTO o1, RigaContabileDTO o2) {
                int compareDataReg = o1.getAreaContabileDTO().getDataRegistrazione()
                        .compareTo(o2.getAreaContabileDTO().getDataRegistrazione());
                int compareDataDoc = o1.getAreaContabileDTO().getDataDocumento()
                        .compareTo(o2.getAreaContabileDTO().getDataDocumento());
                if (compareDataReg != 0) {
                    return compareDataReg;
                } else if (compareDataDoc != 0) {
                    return compareDataDoc;
                } else if (o1.getAreaContabileDTO().getId().compareTo(o2.getAreaContabileDTO().getId()) != 0) {
                    return o1.getAreaContabileDTO().getId().compareTo(o2.getAreaContabileDTO().getId());
                } else {
                    return o1.getId().compareTo(o2.getId());
                }
            }
        });

        final Giornale giornalePrecedente = contabilitaBD.caricaGiornalePrecedente(giornale);

        List<RigaStampaGiornale> listRigheStampa = creaRigheStampaGiornale(list, giornalePrecedente, giornale,
                dataEsercizioCal);

        final JecLocalReport jecJasperReport = new JecLocalReport();
        jecJasperReport.setReportName("Giornale");
        jecJasperReport.setXmlReportResource(
                new ClassPathResource("/it/eurotn/panjea/contabilita/rich/reports/resources/StampaGiornale.jasper"));
        jecJasperReport.setMessages("it.eurotn.panjea.messages.cont.reports.messages");
        jecJasperReport.setDataReport(listRigheStampa);
        HeaderBean headerBean = new HeaderBean();
        headerBean.setCodiceAzienda(aziendaCorrente.getDenominazione());
        headerBean.setUtenteCorrente(PanjeaSwingUtil.getUtenteCorrente().getUserName());
        FooterBean footerBean = new FooterBean();
        jecJasperReport.setDataHeader(headerBean);
        jecJasperReport.setDataFooter(footerBean);
        jecJasperReport.getReportParameters().put("annoGiornale", giornale.getAnno());
        jecJasperReport.getReportParameters().put("meseGiornale",
                getMessageSource().getMessage("mese." + giornale.getMese(), new Object[] {}, Locale.getDefault()));
        jecJasperReport.getReportParameters().put("aziendaCorrente", aziendaCorrente);
        jecJasperReport.getReportParameters().put("ultimoNumeroPagina", new Integer(0));

        final StampaGiornaleScriptlet stampaGiornaleScriptlet = new StampaGiornaleScriptlet();
        // aggiungo la classe di scriptlet al report
        jecJasperReport.getReportParameters().put("REPORT_SCRIPTLET", stampaGiornaleScriptlet);

        // se non ho un giornale precedente o se il mese del giornale corrisponde al mese di inizio
        // esercizio
        // dell'azienda setto i saldi precedenti a 0
        if (giornalePrecedente == null || (giornale.getMese().equals(dataEsercizioCal.get(Calendar.MONTH) + 1))) {
            jecJasperReport.getReportParameters().put("saldoAvereGiornalePrecedente", BigDecimal.ZERO);
            jecJasperReport.getReportParameters().put("saldoDareGiornalePrecedente", BigDecimal.ZERO);
        } else {
            jecJasperReport.getReportParameters().put("ultimoNumeroPagina", giornalePrecedente.getNumeroPagina());
            jecJasperReport.getReportParameters().put("saldoAvereGiornalePrecedente",
                    giornalePrecedente.getSaldoAvere());
            jecJasperReport.getReportParameters().put("saldoDareGiornalePrecedente", giornalePrecedente.getSaldoDare());
        }

        if (definitivo) {
            // rimuovo l'immagine di background dato che rimane nel report
            // singleton una volta settata e quindi
            // anche se eseguo la stampa definitiva se prima ne ho fatta una
            // provvisoria mi ritrovo con
            // il background facsimile
            jecJasperReport.getReportParameters().remove("imageBackGround");
        } else {
            Image facsimile = getImageSource().getImage("stampaGiornale.background.image");
            jecJasperReport.getReportParameters().put("imageBackGround", facsimile);
        }

        jecJasperReport.execute(new Closure() {
            @Override
            public Object call(Object arg0) {
                // Aggiorno aree contabili, righe contabili e giornale se e' definitivo
                if (definitivo) {
                    // se non ci sono movimenti per il mese selezionato devo riportare
                    // l'ultimo numero di movimento del giornale
                    // precedente
                    if (list.size() == 0 && giornalePrecedente != null) {
                        giornale.setDataUltimoDocumento(giornalePrecedente.getDataUltimoDocumento());
                        giornale.setNumeroMovimento(giornalePrecedente.getNumeroMovimento());
                    } else {
                        giornale.setDataUltimoDocumento(stampaGiornaleScriptlet.getUltimaDataMovimento());
                        giornale.setNumeroMovimento(stampaGiornaleScriptlet.getUltimoNumeroMovimento());
                    }

                    giornale.setNumeroPagina(stampaGiornaleScriptlet.getUltimoNumeroPagina());

                    BigDecimal saldoAverePrecedente = (BigDecimal) jecJasperReport.getReportParameters()
                            .get("saldoAvereGiornalePrecedente");
                    BigDecimal saldoDarePrecedente = (BigDecimal) jecJasperReport.getReportParameters()
                            .get("saldoDareGiornalePrecedente");

                    giornale.setSaldoAvere(stampaGiornaleScriptlet.getSaldoAvereAttuale().add(saldoAverePrecedente));
                    giornale.setSaldoDare(stampaGiornaleScriptlet.getSaldoDareAttuale().add(saldoDarePrecedente));
                    giornale.setValido(true);
                    contabilitaBD.asyncaggiornaStampaGiornale(giornale, stampaGiornaleScriptlet.getMapAreeContabili(),
                            stampaGiornaleScriptlet.getMapRigheContabili());

                    mesiGiornaleTableWidget.setRows(contabilitaBD.caricaGiornali((Integer) spinnerAnno.getValue()));
                }
                return null;
            }
        });
    }

    @Override
    public void unLock() {
    }

    @Override
    public void update(Observable o, Object arg) {
        Giornale giornale = (Giornale) arg;
        if (giornale.isStampabile()) { // || giornale.getId() !=
            // null) {

            stampaLibroGiornaleCommand.setEnabled(true);
        } else {
            stampaLibroGiornaleCommand.setEnabled(false);
        }

        // aggiorno i valori del giornale selezionato all'interno
        // dell
        // textArea delle note
        aggiornaValoriGiornale(giornale);
    }

}
