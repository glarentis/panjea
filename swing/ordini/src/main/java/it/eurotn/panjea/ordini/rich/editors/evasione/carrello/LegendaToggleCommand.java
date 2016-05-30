package it.eurotn.panjea.ordini.rich.editors.evasione.carrello;

import it.eurotn.rich.command.JideToggleCommand;

import org.springframework.richclient.util.RcpSupport;

public class LegendaToggleCommand extends JideToggleCommand {

	private static final String COMMAND_ID = "LegendaToggleCommand";

	private CarrelloEvasioneOrdiniTablePage carrelloEvasioneOrdiniTablePage;

	/**
	 * Costruttore.
	 * 
	 * @param carrelloEvasioneOrdiniTablePage
	 *            {@link CarrelloEvasioneOrdiniTablePage}
	 * 
	 */
	public LegendaToggleCommand(final CarrelloEvasioneOrdiniTablePage carrelloEvasioneOrdiniTablePage) {
		super(COMMAND_ID);
		RcpSupport.configure(this);
		this.carrelloEvasioneOrdiniTablePage = carrelloEvasioneOrdiniTablePage;
	}

	@Override
	protected void onDeselection() {
		super.onDeselection();
		carrelloEvasioneOrdiniTablePage.showLegend(false);
	}

	@Override
	protected void onSelection() {
		super.onSelection();
		carrelloEvasioneOrdiniTablePage.showLegend(true);
	}
}
