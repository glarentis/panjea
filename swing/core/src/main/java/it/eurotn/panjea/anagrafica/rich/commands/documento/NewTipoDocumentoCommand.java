/**
 * 
 */
package it.eurotn.panjea.anagrafica.rich.commands.documento;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;

import org.apache.log4j.Logger;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

/**
 * 
 * @author adriano
 * @version 1.0, 22/mag/07
 * 
 */
public class NewTipoDocumentoCommand extends ApplicationWindowAwareCommand {

	private static Logger logger = Logger.getLogger(NewTipoDocumentoCommand.class);

	public static final String COMMAND_ID = "newTipoDocumentoCommand";

	/**
	 * Costruttore.
	 * 
	 */
	public NewTipoDocumentoCommand() {
		super(COMMAND_ID);
	}

	@Override
	protected void doExecuteCommand() {
		logger.debug("--> Enter doExecuteCommand");
		TipoDocumento tipoDocumento = new TipoDocumento();
		LifecycleApplicationEvent event = new OpenEditorEvent(tipoDocumento);
		Application.instance().getApplicationContext().publishEvent(event);
		logger.debug("--> Exit doExecuteCommand");
	}

}
