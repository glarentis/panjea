package it.eurotn.panjea.anagrafica.rich.statusBarItem;

import it.eurotn.panjea.rich.commands.PanjeaExitCommand;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.dialog.MessageDialog;

public class AggiornamentoCompletatoDialog extends MessageDialog {

    private PanjeaExitCommand exitCommand;

    /**
     * Costruttore.
     */
    public AggiornamentoCompletatoDialog() {
        super("ATTENZIONE", "Chiudere Panjea una volta terminato l'aggiornamento con successo!");
    }

    @Override
    protected Object[] getCommandGroupMembers() {
        return new Object[] { getExitCommand() };
    }

    /**
     * @return ExitCommand
     */
    private PanjeaExitCommand getExitCommand() {
        if (exitCommand == null) {
            exitCommand = (PanjeaExitCommand) Application.instance().getActiveWindow().getCommandManager()
                    .getCommand("exitCommand");
            exitCommand.setTermina(true);
        }
        return exitCommand;
    }

    @Override
    protected void onCancel() {
    }
}
