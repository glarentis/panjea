package it.eurotn.panjea.bi.rich.editors.dashboard.widget.analisi.commands;

import it.eurotn.panjea.bi.rich.editors.dashboard.DashBoardPage;
import it.eurotn.rich.command.JideToggleCommand;

import org.springframework.richclient.util.RcpSupport;

public class ToggleFilterDescriptionCommand extends JideToggleCommand {

	public static final String COMMAND_ID = "toggleFilterDescriptionCommand";

	private final DashBoardPage page;

	/**
	 * Costruttore.
	 *
	 * @param page
	 *            pagina legata al comando
	 */
	public ToggleFilterDescriptionCommand(final DashBoardPage page) {
		super(COMMAND_ID);
		RcpSupport.configure(this);
		this.page = page;
		setSelected(page.getAnalisi().isVisualizzaDesrizioneFiltriApplicati());
	}

	@Override
	protected void onDeselection() {
		page.showFilterDescription(false);
	}

	@Override
	protected void onSelection() {
		super.onSelection();
		page.showFilterDescription(true);
	}

}