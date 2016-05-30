/**
 *
 */
package it.eurotn.panjea.anagrafica.rich.search;

import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaBD;
import it.eurotn.rich.search.AbstractSearchObject;

import java.util.List;

import org.springframework.richclient.command.AbstractCommand;

/**
 * Specializzazione della classe <code>AbstractSearchObject</code> responsabile di effettuare la ricerca degli articoli.
 * 
 * @author fattazzo
 */
public class CategoriaEntitaSearchObject extends AbstractSearchObject {

	private IAnagraficaBD anagraficaBD;

	/**
	 * Costruttore.
	 */
	public CategoriaEntitaSearchObject() {
		super("categoriaEntitaSearchObject");
	}

	@Override
	public List<AbstractCommand> getCustomCommands() {
		return null;
	}

	@Override
	public List<?> getData(String fieldSearch, String valueSearch) {
		return anagraficaBD.caricaCategorieEntita(fieldSearch, valueSearch);
	}

	/**
	 * @param anagraficaBD
	 *            The anagraficaBD to set.
	 */
	public void setAnagraficaBD(IAnagraficaBD anagraficaBD) {
		this.anagraficaBD = anagraficaBD;
	}

}
