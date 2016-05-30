package it.eurotn.panjea.anagrafica.rich.search;

import it.eurotn.panjea.anagrafica.domain.NotaAnagrafica;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaTabelleBD;
import it.eurotn.rich.search.AbstractSearchObject;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.richclient.command.AbstractCommand;

public class NoteAnagraficaSearchObject extends AbstractSearchObject {

	private static Logger logger = Logger.getLogger(NoteAnagraficaSearchObject.class);

	private static final String SEARCH_OBJECT_ID = "noteAnagraficaSearchObject";

	private IAnagraficaTabelleBD anagraficaTabelleBD;

	/**
	 * Costruttore.
	 * 
	 */
	public NoteAnagraficaSearchObject() {
		super(SEARCH_OBJECT_ID);
	}

	@Override
	public List<AbstractCommand> getCustomCommands() {
		return null;
	}

	@Override
	public List<?> getData(String fieldSearch, String valueSearch) {

		List<NotaAnagrafica> note = anagraficaTabelleBD.caricaNoteAnagrafica();
		logger.debug("--> Exit getData");
		return note;
	}

	/**
	 * @param anagraficaTabelleBD
	 *            the anagraficaTabelleBD to set
	 */
	public void setAnagraficaTabelleBD(IAnagraficaTabelleBD anagraficaTabelleBD) {
		this.anagraficaTabelleBD = anagraficaTabelleBD;
	}

}
