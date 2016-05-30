package it.eurotn.panjea.beniammortizzabili.rich.rules;

import it.eurotn.panjea.beniammortizzabili.rich.search.RicercaBeniAmmortizzabiliDialog.BeneAmmortizzabileRicerca;
import it.eurotn.panjea.beniammortizzabili2.domain.BeneAmmortizzabile;
import it.eurotn.panjea.beniammortizzabili2.domain.CriteriaRicercaBeniAmmortizzabili;
import it.eurotn.panjea.beniammortizzabili2.domain.Simulazione;
import it.eurotn.panjea.beniammortizzabili2.domain.SottoSpecie;
import it.eurotn.panjea.beniammortizzabili2.domain.Specie;
import it.eurotn.panjea.beniammortizzabili2.domain.TipologiaEliminazione;
import it.eurotn.panjea.beniammortizzabili2.domain.Ubicazione;
import it.eurotn.panjea.beniammortizzabili2.domain.VenditaBene;
import it.eurotn.panjea.rules.AbstractPluginRulesSource;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.rules.Rules;
import org.springframework.rules.constraint.Constraint;

public class BeniAmmortizzabiliPluginRulesSource extends AbstractPluginRulesSource {

	private Rules createBeneAmmortizzabileRicercaRules() {
		return new Rules(BeneAmmortizzabileRicerca.class) {
			@Override
			protected void initRules() {
				add("codiceBene", getNumericConstraint());
				add("annoAcquisto", getNumericConstraint());
			}
		};
	}

	private Rules createBeneAmmortizzabileRules() {
		return new Rules(BeneAmmortizzabile.class) {
			@Override
			protected void initRules() {
				add(BeneAmmortizzabile.PROP_DESCRIZIONE, getRequiredConstraint());
				add("sottoSpecie", getDomainAttributeRequiredConstraint());
				// add("ubicazione",getDomainAttributeRequiredConstraint());
				add(BeneAmmortizzabile.PROP_ANNO_ACQUISTO, new Constraint[] { getRequiredConstraint(),
						getLengthConstraint(4) });
				add(BeneAmmortizzabile.PROP_BENE_DI_PROPRIETA, getRequiredConstraint());
				add(BeneAmmortizzabile.PROP_BENE_IN_LEASING, getRequiredConstraint());
				add(BeneAmmortizzabile.PROP_MATRICOLA_AZIENDALE, maxLength(15));
				add(BeneAmmortizzabile.PROP_MATRICOLA_FORNITORE, maxLength(15));
			}
		};
	}

	private Rules createReportAmmortamenti() {
		return new Rules(CriteriaRicercaBeniAmmortizzabili.class) {
			@Override
			protected void initRules() {
				add(CriteriaRicercaBeniAmmortizzabili.PROP_ANNOAMMORTAMENTO, getRequiredConstraint());
				add(CriteriaRicercaBeniAmmortizzabili.PROP_ANNO, getRequiredConstraint());
			}
		};
	}

	private Rules createSimulazioneRules() {
		return new Rules(Simulazione.class) {
			@Override
			protected void initRules() {
				add(Simulazione.PROP_DATA, getRequiredConstraint());
				add(Simulazione.PROP_DESCRIZIONE, getRequiredConstraint());
			}
		};
	}

	private Rules createSottoSpecieRules() {
		return new Rules(SottoSpecie.class) {
			@Override
			protected void initRules() {
				add(SottoSpecie.PROP_CODICE, getRequiredConstraint());
				add(SottoSpecie.PROP_CODICE, maxLength(2));
				add(Specie.PROP_DESCRIZIONE, getRequiredConstraint());
			}
		};
	}

	private Rules createSpecieRules() {
		return new Rules(Specie.class) {
			@Override
			protected void initRules() {
				add(Specie.PROP_CODICE, getRequiredConstraint());
				add(Specie.PROP_CODICE, maxLength(2));
				add(Specie.PROP_DESCRIZIONE, getRequiredConstraint());
			}
		};
	}

	private Rules createTipologiaEliminazioneRules() {
		return new Rules(TipologiaEliminazione.class) {
			@Override
			protected void initRules() {
				add(TipologiaEliminazione.PROP_CODICE, getRequiredConstraint());
				add(TipologiaEliminazione.PROP_DESCRIZIONE, getRequiredConstraint());
			}
		};
	}

	private Rules createUbicazioneRules() {
		return new Rules(Ubicazione.class) {
			@Override
			protected void initRules() {
				add(Ubicazione.PROP_CODICE, getRequiredConstraint());
				add(Ubicazione.PROP_DESCRIZIONE, getRequiredConstraint());
			}
		};
	}

	/**
	 * Regole di validazione della vendita bene.
	 * 
	 * @return regole di validazione
	 */
	private Rules createVenditaBeneRules() {
		return new Rules(VenditaBene.class) {
			@Override
			protected void initRules() {
				add(VenditaBene.PROP_DATA_VENDITA, getRequiredConstraint());
				add(VenditaBene.PROP_IMPORTO_STORNO_VALORE_BENE,
						new Constraint[] { getRequiredConstraint(),
								not(eq(BigDecimal.ZERO, new Comparator<BigDecimal>() {

									@Override
									public int compare(BigDecimal o1, BigDecimal o2) {
										DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
										return decimalFormat.format(o2).equals(decimalFormat.format(o1)) ? 0 : -1;
									}
								})) });
			}
		};
	}

	@Override
	public List<Rules> getRules() {
		List<Rules> listRules = new ArrayList<Rules>();

		// beniammortizzabili
		listRules.add(createSpecieRules());
		listRules.add(createSottoSpecieRules());
		listRules.add(createBeneAmmortizzabileRicercaRules());
		listRules.add(createBeneAmmortizzabileRules());
		listRules.add(createVenditaBeneRules());

		// tabelle generali beni ammortizzabili
		listRules.add(createUbicazioneRules());
		listRules.add(createTipologiaEliminazioneRules());

		// report
		listRules.add(createReportAmmortamenti());

		listRules.add(createSimulazioneRules());
		return listRules;
	}

}
