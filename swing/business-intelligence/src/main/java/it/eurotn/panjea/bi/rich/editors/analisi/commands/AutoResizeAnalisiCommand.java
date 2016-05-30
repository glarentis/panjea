/**
 *
 */
package it.eurotn.panjea.bi.rich.editors.analisi.commands;

import it.eurotn.panjea.bi.rich.editors.analisi.AnalisiBiEditorController;

import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.command.config.CommandConfigurer;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;

/**
 * @author fattazzo
 *
 */
public class AutoResizeAnalisiCommand extends ApplicationWindowAwareCommand {

	public static final String COMMAND_ID = "DWAutoResizeAnalisiCommand";

	private final AnalisiBiEditorController analisiBiEditorController;

	/**
	 * Costruttore di default.
	 *
	 * @param analisiBiEditorController
	 *            controller
	 */
	public AutoResizeAnalisiCommand(final AnalisiBiEditorController analisiBiEditorController) {
		super(COMMAND_ID);
		CommandConfigurer c = (CommandConfigurer) ApplicationServicesLocator.services().getService(
				CommandConfigurer.class);
		this.setSecurityControllerId(COMMAND_ID);
		c.configure(this);
		this.analisiBiEditorController = analisiBiEditorController;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.richclient.command.ActionCommand#doExecuteCommand()
	 */
	@Override
	protected void doExecuteCommand() {
		analisiBiEditorController.autoResize();
	}

}
