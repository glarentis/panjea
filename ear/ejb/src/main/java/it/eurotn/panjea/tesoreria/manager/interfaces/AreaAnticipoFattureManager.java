package it.eurotn.panjea.tesoreria.manager.interfaces;

import it.eurotn.panjea.anagrafica.documenti.service.exception.DocumentoDuplicateException;
import it.eurotn.panjea.tesoreria.domain.AreaAnticipoFatture;
import it.eurotn.panjea.tesoreria.domain.AreaPagamenti;
import it.eurotn.panjea.tesoreria.domain.Pagamento;
import it.eurotn.panjea.tesoreria.util.ParametriCreazioneAreaChiusure;

import java.util.List;

import javax.ejb.Local;

/**
 * Gestore area AreaAnticipoFattureManager.
 */
@Local
public interface AreaAnticipoFattureManager extends IAreaTesoreriaDAO {

	/**
	 * Crea l'area pagamenti che rappresenta l'anticipo fatture; i pagamenti di questo tipo sono con importo a 0.
	 * 
	 * @param parametriCreazioneAreaChiusure
	 *            parametriCreazioneAreaChiusure
	 * @param pagamenti
	 *            pagamenti
	 * @return AreaPagamenti
	 * @throws DocumentoDuplicateException
	 *             DocumentoDuplicateException
	 */
	AreaAnticipoFatture creaAreaAnticipoFatture(ParametriCreazioneAreaChiusure parametriCreazioneAreaChiusure,
			List<Pagamento> pagamenti) throws DocumentoDuplicateException;

	/**
	 * Crea il documento di pagamento per la chiusura dell'anticipoFattura.
	 * 
	 * @param parametriCreazioneAreaChiusure
	 *            parametriCreazioneAreaChiusure
	 * @param pagamenti
	 *            pagamenti
	 * @return AreaAnticipoFatture
	 * @throws DocumentoDuplicateException
	 *             DocumentoDuplicateException
	 */
	AreaPagamenti creaChiusuraAreaAnticipoFatture(ParametriCreazioneAreaChiusure parametriCreazioneAreaChiusure,
			List<Pagamento> pagamenti) throws DocumentoDuplicateException;

}
