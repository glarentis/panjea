package it.eurotn.panjea.auvend.rich.editors.importazioni.fatture;

import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.command.config.CommandConfigurer;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;

public class ImportaFattureCommand extends ApplicationWindowAwareCommand {

	public static final String COMMAND_ID = "importaAuvendCommand";

	private final ImportazioneFattureTablePage page;

	/**
	 * Costruttore.
	 * 
	 * @param page
	 *            pagina di importazione delle fatture
	 */
	public ImportaFattureCommand(final ImportazioneFattureTablePage page) {
		super(COMMAND_ID);
		CommandConfigurer c = (CommandConfigurer) ApplicationServicesLocator.services().getService(
				CommandConfigurer.class);
		this.page = page;
		this.setSecurityControllerId(COMMAND_ID);
		c.configure(this);
	}

	@Override
	protected void doExecuteCommand() {

		page.importaDepositiSelezionati();

	}

}