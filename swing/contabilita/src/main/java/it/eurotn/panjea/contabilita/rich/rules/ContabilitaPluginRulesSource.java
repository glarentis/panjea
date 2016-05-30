/**
 *
 */
package it.eurotn.panjea.contabilita.rich.rules;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.rules.Rules;
import org.springframework.rules.constraint.Constraint;
import org.springframework.rules.constraint.GreaterThanEqualTo;
import org.springframework.rules.constraint.Or;
import org.springframework.rules.constraint.property.ConditionalPropertyConstraint;
import org.springframework.rules.constraint.property.PropertiesConstraint;
import org.springframework.rules.constraint.property.PropertyConstraint;
import org.springframework.rules.constraint.property.PropertyValueConstraint;
import org.springframework.rules.constraint.property.RequiredIfTrue;

import com.jidesoft.comparator.NumberComparator;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.contabilita.domain.AreaContabile;
import it.eurotn.panjea.contabilita.domain.CausaleRitenutaAcconto;
import it.eurotn.panjea.contabilita.domain.ContabilitaSettings;
import it.eurotn.panjea.contabilita.domain.ContabilitaSettings.ETipoPeriodicita;
import it.eurotn.panjea.contabilita.domain.Conto;
import it.eurotn.panjea.contabilita.domain.ContoBase;
import it.eurotn.panjea.contabilita.domain.ContributoPrevidenziale;
import it.eurotn.panjea.contabilita.domain.ControPartita;
import it.eurotn.panjea.contabilita.domain.Mastro;
import it.eurotn.panjea.contabilita.domain.RegistroIva;
import it.eurotn.panjea.contabilita.domain.RegistroIva.TipoRegistro;
import it.eurotn.panjea.contabilita.domain.RigaContabile;
import it.eurotn.panjea.contabilita.domain.SottoConto;
import it.eurotn.panjea.contabilita.domain.StrutturaContabile;
import it.eurotn.panjea.contabilita.domain.TipoAreaContabile;
import it.eurotn.panjea.contabilita.domain.TipoAreaContabile.GestioneIva;
import it.eurotn.panjea.contabilita.domain.TipoDocumentoBase;
import it.eurotn.panjea.contabilita.rich.editors.riepilogoblacklist.ParametriRicercaRiepilogoBlacklist;
import it.eurotn.panjea.contabilita.rich.pm.StrutturaContabilePM;
import it.eurotn.panjea.contabilita.util.AreaContabileFullDTO;
import it.eurotn.panjea.contabilita.util.ParametriAperturaContabile;
import it.eurotn.panjea.contabilita.util.ParametriChiusuraContabile;
import it.eurotn.panjea.contabilita.util.ParametriRicercaBilancio;
import it.eurotn.panjea.contabilita.util.ParametriRicercaBilancioConfronto;
import it.eurotn.panjea.contabilita.util.ParametriRicercaEstrattoConto;
import it.eurotn.panjea.contabilita.util.ParametriRicercaMovimentiContabili;
import it.eurotn.panjea.contabilita.util.ParametriRicercaSituazioneEP;
import it.eurotn.panjea.contabilita.util.ParametriSituazioneRitenuteAcconto;
import it.eurotn.panjea.pagamenti.domain.CodicePagamento;
import it.eurotn.panjea.partite.domain.TipoAreaPartita;
import it.eurotn.panjea.rich.rules.ConditionalPropertyValueConstraint;
import it.eurotn.panjea.rich.rules.ConfrontoDateConstraint;
import it.eurotn.panjea.rich.rules.PatternCodiceDocumentoValueConstraint;
import it.eurotn.panjea.rich.rules.PropertyResolvableConstraint;
import it.eurotn.panjea.rules.AbstractPluginRulesSource;

/**
 *
 * @author fattazzo
 * @version 1.0, 08/mag/07
 *
 */
public class ContabilitaPluginRulesSource extends AbstractPluginRulesSource {

    /**
     * crea le regole di validazione per {@link AreaContabile}.
     * 
     * @return regole create
     * 
     */
    private Rules createAreaContabileRules() {
        return new Rules(AreaContabile.class) {
            @Override
            protected void initRules() {
                add(getDatiRitenutaAreaContabileConstraint("fondoProfessionistiPresente", "percFondoProfessionisti"));
                add(getDatiRitenutaAreaContabileConstraint("causaleRitenutaPresente", "percentualeAliquota"));
                add(getDatiRitenutaAreaContabileConstraint("causaleRitenutaPresente", "percentualeImponibile"));
                add(getDatiRitenutaAreaContabileConstraint("causaleRitenutaPresente", "tributo"));
                add(getDatiRitenutaAreaContabileConstraint("causaleRitenutaPresente", "sezione"));
                add(getDatiRitenutaAreaContabileConstraint("contributoPrevidenzialePresente", "percContributiva"));
                add(getDatiRitenutaAreaContabileConstraint("contributoPrevidenzialePresente",
                        "percPrevidenzialeCaricoLavoratore"));
                add(getDatiRitenutaAreaContabileConstraint("contributoPrevidenzialePresente",
                        "percPrevidenzialeCaricoAzienda"));
            }
        };
    }

