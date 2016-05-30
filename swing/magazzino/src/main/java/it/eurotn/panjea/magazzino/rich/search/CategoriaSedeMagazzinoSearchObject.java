/**
 * 
 */
package it.eurotn.panjea.magazzino.rich.search;

import it.eurotn.panjea.magazzino.domain.CategoriaSedeMagazzino;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.rich.search.AbstractSearchObject;

import java.util.List;

import org.springframework.richclient.command.AbstractCommand;

/**
 * SearchObject per l'oggetto di dominio {@link CategoriaSedeMagazzino}.
 * 
 * @author fattazzo
 * 
 */
public class CategoriaSedeMagazzinoSearchObject extends AbstractSearchObject {

	private static final String SEARCH_OBJECT_ID = "categoriaSedeMagazzinoSearchObject";

	private IMagazzinoAnagraficaBD magazzinoAnagraficaBD;

	/**
	 * Costruttore.
	 */
	public CategoriaSedeMagazzinoSearchObject() {
		super(SEARCH_OBJECT_ID);
	}

	@Override
	public List<AbstractCommand> getCustomCommands() {
		return null;
	}

	@Override
	public List<?> getData(String fieldSearch, String valueSearch) {
		List<CategoriaSedeMagazzino> list = magazzinoAnagraficaBD
				.caricaCategorieSediMagazzino(fieldSearch, valueSearch);
		return list;
	}

	/**
	 * @param magazzinoAnagraficaBD
	 *            the magazzinoAnagraficaBD to set
	 */
	public void setMagazzinoAnagraficaBD(IMagazzinoAnagraficaBD magazzinoAnagraficaBD) {
		this.magazzinoAnagraficaBD = magazzinoAnagraficaBD;
	}
}
