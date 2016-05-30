package it.eurotn.panjea.ordini.rich.editors.evasione;

import it.eurotn.panjea.ordini.rich.editors.evasione.carrello.CarrelloEvasioneOrdiniTablePage;
import it.eurotn.panjea.rich.editors.RicercaEditor;
import it.eurotn.rich.dialog.JecCompositeDialogPage;

import org.springframework.richclient.dialog.DialogPage;

public class EvasioneOridiniEditor extends RicercaEditor {

	public class EvasioneOrdiniDockingCompositeDialogPage extends RicercaDockingCompositeDialogPage {

		private RisultatiRicercaEvasioneTablePage risultatiRicercaEvasioneTablePage;
		private CarrelloEvasioneOrdiniTablePage carrelloEvasioneOrdiniTablePage;

		/**
		 * Costruttore.
		 *
		 * @param idPage
		 *            id della pagina
		 */
		public EvasioneOrdiniDockingCompositeDialogPage(final String idPage) {
			super(idPage);
		}

		@Override
		protected void addFrames() {
			super.addFrames();
			new RisultatiRicercaEvasioneController(risultatiRicercaEvasioneTablePage, carrelloEvasioneOrdiniTablePage);
		}

		@Override
		public void addPage(DialogPage page) {
			super.addPage(page);

			if (page.getId().equals(RisultatiRicercaEvasioneTablePage.PAGE_ID)) {
				risultatiRicercaEvasioneTablePage = (RisultatiRicercaEvasioneTablePage) page;
			} else if (page.getId().equals(CarrelloEvasioneOrdiniTablePage.PAGE_ID)) {
				carrelloEvasioneOrdiniTablePage = (CarrelloEvasioneOrdiniTablePage) page;
			}
		}

	}

	private EvasioneOrdiniDockingCompositeDialogPage evasioneOrdiniDockingCompositeDialogPage;

	@Override
	protected JecCompositeDialogPage createCompositeDialogPage() {
		if (evasioneOrdiniDockingCompositeDialogPage == null) {
			evasioneOrdiniDockingCompositeDialogPage = new EvasioneOrdiniDockingCompositeDialogPage(getDialogPageId());
		}
		return evasioneOrdiniDockingCompositeDialogPage;
	}

}
