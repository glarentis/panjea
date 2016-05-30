package it.eurotn.panjea.magazzino.rich.editors.confrontolistino;

import it.eurotn.rich.control.table.style.DefaultCellStyleProvider;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import javax.swing.table.TableModel;

import com.jidesoft.grid.CellStyle;

public class ControlloStripeCellStypeProvider extends DefaultCellStyleProvider {

	private class CellStyle1 extends CellStyle {

		@Override
		public Color getBackground() {
			return new Color(RED1, GREEN1, BLUE1);
		}
	}

	private class CellStyle10 extends CellStyle {

		@Override
		public Color getBackground() {
			return new Color(RED2, GREEN2, BLUE2);
		}
	}

	private class CellStyle2 extends CellStyle {

		@Override
		public Color getBackground() {
			return new Color(RED2 + (STEPCOLOR * 4), GREEN2 + (STEPCOLOR * 4), BLUE2 + (STEPCOLOR * 4));
		}
	}

	private class CellStyle3 extends CellStyle {

		@Override
		public Color getBackground() {
			return new Color(RED1 + (STEPCOLOR * 1), GREEN1 + (STEPCOLOR * 1), BLUE1 + (STEPCOLOR * 1));
		}
	}

	private class CellStyle4 extends CellStyle {

		@Override
		public Color getBackground() {
			return new Color(RED2 + (STEPCOLOR * 3), GREEN2 + (STEPCOLOR * 3), BLUE2 + (STEPCOLOR * 3));
		}
	}

	private class CellStyle5 extends CellStyle {

		@Override
		public Color getBackground() {
			return new Color(RED1 + (STEPCOLOR * 2), GREEN1 + (STEPCOLOR * 2), BLUE1 + (STEPCOLOR * 2));
		}
	}

	private class CellStyle6 extends CellStyle {

		@Override
		public Color getBackground() {
			return new Color(RED2 + (STEPCOLOR * 2), GREEN2 + (STEPCOLOR * 2), BLUE2 + (STEPCOLOR * 2));
		}
	}

	private class CellStyle7 extends CellStyle {

		@Override
		public Color getBackground() {
			return new Color(RED1 + (STEPCOLOR * 3), GREEN1 + (STEPCOLOR * 3), BLUE1 + (STEPCOLOR * 3));
		}
	}

	private class CellStyle8 extends CellStyle {

		@Override
		public Color getBackground() {
			return new Color(RED2 + (STEPCOLOR * 1), GREEN2 + (STEPCOLOR * 1), BLUE2 + (STEPCOLOR * 1));
		}
	}

	private class CellStyle9 extends CellStyle {

		@Override
		public Color getBackground() {
			return new Color(RED1 + (STEPCOLOR * 4), GREEN1 + (STEPCOLOR * 4), BLUE1 + (STEPCOLOR * 4));
		}
	}

	private boolean enableConfrontoColumnsStripe;

	private Map<Integer, CellStyle> cellStyleMap;

	public static final int RED1 = 129;
	public static final int GREEN1 = 144;
	public static final int BLUE1 = 163;

	public static final int RED2 = 119;
	public static final int GREEN2 = 173;
	public static final int BLUE2 = 116;

	public static final int STEPCOLOR = 20;

	{
		enableConfrontoColumnsStripe = true;

		cellStyleMap = new HashMap<Integer, CellStyle>();
		cellStyleMap.put(1, new CellStyle1());
		cellStyleMap.put(2, new CellStyle2());
		cellStyleMap.put(3, new CellStyle3());
		cellStyleMap.put(4, new CellStyle4());
		cellStyleMap.put(5, new CellStyle5());
		cellStyleMap.put(6, new CellStyle6());
		cellStyleMap.put(7, new CellStyle7());
		cellStyleMap.put(8, new CellStyle8());
		cellStyleMap.put(9, new CellStyle9());
		cellStyleMap.put(10, new CellStyle10());
	}

	@Override
	public CellStyle getCellStyleAt(TableModel tablemodel, int row, int col) {

		if (enableConfrontoColumnsStripe) {
			// per le colonne iniziali non cambio e tengo lo stripe
			switch (col) {
			case 0:
			case 1:
			case 2:
				return super.getCellStyleAt(tablemodel, row, col);
			default:
				return cellStyleMap.get(col / 3);
			}
		}
		return super.getCellStyleAt(tablemodel, row, col);
	}

	/**
	 * @param enableConfrontoColumnsStripe
	 *            the enableConfrontoColumnsStripe to set
	 */
	public void setEnableConfrontoColumnsStripe(boolean enableConfrontoColumnsStripe) {
		this.enableConfrontoColumnsStripe = enableConfrontoColumnsStripe;
	}
}