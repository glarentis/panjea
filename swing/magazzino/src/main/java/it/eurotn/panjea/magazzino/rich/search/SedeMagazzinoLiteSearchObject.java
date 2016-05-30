package it.eurotn.panjea.magazzino.rich.search;

import it.eurotn.panjea.magazzino.domain.SedeMagazzinoLite;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.rich.search.AbstractSearchObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.richclient.command.AbstractCommand;

public class SedeMagazzinoLiteSearchObject extends AbstractSearchObject {

	private static final String SEARCH_OBJECT_ID = "sedeMagazzinoLiteSearchObject";
	public static final String PARAM_SEDI_RIFATTURAZIONE = "paramSediRifatturazione";

	private IMagazzinoAnagraficaBD magazzinoAnagraficaBD;

	/**
	 * Costruttore.
	 */
	public SedeMagazzinoLiteSearchObject() {
		super(SEARCH_OBJECT_ID);
	}

	@Override
	public List<AbstractCommand> getCustomCommands() {
		return null;
	}

	@Override
	public List<?> getData(String fieldSearch, String valueSearch) {
		Map<String, Object> parameters = searchPanel.getMapParameters();

		List<SedeMagazzinoLite> listSedi = new ArrayList<SedeMagazzinoLite>();

		if (parameters.containsKey(PARAM_SEDI_RIFATTURAZIONE)) {
			listSedi = magazzinoAnagraficaBD.caricaSediMagazzinoDiRifatturazione();
		}

		return listSedi;
	}

	/**
	 * @param magazzinoAnagraficaBD
	 *            the magazzinoAnagraficaBD to set
	 */
	public void setMagazzinoAnagraficaBD(IMagazzinoAnagraficaBD magazzinoAnagraficaBD) {
		this.magazzinoAnagraficaBD = magazzinoAnagraficaBD;
	}

}
