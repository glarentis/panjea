package it.eurotn.panjea.rich.editors.stampe;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.InputEvent;
import java.awt.font.TextAttribute;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.jdesktop.swingx.HorizontalLayout;
import org.jdesktop.swingx.VerticalLayout;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.list.ComboBoxListModel;
import org.springframework.richclient.util.RcpSupport;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jidesoft.converter.ObjectConverterManager;

import it.eurotn.panjea.anagrafica.documenti.domain.ITipoAreaDocumento;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.rich.stampe.ILayoutStampeManager;
import it.eurotn.panjea.rich.stampe.LayoutStampeManager;
import it.eurotn.panjea.rich.stampe.LayoutStampeManager.TipoLayoutPrefefinito;
import it.eurotn.panjea.stampe.domain.LayoutStampa;
import it.eurotn.panjea.stampe.domain.LayoutStampaDocumento;

public class TipoAreaStampeComponent extends JPanel {

    private class SelectLayoutCommand extends ActionCommand {
        private LayoutStampa layout;
        private ComboBoxListModel model;

        /**
         * Costruttore.
         *
         * @param model
         *            stampante sel.
         * @param layout
         *            layout gestito dal pulsante
         */
        public SelectLayoutCommand(final LayoutStampa layout, ComboBoxListModel model) {
            this.layout = layout;
            this.model = model;
            setIcon(RcpSupport.getIcon("report"));
        }

        @Override
        protected void doExecuteCommand() {
            layout.setStampante((String) model.getSelectedItem());
            int key = (int) getParameter(MODIFIERS_PARAMETER_KEY);
            stampaAreaMagazzinoAction.stampa(layout, ((key & InputEvent.CTRL_MASK) != 0));
        }
    }

    private static final long serialVersionUID = 1651286352930407393L;

    public static final String PROPERTY_LAYOUT_STAMPA_SELECTED = "layoutStampaSelected";

    private LayoutStampa layoutStampaPredefinito;

    private boolean showSelectLayoutButton = false;

    private PropertyChangeSupport selectLayoutStampaChangeSupport;

    private ILayoutStampeManager layoutStampeManager;

    private StampaAreaDocumentoAction stampaAreaMagazzinoAction;

    /**
     * Costruttore.
     *
     * @param tipoAreaDocumento
     *            tipo area di riferimento
     * @param layoutStampe
     *            layouts
     * @param showSelectLayoutButton
     *            indica se visualizzare il pulsante per la selezione del layout
     * @param stampaAreaMagazzinoAction
     *            action per lanvciare la stampa
     */
    public TipoAreaStampeComponent(final ITipoAreaDocumento tipoAreaDocumento,
            final List<LayoutStampaDocumento> layoutStampe, final boolean showSelectLayoutButton,
            final StampaAreaDocumentoAction stampaAreaMagazzinoAction) {
        super(new BorderLayout());
        this.stampaAreaMagazzinoAction = stampaAreaMagazzinoAction;
        this.layoutStampeManager = RcpSupport.getBean(LayoutStampeManager.BEAN_ID);
        this.showSelectLayoutButton = showSelectLayoutButton;

        layoutStampaPredefinito = layoutStampeManager.getLayoutStampaPredefinito(layoutStampe,
                TipoLayoutPrefefinito.PREDEFINITO);

        createControl(tipoAreaDocumento, layoutStampe);

        selectLayoutStampaChangeSupport = new PropertyChangeSupport(this);
    }

    /**
     * Aggiunge un listener al property change.
     *
     * @param listener
     *            listener da aggiungere
     */
    public void addSelectLayoutStampaListener(PropertyChangeListener listener) {
        selectLayoutStampaChangeSupport.addPropertyChangeListener(listener);
    }

