/**
 * 
 */
package it.eurotn.panjea.beniammortizzabili.rich.commands;

import it.eurotn.panjea.beniammortizzabili.rich.editors.beni.ReportRubricaBeniDialogPage;
import it.eurotn.rich.command.OpenEditorCommand;

import org.apache.log4j.Logger;
import org.springframework.richclient.application.Application;

/**
 * 
 * @author adriano
 * @version 1.0, 24/nov/06
 */
public class ReportRubricaBeniCommand extends OpenEditorCommand {

	private static Logger logger = Logger.getLogger(ReportRubricaBeniCommand.class);

	@Override
	protected void doExecuteCommand() {
		logger.debug("--> Enter doExecuteCommand");
		ReportRubricaBeniDialogPage stampaRubricaBeniPage = (ReportRubricaBeniDialogPage) Application.instance()
				.getApplicationContext().getBean("reportRubricaBeniDialogPage");
		stampaRubricaBeniPage.setCallingCommand(this);
		stampaRubricaBeniPage.showDialog();
	}
}
