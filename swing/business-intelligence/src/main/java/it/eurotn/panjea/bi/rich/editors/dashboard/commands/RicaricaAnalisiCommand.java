package it.eurotn.panjea.bi.rich.editors.dashboard.commands;

import it.eurotn.panjea.bi.rich.editors.dashboard.DashBoardPage;
import it.eurotn.panjea.exceptions.PanjeaRuntimeException;
import it.eurotn.panjea.magazzino.exception.AnalisiNonPresenteException;

import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.config.CommandConfigurer;

/**
 * Ricarica la ricerca (struttura e dati) nella pagina.
 * 
 * @author giangi
 * @version 1.0, 13/giu/2012
 * 
 */
public class RicaricaAnalisiCommand extends ActionCommand {

	private final DashBoardPage page;
	private static final String COMMAND_ID = "ricaricaAnalisiCommand";

	/**
	 * 
	 * Costruttore.
	 * 
	 * @param page
	 *            pagina legata al comando
	 */
	public RicaricaAnalisiCommand(final DashBoardPage page) {
		super(COMMAND_ID);
		this.page = page;
		CommandConfigurer c = (CommandConfigurer) ApplicationServicesLocator.services().getService(
				CommandConfigurer.class);
		this.setSecurityControllerId(COMMAND_ID);
		c.configure(this);
	}

	@Override
	protected void doExecuteCommand() {
		try {
			page.caricaAnalisi();
		} catch (AnalisiNonPresenteException e) {
			throw new PanjeaRuntimeException(e);
		}

	}

}