    /**
     * crea le regole di validazione per {@link CausaleRitenutaAcconto}.
     * 
     * @return regole create
     */
    private Rules createCausaleRitenutaAccontoRules() {
        return new Rules(CausaleRitenutaAcconto.class) {

            @Override
            protected void initRules() {
                add("codice", getRequiredConstraint());
                add("descrizione", getRequiredConstraint());
                add("percentualeAliquota", getRequiredConstraint());
                add("percentualeImponibile", getRequiredConstraint());
                add("tributo", getRequiredConstraint());
                add("sezione", getRequiredConstraint());
            }
        };
    }

    /**
     * crea le regole di validazione per {@link ContabilitaSettings}.
     * 
     * @return regole create
     * 
     */
    private Rules createContabilitaSettingsRules() {
        return new Rules(ContabilitaSettings.class) {
            @Override
            protected void initRules() {
                add(getPercTrimestraleContabilitaConstraint());
            }
        };
    }

    /**
     * crea le regole di validazione per {@link ContoBase}.
     * 
     * @return regole create
     */
    private Rules createContoBaseRules() {
        return new Rules(ContoBase.class) {

            @Override
            protected void initRules() {
                add("descrizione", getRequiredConstraint());
                add("descrizione", getMaxCharConstraint(50));
                add("sottoConto", getDomainAttributeRequiredConstraint());
                add("tipoContoBase", getRequiredConstraint());
            }
        };
    }

    /**
     * crea le regole di validazione per {@link Conto}.
     * 
     * @return regole create
     */
    private Rules createContoRules() {
        return new Rules(Conto.class) {

            @Override
            protected void initRules() {
                add("codice", new Constraint[] { getRequiredConstraint(), getLengthConstraint(3), not(eq("000")) });
                add("descrizione", getRequiredConstraint());
                add("tipoConto", getRequiredConstraint());
            }
        };
    }

    /**
     * crea le regole di validazione per {@link ContributoPrevidenziale}.
     * 
     * @return regole create
     */
    private Rules createContributoPrevidenzialeRules() {
        return new Rules(ContributoPrevidenziale.class) {

            @Override
            protected void initRules() {
                add("codice", getRequiredConstraint());
                add("percContributiva", getRequiredConstraint());
                add("percCaricoLavoratore", getRequiredConstraint());
            }
        };
    }

    /**
     * crea le regole di validazione per {@link ControPartita}.
     * 
     * @return regole create
     */
    private Rules createControPartitaRules() {
        return new Rules(ControPartita.class) {
            @Override
            protected void initRules() {
                add("descrizione", getRequiredConstraint());
                add("descrizione", getMaxCharConstraint(40));
                add("ordine", getRequiredConstraint());
            };
        };
    }

    /**
     * crea le regole di validazione per {@link AreaContabileFullDTO}.
     * 
     * @return regole create
     */
    private Rules createDocumentoRules() {
        return new Rules(AreaContabileFullDTO.class) {

            @Override
            protected void initRules() {
                add("areaContabile.dataRegistrazione", getRequiredConstraint());
                add("areaContabile.documento.dataDocumento", getRequiredConstraint());
                add(new ConfrontoDataAnnoConstraint("areaContabile.dataRegistrazione", GreaterThanEqualTo.instance(),
                        "areaContabile.annoMovimento"));
                add(new ConfrontoDateConstraint("areaContabile.dataRegistrazione", GreaterThanEqualTo.instance(),
                        "areaContabile.documento.dataDocumento"));

                add("areaContabile.tipoAreaContabile", getDomainAttributeRequiredConstraint());
                Comparator<BigDecimal> comparator = new Comparator<BigDecimal>() {

                    @Override
                    public int compare(BigDecimal o1, BigDecimal o2) {
                        return o1.compareTo(o2);
                    }

                };
                add("areaContabile.documento.totale.codiceValuta", getRequiredConstraint());
                add("areaContabile.documento.totale.tassoDiCambio",
                        new Constraint[] { getRequiredConstraint(), not(eq(BigDecimal.ZERO, comparator)) });

                add(getAreaContabileEntitaConstraint());
                add(getAreaContabileSedeEntitaConstraint());
                add(getAreaContabileBancaConstraint());

                // Rules per AreaPartita
                add(getCodicePagamentoConstraint());
            }
        };
    }

