package it.eurotn.panjea.magazzino.rich.bd;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.documenti.domain.IAreaDocumento;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.documenti.service.exception.AreeCollegatePresentiException;
import it.eurotn.panjea.anagrafica.domain.CodiceIva;
import it.eurotn.panjea.anagrafica.domain.Deposito;
import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.contabilita.domain.AreaContabileLite;
import it.eurotn.panjea.contabilita.domain.TipoAreaContabile;
import it.eurotn.panjea.contabilita.service.exception.CodiceIvaCollegatoAssenteException;
import it.eurotn.panjea.contabilita.service.interfaces.ContabilitaService;
import it.eurotn.panjea.documenti.domain.StatoSpedizione;
import it.eurotn.panjea.documenti.service.interfaces.SpedizioneDocumentiService;
import it.eurotn.panjea.documenti.util.MovimentoSpedizioneDTO;
import it.eurotn.panjea.iva.domain.AreaIva;
import it.eurotn.panjea.iva.domain.RigaIva;
import it.eurotn.panjea.iva.service.interfaces.IvaService;
import it.eurotn.panjea.lotti.exception.RimanenzaLottiNonValidaException;
import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.DatiGenerazione;
import it.eurotn.panjea.magazzino.domain.IRigaArticoloDocumento;
import it.eurotn.panjea.magazzino.domain.InventarioArticolo;
import it.eurotn.panjea.magazzino.domain.NoteAreaMagazzino;
import it.eurotn.panjea.magazzino.domain.RigaArticolo;
import it.eurotn.panjea.magazzino.domain.RigaArticoloComponente;
import it.eurotn.panjea.magazzino.domain.RigaArticoloLite;
import it.eurotn.panjea.magazzino.domain.RigaMagazzino;
import it.eurotn.panjea.magazzino.domain.SedeMagazzino.ETipologiaCodiceIvaAlternativo;
import it.eurotn.panjea.magazzino.domain.SedeMagazzinoLite;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
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
import it.eurotn.panjea.magazzino.importer.util.DocumentoImport;
import it.eurotn.panjea.magazzino.manager.listinoprezzi.ListinoPrezziDTO;
import it.eurotn.panjea.magazzino.manager.listinoprezzi.ParametriListinoPrezzi;
import it.eurotn.panjea.magazzino.manager.omaggio.exception.CodiceIvaPerTipoOmaggioAssenteException;
import it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.provvigioni.interfaces.RigaDocumentoVariazioneProvvigioneStrategy;
import it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.provvigioni.interfaces.TipoVariazioneProvvigioneStrategy;
import it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.sconti.interfaces.RigaDocumentoVariazioneScontoStrategy;
import it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.sconti.interfaces.TipoVariazioneScontoStrategy;
import it.eurotn.panjea.magazzino.service.exception.DocumentiEsistentiPerAreaMagazzinoException;
import it.eurotn.panjea.magazzino.service.exception.DocumentoAssenteAvvisaException;
import it.eurotn.panjea.magazzino.service.exception.DocumentoAssenteBloccaException;
import it.eurotn.panjea.magazzino.service.exception.FatturazioneContabilizzazioneException;
import it.eurotn.panjea.magazzino.service.exception.QtaLottiMaggioreException;
import it.eurotn.panjea.magazzino.service.exception.RigheLottiNonValideException;
import it.eurotn.panjea.magazzino.service.exception.SedePerRifatturazioneAssenteException;
import it.eurotn.panjea.magazzino.service.interfaces.GestioneInventarioArticoloService;
import it.eurotn.panjea.magazzino.service.interfaces.MagazzinoDocumentoService;
import it.eurotn.panjea.magazzino.service.interfaces.MagazzinoStatisticheService;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoFullDTO;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoRicerca;
import it.eurotn.panjea.magazzino.util.IndiceGiacenzaArticolo;
import it.eurotn.panjea.magazzino.util.InventarioArticoloDTO;
import it.eurotn.panjea.magazzino.util.MovimentazioneArticolo;
import it.eurotn.panjea.magazzino.util.MovimentoFatturazioneDTO;
import it.eurotn.panjea.magazzino.util.ParametriCreazioneRigaArticolo;
import it.eurotn.panjea.magazzino.util.ParametriRicercaAreaMagazzino;
import it.eurotn.panjea.magazzino.util.ParametriRicercaMovimentazione;
import it.eurotn.panjea.magazzino.util.ParametriRicercaMovimentazioneArticolo;
import it.eurotn.panjea.magazzino.util.ParametriRicercaValorizzazione;
import it.eurotn.panjea.magazzino.util.PreFatturazioneDTO;
import it.eurotn.panjea.magazzino.util.RigaDestinazione;
import it.eurotn.panjea.magazzino.util.RigaMovimentazione;
import it.eurotn.panjea.magazzino.util.SedeAreaMagazzinoDTO;
import it.eurotn.panjea.magazzino.util.StatisticheArticolo;
import it.eurotn.panjea.magazzino.util.ValorizzazioneArticolo;
import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriCalcoloIndiciRotazioneGiacenza;
import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriRegoleValidazioneRighe;
import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriRicercaFatturazione;
import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriValorizzazioneDistinte;
import it.eurotn.panjea.magazzino.util.rigamagazzino.builder.RigheMagazzinoDTOResult;
import it.eurotn.panjea.mrp.domain.Bom;
import it.eurotn.panjea.mrp.util.ArticoloConfigurazioneKey;
import it.eurotn.panjea.partite.domain.AreaPartite;
import it.eurotn.panjea.partite.domain.TipoAreaPartita;
import it.eurotn.panjea.partite.service.interfaces.PartiteService;
import it.eurotn.panjea.rate.domain.AreaRate;
import it.eurotn.panjea.rate.service.interfaces.RateService;
import it.eurotn.panjea.rich.bd.AbstractBaseBD;
import it.eurotn.panjea.utils.PanjeaSwingUtil;

/**
 * @author giangi
 */
public class MagazzinoDocumentoBD extends AbstractBaseBD implements IMagazzinoDocumentoBD {
    public static final String BEAN_ID = "magazzinoDocumentoBD";

    private static final Logger LOGGER = Logger.getLogger(MagazzinoDocumentoBD.class);

    private MagazzinoDocumentoService magazzinoDocumentoService;
    private MagazzinoStatisticheService magazzinoStatisticheService;

    private PartiteService partiteService;

    private IvaService ivaService;

    private ContabilitaService contabilitaService;

    private RateService rateService;

    private GestioneInventarioArticoloService gestioneInventarioArticoloService;

    private SpedizioneDocumentiService spedizioneDocumentiService;

    @Override
    public RigaArticoloComponente aggiungiRigaComponente(Integer idArticolo, double qta, RigaArticolo rigaDistinta) {
        LOGGER.debug("--> Enter aggiungiRigaComponente");
        start("aggiungiRigaComponente");
        RigaArticoloComponente result = null;
        try {
            result = magazzinoDocumentoService.aggiungiRigaComponente(idArticolo, qta, rigaDistinta);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("aggiungiRigaComponente");
        }
        LOGGER.debug("--> Exit aggiungiRigaComponente ");
        return result;
    }

    @Override
    public void aggiungiVariazione(Integer idAreaMagazzino, BigDecimal variazione, BigDecimal percProvvigione,
            RigaDocumentoVariazioneScontoStrategy variazioneScontoStrategy,
            TipoVariazioneScontoStrategy tipoVariazioneScontoStrategy,
            RigaDocumentoVariazioneProvvigioneStrategy variazioneProvvigioneStrategy,
            TipoVariazioneProvvigioneStrategy tipoVariazioneProvvigioneStrategy) {
        LOGGER.debug("--> Enter aggiungiVariazione");
        start("aggiungiVariazione");
        try {
            magazzinoDocumentoService.aggiungiVariazione(idAreaMagazzino, variazione, percProvvigione,
                    variazioneScontoStrategy, tipoVariazioneScontoStrategy, variazioneProvvigioneStrategy,
                    tipoVariazioneProvvigioneStrategy);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("aggiungiVariazione");
        }
        LOGGER.debug("--> Exit aggiungiVariazione ");
    }

