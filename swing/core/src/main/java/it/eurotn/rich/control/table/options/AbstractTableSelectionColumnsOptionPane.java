package it.eurotn.rich.control.table.options;

import it.eurotn.rich.control.table.TableOptionsCollapsiblePane;

import java.awt.BorderLayout;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;

import org.jfree.ui.tabbedui.VerticalLayout;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.factory.AbstractControlFactory;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.grid.JideTable;
import com.jidesoft.grid.TableColumnChooser;
import com.jidesoft.grid.TableUtils;
import com.jidesoft.pane.CollapsiblePane;
import com.jidesoft.swing.CheckBoxList;
import com.jidesoft.swing.JideButton;
import com.jidesoft.swing.SimpleScrollPane;

public abstract class AbstractTableSelectionColumnsOptionPane extends AbstractControlFactory {

    private class AutoResizeAllColumnsCommand extends ApplicationWindowAwareCommand {

        public static final String COMMAND_ID = "autoResizeAllColumnsCommand";

        /**
         * Costruttore.
         */
        public AutoResizeAllColumnsCommand() {
            super(COMMAND_ID);
            RcpSupport.configure(this);
        }

        @Override
        protected void doExecuteCommand() {
            TableUtils.autoResizeAllColumns(table, null, true, true);
        }

        @Override
        protected void onButtonAttached(AbstractButton button) {
            super.onButtonAttached(button);
            button.setName(COMMAND_ID);
        }
    }

    private final class ListSelectionListenerImplementation implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent e) {

            if (checkBoxList.getCheckBoxListSelectedIndex() != -1) {

                int[] ai = checkBoxList.getCheckBoxListSelectedIndices();
                TableColumnChooser.showColumns(table, ai);
            }
        }
    }

    private class ResetColumnsCommand extends ApplicationWindowAwareCommand {

        public static final String COMMAND_ID = "resetColumnsCommand";

        /**
         * Costruttore.
         *
         */
        public ResetColumnsCommand() {
            super(COMMAND_ID);
            RcpSupport.configure(this);
        }

        @Override
        protected void doExecuteCommand() {
            resetColumns();
        }

    }

    protected JideTable table;

    protected CheckBoxList checkBoxList;

    private ListSelectionListener listSelectionListener;

    /**
     * Costruttore.
     *
     * @param table
     *            tabella di riferimento
     */
    public AbstractTableSelectionColumnsOptionPane(final JideTable table) {
        super();
        this.table = table;
        this.table.getColumnModel().addColumnModelListener(new TableColumnModelListener() {

            @Override
            public void columnAdded(TableColumnModelEvent e) {
                AbstractTableSelectionColumnsOptionPane.this.update();
            }

            @Override
            public void columnMarginChanged(ChangeEvent e) {
                AbstractTableSelectionColumnsOptionPane.this.update();
            }

            @Override
            public void columnMoved(TableColumnModelEvent e) {
                // AbstractTableSelectionColumnsOptionPane.this.update();
            }

            @Override
            public void columnRemoved(TableColumnModelEvent e) {
                AbstractTableSelectionColumnsOptionPane.this.update();
            }

            @Override
            public void columnSelectionChanged(ListSelectionEvent e) {
                // AbstractTableSelectionColumnsOptionPane.this.update();
            }
        });
        setListSelectionListener(new ListSelectionListenerImplementation());
    }

    @Override
    protected JComponent createControl() {
        CollapsiblePane rootPane = new TableOptionsCollapsiblePane();
        rootPane.setLayout(new BorderLayout(4, 4));

        JPanel northPanel = new JPanel(new VerticalLayout());
        northPanel.setOpaque(false);
        // resize di tutte le colonne
        northPanel.add(getResizeAllColumnsButton());
        // reset delle colonne
        northPanel.add(getResetColumnsButton());
        rootPane.add(northPanel, BorderLayout.NORTH);

        rootPane.add(getColumnsChooserComponent(), BorderLayout.CENTER);
        update();

        rootPane.setTitle("Selezione colonne");

        return rootPane;
    }

    /**
     * Restituisce i controlli per la selezione delle colonne.
     *
     * @return controlli creati
     */
    private JComponent getColumnsChooserComponent() {
        checkBoxList = new CheckBoxList(new DefaultListModel());
        checkBoxList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        SimpleScrollPane simpleScrollPane = new SimpleScrollPane();
        simpleScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        simpleScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        simpleScrollPane.setWheelScrollingEnabled(true);
        simpleScrollPane.setViewportView(checkBoxList);
        simpleScrollPane.setVerticalUnitIncrement(120);
        simpleScrollPane.setBorder(BorderFactory.createEmptyBorder());

        return simpleScrollPane;
    }

    /**
     * @return the resetColumnsButton
     */
    public JideButton getResetColumnsButton() {
        JideButton resetColumnsButton = new JideButton();
        resetColumnsButton.setAction(new ResetColumnsCommand().getActionAdapter());
        resetColumnsButton.setHorizontalAlignment(SwingConstants.LEFT);
        resetColumnsButton.setButtonStyle(JideButton.HYPERLINK_STYLE);

        return resetColumnsButton;
    }

    /**
     * @return the resizeAllColumnsButton
     */
    public JideButton getResizeAllColumnsButton() {
        JideButton resizeAllColumnsButton = (JideButton) new AutoResizeAllColumnsCommand().createButton();
        resizeAllColumnsButton.setHorizontalAlignment(SwingConstants.LEFT);
        resizeAllColumnsButton.setButtonStyle(JideButton.HYPERLINK_STYLE);

        return resizeAllColumnsButton;
    }

    /**
     * Azione eseguita sul reset delle colonne.
     */
    protected abstract void onColumnsReset();

    /**
     * Esegue il reset delle colonne.
     */
    public void resetColumns() {
        onColumnsReset();
        checkBoxList.selectAll();
    }

    /**
     * @param listSelectionListener
     *            The listSelectionListener to set.
     */
    public void setListSelectionListener(ListSelectionListener listSelectionListener) {
        this.listSelectionListener = listSelectionListener;
    }

    /**
     * Aggiorna i componenti in base alla tabella di riferimento.
     */
    public void update() {
        if (listSelectionListener != null) {
            checkBoxList.getCheckBoxListSelectionModel().removeListSelectionListener(listSelectionListener);
        }

        updateColumnsComponent(checkBoxList);

        if (listSelectionListener != null) {
            checkBoxList.getCheckBoxListSelectionModel().addListSelectionListener(listSelectionListener);
        }
    }

    /**
     * Aggiorna il componente di selezione delle colonne in base alla tabella di riferimento.
     *
     * @param checkBoxListComponent
     *            componente di selezione
     */
    protected abstract void updateColumnsComponent(CheckBoxList checkBoxListComponent);
}
