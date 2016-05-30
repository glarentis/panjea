/**
 * 
 */
package it.eurotn.panjea.ordini.rich.editors.areaordine;

import it.eurotn.panjea.anagrafica.rich.editors.documento.AbstractEliminaDocumentoCommand;
import it.eurotn.panjea.ordini.domain.documento.AreaOrdine;
import it.eurotn.panjea.ordini.rich.bd.IOrdiniDocumentoBD;
import it.eurotn.panjea.ordini.util.AreaOrdineRicerca;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * @author Leonardo
 * 
 */
public class EliminaAreaOrdineCommand extends AbstractEliminaDocumentoCommand {

	private static Logger logger = Logger.getLogger(EliminaAreaOrdineCommand.class);
	private IOrdiniDocumentoBD ordiniDocumentoBD = null;
	private List<AreaOrdineRicerca> areeOrdineRicerca = Collections.emptyList();

	public static final String PARAM_AREA_ORDINE = "areaOrdineParam";

	/**
	 * Costruttore.
	 * 
	 * @param pageId
	 *            id pagina
	 */
	public EliminaAreaOrdineCommand(final String pageId) {
		super("gestioneDocMagazzinoController");
	}

	@Override
	public Object doDelete(boolean deleteAreeCollegate) {
		logger.debug("--> Enter doDelete");

		AreaOrdine areaOrdine = (AreaOrdine) getParameter(PARAM_AREA_ORDINE, null);

		if (areaOrdine == null) {
			List<AreaOrdine> areeOrdineDaCancellare = new ArrayList<AreaOrdine>();

			for (AreaOrdineRicerca areaOrdineRicerca : areeOrdineRicerca) {
				AreaOrdine areaOrdineCorrente = new AreaOrdine();
				areaOrdineCorrente.setId(areaOrdineRicerca.getIdAreaOrdine());
				areaOrdineCorrente.setDocumento(areaOrdineRicerca.getDocumento());
				areaOrdineCorrente.setTipoAreaOrdine(areaOrdineRicerca.getTipoAreaOrdine());
				areeOrdineDaCancellare.add(areaOrdineCorrente);
			}
			ordiniDocumentoBD.cancellaAreeOrdine(areeOrdineDaCancellare);
			return areeOrdineDaCancellare;
		} else {
			if (areaOrdine.getId() != null) {
				ordiniDocumentoBD.cancellaAreaOrdine(areaOrdine);
				return areaOrdine;
			}
		}

		return null;
	}

	/**
	 * @return the areeOrdineRicerca
	 */
	public List<AreaOrdineRicerca> getAreeOrdineRicerca() {
		return areeOrdineRicerca;
	}

	/**
	 * @param areeOrdineRicerca
	 *            the areeOrdineRicerca to set
	 */
	public void setAreeOrdineRicerca(List<AreaOrdineRicerca> areeOrdineRicerca) {
		this.areeOrdineRicerca = areeOrdineRicerca;
	}

	/**
	 * @param ordiniDocumentoBD
	 *            The ordiniDocumentoBD to set.
	 */
	public void setOrdiniDocumentoBD(IOrdiniDocumentoBD ordiniDocumentoBD) {
		this.ordiniDocumentoBD = ordiniDocumentoBD;
	}

}
