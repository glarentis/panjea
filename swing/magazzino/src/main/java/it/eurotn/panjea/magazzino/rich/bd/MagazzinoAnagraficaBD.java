package it.eurotn.panjea.magazzino.rich.bd;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import it.eurotn.panjea.anagrafica.domain.CodiceIva;
import it.eurotn.panjea.anagrafica.domain.Deposito;
import it.eurotn.panjea.anagrafica.domain.Entita;
import it.eurotn.panjea.anagrafica.domain.FaseLavorazione;
import it.eurotn.panjea.anagrafica.domain.FaseLavorazioneArticolo;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.panjea.magazzino.domain.ArticoloAlternativo;
import it.eurotn.panjea.magazzino.domain.ArticoloDeposito;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.AspettoEsteriore;
import it.eurotn.panjea.magazzino.domain.AttributoArticolo;
import it.eurotn.panjea.magazzino.domain.Categoria;
import it.eurotn.panjea.magazzino.domain.CategoriaCommercialeArticolo;
import it.eurotn.panjea.magazzino.domain.CategoriaSedeMagazzino;
import it.eurotn.panjea.magazzino.domain.CausaleTrasporto;
import it.eurotn.panjea.magazzino.domain.CodiceArticoloEntita;
import it.eurotn.panjea.magazzino.domain.Componente;
import it.eurotn.panjea.magazzino.domain.ConfigurazioneDistinta;
import it.eurotn.panjea.magazzino.domain.DepositoMagazzino;
import it.eurotn.panjea.magazzino.domain.FormulaTrasformazione;
import it.eurotn.panjea.magazzino.domain.Listino;
import it.eurotn.panjea.magazzino.domain.Listino.ETipoListino;
import it.eurotn.panjea.magazzino.domain.MagazzinoSettings;
import it.eurotn.panjea.magazzino.domain.MezzoTrasporto;
import it.eurotn.panjea.magazzino.domain.RaggruppamentoArticoli;
import it.eurotn.panjea.magazzino.domain.RigaListino;
import it.eurotn.panjea.magazzino.domain.RigaRaggruppamentoArticoli;
import it.eurotn.panjea.magazzino.domain.ScaglioneListino;
import it.eurotn.panjea.magazzino.domain.ScaglioneListinoStorico;
import it.eurotn.panjea.magazzino.domain.Sconto;
import it.eurotn.panjea.magazzino.domain.SedeMagazzino;
import it.eurotn.panjea.magazzino.domain.SedeMagazzinoLite;
import it.eurotn.panjea.magazzino.domain.TemplateSpedizioneMovimenti;
import it.eurotn.panjea.magazzino.domain.TipoAttributo;
import it.eurotn.panjea.magazzino.domain.TipoDocumentoBaseMagazzino;
import it.eurotn.panjea.magazzino.domain.TipoMezzoTrasporto;
import it.eurotn.panjea.magazzino.domain.TipoPorto;
import it.eurotn.panjea.magazzino.domain.TipoVariante;
import it.eurotn.panjea.magazzino.domain.TrasportoCura;
import it.eurotn.panjea.magazzino.domain.VersioneListino;
import it.eurotn.panjea.magazzino.domain.moduloprezzo.ListinoTipoMezzoZonaGeografica;
import it.eurotn.panjea.magazzino.domain.omaggio.Omaggio;
import it.eurotn.panjea.magazzino.domain.omaggio.TipoOmaggio;
import it.eurotn.panjea.magazzino.domain.rendicontazione.EntitaTipoEsportazione;
import it.eurotn.panjea.magazzino.domain.rendicontazione.TipoEsportazione;
import it.eurotn.panjea.magazzino.exception.ArticoliDuplicatiManutenzioneListinoException;
import it.eurotn.panjea.magazzino.exception.FormuleLinkateException;
import it.eurotn.panjea.magazzino.exception.ListinoManutenzioneNonValidoException;
import it.eurotn.panjea.magazzino.service.exception.RigaListinoListiniCollegatiException;
import it.eurotn.panjea.magazzino.service.exception.RigheListinoListiniCollegatiException;
import it.eurotn.panjea.magazzino.service.interfaces.DataWarehouseService;
import it.eurotn.panjea.magazzino.service.interfaces.ListinoService;
import it.eurotn.panjea.magazzino.service.interfaces.ListinoTipoMezzoZonaGeograficaService;
import it.eurotn.panjea.magazzino.service.interfaces.MagazzinoAnagraficaService;
import it.eurotn.panjea.magazzino.util.ArticoloConfigurazioneDistinta;
import it.eurotn.panjea.magazzino.util.ArticoloRicerca;
import it.eurotn.panjea.magazzino.util.CategoriaLite;
import it.eurotn.panjea.magazzino.util.ConfrontoListinoDTO;
import it.eurotn.panjea.magazzino.util.ParametriRicercaArticolo;
import it.eurotn.panjea.magazzino.util.RigaListinoDTO;
import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriAggiornaManutenzioneListino;
import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriRicercaConfrontoListino;
import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriRicercaManutenzioneListino;
import it.eurotn.panjea.magazzino.util.parametriricerca.RigaManutenzioneListino;
import it.eurotn.panjea.rich.bd.AbstractBaseBD;
import it.eurotn.panjea.riepilogo.util.RiepilogoArticoloDTO;
import it.eurotn.panjea.riepilogo.util.RiepilogoSedeEntitaDTO;
import it.eurotn.panjea.utils.PanjeaSwingUtil;

public class MagazzinoAnagraficaBD extends AbstractBaseBD implements IMagazzinoAnagraficaBD {

    private static final Logger LOGGER = Logger.getLogger(MagazzinoAnagraficaBD.class);

    public static final String BEAN_ID = "magazzinoAnagraficaBD";

    private MagazzinoAnagraficaService magazzinoAnagraficaService;

    private ListinoService listinoService;

    private ListinoTipoMezzoZonaGeograficaService listinoTipoMezzoZonaGeograficaService;

    private DataWarehouseService dataWarehouseService;

    /**
     * Costruttore.
     */
    public MagazzinoAnagraficaBD() {
        super();
    }

    @Override
    public void aggiornaArticoliAlternativiFiltro(int idArticolo, String formula) {
        LOGGER.debug("--> Enter aggiornaArticoliAlternativiFiltro");
        start("aggiornaArticoliAlternativiFiltro");
        try {
            magazzinoAnagraficaService.aggiornaArticoliAlternativiFiltro(idArticolo, formula);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("aggiornaArticoliAlternativiFiltro");
        }
        LOGGER.debug("--> Exit aggiornaArticoliAlternativiFiltro ");

    }

    @Override
    public void aggiornaListinoDaManutenzione(ParametriAggiornaManutenzioneListino parametriAggiornaManutenzioneListino)
            throws ArticoliDuplicatiManutenzioneListinoException {
        LOGGER.debug("--> Enter aggiornaListinoDaManutenzione");
        start("aggiornaListinoDaManutenzione");
        try {
            listinoService.aggiornaListinoDaManutenzione(parametriAggiornaManutenzioneListino);
        } catch (ArticoliDuplicatiManutenzioneListinoException e) {
            throw e;
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("aggiornaListinoDaManutenzione");
        }
        LOGGER.debug("--> Exit aggiornaListinoDaManutenzione");
    }

    @Override
    public void aggiungiCategoriaMerceologicaACategoriaCommerciale(int idCategoriaMerceologica,
            int idCategoriaCommercialeArticolo) {
        LOGGER.debug("--> Enter aggiungiCategoriaMerceologicaACategoriaCommerciale");
        start("aggiungiCategoriaMerceologicaACategoriaCommerciale");
        try {
            magazzinoAnagraficaService.aggiungiCategoriaMerceologicaACategoriaCommerciale(idCategoriaMerceologica,
                    idCategoriaCommercialeArticolo);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("aggiungiCategoriaMerceologicaACategoriaCommerciale");
        }
        LOGGER.debug("--> Exit aggiungiCategoriaMerceologicaACategoriaCommerciale ");
    }

    @Override
    public Componente aggiungiComponenteAConfigurazione(ConfigurazioneDistinta configurazioneDistinta,
            Componente componentePadre, Articolo articoloDaAggiungere) {
        LOGGER.debug("--> Enter aggiungiComponenteAConfigurazione");
        start("aggiungiComponenteAConfigurazione");
        Componente result = null;
        try {
            result = magazzinoAnagraficaService.aggiungiComponenteAConfigurazione(configurazioneDistinta,
                    componentePadre, articoloDaAggiungere);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("aggiungiComponenteAConfigurazione");
        }
        LOGGER.debug("--> Exit aggiungiComponenteAConfigurazione ");
        return result;
    }

    @Override
    public Set<FaseLavorazioneArticolo> aggiungiFasiLavorazione(ConfigurazioneDistinta configurazione,
            ArticoloLite articoloLite, Set<FaseLavorazioneArticolo> fasiLavorazioni) {
        LOGGER.debug("--> Enter aggiungiFasiLavorazione");
        start("aggiungiFasiLavorazione");
        Set<FaseLavorazioneArticolo> result = null;
        try {
            result = magazzinoAnagraficaService.aggiungiFasiLavorazione(configurazione, articoloLite, fasiLavorazioni);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("aggiungiFasiLavorazione");
        }
        LOGGER.debug("--> Exit aggiungiFasiLavorazione ");
        return result;
    }

    @Override
    public Set<FaseLavorazioneArticolo> aggiungiFasiLavorazione(ConfigurazioneDistinta configurazione,
            Componente componente, Set<FaseLavorazioneArticolo> fasiLavorazioni) {
        LOGGER.debug("--> Enter aggiungiFasiLavorazione");
        start("aggiungiFasiLavorazione");
        Set<FaseLavorazioneArticolo> result = null;
        try {
            result = magazzinoAnagraficaService.aggiungiFasiLavorazione(configurazione, componente, fasiLavorazioni);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("aggiungiFasiLavorazione");
        }
        LOGGER.debug("--> Exit aggiungiFasiLavorazione ");
        return result;
    }

    @Override
    public void associaSediASedePerRifatturazione(List<SedeEntita> sediEntita,
            SedeMagazzinoLite sedePerRifatturazione) {
        LOGGER.debug("--> Enter associaSediASedePerRifatturazione");
        start("associaSediASedePerRifatturazione");
        try {
            magazzinoAnagraficaService.associaSediASedePerRifatturazione(sediEntita, sedePerRifatturazione);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("associaSediASedePerRifatturazione");
        }
        LOGGER.debug("--> Exit associaSediASedePerRifatturazione ");
    }

    @Override
    public void associaSediMagazzinoPerRifatturazione(SedeMagazzinoLite sedeDiRifatturazione,
            List<SedeMagazzinoLite> sediDaAssociare) {
        LOGGER.debug("--> Enter associaSediMagazzinoPerRifatturazione");
        start("associaSediMagazzinoPerRifatturazione");
        try {
            magazzinoAnagraficaService.associaSediMagazzinoPerRifatturazione(sedeDiRifatturazione, sediDaAssociare);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("associaSediMagazzinoPerRifatturazione");
        }
        LOGGER.debug("--> Exit associaSediMagazzinoPerRifatturazione ");
    }

    @Override
    public void asyncAggiornaMovimenti(Date dataIniziale) {
        LOGGER.debug("--> Enter aggiornaMovimenti");
        start("aggiornaMovimenti");
        try {
            dataWarehouseService.sincronizzaMovimenti(dataIniziale);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("aggiornaMovimenti");
        }
        LOGGER.debug("--> Exit aggiornaMovimenti ");
    }

    @Override
    public String calcolaEAN() {
        LOGGER.debug("--> Enter calcolaEAN");
        start("calcolaEAN");
        String result = "";
        try {
            result = magazzinoAnagraficaService.calcolaEAN();
        } catch (Exception e) {
            LOGGER.error("--> errore ", e);
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("calcolaEAN");
        }
        end("calcolaEAN");
        LOGGER.debug("--> Exit calcolaEAN");
        return result;
    }

    @Override
    public void cambiaCategoriaAdArticoli(List<ArticoloRicerca> articoli, Categoria categoriaDestinazione) {
        LOGGER.debug("--> Enter cambiaCategoriaAdArticoli");
        start("cambiaCategoriaAdArticoli");
        try {
            magazzinoAnagraficaService.cambiaCategoriaAdArticoli(articoli, categoriaDestinazione);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cambiaCategoriaAdArticoli");
        }
        LOGGER.debug("--> Exit cambiaCategoriaAdArticoli ");
    }

