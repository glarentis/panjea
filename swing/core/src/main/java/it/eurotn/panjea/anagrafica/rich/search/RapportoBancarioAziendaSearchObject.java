/**
 * 
 */
package it.eurotn.panjea.anagrafica.rich.search;

import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaBD;
import it.eurotn.rich.search.AbstractSearchObject;

import java.util.List;

import org.springframework.richclient.command.AbstractCommand;

/**
 * 
 * @author Leonardo
 * @version 1.0, 31/ago/06
 * 
 */
public class RapportoBancarioAziendaSearchObject extends AbstractSearchObject {

	private IAnagraficaBD anagraficaBD;

	private static final String PAGE_ID = "rapportoBancarioAziendaSearchObject";

	/**
	 * Costruttore.
	 */
	public RapportoBancarioAziendaSearchObject() {
		super(PAGE_ID);
	}

	@Override
	public List<AbstractCommand> getCustomCommands() {
		return null;
	}

	@Override
	public List<?> getData(String fieldSearch, String valueSearch) {
		return anagraficaBD.caricaRapportiBancariAzienda(fieldSearch, valueSearch);
	}

	/**
	 * @param anagraficaBD
	 *            The anagraficaBD to set.
	 */
	public void setAnagraficaBD(IAnagraficaBD anagraficaBD) {
		this.anagraficaBD = anagraficaBD;
	}

}