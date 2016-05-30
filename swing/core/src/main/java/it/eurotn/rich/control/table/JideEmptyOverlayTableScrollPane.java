package it.eurotn.rich.control.table;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import org.jdesktop.swingx.VerticalLayout;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.config.CommandButtonConfigurer;
import org.springframework.richclient.command.config.CommandFaceDescriptor;
import org.springframework.richclient.command.support.ActionCommandInterceptorAdapter;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.dialog.CloseAction;
import org.springframework.richclient.dialog.MessageDialog;
import org.springframework.richclient.factory.ButtonFactory;
import org.springframework.richclient.factory.ComponentFactory;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.grid.TableModelWrapperUtils;
import com.jidesoft.margin.AbstractMarginSupport;
import com.jidesoft.margin.MarginArea;
import com.jidesoft.margin.RowMarginSupport;
import com.jidesoft.margin.RowNumberMargin;
import com.jidesoft.margin.TableRowMarginSupport;
import com.jidesoft.pane.CollapsiblePane;
import com.jidesoft.pane.event.CollapsiblePaneAdapter;
import com.jidesoft.pane.event.CollapsiblePaneEvent;
import com.jidesoft.popup.JidePopup;
import com.jidesoft.swing.DefaultOverlayable;
import com.jidesoft.swing.JideScrollPane;
import com.jidesoft.swing.StyledLabel;
import com.jidesoft.swing.StyledLabelBuilder;

import it.eurotn.panjea.anagrafica.domain.TableLayout;
import it.eurotn.rich.control.table.layout.DefaultTableLayoutManager;
import it.eurotn.rich.control.table.layout.DefaultTableLayoutView;
import it.eurotn.rich.control.table.layout.LayoutCommand;
import it.eurotn.rich.control.table.options.AbstractTableSelectionColumnsOptionPane;

public class JideEmptyOverlayTableScrollPane extends DefaultOverlayable implements TableModelListener {

    private class CancelCommand extends ApplicationWindowAwareCommand {

        private static final String COMMAND_ID = "cancelCommand";

        /**
         * Costruisce il comando.
         *
         */
        public CancelCommand() {
            super(COMMAND_ID);

            CommandFaceDescriptor descriptor = new CommandFaceDescriptor();
            descriptor.setIcon(RcpSupport.getIcon(COMMAND_ID + ".icon"));

            setFaceDescriptor(descriptor);
        }

        @Override
        protected void doExecuteCommand() {
            setCancel(true);
            inCancellazione();
        }
    }

    private class OptionsMessageDialog extends MessageDialog {

        /**
         * Costruttore.
         *
         */
        public OptionsMessageDialog() {
            super("Opzioni tabella", "Opzioni tabella");
            setCloseAction(CloseAction.HIDE);
            setPreferredSize(new Dimension(400, 500));
        }

        @Override
        protected JComponent createDialogContentPane() {
            return optionsControls;
        }

        @Override
        protected void onAboutToShow() {

        }

    }

    /**
     * Stampa i numeri di riga inserendo un * all'ultima riga che corrisponde alnuovo record
     *
     * @author giangi
     * @version 1.0, 15/ott/2012
     *
     */
    private class RowNumberMarginEditable extends RowNumberMargin {
        private static final long serialVersionUID = -7590792455698795561L;

        public RowNumberMarginEditable(RowMarginSupport paramRowMarginSupport) {
            super(paramRowMarginSupport);
        }

        @Override
        public void paintRowMargin(Graphics paramGraphics, Rectangle paramRectangle, int paramInt) {
            int i = ((RowMarginSupport) this._marginSupport).visualRowToActualRow(paramInt);
            if ((AbstractMarginSupport.b == 0) && (i == -1)) {
                return;
            }
            int j = i + 1;
            String str = "" + j;
            if (j == ((RowMarginSupport) this._marginSupport).getRowCount()) {
                str = "*";
            }
            paramGraphics.setColor(getForeground());
            paramGraphics.setFont(getFont());
            paramGraphics.drawString(str,
                    paramRectangle.x + paramRectangle.width - getFontMetrics(getFont()).stringWidth(str),
                    paramRectangle.y
                            + (paramRectangle.height + getFontMetrics(getFont()).getAscent()
                                    - getFontMetrics(getFont()).getDescent()) / 2
                            + ((RowMarginSupport) this._marginSupport).getBaselineAdjustment() - 1);
        }
    }

