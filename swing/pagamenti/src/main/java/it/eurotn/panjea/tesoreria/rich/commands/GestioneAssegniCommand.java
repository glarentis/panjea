package it.eurotn.panjea.tesoreria.rich.commands;

import it.eurotn.panjea.tesoreria.util.ParametriRicercaAssegni;

import org.apache.log4j.Logger;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

public class GestioneAssegniCommand extends ApplicationWindowAwareCommand {
	private static Logger logger = Logger.getLogger(GestioneAssegniCommand.class);

	public static final String COMMAND_ID = "gestioneAssegniCommand";

	@Override
	protected void doExecuteCommand() {
		logger.debug("--> Enter doExecuteCommand");
		ParametriRicercaAssegni parametriRicercaAssegni = new ParametriRicercaAssegni();
		LifecycleApplicationEvent event = new OpenEditorEvent(parametriRicercaAssegni);
		Application.instance().getApplicationContext().publishEvent(event);
		logger.debug("--> Exit doExecuteCommand");
	}
}
