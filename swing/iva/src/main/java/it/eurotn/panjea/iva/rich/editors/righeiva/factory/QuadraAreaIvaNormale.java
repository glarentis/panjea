/**
 * 
 */
package it.eurotn.panjea.iva.rich.editors.righeiva.factory;

import it.eurotn.panjea.anagrafica.domain.Importo.TIPO_OPERAZIONE_VALUTA;
import it.eurotn.panjea.iva.domain.AreaIva;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

/**
 * Quadratore per l'area iva di un documento con gestione iva NORMALE l'area iva viene quadrata quando il totale
 * documento coincide con il totale imponibile sommato al totale imposta della parte iva.
 * 
 * @author Leonardo
 */
public class QuadraAreaIvaNormale implements StrategiaQuadraAreaIva {

	private static Logger logger = Logger.getLogger(QuadraAreaIvaNormale.class);

	@Override
	public BigDecimal getImportoSquadrato(AreaIva areaIva) {

		BigDecimal totaleDoc = areaIva.getDocumento().getTotale().getImportoInValuta();
		BigDecimal totaleImponibile = areaIva.getTotaleImponibile(TIPO_OPERAZIONE_VALUTA.VALUTA);
		BigDecimal totaleImposta = areaIva.getTotaleImposta(TIPO_OPERAZIONE_VALUTA.VALUTA);

		if (areaIva.isNotaCredito()) {
			totaleImponibile = totaleImponibile.negate();
			totaleImposta = totaleImposta.negate();
		}

		return totaleDoc.subtract(totaleImponibile).subtract(totaleImposta);
	}

	@Override
	public boolean isQuadrata(AreaIva areaIva) {
		logger.debug("--> Enter isQuadrata");
		BigDecimal importoSquadrato = getImportoSquadrato(areaIva);

		boolean quadrato = BigDecimal.ZERO.compareTo(importoSquadrato) == 0;
		logger.debug("--> Exit isQuadrata " + quadrato);
		return quadrato;
	}

}