    @Override
    public void cambiaCategoriaCommercialeAdArticoli(List<ArticoloRicerca> articoli,
            CategoriaCommercialeArticolo categoriaCommercialeArticolo,
            CategoriaCommercialeArticolo categoriaCommercialeArticolo2) {
        LOGGER.debug("--> Enter cambiaCategoriaCommercialeAdArticoli");
        start("cambiaCategoriaCommercialeAdArticoli");
        try {
            magazzinoAnagraficaService.cambiaCategoriaCommercialeAdArticoli(articoli, categoriaCommercialeArticolo,
                    categoriaCommercialeArticolo2);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cambiaCategoriaCommercialeAdArticoli");
        }
        LOGGER.debug("--> Exit cambiaCategoriaCommercialeAdArticoli ");
    }

    @Override
    public void cambiaCodiceIVA(CodiceIva codiceIvaDaSostituire, CodiceIva nuovoCodiceIva) {
        LOGGER.debug("--> Enter cambiaCodiceIVA");
        start("cambiaCodiceIVA");
        try {
            magazzinoAnagraficaService.cambiaCodiceIVA(codiceIvaDaSostituire, nuovoCodiceIva);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cambiaCodiceIVA");
        }
        LOGGER.debug("--> Exit cambiaCodiceIVA ");
    }

    @Override
    public boolean cancellaArticoli(List<ArticoloRicerca> articoli) {
        LOGGER.debug("--> Enter cancellaArticoli");
        start("cancellaArticoli");
        boolean okCancellazione = false;
        try {
            okCancellazione = magazzinoAnagraficaService.cancellaArticoli(articoli);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaArticoli");
        }
        LOGGER.debug("--> Exit cancellaArticoli ");
        return okCancellazione;
    }

    @Override
    public void cancellaArticolo(Articolo articolo) {
        LOGGER.debug("--> Enter cancellaArticolo");
        start("cancellaArticolo");
        try {
            magazzinoAnagraficaService.cancellaArticolo(articolo);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaArticolo");
        }
        LOGGER.debug("--> Exit cancellaArticolo ");
    }

    @Override
    public void cancellaArticoloAlternativo(ArticoloAlternativo articoloAlternativo) {
        LOGGER.debug("--> Enter cancellaArticoloAlternativo");
        start("cancellaArticoloAlternativo");
        try {
            magazzinoAnagraficaService.cancellaArticoloAlternativo(articoloAlternativo);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaArticoloAlternativo");
        }
        LOGGER.debug("--> Exit cancellaArticoloAlternativo");
    }

    @Override
    public void cancellaArticoloDeposito(ArticoloDeposito articoloDeposito) {
        LOGGER.debug("--> Enter cancellaArticoloDeposito");
        start("cancellaArticoloDeposito");
        try {
            magazzinoAnagraficaService.cancellaArticoloDeposito(articoloDeposito);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaArticoloDeposito");
        }
        LOGGER.debug("--> Exit cancellaArticoloDeposito ");
    }

    @Override
    public void cancellaAspettoEsteriore(AspettoEsteriore aspettoEsteriore) {
        LOGGER.debug("--> Enter cancellaAspettoEsteriore");
        start("cancellaAspettoEsteriore");
        try {
            magazzinoAnagraficaService.cancellaAspettoEsteriore(aspettoEsteriore);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaAspettoEsteriore");
        }
        LOGGER.debug("--> Exit cancellaAspettoEsteriore ");
    }

    @Override
    public void cancellaCategoria(Integer idCategoria) {
        LOGGER.debug("--> Enter cancellaCategoria");
        start("cancellaCategoria");
        try {
            magazzinoAnagraficaService.cancellaCategoria(idCategoria);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaCategoria");
        }
        LOGGER.debug("--> Exit cancellaCategoria ");
    }

    @Override
    public void cancellaCategoriaCommercialeArticolo(CategoriaCommercialeArticolo categoriaCommercialeArticolo) {
        LOGGER.debug("--> Enter cancellaCategoriaCommercialeArticolo");
        start("cancellaCategoriaCommercialeArticolo");
        try {
            magazzinoAnagraficaService.cancellaCategoriaCommercialeArticolo(categoriaCommercialeArticolo);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaCategoriaCommercialeArticolo");
        }
        LOGGER.debug("--> Exit cancellaCategoriaCommercialeArticolo ");

    }

    @Override
    public void cancellaCategorieSediMagazzino(CategoriaSedeMagazzino categoriaSedeMagazzino) {
        LOGGER.debug("--> Enter cancellaCategorieSediMagazzino");
        start("cancellaCategorieSediMagazzino");
        try {
            magazzinoAnagraficaService.cancellaCategorieSediMagazzino(categoriaSedeMagazzino);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaCategorieSediMagazzino");
        }
        LOGGER.debug("--> Exit cancellaCategorieSediMagazzino ");
    }

    @Override
    public void cancellaCausaleTrasporto(CausaleTrasporto causaleTrasporto) {
        LOGGER.debug("--> Enter cancellaCausaleTrasporto");
        start("cancellaCausaleTrasporto");
        try {
            magazzinoAnagraficaService.cancellaCausaleTrasporto(causaleTrasporto);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaCausaleTrasporto");
        }
        LOGGER.debug("--> Exit cancellaCausaleTrasporto ");
    }

    @Override
    public void cancellaCodiceArticoloEntita(CodiceArticoloEntita codiceArticoloEntita) {
        LOGGER.debug("--> Enter cancellaCodiceArticoloEntita");
        start("cancellaCodiceArticoloEntita");
        try {
            magazzinoAnagraficaService.cancellaCodiceArticoloEntita(codiceArticoloEntita);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaCodiceArticoloEntita");
        }
        LOGGER.debug("--> Exit cancellaCodiceArticoloEntita ");
    }

    @Override
    public void cancellaComponentiConfigurazioneDistinta(List<Componente> componenteSelezionato) {
        LOGGER.debug("--> Enter cancellaComponenteConfigurazioneDistinta");
        start("cancellaComponenteConfigurazioneDistinta");
        try {
            magazzinoAnagraficaService.cancellaComponentiConfigurazioneDistinta(componenteSelezionato);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaComponenteConfigurazioneDistinta");
        }
        LOGGER.debug("--> Exit cancellaComponenteConfigurazioneDistinta ");
    }

    @Override
    public void cancellaConfigurazioneDistinta(ConfigurazioneDistinta configurazioneDistinta) {
        LOGGER.debug("--> Enter cancellaConfigurazioneDistinta");
        start("cancellaConfigurazioneDistinta");
        try {
            magazzinoAnagraficaService.cancellaConfigurazioneDistinta(configurazioneDistinta);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaConfigurazioneDistinta");
        }
        LOGGER.debug("--> Exit cancellaConfigurazioneDistinta ");

    }

    @Override
    public void cancellaEntitaTipoEsportazione(EntitaTipoEsportazione entitaTipoEsportazione) {
        LOGGER.debug("--> Enter cancellaEntitaTipoEsportazione");
        start("cancellaEntitaTipoEsportazione");
        try {
            magazzinoAnagraficaService.cancellaEntitaTipoEsportazione(entitaTipoEsportazione);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaEntitaTipoEsportazione");
        }
        LOGGER.debug("--> Exit cancellaEntitaTipoEsportazione ");
    }

    @Override
    public void cancellaFaseLavorazione(FaseLavorazione faseLavorazione) {
        LOGGER.debug("--> Enter cancellaFaseLavorazione");
        start("cancellaFaseLavorazione");
        try {
            magazzinoAnagraficaService.cancellaFaseLavorazione(faseLavorazione);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaFaseLavorazione");
        }
        LOGGER.debug("--> Exit cancellaFaseLavorazione");
    }

    @Override
    public void cancellaFasiLavorazioneArticolo(ConfigurazioneDistinta configurazioneDistinta,
            List<FaseLavorazioneArticolo> fasiArticoloDaCancellare) {
        LOGGER.debug("--> Enter cancellaFasiLavorazioneArticolo");
        start("cancellaFasiLavorazioneArticolo");
        try {
            magazzinoAnagraficaService.cancellaFasiLavorazioneArticolo(configurazioneDistinta,
                    fasiArticoloDaCancellare);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaFasiLavorazioneArticolo");
        }
        LOGGER.debug("--> Exit cancellaFasiLavorazioneArticolo ");

    }

    @Override
    public void cancellaFormulaTrasformazione(FormulaTrasformazione formulaTrasformazione) {
        LOGGER.debug("--> Enter cancellaFormulaTrasformazione");
        start("cancellaFormulaTrasformazione");
        try {
            magazzinoAnagraficaService.cancellaFormulaTrasformazione(formulaTrasformazione);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaFormulaTrasformazione");
        }
        LOGGER.debug("--> Exit cancellaFormulaTrasformazione ");

    }

    @Override
    public void cancellaListino(Listino listino) {
        LOGGER.debug("--> Enter cancellaListino");
        start("cancellaListino");
        try {
            listinoService.cancellaListino(listino);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaListino");
        }
        LOGGER.debug("--> Exit cancellaListino ");
    }

    @Override
    public void cancellaListinoTipoMezzoZonaGeografica(ListinoTipoMezzoZonaGeografica listinoTipoMezzoZonaGeografica) {
        LOGGER.debug("--> Enter cancellaListinoTipoMezzoZonaGeografica");
        start("cancellaListinoTipoMezzoZonaGeografica");
        try {
            listinoTipoMezzoZonaGeograficaService
                    .cancellaListinoTipoMezzoZonaGeografica(listinoTipoMezzoZonaGeografica);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaListinoTipoMezzoZonaGeografica");
        }
        LOGGER.debug("--> Exit cancellaListinoTipoMezzoZonaGeografica ");
    }

    @Override
    public void cancellaMezzoTrasporto(MezzoTrasporto mezzoTrasporto) {
        LOGGER.debug("--> Enter cancellaMezzoTrasporto");
        start("cancellaMezzoTrasporto");
        try {
            magazzinoAnagraficaService.cancellaMezzoTrasporto(mezzoTrasporto);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaMezzoTrasporto");
        }
        LOGGER.debug("--> Exit cancellaMezzoTrasporto");
    }

    @Override
    public void cancellaRaggruppamento(RaggruppamentoArticoli raggruppamentoArticoli) {
        LOGGER.debug("--> Enter cancellaRaggruppamento");
        start("cancellaRaggruppamento");
        try {
            magazzinoAnagraficaService.cancellaRaggruppamento(raggruppamentoArticoli);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaRaggruppamento");
        }
        LOGGER.debug("--> Exit cancellaRaggruppamento ");

    }

    @Override
    public void cancellaRigaListino(RigaListino rigaListino) {
        LOGGER.debug("--> Enter cancellaRigaListino");
        start("cancellaRigaListino");
        try {
            listinoService.cancellaRigaListino(rigaListino);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaRigaListino");
        }
        LOGGER.debug("--> Exit cancellaRigaListino ");
    }

    @Override
    public void cancellaRigaRaggruppamentoArticoli(RigaRaggruppamentoArticoli rigaRaggruppamentoArticoli) {
        LOGGER.debug("--> Enter cancellaRigaRaggruppamentoArticoli");
        start("cancellaRigaRaggruppamentoArticoli");
        try {
            magazzinoAnagraficaService.cancellaRigaRaggruppamentoArticoli(rigaRaggruppamentoArticoli);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaRigaRaggruppamentoArticoli");
        }
        LOGGER.debug("--> Exit cancellaRigaRaggruppamentoArticoli ");

    }

    @Override
    public void cancellaRigheListino(List<RigaListino> righeListino) {
        LOGGER.debug("--> Enter cancellaRigheListino");
        start("cancellaRigheListino");
        try {
            listinoService.cancellaRigheListino(righeListino);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaRigheListino");
        }
        LOGGER.debug("--> Exit cancellaRigheListino ");
    }

    @Override
    public void cancellaRigheManutenzioneListino(List<RigaManutenzioneListino> righeManutenzioneListino) {
        LOGGER.debug("--> Enter cancellaRigheManutenzioneListino");
        start("cancellaRigheManutenzioneListino");
        try {
            listinoService.cancellaRigheManutenzioneListino(righeManutenzioneListino);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaRigheManutenzioneListino");
        }
        LOGGER.debug("--> Exit cancellaRigheManutenzioneListino ");
    }

    @Override
    public void cancellaSconto(Sconto sconto) {
        LOGGER.debug("--> Enter cancellaSconto");
        start("cancellaSconto");
        try {
            magazzinoAnagraficaService.cancellaSconto(sconto);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaSconto");
        }
        LOGGER.debug("--> Exit cancellaSconto ");
    }

