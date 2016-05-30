/**
 *
 */
package it.eurotn.panjea.contabilita.util.giornaleiva;

import it.eurotn.panjea.contabilita.domain.ContabilitaSettings;
import it.eurotn.panjea.contabilita.domain.ContabilitaSettings.ETipoPeriodicita;

import java.util.HashMap;
import java.util.Map;

/**
 * @author leonardo
 */
public final class DataPeriodicitaGiornaleIvaPrecedenteCalculatorFactory {

	private static Map<ETipoPeriodicita, DataPeriodicitaGiornaleIvaPrecedenteCalculator> periodiCalculator = new HashMap<ContabilitaSettings.ETipoPeriodicita, DataPeriodicitaGiornaleIvaPrecedenteCalculator>();

	static {
		periodiCalculator.put(ETipoPeriodicita.MENSILE, new DataGiornaleIvaMensileCalculator());
		periodiCalculator.put(ETipoPeriodicita.TRIMESTRALE, new DataGiornaleIvaTrimestraleCalculator());
		periodiCalculator.put(ETipoPeriodicita.ANNUALE, new DataGiornaleIvaAnnualeCalculator());
	}

	/**
	 * Calculator per determinare la data del giornale iva precedente rispetto alla periodicità e ad altri parametri
	 * iniziali.
	 * 
	 * @param tipoPeriodicita
	 *            mensile, trimestrale, annuale
	 * @return il calendar con anno e mese avvalorati con la data per la ricerca del giornale iva precedente
	 */
	public static DataPeriodicitaGiornaleIvaPrecedenteCalculator create(ETipoPeriodicita tipoPeriodicita) {
		DataPeriodicitaGiornaleIvaPrecedenteCalculator calculator = periodiCalculator.get(tipoPeriodicita);
		return calculator;
	}

	/**
	 * Costruttore.
	 */
	private DataPeriodicitaGiornaleIvaPrecedenteCalculatorFactory() {
		super();
	}

}
