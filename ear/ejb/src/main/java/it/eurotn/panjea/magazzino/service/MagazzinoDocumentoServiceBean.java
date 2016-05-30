package it.eurotn.panjea.magazzino.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.jboss.annotation.IgnoreDependency;
import org.jboss.annotation.ejb.RemoteBinding;
import org.jboss.annotation.ejb.TransactionTimeout;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.documenti.domain.IAreaDocumento;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.documenti.service.exception.AreeCollegatePresentiException;
import it.eurotn.panjea.anagrafica.documenti.service.exception.DocumentiCollegatiPresentiException;
import it.eurotn.panjea.anagrafica.domain.Azienda;
import it.eurotn.panjea.anagrafica.domain.CodiceIva;
import it.eurotn.panjea.anagrafica.domain.Deposito;
import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.anagrafica.domain.SedeAzienda;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.lite.AziendaLite;
import it.eurotn.panjea.anagrafica.manager.depositi.interfaces.DepositiManager;
import it.eurotn.panjea.anagrafica.manager.interfaces.AziendeManager;
import it.eurotn.panjea.anagrafica.manager.interfaces.SediAziendaManager;
import it.eurotn.panjea.anagrafica.manager.interfaces.SediEntitaManager;
import it.eurotn.panjea.anagrafica.service.exception.AnagraficaServiceException;
import it.eurotn.panjea.anagrafica.service.exception.TipoDocumentoBaseException;
import it.eurotn.panjea.cauzioni.manager.interfaces.CauzioniManager;
import it.eurotn.panjea.cauzioni.util.parametriricerca.SituazioneCauzioniDTO;
import it.eurotn.panjea.contabilita.domain.AreaContabileLite;
import it.eurotn.panjea.contabilita.manager.interfaces.AreaContabileManager;
import it.eurotn.panjea.contabilita.service.exception.ContiBaseException;
import it.eurotn.panjea.documenti.domain.StatoSpedizione;
import it.eurotn.panjea.documenti.manager.interfaces.SpedizioneDocumentiManager;
import it.eurotn.panjea.exception.GenericException;
import it.eurotn.panjea.iva.domain.AreaIva;
import it.eurotn.panjea.iva.manager.interfaces.AreaIvaManager;
import it.eurotn.panjea.lotti.exception.RimanenzaLottiNonValidaException;
import it.eurotn.panjea.magazzino.domain.DatiGenerazione;
import it.eurotn.panjea.magazzino.domain.DatiGenerazione.TipoGenerazione;
import it.eurotn.panjea.magazzino.domain.IRigaArticoloDocumento;
import it.eurotn.panjea.magazzino.domain.NoteAreaMagazzino;
import it.eurotn.panjea.magazzino.domain.RigaArticolo;
import it.eurotn.panjea.magazzino.domain.RigaArticoloComponente;
import it.eurotn.panjea.magazzino.domain.RigaArticoloLite;
import it.eurotn.panjea.magazzino.domain.RigaMagazzino;
import it.eurotn.panjea.magazzino.domain.SedeMagazzino;
import it.eurotn.panjea.magazzino.domain.SedeMagazzino.ETipologiaCodiceIvaAlternativo;
import it.eurotn.panjea.magazzino.domain.SedeMagazzinoLite;
import it.eurotn.panjea.magazzino.domain.TrasportoCura;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino.StatoAreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzinoLite;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino.ProvenienzaPrezzo;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino.StrategiaTotalizzazioneDocumento;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino.TipoMovimento;
import it.eurotn.panjea.magazzino.domain.moduloprezzo.ParametriCalcoloPrezzi;
import it.eurotn.panjea.magazzino.domain.moduloprezzo.PoliticaPrezzo;
import it.eurotn.panjea.magazzino.exception.EsportaDocumentoCassaException;
import it.eurotn.panjea.magazzino.exception.RigaArticoloNonValidaException;
import it.eurotn.panjea.magazzino.exception.SedeNonAppartieneAdEntitaException;
import it.eurotn.panjea.magazzino.exception.TotaleDocumentoNonCoerenteException;
import it.eurotn.panjea.magazzino.importer.manager.interfaces.ImportazioneManager;
import it.eurotn.panjea.magazzino.importer.util.DocumentoImport;
import it.eurotn.panjea.magazzino.manager.documento.exporter.IExporter;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.AreaMagazzinoCancellaManager;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.AreaMagazzinoManager;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.AreaMagazzinoVerificaManager;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.FatturazioneManager;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.PreFatturazioneManager;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.RigaMagazzinoManager;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.RigheCollegateManager;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.TipiAreaMagazzinoManager;
import it.eurotn.panjea.magazzino.manager.interfaces.MagazzinoAnagraficaManager;
import it.eurotn.panjea.magazzino.manager.interfaces.MagazzinoContabilizzazioneManager;
import it.eurotn.panjea.magazzino.manager.interfaces.MagazzinoMovimentazioneManager;
import it.eurotn.panjea.magazzino.manager.interfaces.SediMagazzinoManager;
import it.eurotn.panjea.magazzino.manager.listinoprezzi.ListinoPrezziDTO;
import it.eurotn.panjea.magazzino.manager.listinoprezzi.ParametriListinoPrezzi;
import it.eurotn.panjea.magazzino.manager.listinoprezzi.interfaces.ListinoPrezziManager;
import it.eurotn.panjea.magazzino.manager.omaggio.exception.CodiceIvaPerTipoOmaggioAssenteException;
import it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.provvigioni.interfaces.RigaDocumentoVariazioneProvvigioneStrategy;
import it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.provvigioni.interfaces.TipoVariazioneProvvigioneStrategy;
import it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.sconti.interfaces.RigaDocumentoVariazioneScontoStrategy;
import it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.sconti.interfaces.TipoVariazioneScontoStrategy;
import it.eurotn.panjea.magazzino.service.exception.CategoriaContabileAssenteException;
import it.eurotn.panjea.magazzino.service.exception.ContabilizzazioneException;
import it.eurotn.panjea.magazzino.service.exception.DocumentiEsistentiPerAreaMagazzinoException;
import it.eurotn.panjea.magazzino.service.exception.DocumentoAssenteAvvisaException;
import it.eurotn.panjea.magazzino.service.exception.DocumentoAssenteBloccaException;
import it.eurotn.panjea.magazzino.service.exception.FatturazioneContabilizzazioneException;
import it.eurotn.panjea.magazzino.service.exception.QtaLottiMaggioreException;
import it.eurotn.panjea.magazzino.service.exception.RigheLottiNonValideException;
import it.eurotn.panjea.magazzino.service.exception.SedePerRifatturazioneAssenteException;
import it.eurotn.panjea.magazzino.service.interfaces.MagazzinoDocumentoService;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoFullDTO;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoFullDTOStampa;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoRicerca;
import it.eurotn.panjea.magazzino.util.MovimentoFatturazioneDTO;
import it.eurotn.panjea.magazzino.util.MovimentoPreFatturazioneDTO;
import it.eurotn.panjea.magazzino.util.ParametriCreazioneRigaArticolo;
import it.eurotn.panjea.magazzino.util.ParametriRicercaAreaMagazzino;
import it.eurotn.panjea.magazzino.util.PreFatturazioneDTO;
import it.eurotn.panjea.magazzino.util.RigaDestinazione;
import it.eurotn.panjea.magazzino.util.SedeAreaMagazzinoDTO;
import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriRegoleValidazioneRighe;
import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriRicercaFatturazione;
import it.eurotn.panjea.magazzino.util.rigamagazzino.builder.RigheMagazzinoDTOResult;
import it.eurotn.panjea.magazzino.util.rigamagazzino.builder.domain.RigaMagazzinoBuilder;
import it.eurotn.panjea.magazzino.util.rigamagazzino.builder.domain.RigaMagazzinoFactoryBuilder;
import it.eurotn.panjea.pagamenti.domain.CodicePagamento;
import it.eurotn.panjea.partite.domain.AreaPartite;
import it.eurotn.panjea.partite.domain.TipoAreaPartita;
import it.eurotn.panjea.partite.manager.interfaces.TipiAreaPartitaManager;
import it.eurotn.panjea.rate.domain.AreaRate;
import it.eurotn.panjea.rate.manager.interfaces.AreaRateManager;
import it.eurotn.panjea.rate.manager.interfaces.RateGenerator;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.panjea.sicurezza.license.PanjeaLicenseInterceptor;
import it.eurotn.security.JecPrincipal;

