/**
 * 
 */
package it.eurotn.panjea.intra.rich.commands;

import it.eurotn.rich.command.OpenEditorCommand;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

/**
 * Apre la vista dei risultati per le dichiarazioni da presentare.
 * 
 * @author fattazzo
 * 
 */
public class OpenDichiarazioniDaPresentareCommand extends OpenEditorCommand {

	@Override
	protected void doExecuteCommand() {
		LifecycleApplicationEvent event = new OpenEditorEvent("dichiarazioniDaPresentare");
		Application.instance().getApplicationContext().publishEvent(event);
	}
}
