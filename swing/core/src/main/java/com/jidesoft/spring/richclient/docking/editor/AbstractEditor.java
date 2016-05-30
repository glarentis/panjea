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

import it.eurotn.locking.IDefProperty;
import it.eurotn.panjea.rich.converter.StringConverter;

import java.awt.Image;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JMenuBar;
import javax.swing.JToolBar;
import javax.swing.ProgressMonitor;

import org.springframework.richclient.application.Editor;
import org.springframework.richclient.application.PageComponentContext;
import org.springframework.richclient.application.PageComponentDescriptor;
import org.springframework.richclient.application.config.ApplicationLifecycleAdvisor;
import org.springframework.richclient.application.support.ApplicationServicesAccessor;
import org.springframework.richclient.command.CommandGroup;
import org.springframework.util.Assert;

import com.jidesoft.converter.DefaultObjectConverter;
import com.jidesoft.converter.ObjectConverter;
import com.jidesoft.converter.ObjectConverterManager;
import com.jidesoft.spring.richclient.docking.JideApplicationLifecycleAdvisor;

/**
 * Abstract base class for an editor. Provides ability to specify editor specific toolbar and menubar as well as
 * providing convience methods for getting values from the descriptor, registering property change listeners. Specifies
 * a template method that should be overridden to allow registration of editor specific command executors, and
 * preregisters a save and a saveAs executor that simply call the relevant editor methods
 * 
 * @author Jonny Wray
 */
public abstract class AbstractEditor extends ApplicationServicesAccessor implements Editor {

	private static final String TOOLBAR_SUFFIX = ".editorToolBar";

	private static final String MENU_SUFFIX = ".editorMenuBar";

	private String toolBarCommandGroupName = null;

	private String menuBarCommandGroupName = null;

	private PageComponentDescriptor descriptor;

	private PageComponentContext context;

	private Object editorObject;

	private boolean enableCache;

	private List<Class<?>> titleSkippedObjectConverter;

	{
		titleSkippedObjectConverter = new ArrayList<Class<?>>();
		titleSkippedObjectConverter.add(DefaultObjectConverter.class);
		titleSkippedObjectConverter.add(StringConverter.class);
	}

	/**
	 * Costruttore.
	 */
	public AbstractEditor() {
		enableCache = true;
	}

