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

import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.richclient.application.Editor;
import org.springframework.richclient.application.PageComponent;
import org.springframework.richclient.application.PageComponentDescriptor;
import org.springframework.richclient.core.LabeledObjectSupport;

/**
 * Editor descriptor fashioned after the view descriptor, and taking a lot of
 * the implementation details from DefaultViewDescriptor. As the actual
 * interface does not do anything except bring togther a couple of other
 * interfaces I've not made a specific interface.
 *
 * @author Jonny Wray
 *
 */
public class EditorDescriptor extends LabeledObjectSupport implements PageComponentDescriptor, BeanNameAware {

	private String id;

	private Class editorClass;

	private Map editorProperties;

	private Editor createEditor() {
		if (editorClass == null) {
			throw new RuntimeException("La proprietà editorClass è vuota");
		}
		Object objectEditor = BeanUtils.instantiateClass(editorClass);
		org.springframework.util.Assert.isTrue((objectEditor instanceof Editor),
				"Editor class '" + editorClass + "' was instantiated but is not an Editor");
		Editor editor = (Editor) objectEditor;
		editor.setDescriptor(this);
		ApplicationEventMulticaster multicaster = getApplicationEventMulticaster();
		if (editor instanceof ApplicationListener && multicaster != null) {
			multicaster.addApplicationListener((ApplicationListener) editor);
		}
		if (editorProperties != null) {
			BeanWrapper wrapper = new BeanWrapperImpl(editor);
			wrapper.setPropertyValues(editorProperties);
		}
		if (editor instanceof InitializingBean) {
			try {
				((InitializingBean) editor).afterPropertiesSet();
			} catch (Exception e) {
				throw new BeanInitializationException("Problem running on " + editor, e);
			}
		}
		return editor;
	}

	/**
	 * Constructs and returns the editor, as a page component. Note that this
	 * will still need the editor object injecting into.
	 *
	 * @return
	 */
	@Override
	public PageComponent createPageComponent() {
		return createEditor();
	}

	public ApplicationEventMulticaster getApplicationEventMulticaster() {
		if (getApplicationContext() != null) {
			final String beanName = AbstractApplicationContext.APPLICATION_EVENT_MULTICASTER_BEAN_NAME;
			if (getApplicationContext().containsBean(beanName)) {
				return (ApplicationEventMulticaster) getApplicationContext().getBean(beanName);
			}
		}
		return null;
	}

	/**
	 * @return Returns the editorClass.
	 */
	public Class getEditorClass() {
		return editorClass;
	}

	public Map getEditorProperties() {
		return editorProperties;
	}

	@Override
	public String getId() {
		return id;
	}

	/**
	 * The bean name is the default id
	 */
	@Override
	public void setBeanName(String beanName) {
		setId(beanName);
	}

	public void setEditorClass(Class editorClass) {
		this.editorClass = editorClass;
	}

	public void setEditorProperties(Map editorProperties) {
		this.editorProperties = editorProperties;
	}

	public void setId(String id) {
		this.id = id;
	}
}
