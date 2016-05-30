package it.eurotn.panjea.fatturepa.rich.editors.fatturaelettronicatype.v1_1.datigenerali;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractCellEditor;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.dialog.ConfirmationDialog;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.rich.control.table.JideTableWidget;

public class RiferimentoNumLineaTableCellEditor extends AbstractCellEditor implements TableCellEditor {

    private class EditRiferimentiCommand extends ActionCommand {

        /**
         * Costruttore.
         */
        public EditRiferimentiCommand() {
            super("editCommand");
            RcpSupport.configure(this);
        }

        @Override
        protected void doExecuteCommand() {
            EditRiferimentiDialog dialog = new EditRiferimentiDialog(riferimentoNumLinea);
            dialog.showDialog();
        }

    }

    private class EditRiferimentiDialog extends ConfirmationDialog {

        private JideTableWidget<RiferimentoLineaPM> table;

        private List<Integer> linee;

        /**
         * Costruttore.
         *
         * @param linee
         *            linee
         */
        public EditRiferimentiDialog(final List<Integer> linee) {
            super("Riferimenti numero linee", "_");
            setPreferredSize(new Dimension(300, 400));
            this.linee = new ArrayList<Integer>();

            if (linee != null) {
                this.linee = linee;
            }
        }

        @Override
        protected JComponent createDialogContentPane() {
            JPanel msgPanel = getComponentFactory().createPanel(new BorderLayout(0, 10));
            msgPanel.add(
                    new JLabel(
                            "<html>Inserire i numeri di riferimento delle linee.<br><br>E' consentito inserire solo <b>valori numerici</b>.</html>"),
                    BorderLayout.NORTH);

            table = new JideTableWidget<RiferimentoLineaPM>("riferimentiTable", new RiferimentiNumLineeTableModel());
            msgPanel.add(table.getComponent(), BorderLayout.CENTER);

            List<RiferimentoLineaPM> rif = new ArrayList<RiferimentoLineaPM>();
            for (Integer linea : linee) {
                rif.add(new RiferimentoLineaPM(linea));
            }
            table.setRows(rif);

            return msgPanel;
        }

        @Override
        protected String getCancelCommandId() {
            return "cancelCommand";
        }

        @Override
        protected String getFinishCommandId() {
            return "okCommand";
        }

        @Override
        protected void onConfirm() {
            List<RiferimentoLineaPM> rif = table.getRows();

            riferimentoNumLinea = new ArrayList<Integer>();
            for (RiferimentoLineaPM riferimentoLineaPM : rif) {
                riferimentoNumLinea.add(riferimentoLineaPM.getValue());
            }
            valuesLabel.setText(riferimentoNumLinea.toString());
        }

    }

    private static final long serialVersionUID = -3733827554207369249L;

    private List<Integer> riferimentoNumLinea;

    private JPanel panel = null;
    private EditRiferimentiCommand editRiferimentiCommand = new EditRiferimentiCommand();
    private JLabel valuesLabel = new JLabel();

    @Override
    public Object getCellEditorValue() {
        return riferimentoNumLinea;
    }

    private JPanel getPanel() {
        if (panel == null) {
            panel = new JPanel(new BorderLayout());
            panel.setOpaque(false);
            panel.add(valuesLabel, BorderLayout.CENTER);
            panel.add(editRiferimentiCommand.createButton(), BorderLayout.EAST);
        }

        return panel;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {

        riferimentoNumLinea = (List<Integer>) value;

        getPanel();

        valuesLabel.setText(null);
        if (value != null) {
            valuesLabel.setText(value.toString());
        }

        return panel;
    }

}
