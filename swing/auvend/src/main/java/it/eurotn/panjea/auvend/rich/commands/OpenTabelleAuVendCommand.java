/**
 * 
 */
package it.eurotn.panjea.auvend.rich.commands;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

/**
 * Command per l'apertura delle tabelle di AuVend.
 * 
 * @author adriano
 * @version 1.0, 31/dic/2008
 * 
 */
public class OpenTabelleAuVendCommand extends ApplicationWindowAwareCommand {

	private static final String COMMAND_ID = "openTabelleAuVendCommand";

	/**
	 * Costruttore.
	 * 
	 */
	public OpenTabelleAuVendCommand() {
		super(COMMAND_ID);
	}

	@Override
	protected void doExecuteCommand() {
		LifecycleApplicationEvent event = new OpenEditorEvent("tabelleAuVendEditor");
		Application.instance().getApplicationContext().publishEvent(event);
	}

}
