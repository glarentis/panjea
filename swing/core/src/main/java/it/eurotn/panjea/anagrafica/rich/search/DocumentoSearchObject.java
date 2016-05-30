/**
 * 
 */
package it.eurotn.panjea.anagrafica.rich.search;

import it.eurotn.panjea.anagrafica.documenti.util.ParametriRicercaDocumento;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.rich.bd.IDocumentiBD;
import it.eurotn.rich.search.AbstractSearchObject;

import java.util.List;
import java.util.Map;

import org.springframework.richclient.command.AbstractCommand;

/**
 * @author Leonardo
 */
public class DocumentoSearchObject extends AbstractSearchObject {

	private static final String SEARCH_OBJECT_ID = "documentoSearchObject";

	public static final String ENTITA_KEY = "entita";
	private IDocumentiBD documentiBD;

	/**
	 * Costruttore di default.
	 */
	public DocumentoSearchObject() {
		super(SEARCH_OBJECT_ID);
	}

	@Override
	public List<AbstractCommand> getCustomCommands() {
		return null;
	}

	@Override
	public List<?> getData(String fieldSearch, String valueSearch) {
		Map<String, Object> parameters = searchPanel.getMapParameters();
		ParametriRicercaDocumento parametriRicercaDocumento = new ParametriRicercaDocumento();
		if (valueSearch != null && !valueSearch.isEmpty()) {
			// Rimuovo l'ultimo carattere che nelle ricerche è %
			valueSearch = valueSearch.replace("%", "");
			if ("codice".equals(fieldSearch) || fieldSearch == null) {
				Integer codiceDocumento = null;
				try {
					codiceDocumento = new Integer(valueSearch);
				} catch (Exception e) {
					// non faccio nulla
				}
				parametriRicercaDocumento.setCodice(codiceDocumento);
			}

		}
		if ("dataDocumento".equals(fieldSearch)) {
			parametriRicercaDocumento.setDataDocumento(valueSearch);
		}
		EntitaLite entitaLite = (EntitaLite) parameters.get(DocumentoSearchObject.ENTITA_KEY);
		if (entitaLite != null) {
			parametriRicercaDocumento.setEntita(entitaLite);
		}
		return documentiBD.ricercaDocumenti(parametriRicercaDocumento);
	}

	/**
	 * @param documentiBD
	 *            the documentiBD to set
	 */
	public void setDocumentiBD(IDocumentiBD documentiBD) {
		this.documentiBD = documentiBD;
	}

}