    @Override
    public void cancellaTipoAttributo(TipoAttributo tipoAttributo) {
        LOGGER.debug("--> Enter cancellaTipoAttributo");
        start("cancellaTipoAttributo");
        try {
            magazzinoAnagraficaService.cancellaTipoAttributo(tipoAttributo);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaTipoAttributo");
        }
        LOGGER.debug("--> Exit cancellaTipoAttributo ");
    }

    @Override
    public void cancellaTipoDocumentoBase(TipoDocumentoBaseMagazzino tipoDocumentoBase) {
        LOGGER.debug("--> Enter cancellaTipoDocumentoBase");
        start("cancellaTipoDocumentoBase");
        try {
            magazzinoAnagraficaService.cancellaTipoDocumentoBase(tipoDocumentoBase);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaTipoDocumentoBase");
        }
        LOGGER.debug("--> Exit cancellaTipoDocumentoBase ");
    }

    @Override
    public void cancellaTipoEsportazione(TipoEsportazione tipoEsportazione) {
        LOGGER.debug("--> Enter cancellaTipoEsportazione");
        start("cancellaTipoEsportazione");
        try {
            magazzinoAnagraficaService.cancellaTipoEsportazione(tipoEsportazione);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaTipoEsportazione");
        }
        LOGGER.debug("--> Exit cancellaTipoEsportazione ");
    }

    @Override
    public void cancellaTipoMezzoTrasporto(TipoMezzoTrasporto tipoMezzoTrasporto) {
        LOGGER.debug("--> Enter cancellaTipoMezzoTrasporto");
        start("cancellaTipoMezzoTrasporto");
        try {
            magazzinoAnagraficaService.cancellaTipoMezzoTrasporto(tipoMezzoTrasporto);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaTipoMezzoTrasporto");
        }
        LOGGER.debug("--> Exit cancellaTipoMezzoTrasporto ");
    }

    @Override
    public void cancellaTipoPorto(TipoPorto tipoPorto) {
        LOGGER.debug("--> Enter cancellaTipoPorto");
        start("cancellaTipoPorto");
        try {
            magazzinoAnagraficaService.cancellaTipoPorto(tipoPorto);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaTipoPorto");
        }
        LOGGER.debug("--> Exit cancellaTipoPorto ");
    }

    @Override
    public void cancellaTrasportoCura(TrasportoCura trasportoCura) {
        LOGGER.debug("--> Enter cancellaTrasportoCura");
        start("cancellaTrasportoCura");
        try {
            magazzinoAnagraficaService.cancellaTrasportoCura(trasportoCura);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaTrasportoCura");
        }
        LOGGER.debug("--> Exit cancellaTrasportoCura ");
    }

    @Override
    public void cancellaVersioneListino(VersioneListino versioneListino) {
        LOGGER.debug("--> Enter cancellaVersioneListino");
        start("cancellaVersioneListino");
        try {
            listinoService.cancellaVersioneListino(versioneListino);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaVersioneListino");
        }
        LOGGER.debug("--> Exit cancellaVersioneListino ");
    }

    @Override
    public Set<ArticoloAlternativo> caricaArticoliAlternativi(Articolo articolo) {
        LOGGER.debug("--> Enter caricaArticoliAlternativi");
        start("caricaArticoliAlternativi");
        Set<ArticoloAlternativo> result = null;
        try {
            result = magazzinoAnagraficaService.caricaArticoliAlternativi(articolo);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaArticoliAlternativi");
        }
        LOGGER.debug("--> Exit caricaArticoliAlternativi ");
        return result;
    }

    @Override
    public List<ArticoloDeposito> caricaArticoliDeposito(Deposito deposito) {
        LOGGER.debug("--> Enter caricaArticoliDeposito");
        start("caricaArticoliDeposito");
        List<ArticoloDeposito> list = new ArrayList<ArticoloDeposito>();
        try {
            list = magazzinoAnagraficaService.caricaArticoliDeposito(deposito);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaArticoliDeposito");
        }
        LOGGER.debug("--> Exit caricaArticoliDeposito ");
        return list;
    }

    @Override
    public List<ArticoloDeposito> caricaArticoliDeposito(Integer idArticolo) {
        LOGGER.debug("--> Enter caricaArticoliDeposito");
        start("caricaArticoliDeposito");
        List<ArticoloDeposito> list = new ArrayList<ArticoloDeposito>();
        try {
            list = magazzinoAnagraficaService.caricaArticoliDeposito(idArticolo);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaArticoliDeposito");
        }
        LOGGER.debug("--> Exit caricaArticoliDeposito ");
        return list;
    }

    @Override
    public Articolo caricaArticolo(Articolo articolo, boolean initializeLazy) {
        LOGGER.debug("--> Enter caricArticolo");
        start("caricArticolo");
        Articolo articoloCaricato = null;
        try {
            articoloCaricato = magazzinoAnagraficaService.caricaArticolo(articolo, initializeLazy);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricArticolo");
        }
        LOGGER.debug("--> Exit caricArticolo ");
        return articoloCaricato;
    }

    @Override
    public ArticoloConfigurazioneDistinta caricaArticoloConfigurazioneDistinta(
            ConfigurazioneDistinta configurazioneDistinta) {
        LOGGER.debug("--> Enter caricaArticoloConfigurazioneDistinta");
        start("caricaArticoloConfigurazioneDistinta");
        ArticoloConfigurazioneDistinta result = null;
        try {
            result = magazzinoAnagraficaService.caricaArticoloConfigurazioneDistinta(configurazioneDistinta);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaArticoloConfigurazioneDistinta");
        }
        LOGGER.debug("--> Exit caricaArticoloConfigurazioneDistinta ");
        return result;
    }

    @Override
    public ArticoloDeposito caricaArticoloDeposito(Integer idArticolo, Integer idDeposito) {
        LOGGER.debug("--> Enter caricaArticoloDeposito");
        start("caricaArticoloDeposito");
        ArticoloDeposito articoloDeposito = null;
        try {
            articoloDeposito = magazzinoAnagraficaService.caricaArticoloDeposito(idArticolo, idDeposito);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaArticoloDeposito");
        }
        LOGGER.debug("--> Exit caricaArticoloDeposito ");
        return articoloDeposito;
    }

    @Override
    public ArticoloLite caricaArticoloLite(int idArticolo) {
        LOGGER.debug("--> Enter caricaArticoloLite");
        start("caricaArticoloLite");
        ArticoloLite result = null;
        try {
            result = magazzinoAnagraficaService.caricaArticoloLite(idArticolo);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaArticoloLite");
        }
        LOGGER.debug("--> Exit caricaArticoloLite ");
        return result;
    }

    @Override
    public List<AspettoEsteriore> caricaAspettiEsteriori(String descrizione) {
        LOGGER.debug("--> Enter caricaAspettiEsteriori");
        start("caricaAspettiEsteriori");
        List<AspettoEsteriore> list = new ArrayList<AspettoEsteriore>();
        try {
            list = magazzinoAnagraficaService.caricaAspettiEsteriori(descrizione);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaAspettiEsteriori");
        }
        LOGGER.debug("--> Exit caricaAspettiEsteriori ");
        return list;
    }

    @Override
    public Categoria caricaCategoria(Categoria categoria, boolean initialiazeLazy) {
        LOGGER.debug("--> Enter caricaCategoria");
        start("caricaCategoria");
        Categoria categoriaCaricata = null;
        try {
            categoriaCaricata = magazzinoAnagraficaService.caricaCategoria(categoria, initialiazeLazy);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaCategoria");
        }
        LOGGER.debug("--> Exit caricaCategoria ");
        return categoriaCaricata;
    }

    @Override
    public CategoriaCommercialeArticolo caricaCategoriaCommercialeArticolo(int idCategoriaCommercialeArticolo) {
        LOGGER.debug("--> Enter caricaCategoriaCommercialeArticolo");
        start("caricaCategoriaCommercialeArticolo");
        CategoriaCommercialeArticolo result = new CategoriaCommercialeArticolo();
        result.setId(idCategoriaCommercialeArticolo);
        try {
            result = magazzinoAnagraficaService.caricaCategoriaCommercialeArticolo(result);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaCategoriaCommercialeArticolo");
        }
        LOGGER.debug("--> Exit caricaCategoriaCommercialeArticolo ");
        return result;
    }

    @Override
    public List<CategoriaLite> caricaCategorie() {
        LOGGER.debug("--> Enter caricaCategorie");
        start("caricaCategorie");
        List<CategoriaLite> list = new ArrayList<CategoriaLite>();
        try {
            list = magazzinoAnagraficaService.caricaCategorie();
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaCategorie");
        }
        LOGGER.debug("--> Exit caricaCategorie ");
        return list;
    }

    @Override
    public List<Categoria> caricaCategorieCodiceDescrizione(String fieldSearch, String valueSearch) {
        LOGGER.debug("--> Enter caricaCategorieCodiceDescrizione");
        start("caricaCategorieCodiceDescrizione");
        List<Categoria> result = new ArrayList<Categoria>();
        try {
            result = magazzinoAnagraficaService.caricaCategorieCodiceDescrizione(fieldSearch, valueSearch);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaCategorieCodiceDescrizione");
        }
        LOGGER.debug("--> Exit caricaCategorieCodiceDescrizione ");
        return result;
    }

    @Override
    public List<CategoriaCommercialeArticolo> caricaCategorieCommercialeArticolo(String fieldSearch,
            String valueSearch) {
        LOGGER.debug("--> Enter caricaCategorieCommercialeArticolo");
        start("caricaCategorieCommercialeArticolo");
        List<CategoriaCommercialeArticolo> result = null;
        try {
            result = magazzinoAnagraficaService.caricaCategorieCommercialeArticolo(fieldSearch, valueSearch);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaCategorieCommercialeArticolo");
        }
        LOGGER.debug("--> Exit caricaCategorieCommercialeArticolo ");
        return result;
    }

    @Override
    public List<CategoriaSedeMagazzino> caricaCategorieSediMagazzino(String fieldSearch, String valueSearch) {
        LOGGER.debug("--> Enter caricaCategorieSediMagazzino");
        start("caricaCategorieSediMagazzino");

        List<CategoriaSedeMagazzino> list = new ArrayList<CategoriaSedeMagazzino>();
        try {
            list = magazzinoAnagraficaService.caricaCategorieSediMagazzino(fieldSearch, valueSearch);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaCategorieSediMagazzino");
        }
        LOGGER.debug("--> Exit caricaCategorieSediMagazzino ");
        return list;
    }

    @Override
    public List<CausaleTrasporto> caricaCausaliTraporto(String descrizione) {
        LOGGER.debug("--> Enter caricaCausaliTraporto");
        start("caricaCausaliTraporto");
        List<CausaleTrasporto> list = new ArrayList<CausaleTrasporto>();
        try {
            list = magazzinoAnagraficaService.caricaCausaliTraporto(descrizione);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaCausaliTraporto");
        }
        LOGGER.debug("--> Exit caricaCausaliTraporto ");
        return list;
    }

    @Override
    public List<CodiceArticoloEntita> caricaCodiciArticoloEntita(Entita entita) {
        LOGGER.debug("--> Enter caricaCodiciArticoloEntita");
        start("caricaCodiciArticoloEntita");
        List<CodiceArticoloEntita> list = new ArrayList<CodiceArticoloEntita>();
        try {
            list = magazzinoAnagraficaService.caricaCodiciArticoloEntita(entita);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaCodiciArticoloEntita");
        }
        LOGGER.debug("--> Exit caricaCodiciArticoloEntita ");
        return list;
    }

    @Override
    public List<CodiceArticoloEntita> caricaCodiciArticoloEntita(Integer idArticolo) {
        LOGGER.debug("--> Enter caricaCodiciArticoloEntita");
        start("caricaCodiciArticoloEntita");
        List<CodiceArticoloEntita> list = new ArrayList<CodiceArticoloEntita>();
        try {
            list = magazzinoAnagraficaService.caricaCodiciArticoloEntita(idArticolo);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaCodiciArticoloEntita");
        }
        LOGGER.debug("--> Exit caricaCodiciArticoloEntita ");
        return list;
    }

    @Override
    public Set<Componente> caricaComponenti(ConfigurazioneDistinta configurazioneDistinta) {
        LOGGER.debug("--> Enter caricaComponenti");
        start("caricaComponenti");
        Set<Componente> result = null;
        try {
            result = magazzinoAnagraficaService.caricaComponenti(configurazioneDistinta);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaComponenti");
        }
        LOGGER.debug("--> Exit caricaComponenti ");
        return result;
    }

    @Override
    public Set<Componente> caricaComponenti(int idArticolo) {
        LOGGER.debug("--> Enter caricaComponenti");
        start("caricaComponenti");
        Set<Componente> result = null;
        try {
            result = magazzinoAnagraficaService.caricaComponenti(idArticolo);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaComponenti");
        }
        LOGGER.debug("--> Exit caricaComponenti ");
        return result;
    }

