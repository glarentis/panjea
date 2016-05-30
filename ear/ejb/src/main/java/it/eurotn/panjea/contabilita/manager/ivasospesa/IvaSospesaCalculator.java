package it.eurotn.panjea.contabilita.manager.ivasospesa;

import it.eurotn.panjea.contabilita.util.TotaliCodiceIvaDTO;
import it.eurotn.panjea.iva.domain.AreaIva;
import it.eurotn.panjea.rate.domain.AreaRate;
import it.eurotn.panjea.tesoreria.domain.Pagamento;

import java.util.List;

public abstract class IvaSospesaCalculator {

	/**
	 * Calcola il pagato per l'areaIva.
	 * 
	 * @param pagamento
	 *            pagamento
	 * @param areaIva
	 *            areaIva
	 * @param areaRate
	 *            areaRate
	 * @return List<TotaliCodiceIvaDTO>
	 */
	public abstract List<TotaliCodiceIvaDTO> calcola(Pagamento pagamento, AreaIva areaIva, AreaRate areaRate);

}
