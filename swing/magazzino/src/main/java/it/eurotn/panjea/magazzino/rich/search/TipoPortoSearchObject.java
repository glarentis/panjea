package it.eurotn.panjea.magazzino.rich.search;

import it.eurotn.panjea.magazzino.domain.TipoPorto;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.rich.search.AbstractSearchObject;

import java.util.List;

import org.springframework.richclient.command.AbstractCommand;

import com.jidesoft.converter.ObjectConverterManager;

public class TipoPortoSearchObject extends AbstractSearchObject {
	private static final String PAGE_ID = "tipoPortoSearchObject";
	private IMagazzinoAnagraficaBD magazzinoAnagraficaBD;

	/**
	 * costruttore.
	 */
	public TipoPortoSearchObject() {
		super(PAGE_ID);
	}

	@Override
	public List<AbstractCommand> getCustomCommands() {
		return null;
	}

	@Override
	public List<?> getData(String fieldSearch, String valueSearch) {
		return magazzinoAnagraficaBD.caricaTipiPorto(valueSearch);
	}

	/**
	 * Restituisce l'oggetto TipoPorto corrispondente all'oggetto contenuto nel Model. Se il Model contiene solo una
	 * stringa, genero il TipoPorto tramite converter.
	 * 
	 * @param object
	 *            oggetto contenuto nel formModel
	 * @return TipoPorto reale
	 */
	private TipoPorto getTipoPorto(Object object) {

		if (object instanceof TipoPorto) {
			return (TipoPorto) object;
		}

		if (object instanceof String) {
			return (TipoPorto) ObjectConverterManager.fromString((String) object, TipoPorto.class);
		}

		return null;
	}

	@Override
	public void openDialogPage(Object object) {
		super.openDialogPage(getTipoPorto(object));
	}

	/**
	 * @param magazzinoAnagraficaBD
	 *            The magazzinoAnagraficaBD to set.
	 */
	public void setMagazzinoAnagraficaBD(IMagazzinoAnagraficaBD magazzinoAnagraficaBD) {
		this.magazzinoAnagraficaBD = magazzinoAnagraficaBD;
	}
}
