/**
 *
 */
package it.eurotn.panjea.magazzino.rich.search;

import it.eurotn.panjea.magazzino.domain.Listino;
import it.eurotn.panjea.magazzino.domain.Listino.ETipoListino;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.rich.search.AbstractSearchObject;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.AbstractCommand;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

/**
 * SearchObject per l'oggetto di dominio {@link Listino}.
 * 
 * @author adriano
 * @version 1.0, 17/nov/2008
 */
public class ListinoSearchObject extends AbstractSearchObject {

	private static final String SEARCH_OBJECT_ID = "listinoSearchObject";

	private IMagazzinoAnagraficaBD magazzinoAnagraficaBD;

	private static Logger logger = Logger.getLogger(ListinoSearchObject.class);

	public static final String TIPO_LISTINO_KEY = "tipoListino";

	/**
	 *
	 */
	public ListinoSearchObject() {
		super(SEARCH_OBJECT_ID);
	}

	@Override
	public List<AbstractCommand> getCustomCommands() {
		return null;
	}

	@Override
	public List<?> getData(String fieldSearch, String valueSearch) {
		Map<String, Object> parameters = searchPanel.getMapParameters();

		ETipoListino tipoListino = null;
		if (parameters.get(TIPO_LISTINO_KEY) != null) {
			tipoListino = (ETipoListino) parameters.get(TIPO_LISTINO_KEY);
		}

		logger.debug("--> Enter getData");
		List<Listino> listini = magazzinoAnagraficaBD.caricaListini(tipoListino, fieldSearch, valueSearch);
		logger.debug("--> Exit getData");
		return listini;
	}

	@Override
	public void openEditor(Object object) {
		Listino listino = (Listino) object;
		if (listino.getId() != null) {
			listino = magazzinoAnagraficaBD.caricaListino(listino, true);
		}
		LifecycleApplicationEvent event = new OpenEditorEvent(listino);
		Application.instance().getApplicationContext().publishEvent(event);
	}

	/**
	 * @param magazzinoAnagraficaBD
	 *            The magazzinoAnagraficaBD to set.
	 */
	public void setMagazzinoAnagraficaBD(IMagazzinoAnagraficaBD magazzinoAnagraficaBD) {
		this.magazzinoAnagraficaBD = magazzinoAnagraficaBD;
	}

}
