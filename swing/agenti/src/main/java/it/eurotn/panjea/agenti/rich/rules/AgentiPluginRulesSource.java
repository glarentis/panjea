package it.eurotn.panjea.agenti.rich.rules;

import it.eurotn.panjea.agenti.domain.Agente;
import it.eurotn.panjea.agenti.domain.AgentiSettings;
import it.eurotn.panjea.agenti.domain.BaseProvvigionaleStrategy;
import it.eurotn.panjea.anagrafica.domain.Anagrafica;
import it.eurotn.panjea.anagrafica.domain.SedeAnagrafica;
import it.eurotn.panjea.rich.rules.ConditionalPropertyValueConstraint;
import it.eurotn.panjea.rich.rules.PropertyResolvableConstraint;
import it.eurotn.panjea.rules.AbstractPluginRulesSource;

import java.util.ArrayList;
import java.util.List;

import org.springframework.rules.Rules;
import org.springframework.rules.constraint.Constraint;
import org.springframework.rules.constraint.property.PropertyConstraint;
import org.springframework.rules.constraint.property.PropertyValueConstraint;
import org.springframework.rules.constraint.property.RequiredIfTrue;

/**
 * Rules Source per il plugin di Aton.
 * 
 * @author giangi
 * @version 1.0, 25 Nov. 2010
 */
public class AgentiPluginRulesSource extends AbstractPluginRulesSource {

	/**
	 * @return agente rules source.
	 */
	private Rules createAgenteRules() {

		return new Rules(Agente.class) {

			private PropertyConstraint createPartitaIvaConstraint() {
				Constraint propertyConstraintPartitaIva = getPartitaIVAConstraint();
				PropertyValueConstraint propertyConstraintStato = new PropertyValueConstraint(
						"anagrafica.sedeAnagrafica.datiGeografici.nazione.codice", eq("IT"));
				ConditionalPropertyValueConstraint propertyValueConstraint = new ConditionalPropertyValueConstraint(
						"anagrafica." + Anagrafica.PROP_PARTITE_I_V_A, propertyConstraintStato,
						propertyConstraintPartitaIva, null, "partitaIVAConstraint");
				return propertyValueConstraint;
			}

			private PropertyConstraint createPartitaIvaOrCodiceFiscaleConstraint() {
				Constraint propertyConstraintPartitaIvaOrCodiceFiscale = getFiscalCodeOrPartitaIVAConstraint();
				PropertyValueConstraint propertyConstraintStato = new PropertyValueConstraint(
						"anagrafica.sedeAnagrafica.datiGeografici.nazione.codice", eq("IT"));
				ConditionalPropertyValueConstraint propertyValueConstraint = new ConditionalPropertyValueConstraint(
						"anagrafica." + Anagrafica.PROP_CODICE_FISCALE, propertyConstraintStato,
						propertyConstraintPartitaIvaOrCodiceFiscale, null, "codiceFiscaleOrPartitaIVAConstraint");
				return propertyValueConstraint;
			}

			@Override
			protected void initRules() {
				// add("codice", REQUIRED_CONSTRAINT);
				add("anagrafica." + Anagrafica.PROP_DENOMINAZIONE, getRequiredConstraint());
				add("anagrafica.sedeAnagrafica." + SedeAnagrafica.PROP_DESCRIZIONE, getRequiredConstraint());
				add("anagrafica.sedeAnagrafica." + SedeAnagrafica.PROP_INDIRIZZO, getRequiredConstraint());
				add("anagrafica.sedeAnagrafica." + SedeAnagrafica.PROP_INDIRIZZO, getMaxCharConstraint(60));
				add("anagrafica.sedeAnagrafica." + SedeAnagrafica.PROP_WEB, getInternetAddressConstraint());
				add("anagrafica.sedeAnagrafica." + SedeAnagrafica.PROP_INDIRIZZO_MAIL, getEmailConstraint());
				add("anagrafica.sedeAnagrafica.indirizzoPEC", getEmailConstraint());
				add("anagrafica.sedeAnagrafica.datiGeografici.nazione", getDomainAttributeRequiredConstraint());
				// add("anagrafica.sedeAnagrafica." +
				// SedeAnagrafica.PROP_TELEFONO, NUMERIC_CONSTRAINT);
				// add("anagrafica.sedeAnagrafica." + SedeAnagrafica.PROP_FAX,
				// NUMERIC_CONSTRAINT);
				add(createPartitaIvaConstraint());
				add(createPartitaIvaOrCodiceFiscaleConstraint());

			}
		};
	}

	/**
	 * @return agente rules source.
	 */
	private Rules createAgentiSettingsRules() {

		return new Rules(AgentiSettings.class) {

			@Override
			protected void initRules() {
				add(getProvvigioneStrategyListinoConstraint());
			}
		};
	}

	/**
	 * 
	 * @return constraint per l'entità dell'area magazzino
	 */
	private PropertyConstraint getProvvigioneStrategyListinoConstraint() {
		PropertyConstraint propertyConstraint = new PropertyValueConstraint(
				"baseProvvigionaleSettings.baseProvvigionaleStrategy", eq(BaseProvvigionaleStrategy.LISTINO));
		PropertyConstraint domainObjectIfTrue = new RequiredIfTrue("baseProvvigionaleSettings.listino",
				propertyConstraint);
		PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(domainObjectIfTrue);
		propertyResolvableConstraint.setType("baseProvvigionaleSettingsListinoRequired");
		return propertyResolvableConstraint;
	}

	@Override
	public List<Rules> getRules() {
		List<Rules> listRules = new ArrayList<Rules>();
		listRules.add(createAgenteRules());
		listRules.add(createAgentiSettingsRules());
		return listRules;
	}

}