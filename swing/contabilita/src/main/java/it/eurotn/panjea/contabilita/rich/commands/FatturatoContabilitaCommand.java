package it.eurotn.panjea.contabilita.rich.commands;

import org.springframework.richclient.application.Application;

import it.eurotn.panjea.contabilita.rich.editors.fatturato.FatturatoContabilitaDialogPage;
import it.eurotn.rich.command.OpenEditorCommand;

/**
 *
 * @author adriano
 * @version 1.0, 24/nov/06
 */
public class FatturatoContabilitaCommand extends OpenEditorCommand {

    /**
     * Costruttore.
     */
    public FatturatoContabilitaCommand() {
        super();
        setIdCommand("fatturatoContabilitaCommand");
    }

    @Override
    protected void doExecuteCommand() {
        FatturatoContabilitaDialogPage fatturatoContabilitaDialogPage = (FatturatoContabilitaDialogPage) Application
                .instance().getApplicationContext().getBean("fatturatoContabilitaDialogPage");
        fatturatoContabilitaDialogPage.setCallingCommand(this);
        fatturatoContabilitaDialogPage.showDialog();
    }
}
