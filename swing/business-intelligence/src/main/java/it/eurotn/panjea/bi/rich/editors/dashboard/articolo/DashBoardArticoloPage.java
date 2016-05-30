package it.eurotn.panjea.bi.rich.editors.dashboard.articolo;

import it.eurotn.panjea.bi.rich.editors.dashboard.DashBoardCompositePage;
import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;

import com.jidesoft.pivot.IPivotDataModel;

public class DashBoardArticoloPage extends DashBoardCompositePage implements IPageLifecycleAdvisor {

	private static final String NOME_ANALISI = "dashBoardArticoloPage";
	private Articolo articolo;

	/**
	 * Costruttore.
	 */
	public DashBoardArticoloPage() {
		super("dashboardArticoloPage");
	}

	@Override
	public void loadData() {
		if (dashBoard == null) {
			dashBoard = businessIntelligenceBD.caricaDashBoard(DashBoardArticoloPage.NOME_ANALISI);
			dashBoard.setPrivata(true);
			dashBoard.getFiltriPrivati().add("articoli_Articolo");
			openDashBoard(dashBoard);
		}
		IPivotDataModel filterModel = getFilterPivotDataModel();
		filterModel.getField("articoli_Articolo").setSelectedPossibleValues(new Object[] { articolo.getCodice() });
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
		loadData();
	}

	@Override
	public void setFormObject(Object object) {
		articolo = (Articolo) object;
	}

}