    private class TableOptionsCommand extends ApplicationWindowAwareCommand {

        public static final int MIN_TABLE_HEIGHT = 350;
        public static final int MIN_TABLE_WIDTH = 400;

        public static final String COMMAND_ID = "tableOptionsCommand";

        private DefaultTableLayoutManager layoutManager;

        private JidePopup layoutsPopup;

        /**
         * Costruttore.
         *
         * @param layoutManager
         *            layout manager
         *
         */
        public TableOptionsCommand(final DefaultTableLayoutManager layoutManager) {
            super(COMMAND_ID);
            RcpSupport.configure(this);
            this.layoutManager = layoutManager;
        }

        @Override
        public AbstractButton createButton(String faceDescriptorId, ButtonFactory buttonFactory,
                CommandButtonConfigurer buttonConfigurer) {
            AbstractButton abstractButton = super.createButton(faceDescriptorId, buttonFactory, buttonConfigurer);

            abstractButton.addMouseListener(new MouseAdapter() {

                @Override
                public void mouseEntered(MouseEvent e) {
                    super.mouseEntered(e);

                    List<TableLayout> layouts = layoutManager.getLayouts();
                    if (!layouts.isEmpty() && optionsPanel.isCollapsed()) {
                        getLayoutsPopup(layouts).showPopup();
                    }
                }

            });
            abstractButton.setFocusable(false);
            return abstractButton;
        }

        @Override
        protected void doExecuteCommand() {
            JComponent component = JideEmptyOverlayTableScrollPane.this.getActualComponent();

            // se la tabella è troppo piccola apro le opzioni in un popup,
            // altrimenti a finaco della tabella
            // TODO per il momento se viene aperto il dialog, da quel momento in
            // poi, anche se la tabella verrà
            // ridimensionata, verrà sempre utilizzato il dialog. Bug da
            // risolvere: aprendo il dialog l'optionsControl
            // prende come parent il dialog ne non riesco più ad assegnargli il
            // collapsiblepane se la tabella si
            // ridimensiona
            if (component.getHeight() < MIN_TABLE_HEIGHT || component.getWidth() < MIN_TABLE_WIDTH
                    || !optionsControls.getParent().equals(optionsPanel.getActualComponent())) {
                messageDialog.showDialog();
            } else {
                if (!optionsPanel.isVisible()) {
                    optionsPanel.setVisible(true);
                    optionsPanel.collapse(false);
                } else {
                    optionsPanel.collapse(true);
                }
            }
        }

        /**
         * @param layouts
         *            layouts
         * @return Returns the layoutsPopup.
         */
        public JidePopup getLayoutsPopup(List<TableLayout> layouts) {
            layoutsPopup = new JidePopup();
            layoutsPopup.setLayout(new VerticalLayout(5));

            layoutsPopup.setResizable(false);
            layoutsPopup.setMovable(false);
            layoutsPopup.setOwner(sp.getCorner(JScrollPane.UPPER_RIGHT_CORNER));

            JPanel titlePanel = new JPanel(new BorderLayout());
            titlePanel.setBackground(new Color(204, 204, 214));
            titlePanel.add(new JLabel("Layout disponibili"), BorderLayout.CENTER);

            layoutsPopup.add(titlePanel);

            JPanel buttonPanel = new JPanel(new VerticalLayout(5));
            for (TableLayout layout : layouts) {
                LayoutCommand layoutCommand = new LayoutCommand(layout, layoutManager);
                layoutCommand.addCommandInterceptor(new ActionCommandInterceptorAdapter() {
                    @Override
                    public void postExecution(ActionCommand actioncommand) {
                        layoutsPopup.hidePopup();
                    }
                });
                AbstractButton button = layoutCommand.createButton();
                buttonPanel.add(button);
            }

            JPanel optPanel = new JPanel(new BorderLayout());
            optPanel.setBackground(new Color(204, 204, 214));
            optPanel.add(new JLabel("Opzioni"), BorderLayout.CENTER);
            buttonPanel.add(optPanel);
            buttonPanel.add(tableSelectionColumnsOptionPane.getResizeAllColumnsButton());
            buttonPanel.add(tableSelectionColumnsOptionPane.getResetColumnsButton());
            buttonPanel.add(tableLayoutView.getVisibleNumberRowCommand().createCheckBox());
            buttonPanel.add(tableLayoutView.getMoveHorrizontalCommand().createCheckBox());
            layoutsPopup.add(buttonPanel);

            return layoutsPopup;
        }
    }

