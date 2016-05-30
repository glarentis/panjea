package it.eurotn.panjea.contabilita.manager.rateirisconti.interfaces;

import it.eurotn.panjea.anagrafica.service.exception.TipoDocumentoBaseException;
import it.eurotn.panjea.contabilita.service.exception.ContiBaseException;
import it.eurotn.panjea.contabilita.util.ParametriRicercaMovimentiContabili;

import java.util.Date;

import javax.ejb.Local;

@Local
public interface AreaChiusuraRiscontiManager {

	/**
	 * Crea i movimenti di chiusura dei risconti per l'anno indicato.
	 *
	 * @param anno
	 *            anno di riferimento
	 * @param dataMovimenti
	 *            data dei movimenti
	 * @throws ContiBaseException .
	 * @throws TipoDocumentoBaseException .
	 * @return ParametriRicercaMovimentiContabili per effettuare la ricerca dei documenti creati
	 */
	ParametriRicercaMovimentiContabili creaMovimentiChiusureRisconti(int anno, Date dataMovimenti)
			throws ContiBaseException, TipoDocumentoBaseException;
}
