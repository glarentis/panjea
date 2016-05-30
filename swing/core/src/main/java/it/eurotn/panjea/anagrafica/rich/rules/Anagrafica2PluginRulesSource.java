package it.eurotn.panjea.anagrafica.rich.rules;

import java.util.ArrayList;
import java.util.List;

import org.springframework.rules.Rules;
import org.springframework.rules.constraint.Constraint;
import org.springframework.rules.constraint.LessThanEqualTo;
import org.springframework.rules.constraint.property.PropertyConstraint;
import org.springframework.rules.constraint.property.PropertyValueConstraint;
import org.springframework.rules.constraint.property.RequiredIfTrue;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.domain.Anagrafica;
import it.eurotn.panjea.anagrafica.domain.Azienda;
import it.eurotn.panjea.anagrafica.domain.Banca;
import it.eurotn.panjea.anagrafica.domain.CambioValuta;
import it.eurotn.panjea.anagrafica.domain.Carica;
import it.eurotn.panjea.anagrafica.domain.CategoriaEntita;
import it.eurotn.panjea.anagrafica.domain.Cliente;
import it.eurotn.panjea.anagrafica.domain.CodiceIva;
import it.eurotn.panjea.anagrafica.domain.Contatto;
import it.eurotn.panjea.anagrafica.domain.ContattoSedeEntita;
import it.eurotn.panjea.anagrafica.domain.ContrattoSpesometro;
import it.eurotn.panjea.anagrafica.domain.ConversioneUnitaMisura;
import it.eurotn.panjea.anagrafica.domain.Deposito;
import it.eurotn.panjea.anagrafica.domain.Destinatario;
import it.eurotn.panjea.anagrafica.domain.Filiale;
import it.eurotn.panjea.anagrafica.domain.FormaGiuridica;
import it.eurotn.panjea.anagrafica.domain.Fornitore;
import it.eurotn.panjea.anagrafica.domain.LegaleRappresentante;
import it.eurotn.panjea.anagrafica.domain.Mansione;
import it.eurotn.panjea.anagrafica.domain.NotaAutomatica;
import it.eurotn.panjea.anagrafica.domain.ParametriMail;
import it.eurotn.panjea.anagrafica.domain.RapportoBancario;
import it.eurotn.panjea.anagrafica.domain.RapportoBancarioAzienda;
import it.eurotn.panjea.anagrafica.domain.RapportoBancarioSedeEntita;
import it.eurotn.panjea.anagrafica.domain.SedeAnagrafica;
import it.eurotn.panjea.anagrafica.domain.SedeAzienda;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.TipoDeposito;
import it.eurotn.panjea.anagrafica.domain.TipoSedeEntita;
import it.eurotn.panjea.anagrafica.domain.UnitaMisura;
import it.eurotn.panjea.anagrafica.domain.Vettore;
import it.eurotn.panjea.anagrafica.domain.ZonaGeografica;
import it.eurotn.panjea.anagrafica.domain.datigeografici.Cap;
import it.eurotn.panjea.anagrafica.domain.datigeografici.LivelloAmministrativo1;
import it.eurotn.panjea.anagrafica.domain.datigeografici.LivelloAmministrativo2;
import it.eurotn.panjea.anagrafica.domain.datigeografici.LivelloAmministrativo3;
import it.eurotn.panjea.anagrafica.domain.datigeografici.LivelloAmministrativo4;
import it.eurotn.panjea.anagrafica.domain.datigeografici.Localita;
import it.eurotn.panjea.anagrafica.domain.datigeografici.Nazione;
import it.eurotn.panjea.anagrafica.rich.statusBarItem.UserChangePassword;
import it.eurotn.panjea.anagrafica.util.AziendaAnagraficaDTO;
import it.eurotn.panjea.rich.rules.CollectionValuesRequired;
import it.eurotn.panjea.rich.rules.ConditionalPropertyValueConstraint;
import it.eurotn.panjea.rich.rules.ConfrontoDateConstraint;
import it.eurotn.panjea.rich.rules.PatternCodiceDocumentoValueConstraint;
import it.eurotn.panjea.rich.rules.PropertyResolvableConstraint;
import it.eurotn.panjea.rules.AbstractPluginRulesSource;
import it.eurotn.panjea.stampe.domain.LayoutStampa;
import it.eurotn.rich.binding.CodiceFiscalePM;

public class Anagrafica2PluginRulesSource extends AbstractPluginRulesSource {

