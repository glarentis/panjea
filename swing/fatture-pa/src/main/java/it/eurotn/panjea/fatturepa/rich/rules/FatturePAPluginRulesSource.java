package it.eurotn.panjea.fatturepa.rich.rules;

import java.util.ArrayList;
import java.util.List;

import org.springframework.rules.Rules;
import org.springframework.rules.constraint.And;
import org.springframework.rules.constraint.Or;
import org.springframework.rules.constraint.property.PropertyConstraint;
import org.springframework.rules.constraint.property.PropertyValueConstraint;
import org.springframework.rules.constraint.property.RequiredIfTrue;

import it.eurotn.panjea.fatturepa.domain.AziendaFatturaPA;
import it.eurotn.panjea.fatturepa.domain.FatturaPASettings;
import it.eurotn.panjea.rich.rules.PropertyResolvableConstraint;
import it.eurotn.panjea.rules.AbstractPluginRulesSource;

public class FatturePAPluginRulesSource extends AbstractPluginRulesSource {

    /**
     * Crea le regole per {@link AziendaFatturaPA}.
     *
     * @return regole create
     */
    private Rules createAziendaPARules() {
        return new Rules(AziendaFatturaPA.class) {
            @Override
            protected void initRules() {
                add("codiceIdentificativoFiscale", getRequiredConstraint());
                add("denominazione", getRequiredConstraint());
                add("regimeFiscale", getRequiredConstraint());
                // legale rappresentante
                add("legaleRappresentanteCodiceNazione", getRequiredConstraint());
                add("legaleRappresentanteIdentificativoFiscale", getRequiredConstraint());
                add(getLegaleRappresentanteDenominazioneConstraint());
                add(getLegaleRappresentanteNomeConstraint());
                add(getLegaleRappresentanteCognomeConstraint());
                // sede
                add("sedeIndirizzo", getRequiredConstraint());
                add("sedeCAP", getRequiredConstraint());
                add("sedeComune", getRequiredConstraint());
                add("sedeNazione", getRequiredConstraint());
                // rappresentante fiscale
                add(getRappresentanteFiscaleNazioneConstraint());
                add(getRappresentanteFiscaleIdentificativoFiscaleConstraint());
                add(getRappresentanteFiscaleDenominazioneConstraint());
                add(getRappresentanteFiscaleCognomeConstraint());
                add(getRappresentanteFiscaleNomeConstraint());
                // Iscrizione REA
                add(getDatiIscrizioneReaProvinciaConstraint());
                add(getDatiIscrizioneReaNumeroConstraint());
                add(getDatiIscrizioneReaCapitaleConstraint());
                add(getDatiIscrizioneReaSociConstraint());
                add(getDatiIscrizioneReaLiquidazioneConstraint());
            }
        };
    }

    /**
     * Crea le regole per {@link FatturaPASettings}.
     *
     * @return regole create
     */
    private Rules createFatturaPASettingsRules() {
        return new Rules(FatturaPASettings.class) {
            @Override
            protected void initRules() {
                add("registroProtocollo", getRequiredConstraint());
                add("softwareFirmaElettronica", getRequiredConstraint());
                add("softwareFirmaPath", getRequiredConstraint());
                add("formatoTrasmissione", getRequiredConstraint());
            }
        };
    }

    /**
     * @return note blocco constraint
     */
    private PropertyConstraint getDatiIscrizioneReaCapitaleConstraint() {
        PropertyConstraint propertyConstraint = new PropertyValueConstraint("datiIscrizioneRea.enable", eq(true));
        PropertyConstraint domainObjectIfTrue = new RequiredIfTrue("datiIscrizioneRea.importoCapitaleSociale",
                propertyConstraint);
        PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(
                domainObjectIfTrue);
        propertyResolvableConstraint.setType("required");
        return propertyResolvableConstraint;
    }

