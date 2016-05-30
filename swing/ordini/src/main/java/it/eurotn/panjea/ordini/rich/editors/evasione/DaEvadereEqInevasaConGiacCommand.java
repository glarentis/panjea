package it.eurotn.panjea.ordini.rich.editors.evasione;

import it.eurotn.rich.command.JideToggleCommand;

import org.springframework.richclient.util.RcpSupport;

public class DaEvadereEqInevasaConGiacCommand extends JideToggleCommand {

	public static final String COMMAND_ID = "daEvadereEqInevasaConGiacCommand";

	/**
	 * Costruttore.
	 * 
	 */
	public DaEvadereEqInevasaConGiacCommand() {
		super(COMMAND_ID);
		RcpSupport.configure(this);
	}

}
