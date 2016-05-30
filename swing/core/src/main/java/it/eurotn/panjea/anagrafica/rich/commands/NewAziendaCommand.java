/**
 * 
 */
package it.eurotn.panjea.anagrafica.rich.commands;

import it.eurotn.panjea.anagrafica.domain.Azienda;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

/**
 * Command creazione nuova {@link Azienda}.
 * 
 * @author Aracno
 * @version 1.0, 4-mag-2006
 * 
 */
public class NewAziendaCommand extends ApplicationWindowAwareCommand {

	private static final String ID = "openAziendaCommand";

	/**
	 * 
	 */
	public NewAziendaCommand() {
		super(ID);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.richclient.command.ActionCommand#doExecuteCommand()
	 */
	@Override
	protected void doExecuteCommand() {
		LifecycleApplicationEvent event = new OpenEditorEvent(new Azienda());
		Application.instance().getApplicationContext().publishEvent(event);
	}

}