    /**
     * Crea i controlli.
     *
     * @param tipoAreaDocumento
     *            tipo area documento
     * @param layoutStampe
     *            layouts
     */
    private void createControl(final ITipoAreaDocumento tipoAreaDocumento,
            final List<LayoutStampaDocumento> layoutStampe) {
        setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));

        JComponent titleComponent = createTitleComponent(tipoAreaDocumento);
        this.add(titleComponent, BorderLayout.NORTH);

        JPanel layoutStampePanel = new JPanel(new VerticalLayout());
        for (final LayoutStampaDocumento layoutStampa : layoutStampe) {
            layoutStampePanel.add(createLayoutStampaComponent(layoutStampa));
        }
        layoutStampePanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 8, 0));

        this.add(layoutStampePanel, BorderLayout.CENTER);
    }

    /**
     * Crea i componenti per la gestione delle layout.
     *
     * @param layoutStampa
     *            layout
     * @return componenti creati
     */
    private JComponent createLayoutStampaComponent(final LayoutStampaDocumento layoutStampa) {

        FormLayout layout = new FormLayout("100dlu, 0dlu, 100dlu, pref, pref", "4dlu,pref");
        PanelBuilder builder = new PanelBuilder(layout);

        CellConstraints cc = new CellConstraints();
        JLabel reportNameLabel = new JLabel(layoutStampa.getReportName(),
                RcpSupport.getIcon(LayoutStampa.class.getName()), SwingConstants.LEFT);
        if (layoutStampa.getPredefinito()) {
            Font f = reportNameLabel.getFont();
            Map<TextAttribute, Integer> fontAttributes = new HashMap<TextAttribute, Integer>();
            fontAttributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
            reportNameLabel.setFont(f.deriveFont(f.getStyle() ^ Font.BOLD ^ Font.ITALIC).deriveFont(fontAttributes));
        }
        builder.add(reportNameLabel, cc.xy(1, 2));

        ComboBoxListModel model = new ComboBoxListModel(getPrintersName());
        @SuppressWarnings("unchecked")
        final JComboBox<String> printerComboBox = new JComboBox<String>(model);
        if (layoutStampa.getStampante() != null && !layoutStampa.getStampante().isEmpty()) {
            printerComboBox.setSelectedItem(layoutStampa.getStampante());
        }
        builder.add(printerComboBox, cc.xy(3, 2));
        builder.add(new SelectLayoutCommand(layoutStampa, model).createButton(), cc.xy(4, 2));

        JLabel iconEntitaLabel = new JLabel();
        if (layoutStampa.getEntita() != null) {
            iconEntitaLabel.setIcon(RcpSupport.getIcon(layoutStampa.getEntita().getClass().getName()));
            StringBuilder sb = new StringBuilder(150);
            sb.append("<html>Layout personalizzato per<br><b> ");
            sb.append(ObjectConverterManager.toString(layoutStampa.getEntita()));
            sb.append("</b>");
            if (layoutStampa.getSedeEntita() != null) {
                iconEntitaLabel.setIcon(RcpSupport.getIcon(layoutStampa.getSedeEntita().getClass().getName()));
                sb.append("<br>sede<br><b>");
                sb.append(ObjectConverterManager.toString(layoutStampa.getSedeEntita()));
                sb.append("</b>");
            }
            sb.append("</html>");
            iconEntitaLabel.setToolTipText(sb.toString());
            builder.add(iconEntitaLabel, cc.xy(5, 2));
        }
        return builder.getPanel();
    }

    /**
     * Crea i componenti per la visualizzazione del titolo.
     *
     * @param tipoAreaDocumento
     *            tipo area di riferimento
     * @return controlli creati
     */
    protected JComponent createTitleComponent(ITipoAreaDocumento tipoAreaDocumento) {

        JPanel panel = new JPanel(new HorizontalLayout());
        panel.setBackground(new Color(183, 183, 183));
        JLabel labelTipoArea = new JLabel(ObjectConverterManager.toString(tipoAreaDocumento));
        labelTipoArea.setIcon(RcpSupport.getIcon(TipoDocumento.class.getName()));
        labelTipoArea.setOpaque(false);
        Font f = labelTipoArea.getFont();
        labelTipoArea.setFont(f.deriveFont(f.getStyle() ^ Font.BOLD));
        panel.add(labelTipoArea);

        return panel;
    }

    /**
     * @return Returns the layoutStampaPredefinito.
     */
    public LayoutStampa getLayoutStampaPredefinito() {
        return layoutStampaPredefinito;
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
     * @return Returns the showSelectLayoutButton.
     */
    public boolean isShowSelectLayoutButton() {
        return showSelectLayoutButton;
    }

    /**
     * Aggiunge un listener al property change.
     *
     * @param listener
     *            listener da aggiungere
     */
    public void removeSelectLayoutStampaListener(PropertyChangeListener listener) {
        selectLayoutStampaChangeSupport.removePropertyChangeListener(listener);
    }

    /**
     * @param showSelectLayoutButton
     *            The showSelectLayoutButton to set.
     */
    public void setShowSelectLayoutButton(boolean showSelectLayoutButton) {
        this.showSelectLayoutButton = showSelectLayoutButton;
    }

    /**
     * @param showSelectPrinterControl
     *            The showSelectPrinterControl to set.
     */
    public void setShowSelectPrinterControl(boolean showSelectPrinterControl) {
        // printerComboBox.setVisible(showSelectPrinterControl);
    }
}
