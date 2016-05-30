package it.eurotn.panjea.bi.rich.editors.dashboard.entita;

import it.eurotn.panjea.agenti.domain.Agente;
import it.eurotn.panjea.bi.rich.editors.dashboard.DashBoardCompositePage;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;

import com.jidesoft.pivot.IPivotDataModel;

public class DashBoardAgentePage extends DashBoardCompositePage implements IPageLifecycleAdvisor {

	private static final String NOME_ANALISI = "DashBoardAgentePage";
	private Agente agente;

	/**
	 * Costruttore.
	 */
	public DashBoardAgentePage() {
		super("dashboardAgentePage");
	}

	@Override
	public void loadData() {
		if (dashBoard == null) {
			dashBoard = businessIntelligenceBD.caricaDashBoard(DashBoardAgentePage.NOME_ANALISI);
			dashBoard.setPrivata(true);
			dashBoard.getFiltriPrivati().add("agenti_Cod_Agente");
			openDashBoard(dashBoard);
		}
		IPivotDataModel filterModel = getFilterPivotDataModel();
		filterModel.getField("agenti_Cod_Agente").setSelectedPossibleValues(new Object[] { agente.getCodice() });
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
		agente = (Agente) object;
	}

}
