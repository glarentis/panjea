package it.eurotn.panjea.bi.rich.editors.dashboard.commands;

import it.eurotn.panjea.bi.rich.editors.dashboard.DashBoardPage;

import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.command.config.CommandConfigurer;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;

public class CambiaVisualizzazioneCommand extends ApplicationWindowAwareCommand {

	private final DashBoardPage page;
	private static final String COMMAND_ID = "cambiaVisualizzazioneCommand";

	/**
	 *
	 * Costruttore.
	 *
	 * @param dashBoardCompositePage
	 *
	 * @param page
	 *            pagina collegata al comando
	 * @param dashboard
	 *            dashboard legata al comando
	 */
	public CambiaVisualizzazioneCommand(final DashBoardPage page) {
		super(COMMAND_ID);
		this.page = page;
		CommandConfigurer c = (CommandConfigurer) ApplicationServicesLocator.services().getService(
				CommandConfigurer.class);
		this.setSecurityControllerId(COMMAND_ID);
		c.configure(this);
	}

	@Override
	protected void doExecuteCommand() {
		page.cambiaTipoVisualizzazione();
	}

	/**
	 * @return Returns the page.
	 */
	public DashBoardPage getPage() {
		return page;
	}

}