    private static final long serialVersionUID = 7911119528987712793L;

    private static final String EMPTY_STRING_KEY = "table.rowNone";
    private TableModel tableModel;

    private boolean cancel = false;
    private boolean searchInProgress = false;

    private OptionsMessageDialog messageDialog;

    /**
     * Se <code>true</code> verra sempre visualizzato un messaggio dall'overlay definito tramite i
     * metodi setMessage e setStyledMessage.
     */
    private boolean overlayMessage = false;

    private final JLabel defaultLabel = StyledLabelBuilder
            .createStyledLabel("{" + RcpSupport.getMessage(EMPTY_STRING_KEY) + ":f:gray}");
    private final StyledLabel searchLabel = StyledLabelBuilder
            .createStyledLabel("{" + RcpSupport.getMessage(EMPTY_STRING_KEY) + ":f:black,b}");
    private final JLabel overlayMessageLabel = StyledLabelBuilder.createStyledLabel("");

    private JPanel searchPanel;

    private ActionCommand cancelCommand;

    private boolean enableCancelAction;

    private boolean showOptionPanel;
    private CollapsiblePane optionsPanel;

    private JComponent optionsControls;

    private JideScrollPane sp;

    private MarginArea tableMarginArea;

    private JPanel rootPanel;

    private AbstractTableSelectionColumnsOptionPane tableSelectionColumnsOptionPane;

    private DefaultTableLayoutView tableLayoutView;

    {
        showOptionPanel = true;
        enableCancelAction = false;
    }

    /**
     * Costruttore.
     */
    public JideEmptyOverlayTableScrollPane() {
        super();
    }

    /**
     * Costruttore.
     *
     * @param showOptionPanel
     *            se <code>true</code> visualizza il pannello delle opzioni della tabella
     */
    public JideEmptyOverlayTableScrollPane(boolean showOptionPanel) {
        this();
        this.showOptionPanel = showOptionPanel;
    }

    /**
     * crea il panello overlay iniziale.
     *
     * @return tag.
     */
    public JComponent createDefaultOverlay() {
        setOverlayVisible(true);
        ComponentFactory factory = (ComponentFactory) Application.services().getService(ComponentFactory.class);
        cancelCommand = new CancelCommand();
        JPanel ricercaPanel = factory.createPanel(new FlowLayout(FlowLayout.LEFT));
        ricercaPanel.add(defaultLabel);
        ricercaPanel.add(getSearchPanel());
        ricercaPanel.add(overlayMessageLabel);
        return ricercaPanel;
    }

    /**
     * Esegue la dispose.
     */
    public void dispose() {

        if (optionsPanel != null) {
            tableModel.removeTableModelListener(this);

            getActualComponent().putClientProperty("Overlayable.overlayable", null);

            this.removeAll();

            optionsPanel.removeAll();
            optionsPanel = null;
        }
    }

    /**
     * @return Returns the optionsPanel.
     */
    public CollapsiblePane getOptionsPanel() {
        return optionsPanel;
    }

