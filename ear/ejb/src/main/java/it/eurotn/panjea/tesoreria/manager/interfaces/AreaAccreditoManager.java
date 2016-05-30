/**
 * 
 */
package it.eurotn.panjea.tesoreria.manager.interfaces;

import it.eurotn.panjea.tesoreria.domain.AreaAccredito;
import it.eurotn.panjea.tesoreria.domain.Effetto;
import it.eurotn.panjea.tesoreria.service.exception.DataRichiestaDopoIncassoException;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;

/**
 * Gestore dell'area Accredito.
 * 
 * @author vittorio
 * @version 1.0, 26/nov/2009
 * 
 */
@Local
public interface AreaAccreditoManager extends IAreaTesoreriaDAO {

	/**
	 * Creazione di aree Accredito in base alla data Valuta e area Effetto.
	 * 
	 * @param effetti
	 *            da cui ricavare i dati per generare l'area Accredio
	 * @param dataDopoIncasso
	 *            la data richiesta per la data dopo incasso di un accredito
	 * @return la lista delle aree Accredito generate
	 * @throws DataRichiestaDopoIncassoException
	 *             data richiesta per scrittura posticipata exception
	 */
	List<AreaAccredito> creaAreeAccredito(List<Effetto> effetti, Date dataDopoIncasso)
			throws DataRichiestaDopoIncassoException;

}
