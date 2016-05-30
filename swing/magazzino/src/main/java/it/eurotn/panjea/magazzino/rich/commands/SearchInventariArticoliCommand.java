package it.eurotn.panjea.magazzino.rich.commands;

import it.eurotn.panjea.rich.pages.PanjeaDockingApplicationPage;
import it.eurotn.rich.command.OpenEditorCommand;

import org.springframework.richclient.application.ApplicationPage;

public class SearchInventariArticoliCommand extends OpenEditorCommand {

	@Override
	protected void doExecuteCommand() {
		ApplicationPage applicationPage = getApplicationWindow().getPage();
		((PanjeaDockingApplicationPage) applicationPage)
				.openResultView("it.eurotn.panjea.magazzino.util.InventarioArticoloDTO");
	}

}
