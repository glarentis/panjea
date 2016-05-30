/**
 *
 */
package it.eurotn.panjea.bi.rich.editors.dashboard;

import it.eurotn.panjea.bi.domain.dashboard.DashBoardAnalisi;
import it.eurotn.panjea.bi.rich.editors.dashboard.commands.ApriAnalisiDashBoardCommand;
import it.eurotn.panjea.bi.rich.editors.dashboard.commands.AssociazioneFiltriCommand;
import it.eurotn.panjea.bi.rich.editors.dashboard.commands.CambiaVisualizzazioneCommand;
import it.eurotn.panjea.bi.rich.editors.dashboard.commands.EsportaAnalisiCommand;
import it.eurotn.panjea.bi.rich.editors.dashboard.commands.RicaricaAnalisiCommand;
import it.eurotn.panjea.bi.rich.editors.dashboard.commands.ToggleFilterDescriptionCommand;
import it.eurotn.rich.dialog.DockingCompositeDialogPage;

import com.jidesoft.docking.event.DockableFrameAdapter;
import com.jidesoft.docking.event.DockableFrameEvent;

/**
 * @author fattazzo
 *
 */
public class DashBoardAnalisiFrame extends DashBoardFrame {

	private final class ActivateFrameAdapter extends DockableFrameAdapter {

		@Override
		public void dockableFrameHidden(DockableFrameEvent paramDockableFrameEvent) {
			super.dockableFrameHidden(paramDockableFrameEvent);
			getDockingManager().removeFrame(paramDockableFrameEvent.getDockableFrame().getKey());
		}

		@Override
		public void dockableFrameMoved(DockableFrameEvent paramDockableFrameEvent) {
			super.dockableFrameMoved(paramDockableFrameEvent);
		}

		@Override
		public void dockableFrameRemoved(DockableFrameEvent paramDockableFrameEvent) {
			super.dockableFrameRemoved(paramDockableFrameEvent);
			if (!(paramDockableFrameEvent.getDockableFrame().getClientProperty(
					DockingCompositeDialogPage.PAGE_PROPERTY_NAME) instanceof DashBoardPage)) {
				return;
			}

			DashBoardPage pageToRemove = (DashBoardPage) paramDockableFrameEvent.getDockableFrame().getClientProperty(
					DockingCompositeDialogPage.PAGE_PROPERTY_NAME);
			dashBoardCompositePage.removePage(pageToRemove);
			dashBoardCompositePage.getDashBoard().removeAnalisi(pageToRemove.getAnalisi());
			dashBoardCompositePage.salva();
		}

	}

	private static final long serialVersionUID = 4480677321940309597L;

	private DashBoardPage dashBoardPage;

	private ActivateFrameAdapter activateListener;

	private DashBoardCompositePage dashBoardCompositePage;

	/**
	 * Costruttore.
	 *
	 * @param dashBoardCompositePage
	 *            dashBoardCompositePage
	 *
	 */
	public DashBoardAnalisiFrame(final DashBoardCompositePage dashBoardCompositePage) {
		super();
		this.dashBoardCompositePage = dashBoardCompositePage;
	}

	@Override
	public void dispose() {
		removeDockableFrameListener(getActivateFrameListener());
		activateListener = null;
		super.dispose();
	}

	@Override
	protected void doConfigure() {
		addDockableFrameListener(getActivateFrameListener());

		dashBoardPage = (DashBoardPage) getClientProperty(DockingCompositeDialogPage.PAGE_PROPERTY_NAME);

		setTitle(dashBoardPage.getAnalisi().getNomeAnalisi().toUpperCase());

		CambiaVisualizzazioneCommand cambiaVisualizzazioneCommand = new CambiaVisualizzazioneCommand(dashBoardPage);
		addFooterToolBarCommand(cambiaVisualizzazioneCommand);

		ApriAnalisiDashBoardCommand apriAnalisiDashBoardCommand = new ApriAnalisiDashBoardCommand(dashBoardPage);
		addFooterToolBarCommand(apriAnalisiDashBoardCommand);

		RicaricaAnalisiCommand ricaricaAnalisiCommand = new RicaricaAnalisiCommand(dashBoardPage);
		addFooterToolBarCommand(ricaricaAnalisiCommand);

		EsportaAnalisiCommand esportaAnalisiCommand = new EsportaAnalisiCommand(dashBoardPage);
		addFooterToolBarCommand(esportaAnalisiCommand);

		AssociazioneFiltriCommand associazioneFiltriCommand = new AssociazioneFiltriCommand(dashBoardPage);
		addFooterToolBarCommand(associazioneFiltriCommand);

		ToggleFilterDescriptionCommand toggleFilterDescriptionCommand = new ToggleFilterDescriptionCommand(
				dashBoardPage);
		addFooterToolBarCommand(toggleFilterDescriptionCommand);
	}

	/**
	 *
	 * @return listener per gestire la chiusura del frame
	 */
	private ActivateFrameAdapter getActivateFrameListener() {
		if (activateListener == null) {
			activateListener = new ActivateFrameAdapter();
		}
		return activateListener;
	}

	/**
	 * @return Returns the analisi.
	 */
	public DashBoardAnalisi getAnalisi() {
		return dashBoardPage.getAnalisi();
	}

}
