/**
 * 
 */
package it.eurotn.panjea.beniammortizzabili.rich.search;

import it.eurotn.panjea.beniammortizzabili.rich.bd.IBeniAmmortizzabiliBD;
import it.eurotn.panjea.beniammortizzabili2.domain.Ubicazione;
import it.eurotn.rich.search.AbstractSearchObject;

import java.util.List;

import org.springframework.richclient.command.AbstractCommand;

/**
 * 
 * @author adriano
 * @version 1.0, 06/nov/06
 * 
 */
public class UbicazioneSearchObject extends AbstractSearchObject {

	private IBeniAmmortizzabiliBD beniAmmortizzabiliBD;

	/**
	 * Costruttore.
	 */
	public UbicazioneSearchObject() {
		super("ubicazioneSearchObject");
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
		List<Ubicazione> list = beniAmmortizzabiliBD.caricaUBicazioni(valueSearch);
		return list;
	}

	/**
	 * @param beniAmmortizzabiliBD
	 *            The beniAmmortizzabiliBD to set.
	 */
	public void setBeniAmmortizzabiliBD(IBeniAmmortizzabiliBD beniAmmortizzabiliBD) {
		this.beniAmmortizzabiliBD = beniAmmortizzabiliBD;
	}

}
