package it.eurotn.panjea.tesoreria.manager.interfaces;

import it.eurotn.panjea.anagrafica.documenti.service.exception.DocumentoDuplicateException;
import it.eurotn.panjea.tesoreria.domain.AreaPagamenti;
import it.eurotn.panjea.tesoreria.domain.Pagamento;
import it.eurotn.panjea.tesoreria.util.ParametriCreazioneAreaChiusure;

import java.util.List;

import javax.ejb.Local;

/**
 * 
 * Gestore area Pagamenti.
 * 
 * @author vittorio
 * @version 1.0, 26/nov/2009
 * 
 */

@Local
public interface AreaPagamentiManager extends IAreaTesoreriaDAO {

	/**
	 * Cancellazione del Pagamento.
	 * 
	 * @param pagamento
	 *            da cancellare
	 */
	void cancellaPagamento(Pagamento pagamento);

	/**
	 * Creazione della lista di aree Pagamenti. E' invocato dal AreaChiusureManager.
	 * 
	 * @param parametriCreazioneAreaChiusure
	 *            i parametri
	 * @param pagamenti
	 *            i pagamenti
	 * @return lista di aree pagamenti
	 * @throws DocumentoDuplicateException
	 *             DocumentoDuplicateException
	 */
	List<AreaPagamenti> creaAreaPagamenti(ParametriCreazioneAreaChiusure parametriCreazioneAreaChiusure,
			List<Pagamento> pagamenti) throws DocumentoDuplicateException;

	/**
	 * Salva un {@link Pagamento}.
	 * 
	 * @param pagamento
	 *            pagamento da salvare
	 * @return pagamento salvato
	 */
	Pagamento salvaPagamento(Pagamento pagamento);
}
