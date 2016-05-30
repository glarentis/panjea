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

import it.eurotn.panjea.rich.IEditorListener;
import it.eurotn.panjea.rich.PanjeaLifecycleApplicationEvent;
import it.eurotn.panjea.rich.pages.PanjeaDockingViewDescriptor;
import it.eurotn.rich.editors.AbstractEditorDialogPage;
import it.eurotn.rich.report.ReportManager;
import it.eurotn.rich.services.TableLayoutCache;

import java.awt.Component;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.SwingConstants;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.PageComponent;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.application.support.AbstractView;
import org.springframework.richclient.dialog.DialogPage;
import org.springframework.richclient.security.ClientSecurityEvent;
import org.springframework.richclient.security.LogoutEvent;
import org.springframework.richclient.settings.Settings;
import org.springframework.richclient.settings.SettingsException;
import org.springframework.richclient.settings.SettingsManager;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.document.DocumentPane;
import com.jidesoft.document.IDocumentGroup;
import com.jidesoft.document.PopupMenuCustomizer;
import com.jidesoft.spring.richclient.docking.JideApplicationPage;
import com.jidesoft.spring.richclient.docking.JideApplicationWindow;
import com.jidesoft.spring.richclient.docking.editor.workspace.DefaultCustomizer;
import com.jidesoft.spring.richclient.docking.editor.workspace.DoubleClickListener;
import com.jidesoft.spring.richclient.docking.editor.workspace.PanjeaEditorActivator;
import com.jidesoft.spring.richclient.docking.editor.workspace.ScrollMouseOnTabsMouseWheelListener;
import com.jidesoft.swing.JideTabbedPane;
import com.jidesoft.swing.StringConverter;

/**
 * Encapsulates the Jide mechanism for opening editors in their so called workspace. Basically adapts the Jide workspace
 * concept (ie document pane and DocumentComponent) to the Spring RCP architecture.
 *
 * @author Jonny Wray
 *
 */
public class WorkspaceView extends AbstractView implements ApplicationListener, InitializingBean {

	private class PanjeaDocumentPane extends DocumentPane {

		private static final long serialVersionUID = 1L;

		@Override
		protected IDocumentGroup createDocumentGroup() {
			IDocumentGroup group = super.createDocumentGroup();
			if (group instanceof JideTabbedPane) {
				JideTabbedPane tabbedPane = (JideTabbedPane) group;
				tabbedPane.addMouseListener(tabDoubleClickListener);
				tabbedPane.addMouseWheelListener(new ScrollMouseOnTabsMouseWheelListener(WorkspaceView.this));
			}
			return group;
		}
	}

	private static Logger logger = Logger.getLogger(WorkspaceView.class);
	protected boolean isCtrlPressed = false;
	private static final String PROFILE_KEY = "WorkspaceView";
	private final List<IEditorListener> editorsListener = new ArrayList<IEditorListener>();
	private MouseListener tabDoubleClickListener = null;
	protected DocumentPane contentPane;
	private DropTargetListener dropTargetListener;
	private boolean groupsAllowed = true;
	private boolean heavyweightComponentEnabled = false;
	private boolean reorderAllowed = true;
	private boolean showContextMenu = true;
	private int maxGroupCount = 0;
	private int tabPlacement = SwingConstants.TOP;
	private StringConverter titleConverter = null;
	private boolean updateTitle = true;
	private PanjeaEditorActivator editorActivator;
	private EditorCache editorCache;

	private DocumentPane.TabbedPaneCustomizer tabbedPaneCustomizer;

	private SettingsManager settingsManager;

