package it.eurotn.panjea.fatturepa.rich.commands;

import java.util.List;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

import it.eurotn.panjea.fatturepa.util.ParametriRicercaFatturePA;

public class OpenIdAreeFatturaPARicercaCommand extends ActionCommand {

    public static final String PARAM_ID_AREE_MAGAZZINO = "paramIdAreeMagazzino";

    /**
     * Costruttore.
     */
    public OpenIdAreeFatturaPARicercaCommand() {
        super("openIdAreeFatturaPARicercaCommand");
        RcpSupport.configure(this);
    }

    @Override
    protected void doExecuteCommand() {

        @SuppressWarnings("unchecked")
        List<Integer> idAree = (List<Integer>) getParameter(PARAM_ID_AREE_MAGAZZINO, null);
        if (idAree == null || idAree.isEmpty()) {
            return;
        }

        ParametriRicercaFatturePA parametriRicerca = new ParametriRicercaFatturePA();
        parametriRicerca.setIdAreeDaRicercare(idAree);
        parametriRicerca.setEffettuaRicerca(true);

        OpenEditorEvent event = new OpenEditorEvent(parametriRicerca);
        Application.instance().getApplicationContext().publishEvent(event);
    }

}
