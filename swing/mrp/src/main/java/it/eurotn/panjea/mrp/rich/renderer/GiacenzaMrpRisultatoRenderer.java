package it.eurotn.panjea.mrp.rich.renderer;

import it.eurotn.panjea.mrp.domain.RisultatoMrpFlat;
import it.eurotn.panjea.mrp.rich.editors.risultato.RisultatoTableModel;

import java.awt.Component;
import java.awt.GridLayout;
import java.text.DecimalFormat;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import com.jidesoft.grid.CellStyle;
import com.jidesoft.grid.ContextSensitiveCellRenderer;
import com.jidesoft.grid.EditorContext;
import com.jidesoft.grid.TableModelWrapperUtils;

public class GiacenzaMrpRisultatoRenderer extends ContextSensitiveCellRenderer {

	private static final long serialVersionUID = -8421079767779397558L;

	public static final EditorContext GIACENZA_RISULTATO_FLAT_CONTEXT = new EditorContext(
			"GIACENZA_RISULTATO_FLAT_CONTEXT");

	/**
	 * Crea labelper la giacenza.
	 *
	 * @param testo
	 *            testo
	 * @param tableModel
	 *            tableModel
	 * @param row
	 *            row
	 * @param column
	 *            column
	 * @return label
	 */
	private JLabel creaLabel(String testo, RisultatoTableModel tableModel, int row, int column) {
		JLabel result = new JLabel(testo);
		result.setHorizontalAlignment(SwingConstants.RIGHT);
		if (tableModel.isCellStyleOn() && tableModel.getCellStyleAt(row, column) != null) {
			CellStyle style = tableModel.getCellStyleAt(row, column);
			result.setFont(this.getFont().deriveFont(style.getFontStyle()));
			result.setForeground(style.getForeground());
		}
		return result;
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {

		RisultatoTableModel tableModel = (RisultatoTableModel) TableModelWrapperUtils.getActualTableModel(table
				.getModel());

		int actualRow = TableModelWrapperUtils.getActualRowAt(table.getModel(), row);
		if (actualRow == -1) {
			return this;
		}

		RisultatoMrpFlat rigaRisultato = tableModel.getObject(actualRow);

		JPanel panelGiacenza = new JPanel(new GridLayout(1, 3));

		StringBuilder decimalFormatStringBuilder = new StringBuilder("#,###,###,##0");
		if (rigaRisultato.getArticolo().getNumeroDecimaliQta() > 0) {
			decimalFormatStringBuilder.append(".");
			decimalFormatStringBuilder.append(StringUtils.repeat("0", rigaRisultato.getArticolo()
					.getNumeroDecimaliQta()));
		}

		DecimalFormat df = new DecimalFormat(decimalFormatStringBuilder.toString());

		String formatDisponib = rigaRisultato.getDisponibilita() != null ? df.format(rigaRisultato.getDisponibilita())
				: null;
		String formatDisponibDopoUt = rigaRisultato.getDisponibilitaDopoUtilizzo() != null ? df.format(rigaRisultato
				.getDisponibilitaDopoUtilizzo()) : null;
		String formatGiac = rigaRisultato.getGiacenza() != null ? df.format(rigaRisultato.getGiacenza()) : null;

		String disponibilita = ObjectUtils.defaultIfNull(formatDisponib, "");
		String disponibilitaDopoUtilizzo = ObjectUtils.defaultIfNull(formatDisponibDopoUt, "");
		String giacenza = ObjectUtils.defaultIfNull(formatGiac, "");

		JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		JLabel disp = creaLabel(disponibilita, tableModel, actualRow, column);
		disp.setBackground(label.getBackground());

		JLabel dispUt = creaLabel(disponibilitaDopoUtilizzo, tableModel, actualRow, column);
		JLabel giac = creaLabel(giacenza, tableModel, actualRow, column);

		panelGiacenza.add(disp);
		panelGiacenza.add(dispUt);
		panelGiacenza.add(giac);

		panelGiacenza.setOpaque(true);
		panelGiacenza.setBackground(label.getBackground());
		panelGiacenza.setBorder(BorderFactory.createEmptyBorder());
		return panelGiacenza;
	}
}
