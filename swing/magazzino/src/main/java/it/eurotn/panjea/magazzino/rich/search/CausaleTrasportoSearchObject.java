package it.eurotn.panjea.magazzino.rich.search;

import it.eurotn.panjea.magazzino.domain.CausaleTrasporto;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.rich.search.AbstractSearchObject;

import java.util.List;

import org.springframework.richclient.command.AbstractCommand;

import com.jidesoft.converter.ObjectConverterManager;

public class CausaleTrasportoSearchObject extends AbstractSearchObject {

	private IMagazzinoAnagraficaBD magazzinoAnagraficaBD;

	/**
	 * Costrtuttore.
	 */
	public CausaleTrasportoSearchObject() {
		super("causaleTrasportoSearchObject");
	}

	/**
	 * Restituisce l'oggetto CausaleTrasporto corrispondente all'oggetto contenuto nel Model. Se il Model contiene solo
	 * una stringa, genero il CausaleTrasporto tramite converter.
	 * 
	 * @param object
	 *            oggetto contenuto nel formModel
	 * @return CausaleTrasporto reale
	 */
	private CausaleTrasporto getCausaleTrasporto(Object object) {

		if (object instanceof CausaleTrasporto) {
			return (CausaleTrasporto) object;
		}

		if (object instanceof String) {
			return (CausaleTrasporto) ObjectConverterManager.fromString((String) object, CausaleTrasporto.class);
		}

		return null;
	}

	@Override
	public List<AbstractCommand> getCustomCommands() {
		return null;
	}

	@Override
	public List<?> getData(String fieldSearch, String valueSearch) {
		return magazzinoAnagraficaBD.caricaCausaliTraporto(valueSearch);
	}

	@Override
	public void openDialogPage(Object object) {
		super.openDialogPage(getCausaleTrasporto(object));
	}

	/**
	 * @param magazzinoAnagraficaBD
	 *            The magazzinoAnagraficaBD to set.
	 */
	public void setMagazzinoAnagraficaBD(IMagazzinoAnagraficaBD magazzinoAnagraficaBD) {
		this.magazzinoAnagraficaBD = magazzinoAnagraficaBD;
	}
}
