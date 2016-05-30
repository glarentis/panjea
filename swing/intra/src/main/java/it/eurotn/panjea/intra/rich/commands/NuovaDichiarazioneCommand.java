package it.eurotn.panjea.intra.rich.commands;

import it.eurotn.panjea.intra.domain.DichiarazioneIntra;
import it.eurotn.panjea.intra.domain.DichiarazioneIntraVendite;
import it.eurotn.rich.command.OpenEditorCommand;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

/**
 * Apre l'area Intra del documento preso come parametro.<br/>
 * Se l'area intra non esiste ne viene creata una.
 * 
 * @author giangi
 * @version 1.0, 28/set/2012
 */
public class NuovaDichiarazioneCommand extends OpenEditorCommand {

	@Override
	protected void doExecuteCommand() {
		// Creo la dichiarazione solamente per aprire l'editor.
		// L'editor controlla se l'intra è nuovo e chiede i dati per crearlo
		DichiarazioneIntra dichiarazione = new DichiarazioneIntraVendite();
		LifecycleApplicationEvent event = new OpenEditorEvent(dichiarazione);
		Application.instance().getApplicationContext().publishEvent(event);
	}
}
