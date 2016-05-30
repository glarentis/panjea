/**
 * 
 */
package it.eurotn.panjea.beniammortizzabili.rich.commands;

import it.eurotn.panjea.beniammortizzabili.rich.editors.beni.ReportRegistroBeniDialogPage;
import it.eurotn.rich.command.OpenEditorCommand;

import org.apache.log4j.Logger;
import org.springframework.richclient.application.Application;

/**
 * 
 * @author adriano
 * @version 1.0, 24/nov/06
 */
public class ReportRegistroBeniCommand extends OpenEditorCommand {

	private static Logger logger = Logger.getLogger(ReportRegistroBeniCommand.class);

	@Override
	protected void doExecuteCommand() {
		logger.debug("--> Enter doExecuteCommand");
		ReportRegistroBeniDialogPage stampaRegistroBeniPage = (ReportRegistroBeniDialogPage) Application.instance()
				.getApplicationContext().getBean("reportRegistroBeniDialogPage");
		stampaRegistroBeniPage.setCallingCommand(this);
		stampaRegistroBeniPage.showDialog();
	}
}
