package it.eurotn.panjea.magazzino.rich.commands.documento;

import org.apache.log4j.Logger;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.magazzino.domain.RigaArticolo;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.rich.editors.righemagazzino.RigaArticoloPage;
import it.eurotn.rich.editors.IFormPageEditor;

/**
 * ActionCommand per la creazione di una nuova {@link RigaArticolo} di {@link AreaMagazzino}.
 *
 * @author adriano
 * @version 1.0, 31/ott/2008
 *
 */
public class NewRigaArticoloCommand extends ActionCommand {

	protected static Logger logger = Logger.getLogger(NewRigaArticoloCommand.class);

	private static final String COMMAND_ID = "newRigaArticoloCommand";

	private IFormPageEditor pageEditor;

	/**
	 *
	 */
	public NewRigaArticoloCommand() {
		super(COMMAND_ID);
		this.setSecurityControllerId(RigaArticoloPage.PAGE_ID + ".controller");
		RcpSupport.configure(this);
	}

	@Override
	protected void doExecuteCommand() {
		logger.debug("--> Enter doExecuteCommand");
		pageEditor.onNew();
		logger.debug("--> Exit doExecuteCommand");
	}

	/**
	 * @param pageEditor
	 *            The pageEditor to set.
	 */
	public void setPageEditor(IFormPageEditor pageEditor) {
		this.pageEditor = pageEditor;
	}
}
