/**
 * 
 */
package it.eurotn.panjea.bi.rich.editors.analisi.commands;

import it.eurotn.panjea.bi.rich.editors.analisi.AnalisiBiEditorController;
import it.eurotn.rich.command.JideToggleCommand;

import org.springframework.richclient.util.RcpSupport;

/**
 * @author fattazzo
 * 
 */
public class JoinTypeChangerCommand extends JideToggleCommand {

	public static final String COMMAND_ID = "joinTypeChangerCommand";

	private final AnalisiBiEditorController analisiBiEditorController;

	/**
	 * Costruttore di default.
	 * 
	 * @param analisiBiEditorController
	 *            controller
	 */
	public JoinTypeChangerCommand(final AnalisiBiEditorController analisiBiEditorController) {
		super(COMMAND_ID);
		this.setSecurityControllerId(COMMAND_ID);
		RcpSupport.configure(this);
		this.analisiBiEditorController = analisiBiEditorController;
		setSelected(false);
	}

	@Override
	protected boolean onSelection(boolean selected) {
		analisiBiEditorController.enableAlternativeJoin(selected);
		return super.onSelection(selected);
	}

}
