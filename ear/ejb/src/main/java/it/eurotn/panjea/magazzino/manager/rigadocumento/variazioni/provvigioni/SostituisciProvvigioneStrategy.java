/**
 * 
 */
package it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.provvigioni;

import it.eurotn.panjea.magazzino.domain.IRigaArticoloDocumento;
import it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.provvigioni.interfaces.TipoVariazioneProvvigioneStrategy;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang3.ObjectUtils;

/**
 * @author fattazzo
 * 
 */
public class SostituisciProvvigioneStrategy implements TipoVariazioneProvvigioneStrategy, Serializable {

	private static final long serialVersionUID = 2971035517606371937L;

	@Override
	public BigDecimal calcola(IRigaArticoloDocumento rigaArticolo, BigDecimal percProvvigione) {

		return (BigDecimal) ObjectUtils.defaultIfNull(percProvvigione, BigDecimal.ZERO);
	}

}
