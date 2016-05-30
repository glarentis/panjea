/**
 * 
 */
package it.eurotn.panjea.anagrafica.rich.search;

import it.eurotn.panjea.anagrafica.domain.Banca;
import it.eurotn.panjea.anagrafica.domain.Filiale;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaBD;
import it.eurotn.rich.search.AbstractSearchObject;

import java.util.List;
import java.util.Map;

import org.springframework.richclient.command.AbstractCommand;

/**
 * 
 * 
 * @author adriano
 * @version 1.0, 02/nov/06
 * 
 */
public class FilialeSearchObject extends AbstractSearchObject {

	private IAnagraficaBD anagraficaBD;
	private static final String PAGE_ID = "filialeSearchObject";

	/**
	 * Costruttore.
	 */
	public FilialeSearchObject() {
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
		Map<String, Object> parameters = searchPanel.getMapParameters();
		Banca banca = (Banca) parameters.get(Banca.REF);
		List<Filiale> list = anagraficaBD.caricaFiliali(banca, fieldSearch, valueSearch);
		return list;
	}

	/**
	 * @param anagraficaBD
	 *            The anagraficaBD to set.
	 */
	public void setAnagraficaBD(IAnagraficaBD anagraficaBD) {
		this.anagraficaBD = anagraficaBD;
	}
}
