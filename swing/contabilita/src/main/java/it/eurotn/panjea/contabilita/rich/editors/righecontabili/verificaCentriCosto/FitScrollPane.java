package it.eurotn.panjea.contabilita.rich.editors.righecontabili.verificaCentriCosto;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseWheelListener;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;

public class FitScrollPane extends JScrollPane implements ComponentListener {
	public FitScrollPane() {
		initScrollPane();
	}

	public FitScrollPane(Component view) {
		super(view);
		initScrollPane();
	}

	public FitScrollPane(Component view, int vsbPolicy, int hsbPolicy) {
		super(view, vsbPolicy, hsbPolicy);
		initScrollPane();
	}

	public FitScrollPane(int vsbPolicy, int hsbPolicy) {
		super(vsbPolicy, hsbPolicy);
		initScrollPane();
	}

	public void componentHidden(ComponentEvent e) {
	}

	public void componentMoved(ComponentEvent e) {
	}

	public void componentResized(ComponentEvent e) {
		setSize(getSize().width, getPreferredSize().height);
	}

	public void componentShown(ComponentEvent e) {
	}

	@Override
	public Dimension getPreferredSize() {
		getViewport().setPreferredSize(getViewport().getView().getPreferredSize());
		return super.getPreferredSize();
	}

	private void initScrollPane() {
		setBorder(BorderFactory.createLineBorder(Color.GRAY));
		setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		getViewport().getView().addComponentListener(this);
		removeMouseWheelListeners();
	}

	// remove MouseWheelListener as there is no need for it in FitScrollPane.
	private void removeMouseWheelListeners() {
		MouseWheelListener[] listeners = getMouseWheelListeners();
		for (MouseWheelListener listener : listeners) {
			removeMouseWheelListener(listener);
		}
	}

	@Override
	public void updateUI() {
		super.updateUI();
		removeMouseWheelListeners();
	}
}