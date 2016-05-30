package it.eurotn.panjea.intra.rich.rules;

import it.eurotn.panjea.intra.domain.AreaIntra;
import it.eurotn.panjea.intra.domain.DichiarazioneIntra.TipoDichiarazione;
import it.eurotn.panjea.intra.domain.DichiarazioneIntraAcquisti;
import it.eurotn.panjea.intra.domain.DichiarazioneIntraVendite;
import it.eurotn.panjea.intra.domain.Nomenclatura;
import it.eurotn.panjea.intra.domain.RigaBeneIntra;
import it.eurotn.panjea.intra.domain.RigaServizioIntra;
import it.eurotn.panjea.intra.domain.RigaSezione1Intra;
import it.eurotn.panjea.intra.domain.RigaSezione2Intra;
import it.eurotn.panjea.intra.domain.RigaSezione3Intra;
import it.eurotn.panjea.intra.domain.RigaSezione4Intra;
import it.eurotn.panjea.intra.domain.TipoPeriodo;
import it.eurotn.panjea.intra.manager.interfaces.ServizioManager;
import it.eurotn.panjea.rich.rules.PropertyResolvableConstraint;
import it.eurotn.panjea.rules.AbstractPluginRulesSource;

import java.util.ArrayList;
import java.util.List;

import org.springframework.rules.Rules;
import org.springframework.rules.constraint.And;
import org.springframework.rules.constraint.CompoundConstraint;
import org.springframework.rules.constraint.Constraint;
import org.springframework.rules.constraint.property.ConditionalPropertyConstraint;
import org.springframework.rules.constraint.property.PropertyConstraint;
import org.springframework.rules.constraint.property.PropertyValueConstraint;
import org.springframework.rules.constraint.property.RequiredIfOthersPresent;
import org.springframework.rules.constraint.property.RequiredIfTrue;

public class IntraPluginRulesSource extends AbstractPluginRulesSource {

	/**
	 * @return regole per l'area intra
	 */
	private Rules createAreaIntraRules() {
		return new Rules(AreaIntra.class) {
			@Override
			protected void initRules() {

				// sempre obbligatorie
				add("naturaTransazione", getRequiredConstraint());
				add("modalitaIncasso", getRequiredConstraint());

				// solo per dichiarazioni mensili (il tipoPeriodo viene riportato anche sull'area intra)
				add(getPropertyMensileAreaIntraConstraint("modalitaTrasporto"));
				add(getPropertyMensileAreaIntraConstraint("gruppoCondizioneConsegna"));
				add(getPropertyMensileAreaIntraConstraint("provincia"));
				add(getPropertyMensileAreaIntraConstraint("paese"));
			}
		};
	}

	/**
	 * @return regole per la validatzione di {@link DichiarazioneIntraAcquisti}
	 */
	private Rules createDichiarazioneIntraAcquistiRules() {
		return new Rules(DichiarazioneIntraAcquisti.class) {
			@Override
			protected void initRules() {
				add("anno", getMaxCharConstraint(4));
				add(getMeseDichiarazioneConstraint());
				add(getTrimestreDichiarazioneConstraint());
				add("soggettoObbligato.partitaIva", getPartitaIVAConstraint());
				add("soggettoDelegato.partitaIvaSoggettoDelegato", getPartitaIVAConstraint());
			}
		};
	}

	/**
	 * @return regole per la validatzione di {@link DichiarazioneIntraAcquisti}
	 */
	private Rules createDichiarazioneIntraVenditeRules() {
		return new Rules(DichiarazioneIntraVendite.class) {
			@Override
			protected void initRules() {
				add("anno", getMaxCharConstraint(4));
				add(getMeseDichiarazioneConstraint());
				add(getTrimestreDichiarazioneConstraint());
				add("soggettoObbligato.partitaIva", getPartitaIVAConstraint());
				add("soggettoDelegato.partitaIvaSoggettoDelegato", getPartitaIVAConstraint());
			}
		};
	}

	/**
	 * @return regole per la validatzione di {@link ServizioManager}
	 */
	private Rules createNomenclaturaRules() {
		return new Rules(Nomenclatura.class) {
			@Override
			protected void initRules() {
				add("codice", getRequiredConstraint());
				add("codice", getMaxCharConstraint(10));
				add("descrizione", getRequiredConstraint());
				add("descrizione", getMaxCharConstraint(1000));
			}
		};
	}

