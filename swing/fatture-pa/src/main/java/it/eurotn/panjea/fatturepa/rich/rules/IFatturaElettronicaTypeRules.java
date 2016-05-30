package it.eurotn.panjea.fatturepa.rich.rules;

import org.springframework.rules.Rules;
import org.springframework.rules.constraint.Constraint;
import org.springframework.rules.constraint.Or;
import org.springframework.rules.constraint.property.PropertyConstraint;
import org.springframework.rules.constraint.property.PropertyValueConstraint;
import org.springframework.rules.constraint.property.RequiredIfTrue;

import it.eurotn.panjea.rich.rules.PropertyResolvableConstraint;
import it.gov.fatturapa.sdi.fatturapa.IFatturaElettronicaType;

public class IFatturaElettronicaTypeRules extends Rules {

    private Constraint requiredConstraint = all(new Constraint[] { required() });

    /**
     * Costruttore.
     */
    public IFatturaElettronicaTypeRules() {
        super(IFatturaElettronicaType.class);
    }

    private PropertyConstraint getCedentePrestatoreCognomeConstraint() {
        PropertyConstraint propertyConstraint = new PropertyValueConstraint(
                "fatturaElettronicaHeader.cedentePrestatore.datiAnagrafici.anagrafica.denominazione",
                new Or(eq(null), eq("")));
        PropertyConstraint domainObjectIfTrue = new RequiredIfTrue(
                "fatturaElettronicaHeader.cedentePrestatore.datiAnagrafici.anagrafica.cognome", propertyConstraint);
        PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(
                domainObjectIfTrue);
        propertyResolvableConstraint.setType("cedentePrestatoreCognomeRequired");
        return propertyResolvableConstraint;
    }

    /**
     *
     * @return constraint per la denominazione del rappresentate legale
     */
    private PropertyConstraint getCedentePrestatoreDenominazioneConstraint() {
        PropertyConstraint propertyConstraint = new PropertyValueConstraint(
                "fatturaElettronicaHeader.cedentePrestatore.datiAnagrafici.anagrafica.nome", new Or(eq(null), eq("")));
        PropertyConstraint propertyConstraint2 = new PropertyValueConstraint(
                "fatturaElettronicaHeader.cedentePrestatore.datiAnagrafici.anagrafica.cognome",
                new Or(eq(null), eq("")));

        PropertyConstraint domainObjectIfTrue = new RequiredIfTrue(
                "fatturaElettronicaHeader.cedentePrestatore.datiAnagrafici.anagrafica.denominazione",
                new Or(propertyConstraint, propertyConstraint2));
        PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(
                domainObjectIfTrue);
        propertyResolvableConstraint.setType("cedentePrestatoreDenominazioneRequired");
        return propertyResolvableConstraint;
    }

    private PropertyConstraint getCedentePrestatoreNomeConstraint() {
        PropertyConstraint propertyConstraint = new PropertyValueConstraint(
                "fatturaElettronicaHeader.cedentePrestatore.datiAnagrafici.anagrafica.denominazione",
                new Or(eq(null), eq("")));
        PropertyConstraint domainObjectIfTrue = new RequiredIfTrue(
                "fatturaElettronicaHeader.cedentePrestatore.datiAnagrafici.anagrafica.nome", propertyConstraint);
        PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(
                domainObjectIfTrue);
        propertyResolvableConstraint.setType("cedentePrestatoreNomeRequired");
        return propertyResolvableConstraint;
    }

    private PropertyConstraint getCessionarioCommittenteCognomeConstraint() {
        PropertyConstraint propertyConstraint = new PropertyValueConstraint(
                "fatturaElettronicaHeader.cessionarioCommittente.datiAnagrafici.anagrafica.denominazione",
                new Or(eq(null), eq("")));
        PropertyConstraint domainObjectIfTrue = new RequiredIfTrue(
                "fatturaElettronicaHeader.cessionarioCommittente.datiAnagrafici.anagrafica.cognome",
                propertyConstraint);
        PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(
                domainObjectIfTrue);
        propertyResolvableConstraint.setType("committenteCognomeRequired");
        return propertyResolvableConstraint;
    }

