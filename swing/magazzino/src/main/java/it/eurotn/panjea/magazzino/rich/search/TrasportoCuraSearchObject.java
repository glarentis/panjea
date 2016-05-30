package it.eurotn.panjea.magazzino.rich.search;

import it.eurotn.panjea.magazzino.domain.TrasportoCura;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.rich.search.AbstractSearchObject;

import java.util.List;

import org.springframework.richclient.command.AbstractCommand;

import com.jidesoft.converter.ObjectConverterManager;

public class TrasportoCuraSearchObject extends AbstractSearchObject {
	private IMagazzinoAnagraficaBD magazzinoAnagraficaBD;

	/**
	 * Costruttore.
	 */
	public TrasportoCuraSearchObject() {
		super();
	}

	@Override
	public List<AbstractCommand> getCustomCommands() {
		return null;
	}

	@Override
	public List<?> getData(String fieldSearch, String valueSearch) {
		return magazzinoAnagraficaBD.caricaTrasportiCura(valueSearch);
	}

	/**
	 * 
	 * @return bd.
	 */
	public IMagazzinoAnagraficaBD getMagazzinoAnagraficaBD() {
		return magazzinoAnagraficaBD;
	}

	/**
	 * Restituisce l'oggetto TrasportoCura corrispondente all'oggetto contenuto nel Model. Se il Model contiene solo una
	 * stringa, genero il TrasportoCura tramite converter.
	 * 
	 * @param object
	 *            oggetto contenuto nel formModel
	 * @return TrasportoCura reale
	 */
	private TrasportoCura getTrasportoCura(Object object) {

		if (object instanceof TrasportoCura) {
			return (TrasportoCura) object;
		}

		if (object instanceof String) {
			return (TrasportoCura) ObjectConverterManager.fromString((String) object, TrasportoCura.class);
		}

		return null;
	}

	@Override
	public void openDialogPage(Object object) {
		super.openDialogPage(getTrasportoCura(object));
	}

	/**
	 * @param magazzinoAnagraficaBD
	 *            The magazzinoAnagraficaBD to set.
	 */
	public void setMagazzinoAnagraficaBD(IMagazzinoAnagraficaBD magazzinoAnagraficaBD) {
		this.magazzinoAnagraficaBD = magazzinoAnagraficaBD;
	}
}
