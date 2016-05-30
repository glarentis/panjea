/**
 *
 */
package it.eurotn.panjea.ordini.rich.search;

import it.eurotn.panjea.ordini.rich.bd.IOrdiniDocumentoBD;
import it.eurotn.panjea.ordini.util.parametriricerca.ParametriRicercaAreaOrdine;
import it.eurotn.rich.search.AbstractSearchObject;

import java.util.List;

import org.springframework.richclient.command.AbstractCommand;

/**
 * @author Leonardo
 */
public class AreaOrdineRicercaSearchObject extends AbstractSearchObject {

	private static final String SEARCH_OBJECT_ID = "areaOrdineRicercaSearchObject";

	private IOrdiniDocumentoBD ordiniDocumentoBD;

	/**
	 * Costruttore di default.
	 */
	public AreaOrdineRicercaSearchObject() {
		super(SEARCH_OBJECT_ID);
	}

	@Override
	public List<AbstractCommand> getCustomCommands() {
		return null;
	}

	@Override
	public List<?> getData(String fieldSearch, String valueSearch) {
		// Map<String, Object> parameters = searchPanel.getMapParameters();
		ParametriRicercaAreaOrdine parametriRicerca = new ParametriRicercaAreaOrdine();
		if (valueSearch != null && !valueSearch.isEmpty()) {
			if ("codice".equals(fieldSearch) || fieldSearch == null) {
				parametriRicerca.setNumeroDocumento(valueSearch);
			}
		}
		return ordiniDocumentoBD.ricercaAreeOrdine(parametriRicerca);
	}

	/**
	 * @param ordiniDocumentoBD
	 *            The ordiniDocumentoBD to set.
	 */
	public void setOrdiniDocumentoBD(IOrdiniDocumentoBD ordiniDocumentoBD) {
		this.ordiniDocumentoBD = ordiniDocumentoBD;
	}

}