    /**
     * @return compomente che contiene la label di ricerca
     */
    private JComponent getSearchPanel() {

        if (searchPanel == null) {
            ComponentFactory factory = (ComponentFactory) Application.services().getService(ComponentFactory.class);
            searchPanel = factory.createPanel(new BorderLayout());
            searchPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            searchPanel.setOpaque(false);
            searchPanel.add(searchLabel, BorderLayout.CENTER);
            if (enableCancelAction) {
                searchPanel.add(cancelCommand.createButton(), BorderLayout.EAST);
            }
            cancelCommand.setVisible(false);

            searchPanel.setVisible(false);

        }

        return searchPanel;

    }

    /**
     * sobra escribe la label di overlay di la tablella per indicare che si ha cancellato la
     * ricerca.
     */
    public void inCancellazione() {
        searchLabel.setText("Cancellazione ricerca in corso.");
        cancelCommand.setEnabled(false);
        repaint();
    }

    /**
     * @return the cancel
     */
    public boolean isCancel() {
        return cancel;
    }

    /**
     * @return the showOptionPanel
     */
    public boolean isShowOptionPanel() {
        return showOptionPanel;
    }

    @Override
    public boolean requestFocusInWindow() {
        return sp.requestFocusInWindow();
    }

    /**
     * @param cancel
     *            the cancel to set
     */
    public void setCancel(boolean cancel) {
        this.cancel = cancel;
    }

    /**
     * @param enableCancelAction
     *            the enableCancelAction to set
     */
    public void setEnableCancelAction(boolean enableCancelAction) {
        this.enableCancelAction = enableCancelAction;
    }

    /**
     * Messaggio da visualizzare nell'aoverlay.
     *
     * @param message
     *            messaggio
     */
    public void setMessage(String message) {
        this.overlayMessage = message != null;
        StyledLabelBuilder.setStyledText((StyledLabel) overlayMessageLabel, "{" + message + ":f:gray}");
        updateControl();
    }

    /**
     * Setta la visibilita del numero righe.
     *
     * @param visible
     *            true per visualizzare il numero di righe.
     */
    public void setNumberRowVisible(boolean visible) {
        if (visible) {
            rootPanel.add(tableMarginArea, BorderLayout.BEFORE_LINE_BEGINS);
        } else {
            rootPanel.remove(tableMarginArea);
        }
    }

    /**
     * @param showOptionPanel
     *            the showOptionPanel to set
     */
    public void setShowOptionPanel(boolean showOptionPanel) {
        this.showOptionPanel = showOptionPanel;
        if (sp != null) {
            sp.getCorner(JScrollPane.UPPER_RIGHT_CORNER).setVisible(showOptionPanel);
        }
    }

    /**
     * Messaggio da visualizzare nell'aoverlay. Il messaggio verrà utilizzato per costruire una
     * {@link StyledLabel} quindi potranno essere usati tutti gli styli desiderati.
     *
     * @param message
     *            messaggio
     */
    public void setStyledMessage(String message) {
        this.overlayMessage = message != null;
        StyledLabelBuilder.setStyledText((StyledLabel) overlayMessageLabel, message);
        updateControl();
    }

