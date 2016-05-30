/**
 * 
 */
package it.eurotn.panjea.magazzino.rich.commands;

import it.eurotn.panjea.magazzino.util.ParametriRicercaContratti;
import it.eurotn.panjea.rich.pages.PanjeaDockingApplicationPage;
import it.eurotn.rich.command.OpenEditorCommand;

import java.util.HashMap;
import java.util.Map;

import org.springframework.richclient.application.ApplicationPage;

/**
 * Command per l'esecuzione della ricerca di Contratti.
 * 
 * @author adriano
 * @version 1.0, 18/giu/08
 * 
 */
public class SearchContrattiCommand extends OpenEditorCommand {

	@Override
	protected void doExecuteCommand() {
		ApplicationPage applicationPage = getApplicationWindow().getPage();
		// istanzio una Map di parametri e aggiungo l'oggetto ParametriRicercaContratti
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(ParametriRicercaContratti.REF, new ParametriRicercaContratti());
		((PanjeaDockingApplicationPage) applicationPage).openResultView("Contratto", parameters);
	}

}
