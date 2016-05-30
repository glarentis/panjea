/**
 * 
 */
package it.eurotn.panjea.magazzino.rich.commands;

import it.eurotn.panjea.rich.pages.PanjeaDockingApplicationPage;
import it.eurotn.rich.command.OpenEditorCommand;

import org.springframework.richclient.application.ApplicationPage;

/**
 * Apre la vista dei risultati della ricerca listini.
 * 
 * @author fattazzo
 * 
 */
public class SearchListiniCommand extends OpenEditorCommand {

	@Override
	protected void doExecuteCommand() {
		ApplicationPage applicationPage = getApplicationWindow().getPage();
		((PanjeaDockingApplicationPage) applicationPage)
				.openResultView("it.eurotn.panjea.magazzino.domain.Listino");
	}
}
