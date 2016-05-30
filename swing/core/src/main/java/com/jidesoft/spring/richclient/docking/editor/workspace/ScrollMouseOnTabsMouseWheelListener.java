package com.jidesoft.spring.richclient.docking.editor.workspace;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import com.jidesoft.spring.richclient.docking.editor.WorkspaceView;

/**
 * Scroll del mouse cambia editor ciclando tra essi in sintonia con la direzione scelta.
 */
public class ScrollMouseOnTabsMouseWheelListener implements MouseWheelListener {
	private final WorkspaceView workspaceView;

	/**
	 * Costruttore.
	 * 
	 * @param workspaceView
	 *            workspaceView
	 */
	public ScrollMouseOnTabsMouseWheelListener(final WorkspaceView workspaceView) {
		super();
		this.workspaceView = workspaceView;
	}

	/**
	 * attiva gli editor.
	 * 
	 * @param e
	 *            evento
	 */
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		int direction = e.getWheelRotation();
		if (direction > 0) {
			workspaceView.nextEditor();
		} else {
			workspaceView.prevEditor();
		}
	}
}