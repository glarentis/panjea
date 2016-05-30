package it.eurotn.panjea.ordini.rich.editors.evasione.carrello.sostituzione;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.border.AbstractBorder;

public class BottomBorder extends AbstractBorder {

	private static final long serialVersionUID = -5127204883700024598L;

	protected int thickness;

	protected Color lineColor;

	protected int gap;

	/**
	 * Costruttore.
	 * 
	 * @param color
	 *            colore della linea
	 */
	public BottomBorder(final Color color) {
		this(color, 1, 1);
	}

	/**
	 * Costruttore.
	 * 
	 * @param color
	 *            colore della linea
	 * @param thickness
	 *            spessore
	 */
	public BottomBorder(final Color color, final int thickness) {
		this(color, thickness, thickness);
	}

	/**
	 * Costruttore.
	 * 
	 * @param color
	 *            colore della linea
	 * @param thickness
	 *            spessore
	 * @param gap
	 *            gap
	 */
	public BottomBorder(final Color color, final int thickness, final int gap) {
		lineColor = color;
		this.thickness = thickness;
		this.gap = gap;
	}

	@Override
	public Insets getBorderInsets(Component c) {
		return new Insets(0, 0, gap, 0);
	}

	@Override
	public Insets getBorderInsets(Component c, Insets insets) {
		insets.left = 0;
		insets.top = 0;
		insets.right = 0;
		insets.bottom = gap;
		return insets;
	}

	/**
	 * @return the gap
	 */
	public int getGap() {
		return gap;
	}

	/**
	 * @return the color of the border.
	 */
	public Color getLineColor() {
		return lineColor;
	}

	/**
	 * @return the thickness of the border.
	 */
	public int getThickness() {
		return thickness;
	}

	@Override
	public boolean isBorderOpaque() {
		return false;
	}

	@Override
	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
		Color oldColor = g.getColor();
		int i;

		g.setColor(lineColor);
		for (i = 0; i < thickness; i++) {
			g.drawLine(x, y + height - i - 1, x + width, y + height - i - 1);
		}
		g.setColor(oldColor);
	}

}