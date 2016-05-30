package it.eurotn.panjea.magazzino.rich.editors.righemagazzino;

import it.eurotn.rich.command.JideToggleCommand;

import javax.swing.AbstractButton;

import org.springframework.richclient.util.RcpSupport;

public class StampaDocumentoDopoConfermaToggleCommand extends JideToggleCommand {

	public static final String COMMAND_ID = "stampaDocumentoDopoConfermaToggleCommand";

	/**
	 * Costruttore.
	 */
	public StampaDocumentoDopoConfermaToggleCommand() {
		super(COMMAND_ID);
		RcpSupport.configure(this);
	}

	@Override
	protected void onButtonAttached(AbstractButton button) {
		super.onButtonAttached(button);
		button.setName(COMMAND_ID);
	}

	@Override
	public void setEnabled(boolean enabled) {
		// rimane sempre abilitato
		super.setEnabled(true);
	}
}