    /**
     * crea le regole di validazione per {@link Mastro}.
     * 
     * @return regole create
     */
    private Rules createMastroRules() {
        return new Rules(Mastro.class) {
            @Override
            protected void initRules() {
                add("codice", getRequiredConstraint());
                add("codice", getLengthConstraint(3));
                add("codice", not(eq("000")));
                add("descrizione", getRequiredConstraint());
            }
        };
    }

    /**
     * crea le regole di validazione per {@link ParametriAperturaContabile}.
     * 
     * @return regole create
     */
    private Rules createParametriAperturaContabile() {
        return new Rules(ParametriAperturaContabile.class) {
            @Override
            protected void initRules() {
                add("anno", getRequiredConstraint());
                add("dataMovimento", conjunction().add(eqProperty("anno", "annoMovimento")).add(required()));
            };
        };
    }

    /**
     * crea le regole di validazione per {@link ParametriChiusuraContabile}.
     * 
     * @return regole create
     */
    private Rules createParametriChiusuraContabile() {
        return new Rules(ParametriChiusuraContabile.class) {

            @Override
            protected void initRules() {
                add("anno", getRequiredConstraint());
                add("dataMovimento",
                        conjunction().add(
                                or(eqProperty("anno", "annoMovimento"), eqProperty("anno", "annoMovimentoPrecedente")))
                                .add(required()));
            };
        };
    }

    /**
     * crea le regole di validazione per {@link ParametriRicercaBilancio}.
     * 
     * @return regole create
     */
    private Rules createParametriRicercaBilancio() {
        return new Rules(ParametriRicercaBilancio.class) {

            @Override
            protected void initRules() {
                add("annoCompetenza", getRequiredConstraint());
                add("dataRegistrazione", getPeriodoConstraint(false));
                add(disjunction().add(eq("stampaConti", true)).add(eq("stampaClienti", true))
                        .add(eq("stampaFornitori", true)));
            }
        };
    }

    /**
     * crea le regole di validazione per {@link ParametriRicercaBilancioConfronto}.
     * 
     * @return regole create
     */
    private Rules createParametriRicercaBilancioConfronto() {
        return new Rules(ParametriRicercaBilancioConfronto.class) {

            @Override
            protected void initRules() {
                add("annoCompetenza", getRequiredConstraint());
                add("annoCompetenza2", getRequiredConstraint());
                add("dataRegistrazione", getPeriodoConstraint(true));
                add("dataRegistrazione2", getPeriodoConstraint(true));
            }
        };
    }

    /**
     * crea le regole di validazione per {@link ParametriRicercaEstrattoConto}.
     * 
     * @return regole create
     */
    private Rules createParametriRicercaEstrattoConto() {
        return new Rules(ParametriRicercaEstrattoConto.class) {

            @Override
            protected void initRules() {
                add("annoCompetenza", getRequiredConstraint());
                add("dataRegistrazione", getPeriodoConstraint(true));

                add(getEstrattoContoCentroCostoConstraint());
                add(getEstrattoContoSottoContoConstraint());
            }
        };
    }

    /**
     * crea le regole di validazione per {@link ParametriRicercaMovimentiContabili}.
     * 
     * @return regole create
     */
    private Rules createParametriRicercaMovimentiContabili() {
        return new Rules(ParametriRicercaMovimentiContabili.class) {

            @Override
            protected void initRules() {
                add("annoCompetenza", new Or(regexp("^([1-9][0-9][0-9][0-9])$", "annoCompetenzaControlloMovimenti"),
                        not(required())));

                add("dataRegistrazione", getPeriodoConstraint(false));
                add("dataDocumento", getPeriodoConstraint(false));
            }
        };
    }

    /**
     * crea le regole di validazione per {@link ParametriRicercaRiepilogoBlacklist}.
     * 
     * @return regole create
     */
    private Rules createParametriRicercaRiepilogoBlacklistRules() {
        return new Rules(ParametriRicercaRiepilogoBlacklist.class) {

            @Override
            protected void initRules() {
                add("dataIniziale", getRequiredConstraint());
                add("dataFinale", getRequiredConstraint());
            }
        };
    }