    @Override
    public List<RigaArticoloLite> applicaRegoleValidazione(
            ParametriRegoleValidazioneRighe parametriRegoleValidazioneRighe) {
        LOGGER.debug("--> Enter validaRigheMagazzino");
        start("validaRigheMagazzino");
        List<RigaArticoloLite> listRighe = new ArrayList<RigaArticoloLite>();
        try {
            listRighe = magazzinoDocumentoService.applicaRegoleValidazione(parametriRegoleValidazioneRighe);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("validaRigheMagazzino");
        }
        LOGGER.debug("--> Exit validaRigheMagazzino ");
        return listRighe;
    }

    @Override
    public void avvaloraGiacenzaRealeInventarioArticolo(Date data, DepositoLite deposito) {
        LOGGER.debug("--> Enter avvaloraGiacenzaRealeInventarioArticolo");
        start("avvaloraGiacenzaRealeInventarioArticolo");
        try {
            gestioneInventarioArticoloService.avvaloraGiacenzaRealeInventarioArticolo(data, deposito);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("avvaloraGiacenzaRealeInventarioArticolo");
        }
        LOGGER.debug("--> Exit avvaloraGiacenzaRealeInventarioArticolo ");
    }

    /*
     * (non-Javadoc)
     *
     * @see it.eurotn.panjea.magazzino.rich.bd.IMagazzinoDocumentoBD#calcoloGiacenza
     * (it.eurotn.panjea.magazzino.domain.ArticoloLite, java.util.Date,
     * it.eurotn.panjea.anagrafica.domain.lite.DepositoLite)
     */
    @Override
    public Double calcolaDisponibilita(ArticoloLite articolo, Date data, DepositoLite depositoLite) {
        LOGGER.debug("--> Enter calcoloGiacenza");
        start("calcoloGiacenza");
        Double giacenza = 0.0;
        try {
            giacenza = magazzinoStatisticheService.calcolaDisponibilitaArticolo(articolo.getId(), data,
                    depositoLite.getId());
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("calcoloGiacenza");
        }
        LOGGER.debug("--> Exit calcoloGiacenza ");
        return giacenza;
    }

    @Override
    public List<IndiceGiacenzaArticolo> calcolaIndiciRotazione(ParametriCalcoloIndiciRotazioneGiacenza parametri) {
        LOGGER.debug("--> Enter calcolaIndiciRotazione");
        start("calcolaIndiciRotazione");
        List<IndiceGiacenzaArticolo> result = null;
        try {
            result = magazzinoStatisticheService.calcolaIndiciRotazione(parametri);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("calcolaIndiciRotazione");
        }
        LOGGER.debug("--> Exit calcolaIndiciRotazione ");
        return result;
    }

    @Override
    public PoliticaPrezzo calcolaPrezzoArticolo(final ParametriCalcoloPrezzi parametriCalcoloPrezzi) {
        LOGGER.debug("--> Enter calcolaPrezzoArticolo");
        start("calcolaPrezzoArticolo");
        PoliticaPrezzo result = null;
        try {
            result = magazzinoDocumentoService.calcolaPrezzoArticolo(parametriCalcoloPrezzi);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("calcolaPrezzoArticolo");
        }
        LOGGER.debug("--> Exit calcolaPrezzoArticolo");
        return result;
    }

    @Override
    public StatoSpedizione cambiaStatoSpedizioneMovimento(IAreaDocumento areaDocumento) {
        LOGGER.debug("--> Enter cambiaStatoSpedizioneMovimento");
        start("cambiaStatoSpedizioneMovimento");

        StatoSpedizione statoSpedizione = null;
        try {
            statoSpedizione = magazzinoDocumentoService.cambiaStatoSpedizioneMovimento(areaDocumento);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cambiaStatoSpedizioneMovimento");
        }
        LOGGER.debug("--> Exit cambiaStatoSpedizioneMovimento ");
        return statoSpedizione;
    }

    @Override
    public void cancellaAreaMagazzino(AreaMagazzino areaMagazzino) throws AreeCollegatePresentiException {
        LOGGER.debug("--> Enter cancellaAreaMagazzino");
        start("cancellaAreaMagazzino");
        try {
            magazzinoDocumentoService.cancellaAreaMagazzino(areaMagazzino);
        } catch (AreeCollegatePresentiException e) {
            throw e;
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaAreaMagazzino");
        }
        LOGGER.debug("--> Exit cancellaAreaMagazzino ");
    }

    @Override
    public void cancellaAreaMagazzino(AreaMagazzino areaMagazzino, boolean deleteAreeCollegate,
            boolean forceDeleteAreeCollegate) throws AreeCollegatePresentiException {
        LOGGER.debug("--> Enter cancellaAreaMagazzino");
        start("cancellaAreaMagazzino");
        try {
            magazzinoDocumentoService.cancellaAreaMagazzino(areaMagazzino, deleteAreeCollegate,
                    forceDeleteAreeCollegate);
        } catch (AreeCollegatePresentiException e) {
            throw e;
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaAreaMagazzino");
        }
        LOGGER.debug("--> Exit cancellaAreaMagazzino ");
    }

    @Override
    public void cancellaAreeMagazzino(List<AreaMagazzino> areeMagazzino, boolean deleteAreeCollegate) {
        LOGGER.debug("--> Enter cancellaAreeMagazzino");
        start("cancellaAreeMagazzino");
        try {
            magazzinoDocumentoService.cancellaAreeMagazzino(areeMagazzino, deleteAreeCollegate);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaAreeMagazzino");
        }
        LOGGER.debug("--> Exit cancellaAreeMagazzino ");
    }

    @Override
    public void cancellaInventarioArticolo(Date data, DepositoLite deposito) {
        LOGGER.debug("--> Enter cancellaInventarioArticolo");
        start("cancellaInventarioArticolo");
        try {
            gestioneInventarioArticoloService.cancellaInventarioArticolo(data, deposito);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaInventarioArticolo");
        }
        LOGGER.debug("--> Exit cancellaInventarioArticolo ");
    }

    @Override
    public void cancellaMovimentiInFatturazione(String utente) {
        LOGGER.debug("--> Enter cancellaMovimentiInFatturazione");
        start("cancellaMovimentiInFatturazione");
        try {
            magazzinoDocumentoService.cancellaMovimentiInFatturazione(utente);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaMovimentiInFatturazione");
        }
        LOGGER.debug("--> Exit cancellaMovimentiInFatturazione ");
    }

    @Override
    public void cancellaRigaIva(RigaIva rigaIva) {
        LOGGER.debug("--> Enter cancellaRigaIva");
        start("cancellaRigaIva");
        try {
            ivaService.cancellaRigaIva(rigaIva);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaRigaIva");
        }
        LOGGER.debug("--> Exit cancellaRigaIva ");
    }

    @Override
    public AreaMagazzino cancellaRigaMagazzino(RigaMagazzino rigaMagazzino) {
        LOGGER.debug("--> Enter cancellaRigaMagazzino");
        start("cancellaRigaMagazzino");
        AreaMagazzino areaMagazzino = null;
        try {
            areaMagazzino = magazzinoDocumentoService.cancellaRigaMagazzino(rigaMagazzino);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaRigaMagazzino");
        }
        LOGGER.debug("--> Exit cancellaRigaMagazzino ");
        return areaMagazzino;
    }

    @Override
    public AreaMagazzino cancellaRigheMagazzino(List<RigaMagazzino> righeMagazzino) {
        LOGGER.debug("--> Enter cancellaRigheMagazzino");
        start("cancellaRigheMagazzino");
        AreaMagazzino result = null;
        try {
            result = magazzinoDocumentoService.cancellaRigheMagazzino(righeMagazzino);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaRigheMagazzino");
        }
        LOGGER.debug("--> Exit cancellaRigheMagazzino ");
        return result;
    }

