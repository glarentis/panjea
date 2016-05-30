package it.eurotn.panjea.preventivi.rich.search;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.preventivi.domain.documento.TipoAreaPreventivo;
import it.eurotn.panjea.preventivi.rich.bd.IPreventiviBD;
import it.eurotn.rich.search.AbstractSearchObject;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.AbstractCommand;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

public class TipoAreaPreventivoSearchObject extends AbstractSearchObject {

	private static Logger logger = Logger.getLogger(TipoAreaPreventivoSearchObject.class);

	private static final String SEARCH_OBJECT_ID = "tipoAreaPreventivoSearchObject";
	private IPreventiviBD preventiviBD;

	/**
	 * Costruttore.
	 */
	public TipoAreaPreventivoSearchObject() {
		super(SEARCH_OBJECT_ID);
	}

	@Override
	public List<AbstractCommand> getCustomCommands() {
		return null;
	}

	@Override
	public List<?> getData(String fieldSearch, String valueSearch) {
		logger.debug("--> Enter getData");
		List<TipoAreaPreventivo> tipiAreaPreventivo = preventiviBD.caricaTipiAreaPreventivo(fieldSearch, valueSearch,
				false);
		logger.debug("--> Exit getData");
		return tipiAreaPreventivo;
	}

	@Override
	public void openEditor(Object object) {
		TipoAreaPreventivo tipoAreaPreventivo = (TipoAreaPreventivo) object;
		TipoDocumento tipoDocumento = tipoAreaPreventivo.getTipoDocumento();
		LifecycleApplicationEvent event = new OpenEditorEvent(tipoDocumento);
		Application.instance().getApplicationContext().publishEvent(event);
	}

	/**
	 * @param preventiviBD
	 *            the preventiviBD to set
	 */
	public void setPreventiviBD(IPreventiviBD preventiviBD) {
		this.preventiviBD = preventiviBD;
	}

}
