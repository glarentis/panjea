package it.eurotn.panjea.compoli.rich.rules;

import java.util.ArrayList;
import java.util.List;

import org.springframework.rules.Rules;
import org.springframework.rules.constraint.property.PropertyConstraint;
import org.springframework.rules.constraint.property.PropertyValueConstraint;
import org.springframework.rules.constraint.property.RequiredIfTrue;

import it.eurotn.panjea.contabilita.domain.CausaleRitenutaAcconto;
import it.eurotn.panjea.contabilita.util.ParametriCreazioneComPolivalente;
import it.eurotn.panjea.contabilita.util.ParametriCreazioneComPolivalente.TipologiaInvio;
import it.eurotn.panjea.rich.rules.PropertyResolvableConstraint;
import it.eurotn.panjea.rules.AbstractPluginRulesSource;

public class ComunicazionePolivalentePluginRulesSource extends AbstractPluginRulesSource {

    /**
     * crea le regole di validazione per {@link CausaleRitenutaAcconto}.
     *
     * @return regole create
     */
    private Rules createParametriCreazioneComPolivalenteRules() {
        return new Rules(ParametriCreazioneComPolivalente.class) {

            @Override
            protected void initRules() {
                add("annoRiferimento", getRequiredConstraint());
                add(getParametriCreazioneComPoliProtocolloComunicazioneConstraint());
                add(getParametriCreazioneComPoliProtocolloDocumentoConstraint());
                add(getParametriCreazioneComPoliCodFiscIntermadiarioConstraint());
                add(getParametriCreazioneComPoliDataImpegnoIntermadiarioConstraint());
            }
        };
    }

    /**
     * Crea la regola di validazione per il codice fiscale dell'intermediario.
     *
     * @return constraint
     */
    private PropertyConstraint getParametriCreazioneComPoliCodFiscIntermadiarioConstraint() {
        PropertyConstraint propertyConstraint = new PropertyValueConstraint("intermediarioPresente", eq(Boolean.TRUE));
        PropertyConstraint domainObjectIfTrue = new RequiredIfTrue("intermediario.codiceFiscale", propertyConstraint);
        PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(
                domainObjectIfTrue);
        propertyResolvableConstraint.setType("codiceFiscaleIntermediarioRequired");
        return propertyResolvableConstraint;
    }

    /**
     * Crea la regola di validazione per la data impegno dell'intermediario.
     *
     * @return constraint
     */
    private PropertyConstraint getParametriCreazioneComPoliDataImpegnoIntermadiarioConstraint() {
        PropertyConstraint propertyConstraint = new PropertyValueConstraint("intermediarioPresente", eq(Boolean.TRUE));
        PropertyConstraint domainObjectIfTrue = new RequiredIfTrue("intermediario.dataImpegno", propertyConstraint);
        PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(
                domainObjectIfTrue);
        propertyResolvableConstraint.setType("dataImpegnoIntermediarioRequired");
        return propertyResolvableConstraint;
    }

    /**
     * Crea la regola di validazione per il protocollo comunicazione.
     *
     * @return constraint
     */
    private PropertyConstraint getParametriCreazioneComPoliProtocolloComunicazioneConstraint() {
        PropertyConstraint propertyConstraint = new PropertyValueConstraint("tipologiaInvio",
                not(eq(TipologiaInvio.INVIO_ORDINARIO)));
        PropertyConstraint domainObjectIfTrue = new RequiredIfTrue("protocolloComunicazione", propertyConstraint);
        PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(
                domainObjectIfTrue);
        propertyResolvableConstraint.setType("protocolloComunicazioneRequired");
        return propertyResolvableConstraint;
    }

    /**
     * Crea la regola di validazione per il protocollo documento.
     *
     * @return constraint
     */
    private PropertyConstraint getParametriCreazioneComPoliProtocolloDocumentoConstraint() {
        PropertyConstraint propertyConstraint = new PropertyValueConstraint("tipologiaInvio",
                not(eq(TipologiaInvio.INVIO_ORDINARIO)));
        PropertyConstraint domainObjectIfTrue = new RequiredIfTrue("protocolloDocumento", propertyConstraint);
        PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(
                domainObjectIfTrue);
        propertyResolvableConstraint.setType("protocolloDocumentoRequired");
        return propertyResolvableConstraint;
    }

    @Override
    public List<Rules> getRules() {
        List<Rules> listRules = new ArrayList<>();

        listRules.add(createParametriCreazioneComPolivalenteRules());

        return listRules;
    }

}
