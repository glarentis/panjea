package it.eurotn.panjea.bi.rich.editors.analisi.commands;

import it.eurotn.panjea.bi.rich.editors.analisi.AnalisiBiEditorController;

import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.command.config.CommandConfigurer;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;

public class FlatLayoutCommand extends ApplicationWindowAwareCommand {

	public static final String COMMAND_ID = "DWFlatLayoutCommand";

	private final AnalisiBiEditorController analisiBiEditorController;

	/**
	 * Costruttore di default.
	 *
	 * @param analisiBiEditorController
	 *            controller
	 */
	public FlatLayoutCommand(final AnalisiBiEditorController analisiBiEditorController) {
		super(COMMAND_ID);
		CommandConfigurer c = (CommandConfigurer) ApplicationServicesLocator.services().getService(
				CommandConfigurer.class);
		this.setSecurityControllerId(COMMAND_ID);
		c.configure(this);
		this.analisiBiEditorController = analisiBiEditorController;
	}

	@Override
	protected void doExecuteCommand() {
		analisiBiEditorController.toogleFlatLayout();
	}

}
