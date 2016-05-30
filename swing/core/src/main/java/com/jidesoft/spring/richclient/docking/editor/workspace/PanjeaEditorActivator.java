package com.jidesoft.spring.richclient.docking.editor.workspace;

import it.eurotn.panjea.rich.IEditorListener;
import it.eurotn.panjea.rich.PanjeaLifecycleApplicationEvent;
import it.eurotn.rich.editors.AbstractEditorDialogPage;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.PageComponent;
import org.springframework.richclient.dialog.DialogPage;
import org.springframework.richclient.settings.Settings;
import org.springframework.richclient.settings.support.Memento;

import com.jidesoft.document.DocumentComponentAdapter;
import com.jidesoft.document.DocumentComponentEvent;
import com.jidesoft.spring.richclient.docking.JideApplicationPage;
import com.jidesoft.spring.richclient.docking.editor.AbstractEditor;
import com.jidesoft.spring.richclient.docking.editor.PanjeaDocumentComponent;
import com.jidesoft.spring.richclient.docking.editor.WorkspaceView;

/**
 * Listener class that ensures Spring RCP focus events and page component lifestyle events are called from the JIDE
 * document events.
 *
 * @author Jonny Wray
 */
public class PanjeaEditorActivator extends DocumentComponentAdapter {

	private static Logger logger = Logger.getLogger(PanjeaEditorActivator.class);
	private WorkspaceView workspaceView = null;
	private Settings settings = null;
	private List<IEditorListener> editorsListener = null;

	/**
	 *
	 * Costruttore.
	 *
	 */
	public PanjeaEditorActivator() {
		super();
	}

	@Override
	public void documentComponentActivated(DocumentComponentEvent event) {
		PageComponent pageComponent = ((PanjeaDocumentComponent) event.getDocumentComponent()).getPageComponent();
		if (logger.isDebugEnabled()) {
			logger.debug("Page component " + pageComponent.getId() + " activated");
		}
		pageComponent.componentFocusGained();
	}

	@Override
	public void documentComponentClosed(DocumentComponentEvent event) {
		PanjeaDocumentComponent documentComponent = (PanjeaDocumentComponent) event.getDocumentComponent();
		PageComponent pageComponent = documentComponent.getPageComponent();

		if (logger.isDebugEnabled()) {
			logger.debug("Page component " + pageComponent.getId() + " closed");
		}

		// se è nella mappa degli editor listener lo rimuovo
		if (pageComponent instanceof IEditorListener) {
			editorsListener.remove(pageComponent);
		}

		if (pageComponent instanceof AbstractEditorDialogPage) {
			AbstractEditorDialogPage editorDialogPage = (AbstractEditorDialogPage) pageComponent;
			for (DialogPage page : editorDialogPage.getDialogPages()) {
				if (page instanceof IEditorListener) {
					editorsListener.remove(page);
				}
			}
		}

		((JideApplicationPage) workspaceView.getContext().getPage()).fireClosed(pageComponent);
		event.getDocumentComponent().removeDocumentComponentListener(this);

		PanjeaLifecycleApplicationEvent panjeaEvt = new PanjeaLifecycleApplicationEvent(
				PanjeaLifecycleApplicationEvent.CLOSED, PanjeaLifecycleApplicationEvent.CLOSED);
		Application.instance().getApplicationContext().publishEvent(panjeaEvt);

		// Se posso inserire il componente in cache lo faccio senza chiamare la
		// dispose.
		// Visto che lo sinserisco in una mappa posso permettermi di inserirlo
		// sempre
		if (documentComponent.isCacheable()) { // &&
			// !workspaceView.getEditorCache().containDocument(pageComponent.getId())
			workspaceView.putDocumentInCache(pageComponent.getId(), documentComponent);
		} else {
			pageComponent.getControl().removeAll();
			pageComponent.dispose();
			pageComponent = null;
		}
	}

	@Override
	public void documentComponentClosing(DocumentComponentEvent event) {
		PageComponent pageComponent = ((PanjeaDocumentComponent) event.getDocumentComponent()).getPageComponent();
		if (logger.isDebugEnabled()) {
			logger.debug("Page component " + pageComponent.getId() + " closing");
		}

		final AbstractEditor editor = (AbstractEditor) pageComponent;

		boolean canClose = editor.canClose();
		event.getDocumentComponent().setAllowClosing(canClose);

		// se posso chiudere infine il nostro pageComponent verifico se
		// implementa Memento. in caso affermativo chiamo il metodo saveState()
		if (pageComponent instanceof Memento) {
			logger.debug("--> Chiusura pageComponent nel workspaceview, salvo lo stato del memento "
					+ pageComponent.getId());
			Memento memento = (Memento) pageComponent;
			memento.saveState(settings);

			try {
				settings.save();
			} catch (Exception e) {
				logger.error("--> Errore nel salvare le informazioni per la pagina " + pageComponent.getId(), e);
			}
		}
		((JideApplicationPage) workspaceView.getContext().getPage()).fireFocusLost(pageComponent);
	}

	@Override
	public void documentComponentDeactivated(DocumentComponentEvent event) {
		PageComponent pageComponent = ((PanjeaDocumentComponent) event.getDocumentComponent()).getPageComponent();
		if (logger.isDebugEnabled()) {
			logger.debug("Page component " + pageComponent.getId() + " deactivated");
		}
		((JideApplicationPage) workspaceView.getContext().getPage()).fireFocusLost(pageComponent);
	}

	@Override
	public void documentComponentOpened(DocumentComponentEvent event) {
		PageComponent pageComponent = ((PanjeaDocumentComponent) event.getDocumentComponent()).getPageComponent();
		if (logger.isDebugEnabled()) {
			logger.debug("Page component " + pageComponent.getId() + " opened");
		}
		// all'apertura del nostro pageComponent verifico se implementa memento
		// in caso affermativo chiamo il metodo restoreState(),
		// se invece di essere un abstractSearch � un abstractEditorDialogPage,
		// per le pagine lazy iniziate
		// successivamente
		// la gestione del restoreState(Settings) viene gestita nella
		// ButtonCompositeDialogPage.
		if (pageComponent instanceof Memento) {
			Memento memento = (Memento) pageComponent;
			memento.restoreState(settings);
		}
		((JideApplicationPage) workspaceView.getContext().getPage()).fireOpened(pageComponent);
	}

	/**
	 *
	 * @param editorsListener
	 *            listener sugli eventi dell'editor
	 */
	public void setEditorsListener(List<IEditorListener> editorsListener) {
		this.editorsListener = editorsListener;
	}

	/**
	 *
	 * @param settings
	 *            settings per salvare lo stato dell'editor alla chiusura
	 */
	public void setSettings(Settings settings) {
		this.settings = settings;
	}

	/**
	 *
	 * @param workspaceView
	 *            workspaceview
	 */
	public void setWorkspaceView(WorkspaceView workspaceView) {
		this.workspaceView = workspaceView;
	}
}