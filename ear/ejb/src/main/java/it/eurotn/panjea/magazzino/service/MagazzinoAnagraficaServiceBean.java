package it.eurotn.panjea.magazzino.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.jboss.annotation.ejb.RemoteBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.dao.exception.VincoloException;
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
import it.eurotn.panjea.magazzino.domain.CategoriaContabileArticolo;
import it.eurotn.panjea.magazzino.domain.CategoriaContabileDeposito;
import it.eurotn.panjea.magazzino.domain.CategoriaContabileSedeMagazzino;
import it.eurotn.panjea.magazzino.domain.CategoriaSedeMagazzino;
import it.eurotn.panjea.magazzino.domain.CausaleTrasporto;
import it.eurotn.panjea.magazzino.domain.CodiceArticoloEntita;
import it.eurotn.panjea.magazzino.domain.Componente;
import it.eurotn.panjea.magazzino.domain.ConfigurazioneDistinta;
import it.eurotn.panjea.magazzino.domain.DepositoMagazzino;
import it.eurotn.panjea.magazzino.domain.FormulaTrasformazione;
import it.eurotn.panjea.magazzino.domain.MagazzinoSettings;
import it.eurotn.panjea.magazzino.domain.MezzoTrasporto;
import it.eurotn.panjea.magazzino.domain.RaggruppamentoArticoli;
import it.eurotn.panjea.magazzino.domain.RigaRaggruppamentoArticoli;
import it.eurotn.panjea.magazzino.domain.Sconto;
import it.eurotn.panjea.magazzino.domain.SedeMagazzino;
import it.eurotn.panjea.magazzino.domain.SedeMagazzinoLite;
import it.eurotn.panjea.magazzino.domain.SottoContoContabilizzazione;
import it.eurotn.panjea.magazzino.domain.SottoContoContabilizzazione.ETipoEconomico;
import it.eurotn.panjea.magazzino.domain.TemplateSpedizioneMovimenti;
import it.eurotn.panjea.magazzino.domain.TipoAttributo;
import it.eurotn.panjea.magazzino.domain.TipoDocumentoBaseMagazzino;
import it.eurotn.panjea.magazzino.domain.TipoMezzoTrasporto;
import it.eurotn.panjea.magazzino.domain.TipoPorto;
import it.eurotn.panjea.magazzino.domain.TipoVariante;
import it.eurotn.panjea.magazzino.domain.TrasportoCura;
import it.eurotn.panjea.magazzino.domain.omaggio.Omaggio;
import it.eurotn.panjea.magazzino.domain.omaggio.TipoOmaggio;
import it.eurotn.panjea.magazzino.domain.rendicontazione.EntitaTipoEsportazione;
import it.eurotn.panjea.magazzino.domain.rendicontazione.TipoEsportazione;
import it.eurotn.panjea.magazzino.exception.ArticoliRigaMagazzinoCollegataException;
import it.eurotn.panjea.magazzino.exception.CodiceArticoloEntitaAbitualeEsistenteException;
import it.eurotn.panjea.magazzino.exception.CodiceArticoloEntitaContoTerziEsistenteException;
import it.eurotn.panjea.magazzino.exception.DistintaCircolareException;
import it.eurotn.panjea.magazzino.exception.FormuleLinkateException;
import it.eurotn.panjea.magazzino.exception.GenerazioneCodiceException;
import it.eurotn.panjea.magazzino.exception.GenerazioneDescrizioneException;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.FormulaTrasformazioneManager;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.TipiAreaMagazzinoManager;
import it.eurotn.panjea.magazzino.manager.interfaces.CategorieManager;
import it.eurotn.panjea.magazzino.manager.interfaces.DepositiMagazzinoManager;
import it.eurotn.panjea.magazzino.manager.interfaces.MagazzinoAnagraficaManager;
import it.eurotn.panjea.magazzino.manager.interfaces.MagazzinoContabilizzazioneManager;
import it.eurotn.panjea.magazzino.manager.interfaces.MagazzinoSettingsManager;
import it.eurotn.panjea.magazzino.manager.interfaces.MezziTrasportoManager;
import it.eurotn.panjea.magazzino.manager.interfaces.ScontiManager;
import it.eurotn.panjea.magazzino.manager.interfaces.SediMagazzinoManager;
import it.eurotn.panjea.magazzino.manager.interfaces.articolo.ArticoloDepositoManager;
import it.eurotn.panjea.magazzino.manager.interfaces.articolo.ArticoloManager;
import it.eurotn.panjea.magazzino.manager.interfaces.articolo.CodiceArticoloEntitaManager;
import it.eurotn.panjea.magazzino.manager.interfaces.articolo.DistintaBaseManager;
import it.eurotn.panjea.magazzino.manager.interfaces.articolo.RaggruppamentoArticoliManager;
import it.eurotn.panjea.magazzino.manager.omaggio.interfaces.OmaggioManager;
import it.eurotn.panjea.magazzino.service.exception.ScontoNotValidException;
import it.eurotn.panjea.magazzino.service.interfaces.MagazzinoAnagraficaService;
import it.eurotn.panjea.magazzino.util.ArticoloConfigurazioneDistinta;
import it.eurotn.panjea.magazzino.util.ArticoloRicerca;
import it.eurotn.panjea.magazzino.util.CategoriaLite;
import it.eurotn.panjea.magazzino.util.ParametriRicercaArticolo;
import it.eurotn.panjea.riepilogo.util.RiepilogoArticoloDTO;

