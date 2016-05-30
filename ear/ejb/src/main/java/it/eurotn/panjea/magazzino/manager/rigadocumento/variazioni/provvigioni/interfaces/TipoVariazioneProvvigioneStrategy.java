/**
 * 
 */
package it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.provvigioni.interfaces;

import it.eurotn.panjea.magazzino.domain.IRigaArticoloDocumento;

import java.math.BigDecimal;

/**
 * @author fattazzo
 * 
 */
public interface TipoVariazioneProvvigioneStrategy {

	/**
	 * Calcola la percentuale di provvigione per la riga documento.
	 * 
	 * @param rigaArticolo
	 *            riga articolo
	 * @param percProvvigione
	 *            percentuale di provvigione
	 * @return percentuale provvigione calcolata
	 */
	BigDecimal calcola(IRigaArticoloDocumento rigaArticolo, BigDecimal percProvvigione);
}