    @Override
    public void cancellaTipoAreaMagazzino(TipoAreaMagazzino tipoAreaMagazzino) {
        LOGGER.debug("--> Enter cancellaTipoAreaContabile");
        start("cancellaTipoAreaContabile");
        try {
            magazzinoDocumentoService.cancellaTipoAreaMagazzino(tipoAreaMagazzino);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaTipoAreaContabile");
        }
        LOGGER.debug("--> Exit cancellaTipoAreaContabile ");

    }

    @Override
    public AreaContabileLite caricaAreaContabileLiteByDocumento(Integer idDocumento) {
        LOGGER.debug("--> Enter caricaAreaContabileLiteByDocumento");
        start("caricaAreaContabileLiteByDocumento");
        AreaContabileLite result = null;
        try {
            result = contabilitaService.caricaAreaContabileLiteByDocumento(idDocumento);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaAreaContabileLiteByDocumento");
        }
        LOGGER.debug("--> Exit caricaAreaContabileLiteByDocumento ");
        return result;
    }

    @Override
    public AreaIva caricaAreaIva(AreaIva areaIva) {
        LOGGER.debug("--> Enter caricaAreaIva");
        start("caricaAreaIva");
        AreaIva areaIva2 = null;
        try {
            areaIva2 = ivaService.caricaAreaIva(areaIva);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaAreaIva");
        }
        LOGGER.debug("--> Exit caricaAreaIva ");
        return areaIva2;
    }

    @Override
    public AreaMagazzino caricaAreaMagazzino(AreaMagazzino areaMagazzino) {
        LOGGER.debug("--> Enter caricaAreaMagazzino");
        start("caricaAreaMagazzino");
        AreaMagazzino areaMagazzinoCaricata = null;
        try {
            areaMagazzinoCaricata = magazzinoDocumentoService.caricaAreaMagazzino(areaMagazzino);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaAreaMagazzino");
        }
        LOGGER.debug("--> Exit caricaAreaMagazzino ");
        return areaMagazzinoCaricata;
    }

    @Override
    public AreaMagazzinoFullDTO caricaAreaMagazzinoFullDTO(AreaMagazzino areaMagazzino) {
        LOGGER.debug("--> Enter caricaAreaMagazzinoFullDTO");
        start("caricaAreaMagazzinoFullDTO");
        AreaMagazzinoFullDTO areaMagazzinoFullDTO = null;
        try {
            areaMagazzinoFullDTO = magazzinoDocumentoService.caricaAreaMagazzinoFullDTO(areaMagazzino);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaAreaMagazzinoFullDTO");
        }
        LOGGER.debug("--> Exit caricaAreaMagazzinoFullDTO ");
        return areaMagazzinoFullDTO;
    }

    @Override
    public AreaMagazzinoFullDTO caricaAreaMagazzinoFullDTOByDocumento(Documento documento) {
        LOGGER.debug("--> Enter caricaAreaMagazzinoFullDTOByDocumento");
        start("caricaAreaMagazzinoFullDTOByDocumento");
        AreaMagazzinoFullDTO areaMagazzinoFullDTO = null;
        try {
            areaMagazzinoFullDTO = magazzinoDocumentoService.caricaAreaMagazzinoFullDTOByDocumento(documento);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaAreaMagazzinoFullDTOByDocumento");
        }
        LOGGER.debug("--> Exit caricaAreaMagazzinoFullDTOByDocumento ");
        return areaMagazzinoFullDTO;
    }

    @Override
    public AreaRate caricaAreaRateByDocumento(Documento documento) {
        LOGGER.debug("--> Enter caricaAreaRateByDocumento");
        start("caricaAreaRateByDocumento");
        AreaRate result = new AreaRate();
        try {
            // se non ho il plugin pagamenti non ho nemmeno l'area partite
            if (partiteService != null) {
                result = rateService.caricaAreaRate(documento);
            }
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaAreaRateByDocumento");
        }
        LOGGER.debug("--> Exit caricaAreaRateByDocumento ");
        return result;
    }

    @Override
    public List<AreaMagazzinoLite> caricaAreeMagazzino(ParametriRicercaFatturazione parametriRicercaFatturazione) {
        LOGGER.debug("--> Enter caricaAreeMagazzino");
        start("caricaAreeMagazzino");
        List<AreaMagazzinoLite> list = new ArrayList<AreaMagazzinoLite>();
        try {
            list = magazzinoDocumentoService.caricaAreeMagazzino(parametriRicercaFatturazione);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaAreeMagazzino");
        }
        LOGGER.debug("--> Exit caricaAreeMagazzino ");
        return list;
    }

    @Override
    public List<AreaMagazzinoLite> caricaAreeMagazzinoCollegate(List<AreaMagazzino> areeMagazzino) {
        LOGGER.debug("--> Enter caricaAreeMagazzinoCollegate");
        start("caricaAreeMagazzinoCollegate");
        List<AreaMagazzinoLite> result = null;
        try {
            result = magazzinoDocumentoService.caricaAreeMagazzinoCollegate(areeMagazzino);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaAreeMagazzinoCollegate");
        }
        LOGGER.debug("--> Exit caricaAreeMagazzinoCollegate ");
        return result;
    }

    @Override
    public List<AreaMagazzinoRicerca> caricaAreeMagazzinoConRichiestaDatiAccompagnatori(Date dataEvasione) {
        LOGGER.debug("--> Enter caricaAreeMagazzinoConRichiestaDatiAccompagnatori");
        start("caricaAreeMagazzinoConRichiestaDatiAccompagnatori");

        List<AreaMagazzinoRicerca> results = null;
        try {
            results = magazzinoDocumentoService.caricaAreeMagazzinoConRichiestaDatiAccompagnatori(dataEvasione);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaAreeMagazzinoConRichiestaDatiAccompagnatori");
        }
        LOGGER.debug("--> Exit caricaAreeMagazzinoConRichiestaDatiAccompagnatori ");
        return results;
    }

    @Override
    public List<AreaMagazzinoRicerca> caricaAreeMagazzinoConRichiestaDatiAccompagnatori(List<Integer> idAree) {
        LOGGER.debug("--> Enter caricaAreeMagazzinoConRichiestaDatiAccompagnatori");
        start("caricaAreeMagazzinoConRichiestaDatiAccompagnatori");

        List<AreaMagazzinoRicerca> results = null;
        try {
            results = magazzinoDocumentoService.caricaAreeMagazzinoConRichiestaDatiAccompagnatori(idAree);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaAreeMagazzinoConRichiestaDatiAccompagnatori");
        }
        LOGGER.debug("--> Exit caricaAreeMagazzinoConRichiestaDatiAccompagnatori ");
        return results;
    }

    @Override
    public CodiceIva caricaCodiceIvaPerSostituzione(IRigaArticoloDocumento rigaArticolo)
            throws CodiceIvaPerTipoOmaggioAssenteException {
        LOGGER.debug("--> Enter caricaCodiceIvaByTipo");
        start("caricaCodiceIvaByTipo");
        CodiceIva ivaOmaggio = null;
        try {
            ivaOmaggio = magazzinoDocumentoService.caricaCodiceIvaPerSostituzione(rigaArticolo);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaCodiceIvaByTipo");
        }
        LOGGER.debug("--> Exit caricaCodiceIvaByTipo ");
        return ivaOmaggio;
    }

    @Override
    public Date caricaDataMovimentiInFatturazione() {
        LOGGER.debug("--> Enter caricaDataMovimentiInFatturazione");
        start("caricaDataMovimentiInFatturazione");

        Date dataGenerazione = null;
        try {
            dataGenerazione = magazzinoDocumentoService.caricaDataMovimentiInFatturazione();
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaDataMovimentiInFatturazione");
        }
        LOGGER.debug("--> Exit caricaDataMovimentiInFatturazione ");
        return dataGenerazione;
    }

    @Override
    public List<DatiGenerazione> caricaDatiGenerazioneFatturazioneTemporanea() {
        LOGGER.debug("--> Enter caricaDatiGenerazioneFatturazioneTemporanea");
        start("caricaDatiGenerazioneFatturazioneTemporanea");
        List<DatiGenerazione> datiGenerazione = new ArrayList<DatiGenerazione>();
        try {
            datiGenerazione = magazzinoDocumentoService.caricaDatiGenerazioneFatturazioneTemporanea();
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaDatiGenerazioneFatturazioneTemporanea");
        }
        LOGGER.debug("--> Exit caricaDatiGenerazioneFatturazioneTemporanea ");
        return datiGenerazione;
    }