    /**
     * @return note blocco constraint
     */
    private PropertyConstraint getDatiIscrizioneReaLiquidazioneConstraint() {
        PropertyConstraint propertyConstraint = new PropertyValueConstraint("datiIscrizioneRea.enable", eq(true));
        PropertyConstraint domainObjectIfTrue = new RequiredIfTrue("datiIscrizioneRea.statoLiquidazione",
                propertyConstraint);
        PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(
                domainObjectIfTrue);
        propertyResolvableConstraint.setType("required");
        return propertyResolvableConstraint;
    }

    /**
     * @return note blocco constraint
     */
    private PropertyConstraint getDatiIscrizioneReaNumeroConstraint() {
        PropertyConstraint propertyConstraint = new PropertyValueConstraint("datiIscrizioneRea.enable", eq(true));
        PropertyConstraint domainObjectIfTrue = new RequiredIfTrue("datiIscrizioneRea.numeroRea", propertyConstraint);
        PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(
                domainObjectIfTrue);
        propertyResolvableConstraint.setType("required");
        return propertyResolvableConstraint;
    }

    /**
     * @return note blocco constraint
     */
    private PropertyConstraint getDatiIscrizioneReaProvinciaConstraint() {
        PropertyConstraint propertyConstraint = new PropertyValueConstraint("datiIscrizioneRea.enable", eq(true));
        PropertyConstraint domainObjectIfTrue = new RequiredIfTrue("datiIscrizioneRea.provincia", propertyConstraint);
        PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(
                domainObjectIfTrue);
        propertyResolvableConstraint.setType("required");
        return propertyResolvableConstraint;
    }

    /**
     * @return note blocco constraint
     */
    private PropertyConstraint getDatiIscrizioneReaSociConstraint() {
        PropertyConstraint propertyConstraint = new PropertyValueConstraint("datiIscrizioneRea.enable", eq(true));
        PropertyConstraint domainObjectIfTrue = new RequiredIfTrue("datiIscrizioneRea.tipologiaSoci",
                propertyConstraint);
        PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(
                domainObjectIfTrue);
        propertyResolvableConstraint.setType("required");
        return propertyResolvableConstraint;
    }

    /**
     *
     * @return constraint per il cognome del rappresentate legale
     */
    private PropertyConstraint getLegaleRappresentanteCognomeConstraint() {
        PropertyConstraint propertyConstraint = new PropertyValueConstraint("legaleRappresentanteDenominazione",
                new Or(eq(null), eq("")));
        PropertyConstraint domainObjectIfTrue = new RequiredIfTrue("legaleRappresentanteCognome", propertyConstraint);
        PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(
                domainObjectIfTrue);
        propertyResolvableConstraint.setType("legaleRappresentanteCognomeRequired");
        return propertyResolvableConstraint;
    }

    /**
     *
     * @return constraint per la denominazione del rappresentate legale
     */
    private PropertyConstraint getLegaleRappresentanteDenominazioneConstraint() {
        PropertyConstraint propertyConstraint = new PropertyValueConstraint("legaleRappresentanteNome",
                new Or(eq(null), eq("")));
        PropertyConstraint propertyConstraint2 = new PropertyValueConstraint("legaleRappresentanteCognome",
                new Or(eq(null), eq("")));

        PropertyConstraint domainObjectIfTrue = new RequiredIfTrue("legaleRappresentanteDenominazione",
                new Or(propertyConstraint, propertyConstraint2));
        PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(
                domainObjectIfTrue);
        propertyResolvableConstraint.setType("legaleRappresentanteDenominazioneRequired");
        return propertyResolvableConstraint;
    }

    /**
     *
     * @return constraint per il nome del rappresentate legale
     */
    private PropertyConstraint getLegaleRappresentanteNomeConstraint() {
        PropertyConstraint propertyConstraint = new PropertyValueConstraint("legaleRappresentanteDenominazione",
                new Or(eq(null), eq("")));
        PropertyConstraint domainObjectIfTrue = new RequiredIfTrue("legaleRappresentanteNome", propertyConstraint);
        PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(
                domainObjectIfTrue);
        propertyResolvableConstraint.setType("legaleRappresentanteNomeRequired");
        return propertyResolvableConstraint;
    }