    /**
     * installa l'overlay al tablescrollPane.
     *
     * @param table
     *            componente dove installare l'overlay
     * @param optionPanel
     *            pannello delle opzioni
     * @param paramTableLayoutView
     *            layout view
     */
    public void setTable(JTable table, CollapsiblePane optionPanel, DefaultTableLayoutView paramTableLayoutView,
            AbstractTableSelectionColumnsOptionPane paramTableSelectionColumnsOptionPane) {

        this.tableLayoutView = paramTableLayoutView;
        this.tableSelectionColumnsOptionPane = paramTableSelectionColumnsOptionPane;

        sp = new JideScrollPane(table);
        sp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        sp.setBorder(BorderFactory.createEmptyBorder());
        sp.setViewportBorder(BorderFactory.createEmptyBorder());
        table.setBorder(BorderFactory.createEmptyBorder());

        // add RowMargin for table
        RowMarginSupport tableRowMarginSupport = new TableRowMarginSupport(table, sp);
        tableMarginArea = new MarginArea();

        boolean isWritable = TableModelWrapperUtils
                .getActualTableModel(table.getModel()) instanceof DefaultBeanEditableTableModel;
        if (isWritable) {
            tableMarginArea.addMarginComponent(new RowNumberMarginEditable(tableRowMarginSupport));
        } else {
            tableMarginArea.addMarginComponent(new RowNumberMargin(tableRowMarginSupport));
        }
        // this method will add a border around the margin area so that it
        // always has the same height as the viewport of
        // the scroll pane to align properly with the table rows.
        tableMarginArea.setMarginAreaFor(sp);

        rootPanel = new JPanel(new BorderLayout());
        rootPanel.add(tableMarginArea, BorderLayout.BEFORE_LINE_BEGINS);
        rootPanel.add(sp, BorderLayout.CENTER);

        // Searchable searchable = (Searchable)
        // table.getClientProperty(Searchable.CLIENT_PROPERTY_SEARCHABLE);
        // if (searchable != null) {
        // ((TableSearchable) searchable).setMainIndex(0);
        // MarkerSupport tableMarkerSupport = new TableRowMarkerSupport(table);
        // MarkerArea tableMarkerArea = new MarkerArea(tableMarkerSupport);
        // MarkerUtils.registerSearchable(searchable, tableMarkerArea,
        // Marker.TYPE_NOTE, Color.YELLOW);
        // rootPanel.add(tableMarkerArea, BorderLayout.AFTER_LINE_ENDS);
        // }

        if (showOptionPanel) {
            this.optionsPanel = optionPanel;

            if (optionsPanel != null) {
                this.optionsControls = optionPanel.getContentPane();
                messageDialog = new OptionsMessageDialog();
                this.optionsPanel.addCollapsiblePaneListener(new CollapsiblePaneAdapter() {
                    @Override
                    public void paneCollapsed(CollapsiblePaneEvent arg0) {
                        JideEmptyOverlayTableScrollPane.this.optionsPanel.setVisible(false);
                    }
                });

                optionsPanel.setVisible(false);
                rootPanel.add(optionsPanel, BorderLayout.EAST);

                AbstractButton button = new TableOptionsCommand(tableLayoutView.getLayoutManager()).createButton();
                sp.setCorner(JScrollPane.UPPER_RIGHT_CORNER, button);
            }
        }

        setActualComponent(rootPanel);
        table.getModel().addTableModelListener(this);
        this.tableModel = table.getModel();
        addOverlayComponent(createDefaultOverlay());
        repaint();
    }

    /**
     *
     */
    public void startSearch() {
        searchInProgress = true;
        updateControl();
    }

    /**
     *
     */
    public void stopSearch() {
        searchInProgress = false;
        updateControl();
    }

    @Override
    public void tableChanged(TableModelEvent e) {
        updateControl();
    }

    /**
     * Aggiorna i controlli dell'overlay in base allo stato della tabella.
     */
    private void updateControl() {

        // se è stato settato un messaggio di overlay visualizzo sempre e solo
        // quello
        if (overlayMessage) {
            defaultLabel.setVisible(false);
            getSearchPanel().setVisible(false);
            cancelCommand.setVisible(false);
            overlayMessageLabel.setVisible(true);

            setOverlayVisible(true);
        } else {

            overlayMessageLabel.setVisible(false);

            int numRighe = 0;
            if (tableModel != null) {
                numRighe = TableModelWrapperUtils.getActualTableModel(tableModel).getRowCount();
            }

            // visualizzo l'overlay se la ricerca è in corso o se la tabella è
            // vuota
            setOverlayVisible(searchInProgress || numRighe == 0);

            if (searchInProgress) {
                searchLabel.setText("Caricamento in corso...");
                getSearchPanel().setVisible(true);
                defaultLabel.setVisible(false);

                if (cancelCommand != null) {
                    cancelCommand.setVisible(true);
                    cancelCommand.setEnabled(true);
                }
            } else {
                getSearchPanel().setVisible(false);
                defaultLabel.setVisible(true);
                if (cancelCommand != null) {
                    cancelCommand.setVisible(false);
                }
            }
        }

        repaint();
    }
}
