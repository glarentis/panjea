package it.eurotn.panjea.rich.commands;

import it.eurotn.panjea.rich.editors.update.PanjeaServer;

import java.awt.Desktop;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.util.RcpSupport;

public class ScriptServerCommand extends ApplicationWindowAwareCommand {

	private static final String ID = "scriptServerCommand";

	/**
	 * Costruttore.
	 *
	 */
	public ScriptServerCommand() {
		super(ID);
	}

	@Override
	protected void doExecuteCommand() {
		if (Desktop.isDesktopSupported()) {
			PanjeaServer panjeaServer = RcpSupport.getBean(PanjeaServer.BEAN_ID);
			try {
				Desktop.getDesktop().browse(panjeaServer.getServerAddress().toURI());
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}
	}
}
