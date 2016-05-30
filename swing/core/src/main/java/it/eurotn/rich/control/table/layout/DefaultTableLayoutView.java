package it.eurotn.rich.control.table.layout;

import it.eurotn.panjea.anagrafica.domain.AbstractLayout;
import it.eurotn.panjea.anagrafica.domain.TableLayout;
import it.eurotn.rich.command.JECCommandGroup;
import it.eurotn.rich.command.JideToggleCommand;
import it.eurotn.rich.control.table.JideTableWidget;
import it.eurotn.rich.control.table.TableOptionsCollapsiblePane;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTable;

import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.ActionCommandInterceptor;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.factory.AbstractControlFactory;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.grid.TableUtils;
import com.jidesoft.pane.CollapsiblePane;

public class DefaultTableLayoutView extends AbstractControlFactory implements PropertyChangeListener {

    private class DeleteLayoutCommand extends ApplicationWindowAwareCommand {
        public static final String COMMAND_ID = "deleteLayoutCommand";

        /**
         * Costruttore.
         *
         */
        public DeleteLayoutCommand() {
            super(COMMAND_ID);
            RcpSupport.configure(this);
        }

        @Override
        protected void doExecuteCommand() {
            final LayoutCommand layoutCommand = (LayoutCommand) layoutComboBox.getSelectedItem();

            if (layoutCommand != null) {
                if (layoutCommand.deleteLayout()) {
                    layoutComboBox.removeItem(layoutCommand);
                }
            }
        }

    }

