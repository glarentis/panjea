/**
 * 
 */
package it.eurotn.panjea.beniammortizzabili.rich.commands;

import it.eurotn.panjea.beniammortizzabili.rich.editors.beni.ReportBeniAcquistatiDialogPage;
import it.eurotn.rich.command.OpenEditorCommand;

import org.apache.log4j.Logger;
import org.springframework.richclient.application.Application;

/**
 * 
 * @author adriano
 * @version 1.0, 04/dic/06
 */
public class ReportBeniAcquistatiCommand extends OpenEditorCommand {

	private static Logger logger = Logger.getLogger(ReportBeniAcquistatiCommand.class);

	@Override
	protected void doExecuteCommand() {
		logger.debug("--> Enter doExecuteCommand");
		ReportBeniAcquistatiDialogPage reportBeniAnnualiPage = (ReportBeniAcquistatiDialogPage) Application.instance()
				.getApplicationContext().getBean("reportBeniAcquistatiDialogPage");
		reportBeniAnnualiPage.setCallingCommand(this);
		reportBeniAnnualiPage.showDialog();
		logger.debug("--> Exit doExecuteCommand");
	}
}
