package it.eurotn.panjea.preventivi.rich.rules;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.magazzino.rich.rules.AttributiRigaConstraint;
import it.eurotn.panjea.magazzino.rich.rules.ListinoValutaPropertiesConstraint;
import it.eurotn.panjea.magazzino.rich.rules.NumeroAttributiRigaConstraint;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoFullDTO;
import it.eurotn.panjea.pagamenti.domain.CodicePagamento;
import it.eurotn.panjea.preventivi.domain.RigaArticolo;
import it.eurotn.panjea.preventivi.domain.documento.AreaPreventivo;
import it.eurotn.panjea.preventivi.domain.documento.TipoAreaPreventivo;
import it.eurotn.panjea.preventivi.util.AreaPreventivoFullDTO;
import it.eurotn.panjea.preventivi.util.RigaEvasione;
import it.eurotn.panjea.rate.domain.AreaRate;
import it.eurotn.panjea.rich.rules.ConfrontoDateConstraint;
import it.eurotn.panjea.rich.rules.PropertyResolvableConstraint;
import it.eurotn.panjea.rules.AbstractPluginRulesSource;

import java.util.ArrayList;
import java.util.List;

import org.springframework.rules.Rules;
import org.springframework.rules.constraint.GreaterThanEqualTo;
import org.springframework.rules.constraint.Or;
import org.springframework.rules.constraint.property.PropertyConstraint;
import org.springframework.rules.constraint.property.PropertyValueConstraint;
import org.springframework.rules.constraint.property.RequiredIfTrue;

public class PreventiviPluginRulesSource extends AbstractPluginRulesSource {

	/**
	 * @return regole per la validazione del {@link AreaPreventivo}
	 */
	private Rules createAreaPreventivoRules() {
		return new Rules(AreaPreventivoFullDTO.class) {
			@Override
			protected void initRules() {
				add("areaPreventivo.dataRegistrazione", getRequiredConstraint());
				// add("areaPreventivo.dataConsegna", getRequiredConstraint());
				add("areaPreventivo.documento.dataDocumento", getRequiredConstraint());
				add(new ConfrontoDateConstraint("areaPreventivo.dataRegistrazione", GreaterThanEqualTo.instance(),
						"areaPreventivo.documento.dataDocumento"));
				add("areaPreventivo.tipoAreaPreventivo", getDomainAttributeRequiredConstraint());
				add(getAreaPreventivoSedeEntitaConstraint());
				add(getAreaPreventivoEntitaConstraint());
				add(new ListinoValutaPropertiesConstraint("areaPreventivo.listino",
						"areaPreventivo.documento.totale.codiceValuta"));
				add(new ListinoValutaPropertiesConstraint("areaPreventivo.listinoAlternativo",
						"areaPreventivo.documento.totale.codiceValuta"));

				// Rules per AreaPartita
				add(getCodicePagamentoConstraint());
			}
		};
	}

	/**
	 * Crea le regole per {@link LayoutStampaTipoAreaPreventivo}.
	 * 
	 * @return regole create
	 */
	// private Rules createLayoutStampaTipoAreaPreventivoRules() {
	// return new Rules(LayoutStampaTipoAreaPreventivo.class) {
	// @Override
	// protected void initRules() {
	// add("reportName", getRequiredConstraint());
	// }
	// };
	// }

	/**
	 * 
	 * @0return rules per la riga articolo
	 */
	private Rules createRigaArticoloRules() {
		return new Rules(RigaArticolo.class) {
			@Override
			protected void initRules() {
				// add("dataConsegna", getRequiredConstraint());
				add("articolo", getDomainAttributeRequiredConstraint());
				add("descrizione", getRequiredConstraint());
				add("descrizione", maxLength(100));
				add("unitaMisura", getRequiredConstraint());
				add("unitaMisura", maxLength(3));
				// add("qta", not(eq(0.00)));
				add("codiceIva", getDomainAttributeRequiredConstraint());
				add("attributi", new NumeroAttributiRigaConstraint());
				add("attributi", new AttributiRigaConstraint());
			}
		};
	}

	/**
	 * @return regole per la validazione del {@link RigaEvasione}
	 */
	private Rules createRigaEvasioneRules() {
		return new Rules(RigaEvasione.class) {
			@Override
			protected void initRules() {
				add("dataConsegna", getRequiredConstraint());
				add(lteProperty("quantitaEvasione", "quantitaRiga"));
			}
		};
	}

	/**
	 * @return regole per la validazione del {@link TipoAreaPreventivo}
	 */
	private Rules createTipoAreaPreventivoRules() {
		return new Rules(TipoAreaPreventivo.class) {
			@Override
			protected void initRules() {
				add("tipoDocumentoEvasioneDescrizioneMaschera", getRequiredConstraint());
			}
		};
	}

	/**
	 * 
	 * @return constraint per entita su ordine
	 */
	private PropertyConstraint getAreaPreventivoEntitaConstraint() {
		PropertyConstraint propertyConstraint = new PropertyValueConstraint(
				"areaPreventivo.tipoAreaPreventivo.tipoDocumento.tipoEntita", new Or(eq(TipoEntita.CLIENTE),
						eq(TipoEntita.FORNITORE)));
		PropertyConstraint domainObjectIfTrue = new RequiredIfTrue("areaPreventivo.documento.entita",
				propertyConstraint);
		PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(domainObjectIfTrue);
		propertyResolvableConstraint.setType("areaMagazzinoEntitaRequired");
		return propertyResolvableConstraint;
	}

	/**
	 * 
	 * @return constraint per sedeEntita su ordine
	 */
	private PropertyConstraint getAreaPreventivoSedeEntitaConstraint() {
		PropertyConstraint propertyConstraint = new PropertyValueConstraint(
				"areaPreventivo.tipoAreaPreventivo.tipoDocumento.tipoEntita", new Or(eq(TipoEntita.CLIENTE),
						eq(TipoEntita.FORNITORE)));
		PropertyConstraint domainObjectIfTrue = new RequiredIfTrue("areaPreventivo.documento.sedeEntita",
				propertyConstraint);
		PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(domainObjectIfTrue);
		propertyResolvableConstraint.setType("areaMagazzinoSedeEntitaRequired");
		return propertyResolvableConstraint;
	}

	/**
	 * crea e resituisce i constraint di {@link AreaRate} all'interno di {@link AreaMagazzinoFullDTO}.
	 * 
	 * @return {@link PropertyConstraint} di {@link CodicePagamento}
	 */
	private PropertyConstraint getCodicePagamentoConstraint() {
		PropertyConstraint propertyConstraint = new PropertyValueConstraint("areaRateEnabled", eq(true));
		PropertyConstraint domainObjectIfTrue = new RequiredIfTrue("areaRate.codicePagamento", propertyConstraint);
		PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(domainObjectIfTrue);
		propertyResolvableConstraint.setType("areaMagazzinoCodicePagamentoRequired");
		return propertyResolvableConstraint;
	}

	@Override
	public List<Rules> getRules() {

		List<Rules> listRules = new ArrayList<Rules>();

		listRules.add(createAreaPreventivoRules());
		listRules.add(createTipoAreaPreventivoRules());
		listRules.add(createRigaArticoloRules());
		listRules.add(createRigaEvasioneRules());

		return listRules;
	}

}
