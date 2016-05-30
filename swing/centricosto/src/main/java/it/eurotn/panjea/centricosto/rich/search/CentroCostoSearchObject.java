package it.eurotn.panjea.centricosto.rich.search;

import it.eurotn.panjea.centricosto.domain.CentroCosto;
import it.eurotn.panjea.centricosto.rich.bd.ICentriCostoBD;
import it.eurotn.rich.search.AbstractSearchObject;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.richclient.command.AbstractCommand;

public class CentroCostoSearchObject extends AbstractSearchObject {
	// Se voglio filtrare i centri di costo (ad esempio nell'inserimento della
	// riga contabile filtro per quelli già
	// presenti)
	public static final String FILTER_LIST_KEY = "filterList_key";
	private ICentriCostoBD centriCostoBD;

	@Override
	public List<AbstractCommand> getCustomCommands() {
		return null;
	}

	@Override
	public List<CentroCosto> getData(String fieldSearch, String valueSearch) {
		Map<String, Object> parameters = searchPanel.getMapParameters();
		List<CentroCosto> result = centriCostoBD.caricaCentriCosto(valueSearch);
		if (parameters.get(FILTER_LIST_KEY) != null) {

			@SuppressWarnings("unchecked")
			Set<CentroCosto> filterCentroDiCosto = (Set<CentroCosto>) parameters.get(FILTER_LIST_KEY);
			result.removeAll(filterCentroDiCosto);
		}
		return result;
	}

	/**
	 * @param centriCostoBD
	 *            The centriCostoBD to set.
	 */
	public void setCentriCostoBD(ICentriCostoBD centriCostoBD) {
		this.centriCostoBD = centriCostoBD;
	}

}
