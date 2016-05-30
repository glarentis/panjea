/**
 * 
 */
package it.eurotn.panjea.magazzino.rich.search;

import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.rich.search.AbstractSearchObject;

import java.util.List;

import org.springframework.richclient.command.AbstractCommand;

/**
 * 
 * 
 * @author adriano
 * @version 1.0, 06/nov/2008
 * 
 */
public class FormulaTrasformazioneSearchObject extends AbstractSearchObject {

	private IMagazzinoAnagraficaBD magazzinoAnagraficaBD;

	/**
	 * 
	 */
	public FormulaTrasformazioneSearchObject() {
		super("formulaTrasformazioneSearchObject");
	}

	@Override
	public List<AbstractCommand> getCustomCommands() {
		return null;
	}

	@Override
	public List<?> getData(String fieldSearch, String valueSearch) {
		return magazzinoAnagraficaBD.caricaFormuleTrasformazione(valueSearch);
	}

	/**
	 * @param magazzinoAnagraficaBD
	 *            The magazzinoAnagraficaBD to set.
	 */
	public void setMagazzinoAnagraficaBD(IMagazzinoAnagraficaBD magazzinoAnagraficaBD) {
		this.magazzinoAnagraficaBD = magazzinoAnagraficaBD;
	}

}
