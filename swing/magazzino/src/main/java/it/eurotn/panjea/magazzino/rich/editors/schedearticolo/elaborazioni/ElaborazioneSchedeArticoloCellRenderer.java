/**
 * 
 */
package it.eurotn.panjea.magazzino.rich.editors.schedearticolo.elaborazioni;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;

import com.jidesoft.grid.ContextSensitiveCellRenderer;
import com.jidesoft.grid.EditorContext;
import com.jidesoft.swing.StyledLabel;

/**
 * @author fattazzo
 * 
 */
public class ElaborazioneSchedeArticoloCellRenderer extends ContextSensitiveCellRenderer {

	private static final long serialVersionUID = -4187370017085966718L;

	public static final EditorContext ELABORAZIONE_SCHEDE_ARTICOLO_CONTEXT = new EditorContext(
			"ELABORAZIONE_SCHEDE_ARTICOLO_CONTEXT");

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		StyledLabel styledLabel = new StyledLabel(label.getText());
		styledLabel.setLineWrap(true);
		styledLabel.setRows(1);
		styledLabel.setMaxRows(10);
		return styledLabel;
	}
}
