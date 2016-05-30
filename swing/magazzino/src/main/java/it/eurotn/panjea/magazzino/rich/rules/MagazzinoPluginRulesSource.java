/**
 *
 */
package it.eurotn.panjea.magazzino.rich.rules;

import java.util.ArrayList;
import java.util.List;

import org.springframework.rules.Rules;
import org.springframework.rules.constraint.Constraint;
import org.springframework.rules.constraint.EqualTo;
import org.springframework.rules.constraint.GreaterThanEqualTo;
import org.springframework.rules.constraint.LessThanEqualTo;
import org.springframework.rules.constraint.Not;
import org.springframework.rules.constraint.Or;
import org.springframework.rules.constraint.property.ConditionalPropertyConstraint;
import org.springframework.rules.constraint.property.PropertyConstraint;
import org.springframework.rules.constraint.property.PropertyValueConstraint;
import org.springframework.rules.constraint.property.RequiredIfTrue;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.domain.FaseLavorazione;
import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.panjea.magazzino.domain.ArticoloAlternativo;
import it.eurotn.panjea.magazzino.domain.AspettoEsteriore;
import it.eurotn.panjea.magazzino.domain.AttributoArticolo;
import it.eurotn.panjea.magazzino.domain.AttributoCategoria;
import it.eurotn.panjea.magazzino.domain.Categoria;
import it.eurotn.panjea.magazzino.domain.CategoriaScontoArticolo;
import it.eurotn.panjea.magazzino.domain.CategoriaScontoSede;
import it.eurotn.panjea.magazzino.domain.CategoriaSedeMagazzino;
import it.eurotn.panjea.magazzino.domain.CausaleTrasporto;
import it.eurotn.panjea.magazzino.domain.CodiceArticoloEntita;
import it.eurotn.panjea.magazzino.domain.Componente;
import it.eurotn.panjea.magazzino.domain.Contratto;
import it.eurotn.panjea.magazzino.domain.FormulaTrasformazione;
import it.eurotn.panjea.magazzino.domain.Listino;
import it.eurotn.panjea.magazzino.domain.MagazzinoSettings;
import it.eurotn.panjea.magazzino.domain.MezzoTrasporto;
import it.eurotn.panjea.magazzino.domain.ProvenienzaPrezzoArticolo;
import it.eurotn.panjea.magazzino.domain.RaggruppamentoArticoli;
import it.eurotn.panjea.magazzino.domain.RigaArticolo;
import it.eurotn.panjea.magazzino.domain.RigaContratto;
import it.eurotn.panjea.magazzino.domain.RigaContrattoAgente;
import it.eurotn.panjea.magazzino.domain.RigaListino;
import it.eurotn.panjea.magazzino.domain.RigaNota;
import it.eurotn.panjea.magazzino.domain.RigaRaggruppamentoArticoli;
import it.eurotn.panjea.magazzino.domain.ScaglioneListino;
import it.eurotn.panjea.magazzino.domain.Sconto;
import it.eurotn.panjea.magazzino.domain.ScontoMaggiorazione;
import it.eurotn.panjea.magazzino.domain.SedeMagazzino;
import it.eurotn.panjea.magazzino.domain.SedeMagazzino.ETipologiaCodiceIvaAlternativo;
import it.eurotn.panjea.magazzino.domain.SogliaAddebitoDichiarazioneSettings;
import it.eurotn.panjea.magazzino.domain.SottoContoContabilizzazione;
import it.eurotn.panjea.magazzino.domain.TipoAttributo;
import it.eurotn.panjea.magazzino.domain.TipoAttributo.ETipoDatoTipoAttributo;
import it.eurotn.panjea.magazzino.domain.TipoMezzoTrasporto;
import it.eurotn.panjea.magazzino.domain.TipoPorto;
import it.eurotn.panjea.magazzino.domain.TotalizzatoreTipoAttributo;
import it.eurotn.panjea.magazzino.domain.TrasportoCura;
import it.eurotn.panjea.magazzino.domain.VersioneListino;
import it.eurotn.panjea.magazzino.domain.documento.DatoAccompagnatorioMagazzinoMetaData;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino.TipoMovimento;
import it.eurotn.panjea.magazzino.domain.etichetta.EtichettaArticolo;
import it.eurotn.panjea.magazzino.domain.etichetta.ParametriStampaEtichetteArticolo;
import it.eurotn.panjea.magazzino.domain.omaggio.Omaggio;
import it.eurotn.panjea.magazzino.domain.rendicontazione.TipoEsportazione;
import it.eurotn.panjea.magazzino.manager.listinoprezzi.ParametriListinoPrezzi;
import it.eurotn.panjea.magazzino.rich.editors.contabilizzazione.ParametriContabilizzazioneWrapper;
import it.eurotn.panjea.magazzino.rich.editors.listino.SedeListiniPM;
import it.eurotn.panjea.magazzino.rich.editors.verificaprezzo.ParametriCalcoloPrezziPM;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoFullDTO;
import it.eurotn.panjea.magazzino.util.ParametriCreazioneDocumentoFatturazione;
import it.eurotn.panjea.magazzino.util.ParametriRicercaAreaMagazzino;
import it.eurotn.panjea.magazzino.util.ParametriRicercaMovimentazione;
import it.eurotn.panjea.magazzino.util.ParametriRicercaMovimentazioneArticolo;
import it.eurotn.panjea.magazzino.util.ParametriRicercaValorizzazione;
import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriAggiornaManutenzioneListino;
import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriCreazioneSchedeArticoli;
import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriRicercaConfrontoListino;
import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriRicercaElaborazioni;
import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriRicercaFatturazione;
import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriRicercaManutenzioneListino;
import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriRicercaManutenzioneListino.ProvenienzaPrezzoManutenzioneListino;
import it.eurotn.panjea.magazzino.util.parametriricerca.TipoConfronto.EConfronto;
import it.eurotn.panjea.pagamenti.domain.CodicePagamento;
import it.eurotn.panjea.rate.domain.AreaRate;
import it.eurotn.panjea.rich.rules.CollectionValuesRequired;
import it.eurotn.panjea.rich.rules.ConfrontoDateConstraint;
import it.eurotn.panjea.rich.rules.ConfrontoPropertiesConstraint;
import it.eurotn.panjea.rich.rules.PropertyResolvableConstraint;
import it.eurotn.panjea.rules.AbstractPluginRulesSource;
import it.eurotn.panjea.spedizioni.util.ParametriRicercaSpedizioni;
import it.eurotn.panjea.stampe.domain.LayoutStampa;

/**
 *
 * @author fattazzo
 * @version 1.0, 08/mag/07
 *
 */
