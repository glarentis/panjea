package it.eurotn.panjea.bi.rich.editors.dashboard.entita;

import it.eurotn.panjea.anagrafica.domain.Cliente;
import it.eurotn.panjea.bi.rich.editors.dashboard.DashBoardCompositePage;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;

import com.jidesoft.pivot.IPivotDataModel;

public class DashBoardClientePage extends DashBoardCompositePage implements IPageLifecycleAdvisor {

	private static final String NOME_ANALISI = "DashboardClientePage";
	private Cliente cliente;

	/**
	 * Costruttore.
	 */
	public DashBoardClientePage() {
		super("dashboardClientePage");
	}

	@Override
	public void loadData() {
		if (dashBoard == null) {
			dashBoard = businessIntelligenceBD.caricaDashBoard(DashBoardClientePage.NOME_ANALISI);
			dashBoard.setPrivata(true);
			dashBoard.getFiltriPrivati().add("clienti_Cod_Cliente");
			openDashBoard(dashBoard);
		}
		IPivotDataModel filterModel = getFilterPivotDataModel();
		filterModel.getField("clienti_Cod_Cliente").setSelectedPossibleValues(new Object[] { cliente.getCodice() });
		setFilterPivotDataModel(filterModel);
		ricaricaDatiDashBoard();
	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	@Override
	public void postSetFormObject(Object object) {
	}

	@Override
	public void preSetFormObject(Object object) {
	}

	@Override
	public void refreshData() {
	}

	@Override
	public void setFormObject(Object object) {
		cliente = (Cliente) object;
	}

}