    @Override
    public ConfigurazioneDistinta caricaConfigurazioneDistinta(int idConfigurazione) {
        LOGGER.debug("--> Enter caricaConfigurazioneDistinta");
        start("caricaConfigurazioneDistinta");
        ConfigurazioneDistinta result = null;
        try {
            result = magazzinoAnagraficaService.caricaConfigurazioneDistinta(idConfigurazione);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaConfigurazioneDistinta");
        }
        LOGGER.debug("--> Exit caricaConfigurazioneDistinta ");
        return result;
    }

    @Override
    public List<ConfigurazioneDistinta> caricaConfigurazioniDistinta(ArticoloLite distinta) {
        LOGGER.debug("--> Enter caricaConfigurazioniDistinta");
        start("caricaConfigurazioniDistinta");
        List<ConfigurazioneDistinta> result = null;
        try {
            result = magazzinoAnagraficaService.caricaConfigurazioniDistinta(distinta);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaConfigurazioniDistinta");
        }
        LOGGER.debug("--> Exit caricaConfigurazioniDistinta ");
        return result;
    }

    @Override
    public ConfrontoListinoDTO caricaConfrontoListino(ParametriRicercaConfrontoListino parametri) {
        LOGGER.debug("--> Enter caricaConfrontoListino");
        start("caricaConfrontoListino");
        ConfrontoListinoDTO confrontoListinoDTO = null;
        try {
            confrontoListinoDTO = listinoService.caricaConfrontoListino(parametri);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaConfrontoListino");
        }
        LOGGER.debug("--> Exit caricaConfrontoListino ");
        return confrontoListinoDTO;
    }

    @Override
    public DepositoMagazzino caricaDepositoMagazzinoByDeposito(Deposito deposito) {
        LOGGER.debug("--> Enter caricaDepositoMagazzinoByDeposito");
        start("caricaDepositoMagazzinoByDeposito");
        DepositoMagazzino depositoMagazzinoCaricato = null;
        try {
            depositoMagazzinoCaricato = magazzinoAnagraficaService.caricaDepositoMagazzinoByDeposito(deposito);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaDepositoMagazzinoByDeposito");
        }
        LOGGER.debug("--> Exit caricaDepositoMagazzinoByDeposito ");
        return depositoMagazzinoCaricato;
    }

    @Override
    public Set<Componente> caricaDistinteComponente(Integer idArticolo) {
        LOGGER.debug("--> Enter caricaDistinteComponente");
        start("caricaDistinteComponente");
        Set<Componente> result = null;
        try {
            result = magazzinoAnagraficaService.caricaDistinteComponente(idArticolo);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaDistinteComponente");
        }
        LOGGER.debug("--> Exit caricaDistinteComponente");
        return result;
    }

    @Override
    public List<EntitaTipoEsportazione> caricaEntitaTipoEsportazione() {
        LOGGER.debug("--> Enter caricaEntitaTipoEsportazione");
        start("caricaEntitaTipoEsportazione");
        List<EntitaTipoEsportazione> entitaTipoEsportazione = new ArrayList<EntitaTipoEsportazione>();
        try {
            entitaTipoEsportazione = magazzinoAnagraficaService.caricaEntitaTipoEsportazione();
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaEntitaTipoEsportazione");
        }
        LOGGER.debug("--> Exit caricaEntitaTipoEsportazione ");
        return entitaTipoEsportazione;
    }

    @Override
    public FaseLavorazione caricaFaseLavorazione(int idFase) {
        LOGGER.debug("--> Enter caricaFaseLavorazione");
        start("caricaFaseLavorazione");
        try {
            return magazzinoAnagraficaService.caricaFaseLavorazione(idFase);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaFaseLavorazione");
        }
        LOGGER.debug("--> Exit caricaFaseLavorazione ");
        return null;
    }

    @Override
    public List<FaseLavorazione> caricaFasiLavorazione(String codice) {
        LOGGER.debug("--> Enter caricaFasiLavorazione");
        start("caricaFasiLavorazione");
        List<FaseLavorazione> list = new ArrayList<FaseLavorazione>();
        try {
            list = magazzinoAnagraficaService.caricaFasiLavorazione(codice);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaFasiLavorazione");
        }
        LOGGER.debug("--> Exit caricaFasiLavorazione");
        return list;
    }

    @Override
    public Set<FaseLavorazioneArticolo> caricaFasiLavorazioneArticolo(ConfigurazioneDistinta configurazioneDistinta) {
        LOGGER.debug("--> Enter caricaFasiLavorazione");
        start("caricaFasiLavorazione");
        Set<FaseLavorazioneArticolo> result = null;
        try {
            result = magazzinoAnagraficaService.caricaFasiLavorazioneArticolo(configurazioneDistinta);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaFasiLavorazione");
        }
        LOGGER.debug("--> Exit caricaFasiLavorazione ");
        return result;
    }

    @Override
    public FormulaTrasformazione caricaFormulaTrasformazione(FormulaTrasformazione formulaTrasformazione) {
        LOGGER.debug("--> Enter caricaFormulaTrasformazione");
        start("caricaFormulaTrasformazione");
        FormulaTrasformazione formulaTrasformazioneLoad = null;
        try {
            formulaTrasformazioneLoad = magazzinoAnagraficaService.caricaFormulaTrasformazione(formulaTrasformazione);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaFormulaTrasformazione");
        }
        LOGGER.debug("--> Exit caricaFormulaTrasformazione ");
        return formulaTrasformazioneLoad;
    }

    @Override
    public List<FormulaTrasformazione> caricaFormuleTrasformazione(String codice) {
        LOGGER.debug("--> Enter caricaFormuleTrasformazione");
        start("caricaFormuleTrasformazione");
        List<FormulaTrasformazione> formule = new ArrayList<FormulaTrasformazione>();
        try {
            formule = magazzinoAnagraficaService.caricaFormuleTrasformazione(codice);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaFormuleTrasformazione");
        }
        LOGGER.debug("--> Exit caricaFormuleTrasformazione ");
        return formule;
    }

    @Override
    public BigDecimal caricaImportoListino(Integer idListino, Integer idArticolo) {
        LOGGER.debug("--> Enter caricaImportoListino");
        start("caricaImportoListino");
        BigDecimal importo = null;
        try {
            importo = listinoService.caricaImportoListino(idListino, idArticolo);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaImportoListino");
        }
        LOGGER.debug("--> Exit caricaImportoListino ");
        return importo;
    }

    @Override
    public List<Listino> caricaListini() {
        LOGGER.debug("--> Enter caricaListini");
        start("caricaListini");
        List<Listino> list = new ArrayList<Listino>();
        try {
            list = listinoService.caricaListini();
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaListini");
        }
        LOGGER.debug("--> Exit caricaListini ");
        return list;
    }

    @Override
    public List<Listino> caricaListini(ETipoListino tipoListino, String searchField, String searchValue) {
        LOGGER.debug("--> Enter caricaListini");
        start("caricaListini");
        List<Listino> list = new ArrayList<Listino>();
        try {
            list = listinoService.caricaListini(tipoListino, searchField, searchValue);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaListini");
        }
        LOGGER.debug("--> Exit caricaListini ");
        return list;
    }

    @Override
    public List<ListinoTipoMezzoZonaGeografica> caricaListiniTipoMezzoZonaGeografica() {
        LOGGER.debug("--> Enter caricaListiniTipoMezzoZonaGeografica");
        start("caricaListiniTipoMezzoZonaGeografica");
        List<ListinoTipoMezzoZonaGeografica> list = new ArrayList<ListinoTipoMezzoZonaGeografica>();
        try {
            list = listinoTipoMezzoZonaGeograficaService.caricaListiniTipoMezzoZonaGeografica();
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaListiniTipoMezzoZonaGeografica");
        }
        LOGGER.debug("--> Exit caricaListiniTipoMezzoZonaGeografica ");
        return list;
    }

    @Override
    public Listino caricaListino(Listino listino, boolean initializeRighe) {
        LOGGER.debug("--> Enter caricaListino");
        start("caricaListino");
        Listino listinoCaricato = null;
        try {
            listinoCaricato = listinoService.caricaListino(listino, initializeRighe);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaListino");
        }
        LOGGER.debug("--> Exit caricaListino ");
        return listinoCaricato;
    }

    @Override
    public MagazzinoSettings caricaMagazzinoSettings() {
        LOGGER.debug("--> Enter caricaMagazzinoSettings");
        start("caricaMagazzinoSettings");
        MagazzinoSettings contabilitaSettings = magazzinoAnagraficaService.caricaMagazzinoSettings();
        end("caricaMagazzinoSettings");
        LOGGER.debug("--> Exit caricaMagazzinoSettings");
        return contabilitaSettings;
    }

    @Override
    public List<MezzoTrasporto> caricaMezziTrasporto(String targa, boolean abilitato, EntitaLite entita) {
        LOGGER.debug("--> Enter caricaMezziTrasporto");
        return caricaMezziTrasporto(targa, abilitato, entita, false);
    }

    @Override
    public List<MezzoTrasporto> caricaMezziTrasporto(String valueSearch, boolean abilitati, EntitaLite entitaLite,
            boolean senzaCaricatore) {
        LOGGER.debug("--> Enter caricaMezziTrasporto");
        start("caricaMezziTrasporto");
        List<MezzoTrasporto> list = new ArrayList<MezzoTrasporto>();
        try {
            list = magazzinoAnagraficaService.caricaMezziTrasporto(valueSearch, abilitati, entitaLite, senzaCaricatore);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaMezziTrasporto");
        }
        LOGGER.debug("--> Exit caricaMezziTrasporto ");
        return list;
    }

    @Override
    public List<Omaggio> caricaOmaggi() {
        LOGGER.debug("--> Enter caricaOmaggi");
        start("caricaOmaggi");
        List<Omaggio> list = new ArrayList<Omaggio>();
        try {
            list = magazzinoAnagraficaService.caricaOmaggi();
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaOmaggi");
        }
        LOGGER.debug("--> Exit caricaOmaggi ");
        return list;
    }

    @Override
    public Omaggio caricaOmaggioByTipo(TipoOmaggio tipoOmaggio) {
        LOGGER.debug("--> Enter caricaOmaggioByTipo");
        start("caricaOmaggioByTipo");
        Omaggio omaggio = null;
        try {
            omaggio = magazzinoAnagraficaService.caricaOmaggioByTipo(tipoOmaggio);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaOmaggioByTipo");
        }
        LOGGER.debug("--> Exit caricaOmaggioByTipo ");
        return omaggio;
    }

    @Override
    public List<RaggruppamentoArticoli> caricaRaggruppamenti() {
        LOGGER.debug("--> Enter caricaRaggruppamenti");
        start("caricaRaggruppamenti");
        List<RaggruppamentoArticoli> result = null;
        try {
            result = magazzinoAnagraficaService.caricaRaggruppamenti();
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaRaggruppamenti");
        }
        LOGGER.debug("--> Exit caricaRaggruppamenti ");
        return result;
    }

    @Override
    public RaggruppamentoArticoli caricaRaggruppamentoArticoli(RaggruppamentoArticoli raggruppamentoArticoli) {
        LOGGER.debug("--> Enter caricaRaggruppamentoArticoli");
        start("caricaRaggruppamentoArticoli");
        RaggruppamentoArticoli raggruppamentoArticoliLoad = null;
        try {
            raggruppamentoArticoliLoad = magazzinoAnagraficaService
                    .caricaRaggruppamentoArticoli(raggruppamentoArticoli);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaRaggruppamentoArticoli");
        }
        LOGGER.debug("--> Exit caricaRaggruppamentoArticoli ");
        return raggruppamentoArticoliLoad;
    }

    @Override
    public List<RiepilogoArticoloDTO> caricaRiepilogoArticoli() {
        LOGGER.debug("--> Enter caricaRiepilogoArticoli");
        start("caricaRiepilogoArticoli");
        List<RiepilogoArticoloDTO> riepilogo = null;
        try {
            riepilogo = magazzinoAnagraficaService.caricaRiepilogoArticoli();
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaRiepilogoArticoli");
        }
        LOGGER.debug("--> Exit caricaRiepilogoArticoli ");
        return riepilogo;
    }

    @Override
    public RigaListino caricaRigaListino(Integer idRiga) {
        LOGGER.debug("--> Enter caricaRigaListino");
        start("caricaRigaListino");
        RigaListino rigaListino = null;
        try {
            rigaListino = listinoService.caricaRigaListino(idRiga);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaRigaListino");
        }
        LOGGER.debug("--> Exit caricaRigaListino ");
        return rigaListino;
    }