public class MagazzinoPluginRulesSource extends AbstractPluginRulesSource {
    /**
     * Crea le regole per il plugin del magazzino.
     *
     * @return regole create
     */
    private Rules createAreaMagazzinoRules() {
        return new Rules(AreaMagazzinoFullDTO.class) {
            @Override
            protected void initRules() {
                add("areaMagazzino.dataRegistrazione", getRequiredConstraint());
                add("areaMagazzino.documento.dataDocumento", getRequiredConstraint());
                add("areaMagazzino.annoMovimento", getRequiredConstraint());
                add(new ConfrontoDateConstraint("areaMagazzino.dataRegistrazione", GreaterThanEqualTo.instance(),
                        "areaMagazzino.documento.dataDocumento"));
                add("areaMagazzino.tipoAreaMagazzino", getDomainAttributeRequiredConstraint());
                add("areaMagazzino.depositoOrigine", getDomainAttributeRequiredConstraint());
                add(getAreaMagazzinoSedeEntitaConstraint());
                add(getAreaMagazzinoEntitaConstraint());
                add(getAreaMagazzinoMezzoTrasportoConstraint());
                add("areaMagazzino.trasportoCura", getMaxCharConstraint(45));
                add("areaMagazzino.tipoPorto", getMaxCharConstraint(30));
                add("areaMagazzino.causaleTrasporto", getMaxCharConstraint(40));
                add("areaMagazzino.aspettoEsteriore", getMaxCharConstraint(40));
                add(new ListinoValutaPropertiesConstraint("areaMagazzino.listino",
                        "areaMagazzino.documento.totale.codiceValuta"));
                add(new ListinoValutaPropertiesConstraint("areaMagazzino.listinoAlternativo",
                        "areaMagazzino.documento.totale.codiceValuta"));

                // Rules per AreaPartita
                add(getCodicePagamentoConstraint());

                PropertyConstraint propertyConstraint = new PropertyValueConstraint("areaMagazzino.inserimentoBloccato",
                        eq(false));
                PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(
                        propertyConstraint);
                propertyResolvableConstraint.setType("inserimentoBloccatoPropertyConstraint");
                add(propertyResolvableConstraint);

                // rule per il deposito destinazione obbligatorio
                add(getDepositoDestinazioneRequiredConstraint());

                // rule per richiedere il deposito destinazione diverso da
                // quello di origine in caso di tipo movimento
                // trasferimento
                add(getDepositoDestinazioneTrasferimentoConstraint());
                add(getVettorePresenteConstraint());
            }

        };

    }

    /**
     * Crea le regole per {@link ArticoloAlternativo}.
     *
     * @return regole create
     */
    private Rules createArticoloAlternativoRules() {
        return new Rules(ArticoloAlternativo.class) {
            @Override
            protected void initRules() {
                add("articolo", getRequiredConstraint());
                add("corrispondenza", getRequiredConstraint());
            }
        };
    }

    /**
     * Crea le regole per l'articolo.
     *
     * @return regole create
     */
    private Rules createArticoloRules() {
        return new Rules(Articolo.class) {
            @Override
            protected void initRules() {
                add(getArticoloCodiceConstraint());
                add(getArticoloDescrizioneConstraint());
                add("codiceInterno", getMaxCharConstraint(30));
                add("numeroDecimaliQta", getRequiredConstraint());
                add("numeroDecimaliPrezzo", getRequiredConstraint());
                add("unitaMisura", getDomainAttributeRequiredConstraint());
                add("codiceIva", getDomainAttributeRequiredConstraint());
                add("unitaMisuraQtaMagazzino", getDomainAttributeRequiredConstraint());
                add("note", getMaxCharConstraint(1000));
                add("categoriaCommercialeArticolo", getDomainAttributeRequiredConstraint());
                add(getMeseSchedaArticoloArticoloConstraint());
                add(getAnnoSchedaArticoloArticoloConstraint());
            }
        };
    }

    /**
     * Crea le regole per {@link AspettoEsteriore}.
     *
     * @return regole create
     */
    private Rules createAspettoEsterioreRules() {
        return new Rules(AspettoEsteriore.class) {
            @Override
            protected void initRules() {
                add("descrizione", getRequiredConstraint());
                add("descrizione", getMaxCharConstraint(40));
            }
        };
    }

    /**
     * Crea le regole per l'attributo articolo.
     *
     * @return regole create
     */
    private Rules createAttributoArticoloRules() {
        return new Rules(AttributoArticolo.class) {
            @Override
            protected void initRules() {
                add("riga", getRequiredConstraint());
                add("riga", gt(0));
                add("ordine", getRequiredConstraint());
                add("ordine", gt(0));
                add("ordine", lt(4));
            }
        };
    }

    /**
     * Crea le regole per l'attributo categoria.
     *
     * @return regole create
     */
    private Rules createAttributoCategoriaRules() {
        return new Rules(AttributoCategoria.class) {
            @Override
            protected void initRules() {
                add("riga", getRequiredConstraint());
                add("riga", gt(0));
                add("ordine", getRequiredConstraint());
                add("ordine", gt(0));
                add("ordine", lt(4));
            }
        };
    }

    /**
     * Crea le regole per {@link Categoria}.
     *
     * @return regole create
     */
    private Rules createCategoriaRules() {
        return new Rules(Categoria.class) {
            @Override
            protected void initRules() {
                add("codice", getRequiredConstraint());
                add("descrizioneLinguaAziendale", getRequiredConstraint());
            }
        };
    }

    /**
     * Crea le regole per {@link CategoriaScontoArticolo}.
     *
     * @return regole create
     */
    private Rules createCategoriaScontoArticoloRules() {
        return new Rules(CategoriaScontoArticolo.class) {
            @Override
            protected void initRules() {
                add("codice", getRequiredConstraint());
            }
        };

    }

    /**
     * Crea le regole per {@link CategoriaScontoSede}.
     *
     * @return regole create
     */
    private Rules createCategoriaScontoSedeRules() {
        return new Rules(CategoriaScontoSede.class) {
            @Override
            protected void initRules() {
                add("codice", getRequiredConstraint());
            }

        };
    }

    /**
     * Crea le regole per {@link CategoriaSedeMagazzino}.
     *
     * @return regole create
     */
    private Rules createCategoriaSedeRules() {
        return new Rules(CategoriaSedeMagazzino.class) {
            @Override
            protected void initRules() {
                add("descrizione", getRequiredConstraint());
            }
        };
    }

    // /**
    // * Crea le regole per {@link AttributoRiga}.
    // *
    // * @return regole create
    // */
    // private Rules createAttributoRigaRules() {
    // return new Rules(AttributoRiga.class) {
    //
    // @Override
    // protected void initRules() {
    // add("valore", getRequiredConstraint());
    // add("valore", not(eq(0.0)));
    // }
    // };
    // }

    /**
     * Crea le regole per {@link CausaleTrasporto}.
     *
     * @return regole create
     */
    private Rules createCausaleTrasportoRules() {
        return new Rules(CausaleTrasporto.class) {
            @Override
            protected void initRules() {
                add("descrizione", getRequiredConstraint());
                add("descrizione", getMaxCharConstraint(40));
            }
        };
    }

    /**
     * Crea le regole per {@link CodiceArticoloEntita}.
     *
     * @return regole create
     */
    private Rules createCodiceArticoloEntitaRules() {
        return new Rules(CodiceArticoloEntita.class) {
            @Override
            protected void initRules() {
                add("codice", getRequiredConstraint());
                add("entita", getDomainAttributeRequiredConstraint());
                add("leadTime", getRequiredConstraint());
                add("lottoRiordino", getRequiredConstraint());
                add("qtaMinimaOrdinabile", getRequiredConstraint());
                add("barCode", getMaxCharConstraint(13));
                add("barCode2", getMaxCharConstraint(30));
            }
        };
    }

    /**
     * Crea le regole per {@link Componente}.
     *
     * @return regole create
     */
    private Rules createComponenteRules() {
        return new Rules(Componente.class) {
            @Override
            protected void initRules() {
                add("articolo", getRequiredConstraint());
            }
        };
    }

    /**
     * aggiunta delle rules di {@link Contratto}.
     *
     * @return {@link Rules} di {@link Contratto}
     */
    private Rules createContrattoRules() {
        return new Rules(Contratto.class) {
            @Override
            protected void initRules() {
                add("codice", getRequiredConstraint());
                add("codice", getMaxCharConstraint(15));
                add("descrizione", getRequiredConstraint());
                add("descrizione", getMaxCharConstraint(100));
                add("dataInizio", getRequiredConstraint());
                add("dataFine", getRequiredConstraint());
                add(new ConfrontoDateConstraint("dataInizio", LessThanEqualTo.instance(), "dataFine"));
                add("codiceValuta", getRequiredConstraint());
                add("numeroDecimali", getRequiredConstraint());
            }
        };
    }

