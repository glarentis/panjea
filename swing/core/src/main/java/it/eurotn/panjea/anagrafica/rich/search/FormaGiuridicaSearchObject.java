/**
 * 
 */
package it.eurotn.panjea.anagrafica.rich.search;

import it.eurotn.panjea.anagrafica.domain.FormaGiuridica;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaTabelleBD;
import it.eurotn.rich.search.AbstractSearchObject;

import java.util.List;

import org.springframework.richclient.command.AbstractCommand;

/**
 * Specializzazione della classe <code>AbstractSearchObject</code> responsabile di effettuare la ricerca delle forme
 * giuridiche.
 * 
 * @author Aracno
 * @version 1.0, 28/nov/06
 * 
 */
public class FormaGiuridicaSearchObject extends AbstractSearchObject {

	private IAnagraficaTabelleBD anagraficaTabelleBD;

	/**
	 * Costruttore.
	 */
	public FormaGiuridicaSearchObject() {
		super("formaGiuridicaSearchObject");
	}

	/**
	 * @return Returns the anagraficaTabelleBD.
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
		List<FormaGiuridica> list = anagraficaTabelleBD.caricaFormeGiuridiche(fieldSearch, valueSearch);
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
