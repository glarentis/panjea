/**
 * 
 */
package it.eurotn.panjea.beniammortizzabili.rich.commands;

import it.eurotn.panjea.beniammortizzabili.rich.editors.beni.ReportAmmortamentiDialogPage;
import it.eurotn.rich.command.OpenEditorCommand;

import org.apache.log4j.Logger;
import org.springframework.richclient.application.Application;

/**
 * 
 * @author adriano
 * @version 1.0, 13/dic/06
 */
public class ReportAmmortamentiCommand extends OpenEditorCommand {

	private static Logger logger = Logger.getLogger(ReportAmmortamentiCommand.class);

	@Override
	protected void doExecuteCommand() {
		logger.debug("--> Enter doExecuteCommand");
		ReportAmmortamentiDialogPage reportAmmortamentiPage = (ReportAmmortamentiDialogPage) Application.instance()
				.getApplicationContext().getBean("reportAmmortamentiDialogPage");
		reportAmmortamentiPage.setCallingCommand(this);
		reportAmmortamentiPage.showDialog();
		logger.debug("--> Exit doExecuteCommand");
	}
}
