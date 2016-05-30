package com.jidesoft.spring.richclient.docking.editor.workspace;

import com.jidesoft.document.DocumentPane;
import com.jidesoft.swing.JideTabbedPane;

public class DefaultCustomizer implements DocumentPane.TabbedPaneCustomizer {
	/**
	 * personalizzazione per il focus e i tab del worskspace.
	 * 
	 * @param tabbedPane
	 *            tabbedPane da personalizzare
	 * 
	 */
	@Override
	public void customize(JideTabbedPane tabbedPane) {
		tabbedPane.setRequestFocusEnabled(true);
		tabbedPane.setShowCloseButton(true);
		tabbedPane.setUseDefaultShowCloseButtonOnTab(false);
		tabbedPane.setShowCloseButtonOnTab(true);
	}
}