    /**
     * Crea le rules per {@link AziendaAnagraficaDTO}.
     *
     * @return rules create
     */
    private Rules createAziendaAnagraficaRules() {
        return new Rules(AziendaAnagraficaDTO.class) {

            private PropertyConstraint createPIvaOrCFForAzienda() {
                Constraint propertyConstraintPartitaIvaOrCodiceFiscale = getFiscalCodeOrPartitaIVAConstraint();
                PropertyValueConstraint propertyConstraintStato = new PropertyValueConstraint(
                        "sedeAzienda.sede.datiGeografici.nazione.codice", eq("IT"));
                ConditionalPropertyValueConstraint propertyValueConstraint = new ConditionalPropertyValueConstraint(
                        "azienda." + Azienda.PROP_CODICE_FISCALE, propertyConstraintStato,
                        propertyConstraintPartitaIvaOrCodiceFiscale, null, "codiceFiscaleOrPartitaIVAConstraint");
                return propertyValueConstraint;
            }

            private PropertyConstraint createPIvaOrCFForLegaleRapprConstraint() {
                Constraint propertyConstraintPartitaIvaOrCodiceFiscale = getFiscalCodeOrPartitaIVAConstraint();
                PropertyValueConstraint propertyConstraintStato = new PropertyValueConstraint(
                        "azienda.legaleRappresentante.datiGeograficiNascita.nazione.codice", eq("IT"));
                ConditionalPropertyValueConstraint propertyValueConstraint = new ConditionalPropertyValueConstraint(
                        "azienda.legaleRappresentante." + LegaleRappresentante.PROP_CODICE_FISCALE,
                        propertyConstraintStato, propertyConstraintPartitaIvaOrCodiceFiscale, null,
                        "codiceFiscaleOrPartitaIVAConstraint");
                return propertyValueConstraint;
            }

            @Override
            protected void initRules() {
                add("azienda." + Azienda.PROP_DENOMINAZIONE, getRequiredConstraint());
                add("azienda." + Azienda.PROP_PARTITA_I_V_A, getPartitaIVARequiredConstraint());
                add(createPIvaOrCFForAzienda());
                add("azienda.lingua", getRequiredConstraint());
                add("sedeAzienda.sede." + SedeAnagrafica.PROP_DESCRIZIONE, getRequiredConstraint());
                add("sedeAzienda.sede." + SedeAnagrafica.PROP_INDIRIZZO, getRequiredConstraint());
                add("sedeAzienda.sede." + SedeAnagrafica.PROP_WEB, getInternetAddressConstraint());
                add("sedeAzienda.sede." + SedeAnagrafica.PROP_INDIRIZZO_MAIL, getEmailConstraint());
                // add("sedeAzienda.sede." + SedeAnagrafica.PROP_TELEFONO,
                // NUMERIC_CONSTRAINT);
                // add("sedeAzienda.sede." + SedeAnagrafica.PROP_FAX,
                // NUMERIC_CONSTRAINT);

                // add("azienda.personaFisica." + PersonaFisica.PROP_NOME,
                // getRequiredConstraint());
                // add("azienda.personaFisica." + PersonaFisica.PROP_COGNOME,
                // getRequiredConstraint());
                // add("azienda.personaFisica." + PersonaFisica.PROP_SESSO,
                // getRequiredConstraint());
                // add("azienda.personaFisica." +
                // PersonaFisica.PROP_DATA_NASCITA, getRequiredConstraint());
                //
                // add("azienda.legaleRappresentante." +
                // LegaleRappresentante.PROP_NOME, getRequiredConstraint());
                // add("azienda.legaleRappresentante." +
                // LegaleRappresentante.PROP_COGNOME, getRequiredConstraint());
                // add("azienda.legaleRappresentante." +
                // LegaleRappresentante.PROP_DATA_NASCITA, getRequiredConstraint());
                // add("azienda.legaleRappresentante." +
                // LegaleRappresentante.PROP_SESSO, getRequiredConstraint());
                // add("azienda.legaleRappresentante." +
                // LegaleRappresentante.PROP_TELEFONO,NUMERIC_CONSTRAINT);
                // add("azienda.legaleRappresentante." +
                // LegaleRappresentante.PROP_FAX, NUMERIC_CONSTRAINT);
                add(createPIvaOrCFForLegaleRapprConstraint());

                add("azienda.formaGiuridica", getDomainAttributeRequiredConstraint());
                add("azienda.legaleRappresentante." + LegaleRappresentante.PROP_PARTITA_I_V_A,
                        getPartitaIVAConstraint());
                add("azienda.legaleRappresentante." + LegaleRappresentante.PROP_DATA_CARICA, getRequiredConstraint());
                add("azienda.legaleRappresentante.carica", getDomainAttributeRequiredConstraint());
                add("azienda." + Azienda.PROP_CODICE_SIA, maxLength(5));

                add("azienda.pec", getEmailConstraint());
                add("azienda.codiceSocioConai", maxLength(8));
            }
        };
    }

    /**
     * Crea le rules per {@link Banca}.
     *
     * @return rules create
     */
    private Rules createBancaRules() {
        return new Rules(Banca.class) {

            @Override
            protected void initRules() {
                add(Banca.PROP_DESCRIZIONE, getRequiredConstraint());
                add(Banca.PROP_CODICE,
                        all(new Constraint[] { getRequiredConstraint(), maxLength(5), getNumericConstraint() }));
                add(Banca.PROP_CIN, all(new Constraint[] { getAlphanumericConstraint(), getLengthConstraint(1),
                        getRequiredConstraint() }));
            }
        };
    }

    /**
     * Crea le rules per CambioValuta.
     *
     * @return rules create
     */
    private Rules createCambioValutaAziendaRules() {
        return new Rules(CambioValuta.class) {
            @Override
            protected void initRules() {
                add("data", getRequiredConstraint());
                add("tasso", getRequiredConstraint());
                add("valuta.codiceValuta", getRequiredConstraint());
                add("valuta.codiceValuta", getMaxCharConstraint(3));
                add("valuta.simbolo", getRequiredConstraint());
                add("valuta.simbolo", getMaxCharConstraint(3));
            }
        };
    }

