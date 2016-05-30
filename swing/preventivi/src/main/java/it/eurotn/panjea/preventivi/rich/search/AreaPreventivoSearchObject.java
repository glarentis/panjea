package it.eurotn.panjea.preventivi.rich.search;

import it.eurotn.panjea.anagrafica.documenti.domain.CodiceDocumento;
import it.eurotn.panjea.preventivi.rich.bd.IPreventiviBD;
import it.eurotn.panjea.preventivi.util.parametriricerca.ParametriRicercaAreaPreventivo;
import it.eurotn.rich.search.AbstractSearchObject;

import java.util.List;

import org.springframework.richclient.command.AbstractCommand;

public class AreaPreventivoSearchObject extends AbstractSearchObject {

	private static final String SEARCH_OBJECT_ID = "areaPreventivoSearchObject";

	private IPreventiviBD preventiviBD;

	/**
	 * Costruttore di default.
	 */
	public AreaPreventivoSearchObject() {
		super(SEARCH_OBJECT_ID);
	}

	@Override
	public List<AbstractCommand> getCustomCommands() {
		return null;
	}

	@Override
	public List<?> getData(String fieldSearch, String valueSearch) {
		// Map<String, Object> parameters = searchPanel.getMapParameters();
		ParametriRicercaAreaPreventivo parametriRicerca = new ParametriRicercaAreaPreventivo();
		if (valueSearch != null && !valueSearch.isEmpty()) {
			// Rimuovo l'ultimo carattere che nelle ricerche è %
			valueSearch = valueSearch.replace("%", "");
			if ("codice".equals(fieldSearch) || fieldSearch == null) {
				CodiceDocumento codiceDocumento = null;
				try {
					codiceDocumento = new CodiceDocumento();
					codiceDocumento.setCodice(valueSearch);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				parametriRicerca.setNumeroDocumentoIniziale(codiceDocumento);
				// parametriRicerca.setNumeroDocumentoFinale(codiceDocumento);
			}

		}
		return preventiviBD.ricercaAreePreventivo(parametriRicerca);
	}

	/**
	 * @param preventiviBD
	 *            the preventiviBD to set
	 */
	public void setPreventiviBD(IPreventiviBD preventiviBD) {
		this.preventiviBD = preventiviBD;
	}

}
