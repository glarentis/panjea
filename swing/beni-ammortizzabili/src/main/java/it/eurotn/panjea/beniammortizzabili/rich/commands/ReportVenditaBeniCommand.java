/**
 * 
 */
package it.eurotn.panjea.beniammortizzabili.rich.commands;

import it.eurotn.panjea.beniammortizzabili.rich.editors.beni.ReportVenditaBeniDialogPage;
import it.eurotn.rich.command.OpenEditorCommand;

import org.apache.log4j.Logger;
import org.springframework.richclient.application.Application;

/**
 * 
 * @author adriano
 * @version 1.0, 12/dic/06
 * 
 */
public class ReportVenditaBeniCommand extends OpenEditorCommand {

	private static Logger logger = Logger.getLogger(ReportVenditaBeniCommand.class);

	@Override
	protected void doExecuteCommand() {
		logger.debug("--> Enter doExecuteCommand");
		ReportVenditaBeniDialogPage reportVenditaBeniPage = (ReportVenditaBeniDialogPage) Application.instance()
				.getApplicationContext().getBean("reportVenditaBeniDialogPage");
		reportVenditaBeniPage.setCallingCommand(this);
		reportVenditaBeniPage.showDialog();
		logger.debug("--> Exit doExecuteCommand");
	}
}
