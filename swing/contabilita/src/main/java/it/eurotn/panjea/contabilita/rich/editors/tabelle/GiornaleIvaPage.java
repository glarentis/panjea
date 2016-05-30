package it.eurotn.panjea.contabilita.rich.editors.tabelle;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;

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
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;

import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.CommandGroup;
import org.springframework.richclient.dialog.AbstractDialogPage;
import org.springframework.richclient.dialog.MessageDialog;
import org.springframework.richclient.image.ImageSource;
import org.springframework.richclient.settings.Settings;
import org.springframework.richclient.tree.TreeUtils;
import org.springframework.richclient.util.RcpSupport;
import org.springframework.rules.closure.Closure;

import com.jidesoft.swing.CheckBoxTree;
import com.jidesoft.swing.CheckBoxTreeCellRenderer;
import com.jidesoft.swing.SearchableUtils;

import it.eurotn.locking.ILock;
import it.eurotn.panjea.anagrafica.domain.ValutaAzienda;
import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.contabilita.domain.AreaContabile.StatoAreaContabile;
import it.eurotn.panjea.contabilita.domain.GiornaleIva;
import it.eurotn.panjea.contabilita.domain.RegistroIva.TipoRegistro;
import it.eurotn.panjea.contabilita.rich.bd.IContabilitaBD;
import it.eurotn.panjea.contabilita.rich.scriptlet.StampaRegistroIvaScriptlet;
import it.eurotn.panjea.contabilita.util.ParametriRicercaMovimentiContabili;
import it.eurotn.panjea.contabilita.util.TotaliCodiceIvaDTO;
import it.eurotn.panjea.rich.factory.PanjeaComponentFactory;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.control.table.JideTableWidget;
import it.eurotn.rich.editors.IPageEditor;
import it.eurotn.rich.report.FooterBean;
import it.eurotn.rich.report.HeaderBean;
import it.eurotn.rich.report.JecLocalReport;

/**
 * Gestisce la visualizzazione e stampa dei giornali iva dell'azienda.
 *
 * @author Leonardo
 * @version 1.0, 22/nov/07
 */
public final class GiornaleIvaPage extends AbstractDialogPage implements IPageEditor, Observer {

    /**
     * Listener per aggiornare le info sulla selezione dei registri iva con giornale associato dalla
     * check box tree.
     *
     * @author Leonardo
     */
    private class CheckBoxTreeSelectionListener implements TreeSelectionListener {

        @Override
        public void valueChanged(TreeSelectionEvent event) {
            TreePath path = event.getNewLeadSelectionPath();
            if (path == null) {
                return;
            }
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();

            if (node.getUserObject() instanceof TipoRegistroPM) {
                aggiornaValoriGiornaleIva(null);
            } else if (node.getUserObject() instanceof GiornaleIva) {
                GiornaleIva giornaleIva = (GiornaleIva) node.getUserObject();

                // aggiorno la text area delle info con il giornale iva
                // selezionato
                aggiornaValoriGiornaleIva(giornaleIva);
            }
        }
    }

    /**
     * Renderer che visualizza il nome del mese nella cella.
     *
     * @author Leonardo
     * @version 1.0, 04/ott/07
     */
    private final class MeseTableCellRenderer extends DefaultTableCellRenderer {

        private static final long serialVersionUID = -3141273136692479793L;

        /**
         * Costruttore di default.
         */
        private MeseTableCellRenderer() {
            super();
        }

        @Override
        public Component getTableCellRendererComponent(JTable arg0, Object arg1, boolean arg2, boolean arg3, int row,
                int arg5) {
            JLabel label = (JLabel) super.getTableCellRendererComponent(arg0, arg1, arg2, arg3, row, arg5);

            String mese = getMessageSource().getMessage("mese." + label.getText(), new Object[] {},
                    Locale.getDefault());
            label.setText(mese);

            label.setIcon(getIconSource().getIcon("mese"));

            return label;
        }
    }

    /**
     * Command pereseguire il refresh dei giornali del mese selezionato.
     *
     * @author Leonardo
     */
    private final class RefreshCommand extends ActionCommand {

        /**
         * Costruttore di default.
         */
        private RefreshCommand() {
            super(PAGE_ID + REFRESH_COMMAND);
            RcpSupport.configure(this);
        }

        @Override
        protected void doExecuteCommand() {
            MesePM mesePM = meseTableWidget.getSelectedObject();
            giornaliIva = contabilitaBD.caricaGiornaliIva(mesePM.getAnno(), mesePM.getMese());
            updateCheckBoxTreeData(giornaliIva);
        }

        @Override
        protected void onButtonAttached(AbstractButton button) {
            super.onButtonAttached(button);
            button.setName(PAGE_ID + REFRESH_COMMAND);
        }
    }

    /**
     * Command per la stampa del giornale iva.
     *
     * @author Leonardo
     */
    private final class StampaLibroGiornaleCommand extends ActionCommand {

        /**
         * Costruttore.
         */
        private StampaLibroGiornaleCommand() {
            super(PAGE_ID + STAMPA_GIORNALE_COMMAND);
            RcpSupport.configure(this);
        }

        @Override
        protected void doExecuteCommand() {
            GiornaleIvaPage.LOGGER.debug("--> Execute Stampa giornale iva command");
            stampaGiornaliSelezionati(true);
        }
    }

    /**
     * Command per la stampa provvisoria del giornale iva.
     *
     * @author Leonardo
     */
    private final class StampaLibroGiornaleProvvisorioCommand extends ActionCommand {

