package it.eurotn.panjea.bi.rich.editors.analisi.style;

import it.eurotn.rich.control.table.style.DefaultCellStyleProvider;

import java.awt.Font;

import javax.swing.JTable;

import com.jidesoft.grid.CellStyle;
import com.jidesoft.grid.TableModelWrapperUtils;
import com.jidesoft.grid.TableStyleProvider;

public class AnalisiOpenDialogTableStyleProvider implements TableStyleProvider {

	public static final CellStyle CATEGORIA_STYLE = new CellStyle();

	static {
		CATEGORIA_STYLE.setFontStyle(Font.BOLD);
		CATEGORIA_STYLE.setBackground(DefaultCellStyleProvider.ODD_COLOR);
	}

	/**
	 * Costruttore.
	 */
	public AnalisiOpenDialogTableStyleProvider() {
		// super(new Color[] { DefaultCellStyleProvider.EVEN_COLOR, DefaultCellStyleProvider.ODD_COLOR });
	}

	@Override
	public CellStyle getCellStyleAt(JTable jtable, int i, int j) {
		if (TableModelWrapperUtils.getActualRowAt(jtable.getModel(), i) == -1) {
			return CATEGORIA_STYLE;
		}
		return null;
	}
}
