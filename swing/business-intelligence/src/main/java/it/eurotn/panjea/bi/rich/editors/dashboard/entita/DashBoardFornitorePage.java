package it.eurotn.panjea.bi.rich.editors.dashboard.entita;

import it.eurotn.panjea.anagrafica.domain.Fornitore;
import it.eurotn.panjea.bi.rich.editors.dashboard.DashBoardCompositePage;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;

import com.jidesoft.pivot.IPivotDataModel;

public class DashBoardFornitorePage extends DashBoardCompositePage implements IPageLifecycleAdvisor {

	private static final String NOME_ANALISI = "DashBoardFornitorePage";
	private Fornitore fornitore;

	/**
	 * Costruttore.
	 */
	public DashBoardFornitorePage() {
		super("dashboardFornitorePage");
	}

	@Override
	public void loadData() {
		if (dashBoard == null) {
			dashBoard = businessIntelligenceBD.caricaDashBoard(DashBoardFornitorePage.NOME_ANALISI);
			dashBoard.setPrivata(true);
			dashBoard.getFiltriPrivati().add("fornitori_Cod_Forn_");
			openDashBoard(dashBoard);
		}
		IPivotDataModel filterModel = getFilterPivotDataModel();
		filterModel.getField("fornitori_Cod_Forn_").setSelectedPossibleValues(new Object[] { fornitore.getCodice() });
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
		fornitore = (Fornitore) object;
	}

}