        /**
         * Costruttore.
         */
        private StampaLibroGiornaleProvvisorioCommand() {
            super(PAGE_ID + STAMPA_GIORNALE_PROVVISORIO_COMMAND);
            RcpSupport.configure(this);
        }

        @Override
        protected void doExecuteCommand() {
            GiornaleIvaPage.LOGGER.debug("--> Execute Stampa giornale iva provvisorio command");
            stampaGiornaliSelezionati(false);
        }
    }

    private static final Logger LOGGER = Logger.getLogger(GiornaleIvaPage.class);

    public static final String PAGE_ID = "giornaleIvaPage";
    public static final String ANNO = ".anno";
    public static final String STAMPA_GIORNALE_COMMAND = ".stampaGiornaleCommand";
    public static final String STAMPA_GIORNALE_PROVVISORIO_COMMAND = ".stampaGiornaleProvvisorioCommand";
    public static final String REFRESH_COMMAND = ".refreshCommand";
    public static final String PANEL_TABLE_TITLE = PAGE_ID + ".mesi.title";
    public static final String PANEL_REGISTRI_TITLE = PAGE_ID + ".registri.title";
    public static final String PANEL_NOTE_TITLE = PAGE_ID + ".info.title";
    public static final String NOTE_IMPOSTA = PAGE_ID + ".info.imposta.label";
    public static final String NOTE_IMPONIBILE = PAGE_ID + ".info.imponibile.label";
    public static final String NOTE_TOTALE_DOCUMENTO = PAGE_ID + ".info.totaleDocumento.label";
    public static final String NOTE_NUMERO_MOVIMENTO = PAGE_ID + ".info.protocolloMovimento.label";
    public static final String NOTE_ULTIMA_PAGINA = PAGE_ID + ".info.numeroPagina.label";
    public static final String NOTE_DATA_ULTIMO_MOVIMENTO = PAGE_ID + ".info.dataUltimoDocumento.label";
    public static final String NO_GIORNALE_ASSOCIATO = PAGE_ID + ".info.noGiornaleAssociato.label";
    public static final String DIALOG_NO_MOVIMENTI_TITLE = PAGE_ID + ".dialogNoMovimenti.title";
    public static final String DIALOG_NO_MOVIMENTI_MESSAGE = PAGE_ID + ".dialogNoMovimenti.message";
    public static final String DIALOG_NO_STAMPABILE_MESSAGE = PAGE_ID + ".dialogNoStampabile.message";
    public static final String DIALOG_NO_SELEZIONATI_MESSAGE = PAGE_ID + ".dialogNoSelezionati.message";
    private IContabilitaBD contabilitaBD = null;
    private AziendaCorrente aziendaCorrente = null;
    // header
    private JSpinner spinnerAnno = null;
    // footer
    private StampaLibroGiornaleCommand stampaLibroGiornaleCommand = null;
    private StampaLibroGiornaleProvvisorioCommand stampaLibroGiornaleProvvisorioCommand = null;
    private RefreshCommand refreshCommand = null;
    private JideTableWidget<MesePM> meseTableWidget = null;
    private MyTreeStatoGiornaleIvaRenderer myGiornaleIvaRenderer = null;
    private CheckBoxTree checkBoxTree;
    private JTextPane infoTextArea;
    private List<GiornaleIva> giornaliIva;
    private final Dimension dimensionMesiTable = new Dimension(300, 215);
    private final Dimension dimensionRegistriTable = new Dimension(300, 215);
    private final Dimension dimensionInfoTable = new Dimension(300, 215);
    private JPanel panel = null;

    /**
     * Costruttore.
     */
    private GiornaleIvaPage() {
        super(PAGE_ID);
    }

    /**
     * Aggiunge uno stile al documento.
     *
     * @param doc
     *            styledDoc
     */
    private void addStylesToDocument(StyledDocument doc) {
        Style def = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);

        Style regular = doc.addStyle("regular", def);

        Style style = doc.addStyle("italic", regular);
        StyleConstants.setItalic(style, true);

        style = doc.addStyle("bold", regular);
        StyleConstants.setBold(style, true);

        style = doc.addStyle("small", regular);
        StyleConstants.setFontSize(style, 10);

        style = doc.addStyle("large", regular);
        StyleConstants.setFontSize(style, 16);

        style = doc.addStyle("iconNote", regular);
        StyleConstants.setAlignment(style, StyleConstants.ALIGN_LEFT);

        Icon noteIcon = getIconSource().getIcon("libriGiornalePage.note.icon");

