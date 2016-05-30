/**
 *
 */
package it.eurotn.panjea.bi.rich.editors.dashboard.filtri.commands;

import it.eurotn.panjea.bi.rich.editors.dashboard.filtri.DashboardFiltriPage;

import java.util.ArrayList;

import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.dialog.ConfirmationDialog;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.pivot.PivotField;

/**
 * @author fattazzo
 *
 */
public class CancellaFiltriDashBoardCommand extends ActionCommand {

	public static final String COMMAND_ID = "cancellaFiltriDashBoardCommand";

	private DashboardFiltriPage dashboardFiltriPage;

	/**
	 * Costruttore.
	 *
	 * @param dashboardFiltriPage
	 *            dashboard dei filtri
	 */
	public CancellaFiltriDashBoardCommand(final DashboardFiltriPage dashboardFiltriPage) {
		super(COMMAND_ID);
		RcpSupport.configure(this);

		this.dashboardFiltriPage = dashboardFiltriPage;
	}

	@Override
	protected void doExecuteCommand() {
		ConfirmationDialog dialog = new ConfirmationDialog("Conferma eliminazione filtri",
				"Confermi il reset di tutti i filtri ?") {

			@Override
			protected void onConfirm() {
				dashboardFiltriPage.setFilter(new ArrayList<PivotField>());
			}
		};
		dialog.showDialog();
	}

}