    /**
     * crea le regole di validazione per {@link ParametriRicercaSituazioneEP}.
     * 
     * @return regole create
     */
    private Rules createParametriRicercaSituazioneEP() {
        return new Rules(ParametriRicercaSituazioneEP.class) {

            @Override
            protected void initRules() {
                add("annoCompetenza", getRequiredConstraint());
                add("dataRegistrazione", getPeriodoConstraint(false));
            }
        };
    }

    /**
     * crea le regole di validazione per {@link ParametriSituazioneRitenuteAcconto}.
     * 
     * @return regole create
     * 
     */
    private Rules createParametriSituazioneRitenuteAccontoRules() {
        return new Rules(ParametriSituazioneRitenuteAcconto.class) {
            @Override
            protected void initRules() {
                add("periodo", getPeriodoConstraint(true));
            }
        };
    }

    /**
     * crea le regole di validazione per {@link RegistroIva}.
     * 
     * @return regole create
     */
    private Rules createRegistroIvaRules() {
        return new Rules(RegistroIva.class) {

            @Override
            protected void initRules() {
                add("descrizione", new Constraint[] { getRequiredConstraint(), getMaxCharConstraint(60) });
                add("numero", getRequiredConstraint());
                add("tipoRegistro", getRequiredConstraint());
            }
        };
    }

    /**
     * crea le regole di validazione per {@link RigaContabile}.
     * 
     * @return regole create
     */
    private Rules createRigaContabileRules() {
        return new Rules(RigaContabile.class) {

            @Override
            protected void initRules() {
                /*
                 * L'importo della riga contabile è sempre richiesto ma se il totale documento è diverso da zero deve
                 * esserlo pure l'importo della riga
                 */
                ConditionalPropertyValueConstraint importoConstraint = new ConditionalPropertyValueConstraint("importo",
                        new PropertyValueConstraint("areaContabile.documento.totale.importoInValutaAzienda",
                                eq(BigDecimal.ZERO, NumberComparator.getInstance())),
                        getRequiredConstraint(),
                        conjunction().add(getRequiredConstraint())
                                .add(not(eq(BigDecimal.ZERO, NumberComparator.getInstance()))),
                        "importoRigaContabileConstraint");
                add(importoConstraint);
                add(getContoDareConstraint());
                add(getContoAvereConstraint());
                /*
                 * la ConditionalPropertyConstraint utilizza la proprietà della constraint passata come if
                 * (ifConstraint) per inserire la proprietà da renderizzare sul pannello dell'errore (in questo caso
                 * renderizzerebbe conto.soggettoCentroCosto + messaggio internazionalizzato tramite il setType). Per
                 * ovviare sovrascrivo la getPropertyName() restituendo la proprietà che mi serve NB. Questo è possibile
                 * perchè la ConditionalPropertyConstraint non utilizza il property name per testare i valori, ma le
                 * constraint passate al costruttore.
                 */
                ConditionalPropertyConstraint righeCentroCostoConstraint = new ConditionalPropertyConstraint(
                        eq("conto.soggettoCentroCosto", Boolean.TRUE),
                        new PropertiesConstraint("importo", new RigheCentroCostoConstraint(), "righeCentroCosto")) {
                    @Override
                    public String getPropertyName() {
                        return "importo";
                    }
                };
                righeCentroCostoConstraint.setType("righeCentroCostoConstraint");
                add(righeCentroCostoConstraint);

                ConditionalPropertyConstraint righeRateiRiscontiConstraint = new ConditionalPropertyConstraint(
                        eq("rateiRiscontiAttivi", Boolean.TRUE), new PropertiesConstraint("importoRateoRisconto",
                                new RigheRateiRiscontiConstraint(), "righeRateoRisconto")) {
                    @Override
                    public String getPropertyName() {
                        return "importoRateoRisconto";
                    }
                };
                righeRateiRiscontiConstraint.setType("righeRateiRiscontiConstraint");
                add(righeRateiRiscontiConstraint);
            }
        };
    }

    /**
     * crea le regole di validazione per {@link SottoConto}.
     * 
     * @return regole create
     */
    private Rules createSottoContoRules() {
        return new Rules(SottoConto.class) {

            @Override
            protected void initRules() {
                add("codice", getRequiredConstraint());
                add("codice", not(eq("000000")));
                add("codice", getLengthConstraint(6));
                add("descrizione", getRequiredConstraint());
                add("classificazioneConto", getRequiredConstraint());
            }
        };
    }

    /**
     * crea le regole di validazione per {@link StrutturaContabilePM}.
     * 
     * @return regole create
     */
    private Rules createStrutturaContabilePMRules() {
        return new Rules(StrutturaContabilePM.class) {

            @Override
            protected void initRules() {
                add("formula", getRequiredConstraint());
                add("ordine", getRequiredConstraint());
            };
        };
    }

