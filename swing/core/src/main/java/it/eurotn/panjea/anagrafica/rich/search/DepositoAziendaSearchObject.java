package it.eurotn.panjea.anagrafica.rich.search;

import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaBD;
import it.eurotn.rich.search.AbstractSearchObject;

import java.util.List;

import org.springframework.richclient.command.AbstractCommand;

public class DepositoAziendaSearchObject extends AbstractSearchObject {

	private IAnagraficaBD anagraficaBD = null;

	/**
	 * Costruttore di default.
	 */
	public DepositoAziendaSearchObject() {
		super("depositoAziendaSearchObject");
	}

	/**
	 * @return the anagraficaBD
	 */
	public IAnagraficaBD getAnagraficaBD() {
		return anagraficaBD;
	}

	@Override
	public List<AbstractCommand> getCustomCommands() {
		return null;
	}

	@Override
	public List<?> getData(String fieldSearch, String valueSearch) {
		return anagraficaBD.caricaDepositi(fieldSearch, valueSearch);
	}

	/**
	 * @param anagraficaBD
	 *            the anagraficaBD to set
	 */
	public void setAnagraficaBD(IAnagraficaBD anagraficaBD) {
		this.anagraficaBD = anagraficaBD;
	}

}
