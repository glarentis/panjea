package it.eurotn.panjea.rich.commands;

import it.eurotn.panjea.anagrafica.util.parametriricerca.ParametriRicercaMail;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

public class GestioneEmailCommand extends ApplicationWindowAwareCommand {

    @Override
    protected void doExecuteCommand() {
        logger.debug("--> Apro l'editor per le email");

        LifecycleApplicationEvent event = new OpenEditorEvent(new ParametriRicercaMail());
        Application.instance().getApplicationContext().publishEvent(event);
    }

}
