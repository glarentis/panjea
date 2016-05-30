/**
 * 
 */
package it.eurotn.panjea.anagrafica.rich.search;

import it.eurotn.panjea.anagrafica.domain.TipoSedeEntita;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaTabelleBD;
import it.eurotn.rich.search.AbstractSearchObject;

import java.util.List;

import org.springframework.richclient.command.AbstractCommand;

/**
 * SearchObject per l'oggetto di domino {@link TipoSedeEntita}.
 * 
 * @author adriano
 * @version 1.0, 02/nov/06
 * 
 */
public class TipoSedeEntitaSearchObject extends AbstractSearchObject {

	private IAnagraficaTabelleBD anagraficaTabelleBD;

	private static final String PAGE_ID = "tipoSedeEntitaSearchObject";

	/**
	 * Costruttore.
	 */
	public TipoSedeEntitaSearchObject() {
		super(PAGE_ID);
	}

	/**
	 * @return Returns the anagraficaBD.
	 */
	public IAnagraficaTabelleBD getAnagraficaTabelleBD() {
		return anagraficaTabelleBD;
	}

	@Override
	public List<AbstractCommand> getCustomCommands() {
		return null;
	}

	@Override
	public List<?> getData(String fieldSearch, String valueSearch) {
		List<TipoSedeEntita> list = anagraficaTabelleBD.caricaTipiSede(valueSearch);
		return list;
	}

	/**
	 * @param anagraficaTabelleBD
	 *            The anagraficaTabelleBD to set.
	 */
	public void setAnagraficaTabelleBD(IAnagraficaTabelleBD anagraficaTabelleBD) {
		this.anagraficaTabelleBD = anagraficaTabelleBD;
	}

}
