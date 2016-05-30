/**
 *
 */
package it.eurotn.panjea.bi.rich.editors.dashboard.filtri;

import it.eurotn.panjea.bi.domain.dashboard.DashBoard.LayoutFiltri;
import it.eurotn.panjea.bi.rich.editors.dashboard.DashBoardFrame;
import it.eurotn.panjea.bi.rich.editors.dashboard.filtri.commands.CancellaFiltriDashBoardCommand;
import it.eurotn.panjea.bi.rich.editors.dashboard.filtri.commands.LayoutFiltriCommand;
import it.eurotn.panjea.bi.rich.editors.dashboard.filtri.commands.ModificaFiltriDashBoardCommand;
import it.eurotn.rich.dialog.DockingCompositeDialogPage;

import java.awt.Font;

import javax.swing.AbstractButton;
import javax.swing.JComponent;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.util.RcpSupport;

/**
 * @author fattazzo
 *
 */
public class DashBoardFiltriFrame extends DashBoardFrame {

	private class ApplyFilterCommand extends ActionCommand {

		/**
		 * Costruttore.
		 */
		public ApplyFilterCommand() {
			super("applyFilterCommand");
			RcpSupport.configure(this);
		}

		@Override
		protected void doExecuteCommand() {

			dashBoardFiltriPage.applyFilter();
		}

		@Override
		protected void onButtonAttached(AbstractButton button) {
			super.onButtonAttached(button);
			Font font = button.getFont();
			button.setFont(new Font(font.getName(), font.getStyle(), 11));
		}

	}

	private static final long serialVersionUID = 4527451870392973327L;

	private DashboardFiltriPage dashBoardFiltriPage;

	private ModificaFiltriDashBoardCommand modificaFiltriDashBoardCommand;
	private CancellaFiltriDashBoardCommand cancellaFiltriDashBoardCommand;

	@Override
	protected void doConfigure() {

		dashBoardFiltriPage = (DashboardFiltriPage) getClientProperty(DockingCompositeDialogPage.PAGE_PROPERTY_NAME);

		setTitle("FILTRI");

		LayoutFiltriCommand fillCommand = new LayoutFiltriCommand(dashBoardFiltriPage, LayoutFiltri.FILL);
		LayoutFiltriCommand orizzontaleCommand = new LayoutFiltriCommand(dashBoardFiltriPage, LayoutFiltri.ORIZZONTALE);
		LayoutFiltriCommand verticaleCommand = new LayoutFiltriCommand(dashBoardFiltriPage, LayoutFiltri.VERTICALE);
		addFooterToolBarCommand(fillCommand);
		addFooterToolBarCommand(orizzontaleCommand);
		addFooterToolBarCommand(verticaleCommand);

		modificaFiltriDashBoardCommand = new ModificaFiltriDashBoardCommand(dashBoardFiltriPage);
		addFooterToolBarCommand(modificaFiltriDashBoardCommand);

		cancellaFiltriDashBoardCommand = new CancellaFiltriDashBoardCommand(dashBoardFiltriPage);
		addFooterToolBarCommand(cancellaFiltriDashBoardCommand);
	}

	@Override
	public JComponent getHeaderComponent() {
		return new ApplyFilterCommand().createButton();
	}

	/**
	 * @return layout applicato ai filtri
	 */
	public LayoutFiltri getLayoutFiltri() {
		LayoutFiltri layoutFiltri = (LayoutFiltri) ObjectUtils.defaultIfNull(dashBoardFiltriPage.getLayoutFiltri(),
				LayoutFiltri.FILL);

		return layoutFiltri;
	}

	@Override
	protected void setReadonly(boolean readonly) {
		// le toolbar vengono sempre lasciate visibli
		closeFrameButton.setVisible(!readonly);
	}
}
