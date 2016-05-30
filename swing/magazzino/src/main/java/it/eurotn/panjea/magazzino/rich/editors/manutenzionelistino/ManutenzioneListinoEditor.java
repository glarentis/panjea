package it.eurotn.panjea.magazzino.rich.editors.manutenzionelistino;

import it.eurotn.panjea.rich.editors.DockedEditor;

import org.springframework.richclient.dialog.DialogPage;

public class ManutenzioneListinoEditor extends DockedEditor {
	@Override
	protected void initCompositeDialogPage() {
		super.initCompositeDialogPage();
		ParametriRicercaManutenzioneListinoPage parametriRicercaManutenzioneListinoPage = null;
		RisultatiRicercaManutenzioneListinoTablePage risultatiRicercaManutenzioneListinoTablePage = null;

		for (DialogPage page : getDialogPages()) {
			if (page.getId().equals(ParametriRicercaManutenzioneListinoPage.PAGE_ID)) {
				parametriRicercaManutenzioneListinoPage = (ParametriRicercaManutenzioneListinoPage) page;
			}
			if (page.getId().equals(RisultatiRicercaManutenzioneListinoTablePage.PAGE_ID)) {
				risultatiRicercaManutenzioneListinoTablePage = (RisultatiRicercaManutenzioneListinoTablePage) page;
			}
		}
		parametriRicercaManutenzioneListinoPage
				.setParametriManutezioneForm(risultatiRicercaManutenzioneListinoTablePage
						.getParametriAggiornaManutenzioneListinoForm());
	}
}