    /**
     *
     * @return constraint per il cognome del rappresentate fiscale
     */
    private PropertyConstraint getRappresentanteFiscaleCognomeConstraint() {
        PropertyConstraint propertyConstraint = new PropertyValueConstraint("rappresentanteFiscale.denominazione",
                new Or(eq(null), eq("")));
        PropertyConstraint propertyConstraintEnable = new PropertyValueConstraint("rappresentanteFiscale.enable",
                eq(true));

        PropertyConstraint domainObjectIfTrue = new RequiredIfTrue("rappresentanteFiscale.cognome",
                new And(propertyConstraintEnable, propertyConstraint));
        PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(
                domainObjectIfTrue);
        propertyResolvableConstraint.setType("required");
        return propertyResolvableConstraint;
    }

    /**
     *
     * @return constraint per la denominazione del rappresentate fiscale
     */
    private PropertyConstraint getRappresentanteFiscaleDenominazioneConstraint() {
        PropertyConstraint propertyConstraint = new PropertyValueConstraint("rappresentanteFiscale.nome",
                new Or(eq(null), eq("")));
        PropertyConstraint propertyConstraint2 = new PropertyValueConstraint("rappresentanteFiscale.cognome",
                new Or(eq(null), eq("")));
        PropertyConstraint propertyConstraintEnable = new PropertyValueConstraint("rappresentanteFiscale.enable",
                eq(true));

        PropertyConstraint domainObjectIfTrue = new RequiredIfTrue("rappresentanteFiscale.denominazione",
                new And(propertyConstraintEnable, new Or(propertyConstraint, propertyConstraint2)));
        PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(
                domainObjectIfTrue);
        propertyResolvableConstraint.setType("required");
        return propertyResolvableConstraint;
    }

    /**
     * @return note blocco constraint
     */
    private PropertyConstraint getRappresentanteFiscaleIdentificativoFiscaleConstraint() {
        PropertyConstraint propertyConstraint = new PropertyValueConstraint("rappresentanteFiscale.enable", eq(true));
        PropertyConstraint domainObjectIfTrue = new RequiredIfTrue("rappresentanteFiscale.codiceIdentificativoFiscale",
                propertyConstraint);
        PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(
                domainObjectIfTrue);
        propertyResolvableConstraint.setType("required");
        return propertyResolvableConstraint;
    }

    /**
     * @return note blocco constraint
     */
    private PropertyConstraint getRappresentanteFiscaleNazioneConstraint() {
        PropertyConstraint propertyConstraint = new PropertyValueConstraint("rappresentanteFiscale.enable", eq(true));
        PropertyConstraint domainObjectIfTrue = new RequiredIfTrue("rappresentanteFiscale.nazione", propertyConstraint);
        PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(
                domainObjectIfTrue);
        propertyResolvableConstraint.setType("required");
        return propertyResolvableConstraint;
    }

    /**
     *
     * @return constraint per il nome del rappresentate fiscale
     */
    private PropertyConstraint getRappresentanteFiscaleNomeConstraint() {
        PropertyConstraint propertyConstraint = new PropertyValueConstraint("rappresentanteFiscale.denominazione",
                new Or(eq(null), eq("")));
        PropertyConstraint propertyConstraintEnable = new PropertyValueConstraint("rappresentanteFiscale.enable",
                eq(true));

        PropertyConstraint domainObjectIfTrue = new RequiredIfTrue("rappresentanteFiscale.nome",
                new And(propertyConstraintEnable, propertyConstraint));
        PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(
                domainObjectIfTrue);
        propertyResolvableConstraint.setType("required");
        return propertyResolvableConstraint;
    }

    @Override
    public List<Rules> getRules() {
        List<Rules> listRules = new ArrayList<Rules>();

        listRules.add(createAziendaPARules());
        listRules.add(createFatturaPASettingsRules());

        listRules.add(new IFatturaElettronicaTypeRules());

        return listRules;
    }

}