@Stateless(name = "Panjea.MagazzinoDocumentoService")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@RemoteBinding(jndiBinding = "Panjea.MagazzinoDocumentoService")
@Interceptors(value = PanjeaLicenseInterceptor.class)
public class MagazzinoDocumentoServiceBean implements MagazzinoDocumentoService {

    private static Logger logger = Logger.getLogger(MagazzinoDocumentoServiceBean.class);

    public static final String FATTURAZIONE_CONFERMA_MESSAGE_SELECTOR = "fatturazioneConfermaMessageSelector";

    public static final String FATTURAZIONE_CONTABILIZZAZIONE_MESSAGE_SELECTOR = "fatturazioneContabilizzazioneMessageSelector";

    @Resource
    private SessionContext sessionContext;

    @EJB
    private MagazzinoContabilizzazioneManager magazzinoContabilizzazioneManager;

    @EJB
    private RigaMagazzinoManager rigaMagazzinoManager;

    @EJB
    private TipiAreaMagazzinoManager tipiAreaMagazzinoManager;

    @EJB
    private AreaMagazzinoManager areaMagazzinoManager;

    @EJB
    private AreaRateManager areaRateManager;

    @EJB
    private TipiAreaPartitaManager tipiAreaPartitaManager;

    @EJB
    private RateGenerator rateGenerator;

    @EJB
    private AreaMagazzinoVerificaManager areaMagazzinoVerificaManager;

    @EJB
    private FatturazioneManager fatturazioneManager;

    @EJB
    @IgnoreDependency
    private PreFatturazioneManager preFatturazioneManager;

    // utilizzato solamente per fare dei flush della sessione
    @EJB
    private PanjeaDAO panjeaDAO;

    @EJB
    @IgnoreDependency
    private AreaContabileManager areaContabileManager;

    @EJB
    ListinoPrezziManager listinoPrezziManager;

    @EJB
    private MagazzinoMovimentazioneManager magazzinoMovimentazioneManager;

    @EJB
    @IgnoreDependency
    private AreaIvaManager areaIvaManager;

    @EJB
    private AreaMagazzinoCancellaManager areaMagazzinoCancellaManager;

    @EJB
    private RigheCollegateManager righeCollegateManager;

    @EJB
    private SediAziendaManager sediAziendaManager;

    @EJB
    private SediMagazzinoManager sediMagazzinoManager;

    @EJB
    private MagazzinoAnagraficaManager magazzinoAnagraficaManager;

    @EJB
    private CauzioniManager cauzioniManager;

    @EJB
    private SediEntitaManager sediEntitaManager;
    @EJB
    private ImportazioneManager importazioneManager;

    @EJB
    private SpedizioneDocumentiManager spedizioneDocumentiManager;

    @EJB
    @IgnoreDependency
    private AziendeManager aziendeManager;

    @EJB
    private DepositiManager depositiManager;

    @TransactionTimeout(value = 7200)
    @Override
    public void aggiornaPrezzoRighe(List<RigaArticoloLite> righe) {

        Map<Integer, AreaMagazzinoFullDTO> aree = new HashMap<Integer, AreaMagazzinoFullDTO>();

        // modifico i prezzi delle righe
        for (RigaArticoloLite rigaArticoloLite : righe) {
            boolean rigaAggiornata = rigaMagazzinoManager.aggiornaPrezzoRiga(rigaArticoloLite);
            if (!aree.containsKey(rigaArticoloLite.getAreaMagazzino().getId()) && rigaAggiornata) {
                AreaMagazzinoFullDTO areaMagazzinoFullDTO = caricaAreaMagazzinoFullDTO(
                        rigaArticoloLite.getAreaMagazzino().creaProxyAreaMagazzino());
                aree.put(areaMagazzinoFullDTO.getAreaMagazzino().getId(), areaMagazzinoFullDTO);
            }
        }

        // Confermo di nuovo tutti i documenti
        for (AreaMagazzinoFullDTO areaMagazzinoFullDTO : aree.values()) {
            try {
                validaRigheMagazzino(areaMagazzinoFullDTO.getAreaMagazzino(), areaMagazzinoFullDTO.getAreaRate(),
                        areaMagazzinoFullDTO.getAreaContabileLite() != null, true);
            } catch (TotaleDocumentoNonCoerenteException e) {
                throw new GenericException("Eccezione lanciata in uno stato del documento anomalo");
            }
        }
    }

    @Override
    public RigaArticoloComponente aggiungiRigaComponente(Integer idArticolo, double qta, RigaArticolo rigaDistinta) {
        return rigaMagazzinoManager.aggiungiRigaComponente(idArticolo, qta, rigaDistinta);
    }

    @Override
    public void aggiungiVariazione(Integer idAreaMagazzino, BigDecimal variazione, BigDecimal percProvvigione,
            RigaDocumentoVariazioneScontoStrategy variazioneScontoStrategy,
            TipoVariazioneScontoStrategy tipoVariazioneScontoStrategy,
            RigaDocumentoVariazioneProvvigioneStrategy variazioneProvvigioneStrategy,
            TipoVariazioneProvvigioneStrategy tipoVariazioneProvvigioneStrategy) {
        areaMagazzinoManager.aggiungiVariazione(idAreaMagazzino, variazione, percProvvigione, variazioneScontoStrategy,
                tipoVariazioneScontoStrategy, variazioneProvvigioneStrategy, tipoVariazioneProvvigioneStrategy);
    }

