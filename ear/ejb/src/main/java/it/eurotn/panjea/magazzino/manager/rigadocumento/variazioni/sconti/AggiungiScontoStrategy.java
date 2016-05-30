/**
 * 
 */
package it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.sconti;

import it.eurotn.panjea.magazzino.domain.IRigaArticoloDocumento;
import it.eurotn.panjea.magazzino.domain.Sconto;
import it.eurotn.panjea.magazzino.domain.moduloprezzo.PoliticaPrezzo;
import it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.sconti.interfaces.TipoVariazioneScontoStrategy;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author fattazzo
 * 
 */
public class AggiungiScontoStrategy implements TipoVariazioneScontoStrategy, Serializable {

	private static final long serialVersionUID = 1117916437786219941L;

	@Override
	public Sconto calcola(IRigaArticoloDocumento rigaArticolo, BigDecimal variazione) {

		Sconto sconto = new Sconto(rigaArticolo.getVariazione1(), rigaArticolo.getVariazione2(),
				rigaArticolo.getVariazione3(), rigaArticolo.getVariazione4());

		PoliticaPrezzo politicaPrezzo = rigaArticolo.getPoliticaPrezzo();
		sconto.aggiungiInCoda(variazione, politicaPrezzo != null && politicaPrezzo.isSconto1Bloccato());
		return sconto;
	}

}
