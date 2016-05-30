/**
 * 
 */
package it.eurotn.panjea.anagrafica.rich.search;

import it.eurotn.panjea.anagrafica.domain.Mansione;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaTabelleBD;
import it.eurotn.rich.search.AbstractSearchObject;

import java.util.List;

import org.springframework.richclient.command.AbstractCommand;

/**
 * Classe specializzata per il recupero delle mansioni per la loro ricerca .
 * 
 * @author adriano
 * @version 1.0, 06/nov/06
 * 
 */
public class MansioneSearchObject extends AbstractSearchObject {

	private IAnagraficaTabelleBD anagraficaTabelleBD;

	/**
	 * Costruttore.
	 */
	public MansioneSearchObject() {
		super("mansioneSearchObject");
	}

	/**
	 * @return Returns the anagraficaTabelleBD.
	 */
	public IAnagraficaTabelleBD getAnagraficaTabelleBD() {
		return anagraficaTabelleBD;
	}

	@Override
	public List<AbstractCommand> getCustomCommands() {
		return null;
	}

	@Override
	public List<?> getData(String fieldSearch, String valueSearch) {
		List<Mansione> list = anagraficaTabelleBD.caricaMansioni(valueSearch);
		return list;
	}

	/**
	 * @param anagraficaTabelleBD
	 *            The anagraficaTabelleBD to set.
	 */
	public void setAnagraficaTabelleBD(IAnagraficaTabelleBD anagraficaTabelleBD) {
		this.anagraficaTabelleBD = anagraficaTabelleBD;
	}

}
