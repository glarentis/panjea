package it.eurotn.panjea.rate.manager.interfaces;

import it.eurotn.panjea.anagrafica.service.exception.TipoDocumentoBaseException;
import it.eurotn.panjea.tesoreria.domain.AreaAcconto;
import it.eurotn.panjea.tesoreria.domain.AreaChiusure;
import it.eurotn.panjea.tesoreria.domain.Pagamento;

import java.util.List;

import javax.ejb.Local;

@Local
public interface PagamentiAccontoGenerator {

	/**
	 * Usando tutti gli acconti disponibili vengono creati i pagamenti per ogni rata.
	 * 
	 * @param pagamenti
	 *            pagamento da recupero rata e importo da pagare con associato l'importo da pagare per l'acconto
	 * @param acconti
	 *            acconti di origine disponibili, se null carica acconti automatici per ogni rata
	 * @return la lista di aree chiusura generate
	 * @throws TipoDocumentoBaseException
	 *             TipoDocumentoBaseException
	 */
	List<AreaChiusure> creaPagamentiConAcconto(List<Pagamento> pagamenti, List<AreaAcconto> acconti)
			throws TipoDocumentoBaseException;
}