/**
 *
 *
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
@Stateless(name = "Panjea.MagazzinoAnagraficaService")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@RemoteBinding(jndiBinding = "Panjea.MagazzinoAnagraficaService")
public class MagazzinoAnagraficaServiceBean implements MagazzinoAnagraficaService {

    @EJB
    private DistintaBaseManager distintaBaseManager;
    @EJB
    private MagazzinoAnagraficaManager magazzinoAnagraficaManager;
    @EJB
    private FormulaTrasformazioneManager formulaTrasformazioneManager;
    @EJB
    private ArticoloManager articoloManager;
    @EJB
    private ScontiManager scontiManager;
    @EJB
    private MagazzinoContabilizzazioneManager magazzinoContabilizzazioneManager;
    @EJB
    private SediMagazzinoManager sediMagazzinoManager;
    @EJB
    private DepositiMagazzinoManager depositiMagazzinoManager;
    @EJB
    private CategorieManager categorieManager;
    @EJB
    private MagazzinoSettingsManager magazzinoSettingsManager;
    @EJB
    private CodiceArticoloEntitaManager codiceArticoloEntitaManager;
    @EJB
    private TipiAreaMagazzinoManager tipiAreaMagazzinoManager;
    @EJB
    private RaggruppamentoArticoliManager raggruppamentoArticoliManager;
    @EJB
    private OmaggioManager omaggioManager;
    @EJB
    private ArticoloDepositoManager articoloDepositoManager;
    @EJB
    private MezziTrasportoManager mezziTrasportoManager;

    @Override
    public void aggiornaArticoliAlternativiFiltro(int idArticolo, String formula) {
        articoloManager.aggiornaArticoliAlternativiFiltro(idArticolo, formula);
    }

    @Override
    public void aggiungiCategoriaMerceologicaACategoriaCommerciale(int idCategoriaMerceologica,
            int idCategoriaCommercialeArticolo) {
        Categoria categoria = new Categoria();
        categoria.setId(idCategoriaMerceologica);
        CategoriaCommercialeArticolo categoriaCommerciale = new CategoriaCommercialeArticolo();
        categoriaCommerciale.setId(idCategoriaCommercialeArticolo);
        magazzinoAnagraficaManager.aggiungiCategoriaMerceologicaACategoriaCommerciale(categoria, categoriaCommerciale);
    }

    @Override
    public Componente aggiungiComponenteAConfigurazione(ConfigurazioneDistinta configurazioneDistinta,
            Componente componentePadre, Articolo articoloDaAggiungere) throws DistintaCircolareException {
        return distintaBaseManager.aggiungiComponenteAConfigurazione(configurazioneDistinta, componentePadre,
                articoloDaAggiungere);
    }

    @Override
    public Componente aggiungiComponenteAConfigurazionePerImportazione(ConfigurazioneDistinta configurazioneDistinta,
            Componente componentePadre, Articolo articoloComponente) {
        return distintaBaseManager.aggiungiComponenteAConfigurazionePerImportazione(configurazioneDistinta,
                componentePadre, articoloComponente);
    }

    @Override
    public Set<FaseLavorazioneArticolo> aggiungiFasiLavorazione(ConfigurazioneDistinta configurazione,
            ArticoloLite articoloLite, Set<FaseLavorazioneArticolo> fasiLavorazioni) {
        return distintaBaseManager.aggiungiFasiLavorazione(configurazione, articoloLite, fasiLavorazioni);
    }

    @Override
    public Set<FaseLavorazioneArticolo> aggiungiFasiLavorazione(ConfigurazioneDistinta configurazione,
            Componente componente, Set<FaseLavorazioneArticolo> fasiLavorazioni) {
        return distintaBaseManager.aggiungiFasiLavorazione(configurazione, componente, fasiLavorazioni);
    }

    @Override
    public void associaSediASedePerRifatturazione(List<SedeEntita> sediEntita,
            SedeMagazzinoLite sedePerRifatturazione) {
        sediMagazzinoManager.associaSediASedePerRifatturazione(sediEntita, sedePerRifatturazione);
    }

    @Override
    @RolesAllowed("gestioneFatturazione")
    public void associaSediMagazzinoPerRifatturazione(SedeMagazzinoLite sedeDiRifatturazione,
            List<SedeMagazzinoLite> sediDaAssociare) {
        sediMagazzinoManager.associaSediMagazzinoPerRifatturazione(sedeDiRifatturazione, sediDaAssociare);
    }

    @Override
    public String calcolaEAN() {
        return articoloManager.calcolaEAN();
    }

    @Override
    public void cambiaCategoriaAdArticoli(List<ArticoloRicerca> articoli, Categoria categoriaDestinazione) {
        articoloManager.cambiaCategoriaAdArticoli(articoli, categoriaDestinazione);
    }

    @Override
    public void cambiaCategoriaCommercialeAdArticoli(List<ArticoloRicerca> articoli,
            CategoriaCommercialeArticolo categoriaCommercialeArticolo,
            CategoriaCommercialeArticolo categoriaCommercialeArticolo2) {
        articoloManager.cambiaCategoriaCommercialeAdArticoli(articoli, categoriaCommercialeArticolo,
                categoriaCommercialeArticolo2);
    }

    @Override
    public void cambiaCodiceIVA(CodiceIva codiceIvaDaSostituire, CodiceIva nuovoCodiceIva) {
        articoloManager.cambiaCodiceIVA(codiceIvaDaSostituire, nuovoCodiceIva);
    }

    @TransactionAttribute(TransactionAttributeType.NEVER)
    @Override
    public boolean cancellaArticoli(List<ArticoloRicerca> articoli) {
        boolean tuttiArticoliCancellati = true;
        ArticoliRigaMagazzinoCollegataException articoliRigaMagazzinoCollegataException = new ArticoliRigaMagazzinoCollegataException();

        for (ArticoloRicerca articolo : articoli) {
            try {
                articoloManager.cancellaArticolo(articolo.createProxyArticolo());
            } catch (Exception e) {
                // aggiungere gli articoli in una eccezione per restituire la
                // lista di articoli bloccati da riga
                // magazzino
                if (e.getCause().getCause() instanceof VincoloException) {
                    articoliRigaMagazzinoCollegataException.addArticolo(articolo);
                } else {
                    throw new RuntimeException("errore durante la cancellazione dell'articolo.", e);
                }
            }
        }

        if (articoliRigaMagazzinoCollegataException.getArticoli().size() > 0) {
            throw new RuntimeException(articoliRigaMagazzinoCollegataException);
        }
        return tuttiArticoliCancellati;
    }

    @Override
    public void cancellaArticolo(Articolo articolo) {
        articoloManager.cancellaArticolo(articolo);
    }

    @Override
    public void cancellaArticoloAlternativo(ArticoloAlternativo articoloAlternativo) {
        articoloManager.cancellaArticoloAlternativo(articoloAlternativo);
    }

    @Override
    public void cancellaArticoloDeposito(ArticoloDeposito articoloDeposito) {
        articoloDepositoManager.cancellaArticoloDeposito(articoloDeposito);
    }

    @Override
    public void cancellaAspettoEsteriore(AspettoEsteriore aspettoEsteriore) {
        magazzinoAnagraficaManager.cancellaAspettoEsteriore(aspettoEsteriore);
    }

    @Override
    public void cancellaCategoria(Integer idCategoria) {
        categorieManager.cancellaCategoria(idCategoria);
    }

    @Override
    public void cancellaCategoriaCommercialeArticolo(CategoriaCommercialeArticolo categoriaCommercialeArticolo) {
        magazzinoAnagraficaManager.cancellaCategoriaCommercialeArticolo(categoriaCommercialeArticolo);
    }

    @Override
    public void cancellaCategoriaContabileArticolo(CategoriaContabileArticolo categoriaContabileArticolo) {
        magazzinoContabilizzazioneManager.cancellaCategoriaContabileArticolo(categoriaContabileArticolo);
    }

    @Override
    public void cancellaCategoriaContabileDeposito(CategoriaContabileDeposito categoriaContabileDeposito) {
        magazzinoContabilizzazioneManager.cancellaCategoriaContabileDeposito(categoriaContabileDeposito);

    }

    @Override
    public void cancellaCategoriaContabileSedeMagazzino(
            CategoriaContabileSedeMagazzino categoriaContabileSedeMagazzino) {
        magazzinoContabilizzazioneManager.cancellaCategoriaContabileSedeMagazzino(categoriaContabileSedeMagazzino);

    }

    @Override
    public void cancellaCategorieSediMagazzino(CategoriaSedeMagazzino categoriaSedeMagazzino) {
        sediMagazzinoManager.cancellaCategorieSediMagazzino(categoriaSedeMagazzino);
    }

    @Override
    public void cancellaCausaleTrasporto(CausaleTrasporto causaleTrasporto) {
        magazzinoAnagraficaManager.cancellaCausaleTrasporto(causaleTrasporto);
    }

    @Override
    public void cancellaCodiceArticoloEntita(CodiceArticoloEntita codiceArticoloEntita) {
        codiceArticoloEntitaManager.cancellaCodiceArticoloEntita(codiceArticoloEntita);
    }

    @Override
    public void cancellaComponentiConfigurazioneDistinta(List<Componente> componenteSelezionato) {
        distintaBaseManager.cancellaComponentiConfigurazioneDistinta(componenteSelezionato);
    }

    @Override
    public void cancellaConfigurazioneDistinta(ConfigurazioneDistinta configurazioneDistinta) {
        distintaBaseManager.cancellaConfigurazioneDistinta(configurazioneDistinta);
    }

    @Override
    public void cancellaDepositoMagazzino(Integer id) {
        depositiMagazzinoManager.cancellaDepositoMagazzino(id);
    }

    @Override
    public void cancellaEntitaTipoEsportazione(EntitaTipoEsportazione entitaTipoEsportazione) {
        magazzinoAnagraficaManager.cancellaEntitaTipoEsportazione(entitaTipoEsportazione);
    }

    @Override
    public void cancellaFaseLavorazione(FaseLavorazione faseLavorazione) {
        magazzinoAnagraficaManager.cancellaFaseLavorazione(faseLavorazione);
    }

    @Override
    public void cancellaFasiLavorazioneArticolo(ConfigurazioneDistinta configurazioneDistinta,
            List<FaseLavorazioneArticolo> fasiArticoloDaCancellare) {
        distintaBaseManager.cancellaFasiLavorazioneArticolo(configurazioneDistinta, fasiArticoloDaCancellare);
    }

    @Override
    public void cancellaFormulaTrasformazione(FormulaTrasformazione formulaTrasformazione) {
        formulaTrasformazioneManager.cancellaFormulaTrasformazione(formulaTrasformazione);
    }

    @Override
    public void cancellaMezzoTrasporto(MezzoTrasporto mezzoTrasporto) {
        mezziTrasportoManager.cancellaMezzoTrasporto(mezzoTrasporto);
    }

    @Override
    public void cancellaRaggruppamento(RaggruppamentoArticoli raggruppamentoArticoli) {
        raggruppamentoArticoliManager.cancellaRaggruppamento(raggruppamentoArticoli);
    }

    @Override
    public void cancellaRigaRaggruppamentoArticoli(RigaRaggruppamentoArticoli rigaRaggruppamentoArticoli) {
        raggruppamentoArticoliManager.cancellaRigaRaggruppamentoArticoli(rigaRaggruppamentoArticoli);
    }

    @Override
    public void cancellaSconto(Sconto sconto) {
        scontiManager.cancellaSconto(sconto);
    }

    @Override
    public void cancellaSottoContoContabilizzazione(Integer id) {
        magazzinoContabilizzazioneManager.cancellaSottoContoContabilizzazione(id);
    }

    @Override
    public void cancellaTipoAttributo(TipoAttributo tipoAttributo) {
        magazzinoAnagraficaManager.cancellaTipoAttributo(tipoAttributo);
    }

    @Override
    public void cancellaTipoDocumentoBase(TipoDocumentoBaseMagazzino tipoDocumentoBase) {
        tipiAreaMagazzinoManager.cancellaTipoDocumentoBase(tipoDocumentoBase);
    }

    @Override
    public void cancellaTipoEsportazione(TipoEsportazione tipoEsportazione) {
        magazzinoAnagraficaManager.cancellaTipoEsportazione(tipoEsportazione);
    }

    @Override
    public void cancellaTipoMezzoTrasporto(TipoMezzoTrasporto tipoMezzoTrasporto) {
        mezziTrasportoManager.cancellaTipoMezzoTrasporto(tipoMezzoTrasporto);
    }

    @Override
    public void cancellaTipoPorto(TipoPorto tipoPorto) {
        magazzinoAnagraficaManager.cancellaTipoPorto(tipoPorto);
    }

    @Override
    public void cancellaTrasportoCura(TrasportoCura trasportoCura) {
        magazzinoAnagraficaManager.cancellaTrasportoCura(trasportoCura);
    }

    @Override
    public List<ArticoloRicerca> caricaArticoli() {
        return articoloManager.caricaArticoli();
    }

    @Override
    public Set<ArticoloAlternativo> caricaArticoliAlternativi(Articolo articolo) {
        return articoloManager.caricaArticoliAlternativi(articolo);
    }

    @Override
    public List<ArticoloDeposito> caricaArticoliDeposito(Deposito deposito) {
        return articoloDepositoManager.caricaArticoliDeposito(deposito);
    }

    @Override
    public List<ArticoloDeposito> caricaArticoliDeposito(Integer idArticolo) {
        return articoloDepositoManager.caricaArticoliDeposito(idArticolo);
    }

    @Override
    public Articolo caricaArticolo(Articolo articolo, boolean initializeLazy) {
        return articoloManager.caricaArticolo(articolo, initializeLazy);
    }

    @Override
    public ArticoloConfigurazioneDistinta caricaArticoloConfigurazioneDistinta(
            ConfigurazioneDistinta configurazioneDistinta) throws DistintaCircolareException {
        return distintaBaseManager.caricaArticoloConfigurazioneDistinta(configurazioneDistinta);
    }

    @Override
    public ArticoloDeposito caricaArticoloDeposito(Integer idArticolo, Integer idDeposito) {
        return articoloDepositoManager.caricaArticoloDeposito(idArticolo, idDeposito);
    }

    @Override
    public ArticoloLite caricaArticoloLite(int idArticolo) {
        return articoloManager.caricaArticoloLite(idArticolo);
    }

    @Override
    public List<AspettoEsteriore> caricaAspettiEsteriori(String descrizione) {
        return magazzinoAnagraficaManager.caricaAspettiEsteriori(descrizione);
    }

    @Override
    public Categoria caricaCategoria(Categoria categoria, boolean initializeLazy) {
        return categorieManager.caricaCategoria(categoria, initializeLazy);
    }

    @Override
    public CategoriaCommercialeArticolo caricaCategoriaCommercialeArticolo(
            CategoriaCommercialeArticolo categoriaCommercialeArticolo) {
        return magazzinoAnagraficaManager.caricaCategoriaCommercialeArticolo(categoriaCommercialeArticolo);
    }

    @Override
    public List<CategoriaLite> caricaCategorie() {
        return categorieManager.caricaCategorie();
    }

    @Override
    public List<Categoria> caricaCategorieCodiceDescrizione(String fieldSearch, String valueSearch) {
        return categorieManager.caricaCategorieCodiceDescrizione(fieldSearch, valueSearch);
    }

    @Override
    public List<CategoriaCommercialeArticolo> caricaCategorieCommercialeArticolo(String fieldSearch,
            String valueSearch) {
        return magazzinoAnagraficaManager.caricaCategorieCommercialeArticolo(fieldSearch, valueSearch);
    }

    @Override
    public List<CategoriaContabileArticolo> caricaCategorieContabileArticolo(String fieldSearch, String valueSearch) {
        return magazzinoContabilizzazioneManager.caricaCategorieContabileArticolo(fieldSearch, valueSearch);
    }

    @Override
    public List<CategoriaContabileDeposito> caricaCategorieContabileDeposito(String fieldSearch, String valueSearch) {
        return magazzinoContabilizzazioneManager.caricaCategorieContabileDeposito(fieldSearch, valueSearch);
    }

    @Override
    public List<CategoriaContabileSedeMagazzino> caricaCategorieContabileSedeMagazzino(String fieldSearch,
            String valueSearch) {
        return magazzinoContabilizzazioneManager.caricaCategorieContabileSedeMagazzino(fieldSearch, valueSearch);
    }

    @Override
    public List<CategoriaSedeMagazzino> caricaCategorieSediMagazzino(String fieldSearch, String valueSearch) {
        return sediMagazzinoManager.caricaCategorieSediMagazzino(fieldSearch, valueSearch);
    }

    @Override
    public List<CausaleTrasporto> caricaCausaliTraporto(String descrizione) {
        return magazzinoAnagraficaManager.caricaCausaliTraporto(descrizione);
    }

    @Override
    public List<CodiceArticoloEntita> caricaCodiciArticoloEntita(Entita entita) {
        return codiceArticoloEntitaManager.caricaCodiciArticoloEntita(entita);
    }

    @Override
    public List<CodiceArticoloEntita> caricaCodiciArticoloEntita(Integer idArticolo) {
        return codiceArticoloEntitaManager.caricaCodiciArticoloEntita(idArticolo);
    }

    /**
     *
     * @param configurazioneDistinta
     *            configurazioneDistint configurazione da caricare
     *
     * @return componenti che compongono la configurazione della distinta.
     * @throws DistintaCircolareException
     *             rilanciata se ho un collegamento circolare.
     */
    @Override
    public Set<Componente> caricaComponenti(ConfigurazioneDistinta configurazioneDistinta)
            throws DistintaCircolareException {
        return distintaBaseManager.caricaComponenti(configurazioneDistinta);
    }

    @Override
    public Set<Componente> caricaComponenti(int idArticolo) throws DistintaCircolareException {
        Articolo articolo = new Articolo();
        articolo.setId(idArticolo);
        return distintaBaseManager.caricaComponenti(articolo);
    }

    @Override
    public ConfigurazioneDistinta caricaConfigurazioneDistinta(int idConfigurazione) {
        return distintaBaseManager.caricaConfigurazioneDistinta(idConfigurazione);
    }

    @Override
    public List<ConfigurazioneDistinta> caricaConfigurazioniDistinta(ArticoloLite distinta) {
        return distintaBaseManager.caricaConfigurazioniDistinta(distinta);
    }

    @Override
    public DepositoMagazzino caricaDepositoMagazzino(Integer id) {
        return depositiMagazzinoManager.caricaDepositoMagazzino(id);
    }

    @Override
    public DepositoMagazzino caricaDepositoMagazzinoByDeposito(Deposito deposito) {
        return depositiMagazzinoManager.caricaDepositoMagazzinoByDeposito(deposito);
    }

    @Override
    public Set<Componente> caricaDistinteComponente(Integer idArticolo) {
        return distintaBaseManager.caricaDistinteComponente(idArticolo);
    }

    @Override
    public List<EntitaTipoEsportazione> caricaEntitaTipoEsportazione() {
        return magazzinoAnagraficaManager.caricaEntitaTipoEsportazione();
    }

    @Override
    public FaseLavorazione caricaFaseLavorazione(int idFase) {
        FaseLavorazione fase = new FaseLavorazione();
        fase.setId(idFase);
        return magazzinoAnagraficaManager.caricaFaseLavorazione(fase);
    }

    @Override
    public List<FaseLavorazione> caricaFasiLavorazione(String codice) {
        return magazzinoAnagraficaManager.caricaFasiLavorazione(codice);
    }

    @Override
    public Set<FaseLavorazioneArticolo> caricaFasiLavorazioneArticolo(ConfigurazioneDistinta configurazioneDistinta) {
        return distintaBaseManager.caricaFasiLavorazione(configurazioneDistinta, null);
    }

    @Override
    public FormulaTrasformazione caricaFormulaTrasformazione(FormulaTrasformazione formulaTrasformazione) {
        return formulaTrasformazioneManager.caricaFormulaTrasformazione(formulaTrasformazione);
    }

    @Override
    public List<FormulaTrasformazione> caricaFormuleTrasformazione(String codice) {
        return formulaTrasformazioneManager.caricaFormuleTrasformazione(codice);
    }

    @Override
    public MagazzinoSettings caricaMagazzinoSettings() {
        return magazzinoSettingsManager.caricaMagazzinoSettings();
    }

    @Override
    public List<MezzoTrasporto> caricaMezziTrasporto(String targa, boolean abilitato, EntitaLite entita,
            boolean senzaCaricatore) {
        return mezziTrasportoManager.caricaMezziTrasporto(targa, abilitato, entita, senzaCaricatore);
    }

    @Override
    public List<Omaggio> caricaOmaggi() {
        return omaggioManager.caricaOmaggi();
    }

    @Override
    public Omaggio caricaOmaggioByTipo(TipoOmaggio tipoOmaggio) {
        return omaggioManager.caricaOmaggioByTipo(tipoOmaggio);
    }

    @Override
    public List<RaggruppamentoArticoli> caricaRaggruppamenti() {
        return raggruppamentoArticoliManager.caricaRaggruppamenti();
    }

    @Override
    public RaggruppamentoArticoli caricaRaggruppamentoArticoli(RaggruppamentoArticoli raggruppamentoArticoli) {
        return raggruppamentoArticoliManager.caricaRaggruppamentoArticoli(raggruppamentoArticoli);
    }

    @Override
    public List<RiepilogoArticoloDTO> caricaRiepilogoArticoli() {
        return articoloManager.caricaRiepilogoArticoli();
    }

    @Override
    public Set<RigaRaggruppamentoArticoli> caricaRigheRaggruppamento(RaggruppamentoArticoli raggruppamentoArticoli) {
        return raggruppamentoArticoliManager.caricaRigheRaggruppamento(raggruppamentoArticoli);
    }

    @Override
    public List<RigaRaggruppamentoArticoli> caricaRigheRaggruppamentoArticoliByArticolo(int idArticolo) {
        return raggruppamentoArticoliManager.caricaRigheRaggruppamentoArticoliByArticolo(idArticolo);
    }

    @Override
    public List<Sconto> caricaSconti() {
        return scontiManager.caricaSconti();
    }

    @Override
    public Sconto caricaSconto(Sconto sconto) {
        return scontiManager.caricaSconto(sconto);
    }

    @Override
    public SedeMagazzino caricaSedeMagazzinoBySedeEntita(SedeEntita sedeEntita, boolean ignoraEreditaDatiCommerciali) {
        return sediMagazzinoManager.caricaSedeMagazzinoBySedeEntita(sedeEntita, ignoraEreditaDatiCommerciali);
    }

    @Override
    public SedeMagazzino caricaSedeMagazzinoPrincipale(Entita entita) {
        return sediMagazzinoManager.caricaSedeMagazzinoPrincipale(entita);
    }

    @Override
    public List<SedeMagazzino> caricaSediMagazzino(Map<String, Object> parametri, boolean textAsLike) {
        return sediMagazzinoManager.caricaSediMagazzino(parametri, textAsLike);
    }

    @Override
    public List<SedeMagazzinoLite> caricaSediMagazzinoByEntita(Entita entita) {
        return sediMagazzinoManager.caricaSediMagazzinoByEntita(entita);
    }

    @Override
    public List<SedeMagazzinoLite> caricaSediMagazzinoDiRifatturazione() {
        return sediMagazzinoManager.caricaSediMagazzinoDiRifatturazione();
    }

    @Override
    public List<SedeMagazzinoLite> caricaSediRifatturazioneAssociate() {
        return sediMagazzinoManager.caricaSediRifatturazioneAssociate();
    }

    @Override
    public List<SedeMagazzinoLite> caricaSediRifatturazioneNonAssociate(EntitaLite entita) {
        return sediMagazzinoManager.caricaSediRifatturazioneNonAssociate(entita);
    }

    @Override
    public List<SottoContoContabilizzazione> caricaSottoContiContabilizzazione(ETipoEconomico tipoEconomico) {
        return magazzinoContabilizzazioneManager.caricaSottoContiContabilizzazione(tipoEconomico);
    }

    @Override
    public SottoContoContabilizzazione caricaSottoContoContabilizzazione(Integer id) {
        return magazzinoContabilizzazioneManager.caricaSottoContoContabilizzazione(id);
    }

    @Override
    public TemplateSpedizioneMovimenti caricaTemplateSpedizioneMovimenti() {
        return magazzinoAnagraficaManager.caricaTemplateSpedizioneMovimenti();
    }

    @Override
    public List<TipoAttributo> caricaTipiAttributo() {
        return magazzinoAnagraficaManager.caricaTipiAttributo();
    }

    @Override
    public List<TipoDocumentoBaseMagazzino> caricaTipiDocumentoBase() {
        return tipiAreaMagazzinoManager.caricaTipiDocumentoBase();
    }

    @Override
    public List<TipoEsportazione> caricaTipiEsportazione(String nome) {
        return magazzinoAnagraficaManager.caricaTipiEsportazione(nome);
    }

    @Override
    public List<TipoMezzoTrasporto> caricaTipiMezzoTrasporto() {
        return mezziTrasportoManager.caricaTipiMezzoTrasporto();
    }

    @Override
    public List<TipoPorto> caricaTipiPorto(String descrizione) {
        return magazzinoAnagraficaManager.caricaTipiPorto(descrizione);
    }

    @Override
    public TipoAttributo caricaTipoAttributo(TipoAttributo tipoAttributo) {
        return magazzinoAnagraficaManager.caricaTipoAttributo(tipoAttributo);
    }

    @Override
    public TipoEsportazione caricaTipoEsportazione(Integer idTipoEsportazione, boolean loadLazy) {
        return magazzinoAnagraficaManager.caricaTipoEsportazione(idTipoEsportazione, loadLazy);
    }

    @Override
    public TipoVariante caricaTipoVariante(TipoVariante tipoVariante) {
        return magazzinoAnagraficaManager.caricaTipoVariante(tipoVariante);
    }

    @Override
    public List<TrasportoCura> caricaTrasportiCura(String descrizione) {
        return magazzinoAnagraficaManager.caricaTrasportiCura(descrizione);
    }

    @Override
    public Articolo cloneArticolo(int idArticolo, String nuovoCodice, String nuovaDescrizione, boolean copyDistinta,
            boolean copyDistintaConfigurazioni, List<AttributoArticolo> attributi, boolean copyListino,
            boolean azzeraPrezziListino) {
        Articolo articolo = new Articolo();
        articolo.setId(idArticolo);
        return articoloManager.cloneArticolo(articolo, nuovoCodice, nuovaDescrizione, copyDistinta,
                copyDistintaConfigurazioni, attributi, copyListino, azzeraPrezziListino);
    }

    @Override
    public Articolo creaArticolo(Categoria categoria) {
        return articoloManager.creaArticolo(categoria);
    }

    @Override
    public Categoria creaCategoria(Integer idCategoriaPadre) {
        return categorieManager.creaCategoria(idCategoriaPadre);
    }

    @Override
    public MezzoTrasporto creaNuovoDepositoMezzoDiTrasporto(MezzoTrasporto mezzoTrasporto, String codiceDeposito,
            String descrizioneDeposito) {
        return mezziTrasportoManager.creaNuovoDepositoMezzoDiTrasporto(mezzoTrasporto, codiceDeposito,
                descrizioneDeposito);
    }

    @Override
    public List<ArticoloRicerca> ricercaArticoli(ParametriRicercaArticolo parametriRicercaArticolo) {
        return articoloManager.ricercaArticoli(parametriRicercaArticolo);
    }

    @Override
    public List<ArticoloRicerca> ricercaArticoliSearchObject(ParametriRicercaArticolo parametriRicercaArticolo) {
        return articoloManager.ricercaArticoliSearchObject(parametriRicercaArticolo);
    }

    @Override
    public List<Sconto> ricercaSconti(String codiceSconto) {
        return scontiManager.ricercaSconti(codiceSconto);
    }

    @Override
    public MezzoTrasporto rimuoviDepositoDaMezzoDiTrasporto(MezzoTrasporto mezzoTrasporto) {
        return mezziTrasportoManager.rimuoviDepositoDaMezzoDiTrasporto(mezzoTrasporto);
    }

    @Override
    public void rimuoviReferenzaCircolare(ArticoloLite articolo) {
        distintaBaseManager.rimuoviReferenzaCircolare(articolo);
    }

    @Override
    public void rimuoviSedePerRifatturazione(SedeMagazzinoLite sedeMagazzino) {
        sediMagazzinoManager.rimuoviSedePerRifatturazione(sedeMagazzino);
    }

    @Override
    public Articolo salvaArticolo(Articolo articolo)
            throws GenerazioneCodiceException, GenerazioneDescrizioneException {
        return articoloManager.salvaArticolo(articolo);
    }

    @Override
    public ArticoloAlternativo salvaArticoloAlternativo(ArticoloAlternativo articoloAlternativo) {
        return articoloManager.salvaArticoloAlternativo(articoloAlternativo);
    }

    @Override
    public ArticoloDeposito salvaArticoloDeposito(ArticoloDeposito articoloDeposito) {
        return articoloDepositoManager.salvaArticoloDeposito(articoloDeposito);
    }

    @Override
    public AspettoEsteriore salvaAspettoEsteriore(AspettoEsteriore aspettoEsteriore) {
        return magazzinoAnagraficaManager.salvaAspettoEsteriore(aspettoEsteriore);
    }

    @Override
    public AttributoArticolo salvaAttributoArticolo(AttributoArticolo attributoArticolo) {
        return articoloManager.salvaAttributoArticolo(attributoArticolo);
    }

    @Override
    public Categoria salvaCategoria(Categoria categoria) {
        return categorieManager.salvaCategoria(categoria);
    }

    @Override
    public CategoriaCommercialeArticolo salvaCategoriaCommercialeArticolo(
            CategoriaCommercialeArticolo categoriaCommercialeArticolo) {
        return magazzinoAnagraficaManager.salvaCategoriaCommercialeArticolo(categoriaCommercialeArticolo);
    }

    @Override
    public CategoriaContabileArticolo salvaCategoriaContabileArticolo(
            CategoriaContabileArticolo categoriaContabileArticolo) {
        return magazzinoContabilizzazioneManager.salvaCategoriaContabileArticolo(categoriaContabileArticolo);
    }

    @Override
    public CategoriaContabileDeposito salvaCategoriaContabileDeposito(
            CategoriaContabileDeposito categoriaContabileDeposito) {
        return magazzinoContabilizzazioneManager.salvaCategoriaContabileDeposito(categoriaContabileDeposito);
    }

    @Override
    public CategoriaContabileSedeMagazzino salvaCategoriaContabileSedeMagazzino(
            CategoriaContabileSedeMagazzino categoriaContabileSedeMagazzino) {
        return magazzinoContabilizzazioneManager.salvaCategoriaContabileSedeMagazzino(categoriaContabileSedeMagazzino);
    }

    @Override
    public CategoriaSedeMagazzino salvaCategoriaSedeMagazzino(CategoriaSedeMagazzino categoriaSedeMagazzino) {
        return sediMagazzinoManager.salvaCategoriaSedeMagazzino(categoriaSedeMagazzino);
    }

    @Override
    public CausaleTrasporto salvaCausaleTraporto(CausaleTrasporto causaleTrasporto) {
        return magazzinoAnagraficaManager.salvaCausaleTraporto(causaleTrasporto);
    }

    @Override
    public CodiceArticoloEntita salvaCodiceArticoloEntita(CodiceArticoloEntita codiceArticoloEntita)
            throws CodiceArticoloEntitaContoTerziEsistenteException, CodiceArticoloEntitaAbitualeEsistenteException {
        return codiceArticoloEntitaManager.salvaCodiceArticoloEntita(codiceArticoloEntita);
    }

    @Override
    public Componente salvaComponente(Componente componente) {
        return distintaBaseManager.salvaComponente(componente);
    }

    @Override
    public ConfigurazioneDistinta salvaConfigurazioneDistinta(ConfigurazioneDistinta configurazioneDistinta) {
        return distintaBaseManager.salvaConfigurazioneDistinta(configurazioneDistinta);
    }

    @Override
    public DepositoMagazzino salvaDepositoMagazzino(DepositoMagazzino depositoMagazzino) {
        return depositiMagazzinoManager.salvaDepositoMagazzino(depositoMagazzino);
    }

    @Override
    public void salvaDistintaArticolo(ArticoloLite articoloLite, Set<Componente> distinte, Set<Componente> componenti,
            Set<FaseLavorazioneArticolo> fasiLavorazioni) throws DistintaCircolareException {
        distintaBaseManager.salvaDistintaArticolo(articoloLite, distinte, componenti, fasiLavorazioni);
    }

    @Override
    public EntitaTipoEsportazione salvaEntitaTipoEsportazione(EntitaTipoEsportazione entitaTipoEsportazione) {
        return magazzinoAnagraficaManager.salvaEntitaTipoEsportazione(entitaTipoEsportazione);
    }

    @Override
    public FaseLavorazione salvaFaseLavorazione(FaseLavorazione faseLavorazione) {
        return magazzinoAnagraficaManager.salvaFaseLavorazione(faseLavorazione);
    }

    @Override
    public FaseLavorazioneArticolo salvaFaseLavorazioneArticolo(FaseLavorazioneArticolo faseLavorazioneArticolo) {
        return magazzinoAnagraficaManager.salvaFaseLavorazioneArticolo(faseLavorazioneArticolo);
    }

    @Override
    public FormulaTrasformazione salvaFormulaTrasformazione(FormulaTrasformazione formulaTrasformazione) {
        return formulaTrasformazioneManager.salvaFormulaTrasformazione(formulaTrasformazione);
    }

    @Override
    public MagazzinoSettings salvaMagazzinoSettings(MagazzinoSettings magazzinoSettings) {
        return magazzinoSettingsManager.salvaMagazzinoSettings(magazzinoSettings);
    }

    @Override
    public MezzoTrasporto salvaMezzoTrasporto(MezzoTrasporto mezzoTrasporto) {
        return mezziTrasportoManager.salvaMezzoTrasporto(mezzoTrasporto);
    }

    @Override
    public Omaggio salvaOmaggio(Omaggio omaggio) {
        return omaggioManager.salvaOmaggio(omaggio);
    }

    @Override
    public RaggruppamentoArticoli salvaRaggruppamento(RaggruppamentoArticoli raggruppamentoArticoli) {
        return raggruppamentoArticoliManager.salvaRaggruppamento(raggruppamentoArticoli);
    }

    @Override
    public RigaRaggruppamentoArticoli salvaRigaRaggruppamentoArticoli(
            RigaRaggruppamentoArticoli rigaRaggruppamentoArticoli) {
        return raggruppamentoArticoliManager.salvaRigaRaggruppamentoArticoli(rigaRaggruppamentoArticoli);
    }

    @Override
    public Sconto salvaSconto(Sconto sconto) throws ScontoNotValidException {
        return scontiManager.salvaSconto(sconto);
    }

    @Override
    public SedeMagazzino salvaSedeMagazzino(SedeMagazzino sedeMagazzino) {
        return sediMagazzinoManager.salvaSedeMagazzino(sedeMagazzino);
    }

    @Override
    public List<SedeMagazzino> salvaSediMagazzino(List<SedeMagazzino> sedi) {

        List<SedeMagazzino> sediSalvate = new ArrayList<SedeMagazzino>();

        for (SedeMagazzino sedeMagazzino : sedi) {
            sediSalvate.add(salvaSedeMagazzino(sedeMagazzino));
        }

        return sediSalvate;
    }

    @Override
    public SottoContoContabilizzazione salvaSottoContoContabilizzazione(
            SottoContoContabilizzazione sottoContoContabilizzazione) {
        return magazzinoContabilizzazioneManager.salvaSottoContoContabilizzazione(sottoContoContabilizzazione);
    }

    @Override
    public TemplateSpedizioneMovimenti salvaTemplateSpedizioneMovimenti(
            TemplateSpedizioneMovimenti templateSpedizioneMovimenti) {
        return magazzinoAnagraficaManager.salvaTemplateSpedizioneMovimenti(templateSpedizioneMovimenti);
    }

    @Override
    public TipoAttributo salvaTipoAttributo(TipoAttributo tipoAttributo) {
        return magazzinoAnagraficaManager.salvaTipoAttributo(tipoAttributo);
    }

    @Override
    public TipoDocumentoBaseMagazzino salvaTipoDocumentoBase(TipoDocumentoBaseMagazzino tipoDocumentoBase) {
        return tipiAreaMagazzinoManager.salvaTipoDocumentoBase(tipoDocumentoBase);
    }

    @Override
    public TipoEsportazione salvaTipoEsportazione(TipoEsportazione tipoEsportazione) {
        return magazzinoAnagraficaManager.salvaTipoEsportazione(tipoEsportazione);
    }

    @Override
    public TipoMezzoTrasporto salvaTipoMezzoTrasporto(TipoMezzoTrasporto tipoMezzoTrasporto) {
        return mezziTrasportoManager.salvaTipoMezzoTrasporto(tipoMezzoTrasporto);
    }

    @Override
    public TipoPorto salvaTipoPorto(TipoPorto tipoPorto) {
        return magazzinoAnagraficaManager.salvaTipoPorto(tipoPorto);
    }

    @Override
    public TrasportoCura salvaTrasportoCura(TrasportoCura trasportoCura) {
        return magazzinoAnagraficaManager.salvaTrasportoCura(trasportoCura);
    }

    @Override
    public Componente sostituisciComponenteAConfigurazione(Integer idConfigurazioneDistinta, Integer idComponentePadre,
            Integer idComponenteSelezionato, Integer idArticoloSostitutivo) throws DistintaCircolareException {
        return distintaBaseManager.sostituisciComponenteAConfigurazione(idConfigurazioneDistinta, idComponentePadre,
                idComponenteSelezionato, idArticoloSostitutivo);
    }

    @Override
    public void verificaFormuleLinkate(TipoAttributo tipoAttributo, FormulaTrasformazione formulaTrasformazione,
            Map<TipoAttributo, FormulaTrasformazione> formuleTipiAttributo) throws FormuleLinkateException {
        formulaTrasformazioneManager.verificaFormuleLinkate(tipoAttributo, formulaTrasformazione, formuleTipiAttributo);
    }
}