package it.eurotn.panjea.magazzino.rich.editors.fatturazione;

import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriRicercaFatturazione;
import it.eurotn.panjea.rich.editors.RicercaEditor;
import it.eurotn.rich.dialog.JecCompositeDialogPage;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.springframework.richclient.dialog.DialogPage;

public class FatturazioneEditor extends RicercaEditor {

	public class FatturazioneDockingCompositeDialogPage extends RicercaDockingCompositeDialogPage {

		/**
		 * Costruttore.
		 *
		 * @param idPage
		 *            id della pagina
		 */
		public FatturazioneDockingCompositeDialogPage(final String idPage) {
			super(idPage);
		}

		@Override
		public void addPage(DialogPage page) {
			super.addPage(page);

			if (RisultatiRicercaFatturazioneTablePage.PAGE_ID.equals(page.getId())) {
				page.addPropertyChangeListener(RisultatiRicercaFatturazioneTablePage.FATTURA_MOVIMENTI,
						fatturazioneMovimentiListener);
				risultatiRicercaFatturazioneTablePage = (RisultatiRicercaFatturazioneTablePage) page;
			} else if (ParametriRicercaFatturazionePage.PAGE_ID.equals(page.getId())) {
				parametriRicercaFatturazionePage = (ParametriRicercaFatturazionePage) page;
			}
		}

	}

	private class FatturazioneMovimentiListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent arg0) {
			FatturazioneEditor.this.canCloseEditor = !(Boolean) arg0.getNewValue();
			// se si sta fatturando le pagine diventano readonly
			parametriRicercaFatturazionePage.setReadOnly((Boolean) arg0.getNewValue());
			risultatiRicercaFatturazioneTablePage.setReadOnly((Boolean) arg0.getNewValue());

			// finita la fatturazione ripulisco la pagina dei parametri
			if (!(Boolean) arg0.getNewValue()) {
				parametriRicercaFatturazionePage.setFormObject(new ParametriRicercaFatturazione());
			}
		}

	}

	private FatturazioneDockingCompositeDialogPage fatturazioneDockingCompositeDialogPage = null;
	private ParametriRicercaFatturazionePage parametriRicercaFatturazionePage = null;
	private RisultatiRicercaFatturazioneTablePage risultatiRicercaFatturazioneTablePage = null;

	private FatturazioneMovimentiListener fatturazioneMovimentiListener = new FatturazioneMovimentiListener();

	private boolean canCloseEditor = true;

	@Override
	public boolean canClose() {
		if (canCloseEditor) {
			return super.canClose();
		} else {
			return canCloseEditor;
		}
	}

	@Override
	protected JecCompositeDialogPage createCompositeDialogPage() {
		if (fatturazioneDockingCompositeDialogPage == null) {
			fatturazioneDockingCompositeDialogPage = new FatturazioneDockingCompositeDialogPage(getDialogPageId());
		}
		return fatturazioneDockingCompositeDialogPage;
	}

}