    /**
     * Crea le rules per Cap.
     *
     * @return rules create
     */
    private Rules createCapRules() {
        return new Rules(Cap.class) {

            @Override
            protected void initRules() {
                add("descrizione", getRequiredConstraint());
                add("descrizione", getMaxCharConstraint(60));
            }
        };
    }

    /**
     * Crea le rules per {@link Carica}.
     *
     * @return rules create
     */
    private Rules createCaricaRules() {
        return new Rules(Carica.class) {

            @Override
            protected void initRules() {
                add(Carica.PROP_DESCRIZIONE, getRequiredConstraint());
            }
        };
    }

    /**
     * Crea le rules per {@link CategoriaEntita}.
     *
     * @return rules create
     */
    private Rules createCategoriEntitaRules() {
        return new Rules(CategoriaEntita.class) {

            @Override
            protected void initRules() {
                add("sezione", getRequiredConstraint());
                add("sezione", getMaxCharConstraint(30));
                add("descrizione", getRequiredConstraint());
                add("descrizione", getMaxCharConstraint(60));
            }
        };
    }

    /**
     * Crea le rules per {@link Cliente}.
     *
     * @return rules create
     */
    private Rules createClienteRules() {
        return new Rules(Cliente.class) {

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

            /**
             * @return constraint per rendere obbligatoria la valuta per nazioni intra
             */
            private PropertyConstraint getCodiceIdentificativoFiscaleConstraint() {
                PropertyConstraint propertyConstraint = new PropertyValueConstraint("fatturazionePA", eq(true));
                PropertyConstraint valutaIfIntra = new RequiredIfTrue("codiceIdentificativoFiscale",
                        propertyConstraint);
                PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(
                        valutaIfIntra);
                propertyResolvableConstraint.setType("codiceIdentificativoFiscaleRequired");
                return propertyResolvableConstraint;
            }

            @Override
            protected void initRules() {
                // add("codice", getRequiredConstraint());
                add("anagrafica." + Anagrafica.PROP_DENOMINAZIONE, getRequiredConstraint());
                add("anagrafica.sedeAnagrafica." + SedeAnagrafica.PROP_DESCRIZIONE, getRequiredConstraint());
                add("anagrafica.sedeAnagrafica." + SedeAnagrafica.PROP_INDIRIZZO, getRequiredConstraint());
                add("anagrafica.sedeAnagrafica." + SedeAnagrafica.PROP_INDIRIZZO, getMaxCharConstraint(60));
                add("anagrafica.sedeAnagrafica." + SedeAnagrafica.PROP_WEB, getInternetAddressConstraint());
                add("anagrafica.sedeAnagrafica." + SedeAnagrafica.PROP_INDIRIZZO_MAIL, getEmailConstraint());
                add("anagrafica.sedeAnagrafica.indirizzoMailSpedizione", getEmailConstraint());
                add("anagrafica.sedeAnagrafica.indirizzoPEC", getEmailConstraint());
                add("anagrafica.sedeAnagrafica.datiGeografici.nazione", getDomainAttributeRequiredConstraint());
                // add("anagrafica.sedeAnagrafica." +
                // SedeAnagrafica.PROP_TELEFONO, NUMERIC_CONSTRAINT);
                // add("anagrafica.sedeAnagrafica." + SedeAnagrafica.PROP_FAX,
                // NUMERIC_CONSTRAINT);
                add(createPartitaIvaConstraint());
                add(createPartitaIvaOrCodiceFiscaleConstraint());
                add(getSedeEntitaNoteBloccoConstraint());
                // add(getCodiceIdentificativoFiscaleConstraint());

            }
        };
    }

    /**
     * Crea le rules per {@link CodiceFiscalePM}.
     *
     * @return rules create
     */
    private Rules createCodiceFiscalePMRules() {
        return new Rules(CodiceFiscalePM.class) {

            @Override
            protected void initRules() {
                add(CodiceFiscalePM.PROP_COGNOME, getRequiredConstraint());
                add(CodiceFiscalePM.PROP_COGNOME, getMinCharConstraint(3));
                add(CodiceFiscalePM.PROP_NOME, getRequiredConstraint());
                add(CodiceFiscalePM.PROP_NOME, getMinCharConstraint(3));
                add(CodiceFiscalePM.PROP_SESSO, getRequiredConstraint());
                add(CodiceFiscalePM.PROP_DATA_NASCITA, getRequiredConstraint());
                add("comune", getDomainAttributeRequiredConstraint());
                add("comune.codiceCatastale", getRequiredConstraint());
            }
        };
    }

