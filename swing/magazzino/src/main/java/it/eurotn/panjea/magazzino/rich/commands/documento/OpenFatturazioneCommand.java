package it.eurotn.panjea.magazzino.rich.commands.documento;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriRicercaFatturazione;

/**
 * Command per l'apertura dell'editor della fatturazione.
 *
 * @author fattazzo
 *
 */
public class OpenFatturazioneCommand extends ApplicationWindowAwareCommand {

	public static final String COMMAND_ID = "openFatturazioneCommand";

	/**
	 * Costruttore.
	 *
	 */
	public OpenFatturazioneCommand() {
		super(COMMAND_ID);
		this.setSecurityControllerId(COMMAND_ID);
		RcpSupport.configure(this);
	}

	@Override
	protected void doExecuteCommand() {
		LifecycleApplicationEvent event = new OpenEditorEvent(new ParametriRicercaFatturazione());
		Application.instance().getApplicationContext().publishEvent(event);
	}

}
