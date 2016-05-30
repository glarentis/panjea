/**
 * 
 */
package it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.sconti;

import it.eurotn.panjea.magazzino.domain.IRigaArticoloDocumento;
import it.eurotn.panjea.magazzino.domain.Sconto;
import it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.sconti.interfaces.RigaDocumentoVariazioneScontoStrategy;
import it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.sconti.interfaces.TipoVariazioneScontoStrategy;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author fattazzo
 * 
 */
public class RigaTutteVariazioneScontoStrategy implements RigaDocumentoVariazioneScontoStrategy, Serializable {

	private static final long serialVersionUID = 8580120419431550879L;

	@Override
	public IRigaArticoloDocumento applicaVariazione(IRigaArticoloDocumento rigaArticolo, BigDecimal variazione,
			TipoVariazioneScontoStrategy scontoStrategy) {

		Sconto sconto = scontoStrategy.calcola(rigaArticolo, variazione);

		rigaArticolo.setVariazione1(sconto.getSconto1());
		rigaArticolo.setVariazione2(sconto.getSconto2());
		rigaArticolo.setVariazione3(sconto.getSconto3());
		rigaArticolo.setVariazione4(sconto.getSconto4());

		return rigaArticolo;
	}

}