	@Override
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		descriptor.addPropertyChangeListener(listener);
	}

	@Override
	public void addPropertyChangeListener(String name, PropertyChangeListener listener) {
		descriptor.addPropertyChangeListener(name, listener);
	}

	@Override
	public boolean canClose() {
		return true;
	}

	@Override
	public void close() {
	}

	@Override
	public void componentClosed() {

	}

	@Override
	public void componentFocusGained() {

	}

	@Override
	public void componentFocusLost() {

	}

	@Override
	public void componentOpened() {

	}

	/**
	 * This method is called when an editor is removed from the workspace. Concrete subclasses can, and should, override
	 * the method and release any resources. Note that the super class method must be called or a memory leak will
	 * result
	 */
	@Override
	public void dispose() {
	}

	/**
	 * The caption is used by the status bar as an opened message.
	 * 
	 * @return The caption, if not overridden comes from the descriptor
	 */
	@Override
	public String getCaption() {
		return descriptor.getCaption();
	}

	/**
	 * @param name
	 *            name of the command group to get
	 * @return CommandGroup
	 */
	public CommandGroup getCommandGroup(String name) {
		ApplicationLifecycleAdvisor advisor = getApplication().getLifecycleAdvisor();
		if (advisor instanceof JideApplicationLifecycleAdvisor) {
			JideApplicationLifecycleAdvisor dockingAdvisor = (JideApplicationLifecycleAdvisor) advisor;
			CommandGroup commandGroup = dockingAdvisor.getSpecificCommandGroup(name);
			return commandGroup;
		}
		return null;
	}

	@Override
	public PageComponentContext getContext() {
		return context;
	}

	/**
	 * Implement to provide editor specific control.
	 * 
	 * @return The component that is the central control in the editor. It has toolbar and menubar added to it by the
	 *         framework if specified
	 */
	@Override
	public abstract JComponent getControl();

	/**
	 * The description is used by the editor tab tooltip.
	 * 
	 * @return The description, if not overridden comes from the descriptor
	 */
	@Override
	public String getDescription() {
		return descriptor.getDescription();
	}

	/**
	 * @return PageComponentDescriptor
	 */
	public PageComponentDescriptor getDescriptor() {
		return descriptor;
	}

	/**
	 * The display name is used as the editor tab title.
	 * 
	 * @return The display name, if not overridden comes from the descriptor
	 */
	@Override
	public String getDisplayName() {
		if (getEditorInput() == null
				|| (getEditorInput() instanceof IDefProperty && ((IDefProperty) getEditorInput()).isNew())) {
			return descriptor.getDisplayName();
		}
		ObjectConverter converter = ObjectConverterManager.getConverter(getEditorInput().getClass());
		if (converter != null && !titleSkippedObjectConverter.contains(converter.getClass())
				&& converter.supportToString(getEditorInput(), null)) {
			return converter.toString(getEditorInput(), null);
		}
		return descriptor.getDisplayName();
	}

	@Override
	public Object getEditorInput() {
		return editorObject;
	}

	/**
	 * Returns the view specific menu bar constructed from the command group given by the menuBarCommandGroupName or its
	 * default.
	 * 
	 * @return menuBar
	 */
	public JMenuBar getEditorMenuBar() {
		CommandGroup commandGroup = getCommandGroup(getMenuBarCommandGroupName());
		if (commandGroup == null) {
			return null;
		}
		// commandGroup.setCommandContext(this);
		return commandGroup.createMenuBar();
	}

	/**
	 * Returns the view specific menu bar constructed from the command group given by the toolBarCommandGroupName or its
	 * default.
	 * 
	 * @return tool bar
	 */
	public JToolBar getEditorToolBar() {
		CommandGroup commandGroup = getCommandGroup(getToolBarCommandGroupName());
		if (commandGroup == null) {
			return null;
		}
		// commandGroup.setCommandContext(this);
		return (JToolBar) commandGroup.createToolBar();
	}

	/**
	 * Default comes from the descriptor.
	 * 
	 * @return the icon used in the editor document tab
	 */
	@Override
	public Icon getIcon() {
		return descriptor.getIcon();
	}

	/**
	 * Implement to provide editor specific id. As one editor descriptor can give rise to multiple editors this needs to
	 * be unique for each editor instance.
	 * 
	 * @return The identifing string of the editor instance. Each editor in the collection displayed is unique.
	 */
	@Override
	public abstract String getId();

	/**
	 * Default comes from the descriptor.
	 * 
	 * @return Image
	 */
	@Override
	public Image getImage() {
		return descriptor.getImage();
	}

	/**
	 * @return menuBarCommandGroupName
	 */
	public String getMenuBarCommandGroupName() {
		if (menuBarCommandGroupName == null) {
			return descriptor.getId() + MENU_SUFFIX;
		}
		return menuBarCommandGroupName;
	}

	/**
	 * @return toolBarCommandGroupName
	 */
	public String getToolBarCommandGroupName() {
		if (toolBarCommandGroupName == null) {
			return descriptor.getId() + TOOLBAR_SUFFIX;
		}
		return toolBarCommandGroupName;
	}

	/**
	 * This should be implemented to do the actual initization code with the specific editor object and is called just
	 * after construction.
	 * 
	 * @param editorObject
	 *            editorObject
	 */
	public abstract void initialize(Object editorObject);

	/**
	 * @return Default is true which means editors can always be saved
	 */
	@Override
	public boolean isDirty() {
		return true;
	}

	/**
	 * @return Returns the enableCache.
	 */
	public boolean isEnableCache() {
		return enableCache;
	}

	/**
	 * @return Default is true
	 */
	@Override
	public boolean isSaveAsSupported() {
		return true;
	}

	/**
	 * @return Default is false
	 */
	@Override
	public boolean isSaveOnCloseRecommended() {
		return false;
	}

	/**
	 * Registers some editor specific command executors for save and save as that just call the relevant methods within
	 * the editor.
	 * 
	 * @param paramContext
	 *            PageComponentContext
	 */
	private void registerCommandExecutors(PageComponentContext paramContext) {
	}

	/**
	 * Template method called once when this editor is initialized; allows subclasses to register local executors for
	 * shared commands with the view context.
	 * 
	 * @param paramContext
	 *            the view context
	 */
	protected void registerLocalCommandExecutors(PageComponentContext paramContext) {

	}

	@Override
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		descriptor.removePropertyChangeListener(listener);
	}

	@Override
	public void removePropertyChangeListener(String name, PropertyChangeListener listener) {
		descriptor.removePropertyChangeListener(name, listener);
	}

	/**
	 * Null implementation does not do anything but the method is registered to the local save executor and
	 * GlobalCommandIds.SAVE.
	 */
	@Override
	public void save() {

	}

	/**
	 * Null implementation does not do anything and is not registered to an executor by default.
	 * 
	 * @param arg0
	 *            ProgressMonitor
	 */
	public void save(ProgressMonitor arg0) {

	}

	/**
	 * Null implementation does not do anything but the method is registered to the local save executor and
	 * GlobalCommandIds.SAVE_AS.
	 */
	@Override
	public void saveAs() {

	}

	@Override
	public final void setContext(PageComponentContext context) {
		Assert.notNull(context, "This editors's page component context is required");
		Assert.state(this.context == null, "An editor's context may only be set once");
		this.context = context;
		registerCommandExecutors(context);
		registerLocalCommandExecutors(context);
	}

	@Override
	public void setDescriptor(PageComponentDescriptor descriptor) {
		Assert.notNull(descriptor, "The editor descriptor is required");
		Assert.state(this.descriptor == null, "An editor's descriptor may only be set once");
		this.descriptor = descriptor;
	}

	@Override
	public void setEditorInput(Object paramEditorObject) {
		this.editorObject = paramEditorObject;
		initialize(editorObject);
	}

	/**
	 * @param enableCache
	 *            The enableCache to set.
	 */
	public void setEnableCache(boolean enableCache) {
		this.enableCache = enableCache;
	}

	/**
	 * Injects a user defined name for the menubar command group. If not definied then the default is constructed from
	 * descriptor.getId() + ".editorMenuBar". Note the descriptor id is used as this is constant for one editor
	 * prototype whereas the actual editor id will change with the instance and so cannot be used here.
	 * 
	 * @param menuBarCommandGroupName
	 *            menuBarCommandGroupName
	 */
	public void setMenuBarCommandGroupName(String menuBarCommandGroupName) {
		this.menuBarCommandGroupName = menuBarCommandGroupName;
	}

	/*
	 * The code below supports declarative editor specific commands that can be turned into toolbars and/or menubars and
	 * added to the editor window.
	 * 
	 * TODO: This shares a lot of code with the same concept for views. Either move to a common super class (eg
	 * AbstractPageComponent) or to a helper class to extend by composition.
	 */
	/**
	 * Injects a user defined name for the toolbar command group. If not definied then the default is constructed from
	 * descriptor.getId() + ".editorToolBar". Note the descriptor id is used as this is constant for one editor
	 * prototype whereas the actual editor id will change with the instance and so cannot be used here.
	 * 
	 * @param toolBarCommandGroupName
	 *            toolBarCommandGroupName
	 */
	public void setToolBarCommandGroupName(String toolBarCommandGroupName) {
		this.toolBarCommandGroupName = toolBarCommandGroupName;
	}
}