    @Override
    public List<RigaArticoloLite> applicaRegoleValidazione(
            ParametriRegoleValidazioneRighe parametriRegoleValidazioneRighe) {
        return rigaMagazzinoManager.applicaRegoleValidazione(parametriRegoleValidazioneRighe);
    }

    @Override
    public PoliticaPrezzo calcolaPrezzoArticolo(final ParametriCalcoloPrezzi parametriCalcoloPrezzi) {
        return rigaMagazzinoManager.calcolaPrezzoArticolo(parametriCalcoloPrezzi);
    }

    @Override
    @RolesAllowed("gestioneDocMagazzino")
    public AreaMagazzino cambiaStatoDaConfermatoInProvvisorio(AreaMagazzino areaMagazzino) {
        return areaMagazzinoManager.cambiaStatoDaConfermatoInProvvisorio(areaMagazzino);
    }

    @Override
    @RolesAllowed("gestioneDocMagazzino")
    public AreaMagazzino cambiaStatoDaProvvisorioInConfermato(AreaMagazzino areaMagazzino) {
        return areaMagazzinoManager.cambiaStatoDaProvvisorioInConfermato(areaMagazzino);
    }

    @Override
    @RolesAllowed("gestioneDocMagazzino")
    public AreaMagazzino cambiaStatoDaProvvisorioInForzato(AreaMagazzino areaMagazzino) {
        return areaMagazzinoManager.cambiaStatoDaProvvisorioInForzato(areaMagazzino);
    }

    @Override
    public StatoSpedizione cambiaStatoSpedizioneMovimento(IAreaDocumento areaDocumento) {
        return spedizioneDocumentiManager.cambiaStatoSpedizioneMovimento(areaDocumento);
    }

    @Override
    public void cancellaAreaMagazzino(AreaMagazzino areaMagazzino)
            throws DocumentiCollegatiPresentiException, TipoDocumentoBaseException, AreeCollegatePresentiException {
        areaMagazzinoCancellaManager.cancellaAreaMagazzino(areaMagazzino);
    }

    @Override
    @RolesAllowed("gestioneDocMagazzino")
    public void cancellaAreaMagazzino(AreaMagazzino areaMagazzino, boolean deleteAreeCollegate,
            boolean forceDeleteAreeCollegate) throws DocumentiCollegatiPresentiException, TipoDocumentoBaseException,
                    AreeCollegatePresentiException {
        areaMagazzinoCancellaManager.cancellaAreaMagazzino(areaMagazzino, deleteAreeCollegate,
                forceDeleteAreeCollegate);
    }

    @TransactionAttribute(TransactionAttributeType.NEVER)
    @Override
    @RolesAllowed("gestioneDocMagazzino")
    public void cancellaAreeMagazzino(List<AreaMagazzino> areeMagazzino, boolean deleteAreeCollegate)
            throws DocumentiCollegatiPresentiException, TipoDocumentoBaseException, AreeCollegatePresentiException {
        for (AreaMagazzino areaMagazzino : areeMagazzino) {
            cancellaAreaMagazzino(areaMagazzino, deleteAreeCollegate, false);
        }
    }

    @TransactionAttribute(TransactionAttributeType.NEVER)
    @Override
    @RolesAllowed("gestioneFatturazione")
    public void cancellaMovimentiInFatturazione(String utente) {
        fatturazioneManager.cancellaMovimentiInFatturazione(utente);
    }

    @Override
    @RolesAllowed("gestioneDocMagazzino")
    public AreaMagazzino cancellaRigaMagazzino(RigaMagazzino rigaMagazzino) {
        return areaMagazzinoCancellaManager.cancellaRigaMagazzino(rigaMagazzino);
    }

    @Override
    public AreaMagazzino cancellaRigheMagazzino(List<RigaMagazzino> righeMagazzino) {
        return areaMagazzinoCancellaManager.cancellaRigheMagazzino(righeMagazzino);
    }

    @Override
    public void cancellaTipoAreaMagazzino(TipoAreaMagazzino tipoAreaMagazzino) {
        tipiAreaMagazzinoManager.cancellaTipoAreaMagazzino(tipoAreaMagazzino);
    }

    @Override
    public AreaMagazzino caricaAreaMagazzino(AreaMagazzino areaMagazzino) {
        return areaMagazzinoManager.caricaAreaMagazzino(areaMagazzino);
    }

    @Override
    @RolesAllowed("visualizzaDocMagazzino")
    public AreaMagazzinoFullDTO caricaAreaMagazzinoFullDTO(AreaMagazzino areaMagazzino) {
        logger.debug("--> Enter caricaAreaMagazzinoFullDTO");
        AreaMagazzinoFullDTO areaMagazzinoFullDTO = areaMagazzinoManager.caricaAreaMagazzinoFullDTO(areaMagazzino);

        AreaMagazzino areaMagazzinoLoad = areaMagazzinoFullDTO.getAreaMagazzino();
        // caricamento e set di areaPartite
        AreaRate areaRateLoad = areaRateManager.caricaAreaRate(areaMagazzinoLoad.getDocumento());

        // Carico gli agenti
        if (areaMagazzinoLoad.getTipoAreaMagazzino().isGestioneAgenti()) {
            areaMagazzinoFullDTO.setAgenti(areaMagazzinoManager.caricaAgentiPerAreaMagazzino(areaMagazzino.getId()));
        }

        // set di AreaIva
        if (areaMagazzinoLoad.getDocumento().getTipoDocumento().isRigheIvaEnable()) {
            areaMagazzinoFullDTO.setAreaIva(areaIvaManager.caricaAreaIvaByDocumento(areaMagazzinoLoad.getDocumento()));
            // allinea l'attributo documento di AreaIva salvata in precedenza con il
            // nuovo attributo documento in areaMagazzino
            areaMagazzinoFullDTO.getAreaIva().setDocumento(areaMagazzinoFullDTO.getAreaMagazzino().getDocumento());
            areaMagazzinoFullDTO.setRiepilogoIva(areaMagazzinoFullDTO.getAreaIva().getRigheIva());
        } else {
            if (areaMagazzinoLoad.getTipoAreaMagazzino().isRiepilogoIva()) {
                // Devo calcolare le righe iva
                areaMagazzinoFullDTO
                        .setRiepilogoIva(areaIvaManager.generaRigheIvaRiepilogo(areaMagazzinoLoad, areaRateLoad));
            }
        }

        // inizializzazione di enableAreaPartite
        areaMagazzinoFullDTO.setAreaRateEnabled(areaRateLoad.getId() != null);
        areaMagazzinoFullDTO.setAreaRate(areaRateLoad);

        // Carica un eventuale area contabile
        AreaContabileLite areaContabileLite = areaContabileManager
                .caricaAreaContabileLiteByDocumento(areaMagazzinoLoad.getDocumento());
        areaMagazzinoFullDTO.setAreaContabileLite(areaContabileLite);

        try {
            areaMagazzinoFullDTO.setAreaContabileEnabled(areaContabileManager.isTipoAreaPresente(
                    areaMagazzinoFullDTO.getAreaMagazzino().getTipoAreaMagazzino().getTipoDocumento().getId()));
        } catch (AnagraficaServiceException e) {
            throw new RuntimeException("Errore durante il caricamento del tipo area contabile", e);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("--> Exit caricaAreaMagazzinoFullDTO " + areaMagazzinoFullDTO);
        }
        return areaMagazzinoFullDTO;
    }