    @Override
    public List<RigaListinoDTO> caricaRigheListinoByArticolo(Integer idArticolo) {
        LOGGER.debug("--> Enter caricaRigheListinoByArticolo");
        start("caricaRigheListinoByArticolo");
        List<RigaListinoDTO> result = null;
        try {
            result = listinoService.caricaRigheListinoByArticolo(idArticolo);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaRigheListinoByArticolo");
        }
        LOGGER.debug("--> Exit caricaRigheListinoByArticolo ");
        return result;
    }

    @Override
    public List<RigaListinoDTO> caricaRigheListinoByVersione(Integer idVersioneListino) {
        LOGGER.debug("--> Enter caricaRigheListinoByVersione");
        start("caricaRigheListino");
        List<RigaListinoDTO> result = null;
        try {
            // System.err.println("AAAA " + Thread.currentThread().getId());
            result = listinoService.caricaRigheListinoByVersione(idVersioneListino);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaVersioneListino");
        }
        LOGGER.debug("--> Exit caricaRigheListinoByVersione ");
        return result;
    }

    @Override
    public List<RigaListino> caricaRigheListinoDaAggiornare(Date data, List<ArticoloLite> articoli) {
        LOGGER.debug("--> Enter caricaRigheListinoDaAggiornare");
        start("caricaRigheListinoDaAggiornare");
        List<RigaListino> result = new ArrayList<RigaListino>();
        try {
            result = listinoService.caricaRigheListinoDaAggiornare(data, articoli);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaRigheListinoDaAggiornare");
        }
        LOGGER.debug("--> Exit caricaRigheListinoDaAggiornare");
        return result;
    }

    @Override
    public List<RigaManutenzioneListino> caricaRigheManutenzioneListino() {
        LOGGER.debug("--> Enter caricaParametriRicercaManutenzioneListino");
        start("caricaParametriRicercaManutenzioneListino");
        List<RigaManutenzioneListino> list = new ArrayList<RigaManutenzioneListino>();
        try {
            list = listinoService.caricaRigheManutenzioneListino();
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaParametriRicercaManutenzioneListino");
        }
        LOGGER.debug("--> Exit caricaParametriRicercaManutenzioneListino");
        return list;
    }

    @Override
    public Set<RigaRaggruppamentoArticoli> caricaRigheRaggruppamento(RaggruppamentoArticoli raggruppamentoArticoli) {
        LOGGER.debug("--> Enter caricaRigheRaggruppamento");
        start("caricaRigheRaggruppamento");
        Set<RigaRaggruppamentoArticoli> result = null;
        try {
            result = magazzinoAnagraficaService.caricaRigheRaggruppamento(raggruppamentoArticoli);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaRigheRaggruppamento");
        }
        LOGGER.debug("--> Exit caricaRigheRaggruppamento ");
        return result;
    }

    @Override
    public List<RigaRaggruppamentoArticoli> caricaRigheRaggruppamentoArticoliByArticolo(int idArticolo) {
        LOGGER.debug("--> Enter caricaRigheRaggruppamentoArticoliByArticolo");
        start("caricaRigheRaggruppamentoArticoliByArticolo");
        List<RigaRaggruppamentoArticoli> result = null;
        try {
            result = magazzinoAnagraficaService.caricaRigheRaggruppamentoArticoliByArticolo(idArticolo);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaRigheRaggruppamentoArticoliByArticolo");
        }
        LOGGER.debug("--> Exit caricaRigheRaggruppamentoArticoliByArticolo ");
        return result;
    }

    @Override
    public List<Sconto> caricaSconti() {
        LOGGER.debug("--> Enter caricaSconti");
        start("caricaSconti");
        List<Sconto> list = new ArrayList<Sconto>();
        try {
            list = magazzinoAnagraficaService.caricaSconti();
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaSconti");
        }
        LOGGER.debug("--> Exit caricaSconti ");
        return list;
    }

    @Override
    public Sconto caricaSconto(Sconto sconto) {
        LOGGER.debug("--> Enter caricaSconto");
        start("caricaSconto");
        Sconto scontoCaricato = null;
        try {
            scontoCaricato = magazzinoAnagraficaService.caricaSconto(sconto);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaSconto");
        }
        LOGGER.debug("--> Exit caricaSconto ");
        return scontoCaricato;
    }

    @Override
    public SedeMagazzino caricaSedeMagazzinoBySedeEntita(SedeEntita sedeEntita, boolean ignoraEreditaDatiCommerciali) {
        LOGGER.debug("--> Enter caricaSedeMagazzinoBySedeEntita");
        start("caricaSedeMagazzinoBySedeEntita");
        SedeMagazzino sedeMagazzino = null;
        try {
            sedeMagazzino = magazzinoAnagraficaService.caricaSedeMagazzinoBySedeEntita(sedeEntita,
                    ignoraEreditaDatiCommerciali);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaSedeMagazzinoBySedeEntita");
        }
        LOGGER.debug("--> Exit caricaSedeMagazzinoBySedeEntita ");
        return sedeMagazzino;
    }

    @Override
    public SedeMagazzino caricaSedeMagazzinoPrincipale(Entita entita) {
        LOGGER.debug("--> Enter caricaSedeMagazzinoPrincipale");
        start("caricaSedeMagazzinoPrincipale");
        SedeMagazzino sedeMagazzino = null;
        try {
            sedeMagazzino = magazzinoAnagraficaService.caricaSedeMagazzinoPrincipale(entita);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaSedeMagazzinoPrincipale");
        }
        LOGGER.debug("--> Exit caricaSedeMagazzinoPrincipale ");
        return sedeMagazzino;
    }

    @Override
    public List<SedeMagazzino> caricaSediMagazzino(Map<String, Object> parametri, boolean textAsLike) {
        LOGGER.debug("--> Enter caricaSediMagazzino");
        start("caricaSediMagazzino");
        List<SedeMagazzino> sedi = null;
        try {
            sedi = magazzinoAnagraficaService.caricaSediMagazzino(parametri, textAsLike);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaSediMagazzino");
        }
        LOGGER.debug("--> Exit caricaSediMagazzino ");
        return sedi;
    }

    @Override
    public List<SedeMagazzinoLite> caricaSediMagazzinoByEntita(Entita entita) {
        LOGGER.debug("--> Enter caricaSediMagazzinoByEntita");
        start("caricaSediMagazzinoByEntita");
        List<SedeMagazzinoLite> result = new ArrayList<SedeMagazzinoLite>();
        try {
            result = magazzinoAnagraficaService.caricaSediMagazzinoByEntita(entita);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaSediMagazzinoByEntita");
        }
        LOGGER.debug("--> Exit caricaSediMagazzinoByEntita");
        return result;
    }

    @Override
    public List<RiepilogoSedeEntitaDTO> caricaSediMagazzinoByListino(Listino listino) {
        LOGGER.debug("--> Enter caricaSediMagazzinoByListino");
        start("caricaSediMagazzinoByListino");
        List<RiepilogoSedeEntitaDTO> result = null;
        try {
            result = listinoService.caricaSediMagazzinoByListino(listino);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaSediMagazzinoByListino");
        }
        LOGGER.debug("--> Exit caricaSediMagazzinoByListino ");
        return result;
    }

    @Override
    public List<SedeMagazzinoLite> caricaSediMagazzinoDiRifatturazione() {
        LOGGER.debug("--> Enter caricaSediMagazzinoDiRifatturazione");
        start("caricaSediMagazzinoDiRifatturazione");

        List<SedeMagazzinoLite> sediMagazzino = new ArrayList<SedeMagazzinoLite>();

        try {
            sediMagazzino = magazzinoAnagraficaService.caricaSediMagazzinoDiRifatturazione();
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaSediMagazzinoDiRifatturazione");
        }
        LOGGER.debug("--> Exit caricaSediMagazzinoDiRifatturazione ");
        return sediMagazzino;
    }

    @Override
    public List<SedeMagazzinoLite> caricaSediRifatturazioneAssociate() {
        LOGGER.debug("--> Enter caricaSediRifatturazioneAssociate");
        start("caricaSediRifatturazioneAssociate");

        List<SedeMagazzinoLite> sediMagazzino = new ArrayList<SedeMagazzinoLite>();
        try {
            sediMagazzino = magazzinoAnagraficaService.caricaSediRifatturazioneAssociate();
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaSediRifatturazioneAssociate");
        }
        LOGGER.debug("--> Exit caricaSediRifatturazioneAssociate ");
        return sediMagazzino;
    }

    @Override
    public List<SedeMagazzinoLite> caricaSediRifatturazioneNonAssociate(EntitaLite entita) {
        LOGGER.debug("--> Enter caricaSediRifatturazioneNonAssociate");
        start("caricaSediRifatturazioneNonAssociate");

        List<SedeMagazzinoLite> sediMagazzino = new ArrayList<SedeMagazzinoLite>();
        try {
            sediMagazzino = magazzinoAnagraficaService.caricaSediRifatturazioneNonAssociate(entita);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaSediRifatturazioneNonAssociate");
        }
        LOGGER.debug("--> Exit caricaSediRifatturazioneNonAssociate ");
        return sediMagazzino;
    }

    @Override
    public List<ScaglioneListinoStorico> caricaStoricoScaglione(ScaglioneListino scaglioneListino,
            Integer numeroVersione) {
        LOGGER.debug("--> Enter caricaStoricoScaglione");
        start("caricaStoricoScaglione");

        List<ScaglioneListinoStorico> storico = null;
        try {
            storico = listinoService.caricaStoricoScaglione(scaglioneListino, numeroVersione);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaStoricoScaglione");
        }
        LOGGER.debug("--> Exit caricaStoricoScaglione ");
        return storico;
    }

    @Override
    public TemplateSpedizioneMovimenti caricaTemplateSpedizioneMovimenti() {
        LOGGER.debug("--> Enter caricaTemplateSpedizioneMovimenti");
        start("caricaTemplateSpedizioneMovimenti");

        TemplateSpedizioneMovimenti template = null;
        try {
            template = magazzinoAnagraficaService.caricaTemplateSpedizioneMovimenti();
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaTemplateSpedizioneMovimenti");
        }
        LOGGER.debug("--> Exit caricaTemplateSpedizioneMovimenti ");
        return template;
    }

    @Override
    public List<TipoAttributo> caricaTipiAttributo() {
        LOGGER.debug("--> Enter caricaTipiAttributo");
        start("caricaTipiAttributo");
        List<TipoAttributo> listResult = new ArrayList<TipoAttributo>();
        try {
            listResult = magazzinoAnagraficaService.caricaTipiAttributo();
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaTipiAttributo");
        }
        LOGGER.debug("--> Exit caricaTipiAttributo ");
        return listResult;
    }

    @Override
    public List<TipoDocumentoBaseMagazzino> caricaTipiDocumentoBase() {
        LOGGER.debug("--> Enter caricaTipiDocumentoBase");
        start("caricaTipiDocumentoBase");
        List<TipoDocumentoBaseMagazzino> tipi = new ArrayList<TipoDocumentoBaseMagazzino>();
        try {
            tipi = magazzinoAnagraficaService.caricaTipiDocumentoBase();
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaTipiDocumentoBase");
        }
        LOGGER.debug("--> Exit caricaTipiDocumentoBase ");
        return tipi;
    }

    @Override
    public List<TipoEsportazione> caricaTipiEsportazione(String nome) {
        LOGGER.debug("--> Enter caricaTipiEsportazione");
        start("caricaTipiEsportazione");
        List<TipoEsportazione> tipiEsportazione = null;
        try {
            tipiEsportazione = magazzinoAnagraficaService.caricaTipiEsportazione(nome);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaTipiEsportazione");
        }
        LOGGER.debug("--> Exit caricaTipiEsportazione ");
        return tipiEsportazione;
    }

    @Override
    public List<TipoMezzoTrasporto> caricaTipiMezzoTrasporto() {
        LOGGER.debug("--> Enter caricaTipiMezzoTrasporto");
        start("caricaTipiMezzoTrasporto");
        List<TipoMezzoTrasporto> list = new ArrayList<TipoMezzoTrasporto>();
        try {
            list = magazzinoAnagraficaService.caricaTipiMezzoTrasporto();
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaTipiMezzoTrasporto");
        }
        LOGGER.debug("--> Exit caricaTipiMezzoTrasporto ");
        return list;
    }

    @Override
    public List<TipoPorto> caricaTipiPorto(String descrizione) {
        LOGGER.debug("--> Enter caricaTipiPorto");
        start("caricaTipiPorto");
        List<TipoPorto> list = new ArrayList<TipoPorto>();
        try {
            list = magazzinoAnagraficaService.caricaTipiPorto(descrizione);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaTipiPorto");
        }
        LOGGER.debug("--> Exit caricaTipiPorto ");
        return list;
    }

