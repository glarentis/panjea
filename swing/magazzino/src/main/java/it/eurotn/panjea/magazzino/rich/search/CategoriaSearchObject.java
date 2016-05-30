/**
 * 
 */
package it.eurotn.panjea.magazzino.rich.search;

import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.rich.search.AbstractSearchObject;

import java.util.List;

import org.springframework.richclient.command.AbstractCommand;

/**
 * Specializzazione della classe <code>AbstractSearchObject</code> responsabile di effettuare la ricerca degli articoli.
 * 
 * @author fattazzo
 */
public class CategoriaSearchObject extends AbstractSearchObject {

	private IMagazzinoAnagraficaBD magazzinoAnagraficaBD;

	/**
	 * Costruttore.
	 */
	public CategoriaSearchObject() {
		super("categoriaSearchObject");
	}

	@Override
	public List<AbstractCommand> getCustomCommands() {
		return null;
	}

	@Override
	public List<?> getData(String fieldSearch, String valueSearch) {
		return magazzinoAnagraficaBD.caricaCategorieCodiceDescrizione(fieldSearch, valueSearch);
	}

	/**
	 * @param magazzinoAnagraficaBD
	 *            The magazzinoAnagraficaBD to set.
	 */
	public void setMagazzinoAnagraficaBD(IMagazzinoAnagraficaBD magazzinoAnagraficaBD) {
		this.magazzinoAnagraficaBD = magazzinoAnagraficaBD;
	}
}
