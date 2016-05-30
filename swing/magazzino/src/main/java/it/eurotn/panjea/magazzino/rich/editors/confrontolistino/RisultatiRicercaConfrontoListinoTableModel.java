/**
 * 
 */
package it.eurotn.panjea.magazzino.rich.editors.confrontolistino;

import it.eurotn.panjea.magazzino.util.RigaConfrontoListinoDTO;
import it.eurotn.panjea.magazzino.util.ValoreConfronto;
import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.lang.reflect.Method;
import java.math.BigDecimal;

import com.jidesoft.converter.ConverterContext;
import com.jidesoft.grid.CellPainter;
import com.jidesoft.grid.CellStyle;
import com.jidesoft.grid.StyleModel;
import com.jidesoft.swing.JideSwingUtilities;

/**
 * @author fattazzo
 * 
 */
public class RisultatiRicercaConfrontoListinoTableModel extends DefaultBeanTableModel<RigaConfrontoListinoDTO>
		implements StyleModel {

	static final CellStyle CELL_STYLE_UNDERLAY = new CellStyle();

	private static final long serialVersionUID = -481825582816117028L;

	private static final ConverterContext PERCENTUALE_CONTEXT = new NumberWithDecimalConverterContext();
	private final ConverterContext prezzoContext;

	private boolean enableProgressBar = false;

	{
		PERCENTUALE_CONTEXT.setUserObject(2);

		prezzoContext = new NumberWithDecimalConverterContext();

		CELL_STYLE_UNDERLAY.setUnderlayCellPainter(new CellPainter() {
			@Override
			public void paint(Graphics g, Component component, int row, int column, Rectangle cellRect, Object value) {
				Graphics2D g2d = (Graphics2D) g.create();
				BigDecimal perc = (BigDecimal) value;
				if (perc == null) {
					JideSwingUtilities.fillGradient(g2d, cellRect, new Color(255, 102, 0), new Color(255, 153, 51),
							false);
				} else {
					Rectangle clip = new Rectangle(cellRect.x, cellRect.y, (int) (cellRect.width
							* perc.abs().doubleValue() / 100.0), cellRect.height);
					g2d.clip(clip);
					if (perc.doubleValue() < 0.0) {
						JideSwingUtilities.fillGradient(g2d, cellRect, Color.RED, new Color(252, 85, 85), false);
					} else if (perc.doubleValue() <= 100.0) {
						JideSwingUtilities.fillGradient(g2d, cellRect, new Color(16, 186, 16),
								new Color(153, 238, 153), false);
					} else {
						JideSwingUtilities.fillGradient(g2d, cellRect, new Color(75, 126, 176),
								new Color(170, 208, 246), false);
					}
				}
				g2d.dispose();
			}
		});
	}

	/**
	 * Costruttore.
	 */
	public RisultatiRicercaConfrontoListinoTableModel() {
		super("RisultatiRicercaConfrontoListinoTableModel", new String[] { "articolo.categoria", "articolo",
				"prezzoBase", "valoriConfronto1.prezzo", "valoriConfronto1.variazioneValore",
				"valoriConfronto1.variazionePercentuale", "valoriConfronto2.prezzo",
				"valoriConfronto2.variazioneValore", "valoriConfronto2.variazionePercentuale",
				"valoriConfronto3.prezzo", "valoriConfronto3.variazioneValore",
				"valoriConfronto3.variazionePercentuale", "valoriConfronto4.prezzo",
				"valoriConfronto4.variazioneValore", "valoriConfronto4.variazionePercentuale",
				"valoriConfronto5.prezzo", "valoriConfronto5.variazioneValore",
				"valoriConfronto5.variazionePercentuale", "valoriConfronto6.prezzo",
				"valoriConfronto6.variazioneValore", "valoriConfronto6.variazionePercentuale",
				"valoriConfronto7.prezzo", "valoriConfronto7.variazioneValore",
				"valoriConfronto7.variazionePercentuale", "valoriConfronto8.prezzo",
				"valoriConfronto8.variazioneValore", "valoriConfronto8.variazionePercentuale",
				"valoriConfronto9.prezzo", "valoriConfronto9.variazioneValore",
				"valoriConfronto9.variazionePercentuale", "valoriConfronto10.prezzo",
				"valoriConfronto10.variazioneValore", "valoriConfronto10.variazionePercentuale" },
				RigaConfrontoListinoDTO.class);
	}

	@Override
	public CellStyle getCellStyleAt(int row, int col) {
		switch (col) {
		case 5:
		case 8:
		case 11:
		case 14:
		case 17:
		case 20:
		case 23:
		case 26:
		case 29:
		case 32:
			return enableProgressBar ? CELL_STYLE_UNDERLAY : null;
		default:
			return null;
		}
	}

	@Override
	public int getColumnCount() {
		int numeroConfronti = 0;
		if (!getObjects().isEmpty()) {
			numeroConfronti = getElementAt(0).getNumeroConfronti();
		}
		return 3 + (numeroConfronti * 3);
	}

	@Override
	public String getColumnName(int column) {
		switch (column) {
		case 0:
		case 1:
		case 2:
			return super.getColumnName(column);
		default:
			String desc = "";
			try {
				Method method = RigaConfrontoListinoDTO.class.getDeclaredMethod("getValoriConfronto" + column / 3,
						(Class<?>[]) null);
				ValoreConfronto valoreConfronto = (ValoreConfronto) method.invoke(getElementAt(0), (Object[]) null);
				desc = valoreConfronto.getDescrizione();
			} catch (Exception e) {
				desc = "";
				e.printStackTrace();
			}
			return desc + " " + super.getColumnName(column);
		}
	}

	@Override
	public ConverterContext getConverterContextAt(int row, int col) {
		switch (col) {
		case 0:
		case 1:
			return null;
		case 5:
		case 8:
		case 11:
		case 14:
		case 17:
		case 20:
		case 23:
		case 26:
		case 29:
		case 32:
			return PERCENTUALE_CONTEXT;
		default:
			return prezzoContext;
		}
	}

	@Override
	public boolean isCellStyleOn() {
		return true;
	}

	/**
	 * @param enableProgressBar
	 *            the enableProgressBar to set
	 */
	public void setEnableProgressBar(boolean enableProgressBar) {
		this.enableProgressBar = enableProgressBar;
	}

	/**
	 * @param numeroDecimaliPrezzo
	 *            the numeroDecimaliPrezzo to set
	 */
	public void setNumeroDecimaliPrezzo(Integer numeroDecimaliPrezzo) {
		prezzoContext.setUserObject(numeroDecimaliPrezzo);
	}

}