    @Override
    public List<Deposito> caricaDepositiPerInventari() {
        LOGGER.debug("--> Enter caricaDepositiPerInventari");
        start("caricaDepositiPerInventari");
        List<Deposito> depositi = new ArrayList<Deposito>();
        try {
            depositi = gestioneInventarioArticoloService.caricaDepositiPerInventari();
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaDepositiPerInventari");
        }
        LOGGER.debug("--> Exit caricaDepositiPerInventari ");
        return depositi;
    }

    @Override
    public Collection<DocumentoImport> caricaDocumenti(String codiceImporter, byte[] fileImport) {
        LOGGER.debug("--> Enter caricaDocumenti");
        start("caricaDocumenti");

        Collection<DocumentoImport> results = null;
        try {
            results = magazzinoDocumentoService.caricaDocumenti(codiceImporter, fileImport);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaDocumenti");
        }
        LOGGER.debug("--> Exit caricaDocumenti ");
        return results;
    }

    @Override
    public List<DatiGenerazione> caricaFatturazioni(int annoFatturazione) {
        LOGGER.debug("--> Enter caricaFatturazioni");
        start("caricaFatturazioni");

        List<DatiGenerazione> list = new ArrayList<DatiGenerazione>();
        try {
            list = magazzinoDocumentoService.caricaFatturazioni(annoFatturazione);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaFatturazioni");
        }
        LOGGER.debug("--> Exit caricaFatturazioni ");
        return list;
    }

    @Override
    public List<Integer> caricaIdAreeMagazzinoPerStampaEvasione(List<AreaMagazzinoRicerca> aree) {
        LOGGER.debug("--> Enter caricaIdAreeMagazzinoPerStampaEvasione");
        start("caricaIdAreeMagazzinoPerStampaEvasione");
        List<Integer> idAree = null;
        try {
            idAree = magazzinoDocumentoService.caricaIdAreeMagazzinoPerStampaEvasione(aree);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaIdAreeMagazzinoPerStampaEvasione");
        }
        LOGGER.debug("--> Exit caricaIdAreeMagazzinoPerStampaEvasione ");
        return idAree;
    }

    @Override
    public List<String> caricaImporter() {
        LOGGER.debug("--> Enter caricaImporter");
        start("caricaImporter");

        List<String> importers = null;
        try {
            importers = magazzinoDocumentoService.caricaImporter();
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaImporter");
        }
        LOGGER.debug("--> Exit caricaImporter ");
        return importers;
    }

    @Override
    public List<InventarioArticoloDTO> caricaInventariiArticoli() {
        LOGGER.debug("--> Enter caricaInventariiArticoli");
        start("caricaInventariiArticoli");
        List<InventarioArticoloDTO> inventari = new ArrayList<InventarioArticoloDTO>();
        try {
            inventari = gestioneInventarioArticoloService.caricaInventariiArticoli();
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaInventariiArticoli");
        }
        LOGGER.debug("--> Exit caricaInventariiArticoli ");
        return inventari;
    }

    @Override
    public List<InventarioArticolo> caricaInventarioArticolo(Date date, DepositoLite depositoLite,
            boolean caricaGiacenzeAZero) {
        LOGGER.debug("--> Enter caricaInventarioArticolo");
        start("caricaInventarioArticolo");
        List<InventarioArticolo> inventari = new ArrayList<InventarioArticolo>();
        try {
            inventari = gestioneInventarioArticoloService.caricaInventarioArticolo(date, depositoLite,
                    caricaGiacenzeAZero);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaInventarioArticolo");
        }
        LOGGER.debug("--> Exit caricaInventarioArticolo ");
        return inventari;
    }

    @Override
    public InventarioArticolo caricaInventarioArticolo(InventarioArticolo inventarioArticolo) {
        LOGGER.debug("--> Enter caricaInventarioArticolo");
        InventarioArticolo inventarioArticoloSalvato = null;
        start("caricaInventarioArticolo");
        try {
            inventarioArticoloSalvato = gestioneInventarioArticoloService.caricaInventarioArticolo(inventarioArticolo);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaInventarioArticolo");
        }
        LOGGER.debug("--> Exit caricaInventarioArticolo ");
        return inventarioArticoloSalvato;
    }

    @Override
    public List<ListinoPrezziDTO> caricaListinoPrezzi(ParametriListinoPrezzi parametriListinoPrezzi) {
        LOGGER.debug("--> Enter caricaListinoPrezzi");
        start("caricaListinoPrezzi");
        List<ListinoPrezziDTO> result = null;
        try {
            result = magazzinoDocumentoService.caricaListinoPrezzi(parametriListinoPrezzi);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaListinoPrezzi");
        }
        LOGGER.debug("--> Exit caricaListinoPrezzi ");
        return result;
    }

    @Override
    public List<RigaMovimentazione> caricaMovimentazione(ParametriRicercaMovimentazione parametriRicercaMovimentazione,
            int page, int sizeOfPage) {
        LOGGER.debug("--> Enter caricaMovimentazione");
        start("caricaMovimentazione");
        List<RigaMovimentazione> righeMovimentazione = null;
        try {
            righeMovimentazione = magazzinoStatisticheService.caricaMovimentazione(parametriRicercaMovimentazione, page,
                    sizeOfPage);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaMovimentazione");
        }
        LOGGER.debug("--> Exit caricaMovimentazione");
        return righeMovimentazione;
    }

    @Override
    public MovimentazioneArticolo caricaMovimentazioneArticolo(
            ParametriRicercaMovimentazioneArticolo parametriRicercaMovimentazioneArticolo) {
        LOGGER.debug("--> Enter caricaMovimentazioneArticolo");
        start("caricaMovimentazioneArticolo");
        MovimentazioneArticolo movimentazioneArticolo = null;
        try {
            movimentazioneArticolo = magazzinoStatisticheService
                    .caricaMovimentazioneArticolo(parametriRicercaMovimentazioneArticolo);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaMovimentazioneArticolo");
        }
        LOGGER.debug("--> Exit caricaMovimentazioneArticolo ");
        return movimentazioneArticolo;
    }

    @Override
    public List<AreaMagazzinoLite> caricaMovimentiPerFatturazione(Date dataCreazione) {
        LOGGER.debug("--> Enter caricaMovimentiPerFatturazione");
        start("caricaMovimentiPerFatturazione");

        List<AreaMagazzinoLite> list = new ArrayList<AreaMagazzinoLite>();
        try {
            list = magazzinoDocumentoService.caricaMovimentiPerFatturazione(dataCreazione);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaMovimentiPerFatturazione");
        }
        LOGGER.debug("--> Exit caricaMovimentiPerFatturazione ");

        return list;
    }

    @Override
    public List<MovimentoSpedizioneDTO> caricaMovimentiPerSpedizione(Class<? extends IAreaDocumento> areaDocumentoClass,
            List<Integer> idDocumenti) {
        LOGGER.debug("--> Enter caricaMovimentiPerSpedizione");
        start("caricaMovimentiPerSpedizione");

        List<MovimentoSpedizioneDTO> movimenti = null;
        try {
            movimenti = spedizioneDocumentiService.caricaMovimentiPerSpedizione(areaDocumentoClass, idDocumenti);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaMovimentiPerSpedizione");
        }
        LOGGER.debug("--> Exit caricaMovimentiPerSpedizione ");
        return movimenti;
    }

    @Override
    public List<MovimentoFatturazioneDTO> caricaMovimentPerFatturazione(Date dataCreazione, String utente) {
        LOGGER.debug("--> Enter caricaMovimentPerFatturazione");
        start("caricaMovimentPerFatturazione");
        List<MovimentoFatturazioneDTO> list = new ArrayList<MovimentoFatturazioneDTO>();
        try {
            list = magazzinoDocumentoService.caricaMovimentPerFatturazione(dataCreazione, utente);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaMovimentPerFatturazione");
        }
        LOGGER.debug("--> Exit caricaMovimentPerFatturazione ");
        return list;
    }