        if (noteIcon != null) {
            StyleConstants.setIcon(style, noteIcon);
        }
    }

    /**
     * @param giornaleIva
     *            giornaleIva
     */
    private void aggiornaValoriGiornaleIva(GiornaleIva giornaleIva) {
        LOGGER.debug("--> Enter aggiornaValoriGiornale");

        infoTextArea.setText("");

        if (giornaleIva == null) {
            return;
        }

        StyledDocument doc = infoTextArea.getStyledDocument();
        addStylesToDocument(doc);

        try {
            if (giornaleIva.isNew()) {
                doc.insertString(doc.getLength(), getMessage(NO_GIORNALE_ASSOCIATO) + " ", doc.getStyle("bold"));
                return;
            }
            Format format = new DecimalFormat(ValutaAzienda.MASCHERA_VALUTA_GENERICA);

            doc.insertString(doc.getLength(), getMessage(NOTE_DATA_ULTIMO_MOVIMENTO) + " ", doc.getStyle("bold"));
            doc.insertString(doc.getLength(), giornaleIva.getDataUltimoDocumento() + "\n", doc.getStyle("regular"));

            doc.insertString(doc.getLength(), getMessage(NOTE_ULTIMA_PAGINA) + " ", doc.getStyle("bold"));
            doc.insertString(doc.getLength(), giornaleIva.getNumeroPagina() + "\n", doc.getStyle("regular"));

            doc.insertString(doc.getLength(), getMessage(NOTE_IMPONIBILE) + " ", doc.getStyle("bold"));
            doc.insertString(doc.getLength(), format.format(giornaleIva.getImponibile()) + "\n",
                    doc.getStyle("regular"));

            doc.insertString(doc.getLength(), getMessage(NOTE_IMPOSTA) + " ", doc.getStyle("bold"));
            doc.insertString(doc.getLength(), format.format(giornaleIva.getImposta()) + "\n", doc.getStyle("regular"));

            doc.insertString(doc.getLength(), getMessage(NOTE_TOTALE_DOCUMENTO) + " ", doc.getStyle("bold"));
            doc.insertString(doc.getLength(), format.format(giornaleIva.getTotaleDocumento()) + "\n",
                    doc.getStyle("regular"));

        } catch (BadLocationException e) {
            e.printStackTrace();
        }

        LOGGER.debug("--> Exit aggiornaValoriGiornale");
    }

    /**
     * Avvolora i parametri per la ricerca in base al registro iva selezionato nella tabella.
     *
     * @param giornaleIva
     *            giornaleIva
     *
     * @return ParametriRicercaMovimentiContabili
     */
    private ParametriRicercaMovimentiContabili avvaloraParametriRicerca(GiornaleIva giornaleIva) {
        Calendar calendar = new GregorianCalendar(giornaleIva.getAnno(), giornaleIva.getMese() - 1, 1);

        ParametriRicercaMovimentiContabili parametriRicercaMovimentiContabili = new ParametriRicercaMovimentiContabili();
        parametriRicercaMovimentiContabili.setAnnoCompetenza(((Integer) spinnerAnno.getValue()).toString());

        parametriRicercaMovimentiContabili.getDataRegistrazione().setDataIniziale(calendar.getTime());

        calendar.set(giornaleIva.getAnno(), giornaleIva.getMese() - 1,
                calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        parametriRicercaMovimentiContabili.getDataRegistrazione().setDataFinale(calendar.getTime());

        parametriRicercaMovimentiContabili.setEscludiMovimentiStampati(false);
        parametriRicercaMovimentiContabili.getStatiAreaContabile().add(StatoAreaContabile.CONFERMATO);
        parametriRicercaMovimentiContabili.getStatiAreaContabile().add(StatoAreaContabile.VERIFICATO);

        // cerca su tutti i tipi documento, cmq non mi interessa filtrare per
        // tipo documento visto che filtro
        // principalmente su registro iva
        parametriRicercaMovimentiContabili.setTipiDocumento(null);
        parametriRicercaMovimentiContabili.setRegistroIva(giornaleIva.getRegistroIva());
        parametriRicercaMovimentiContabili.setFiltraAbilitatiStampaRegistroIva(true);

        return parametriRicercaMovimentiContabili;
    }

    /**
     * Crea la parte del footer aggiungendo una button bar con i commands per la stampa.
     *
     * @return JComponent
     */
    private JComponent createButtonBar() {
        LOGGER.debug("--> Enter createButtonBar");

        CommandGroup commandGroup = new CommandGroup();

        stampaLibroGiornaleProvvisorioCommand = new StampaLibroGiornaleProvvisorioCommand();
        commandGroup.add(stampaLibroGiornaleProvvisorioCommand);

        stampaLibroGiornaleCommand = new StampaLibroGiornaleCommand();
        commandGroup.add(stampaLibroGiornaleCommand);

        refreshCommand = new RefreshCommand();
        commandGroup.add(refreshCommand);

        JPanel buttonPanel = getComponentFactory().createPanel(new BorderLayout());
        buttonPanel.add(commandGroup.createButtonBar(), BorderLayout.EAST);
        LOGGER.debug("--> Exit createButtonBar");
        return buttonPanel;
    }

    /**
     * Crea una check box tree per la visualizzazione e selezione dei registri iva da stampare.
     *
     * @return CheckBoxTree
     */
    private CheckBoxTree createCheckboxTree() {
        DefaultMutableTreeNode root = createTreeNode(null);
        DefaultTreeModel treeModel = new DefaultTreeModel(root);

        checkBoxTree = new CheckBoxTree(treeModel) {

            private static final long serialVersionUID = 1L;

            @Override
            public String convertValueToText(Object obj, boolean flag, boolean flag1, boolean flag2, int i,
                    boolean flag3) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) obj;

                if (node.getUserObject() instanceof TipoRegistroPM) {
                    return ((TipoRegistroPM) node.getUserObject()).getTipoRegistro().name();
                }

                if (node.getUserObject() instanceof GiornaleIva) {
                    return ((GiornaleIva) node.getUserObject()).getRegistroIva().getDescrizione();
                }
                return "";
            }

            @Override
            protected CheckBoxTreeCellRenderer createCellRenderer(TreeCellRenderer arg0) {
                return getMyCellRenderer();
            }

            @Override
            public Dimension getPreferredScrollableViewportSize() {
                return new Dimension(100, 100);
            }
        };
        checkBoxTree.setRootVisible(false);
        checkBoxTree.setShowsRootHandles(true);

        // qui lo aggiunge sulla selezione della riga
        checkBoxTree.addTreeSelectionListener(new CheckBoxTreeSelectionListener());

        TreeUtils.expandAll(checkBoxTree, true);
        SearchableUtils.installSearchable(checkBoxTree);
        return checkBoxTree;
    }

    @Override
    protected JComponent createControl() {
        LOGGER.debug("--> Enter createControl");
        if (panel == null) {
            panel = getComponentFactory().createPanel(new BorderLayout());
            JPanel panelData = getComponentFactory().createPanel(new BorderLayout());
            panel.add(panelData, BorderLayout.NORTH);

            panelData.add(createHeaderControl(), BorderLayout.NORTH);
            panelData.add(createButtonBar(), BorderLayout.SOUTH);

            JPanel panelTable = getComponentFactory().createPanel(new BorderLayout());
            panelTable.setPreferredSize(new Dimension(400, 400));
            /*
             * creo prima la tabella dei registri iva e solo dopo quella dei mesi cos? sulla
             * selezione automatica della prima riga della tabella dei mesi mi vengono caricati i
             * registri iva di gennaio
             */
            panelTable.add(createRegistriIvaTable(), BorderLayout.CENTER);
            panelTable.add(createTableMesi(), BorderLayout.WEST);
            panelTable.add(createInfo(), BorderLayout.EAST);

            panelData.add(panelTable, BorderLayout.CENTER);

            Border padding = BorderFactory.createEmptyBorder(20, 20, 5, 20);
            panel.setBorder(padding);
        }
        LOGGER.debug("--> Exit createControl");
        return panel;
    }

    /**
     * Crea l'header inserendo un jspinner per la selezione dell'anno, al cambio di anno viene
     * aggiornata la lista di mesiPM.
     *
     * @return JComponent
     */
    private JComponent createHeaderControl() {
        LOGGER.debug("--> Enter createHeaderControl");
        JPanel headerPanel = getComponentFactory().createPanel(new BorderLayout());

        headerPanel.add(new JLabel(getMessageSource().getMessage(PAGE_ID + ANNO, new Object[] {}, Locale.getDefault())),
                BorderLayout.WEST);

        SpinnerNumberModel model = new SpinnerNumberModel(new Integer(aziendaCorrente.getAnnoContabile()),
                new Integer(0), new Integer(aziendaCorrente.getAnnoContabile() + 100), new Integer(1));
        spinnerAnno = new JSpinner(model);
        spinnerAnno.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent event) {
                GiornaleIvaPage.LOGGER.debug("--> Change listener su spinner anno");

                // aggiorno i dati della tabella sul cambio di anno dello
                // spinner
                meseTableWidget.setRows(getMesi((Integer) spinnerAnno.getValue()));

                // cancello i dati stampati nelle info
                aggiornaValoriGiornaleIva(null);
            }
        });
        JPanel panelSpinner = getComponentFactory().createPanel(new BorderLayout());
        panelSpinner.add(spinnerAnno, BorderLayout.WEST);

        headerPanel.add(panelSpinner, BorderLayout.CENTER);

        Border padding = BorderFactory.createEmptyBorder(0, 0, 5, 0);
        headerPanel.setBorder(padding);
        LOGGER.debug("--> Exit createHeaderControl");
        return headerPanel;
    }

    /**
     * @return JComponent
     */
    private JComponent createInfo() {
        infoTextArea = new JTextPane();
        infoTextArea.setBackground(UIManager.getDefaults().getColor("Panel.background"));
        infoTextArea.setEditable(false);
        infoTextArea.setPreferredSize(dimensionInfoTable);

        JScrollPane scrollPane = getComponentFactory().createScrollPane(infoTextArea);
        ((PanjeaComponentFactory) getComponentFactory()).createTitledBorderFor(getMessage(PANEL_NOTE_TITLE),
                scrollPane);
        return scrollPane;
    }

    /**
     * Crea la treetable che visualizza i registri iva raggruppati per tipoRegistro e per ogni
     * registro ? visualizzato se esiste il giornale tramite una icona che ne rappresenta lo stato
     *
     * @return JComponent
     */
    private JComponent createRegistriIvaTable() {
        LOGGER.debug("--> Enter createRegistriIvaTable");
        checkBoxTree = createCheckboxTree();
        checkBoxTree.setOpaque(false);
        JScrollPane scrollPane = getComponentFactory().createScrollPane(checkBoxTree);
        scrollPane.setPreferredSize(dimensionRegistriTable);
        scrollPane.setMinimumSize(dimensionRegistriTable);
        ((PanjeaComponentFactory) getComponentFactory()).createTitledBorderFor(getMessage(PANEL_REGISTRI_TITLE),
                scrollPane);
        LOGGER.debug("--> Exit createRegistriIvaTable");
        return scrollPane;
    }

    /**
     * Crea la tabella dei 12 mesi per l'anno selezionato nel jspinner. Si preoccupa inoltre di
     * aggiornare la treeTable con i registriIva e relativi giornali iva sul cambio di selezione del
     * mese
     *
     * @return JComponent
     */
    private JComponent createTableMesi() {
        LOGGER.debug("--> Enter createTableMesi");
        JPanel panelMesi = getComponentFactory().createPanel(new BorderLayout());
        ((PanjeaComponentFactory) getComponentFactory()).createTitledBorderFor(getMessage(PANEL_TABLE_TITLE),
                panelMesi);
        if (meseTableWidget == null) {
            meseTableWidget = new JideTableWidget<MesePM>(PAGE_ID + ".table", new String[] { "mese", "anno" },
                    MesePM.class);
            meseTableWidget.addSelectionObserver(this);
            meseTableWidget.setRows(getMesi((Integer) spinnerAnno.getValue()));
            meseTableWidget.getComponent();
            meseTableWidget.getTable().getColumnModel().getColumn(0).setCellRenderer(new MeseTableCellRenderer());

            panelMesi.add(meseTableWidget.getTable(), BorderLayout.CENTER);
            panelMesi.setPreferredSize(dimensionMesiTable);
        }
        LOGGER.debug("--> Exit createTableMesi");
        return panelMesi;
    }

    /**
     * Il treeNode che definisce i dati da visualizzare nella tabella per riempire il model.
     *
     * @param giornaliIvaList
     *            giornaliIvaList
     * @return DefaultMutableTreeTableNode
     */
    public DefaultMutableTreeNode createTreeNode(List<GiornaleIva> giornaliIvaList) {
        LOGGER.debug("--> Enter createTreeNode");
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode();

        if (giornaliIvaList == null || giornaliIvaList.isEmpty()) {
            return rootNode;
        }

        DefaultMutableTreeNode nodeTipoRegistro = new DefaultMutableTreeNode();
        DefaultMutableTreeNode nodeGiornaleIva = null;

        TipoRegistro tipoRegistro = null;

        // registro iva di tipo riepilogativo
        GiornaleIva giornaleIvaRiepilogativo = null;

        // la lista deve essere ordinata per tipo registro
        for (GiornaleIva giornaleIva : giornaliIvaList) {
            LOGGER.debug("--> registro iva associato al giornale iva " + giornaleIva.getRegistroIva().getDescrizione());
            TipoRegistro tipoRegistroCorrente = giornaleIva.getRegistroIva().getTipoRegistro();

            // non aggiungo al tree il tipoRegistro riepilogativo e lo segno per successiva
            // rimozione dato che non mi
            // serve in questa pagina; lo tolgo per non trovarmelo all'interno dei messaggi che
            // stampo all'utente se non
            // trovo risultati nel caso in cui seleziono tutto il tree
            if (tipoRegistroCorrente.compareTo(TipoRegistro.RIEPILOGATIVO) == 0) {
                giornaleIvaRiepilogativo = giornaleIva;
                continue;
            }

            /*
             * se non ho ancora creato il nodo padre (tipoRegistro==null) oppure ho il nodo padre ma
             * il corrente e' diverso !tipoRegistroCorrente.equals(tipoRegistro) e il tipoRegistro
             * non e' RIEPILOGATIVO allora creo un nodo con il tipoRegistroCorrente
             */
            if (tipoRegistro == null || !tipoRegistroCorrente.equals(tipoRegistro)) {
                LOGGER.debug("--> Creo il nodo per tipo registro " + tipoRegistroCorrente.name());
                nodeTipoRegistro = new DefaultMutableTreeNode(new TipoRegistroPM(tipoRegistroCorrente));
                rootNode.add(nodeTipoRegistro);

                tipoRegistro = tipoRegistroCorrente;
            }

            LOGGER.debug("--> Creo il nodo per il registro iva associato al giornale iva "
                    + giornaleIva.getRegistroIva().getDescrizione());

            nodeGiornaleIva = new DefaultMutableTreeNode(giornaleIva);
            nodeTipoRegistro.add(nodeGiornaleIva);

            /*
             * dato che gli stati sono: 0 --> stampato 1 --> non stampato 2 --> non valido controllo
             * se lo stato corrente e' maggiore dello stato del nodo padre cosi' da settare lo stato
             * peggiore al tipo registro che raggruppa i giornali iva
             */
            int statoCorrente = giornaleIva.getStato();
            if (statoCorrente > ((TipoRegistroPM) nodeTipoRegistro.getUserObject()).getStato()) {
                ((TipoRegistroPM) nodeTipoRegistro.getUserObject()).setStato(statoCorrente);
            }
        }
        // tolgo dalla lista gestita in questa pagina il tipo RIEPILOGATIVO ,
        // non mi serve per nessuna ragione
        if (giornaleIvaRiepilogativo != null) {
            giornaliIvaList.remove(giornaleIvaRiepilogativo);
        }
        LOGGER.debug("--> Exit createTreeNode");
        return rootNode;
    }

    @Override
    public void dispose() {
        // non faccio niente
    }

    /**
     * @return the aziendaCorrente
     */
    public AziendaCorrente getAziendaCorrente() {
        return aziendaCorrente;
    }

    /**
     * @return the contabilitaBD
     */
    public IContabilitaBD getContabilitaBD() {
        return contabilitaBD;
    }

    /* metodi per collegare dei commands agli shortcuts definiti per gli editor */
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
     * Dalla lista di registri iva caricati recupero i giornali iva del tiporegistro scelto.
     *
     * @param tipoRegistro
     *            il tiporegistro scelto per recuperare i giornali iva associati
     * @return List<GiornaleIva>
     */
    private List<GiornaleIva> getGiornaliIva(TipoRegistro tipoRegistro) {
        List<GiornaleIva> giornaliDiTipoRegistro = new ArrayList<GiornaleIva>();

        for (GiornaleIva giornaleIva : giornaliIva) {
            if (giornaleIva.getRegistroIva().getTipoRegistro().equals(tipoRegistro)) {
                giornaliDiTipoRegistro.add(giornaleIva);
            }
        }

        return giornaliDiTipoRegistro;
    }

    /**
     * Recupera la lista di registri selezionati nella checkboxtree.
     *
     * @return List<GiornaleIva>
     */
    private List<GiornaleIva> getGiornaliSelezionati() {
        LOGGER.debug("--> Enter getGiornaliSelezionati");
        List<GiornaleIva> registriIvaDaStampare = new ArrayList<GiornaleIva>();

        TreePath[] treePaths = checkBoxTree.getCheckBoxTreeSelectionModel().getSelectionPaths();
        if (treePaths == null) {
            // torna una lista vuota dato che non ci sono registri da stampare
            // selezionati nella checkBoxTree
            LOGGER.debug("--> Exit getGiornaliSelezionati nessun elemento selezionato " + registriIvaDaStampare.size());
            return registriIvaDaStampare;
        }

        for (TreePath tp : treePaths) {
            DefaultMutableTreeNode obj = (DefaultMutableTreeNode) tp.getLastPathComponent();

            // recupera tutti i record del tipo registro
            if (obj.getUserObject() instanceof TipoRegistroPM) {
                TipoRegistroPM tipoRegistroPM = (TipoRegistroPM) obj.getUserObject();
                registriIvaDaStampare.addAll(getGiornaliIva(tipoRegistroPM.getTipoRegistro()));
            } else if (obj.getUserObject() instanceof GiornaleIva) {
                // aggiunge il giornale iva selezionato singolarmente
                GiornaleIva giornaleIva = (GiornaleIva) obj.getUserObject();
                registriIvaDaStampare.add(giornaleIva);
            } else {
                // aggiungo tutti i giornali dato che ho selezionato il root node
                registriIvaDaStampare.addAll(giornaliIva);
            }
        }
        LOGGER.debug("--> Exit getGiornaliSelezionati " + registriIvaDaStampare.size());
        return registriIvaDaStampare;
    }

    /**
     * Metodo che crea 12 mesi per l'anno selezionato.
     *
     * @param anno
     *            l'anno da settare
     * @return List<MesePM>
     */
    private List<MesePM> getMesi(int anno) {
        LOGGER.debug("--> Enter getMesi");
        List<MesePM> mesi = new ArrayList<MesePM>();
        for (int i = 1; i < 13; i++) {
            MesePM mesePM = new MesePM(i, anno);
            mesi.add(mesePM);
        }
        LOGGER.debug("--> Exit getMesi");
        return mesi;
    }

    /**
     * Ritorna il CheckBoxTreeCellRenderer per la CheckBoxTree che visualizza i registri iva.
     *
     * @return CheckBoxTreeCellRenderer
     */
    private CheckBoxTreeCellRenderer getMyCellRenderer() {
        if (myGiornaleIvaRenderer == null) {
            myGiornaleIvaRenderer = new MyTreeStatoGiornaleIvaRenderer();
        }
        return myGiornaleIvaRenderer;
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
        // non faccio niente
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
    public boolean onSave() {
        return true;
    }

    @Override
    public boolean onUndo() {
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

    /**
     * Presenta all'utente la lista di registri iva che vengono ricevuti.
     *
     * @param giornaliMessaggio
     *            giornaliMessaggio
     * @param message
     *            message
     */
    private void printMessageForGiornaliNonStampati(List<GiornaleIva> giornaliMessaggio, String message) {
        if (!giornaliMessaggio.isEmpty()) {
            message = message + "\n";
            for (GiornaleIva giornaleXmessaggio : giornaliMessaggio) {

                Calendar cal = Calendar.getInstance();
                cal.set(giornaleXmessaggio.getAnno(), giornaleXmessaggio.getMese() - 1, 1);
                Format format = new SimpleDateFormat("MM/yy");
                String registroNonStampato = format.format(cal.getTime()) + " - "
                        + giornaleXmessaggio.getRegistroIva().getNumero() + " - "
                        + giornaleXmessaggio.getRegistroIva().getDescrizione();
                message = message + registroNonStampato + "<br>";
            }

            MessageDialog dialog = new MessageDialog(getMessage(DIALOG_NO_MOVIMENTI_TITLE), message);
            dialog.showDialog();
        }
    }

    @Override
    public void refreshData() {
        // non faccio niente
    }

    /* metodi salvare le preferenze per alcuni componenti tramite settings */
    @Override
    public void restoreState(Settings settings) {
        // non faccio niente
    }

    @Override
    public void saveState(Settings settings) {
        // non faccio niente
    }

    /**
     * @param aziendaCorrente
     *            the aziendaCorrente to set
     */
    public void setAziendaCorrente(AziendaCorrente aziendaCorrente) {
        this.aziendaCorrente = aziendaCorrente;
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
        // non faccio niente
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * @param definitivo
     *            se stampare i registri in modo definitivo
     */
    private void stampaGiornaliSelezionati(final boolean definitivo) {
        LOGGER.debug("--> Enter stampaGiornaliSelezionati");
        List<GiornaleIva> giornaliIvaSelezionati = getGiornaliSelezionati();

        // se non ho selezionato nessun registro iva avverto l'utente di
        // selezionarne almeno uno
        if (giornaliIvaSelezionati.isEmpty()) {
            MessageDialog dialog = new MessageDialog(getMessage(DIALOG_NO_MOVIMENTI_TITLE),
                    getMessage(DIALOG_NO_SELEZIONATI_MESSAGE));
            dialog.showDialog();
        }

        // lista di giornali iva non stampati a causa del fatto che non sono
        // stampabili
        // (il giornale iva dell'anno precedente non e' stato stampato)
        List<GiornaleIva> giornaliNonStampabili = new ArrayList<GiornaleIva>();

        for (final GiornaleIva giornaleIva : giornaliIvaSelezionati) {

            ParametriRicercaMovimentiContabili parametri = avvaloraParametriRicerca(giornaleIva);
            List<TotaliCodiceIvaDTO> list = contabilitaBD.ricercaRigheIva(parametri);
            LOGGER.debug("--> trovate " + list.size());

            // giornali che non stampo dato che il precedente non e' stampato e
            // stiamo eseguendo una stampa definitiva
            if (!giornaleIva.isStampabile() && definitivo) {
                LOGGER.warn(
                        "--> Non stampo il registro iva dato che il precedente non e' stampato e voglio stampare il definitivo");
                giornaliNonStampabili.add(giornaleIva);
                continue;
            }

            // aggiungo le eventuali righe per riempire l'ultima pagina
            int righeFiller = list.size() % 48;

            for (int i = 0; i < 48 - righeFiller; i++) {
                TotaliCodiceIvaDTO totaliCodiceIvaDTO = new TotaliCodiceIvaDTO();
                totaliCodiceIvaDTO.setFiller(true);
                list.add(totaliCodiceIvaDTO);
            }

            // tengo il numero di righe della lista
            final int nrRighe = list.size();

            // se non ho righe devo cmq stampare il registro iva,dato che jasper
            // senza dettaglio non stampa nulla,
            // inserisco una riga vuota per far stampare il report,mi rimane la
            // variabile nrRighe da testare per settare
            // i dati del report in funzione del report senza righe
            if (nrRighe == 0) {
                LOGGER.warn("--> ");
                TotaliCodiceIvaDTO totaliCodiceIvaDTO = new TotaliCodiceIvaDTO();
                totaliCodiceIvaDTO.setImponibile(BigDecimal.ZERO);
                totaliCodiceIvaDTO.setImposta(BigDecimal.ZERO);
                list.add(totaliCodiceIvaDTO);
            }

            final GiornaleIva giornaleIvaPrecedente = contabilitaBD.caricaGiornaleIvaPrecedente(giornaleIva);

            // carica il riepilogo per questo registro iva
            List<TotaliCodiceIvaDTO> righeIvaPerRiepilogoCodiciIva = contabilitaBD
                    .ricercaRigheRiepilogoCodiciIva(parametri);
            LOGGER.debug(
                    "--> righe trovate da presentare nel riepilogo codici iva " + righeIvaPerRiepilogoCodiciIva.size());

            JecLocalReport jecReport = new JecLocalReport();
            Calendar cal = Calendar.getInstance();
            cal.set(giornaleIva.getAnno(), giornaleIva.getMese() - 1, 1);
            Format format = new SimpleDateFormat("MM/yy");
            jecReport.setReportName(format.format(cal.getTime()) + " - " + giornaleIva.getRegistroIva().getNumero()
                    + " - " + giornaleIva.getRegistroIva().getDescrizione());

            jecReport.setXmlReportResource(new ClassPathResource(
                    "/it/eurotn/panjea/contabilita/rich/reports/resources/StampaRegistroIva.jasper"));
            jecReport.setMessages("it.eurotn.panjea.messages.cont.reports.messages");

            jecReport.setDataReport(list);

            // SUB_REPORT CONFIG
            JecLocalReport jecJasperSubReportRiepilogo = new JecLocalReport();
            jecJasperSubReportRiepilogo.setXmlReportResource(new ClassPathResource(
                    "/it/eurotn/panjea/contabilita/rich/reports/resources/SubReportRiepilogoCodici.jasper"));
            jecReport.getSubReports().put("REPORT_SUBRIEPILOGO", jecJasperSubReportRiepilogo);

            HeaderBean headerBean = new HeaderBean();
            headerBean.setCodiceAzienda(aziendaCorrente.getDenominazione());
            headerBean.setUtenteCorrente(PanjeaSwingUtil.getUtenteCorrente().getUserName());
            FooterBean footerBean = new FooterBean();
            jecReport.setDataHeader(headerBean);
            jecReport.setDataFooter(footerBean);
            jecReport.getReportParameters().put("annoRegistroIva", giornaleIva.getAnno());
            jecReport.getReportParameters().put("numeroRegistroIva", giornaleIva.getRegistroIva().getNumero());
            jecReport.getReportParameters().put("descrizioneRegistroIva",
                    giornaleIva.getRegistroIva().getDescrizione());
            jecReport.getReportParameters().put("descrizioneTipoRegistro",
                    giornaleIva.getRegistroIva().getTipoRegistro().name());
            jecReport.getReportParameters().put("meseRegistroIva", getMessageSource()
                    .getMessage("mese." + giornaleIva.getMese(), new Object[] {}, Locale.getDefault()));
            jecReport.getReportParameters().put("aziendaCorrente", aziendaCorrente);
            jecReport.getReportParameters().put("ultimoNumeroPagina", new Integer(0));
            jecReport.getReportParameters().put("riepilogoCodiciIvaList", righeIvaPerRiepilogoCodiciIva);

            final StampaRegistroIvaScriptlet stampaGiornaleIvaScriptlet = new StampaRegistroIvaScriptlet();
            // aggiungo la classe di scriptlet al report
            jecReport.getReportParameters().put("REPORT_SCRIPTLET", stampaGiornaleIvaScriptlet);

            if (definitivo) {
                // se e' definitivo prendo l'ultimo numero pagine
                // del giornale precedente.
                if (giornaleIvaPrecedente != null && giornaleIva.getMese() > 1) {
                    jecReport.getReportParameters().put("ultimoNumeroPagina", giornaleIvaPrecedente.getNumeroPagina());
                    jecReport.getReportParameters().put("saldoImponibileRegistroIvaPrecedente",
                            giornaleIvaPrecedente.getImponibile());
                    jecReport.getReportParameters().put("saldoImpostaRegistroIvaPrecedente",
                            giornaleIvaPrecedente.getImposta());
                    jecReport.getReportParameters().put("saldoTotaleDocumentoRegistroIvaPrecedente",
                            giornaleIvaPrecedente.getTotaleDocumento());
                } else {
                    jecReport.getReportParameters().put("saldoImponibileRegistroIvaPrecedente", BigDecimal.ZERO);
                    jecReport.getReportParameters().put("saldoImpostaRegistroIvaPrecedente", BigDecimal.ZERO);
                    jecReport.getReportParameters().put("saldoTotaleDocumentoRegistroIvaPrecedente", BigDecimal.ZERO);
                }

                // rimuovo l'immagine di background dato che rimane nel report
                // singleton una volta settata e quindi
                // anche se eseguo la stampa definitiva se prima ne ho fatta una
                // provvisoria mi ritrovo con
                // il background facsimile
                jecReport.getReportParameters().remove("imageBackGround");
            } else {
                ImageSource imageSource = (ImageSource) getService(
                        org.springframework.richclient.image.ImageSource.class);
                Image facsimile = imageSource.getImage("stampaGiornale.background.image");
                jecReport.getReportParameters().put("imageBackGround", facsimile);
            }

            jecReport.execute(new Closure() {

                @Override
                public Object call(Object arg0) {

                    if (definitivo) {
                        if (nrRighe == 0 && giornaleIvaPrecedente != null) {
                            giornaleIva.setDataUltimoDocumento(giornaleIvaPrecedente.getDataUltimoDocumento());
                            giornaleIva.setProtocolloMovimento(giornaleIvaPrecedente.getProtocolloMovimento());
                        } else {
                            giornaleIva.setDataUltimoDocumento(stampaGiornaleIvaScriptlet.getUltimaDataMovimento());
                            giornaleIva.setProtocolloMovimento(stampaGiornaleIvaScriptlet.getUltimoNumeroMovimento());
                        }
                        giornaleIva.setNumeroPagina(stampaGiornaleIvaScriptlet.getUltimoNumeroPagina());
                        giornaleIva.setImponibile(stampaGiornaleIvaScriptlet.getImponibileAttuale());
                        giornaleIva.setImposta(stampaGiornaleIvaScriptlet.getImpostaAttuale());
                        giornaleIva.setTotaleDocumento(stampaGiornaleIvaScriptlet.getTotaleAttuale());

                        giornaleIva.setStato(GiornaleIva.STAMPATO);
                        contabilitaBD.aggiornaStampaRegistroIva(giornaleIva,
                                stampaGiornaleIvaScriptlet.getMapAreeContabili(),
                                stampaGiornaleIvaScriptlet.getMapRigheIva());
                    }
                    return null;
                }
            });
        }

        String messageNonStampabile = getMessage(DIALOG_NO_STAMPABILE_MESSAGE);

        // stampo il messaggio riepilogando i registri non stampati per mancanza
        // di righe
        printMessageForGiornaliNonStampati(giornaliNonStampabili, messageNonStampabile);

        // ripulisco la selezione, dato che comunque ricarico i registri iva
        checkBoxTree.getCheckBoxTreeSelectionModel().clearSelection();

        // aggiorno la checkboxtree di registri iva
        refreshCommand.execute();
        LOGGER.debug("--> Exit stampaGiornaliSelezionati");
    }

    @Override
    public void unLock() {
        // non faccio niente
    }

    @Override
    public void update(Observable obs, Object arg) {
        if (arg != null) {
            GiornaleIvaPage.LOGGER
                    .debug("--> cambiata la selezione del mese, ricarico i registri iva per il mese " + arg);
            MesePM mesePM = (MesePM) arg;
            giornaliIva = contabilitaBD.caricaGiornaliIva(mesePM.getAnno(), mesePM.getMese());

            // aggiorno il treetable con i nuovi giornali iva del mese
            // selezionato
            updateCheckBoxTreeData(giornaliIva);

            // cancello i dati stampati nelle info
            aggiornaValoriGiornaleIva(null);
        }
    }

    /**
     * Metodo per risettare il modello dei dati per il treeTable da chiamare per aggiornare il tree.
     *
     * @param giornaliIvaSelezionati
     *            giornaliIvaSelezionati
     */
    public void updateCheckBoxTreeData(List<GiornaleIva> giornaliIvaSelezionati) {
        LOGGER.debug("--> Enter updateCheckBoxTreeData");
        if (checkBoxTree != null) {
            ((DefaultTreeModel) checkBoxTree.getModel()).setRoot(createTreeNode(giornaliIvaSelezionati));
            TreeUtils.expandAll(checkBoxTree, true);
        }
        LOGGER.debug("--> Exit updateCheckBoxTreeData");
    }
}
