/**
 * 
 */
package it.eurotn.panjea.anagrafica.rich.search;

import it.eurotn.panjea.anagrafica.domain.lite.AziendaLite;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaBD;
import it.eurotn.rich.search.AbstractSearchObject;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.richclient.command.AbstractCommand;

/**
 * 
 * @author Leonardo
 * @version 1.0, 27/ott/06
 * 
 */
public class AziendaLiteSearchObject extends AbstractSearchObject {

	private static Logger logger = Logger.getLogger(AziendaLiteSearchObject.class);

	private IAnagraficaBD anagraficaBD;

	private static final String PAGE_ID = "aziendaLiteSearchObject";

	/**
	 * Costruttore.
	 * 
	 */
	public AziendaLiteSearchObject() {
		super(PAGE_ID);
	}

	/**
	 * @return Returns the anagraficaBD.
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
		logger.debug("--> Enter getData");
		List<AziendaLite> list = anagraficaBD.caricaAziende();
		logger.debug("--> Exit getData #AziendaLite " + list.size());
		return list;
	};

	/**
	 * @param anagraficaBD
	 *            The anagraficaBD to set.
	 */
	public void setAnagraficaBD(IAnagraficaBD anagraficaBD) {
		this.anagraficaBD = anagraficaBD;
	}

}