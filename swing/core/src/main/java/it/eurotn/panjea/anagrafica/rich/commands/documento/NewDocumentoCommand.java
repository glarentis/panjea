/**
 * 
 */
package it.eurotn.panjea.anagrafica.rich.commands.documento;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;

import org.apache.log4j.Logger;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

/**
 * 
 * @author Leonardo
 * @version 1.0, 28/mag/07
 * 
 */
public class NewDocumentoCommand extends ApplicationWindowAwareCommand {

	private static Logger logger = Logger.getLogger(NewDocumentoCommand.class);

	public static final String COMMAND_ID = "newDocumentoCommand";

	/**
	 * Costruttore.
	 * 
	 */
	public NewDocumentoCommand() {
		super(COMMAND_ID);
	}

	@Override
	protected void doExecuteCommand() {
		logger.debug("--> Enter doExecuteCommand");
		Documento documento = new Documento();
		LifecycleApplicationEvent event = new OpenEditorEvent(documento);
		Application.instance().getApplicationContext().publishEvent(event);
		logger.debug("--> Exit doExecuteCommand");
	}

}
