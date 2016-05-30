/**
 * 
 */
package it.eurotn.panjea.beniammortizzabili.rich.search;

import it.eurotn.panjea.beniammortizzabili.rich.bd.IBeniAmmortizzabiliBD;
import it.eurotn.panjea.beniammortizzabili2.domain.Gruppo;
import it.eurotn.rich.search.AbstractSearchObject;

import java.util.List;

import org.springframework.richclient.command.AbstractCommand;

/**
 * 
 * @author adriano
 * @version 1.0, 02/dic/06
 * 
 */
public class GruppoSearchObject extends AbstractSearchObject {

	private IBeniAmmortizzabiliBD beniAmmortizzabiliBD;

	/**
	 * Costruttore.
	 * 
	 */
	public GruppoSearchObject() {
		super("gruppoSearchObject");
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
		List<Gruppo> list = beniAmmortizzabiliBD.caricaGruppi(fieldSearch, valueSearch);
		return list;
	};

	/**
	 * @param beniAmmortizzabiliBD
	 *            The beniAmmortizzabiliBD to set.
	 */
	public void setBeniAmmortizzabiliBD(IBeniAmmortizzabiliBD beniAmmortizzabiliBD) {
		this.beniAmmortizzabiliBD = beniAmmortizzabiliBD;
	}

}