    @Override
    public TipoAttributo caricaTipoAttributo(TipoAttributo tipoAttributo) {
        LOGGER.debug("--> Enter caricaTipoAttributo");
        start("caricaTipoAttributo");
        TipoAttributo tipoAttributoCaricato = null;
        try {
            tipoAttributoCaricato = magazzinoAnagraficaService.caricaTipoAttributo(tipoAttributo);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaTipoAttributo");
        }
        LOGGER.debug("--> Exit caricaTipoAttributo ");
        return tipoAttributoCaricato;
    }

    @Override
    public TipoEsportazione caricaTipoEsportazione(Integer idTipoEsportazione, boolean loadLazy) {
        LOGGER.debug("--> Enter caricaTipoEsportazione");
        start("caricaTipoEsportazione");
        TipoEsportazione tipoEsportazione = null;
        try {
            tipoEsportazione = magazzinoAnagraficaService.caricaTipoEsportazione(idTipoEsportazione, loadLazy);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaTipoEsportazione");
        }
        LOGGER.debug("--> Exit caricaTipoEsportazione ");
        return tipoEsportazione;
    }

    @Override
    public TipoVariante caricaTipoVariante(TipoVariante tipoVariante) {
        LOGGER.debug("--> Enter caricaTipoVariante");
        start("caricaTipoVariante");
        TipoVariante tipoVarianteCaricata = null;
        try {
            tipoVarianteCaricata = magazzinoAnagraficaService.caricaTipoVariante(tipoVariante);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaTipoVariante");
        }
        LOGGER.debug("--> Exit caricaTipoVariante ");
        return tipoVarianteCaricata;
    }

    @Override
    public List<TrasportoCura> caricaTrasportiCura(String descrizione) {
        LOGGER.debug("--> Enter caricaTrasportiCura");
        start("caricaTrasportiCura");
        List<TrasportoCura> list = new ArrayList<TrasportoCura>();
        try {
            list = magazzinoAnagraficaService.caricaTrasportiCura(descrizione);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaTrasportiCura");
        }
        LOGGER.debug("--> Exit caricaTrasportiCura ");
        return list;
    }

    @Override
    public VersioneListino caricaVersioneListino(VersioneListino versioneListino, boolean initializeLazy) {
        LOGGER.debug("--> Enter caricaVersioneListino");
        start("caricaVersioneListino");
        VersioneListino versioneListinoCaricata = null;
        try {
            versioneListinoCaricata = listinoService.caricaVersioneListino(versioneListino, initializeLazy);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaVersioneListino");
        }
        LOGGER.debug("--> Exit caricaVersioneListino ");
        return versioneListinoCaricata;
    }

    @Override
    public VersioneListino caricaVersioneListinoByData(Listino listino, Date data) {
        LOGGER.debug("--> Enter caricaVersioneListinoByData");
        start("caricaVersioneListinoByData");
        VersioneListino versioneListino = null;
        try {
            versioneListino = listinoService.caricaVersioneListinoByData(listino, data);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaVersioneListinoByData");
        }
        LOGGER.debug("--> Exit caricaVersioneListinoByData ");
        return versioneListino;
    }

    @Override
    public List<VersioneListino> caricaVersioniListino(String fieldSearch, String valueSearch,
            ETipoListino tipoListino) {
        LOGGER.debug("--> Enter caricaVersioniListino");
        start("caricaVersioniListino");
        List<VersioneListino> list = new ArrayList<VersioneListino>();
        try {
            list = listinoService.caricaVersioniListino(fieldSearch, valueSearch, tipoListino);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaVersioniListino");
        }
        LOGGER.debug("--> Exit caricaVersioniListino ");
        return list;
    }

    @Override
    public Articolo cloneArticolo(int idArticolo, String nuovoCodice, String nuovaDescrizione, boolean copyDistinta,
            List<AttributoArticolo> attributi, boolean copyListino, boolean azzeraPrezziListino) {
        LOGGER.debug("--> Enter cloneArticolo");
        start("cloneArticolo");
        Articolo result = null;
        try {
            result = magazzinoAnagraficaService.cloneArticolo(idArticolo, nuovoCodice, nuovaDescrizione, copyDistinta,
                    true, attributi, copyListino, azzeraPrezziListino);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cloneArticolo");
        }
        LOGGER.debug("--> Exit cloneArticolo ");
        return result;
    }

    @Override
    public VersioneListino copiaVersioneListino(VersioneListino versioneListino, Date dataNuovaVersioneListino) {
        LOGGER.debug("--> Enter copiaVersioneListino");
        start("copiaVersioneListino");
        VersioneListino versioneListinoCopiata = null;
        try {
            versioneListinoCopiata = listinoService.copiaVersioneListino(versioneListino, dataNuovaVersioneListino);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("copiaVersioneListino");
        }
        LOGGER.debug("--> Exit copiaVersioneListino ");
        return versioneListinoCopiata;
    }

    @Override
    public Articolo creaArticolo(Categoria categoria) {
        LOGGER.debug("--> Enter creaArticolo");
        start("creaArticolo");
        Articolo articolo = null;
        try {
            articolo = this.magazzinoAnagraficaService.creaArticolo(categoria);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("creaArticolo");
        }
        LOGGER.debug("--> Exit creaArticolo ");
        return articolo;
    }

    @Override
    public Categoria creaCategoria(Integer idCategoriaPadre) {
        LOGGER.debug("--> Enter creaCategoria");
        start("creaCategoria");
        Categoria categoriaCreata = null;
        try {
            categoriaCreata = magazzinoAnagraficaService.creaCategoria(idCategoriaPadre);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("creaCategoria");
        }
        LOGGER.debug("--> Exit creaCategoria ");
        return categoriaCreata;
    }

    @Override
    public MezzoTrasporto creaNuovoDepositoMezzoDiTrasporto(MezzoTrasporto mezzoTrasporto, String codiceDeposito,
            String descrizioneDeposito) {
        LOGGER.debug("--> Enter creaNuovoDepositoMezzoDiTrasporto");
        start("creaNuovoDepositoMezzoDiTrasporto");

        MezzoTrasporto mezzoTrasportoConDeposito = null;
        try {
            mezzoTrasportoConDeposito = magazzinoAnagraficaService.creaNuovoDepositoMezzoDiTrasporto(mezzoTrasporto,
                    codiceDeposito, descrizioneDeposito);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("creaNuovoDepositoMezzoDiTrasporto");
        }
        LOGGER.debug("--> Exit creaNuovoDepositoMezzoDiTrasporto ");
        return mezzoTrasportoConDeposito;
    }

    @Override
    public void inserisciRigheRicercaManutenzioneListino(
            ParametriRicercaManutenzioneListino parametriRicercaManutenzioneListino)
                    throws ListinoManutenzioneNonValidoException {
        LOGGER.debug("--> Enter salvaParametriRicercaManutenzioneListino");
        start("salvaParametriRicercaManutenzioneListino");
        try {
            listinoService.inserisciRigheRicercaManutenzioneListino(parametriRicercaManutenzioneListino);
        } catch (ListinoManutenzioneNonValidoException e) {
            throw e;
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaParametriRicercaManutenzioneListino");
        }
        LOGGER.debug("--> Exit salvaParametriRicercaManutenzioneListino ");
    }

    @Override
    public List<ArticoloRicerca> ricercaArticoli(ParametriRicercaArticolo parametriRicercaArticolo) {
        LOGGER.debug("--> Enter ricercaArticoli");
        start("ricercaArticoli");
        List<ArticoloRicerca> list = new ArrayList<ArticoloRicerca>();
        try {
            list = magazzinoAnagraficaService.ricercaArticoli(parametriRicercaArticolo);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("ricercaArticoli");
        }
        LOGGER.debug("--> Exit ricercaArticoli ");
        return list;
    }

    @Override
    public List<ArticoloRicerca> ricercaArticoliSearchObject(ParametriRicercaArticolo parametriRicercaArticolo) {
        LOGGER.debug("--> Enter ricercaArticoli");
        start("ricercaArticoli");
        List<ArticoloRicerca> articoli = null;
        try {
            articoli = magazzinoAnagraficaService.ricercaArticoliSearchObject(parametriRicercaArticolo);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("ricercaArticoli");
        }
        LOGGER.debug("--> Exit ricercaArticoli ");
        return articoli;
    }

    @Override
    public List<Sconto> ricercaSconti(String codiceSconto) {
        LOGGER.debug("--> Enter ricercaSconti");
        start("ricercaSconti");
        List<Sconto> list = new ArrayList<Sconto>();
        try {
            list = magazzinoAnagraficaService.ricercaSconti(codiceSconto);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("ricercaSconti");
        }
        LOGGER.debug("--> Exit ricercaSconti ");
        return list;
    }

    @Override
    public MezzoTrasporto rimuoviDepositoDaMezzoDiTrasporto(MezzoTrasporto mezzoTrasporto) {
        LOGGER.debug("--> Enter rimuoviDepositoDaMezzoDiTrasporto");
        start("rimuoviDepositoDaMezzoDiTrasporto");

        MezzoTrasporto mezzoTrasportoEdit = null;
        try {
            mezzoTrasportoEdit = magazzinoAnagraficaService.rimuoviDepositoDaMezzoDiTrasporto(mezzoTrasporto);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("rimuoviDepositoDaMezzoDiTrasporto");
        }
        LOGGER.debug("--> Exit rimuoviDepositoDaMezzoDiTrasporto ");
        return mezzoTrasportoEdit;
    }

    @Override
    public void rimuoviReferenzaCircolare(ArticoloLite articolo) {
        LOGGER.debug("--> Enter rimuoviReferenzaCircolare");
        start("rimuoviReferenzaCircolare");
        try {
            magazzinoAnagraficaService.rimuoviReferenzaCircolare(articolo);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("rimuoviReferenzaCircolare");
        }
        LOGGER.debug("--> Exit rimuoviReferenzaCircolare ");
    }

    @Override
    public void rimuoviSedePerRifatturazione(SedeMagazzinoLite sedeMagazzino) {
        LOGGER.debug("--> Enter rimuoviSedePerRifatturazione");
        start("rimuoviSedePerRifatturazione");
        try {
            magazzinoAnagraficaService.rimuoviSedePerRifatturazione(sedeMagazzino);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("rimuoviSedePerRifatturazione");
        }
        LOGGER.debug("--> Exit rimuoviSedePerRifatturazione ");
    }

    @Override
    public Articolo salvaArticolo(Articolo articolo) {
        LOGGER.debug("--> Enter salvaArticolo");
        start("salvaArticolo");
        Articolo articoloSave = null;
        try {
            articoloSave = magazzinoAnagraficaService.salvaArticolo(articolo);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaArticolo");
        }
        LOGGER.debug("--> Exit salvaArticolo ");
        return articoloSave;
    }

    @Override
    public ArticoloAlternativo salvaArticoloAlternativo(ArticoloAlternativo articoloAlternativo) {
        LOGGER.debug("--> Enter salvaArticoloAlternativo");
        start("salvaArticoloAlternativo");
        ArticoloAlternativo articoloSave = null;
        try {
            articoloSave = magazzinoAnagraficaService.salvaArticoloAlternativo(articoloAlternativo);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaArticoloAlternativo");
        }
        LOGGER.debug("--> Exit salvaArticoloAlternativo ");
        return articoloSave;
    }

    @Override
    public ArticoloDeposito salvaArticoloDeposito(ArticoloDeposito articoloDeposito) {
        LOGGER.debug("--> Enter salvaArticoloDeposito");
        start("salvaArticoloDeposito");
        ArticoloDeposito articoloDepositoSalvato = null;
        try {
            articoloDepositoSalvato = magazzinoAnagraficaService.salvaArticoloDeposito(articoloDeposito);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaArticoloDeposito");
        }
        LOGGER.debug("--> Exit salvaArticoloDeposito");
        return articoloDepositoSalvato;
    }

    @Override
    public AspettoEsteriore salvaAspettoEsteriore(AspettoEsteriore aspettoEsteriore) {
        LOGGER.debug("--> Enter salvaAspettoEsteriore");
        start("salvaAspettoEsteriore");
        AspettoEsteriore aspettoEsterioreSalvato = null;
        try {
            aspettoEsterioreSalvato = magazzinoAnagraficaService.salvaAspettoEsteriore(aspettoEsteriore);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaAspettoEsteriore");
        }
        LOGGER.debug("--> Exit salvaAspettoEsteriore ");
        return aspettoEsterioreSalvato;
    }

