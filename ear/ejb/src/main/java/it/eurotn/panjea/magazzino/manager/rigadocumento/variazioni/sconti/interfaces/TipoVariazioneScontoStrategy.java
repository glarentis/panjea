/**
 * 
 */
package it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.sconti.interfaces;

import it.eurotn.panjea.magazzino.domain.IRigaArticoloDocumento;
import it.eurotn.panjea.magazzino.domain.Sconto;

import java.math.BigDecimal;

/**
 * @author fattazzo
 * 
 */
public interface TipoVariazioneScontoStrategy {

	/**
	 * Calcola la variazione alla riga documento.
	 * 
	 * @param rigaArticolo
	 *            riga a cui applicare la variazione
	 * @param variazione
	 *            variazione da applicare
	 * @return sconto calcolato
	 */
	Sconto calcola(IRigaArticoloDocumento rigaArticolo, BigDecimal variazione);
}
