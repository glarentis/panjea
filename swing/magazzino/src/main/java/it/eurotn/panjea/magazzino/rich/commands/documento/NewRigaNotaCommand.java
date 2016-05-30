package it.eurotn.panjea.magazzino.rich.commands.documento;

import org.apache.log4j.Logger;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.magazzino.domain.RigaNota;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.rich.editors.IPageEditor;

/**
 * ActionCommand per la creazione di una nuova {@link RigaNota} di {@link AreaMagazzino}.
 *
 * @author adriano
 * @version 1.0, 31/ott/2008
 *
 */
public class NewRigaNotaCommand extends ActionCommand {

	private static Logger logger = Logger.getLogger(NewRigaNotaCommand.class);

	private static final String COMMAND_ID = "newRigaNotaCommand";

	private IPageEditor pageEditor;

	/**
	 * @param pageId
	 *            id della pagina
	 *
	 */
	public NewRigaNotaCommand(final String pageId) {
		super(COMMAND_ID);
		this.setSecurityControllerId(pageId + ".controller");
		RcpSupport.configure(this);
	}

	@Override
	protected void doExecuteCommand() {
		logger.debug("--> Enter doExecuteCommand");
		pageEditor.onNew();
		logger.debug("--> Exit doExecuteCommand");
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
	}

	/**
	 * @param pageEditor
	 *            The pageEditor to set.
	 */
	public void setPageEditor(IPageEditor pageEditor) {
		this.pageEditor = pageEditor;
	}

}
