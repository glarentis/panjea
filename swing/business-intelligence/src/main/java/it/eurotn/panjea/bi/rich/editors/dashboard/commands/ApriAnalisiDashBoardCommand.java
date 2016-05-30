package it.eurotn.panjea.bi.rich.editors.dashboard.commands;

import it.eurotn.panjea.bi.rich.editors.dashboard.DashBoardPage;

import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.command.config.CommandConfigurer;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;

public class ApriAnalisiDashBoardCommand extends ApplicationWindowAwareCommand {

	private final DashBoardPage page;
	private static final String COMMAND_ID = "apriAnalisiDashBoardCommand";

	/**
	 * 
	 * Costruttore.
	 * 
	 * @param page
	 *            pagina collegata al comando
	 */
	public ApriAnalisiDashBoardCommand(final DashBoardPage page) {
		super(COMMAND_ID);
		this.page = page;
		CommandConfigurer c = (CommandConfigurer) ApplicationServicesLocator.services().getService(
				CommandConfigurer.class);
		this.setSecurityControllerId(COMMAND_ID);
		c.configure(this);
	}

	@Override
	protected void doExecuteCommand() {
		page.apriAnalisi();
	}

}
