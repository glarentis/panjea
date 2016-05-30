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
public class RigaPresenteInRigaVariazioneProvivgioneStrategy implements RigaDocumentoVariazioneProvvigioneStrategy,
		Serializable {

	private static final long serialVersionUID = 6036423024996134020L;

	@Override
	public IRigaArticoloDocumento applicaVariazione(IRigaArticoloDocumento rigaArticolo, BigDecimal percProvvigione,
			TipoVariazioneProvvigioneStrategy provvigioneStrategy) {

		boolean provvigionePresente = rigaArticolo.getPercProvvigione() != null;

		// solo sulla riga articolo di magazzino ho l'agente quindi devo tenerlo in considerazione, per questo mi tocca
		// usare il cast per eseguire il controllo
		if (rigaArticolo instanceof RigaArticolo) {
			AgenteLite agente = ((RigaArticolo) rigaArticolo).getAgente();
			provvigionePresente = provvigionePresente && agente != null && !agente.isNew();
		}

		if (provvigionePresente) {
			BigDecimal newProvvigione = provvigioneStrategy.calcola(rigaArticolo, percProvvigione);
			rigaArticolo.setPercProvvigione(newProvvigione);
		}

		return rigaArticolo;
	}

}
