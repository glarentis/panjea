package it.eurotn.panjea.ordini.rich.editors.ordiniimportati;

import it.eurotn.panjea.ordini.domain.RigaOrdineImportata;
import it.eurotn.panjea.pagamenti.domain.CodicePagamento;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.math.BigDecimal;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;

import org.jdesktop.swingx.HorizontalLayout;
import org.springframework.richclient.image.EmptyIcon;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.grid.ContextSensitiveCellRenderer;
import com.jidesoft.grid.EditorContext;
import com.jidesoft.grid.TableModelWrapperUtils;

public class NumeroRigaCellRenderer extends ContextSensitiveCellRenderer {

	private static final long serialVersionUID = -5251914557805550664L;

	public static final EditorContext NUMERO_RIGA_CONTEXT = new EditorContext("NUMERO_RIGA_CONTEXT");
	public static final Icon ICON_NOTE = RcpSupport.getIcon("note.icon");
	public static final Icon ICON_NOTE_IN_RIGA = RcpSupport.getIcon("number");
	public static final Icon ICON_FIDO = RcpSupport.getIcon("fido.icon");
	public static final Icon ICON_COD_PAG = RcpSupport.getIcon(CodicePagamento.class.getName());

	public static final JLabel NOTE_LABEL = new JLabel(ICON_NOTE);
	public static final JLabel NOTE_IN_RIGA_LABEL = new JLabel(ICON_NOTE_IN_RIGA);
	public static final JLabel FIDO_LABEL = new JLabel(ICON_FIDO);
	public static final JLabel COD_PAG_LABEL = new JLabel(ICON_COD_PAG);

	private JPanel iconsLabelPanel = null;

	/**
	 * Costruttore.
	 *
	 */
	public NumeroRigaCellRenderer() {
		super();

		iconsLabelPanel = new JPanel(new HorizontalLayout(2));
		iconsLabelPanel.add(NOTE_LABEL);
		iconsLabelPanel.add(NOTE_IN_RIGA_LABEL);
		iconsLabelPanel.add(FIDO_LABEL);
		iconsLabelPanel.add(COD_PAG_LABEL);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {

		NOTE_LABEL.setVisible(false);
		NOTE_IN_RIGA_LABEL.setVisible(false);
		FIDO_LABEL.setVisible(false);
		COD_PAG_LABEL.setVisible(false);

		JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		label.setOpaque(true);
		label.setIcon(EmptyIcon.SMALL);
		label.setHorizontalTextPosition(SwingConstants.RIGHT);

		@SuppressWarnings("unchecked")
		DefaultBeanTableModel<RigaOrdineImportata> tableModel = (DefaultBeanTableModel<RigaOrdineImportata>) TableModelWrapperUtils
		.getActualTableModel(table.getModel());

		int actualRow = TableModelWrapperUtils.getActualRowAt(table.getModel(), row);
		if (actualRow == -1) {
			return label;
		}

		RigaOrdineImportata rigaOrdine = tableModel.getObject(actualRow);

		StringBuilder sb = new StringBuilder("<HTML>");

		boolean noteEntita = rigaOrdine.getOrdine().getEntita() != null
				&& rigaOrdine.getOrdine().getEntita().getBloccoSede().isBlocco()
				&& rigaOrdine.getOrdine().getEntita().getBloccoSede().getNoteBlocco() != null;
		boolean noteSedeEntita = rigaOrdine.getOrdine().getSedeEntita() != null
				&& rigaOrdine.getOrdine().getSedeEntita().getBloccoSede().isBlocco()
				&& rigaOrdine.getOrdine().getSedeEntita().getBloccoSede().getNoteBlocco() != null;
		NOTE_LABEL.setVisible(noteEntita || noteSedeEntita);

		if (!rigaOrdine.getOrdine().isPagamentoStandard()) {
			COD_PAG_LABEL.setVisible(true);
		}

		sb.append(rigaOrdine.getOrdine().getNoteImportazione());

		if (rigaOrdine.getOrdine().getNote() != null) {
			NOTE_LABEL.setVisible(true);
			sb.append("<b>NOTE ORDINE:</b>");
			sb.append(rigaOrdine.getOrdine().getNote());
			sb.append("<BR>");
		}

		if (rigaOrdine.getNote() != null) {
			NOTE_IN_RIGA_LABEL.setVisible(true);
			sb.append("<b>NOTE RIGA:</b>");
			sb.append(rigaOrdine.getNote());
			sb.append("<BR>");
		}

		FIDO_LABEL.setVisible(rigaOrdine.getOrdine().getImportoFidoResiduo().compareTo(BigDecimal.ZERO) < 0);

		JPanel panel = new JPanel(new BorderLayout());
		panel.setOpaque(true);
		panel.setBackground(label.getBackground());
		iconsLabelPanel.setBackground(label.getBackground());
		panel.add(iconsLabelPanel, BorderLayout.WEST);
		panel.add(label, BorderLayout.EAST);
		panel.setToolTipText(sb.toString());
		return panel;
	}
}