    @Override
    public AreaMagazzinoFullDTOStampa caricaAreaMagazzinoFullDTO(Map<Object, Object> parametri) {
        int id = (Integer) parametri.get("id");
        AreaMagazzino areaMagazzino = new AreaMagazzino();
        areaMagazzino.setId(id);

        AreaMagazzinoFullDTO areaMagazzinoFullDTO = caricaAreaMagazzinoFullDTO(areaMagazzino);
        @SuppressWarnings("unchecked")
        List<RigaMagazzino> righeMagazzino = (List<RigaMagazzino>) caricaRigheMagazzinoStampa(areaMagazzino);
        righeMagazzino = convertRigheMagazzinoToDomain(righeMagazzino);

        areaMagazzinoFullDTO.setRigheMagazzino(righeMagazzino);

        int maxNumeroDecimaliQta = 0;
        int maxNumeroDecimaliPrezzo = 0;
        for (RigaMagazzino rigaMagazzino : areaMagazzinoFullDTO.getRigheMagazzino()) {
            if (rigaMagazzino instanceof RigaArticolo) {
                ((RigaArticolo) rigaMagazzino).getRigheLotto().size();
                if (maxNumeroDecimaliQta < ((RigaArticolo) rigaMagazzino).getNumeroDecimaliQta()) {
                    maxNumeroDecimaliQta = ((RigaArticolo) rigaMagazzino).getNumeroDecimaliQta();
                }
                if (maxNumeroDecimaliPrezzo < ((RigaArticolo) rigaMagazzino).getNumeroDecimaliPrezzo()) {
                    maxNumeroDecimaliPrezzo = ((RigaArticolo) rigaMagazzino).getNumeroDecimaliPrezzo();
                }
            }
        }

        SedeAzienda sedeAzienda = null;
        Deposito depositoOrigine = null;
        if (areaMagazzinoFullDTO.getAreaMagazzino().getDepositoOrigine() != null
                && areaMagazzinoFullDTO.getAreaMagazzino().getDepositoOrigine().getId() != null) {
            depositoOrigine = depositiManager
                    .caricaDeposito(areaMagazzinoFullDTO.getAreaMagazzino().getDepositoOrigine().getId());
            sedeAzienda = depositoOrigine.getSedeDeposito();
            sedeAzienda.getAzienda().getDenominazione();
        }
        Deposito deposito = null;
        if (areaMagazzinoFullDTO.getAreaMagazzino().getDepositoDestinazione() != null
                && areaMagazzinoFullDTO.getAreaMagazzino().getDepositoDestinazione().getId() != null) {
            deposito = depositiManager
                    .caricaDeposito(areaMagazzinoFullDTO.getAreaMagazzino().getDepositoDestinazione().getId());
        }

        TrasportoCura trasportoCura = null;
        String areaMagTrasportoCura = areaMagazzinoFullDTO.getAreaMagazzino().getTrasportoCura();
        if (areaMagTrasportoCura != null && !areaMagTrasportoCura.isEmpty()) {
            trasportoCura = magazzinoAnagraficaManager.caricaTrasportoCuraByDescrizione(areaMagTrasportoCura);
        }

        String noteEntitaStampa = null;
        if (areaMagazzinoFullDTO.getAreaMagazzino().getDocumento().getEntita() != null) {
            noteEntitaStampa = sediEntitaManager
                    .caricaNoteStampa(areaMagazzinoFullDTO.getAreaMagazzino().getDocumento().getSedeEntita().getId());
        }

        SedeEntita sedeCollegata = null;
        SedeMagazzino sm = null;
        if (areaMagazzinoFullDTO.getAreaMagazzino().getDocumento().getSedeEntita() != null) {
            sedeCollegata = areaMagazzinoFullDTO.getAreaMagazzino().getDocumento().getSedeEntita().getSedeCollegata();
            sm = areaMagazzinoFullDTO.getAreaMagazzino().getDocumento().getSedeEntita().getSedeMagazzino();
        }
        if (sm != null && sedeCollegata == null) {
            if (sm.getSedePerRifatturazione() != null && areaMagazzinoFullDTO.getAreaMagazzino().isContoTerzi()) {
                try {
                    sedeCollegata = sediEntitaManager
                            .caricaSedeEntita(sm.getSedePerRifatturazione().getSedeEntita().getId());
                } catch (AnagraficaServiceException e) {
                    logger.error("-->errore nel caricare la sede entità per rifatturazione", e);
                    throw new RuntimeException(e);
                }
            }
        }

        AziendaLite aziendaLite = aziendeManager.caricaAzienda();
        Azienda az = new Azienda();
        az.setId(aziendaLite.getId());
        SedeAzienda azienda = null;
        try {
            azienda = sediAziendaManager.caricaSedePrincipaleAzienda(az);
            azienda.getAzienda().getDenominazione();
        } catch (AnagraficaServiceException e) {
            logger.error("--> errore nel caricare la sede principale dell'azienda.", e);
            throw new RuntimeException("errore nel caricare la sede principale dell'azienda.", e);
        }

        AreaMagazzinoFullDTOStampa areaMagazzinoFullDTOStampa = new AreaMagazzinoFullDTOStampa(areaMagazzinoFullDTO,
                sedeAzienda, deposito, depositoOrigine, trasportoCura, sedeCollegata, azienda);

        if (sedeCollegata != null && sm != null && sm.getSedePerRifatturazione() != null
                && areaMagazzinoFullDTO.getAreaMagazzino().isContoTerzi()) {
            areaMagazzinoFullDTOStampa
                    .setDestinatarioDenominazione(sedeCollegata.getEntita().getAnagrafica().getDenominazione());
        }
        areaMagazzinoFullDTOStampa.setMaxNumeroDecimaliQta(maxNumeroDecimaliQta);
        areaMagazzinoFullDTOStampa.setMaxNumeroDecimaliPrezzo(maxNumeroDecimaliPrezzo);
        areaMagazzinoFullDTOStampa.setNoteEntita(noteEntitaStampa);

        // aggiungo la situazione cauzioni
        if (parametri.containsKey("cauzioni")) {
            List<SituazioneCauzioniDTO> situazioneCauzioni = new ArrayList<SituazioneCauzioniDTO>();
            situazioneCauzioni = cauzioniManager.caricaSituazioneCauzioniAreaMagazzino(areaMagazzino);
            areaMagazzinoFullDTOStampa.addSituazioneCauzioni(situazioneCauzioni);
        }

        // Se l'entità ha una sede di rifatturazione,carico la sede e la
        // sostituisco a quella dell'entità

        return areaMagazzinoFullDTOStampa;
    }

