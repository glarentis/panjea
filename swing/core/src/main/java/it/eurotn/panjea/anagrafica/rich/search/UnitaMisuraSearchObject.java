/**
 * 
 */
package it.eurotn.panjea.anagrafica.rich.search;

import it.eurotn.panjea.anagrafica.domain.UnitaMisura;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaTabelleBD;
import it.eurotn.rich.search.AbstractSearchObject;

import java.util.List;

import org.springframework.richclient.command.AbstractCommand;

/**
 * SearchObject per la classe di dominio {@link UnitaMisura} .
 * 
 * @author adriano
 * @version 1.0, 02/mag/08
 * 
 */
public class UnitaMisuraSearchObject extends AbstractSearchObject {

	private IAnagraficaTabelleBD anagraficaTabelleBD;

	private static final String SEARCH_OBJECT_ID = "unitaMisuraSearchObject";

	/**
	 * Costruttore.
	 */
	public UnitaMisuraSearchObject() {
		super(SEARCH_OBJECT_ID);

	}

	@Override
	public List<AbstractCommand> getCustomCommands() {
		return null;
	}

	@Override
	public List<?> getData(String fieldSearch, String valueSearch) {
		return anagraficaTabelleBD.caricaUnitaMisura(valueSearch);
	}

	/**
	 * @param anagraficaTabelleBD
	 *            The anagraficaTabelleBD to set.
	 */
	public void setAnagraficaTabelleBD(IAnagraficaTabelleBD anagraficaTabelleBD) {
		this.anagraficaTabelleBD = anagraficaTabelleBD;
	}

}
