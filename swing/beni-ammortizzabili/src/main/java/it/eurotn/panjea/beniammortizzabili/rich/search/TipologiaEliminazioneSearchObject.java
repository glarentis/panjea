/**
 * 
 */
package it.eurotn.panjea.beniammortizzabili.rich.search;

import it.eurotn.panjea.beniammortizzabili.rich.bd.IBeniAmmortizzabiliBD;
import it.eurotn.panjea.beniammortizzabili2.domain.TipologiaEliminazione;
import it.eurotn.rich.search.AbstractSearchObject;

import java.util.List;

import org.springframework.richclient.command.AbstractCommand;

/**
 * @author Leonardo
 * 
 */
public class TipologiaEliminazioneSearchObject extends AbstractSearchObject {

	private IBeniAmmortizzabiliBD beniAmmortizzabiliBD;

	/**
	 * Costruttore.
	 */
	public TipologiaEliminazioneSearchObject() {
		super("tipologiaEliminazioneSearchObject");
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
		List<TipologiaEliminazione> list = beniAmmortizzabiliBD.caricaTipologieEliminazione(valueSearch);
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
