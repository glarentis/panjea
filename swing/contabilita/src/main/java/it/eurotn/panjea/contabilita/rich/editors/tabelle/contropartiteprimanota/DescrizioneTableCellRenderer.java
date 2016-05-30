package it.eurotn.panjea.contabilita.rich.editors.tabelle.contropartiteprimanota;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.image.IconSource;

import com.jidesoft.grid.TableModelWrapperUtils;

import it.eurotn.panjea.contabilita.domain.Conto;
import it.eurotn.panjea.contabilita.domain.ContoBase;
import it.eurotn.panjea.contabilita.domain.ControPartita;
import it.eurotn.panjea.contabilita.domain.SottoConto;
import it.eurotn.panjea.contabilita.rich.editors.righecontabili.ControPartiteAreaContabileTableModel;

/**
 * Rendere applicato alla cella che contiene la descrizione di una contropartita. Il renderer visualizza l'icona delle
 * classi (SottoConto, Conto o ContoBase) in vase alla sua tipologia seguita dalla descrizione. Nel caso la
 * contropartita sia di tipo sottoconto e l'id nullo invece della sua descrizione viene visualizzato il codice del
 * sottoconto ( completo di codice mastro e conto )
 *
 * @author fattazzo
 *
 */
public class DescrizioneTableCellRenderer extends DefaultTableCellRenderer {

    private static final long serialVersionUID = 4394812747704713757L;

    private final IconSource iconSource;

    /**
     * Costruttore.
     */
    public DescrizioneTableCellRenderer() {
        super();
        this.iconSource = (IconSource) Application.services().getService(IconSource.class);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {
        JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        ControPartiteAreaContabileTableModel model = (ControPartiteAreaContabileTableModel) TableModelWrapperUtils
                .getActualTableModel(table.getModel());
        int actualRow = TableModelWrapperUtils.getActualRowAt(table.getModel(), row);
        ControPartita controPartita = model.getObject(actualRow);

        // aggiungo l'icona in base alla tipologia della contropartita
        switch (controPartita.getTipologiaContoControPartita()) {
        case CONTO:
            label.setIcon(iconSource.getIcon(Conto.class.getName()));
            break;
        case SOTTOCONTO:
            // se la contropartita è di tipo sottoconto e l'id è nullo ( significa che è una contropartita generata da
            // una contropartita di tipo conto ) invece della descrizione visualizzo il codice sottoconto.
            if (controPartita.isNew()) {
                SottoConto sottoConto = null;
                if (controPartita.getAvere() == null) {
                    sottoConto = controPartita.getDare();
                } else {
                    sottoConto = controPartita.getAvere();
                }
                label.setText("           " + sottoConto.getSottoContoCodice() + " - " + sottoConto.getDescrizione());
                label.setIcon(null);
            } else {
                label.setIcon(iconSource.getIcon(SottoConto.class.getName()));
            }
            break;
        case CONTO_BASE:
            label.setIcon(iconSource.getIcon(ContoBase.class.getName()));
            break;
        default:
            break;
        }

        return label;
    }
}
