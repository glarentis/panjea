package it.eurotn.panjea.anagrafica.rich.search.datigeografici;

import it.eurotn.panjea.anagrafica.rich.bd.IDatiGeograficiBD;
import it.eurotn.rich.search.AbstractSearchObject;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.richclient.command.AbstractCommand;

public class NazioneSearchObject extends AbstractSearchObject {

	private static Logger logger = Logger.getLogger(NazioneSearchObject.class);
	private IDatiGeograficiBD datiGeograficiBD;

	@Override
	public List<AbstractCommand> getCustomCommands() {
		return null;
	}

	@Override
	public List<?> getData(String fieldSearch, String valueSearch) {
		return datiGeograficiBD.caricaNazioni(valueSearch);
	}

	/**
	 * @param datiGeograficiBD
	 *            the datiGeograficiBD to set
	 */
	public void setDatiGeograficiBD(IDatiGeograficiBD datiGeograficiBD) {
		this.datiGeograficiBD = datiGeograficiBD;
	}

}