    @Override
    public NoteAreaMagazzino caricaNoteSede(SedeEntita sedeEntita) {
        LOGGER.debug("--> Enter caricaNoteSede");
        start("caricaNoteSede");
        NoteAreaMagazzino note = null;
        try {
            note = magazzinoDocumentoService.caricaNoteSede(sedeEntita);
        } catch (Exception e) {
            LOGGER.error("--> errore nel caricare le note sede", e);
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaNoteSede");
        }
        LOGGER.debug("--> Exit caricaNoteSede ");
        return note;
    }

    @Override
    public RigaIva caricaRigaIva(Integer id) {
        LOGGER.debug("--> Enter caricaRigaIva");
        start("caricaRigaIva");
        RigaIva rigaIva = null;
        try {
            rigaIva = ivaService.caricaRigaIva(id);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaRigaIva");
        }
        LOGGER.debug("--> Exit caricaRigaIva ");
        return rigaIva;
    }

    @Override
    public RigaMagazzino caricaRigaMagazzino(RigaMagazzino rigaMagazzino) {
        LOGGER.debug("--> Enter caricaRigaMagazzino");
        start("caricaRigaMagazzino");
        RigaMagazzino rigaMagazzinoCaricata = null;
        try {
            rigaMagazzinoCaricata = magazzinoDocumentoService.caricaRigaMagazzino(rigaMagazzino);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaRigaMagazzino");
        }
        LOGGER.debug("--> Exit caricaRigaMagazzino");
        return rigaMagazzinoCaricata;
    }

    @Override
    public List<? extends RigaMagazzino> caricaRigheMagazzinobyAreaMagazzino(AreaMagazzino areaMagazzino) {
        LOGGER.debug("--> Enter caricaRigheMagazzinobyAreaMagazzino");
        start("caricaRigheMagazzinobyAreaMagazzino");
        List<? extends RigaMagazzino> righeMagazzino = null;
        try {
            righeMagazzino = magazzinoDocumentoService.caricaRigheMagazzino(areaMagazzino);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaRigheMagazzinobyAreaMagazzino");
        }
        LOGGER.debug("--> Exit caricaRigheMagazzinobyAreaMagazzino ");
        return righeMagazzino;
    }

    @Override
    public List<RigaDestinazione> caricaRigheMagazzinoCollegate(RigaMagazzino rigaMagazzino) {
        LOGGER.debug("--> Enter caricaRigheMagazzinoCollegate");
        start("caricaRigheMagazzinoCollegate");
        List<RigaDestinazione> righe = null;
        try {
            righe = magazzinoDocumentoService.caricaRigheMagazzinoCollegate(rigaMagazzino);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaRigheMagazzinoCollegate");
        }
        LOGGER.debug("--> Exit caricaRigheMagazzinoCollegate ");
        return righe;
    }

    @Override
    public RigheMagazzinoDTOResult caricaRigheMagazzinoDTObyAreaMagazzino(AreaMagazzino areaMagazzino) {
        LOGGER.debug("--> Enter caricaRigheMagazzinoDTObyAreaMagazzino");
        start("caricaRigheMagazzinoDTObyAreaMagazzino");
        RigheMagazzinoDTOResult righeMagazzinoDTO = null;
        try {
            righeMagazzinoDTO = magazzinoDocumentoService.caricaRigheMagazzinoDTO(areaMagazzino);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaRigheMagazzinoDTObyAreaMagazzino");
        }
        LOGGER.debug("--> Exit caricaRigheMagazzinoDTObyAreaMagazzino ");
        return righeMagazzinoDTO;
    }

    @Override
    public SedeAreaMagazzinoDTO caricaSedeAreaMagazzinoDTO(SedeEntita sedeEntita) {
        LOGGER.debug("--> Enter caricaSedeAreaMagazzinoDTO");
        start("caricaSedeAreaMagazzinoDTO");
        SedeAreaMagazzinoDTO sedeAreaMagazzinoDTO = null;
        try {
            sedeAreaMagazzinoDTO = magazzinoDocumentoService.caricaSedeAreaMagazzinoDTO(sedeEntita);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaSedeAreaMagazzinoDTO");
        }
        LOGGER.debug("--> Exit caricaSedeAreaMagazzinoDTO ");
        return sedeAreaMagazzinoDTO;
    }

    @Override
    public StatisticheArticolo caricaStatisticheArticolo(Articolo articolo) {
        return caricaStatisticheArticolo(articolo, Calendar.getInstance().get(Calendar.YEAR));
    }

    @Override
    public StatisticheArticolo caricaStatisticheArticolo(Articolo articolo, Integer anno) {
        LOGGER.debug("--> Enter caricaStatisticheArticolo");
        start("caricaStatisticheArticolo");
        StatisticheArticolo statisticheArticolo = null;
        try {
            statisticheArticolo = magazzinoStatisticheService.caricaStatisticheArticolo(articolo, anno);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaStatisticheArticolo");
        }
        LOGGER.debug("--> Exit caricaStatisticheArticolo ");
        return statisticheArticolo;
    }

    @Override
    public List<TipoAreaMagazzino> caricaTipiAreaMagazzino(String fieldSearch, String valueSearch,
            boolean loadTipiDocumentoDisabilitati) {
        LOGGER.debug("--> Enter caricaTipiAreaMagazzino");
        start("caricaTipiAreaMagazzino");
        List<TipoAreaMagazzino> tipiAreaMagazzino = new ArrayList<TipoAreaMagazzino>();
        try {
            tipiAreaMagazzino = magazzinoDocumentoService.caricaTipiAreaMagazzino(fieldSearch, valueSearch,
                    loadTipiDocumentoDisabilitati);

        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaTipiAreaMagazzino");
        }
        LOGGER.debug("--> Exit caricaTipiAreaMagazzino ");
        return tipiAreaMagazzino;
    }

    @Override
    public List<TipoDocumento> caricaTipiDocumentoAnagraficaPerFatturazione() {
        LOGGER.debug("--> Enter caricaTipiDocumentoAnagraficaPerFatturazione");
        start("caricaTipiDocumentoAnagraficaPerFatturazione");
        List<TipoDocumento> list = new ArrayList<TipoDocumento>();
        try {
            list = magazzinoDocumentoService.caricaTipiDocumentoAnagraficaPerFatturazione();
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaTipiDocumentoAnagraficaPerFatturazione");
        }
        LOGGER.debug("--> Exit caricaTipiDocumentoAnagraficaPerFatturazione ");
        return list;
    }

    @Override
    public List<TipoDocumento> caricaTipiDocumentoDestinazioneFatturazione() {
        LOGGER.debug("--> Enter caricaTipiDocumentoDestinazioneFatturazione");
        start("caricaTipiDocumentoDestinazioneFatturazione");

        List<TipoDocumento> list = new ArrayList<TipoDocumento>();
        try {
            list = magazzinoDocumentoService.caricaTipiDocumentoDestinazioneFatturazione();
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaTipiDocumentoDestinazioneFatturazione");
        }
        LOGGER.debug("--> Exit caricaTipiDocumentoDestinazioneFatturazione ");
        return list;
    }

    @Override
    public List<TipoDocumento> caricaTipiDocumentoPerFatturazione(TipoDocumento tipoDocumentoDiFatturazione) {
        LOGGER.debug("--> Enter caricaTipiAreeMagazzinoPerFatturazione");
        start("caricaTipiAreeMagazzinoPerFatturazione");

        List<TipoDocumento> list = new ArrayList<TipoDocumento>();
        try {
            list = magazzinoDocumentoService.caricaTipiDocumentoPerFatturazione(tipoDocumentoDiFatturazione);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaTipiAreeMagazzinoPerFatturazione");
        }
        LOGGER.debug("--> Exit caricaTipiAreeMagazzinoPerFatturazione ");
        return list;
    }

