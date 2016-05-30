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

import java.util.*;

/**
 * A default implementation of an editor factory that can be configured with an injected map that maps a Class to an
 * editor descriptor.
 * 
 * @author Jonny Wray
 * 
 */
public class DefaultEditorRegistry implements EditorRegistry {

	private Map editorMap;

	public void setEditorMap(Map editorMap) {
		this.editorMap = editorMap;
	}

	/**
	 * Returns an EditorDescriptor keyed by the class of the injected object. If non exists null is returned.
	 */
	@Override
	public EditorDescriptor getEditorDescriptor(Object editorObject) {
		EditorDescriptor descriptor;

		if (editorObject instanceof String) {
			descriptor = (EditorDescriptor) editorMap.get(editorObject);
		} else {
			Class<? extends Object> editorClass = editorObject.getClass();
			descriptor = (EditorDescriptor) editorMap.get(editorClass);
			if (descriptor == null) {
				Iterator it = editorMap.keySet().iterator();
				while (it.hasNext()) {
					if (it.next() instanceof Class) {
						Class klass = (Class) it.next();
						if (klass.isAssignableFrom(editorClass)) {
							descriptor = (EditorDescriptor) editorMap.get(klass);
						}
					} else {
						// se non e' una class deve essere una stringa
						String editorKey = (String) it.next();
						descriptor = (EditorDescriptor) editorMap.get(editorKey);
					}
				}
			}
		}
		return descriptor;
	}

}
