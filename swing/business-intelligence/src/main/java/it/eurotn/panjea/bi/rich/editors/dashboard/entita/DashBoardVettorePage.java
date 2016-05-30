package it.eurotn.panjea.bi.rich.editors.dashboard.entita;

import it.eurotn.panjea.anagrafica.domain.Vettore;
import it.eurotn.panjea.bi.rich.editors.dashboard.DashBoardCompositePage;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;

import com.jidesoft.pivot.IPivotDataModel;

public class DashBoardVettorePage extends DashBoardCompositePage implements IPageLifecycleAdvisor {

	private static final String NOME_ANALISI = "DashBoardVettorePage";
	private Vettore vettore;

	/**
	 * Costruttore.
	 */
	public DashBoardVettorePage() {
		super("dashboardVettorePage");
	}

	@Override
	public void loadData() {
		if (dashBoard == null) {
			dashBoard = businessIntelligenceBD.caricaDashBoard(DashBoardVettorePage.NOME_ANALISI);
			dashBoard.setPrivata(true);
			dashBoard.getFiltriPrivati().add("vettori_Cod__vett_");
			openDashBoard(dashBoard);
		}
		IPivotDataModel filterModel = getFilterPivotDataModel();
		filterModel.getField("vettori_Cod__vett_").setSelectedPossibleValues(new Object[] { vettore.getCodice() });
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
		vettore = (Vettore) object;
	}
}
