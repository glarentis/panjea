package it.eurotn.panjea.contabilita.manager.interfaces;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.service.exception.TipoDocumentoBaseException;
import it.eurotn.panjea.contabilita.domain.AreaContabile.StatoAreaContabile;
import it.eurotn.panjea.contabilita.domain.EstrattoConto;
import it.eurotn.panjea.contabilita.service.exception.ContabilitaException;
import it.eurotn.panjea.contabilita.util.ParametriRicercaEstrattoConto;

import javax.ejb.Local;

@Local
public interface EstrattoContoManager {
	/**
	 * Carica l'estratto conto di un conto.<br/>
	 * Il saldo precedente e quello finale vengono calcolati senza tenere conto dei filtri su {@link StatoAreaContabile}
	 * e {@link TipoDocumento}.<br/>
	 * Le righe dell'estratto contno contengono anche le aperture/chiusure, perrò la colonna saldo sulla riga non viene
	 * toccata.
	 * 
	 * @param parametriRicercaEstrattoConto
	 * @return
	 * @throws ContabilitaException
	 * @throws TipoDocumentoBaseException
	 */
	public EstrattoConto caricaEstrattoConto(ParametriRicercaEstrattoConto parametriRicercaEstrattoConto)
			throws ContabilitaException, TipoDocumentoBaseException;
}
