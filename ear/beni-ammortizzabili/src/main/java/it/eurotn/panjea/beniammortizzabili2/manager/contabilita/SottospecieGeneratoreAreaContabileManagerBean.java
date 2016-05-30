/**
 *
 */
package it.eurotn.panjea.beniammortizzabili2.manager.contabilita;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.beniammortizzabili.exception.SottocontiBeniNonValidiException;
import it.eurotn.panjea.beniammortizzabili2.domain.PoliticaCalcolo;
import it.eurotn.panjea.beniammortizzabili2.domain.PoliticaCalcoloSottoSpecie;
import it.eurotn.panjea.beniammortizzabili2.domain.Simulazione;
import it.eurotn.panjea.beniammortizzabili2.domain.SottoSpecie;
import it.eurotn.panjea.beniammortizzabili2.domain.SottocontiBeni;
import it.eurotn.panjea.beniammortizzabili2.domain.Specie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

/**
 * @author fattazzo
 *
 */
@Stateless(mappedName = "Panjea.SottospecieGeneratoreAreaContabileManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.SottospecieGeneratoreAreaContabileManager")
public class SottospecieGeneratoreAreaContabileManagerBean extends GeneratoreAreaContabileAbstractManagerBean {

	@Override
	protected List<ImportoSottoconti> caricaImportoSottoconti(Simulazione simulazione)
			throws SottocontiBeniNonValidiException {

		List<ImportoSottoconti> sottoContiResult = new ArrayList<>();
		Map<EntityBase, SottocontiBeni> sottospecieNonValide = new HashMap<EntityBase, SottocontiBeni>();

		// per ogni sottospecie verifico che i conti siano inseriti correttamente altrimenti alla fine rilancio una
		// eccezione
		// di tutte quelle a cui mancano
		for (PoliticaCalcolo politicaCalcolo : simulazione.getPoliticheCalcolo()) {
			if (politicaCalcolo instanceof PoliticaCalcoloSottoSpecie) {
				SottoSpecie sottoSpecie = ((PoliticaCalcoloSottoSpecie) politicaCalcolo).getSottoSpecie();

				SottocontiBeni sottocontiBeni = getSottoContiSottoSpecie(sottoSpecie);

				if (sottocontiBeni.isSottoContiGenerazioneAreaContabileValid()) {

					ImportoSottoconti importoSottoconti = new ImportoSottoconti(politicaCalcolo, sottocontiBeni);
					sottoContiResult.add(importoSottoconti);
				} else {
					sottospecieNonValide.put(sottoSpecie, sottocontiBeni);
				}
			}
		}

		// se ho specie con sottoconti non validi rilancio l'eccezione
		if (!sottospecieNonValide.isEmpty()) {
			throw new SottocontiBeniNonValidiException(sottospecieNonValide);
		}

		return sottoContiResult;
	}

	private SottocontiBeni getSottoContiSottoSpecie(SottoSpecie sottoSpecie) {

		SottocontiBeni sottocontiBeniSottoSpecie = new SottocontiBeni();
		sottocontiBeniSottoSpecie.setSottoContoAmmortamento(sottoSpecie.getSottoContoAmmortamento());
		sottocontiBeniSottoSpecie.setSottoContoFondoAmmortamento(sottoSpecie.getSottoContoFondoAmmortamento());
		sottocontiBeniSottoSpecie
				.setSottoContoAmmortamentoAnticipato(sottoSpecie.getSottoContoAmmortamentoAnticipato());
		sottocontiBeniSottoSpecie.setSottoContoFondoAmmortamentoAnticipato(sottoSpecie
				.getSottoContoFondoAmmortamentoAnticipato());

		// aggiungo i sottoconti della specie dove non sono presenti
		Specie specie = sottoSpecie.getSpecie();
		SottocontiBeni sottocontiBeniSpecie = new SottocontiBeni();
		sottocontiBeniSpecie.setSottoContoAmmortamento(specie.getSottoContoAmmortamento());
		sottocontiBeniSpecie.setSottoContoFondoAmmortamento(specie.getSottoContoFondoAmmortamento());
		sottocontiBeniSpecie.setSottoContoAmmortamentoAnticipato(specie.getSottoContoAmmortamentoAnticipato());
		sottocontiBeniSpecie
				.setSottoContoFondoAmmortamentoAnticipato(specie.getSottoContoFondoAmmortamentoAnticipato());
		sottocontiBeniSottoSpecie.avvaloraSottoContiMancanti(sottocontiBeniSpecie);

		return sottocontiBeniSottoSpecie;
	}

}
