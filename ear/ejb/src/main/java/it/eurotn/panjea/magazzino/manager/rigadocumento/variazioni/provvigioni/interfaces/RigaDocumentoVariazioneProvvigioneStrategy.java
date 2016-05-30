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
public interface RigaDocumentoVariazioneProvvigioneStrategy {

	/**
	 * Applica la variazione della provvigione alla riga articolo.
	 * 
	 * @param rigaArticolo
	 *            riga articolo
	 * @param percProvvigione
	 *            percentuale di provvigione da applicare
	 * @param provvigioneStrategy
	 *            strategia di applicazione della provvigione
	 * @return riga modificata
	 */
	IRigaArticoloDocumento applicaVariazione(IRigaArticoloDocumento rigaArticolo, BigDecimal percProvvigione,
			TipoVariazioneProvvigioneStrategy provvigioneStrategy);
}
