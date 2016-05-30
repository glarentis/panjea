package it.eurotn.panjea.magazzino.rich.editors.articolo.marchioce;

import it.eurotn.panjea.magazzino.rich.editors.articolo.marchioce.ArticoloMarchiCEDialog.ViewType;

import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.util.RcpSupport;

public class AnteprimaMarchioCECommand extends ApplicationWindowAwareCommand {

	public static final String COMMAND_ID = "anteprimaMarchioCECommand";

	private final ArticoloMarchiCEDialog articoloMarchiCEDialog;
	private ViewType viewType;

	/**
	 * Costruttore.
	 * 
	 * @param articoloMarchiCEDialog
	 *            articoloMarchiCEDialog
	 */
	public AnteprimaMarchioCECommand(final ArticoloMarchiCEDialog articoloMarchiCEDialog) {
		super(COMMAND_ID);
		RcpSupport.configure(this);
		this.articoloMarchiCEDialog = articoloMarchiCEDialog;
		this.viewType = ViewType.LIST;
	}

	@Override
	protected void doExecuteCommand() {

		if (this.viewType == ViewType.LIST) {
			this.viewType = ViewType.PREVIEW;
		} else {
			this.viewType = ViewType.LIST;
		}

		articoloMarchiCEDialog.swicthToView(viewType);
	}

}