    /**
     * @return Rules per DatoAccompagnatorioMagazzinoMetaData
     */
    private Rules createDatoAccompagnatorioMagazzinoMetaDataRules() {
        return new Rules(DatoAccompagnatorioMagazzinoMetaData.class) {
            @Override
            protected void initRules() {
                add("ordinamento", getRequiredConstraint());
            }
        };
    }

    /**
     * Crea le regole per {@link ParametriStampaEtichetteArticolo}.
     *
     * @return regole create
     */
    private Rules createEtichettaArticoloRules() {
        return new Rules(EtichettaArticolo.class) {
            @Override
            protected void initRules() {
                add("articolo", getRequiredConstraint());
            }
        };
    }

    /**
     * @return Rules per FaseLavorazione
     */
    private Rules createFaseLavorazioneRules() {
        return new Rules(FaseLavorazione.class) {
            @Override
            protected void initRules() {
                add("codice", getRequiredConstraint());
                add("codice", getMaxCharConstraint(100));
                add("descrizione", getRequiredConstraint());
                add("descrizione", getMaxCharConstraint(200));
            }
        };
    }

    /**
     * Crea le regole per {@link FormulaTrasformazione}.
     *
     * @return regole create
     */
    private Rules createFormulaTrasformazioneQta() {
        return new Rules(FormulaTrasformazione.class) {
            @Override
            protected void initRules() {
                add("formula", getRequiredConstraint());
                add("codice", getRequiredConstraint());
            }
        };
    }

    /**
     * Crea le regole per {@link LayoutStampaTipoAreaMagazzino}.
     *
     * @return regole create
     */
    private Rules createLayoutStampaTipoAreaMagazzinoRules() {
        return new Rules(LayoutStampa.class) {
            @Override
            protected void initRules() {
                add("reportName", getRequiredConstraint());
            }
        };
    }

    /**
     * Crea le regole per {@link Listino}.
     *
     * @return regole create
     */
    private Rules createListinoRules() {
        return new Rules(Listino.class) {
            @Override
            protected void initRules() {
                add("codice", getRequiredConstraint());
                add("descrizione", getRequiredConstraint());
                add("codiceValuta", getRequiredConstraint());
                add("tipoListino", getRequiredConstraint());
            }
        };
    }

    /**
     * Crea le regole per {@link MagazzinoSettings}.
     *
     * @return regole create
     */
    private Rules createMagazzinoSettingsRules() {
        return new Rules(MagazzinoSettings.class) {
            @Override
            protected void initRules() {
                add("codiceGS1", getMaxCharConstraint(9));
                add("sogliaAddebitoDichiarazione", new CollectionValuesRequired());
            }
        };
    }

    /**
     * Crea le regole per {@link MezzoTrasporto}.
     *
     * @return regole create
     */
    private Rules createMezzoTrasportoRules() {
        return new Rules(MezzoTrasporto.class) {
            @Override
            protected void initRules() {
                add("targa", getRequiredConstraint());
                add("targa", getMaxCharConstraint(10));
                add("descrizione", getRequiredConstraint());
                add("targa", getMaxCharConstraint(30));
                add("tipoMezzoTrasporto", getDomainAttributeRequiredConstraint());
            }
        };
    }

    /**
     * Crea le regole per l'omaggio.
     *
     * @return regole create
     */
    private Rules createOmaggioRules() {
        return new Rules(Omaggio.class) {
            @Override
            protected void initRules() {
                add("tipoOmaggio", getRequiredConstraint());
                add("descrizionePerStampa", getMaxCharConstraint(1000));
            }
        };
    }

    /**
     * Crea le regole per {@link ParametriAggiornaManutenzioneListino}.
     *
     * @return regole create
     */
    private Rules createParametriAggiornaManutenzioneListinoRules() {
        return new Rules(ParametriAggiornaManutenzioneListino.class) {
            @Override
            protected void initRules() {
                add("versioneListino", getDomainAttributeRequiredConstraint());
            }
        };
    }

    /**
     * Crea le regole per {@link ParametriCalcoloPrezziPM}.
     *
     * @return regole create
     */
    private Rules createParametriCalcoloPrezziPMRules() {
        return new Rules(ParametriCalcoloPrezziPM.class) {
            @Override
            protected void initRules() {
                add("data", getRequiredConstraint());
                add(getParametriCalcoloPrezziTipoMezzoConstraint());
                add(getParametriCalcoloPrezziSedeEntitaConstraint());
                add(getEntitaCalcoloPrezziConstraint());
                add(getArticoloCalcoloPrezziConstraint());
                add("codiceValuta", getRequiredConstraint());
                add(new ListinoValutaPropertiesConstraint("listino", "codiceValuta"));
                add(new ListinoValutaPropertiesConstraint("listinoAlternativo", "codiceValuta"));
            }
        };
    }

    /**
     * Crea le regole per {@link ParametriContabilizzazioneWrapper}.
     *
     * @return regole create
     */
    private Rules createParametriContabilizzazioneWrapperRules() {
        return new Rules(ParametriContabilizzazioneWrapper.class) {
            @Override
            protected void initRules() {
                add("anno", getRequiredConstraint());
                add("tipoGenerazione", getRequiredConstraint());
            }
        };
    }

    /**
     * Crea le regole per {@link ParametriCreazioneDocumentoFatturazione}.
     *
     * @return regole create
     */
    private Rules createParametriCreazioneDocumentoFatturazione() {
        return new Rules(ParametriCreazioneDocumentoFatturazione.class) {
            @Override
            protected void initRules() {
                add("dataDocumento", getRequiredConstraint());
                add("note", getMaxCharConstraint(60));
            }
        };
    }

    /**
     * Crea le regole per {@link ParametriCreazioneSchedeArticoli}.
     *
     * @return regole create
     */
    private Rules createParametriCreazioneSchedeArticoliRules() {
        return new Rules(ParametriCreazioneSchedeArticoli.class) {
            @Override
            protected void initRules() {
                add("mese", getRequiredConstraint());
                add("anno", getRequiredConstraint());
                add("articoli", getRequiredConstraint());
                add("articoli", getCollectionRequiredRequired());
                add("note", getRequiredConstraint());
            }
        };
    }

    private Rules createParametriListinoPrezziRules() {
        return new Rules(ParametriListinoPrezzi.class) {
            @Override
            protected void initRules() {
                add("numRecordInPagina", getRequiredConstraint());
                add("articoloPartenza", getRequiredConstraint());
                add("entita", getRequiredConstraint());
                add("data", getRequiredConstraint());
            }
        };
    }

    /**
     * Crea le regole per {@link ParametriRicercaAreaMagazzino}.
     *
     * @return regole create
     */
    private Rules createParametriRicercaAreaMagazzinoRules() {
        return new Rules(ParametriRicercaAreaMagazzino.class) {
            @Override
            protected void initRules() {
                add("dataDocumento", getPeriodoConstraint(false));
                add("dataRegistrazione", getPeriodoConstraint(false));
            }
        };
    }

    /**
     * Crea le regole per {@link ParametriRicercaConfrontoListino}.
     *
     * @return regole create
     */
    private Rules createParametriRicercaConfrontoListinoRules() {
        return new Rules(ParametriRicercaConfrontoListino.class) {
            @Override
            protected void initRules() {
                add("confrontoBase.confronto", getRequiredConstraint());
                add(getListinoTipoConfrontoRequiredConstraint("confrontoBase"));
                add("confronti", getCollectionRequiredRequired());
            }
        };
    }

    /**
     * Crea le regole per {@link ParametriRicercaElaborazioni}.
     *
     * @return regole create
     */
    private Rules createParametriRicercaElaborazioniRules() {
        return new Rules(ParametriRicercaElaborazioni.class) {
            @Override
            protected void initRules() {
                add("mese", getRequiredConstraint());
                add("anno", getRequiredConstraint());
            }
        };
    }