    /**
     * crea le regole di validazione per {@link StrutturaContabile}.
     * 
     * @return regole create
     */
    private Rules createStrutturaContabileRules() {
        return new Rules(StrutturaContabile.class) {
            @Override
            protected void initRules() {
                add("ordine", getRequiredConstraint());
            };
        };
    }

    /**
     * crea le regole di validazione per {@link TipoAreaContabile}.
     * 
     * @return regole create
     */
    private Rules createTipoAreaContabile() {
        return new Rules(TipoAreaContabile.class) {

            @Override
            protected void initRules() {
                add("gestioneIva", getRequiredConstraint());
                add(getRegistroIvaConstraint());
                add(getTipologiaCorrispettivoConstraint());
                add(getRegistroIvaCollegatoConstraint());
                add("statoAreaContabileGenerata", getRequiredConstraint());
                add(getTipoRitenutaAccontoConstraint());
                add(getPatternCodiceTACProtocolloConstraint());
                add(getPatternCodiceTACProtocolloCollegatoConstraint());
            }
        };
    }

    /**
     * crea le regole di validazione per {@link TipoDocumentoBase}.
     * 
     * @return regole create
     */
    private Rules createTipoDocumentoBase() {
        return new Rules(TipoDocumentoBase.class) {

            @Override
            protected void initRules() {
                add("tipoAreaContabile", getRequiredConstraint());
                add("tipoOperazione", getRequiredConstraint());
            };
        };
    }

    /**
     * Crea la regola di validazione per la banca dell'area contabile.
     * 
     * @return constraint
     */
    private PropertyConstraint getAreaContabileBancaConstraint() {
        PropertyConstraint propertyConstraint = new PropertyValueConstraint(
                "areaContabile.tipoAreaContabile.tipoDocumento.tipoEntita", eq(TipoEntita.BANCA));
        PropertyConstraint domainObjectIfTrue = new RequiredIfTrue("areaContabile.documento.rapportoBancarioAzienda",
                propertyConstraint);
        PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(
                domainObjectIfTrue);
        propertyResolvableConstraint.setType("areaContabileEntitaRequired");
        return propertyResolvableConstraint;
    }

    /**
     * Crea la regola di validazione per l'entita dell'area contabile.
     * 
     * @return constraint.0
     */
    private PropertyConstraint getAreaContabileEntitaConstraint() {
        PropertyConstraint propertyConstraint = new PropertyValueConstraint(
                "areaContabile.tipoAreaContabile.tipoDocumento.tipoEntita",
                new Or(eq(TipoEntita.CLIENTE), eq(TipoEntita.FORNITORE)));
        PropertyConstraint domainObjectIfTrue = new RequiredIfTrue("areaContabile.documento.entita",
                propertyConstraint);
        PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(
                domainObjectIfTrue);
        propertyResolvableConstraint.setType("areaContabileEntitaRequired");
        return propertyResolvableConstraint;
    }

    /**
     * Crea la regola di validazione per l'entita dell'area contabile.
     * 
     * @return constraint.0
     */
    private PropertyConstraint getAreaContabileSedeEntitaConstraint() {
        PropertyConstraint propertyConstraint = new PropertyValueConstraint(
                "areaContabile.tipoAreaContabile.tipoDocumento.tipoEntita",
                new Or(eq(TipoEntita.CLIENTE), eq(TipoEntita.FORNITORE)));
        PropertyConstraint domainObjectIfTrue = new RequiredIfTrue("areaContabile.documento.sedeEntita",
                propertyConstraint);
        PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(
                domainObjectIfTrue);
        propertyResolvableConstraint.setType("areaContabileEntitaRequired");
        return propertyResolvableConstraint;
    }

    /**
     * Crea la regola di validazione per datiIntermediario.codiceFiscale di ParametriCreazioneSpesometro.<br>
     * Obbligatorio se viene inserito il numero Iscrizione Albo.
     * 
     * @return constraint
     */
    private PropertyConstraint getCodiceFiscale1SpesometroConstraint() {
        PropertyConstraint propertyConstraint1 = new PropertyValueConstraint("datiIntermediario.numeroIscrizioneAlbo",
                present());
        PropertyConstraint domainObjectIfTrue = new RequiredIfTrue("datiIntermediario.codiceFiscale",
                propertyConstraint1);
        PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(
                domainObjectIfTrue);
        propertyResolvableConstraint.setType("proprietaSpesometroRequired");
        return propertyResolvableConstraint;
    }

