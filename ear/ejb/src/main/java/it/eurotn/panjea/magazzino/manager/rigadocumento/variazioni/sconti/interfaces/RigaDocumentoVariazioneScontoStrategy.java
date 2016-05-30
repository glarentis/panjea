/**
 * 
 */
package it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.sconti.interfaces;

import it.eurotn.panjea.magazzino.domain.IRigaArticoloDocumento;

import java.math.BigDecimal;

/**
 * @author fattazzo
 * 
 */
public interface RigaDocumentoVariazioneScontoStrategy {

	/**
	 * Applica la variazione alla riga articolo.
	 * 
	 * @param rigaArticolo
	 *            riga articolo
	 * @param variazione
	 *            variazione da applicare
	 * @param scontoStrategy
	 *            strategia di applicazione della variazione
	 * @return riga modificata
	 */
	IRigaArticoloDocumento applicaVariazione(IRigaArticoloDocumento rigaArticolo, BigDecimal variazione,
			TipoVariazioneScontoStrategy scontoStrategy);

}
