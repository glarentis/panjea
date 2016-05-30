package it.eurotn.panjea.contabilita.rich.commands;

import org.apache.log4j.Logger;
import org.springframework.richclient.application.Application;

import it.eurotn.panjea.contabilita.rich.editors.riepilogoblacklist.ReportRiepilogoDocumentiBlacklistDialogPage;
import it.eurotn.rich.command.OpenEditorCommand;

/**
 *
 * @author adriano
 * @version 1.0, 24/nov/06
 */
public class ReportRiepilogoDocumentiBlacklistCommand extends OpenEditorCommand {

    private static final Logger LOGGER = Logger.getLogger(ReportRiepilogoDocumentiBlacklistCommand.class);

    @Override
    protected void doExecuteCommand() {
        LOGGER.debug("--> Enter doExecuteCommand");
        ReportRiepilogoDocumentiBlacklistDialogPage page = (ReportRiepilogoDocumentiBlacklistDialogPage) Application
                .instance().getApplicationContext().getBean("reportRiepilogoDocumentiBlacklistDialogPage");
        page.setCallingCommand(this);
        page.showDialog();
    }
}
