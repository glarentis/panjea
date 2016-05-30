/**
 * 
 */
package it.eurotn.panjea.anagrafica.rich.search;

import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaBD;
import it.eurotn.rich.search.AbstractSearchObject;

import java.util.List;

import org.springframework.richclient.command.AbstractCommand;

/**
 * @author fattazzo
 * 
 */
public class AnagraficaLiteSearchObject extends AbstractSearchObject {

	public static final String SEARCH_ID = "anagraficaLiteSearchObject";

	private IAnagraficaBD anagraficaBD;

	/**
	 * Costruttore.
	 */
	public AnagraficaLiteSearchObject() {
		super(SEARCH_ID);
	}

	@Override
	public List<AbstractCommand> getCustomCommands() {
		return null;
	}

	@Override
	public List<?> getData(String fieldSearch, String valueSearch) {

		String codice = null;
		String denominazione = null;
		if (fieldSearch.equals("codice")) {
			codice = valueSearch;
		}
		if (fieldSearch.equals("denominazione")) {
			denominazione = valueSearch;
		}
		return anagraficaBD.caricaAnagraficheSearchObject(codice, denominazione);
	}

	/**
	 * @param anagraficaBD
	 *            the anagraficaBD to set
	 */
	public void setAnagraficaBD(IAnagraficaBD anagraficaBD) {
		this.anagraficaBD = anagraficaBD;
	}

}
