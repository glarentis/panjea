/**
 * 
 */
package it.eurotn.panjea.intra.rich.commands;

import it.eurotn.panjea.rich.pages.PanjeaDockingApplicationPage;
import it.eurotn.rich.command.OpenEditorCommand;

import org.springframework.richclient.application.ApplicationPage;

/**
 * Apre la vista dei risultati della ricerca listini.
 * 
 * @author fattazzo
 * 
 */
public class SearchDichiarazioniCommand extends OpenEditorCommand {

	@Override
	protected void doExecuteCommand() {
		ApplicationPage applicationPage = getApplicationWindow().getPage();
		((PanjeaDockingApplicationPage) applicationPage)
				.openResultView("it.eurotn.panjea.intra.domain.DichiarazioneIntra");
	}
}