    @Override
    public TipoAreaMagazzino caricaTipoAreaMagazzino(Integer id) {
        LOGGER.debug("--> Enter caricaTipoAreaMagazzino");
        start("caricaTipoAreaMagazzino");
        TipoAreaMagazzino result = null;
        try {
            result = magazzinoDocumentoService.caricaTipoAreaMagazzino(id);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaTipoAreaMagazzino");
        }
        LOGGER.debug("--> Exit caricaTipoAreaMagazzino ");
        return result;
    }

    @Override
    public TipoAreaMagazzino caricaTipoAreaMagazzinoPerTipoDocumento(Integer id) {
        LOGGER.debug("--> Enter caricaTipoAreaMagazzinoPerTipoDocumento");
        start("caricaTipoAreaMagazzinoPerTipoDocumento");
        TipoAreaMagazzino tipoAreaMagazzino = null;
        try {
            tipoAreaMagazzino = magazzinoDocumentoService.caricaTipoAreaMagazzinoPerTipoDocumento(id);
            org.springframework.util.Assert.notNull(tipoAreaMagazzino.getTipoDocumento(),
                    "Tipo documento non può essere mai null");
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaTipoAreaMagazzinoPerTipoDocumento");
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(
                    "--> Exit caricaTipoAreaMagazzinoPerTipoDocumento con tipoAreaMagazzino= " + tipoAreaMagazzino);
        }
        return tipoAreaMagazzino;
    }

    @Override
    public TipoAreaPartita caricaTipoAreaPartitaPerTipoDocumento(TipoDocumento tipoDocumento) {
        LOGGER.debug("--> Enter caricaTipoAreaPartitaPerTipoDocumento");
        start("caricaTipoAreaPartitaPerTipoDocumento");
        TipoAreaPartita tipoAreaPartita = null;
        try {
            tipoAreaPartita = magazzinoDocumentoService.caricaTipoAreaPartitaPerTipoDocumento(tipoDocumento);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaTipoAreaPartitaPerTipoDocumento");
        }
        LOGGER.debug("--> Exit caricaTipoAreaPartitaPerTipoDocumento ");
        return tipoAreaPartita;
    }

    @Override
    public AreaMagazzinoLite caricaUltimoInventario(Integer idDeposito) {
        LOGGER.debug("--> Enter caricaUltimoInventario");
        start("caricaUltimoInventario");

        AreaMagazzinoLite areaMagazzinoLite = null;
        try {
            areaMagazzinoLite = magazzinoDocumentoService.caricaUltimoInventario(idDeposito);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaUltimoInventario");
        }
        LOGGER.debug("--> Exit caricaUltimoInventario ");
        return areaMagazzinoLite;
    }

    @Override
    public List<ValorizzazioneArticolo> caricaValorizzazione(
            ParametriRicercaValorizzazione parametriRicercaValorizzazione) {
        LOGGER.debug("--> Enter caricaValorizzazione");
        start("caricaValorizzazione");
        List<ValorizzazioneArticolo> valorizzazione = new ArrayList<ValorizzazioneArticolo>();
        try {
            valorizzazione = magazzinoStatisticheService.caricaValorizzazione(parametriRicercaValorizzazione);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaValorizzazione");
        }
        LOGGER.debug("--> Exit caricaValorizzazione ");
        return valorizzazione;
    }

    @Override
    public void collegaTestata(Set<Integer> righeMagazzinoDaCambiare) {
        LOGGER.debug("--> Enter collegaTestata");
        start("collegaTestata");
        try {
            magazzinoDocumentoService.collegaTestata(righeMagazzinoDaCambiare);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("collegaTestata");
        }
        LOGGER.debug("--> Exit collegaTestata ");
    }

    @Override
    public DatiGenerazione confermaMovimentiInFatturazione(String utente) throws Exception {
        LOGGER.debug("--> Enter confermaMovimentiInFatturazione");
        start("confermaMovimentiInFatturazione");

        DatiGenerazione datiGenerazione = null;
        try {
            datiGenerazione = magazzinoDocumentoService.confermaMovimentiInFatturazione(utente);

        } catch (FatturazioneContabilizzazioneException e) {
            throw e;
        } catch (Exception e) {
            // Rilancio l'eccezione perchè la gestiosco a parte nella gui.
            throw e;
        } finally {
            end("confermaMovimentiInFatturazione");
        }
        LOGGER.debug("--> Exit confermaMovimentiInFatturazione ");
        return datiGenerazione;
    }

    @Override
    public void creaInventariArticolo(Date data, List<DepositoLite> depositi) {
        LOGGER.debug("--> Enter creaInventariArticolo");
        start("creaInventariArticolo");
        try {
            gestioneInventarioArticoloService.creaInventariArticolo(data, depositi);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("creaInventariArticolo");
        }
        LOGGER.debug("--> Exit creaInventariArticolo ");
    }

    @Override
    public RigaArticolo creaRigaArticolo(ParametriCreazioneRigaArticolo parametri) {
        LOGGER.debug("--> Enter creaRigaArticolo");
        start("creaRigaArticolo");
        RigaArticolo rigaArticolo = null;
        try {
            rigaArticolo = magazzinoDocumentoService.creaRigaArticolo(parametri);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("creaRigaArticolo");
        }
        LOGGER.debug("--> Exit creaRigaArticolo ");
        return rigaArticolo;
    }

    @Override
    public boolean creaRigaNoteAutomatica(AreaMagazzino areaMagazzino, String note) {
        LOGGER.debug("--> Enter creaRigaNoteAutomatica");
        start("creaRigaNoteAutomatica");
        boolean rigaGenerata = Boolean.FALSE;
        try {
            rigaGenerata = magazzinoDocumentoService.creaRigaNoteAutomatica(areaMagazzino, note);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("creaRigaNoteAutomatica");
        }
        LOGGER.debug("--> Exit creaRigaNoteAutomatica ");
        return rigaGenerata;
    }

