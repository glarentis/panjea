package it.eurotn.panjea.rich.editors.stampe;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;

import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.converter.ObjectConverterManager;
import com.jidesoft.dialog.JideOptionPane;
import com.jidesoft.grid.AbstractTableCellEditorRenderer;
import com.jidesoft.grid.CellRolloverSupport;
import com.jidesoft.grid.EditorContext;
import com.jidesoft.grid.TableModelWrapperUtils;
import com.jidesoft.swing.JideButton;
import com.jidesoft.swing.JideToggleButton;

import it.eurotn.panjea.stampe.domain.LayoutStampa;
import it.eurotn.panjea.stampe.domain.LayoutStampaDocumento;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

public class GestioneStampeButtonsCellEditorRenderer extends AbstractTableCellEditorRenderer
        implements CellRolloverSupport {
    private static final long serialVersionUID = -1559557761862573989L;

    public static final EditorContext CONTEXT = new EditorContext("gestioneStampeContext");

    @SuppressWarnings("rawtypes")
    @Override
    public void configureTableCellEditorRendererComponent(final JTable table, Component editorRendererComponent,
            boolean forRenderer, Object value, boolean isSelected, boolean hasFocus, final int row, int column) {

        JideToggleButton txtButton = (JideToggleButton) (((JPanel) editorRendererComponent).getComponent(0));
        JideToggleButton batchButton = (JideToggleButton) (((JPanel) editorRendererComponent).getComponent(1));
        JideToggleButton numeroCopieButton = (JideToggleButton) (((JPanel) editorRendererComponent).getComponent(2));
        JideToggleButton previewButton = (JideToggleButton) (((JPanel) editorRendererComponent).getComponent(3));
        JideToggleButton mailButton = (JideToggleButton) (((JPanel) editorRendererComponent).getComponent(4));
        JideToggleButton internoButton = (JideToggleButton) (((JPanel) editorRendererComponent).getComponent(5));
        JButton removeButton = (JButton) (((JPanel) editorRendererComponent).getComponent(6));

        final DefaultBeanTableModel innerModel = (DefaultBeanTableModel) TableModelWrapperUtils
                .getActualTableModel(table.getModel());
        final int roxInner = TableModelWrapperUtils.getActualRowAt(table.getModel(), row);

        txtButton.setEnabled(roxInner != -1);
        batchButton.setEnabled(roxInner != -1);
        numeroCopieButton.setEnabled(roxInner != -1);
        previewButton.setEnabled(roxInner != -1);
        mailButton.setEnabled(roxInner != -1);
        internoButton.setEnabled(roxInner != -1);
        removeButton.setEnabled(roxInner != -1);

        if (roxInner != -1) {
            final LayoutStampa layoutStampa = (LayoutStampa) innerModel.getElementAt(roxInner);

            mailButton.setVisible(layoutStampa instanceof LayoutStampaDocumento);
            internoButton.setVisible(layoutStampa instanceof LayoutStampaDocumento);

            if (!forRenderer) {
                txtButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                        innerModel.setValueAt(!layoutStampa.getSoloTesto(), roxInner,
                                (int) innerModel.getColumnPropertyPosition().get("soloTesto"));
                    }
                });
                batchButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        innerModel.setValueAt(!layoutStampa.getBatch(), roxInner,
                                (int) innerModel.getColumnPropertyPosition().get("batch"));
                    }
                });
                numeroCopieButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        innerModel.setValueAt(!layoutStampa.getConfermaNumeroCopie(), roxInner,
                                (int) innerModel.getColumnPropertyPosition().get("confermaNumeroCopie"));
                    }
                });
                previewButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        innerModel.setValueAt(!layoutStampa.getPreview(), roxInner,
                                (int) innerModel.getColumnPropertyPosition().get("preview"));
                    }
                });
                mailButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        innerModel.setValueAt(!layoutStampa.isMailLayout(), roxInner,
                                (int) innerModel.getColumnPropertyPosition().get("mailLayout"));
                    }
                });
                internoButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        innerModel.setValueAt(!layoutStampa.isInterno(), roxInner,
                                (int) innerModel.getColumnPropertyPosition().get("interno"));
                    }
                });
                removeButton.addActionListener(new ActionListener() {
                    @SuppressWarnings("unchecked")
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("<html>Rimuovere il layout <b> ");
                        sb.append(layoutStampa.getReportName());
                        sb.append("</b>");
                        if (LayoutStampaDocumento.class.getName().equals(layoutStampa.getClass().getName())) {
                            sb = sb.append("per il tipo documento <b>");
                            sb = sb.append(ObjectConverterManager
                                    .toString(((LayoutStampaDocumento) layoutStampa).getTipoDocumento()));
                            sb = sb.append("</b> ?</html>");
                        }
                        String message = sb.toString();
                        int result = JideOptionPane.showConfirmDialog(null, message, "ATTENZIONE",
                                JideOptionPane.YES_NO_OPTION);
                        if (result == JideOptionPane.YES_OPTION) {
                            innerModel.removeObject(layoutStampa);
                        }
                    }
                });
            }

            txtButton.setToolTipText("Layout solo testo");
            txtButton.setSelected(layoutStampa.getSoloTesto());

            batchButton.setToolTipText("batch");
            batchButton.setSelected(layoutStampa.getBatch());

            numeroCopieButton.setToolTipText("Conferma del numero copie");
            numeroCopieButton.setSelected(layoutStampa.getConfermaNumeroCopie());

            previewButton.setToolTipText("Visualizza l'anteprima del report");
            previewButton.setSelected(layoutStampa.getPreview());

            mailButton.setToolTipText("Usa il report per l'invio delle email");
            mailButton.setSelected(false);
            mailButton.setSelected(layoutStampa.isMailLayout());

            internoButton.setToolTipText("Layout per stampe ad uso interno");
            internoButton.setSelected(false);
            internoButton.setSelected(layoutStampa.isInterno());

            removeButton.setToolTipText("Rimuovi layout");
        }
        editorRendererComponent.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
        editorRendererComponent.setForeground(isSelected ? table.getSelectionForeground() : table.getForeground());
    }

    /**
     * Crea un button standard per il renderer/editor.
     *
     * @param idIcon
     *            id icona
     * @return button
     */
    private JButton createButton(String idIcon) {
        JButton button = new JideButton(RcpSupport.getIcon(idIcon));
        button.setOpaque(false);
        button.setContentAreaFilled(true);
        button.setFocusPainted(false);
        button.setFocusable(false);
        button.setRequestFocusEnabled(false);
        return button;
    }

    @Override
    public Component createTableCellEditorRendererComponent(JTable table, int row, int column) {
        JPanel panel = new JPanel(new GridLayout(1, 3, 2, 0));
        panel.setOpaque(true);
        panel.add(createToggleButton("txt"));
        panel.add(createToggleButton("batch"));
        panel.add(createToggleButton("copyCommand.icon"));
        panel.add(createToggleButton("previewCommand.icon"));
        panel.add(createToggleButton("email"));
        panel.add(createToggleButton("azienda.image"));
        panel.add(createButton("deleteCommand.icon"));
        return panel;
    }

    /**
     * Crea un toggle button standard per il renderer/editor.
     *
     * @param idIcon
     *            id icona
     * @return button
     */
    private JideToggleButton createToggleButton(String idIcon) {
        JideToggleButton button = new JideToggleButton(RcpSupport.getIcon(idIcon));
        button.setOpaque(false);
        button.setContentAreaFilled(true);
        button.setFocusable(false);
        button.setRequestFocusEnabled(false);
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        return null;
    }

    @Override
    public boolean isRollover(JTable table, MouseEvent e, int row, int column) {
        return true;
    }
}