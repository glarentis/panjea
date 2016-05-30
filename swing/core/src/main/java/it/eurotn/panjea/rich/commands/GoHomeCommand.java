package it.eurotn.panjea.rich.commands;

import it.eurotn.panjea.rich.editors.webbrowser.JasperServerUrl;

import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;

/**
 * Command per visualizzare la home page dell'applicazione.
 * 
 * @author Leonardo
 */
public class GoHomeCommand extends ApplicationWindowAwareCommand {

	private static final String ID = "goHomeCommand";

	/**
	 * Costruttore.
	 * 
	 */
	public GoHomeCommand() {
		super(ID);
	}

	@Override
	protected void doExecuteCommand() {

		getApplicationWindow().getPage().openEditor(new JasperServerUrl());
	}
}