    /**
     * Crea la regola di validazione per datiIntermediario.codiceFiscale di ParametriCreazioneSpesometro.<br>
     * Obbligatorio se viene inserita la data Impegno.
     * 
     * @return constraint
     */
    private PropertyConstraint getCodiceFiscale2SpesometroConstraint() {
        PropertyConstraint propertyConstraint2 = new PropertyValueConstraint("datiIntermediario.dataImpegno",
                present());
        PropertyConstraint domainObjectIfTrue = new RequiredIfTrue("datiIntermediario.codiceFiscale",
                propertyConstraint2);
        PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(
                domainObjectIfTrue);
        propertyResolvableConstraint.setType("proprietaSpesometroRequired");
        return propertyResolvableConstraint;
    }

    /**
     * Crea la regola di validazione per datiIntermediario.codiceFiscale di ParametriCreazioneSpesometro.<br>
     * Obbligatorio se viene inserita la tipologia Comunicazione.
     * 
     * @return constraint
     */
    private PropertyConstraint getCodiceFiscale3SpesometroConstraint() {
        PropertyConstraint propertyConstraint3 = new PropertyValueConstraint("datiIntermediario.tipologiaComunicazione",
                present());
        PropertyConstraint domainObjectIfTrue = new RequiredIfTrue("datiIntermediario.codiceFiscale",
                propertyConstraint3);
        PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(
                domainObjectIfTrue);
        propertyResolvableConstraint.setType("proprietaSpesometroRequired");
        return propertyResolvableConstraint;
    }

    /**
     * Constraint per la fissare l'attributo {@link CodicePagamento} required se {@link TipoDocumento} ha un
     * {@link TipoAreaPartita} collegato<br>
     * {@link AreaContabileFullDTO} E' possibile caricarla solamente attraverso TipoDocumento.
     * 
     * @return PropertyConstraint
     */
    private PropertyConstraint getCodicePagamentoConstraint() {
        PropertyConstraint propertyConstraint = new PropertyValueConstraint("areaRateEnabled", eq(true));
        PropertyConstraint domainObjectIfTrue = new RequiredIfTrue("areaRate.codicePagamento", propertyConstraint);
        PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(
                domainObjectIfTrue);
        propertyResolvableConstraint.setType("areaContabileCodicePagamentoRequired");
        return propertyResolvableConstraint;
    }

    /**
     * Crea la regola di validazione per il conto avere della riga contabile.
     * 
     * @return constraint
     */
    private PropertyConstraint getContoAvereConstraint() {
        PropertyConstraint propertyConstraint = new PropertyValueConstraint("contoDare",
                not(getDomainAttributeRequiredConstraint()));
        PropertyConstraint domainObjectIfTrue = new RequiredIfTrue("contoAvere", propertyConstraint);
        PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(
                domainObjectIfTrue);
        propertyResolvableConstraint.setType("areaContabileContoDareRequired");
        return propertyResolvableConstraint;
    }

    /**
     * Crea la regola di validazione per il conto dare della riga contabile.
     * 
     * @return constraint
     */
    private PropertyConstraint getContoDareConstraint() {
        PropertyConstraint propertyConstraint = new PropertyValueConstraint("contoAvere",
                not(getDomainAttributeRequiredConstraint()));
        PropertyConstraint domainObjectIfTrue = new RequiredIfTrue("contoDare", propertyConstraint);
        PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(
                domainObjectIfTrue);
        propertyResolvableConstraint.setType("areaContabileContoAvereRequired");
        return propertyResolvableConstraint;
    }

    /**
     * Crea la regola di validazione per datiIntermediario.dataImpegno di ParametriCreazioneSpesometro.<br>
     * Obbligatorio se viene inserito il codice fiscale.
     * 
     * @return constraint
     */
    private PropertyConstraint getDataImpegnoSpesometroConstraint() {
        PropertyConstraint propertyConstraint = new PropertyValueConstraint("datiIntermediario.codiceFiscale",
                present());
        PropertyConstraint domainObjectIfTrue = new RequiredIfTrue("datiIntermediario.dataImpegno", propertyConstraint);
        PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(
                domainObjectIfTrue);
        propertyResolvableConstraint.setType("proprietaSpesometroRequired");
        return propertyResolvableConstraint;
    }