    /**
     * Crea le rules per {@link CodiceIva}.
     *
     * @return rules create
     */
    private Rules createCodiceIva() {
        return new Rules(CodiceIva.class) {

            @Override
            protected void initRules() {
                add("codice", getRequiredConstraint());
                add("codice", getMaxCharConstraint(6));
                add("descrizioneInterna", getRequiredConstraint());
                add("descrizioneRegistro", getMaxCharConstraint(60));
                add("descrizioneInterna", getMaxCharConstraint(60));
                add("descrizioneDocumenti", getMaxCharConstraint(60));
                add("tipoCodiceIva", getRequiredConstraint());
                add("tipoCaratteristica", getRequiredConstraint());
                add("tipoTotalizzazione", getRequiredConstraint());
                add("percApplicazione", getRequiredConstraint());
                add("percIndetraibilita", getRequiredConstraint());
                add("indicatoreVolumeAffari", getRequiredConstraint());
                add("codiceEsportazioneDocumento", getMaxCharConstraint(1));
                add("tipologiaSpesometro", getRequiredConstraint());
            }
        };
    }

    /**
     * Crea le rules per {@link ContattoSedeEntita}.
     *
     * @return rules create
     */
    private Rules createContattiRules() {
        return new Rules(ContattoSedeEntita.class) {

            @Override
            protected void initRules() {
                add("contatto." + Contatto.PROP_NOME, getRequiredConstraint());
                add("contatto." + Contatto.PROP_EMAIL, getEmailConstraint());
                add("mansione", getDomainAttributeRequiredConstraint());
                add("sedeEntita", getDomainAttributeRequiredConstraint());
            }
        };
    }

    /**
     * Crea le rules per {@link ContattoSPes}.
     *
     * @return rules create
     */
    private Rules createContrattiRules() {
        return new Rules(ContrattoSpesometro.class) {

            @Override
            protected void initRules() {
                add("codice", getRequiredConstraint());
                add("dataInizio", getRequiredConstraint());
                add("dataFine", getRequiredConstraint());
                add("entita", getDomainAttributeRequiredConstraint());
            }
        };
    }

    /**
     * Crea le rules per {@link UnitaMisura}.
     *
     * @return rules create
     */
    private Rules createConversioneUnitaMisuraRules() {
        return new Rules(ConversioneUnitaMisura.class) {

            @Override
            protected void initRules() {
                add("unitaMisuraOrigine", getDomainAttributeRequiredConstraint());
                add("unitaMisuraDestinazione", getDomainAttributeRequiredConstraint());
                add("formula", getRequiredConstraint());
            }
        };
    }

    /**
     * Crea le rules per {@link Deposito}.
     *
     * @return rules create
     */
    private Rules createDepositoRules() {
        return new Rules(Deposito.class) {

            @Override
            protected void initRules() {
                add(Deposito.PROP_CODICE, getMaxCharConstraint(10));
                add(Deposito.PROP_CODICE, getRequiredConstraint());
                add(Deposito.PROP_DESCRIZIONE, getRequiredConstraint());
                add(Deposito.PROP_DESCRIZIONE, getMaxCharConstraint(130));
                add(Deposito.PROP_INDIRIZZO, getRequiredConstraint());
                add(Deposito.PROP_INDIRIZZO, getMaxCharConstraint(30));
                add("sedeDeposito", getDomainAttributeRequiredConstraint());
                add("tipoDeposito", getDomainAttributeRequiredConstraint());
            }
        };
    }

    /**
     * Crea le rules per {@link ParametriMail}.
     *
     * @return rules create
     */
    private Rules createDestinatarioRules() {
        return new Rules(Destinatario.class) {
            @Override
            protected void initRules() {
                add("email", getRequiredConstraint());
                add("email", getEmailConstraint());
            }
        };
    }

    /**
     * Crea le rules per {@link Filiale}.
     *
     * @return rules create
     */
    private Rules createFilialeRules() {
        return new Rules(Filiale.class) {

            @Override
            protected void initRules() {
                add(Filiale.PROP_DESCRIZIONE, getRequiredConstraint());
                add(Filiale.PROP_CODICE,
                        all(new Constraint[] { getRequiredConstraint(), maxLength(5), getNumericConstraint() }));
                add("banca", getDomainAttributeRequiredConstraint());
            }
        };
    }

    /**
     * Crea le rules per {@link FormaGiuridica}.
     *
     * @return rules create
     */
    private Rules createFormaGiuridicaRules() {
        return new Rules(FormaGiuridica.class) {

            @Override
            protected void initRules() {
                add(FormaGiuridica.PROP_SIGLA, getMaxCharConstraint(14));
                add(FormaGiuridica.PROP_SIGLA, getRequiredConstraint());
                add(FormaGiuridica.PROP_DESCRIZIONE, getRequiredConstraint());
            }
        };
    }