    @Override
    public AttributoArticolo salvaAttributoArticolo(AttributoArticolo attributoArticolo) {
        LOGGER.debug("--> Enter salvaAttributoArticolo");
        start("salvaAttributoArticolo");
        AttributoArticolo attributoArticoloSalvato = null;
        try {
            attributoArticoloSalvato = magazzinoAnagraficaService.salvaAttributoArticolo(attributoArticolo);
        } catch (Exception e1) {
            PanjeaSwingUtil.checkAndThrowException(e1);
        } finally {
            end("salvaAttributoArticolo");
        }
        LOGGER.debug("--> Exit salvaAttributoArticolo ");
        return attributoArticoloSalvato;
    }

    @Override
    public Categoria salvaCategoria(Categoria categoria) {
        LOGGER.debug("--> Enter salvaCategoria");
        start("salvaCategoria");
        Categoria categoriaSalvata = null;
        try {
            categoriaSalvata = magazzinoAnagraficaService.salvaCategoria(categoria);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaCategoria");
        }
        LOGGER.debug("--> Exit salvaCategoria ");
        return categoriaSalvata;
    }

    @Override
    public CategoriaCommercialeArticolo salvaCategoriaCommercialeArticolo(
            CategoriaCommercialeArticolo categoriaCommercialeArticolo) {
        LOGGER.debug("--> Enter salvaCategoriaCommercialeArticolo");
        start("salvaCategoriaCommercialeArticolo");
        CategoriaCommercialeArticolo result = null;
        try {
            result = magazzinoAnagraficaService.salvaCategoriaCommercialeArticolo(categoriaCommercialeArticolo);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaCategoriaCommercialeArticolo");
        }
        LOGGER.debug("--> Exit salvaCategoriaCommercialeArticolo ");
        return result;
    }

    @Override
    public CategoriaSedeMagazzino salvaCategoriaSedeMagazzino(CategoriaSedeMagazzino categoriaSedeMagazzino) {
        LOGGER.debug("--> Enter salvaCategoriaSedeMagazzino");
        start("salvaCategoriaSedeMagazzino");
        CategoriaSedeMagazzino categoriaSedeMagazzinoSalvata = null;
        try {
            categoriaSedeMagazzinoSalvata = magazzinoAnagraficaService
                    .salvaCategoriaSedeMagazzino(categoriaSedeMagazzino);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaCategoriaSedeMagazzino");
        }
        LOGGER.debug("--> Exit salvaCategoriaSedeMagazzino ");
        return categoriaSedeMagazzinoSalvata;
    }

    @Override
    public CausaleTrasporto salvaCausaleTraporto(CausaleTrasporto causaleTrasporto) {
        LOGGER.debug("--> Enter salvaCausaleTraporto");
        start("salvaCausaleTraporto");
        CausaleTrasporto causaleTrasportoSalvata = null;
        try {
            causaleTrasportoSalvata = magazzinoAnagraficaService.salvaCausaleTraporto(causaleTrasporto);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaCausaleTraporto");
        }
        LOGGER.debug("--> Exit salvaCausaleTraporto ");
        return causaleTrasportoSalvata;
    }

    @Override
    public CodiceArticoloEntita salvaCodiceArticoloEntita(CodiceArticoloEntita codiceArticoloEntita) {
        LOGGER.debug("--> Enter salvaCodiceArticoloEntita");
        start("salvaCodiceArticoloEntita");
        CodiceArticoloEntita codiceArticoloEntitaSalvata = null;
        try {
            codiceArticoloEntitaSalvata = magazzinoAnagraficaService.salvaCodiceArticoloEntita(codiceArticoloEntita);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaCodiceArticoloEntita");
        }
        LOGGER.debug("--> Exit salvaCodiceArticoloEntita");
        return codiceArticoloEntitaSalvata;
    }

    @Override
    public Componente salvaComponente(Componente componente) {
        LOGGER.debug("--> Enter salvaComponente");
        start("salvaComponente");
        Componente componenteSalvato = null;
        try {
            componenteSalvato = magazzinoAnagraficaService.salvaComponente(componente);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaComponente");
        }
        LOGGER.debug("--> Exit salvaComponente");
        return componenteSalvato;
    }

    @Override
    public ConfigurazioneDistinta salvaConfigurazioneDistinta(ConfigurazioneDistinta configurazioneDistinta) {
        LOGGER.debug("--> Enter salvaConfigurazioneDistinta");
        start("salvaConfigurazioneDistinta");
        ConfigurazioneDistinta result = null;
        try {
            result = magazzinoAnagraficaService.salvaConfigurazioneDistinta(configurazioneDistinta);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaConfigurazioneDistinta");
        }
        LOGGER.debug("--> Exit salvaConfigurazioneDistinta ");
        return result;
    }

    @Override
    public DepositoMagazzino salvaDepositoMagazzino(DepositoMagazzino depositoMagazzino) {
        LOGGER.debug("--> Enter salvaDepositoMagazzino");
        start("salvaDepositoMagazzino");
        DepositoMagazzino depositoMagazzinoSalvato = null;
        try {
            depositoMagazzinoSalvato = magazzinoAnagraficaService.salvaDepositoMagazzino(depositoMagazzino);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaDepositoMagazzino");
        }
        LOGGER.debug("--> Exit salvaDepositoMagazzino ");
        return depositoMagazzinoSalvato;
    }

    @Override
    public void salvaDistintaArticolo(ArticoloLite articoloLite, Set<Componente> distinte, Set<Componente> componenti,
            Set<FaseLavorazioneArticolo> fasiLavorazioni) {
        LOGGER.debug("--> Enter salvaComponentiArticolo");
        start("salvaComponentiArticolo");
        try {
            magazzinoAnagraficaService.salvaDistintaArticolo(articoloLite, distinte, componenti, fasiLavorazioni);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaComponentiArticolo");
        }
        LOGGER.debug("--> Exit salvaComponentiArticolo ");
    }

    @Override
    public EntitaTipoEsportazione salvaEntitaTipoEsportazione(EntitaTipoEsportazione entitaTipoEsportazione) {
        LOGGER.debug("--> Enter salvaEntitaTipoEsportazione");
        start("salvaEntitaTipoEsportazione");
        EntitaTipoEsportazione entitaTipoEsportazioneSalvato = null;
        try {
            entitaTipoEsportazioneSalvato = magazzinoAnagraficaService
                    .salvaEntitaTipoEsportazione(entitaTipoEsportazione);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaEntitaTipoEsportazione");
        }
        LOGGER.debug("--> Exit salvaEntitaTipoEsportazione ");
        return entitaTipoEsportazioneSalvato;
    }

    @Override
    public FaseLavorazione salvaFaseLavorazione(FaseLavorazione faseLavorazione) {
        LOGGER.debug("--> Enter salvaFaseLavorazione");
        start("salvaFaseLavorazione");
        FaseLavorazione faseLavorazioneSalvata = null;
        try {
            faseLavorazioneSalvata = magazzinoAnagraficaService.salvaFaseLavorazione(faseLavorazione);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaFaseLavorazione");
        }
        LOGGER.debug("--> Exit salvaFaseLavorazione");
        return faseLavorazioneSalvata;
    }

    @Override
    public FaseLavorazioneArticolo salvaFaseLavorazioneArticolo(FaseLavorazioneArticolo faseLavorazioneArticolo) {
        LOGGER.debug("--> Enter salvaFaseLavorazioneArticolo");
        start("salvaFaseLavorazioneArticolo");
        FaseLavorazioneArticolo faseLavorazioneArticoloSalvato = null;
        try {
            faseLavorazioneArticoloSalvato = magazzinoAnagraficaService
                    .salvaFaseLavorazioneArticolo(faseLavorazioneArticolo);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaFaseLavorazioneArticolo");
        }
        LOGGER.debug("--> Exit salvaFaseLavorazioneArticolo");
        return faseLavorazioneArticoloSalvato;
    }

    @Override
    public FormulaTrasformazione salvaFormulaTrasformazione(FormulaTrasformazione formulaTrasformazione) {
        LOGGER.debug("--> Enter salvaFormulaTrasformazione");
        start("salvaFormulaTrasformazione");
        FormulaTrasformazione formulaTrasformazioneSave = null;
        try {
            formulaTrasformazioneSave = magazzinoAnagraficaService.salvaFormulaTrasformazione(formulaTrasformazione);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaFormulaTrasformazione");
        }
        LOGGER.debug("--> Exit salvaFormulaTrasformazione ");
        return formulaTrasformazioneSave;
    }

    @Override
    public Listino salvaListino(Listino listino) {
        LOGGER.debug("--> Enter salvaListino");
        start("salvaListino");
        try {
            listino = listinoService.salvaListino(listino);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaListino");
        }
        LOGGER.debug("--> Exit salvaListino ");
        return listino;
    }

    @Override
    public ListinoTipoMezzoZonaGeografica salvaListinoTipoMezzoZonaGeografica(
            ListinoTipoMezzoZonaGeografica listinoTipoMezzoZonaGeografica) {
        LOGGER.debug("--> Enter salvaListinoTipoMezzoZonaGeografica");
        start("salvaListinoTipoMezzoZonaGeografica");
        ListinoTipoMezzoZonaGeografica listinoTipoMezzoZonaGeograficaSalvato = null;
        try {
            listinoTipoMezzoZonaGeograficaSalvato = listinoTipoMezzoZonaGeograficaService
                    .salvaListinoTipoMezzoZonaGeografica(listinoTipoMezzoZonaGeografica);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaListinoTipoMezzoZonaGeografica");
        }
        LOGGER.debug("--> Exit salvaListinoTipoMezzoZonaGeografica ");
        return listinoTipoMezzoZonaGeograficaSalvato;
    }

    @Override
    public MagazzinoSettings salvaMagazzinoSettings(MagazzinoSettings magazzinoSettings) {
        LOGGER.debug("--> Enter salvaMagazzinoSettings");
        start("salvaMagazzinoSettings");
        MagazzinoSettings magazzinoSettingsSalvato = magazzinoAnagraficaService
                .salvaMagazzinoSettings(magazzinoSettings);
        end("salvaMagazzinoSettings");
        LOGGER.debug("--> Exit salvaMagazzinoSettings");
        return magazzinoSettingsSalvato;
    }

    @Override
    public MezzoTrasporto salvaMezzoTrasporto(MezzoTrasporto mezzoTrasporto) {
        LOGGER.debug("--> Enter salvaMezzoTrasporto");
        start("salvaMezzoTrasporto");
        MezzoTrasporto mezzoTrasportoSalvato = null;
        try {
            mezzoTrasportoSalvato = magazzinoAnagraficaService.salvaMezzoTrasporto(mezzoTrasporto);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaMezzoTrasporto");
        }
        LOGGER.debug("--> Exit salvaMezzoTrasporto");
        return mezzoTrasportoSalvato;
    }

    @Override
    public Omaggio salvaOmaggio(Omaggio omaggio) {
        LOGGER.debug("--> Enter salvaOmaggio");
        start("salvaOmaggio");
        Omaggio omaggioSalvato = null;
        try {
            omaggioSalvato = magazzinoAnagraficaService.salvaOmaggio(omaggio);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaOmaggio");
        }
        LOGGER.debug("--> Exit salvaOmaggio");
        return omaggioSalvato;
    }

    @Override
    public List<RigaListino> salvaPrezzoRigheListino(List<RigaListino> listRigheListino)
            throws RigheListinoListiniCollegatiException {
        LOGGER.debug("--> Enter salvaRigheListino");
        start("salvaRigheListino");
        List<RigaListino> listRigheSalvate = new ArrayList<RigaListino>();
        try {
            listRigheSalvate = listinoService.salvaPrezzoRigheListino(listRigheListino);
        } catch (RigheListinoListiniCollegatiException e) {
            throw e;
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaRigheListino");
        }
        LOGGER.debug("--> Exit salvaRigheListino ");
        return listRigheSalvate;
    }

    @Override
    public List<RigaListino> salvaPrezzoRigheListino(List<RigaListino> listRigheListino,
            boolean aggiornaListiniCollegati) {
        LOGGER.debug("--> Enter salvaRigheListino");
        start("salvaRigheListino");
        List<RigaListino> listRigheSalvate = new ArrayList<RigaListino>();
        try {
            listRigheSalvate = listinoService.salvaPrezzoRigheListino(listRigheListino, aggiornaListiniCollegati);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaRigheListino");
        }
        LOGGER.debug("--> Exit salvaRigheListino ");
        return listRigheSalvate;
    }

