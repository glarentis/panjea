/**
 * 
 */
package it.eurotn.panjea.anagrafica.rich.search;

import it.eurotn.panjea.anagrafica.domain.Cliente;
import it.eurotn.panjea.anagrafica.domain.Contatto;
import it.eurotn.panjea.anagrafica.domain.ContattoSedeEntita;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaBD;
import it.eurotn.rich.search.AbstractSearchObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.richclient.command.AbstractCommand;

/**
 * @author Leonardo
 * 
 */
public class ContattoSearchObject extends AbstractSearchObject {

	private IAnagraficaBD anagraficaBD;

	private static final String SEARCH_ID = "contattoSearchObject";

	/**
	 * 
	 */
	public ContattoSearchObject() {
		super(SEARCH_ID);
	}

	/**
	 * @return the anagraficaBD
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
		Integer idEntita = (Integer) parameters.get("entitaId");
		Integer idSedeEntita = (Integer) parameters.get("sedeEntitaId");

		// posso creare cmq sia un cliente dato che nel metodo non viene
		// controllata l'entita' ma i parametri passati e
		// l'id della stessa
		Cliente cfake = new Cliente();
		cfake.setId(idEntita);
		List<Contatto> contatti = new ArrayList<Contatto>();
		// carica i contatti dell'entita'
		if (idEntita != null && idSedeEntita == null) {
			contatti = anagraficaBD.caricaContattiPerEntita(cfake);
		} else if (idEntita != null && idSedeEntita != null) { // o carica i
																// contatti
																// della sede
																// entita'
			SedeEntita sefake = new SedeEntita();
			sefake.setId(idSedeEntita);
			List<ContattoSedeEntita> contattiSedeEntita = anagraficaBD.caricaContattiSedeEntitaPerSedeEntita(sefake);
			for (ContattoSedeEntita contattoSedeEntita : contattiSedeEntita) {
				contatti.add(contattoSedeEntita.getContatto());
			}
		}
		return contatti;
	}

	/**
	 * @param anagraficaBD
	 *            the anagraficaBD to set
	 */
	public void setAnagraficaBD(IAnagraficaBD anagraficaBD) {
		this.anagraficaBD = anagraficaBD;
	}

}