	/**
	 * 
	 * @return regole validazione rigaBeneIntra
	 */
	private Rules createRigaBeneIntraRules() {
		return new Rules(RigaBeneIntra.class) {
			@Override
			protected void initRules() {
				add("nomenclatura", getRequiredConstraint());
			}
		};
	}

	/**
	 * 
	 * @return regole validazione rigaBeneIntra
	 */
	private Rules createRigaServizioIntraRules() {
		return new Rules(RigaServizioIntra.class) {
			@Override
			protected void initRules() {
				add("servizio", getRequiredConstraint());
			}
		};
	}

	/**
	 * @return regole per la validatzione di {@link RigaSezione1Intra}
	 */
	private Rules createRigaSezione1IntraRules() {
		return new Rules(RigaSezione1Intra.class) {
			@Override
			protected void initRules() {
				add("progressivo", getRequiredConstraint());
				add("fornitoreStato", getRequiredConstraint());
				add("fornitorepiva", getRequiredConstraint());
				add("importo", getRequiredConstraint());
				add(getPropertyMensileRigaSezioneConstraint("naturaTransazione", TipoPeriodo.M));
				add("nomenclatura", getRequiredConstraint());
				// add(new AbstractPropertyConstraint("massaSupplementare") {
				//
				// @Override
				// protected boolean test(PropertyAccessStrategy domainObjectAccessStrategy) {
				// RigaSezione1Intra rigaSezione1Intra = (RigaSezione1Intra) domainObjectAccessStrategy
				// .getDomainObject();
				// Long massaSupplementare = rigaSezione1Intra.getMassaSupplementare();
				// TipoPeriodo tipoPeriodo = rigaSezione1Intra.getDichiarazione() != null ? rigaSezione1Intra
				// .getDichiarazione().getTipoPeriodo() : null;
				// TipoDichiarazione tipoDichiarazione = rigaSezione1Intra.getDichiarazione() != null ?
				// rigaSezione1Intra
				// .getDichiarazione().getTipoDichiarazione() : null;
				// Nomenclatura nomenclatura = rigaSezione1Intra.getNomenclatura();
				//
				// if (massaSupplementare == null && TipoPeriodo.M.equals(tipoPeriodo)
				// && TipoDichiarazione.ACQUISTI.equals(tipoDichiarazione)
				// && nomenclatura.getUmsupplementare() != null) {
				// return true;
				// }
				//
				// return true;
				// }
				// });

				// add("massaSupplementare", getMassaSupplementareConstraint());
				// add("massaNetta", getMassaConstraint());
				add(getPropertyMensileRigaSezioneConstraint("valoreStatisticoEuro", TipoPeriodo.M));
				add(getPropertyMensileRigaSezioneConstraint("gruppoCondizioneConsegna", TipoPeriodo.M));
				add(getPropertyMensileRigaSezioneConstraint("modalitaTrasporto", TipoPeriodo.M));
				// add(getPropertyMensileRigaSezioneConstraint("paese", TipoPeriodo.M));
				add(getPropertyMensileRigaSezioneConstraint("provincia", TipoPeriodo.M));
				add(getPropertyMensileRigaSezioneConstraint("paeseOrigineArticolo", TipoPeriodo.M,
						TipoDichiarazione.ACQUISTI));

			}
		};
	}

	/**
	 * @return regole per la validatzione di {@link RigaSezione1Intra}
	 */
	private Rules createRigaSezione2IntraRules() {
		return new Rules(RigaSezione2Intra.class) {
			@Override
			protected void initRules() {
				add("progressivo", getRequiredConstraint());
				add("mese", getRequiredConstraint());
				add(new PropertyValueConstraint("mese", range(1, 12)));
				add(new PropertyValueConstraint("trimestre", range(1, 4)));
				add("anno", getLengthConstraint(4));
				add("trimestre", getRequiredConstraint());
				add("anno", getRequiredConstraint());
				add("fornitoreStato", getRequiredConstraint());
				add("fornitorepiva", getRequiredConstraint());
				add("importo", getRequiredConstraint());
				add("naturaTransazione", getRequiredConstraint());
				add("nomenclatura", getRequiredConstraint());
				add(getPropertyMensileRigaSezioneConstraint("valoreStatisticoEuro", TipoPeriodo.M));
			}
		};
	}

