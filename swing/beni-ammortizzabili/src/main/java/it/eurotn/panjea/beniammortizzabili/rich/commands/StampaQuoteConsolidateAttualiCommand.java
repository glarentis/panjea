package it.eurotn.panjea.beniammortizzabili.rich.commands;

import it.eurotn.panjea.beniammortizzabili.rich.editors.beni.ReportQuoteAnnoDialogPage;

import org.apache.log4j.Logger;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;

public class StampaQuoteConsolidateAttualiCommand extends ApplicationWindowAwareCommand {

	private static Logger logger = Logger.getLogger(StampaQuoteConsolidateAttualiCommand.class);

	@Override
	protected void doExecuteCommand() {
		logger.debug("--> Enter doExecuteCommand");
		ReportQuoteAnnoDialogPage reportQuoteAnnoDialogPage = (ReportQuoteAnnoDialogPage) Application.instance()
				.getApplicationContext().getBean("reportQuoteAnnoDialogPage");
		reportQuoteAnnoDialogPage.setCallingCommand(this);
		reportQuoteAnnoDialogPage.showDialog();
	}

}
