package it.eurotn.panjea.cauzioni.rich.editors.situazionecauzioni;

import it.eurotn.panjea.rich.editors.RicercaEditor;
import it.eurotn.rich.dialog.JecCompositeDialogPage;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.springframework.richclient.dialog.DialogPage;

public class SituazioneCauzioniEditor extends RicercaEditor {

	public class SituazioneCauzioniCompositeDialogPage extends RicercaDockingCompositeDialogPage {

		public class MovimentazioneListener implements PropertyChangeListener {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (!movimentazioneCauzioniTablePage.isControlCreated()) {
					// i controlli potrebbero essere non ancora crati visto che
					// viene chiamato in questo property change
					return;
				}
				if (RisultatiRicercaSituazioneCauzioniTablePage.SELECTED_OBJECT_CHANGE.equals(evt.getPropertyName())) {
					movimentazioneCauzioniTablePage.setFormObject(evt.getNewValue());
					movimentazioneCauzioniTablePage.refreshData();
				}

				if (RisultatiRicercaSituazioneCauzioniTablePage.SHOW_DETAIL_CHANGE.equals(evt.getPropertyName())) {
					movimentazioneCauzioniTablePage.setVisible((Boolean) evt.getNewValue());
					movimentazioneCauzioniTablePage.refreshData();
				}
			}
		}

		private RisultatiRicercaSituazioneCauzioniTablePage risultatiRicercaSituazioneCauzioniTablePage;

		private MovimentazioneCauzioniTablePage movimentazioneCauzioniTablePage;
		private MovimentazioneListener movimentazioneListener = new MovimentazioneListener();

		/**
		 *
		 * Costruttore.
		 *
		 * @param pageId
		 *            pageId
		 */
		public SituazioneCauzioniCompositeDialogPage(final String pageId) {
			super(pageId);
		}

		@Override
		public void addPage(DialogPage page) {
			super.addPage(page);

			if (RisultatiRicercaSituazioneCauzioniTablePage.PAGE_ID.equals(page.getId())) {
				risultatiRicercaSituazioneCauzioniTablePage = (RisultatiRicercaSituazioneCauzioniTablePage) page;
				risultatiRicercaSituazioneCauzioniTablePage.addPropertyChangeListener(
						RisultatiRicercaSituazioneCauzioniTablePage.SELECTED_OBJECT_CHANGE, movimentazioneListener);
				risultatiRicercaSituazioneCauzioniTablePage.addPropertyChangeListener(
						RisultatiRicercaSituazioneCauzioniTablePage.SHOW_DETAIL_CHANGE, movimentazioneListener);
			} else {
				if (MovimentazioneCauzioniTablePage.PAGE_ID.equals(page.getId())) {
					movimentazioneCauzioniTablePage = (MovimentazioneCauzioniTablePage) page;
				}
			}
		}
	}

	private SituazioneCauzioniCompositeDialogPage situazioneCauzioniCompositeDialogPage;

	@Override
	protected JecCompositeDialogPage createCompositeDialogPage() {
		if (situazioneCauzioniCompositeDialogPage == null) {
			situazioneCauzioniCompositeDialogPage = new SituazioneCauzioniCompositeDialogPage(getDialogPageId());
		}
		return situazioneCauzioniCompositeDialogPage;
	}

}
