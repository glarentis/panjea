package com.jidesoft.spring.richclient.docking.editor;

import it.eurotn.rich.editors.AbstractEditorDialogPage;

import java.awt.event.ActionEvent;
import java.util.Locale;

import javax.swing.AbstractAction;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.apache.log4j.Logger;
import org.springframework.context.MessageSource;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.image.IconSource;

import com.jidesoft.document.IDocumentGroup;
import com.jidesoft.document.IDocumentPane;
import com.jidesoft.document.PopupMenuCustomizer;

/**
 * Customizza i menu sugli editor.
 * 
 * @author giangi
 * 
 */
public class PanjeaPopupMenuCustomizer implements PopupMenuCustomizer {

	private class LockUnLockLayoutAction extends AbstractAction {
		private static final long serialVersionUID = 1233669972403572499L;
		private AbstractEditorDialogPage editorDialogPage;

		/**
		 * 
		 * Costruttore.
		 * 
		 * @param editorDialogPage
		 *            dialogPage per la quale sbloccare il layout
		 */
		public LockUnLockLayoutAction(final AbstractEditorDialogPage editorDialogPage) {
			super();
			this.editorDialogPage = editorDialogPage;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			JCheckBoxMenuItem check = (JCheckBoxMenuItem) e.getSource();
			editorDialogPage.lockLayout(check.isSelected());
		}
	}

	private class RipristinaLayoutAction extends AbstractAction {
		private static final long serialVersionUID = 1233669972403572499L;
		private AbstractEditorDialogPage editorDialogPage;

		/**
		 * 
		 * Costruttore.
		 * 
		 * @param editorDialogPage
		 *            dialogPage per la quale ripristinare il layout
		 */
		public RipristinaLayoutAction(final AbstractEditorDialogPage editorDialogPage) {
			super();
			this.editorDialogPage = editorDialogPage;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			logger.debug("--> cancello il layout per la pagina " + editorDialogPage.getId());
			editorDialogPage.restoreLayout();
			editorCache.removeDocument(editorDialogPage.getId());
			editorDialogPage.setEnableCache(false);
			logger.debug("--> cancellato il layout   per la pagina " + editorDialogPage.getId());
		}
	}

	private static Logger logger = Logger.getLogger(PanjeaPopupMenuCustomizer.class);
	private final EditorCache editorCache;

	/**
	 * 
	 * Costruttore.
	 * 
	 * @param editorCache
	 *            cache degli editor. Utilizzata per rimuovere dalla cache l'editor durante il restore.
	 * 
	 */
	public PanjeaPopupMenuCustomizer(final EditorCache editorCache) {
		super();
		this.editorCache = editorCache;
	}

	@Override
	public void customizePopupMenu(JPopupMenu popupMenu, IDocumentPane documentPane, String arg2, IDocumentGroup arg3,
			boolean arg4) {
		// Recupero il nome del file per il popup
		PanjeaDocumentComponent documentComponent = (PanjeaDocumentComponent) documentPane.getActiveDocument();
		if (documentComponent.getPageComponent() instanceof AbstractEditorDialogPage) {
			AbstractEditorDialogPage editorDialogPage = ((AbstractEditorDialogPage) documentComponent
					.getPageComponent());
			if (editorDialogPage.isLayoutSupported()) {
				popupMenu.addSeparator();
				popupMenu.add(ripristinaLayoutItem(editorDialogPage));
				popupMenu.add(lockLayoutItem(editorDialogPage));
			}
		}
	}

	/**
	 * 
	 * @param editorDialogPage
	 *            dialogPage
	 * @return item per bloccare il layout
	 */
	private JMenuItem lockLayoutItem(AbstractEditorDialogPage editorDialogPage) {
		JMenuItem menuItem = new JCheckBoxMenuItem(new LockUnLockLayoutAction(editorDialogPage));
		menuItem.setSelected(editorDialogPage.isLockLayout());
		MessageSource messageSource = (MessageSource) Application.services().getService(MessageSource.class);
		IconSource iconSource = (IconSource) Application.services().getService(IconSource.class);
		menuItem.setText(messageSource.getMessage("bloccaLayout.label", null, Locale.getDefault()));
		menuItem.setIcon(iconSource.getIcon("bloccaLayoutCommand.icon"));
		return menuItem;
	}

	/**
	 * 
	 * @param editorDialogPage
	 *            dialogPage
	 * @return item per ripristinare il layout
	 */
	private JMenuItem ripristinaLayoutItem(AbstractEditorDialogPage editorDialogPage) {
		JMenuItem menuItem = new JMenuItem(new RipristinaLayoutAction(editorDialogPage));
		MessageSource messageSource = (MessageSource) Application.services().getService(MessageSource.class);
		IconSource iconSource = (IconSource) Application.services().getService(IconSource.class);
		menuItem.setText(messageSource.getMessage("rispristinaLayout.label", null, Locale.getDefault()));
		menuItem.setIcon(iconSource.getIcon("restoreDefaultLayoutCommand.icon"));
		return menuItem;
	}
}