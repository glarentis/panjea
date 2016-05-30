/**
 *
 */
package it.eurotn.panjea.ordini.rich.rules;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.magazzino.rich.rules.AttributiRigaConstraint;
import it.eurotn.panjea.magazzino.rich.rules.ListinoValutaPropertiesConstraint;
import it.eurotn.panjea.magazzino.rich.rules.NumeroAttributiRigaConstraint;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoFullDTO;
import it.eurotn.panjea.ordini.domain.RigaArticolo;
import it.eurotn.panjea.ordini.domain.documento.AreaOrdine;
import it.eurotn.panjea.ordini.domain.documento.TipoAreaOrdine;
import it.eurotn.panjea.ordini.domain.documento.evasione.RigaDistintaCarico;
import it.eurotn.panjea.ordini.rich.editors.produzione.evasione.ParametriEvasioneProduzione;
import it.eurotn.panjea.ordini.util.AreaOrdineFullDTO;
import it.eurotn.panjea.pagamenti.domain.CodicePagamento;
import it.eurotn.panjea.plugin.PluginManager;
import it.eurotn.panjea.rate.domain.AreaRate;
import it.eurotn.panjea.rich.rules.ConfrontoDateConstraint;
import it.eurotn.panjea.rich.rules.PropertyResolvableConstraint;
import it.eurotn.panjea.rules.AbstractPluginRulesSource;
import it.eurotn.panjea.stampe.domain.LayoutStampa;

import java.util.ArrayList;
import java.util.List;

import org.springframework.richclient.util.RcpSupport;
import org.springframework.rules.Rules;
import org.springframework.rules.constraint.GreaterThanEqualTo;
import org.springframework.rules.constraint.Or;
import org.springframework.rules.constraint.property.PropertyConstraint;
import org.springframework.rules.constraint.property.PropertyValueConstraint;
import org.springframework.rules.constraint.property.RequiredIfTrue;

/**
 *
 * @author giangi
 * @version 1.0, 19.04.2010
 *
 */
public class OrdiniPluginRulesSource extends AbstractPluginRulesSource {

	/**
	 * @return regole per la validazione del {@link AreaOrdine}
	 */
	private Rules createAreaOrdineRules() {
		return new Rules(AreaOrdineFullDTO.class) {
			@Override
			protected void initRules() {
				add("areaOrdine.dataRegistrazione", getRequiredConstraint());
				add("areaOrdine.dataConsegna", getRequiredConstraint());
				add("areaOrdine.documento.dataDocumento", getRequiredConstraint());
				add(new ConfrontoDateConstraint("areaOrdine.dataRegistrazione", GreaterThanEqualTo.instance(),
						"areaOrdine.documento.dataDocumento"));
				add("areaOrdine.tipoAreaOrdine", getDomainAttributeRequiredConstraint());
				add("areaOrdine.depositoOrigine", getDomainAttributeRequiredConstraint());
				add("areaOrdine.causaleTrasporto", getMaxCharConstraint(40));
				add(getAreaOrdineSedeEntitaConstraint());
				add(getAreaOrdineEntitaConstraint());
				add(new ListinoValutaPropertiesConstraint("areaOrdine.listino",
						"areaOrdine.documento.totale.codiceValuta"));
				add(new ListinoValutaPropertiesConstraint("areaOrdine.listinoAlternativo",
						"areaOrdine.documento.totale.codiceValuta"));

				PluginManager pluginManager = RcpSupport.getBean(PluginManager.BEAN_ID);
				if (pluginManager.isPresente(PluginManager.PLUGIN_AGENTI)) {
					add(getAgenteAreaOrdineConstraint());
				}

				// Rules per AreaPartita
				add(getCodicePagamentoConstraint());
			}
		};
	}

	/**
	 * Crea le regole per {@link LayoutStampaTipoAreaOrdine}.
	 *
	 * @return regole create
	 */
	private Rules createLayoutStampaTipoAreaOrdineRules() {
		return new Rules(LayoutStampa.class) {
			@Override
			protected void initRules() {
				add("reportName", getRequiredConstraint());
			}
		};
	}

	/**
	 * @return regole per la validazione di {@link ParametriEvasioneProduzione}
	 */
	private Rules createParametriEvasioneProduzioneRules() {
		return new Rules(ParametriEvasioneProduzione.class) {
			@Override
			protected void initRules() {
				add("tipoAreaMagazzino", getDomainAttributeRequiredConstraint());
			}
		};
	}

