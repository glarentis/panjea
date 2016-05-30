package it.eurotn.panjea.magazzino.rich.editors.categoriaarticolo;

import it.eurotn.panjea.magazzino.rich.editors.articolo.ArticoloPage;
import it.eurotn.panjea.rich.editors.DockedEditor;
import it.eurotn.rich.editors.IEditorCommands;

import org.springframework.richclient.dialog.DialogPage;

public class CategoriaArticoloEditor extends DockedEditor {
	@Override
	protected void initCompositeDialogPage() {
		super.initCompositeDialogPage();
		GestioneArticoliTablePage articoliPage = null;
		IEditorCommands editorCommands = null;
		for (DialogPage dialogPage : getDialogPages()) {
			if (dialogPage.getId().equals(ArticoliTablePage.PAGE_ID)) {
				articoliPage = (GestioneArticoliTablePage) dialogPage;
			}
			if (dialogPage.getId().equals(ArticoloPage.PAGE_ID)) {
				editorCommands = (IEditorCommands) dialogPage;
			}
		}
		org.springframework.util.Assert.notNull(articoliPage,
				"la pagina articoliTablePage per l'editor delle categorie articolo non trovata");
		org.springframework.util.Assert.notNull(editorCommands,
				"la pagina articoloPage per l'editor delle categorie articolo non trovata");
		articoliPage.setArticoloPage(editorCommands);
	}
}
