/**
 *
 */
package it.eurotn.panjea.ordini.rich.editors.righeordine;

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

import it.eurotn.panjea.ordini.util.RigaArticoloDTO;
import it.eurotn.panjea.ordini.util.RigaOrdineDTO;
import it.eurotn.panjea.ordini.util.RigaTestataDTO;
import it.eurotn.panjea.rich.components.CompoundIcon;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

/**
 * Renderer che si occupa di visualizzare il valore della riga ordine in base al suo livello.
 *
 * @author fattazzo
 *
 */
public class LivelloRigaOrdineCellRenderer extends ContextSensitiveCellRenderer {

    private static final long serialVersionUID = -2948072765936344415L;

    public static final EditorContext LIVELLO_RIGA_ORDINE_CONTEXT = new EditorContext("LIVELLO_RIGA_ORDINE_CONTEXT");
    private static final String RIGA_ORDINE_CHIUSA_ICON = "rigaChiusa.icon";
    private static final String RIGA_ORDINE_COLLEGATA_ICON = "rigaCollegata.icon";
    public static final String RIGA_TESTATA_DOCUMENTO_ICON = "rigaTestataDocumento.icon";
    public static final String RIGA_TESTATA_ICON = "rigaTestataOrdine.icon";

    private final IconSource iconSource = (IconSource) ApplicationServicesLocator.services()
            .getService(IconSource.class);

    /**
     * Costruttore.
     *
     */
    public LivelloRigaOrdineCellRenderer() {
        super();
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {

        JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        label.setOpaque(false);

        @SuppressWarnings("unchecked")
        DefaultBeanTableModel<RigaOrdineDTO> tableModel = (DefaultBeanTableModel<RigaOrdineDTO>) TableModelWrapperUtils
                .getActualTableModel(table.getModel());

        int actualRow = TableModelWrapperUtils.getActualRowAt(table.getModel(), row);
        if (actualRow == -1) {
            return label;
        }

        RigaOrdineDTO rigaOrdine = tableModel.getObject(actualRow);
        List<Icon> icone = new ArrayList<Icon>();
        Integer livello = rigaOrdine.getLivello() != null ? rigaOrdine.getLivello() : new Integer(0);
        for (int i = 0; i < livello; i++) {
            icone.add(new EmptyIcon(IconSize.SMALL));
        }

        if (rigaOrdine instanceof RigaArticoloDTO && ((RigaArticoloDTO) rigaOrdine).isEvasa()) {
            icone.add(iconSource.getIcon(RIGA_ORDINE_CHIUSA_ICON));
        }

        if (rigaOrdine instanceof RigaTestataDTO) {
            if (((RigaTestataDTO) rigaOrdine).isRigaTestataDocumento()) {
                icone.add(iconSource.getIcon(RIGA_TESTATA_DOCUMENTO_ICON));
                label.setText("<html><b>" + ((RigaTestataDTO) rigaOrdine).getDescrizione() + "</b></html>");
            } else {
                icone.add(iconSource.getIcon(RIGA_TESTATA_ICON));
                label.setText("<html><u>" + ((RigaTestataDTO) rigaOrdine).getDescrizione() + "</u></html>");
            }
        } else if (rigaOrdine.isRigaCollegata()) {
            icone.add(iconSource.getIcon(RIGA_ORDINE_COLLEGATA_ICON));
        }

        CompoundIcon icon = new CompoundIcon(icone.toArray(new Icon[icone.size()]));
        label.setIcon(icon);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(label.getBackground());
        panel.add(label, BorderLayout.CENTER);

        JLabel labelNote = null;
        if (rigaOrdine instanceof RigaArticoloDTO) {
            String note = ((RigaArticoloDTO) rigaOrdine).getNoteRiga();
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
