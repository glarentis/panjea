package it.eurotn.panjea.ordini.rich.search;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.magazzino.rich.search.TipoAreaMagazzinoSearchObject;
import it.eurotn.panjea.ordini.domain.documento.TipoAreaOrdine;
import it.eurotn.panjea.ordini.rich.bd.IOrdiniDocumentoBD;
import it.eurotn.rich.search.AbstractSearchObject;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.AbstractCommand;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

public class TipoAreaOrdineSearchObject extends AbstractSearchObject {

	private static Logger logger = Logger.getLogger(TipoAreaMagazzinoSearchObject.class);

	private static final String SEARCH_OBJECT_ID = "tipoAreaOrdineSearchObject";
	private IOrdiniDocumentoBD ordiniDocumentoBD;

	/**
	 * Costruttore.
	 */
	public TipoAreaOrdineSearchObject() {
		super(SEARCH_OBJECT_ID);
	}

	@Override
	public List<AbstractCommand> getCustomCommands() {
		return null;
	}

	@Override
	public List<?> getData(String fieldSearch, String valueSearch) {
		logger.debug("--> Enter getData");
		List<TipoAreaOrdine> tipiAreaOrdine = ordiniDocumentoBD.caricaTipiAreaOrdine(fieldSearch, valueSearch, false);
		logger.debug("--> Exit getData");
		return tipiAreaOrdine;
	}

	@Override
	public void openEditor(Object object) {
		TipoAreaOrdine tipoAreaOrdine = (TipoAreaOrdine) object;
		TipoDocumento tipoDocumento = tipoAreaOrdine.getTipoDocumento();
		LifecycleApplicationEvent event = new OpenEditorEvent(tipoDocumento);
		Application.instance().getApplicationContext().publishEvent(event);
	}

	/**
	 * @param ordiniDocumentoBD
	 *            the ordiniDocumentoBD to set
	 */
	public void setOrdiniDocumentoBD(IOrdiniDocumentoBD ordiniDocumentoBD) {
		this.ordiniDocumentoBD = ordiniDocumentoBD;
	}

}
