package com.jidesoft.spring.richclient.docking.editor.workspace;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;

import com.jidesoft.docking.DockingManager;

public class DoubleClickListener extends MouseAdapter {

	private final DockingManager manager;
	private byte[] fullScreenLayout = null;

	/**
	 * 
	 * @param manager
	 *            dockingManager al quale aggiungere il listener
	 */
	public DoubleClickListener(final DockingManager manager) {
		super();
		this.manager = manager;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 2) {
			if (manager.isAutohideAllTabs()) {
				fullScreenLayout = manager.getLayoutRawData();
				manager.autohideAll();
			} else {
				// call next two methods so that the farme bounds and state will not change.
				manager.setUseFrameBounds(false);
				manager.setUseFrameState(false);
				if (fullScreenLayout != null) {
					manager.setLayoutRawData(fullScreenLayout);
				}
			}
		}
	}
}