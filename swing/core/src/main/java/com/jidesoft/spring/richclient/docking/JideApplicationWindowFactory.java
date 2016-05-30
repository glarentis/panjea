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
package com.jidesoft.spring.richclient.docking;

import org.springframework.richclient.application.ApplicationWindow;
import org.springframework.richclient.application.ApplicationWindowFactory;

import com.jidesoft.docking.PopupMenuCustomizer;

/**
 * Factory for JideApplicationWindow objects. Actually constructs the underlying JFrame extension from JIDE that forms
 * the holder frame for the application and configures the underlying docking manager based on the various configurable
 * parameters.
 * 
 * @author Jonny Wray
 * 
 */
public class JideApplicationWindowFactory implements ApplicationWindowFactory {

	@Override
	public ApplicationWindow createApplicationWindow() {
		JideApplicationWindow window = new JideApplicationWindow();
		return window;
	}

	/**
	 * Specify the popup menu customizer to be used the the docking manager.
	 * 
	 * @param customizer
	 *            classe per customizzare il menù
	 */
	public void setPopupMenuCustomizer(PopupMenuCustomizer customizer) {
	}

}
