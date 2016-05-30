/**
 * 
 */
package it.eurotn.panjea.auvend.rich.rules;

import it.eurotn.panjea.auvend.domain.LetturaFlussoAuVend;
import it.eurotn.panjea.auvend.domain.TipoDocumentoBaseAuVend;
import it.eurotn.panjea.auvend.util.ParametriRecuperoCarichiRifornimenti;
import it.eurotn.panjea.auvend.util.ParametriRecuperoFatturazioneRifornimenti;
import it.eurotn.panjea.rich.rules.PropertyResolvableConstraint;
import it.eurotn.panjea.rules.AbstractPluginRulesSource;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.rules.Rules;
import org.springframework.rules.constraint.property.PropertyConstraint;
import org.springframework.rules.constraint.property.PropertyValueConstraint;
import org.springframework.rules.constraint.property.RequiredIfTrue;

/**
 * Rules Source per il plugin di AuVend.
 * 
 * @author adriano
 * @version 1.0, 30/dic/2008
 * 
 */
public class AuVendPluginRulesSource extends AbstractPluginRulesSource {

	private Logger logger = Logger.getLogger(AuVendPluginRulesSource.class);

	/**
	 * crea e restituisce il PropertyConstraint per l'obbligatorieta' di depositoOrigine se il tipoOperazione e'
	 * RECUPERO_FATTURA attualmente non funziona.
	 * 
	 * @return constraint
	 */
	@SuppressWarnings("unused")
	private PropertyConstraint createDepositoSorgenteRequired() {
		PropertyValueConstraint propertyValueConstraint = new PropertyValueConstraint("tipoOperazione",
				not(eq(TipoDocumentoBaseAuVend.TipoOperazione.RECUPERO_FATTURA)));
		RequiredIfTrue RequiredIfTrue = new RequiredIfTrue("tipoAreaMagazzino.depositoOrigine", propertyValueConstraint);
		PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(RequiredIfTrue);
		propertyResolvableConstraint.setType("depositoOrigineRequired");
		return propertyResolvableConstraint;
	}

	/**
	 * @return reogle di validazione per {@link LetturaFlussoAuVend}.
	 */
	private Rules createLetturaFlussoAuVendRules() {
		return new Rules(LetturaFlussoAuVend.class) {

			@Override
			protected void initRules() {
				logger.debug("--> Enter initRules");
				add("tipoOperazione", getRequiredConstraint());
				add("deposito", getDomainAttributeRequiredConstraint());
				logger.debug("--> Exit initRules");
			}

		};
	}

	/**
	 * @return reogle di validazione per {@link ParametriRecuperoCarichiRifornimenti}.
	 */
	private Rules createParametriRecuperoCarichiRules() {
		return new Rules(ParametriRecuperoCarichiRifornimenti.class) {

			@Override
			protected void initRules() {
				logger.debug("--> Enter initRules");
				add("dataRiferimento", getRequiredConstraint());
				add("lettureFlussoAuVend", getCollectionRequiredRequired());
				logger.debug("--> Exit initRules");
			}

		};
	}

	private Rules createParametriRecuperoFatturazioneRifornimentiRules() {
		return new Rules(ParametriRecuperoFatturazioneRifornimenti.class) {

			@Override
			protected void initRules() {
				logger.debug("--> Enter initRules");
				add("dataInizio", getRequiredConstraint());
				add("dataFine", getRequiredConstraint());
				logger.debug("--> Exit initRules");
			}
		};
	}

	/**
	 * @return reogle di validazione per {@link TipoDocumentoBaseAuVend}.
	 */
	private Rules createTipoDocumentoBaseAuVendRules() {
		return new Rules(TipoDocumentoBaseAuVend.class) {

			@Override
			protected void initRules() {
				logger.debug("--> Enter initRules");
				// add(createDepositoSorgenteRequired());
				logger.debug("--> Exit initRules");
			}

		};
	}

	@Override
	public List<Rules> getRules() {
		List<Rules> listRules = new ArrayList<Rules>();
		listRules.add(createParametriRecuperoCarichiRules());
		listRules.add(createLetturaFlussoAuVendRules());
		listRules.add(createTipoDocumentoBaseAuVendRules());
		listRules.add(createParametriRecuperoFatturazioneRifornimentiRules());
		return listRules;
	}

}
