package it.eurotn.panjea.magazzino.rich.editors.fatturazione.consultazione;

import it.eurotn.panjea.rich.editors.DockedEditor;
import it.eurotn.rich.dialog.DockingCompositeDialogPage;
import it.eurotn.rich.dialog.JecCompositeDialogPage;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.springframework.richclient.dialog.DialogPage;

public class FatturazioniEditor extends DockedEditor {

	public class FatturazioniDockingCompositeDialogPage extends DockingCompositeDialogPage {

		public class AnteprimaDocumentoListener implements PropertyChangeListener {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (!anteprimaDocumentoPage.isControlCreated()) {
					// i controlli potrebbero essere non ancora crati visto che
					// viene chiamato in questo property change
					return;
				}
				if (MovimentiFatturazioneTablePage.ANTEPRIMA_DOCUMENTO_PROPERTY.equals(evt.getPropertyName())) {
					anteprimaDocumentoPage.setVisible(!anteprimaDocumentoPage.isVisible());
					if (anteprimaDocumentoPage.isVisible()) {
						anteprimaDocumentoPage.update(null, movimentiFatturazioneTablePage.getTable()
								.getSelectedObject());
					}
				}
			}
		}

		private class FatturazioneMovimentiListener implements PropertyChangeListener {

			@Override
			public void propertyChange(PropertyChangeEvent arg0) {
				FatturazioniEditor.this.canCloseEditor = !(Boolean) arg0.getNewValue();
				// se si sta fatturando le pagine diventano readonly
				fatturazioniTablePage.setReadOnly((Boolean) arg0.getNewValue());
				movimentiFatturazioneTablePage.setReadOnly((Boolean) arg0.getNewValue());
			}

		}

		private FatturazioniTablePage fatturazioniTablePage = null;
		private MovimentiFatturazioneTablePage movimentiFatturazioneTablePage = null;
		private AnteprimaDocumentoPage anteprimaDocumentoPage = null;
		private AnteprimaDocumentoListener anteprimaDocumentoListener = new AnteprimaDocumentoListener();

		private FatturazioneMovimentiListener fatturazioneMovimentiListener = new FatturazioneMovimentiListener();

		/**
		 * Costruttore.
		 * 
		 * @param idPage
		 *            id della pagina
		 */
		public FatturazioniDockingCompositeDialogPage(final String idPage) {
			super(idPage);
		}

		@Override
		public void addPage(DialogPage page) {
			super.addPage(page);

			if (MovimentiFatturazioneTablePage.PAGE_ID.equals(page.getId())) {
				movimentiFatturazioneTablePage = (MovimentiFatturazioneTablePage) page;
				movimentiFatturazioneTablePage.addPropertyChangeListener(
						MovimentiFatturazioneTablePage.ANTEPRIMA_DOCUMENTO_PROPERTY, anteprimaDocumentoListener);
				movimentiFatturazioneTablePage.addPropertyChangeListener(
						MovimentiFatturazioneTablePage.MOVIMENTI_IN_FATTURAZIONE, fatturazioneMovimentiListener);
			} else {
				if (FatturazioniTablePage.PAGE_ID.equals(page.getId())) {
					fatturazioniTablePage = (FatturazioniTablePage) page;
				} else {
					if (AnteprimaDocumentoPage.PAGE_ID.equals(page.getId())) {
						anteprimaDocumentoPage = (AnteprimaDocumentoPage) page;
					}
				}
			}

			if (fatturazioniTablePage != null && movimentiFatturazioneTablePage != null) {
				fatturazioniTablePage.getTable().addSelectionObserver(movimentiFatturazioneTablePage);
			}

			if (anteprimaDocumentoPage != null && movimentiFatturazioneTablePage != null) {
				movimentiFatturazioneTablePage.getTable().addSelectionObserver(anteprimaDocumentoPage);
			}
		}

		@Override
		public void afterPropertiesSet() throws Exception {
			super.afterPropertiesSet();

		}

	}

	private FatturazioniDockingCompositeDialogPage fatturazioniDockingCompositeDialogPage = null;

	private boolean canCloseEditor = true;

	@Override
	public boolean canClose() {
		return canCloseEditor && super.canClose();
	}

	@Override
	protected JecCompositeDialogPage createCompositeDialogPage() {
		if (fatturazioniDockingCompositeDialogPage == null) {
			fatturazioniDockingCompositeDialogPage = new FatturazioniDockingCompositeDialogPage(getDialogPageId());
		}
		return fatturazioniDockingCompositeDialogPage;
	}
}