	/**
	 * Costruttore di default.
	 */
	public WorkspaceView() {
		editorCache = new EditorCache();
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {

			@Override
			public boolean dispatchKeyEvent(KeyEvent e) {
				isCtrlPressed = e.isControlDown();
				return false;
			}
		});

	}

	/**
	 * Adds a document to the editor workspace. The behaviour when an editor is already open, with editor identity
	 * defined by id property, is determined by the closeAndReopenEditor property. If this property is true, the
	 * default, the editor is closed and reopened. If false, the existing editor becomes the active one.
	 *
	 * @param pageComponent
	 *            pageComponent (AbstractEditor)
	 */
	public PageComponent addDocumentComponent(final PageComponent pageComponent, Object callerEditorInput) {
		logger.debug("--> Enter addDocumentComponent");
		String id = pageComponent.getId();
		PageComponent result = null;
		if (contentPane.getDocument(id) != null && !isCtrlPressed) {
			// se non chiudo l'editor il pageComponent preparato con il nuovo
			// oggetto mi serve solo
			// per usare il nuovo oggetto e settarlo quindi alla pageComponent
			// esistente nel documentPane
			// oggetto del nuovo editor
			Object editorInput = ((AbstractEditor) pageComponent).getEditorInput();
			// editor esistente
			PanjeaDocumentComponent panjeaDocument = (PanjeaDocumentComponent) contentPane.getDocument(id);

			// aumento il contatore nella cache
			// anche se l'editor è già aperto.
			editorCache.getDocument(id);

			// devo chiedere se posso cambiare l'oggetto nell'editor già aperto
			final AbstractEditor editor = (AbstractEditor) panjeaDocument.getPageComponent();
			boolean canClose = editor.canClose();
			if (canClose) {
				// setto il nuovo oggetto nel vecchio editor, viene chiamato il
				// metodo abstract initialize
				// (AbstractEditor)
				// implementato nella AbstractEditorDialogPage.
				((AbstractEditor) panjeaDocument.getPageComponent()).setEditorInput(editorInput);
			}
			// rendo l'editor attivo e quindi lo porto in primo piano
			// controllando che ci sia ancora ( potrebbe essere
			// stato chiuso)
			if (contentPane.getDocument(id) != null) {
				contentPane.setActiveDocument(id);
			}
			result = panjeaDocument.getPageComponent();
		} else {
			PanjeaDocumentComponent document = null;
			if (editorCache.containDocument(pageComponent.getId()) && !isCtrlPressed) {
				document = editorCache.getDocument(pageComponent.getId());
			} else {
				document = constructDocumentComponent(pageComponent);
				document.enableCache(!isCtrlPressed);
			}

			// devo aggiungere editors e pagine in ascolto degli eventi
			// dell'applicazione
			// non solo quando creo l'editor, ma anche quando richiamo l'editor
			// dalla cache
			// (viene deregistrato alla chiusura dello stesso)
			document.addDocumentComponentListener(editorActivator);
			if (pageComponent instanceof IEditorListener) {
				editorsListener.add((IEditorListener) document.getPageComponent());
			}
			if (pageComponent instanceof AbstractEditorDialogPage) {
				AbstractEditorDialogPage editorDialogPage = (AbstractEditorDialogPage) document.getPageComponent();

				for (DialogPage page : editorDialogPage.getDialogPages()) {
					if (page instanceof IEditorListener) {
						editorsListener.add((IEditorListener) page);
					}
				}
			}

			((AbstractEditor) document.getPageComponent()).setEditorInput(((AbstractEditor) pageComponent)
					.getEditorInput());
			if (contentPane.getDocument(document.getName()) == null) {
				contentPane.openDocument(document);
			}
			if (contentPane.getDocument(document.getName()) != null) {
				contentPane.setActiveDocument(document.getName());
			}
			result = document.getPageComponent();
		}

		if (contentPane.getActiveDocument() != null) {
			final PanjeaDocumentComponent doc = (PanjeaDocumentComponent) contentPane.getActiveDocument();
			contentPane.getActiveDocument().setTitle(((AbstractEditor) doc.getPageComponent()).getDisplayName());

			if (doc.getPageComponent() instanceof AbstractEditorDialogPage) {
				final AbstractEditorDialogPage page = (AbstractEditorDialogPage) doc.getPageComponent();
				page.addCurrentObjectChangeListeners(new PropertyChangeListener() {

					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						doc.setTitle(page.getDisplayName());
					}
				});
				page.addDisplayNameChangeListeners(new PropertyChangeListener() {

					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						doc.setTitle(evt.getNewValue().toString());
					}
				});
				// Se l'editor è una composite Page e ho aperto l'editor con la
				// richiesta della pagina mi posiziono
				// sulla
				// pagina.
				if (callerEditorInput != null && callerEditorInput instanceof String) {
					String[] token = ((String) callerEditorInput).split("#");
					if (token.length == 2) {
						page.getCompositeDialogPage().showPage(token[1]);
					}
				}
			}
		}
		return result;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		tabDoubleClickListener = new DoubleClickListener(
				((JideApplicationWindow) getActiveWindow()).getDockingManager());
		tabbedPaneCustomizer = new DefaultCustomizer();
		editorActivator = new PanjeaEditorActivator();
		editorActivator.setEditorsListener(editorsListener);
		editorActivator.setSettings(getSettings());
		editorActivator.setWorkspaceView(this);
	}

	/**
	 * Svuota la cache di editors e reports.
	 */
	public void clearCache() {
		// pulisco la cache degli editors
		editorCache.clear();

		// pulisco la cache dei reports
		ReportManager reportManager = RcpSupport.getBean(ReportManager.BEAN_ID);
		reportManager.clearReportsNameCache();

		TableLayoutCache tableLayoutCache = (TableLayoutCache) Application.services()
				.getService(TableLayoutCache.class);
		tableLayoutCache.clearCache();
	}

	/**
	 * Chiude tutti gli editor aperti.
	 */
	public void closeAllEditors() {
		DocumentPane documentPane = (DocumentPane) createControl();
		documentPane.closeAll();
	}

	/**
	 *
	 * @param pageComponent
	 *            pagina da utilizzare per costruire il document component
	 * @return PanjeaDocumentPane document Pane con il pageComponent associato
	 */
	protected PanjeaDocumentComponent constructDocumentComponent(final PageComponent pageComponent) {
		logger.debug("--> Enter constructDocumentComponent");
		String id = pageComponent.getId();
		if (isCtrlPressed) {
			id = id + Calendar.getInstance().getTimeInMillis();
		}

		// JComponent component =
		// pageComponent.getContext().getPane().getControl();
		// component.setBorder(BorderFactory.createEmptyBorder());
		PanjeaDocumentComponent document = new PanjeaDocumentComponent(id, pageComponent);
		registerDropTargetListeners(pageComponent.getControl());

		logger.debug("--> Exit constructDocumentComponent");
		return document;
	}

	/**
	 *
	 * @return DocumentPage, controllo principale del workspaceView.
	 */
	private DocumentPane constructDocumentPane() {
		DocumentPane documentPane = new PanjeaDocumentPane();
		documentPane.setHeavyweightComponentEnabled(heavyweightComponentEnabled);
		documentPane.setTabbedPaneCustomizer(tabbedPaneCustomizer);
		documentPane.setReorderAllowed(reorderAllowed);
		documentPane.setShowContextMenu(showContextMenu);
		documentPane.setTabPlacement(tabPlacement);
		documentPane.setUpdateTitle(updateTitle);
		documentPane.setGroupsAllowed(groupsAllowed);
		documentPane.setMaximumGroupCount(maxGroupCount);
		documentPane.setBorder(null);
		if (titleConverter != null) {
			documentPane.setTitleConverter(titleConverter);
		}
		PopupMenuCustomizer popupMenuCustomizer = new PanjeaPopupMenuCustomizer(editorCache);
		documentPane.setPopupMenuCustomizer(popupMenuCustomizer);
		return documentPane;
	}

	@Override
	protected JComponent createControl() {
		if (contentPane == null) {
			contentPane = constructDocumentPane();
			registerDropTargetListeners(contentPane);
			contentPane.getLayoutPersistence().setProfileKey(PROFILE_KEY);
		}
		return contentPane;
	}

	/**
	 * This is a bit of a hack to get over a limitation in the JIDE docking framework. When focus is regained to the
	 * workspace by activation the currently activated document the documentComponentActivated is not fired. This needs
	 * to be fired when we know the workspace has become active. For some reason it dosen't work when using the
	 * componentFocusGained method
	 */
	public void fireFocusGainedOnActiveComponent() {
		logger.debug("-->ENTER fireFocusGainedOnActiveComponent");
		PanjeaDocumentComponent document = (PanjeaDocumentComponent) contentPane.getActiveDocument();
		if (document != null) {
			JideApplicationPage page = (JideApplicationPage) getActiveWindow().getPage();
			page.fireFocusGained(document.getPageComponent());
		}
		logger.debug("-->EXIT fireFocusGainedOnActiveComponent");
	}

	/**
	 * @return Returns the editorCache.
	 */
	public EditorCache getEditorCache() {
		return editorCache;
	}

	/**
	 * Restituisce il numero di editor aperti.
	 *
	 * @return numero di editor aperti
	 */
	public int getEditorCount() {
		DocumentPane documentPane = (DocumentPane) createControl();
		return documentPane.getDocumentCount();
	}

	/**
	 * Restituisce un array di {@link PageComponent} che stanno attualmente gestendo l'oggetto della classe richiesta.
	 *
	 * @param objectClass
	 *            classe dell'oggetto
	 * @return array di {@link PageComponent} trovati
	 */
	public PageComponent[] getPageComponent(Class<?> objectClass) {

		List<PageComponent> pages = new ArrayList<PageComponent>();

		for (int i = 0; i < contentPane.getDocumentCount(); i++) {
			PanjeaDocumentComponent document = (PanjeaDocumentComponent) contentPane.getDocumentAt(i);
			PageComponent page = document.getPageComponent();
			if (page instanceof AbstractEditor && ((AbstractEditor) page).getEditorInput() != null
					&& ((AbstractEditor) page).getEditorInput().getClass().equals(objectClass)) {
				pages.add(page);
			}
		}
		return pages.toArray(new PageComponent[pages.size()]);
	}

	/**
	 * @return settings locali
	 */
	public Settings getSettings() {
		try {
			org.springframework.util.Assert.notNull(getSettingsManager(),
					"Settings manager deve essere iniettato da xml nel panjea-pages-context.xml");
			Settings s = getSettingsManager().getUserSettings();
			org.springframework.util.Assert.notNull(s, "UserSettings è null");
			return s;
		} catch (SettingsException e) {
			logger.error("--> Errore nell'accesso ai settings", e);
		}
		return null;
	}

	/**
	 * @return manager per il settings
	 */
	public SettingsManager getSettingsManager() {
		return settingsManager;
	}

	/**
	 * passa al prossimo editor.
	 */
	public void nextEditor() {
		DocumentPane documentPane = (DocumentPane) createControl();
		documentPane.nextDocument();
	}

	@Override
	public void onApplicationEvent(ApplicationEvent applicationEvent) {
		if (applicationEvent instanceof ClientSecurityEvent) {
			logger.debug("---> SecurityEvent catched");
			if (applicationEvent instanceof LogoutEvent) {
				closeAllEditors();
				clearCache();
			}
		}

		if (applicationEvent instanceof PanjeaLifecycleApplicationEvent) {
			List<String> purgeEvents = Arrays.asList(PanjeaLifecycleApplicationEvent.CLOSE_APP,
					PanjeaLifecycleApplicationEvent.NEXT_EDITOR, PanjeaLifecycleApplicationEvent.PREV_EDITOR,
					PanjeaLifecycleApplicationEvent.SELECTED);
			PanjeaLifecycleApplicationEvent plae = ((PanjeaLifecycleApplicationEvent) applicationEvent);
			String eventType = plae.getEventType();

			// Controllo che la finestra sia effettivamente il workSpace
			// configurato.
			// Ade esempio la SearchResult è di tiop workSpace ma su xml non è
			// configurata come isWorkspace
			boolean isWS = ((PanjeaDockingViewDescriptor) getDescriptor()).isWorkspace();

			if (eventType.equals(PanjeaLifecycleApplicationEvent.CLOSE_APP)) {
				clearCache();
				closeAllEditors();
			} else if (eventType.equals(PanjeaLifecycleApplicationEvent.NEXT_EDITOR) && isWS) {
				nextEditor();
			} else if (eventType.equals(PanjeaLifecycleApplicationEvent.PREV_EDITOR) && isWS) {
				prevEditor();
			} else if (eventType.equals(LifecycleApplicationEvent.DELETED) && isWS) {
				// recupero gli editor e controllo se gestiscono l'oggetto
				// cancellato. In caso lo chiudo
				removeDocumentComponent(plae.getObject());
			} else if (eventType.equals(PanjeaLifecycleApplicationEvent.CLEAR_CACHE)) {
				// pulisco la cache degli editors
				clearCache();
			}
			if (!purgeEvents.contains(plae.getEventType())) {
				List<IEditorListener> listaEditor = new ArrayList<IEditorListener>(editorsListener);
				for (IEditorListener listener : listaEditor) {
					if (listener != null && editorsListener.contains(listener)) {
						// utilizzo la contains su editorsListener perchè se
						// rlancio una delete e chiudo un editor
						// potrebbe non essere + presente nella collection
						// editorsListener, mentre in origine lo era
						listener.onEditorEvent(plae);
					}
				}
			}
		}
	}

	/**
	 * passa all'editor successivo.
	 */
	public void prevEditor() {
		DocumentPane documentPane = (DocumentPane) createControl();
		documentPane.prevDocument();
	}

	/**
	 * Mette il documento in cache.<br/>
	 * Se la cache ha raggiunto il limite massimo e non ci sono editor aperti cancella l'editor meno utilizzato dalla
	 * cache<br/>
	 * altrimenti non inserisce l'editor in cache.
	 *
	 * @param id
	 *            id del documento
	 * @param documentComponent
	 *            documentComponent
	 */
	public void putDocumentInCache(String id, PanjeaDocumentComponent documentComponent) {
		editorCache.putDocument(id, documentComponent, contentPane.getDocumentNames());
	}

	/**
	 * @param component
	 *            the component to register
	 */
	private void registerDropTargetListeners(Component component) {
		if (dropTargetListener != null) {
			new DropTarget(component, dropTargetListener);
		}
	}

	/**
	 * rimuove l'editor contenente l'oggetto interessato.
	 *
	 * @param editorObject
	 *            oggetto rimosso
	 */
	public void removeDocumentComponent(Object editorObject) {
		String documentNameToClose = "";
		AbstractEditor editor = null;
		for (int i = 0; i < contentPane.getDocumentCount(); i++) {
			editor = (AbstractEditor) ((PanjeaDocumentComponent) contentPane.getDocumentAt(i)).getPageComponent();
			if (editor != null && editor.getEditorInput() != null && editor.getEditorInput().equals(editorObject)) {
				documentNameToClose = contentPane.getDocumentAt(i).getName();
				break;
			}
		}
		// La chiusura dell'editor fa scattare l'editorActivator che rimuove
		// dalla pageComponentMap la pagina.-
		// rimuovo dopo il ciclo altrimenti ho una concurrentException.
		if (!documentNameToClose.isEmpty()) {
			contentPane.closeDocument(documentNameToClose);
		}
	}

	/**
	 * Configure the groupsAllowed property of the document pane.
	 *
	 * @param groupsAllowed
	 *            .
	 */
	public void setGroupsAllowed(boolean groupsAllowed) {
		this.groupsAllowed = groupsAllowed;
	}

	/**
	 * Specifies if heavyweight components are enabled for the workspace. Default is false.
	 *
	 * @param heavyweightComponentEnabled
	 *            enable heavyweight components.
	 */
	public void setHeavyweightComponentEnabled(boolean heavyweightComponentEnabled) {
		this.heavyweightComponentEnabled = heavyweightComponentEnabled;
	}

	/**
	 * Configures the maximum group count for the document pane. Default is zero which implies unlimited.
	 *
	 * @param paramMaxGroupCount
	 *            .
	 */
	public void setMaximumGroupCount(int paramMaxGroupCount) {
		this.maxGroupCount = paramMaxGroupCount;
	}

	/**
	 * Specifies if reordering is allowed in the underlying document pane. If not set then default is true.
	 *
	 * @param reorderAllowed
	 *            .
	 */
	public void setReorderAllowed(boolean reorderAllowed) {
		this.reorderAllowed = reorderAllowed;
	}

	/**
	 *
	 * @param settingsManager
	 *            settings manager
	 */
	public void setSettingsManager(SettingsManager settingsManager) {
		this.settingsManager = settingsManager;
	}

	/**
	 * Specifies if the show context menu flag is set on the underlying document pane, default if not set is true.
	 *
	 * @param showContextMenu
	 *            .
	 */
	public void setShowContextMenu(boolean showContextMenu) {
		this.showContextMenu = showContextMenu;
	}

	/**
	 * Specifies a TabbedPaneCustomizer that allows the appearence of the tabbed pane used in the workspace to be
	 * customized. Note, the method call setRequestFocusEnabled(true); should be made to ensure correct JIDE to Spring
	 * RCP event translation. If not customizer is specified then this requestFocusEnabled is set to true by default.
	 *
	 * @param tabbedPaneCustomizer
	 *            the specific customizer
	 */
	public void setTabbedPaneCustomizer(DocumentPane.TabbedPaneCustomizer tabbedPaneCustomizer) {
		this.tabbedPaneCustomizer = tabbedPaneCustomizer;
	}

	/**
	 * @param paramTabDoubleClickListener
	 *            listener per il doppio click
	 */
	public void setTabDoubleClickMouseListener(MouseListener paramTabDoubleClickListener) {
		this.tabDoubleClickListener = paramTabDoubleClickListener;
	}

	/**
	 * Configures the tab placement of the underlying document pane.
	 *
	 * @param tabPlacement
	 *            .
	 */
	public void setTabPlacement(int tabPlacement) {
		this.tabPlacement = tabPlacement;
	}

	/**
	 * Configure the title converter of the document pane.
	 *
	 * @param titleConverter
	 *            .
	 */
	public void setTitleConverter(StringConverter titleConverter) {
		this.titleConverter = titleConverter;
	}

	/**
	 * Configure the updateTitle property of the document pane.
	 *
	 * @param updateTitle
	 *            .
	 */
	public void setUpdateTitle(boolean updateTitle) {
		this.updateTitle = updateTitle;
	}
}
