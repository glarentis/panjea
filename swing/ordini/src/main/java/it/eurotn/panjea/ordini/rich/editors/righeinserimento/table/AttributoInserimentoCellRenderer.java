package it.eurotn.panjea.ordini.rich.editors.righeinserimento.table;

import java.awt.Component;
import java.awt.GridLayout;
import java.math.BigDecimal;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;

import com.jidesoft.grid.TableModelWrapperUtils;

import it.eurotn.panjea.magazzino.domain.AttributoRigaArticolo;
import it.eurotn.panjea.magazzino.domain.TipoAttributo.ETipoDatoTipoAttributo;
import it.eurotn.panjea.ordini.util.righeinserimento.RigaOrdineInserimento;
import it.eurotn.panjea.util.DefaultNumberFormatterFactory;
import it.eurotn.rich.control.table.DefaultBeanTableModel;
import it.eurotn.rich.control.table.renderer.IconContextSensitiveCellRenderer;

public class AttributoInserimentoCellRenderer extends IconContextSensitiveCellRenderer {

	private static final long serialVersionUID = 7541360529351674600L;

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {

		JLabel labelValoreIns = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
				column);
		labelValoreIns.setHorizontalAlignment(getTextPosition());
		JLabel labelValore = new JLabel();
		labelValore.setBackground(labelValoreIns.getBackground());
		labelValore.setForeground(labelValoreIns.getForeground());
		labelValore.setHorizontalAlignment(getTextPosition());

		if (DefaultBeanTableModel.class
				.isAssignableFrom(TableModelWrapperUtils.getActualTableModel(table.getModel()).getClass())) {
			@SuppressWarnings("unchecked")
			DefaultBeanTableModel<RigaOrdineInserimento> tableModel = (DefaultBeanTableModel<RigaOrdineInserimento>) TableModelWrapperUtils
					.getActualTableModel(table.getModel());
			int actualRow = TableModelWrapperUtils.getActualRowAt(table.getModel(), row);
			if (actualRow == -1) {
				return labelValoreIns;
			}

			RigaOrdineInserimento riga = tableModel.getObject(actualRow);
			AttributoRigaArticolo attributo = riga.getAttributi().get(table.getColumnName(column));
			AttributoRigaArticolo attributoIns = riga.getAttributiInserimento().get(table.getColumnName(column));

			labelValoreIns.setOpaque(true);
			if (attributo != null) {
				String valoreString = attributo.getValore();
				String valoreInsString = attributoIns.getValore();

				if (attributoIns.isUpdatable()
						&& attributoIns.getTipoAttributo().getTipoDato() == ETipoDatoTipoAttributo.NUMERICO) {
					valoreString = getValoreAttributoFormattato(attributo.getTipoAttributo().getNumeroDecimali(),
							attributo.getValoreTipizzato());
					valoreInsString = getValoreAttributoFormattato(attributoIns.getTipoAttributo().getNumeroDecimali(),
							attributoIns.getValoreTipizzato());

					labelValore.setText(valoreString);
					labelValoreIns.setText(valoreInsString);
					labelValoreIns.setOpaque(false);

					JPanel panel = new JPanel(new GridLayout(1, 2));
					panel.setBackground(labelValoreIns.getBackground());
					panel.add(labelValore);
					panel.add(labelValoreIns);
					return panel;
				}
			}
		}

		return labelValoreIns;
	}

	private String getValoreAttributoFormattato(int numeroDecimali, Object valoreTipizzato) {
		try {
			return new DefaultNumberFormatterFactory("#,##0", numeroDecimali, BigDecimal.class).getDefaultFormatter()
					.valueToString(valoreTipizzato);
		} catch (Exception e) {
			return "";
		}
	}
}
