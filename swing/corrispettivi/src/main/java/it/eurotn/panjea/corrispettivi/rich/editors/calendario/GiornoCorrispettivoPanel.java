package it.eurotn.panjea.corrispettivi.rich.editors.calendario;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.text.Format;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.springframework.context.MessageSource;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.dialog.CloseAction;
import org.springframework.richclient.factory.ComponentFactory;
import org.springframework.richclient.image.IconSource;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.domain.ValutaAzienda;
import it.eurotn.panjea.anagrafica.rich.commands.OpenAreeDocumentoCommand;
import it.eurotn.panjea.corrispettivi.domain.Corrispettivo;
import it.eurotn.panjea.corrispettivi.domain.GiornoCorrispettivo;
import it.eurotn.panjea.corrispettivi.rich.bd.CorrispettiviBD;
import it.eurotn.panjea.corrispettivi.rich.bd.ICorrispettiviBD;
import it.eurotn.panjea.corrispettivi.rich.editors.corrispettivo.CorrispettivoPage;
import it.eurotn.panjea.corrispettivi.rich.editors.corrispettivo.CorrispettivoTitledPageApplicationDialog;
import it.eurotn.panjea.utils.PanjeaSwingUtil;

/**
 *
 * @author Fattazzo <g.fattarsi@eurotn.it>
 */
public class GiornoCorrispettivoPanel extends JPanel {

    /**
     * Cliccando due volte sul documento visualizzato nella lista viene aperto nell'editor.
     */
    private class ListDocumentoMouseAdapter extends MouseAdapter {

        private final JList<Documento> list;

