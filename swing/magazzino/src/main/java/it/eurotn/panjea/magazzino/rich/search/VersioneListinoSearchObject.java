/**
 * 
 */
package it.eurotn.panjea.magazzino.rich.search;

import it.eurotn.panjea.magazzino.domain.Listino;
import it.eurotn.panjea.magazzino.domain.Listino.ETipoListino;
import it.eurotn.panjea.magazzino.domain.VersioneListino;
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
 * SearchObject per l'oggetto di dominio {@link VersioneListino}.
 * 
 * @author Leonardo
 * @version 1.0, 08/set/2010
 */
public class VersioneListinoSearchObject extends AbstractSearchObject {

	private static final String SEARCH_OBJECT_ID = "versioneListinoSearchObject";

	private IMagazzinoAnagraficaBD magazzinoAnagraficaBD;

	private static Logger logger = Logger.getLogger(VersioneListinoSearchObject.class);

	public static final String TIPO_LISTINO_KEY = "tipoListino";

	/**
	 * 
	 */
	public VersioneListinoSearchObject() {
		super(SEARCH_OBJECT_ID);
	}

	@Override
	public List<AbstractCommand> getCustomCommands() {
		return null;
	}

	@Override
	public List<?> getData(String fieldSearch, String valueSearch) {
		logger.debug("--> Enter getData");
		Map<String, Object> parameters = searchPanel.getMapParameters();

		ETipoListino tipoListino = null;
		if (parameters.get(TIPO_LISTINO_KEY) != null) {
			tipoListino = (ETipoListino) parameters.get(TIPO_LISTINO_KEY);
		}
		List<VersioneListino> versioneListini = magazzinoAnagraficaBD.caricaVersioniListino(fieldSearch, valueSearch,
				tipoListino);
		logger.debug("--> Exit getData");
		return versioneListini;
	}

	@Override
	public void openEditor(Object object) {
		VersioneListino versioneListino = (VersioneListino) object;
		Listino listino = new Listino();
		if (versioneListino.getId() != null && versioneListino.getListino().getId() != null) {
			listino = versioneListino.getListino();
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
