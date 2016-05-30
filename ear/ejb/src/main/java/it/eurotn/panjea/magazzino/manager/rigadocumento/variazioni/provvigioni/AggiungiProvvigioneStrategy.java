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
public class AggiungiProvvigioneStrategy implements TipoVariazioneProvvigioneStrategy, Serializable {

	private static final long serialVersionUID = -3771823965457875941L;

	@Override
	public BigDecimal calcola(IRigaArticoloDocumento rigaArticolo, BigDecimal percProvvigione) {

		BigDecimal percProvvPrec = (BigDecimal) ObjectUtils.defaultIfNull(rigaArticolo.getPercProvvigione(),
				BigDecimal.ZERO);

		percProvvigione = (BigDecimal) ObjectUtils.defaultIfNull(percProvvigione, BigDecimal.ZERO);

		return percProvvPrec.add(percProvvigione);
	}

}
