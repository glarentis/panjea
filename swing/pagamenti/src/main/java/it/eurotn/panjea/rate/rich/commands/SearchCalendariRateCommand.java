package it.eurotn.panjea.rate.rich.commands;

import it.eurotn.panjea.rate.domain.CalendarioRate;
import it.eurotn.panjea.rich.pages.PanjeaDockingApplicationPage;
import it.eurotn.rich.command.OpenEditorCommand;

import org.springframework.richclient.application.ApplicationPage;

public class SearchCalendariRateCommand extends OpenEditorCommand {

	public static final String COMMAND_ID = "searchCalendariRateCommand";

	@Override
	protected void doExecuteCommand() {
		ApplicationPage applicationPage = getApplicationWindow().getPage();
		((PanjeaDockingApplicationPage) applicationPage)
				.openResultView(CalendarioRate.class.getName());
	}

}
