package it.eurotn.panjea.rich.commands;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.command.support.ExitCommand;

public class PanjeaExitCommand extends ExitCommand {

    private boolean termina = false;

    /**
     * Costruttore. Questo command ha come id l'id di ExitCommand da cui eredita.
     */
    public PanjeaExitCommand() {
        super();
    }

    @Override
    protected void doExecuteCommand() {
        String osName = System.getProperty("os.name");

        // verifico che il sistema operativo non sia MAC OS oppure che non abbia forzato la chiusura di panjea
        if ((osName.toLowerCase().startsWith("windows") || (osName.toLowerCase().startsWith("linux"))) && !termina) {
            getApplicationWindow().getControl().setVisible(false);
        } else {
            // forzo la chiusura dell'applicazione
            Application.instance().close(true, 0);
        }
    }

    /**
     * @param termina
     *            forza il termine di Panjea, false di default.
     */
    public void setTermina(boolean termina) {
        this.termina = termina;
    }

}
