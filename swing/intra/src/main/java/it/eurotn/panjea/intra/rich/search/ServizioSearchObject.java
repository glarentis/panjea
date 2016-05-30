package it.eurotn.panjea.intra.rich.search;

import it.eurotn.panjea.intra.domain.Nomenclatura;
import it.eurotn.panjea.intra.domain.Servizio;
import it.eurotn.panjea.intra.rich.bd.IIntraBD;
import it.eurotn.panjea.magazzino.domain.Articolo.ETipoArticolo;
import it.eurotn.rich.search.AbstractSearchObject;

import java.util.List;
import java.util.Map;

import org.springframework.richclient.command.AbstractCommand;

/**
 * Specializzazione della classe <code>AbstractSearchObject</code> responsabile di effettuare la ricerca dei servizi
 * intra.
 */
public class ServizioSearchObject extends AbstractSearchObject {

	public static final String PARAM_TIPO_ARTICOLO = "tipoArticolo";
	private static final String SEARCH_ID = "nomenclaturaSearchObject";

	private IIntraBD intraBD;

	/**
	 * Costruttore.
	 */
	public ServizioSearchObject() {
		super(SEARCH_ID);
	}

	@Override
	public List<AbstractCommand> getCustomCommands() {
		return null;
	}

	@Override
	public List<?> getData(String fieldSearch, String valueSearch) {
		Map<String, Object> parameters = searchPanel.getMapParameters();
		Class<?> clazz = null;
		if (parameters.get(PARAM_TIPO_ARTICOLO) != null) {
			ETipoArticolo tipoArticolo = (ETipoArticolo) parameters.get(PARAM_TIPO_ARTICOLO);
			clazz = getServizioClass(tipoArticolo);
		}
		return intraBD.caricaServizi(clazz, fieldSearch, valueSearch);
	}

	/**
	 * Restituisce la classe Servizio o nomenclatura associata al tipo articolo.
	 * 
	 * @param tipoArticolo
	 *            il tipo articolo
	 * @return class Servizio o Nomenclatura o null
	 */
	private Class<?> getServizioClass(ETipoArticolo tipoArticolo) {
		Class<?> clazz = null;
		switch (tipoArticolo) {
		case ACCESSORI:
		case FISICO:
			clazz = Nomenclatura.class;
			break;
		case SERVIZI:
			clazz = Servizio.class;
		default:
			break;
		}
		return clazz;
	}

	/**
	 * @param intraBD
	 *            the intraBD to set
	 */
	public void setIntraBD(IIntraBD intraBD) {
		this.intraBD = intraBD;
	}

}
