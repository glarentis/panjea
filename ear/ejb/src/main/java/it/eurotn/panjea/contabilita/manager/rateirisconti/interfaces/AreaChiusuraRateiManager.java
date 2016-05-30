package it.eurotn.panjea.contabilita.manager.rateirisconti.interfaces;

import it.eurotn.panjea.anagrafica.service.exception.TipoDocumentoBaseException;
import it.eurotn.panjea.contabilita.domain.RigaContabile;
import it.eurotn.panjea.contabilita.domain.SottoConto;
import it.eurotn.panjea.contabilita.service.exception.ContiBaseException;

import java.math.BigDecimal;

import javax.ejb.Local;

@Local
public interface AreaChiusuraRateiManager {
	/**
	 *
	 * @param rigaContabile
	 *            rigacontabile che genera il rateo
	 * @param importoRateo
	 *            importo rateo
	 * @param contoRateo
	 *            conto per la riga rateo
	 * @return rigaContabile con i dati aggiornati
	 * @throws ContiBaseException .
	 * @throws TipoDocumentoBaseException .
	 */
	RigaContabile creaAreaContabileChiusura(RigaContabile rigaContabile, BigDecimal importoRateo, SottoConto contoRateo)
			throws ContiBaseException, TipoDocumentoBaseException;
}
