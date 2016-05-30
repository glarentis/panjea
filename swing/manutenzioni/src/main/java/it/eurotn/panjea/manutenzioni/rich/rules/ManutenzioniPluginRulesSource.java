package it.eurotn.panjea.manutenzioni.rich.rules;

import java.util.ArrayList;
import java.util.List;

import org.springframework.rules.Rules;
import org.springframework.rules.constraint.property.PropertyConstraint;
import org.springframework.rules.constraint.property.PropertyValueConstraint;
import org.springframework.rules.constraint.property.RequiredIfTrue;

import it.eurotn.panjea.manutenzioni.domain.Installazione;
import it.eurotn.panjea.manutenzioni.domain.Operatore;
import it.eurotn.panjea.manutenzioni.domain.documento.AreaInstallazione;
import it.eurotn.panjea.manutenzioni.domain.documento.RigaInstallazione;
import it.eurotn.panjea.manutenzioni.domain.documento.RigaInstallazione.TipoMovimento;
import it.eurotn.panjea.rich.rules.PropertyResolvableConstraint;
import it.eurotn.panjea.rules.AbstractPluginRulesSource;

public class ManutenzioniPluginRulesSource extends AbstractPluginRulesSource {

    /**
     * Crea le rules per {@link AreaInstallazione}.
     *
     * @return rules create
     */
    private Rules createAreaInstallazioneRules() {
        return new Rules(AreaInstallazione.class) {
            @Override
            protected void initRules() {
                add("documento.dataDocumento", getRequiredConstraint());
                add("tipoAreaDocumento", getDomainAttributeRequiredConstraint());
                add("documento.entita", getDomainAttributeRequiredConstraint());
                add("documento.sedeEntita", getDomainAttributeRequiredConstraint());
                add("depositoOrigine", getDomainAttributeRequiredConstraint());
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
                add("listino", getDomainAttributeRequiredConstraint());
                add("deposito.sedeEntita", getDomainAttributeRequiredConstraint());
                add("deposito.sedeDeposito", getDomainAttributeRequiredConstraint());
            }
        };
    }

    /**
     * Crea le rules per {@link Operatore}.
     *
     * @return rules create
     */
    private Rules createOperatoreRules() {
        return new Rules(Operatore.class) {
            @Override
            protected void initRules() {
                add("codice", getRequiredConstraint());
                add("nome", getRequiredConstraint());
                add("cognome", getRequiredConstraint());
                add("deposito", getDomainAttributeRequiredConstraint());
            }
        };
    }

    private Rules createRigaInstallazioneRules() {
        return new Rules(RigaInstallazione.class) {
            @Override
            protected void initRules() {
                add(getCausaleInstallazioneConstraint());
                add(getArticoloInstallazioneConstraint());
                add(getArticoloRitiroConstraint());
                add(getCausaleRitiroConstraint());
            }
        };
    }

    private PropertyConstraint getArticoloInstallazioneConstraint() {
        PropertyConstraint propertyConstraint = new PropertyValueConstraint("tipoMovimento",
                or(eq(TipoMovimento.INSTALLAZIONE), eq(TipoMovimento.SOSTITUZIONE)));
        PropertyConstraint domainObjectIfTrue = new RequiredIfTrue("articoloInstallazione", propertyConstraint);
        PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(
                domainObjectIfTrue);
        propertyResolvableConstraint.setType("Required");
        return propertyResolvableConstraint;
    }

    private PropertyConstraint getArticoloRitiroConstraint() {
        PropertyConstraint propertyConstraint = new PropertyValueConstraint("tipoMovimento", eq(TipoMovimento.RITIRO));
        PropertyConstraint domainObjectIfTrue = new RequiredIfTrue("articoloRitiro", propertyConstraint);
        PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(
                domainObjectIfTrue);
        propertyResolvableConstraint.setType("Required");
        return propertyResolvableConstraint;
    }

    private PropertyConstraint getCausaleInstallazioneConstraint() {
        PropertyConstraint propertyConstraint = new PropertyValueConstraint("tipoMovimento",
                or(eq(TipoMovimento.INSTALLAZIONE), eq(TipoMovimento.SOSTITUZIONE)));
        PropertyConstraint domainObjectIfTrue = new RequiredIfTrue("causaleInstallazione", propertyConstraint);
        PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(
                domainObjectIfTrue);
        propertyResolvableConstraint.setType("Required");
        return propertyResolvableConstraint;
    }

    private PropertyConstraint getCausaleRitiroConstraint() {
        PropertyConstraint propertyConstraint = new PropertyValueConstraint("tipoMovimento", eq(TipoMovimento.RITIRO));
        PropertyConstraint domainObjectIfTrue = new RequiredIfTrue("causaleRitiro", propertyConstraint);
        PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(
                domainObjectIfTrue);
        propertyResolvableConstraint.setType("Required");
        return propertyResolvableConstraint;
    }

    @Override
    public List<Rules> getRules() {

        List<Rules> rules = new ArrayList<Rules>();
        rules.add(createOperatoreRules());
        rules.add(createAreaInstallazioneRules());
        rules.add(createInstallazioneRules());
        rules.add(createRigaInstallazioneRules());
        return rules;
    }

}