    /**
     * Constraint per l'obbligatorietà del tipo ritenuta acconto.
     * 
     * @param propertyTest
     *            proprietà dei dati ritenuta da testare
     * @param propertyRequired
     *            proprietà richiesta
     * @return PropertyConstraint
     */
    private PropertyConstraint getDatiRitenutaAreaContabileConstraint(String propertyTest, String propertyRequired) {
        PropertyConstraint propertyConstraint = new PropertyValueConstraint(
                "datiRitenutaAccontoAreaContabile." + propertyTest, eq(true));
        PropertyConstraint domainObjectIfTrue = new RequiredIfTrue(
                "datiRitenutaAccontoAreaContabile." + propertyRequired, propertyConstraint);
        PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(
                domainObjectIfTrue);
        propertyResolvableConstraint.setType("datiRitenutaAreaContabileRequired");
        return propertyResolvableConstraint;
    }

    /**
     * Crea la regola di validazione per il centro di costo dell'estratto conto.
     * 
     * @return constraint
     */
    private PropertyConstraint getEstrattoContoCentroCostoConstraint() {
        PropertyConstraint propertyConstraint = new PropertyValueConstraint("centroCosto",
                not(getRequiredConstraint()));
        PropertyConstraint domainObjectIfTrue = new RequiredIfTrue("sottoConto", propertyConstraint);
        PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(
                domainObjectIfTrue);
        propertyResolvableConstraint.setType("estrattoContoCentroCostoRequired");
        return propertyResolvableConstraint;
    }

    /**
     * Crea la regola di validazione per il sotto conto dell'estratto conto.
     * 
     * @return constraint
     */
    private PropertyConstraint getEstrattoContoSottoContoConstraint() {
        PropertyConstraint propertyConstraint = new PropertyValueConstraint("sottoConto",
                not(getDomainAttributeRequiredConstraint()));
        PropertyConstraint domainObjectIfTrue = new RequiredIfTrue("centroCosto", propertyConstraint);
        PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(
                domainObjectIfTrue);
        propertyResolvableConstraint.setType("estrattoContoSottoContoRequired");
        return propertyResolvableConstraint;
    }

    /**
     * Crea la regola di validazione per datiIntermediario.numeroIscrizioneAlbo di ParametriCreazioneSpesometro.<br>
     * Obbligatorio se viene inserito il codice fiscale.
     * 
     * @return constraint
     */
    private PropertyConstraint getNrIscrizioneAlboSpesometroConstraint() {
        PropertyConstraint propertyConstraint = new PropertyValueConstraint("datiIntermediario.codiceFiscale",
                present());
        PropertyConstraint domainObjectIfTrue = new RequiredIfTrue("datiIntermediario.numeroIscrizioneAlbo",
                propertyConstraint);
        PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(
                domainObjectIfTrue);
        propertyResolvableConstraint.setType("proprietaSpesometroRequired");
        return propertyResolvableConstraint;
    }

    /**
     * Crea la regola di validazione del pattern di generazione del codice documento.
     * 
     * @return constraint
     */
    private PropertyConstraint getPatternCodiceTACProtocolloCollegatoConstraint() {
        PropertyConstraint propertyConstraint = new PropertyValueConstraint("registroProtocolloCollegato", present());
        PropertyConstraint constraint = new PatternCodiceDocumentoValueConstraint("patternNumeroProtocollo",
                propertyConstraint);

        PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(constraint);
        propertyResolvableConstraint.setType("patternNumeroDocumentoRequired");
        return propertyResolvableConstraint;
    }

    /**
     * Crea la regola di validazione del pattern di generazione del codice documento.
     * 
     * @return constraint
     */
    private PropertyConstraint getPatternCodiceTACProtocolloConstraint() {
        PropertyConstraint propertyConstraint = new PropertyValueConstraint("registroProtocollo", present());
        PropertyConstraint constraint = new PatternCodiceDocumentoValueConstraint("patternNumeroProtocollo",
                propertyConstraint);

        PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(constraint);
        propertyResolvableConstraint.setType("patternNumeroDocumentoRequired");
        return propertyResolvableConstraint;
    }

    /**
     * Crea la regola di validazione per la percentuale trimestrale delle contaiblità settings.
     * 
     * @return constraint
     */
    private PropertyConstraint getPercTrimestraleContabilitaConstraint() {
        PropertyConstraint propertyConstraint = new PropertyValueConstraint("tipoPeriodicita",
                eq(ETipoPeriodicita.TRIMESTRALE));
        PropertyConstraint requiredIfTrue = new RequiredIfTrue("percTrimestrale", propertyConstraint);
        PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(requiredIfTrue);
        propertyResolvableConstraint.setType("percTRimestraleContabilitaRequired");
        return propertyResolvableConstraint;
    }