    /**
     * Crea le regole per {@link ParametriRicercaFatturazione}.
     *
     * @return regole create
     */
    private Rules createParametriRicercaFatturazioneRules() {
        return new Rules(ParametriRicercaFatturazione.class) {
            @Override
            protected void initRules() {
                add("periodo", getPeriodoConstraint(true));
                add("tipoDocumentoDestinazione", getDomainAttributeRequiredConstraint());
                add("codiceValuta", getRequiredConstraint());
            }
        };
    }

    /**
     * Crea le regole per {@link ParametriRicercaManutenzioneListino}.
     *
     * @return regole create
     */
    private Rules createParametriRicercaManutenzioneListinoRules() {
        return new Rules(ParametriRicercaManutenzioneListino.class) {
            @Override
            protected void initRules() {
                add(getVersioneListinoPerManutenzioneConstraint());
                add(getDepositoPerManutenzioneConstraint());
                add("provenienzaManutenzioneListino", getRequiredConstraint());
                add("provenienzaPrezzoManutenzioneListino", getRequiredConstraint());
                add("variazione", getRequiredConstraint());
                add("numeroDecimali", LessThanEqualTo.value(6));
                add(getFilePerManutenzioneConstraint());
            }
        };
    }

    /**
     * Crea le regole per {@link ParametriRicercaMovimentazioneArticolo}.
     *
     * @return regole create
     */
    private Rules createParametriRicercaMovimentazioneArticoloRules() {
        return new Rules(ParametriRicercaMovimentazioneArticolo.class) {
            @Override
            protected void initRules() {
                add("articoloLite", getDomainAttributeRequiredConstraint());
                add("depositoLite", getDomainAttributeRequiredConstraint());
                add("periodo", getPeriodoConstraint(true));
            }
        };
    }

    /**
     * Crea le regole per {@link ParametriRicercaMovimentazione}.
     *
     * @return regole create
     */
    private Rules createParametriRicercaMovimentazioneRules() {
        return new Rules(ParametriRicercaMovimentazione.class) {
            @Override
            protected void initRules() {
                add("periodo", getPeriodoConstraint(false));
            }
        };
    }

    /**
     * Crea le regole per l'articolo.
     *
     * @return regole create
     */
    private Rules createParametriRicercaSpedizioniRules() {
        return new Rules(ParametriRicercaSpedizioni.class) {
            @Override
            protected void initRules() {
                add("vettore", getDomainAttributeRequiredConstraint());
            }
        };
    }

    /**
     * Crea le regole per {@link ParametriRicercaValorizzazione}.
     *
     * @return regole create
     */
    private Rules createParametriRicercaValorizzazioneRules() {
        return new Rules(ParametriRicercaValorizzazione.class) {
            @Override
            protected void initRules() {
                add("depositi", or(getCollectionRequiredRequired(), eq("tuttiDepositi", Boolean.TRUE)));
                add("categorie", getCollectionRequiredRequired());
                add("data", getRequiredConstraint());
            }
        };
    }

    /**
     * Crea le regole per {@link ParametriStampaEtichetteArticolo}.
     *
     * @return regole create
     */
    private Rules createParametriStampaEtichetteArticoloRules() {
        return new Rules(ParametriStampaEtichetteArticolo.class) {
            @Override
            protected void initRules() {
                add("reportName", getRequiredConstraint());
            }
        };
    }

    /**
     * @return RaggruppamentoArticoli rules
     */
    private Rules createRaggruppamentoArticoliRules() {
        return new Rules(RaggruppamentoArticoli.class) {
            @Override
            protected void initRules() {
                add("descrizione", getRequiredConstraint());
                add("descrizione", getMaxCharConstraint(30));
            }
        };
    }

    /**
     * Crea le regole per {@link RigaArticolo}.
     *
     * @return regole create
     */
    private Rules createRigaArticoloRules() {
        return new Rules(RigaArticolo.class) {

            @Override
            protected void initRules() {
                add("articolo", getDomainAttributeRequiredConstraint());
                add("descrizione", getRequiredConstraint());
                add("descrizione", maxLength(100));
                add("unitaMisura", getRequiredConstraint());
                add("unitaMisura", maxLength(3));
                add("unitaMisuraQtaMagazzino", maxLength(3));
                add(new QtaRigaArticoloConstraint("qta", "qta"));
                add("qta", getQtaChiusaConstraint());
                add("codiceIva", getDomainAttributeRequiredConstraint());
                add("attributi", new NumeroAttributiRigaConstraint());
                add("attributi", new AttributiRigaConstraint());
                add("righeLotto", new RigheLottiConstraint());
                add(new QtaComponentiConstraint("componenti",
                        "areaMagazzino.tipoAreaMagazzino.tipoDocumento.classeTipoDocumento"));
            }
        };
    }

    /**
     * Crea le regole per la {@link RigaContrattoAgente}.
     *
     * @return regole create
     */
    private Rules createRigaContrattoAgenteRules() {
        return new Rules(RigaContrattoAgente.class) {
            @Override
            protected void initRules() {
                add("valoreProvvigione", getRequiredConstraint());
            }
        };
    }

    /**
     * aggiunta delle rules di {@link RigaContratto}.
     *
     * @return {@link Rules} di {@link RigaContratto}
     */
    private Rules createRigaContrattoRules() {
        return new Rules(RigaContratto.class) {
            @Override
            protected void initRules() {
                add(getRigaContrattoScontoRequired());
                add(getRigaContrattoValorePrezzoRequired());
                add("righeContrattoAgente", new RigheContrattoAgentiConstraint());
                add("strategiaPrezzo.quantitaSogliaPrezzo", getRequiredConstraint());
                add("strategiaSconto.quantitaSogliaSconto", getRequiredConstraint());
            }
        };
    }

    /**
     * Crea le regole per {@link RigaListino}.
     *
     * @return regole create
     */
    private Rules createRigaListinoRules() {
        return new Rules(RigaListino.class) {
            @Override
            protected void initRules() {
                add("articolo", getDomainAttributeRequiredConstraint());
                add("numeroDecimaliPrezzo", getRequiredConstraint());
                add("numeroDecimaliPrezzo", range(0, 6));
                add("prezzo", getRequiredConstraint());
                add("scaglioni", new ScaglioniListinoRigaListinoConstraint());
            }
        };
    }

    /**
     * Crea le regole per {@link RigaNota}.
     *
     * @return regole create
     */
    private Rules createRigaNotaRules() {
        return new Rules(RigaNota.class) {
            @Override
            protected void initRules() {
                // add("nota", getRequiredConstraint());
            }
        };
    }

    /**
     * @return RigaRaggruppamentoArticoli rules
     */
    private Rules createRigaRaggruppamentoArticoliRules() {
        return new Rules(RigaRaggruppamentoArticoli.class) {
            @Override
            protected void initRules() {
                add("articolo", getDomainAttributeRequiredConstraint());
                add("qta", not(eq(0.00)));
            }
        };
    }

    /**
     * Crea le regole per lo scaglione listino.
     *
     * @return regole create
     */
    private Rules createScaglioneListinoRules() {
        return new Rules(ScaglioneListino.class) {
            @Override
            protected void initRules() {
                add("quantita", getRequiredConstraint());
                add("prezzo", getRequiredConstraint());
            }
        };
    }

    /**
     * Crea le regole per {@link ScontoMaggiorazione}.
     *
     * @return regole create
     */
    private Rules createScontoMaggiorazione() {
        return new Rules(ScontoMaggiorazione.class) {
            @Override
            protected void initRules() {
                add("sconto", getDomainAttributeRequiredConstraint());
            }
        };
    }