    @Override
    public AreaMagazzinoFullDTO caricaAreaMagazzinoFullDTOByDocumento(Documento documento) {
        AreaMagazzino areaMagazzino = areaMagazzinoManager.caricaAreaMagazzinoByDocumento(documento);
        return caricaAreaMagazzinoFullDTO(areaMagazzino);
    }

    @Override
    public List<AreaMagazzinoLite> caricaAreeMagazzino(ParametriRicercaFatturazione parametriRicercaFatturazione) {
        return fatturazioneManager.caricaAreeMagazzino(parametriRicercaFatturazione);
    }

    @Override
    public List<AreaMagazzinoLite> caricaAreeMagazzinoCollegate(List<AreaMagazzino> areeMagazzino) {
        return areaMagazzinoManager.caricaAreeMagazzinoCollegate(areeMagazzino);
    }

    @Override
    public List<AreaMagazzinoRicerca> caricaAreeMagazzinoConRichiestaDatiAccompagnatori(Date dataEvasione) {
        return areaMagazzinoManager.caricaAreeMagazzinoConRichiestaDatiAccompagnatori(dataEvasione);
    }

    @Override
    public List<AreaMagazzinoRicerca> caricaAreeMagazzinoConRichiestaDatiAccompagnatori(List<Integer> idAree) {
        return areaMagazzinoManager.caricaAreeMagazzinoConRichiestaDatiAccompagnatori(idAree);
    }

    @Override
    public List<AreaMagazzinoRicerca> caricaAreeMAgazzinoDaContabilizzare(TipoGenerazione tipoGenerazione, int anno) {
        return magazzinoContabilizzazioneManager.caricaAreeMAgazzinoDaContabilizzare(tipoGenerazione, anno);
    }

    @Override
    public CodiceIva caricaCodiceIvaPerSostituzione(IRigaArticoloDocumento rigaArticolo)
            throws CodiceIvaPerTipoOmaggioAssenteException {
        return rigaMagazzinoManager.caricaCodiceIvaPerSostituzione(rigaArticolo);
    }

    @Override
    public Date caricaDataMovimentiInFatturazione() {
        return fatturazioneManager.caricaDataMovimentiInFatturazione();
    }

    @Override
    public List<DatiGenerazione> caricaDatiGenerazioneFatturazioneTemporanea() {
        return fatturazioneManager.caricaDatiGenerazioneFatturazioneTemporanea();
    }

    @Override
    public Collection<DocumentoImport> caricaDocumenti(String codiceImporter, byte[] fileImport) {
        return importazioneManager.caricaDocumenti(codiceImporter, fileImport);
    }

    @Override
    public List<DatiGenerazione> caricaFatturazioni(int annoFatturazione) {
        return fatturazioneManager.caricaFatturazioni(annoFatturazione);
    }

    @Override
    public List<Integer> caricaIdAreeMagazzinoPerStampaEvasione(List<AreaMagazzinoRicerca> aree) {
        return areaMagazzinoManager.caricaIdAreeMagazzinoPerStampaEvasione(aree);
    }

    @Override
    public List<String> caricaImporter() {
        return importazioneManager.caricaImporter();
    }

    @Override
    @TransactionTimeout(value = 7200)
    public List<ListinoPrezziDTO> caricaListinoPrezzi(ParametriListinoPrezzi parametriListinoPrezzi) {
        return listinoPrezziManager.caricaListinoPrezzi(parametriListinoPrezzi);
    }

    @Override
    public List<AreaMagazzinoLite> caricaMovimentiPerFatturazione(Date dataCreazione) {
        return fatturazioneManager.caricaMovimentiPerFatturazione(dataCreazione);
    }

    @TransactionAttribute(TransactionAttributeType.NEVER)
    @Override
    public List<MovimentoFatturazioneDTO> caricaMovimentPerFatturazione(Date dataCreazione, String utente) {
        return fatturazioneManager.caricaMovimentPerFatturazione(dataCreazione, utente);
    }

    @Override
    public NoteAreaMagazzino caricaNoteSede(SedeEntita sedeEntita) {
        return sediMagazzinoManager.caricaNoteSede(sedeEntita);
    }

    @Override
    @RolesAllowed("visualizzaDocMagazzino")
    public RigaMagazzino caricaRigaMagazzino(RigaMagazzino rigaMagazzino) {
        return rigaMagazzinoManager.getDao(rigaMagazzino).caricaRigaMagazzino(rigaMagazzino);
    }

    @Override
    @RolesAllowed("visualizzaDocMagazzino")
    public List<? extends RigaMagazzino> caricaRigheMagazzino(AreaMagazzino areaMagazzino) {
        return rigaMagazzinoManager.getDao().caricaRigheMagazzino(areaMagazzino);
    }

    @Override
    public List<RigaDestinazione> caricaRigheMagazzinoCollegate(RigaMagazzino rigaMagazzino) {
        return righeCollegateManager.caricaRigheMagazzinoCollegate(rigaMagazzino);
    }

    @Override
    @RolesAllowed("visualizzaDocMagazzino")
    public RigheMagazzinoDTOResult caricaRigheMagazzinoDTO(AreaMagazzino areaMagazzino) {
        return rigaMagazzinoManager.getDao().caricaRigheMagazzinoDTO(areaMagazzino);
    }

    @Override
    public List<? extends RigaMagazzino> caricaRigheMagazzinoStampa(AreaMagazzino areaMagazzino) {
        return rigaMagazzinoManager.getDao().caricaRigheMagazzinoStampa(areaMagazzino);
    }

    @Override
    public SedeAreaMagazzinoDTO caricaSedeAreaMagazzinoDTO(SedeEntita sedeEntita) {
        return areaMagazzinoManager.caricaSedeAreaMagazzinoDTO(sedeEntita);
    }

    @Override
    public List<TipoAreaMagazzino> caricaTipiAreaMagazzino(String fieldSearch, String valueSearch,
            boolean loadTipiDocumentoDisabilitati) {
        return tipiAreaMagazzinoManager.caricaTipiAreaMagazzino(fieldSearch, valueSearch,
                loadTipiDocumentoDisabilitati);
    }

    @Override
    public List<TipoDocumento> caricaTipiDocumentiMagazzino() {
        return tipiAreaMagazzinoManager.caricaTipiDocumentiMagazzino();
    }

    @Override
    public List<TipoDocumento> caricaTipiDocumentoAnagraficaPerFatturazione() {
        return tipiAreaMagazzinoManager.caricaTipiDocumentoAnagraficaPerFatturazione();
    }

