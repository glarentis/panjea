package it.eurotn.panjea.magazzino.rich.editors.prefatturazione;

import it.eurotn.panjea.rich.editors.DockedEditor;

import org.springframework.richclient.dialog.DialogPage;

public class PreFatturazioneEditor extends DockedEditor {

	@Override
	protected void initCompositeDialogPage() {
		super.initCompositeDialogPage();
		MovimentiPreFatturazioneTablePage movimentiPreFatturazioneTablePage = null;
		MovimentiInFatturaTablePage movimentiInFatturaTablePage = null;

		for (DialogPage page : getDialogPages()) {
			if (MovimentiPreFatturazioneTablePage.PAGE_ID.equals(page.getId())) {
				movimentiPreFatturazioneTablePage = (MovimentiPreFatturazioneTablePage) page;
			}
			if (MovimentiInFatturaTablePage.PAGE_ID.equals(page.getId())) {
				movimentiInFatturaTablePage = (MovimentiInFatturaTablePage) page;
			}
		}
		movimentiPreFatturazioneTablePage.setMovimentiInFatturaTablePage(movimentiInFatturaTablePage);
	}

}
