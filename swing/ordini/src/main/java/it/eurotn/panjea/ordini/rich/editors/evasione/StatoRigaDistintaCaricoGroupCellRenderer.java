package it.eurotn.panjea.ordini.rich.editors.evasione;

import it.eurotn.panjea.ordini.domain.documento.evasione.RigaDistintaCarico;
import it.eurotn.panjea.ordini.domain.documento.evasione.RigaDistintaCarico.StatoRiga;
import it.eurotn.panjea.rich.components.CompoundIcon;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;

import org.springframework.richclient.image.EmptyIcon;

import com.jidesoft.grid.CellRendererManager;
import com.jidesoft.grid.DefaultGroupRow;
import com.jidesoft.grid.IndexReferenceRow;
import com.jidesoft.grid.TableModelWrapperUtils;

public class StatoRigaDistintaCaricoGroupCellRenderer extends StatoRigaDistintaCaricoCellRenderer {
	private static final long serialVersionUID = 3131358058774840111L;

	/**
	 * Recupera lo stato di un riga raggruppata in base allo stato delle righe contenute.<br/>
	 * 
	 * 
	 * @param rowGroup
	 *            riga raggrupata
	 * @param tableModel
	 *            table model base
	 * @return stato della riga gruppo. Se almeno una riga è selezionabile il gruppo diventa selezionabile.
	 */
	private StatoRiga getStatoRigaRaggruppata(DefaultBeanTableModel<RigaDistintaCarico> tableModel,
			DefaultGroupRow rowGroup) {
		StatoRiga statoRiga = StatoRiga.SELEZIONATA;
		for (Object indexRow : rowGroup.getChildren()) {
			if (indexRow instanceof DefaultGroupRow) {
				statoRiga = getStatoRigaRaggruppata(tableModel, (DefaultGroupRow) indexRow);
				if (statoRiga == StatoRiga.SELEZIONABILE) {
					break;
				}
			} else {
				RigaDistintaCarico rigaEvasione = tableModel.getObject(((IndexReferenceRow) indexRow).getRowIndex());
				statoRiga = rigaEvasione.getStato();
				if (statoRiga == StatoRiga.SELEZIONABILE) {
					break;
				}
			}
		}
		return statoRiga;
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {

		JLabel labelRender = (JLabel) CellRendererManager.getRenderer(value.getClass()).getTableCellRendererComponent(
				table, value, isSelected, hasFocus, row, column);
		JLabel label = new JLabel();
		label.setToolTipText(null);
		label.setOpaque(false);
		label.setText(labelRender.getText());

		@SuppressWarnings("unchecked")
		DefaultBeanTableModel<RigaDistintaCarico> model = (DefaultBeanTableModel<RigaDistintaCarico>) TableModelWrapperUtils
				.getActualTableModel(table.getModel(), DefaultBeanTableModel.class);

		StatoRiga statoRiga = getStatoRigaRaggruppata(model, (DefaultGroupRow) value);

		label.setIcon(new CompoundIcon(ICONS[statoRiga.ordinal()], labelRender.getIcon() == null ? EmptyIcon.SMALL
				: labelRender.getIcon()));
		return label;
	}

}