    @Override
    @RolesAllowed("gestioneFatturazione")
    public List<TipoDocumento> caricaTipiDocumentoDestinazioneFatturazione() {
        return fatturazioneManager.caricaTipiDocumentoDestinazioneFatturazione();
    }

    @Override
    @RolesAllowed("gestioneFatturazione")
    public List<TipoDocumento> caricaTipiDocumentoPerFatturazione(TipoDocumento tipoDocumentoDiFatturazione) {
        return fatturazioneManager.caricaTipiDocumentoPerFatturazione(tipoDocumentoDiFatturazione);
    }

    @Override
    public TipoAreaMagazzino caricaTipoAreaMagazzino(Integer id) {
        return tipiAreaMagazzinoManager.caricaTipoAreaMagazzino(id);
    }

    @Override
    public TipoAreaMagazzino caricaTipoAreaMagazzinoPerTipoDocumento(Integer idTipoDocumento) {
        return tipiAreaMagazzinoManager.caricaTipoAreaMagazzinoPerTipoDocumento(idTipoDocumento);
    }

    @Override
    public TipoAreaPartita caricaTipoAreaPartitaPerTipoDocumento(TipoDocumento tipoDocumento) {
        return tipiAreaPartitaManager.caricaTipoAreaPartitaPerTipoDocumento(tipoDocumento);
    }

    @Override
    public AreaMagazzinoLite caricaUltimoInventario(Integer idDeposito) {
        return magazzinoMovimentazioneManager.caricaUltimoInventario(idDeposito);
    }

    @Override
    public void collegaRigaMagazzino(List<RigaMagazzino> righeDaCollegare, AreaMagazzino areaMagazzino,
            boolean ordinamento) {
        righeCollegateManager.collegaRigaMagazzino(righeDaCollegare, areaMagazzino, ordinamento);
    }

    @Override
    public void collegaTestata(Set<Integer> righeMagazzinoDaCambiare) {
        logger.debug("--> Enter collegaTestata");
        rigaMagazzinoManager.collegaTestata(righeMagazzinoDaCambiare);
        logger.debug("--> Exit collegaTestata");
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.NEVER)
    @RolesAllowed("gestioneFatturazione")
    public DatiGenerazione confermaMovimentiInFatturazione(String utente)
            throws FatturazioneContabilizzazioneException {
        logger.debug(
                "--> Inizio conferma codumenti in fatturazione. ---------------------------------------------------------------------------");

        List<MovimentoFatturazioneDTO> listMovimentiDTO = fatturazioneManager.caricaMovimentPerFatturazione(null,
                utente);

        // npe da mail
        if (listMovimentiDTO == null || listMovimentiDTO.isEmpty()) {
            return null;
        }

        List<AreaMagazzino> areeDaConfermare = new ArrayList<AreaMagazzino>();
        for (MovimentoFatturazioneDTO movimentoFatturazioneDTO : listMovimentiDTO) {
            areeDaConfermare.add(areaMagazzinoManager.caricaAreaMagazzino(movimentoFatturazioneDTO.getAreaMagazzino()));
        }
        DatiGenerazione datiGenerazione = fatturazioneManager.confermaMovimentiInFatturazione(areeDaConfermare);

        String uuid = areeDaConfermare.get(0).getUUIDContabilizzazione();

        try {
            magazzinoContabilizzazioneManager.contabilizzaAreeMagazzino(uuid,
                    FATTURAZIONE_CONTABILIZZAZIONE_MESSAGE_SELECTOR, false);
        } catch (ContiBaseException e) {
            throw new RuntimeException(e);
        } catch (ContabilizzazioneException e1) {
            throw new FatturazioneContabilizzazioneException(datiGenerazione, e1);
        }

        return datiGenerazione;
    }

    @Override
    public void contabilizzaAreeMagazzino(List<Integer> idAreeMagazzino, boolean forzaGenerazioneAreaContabile)
            throws ContiBaseException, CategoriaContabileAssenteException, ContabilizzazioneException {
        magazzinoContabilizzazioneManager.contabilizzaAreeMagazzino(idAreeMagazzino, forzaGenerazioneAreaContabile);
    }

    @TransactionAttribute(TransactionAttributeType.NEVER)
    @Override
    public void contabilizzaAreeMagazzino(List<Integer> idAreeMagazzino, boolean forzaGenerazioneAreaContabile,
            int annoContabile)
                    throws ContiBaseException, CategoriaContabileAssenteException, ContabilizzazioneException {
        magazzinoContabilizzazioneManager.contabilizzaAreeMagazzino(idAreeMagazzino, forzaGenerazioneAreaContabile,
                annoContabile);
    }

    /**
     * Si preoccupa di convertire ed eventualmente raggruppare eventuali righe dove l'articolo padre
     * e il prezzo sono gli stessi.<br>
     * Nota che per il raggruppamento vengono unite le righe contigue con padre e prezzo uguali.
     *
     * @param righeMagazzino
     *            la lista di RigaMagazzinoBuilder da scorrere e raggruppare
     * @return List<RigaMagazzino>
     */
    private List<RigaMagazzino> convertRigheMagazzinoToDomain(List<RigaMagazzino> righeMagazzino) {

        Map<String, RigaMagazzino> righeComposte = new HashMap<String, RigaMagazzino>();
        List<RigaMagazzino> result = new ArrayList<RigaMagazzino>();

        RigaMagazzinoFactoryBuilder factoryBuilder = new RigaMagazzinoFactoryBuilder();

        for (RigaMagazzino rigaMagazzinoResult : righeMagazzino) {
            RigaMagazzinoBuilder dtoBuilder = factoryBuilder.getBuilder(rigaMagazzinoResult);
            dtoBuilder.fillResult(rigaMagazzinoResult, result, righeComposte);
        }

        return result;
    }

    @Override
    public RigaArticolo creaRigaArticolo(ParametriCreazioneRigaArticolo parametriCreazioneRigaArticolo) {
        return (RigaArticolo) rigaMagazzinoManager.getDao(parametriCreazioneRigaArticolo)
                .creaRigaArticolo(parametriCreazioneRigaArticolo);
    }

    @Override
    public boolean creaRigaNoteAutomatica(AreaMagazzino areaMagazzino, String note) {
        return rigaMagazzinoManager.creaRigaNoteAutomatica(areaMagazzino, note);
    }

