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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.richclient.application.PageLayoutBuilder;
import org.springframework.richclient.application.support.AbstractPageDescriptor;

import com.jidesoft.spring.richclient.docking.editor.DefaultEditorRegistry;
import com.jidesoft.spring.richclient.docking.editor.EditorRegistry;

/**
 * An implementation of a Spring RCP PageDescriptor that describes a Spring Page within a JideApplicationWindow.
 * 
 * @author Tom Corbin
 * @author Jonny Wray
 * 
 */
public class JidePageDescriptor extends AbstractPageDescriptor {
	private final Logger log = Logger.getLogger(JidePageDescriptor.class);

	private List<?> _viewDescriptors = new ArrayList();
	private EditorRegistry editorFactory = new DefaultEditorRegistry();
	private Object initialEditorContents = null;

	@Override
	public void buildInitialLayout(PageLayoutBuilder pageLayout) {
		log.debug("Building initial layout");
		for (Iterator iter = _viewDescriptors.iterator(); iter.hasNext();) {
			String viewDescriptorId = (String) iter.next();
			pageLayout.addView(viewDescriptorId);
			if (log.isDebugEnabled()) {
				log.debug("Added " + viewDescriptorId + " to page layout");
			}
		}
	}

	public EditorRegistry getEditorFactory() {
		return editorFactory;
	}

	public Object getInitialEditorContents() {
		return initialEditorContents;
	}

	public List getViewDescriptors() {
		return _viewDescriptors;
	}

	/**
	 * @param beanName
	 *            The bean name is the default id
	 */
	@Override
	public void setBeanName(String beanName) {
		super.setBeanName(beanName);
	}

	/**
	 * Injects an editor factory to be used to obtain editor descriptors from a given editor object, the details of
	 * which is of course application specific. If an editor factory is not injected the default is used which simply
	 * returns nothing for every editor object
	 * 
	 * @param editorFactory
	 *            editorFactory
	 */
	public void setEditorFactory(EditorRegistry editorFactory) {
		this.editorFactory = editorFactory;
	}

	/**
	 * 
	 * @param initialEditorContents
	 *            This sets the editor object that should be opened when the application is started.
	 */
	public void setInitialEditorContents(Object initialEditorContents) {
		this.initialEditorContents = initialEditorContents;
	}

	/**
	 * 
	 * @param viewDescriptors
	 *            viewDescriptor utilizzato per craere i controlli delle varie view
	 */
	public void setViewDescriptors(List<?> viewDescriptors) {
		_viewDescriptors = viewDescriptors;
	}
}