    /**
     * Crea le regole per lo sconto.
     *
     * @return regole create
     */
    private Rules createScontoRules() {
        return new Rules(Sconto.class) {
            @Override
            protected void initRules() {
                add("codice", getRequiredConstraint());
                add("descrizione", getRequiredConstraint());
            }
        };
    }

    /**
     * Crea le regole per {@link SedeListiniPM}.
     *
     * @return regole create
     */
    private Rules createSedeListiniPMRules() {
        return new Rules(SedeListiniPM.class) {
            @Override
            protected void initRules() {
                add("entita", getDomainAttributeRequiredConstraint());
            }
        };
    }

    /**
     * Crea le regole per {@link SedeMagazzino}.
     *
     * @return regole create
     */
    private Rules createSedeMagazzinoRules() {
        return new Rules(SedeMagazzino.class) {
            @Override
            protected void initRules() {
                add(getNoteBloccoConstraint());
                add("noteBlocco", maxLength(300));
                add(getCodiceIvaAlternativoSedeMagazzinoRequiredConstraint());
                add(getDataScadenzaDichiarazioneIntentoSedeMagazzinoRequiredConstraint());
                add(getTestoDichiarazioneIntentoSedeMagazzinoRequiredConstraint());
            }

        };

    }

    /**
     * Crea le regole per {@link SogliaAddebitoDichiarazioneSettings}.
     *
     * @return regole create
     */
    private Rules createSogliaAddebitoDichiarazioneSettingsRules() {
        return new Rules(SogliaAddebitoDichiarazioneSettings.class) {
            @Override
            protected void initRules() {
                add("dataVigore", getRequiredConstraint());
                add("prezzo", getRequiredConstraint());
            }
        };
    }

    /**
     *
     * @return regole per il sottocontoContabilizzazione
     */
    private Rules createSottoContoContabilizzaioneRules() {
        return new Rules(SottoContoContabilizzazione.class) {
            @Override
            protected void initRules() {
                add("sottoConto", getDomainAttributeRequiredConstraint());
            }
        };
    }

    /**
     * Crea le regole per {@link TipoAreaMagazzino}.
     *
     * @return regole create
     */
    private Rules createTipoAreaMagazzinoRules() {
        return new Rules(TipoAreaMagazzino.class) {
            @Override
            protected void initRules() {
                add(getDepositoOrigineTipoAreaMagazzinoConstraint());
                add(getDepositoDestinazioneTipoAreaMagazzinoConstraint());
                add("sezioneTipoMovimento", getRequiredConstraint());
                add("numeroCopiePerStampa", getRequiredConstraint());
            }
        };
    }

    /**
     * Crea le regole per {@link TipoAttributo}.
     *
     * @return regole create
     */
    private Rules createTipoAttributoRules() {
        return new Rules(TipoAttributo.class) {
            @Override
            protected void initRules() {
                add("codice", getRequiredConstraint());
                add("codice", getMaxCharConstraint(10));
                add("tipoDato", getRequiredConstraint());
                add("nomeLinguaAziendale", getRequiredConstraint());
                add(getTipoAttributoNumeroDecimaliConstraint());
                add(getUnitaMisuraTipoAttributoConstraint());
            }
        };
    }

    /**
     * Crea le regole per il tipo esportazione.
     *
     * @return regole create
     */
    private Rules createTipoEsportazioneRules() {
        return new Rules(TipoEsportazione.class) {
            @Override
            protected void initRules() {
                add("nome", getRequiredConstraint());
                add("jndiName", getRequiredConstraint());
                add("template", getRequiredConstraint());
                add("nomeFile", getRequiredConstraint());
                add("tipoSpedizione", getRequiredConstraint());
                add("tipiAreeMagazzino", getCollectionRequiredRequired());
                add("datiSpedizione.suffissoNomeFile", getRequiredConstraint());
            }
        };
    }

