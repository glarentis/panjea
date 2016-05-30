package it.eurotn.panjea.bi.rich.editors.dashboard;

import it.eurotn.panjea.bi.rich.bd.IBusinessIntelligenceBD;
import it.eurotn.panjea.bi.rich.editors.dashboard.commands.VisualizzaFiltriCommand;

import javax.swing.JToolBar;

import org.springframework.richclient.command.CommandGroup;

public class ToolBarDashBoard extends JToolBar {

	private static final long serialVersionUID = -8907844971298859555L;

	private DashBoardCompositePage dashboardPage;

	/**
	 * Costruttore.
	 *
	 * @param paramDashBoard
	 *            dashboard
	 * @param businessIntelligenceBD
	 *            bd per il datawarehouse
	 */
	public ToolBarDashBoard(final DashBoardCompositePage paramDashBoard,
			final IBusinessIntelligenceBD businessIntelligenceBD) {
		CommandGroup toolbar = new CommandGroup();
		this.dashboardPage = paramDashBoard;
		VisualizzaFiltriCommand visualizzaFiltriCommand = new VisualizzaFiltriCommand(dashboardPage);
		// ExportToJrxmlCommand jasperCommand = new ExportToJrxmlCommand(dashboardPage);

		/** -----------IMPOSTAZIONE FILTRI DEL CRUSCOTTO---------- **/
		toolbar.add(visualizzaFiltriCommand);

		add(toolbar.createToolBar());
	}

	/**
	 * dispose.
	 */
	public void dispose() {
		dashboardPage = null;
	}
}