    @Override
    public void esportaDocumento(AreaMagazzino areaMagazzino) throws EsportaDocumentoCassaException {
        LOGGER.debug("--> Enter esportaDocumento");
        start("esportaDocumento");
        try {
            magazzinoDocumentoService.esportaDocumento(areaMagazzino);
        } catch (EsportaDocumentoCassaException e) {
            throw e;
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);

        } finally {
            end("esportaDocumento");
        }
        LOGGER.debug("--> Exit esportaDocumento ");
    }

    @Override
    public void generaFatturazioneDifferitaTemporanea(List<AreaMagazzinoLite> areeDaFatturare,
            TipoDocumento tipoDocumentoDestinazione, Date dataDocumentoDestinazione, String noteFatturazione,
            SedeMagazzinoLite sedePerRifatturazione) throws RigaArticoloNonValidaException,
                    SedePerRifatturazioneAssenteException, SedeNonAppartieneAdEntitaException {
        LOGGER.debug("--> Enter generaFatturazioneDifferitaTemporanea");
        start("generaFatturazioneDifferitaTemporanea");
        try {
            magazzinoDocumentoService.generaFatturazioneDifferitaTemporanea(areeDaFatturare, tipoDocumentoDestinazione,
                    dataDocumentoDestinazione, noteFatturazione, sedePerRifatturazione);
        } catch (RigaArticoloNonValidaException e) {
            throw e;
        } catch (SedePerRifatturazioneAssenteException e1) {
            throw e1;
        } catch (SedeNonAppartieneAdEntitaException e) {
            throw e;
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("generaFatturazioneDifferitaTemporanea");
        }
        LOGGER.debug("--> Exit generaFatturazioneDifferitaTemporanea ");
    }

    @Override
    public List<AreaMagazzinoRicerca> generaInventario(Date dataInventario, Date dataInventarioArticolo,
            DepositoLite deposito) {
        LOGGER.debug("--> Enter generaInventario");
        start("generaInventario");
        List<AreaMagazzinoRicerca> aree = new ArrayList<AreaMagazzinoRicerca>();
        try {
            aree = gestioneInventarioArticoloService.generaInventario(dataInventario, dataInventarioArticolo, deposito);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("generaInventario");
        }
        LOGGER.debug("--> Exit generaInventario ");
        return aree;
    }

    @Override
    public PreFatturazioneDTO generaPreFatturazione(List<AreaMagazzinoLite> areeDaFatturare,
            TipoDocumento tipoDocumentoDestinazione, Date dataDocumentoDestinazione, String noteFatturazione,
            SedeMagazzinoLite sedePerRifatturazione) throws RigaArticoloNonValidaException,
                    SedePerRifatturazioneAssenteException, SedeNonAppartieneAdEntitaException {
        LOGGER.debug("--> Enter generaPreFatturazione");
        start("generaPreFatturazione");

        PreFatturazioneDTO preFatturazioneDTO = null;
        try {
            preFatturazioneDTO = magazzinoDocumentoService.generaPreFatturazione(areeDaFatturare,
                    tipoDocumentoDestinazione, dataDocumentoDestinazione, noteFatturazione, sedePerRifatturazione);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("generaPreFatturazione");
        }
        LOGGER.debug("--> Exit generaPreFatturazione ");
        return preFatturazioneDTO;
    }

    /**
     *
     * @return the magazzinoDocumentoService.
     */

    public MagazzinoDocumentoService getMagazzinoDocumentoService() {
        return magazzinoDocumentoService;
    }

    /**
     *
     * @return the partiteService.
     */
    public PartiteService getPartiteService() {
        return partiteService;
    }

    /**
     * @return the rateService
     */
    public RateService getRateService() {
        return rateService;
    }

    @Override
    public List<String> importaArticoliInventario(byte[] fileImportData, Integer idDeposito) {
        LOGGER.debug("--> Enter importaArticoliInventario");
        start("importaArticoliInventario");
        List<String> result = new ArrayList<String>();
        try {
            result = gestioneInventarioArticoloService.importaArticoliInventario(fileImportData, idDeposito);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("importaArticoliInventario");
        }
        LOGGER.debug("--> Exit importaArticoliInventario ");
        return result;
    }

    @Override
    public List<AreaMagazzinoRicerca> importaDocumenti(Collection<DocumentoImport> documenti, String codiceImporter) {
        LOGGER.debug("--> Enter importaDocumenti");
        start("importaDocumenti");
        List<AreaMagazzinoRicerca> areeRicerca = new ArrayList<AreaMagazzinoRicerca>();
        try {
            areeRicerca = magazzinoDocumentoService.importaDocumenti(documenti, codiceImporter);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("importaDocumenti");
        }
        LOGGER.debug("--> Exit importaDocumenti ");
        return areeRicerca;
    }

    @Override
    public void impostaComeEsportato(DatiGenerazione datiGenerazione) {
        LOGGER.debug("--> Enter impostaComeEsportato");
        start("impostaComeEsportato");
        try {
            magazzinoDocumentoService.impostaComeEsportato(datiGenerazione);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("impostaComeEsportato");
        }
        LOGGER.debug("--> Exit impostaComeEsportato ");
    }

    @Override
    public void inserisciRaggruppamentoArticoli(Integer idAreaMagazzino, ProvenienzaPrezzo provenienzaPrezzo,
            Integer idRaggruppamentoArticoli, Date data, Integer idSedeEntita, Integer idListinoAlternativo,
            Integer idListino, Importo importo, CodiceIva codiceIvaAlternativo, Integer idTipoMezzo,
            Integer idZonaGeografica, boolean noteSuDestinazione, TipoMovimento tipoMovimento, String codiceValuta,
            String codiceLingua, Integer idAgente, ETipologiaCodiceIvaAlternativo tipologiaCodiceIvaAlternativo,
            BigDecimal percentualeScontoCommerciale) {
        LOGGER.debug("--> Enter inserisciRaggruppamentoArticoli");
        start("inserisciRaggruppamentoArticoli");
        try {
            magazzinoDocumentoService.inserisciRaggruppamentoArticoli(idAreaMagazzino, provenienzaPrezzo,
                    idRaggruppamentoArticoli, data, idSedeEntita, idListinoAlternativo, idListino, importo,
                    codiceIvaAlternativo, idTipoMezzo, idZonaGeografica, noteSuDestinazione, tipoMovimento,
                    codiceValuta, codiceLingua, idAgente, tipologiaCodiceIvaAlternativo, percentualeScontoCommerciale);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("inserisciRaggruppamentoArticoli");
        }
        LOGGER.debug("--> Exit inserisciRaggruppamentoArticoli ");
    }

    @Override
    public void modificaPrezziRigheNonValidePerFatturazione(List<RigaArticoloLite> righeNonValide) {
        LOGGER.debug("--> Enter modificaPrezziRigheNonValidePerFatturazione");
        start("modificaPrezziRigheNonValidePerFatturazione");
        try {
            magazzinoDocumentoService.aggiornaPrezzoRighe(righeNonValide);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("modificaPrezziRigheNonValidePerFatturazione");
        }
        LOGGER.debug("--> Exit modificaPrezziRigheNonValidePerFatturazione ");
    }

    @Override
    public AreaMagazzinoFullDTO ricalcolaPrezziMagazzino(Integer idAreaMagazzino) {
        LOGGER.debug("--> Enter ricalcolaPrezziMagazzino");
        start("ricalcolaPrezziMagazzino");
        AreaMagazzinoFullDTO areaMagazzinoFullDTO = null;
        try {
            areaMagazzinoFullDTO = magazzinoDocumentoService.ricalcolaPrezziMagazzino(idAreaMagazzino);
        } catch (Exception e) {
            LOGGER.error("--> errore ", e);
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("ricalcolaPrezziMagazzino");
        }
        LOGGER.debug("--> Exit ricalcolaPrezziMagazzino ");
        return areaMagazzinoFullDTO;
    }

    @Override
    public List<AreaMagazzinoRicerca> ricercaAreeMagazzino(
            ParametriRicercaAreaMagazzino parametriRicercaAreaMagazzino) {
        LOGGER.debug("--> Enter ricercaAreeMagazzino");
        start("ricercaAreeMagazzino");
        List<AreaMagazzinoRicerca> areeMagazzino = null;
        try {
            areeMagazzino = magazzinoDocumentoService.ricercaAreeMagazzino(parametriRicercaAreaMagazzino);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("ricercaAreeMagazzino");
        }
        LOGGER.debug("--> Exit ricercaAreeMagazzino ");
        return areeMagazzino;
    }

    @Override
    public List<Documento> ricercaDocumentiSenzaAreaMagazzino(Documento documento, boolean soloAttributiNotNull) {
        LOGGER.debug("--> Enter ricercaDocumentiSenzaAreaMagazzino");
        start("ricercaDocumentiSenzaAreaMagazzino");
        List<Documento> documenti = new ArrayList<Documento>();
        try {
            documenti = magazzinoDocumentoService.ricercaDocumentiSenzaAreaMagazzino(documento, soloAttributiNotNull);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("ricercaDocumentiSenzaAreaMagazzino");
        }
        LOGGER.debug("--> Exit ricercaDocumentiSenzaAreaMagazzino ");
        return documenti;
    }

    @Override
    public AreaMagazzinoFullDTO salvaAreaMagazzino(AreaMagazzino areaMagazzino, AreaRate areaRate,
            boolean forzaSalvataggio) throws DocumentoAssenteBloccaException, DocumentoAssenteAvvisaException,
                    DocumentiEsistentiPerAreaMagazzinoException {
        LOGGER.debug("--> Enter salvaAreaMagazzino");
        start("salvaAreaMagazzino");
        AreaMagazzinoFullDTO areaMagazzinoFullDTO = null;
        try {
            areaMagazzinoFullDTO = magazzinoDocumentoService.salvaAreaMagazzino(areaMagazzino, areaRate,
                    forzaSalvataggio);
        } catch (DocumentoAssenteBloccaException e) {
            LOGGER.debug("--> Documento assente -> blocca documento ");
            throw e;
        } catch (DocumentoAssenteAvvisaException e) {
            LOGGER.debug("--> Documento assente -> avvisa ");
            throw e;
        } catch (DocumentiEsistentiPerAreaMagazzinoException e) {
            LOGGER.debug("--> Documenti esistenti ");
            throw e;
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaAreaMagazzino");
        }
        LOGGER.debug("--> Exit salvaAreaMagazzino ");
        return areaMagazzinoFullDTO;
    }

    @Override
    public InventarioArticolo salvaInventarioArticolo(InventarioArticolo inventarioArticolo) {
        LOGGER.debug("--> Enter salvaInventarioArticolo");
        start("salvaInventarioArticolo");
        InventarioArticolo inventarioArticoloSalvato = null;
        try {
            inventarioArticoloSalvato = gestioneInventarioArticoloService.salvaInventarioArticolo(inventarioArticolo);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaInventarioArticolo");
        }
        LOGGER.debug("--> Exit salvaInventarioArticolo ");
        return inventarioArticoloSalvato;
    }

    @Override
    public RigaIva salvaRigaIva(RigaIva rigaIva, TipoAreaContabile tipoAreaContabile)
            throws CodiceIvaCollegatoAssenteException {
        LOGGER.debug("--> Enter salvaRigaIva");
        start("salvaRigaIva");
        RigaIva rigaIvaSave = null;
        try {
            rigaIvaSave = ivaService.salvaRigaIva(rigaIva, tipoAreaContabile);
        } catch (CodiceIvaCollegatoAssenteException e) {
            throw e;
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaRigaIva");
        }
        LOGGER.debug("--> Exit salvaRigaIva ");
        return rigaIvaSave;
    }

    @Override
    public RigaMagazzino salvaRigaMagazzino(RigaMagazzino rigaMagazzino)
            throws RimanenzaLottiNonValidaException, RigheLottiNonValideException, QtaLottiMaggioreException {
        LOGGER.debug("--> Enter salvaRigaMagazzino");
        start("salvaRigaMagazzino");
        RigaMagazzino rigaResult = null;
        try {
            rigaResult = magazzinoDocumentoService.salvaRigaMagazzino(rigaMagazzino);
        } catch (RimanenzaLottiNonValidaException e) {
            throw e;
        } catch (RigheLottiNonValideException e) {
            throw e;
        } catch (QtaLottiMaggioreException e) {
            throw e;
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaRigaMagazzino");
        }
        LOGGER.debug("--> Exit salvaRigaMagazzino ");
        return rigaResult;
    }

    @Override
    public TipoAreaMagazzino salvaTipoAreaMagazzino(TipoAreaMagazzino tipoAreaMagazzino) {
        LOGGER.debug("--> Enter salvaTipoAreaMagazzino");
        start("salvaTipoAreaMagazzino");
        TipoAreaMagazzino tipoAreaMagazzinoCaricata = null;
        try {
            tipoAreaMagazzinoCaricata = magazzinoDocumentoService.salvaTipoAreaMagazzino(tipoAreaMagazzino);
        } catch (Exception e) {
            LOGGER.error("--> errore ", e);
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaTipoAreaMagazzino");
        }
        LOGGER.debug("--> Exit salvaTipoAreaMagazzino ");
        return tipoAreaMagazzinoCaricata;
    }

    /**
     * @param contabilitaService
     *            The contabilitaService to set.
     */
    public void setContabilitaService(ContabilitaService contabilitaService) {
        this.contabilitaService = contabilitaService;
    }

    /**
     * @param gestioneInventarioArticoloService
     *            the gestioneInventarioArticoloService to set
     */
    public void setGestioneInventarioArticoloService(
            GestioneInventarioArticoloService gestioneInventarioArticoloService) {
        this.gestioneInventarioArticoloService = gestioneInventarioArticoloService;
    }

    /**
     * @param ivaService
     *            The ivaService to set.
     */
    public void setIvaService(IvaService ivaService) {
        this.ivaService = ivaService;
    }

    /**
     *
     * @param magazzinoDocumentoService
     *            .
     */
    public void setMagazzinoDocumentoService(MagazzinoDocumentoService magazzinoDocumentoService) {
        this.magazzinoDocumentoService = magazzinoDocumentoService;
    }

    /**
     *
     * @param magazzinoStatisticheService
     *            .
     */
    public void setMagazzinoStatisticheService(MagazzinoStatisticheService magazzinoStatisticheService) {
        this.magazzinoStatisticheService = magazzinoStatisticheService;
    }

    /**
     *
     * @param partiteService
     *            .
     */
    public void setPartiteService(PartiteService partiteService) {
        this.partiteService = partiteService;
    }

    /**
     * @param rateService
     *            the rateService to set
     */
    public void setRateService(RateService rateService) {
        this.rateService = rateService;
    }

    /**
     * @param spedizioneDocumentiService
     *            the spedizioneDocumentiService to set
     */
    public void setSpedizioneDocumentiService(SpedizioneDocumentiService spedizioneDocumentiService) {
        this.spedizioneDocumentiService = spedizioneDocumentiService;
    }

    @Override
    public void spostaRighe(Set<Integer> righeDaSpostare, Integer idDest) {
        LOGGER.debug("--> Enter sposta");
        start("sposta");
        try {
            magazzinoDocumentoService.spostaRighe(righeDaSpostare, idDest);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("sposta");
        }
        LOGGER.debug("--> Exit sposta ");
    }

    @Override
    public AreaMagazzino totalizzaDocumento(StrategiaTotalizzazioneDocumento strategia, AreaMagazzino areaMagazzino,
            AreaPartite areaPartite) {
        LOGGER.debug("--> Enter totalizzaDocumento");
        start("totalizzaDocumento");
        AreaMagazzino areaMagazzinoTotalizzata = null;
        try {
            areaMagazzinoTotalizzata = magazzinoDocumentoService.totalizzaDocumento(strategia, areaMagazzino,
                    areaPartite);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("totalizzaDocumento");
        }
        LOGGER.debug("--> Exit totalizzaDocumento ");
        return areaMagazzinoTotalizzata;
    }

    @Override
    public AreaMagazzinoFullDTO validaRigheIva(AreaIva areaIva, Integer idAreaMagazzino) {
        LOGGER.debug("--> Enter validaRigheIva");
        start("validaRigheIva");
        AreaMagazzinoFullDTO areaMagazzinoFullDTO = null;
        try {
            areaMagazzinoFullDTO = magazzinoDocumentoService.validaAreaIva(areaIva, idAreaMagazzino);
        } catch (Exception e) {
            LOGGER.error("--> errore ", e);
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("validaRigheIva");
        }
        LOGGER.debug("--> Exit validaRigheIva ");
        return areaMagazzinoFullDTO;
    }

    @Override
    public AreaMagazzinoFullDTO validaRigheMagazzino(AreaMagazzino areaMagazzino, AreaRate areaRate,
            AreaContabileLite areaContabileLite, boolean forzaStato) throws TotaleDocumentoNonCoerenteException {
        LOGGER.debug("--> Enter confermaRigheMagazzino");
        start("confermaRigheMagazzino");
        AreaMagazzinoFullDTO areaMagazzinoFullDTO = null;
        try {
            boolean areaContabilePresente = false;
            if (areaContabileLite != null && areaContabileLite.getId() != null) {
                areaContabilePresente = true;
            }

            areaMagazzinoFullDTO = magazzinoDocumentoService.validaRigheMagazzino(areaMagazzino, areaRate,
                    areaContabilePresente, forzaStato);
        } catch (TotaleDocumentoNonCoerenteException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("--> errore ", e);
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("confermaRigheMagazzino");
        }
        LOGGER.debug("--> Exit confermaRigheMagazzino");
        return areaMagazzinoFullDTO;
    }

    @Override
    public Map<ArticoloConfigurazioneKey, Bom> valorizzaDistinte(
            ParametriValorizzazioneDistinte parametriValorizzazioneDistinte) {
        LOGGER.debug("--> Enter valorizzaDistinte");
        start("valorizzaDistinte");
        Map<ArticoloConfigurazioneKey, Bom> result = null;
        try {
            result = magazzinoStatisticheService.valorizzaDistinte(parametriValorizzazioneDistinte);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("valorizzaDistinte");
        }
        LOGGER.debug("--> Exit valorizzaDistinte ");
        return result;
    }
}
