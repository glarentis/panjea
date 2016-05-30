/**
 *
 */
package it.eurotn.panjea.tesoreria.manager.interfaces;

import it.eurotn.panjea.anagrafica.service.exception.TipoDocumentoBaseException;
import it.eurotn.panjea.tesoreria.domain.AreaDistintaBancaria;
import it.eurotn.panjea.tesoreria.domain.AreaEffetti;
import it.eurotn.panjea.tesoreria.domain.AreaTesoreria;
import it.eurotn.panjea.tesoreria.domain.Effetto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

/**
 * Gestore dell'area distinta bancaria.
 *
 * @author vittorio
 * @version 1.0, 26/nov/2009
 *
 */
public interface AreaDistintaBancariaManager extends IAreaTesoreriaDAO {

	/**
	 * Carica l'area distinta bancaria in base all'area effetti.
	 *
	 * @param areaEffetti
	 *            area effetti
	 * @return area caricata
	 */
	AreaTesoreria caricaAreaDistinta(AreaEffetti areaEffetti);

	/**
	 * Creazione della distinta Bancaria.
	 *
	 * @param dataDocumento
	 *            da generare
	 * @param nDocumento
	 *            da generare
	 * @param areaEffetti
	 *            da collegare alla distinta
	 * @param spese
	 *            le spese effetti
	 * @param speseDistinta
	 *            spese della distinta
	 * @param effetti
	 *            gli effetti della distinta bancaria
	 * @return l'areaDistitaBancaria creata
	 * @throws TipoDocumentoBaseException
	 *             lanciata se non ho il tipodocumentobase per la distinta
	 */

	AreaDistintaBancaria creaAreaDistintaBancaria(Date dataDocumento, String nDocumento, AreaEffetti areaEffetti,
			BigDecimal spese, BigDecimal speseDistinta, Set<Effetto> effetti) throws TipoDocumentoBaseException;
}
