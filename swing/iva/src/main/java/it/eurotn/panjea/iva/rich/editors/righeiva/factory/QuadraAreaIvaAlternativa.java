/**
 * 
 */
package it.eurotn.panjea.iva.rich.editors.righeiva.factory;

import it.eurotn.panjea.anagrafica.domain.Importo.TIPO_OPERAZIONE_VALUTA;
import it.eurotn.panjea.iva.domain.AreaIva;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

/**
 * Quadratore per l'area iva di un documento con gestione iva INTRA o ART.17(comportamento equivalente) l'area iva viene
 * quadrata quando il totale documento coincide con il totale imponibile non considerando il totale imposta.
 * 
 * @author Leonardo
 */
public class QuadraAreaIvaAlternativa implements StrategiaQuadraAreaIva {

	private static Logger logger = Logger.getLogger(QuadraAreaIvaAlternativa.class);

	@Override
	public BigDecimal getImportoSquadrato(AreaIva areaIva) {

		BigDecimal totaleDocumento = areaIva.getDocumento().getTotale().getImportoInValuta();
		BigDecimal totaleImponibile = areaIva.getTotaleImponibile(TIPO_OPERAZIONE_VALUTA.VALUTA).abs();

		return totaleDocumento.subtract(totaleImponibile);
	}

	@Override
	public boolean isQuadrata(AreaIva areaIva) {
		logger.debug("--> Enter isQuadrata");
		BigDecimal totaleImponibile = areaIva.getTotaleImponibile(TIPO_OPERAZIONE_VALUTA.VALUTA).abs();
		boolean quadrato = areaIva.getDocumento().getTotale().getImportoInValuta().compareTo(totaleImponibile) == 0;
		logger.debug("--> Exit isQuadrata " + quadrato);
		return quadrato;
	}

}