        /**
         * Costruttore.
         *
         * @param list
         *            lista
         */
        public ListDocumentoMouseAdapter(final JList<Documento> list) {
            this.list = list;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                if (!list.isSelectionEmpty()) {
                    Documento documentoSelezionato = list.getSelectedValue();
                    Map<Object, Object> params = new HashMap<Object, Object>();
                    params.put(OpenAreeDocumentoCommand.PARAM_ID_DOCUMENTO, documentoSelezionato.getId());

                    OpenAreeDocumentoCommand openAreeDocumentoCommand = new OpenAreeDocumentoCommand();
                    openAreeDocumentoCommand.execute(params);
                }
            }
        }
    }

    /**
     * Render applicato alla lista per visualizzare il documento.
     */
    private class ListDocumentoRenderer extends DefaultListCellRenderer {

        private static final long serialVersionUID = -4915926793916759465L;

        @Override
        public Component getListCellRendererComponent(@SuppressWarnings("rawtypes") JList list, Object value, int index,
                boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            Documento documento = (Documento) value;
            label.setIcon(RcpSupport.getIcon(Documento.class.getName()));

            String numeroDoc = messageSource.getMessage(NUMERO_DOCUMENTO_LABEL, new Object[] {}, Locale.getDefault());
            String totaleDoc = messageSource.getMessage(TOTALE_DOCUMENTO_LABEL, new Object[] {}, Locale.getDefault());

            label.setText(numeroDoc + " " + documento.getCodice().getCodice() + " " + totaleDoc + " "
                    + importoFormat.format(documento.getTotale().getImportoInValutaAzienda()));
            Font font = label.getFont();
            label.setFont(new Font(font.getFontName(), font.getStyle(), 12));
            label.setOpaque(false);

            return label;
        }
    }

    private static final long serialVersionUID = 222311636537816816L;
    private static final String LABEL_DOCUMENTI_PRESENTI = "giornoCorrispettivoPanel.documentiPresenti.label";
    private static final String NUMERO_DOCUMENTO_LABEL = "giornoCorrispettivoPanel.numeroDocumento.label";
    private static final String TOTALE_DOCUMENTO_LABEL = "giornoCorrispettivoPanel.totale.label";
    private static final String CORRISPETTIVO_LABEL = "giornoCorrispettivoPanel.corrispettivo.label";
    public static final String UPDATE_CORRISPETTIVO_PROPERTY = "updateCorrispettivoProperty";
    private final GiornoCorrispettivo giornoCorrispettivo;
    private final CalendarioCorrispettivoMediator calendarioCorrispettivoMediator;
    private boolean today;
    private final IconSource iconSource;
    private final MessageSource messageSource;

    private final ICorrispettiviBD corrispettiviBD;

    private JLabel labelImportoCorrispettivo;

    private Format importoFormat = new DecimalFormat(ValutaAzienda.MASCHERA_VALUTA_GENERICA);

    /**
     * Costruttore.
     *
     * @param giornoCorrispettivo
     *            giornoCorrispettivo
     * @param calendarioCorrispettivoMediator
     *            calendarioCorrispettivoMediator
     */
    public GiornoCorrispettivoPanel(final GiornoCorrispettivo giornoCorrispettivo,
            final CalendarioCorrispettivoMediator calendarioCorrispettivoMediator) {
        super(new BorderLayout());
        this.calendarioCorrispettivoMediator = calendarioCorrispettivoMediator;
        this.giornoCorrispettivo = giornoCorrispettivo;
        this.today = false;
        this.iconSource = (IconSource) ApplicationServicesLocator.services().getService(IconSource.class);
        this.messageSource = (MessageSource) ApplicationServicesLocator.services().getService(MessageSource.class);
        this.corrispettiviBD = RcpSupport.getBean(CorrispettiviBD.BEAN_ID);
        buildUI();

        this.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent arg0) {
                super.mouseClicked(arg0);
                GiornoCorrispettivoPanel.this.calendarioCorrispettivoMediator
                        .setSelectedDay(GiornoCorrispettivoPanel.this);
            }
        });
        this.addKeyListener(new GiornoCorrispettivoPanelKeyListener(this));
    }

    /**
     *
     */
    private void buildUI() {

        // cancello tutti gli eventuali componenti
        this.removeAll();

        this.setOpaque(true);
        // setto il colore del giorno
        fillColor();

        // creo il pannello che contiene la label con il numero del giorno
        JPanel labelGiornoPanel = new JPanel(new BorderLayout());
        labelGiornoPanel.setOpaque(false);
        labelGiornoPanel.add(createLabelGiorno(), BorderLayout.WEST);
        // labelGiornoPanel.add(createImportPanel(), BorderLayout.EAST);

        // creo il pannello che contiene in alto il corrispettivo da inserire/modificare e
        // in basso la lista degli eventuali documenti presenti per il giorno.
        JPanel dataPanel = new JPanel(new BorderLayout());
        dataPanel.add(createCorrispettivoPanel(), BorderLayout.NORTH);
        dataPanel.add(createAreeContabiliPanel(), BorderLayout.CENTER);
        dataPanel.setOpaque(false);

        JPanel rootPanel = new JPanel(new BorderLayout());
        rootPanel.add(labelGiornoPanel, BorderLayout.NORTH);
        rootPanel.add(dataPanel, BorderLayout.CENTER);
        rootPanel.setOpaque(false);

        // aggiungo il pannello
        this.add(rootPanel, BorderLayout.CENTER);

    }

    /**
     * Crea i controlli per l'area contabile.
     *
     * @return controlli creati
     */
    private JComponent createAreeContabiliPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        if (giornoCorrispettivo.getDocumenti() != null && giornoCorrispettivo.getDocumenti().size() > 0) {
            JLabel label = new JLabel(
                    messageSource.getMessage(LABEL_DOCUMENTI_PRESENTI, new Object[] {}, Locale.getDefault()));
            Font font = label.getFont();
            label.setFont(new Font(font.getFontName(), font.getStyle(), 12));
            panel.add(label, BorderLayout.NORTH);

            DefaultListModel<Documento> documentiListModel = new DefaultListModel<>();

            for (Documento documento : giornoCorrispettivo.getDocumenti()) {
                documentiListModel.addElement(documento);
            }

            JList<Documento> listDoc = new JList<>(documentiListModel);
            listDoc.setCellRenderer(new ListDocumentoRenderer());
            listDoc.setOpaque(false);
            listDoc.addMouseListener(new ListDocumentoMouseAdapter(listDoc));
            listDoc.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

                @Override
                public void valueChanged(ListSelectionEvent e) {
                    // se questo evento e' parte di una serie, associati alla stessa modifica
                    // non devo eseguire nessuna operazione
                    if (e != null && e.getValueIsAdjusting()) {
                        return;
                    }
                    GiornoCorrispettivoPanel.this.calendarioCorrispettivoMediator
                            .setSelectedDay(GiornoCorrispettivoPanel.this);
                }
            });

            JScrollPane scrollPane = getComponentFactory().createScrollPane(listDoc);
            scrollPane.setBorder(BorderFactory.createEmptyBorder());
            scrollPane.getViewport().setOpaque(false);
            scrollPane.setOpaque(false);
            scrollPane.setBackground(CalendarioCorrispettiviPanel.TODAY_COLOR);
            panel.add(scrollPane, BorderLayout.CENTER);
        }

        panel.setBackground(CalendarioCorrispettiviPanel.TODAY_COLOR);
        panel.setPreferredSize(new Dimension(200, 49));
        panel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        return panel;
    }

    /**
     * Crea i controlli per il corrispettivo.
     *
     * @return controlli creati
     */
    private JComponent createCorrispettivoPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 2));

        JLabel label = new JLabel(messageSource.getMessage(CORRISPETTIVO_LABEL, new Object[] {}, Locale.getDefault()));
        label.setOpaque(false);
        Font font = label.getFont();
        label.setFont(new Font(font.getFontName(), font.getStyle(), 12));
        panel.add(label, BorderLayout.WEST);

        if (giornoCorrispettivo.getCorrispettivo().getTotale() == null) {
            labelImportoCorrispettivo = new JLabel("0,00");
        } else {
            labelImportoCorrispettivo = new JLabel(
                    importoFormat.format(giornoCorrispettivo.getCorrispettivo().getTotale()));
        }
        labelImportoCorrispettivo.setHorizontalAlignment(SwingConstants.RIGHT);
        labelImportoCorrispettivo.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 2));
        labelImportoCorrispettivo.setFont(new Font(font.getFontName(), font.getStyle(), 12));
        panel.add(labelImportoCorrispettivo, BorderLayout.CENTER);

        JLabel labelEdit = new JLabel(iconSource.getIcon("edit"));
        labelEdit.setCursor(new Cursor(Cursor.HAND_CURSOR));
        labelEdit.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                openCorrispettivoPage();
            }
        });
        panel.add(labelEdit, BorderLayout.EAST);

        return panel;
    }

    private Component createImportPanel() {

        JLabel labelEdit = new JLabel(iconSource.getIcon("import"));
        labelEdit.setToolTipText(
                "Importa i corrispettivi per il giorno " + new Integer(giornoCorrispettivo.getNumero()).toString());
        labelEdit.setCursor(new Cursor(Cursor.HAND_CURSOR));
        labelEdit.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                importaCorrispettivi(giornoCorrispettivo.getCorrispettivo().getData());
            }
        });

        return labelEdit;
    }

    /**
     * Crea la label che visualizza il numero del giorno.
     *
     * @return label creata.
     */
    private JComponent createLabelGiorno() {
        JPanel panel = getComponentFactory().createPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panel.setOpaque(false);

        JLabel labelGiorno = new JLabel(new Integer(giornoCorrispettivo.getNumero()).toString());
        Font font = labelGiorno.getFont();
        labelGiorno.setFont(new Font(font.getFontName(), font.getStyle(), 20));
        labelGiorno.setForeground(CalendarioCorrispettiviPanel.DAY_NUMBER_COLOR);
        labelGiorno.setOpaque(false);
        panel.add(labelGiorno);

        panel.add(createImportPanel());

        return panel;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final GiornoCorrispettivoPanel other = (GiornoCorrispettivoPanel) obj;
        if (this.giornoCorrispettivo != other.giornoCorrispettivo
                && (this.giornoCorrispettivo == null || !this.giornoCorrispettivo.equals(other.giornoCorrispettivo))) {
            return false;
        }
        return true;
    }

    /**
     * Assegna il colore in base al giornoCorrispettivo.
     *
     */
    private void fillColor() {
        if (today) {
            this.setBackground(CalendarioCorrispettiviPanel.TODAY_COLOR);
            this.setBorder(BorderFactory.createLineBorder(CalendarioCorrispettiviPanel.BORDER_COLOR, 2));
        } else {
            if (giornoCorrispettivo.isFeriale()) {
                this.setBackground(CalendarioCorrispettiviPanel.FERIALE_COLOR);
            } else {
                this.setBackground(CalendarioCorrispettiviPanel.FESTIVO_COLOR);
            }
            this.setBorder(BorderFactory.createLineBorder(CalendarioCorrispettiviPanel.BORDER_COLOR, 1));
        }
    }

    /**
     * @return the calendarioCorrispettivoMediator
     */
    public CalendarioCorrispettivoMediator getCalendarioCorrispettivoMediator() {
        return calendarioCorrispettivoMediator;
    }

    /**
     * Restituisce il component factory dell'applicazione.
     *
     * @return component factory
     */
    private ComponentFactory getComponentFactory() {
        return (ComponentFactory) Application.services().getService(ComponentFactory.class);
    }

    /**
     * @return the giornoCorrispettivo
     */
    public GiornoCorrispettivo getGiornoCorrispettivo() {
        return giornoCorrispettivo;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        return hash;
    }

    /**
     * Importa i corrispettivi per la data indicata.
     *
     * @param data
     *            data di importazione
     */
    private void importaCorrispettivi(Date data) {
        corrispettiviBD.importa(data);

        GiornoCorrispettivoPanel.this.firePropertyChange(GiornoCorrispettivoPanel.UPDATE_CORRISPETTIVO_PROPERTY, null,
                giornoCorrispettivo);
    }

    /**
     * Apre la papgina del giorno corrispettivo.
     */
    public void openCorrispettivoPage() {
        Corrispettivo corrispettivoClone = (Corrispettivo) PanjeaSwingUtil
                .cloneObject(giornoCorrispettivo.getCorrispettivo());
        CorrispettivoTitledPageApplicationDialog corrispettivoTitledPageApplicationDialog = new CorrispettivoTitledPageApplicationDialog(
                new CorrispettivoPage(corrispettivoClone), giornoCorrispettivo, corrispettiviBD);
        corrispettivoTitledPageApplicationDialog.setPreferredSize(new Dimension(430, 250));
        corrispettivoTitledPageApplicationDialog.setCloseAction(CloseAction.HIDE);
        corrispettivoTitledPageApplicationDialog.showDialog();

        if (corrispettivoTitledPageApplicationDialog.isConfirmed()) {
            // imposto la label importo con il nuovo valore formattato del totale
            Format format = new DecimalFormat(ValutaAzienda.MASCHERA_VALUTA_GENERICA);
            labelImportoCorrispettivo.setText(format.format(corrispettivoTitledPageApplicationDialog.getTotale()));
            // notifico
            GiornoCorrispettivoPanel.this.firePropertyChange(GiornoCorrispettivoPanel.UPDATE_CORRISPETTIVO_PROPERTY,
                    null, corrispettivoTitledPageApplicationDialog.getGiornoCorrispettivo());
        }
        corrispettivoTitledPageApplicationDialog.disposeDialog();
        corrispettivoTitledPageApplicationDialog = null;
    }

    /**
     * Setta la selezione del giorno corrispettivo.
     *
     * @param isSelected
     *            <code>true</code> se selezionato, <code>false</code> altrimenti
     */
    public void setSelected(boolean isSelected) {
        if (isSelected) {
            this.setBackground(CalendarioCorrispettiviPanel.SELECTED_DAY_COLOR);
            this.requestFocus();
        } else {
            fillColor();
        }
    }

    /**
     * Setta il giorno corrispettivo come giorno corrente.
     *
     * @param today
     *            <code>true</code> se è il giorno corrente
     */
    public void setToday(boolean today) {
        this.today = today;
        fillColor();
    }
}
