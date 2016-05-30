package it.eurotn.panjea.tesoreria.rich.search;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.domain.lite.ClienteLite;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.domain.lite.FornitoreLite;
import it.eurotn.panjea.tesoreria.domain.AreaAcconto;
import it.eurotn.panjea.tesoreria.rich.bd.ITesoreriaBD;
import it.eurotn.panjea.tesoreria.util.parametriricerca.ParametriRicercaAcconti;
import it.eurotn.panjea.tesoreria.util.parametriricerca.ParametriRicercaAcconti.EStatoAcconto;
import it.eurotn.rich.search.AbstractSearchObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.richclient.command.AbstractCommand;

public class AreaAccontoSearchObject extends AbstractSearchObject {

	private static Logger logger = Logger.getLogger(AreaAccontoSearchObject.class);

	private static final String SEARCH_OBJECT_ID = "areaAccontoSearchObject";

	private ITesoreriaBD tesoreriaBD;

	public static final String PARAM_ENTITA = "paramEntita";
	public static final String PARAM_STATO_ACCONTO = "paramStatoAcconto";

	/**
	 * Costruttore.
	 */
	public AreaAccontoSearchObject() {
		super(SEARCH_OBJECT_ID);
	}

	@Override
	public List<AbstractCommand> getCustomCommands() {
		return null;
	}

	@Override
	public List<?> getData(String fieldSearch, String valueSearch) {
		Map<String, Object> parameters = searchPanel.getMapParameters();

		ParametriRicercaAcconti parametriRicercaAcconti = new ParametriRicercaAcconti();
		parametriRicercaAcconti.setStatoAcconto((EStatoAcconto) parameters.get(PARAM_STATO_ACCONTO));

		EntitaLite entita = (EntitaLite) parameters.get(PARAM_ENTITA);
		if (entita instanceof ClienteLite) {
			parametriRicercaAcconti.setTipoEntita(TipoEntita.CLIENTE);
		} else if (entita instanceof FornitoreLite) {
			parametriRicercaAcconti.setTipoEntita(TipoEntita.FORNITORE);
		}

		parametriRicercaAcconti.setEntita(entita);

		List<AreaAcconto> acconti = new ArrayList<AreaAcconto>();
		acconti = tesoreriaBD.caricaAcconti(parametriRicercaAcconti);

		logger.debug("--> Exit getData");
		return acconti;
	}

	/**
	 * @param tesoreriaBD
	 *            the tesoreriaBD to set
	 */
	public void setTesoreriaBD(ITesoreriaBD tesoreriaBD) {
		this.tesoreriaBD = tesoreriaBD;
	}

}
