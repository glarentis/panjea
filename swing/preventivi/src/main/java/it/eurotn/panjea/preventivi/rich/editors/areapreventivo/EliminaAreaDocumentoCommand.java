package it.eurotn.panjea.preventivi.rich.editors.areapreventivo;

import it.eurotn.panjea.anagrafica.rich.editors.documento.AbstractEliminaDocumentoCommand;
import it.eurotn.panjea.preventivi.domain.documento.AreaPreventivo;
import it.eurotn.panjea.preventivi.domain.documento.interfaces.IAreaDocumentoTestata;
import it.eurotn.panjea.preventivi.rich.bd.interfaces.IAreaDocumentoBD;
import it.eurotn.panjea.preventivi.util.AreaPreventivoRicerca;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EliminaAreaDocumentoCommand<E extends IAreaDocumentoTestata> extends AbstractEliminaDocumentoCommand {

	private IAreaDocumentoBD<E, ?> areaBD;
	private List<AreaPreventivoRicerca> areeRicerca = Collections.emptyList();

	public static final String PARAM_AREA_DA_CANCELLARE = "areaDaCancellare";

	/**
	 * Costruttore.
	 */
	public EliminaAreaDocumentoCommand() {
		super("gestioneDocMagazzinoController");
	}

	@Override
	public Object doDelete(boolean deleteAreeCollegate) {
		logger.debug("--> Enter doDelete");

		@SuppressWarnings("unchecked")
		E areaDaParametro = (E) getParameter(PARAM_AREA_DA_CANCELLARE, null);

		if (areaDaParametro == null) {
			List<AreaPreventivo> areeDaCancellare = new ArrayList<AreaPreventivo>();

			for (AreaPreventivoRicerca areaRicerca : areeRicerca) {
				AreaPreventivo areaCorrente = areaRicerca.creaProxyAreaPreventivo(areaRicerca);
				areeDaCancellare.add(areaCorrente);
			}
			areaBD.cancellaAree(areeDaCancellare);
			return areeDaCancellare;

		} else if (areaDaParametro.getId() != null) {
			areaBD.cancellaArea(areaDaParametro);
			return areaDaParametro;
		}

		return null;
	}

	/**
	 * @return the areeOrdineRicerca
	 */
	public List<AreaPreventivoRicerca> getAreeRicerca() {
		return areeRicerca;
	}

	/**
	 * @param preventiviBD
	 *            the preventiviBD to set
	 */
	public void setAreaBD(IAreaDocumentoBD<E, ?> preventiviBD) {
		this.areaBD = preventiviBD;
	}

	/**
	 * @param areeOrdineRicerca
	 *            the areeOrdineRicerca to set
	 */
	public void setAreeRicerca(List<AreaPreventivoRicerca> areeOrdineRicerca) {
		this.areeRicerca = areeOrdineRicerca;
	}

}
