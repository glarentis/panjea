/**
 * 
 */
package it.eurotn.panjea.anagrafica.rich.search;

import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaTabelleBD;
import it.eurotn.rich.search.AbstractSearchObject;

import java.util.List;

import org.springframework.richclient.command.AbstractCommand;

/**
 * @author Leonardo
 */
public class ZonaGeograficaSearchObject extends AbstractSearchObject {

	private static final String SEARCH_OBJECT_ID = "zonaGeograficaSearchObject";

	private IAnagraficaTabelleBD anagraficaTabelleBD;

	/**
	 * Costruttore di default.
	 */
	public ZonaGeograficaSearchObject() {
		super(SEARCH_OBJECT_ID);
	}

	@Override
	public List<AbstractCommand> getCustomCommands() {
		return null;
	}

	@Override
	public List<?> getData(String fieldSearch, String valueSearch) {
		return anagraficaTabelleBD.caricaZoneGeografiche(fieldSearch, valueSearch);
	}

	/**
	 * @param anagraficaTabelleBD
	 *            the anagraficaTabelleBD to set
	 */
	public void setAnagraficaTabelleBD(IAnagraficaTabelleBD anagraficaTabelleBD) {
		this.anagraficaTabelleBD = anagraficaTabelleBD;
	}

}
