package it.eurotn.rich.components;

import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class ScaledImageLabel extends JLabel {

	private static final long serialVersionUID = 5669470584539232309L;

	@Override
	protected void paintComponent(Graphics g) {
		ImageIcon icon = (ImageIcon) getIcon();
		if (icon != null) {
			ImageDrawer.drawScaledImage(icon.getImage(), this, g);
		}
	}
}