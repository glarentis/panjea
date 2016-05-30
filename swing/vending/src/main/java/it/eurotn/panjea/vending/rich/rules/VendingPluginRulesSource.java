package it.eurotn.panjea.vending.rich.rules;

import java.util.ArrayList;
import java.util.List;

import org.springframework.rules.Rules;
import org.springframework.rules.constraint.property.PropertyConstraint;
import org.springframework.rules.constraint.property.PropertyValueConstraint;
import org.springframework.rules.constraint.property.RequiredIfTrue;

import it.eurotn.panjea.magazzino.util.AreaMagazzinoFullDTO;
import it.eurotn.panjea.manutenzioni.domain.Installazione;
import it.eurotn.panjea.rich.rules.PropertyResolvableConstraint;
import it.eurotn.panjea.rules.AbstractPluginRulesSource;
import it.eurotn.panjea.vending.domain.Distributore;
import it.eurotn.panjea.vending.domain.EvaDtsImportFolder;
import it.eurotn.panjea.vending.domain.LetturaSelezionatrice;
import it.eurotn.panjea.vending.domain.Modello;
import it.eurotn.panjea.vending.domain.MovimentoCassa;
import it.eurotn.panjea.vending.domain.TipoModello;
import it.eurotn.panjea.vending.domain.VendingSettings;

public class VendingPluginRulesSource extends AbstractPluginRulesSource {

    private Rules createAreaRifornimentoRules() {
        return new Rules(AreaMagazzinoFullDTO.class) {

            @Override
            protected void initRules() {
                add(getARInstallazioneConstraint());
                add(getAROperatoreConstraint());
                add(getARDistributoreConstraint());
            }
        };
    }

    /**
     * Crea le rules per {@link Distributore}.
     *
     * @return rules create
     */
    private Rules createDistributoreRules() {
        return new Rules(Distributore.class) {

            @Override
            protected void initRules() {
                add("codice", getRequiredConstraint());
                add("codice", getMaxCharConstraint(30));
                add("descrizione", getRequiredConstraint());
                add("descrizione", getMaxCharConstraint(100));
                add("datiVending.modello", getDomainAttributeRequiredConstraint());
            }
        };
    }

    private Rules createEvaDtsImportFolderRules() {
        return new Rules(EvaDtsImportFolder.class) {

            @Override
            protected void initRules() {
                add("folder", getRequiredConstraint());
                add("fieldIDName", getRequiredConstraint());
                add("fieldIDContent", getRequiredConstraint());
                add("gestioneValoreIDDoppio", getRequiredConstraint());
            }
        };
    }

    /**
     * Crea le rules per {@link Installazione}.
     *
     * @return rules create
     */
    private Rules createInstallazioneRules() {
        return new Rules(Installazione.class) {
            @Override
            protected void initRules() {
                add("codice", getMaxCharConstraint(10));
                add("descrizione", getRequiredConstraint());
                add("descrizione", getMaxCharConstraint(130));
                add("deposito.sedeEntita", getDomainAttributeRequiredConstraint());
                add("deposito.sedeDeposito", getDomainAttributeRequiredConstraint());
                add("tipoAreaMagazzino", getDomainAttributeRequiredConstraint());
            }
        };
    }

    /**
     * Crea le rules per {@link LetturaSelezionatrice}.
     *
     * @return rules create
     */
    private Rules createLetturaSelezionatriceRules() {
        return new Rules(LetturaSelezionatrice.class) {

            @Override
            protected void initRules() {
                add("progressivo", getRequiredConstraint());
                add("data", getRequiredConstraint());
                add("dataRifornimento", getRequiredConstraint());
                add("cassaDestinazione", getDomainAttributeRequiredConstraint());
            }
        };
    }

    /**
     * Crea le rules per {@link Modello}.
     *
     * @return rules create
     */
    private Rules createModelloRules() {
        return new Rules(Modello.class) {

            @Override
            protected void initRules() {
                add("codice", getRequiredConstraint());
                add("codice", getMaxCharConstraint(10));
                add("descrizione", getRequiredConstraint());
                add("descrizione", getMaxCharConstraint(50));
                add("tipoModello", getDomainAttributeRequiredConstraint());
            }
        };
    }

    /**
     * Crea le rules per {@link MovimentoCassa}.
     *
     * @return rules create
     */
    private Rules createMovimentoCassaRules() {
        return new Rules(MovimentoCassa.class) {

            @Override
            protected void initRules() {
                add("cassa", getDomainAttributeRequiredConstraint());
                add("data", getRequiredConstraint());
                add("righe", getCollectionRequiredRequired());
            }
        };
    }

    /**
     * Crea le rules per {@link TipoModello}.
     *
     * @return rules create
     */
    private Rules createTipoModelloRules() {
        return new Rules(TipoModello.class) {

            @Override
            protected void initRules() {
                add("codice", getRequiredConstraint());
                add("codice", getMaxCharConstraint(10));
                add("descrizione", getRequiredConstraint());
                add("descrizione", getMaxCharConstraint(50));
                add("tipoComunicazione", getDomainAttributeRequiredConstraint());
            }
        };
    }

    private Rules createVendingSettingsRules() {
        return new Rules(VendingSettings.class) {

            @Override
            protected void initRules() {
                add("evadtsTipoDocumentoImportazione", getDomainAttributeRequiredConstraint());
            }
        };
    }

    /**
     * @return distributore area rifornimento constraint
     */
    private PropertyConstraint getARDistributoreConstraint() {
        PropertyConstraint propertyConstraint = new PropertyValueConstraint(
                "areaMagazzino.tipoAreaMagazzino.gestioneVending", eq(true));
        PropertyConstraint domainObjectIfTrue = new RequiredIfTrue("areaRifornimento.distributore", propertyConstraint);
        PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(
                domainObjectIfTrue);
        propertyResolvableConstraint.setType("required");
        return propertyResolvableConstraint;
    }

    /**
     * @return installazione area rifornimento constraint
     */
    private PropertyConstraint getARInstallazioneConstraint() {
        PropertyConstraint propertyConstraint = new PropertyValueConstraint(
                "areaMagazzino.tipoAreaMagazzino.gestioneVending", eq(true));
        PropertyConstraint domainObjectIfTrue = new RequiredIfTrue("areaRifornimento.installazione",
                propertyConstraint);
        PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(
                domainObjectIfTrue);
        propertyResolvableConstraint.setType("required");
        return propertyResolvableConstraint;
    }

    /**
     * @return operatore area rifornimento constraint
     */
    private PropertyConstraint getAROperatoreConstraint() {
        PropertyConstraint propertyConstraint = new PropertyValueConstraint(
                "areaMagazzino.tipoAreaMagazzino.gestioneVending", eq(true));
        PropertyConstraint domainObjectIfTrue = new RequiredIfTrue("areaRifornimento.operatore", propertyConstraint);
        PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(
                domainObjectIfTrue);
        propertyResolvableConstraint.setType("required");
        return propertyResolvableConstraint;
    }

    @Override
    public List<Rules> getRules() {

        List<Rules> rules = new ArrayList<>();

        rules.add(createTipoModelloRules());
        rules.add(createModelloRules());
        rules.add(createDistributoreRules());
        rules.add(createAreaRifornimentoRules());
        rules.add(createInstallazioneRules());
        rules.add(createMovimentoCassaRules());
        rules.add(createLetturaSelezionatriceRules());

        rules.add(createVendingSettingsRules());
        rules.add(createEvaDtsImportFolderRules());

        return rules;
    }
}
