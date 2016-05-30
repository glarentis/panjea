/**
 *
 */
package it.eurotn.panjea.beniammortizzabili2.manager.contabilita;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.beniammortizzabili.exception.SottocontiBeniNonValidiException;
import it.eurotn.panjea.beniammortizzabili2.domain.BeneAmmortizzabileLite;
import it.eurotn.panjea.beniammortizzabili2.domain.PoliticaCalcolo;
import it.eurotn.panjea.beniammortizzabili2.domain.PoliticaCalcoloBene;
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
@Stateless(mappedName = "Panjea.BeneGeneratoreAreaContabileManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.BeneGeneratoreAreaContabileManager")
public class BeneGeneratoreAreaContabileManagerBean extends GeneratoreAreaContabileAbstractManagerBean {

	@Override
	protected List<ImportoSottoconti> caricaImportoSottoconti(Simulazione simulazione)
			throws SottocontiBeniNonValidiException {
		List<ImportoSottoconti> sottoContiResult = new ArrayList<>();
		Map<EntityBase, SottocontiBeni> beniNonValidi = new HashMap<EntityBase, SottocontiBeni>();

		// per ogni bene verifico che i conti siano inseriti correttamente altrimenti alla fine rilancio una
		// eccezione di tutti quelli a cui mancano
		for (PoliticaCalcolo politicaCalcolo : simulazione.getPoliticheCalcolo()) {

			if (politicaCalcolo instanceof PoliticaCalcoloBene) {
				BeneAmmortizzabileLite bene = ((PoliticaCalcoloBene) politicaCalcolo).getBene();

				SottocontiBeni sottocontiBeni = getSottoContiBene(bene);

				if (sottocontiBeni.isSottoContiGenerazioneAreaContabileValid()) {

					ImportoSottoconti importoSottoconti = new ImportoSottoconti(politicaCalcolo, sottocontiBeni);
					sottoContiResult.add(importoSottoconti);
				} else {
					beniNonValidi.put(bene, sottocontiBeni);
				}
			}
		}

		// se ho specie con sottoconti non validi rilancio l'eccezione
		if (!beniNonValidi.isEmpty()) {
			throw new SottocontiBeniNonValidiException(beniNonValidi);
		}

		return sottoContiResult;
	}

	private SottocontiBeni getSottoContiBene(BeneAmmortizzabileLite bene) {

		SottocontiBeni sottocontiBeni = new SottocontiBeni();
		sottocontiBeni.setSottoContoAmmortamento(bene.getSottoContoAmmortamento());
		sottocontiBeni.setSottoContoFondoAmmortamento(bene.getSottoContoFondoAmmortamento());
		sottocontiBeni.setSottoContoAmmortamentoAnticipato(bene.getSottoContoAmmortamentoAnticipato());
		sottocontiBeni.setSottoContoFondoAmmortamentoAnticipato(bene.getSottoContoFondoAmmortamentoAnticipato());

		SottoSpecie sottoSpecie = bene.getSottoSpecie();
		SottocontiBeni sottocontiBeniSottoSpecie = new SottocontiBeni();
		sottocontiBeniSottoSpecie.setSottoContoAmmortamento(sottoSpecie.getSottoContoAmmortamento());
		sottocontiBeniSottoSpecie.setSottoContoFondoAmmortamento(sottoSpecie.getSottoContoFondoAmmortamento());
		sottocontiBeniSottoSpecie
				.setSottoContoAmmortamentoAnticipato(sottoSpecie.getSottoContoAmmortamentoAnticipato());
		sottocontiBeniSottoSpecie.setSottoContoFondoAmmortamentoAnticipato(sottoSpecie
				.getSottoContoFondoAmmortamentoAnticipato());
		sottocontiBeni.avvaloraSottoContiMancanti(sottocontiBeniSottoSpecie);

		// aggiungo i sottoconti della specie dove non sono presenti
		Specie specie = sottoSpecie.getSpecie();
		SottocontiBeni sottocontiBeniSpecie = new SottocontiBeni();
		sottocontiBeniSpecie.setSottoContoAmmortamento(specie.getSottoContoAmmortamento());
		sottocontiBeniSpecie.setSottoContoFondoAmmortamento(specie.getSottoContoFondoAmmortamento());
		sottocontiBeniSpecie.setSottoContoAmmortamentoAnticipato(specie.getSottoContoAmmortamentoAnticipato());
		sottocontiBeniSpecie
				.setSottoContoFondoAmmortamentoAnticipato(specie.getSottoContoFondoAmmortamentoAnticipato());
		sottocontiBeni.avvaloraSottoContiMancanti(sottocontiBeniSpecie);

		return sottocontiBeni;
	}

}
