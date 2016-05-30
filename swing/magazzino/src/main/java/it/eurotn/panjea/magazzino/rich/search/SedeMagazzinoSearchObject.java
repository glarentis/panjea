/**
 * 
 */
package it.eurotn.panjea.magazzino.rich.search;

import it.eurotn.panjea.magazzino.domain.SedeMagazzino;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.rich.search.AbstractSearchObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.richclient.command.AbstractCommand;

/**
 * SearchObject per la classe di dominio {@link SedeMagazzino}.
 * 
 * @author adriano
 * @version 1.0, 17/ott/2008
 * 
 */
public class SedeMagazzinoSearchObject extends AbstractSearchObject {

	private IMagazzinoAnagraficaBD magazzinoAnagraficaBD;

	public static final String PARAMETER_ENTITA_ID = "sedeEntita.entita.id";
	public static final String PARAMETER_TIPO_SEDE = "sedeEntita.tipoSede";
	public static final String PARAMETER_EREDITA_DATI_COMMERCIALI = "sedeEntita.ereditaDatiCommerciali";

	private static final String SEARCH_OBJECT_ID = "sedeMagazzinoSearchObject";

	/**
	 * 
	 */
	public SedeMagazzinoSearchObject() {
		super(SEARCH_OBJECT_ID);
	}

	@Override
	public List<AbstractCommand> getCustomCommands() {
		return null;
	}

	@Override
	public List<?> getData(String fieldSearch, String valueSearch) {
		Map<String, Object> parameters = searchPanel.getMapParameters();
		parameters.put(fieldSearch, valueSearch);
		List<SedeMagazzino> sedi = new ArrayList<SedeMagazzino>();
		sedi = magazzinoAnagraficaBD.caricaSediMagazzino(parameters, true);
		return sedi;
	}

	/**
	 * @param magazzinoAnagraficaBD
	 *            The magazzinoAnagraficaBD to set.
	 */
	public void setMagazzinoAnagraficaBD(IMagazzinoAnagraficaBD magazzinoAnagraficaBD) {
		this.magazzinoAnagraficaBD = magazzinoAnagraficaBD;
	}

}