	/**
	 * @return regole per la validatzione di {@link RigaSezione1Intra}
	 */
	private Rules createRigaSezione3IntraRules() {
		return new Rules(RigaSezione3Intra.class) {
			@Override
			protected void initRules() {
				add("progressivo", getRequiredConstraint());
				add("fornitoreStato", getRequiredConstraint());
				add("fornitorepiva", getRequiredConstraint());
				add("importo", getRequiredConstraint());
				add("numeroFattura", getRequiredConstraint());
				add("dataFattura", getRequiredConstraint());
				add("servizio", getRequiredConstraint());
				add("modalitaErogazione", getRequiredConstraint());
				add("modalitaIncasso", getRequiredConstraint());
				add("paesePagamento", getRequiredConstraint());
				getPropertyTipoDichiarazioneRigaSezioneConstraint("importoInValuta", TipoDichiarazione.ACQUISTI);
			}
		};
	}

	/**
	 * @return regole per la validatzione di {@link RigaSezione1Intra}
	 */
	private Rules createRigaSezione4IntraRules() {
		return new Rules(RigaSezione4Intra.class) {
			@Override
			protected void initRules() {
				add("progressivo", getRequiredConstraint());
				add("sezioneDoganale", getRequiredConstraint());
				add("anno", getRequiredConstraint());
				add("anno", getLengthConstraint(4));
				add("protocolloDic", getRequiredConstraint());
				add("progrSezione3", getRequiredConstraint());
				add("fornitoreStato", getRequiredConstraint());
				add("fornitorepiva", getRequiredConstraint());
				add("importo", getRequiredConstraint());
				add("numeroFattura", getRequiredConstraint());
				add("dataFattura", getRequiredConstraint());
				add("servizio", getRequiredConstraint());
				add("modalitaErogazione", getRequiredConstraint());
				add("modalitaIncasso", getRequiredConstraint());
				add("paesePagamento", getRequiredConstraint());
			}
		};
	}

	private Constraint getMassaConstraint() {
		PropertyConstraint propertyConstraint = new PropertyValueConstraint("nomenclatura.umsupplementare",
				not(present()));
		PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(propertyConstraint);
		propertyResolvableConstraint.setType("qtaChiusaPresente");

		PropertyConstraint propertyConstraint2 = new PropertyValueConstraint("dichiarazione.tipoPeriodo",
				eq(TipoPeriodo.M));
		PropertyResolvableConstraint propertyResolvableConstraint2 = new PropertyResolvableConstraint(
				propertyConstraint2);
		propertyResolvableConstraint2.setType("qtaChiusaPresente");

		return conjunction().add(propertyResolvableConstraint).add(propertyResolvableConstraint2);
		// PropertyConstraint requiredIfTrue = new RequiredIfTrue("massaNetta",
		// conjunction().add(propertyConstraint).add(
		// propertyConstraint2));
		// PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(requiredIfTrue);
		// propertyResolvableConstraint.setType("massaNettaRequired");
		// return requiredIfTrue;
	}

	private Constraint getMassaSupplementareConstraint() {
		PropertyConstraint propertyConstraint2 = new PropertyValueConstraint("dichiarazione.tipoPeriodo",
				eq(TipoPeriodo.M));
		PropertyResolvableConstraint propertyResolvableConstraint2 = new PropertyResolvableConstraint(
				propertyConstraint2);
		propertyResolvableConstraint2.setType("tipoMensilePresente");

		RequiredIfOthersPresent requiredIfOthersPresent = new RequiredIfOthersPresent("massaSupplementare",
				new String[] { "nomenclatura.umsupplementare" });
		PropertyResolvableConstraint propertyResolvableConstraint3 = new PropertyResolvableConstraint(
				requiredIfOthersPresent);
		propertyResolvableConstraint3.setType("tipoMensilePresente");

		return new And(propertyResolvableConstraint2, propertyResolvableConstraint3);
	}

	/**
	 * @return range di mese da 1 a 12 se tipo periodo = mensile
	 */
	private PropertyConstraint getMeseDichiarazioneConstraint() {
		PropertyConstraint propertyConstraint = new PropertyValueConstraint("tipoPeriodo", eq(TipoPeriodo.M));
		PropertyConstraint domainObjectIfTrue = new ConditionalPropertyConstraint(propertyConstraint,
				new PropertyValueConstraint("mese", range(1, 12)));
		PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(domainObjectIfTrue);
		propertyResolvableConstraint.setType("meseDichiarazioneRangeRequired");
		return propertyResolvableConstraint;
	}

