/**
 * 
 */
package it.eurotn.panjea.anagrafica.rich.commands.documento;

import it.eurotn.panjea.rich.pages.PanjeaDockingApplicationPage;

import org.springframework.richclient.application.ApplicationPage;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;

/**
 * 
 * 
 * @author adriano
 * @version 1.0, 18/mag/07
 * 
 */
public class SearchTipiDocumentoCommand extends ApplicationWindowAwareCommand {

	private static final String COMMAND_ID = "searchTipiDocumentoCommand";

	@Override
	protected void doExecuteCommand() {
		ApplicationPage applicationPage = getApplicationWindow().getPage();
		((PanjeaDockingApplicationPage) applicationPage)
				.openResultView("it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento");
	}

	@Override
	public String getId() {
		return COMMAND_ID;
	}

}
