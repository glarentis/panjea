/**
 *
 */
package it.eurotn.panjea.tesoreria.manager.interfaces;

import it.eurotn.panjea.anagrafica.service.exception.TipoDocumentoBaseException;
import it.eurotn.panjea.tesoreria.domain.AreaBonifico;
import it.eurotn.panjea.tesoreria.domain.AreaPagamenti;
import it.eurotn.panjea.tesoreria.domain.Pagamento;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

import javax.ejb.Local;

/**
 * Gestore dell'area bonifico.
 * 
 * @author leonardo
 */
@Local
public interface AreaBonificoManager extends IAreaTesoreriaDAO {

	/**
	 * Carica l'area bonifico in base all'area pagamenti.
	 * 
	 * @param areaPagamenti
	 *            area pagamenti
	 * @return area caricata
	 */
	AreaBonifico caricaAreaBonifico(AreaPagamenti areaPagamenti);

	/**
	 * Crea l'area bonifico.
	 * 
	 * @param dataDocumento
	 *            dataDocumento
	 * @param numeroDocumento
	 *            numeroDocumento
	 * @param areaPagamenti
	 *            areaPagamenti da collegare al bonifico
	 * @param spese
	 *            spese
	 * @param pagamenti
	 *            pagamenti effettivi, vanno salvati solo questi sull'area pagamenti, l'areaBonifico prende i pagamenti
	 *            dell'areaPagamenti collegata
	 * @return AreaBonifico
	 * @throws TipoDocumentoBaseException
	 *             TipoDocumentoBaseException
	 */
	AreaBonifico creaAreaBonifico(Date dataDocumento, String numeroDocumento, AreaPagamenti areaPagamenti,
			BigDecimal spese, Set<Pagamento> pagamenti) throws TipoDocumentoBaseException;
}