    /**
     * Crea le rules per {@link Fornitore}.
     *
     * @return rules create
     */
    private Rules createFornitoreRules() {
        return new Rules(Fornitore.class) {

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
                // add("codice", getRequiredConstraint());
                add("anagrafica." + Anagrafica.PROP_DENOMINAZIONE, getRequiredConstraint());
                add("anagrafica.sedeAnagrafica." + SedeAnagrafica.PROP_DESCRIZIONE, getRequiredConstraint());
                add("anagrafica.sedeAnagrafica." + SedeAnagrafica.PROP_INDIRIZZO, getRequiredConstraint());
                add("anagrafica.sedeAnagrafica." + SedeAnagrafica.PROP_WEB, getInternetAddressConstraint());
                add("anagrafica.sedeAnagrafica." + SedeAnagrafica.PROP_INDIRIZZO_MAIL, getEmailConstraint());
                add("anagrafica.sedeAnagrafica.indirizzoMailSpedizione", getEmailConstraint());
                add("anagrafica.sedeAnagrafica.indirizzoPEC", getEmailConstraint());
                add("anagrafica.sedeAnagrafica.datiGeografici.nazione", getDomainAttributeRequiredConstraint());
                // add("anagrafica.sedeAnagrafica." +
                // SedeAnagrafica.PROP_TELEFONO, NUMERIC_CONSTRAINT);
                // add("anagrafica.sedeAnagrafica." + SedeAnagrafica.PROP_FAX,
                // NUMERIC_CONSTRAINT);
                add(createPartitaIvaConstraint());
                add(createPartitaIvaOrCodiceFiscaleConstraint());
                add(getSedeEntitaNoteBloccoConstraint());
            }
        };
    }

    /**
     * Crea le rules per {@link LayoutStampa}.
     *
     * @return rules create
     */
    private Rules createLayoutStampaRules() {
        return new Rules(LayoutStampa.class) {

            @Override
            protected void initRules() {
                add("stampante", getRequiredConstraint());
            }
        };
    }

    /**
     * Crea le rules per {@link LivelloAmministrativo1}.
     *
     * @return rules create
     */
    private Rules createLivello1Rules() {
        return new Rules(LivelloAmministrativo1.class) {

            @Override
            protected void initRules() {
                add("codiceIstat", getMaxCharConstraint(3));
                add("nome", getRequiredConstraint());
                add("stato", getDomainAttributeRequiredConstraint());
                add("nome", getMaxCharConstraint(120));
            }
        };
    }

    /**
     * Crea le rules per {@link LivelloAmministrativo2}.
     *
     * @return rules create
     */
    private Rules createLivello2Rules() {
        return new Rules(LivelloAmministrativo2.class) {

            @Override
            protected void initRules() {
                add("codiceIstat", getMaxCharConstraint(3));
                add("nome", getRequiredConstraint());
                add("sigla", getMaxCharConstraint(2));
                add("nome", getMaxCharConstraint(120));
            }
        };
    }

    /**
     * Crea le rules per {@link LivelloAmministrativo3}.
     *
     * @return rules create
     */
    private Rules createLivello3Rules() {
        return new Rules(LivelloAmministrativo3.class) {

            @Override
            protected void initRules() {
                add("codiceIstat", getMaxCharConstraint(6));
                add("nome", getRequiredConstraint());
                add("codiceCatastale", getMaxCharConstraint(4));
                add("nome", getMaxCharConstraint(120));
            }
        };
    }

    /**
     * Crea le rules per {@link LivelloAmministrativo4}.
     *
     * @return rules create
     */
    private Rules createLivello4Rules() {
        return new Rules(LivelloAmministrativo4.class) {

            @Override
            protected void initRules() {
                add("nome", getRequiredConstraint());
                add("nome", getMaxCharConstraint(120));
            }
        };
    }

    /**
     * Crea le rules per Localita.
     *
     * @return rules create
     */
    private Rules createLocalitaRules() {
        return new Rules(Localita.class) {

            @Override
            protected void initRules() {
                add("descrizione", getRequiredConstraint());
                add("descrizione", getMaxCharConstraint(60));
            }
        };
    }

    /**
     * Crea le rules per {@link Mansione}.
     *
     * @return rules create
     */
    private Rules createMansioneRules() {
        return new Rules(Mansione.class) {

            @Override
            protected void initRules() {
                add(Mansione.PROP_DESCRIZIONE, getRequiredConstraint());
            }
        };
    }

    /**
     * Crea le rules per Nazione.
     *
     * @return rules create
     */
    private Rules createNazioneRules() {
        return new Rules(Nazione.class) {

            @Override
            protected void initRules() {
                add("codice", getRequiredConstraint());
                add("codice", getMaxCharConstraint(2));
                add("descrizione", getRequiredConstraint());
                add("descrizione", getMaxCharConstraint(60));
                add("codiceSpesometro", getMaxCharConstraint(3));
                add(getNazioneValutaSeIntraConstraint());
            }
        };
    }

    /**
     * Crea le rules per {@link NotaAutomatica}.
     *
     * @return rules create
     */
    private Rules createNotaAutomaticaRules() {
        return new Rules(NotaAutomatica.class) {

            @Override
            protected void initRules() {
                add("nota", getRequiredConstraint());
                add(getDataFineNotaAutomaticaConstraint());
                add(getDataInizioNotaAutomaticaConstraint());
            }
        };
    }

    /**
     * Crea le rules per {@link ParametriMail}.
     *
     * @return rules create
     */
    private Rules createParametriMailRules() {
        return new Rules(ParametriMail.class) {
            @Override
            protected void initRules() {
                add("datiMail", getDomainAttributeRequiredConstraint());
                add("nomeAllegato", getRequiredConstraint());
                add("nomeAllegato", getMinCharConstraint(3));
                add("oggetto", getRequiredConstraint());
                add("testo", getRequiredConstraint());
                add("destinatari", new CollectionValuesRequired());
                add("destinatari", getCollectionRequiredRequired());
            }
        };
    }

    /**
     * Crea le rules per {@link RapportoBancarioAzienda}.
     *
     * @return rules create
     */
    private Rules createRapportoBancarioAziendaRules() {
        return new Rules(RapportoBancarioAzienda.class) {

            @Override
            protected void initRules() {
                add(RapportoBancario.PROP_DESCRIZIONE, getRequiredConstraint());
                add(RapportoBancario.PROP_DESCRIZIONE, getMaxCharConstraint(40));
                add("banca", getDomainAttributeRequiredConstraint());
                add("filiale", getDomainAttributeRequiredConstraint());
                add("codicePaese", getMaxCharConstraint(2));
                add("checkDigit", getMaxCharConstraint(2));
                add(RapportoBancario.PROP_NUMERO, all(new Constraint[] { getAlphanumericConstraint(),
                        getLengthConstraint(12), getRequiredConstraint() }));
                add(RapportoBancario.PROP_CIN,
                        all(new Constraint[] { getAlphabeticConstraint(), maxLength(1), getRequiredConstraint() }));
                add(RapportoBancario.PROP_BIC, all(new Constraint[] { getAlphanumericConstraint(),
                        disjunction().add(getLengthConstraint(8)).add(getLengthConstraint(11)).add(not(required())) }));
                add(new ConfrontoDateConstraint(RapportoBancario.PROP_DATA_APERTURA, LessThanEqualTo.instance(),
                        RapportoBancario.PROP_DATA_CHIUSURA));
                add(RapportoBancario.PROP_DATA_APERTURA, getRequiredConstraint());
            }
        };
    }

    /**
     * Crea le rules per {@link RapportoBancarioAzienda}.
     *
     * @return rules create
     */
    private Rules createRapportoBancarioSedeEntitaRules() {
        return new Rules(RapportoBancarioSedeEntita.class) {

            @Override
            protected void initRules() {
                add(RapportoBancario.PROP_DESCRIZIONE, getRequiredConstraint());
                add(RapportoBancario.PROP_DESCRIZIONE, getMaxCharConstraint(40));
                add("banca", getDomainAttributeRequiredConstraint());
                add("filiale", getDomainAttributeRequiredConstraint());
                add("codicePaese", getMaxCharConstraint(2));
                add("checkDigit", getMaxCharConstraint(2));
                add(RapportoBancario.PROP_NUMERO, all(new Constraint[] { getAlphanumericConstraint(),
                        disjunction().add(getLengthConstraint(12)).add(not(required())) }));
                add(RapportoBancario.PROP_CIN, all(new Constraint[] { getAlphabeticConstraint(), maxLength(1) }));
                add(RapportoBancario.PROP_BIC, all(new Constraint[] { getAlphanumericConstraint(),
                        disjunction().add(getLengthConstraint(8)).add(getLengthConstraint(11)).add(not(required())) }));
            }
        };
    }

    /**
     * Crea le rules per {@link SedeAzienda}.
     *
     * @return rules create
     */
    private Rules createSedeAziendaRules() {
        return new Rules(SedeAzienda.class) {

            @Override
            protected void initRules() {
                add("sede." + SedeAnagrafica.PROP_DESCRIZIONE, getRequiredConstraint());
                add("sede." + SedeAnagrafica.PROP_DESCRIZIONE, getMaxCharConstraint(60));
                add("sede." + SedeAnagrafica.PROP_INDIRIZZO, getRequiredConstraint());
                add("sede." + SedeAnagrafica.PROP_INDIRIZZO, getMaxCharConstraint(60));
                add("sede." + SedeAnagrafica.PROP_WEB, getInternetAddressConstraint());
                add("sede." + SedeAnagrafica.PROP_INDIRIZZO_MAIL, getEmailConstraint());
                // add("sede." + SedeAnagrafica.PROP_TELEFONO,
                // NUMERIC_CONSTRAINT);
                add("sede." + SedeAnagrafica.PROP_FAX, getNumericConstraint());
                add("tipoSede", getDomainAttributeRequiredConstraint());
            }
        };
    }

    /**
     * Crea le rules per {@link SedeEntita}.
     *
     * @return rules create
     */
    private Rules createSedeEntitaRules() {
        return new Rules(SedeEntita.class) {

            @Override
            protected void initRules() {
                add("sede." + SedeAnagrafica.PROP_DESCRIZIONE, getRequiredConstraint());
                add("sede." + SedeAnagrafica.PROP_DESCRIZIONE, getMaxCharConstraint(60));
                add("sede." + SedeAnagrafica.PROP_INDIRIZZO, getMaxCharConstraint(60));
                add("sede." + SedeAnagrafica.PROP_WEB, getInternetAddressConstraint());
                add("sede." + SedeAnagrafica.PROP_INDIRIZZO_MAIL, getEmailConstraint());
                add("sede.indirizzoMailSpedizione", getEmailConstraint());
                add("sede.indirizzoPEC", getEmailConstraint());
                add("tipoSede", getDomainAttributeRequiredConstraint());
                add("cig", getMaxCharConstraint(15));
                add("cup", getMaxCharConstraint(15));
                add("lingua", getRequiredConstraint());
                add(getSedeEntitaNoteBloccoConstraint());
            }
        };
    }

    /**
     * Crea le rules per {@link TipoDeposito}.
     *
     * @return rules create
     */
    private Rules createTipoDepositoRules() {
        return new Rules(TipoDeposito.class) {
            @Override
            protected void initRules() {
                add("codice", getRequiredConstraint());
                add("codice", getMaxCharConstraint(20));
            }
        };
    }

    /**
     * Crea le rules per {@link TipoDocumento}.
     *
     * @return rules create
     */
    private Rules createTipoDocumentoRules() {
        return new Rules(TipoDocumento.class) {

            @Override
            protected void initRules() {
                add("codice", getRequiredConstraint());
                add("descrizione", getRequiredConstraint());
                add("tipoEntita", getRequiredConstraint());
                add("classeTipoDocumentoInstance", getRequiredConstraint());
                add("gruppo", getDomainAttributeRequiredConstraint());
                add(getPatternCodiceDocumentoConstraint());
            };
        };
    }

    /**
     * Crea le rules per {@link TipoSedeEntita}.
     *
     * @return rules create
     */
    private Rules createTipoSedeEntitaRules() {
        return new Rules(TipoSedeEntita.class) {

            @Override
            protected void initRules() {
                add(TipoSedeEntita.PROP_CODICE, getRequiredConstraint());
                add(TipoSedeEntita.PROP_DESCRIZIONE, getRequiredConstraint());
                add(TipoSedeEntita.PROP_TIPO_SEDE, getRequiredConstraint());
            }
        };
    }

    /**
     * Crea le rules per {@link UnitaMisura}.
     *
     * @return rules create
     */
    private Rules createUnitaMisuraRules() {
        return new Rules(UnitaMisura.class) {

            @Override
            protected void initRules() {
                add("codice", getRequiredConstraint());
                add("codice", maxLength(3));
            }
        };
    }

    /**
     * Crea le rules per {@link UserChangePassword}.
     *
     * @return rules create
     */
    private Rules createUserChangePasswordRules() {
        return new Rules(UserChangePassword.class) {

            @Override
            protected void initRules() {
                add("newPassword", getRequiredConstraint());
                add("newPasswordConfirm", getRequiredConstraint());
                add(eqProperty("newPasswordConfirm", "newPassword"));
            }
        };
    }

    /**
     * Crea le rules per {@link Vettore}.
     *
     * @return rules create
     */
    private Rules createVettoreRules() {
        return new Rules(Vettore.class) {

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
                // add("codice", getRequiredConstraint());
                add("anagrafica." + Anagrafica.PROP_DENOMINAZIONE, getRequiredConstraint());
                add("anagrafica.sedeAnagrafica." + SedeAnagrafica.PROP_DESCRIZIONE, getRequiredConstraint());
                add("anagrafica.sedeAnagrafica." + SedeAnagrafica.PROP_INDIRIZZO, getRequiredConstraint());
                add("anagrafica.sedeAnagrafica." + SedeAnagrafica.PROP_WEB, getInternetAddressConstraint());
                add("anagrafica.sedeAnagrafica." + SedeAnagrafica.PROP_INDIRIZZO_MAIL, getEmailConstraint());
                add("anagrafica.sedeAnagrafica.indirizzoMailSpedizione", getEmailConstraint());
                add("anagrafica.sedeAnagrafica.indirizzoPEC", getEmailConstraint());
                add("anagrafica.sedeAnagrafica.datiGeografici.nazione", getDomainAttributeRequiredConstraint());
                // add("anagrafica.sedeAnagrafica." +
                // SedeAnagrafica.PROP_TELEFONO, NUMERIC_CONSTRAINT);
                // add("anagrafica.sedeAnagrafica." + SedeAnagrafica.PROP_FAX,
                // NUMERIC_CONSTRAINT);
                add("numeroCopiePerStampa", getRequiredConstraint());
                add(createPartitaIvaConstraint());
                add(createPartitaIvaOrCodiceFiscaleConstraint());

                add("datiSpedizioni.codiceClienteMittenteItalia", getMaxCharConstraint(7));
                add("datiSpedizioni.codiceClienteMittenteEstero", getMaxCharConstraint(7));
                add("datiSpedizioni.codiceTariffaItalia", getMaxCharConstraint(3));
                add("datiSpedizioni.codiceTariffaEstero", getMaxCharConstraint(3));
                add("datiSpedizioni.tipoStampante", getMaxCharConstraint(10));
                add("datiSpedizioni.puntoOperativoPartenzaMerce", getMaxCharConstraint(3));
                add("datiSpedizioni.tipoServizioBolle", getMaxCharConstraint(1));
                add("datiSpedizioni.numeroSerie", getMaxCharConstraint(2));
                add("datiSpedizioni.puntoOperativoArrivo", getMaxCharConstraint(3));
                add("datiSpedizioni.naturaMerceMittente", getMaxCharConstraint(15));
                add("datiSpedizioni.codiceTrattamentoMerce", getMaxCharConstraint(2));
            }
        };
    }

    /**
     * Crea le rules per {@link ZonaGeografica}.
     *
     * @return rules create
     */
    private Rules createZonaGeograficaRules() {
        return new Rules(ZonaGeografica.class) {
            @Override
            protected void initRules() {
                add("codice", getRequiredConstraint());
                add("codice", getMaxCharConstraint(10));
                add("descrizione", getRequiredConstraint());
                add("descrizione", getMaxCharConstraint(30));
            }
        };
    }

    /**
     * @return constraint per rendere obbligatoria la data inizio delle note automatiche se c'è il flag ripeti
     */
    private PropertyConstraint getDataFineNotaAutomaticaConstraint() {
        PropertyConstraint propertyConstraint = new PropertyValueConstraint("ripetiAnnualmente", eq(true));
        PropertyConstraint dataInizioConstraint = new RequiredIfTrue("dataFine", propertyConstraint);
        PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(
                dataInizioConstraint);
        propertyResolvableConstraint.setType("dataPerRipertiRequired");
        return propertyResolvableConstraint;
    }

    /**
     * @return constraint per rendere obbligatoria la data inizio delle note automatiche se c'è il flag ripeti
     */
    private PropertyConstraint getDataInizioNotaAutomaticaConstraint() {
        PropertyConstraint propertyConstraint = new PropertyValueConstraint("ripetiAnnualmente", eq(true));
        PropertyConstraint dataInizioConstraint = new RequiredIfTrue("dataInizio", propertyConstraint);
        PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(
                dataInizioConstraint);
        propertyResolvableConstraint.setType("dataPerRipertiRequired");
        return propertyResolvableConstraint;
    }

    /**
     * @return constraint per rendere obbligatoria la valuta per nazioni intra
     */
    private PropertyConstraint getNazioneValutaSeIntraConstraint() {
        PropertyConstraint propertyConstraint = new PropertyValueConstraint("intra", eq(true));
        PropertyConstraint valutaIfIntra = new RequiredIfTrue("codiceValuta", propertyConstraint);
        PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(valutaIfIntra);
        propertyResolvableConstraint.setType("valutaNazioneIntraRequired");
        return propertyResolvableConstraint;
    }

    /**
     * Crea la regola di validazione del pattern di generazione del codice documento.
     *
     * @return constraint
     */
    private PropertyConstraint getPatternCodiceDocumentoConstraint() {
        PropertyConstraint propertyConstraint = new PropertyValueConstraint("registroProtocollo", present());
        PropertyConstraint constraint = new PatternCodiceDocumentoValueConstraint("patternNumeroDocumento",
                propertyConstraint);

        PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(constraint);
        propertyResolvableConstraint.setType("patternNumeroDocumentoRequired");
        return propertyResolvableConstraint;
    }

    @Override
    public List<Rules> getRules() {
        List<Rules> listRules = new ArrayList<Rules>();

        // dati geografici
        listRules.add(createNazioneRules());
        listRules.add(createLivello1Rules());
        listRules.add(createLivello2Rules());
        listRules.add(createLivello3Rules());
        listRules.add(createLivello4Rules());
        listRules.add(createLocalitaRules());
        listRules.add(createCapRules());

        listRules.add(createMansioneRules());
        listRules.add(createTipoSedeEntitaRules());
        listRules.add(createCaricaRules());
        listRules.add(createFormaGiuridicaRules());

        listRules.add(createBancaRules());
        listRules.add(createFilialeRules());

        // azienda
        listRules.add(createAziendaAnagraficaRules());
        listRules.add(createRapportoBancarioAziendaRules());

        // entita'
        listRules.add(createFornitoreRules());
        listRules.add(createClienteRules());
        listRules.add(createVettoreRules());
        listRules.add(createRapportoBancarioSedeEntitaRules());

        // sedi entità e azienda
        listRules.add(createSedeEntitaRules());
        listRules.add(createSedeAziendaRules());

        // depositi sede azienda e contatti sede entita'
        listRules.add(createContattiRules());
        listRules.add(createDepositoRules());

        // varie
        listRules.add(createCodiceFiscalePMRules());
        listRules.add(createTipoDocumentoRules());

        listRules.add(createCodiceIva());

        listRules.add(createZonaGeograficaRules());

        listRules.add(createTipoDepositoRules());
        listRules.add(createUnitaMisuraRules());
        listRules.add(createConversioneUnitaMisuraRules());
        listRules.add(createCategoriEntitaRules());

        listRules.add(createParametriMailRules());
        listRules.add(createDestinatarioRules());

        listRules.add(createCambioValutaAziendaRules());
        listRules.add(createContrattiRules());

        listRules.add(createUserChangePasswordRules());

        listRules.add(createLayoutStampaRules());

        listRules.add(createNotaAutomaticaRules());

        return listRules;
    }

    /**
     *
     * @return constraint per il mezzo trasporto dell'area magazzino.
     */
    private PropertyConstraint getSedeEntitaNoteBloccoConstraint() {
        PropertyConstraint propertyConstraint = new PropertyValueConstraint("bloccoSede.blocco", eq(true));
        PropertyConstraint domainObjectIfTrue = new RequiredIfTrue("bloccoSede.noteBlocco", propertyConstraint);
        PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(
                domainObjectIfTrue);
        propertyResolvableConstraint.setType("sedeEntitaBloccoSedeRequired");
        return propertyResolvableConstraint;
    }
}
