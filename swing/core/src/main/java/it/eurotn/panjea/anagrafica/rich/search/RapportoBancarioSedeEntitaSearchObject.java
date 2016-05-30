/**
 *
 */
package it.eurotn.panjea.anagrafica.rich.search;

import it.eurotn.panjea.anagrafica.domain.RapportoBancarioSedeEntita;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaBD;
import it.eurotn.rich.search.AbstractSearchObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.richclient.command.AbstractCommand;

/**
 *
 * @author Leonardo
 * @version 1.0, 31/ago/06
 *
 */
public class RapportoBancarioSedeEntitaSearchObject extends AbstractSearchObject {

	private IAnagraficaBD anagraficaBD;

	private static final String PAGE_ID = "rapportoBancarioSedeEntitaSearchObject";

	/**
	 * Costruttore.
	 */
	public RapportoBancarioSedeEntitaSearchObject() {
		super(PAGE_ID);
	}

	@Override
	public List<AbstractCommand> getCustomCommands() {
		return null;
	};

	@Override
	public List<?> getData(String fieldSearch, String valueSearch) {
		Map<String, Object> parameters = searchPanel.getMapParameters();
		Integer idEntita = (Integer) parameters.get("idEntita");
		List<RapportoBancarioSedeEntita> list = new ArrayList<RapportoBancarioSedeEntita>();
		if (idEntita != null) {
			list = anagraficaBD.caricaRapportiBancariSedeEntitaPrincipale(fieldSearch, valueSearch, idEntita);
		} else {
			Integer idSedeEntita = (Integer) parameters.get("idSedeEntita");
			if (idSedeEntita != null) {
				list = anagraficaBD.caricaRapportiBancariSedeEntita(idSedeEntita, false);
			}
		}
		return list;
	}

	/**
	 *
	 * @param anagraficaBD
	 *            bd anagrafica.
	 */
	public void setAnagraficaBD(IAnagraficaBD anagraficaBD) {
		this.anagraficaBD = anagraficaBD;
	}

}