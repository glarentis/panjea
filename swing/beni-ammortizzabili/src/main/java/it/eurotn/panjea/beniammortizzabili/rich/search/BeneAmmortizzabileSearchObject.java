/**
 * 
 */
package it.eurotn.panjea.beniammortizzabili.rich.search;

import it.eurotn.panjea.beniammortizzabili.rich.bd.IBeniAmmortizzabiliBD;
import it.eurotn.rich.search.AbstractSearchObject;

import java.util.List;

import org.springframework.richclient.command.AbstractCommand;

/**
 * 
 * @author adriano
 * @version 1.0, 16/nov/06
 * 
 */
public class BeneAmmortizzabileSearchObject extends AbstractSearchObject {

	private IBeniAmmortizzabiliBD beniAmmortizzabiliBD;

	/**
	 * Costruttore.
	 */
	public BeneAmmortizzabileSearchObject() {
		super("beneAmmortizzabileSearchObject");
	}

	/**
	 * @return Returns the beniAmmortizzabiliBD.
	 */
	public IBeniAmmortizzabiliBD getBeniAmmortizzabiliBD() {
		return beniAmmortizzabiliBD;
	}

	@Override
	public List<AbstractCommand> getCustomCommands() {
		return null;
	}

	@Override
	public List<?> getData(String fieldSearch, String valueSearch) {
		return beniAmmortizzabiliBD.ricercaBeniAmmortizzabili(fieldSearch, valueSearch);
	}

	/**
	 * @param beniAmmortizzabiliBD
	 *            The beniAmmortizzabiliBD to set.
	 */
	public void setBeniAmmortizzabiliBD(IBeniAmmortizzabiliBD beniAmmortizzabiliBD) {
		this.beniAmmortizzabiliBD = beniAmmortizzabiliBD;
	}

}