    /**
     * Crea le regole per {@link TipoMezzoTrasporto}.
     *
     * @return regole create
     */
    private Rules createTipoMezzoTrasportoRules() {
        return new Rules(TipoMezzoTrasporto.class) {
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
     * Crea le regole per {@link TipoPorto}.
     *
     * @return regole create
     */
    private Rules createTipoPortoRules() {
        return new Rules(TipoPorto.class) {
            @Override
            protected void initRules() {
                add("descrizione", getRequiredConstraint());
                add("descrizione", getMaxCharConstraint(30));
            }
        };
    }

    /**
     * Crea le regole per {@link TrasportoCura}.
     *
     * @return regole create
     */
    private Rules createTrasportoCuraRules() {
        return new Rules(TrasportoCura.class) {
            @Override
            protected void initRules() {
                add("descrizione", getRequiredConstraint());
                add("descrizione", getMaxCharConstraint(45));
            }
        };
    }

    /**
     * Crea le regole per {@link VersioneListino}.
     *
     * @return regole create
     */
    private Rules createVersioneListinoRules() {
        return new Rules(VersioneListino.class) {
            @Override
            protected void initRules() {
                add("dataVigore", getRequiredConstraint());
            }
        };
    }

    /**
     * @return note blocco constraint
     */
    private PropertyConstraint getAnnoSchedaArticoloArticoloConstraint() {
        PropertyConstraint propertyConstraint = new PropertyValueConstraint("gestioneSchedaArticolo", eq(true));
        PropertyConstraint domainObjectIfTrue = new RequiredIfTrue("gestioneSchedaArticoloAnno", propertyConstraint);
        PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(
                domainObjectIfTrue);
        propertyResolvableConstraint.setType("gestioneSchedaArticoloAnnoRequired");
        return propertyResolvableConstraint;
    }

    /**
     *
     * @return constraint per l'entità dell'area magazzino
     */
    private PropertyConstraint getAreaMagazzinoEntitaConstraint() {
        PropertyConstraint propertyConstraint = new PropertyValueConstraint(
                "areaMagazzino.tipoAreaMagazzino.tipoDocumento.tipoEntita",
                new Or(eq(TipoEntita.CLIENTE), eq(TipoEntita.FORNITORE)));
        PropertyConstraint domainObjectIfTrue = new RequiredIfTrue("areaMagazzino.documento.entita",
                propertyConstraint);
        PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(
                domainObjectIfTrue);
        propertyResolvableConstraint.setType("areaMagazzinoEntitaRequired");
        return propertyResolvableConstraint;
    }

    /**
     *
     * @return constraint per il mezzo trasporto dell'area magazzino.
     */
    private PropertyConstraint getAreaMagazzinoMezzoTrasportoConstraint() {
        PropertyConstraint propertyConstraint = new PropertyValueConstraint(
                "areaMagazzino.tipoAreaMagazzino.richiestaMezzoTrasportoObbligatorio", eq(true));
        PropertyConstraint domainObjectIfTrue = new RequiredIfTrue("areaMagazzino.mezzoTrasporto", propertyConstraint);
        PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(
                domainObjectIfTrue);
        propertyResolvableConstraint.setType("areaMagazzinoMezzoTrasportoRequired");
        return propertyResolvableConstraint;
    }

    /**
     *
     * @return constraint per la sede entità dell'area magazzino.
     */
    private PropertyConstraint getAreaMagazzinoSedeEntitaConstraint() {
        PropertyConstraint propertyConstraint = new PropertyValueConstraint(
                "areaMagazzino.tipoAreaMagazzino.tipoDocumento.tipoEntita",
                new Or(eq(TipoEntita.CLIENTE), eq(TipoEntita.FORNITORE)));
        PropertyConstraint domainObjectIfTrue = new RequiredIfTrue("areaMagazzino.documento.sedeEntita",
                propertyConstraint);
        PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(
                domainObjectIfTrue);
        propertyResolvableConstraint.setType("areaMagazzinoSedeEntitaRequired");
        return propertyResolvableConstraint;
    }

    /**
     * Crea la regola di validazione per il sotto conto dell'estratto conto.
     *
     * @return constraint
     */
    private PropertyConstraint getArticoloCalcoloPrezziConstraint() {
        PropertyConstraint propertyConstraint = new PropertyValueConstraint("articolo",
                not(getDomainAttributeRequiredConstraint()));
        PropertyConstraint domainObjectIfTrue = new RequiredIfTrue("entita", propertyConstraint);
        PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(
                domainObjectIfTrue);
        propertyResolvableConstraint.setType("parametriVerificaPrezziArticoloRequired");
        return propertyResolvableConstraint;
    }

    /**
     *
     * @return constraint per il codice dell'articolo.
     */
    private PropertyConstraint getArticoloCodiceConstraint() {
        PropertyConstraint propertyConstraint = new PropertyValueConstraint(
                "categoria.generazioneCodiceArticoloData.mascheraDescrizioneArticolo", new Or(eq(null), eq("")));
        PropertyConstraint domainObjectIfTrue = new RequiredIfTrue("codice", propertyConstraint);
        PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(
                domainObjectIfTrue);
        propertyResolvableConstraint.setType("articoloCodiceAziendaleRequired");
        return propertyResolvableConstraint;
    }

    /**
     *
     * @return constraint per la descrizione dell'articolo.
     */
    private PropertyConstraint getArticoloDescrizioneConstraint() {
        PropertyConstraint propertyConstraint = new PropertyValueConstraint(
                "categoria.generazioneCodiceArticoloData.mascheraDescrizioneArticolo", new Or(eq(null), eq("")));
        PropertyConstraint domainObjectIfTrue = new RequiredIfTrue("descrizione", propertyConstraint);
        PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(
                domainObjectIfTrue);
        propertyResolvableConstraint.setType("articoloDescrizioneAziendaleRequired");
        return propertyResolvableConstraint;
    }

    /**
     * Se il tipo codice iva alternativo non è NESSUNO il codice iva alternativo deve essere
     * obbligatorio.
     *
     * @return Constraint
     */
    private PropertyConstraint getCodiceIvaAlternativoSedeMagazzinoRequiredConstraint() {
        PropertyConstraint propertyConstraint = new PropertyValueConstraint("tipologiaCodiceIvaAlternativo",
                new Not(eq(ETipologiaCodiceIvaAlternativo.NESSUNO)));

        PropertyConstraint domainObjectIfTrue = new RequiredIfTrue("codiceIvaAlternativo", propertyConstraint);

        PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(
                domainObjectIfTrue);
        propertyResolvableConstraint.setType("codiceIvaAlternativoRequired");

        return propertyResolvableConstraint;
    }

    /**
     * crea e resituisce i constraint di {@link AreaRate} all'interno di
     * {@link AreaMagazzinoFullDTO}.
     *
     * @return {@link PropertyConstraint} di {@link CodicePagamento}
     */
    private PropertyConstraint getCodicePagamentoConstraint() {
        PropertyConstraint propertyConstraint = new PropertyValueConstraint("areaRateEnabled", eq(true));
        PropertyConstraint domainObjectIfTrue = new RequiredIfTrue("areaRate.codicePagamento", propertyConstraint);
        PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(
                domainObjectIfTrue);
        propertyResolvableConstraint.setType("areaMagazzinoCodicePagamentoRequired");
        return propertyResolvableConstraint;
    }

    /**
     * Se il tipo codice iva alternativo è ESENZIONE_DICHIARAZIONE_INTENTO la data della
     * dichiarazione d'intento deve essere obbligatorio.
     *
     * @return Constraint
     */
    private PropertyConstraint getDataScadenzaDichiarazioneIntentoSedeMagazzinoRequiredConstraint() {
        PropertyConstraint propertyConstraint = new PropertyValueConstraint("tipologiaCodiceIvaAlternativo",
                eq(ETipologiaCodiceIvaAlternativo.ESENZIONE_DICHIARAZIONE_INTENTO));

        PropertyConstraint domainObjectIfTrue = new RequiredIfTrue("dichiarazioneIntento.dataScadenza",
                propertyConstraint);

        PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(
                domainObjectIfTrue);
        propertyResolvableConstraint.setType("dataScadenzaDichiarazioneIntentoRequired");

        return propertyResolvableConstraint;
    }

    /**
     * Se TRASFERIMENTO o CARICO_PRODUZIONE il deposito deve essere inserito.
     *
     * @return Constraint
     */
    private PropertyConstraint getDepositoDestinazioneRequiredConstraint() {
        PropertyConstraint propertyConstraint = new PropertyValueConstraint(
                "areaMagazzino.tipoAreaMagazzino.tipoMovimento",
                new Or(eq(TipoMovimento.TRASFERIMENTO), eq(TipoMovimento.CARICO_PRODUZIONE)));

        PropertyConstraint domainObjectIfTrue = new RequiredIfTrue("areaMagazzino.depositoDestinazione",
                propertyConstraint);

        PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(
                domainObjectIfTrue);
        propertyResolvableConstraint.setType("depositoDestinazioneRequired");

        return propertyResolvableConstraint;
    }

    /**
     * @return constraint per il deposito di destinazione dell'area magazzino.
     */
    private PropertyConstraint getDepositoDestinazioneTipoAreaMagazzinoConstraint() {
        PropertyConstraint propertyConstraint = new PropertyValueConstraint("depositoDestinazioneBloccato", eq(true));
        PropertyConstraint domainObjectIfTrue = new RequiredIfTrue("depositoDestinazione", propertyConstraint);
        PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(
                domainObjectIfTrue);
        propertyResolvableConstraint.setType("depositoDestinazioneRequired");
        return propertyResolvableConstraint;
    }

    /**
     * Se CARICO_PRODUZIONE il deposito può essere uguale.
     *
     * @return constraint per per il deposito di destinazione dell'area magazzino.
     */
    private PropertyConstraint getDepositoDestinazioneTrasferimentoConstraint() {
        PropertyConstraint propertyConstraint = new PropertyValueConstraint(
                "areaMagazzino.tipoAreaMagazzino.tipoMovimento", eq(TipoMovimento.TRASFERIMENTO));

        ConfrontoPropertiesConstraint constraintConfronto = new ConfrontoPropertiesConstraint(
                "areaMagazzino.depositoDestinazione", EqualTo.instance(), "areaMagazzino.depositoOrigine",
                propertyConstraint, true);

        return constraintConfronto;
    }

    /**
     * @return constraint per il deposito di origine dell'area magazzino.
     */
    private PropertyConstraint getDepositoOrigineTipoAreaMagazzinoConstraint() {
        PropertyConstraint propertyConstraint = new PropertyValueConstraint("depositoOrigineBloccato", eq(true));
        PropertyConstraint domainObjectIfTrue = new RequiredIfTrue("depositoOrigine", propertyConstraint);
        PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(
                domainObjectIfTrue);
        propertyResolvableConstraint.setType("depositoOrigineRequired");
        return propertyResolvableConstraint;
    }

    /**
     * @return Deposito per manutenzione listino rule
     */
    private PropertyConstraint getDepositoPerManutenzioneConstraint() {
        PropertyConstraint propertyConstraint = new PropertyValueConstraint("provenienzaPrezzoManutenzioneListino",
                new Or(eq(ProvenienzaPrezzoManutenzioneListino.ULTIMO_COSTO_DEPOSITO),
                        eq(ProvenienzaPrezzoManutenzioneListino.COSTO_MEDIO_PONDERATO)));
        PropertyConstraint domainObjectIfTrue = new RequiredIfTrue("deposito", propertyConstraint);

        PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(
                domainObjectIfTrue);
        propertyResolvableConstraint.setType("depositoOrigineRequired");
        return propertyResolvableConstraint;
    }

    /**
     * Crea la regola di validazione per il centro di costo dell'estratto conto.
     *
     * @return constraint
     */
    private PropertyConstraint getEntitaCalcoloPrezziConstraint() {
        PropertyConstraint propertyConstraint = new PropertyValueConstraint("entita",
                not(getDomainAttributeRequiredConstraint()));
        PropertyConstraint domainObjectIfTrue = new RequiredIfTrue("articolo", propertyConstraint);
        PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(
                domainObjectIfTrue);
        return propertyResolvableConstraint;
    }

    /**
     *
     * @return property costraint
     */
    private PropertyConstraint getFilePerManutenzioneConstraint() {
        PropertyValueConstraint listinoConstraint = new PropertyValueConstraint("provenienzaPrezzoManutenzioneListino",
                eq(ProvenienzaPrezzoManutenzioneListino.FILE_ESTERNO));
        PropertyConstraint domainObjectIfTrue = new RequiredIfTrue("file", listinoConstraint);
        PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(
                domainObjectIfTrue);
        propertyResolvableConstraint.setType("required");
        return propertyResolvableConstraint;
    }

    /**
     * Se il confronto è LISTINO il listino deve essere inserito.
     *
     * @param propertyTipoConfronto
     *            proprietà tipo confronto
     * @return Constraint
     */
    private PropertyConstraint getListinoTipoConfrontoRequiredConstraint(String propertyTipoConfronto) {
        PropertyConstraint propertyConstraint = new PropertyValueConstraint(propertyTipoConfronto + ".confronto",
                eq(EConfronto.LISTINO));

        PropertyConstraint domainObjectIfTrue = new RequiredIfTrue(propertyTipoConfronto + ".listino",
                propertyConstraint);

        PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(
                domainObjectIfTrue);
        propertyResolvableConstraint.setType("listinoTipoConfrontoRequired");

        return propertyResolvableConstraint;
    }

    // /**
    // * @return PropertyConstraint
    // */
    // private PropertyConstraint getParametriCalcoloPrezziEntitaConstraint() {
    // PropertyConstraint propertyConstraint = new
    // PropertyValueConstraint("articolo.provenienzaPrezzoArticolo",
    // eq(ProvenienzaPrezzoArticolo.TIPOMEZZOZONAGEOGRAFICA));
    // PropertyConstraint domainObjectIfTrue = new RequiredIfTrue("entita",
    // propertyConstraint);
    // PropertyResolvableConstraint propertyResolvableConstraint = new
    // PropertyResolvableConstraint(domainObjectIfTrue);
    // //
    // propertyResolvableConstraint.setType("areaMagazzinoMezzoTrasportoRequired");
    // return propertyResolvableConstraint;
    // }

    // /**
    // * @return PropertyConstraint
    // */
    // private PropertyConstraint getParametriCalcoloPrezziListinoConstraint() {
    // PropertyConstraint propertyConstraint = new
    // PropertyValueConstraint("articolo.provenienzaPrezzoArticolo",
    // eq(ProvenienzaPrezzoArticolo.DOCUMENTO));
    // PropertyConstraint domainObjectIfTrue = new RequiredIfTrue("listino",
    // propertyConstraint);
    // PropertyResolvableConstraint propertyResolvableConstraint = new
    // PropertyResolvableConstraint(domainObjectIfTrue);
    // //
    // propertyResolvableConstraint.setType("areaMagazzinoMezzoTrasportoRequired");
    // return propertyResolvableConstraint;
    // }

    /**
     * @return note blocco constraint
     */
    private PropertyConstraint getMeseSchedaArticoloArticoloConstraint() {
        PropertyConstraint propertyConstraint = new PropertyValueConstraint("gestioneSchedaArticolo", eq(true));
        PropertyConstraint domainObjectIfTrue = new ConditionalPropertyConstraint(propertyConstraint,
                new PropertyValueConstraint("gestioneSchedaArticoloMese", range(1, 12)));
        PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(
                domainObjectIfTrue);
        propertyResolvableConstraint.setType("gestioneSchedaArticoloMeseRange");
        return propertyResolvableConstraint;
    }

    /**
     * @return note blocco constraint
     */
    private PropertyConstraint getNoteBloccoConstraint() {
        PropertyConstraint propertyConstraint = new PropertyValueConstraint("blocco", eq(true));
        PropertyConstraint domainObjectIfTrue = new RequiredIfTrue("noteBlocco", propertyConstraint);
        PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(
                domainObjectIfTrue);
        propertyResolvableConstraint.setType("noteBloccoRequired");
        return propertyResolvableConstraint;
    }

    /**
     * @return PropertyConstraint
     */
    private PropertyConstraint getParametriCalcoloPrezziSedeEntitaConstraint() {
        PropertyConstraint propertyConstraint = new PropertyValueConstraint("articolo.provenienzaPrezzoArticolo",
                eq(ProvenienzaPrezzoArticolo.TIPOMEZZOZONAGEOGRAFICA));
        PropertyConstraint domainObjectIfTrue = new RequiredIfTrue("sedeEntita", propertyConstraint);
        PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(
                domainObjectIfTrue);
        // propertyResolvableConstraint.setType("areaMagazzinoMezzoTrasportoRequired");
        return propertyResolvableConstraint;
    }

    /**
     * @return PropertyConstraint
     */
    private PropertyConstraint getParametriCalcoloPrezziTipoMezzoConstraint() {
        PropertyConstraint propertyConstraint = new PropertyValueConstraint("articolo.provenienzaPrezzoArticolo",
                eq(ProvenienzaPrezzoArticolo.TIPOMEZZOZONAGEOGRAFICA));
        PropertyConstraint domainObjectIfTrue = new RequiredIfTrue("tipoMezzoTrasporto", propertyConstraint);
        PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(
                domainObjectIfTrue);
        propertyResolvableConstraint.setType("mezzoTrasportoRequired");
        return propertyResolvableConstraint;
    }

    /**
     * @return Constraint
     */
    private Constraint getQtaChiusaConstraint() {
        PropertyConstraint propertyConstraint = new PropertyValueConstraint("qtaChiusa", not(eq(0.00)));
        PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(
                propertyConstraint);
        propertyResolvableConstraint.setType("qtaChiusaPresente");

        ConfrontoPropertiesConstraint constraintConfronto = new ConfrontoPropertiesConstraint("qta",
                GreaterThanEqualTo.instance(), "qtaChiusa");

        PropertyResolvableConstraint qtaMaggioreUgualeQtaChiusa = new PropertyResolvableConstraint(constraintConfronto);
        qtaMaggioreUgualeQtaChiusa.setType("qtaMaggioreUgualeQtaChiusaConstraint");

        PropertyConstraint propertyConstraint2 = new PropertyValueConstraint("qtaChiusa", eq(0.00));
        PropertyResolvableConstraint propertyResolvableConstraint2 = new PropertyResolvableConstraint(
                propertyConstraint2);
        propertyResolvableConstraint2.setType("qtaChiusaAssente");

        return disjunction().add(propertyResolvableConstraint2)
                .add(conjunction().add(propertyResolvableConstraint).add(qtaMaggioreUgualeQtaChiusa));
    }

    /**
     * @return PropertyConstraint
     */
    private PropertyConstraint getRigaContrattoScontoRequired() {
        PropertyConstraint propertyConstraint = new PropertyValueConstraint("strategiaScontoAbilitata", eq(true));
        PropertyConstraint domainObjectIfTrue = new RequiredIfTrue("strategiaSconto.sconto", propertyConstraint);
        PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(
                domainObjectIfTrue);
        propertyResolvableConstraint.setType("rigaContrattoValorePrezzoRequired");
        return propertyResolvableConstraint;
    }

    /**
     * @return PropertyConstraint
     */
    private PropertyConstraint getRigaContrattoValorePrezzoRequired() {
        PropertyConstraint propertyConstraint = new PropertyValueConstraint("strategiaPrezzoAbilitata", eq(true));
        PropertyConstraint domainObjectIfTrue = new RequiredIfTrue("strategiaPrezzo.valorePrezzo", propertyConstraint);
        PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(
                domainObjectIfTrue);
        propertyResolvableConstraint.setType("rigaContrattoValorePrezzoRequired");
        return propertyResolvableConstraint;
    }

    @Override
    public List<Rules> getRules() {
        List<Rules> listRules = new ArrayList<Rules>();

        listRules.add(createArticoloRules());

        listRules.add(createTipoAttributoRules());

        listRules.add(createMagazzinoSettingsRules());

        listRules.add(createCodiceArticoloEntitaRules());

        listRules.add(createCategoriaRules());
        listRules.add(createCategoriaScontoSedeRules());
        listRules.add(createCategoriaScontoArticoloRules());

        listRules.add(createScontoMaggiorazione());

        listRules.add(createAreaMagazzinoRules());
        listRules.add(createTipoAreaMagazzinoRules());
        listRules.add(createDatoAccompagnatorioMagazzinoMetaDataRules());

        listRules.add(createParametriRicercaAreaMagazzinoRules());
        listRules.add(createParametriCreazioneDocumentoFatturazione());
        listRules.add(createParametriRicercaValorizzazioneRules());
        listRules.add(createParametriRicercaMovimentazioneArticoloRules());
        listRules.add(createParametriRicercaMovimentazioneRules());

        listRules.add(createListinoRules());

        listRules.add(createTrasportoCuraRules());
        listRules.add(createTipoPortoRules());
        listRules.add(createCausaleTrasportoRules());
        listRules.add(createAspettoEsterioreRules());

        listRules.add(createVersioneListinoRules());
        listRules.add(createRigaListinoRules());

        listRules.add(createContrattoRules());
        listRules.add(createRigaContrattoRules());

        listRules.add(createTipoMezzoTrasportoRules());
        listRules.add(createMezzoTrasportoRules());

        listRules.add(createRigaArticoloRules());
        listRules.add(createRigaNotaRules());
        listRules.add(createParametriRicercaFatturazioneRules());

        listRules.add(createFormulaTrasformazioneQta());

        listRules.add(createFaseLavorazioneRules());

        listRules.add(createParametriCalcoloPrezziPMRules());

        listRules.add(createSedeMagazzinoRules());

        listRules.add(createParametriRicercaManutenzioneListinoRules());
        listRules.add(createParametriAggiornaManutenzioneListinoRules());

        listRules.add(createParametriContabilizzazioneWrapperRules());

        listRules.add(createSedeListiniPMRules());

        listRules.add(createAttributoArticoloRules());
        listRules.add(createAttributoCategoriaRules());

        listRules.add(createRigaRaggruppamentoArticoliRules());
        listRules.add(createRaggruppamentoArticoliRules());

        listRules.add(createRigaContrattoAgenteRules());

        listRules.add(createParametriRicercaSpedizioniRules());

        listRules.add(createTipoEsportazioneRules());

        listRules.add(createParametriStampaEtichetteArticoloRules());
        listRules.add(createEtichettaArticoloRules());

        listRules.add(createComponenteRules());
        listRules.add(createOmaggioRules());

        listRules.add(createLayoutStampaTipoAreaMagazzinoRules());

        listRules.add(createScontoRules());
        listRules.add(createCategoriaSedeRules());

        listRules.add(createSottoContoContabilizzaioneRules());

        listRules.add(createParametriRicercaConfrontoListinoRules());

        listRules.add(createParametriCreazioneSchedeArticoliRules());
        listRules.add(createParametriRicercaElaborazioniRules());

        listRules.add(createScaglioneListinoRules());
        listRules.add(createArticoloAlternativoRules());

        listRules.add(createSogliaAddebitoDichiarazioneSettingsRules());

        listRules.add(createParametriListinoPrezziRules());

        return listRules;
    }

    /**
     * Se il tipo codice iva alternativo è ESENZIONE_DICHIARAZIONE_INTENTO il testo della
     * dichiarazione d'intento deve essere obbligatorio.
     *
     * @return Constraint
     */
    private PropertyConstraint getTestoDichiarazioneIntentoSedeMagazzinoRequiredConstraint() {
        PropertyConstraint propertyConstraint = new PropertyValueConstraint("tipologiaCodiceIvaAlternativo",
                eq(ETipologiaCodiceIvaAlternativo.ESENZIONE_DICHIARAZIONE_INTENTO));

        PropertyConstraint domainObjectIfTrue = new RequiredIfTrue("dichiarazioneIntento.testo", propertyConstraint);

        PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(
                domainObjectIfTrue);
        propertyResolvableConstraint.setType("testoDichiarazioneIntentoRequired");

        return propertyResolvableConstraint;
    }

    /**
     * @return constraint per il numero decimali del tipo attributo
     */
    private PropertyConstraint getTipoAttributoNumeroDecimaliConstraint() {
        PropertyConstraint propertyConstraint = new PropertyValueConstraint("tipoDato",
                eq(ETipoDatoTipoAttributo.NUMERICO));
        PropertyConstraint domainObjectIfTrue = new RequiredIfTrue("numeroDecimali", propertyConstraint);
        PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(
                domainObjectIfTrue);
        propertyResolvableConstraint.setType("tipoAttributoNumeroDecimaliRequired");
        return propertyResolvableConstraint;
    }

    /**
     * @return constraint per il numero decimali del tipo attributo
     */
    private PropertyConstraint getUnitaMisuraTipoAttributoConstraint() {
        PropertyConstraint propertyConstraint = new PropertyValueConstraint("totalizzatoreTipoAttributo",
                not(eq(TotalizzatoreTipoAttributo.NESSUNO)));
        PropertyConstraint domainObjectIfTrue = new RequiredIfTrue("unitaMisura", propertyConstraint);
        PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(
                domainObjectIfTrue);
        propertyResolvableConstraint.setType("tipoAttributoUnitaMisuraRequired");
        return propertyResolvableConstraint;
    }

    /**
     * @return PropertyConstraint
     */
    private PropertyConstraint getVersioneListinoPerManutenzioneConstraint() {
        PropertyValueConstraint listinoConstraint = new PropertyValueConstraint("provenienzaPrezzoManutenzioneListino",
                eq(ProvenienzaPrezzoManutenzioneListino.LISTINO));
        PropertyConstraint domainObjectIfTrue = new RequiredIfTrue("versioneListino", listinoConstraint);
        PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(
                domainObjectIfTrue);
        propertyResolvableConstraint.setType("versioneListinoRequired");
        return propertyResolvableConstraint;
    }

    /**
     * se il parametro vettore e presente, debe essere inserito anche lòa sede del vettore.
     *
     * @return properticonstraint.
     */
    private PropertyConstraint getVettorePresenteConstraint() {
        PropertyConstraint propertyConstraint = new PropertyValueConstraint("areaMagazzino.vettore.id", present());
        PropertyConstraint domainObjectIfTrue = new RequiredIfTrue("areaMagazzino.sedeVettore", propertyConstraint);
        PropertyResolvableConstraint propertyResolvableConstraint = new PropertyResolvableConstraint(
                domainObjectIfTrue);
        propertyResolvableConstraint.setType("areaMagazzinoSedeVettoreRequired");
        return propertyResolvableConstraint;
    }
}