    /**
     *
     * @return constraint per la denominazione del rappresentate legale
     */
    private PropertyConstraint getCessionarioCommittenteDenominazioneConstraint() {
        PropertyConstraint propertyConstraint = new PropertyValueConstraint(
                "fatturaElettronicaHeader.cessionarioCommittente.datiAnagrafici.anagrafica.nome",
                new Or(eq(null), eq("")));
        PropertyConstraint propertyConstraint2 = new PropertyValueConstraint(
                "fatturaElettronicaHeader.cessionarioCommittente.datiAnagrafici.anagrafica.cognome",
                new Or(eq(null), eq("")));

        PropertyConstraint domainObjectIfTrue = new RequiredIfTrue(
                "fatturaElettronicaHeader.cessionarioCommittente.datiAnagrafici.anagrafica.denominazione",
                new Or(propertyConstraint, propertyConstraint2));
        PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(
                domainObjectIfTrue);
        propertyResolvableConstraint.setType("committenteDenominazioneRequired");
        return propertyResolvableConstraint;
    }

    private PropertyConstraint getCessionarioCommittenteNomeConstraint() {
        PropertyConstraint propertyConstraint = new PropertyValueConstraint(
                "fatturaElettronicaHeader.cessionarioCommittente.datiAnagrafici.anagrafica.denominazione",
                new Or(eq(null), eq("")));
        PropertyConstraint domainObjectIfTrue = new RequiredIfTrue(
                "fatturaElettronicaHeader.cessionarioCommittente.datiAnagrafici.anagrafica.nome", propertyConstraint);
        PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(
                domainObjectIfTrue);
        propertyResolvableConstraint.setType("committenteNomeRequired");
        return propertyResolvableConstraint;
    }

    /**
     * @return the requiredConstraint
     */
    public Constraint getRequiredConstraint() {
        return requiredConstraint;
    }

    @Override
    protected void initRules() {

        // cedente prestatore
        add("fatturaElettronicaHeader.cedentePrestatore.datiAnagrafici.idFiscaleIVA.idPaese", getRequiredConstraint());
        add("fatturaElettronicaHeader.cedentePrestatore.datiAnagrafici.idFiscaleIVA.idCodice", getRequiredConstraint());
        add(getCedentePrestatoreDenominazioneConstraint());
        add(getCedentePrestatoreNomeConstraint());
        add(getCedentePrestatoreCognomeConstraint());
        add("fatturaElettronicaHeader.cedentePrestatore.datiAnagrafici.regimeFiscale", getRequiredConstraint());
        add("fatturaElettronicaHeader.cedentePrestatore.sede.indirizzo", getRequiredConstraint());
        add("fatturaElettronicaHeader.cedentePrestatore.sede.nazione", getRequiredConstraint());
        add("fatturaElettronicaHeader.cedentePrestatore.sede.comune", getRequiredConstraint());
        add("fatturaElettronicaHeader.cedentePrestatore.sede.cap", getRequiredConstraint());
        add("fatturaElettronicaHeader.cedentePrestatore.riferimentoAmministrazione", maxLength(20));

        // committente
        add(getCessionarioCommittenteDenominazioneConstraint());
        add(getCessionarioCommittenteNomeConstraint());
        add(getCessionarioCommittenteCognomeConstraint());
        add("fatturaElettronicaHeader.cessionarioCommittente.datiAnagrafici.regimeFiscale", getRequiredConstraint());
        add("fatturaElettronicaHeader.cessionarioCommittente.sede.indirizzo", getRequiredConstraint());
        add("fatturaElettronicaHeader.cessionarioCommittente.sede.nazione", getRequiredConstraint());
        add("fatturaElettronicaHeader.cessionarioCommittente.sede.comune", getRequiredConstraint());
        add("fatturaElettronicaHeader.cessionarioCommittente.sede.cap", getRequiredConstraint());
    }

}
