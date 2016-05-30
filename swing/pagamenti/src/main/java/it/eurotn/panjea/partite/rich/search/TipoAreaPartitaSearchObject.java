/**
 * 
 */
package it.eurotn.panjea.partite.rich.search;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.partite.domain.TipoAreaPartita;
import it.eurotn.panjea.partite.domain.TipoAreaPartita.TipoPartita;
import it.eurotn.panjea.partite.rich.bd.IPartiteBD;
import it.eurotn.rich.search.AbstractSearchObject;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.AbstractCommand;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

/**
 * {@link TipoAreaPartitaSearchObject} che carica i {@link TipoAreaPartita}. Se c'e' il parametro PARAM_TIPO_PARTITA
 * viene caricata la lista di {@link TipoAreaPartita} filtrata, altrimenti vengono caricate tutte le
 * {@link TipoAreaPartita} disponibili.
 * 
 * @author Leonardo
 */
public class TipoAreaPartitaSearchObject extends AbstractSearchObject {

	private static Logger logger = Logger.getLogger(TipoAreaPartitaSearchObject.class);
	private static final String SEARCH_OBJECT_ID = "tipoAreaPartitaSearchObject";
	private IPartiteBD partiteBD = null;

	public static final String PARAM_TIPO_PARTITA = "tipoPartita";
	public static final String PARAM_ESCLUDI_TIPIAREAPARTITE_DISTINTA = "escludiTipiAreaPartiteDistinta";

	/**
	 * Costruttore.
	 */
	public TipoAreaPartitaSearchObject() {
		super(SEARCH_OBJECT_ID);
	}

	@Override
	public List<AbstractCommand> getCustomCommands() {
		return null;
	}

	@Override
	public List<?> getData(String fieldSearch, String valueSearch) {
		Map<String, Object> parameters = searchPanel.getMapParameters();
		TipoPartita tipoPartita = (TipoPartita) parameters.get(PARAM_TIPO_PARTITA);
		Boolean escludiTipiAreaPartiteDistinta = true;
		if (parameters.containsKey(PARAM_ESCLUDI_TIPIAREAPARTITE_DISTINTA)) {
			escludiTipiAreaPartiteDistinta = (Boolean) parameters.get(PARAM_ESCLUDI_TIPIAREAPARTITE_DISTINTA);
		}
		List<TipoAreaPartita> list = null;
		list = partiteBD.caricaTipiAreaPartitaPerPagamenti(fieldSearch, valueSearch, tipoPartita, false,
				escludiTipiAreaPartiteDistinta);
		logger.debug("--> Exit getData " + list.size());
		return list;
	}

	@Override
	public void openEditor(Object object) {
		TipoAreaPartita tipoAreaPartita = (TipoAreaPartita) object;
		TipoDocumento tipoDocumento = tipoAreaPartita.getTipoDocumento();
		LifecycleApplicationEvent event = new OpenEditorEvent(tipoDocumento);
		Application.instance().getApplicationContext().publishEvent(event);
	}

	/**
	 * @param partiteBD
	 *            the partiteBD to set
	 */
	public void setPartiteBD(IPartiteBD partiteBD) {
		this.partiteBD = partiteBD;
	}

}
