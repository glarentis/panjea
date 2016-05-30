/**
 * 
 */
package it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.provvigioni;

import it.eurotn.panjea.magazzino.domain.IRigaArticoloDocumento;
import it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.provvigioni.interfaces.RigaDocumentoVariazioneProvvigioneStrategy;
import it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.provvigioni.interfaces.TipoVariazioneProvvigioneStrategy;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author fattazzo
 * 
 */
public class RigaTutteVariazioneProvvigioneStrategy implements RigaDocumentoVariazioneProvvigioneStrategy, Serializable {

	private static final long serialVersionUID = -49061844043609906L;

	@Override
	public IRigaArticoloDocumento applicaVariazione(IRigaArticoloDocumento rigaArticolo, BigDecimal percProvvigione,
			TipoVariazioneProvvigioneStrategy provvigioneStrategy) {

		BigDecimal newPercProvvigione = provvigioneStrategy.calcola(rigaArticolo, percProvvigione);
		rigaArticolo.setPercProvvigione(newPercProvvigione);

		return rigaArticolo;
	}

}
