package it.eurotn.panjea.preventivi.rich.editors.righepreventivo;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;

import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.image.EmptyIcon;
import org.springframework.richclient.image.IconSize;
import org.springframework.richclient.image.IconSource;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.grid.ContextSensitiveCellRenderer;
import com.jidesoft.grid.EditorContext;
import com.jidesoft.grid.TableModelWrapperUtils;

import it.eurotn.panjea.preventivi.util.RigaArticoloDTO;
import it.eurotn.panjea.preventivi.util.RigaPreventivoDTO;
import it.eurotn.panjea.preventivi.util.RigaTestataDTO;
import it.eurotn.panjea.rich.components.CompoundIcon;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

public class LivelloRigaPreventivoCellRenderer extends ContextSensitiveCellRenderer {
    private static final long serialVersionUID = -2948072765936344415L;

    public static final EditorContext LIVELLO_RIGA_PREVENTIVO_CONTEXT = new EditorContext(
            "LIVELLO_RIGA_PREVENTIVO_CONTEXT");
    private static final String RIGA_CHIUSA_ICON = "rigaChiusa.icon";
    public static final String RIGA_TESTATA_ICON = "rigaTestataOrdine.icon";

    private final IconSource iconSource = (IconSource) ApplicationServicesLocator.services()
            .getService(IconSource.class);

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {

        JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        label.setOpaque(false);

        @SuppressWarnings("unchecked")
        DefaultBeanTableModel<RigaPreventivoDTO> tableModel = (DefaultBeanTableModel<RigaPreventivoDTO>) TableModelWrapperUtils
                .getActualTableModel(table.getModel());

        int actualRow = TableModelWrapperUtils.getActualRowAt(table.getModel(), row);
        if (actualRow == -1) {
            return label;
        }

        RigaPreventivoDTO rigaPreventivo = tableModel.getObject(actualRow);
        List<Icon> icone = new ArrayList<Icon>();
        Integer livello = rigaPreventivo.getLivello() != null ? rigaPreventivo.getLivello() : new Integer(0);
        for (int i = 0; i < livello; i++) {
            icone.add(new EmptyIcon(IconSize.SMALL));
        }

        if (rigaPreventivo instanceof RigaArticoloDTO && ((RigaArticoloDTO) rigaPreventivo).isChiusa()) {
            icone.add(iconSource.getIcon(RIGA_CHIUSA_ICON));
        }

        if (rigaPreventivo instanceof RigaTestataDTO) {
            label.setText("<html><B>" + ((RigaTestataDTO) rigaPreventivo).getDescrizione() + "</B></html>");
            icone.add(iconSource.getIcon(RIGA_TESTATA_ICON));
        }

        CompoundIcon icon = new CompoundIcon(icone.toArray(new Icon[icone.size()]));
        label.setIcon(icon);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(label.getBackground());
        panel.add(label, BorderLayout.CENTER);

        JLabel labelNote = null;
        if (rigaPreventivo instanceof RigaArticoloDTO) {
            String note = ((RigaArticoloDTO) rigaPreventivo).getNoteRiga();
            if (note != null && !note.isEmpty()) {
                labelNote = new JLabel(RcpSupport.getIcon("note.icon"));
                labelNote.setToolTipText(note);
                panel.add(labelNote, BorderLayout.EAST);
                panel.setToolTipText(note);
            }
        }
        return panel;
    }
}
