package it.eurotn.panjea.contabilita.manager.interfaces;

import it.eurotn.panjea.anagrafica.service.exception.TipoDocumentoBaseException;
import it.eurotn.panjea.contabilita.service.exception.ContabilitaException;
import it.eurotn.panjea.contabilita.util.ParametriRicercaSituazioneEP;
import it.eurotn.panjea.contabilita.util.SituazioneEpDTO;

import javax.ejb.Local;

@Local
public interface SituazioneEPManager {

	/**
	 * Calcolo della situazione EconomicaPatrimoniale eseguendo il Bilancio e ripartendo i suoi valori per conti
	 * economici, patrimoniali ed ordine.
	 * 
	 * @param parametriRicercaSituazioneEP
	 * @return
	 * @throws it.eurotn.panjea.contabilita.service.exception.ContabilitaException
	 * @throws TipoDocumentoBaseException
	 */
	SituazioneEpDTO caricaSituazioneEP(ParametriRicercaSituazioneEP parametriRicercaSituazioneEP)
			throws ContabilitaException, TipoDocumentoBaseException;
}
