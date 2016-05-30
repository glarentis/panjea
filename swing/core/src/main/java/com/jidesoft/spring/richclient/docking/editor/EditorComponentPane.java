/*
 * Copyright 2005 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.jidesoft.spring.richclient.docking.editor;

import java.awt.*;

import javax.swing.*;

import org.springframework.richclient.application.*;
import org.springframework.richclient.application.support.SimplePageComponentPane;

public class EditorComponentPane extends SimplePageComponentPane {

	public EditorComponentPane(PageComponent component) {
		super(component);
	}

	protected JComponent createControl() {
		JPanel panel = new JPanel(new BorderLayout());
		JPanel actionBarsPanel = createActionBarsPanel();
		if (actionBarsPanel != null) {
			panel.add(actionBarsPanel, BorderLayout.NORTH);
		}
		Component component = ((Editor) getPageComponent()).getControl();
		panel.add(component, BorderLayout.CENTER);
		return panel;
	}

	private JPanel createActionBarsPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		JMenuBar menuBar = createViewMenuBar();
		JToolBar toolBar = createViewToolBar();
		if (menuBar == null && toolBar == null) {
			return null;
		}
		if (menuBar != null && toolBar != null) {
			panel.add(menuBar, BorderLayout.NORTH);
			panel.add(toolBar, BorderLayout.SOUTH);
		} else if (menuBar != null) {
			panel.add(menuBar, BorderLayout.NORTH);
		} else if (toolBar != null) {
			panel.add(toolBar, BorderLayout.NORTH);
		}
		panel.setBorder(BorderFactory.createLoweredBevelBorder());
		return panel;
	}

	/**
	 * Returns the view specific toolbar if the underlying view implementation supports it.
	 */
	protected JToolBar createViewToolBar() {
		if (getPageComponent() instanceof AbstractEditor) {
			AbstractEditor editor = (AbstractEditor) getPageComponent();
			return editor.getEditorToolBar();
		}
		return null;
	}

	/**
	 * Returns the view specific menubar if the underlying view implementation supports it.
	 */
	protected JMenuBar createViewMenuBar() {
		if (getPageComponent() instanceof AbstractEditor) {
			AbstractEditor editor = (AbstractEditor) getPageComponent();
			return editor.getEditorMenuBar();
		}
		return null;
	}
}
