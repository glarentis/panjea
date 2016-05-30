/**
 *
 */
package it.eurotn.panjea.beniammortizzabili2.manager.contabilita;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.beniammortizzabili.exception.SottocontiBeniNonValidiException;
import it.eurotn.panjea.beniammortizzabili2.domain.PoliticaCalcolo;
import it.eurotn.panjea.beniammortizzabili2.domain.PoliticaCalcoloSpecie;
import it.eurotn.panjea.beniammortizzabili2.domain.Simulazione;
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
@Stateless(mappedName = "Panjea.SpecieGeneratoreAreaContabileManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.SpecieGeneratoreAreaContabileManager")
public class SpecieGeneratoreAreaContabileManagerBean extends GeneratoreAreaContabileAbstractManagerBean {

	@Override
	protected List<ImportoSottoconti> caricaImportoSottoconti(Simulazione simulazione)
			throws SottocontiBeniNonValidiException {

		List<ImportoSottoconti> sottoContiResult = new ArrayList<>();
		Map<EntityBase, SottocontiBeni> specieNonValide = new HashMap<EntityBase, SottocontiBeni>();

		// per ogni specie verifico che i conti siano inseriti correttamente altrimenti alla fine rilancio una eccezione
		// di tutte quelle a cui mancano
		for (PoliticaCalcolo politicaCalcolo : simulazione.getPoliticheCalcolo()) {

			if (politicaCalcolo instanceof PoliticaCalcoloSpecie) {
				Specie specie = ((PoliticaCalcoloSpecie) politicaCalcolo).getSpecie();

				SottocontiBeni sottocontiBeni = new SottocontiBeni();
				sottocontiBeni.setSottoContoAmmortamento(specie.getSottoContoAmmortamento());
				sottocontiBeni.setSottoContoFondoAmmortamento(specie.getSottoContoFondoAmmortamento());
				sottocontiBeni.setSottoContoAmmortamentoAnticipato(specie.getSottoContoAmmortamentoAnticipato());
				sottocontiBeni.setSottoContoFondoAmmortamentoAnticipato(specie
						.getSottoContoFondoAmmortamentoAnticipato());

				if (sottocontiBeni.isSottoContiGenerazioneAreaContabileValid()) {

					ImportoSottoconti importoSottoconti = new ImportoSottoconti(politicaCalcolo, sottocontiBeni);
					sottoContiResult.add(importoSottoconti);
				} else {
					specieNonValide.put(specie, sottocontiBeni);
				}
			}
		}

		// se ho specie con sottoconti non validi rilancio l'eccezione
		if (!specieNonValide.isEmpty()) {
			throw new SottocontiBeniNonValidiException(specieNonValide);
		}

		return sottoContiResult;
	}

}
