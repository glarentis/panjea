package it.eurotn.panjea.magazzino.rich.editors.listino;

import it.eurotn.panjea.rich.editors.DockedEditor;

import org.springframework.richclient.dialog.DialogPage;

public class ListinoEditor extends DockedEditor {

	@Override
	protected void initCompositeDialogPage() {
		super.initCompositeDialogPage();
		VersioniListinoTablePage versioniListinoTablePage = null;
		RigheListinoTablePage righeListinoTablePage = null;

		for (DialogPage page : getDialogPages()) {
			if (page.getId().equals(VersioniListinoTablePage.PAGE_ID)) {
				versioniListinoTablePage = (VersioniListinoTablePage) page;
			}
			if (page.getId().equals(RigheListinoTablePage.PAGE_ID)) {
				righeListinoTablePage = (RigheListinoTablePage) page;
			}
		}
		righeListinoTablePage.setVersioniListinoTablePage(versioniListinoTablePage);
	}

}