	/**
	 * Property required if tipoPeriodo = mensile.
	 * 
	 * @param propertyName
	 *            la property da rendere obbligatoria se il periodo è mensile
	 * @return PropertyConstraint
	 */
	private PropertyConstraint getPropertyMensileAreaIntraConstraint(String propertyName) {
		PropertyConstraint propertyConstraint = new PropertyValueConstraint("tipoPeriodo", eq(TipoPeriodo.M));
		PropertyConstraint requiredIfTrue = new RequiredIfTrue(propertyName, propertyConstraint);
		PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(requiredIfTrue);
		propertyResolvableConstraint.setType(propertyName + "Required");
		return propertyResolvableConstraint;
	}

	/**
	 * Property required if tipoPeriodo = mensile.
	 * 
	 * @param propertyName
	 *            la property da rendere obbligatoria se il periodo è mensile
	 * @return PropertyConstraint
	 */
	private PropertyConstraint getPropertyMensileRigaSezioneConstraint(String propertyName, TipoPeriodo tipoPeriodo) {
		PropertyConstraint propertyConstraint = new PropertyValueConstraint("dichiarazione.tipoPeriodo",
				eq(tipoPeriodo));
		PropertyConstraint requiredIfTrue = new RequiredIfTrue(propertyName, propertyConstraint);
		PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(requiredIfTrue);
		propertyResolvableConstraint.setType(propertyName + "Required");
		return propertyResolvableConstraint;
	}

	/**
	 * Property required if tipoPeriodo = mensile.
	 * 
	 * @param propertyName
	 *            la property da rendere obbligatoria se il periodo è mensile
	 * @return PropertyConstraint
	 */
	private PropertyConstraint getPropertyMensileRigaSezioneConstraint(String propertyName, TipoPeriodo tipoPeriodo,
			TipoDichiarazione tipoDichiarazione) {
		PropertyConstraint propertyConstraint = new PropertyValueConstraint("dichiarazione.tipoPeriodo",
				eq(tipoPeriodo));
		PropertyConstraint propertyConstraint2 = new PropertyValueConstraint("dichiarazione.tipoDichiarazione",
				eq(tipoDichiarazione));
		CompoundConstraint add = conjunction().add(propertyConstraint).add(propertyConstraint2);
		PropertyConstraint requiredIfTrue = new RequiredIfTrue(propertyName, add);
		PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(requiredIfTrue);
		propertyResolvableConstraint.setType(propertyName + "Required");
		return propertyResolvableConstraint;
	}

	private PropertyConstraint getPropertyTipoDichiarazioneRigaSezioneConstraint(String propertyName,
			TipoDichiarazione tipoDichiarazione) {
		PropertyConstraint propertyConstraint = new PropertyValueConstraint("dichiarazione.tipoDichiarazione",
				eq(tipoDichiarazione));
		PropertyConstraint requiredIfTrue = new RequiredIfTrue(propertyName, propertyConstraint);
		PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(requiredIfTrue);
		propertyResolvableConstraint.setType(propertyName + "Required");
		return propertyResolvableConstraint;
	}

	@Override
	public List<Rules> getRules() {
		List<Rules> listRules = new ArrayList<Rules>();
		listRules.add(createNomenclaturaRules());
		listRules.add(createRigaBeneIntraRules());
		listRules.add(createRigaServizioIntraRules());
		listRules.add(createAreaIntraRules());

		listRules.add(createDichiarazioneIntraAcquistiRules());
		listRules.add(createDichiarazioneIntraVenditeRules());

		listRules.add(createRigaSezione1IntraRules());
		listRules.add(createRigaSezione2IntraRules());
		listRules.add(createRigaSezione3IntraRules());
		listRules.add(createRigaSezione4IntraRules());

		return listRules;
	}

	/**
	 * @return range di trimestre da 1 a 4 se tipo periodo = trimestrale
	 */
	private PropertyConstraint getTrimestreDichiarazioneConstraint() {
		PropertyConstraint propertyConstraint = new PropertyValueConstraint("tipoPeriodo", eq(TipoPeriodo.T));
		PropertyConstraint domainObjectIfTrue = new ConditionalPropertyConstraint(propertyConstraint,
				new PropertyValueConstraint("trimestre", range(1, 4)));
		PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(domainObjectIfTrue);
		propertyResolvableConstraint.setType("trimestreDichiarazioneRangeRequired");
		return propertyResolvableConstraint;
	}

}