    public class LayoutComboBoxListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            applicaCorrente();
        }
    }

    public class LayoutRenderer extends DefaultListCellRenderer {

        private static final long serialVersionUID = 3967874760834624559L;

        private Map<Boolean, Icon> iconsLayout = null;

        {
            iconsLayout = new HashMap<Boolean, Icon>();
            iconsLayout.put(Boolean.TRUE, getIconSource().getIcon("tableLayoutGlobal"));
            iconsLayout.put(Boolean.FALSE, getIconSource().getIcon("tableLayoutUser"));
        }

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
                boolean cellHasFocus) {

            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value != null) {
                LayoutCommand layoutCommand = (LayoutCommand) value;
                label.setText(layoutCommand.getTableLayout().getName());
                label.setIcon(iconsLayout.get(layoutCommand.getTableLayout().isGlobal()));
            }
            return label;
        }
    }

    public class MoveHorrizontalCommand extends JideToggleCommand {

        public static final String COMMAND_ID = "moveHorrizontal";

        private boolean selectedOnInit;

        /**
         * Command per abiltare l'auto resize delle colonne.
         */
        public MoveHorrizontalCommand() {
            super(COMMAND_ID);
            RcpSupport.configure(this);
            this.selectedOnInit = true;
            this.setSelected(tableWidget.getTable().getAutoResizeMode() == JTable.AUTO_RESIZE_OFF);
            this.selectedOnInit = false;
        }

        @Override
        protected void onButtonAttached(AbstractButton button) {
            super.onButtonAttached(button);
            button.setName(COMMAND_ID);
        }

        @Override
        protected void onDeselection() {
            super.onDeselection();
            if (!selectedOnInit) {
                tableWidget.getTable().setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
                TableUtils.autoResizeAllColumns(tableWidget.getTable());
                getCurrentLayout().setEstendiColonne(false);
            }
        }

        @Override
        protected void onSelection() {
            super.onSelection();
            if (!selectedOnInit) {
                tableWidget.getTable().setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                TableUtils.autoResizeAllColumns(tableWidget.getTable());
                getCurrentLayout().setEstendiColonne(true);
            }
        }

    }

    private final class SalvaTableLayoutCommandInterceptor implements ActionCommandInterceptor {

        @Override
        public void postExecution(ActionCommand arg0) {

            if (((SalvaTableLayoutCommand) arg0).getLayoutSalvato() != null) {
                updateLayoutsToolBar();

                AbstractLayout layout = ((SalvaTableLayoutCommand) arg0).getLayoutSalvato();

                layoutComboBox.setSelectedItem(layout);
                for (int i = 0; i < layoutComboBox.getItemCount(); i++) {
                    LayoutCommand layoutCommand = (LayoutCommand) layoutComboBox.getItemAt(i);
                    // npe dalla mail. aggiungo verifiche
                    if (layoutCommand != null && layoutCommand.getTableLayout() != null
                            && layoutCommand.getTableLayout().getName() != null
                            && layoutCommand.getTableLayout().getName().equals(layout.getName())) {
                        layoutComboBox.setSelectedIndex(i);
                        break;
                    }
                }
            }
        }

        @Override
        public boolean preExecution(ActionCommand command) {
            command.addParameter(SalvaTableLayoutCommand.LAYOUT_NAME, getCurrentLayout().getName());
            return true;
        }
    }

    public class VisibleNumberRowCommand extends JideToggleCommand {

        public static final String COMMAND_ID = "visualizzaNumeriRiga";

        /**
         * Costruttore.
         */
        public VisibleNumberRowCommand() {
            super(COMMAND_ID);
            RcpSupport.configure(this);
        }

        @Override
        protected void onButtonAttached(AbstractButton button) {
            super.onButtonAttached(button);
            button.setName(COMMAND_ID);
        }

        @Override
        protected void onDeselection() {
            super.onDeselection();
            tableWidget.setNumberRowVisible(false);
            getCurrentLayout().setVisualizzaNumeriRiga(false);
        }

        @Override
        protected void onSelection() {
            super.onSelection();
            tableWidget.setNumberRowVisible(true);
            getCurrentLayout().setVisualizzaNumeriRiga(true);
        }
    }

    protected DefaultTableLayoutManager layoutManager;

    private CollapsiblePane rootPanel = null;

    private SalvaTableLayoutCommand salvaTableLayoutCommand;
    private DeleteLayoutCommand deleteLayoutCommand;

    protected JComboBox layoutComboBox = new JComboBox();

    private SalvaTableLayoutCommandInterceptor salvaTableLayoutCommandInterceptor;

    private final JideTableWidget<?> tableWidget;

    private VisibleNumberRowCommand visibleNumberRowCommand;

    private MoveHorrizontalCommand moveHorrizontalCommand;

    private LayoutComboBoxListener comboboxItemSelectedListener;

    /**
     * Costruttore.
     *
     * @param tableWidget
     *            table widget
     */
    public DefaultTableLayoutView(final JideTableWidget<?> tableWidget) {
        super();
        this.tableWidget = tableWidget;
    }

    /**
     * Applica il layout selezionato.
     */
    public void applicaCorrente() {
        TableLayout layout = getCurrentLayout();
        if (layout != null) {// npe mail
            moveHorrizontalCommand.setSelected(layout.getEstendiColonne());
            visibleNumberRowCommand.setSelected(layout.getVisualizzaNumeriRiga());

            if (layout.getEstendiColonne()) {
                TableUtils.autoResizeAllColumns(tableWidget.getTable());
            } else {
                TableUtils.autoResizeAllColumns(tableWidget.getTable());
            }
            tableWidget.setNumberRowVisible(layout.getVisualizzaNumeriRiga());
            layoutManager.applica(layout);
        }
    }

    @Override
    protected JComponent createControl() {

        rootPanel = new TableOptionsCollapsiblePane();

        JPanel componentPanel = getComponentFactory().createPanel(new BorderLayout());
        componentPanel.setOpaque(false);
        updateLayoutsToolBar();
        layoutComboBox.setRenderer(new LayoutRenderer());
        layoutComboBox.setOpaque(false);
        comboboxItemSelectedListener = new LayoutComboBoxListener();
        layoutComboBox.addActionListener(comboboxItemSelectedListener);
        componentPanel.add(layoutComboBox, BorderLayout.CENTER);

        JECCommandGroup commandGroup = new JECCommandGroup();
        commandGroup.add(getSalvaTableLayoutCommand());
        commandGroup.add(getDeleteLayoutCommand());
        JComponent toolbar = commandGroup.createToolBar();
        toolbar.setOpaque(false);
        componentPanel.add(toolbar, BorderLayout.EAST);

        rootPanel.add(componentPanel);
        // visibilità numero righe
        AbstractButton visibleNumberRowCheckBox = getVisibleNumberRowCommand().createCheckBox();
        visibleNumberRowCheckBox.setOpaque(false);
        visibleNumberRowCheckBox.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 0));

        // abilita "muovi in orizzontale"
        AbstractButton moveHorrizontalCheckBox = getMoveHorrizontalCommand().createCheckBox();
        moveHorrizontalCheckBox.setOpaque(false);
        moveHorrizontalCheckBox.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 0));

        rootPanel.add(visibleNumberRowCheckBox);
        rootPanel.add(moveHorrizontalCheckBox);

        rootPanel.setTitle("Gestione layout");

        return rootPanel;
    }

    /**
     * Esegue la dispose dell'oggetto.
     */
    public void dispose() {
        if (layoutManager != null) {
            layoutManager.setTableWidget(null);
        }
        layoutManager = null;

        if (layoutComboBox != null) {
            layoutComboBox.removeActionListener(comboboxItemSelectedListener);
            layoutComboBox.removeAllItems();
            layoutComboBox = null;
        }

        if (salvaTableLayoutCommand != null) {
            salvaTableLayoutCommand.removeCommandInterceptor(salvaTableLayoutCommandInterceptor);
            salvaTableLayoutCommand.setLayoutManager(null);
        }
    }

    /**
     *
     * @return Layout selezionato ella combo.
     */
    public TableLayout getCurrentLayout() {
        if (layoutComboBox.getSelectedItem() != null) {
            return ((LayoutCommand) layoutComboBox.getSelectedItem()).getTableLayout();
        }
        return layoutManager.getDefaultLayout();
    }

    /**
     * @return the deleteLayoutCommand
     */
    public DeleteLayoutCommand getDeleteLayoutCommand() {
        if (deleteLayoutCommand == null) {
            deleteLayoutCommand = new DeleteLayoutCommand();
        }

        return deleteLayoutCommand;
    }

    /**
     * @return the layoutManager
     */
    public DefaultTableLayoutManager getLayoutManager() {
        if (layoutManager == null) {
            layoutManager = new DefaultTableLayoutManager(tableWidget);
            layoutManager.addLayoutListener(this);
        }
        return layoutManager;
    }

    /**
     * @return the moveHorrizontalCommand
     */
    public MoveHorrizontalCommand getMoveHorrizontalCommand() {
        if (moveHorrizontalCommand == null) {
            moveHorrizontalCommand = new MoveHorrizontalCommand();
        }

        return moveHorrizontalCommand;
    }

    /**
     * @return the salvaTableLayoutCommand
     */
    public SalvaTableLayoutCommand getSalvaTableLayoutCommand() {
        if (salvaTableLayoutCommand == null) {
            salvaTableLayoutCommand = new SalvaTableLayoutCommand(this.getLayoutManager());

            salvaTableLayoutCommandInterceptor = new SalvaTableLayoutCommandInterceptor();
            salvaTableLayoutCommand.addCommandInterceptor(salvaTableLayoutCommandInterceptor);
        }

        return salvaTableLayoutCommand;
    }

    /**
     * @return the visibleNumberRowCommand
     */
    public VisibleNumberRowCommand getVisibleNumberRowCommand() {
        if (visibleNumberRowCommand == null) {
            visibleNumberRowCommand = new VisibleNumberRowCommand();
        }

        return visibleNumberRowCommand;
    }

    /**
     * Nasconde il layout manager.
     */
    public void hide() {
        rootPanel.setVisible(false);
    }

    /**
     * Indica se il table layout view è visualizzato.
     *
     * @return <code>true</code> se visualizzato, <code>false</code> altrimenti
     */
    public boolean isVisible() {
        return rootPanel.isVisible();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

        TableLayout tableLayout = (TableLayout) evt.getNewValue();

        layoutComboBox.removeActionListener(comboboxItemSelectedListener);
        for (int i = 0; i < layoutComboBox.getItemCount(); i++) {
            LayoutCommand layoutCommand = (LayoutCommand) layoutComboBox.getItemAt(i);
            if (layoutCommand.getTableLayout().getId().equals(tableLayout.getId())) {
                layoutComboBox.setSelectedIndex(i);
                break;
            }
        }
        layoutComboBox.addActionListener(comboboxItemSelectedListener);
    }

    /**
     * @param layoutManager
     *            The layoutManager to set.
     */
    public void setLayoutManager(DefaultTableLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    /**
     * Visualizza il layout manager.
     */
    public void show() {
        rootPanel.setVisible(true);
    }

    /**
     * Cambia lo stato visible del layout manager. Se lo stato è visible = true lo porta in stato visible = false e
     * viceversa.
     */
    public void toggleVisibleState() {
        rootPanel.setVisible(!rootPanel.isVisible());
    }

    /**
     * Aggiorna la toolbar creando i pulsanti di tutti i layout presenti.
     *
     */
    public void updateLayoutsToolBar() {

        List<TableLayout> layouts = getLayoutManager().getLayouts();

        layoutComboBox.removeAllItems();

        for (TableLayout layout : layouts) {

            LayoutCommand layoutCommand = new LayoutCommand(layout, getLayoutManager());

            layoutComboBox.addItem(layoutCommand);
        }
        layoutComboBox.setSelectedItem(null);

        getDeleteLayoutCommand().setVisible(!getLayoutManager().isReadOnly());
        getSalvaTableLayoutCommand().setVisible(!getLayoutManager().isReadOnly());
    }

}