    @Override
    public RaggruppamentoArticoli salvaRaggruppamento(RaggruppamentoArticoli raggruppamentoArticoli) {
        LOGGER.debug("--> Enter salvaRaggruppamento");
        start("salvaRaggruppamento");
        RaggruppamentoArticoli result = null;
        try {
            result = magazzinoAnagraficaService.salvaRaggruppamento(raggruppamentoArticoli);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaRaggruppamento");
        }
        LOGGER.debug("--> Exit salvaRaggruppamento ");
        return result;
    }

    @Override
    public RigaListino salvaRigaListino(RigaListino rigaListino) throws RigaListinoListiniCollegatiException {
        LOGGER.debug("--> Enter salvaRigaListino");
        start("salvaRigaListino");
        try {
            rigaListino = listinoService.salvaRigaListino(rigaListino);
        } catch (RigaListinoListiniCollegatiException e) {
            throw e;
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaRigaListino");
        }
        LOGGER.debug("--> Exit salvaRigaListino ");
        return rigaListino;
    }

    @Override
    public RigaListino salvaRigaListino(RigaListino rigaListino, boolean aggiornaListiniCollegati) {
        LOGGER.debug("--> Enter salvaRigaListino");
        start("salvaRigaListino");
        try {
            rigaListino = listinoService.salvaRigaListino(rigaListino, aggiornaListiniCollegati);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaRigaListino");
        }
        LOGGER.debug("--> Exit salvaRigaListino ");
        return rigaListino;
    }

    @Override
    public List<RigaManutenzioneListino> salvaRigaManutenzioneListino(RigaManutenzioneListino rigaManutenzioneListino) {
        LOGGER.debug("--> Enter salvaRigaManutenzioneListino");
        start("salvaRigaManutenzioneListino");
        List<RigaManutenzioneListino> righe = null;
        try {
            righe = listinoService.salvaRigaManutenzioneListino(rigaManutenzioneListino);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaRigaManutenzioneListino");
        }
        LOGGER.debug("--> Exit salvaRigaManutenzioneListino ");
        return righe;
    }

    @Override
    public RigaRaggruppamentoArticoli salvaRigaRaggruppamentoArticoli(
            RigaRaggruppamentoArticoli rigaRaggruppamentoArticoli) {
        LOGGER.debug("--> Enter salvaRigaRaggruppamentoArticoli");
        start("salvaRigaRaggruppamentoArticoli");
        RigaRaggruppamentoArticoli result = null;
        try {
            result = magazzinoAnagraficaService.salvaRigaRaggruppamentoArticoli(rigaRaggruppamentoArticoli);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaRigaRaggruppamentoArticoli");
        }
        LOGGER.debug("--> Exit salvaRigaRaggruppamentoArticoli ");
        return result;
    }

    @Override
    public Sconto salvaSconto(Sconto sconto) {
        LOGGER.debug("--> Enter salvaSconto");
        start("salvaSconto");
        Sconto scontoSalvato = null;
        try {
            scontoSalvato = magazzinoAnagraficaService.salvaSconto(sconto);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaSconto");
        }
        LOGGER.debug("--> Exit salvaSconto ");
        return scontoSalvato;
    }

    @Override
    public SedeMagazzino salvaSedeMagazzino(SedeMagazzino sedeMagazzino) {
        LOGGER.debug("--> Enter salvaSedeMagazzino");
        start("salvaSedeMagazzino");
        SedeMagazzino sedeMagazzinoSalvata = null;
        try {
            sedeMagazzinoSalvata = magazzinoAnagraficaService.salvaSedeMagazzino(sedeMagazzino);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaSedeMagazzino");
        }
        LOGGER.debug("--> Exit salvaSedeMagazzino ");
        return sedeMagazzinoSalvata;
    }

    @Override
    public List<SedeMagazzino> salvaSediMagazzino(List<SedeMagazzino> sedi) {
        LOGGER.debug("--> Enter salvaSediMagazzino");
        start("salvaSediMagazzino");
        List<SedeMagazzino> sediSalvate = null;
        try {
            sediSalvate = magazzinoAnagraficaService.salvaSediMagazzino(sedi);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaSediMagazzino");
        }
        LOGGER.debug("--> Exit salvaSediMagazzino ");
        return sediSalvate;
    }

    @Override
    public TemplateSpedizioneMovimenti salvaTemplateSpedizioneMovimenti(
            TemplateSpedizioneMovimenti templateSpedizioneMovimenti) {
        LOGGER.debug("--> Enter salvaTemplateSpedizioneMovimenti");
        start("salvaTemplateSpedizioneMovimenti");

        TemplateSpedizioneMovimenti templateSalvato = null;
        try {
            templateSalvato = magazzinoAnagraficaService.salvaTemplateSpedizioneMovimenti(templateSpedizioneMovimenti);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaTemplateSpedizioneMovimenti");
        }
        LOGGER.debug("--> Exit salvaTemplateSpedizioneMovimenti ");
        return templateSalvato;
    }

    @Override
    public TipoAttributo salvaTipoAttributo(TipoAttributo tipoAttributo) {
        LOGGER.debug("--> Enter salvaTipoAttributo");
        start("salvaTipoAttributo");
        TipoAttributo tipoAttributoSalvato = null;
        try {
            tipoAttributoSalvato = magazzinoAnagraficaService.salvaTipoAttributo(tipoAttributo);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaTipoAttributo");
        }
        LOGGER.debug("--> Exit salvaTipoAttributo ");
        return tipoAttributoSalvato;
    }

    @Override
    public TipoDocumentoBaseMagazzino salvaTipoDocumentoBase(TipoDocumentoBaseMagazzino tipoDocumentoBase) {
        LOGGER.debug("--> Enter salvaTipoDocumentoBase");
        start("salvaTipoDocumentoBase");
        TipoDocumentoBaseMagazzino tipoDocumentoBaseMagazzino = null;
        try {
            tipoDocumentoBaseMagazzino = magazzinoAnagraficaService.salvaTipoDocumentoBase(tipoDocumentoBase);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaTipoDocumentoBase");
        }
        LOGGER.debug("--> Exit salvaTipoDocumentoBase ");
        return tipoDocumentoBaseMagazzino;
    }

    @Override
    public TipoEsportazione salvaTipoEsportazione(TipoEsportazione tipoEsportazione) {
        LOGGER.debug("--> Enter salvaTipoEsportazione");
        start("salvaTipoEsportazione");
        TipoEsportazione tipoEsportazioneSalvata = null;
        try {
            tipoEsportazioneSalvata = magazzinoAnagraficaService.salvaTipoEsportazione(tipoEsportazione);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaTipoEsportazione");
        }
        LOGGER.debug("--> Exit salvaTipoEsportazione ");
        return tipoEsportazioneSalvata;
    }

    @Override
    public TipoMezzoTrasporto salvaTipoMezzoTrasporto(TipoMezzoTrasporto tipoMezzoTrasporto) {
        LOGGER.debug("--> Enter salvaTipoMezzoTrasporto");
        start("salvaTipoMezzoTrasporto");
        TipoMezzoTrasporto tipoMezzoTrasportoSalvato = null;
        try {
            tipoMezzoTrasportoSalvato = magazzinoAnagraficaService.salvaTipoMezzoTrasporto(tipoMezzoTrasporto);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaTipoMezzoTrasporto");
        }
        LOGGER.debug("--> Exit salvaTipoMezzoTrasporto");
        return tipoMezzoTrasportoSalvato;
    }

    @Override
    public TipoPorto salvaTipoPorto(TipoPorto tipoPorto) {
        LOGGER.debug("--> Enter salvaTipoPorto");
        start("salvaTipoPorto");
        TipoPorto tipoPortoSalvato = null;
        try {
            tipoPortoSalvato = magazzinoAnagraficaService.salvaTipoPorto(tipoPorto);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaTipoPorto");
        }
        LOGGER.debug("--> Exit salvaTipoPorto ");
        return tipoPortoSalvato;
    }

    @Override
    public TrasportoCura salvaTrasportoCura(TrasportoCura trasportoCura) {
        LOGGER.debug("--> Enter salvaTrasportoCura");
        start("salvaTrasportoCura");
        TrasportoCura trasportoCuraSalvato = null;
        try {
            trasportoCuraSalvato = magazzinoAnagraficaService.salvaTrasportoCura(trasportoCura);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaTrasportoCura");
        }
        LOGGER.debug("--> Exit salvaTrasportoCura ");
        return trasportoCuraSalvato;
    }

    @Override
    public VersioneListino salvaVersioneListino(VersioneListino versioneListino) {
        LOGGER.debug("--> Enter salvaVersioneListino");
        start("salvaVersioneListino");
        VersioneListino versioneListinoSalvata = null;
        try {
            versioneListinoSalvata = listinoService.salvaVersioneListino(versioneListino);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaVersioneListino");
        }
        LOGGER.debug("--> Exit salvaVersioneListino ");
        return versioneListinoSalvata;
    }

    /**
     * @param dataWarehouseService
     *            the dataWarehouseService to set
     */
    public void setDataWarehouseService(DataWarehouseService dataWarehouseService) {
        this.dataWarehouseService = dataWarehouseService;
    }

    /**
     * @param listinoService
     *            the listinoService to set
     */
    public void setListinoService(ListinoService listinoService) {
        this.listinoService = listinoService;
    }

    /**
     * @param listinoTipoMezzoZonaGeograficaService
     *            the listinoTipoMezzoZonaGeograficaService to set
     */
    public void setListinoTipoMezzoZonaGeograficaService(
            ListinoTipoMezzoZonaGeograficaService listinoTipoMezzoZonaGeograficaService) {
        this.listinoTipoMezzoZonaGeograficaService = listinoTipoMezzoZonaGeograficaService;
    }

    /**
     * @param magazzinoAnagraficaService
     *            the magazzinoAnagraficaService to set
     */
    public void setMagazzinoAnagraficaService(MagazzinoAnagraficaService magazzinoAnagraficaService) {
        this.magazzinoAnagraficaService = magazzinoAnagraficaService;
    }

    @Override
    public void sincronizzaAnagrafiche() {
        LOGGER.debug("--> Enter sincronizzaAnagrafiche");
        start("sincronizzaAnagrafiche");
        try {
            dataWarehouseService.sincronizzaAnagrafiche();
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("sincronizzaAnagrafiche");
        }
        LOGGER.debug("--> Exit sincronizzaAnagrafiche ");
    }

    @Override
    public void sincronizzaDimensionedata() {
        LOGGER.debug("--> Enter sincronizzaDimensionedata");
        start("sincronizzaDimensionedata");
        try {
            dataWarehouseService.sincronizzaDimensionedata();
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("sincronizzaDimensionedata");
        }
        LOGGER.debug("--> Exit sincronizzaDimensionedata ");
    }

    @Override
    public void sincronizzaDMS() {
        LOGGER.debug("--> Enter sincronizzaDMS");
        start("sincronizzaDMS");
        try {
            dataWarehouseService.sincronizzaDMS();
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("sincronizzaDMS");
        }
        LOGGER.debug("--> Exit sincronizzaDMS ");

    }

    @Override
    public Componente sostituisciComponenteAConfigurazione(ConfigurazioneDistinta configurazioneDistinta,
            Componente componentePadre, Componente componenteSelezionato, Articolo articoloSostitutivo) {
        LOGGER.debug("--> Enter sostituisciComponenteAConfigurazione");
        start("sostituisciComponenteAConfigurazione");
        Componente result = null;
        try {
            Integer idComponentePadre = null;
            if (componentePadre != null) {
                idComponentePadre = componentePadre.getId();
            }
            result = magazzinoAnagraficaService.sostituisciComponenteAConfigurazione(configurazioneDistinta.getId(),
                    idComponentePadre, componenteSelezionato.getId(), articoloSostitutivo.getId());
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("sostituisciComponenteAConfigurazione");
        }
        LOGGER.debug("--> Exit sostituisciComponenteAConfigurazione ");
        return result;
    }

    @Override
    public void verificaFormuleLinkate(TipoAttributo tipoAttributo, FormulaTrasformazione formulaTrasformazione,
            Map<TipoAttributo, FormulaTrasformazione> formuleTipiAttributo) throws FormuleLinkateException {
        LOGGER.debug("--> Enter verificaFormuleLinkate");
        start("verificaFormuleLinkate");
        try {
            magazzinoAnagraficaService.verificaFormuleLinkate(tipoAttributo, formulaTrasformazione,
                    formuleTipiAttributo);
        } catch (FormuleLinkateException e) {
            throw e;
        } catch (Exception e1) {
            PanjeaSwingUtil.checkAndThrowException(e1);
        } finally {
            end("verificaFormuleLinkate");
        }
        LOGGER.debug("--> Exit verificaFormuleLinkate ");
    }
}
