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
public class RigaNonPresentiInRigaVariazioneScontoStrategy implements RigaDocumentoVariazioneScontoStrategy,
		Serializable {

	private static final long serialVersionUID = -9037514146584971181L;

	@Override
	public IRigaArticoloDocumento applicaVariazione(IRigaArticoloDocumento rigaArticolo, BigDecimal variazione,
			TipoVariazioneScontoStrategy scontoStrategy) {

		// considero la variazione1 come vuota se lo sconto1 è bloccato e quindi si tratta di uno sconto commerciale del
		// pagamento
		BigDecimal variazione1 = rigaArticolo.getPoliticaPrezzo().isSconto1Bloccato() ? BigDecimal.ZERO : rigaArticolo
				.getVariazione1();

		Sconto scontoPrec = new Sconto(variazione1, rigaArticolo.getVariazione2(), rigaArticolo.getVariazione3(),
				rigaArticolo.getVariazione4());

		if (scontoPrec.isEmpty()) {
			Sconto sconto = scontoStrategy.calcola(rigaArticolo, variazione);

			rigaArticolo.setVariazione1(sconto.getSconto1());
			rigaArticolo.setVariazione2(sconto.getSconto2());
			rigaArticolo.setVariazione3(sconto.getSconto3());
			rigaArticolo.setVariazione4(sconto.getSconto4());
		}

		return rigaArticolo;
	}

}
