package it.eurotn.panjea.magazzino.rich.search;

import it.eurotn.panjea.magazzino.domain.AspettoEsteriore;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.rich.search.AbstractSearchObject;

import java.util.List;

import org.springframework.richclient.command.AbstractCommand;

import com.jidesoft.converter.ObjectConverterManager;

public class AspettoEsterioreSearchObject extends AbstractSearchObject {
	private static final String PAGE_ID = "aspettoEsterioreSearchObject";
	private IMagazzinoAnagraficaBD magazzinoAnagraficaBD;

	/**
	 * Costruttore.
	 */
	public AspettoEsterioreSearchObject() {
		super(PAGE_ID);
	}

	/**
	 * Restituisce l'oggetto AspettoEsteriore corrispondente all'oggetto contenuto nel Model. Se il Model contiene solo
	 * una stringa, genero il AspettoEsteriore tramite converter.
	 * 
	 * @param object
	 *            oggetto contenuto nel formModel
	 * @return AspettoEsteriore reale
	 */
	private AspettoEsteriore getAspettoEsteriore(Object object) {

		if (object instanceof AspettoEsteriore) {
			return (AspettoEsteriore) object;
		}

		if (object instanceof String) {
			return (AspettoEsteriore) ObjectConverterManager.fromString((String) object, AspettoEsteriore.class);
		}

		return null;
	}

	@Override
	public List<AbstractCommand> getCustomCommands() {
		return null;
	}

	@Override
	public List<?> getData(String fieldSearch, String valueSearch) {
		return magazzinoAnagraficaBD.caricaAspettiEsteriori(valueSearch);
	}

	@Override
	public void openDialogPage(Object object) {
		super.openDialogPage(getAspettoEsteriore(object));
	}

	/**
	 * @param magazzinoAnagraficaBD
	 *            The magazzinoAnagraficaBD to set.
	 */
	public void setMagazzinoAnagraficaBD(IMagazzinoAnagraficaBD magazzinoAnagraficaBD) {
		this.magazzinoAnagraficaBD = magazzinoAnagraficaBD;
	}

}
