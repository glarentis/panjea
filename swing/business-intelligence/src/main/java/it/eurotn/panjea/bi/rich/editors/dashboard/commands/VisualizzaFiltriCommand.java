package it.eurotn.panjea.bi.rich.editors.dashboard.commands;

import it.eurotn.panjea.bi.rich.editors.dashboard.DashBoardCompositePage;

import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.config.CommandConfigurer;

public class VisualizzaFiltriCommand extends ActionCommand {

	private static final String COMMAND_ID = "visualizzaFiltroCommand";
	private DashBoardCompositePage dashboardPage;

	/**
	 *
	 * Costruttore.
	 *
	 * @param dashboardPage
	 *            pagina della dashBoard.
	 */
	public VisualizzaFiltriCommand(final DashBoardCompositePage dashboardPage) {
		super(COMMAND_ID);
		this.dashboardPage = dashboardPage;
		CommandConfigurer c = (CommandConfigurer) ApplicationServicesLocator.services().getService(
				CommandConfigurer.class);
		this.setSecurityControllerId(COMMAND_ID);
		c.configure(this);
	}

	@Override
	protected void doExecuteCommand() {
		dashboardPage.visualizzaFiltri();
	}

}