	/**
	 *
	 * @return rules per la riga articolo
	 */
	private Rules createRigaArticoloRules() {
		return new Rules(RigaArticolo.class) {
			@Override
			protected void initRules() {
				add("dataConsegna", getRequiredConstraint());
				add("articolo", getDomainAttributeRequiredConstraint());
				add("descrizione", getRequiredConstraint());
				add("descrizione", maxLength(100));
				add("unitaMisura", getRequiredConstraint());
				add("unitaMisura", maxLength(3));
				add("codiceIva", getDomainAttributeRequiredConstraint());
				add("attributi", new NumeroAttributiRigaConstraint());
				add("attributi", new AttributiRigaConstraint());
			}
		};
	}

	/**
	 * Crea le regole per {@link RigaDistintaCarico}.
	 *
	 * @return regole create
	 */
	private Rules createRigaDistintaCaricoRules() {
		return new Rules(RigaDistintaCarico.class) {
			@Override
			protected void initRules() {
				add("articolo", getDomainAttributeRequiredConstraint());
				add("qtaDaEvadere", gt(0.0));
				add("moltiplicatoreQta", not(eq(0.0)));
			}
		};
	}

	/**
	 * @return regole per la validazione del {@link TipoAreaOrdine}
	 */
	private Rules createTipoAreaOrdineRules() {
		return new Rules(TipoAreaOrdine.class) {
			@Override
			protected void initRules() {
				add(getDepositoOrigineTipoAreaMagazzinoConstraint());
				add("tipoDocumentoEvasioneDescrizioneMaschera", getRequiredConstraint());
			}
		};
	}

	/**
	 *
	 * @return constraint per entita su ordine
	 */
	private PropertyConstraint getAgenteAreaOrdineConstraint() {
		PropertyConstraint propertyConstraint = new PropertyValueConstraint(
				"areaOrdine.tipoAreaOrdine.tipoDocumento.tipoEntita", eq(TipoEntita.CLIENTE));
		PropertyConstraint domainObjectIfTrue = new AgenteContraint("areaOrdine.agente", propertyConstraint);
		PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(domainObjectIfTrue);
		propertyResolvableConstraint.setType("agenteAreaOrdineRequired");
		return propertyResolvableConstraint;
	}

	/**
	 *
	 * @return constraint per entita su ordine
	 */
	private PropertyConstraint getAreaOrdineEntitaConstraint() {
		PropertyConstraint propertyConstraint = new PropertyValueConstraint(
				"areaOrdine.tipoAreaOrdine.tipoDocumento.tipoEntita", new Or(eq(TipoEntita.CLIENTE),
						eq(TipoEntita.FORNITORE)));
		PropertyConstraint domainObjectIfTrue = new RequiredIfTrue("areaOrdine.documento.entita", propertyConstraint);
		PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(domainObjectIfTrue);
		propertyResolvableConstraint.setType("areaMagazzinoEntitaRequired");
		return propertyResolvableConstraint;
	}

	/**
	 *
	 * @return constraint per sedeEntita su ordine
	 */
	private PropertyConstraint getAreaOrdineSedeEntitaConstraint() {
		PropertyConstraint propertyConstraint = new PropertyValueConstraint(
				"areaOrdine.tipoAreaOrdine.tipoDocumento.tipoEntita", new Or(eq(TipoEntita.CLIENTE),
						eq(TipoEntita.FORNITORE)));
		PropertyConstraint domainObjectIfTrue = new RequiredIfTrue("areaOrdine.documento.sedeEntita",
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

	/**
	 * @return constraint per il deposito di origine dell'area magazzino.
	 */
	private PropertyConstraint getDepositoOrigineTipoAreaMagazzinoConstraint() {
		PropertyConstraint propertyConstraint = new PropertyValueConstraint("depositoOrigineBloccato", eq(true));
		PropertyConstraint domainObjectIfTrue = new RequiredIfTrue("depositoOrigine", propertyConstraint);
		PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(domainObjectIfTrue);
		propertyResolvableConstraint.setType("depositoOrigineRequired");
		return propertyResolvableConstraint;
	}

	@Override
	public List<Rules> getRules() {
		List<Rules> listRules = new ArrayList<Rules>();

		listRules.add(createAreaOrdineRules());
		listRules.add(createTipoAreaOrdineRules());
		listRules.add(createRigaArticoloRules());

		listRules.add(createLayoutStampaTipoAreaOrdineRules());

		listRules.add(createRigaDistintaCaricoRules());
		listRules.add(createParametriEvasioneProduzioneRules());

		return listRules;
	}

}