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

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.richclient.application.Application;

/**
 * A simple listener that reacts to OpenEditorEvents and calls openEditor on the currently active page.
 * 
 * @author Jonny Wray
 * 
 */
public class WorkspaceListener implements ApplicationListener {

	// In reality this should probably be launched in a worker
	// thread to account for open events that take some time.
	/**
	 * Riceve eventi dall'applicazione e se è un openEditor rigira la chiamata alla Pagina attiva.
	 * 
	 * @param event
	 *            evento ricevuto
	 */
	@Override
	public void onApplicationEvent(final ApplicationEvent event) {
		if (event instanceof OpenEditorEvent) {
			OpenEditorEvent editorEvent = (OpenEditorEvent) event;
			Application.instance().getActiveWindow().getPage().openEditor(editorEvent.getObject());
		}
	}
}