    @Override
    public void esportaDocumento(AreaMagazzino areaMagazzino) throws EsportaDocumentoCassaException {
        logger.debug("--> Enter esportaDocumento");

        if (areaMagazzino.getTipoAreaMagazzino().getStrategiaEsportazioneFlusso() == null) {
            throw new RuntimeException("Strategia esportazione flusso a null, impossibile esportare il documento");
        }

        IExporter exporter = (IExporter) sessionContext
                .lookup(areaMagazzino.getTipoAreaMagazzino().getStrategiaEsportazioneFlusso().getJndiNameExporter());
        if (exporter == null) {
            logger.warn("L'exporter non è stato trovato nell'albero jndi. Indirizzo jndi:"
                    + areaMagazzino.getTipoAreaMagazzino().getStrategiaEsportazioneFlusso().getJndiNameExporter());
        } else {
            exporter.esporta(areaMagazzino);
        }

        logger.debug("--> Exit esportaDocumento");
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.NEVER)
    @RolesAllowed("gestioneFatturazione")
    public void generaFatturazioneDifferitaTemporanea(List<AreaMagazzinoLite> areeDaFatturare,
            TipoDocumento tipoDocumentoDestinazione, Date dataDocumentoDestinazione, String noteFatturazione,
            SedeMagazzinoLite sedePerRifatturazione) throws RigaArticoloNonValidaException,
                    SedePerRifatturazioneAssenteException, SedeNonAppartieneAdEntitaException {
        fatturazioneManager.genera(areeDaFatturare, tipoDocumentoDestinazione, dataDocumentoDestinazione,
                noteFatturazione, sedePerRifatturazione, getPrincipal().getUserName());
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.NEVER)
    @RolesAllowed("gestioneFatturazione")
    public PreFatturazioneDTO generaPreFatturazione(List<AreaMagazzinoLite> areeDaFatturare,
            TipoDocumento tipoDocumentoDestinazione, Date dataDocumentoDestinazione, String noteFatturazione,
            SedeMagazzinoLite sedePerRifatturazione) throws RigaArticoloNonValidaException,
                    SedePerRifatturazioneAssenteException, SedeNonAppartieneAdEntitaException {

        List<MovimentoPreFatturazioneDTO> movimenti = new ArrayList<MovimentoPreFatturazioneDTO>();

        // creo un utente random per eseguire la prefatturazione
        String utente = RandomStringUtils.random(15, true, true);

        // creo i documenti temporanei della fatturaizone
        fatturazioneManager.genera(areeDaFatturare, tipoDocumentoDestinazione, dataDocumentoDestinazione,
                noteFatturazione, sedePerRifatturazione, utente);

        // carico i documenti creati
        movimenti = preFatturazioneManager.caricaMovimentPreFatturazione(utente);

        // cancello la fatturazione temporanea
        fatturazioneManager.cancellaMovimentiInFatturazione(utente);

        PreFatturazioneDTO preFatturazioneDTO = new PreFatturazioneDTO();
        preFatturazioneDTO.setMovimenti(movimenti);

        return preFatturazioneDTO;
    }

    /**
     * Restituisce il nome dell'utente.
     *
     * @return nome
     */
    private JecPrincipal getPrincipal() {
        return (JecPrincipal) sessionContext.getCallerPrincipal();
    }

    @Override
    public List<AreaMagazzinoRicerca> importaDocumenti(Collection<DocumentoImport> documenti, String codiceImporter) {
        return importazioneManager.importaDocumenti(documenti, codiceImporter);
    }

    @Override
    public void impostaComeEsportato(DatiGenerazione datiGenerazione) {
        fatturazioneManager.impostaComeEsportato(datiGenerazione);
    }

    @Override
    public void inserisciRaggruppamentoArticoli(Integer idAreaMagazzino, ProvenienzaPrezzo provenienzaPrezzo,
            Integer idRaggruppamentoArticoli, Date data, Integer idSedeEntita, Integer idListinoAlternativo,
            Integer idListino, Importo importo, CodiceIva codiceIvaAlternativo, Integer idTipoMezzo,
            Integer idZonaGeografica, boolean noteSuDestinazione, TipoMovimento tipoMovimento, String codiceValuta,
            String codiceLingua, Integer idAgente, ETipologiaCodiceIvaAlternativo tipologiaCodiceIvaAlternativo,
            BigDecimal percentualeScontoCommerciale)
                    throws RimanenzaLottiNonValidaException, RigheLottiNonValideException, QtaLottiMaggioreException {
        rigaMagazzinoManager.inserisciRaggruppamentoArticoli(idAreaMagazzino, provenienzaPrezzo,
                idRaggruppamentoArticoli, data, idSedeEntita, idListinoAlternativo, idListino, importo,
                codiceIvaAlternativo, idTipoMezzo, idZonaGeografica, noteSuDestinazione, tipoMovimento, codiceValuta,
                codiceLingua, idAgente, tipologiaCodiceIvaAlternativo, percentualeScontoCommerciale);
    }

    @Override
    public AreaMagazzinoFullDTO ricalcolaPrezziMagazzino(Integer idAreaMagazzino) {
        // carico l'area ordine
        AreaMagazzino areaMagazzino = areaMagazzinoManager.ricalcolaPrezziMagazzino(idAreaMagazzino);

        try {
            return caricaAreaMagazzinoFullDTO(areaMagazzino);
        } catch (Exception e) {
            logger.error("--> errore durante il caricamento dell'area magazzino full dto.", e);
            throw new RuntimeException("errore durante il caricamento dell'area magazzino full dto.", e);
        }
    }

    @Override
    public List<AreaMagazzinoRicerca> ricercaAreeMagazzino(
            ParametriRicercaAreaMagazzino paramentriRicercaAreaMagazzino) {
        return areaMagazzinoManager.ricercaAreeMagazzino(paramentriRicercaAreaMagazzino);
    }

    @Override
    public List<Documento> ricercaDocumentiSenzaAreaMagazzino(Documento documento, boolean soloAttributiNotNull) {
        return areaMagazzinoVerificaManager.ricercaDocumentiSenzaAreaMagazzino(documento, soloAttributiNotNull);
    }

    @Override
    @RolesAllowed("gestioneDocMagazzino")
    public AreaMagazzinoFullDTO salvaAreaMagazzino(AreaMagazzino areaMagazzino, AreaRate areaRate,
            boolean forzaSalvataggio) throws DocumentoAssenteAvvisaException, DocumentoAssenteBloccaException,
                    DocumentiEsistentiPerAreaMagazzinoException {
        logger.debug("--> Enter salvaAreaMagazzino");

        boolean cambioSconto = areaRateManager.checkCambioScontoCommerciale(areaRate);
        if (cambioSconto) {
            CodicePagamento codicePagamento = areaRate.getCodicePagamento();
            BigDecimal scontoComm = (codicePagamento != null) ? codicePagamento.getPercentualeScontoCommerciale()
                    : BigDecimal.ZERO;
            rigaMagazzinoManager.aggiornaScontoCommerciale(areaMagazzino, scontoComm);
        }

        boolean cambioStato = false;
        cambioStato = areaMagazzinoVerificaManager.checkCambioStato(areaMagazzino, areaRate);
        // se devo cambiare lo stato vado anche a svalidare le righe magazzino
        if (cambioStato) {
            areaMagazzino.getDatiValidazioneRighe().invalida();
            areaMagazzino = cambiaStatoDaConfermatoInProvvisorio(areaMagazzino);
        }

        AreaMagazzino areaMagazzinoSave = areaMagazzinoManager.salvaAreaMagazzino(areaMagazzino, forzaSalvataggio);

        if (areaRate != null) {
            // aggiorna AreaPartite con il documento salvato
            areaRate.setDocumento(areaMagazzinoSave.getDocumento());
            areaRateManager.salvaAreaRate(areaRate);
        }

        AreaMagazzinoFullDTO areaMagazzinoFullDTO = caricaAreaMagazzinoFullDTO(areaMagazzinoSave);

        logger.debug("--> Exit salvaAreaMagazzino");
        return areaMagazzinoFullDTO;
    }

