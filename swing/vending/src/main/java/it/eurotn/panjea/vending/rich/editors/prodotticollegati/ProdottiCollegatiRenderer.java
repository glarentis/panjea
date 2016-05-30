package it.eurotn.panjea.vending.rich.editors.prodotticollegati;

import java.awt.Component;
import java.awt.Font;
import java.awt.font.TextAttribute;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JTable;

import com.jidesoft.grid.TableModelWrapperUtils;

import it.eurotn.panjea.magazzino.rich.control.table.renderer.ArticoloLiteRenderer;
import it.eurotn.panjea.manutenzioni.domain.ProdottoCollegato;
import it.eurotn.panjea.manutenzioni.domain.TipoProdottoCollegato;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

public class ProdottiCollegatiRenderer extends ArticoloLiteRenderer {

    private static final long serialVersionUID = -3353350881118634345L;

    private Class<? extends ProdottoCollegato> classToApply;

    /**
     * Costruttore.
     *
     * @param classToApply
     *            classe del prodotto alla quale applicare il renderer
     */
    public ProdottiCollegatiRenderer(final Class<? extends ProdottoCollegato> classToApply) {
        super();
        this.classToApply = classToApply;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {
        JLabel articoloLabel = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
                column);

        if (DefaultBeanTableModel.class
                .isAssignableFrom(TableModelWrapperUtils.getActualTableModel(table.getModel()).getClass())) {
            @SuppressWarnings("unchecked")
            DefaultBeanTableModel<ProdottoCollegato> tableModel = (DefaultBeanTableModel<ProdottoCollegato>) TableModelWrapperUtils
                    .getActualTableModel(table.getModel());
            int actualRow = TableModelWrapperUtils.getActualRowAt(table.getModel(), row);
            if (actualRow == -1) {
                return articoloLabel;
            }

            ProdottoCollegato prodotto = tableModel.getObject(actualRow);

            setOpaque(true);
            if (prodotto != null && prodotto.getClass().equals(classToApply)) {
                Font font = articoloLabel.getFont();
                @SuppressWarnings("unchecked")
                Map<TextAttribute, Object> attributes = (Map<TextAttribute, Object>) font.getAttributes();
                if (prodotto.getTipo() == TipoProdottoCollegato.AGGIUNTO) {
                    attributes.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_ULTRABOLD);
                    attributes.put(TextAttribute.POSTURE, TextAttribute.POSTURE_OBLIQUE);
                } else {
                    attributes.put(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
                }
                Font newFont = new Font(attributes);
                articoloLabel.setFont(newFont);
            }
        }
        return articoloLabel;
    }
}
