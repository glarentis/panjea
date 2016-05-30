/**
 *
 */
package it.eurotn.panjea.magazzino.rich.editors.righemagazzino;

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

import it.eurotn.panjea.magazzino.util.RigaArticoloDTO;
import it.eurotn.panjea.magazzino.util.RigaMagazzinoDTO;
import it.eurotn.panjea.magazzino.util.RigaTestataDTO;
import it.eurotn.panjea.rich.components.CompoundIcon;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

/**
 * Renderer che si occupa di visualizzare il valore della riga magazzino in base al suo livello.
 *
 * @author fattazzo
 *
 */
public class LivelloRigaMagazzinoCellRenderer extends ContextSensitiveCellRenderer {

    private static final long serialVersionUID = -2948072765936344415L;

    public static final EditorContext LIVELLO_RIGA_MAGAZZINO_CONTEXT = new EditorContext(
            "LIVELLO_RIGA_MAGAZZINO_CONTEXT");
    private static final String RIGA_MAGAZZINO_COLLEGATA_ICON = "rigaCollegata.icon";
    private static final String RIGA_MAGAZZINO_CHIUSA_ICON = "rigaChiusa.icon";
    public static final String RIGA_TESTATA_DOCUMENTO_ICON = "rigaTestataDocumento.icon";
    public static final String RIGA_TESTATA_ICON = "rigaTestataMagazzino.icon";

    private final IconSource iconSource = (IconSource) ApplicationServicesLocator.services()
            .getService(IconSource.class);

    /**
     * Costruttore.
     *
     */
    public LivelloRigaMagazzinoCellRenderer() {
        super();
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {

        JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        label.setOpaque(false);

        @SuppressWarnings("unchecked")
        DefaultBeanTableModel<RigaMagazzinoDTO> tableModel = (DefaultBeanTableModel<RigaMagazzinoDTO>) TableModelWrapperUtils
                .getActualTableModel(table.getModel());

        int actualRow = TableModelWrapperUtils.getActualRowAt(table.getModel(), row);
        if (actualRow == -1) {
            return label;
        }

        RigaMagazzinoDTO rigaMagazzino = tableModel.getObject(actualRow);

        label.setIcon(null);
        List<Icon> icone = new ArrayList<Icon>();
        Integer livello = rigaMagazzino.getLivello() != null ? rigaMagazzino.getLivello() : new Integer(0);
        for (int i = 0; i < livello; i++) {
            icone.add(new EmptyIcon(IconSize.SMALL));
        }

        if (rigaMagazzino instanceof RigaArticoloDTO && ((RigaArticoloDTO) rigaMagazzino).isChiusa()) {
            icone.add(iconSource.getIcon(RIGA_MAGAZZINO_CHIUSA_ICON));
        }

        if (rigaMagazzino instanceof RigaTestataDTO) {
            if (((RigaTestataDTO) rigaMagazzino).isRigaTestataDocumento()) {
                icone.add(iconSource.getIcon(RIGA_TESTATA_DOCUMENTO_ICON));
                label.setText("<html><b>" + ((RigaTestataDTO) rigaMagazzino).getDescrizione() + "</b></html>");
            } else {
                icone.add(iconSource.getIcon(RIGA_TESTATA_ICON));
                label.setText("<html><u>" + ((RigaTestataDTO) rigaMagazzino).getDescrizione() + "</u></html>");
            }
        } else if (rigaMagazzino.isRigaCollegata()) {
            icone.add(iconSource.getIcon(RIGA_MAGAZZINO_COLLEGATA_ICON));
        }

        CompoundIcon icon = new CompoundIcon(icone.toArray(new Icon[icone.size()]));
        label.setIcon(icon);
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(label.getBackground());
        panel.add(label, BorderLayout.CENTER);

        JLabel labelNote = null;
        if (rigaMagazzino instanceof RigaArticoloDTO) {
            String note = ((RigaArticoloDTO) rigaMagazzino).getNoteRiga();
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
