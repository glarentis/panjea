/**
 * 
 */
package it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.sconti;

import it.eurotn.panjea.magazzino.domain.IRigaArticoloDocumento;
import it.eurotn.panjea.magazzino.domain.Sconto;
import it.eurotn.panjea.magazzino.domain.moduloprezzo.PoliticaPrezzo;
import it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.sconti.interfaces.RigaDocumentoVariazioneScontoStrategy;
import it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.sconti.interfaces.TipoVariazioneScontoStrategy;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author fattazzo
 * 
 */
public class RigaPresenteDaContrattoVariazioneScontoStrategy implements RigaDocumentoVariazioneScontoStrategy,
		Serializable {

	private static final long serialVersionUID = -4928519323622154858L;

	@Override
	public IRigaArticoloDocumento applicaVariazione(IRigaArticoloDocumento rigaArticolo, BigDecimal variazione,
			TipoVariazioneScontoStrategy scontoStrategy) {

		PoliticaPrezzo politicaPrezzo = rigaArticolo.getPoliticaPrezzo();

		boolean variazioniPresenti = politicaPrezzo != null && politicaPrezzo.isPoliticaScontiPresenti();

		if (variazioniPresenti) {
			// se ho lo sconto1 bloccato e almeno lo sconto2 è diverso da 0 significa che ho degli sconti che provengono
			// dai contratti ( sconto1 bloccato = sconto commerciale sul pagamento )
			boolean scontiDaContrattoPresenti = !politicaPrezzo.isSconto1Bloccato()
					|| politicaPrezzo.isSconto1Bloccato()
					&& politicaPrezzo.getSconti().getRisultatoPrezzo(rigaArticolo.getQta()).getValue().getSconto2()
							.compareTo(BigDecimal.ZERO) != 0;

			variazioniPresenti = variazioniPresenti && scontiDaContrattoPresenti;
		}

		if (variazioniPresenti) {
			Sconto sconto = scontoStrategy.calcola(rigaArticolo, variazione);

			rigaArticolo.setVariazione1(sconto.getSconto1());
			rigaArticolo.setVariazione2(sconto.getSconto2());
			rigaArticolo.setVariazione3(sconto.getSconto3());
			rigaArticolo.setVariazione4(sconto.getSconto4());
		}

		return rigaArticolo;
	}

}