    @Override
    @RolesAllowed("gestioneDocMagazzino")
    public RigaMagazzino salvaRigaMagazzino(RigaMagazzino rigaMagazzino)
            throws RigheLottiNonValideException, RimanenzaLottiNonValidaException, QtaLottiMaggioreException {
        try {
            return rigaMagazzinoManager.getDao(rigaMagazzino).salvaRigaMagazzino(rigaMagazzino);

        } catch (RimanenzaLottiNonValidaException e) {
            sessionContext.setRollbackOnly();
            throw e;
        } catch (RigheLottiNonValideException e) {
            sessionContext.setRollbackOnly();
            throw e;
        } catch (QtaLottiMaggioreException e) {
            sessionContext.setRollbackOnly();
            throw e;
        }
    }

    @Override
    public TipoAreaMagazzino salvaTipoAreaMagazzino(TipoAreaMagazzino tipoAreaMagazzino) {
        return tipiAreaMagazzinoManager.salvaTipoAreaMagazzino(tipoAreaMagazzino);
    }

    @Override
    public void spostaRighe(Set<Integer> righeDaSpostare, Integer idDest) {
        areaMagazzinoManager.spostaRighe(righeDaSpostare, idDest);
    }

    @Override
    public AreaMagazzino totalizzaDocumento(StrategiaTotalizzazioneDocumento strategia, AreaMagazzino areaMagazzino,
            AreaPartite areaPartite) {
        return areaMagazzinoManager.totalizzaDocumento(strategia, areaMagazzino, areaPartite);
    }

    @Override
    public AreaMagazzinoFullDTO validaAreaIva(AreaIva areaIva, Integer idAreaMagazzino) {
        return validaAreaIva(areaIva, idAreaMagazzino, true);
    }

    @Override
    public AreaMagazzinoFullDTO validaAreaIva(AreaIva areaIva, Integer idAreaMagazzino, boolean totalizzaDocumento) {
        logger.debug("--> Enter validaAreaIva");
        areaIvaManager.validaAreaIva(areaIva);
        AreaMagazzino areaMagazzino = new AreaMagazzino();
        areaMagazzino.setId(idAreaMagazzino);
        areaMagazzino = caricaAreaMagazzino(areaMagazzino);
        areaMagazzino.setStatoAreaMagazzino(StatoAreaMagazzino.CONFERMATO);
        if (totalizzaDocumento) {
            // Totalizzo il documento
            ((Session) panjeaDAO.getEntityManager().getDelegate()).refresh(areaIva);
            areaMagazzino = areaMagazzinoManager.totalizzaDocumento(
                    areaMagazzino.getTipoAreaMagazzino().getStrategiaTotalizzazioneDocumento(), areaMagazzino,
                    areaIva.getRigheIva());
            try {
                logger.debug("--> salvo l'area magazzino modificata dalla totalizzazione");
                panjeaDAO.saveWithoutFlush(areaMagazzino);
            } catch (Exception e) {
                logger.error("--> errore nel salvare l'area magazzino " + areaMagazzino, e);
                throw new RuntimeException(e);
            }
        }

        AreaRate areaRate = areaRateManager.caricaAreaRate(areaIva.getDocumento());
        // Se esiste l'area partite sovrascrivo le rate presenti.
        // devo cmq evitare di creare le rate se il tipo oerazione non lo
        // prevede
        // (caso in cui l'area partite mi serve solo per il codice di
        // pagamento).
        // se il tipoOperazione è nessuno passo direttamente al cambio di stato
        // per chiudere il documento.
        if (areaRate.getId() != null && areaRate.isGenerazioneRateAllowed()) {
            if (areaMagazzino.getVersion() == null) {
                // significa che non l'ho mai caricato
                areaMagazzino = caricaAreaMagazzino(areaMagazzino);
            }
            areaRate = rateGenerator.generaRate(areaMagazzino, areaRate);
            areaRate.getDatiValidazione().valida(getPrincipal().getUserName());
            areaRateManager.salvaAreaRate(areaRate);
        }
        AreaMagazzinoFullDTO areaMagazzinoFullDTO = caricaAreaMagazzinoFullDTO(areaMagazzino);
        logger.debug("--> Exit validaAreaIva");
        return areaMagazzinoFullDTO;
    }

    @Override
    @RolesAllowed("gestioneDocMagazzino")
    public AreaMagazzinoFullDTO validaRigheMagazzino(AreaMagazzino areaMagazzino, AreaRate areaRate,
            Boolean areaContabilePresente, boolean forzaStato) throws TotaleDocumentoNonCoerenteException {
        logger.debug("--> Enter confermaRigheMagazzino");

        AreaMagazzino areaMagazzinoConfermata = null;
        try {
            areaMagazzinoConfermata = areaMagazzinoManager.validaRigheMagazzino(areaMagazzino, areaRate,
                    areaContabilePresente, forzaStato);
        } catch (TotaleDocumentoNonCoerenteException e) {
            throw e;
        } catch (Exception e) {
            logger.error("--> errore nel confermare le righe magazzino ", e);
            throw new RuntimeException("Errore nel confermare le righe magazzino", e);
        }

        AreaMagazzinoFullDTO areaMagazzinoFullDTO = null;

        AreaIva areaIva = areaIvaManager.caricaAreaIvaByDocumento(areaMagazzino.getDocumento());
        if (areaMagazzinoConfermata.getStatoAreaMagazzino() != StatoAreaMagazzino.FORZATO && areaIva != null
                && areaIva.getId() != null) {
            areaMagazzinoFullDTO = validaAreaIva(areaIva, areaMagazzinoConfermata.getId(), false);
        }

        if (areaRate != null && areaRate.getId() != null && areaRate.isGenerazioneRateAllowed()) {
            areaRate = areaRateManager.caricaAreaRate(areaRate.getId());
            areaRate = rateGenerator.generaRate(areaMagazzino, areaRate);
            areaRate.getDatiValidazione().valida(getPrincipal().getUserName());
            areaRateManager.salvaAreaRate(areaRate);
        }
        areaMagazzinoFullDTO = caricaAreaMagazzinoFullDTO(areaMagazzinoConfermata);

        logger.debug("--> Exit confermaRigheMagazzino");
        return areaMagazzinoFullDTO;
    }
}
