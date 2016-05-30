/**
 *
 */
package it.eurotn.panjea.anagrafica.rich.search.documenti;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.rich.bd.IDocumentiBD;
import it.eurotn.rich.search.AbstractSearchObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.AbstractCommand;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

/**
 * Search Object per il tipo documento.
 *
 * @author Leonardo
 * @version 1.0, 28/mag/07
 */
public class TipoDocumentoSearchObject extends AbstractSearchObject {

	private IDocumentiBD documentiBD;

	public static final String PARAM_CLASSE_TIPO_DOC = "classeTipoDocumento";
	public static final String PARAM_TIPI_ENTITA = "tipiEntita";

	private static final String PAGE_ID = "tipoDocumentoSearchObject";

	/**
	 * Costruttore di default.
	 */
	public TipoDocumentoSearchObject() {
		super(PAGE_ID);
	}

	@Override
	public List<AbstractCommand> getCustomCommands() {
		return null;
	}

	@Override
	public List<?> getData(String fieldSearch, String valueSearch) {
		List<TipoDocumento> list;
		list = documentiBD.caricaTipiDocumento(fieldSearch, valueSearch, false);

		Map<String, Object> parameters = searchPanel.getMapParameters();

		// filtro i tipi documento per cla classe se richiesto
		List<TipoDocumento> tipiDocValdi = new ArrayList<TipoDocumento>();
		if (parameters.get(PARAM_CLASSE_TIPO_DOC) != null) {
			for (TipoDocumento tipoDocumento : list) {
				if (tipoDocumento.getClasseTipoDocumento().equals(parameters.get(PARAM_CLASSE_TIPO_DOC))) {
					tipiDocValdi.add(tipoDocumento);
				}
			}
			list.retainAll(tipiDocValdi);
		}

		// filtro i tipi documento per i tipi entità se richiesto
		List<TipoDocumento> tipiDocEntitaValde = new ArrayList<TipoDocumento>();
		@SuppressWarnings("unchecked")
		Collection<TipoEntita> tipiEntitaRichiesti = (Collection<TipoEntita>) parameters.get(PARAM_TIPI_ENTITA);
		if (tipiEntitaRichiesti != null) {
			for (TipoDocumento tipoDocumento : list) {
				if (tipiEntitaRichiesti.contains(tipoDocumento.getTipoEntita())) {
					tipiDocEntitaValde.add(tipoDocumento);
				}
			}
			list.retainAll(tipiDocEntitaValde);
		}

		return list;
	}

	/**
	 * @return Returns the documentiBD.
	 */
	public IDocumentiBD getDocumentiBD() {
		return documentiBD;
	}

	@Override
	public void openEditor(Object object) {
		TipoDocumento tipoDocumento = documentiBD.caricaTipoDocumento(((TipoDocumento) object).getId());
		LifecycleApplicationEvent event = new OpenEditorEvent(tipoDocumento);
		Application.instance().getApplicationContext().publishEvent(event);
	}

	/**
	 * @param documentiBD
	 *            The documentiBD to set.
	 */
	public void setDocumentiBD(IDocumentiBD documentiBD) {
		this.documentiBD = documentiBD;
	}

}
