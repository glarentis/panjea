/**
 * 
 */
package it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.provvigioni;

import it.eurotn.panjea.agenti.domain.lite.AgenteLite;
import it.eurotn.panjea.magazzino.domain.IRigaArticoloDocumento;
import it.eurotn.panjea.magazzino.domain.RigaArticolo;
import it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.provvigioni.interfaces.RigaDocumentoVariazioneProvvigioneStrategy;
import it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.provvigioni.interfaces.TipoVariazioneProvvigioneStrategy;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author fattazzo
 * 
 */
public class RigaNonPresenteInRigaVariazioneProvvigioneStrategy implements RigaDocumentoVariazioneProvvigioneStrategy,
		Serializable {

	private static final long serialVersionUID = -584998007901102876L;

	@Override
	public IRigaArticoloDocumento applicaVariazione(IRigaArticoloDocumento rigaArticolo, BigDecimal percProvvigione,
			TipoVariazioneProvvigioneStrategy provvigioneStrategy) {

		boolean provvigionePresente = rigaArticolo.getPercProvvigione() != null;

		boolean agentePresente = true;
		// solo sulla riga articolo di magazzino ho l'agente quindi devo tenerlo in considerazione, per questo mi tocca
		// usare il cast per eseguire il controllo
		if (rigaArticolo instanceof RigaArticolo) {
			AgenteLite agente = ((RigaArticolo) rigaArticolo).getAgente();
			agentePresente = agente != null && !agente.isNew();
		}

		if (!provvigionePresente && agentePresente) {
			BigDecimal newProvvigione = provvigioneStrategy.calcola(rigaArticolo, percProvvigione);
			rigaArticolo.setPercProvvigione(newProvvigione);
		}

		return rigaArticolo;
	}

}
