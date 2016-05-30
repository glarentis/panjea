/**
 *
 */
package it.eurotn.panjea.bi.rich.editors.dashboard.filtri.commands;

import it.eurotn.panjea.bi.domain.dashboard.DashBoard.LayoutFiltri;
import it.eurotn.panjea.bi.rich.editors.dashboard.filtri.DashboardFiltriPage;

import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.util.RcpSupport;

/**
 * @author fattazzo
 *
 */
public class LayoutFiltriCommand extends ActionCommand {

	private DashboardFiltriPage dashboardFiltriPage;

	private LayoutFiltri layoutFiltri;

	/**
	 * Costruttore.
	 *
	 * @param dashboardFiltriPage
	 *            pagina dei filtri
	 * @param layoutFiltri
	 *            layout da applicare alla pagina
	 */
	public LayoutFiltriCommand(final DashboardFiltriPage dashboardFiltriPage, final LayoutFiltri layoutFiltri) {
		super(layoutFiltri.name().toLowerCase() + "LayoutFiltriCommand");
		RcpSupport.configure(this);

		this.dashboardFiltriPage = dashboardFiltriPage;
		this.layoutFiltri = layoutFiltri;
	}

	@Override
	protected void doExecuteCommand() {
		dashboardFiltriPage.applyLayout(layoutFiltri);
	}

}