    /**
     * Crea la regola di validazione per il registro iva collegato del tipo area contabile.
     * 
     * @return constraint
     */
    private PropertyConstraint getRegistroIvaCollegatoConstraint() {
        PropertyConstraint propertyConstraint = new PropertyValueConstraint("gestioneIva",
                not(eq(GestioneIva.NORMALE)));
        PropertyConstraint requiredIfTrue = new RequiredIfTrue("registroIvaCollegato", propertyConstraint);
        PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(requiredIfTrue);
        propertyResolvableConstraint.setType("areaContabileNumeroDocumentoRequired");
        return propertyResolvableConstraint;
    }

    /**
     * Crea la regola di validazione per il registro iva del tipo area contabile.
     * 
     * @return constraint
     */
    private PropertyConstraint getRegistroIvaConstraint() {
        PropertyConstraint propertyConstraint = new PropertyValueConstraint("tipoDocumento.righeIvaEnable", eq(true));
        PropertyConstraint requiredIfTrue = new RequiredIfTrue("registroIva", propertyConstraint);
        PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(requiredIfTrue);
        propertyResolvableConstraint.setType("areaContabileNumeroDocumentoRequired");
        return propertyResolvableConstraint;
    }

    @Override
    public List<Rules> getRules() {
        List<Rules> listRules = new ArrayList<Rules>();

        // piano dei conti
        listRules.add(createMastroRules());
        listRules.add(createContoRules());
        listRules.add(createSottoContoRules());

        listRules.add(createTipoAreaContabile());

        listRules.add(createParametriRicercaSituazioneEP());
        listRules.add(createParametriRicercaMovimentiContabili());
        listRules.add(createParametriRicercaBilancio());
        listRules.add(createParametriRicercaBilancioConfronto());
        listRules.add(createParametriRicercaEstrattoConto());

        listRules.add(createDocumentoRules());
        listRules.add(createRigaContabileRules());

        listRules.add(createRegistroIvaRules());

        // Conto Base
        listRules.add(createContoBaseRules());
        listRules.add(createTipoDocumentoBase());

        listRules.add(createParametriAperturaContabile());
        listRules.add(createParametriChiusuraContabile());

        listRules.add(createControPartitaRules());

        listRules.add(createStrutturaContabilePMRules());
        listRules.add(createStrutturaContabileRules());

        listRules.add(createContabilitaSettingsRules());

        listRules.add(createCausaleRitenutaAccontoRules());
        listRules.add(createContributoPrevidenzialeRules());

        listRules.add(createAreaContabileRules());

        listRules.add(createParametriRicercaRiepilogoBlacklistRules());

        listRules.add(createParametriSituazioneRitenuteAccontoRules());

        return listRules;
    }

    /**
     * Crea la regola di validazione per datiIntermediario.tipologiaComunicazione di ParametriCreazioneSpesometro.<br>
     * Obbligatorio se viene inserito il codice fiscale.
     * 
     * @return constraint
     */
    private PropertyConstraint getTipologiaComunicazioneSpesometroConstraint() {
        PropertyConstraint propertyConstraint = new PropertyValueConstraint("datiIntermediario.codiceFiscale",
                present());
        PropertyConstraint domainObjectIfTrue = new RequiredIfTrue("datiIntermediario.tipologiaComunicazione",
                propertyConstraint);
        PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(
                domainObjectIfTrue);
        propertyResolvableConstraint.setType("proprietaSpesometroRequired");
        return propertyResolvableConstraint;
    }

    /**
     * Crea la regola di validazione per la tipologia corrispettivo del tipo area contabile.
     * 
     * @return constraint
     */
    private PropertyConstraint getTipologiaCorrispettivoConstraint() {
        PropertyConstraint propertyConstraint = new PropertyValueConstraint("registroIva.tipoRegistro",
                eq(TipoRegistro.CORRISPETTIVO));
        PropertyConstraint requiredIfTrue = new RequiredIfTrue("tipologiaCorrispettivo", propertyConstraint);
        PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(requiredIfTrue);
        propertyResolvableConstraint.setType("areaContabileNumeroDocumentoRequired");
        return propertyResolvableConstraint;
    }

    /**
     * Constraint per l'obbligatorietà del tipo ritenuta acconto.
     * 
     * @return PropertyConstraint
     */
    private PropertyConstraint getTipoRitenutaAccontoConstraint() {
        PropertyConstraint propertyConstraint = new PropertyValueConstraint("ritenutaAcconto", eq(true));
        PropertyConstraint domainObjectIfTrue = new RequiredIfTrue("tipoRitenutaAcconto", propertyConstraint);
        PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(
                domainObjectIfTrue);
        propertyResolvableConstraint.setType("tipoAreaContabileRitenutaAccontoRequired");
        return propertyResolvableConstraint;
    }
}
