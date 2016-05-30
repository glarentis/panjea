package it.eurotn.panjea.contabilita.rich.bd;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.documenti.service.exception.AreeCollegatePresentiException;
import it.eurotn.panjea.anagrafica.documenti.service.exception.DocumentoDuplicateException;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.contabilita.domain.AreaContabile;
import it.eurotn.panjea.contabilita.domain.ControPartita;
import it.eurotn.panjea.contabilita.domain.EstrattoConto;
import it.eurotn.panjea.contabilita.domain.Giornale;
import it.eurotn.panjea.contabilita.domain.GiornaleIva;
import it.eurotn.panjea.contabilita.domain.NoteAreaContabile;
import it.eurotn.panjea.contabilita.domain.RegistroIva.TipoRegistro;
import it.eurotn.panjea.contabilita.domain.RigaContabile;
import it.eurotn.panjea.contabilita.domain.SottoConto;
import it.eurotn.panjea.contabilita.domain.TipoAreaContabile;
import it.eurotn.panjea.contabilita.service.exception.AreaContabileDuplicateException;
import it.eurotn.panjea.contabilita.service.exception.CodiceIvaCollegatoAssenteException;
import it.eurotn.panjea.contabilita.service.exception.ContabilitaException;
import it.eurotn.panjea.contabilita.service.interfaces.ContabilitaService;
import it.eurotn.panjea.contabilita.util.AreaContabileDTO;
import it.eurotn.panjea.contabilita.util.AreaContabileFullDTO;
import it.eurotn.panjea.contabilita.util.LiquidazioneIvaDTO;
import it.eurotn.panjea.contabilita.util.ParametriAperturaContabile;
import it.eurotn.panjea.contabilita.util.ParametriChiusuraContabile;
import it.eurotn.panjea.contabilita.util.ParametriRicercaBilancio;
import it.eurotn.panjea.contabilita.util.ParametriRicercaBilancioConfronto;
import it.eurotn.panjea.contabilita.util.ParametriRicercaEstrattoConto;
import it.eurotn.panjea.contabilita.util.ParametriRicercaMovimentiContabili;
import it.eurotn.panjea.contabilita.util.ParametriRicercaSituazioneEP;
import it.eurotn.panjea.contabilita.util.RegistroLiquidazioneDTO;
import it.eurotn.panjea.contabilita.util.RigaContabileDTO;
import it.eurotn.panjea.contabilita.util.SaldoConto;
import it.eurotn.panjea.contabilita.util.SaldoContoConfronto;
import it.eurotn.panjea.contabilita.util.SituazioneEpDTO;
import it.eurotn.panjea.contabilita.util.TotaliCodiceIvaDTO;
import it.eurotn.panjea.iva.domain.AreaIva;
import it.eurotn.panjea.iva.domain.RigaIva;
import it.eurotn.panjea.iva.service.interfaces.IvaService;
import it.eurotn.panjea.iva.util.RigaIvaRicercaDTO;
import it.eurotn.panjea.iva.util.parametriricerca.ParametriRicercaRigheIva;
import it.eurotn.panjea.partite.domain.TipoAreaPartita;
import it.eurotn.panjea.rate.domain.AreaRate;
import it.eurotn.panjea.rich.bd.AbstractBaseBD;
import it.eurotn.panjea.utils.PanjeaSwingUtil;

/**
 * Implementazione del business delegate per ContabilitaService.
 *
 * @author adriano
 * @version 1.0, 16/mag/07
 */
public class ContabilitaBD extends AbstractBaseBD implements IContabilitaBD {

    public static final String BEAN_ID = "contabilitaBD";

    private static Logger logger = Logger.getLogger(ContabilitaBD.class);
    private ContabilitaService contabilitaService;
    private IvaService ivaService;

    /**
     * Costruttore.
     */
    public ContabilitaBD() {
        super();
    }

    @Override
    public void aggiornaStampaRegistroIva(GiornaleIva giornaleIva, Map<AreaContabileDTO, Integer> mapAreeContabili,
            Map<TotaliCodiceIvaDTO, Integer> mapRigheIva) {
        logger.debug("--> Enter aggiornaStampaRegistroIva");
        start("aggiornaStampaRegistroIva");

        try {
            contabilitaService.aggiornaStampaRegistroIva(giornaleIva, mapAreeContabili, mapRigheIva);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("aggiornaStampaRegistroIva");
        }
        logger.debug("--> Exit aggiornaStampaRegistroIva");
    }

    @Override
    public void asyncaggiornaStampaGiornale(Giornale giornale, Map<AreaContabileDTO, Integer> mapAreeContabili,
            Map<RigaContabileDTO, List<Integer>> mapRigheContabili) {
        logger.debug("--> Enter aggiornaStampaGiornale");
        start("aggiornaStampaGiornale");
        try {
            contabilitaService.aggiornaStampaGiornale(giornale, mapAreeContabili, mapRigheContabili);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("aggiornaStampaGiornale");
        }
        logger.debug("--> Exit aggiornaStampaGiornale");
    }

    @Override
    public BigDecimal calcoloSaldo(SottoConto conto, Date data, Integer annoEsercizio) {
        logger.debug("--> Enter calcoloSaldoDaData");
        start("calcoloSaldoDaData");
        BigDecimal saldo = null;
        try {
            saldo = contabilitaService.calcoloSaldo(conto, data, annoEsercizio);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("calcoloSaldoDaData");
        }
        logger.debug("--> Exit calcoloSaldoDaData");
        return saldo;
    }

    @Override
    public AreaContabile cambiaStatoAreaContabileInConfermato(AreaContabile areaContabile) {
        logger.debug("--> Enter cambiaStatoAreaContabileInConfermato");
        start("cambiaStatoAreaContabileInConfermato");
        AreaContabile areaContabile2 = null;
        try {
            areaContabile2 = contabilitaService.cambiaStatoAreaContabileInConfermato(areaContabile);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cambiaStatoAreaContabileInConfermato");
        }
        logger.debug("--> Exit cambiaStatoAreaContabileInConfermato ");
        return areaContabile2;
    }

    @Override
    public AreaContabile cambiaStatoAreaContabileInProvvisorio(AreaContabile areaContabile) {
        logger.debug("--> Enter cambiaStatoAreaContabileInProvvisorio");
        start("cambiaStatoAreaContabileInProvvisorio");
        AreaContabile areaContabile2 = null;
        try {
            areaContabile2 = contabilitaService.cambiaStatoAreaContabileInProvvisorio(areaContabile);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cambiaStatoAreaContabileInProvvisorio");
        }
        logger.debug("--> Exit cambiaStatoAreaContabileInProvvisorio ");
        return areaContabile2;
    }

    @Override
    public AreaContabile cambiaStatoAreaContabileInSimulato(AreaContabile areaContabile) {
        logger.debug("--> Enter cambiaStatoAreaContabileInSimulato");
        start("cambiaStatoAreaContabileInSimulato");
        AreaContabile areaContabile2 = null;
        try {
            areaContabile2 = contabilitaService.cambiaStatoAreaContabileInSimulato(areaContabile);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cambiaStatoAreaContabileInSimulato");
        }
        logger.debug("--> Exit cambiaStatoAreaContabileInSimulato ");
        return areaContabile2;
    }

    @Override
    public AreaContabile cambiaStatoAreaContabileInVerificato(AreaContabile areaContabile) {
        logger.debug("--> Enter cambiaStatoAreaContabileInVerificato");
        start("cambiaStatoAreaContabileInVerificato");
        AreaContabile areaContabile2 = null;
        try {
            areaContabile2 = contabilitaService.cambiaStatoAreaContabileInVerificato(areaContabile);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cambiaStatoAreaContabileInVerificato");
        }
        logger.debug("--> Exit cambiaStatoAreaContabileInVerificato ");
        return areaContabile2;
    }

    @Override
    public void cancellaAreaContabile(AreaContabile areaContabile) throws AreeCollegatePresentiException {
        logger.debug("--> Enter cancellaAreaContabile");
        start("cancellaAreaContabile");
        try {
            contabilitaService.cancellaAreaContabile(areaContabile);
        } catch (AreeCollegatePresentiException e) {
            throw e;
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaAreaContabile");
        }
        logger.debug("--> Exit cancellaAreaContabile ");
    }

    @Override
    public void cancellaAreaContabile(AreaContabile areaContabile, boolean deleteAreeCollegate,
            boolean forceDeleteAreeCollegate) throws AreeCollegatePresentiException {
        logger.debug("--> Enter cancellaAreaContabile");
        start("cancellaAreaContabile");
        try {
            contabilitaService.cancellaAreaContabile(areaContabile, deleteAreeCollegate, forceDeleteAreeCollegate);
        } catch (AreeCollegatePresentiException e) {
            throw e;
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaAreaContabile");
        }
        logger.debug("--> Exit cancellaAreaContabile ");
    }

    @Override
    public void cancellaAreeContabili(List<Integer> idAreeContabili, boolean deleteAreeCollegate) {
        logger.debug("--> Enter cancellaAreeContabili");
        start("cancellaAreeContabili");
        try {
            contabilitaService.cancellaAreeContabili(idAreeContabili, deleteAreeCollegate);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaAreeContabili");
        }
        logger.debug("--> Exit cancellaAreeContabili ");
    }

    @Override
    public AreaContabile cancellaRigaContabile(RigaContabile rigaContabile) {
        logger.debug("--> Enter cancellaRigaContabile");
        start("cancellaRigaContabile");
        AreaContabile areaContabile = null;
        try {
            areaContabile = contabilitaService.cancellaRigaContabile(rigaContabile);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaRigaContabile");
        }
        logger.debug("--> Exit cancellaRigaContabile ");
        return areaContabile;
    }

    @Override
    public void cancellaRigaIva(RigaIva rigaIva) {
        logger.debug("--> Enter cancellaRigaIva");
        start("cancellaRigaIva");
        try {
            ivaService.cancellaRigaIva(rigaIva);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaRigaIva");
        }
        logger.debug("--> Exit cancellaRigaIva ");

    }

    @Override
    public AreaContabile cancellaRigheContabili(AreaContabile areaContabile) {
        logger.debug("--> Enter cancellaRigheContabili");
        start("cancellaRigheContabili");
        AreaContabile areaContabileCaricata = null;
        try {
            areaContabileCaricata = contabilitaService.cancellaRigheContabili(areaContabile);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaRigheContabili");
        }
        logger.debug("--> Exit cancellaRigheContabili ");
        return areaContabileCaricata;
    }

    @Override
    public AreaContabile cancellaRigheContabili(List<RigaContabile> righeContabili) {
        logger.debug("--> Enter cancellaRigheContabili");
        start("cancellaRigheContabili");
        AreaContabile areaContabile = null;
        try {
            areaContabile = contabilitaService.cancellaRigheContabili(righeContabili);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaRigheContabili");
        }
        logger.debug("--> Exit cancellaRigheContabili ");
        return areaContabile;
    }

    @Override
    public AreaContabile caricaAreaContabile(Integer id) {
        logger.debug("--> Enter caricaAreaContabile");
        start("caricaAreaContabile");
        AreaContabile areaContabile = null;
        try {
            areaContabile = contabilitaService.caricaAreaContabile(id);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaAreaContabile");
        }
        logger.debug("--> Exit caricaAreaContabile ");
        return areaContabile;
    }

    @Override
    public AreaContabile caricaAreaContabileByDocumento(Integer idDocumento) {
        logger.debug("--> Enter caricAreaContabileByDocumento");
        start("caricAreaContabileByDocumento");

        AreaContabile areaContabile = null;
        try {
            areaContabile = contabilitaService.caricaAreaContabileByDocumento(idDocumento);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricAreaContabileByDocumento");
        }
        logger.debug("--> Exit caricAreaContabileByDocumento");
        return areaContabile;
    }

    @Override
    public AreaContabileFullDTO caricaAreaContabileFullDTO(Integer id) {
        logger.debug("--> Enter caricaAreaContabileFullDTO");
        start("caricaAreaContabileFullDTO");

        AreaContabileFullDTO areaContabileFullDTO = null;
        try {
            areaContabileFullDTO = contabilitaService.caricaAreaContabileFullDTO(id);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaAreaContabileFullDTO");
        }
        logger.debug("--> Exit caricaAreaContabileFullDTO ");
        return areaContabileFullDTO;
    }

    @Override
    public AreaContabileFullDTO caricaAreaContabileFullDTOByDocumento(Integer idDocumento) {
        logger.debug("--> Enter caricaAreaFullDTOContabileByDocumento");
        start("caricaAreaFullDTOContabileByDocumento");

        AreaContabileFullDTO areaContabileFullDTO = null;
        try {
            areaContabileFullDTO = contabilitaService.caricaAreaContabileFullDTOByDocumento(idDocumento);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaAreaFullDTOContabileByDocumento");
        }
        logger.debug("--> Exit caricaAreaFullDTOContabileByDocumento");
        return areaContabileFullDTO;
    }

    @Override
    public AreaIva caricaAreaIva(AreaIva areaIva) {
        logger.debug("--> Enter caricaAreaIva");
        start("caricaAreaIva");
        AreaIva areaIva2 = null;
        try {
            areaIva2 = ivaService.caricaAreaIva(areaIva);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaAreaIva");
        }
        logger.debug("--> Exit caricaAreaIva ");
        return areaIva2;
    }

    @Override
    public List<SaldoConto> caricaBilancio(ParametriRicercaBilancio parametriRicercaBilancio) {
        logger.debug("--> Enter caricaBilancio");
        start("caricaBilancio");
        List<SaldoConto> list = new ArrayList<SaldoConto>();
        try {
            list = contabilitaService.caricaBilancio(parametriRicercaBilancio);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaBilancio");
        }
        logger.debug("--> Exit caricaBilancio");
        return list;
    }

    @Override
    public List<SaldoContoConfronto> caricaBilancioConfronto(
            ParametriRicercaBilancioConfronto parametriRicercaBilancioConfronto) {
        logger.debug("--> Enter caricaBilancioConfronto");
        start("caricaBilancioConfronto");
        List<SaldoContoConfronto> list = new ArrayList<SaldoContoConfronto>();
        try {
            list = contabilitaService.caricaBilancioConfronto(parametriRicercaBilancioConfronto);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaBilancioConfronto");
        }
        logger.debug("--> Exit caricaBilancioConfronto");
        return list;
    }

    @Override
    public EstrattoConto caricaEstrattoConto(ParametriRicercaEstrattoConto parametriRicercaEstrattoConto) {
        logger.debug("--> Enter caricaEstrattoConto");
        start("caricaEstrattoConto");
        EstrattoConto estrattoConto = null;
        try {
            estrattoConto = contabilitaService.caricaEstrattoConto(parametriRicercaEstrattoConto);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaEstrattoConto");
        }
        logger.debug("--> Exit caricaEstrattoConto");
        return estrattoConto;
    }

    @Override
    public GiornaleIva caricaGiornaleIvaPrecedente(GiornaleIva giornaleIvaAttuale) {
        logger.debug("--> Enter caricaGiornaleIvaPrecedente");
        start("caricaGiornaleIvaPrecedente");

        GiornaleIva giornaleIvaPrecedente = null;
        try {
            giornaleIvaPrecedente = contabilitaService.caricaGiornaleIvaPrecedente(giornaleIvaAttuale);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaGiornaleIvaPrecedente");
        }
        logger.debug("--> Exit caricaGiornaleIvaPrecedente");
        return giornaleIvaPrecedente;
    }

    @Override
    public Giornale caricaGiornalePrecedente(Giornale giornaleAttuale) {
        logger.debug("--> Enter caricaGiornalePrecedente");
        start("caricaGiornalePrecedente");

        Giornale giornaleCaricato = null;
        try {
            giornaleCaricato = contabilitaService.caricaGiornalePrecedente(giornaleAttuale);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaGiornalePrecedente");
        }
        logger.debug("--> Exit caricaGiornalePrecedente");
        return giornaleCaricato;
    }

    @Override
    public List<Giornale> caricaGiornali(int annoCompetenza) {
        logger.debug("--> Enter caricaGiornali");
        start("caricaGiornali");

        List<Giornale> list = new ArrayList<Giornale>();
        try {
            list = contabilitaService.caricaGiornali(annoCompetenza);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaGiornali");
        }
        logger.debug("--> Exit caricaGiornali");
        return list;
    }

    @Override
    public List<GiornaleIva> caricaGiornaliIva(Integer anno, Integer mese) {
        logger.debug("--> Enter caricaGiornaliIva");
        start("caricaGiornaliIva");

        List<GiornaleIva> list = new ArrayList<GiornaleIva>();
        try {
            list = contabilitaService.caricaGiornaliIva(anno, mese);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaGiornaliIva");
        }
        logger.debug("--> Exit caricaGiornaliIva");
        return list;
    }

    @Override
    public LiquidazioneIvaDTO caricaLiquidazioneIva(Date dataInizioPeriodo, Date dataFinePeriodo) {
        logger.debug("--> Enter caricaLiquidazioneIva");
        start("caricaLiquidazioneIva");

        LiquidazioneIvaDTO liquidazioneIvaDTO = null;
        try {
            liquidazioneIvaDTO = contabilitaService.caricaLiquidazioneIva(dataInizioPeriodo, dataFinePeriodo);
        } catch (ContabilitaException e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaLiquidazioneIva");
        }

        logger.debug("--> Exit caricaLiquidazioneIva");
        return liquidazioneIvaDTO;
    }

    @Override
    public NoteAreaContabile caricaNoteSede(SedeEntita sedeEntita) {
        logger.debug("--> Enter caricaNoteSede");
        start("caricaNoteSede");

        NoteAreaContabile noteAreaContabile = null;
        try {
            noteAreaContabile = contabilitaService.caricaNoteSede(sedeEntita);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaNoteSede");
        }
        logger.debug("--> Exit caricaNoteSede ");
        return noteAreaContabile;
    }

    @Override
    public List<RegistroLiquidazioneDTO> caricaRegistriLiquidazione(Integer anno) {
        logger.debug("--> Enter ricercaAreaContabilePerLiquidazione");
        start("ricercaAreaContabilePerLiquidazione");
        List<RegistroLiquidazioneDTO> registri = null;
        try {
            registri = contabilitaService.caricaRegistriLiquidazione(anno);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("ricercaAreaContabilePerLiquidazione");
        }
        logger.debug("--> Exit ricercaAreaContabilePerLiquidazione ");
        return registri;
    }

    @Override
    public RigaContabile caricaRigaContabile(Integer id) {
        logger.debug("--> Enter caricaRigaContabile");
        start("caricaRigaContabile");
        RigaContabile rigaContabile = null;
        try {
            rigaContabile = contabilitaService.caricaRigaContabile(id);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaRigaContabile");
        }
        logger.debug("--> Exit caricaRigaContabile ");
        return rigaContabile;
    }

    @Override
    public RigaContabile caricaRigaContabileLazy(Integer id) {
        logger.debug("--> Enter caricaRigaContabileLazy");
        start("caricaRigaContabileLazy");
        RigaContabile result = null;
        try {
            result = contabilitaService.caricaRigaContabileLazy(id);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaRigaContabileLazy");
        }
        logger.debug("--> Exit caricaRigaContabileLazy ");
        return result;
    }

    @Override
    public RigaIva caricaRigaIva(Integer id) {
        logger.debug("--> Enter caricaRigaIva");
        start("caricaRigaIva");
        RigaIva rigaIva = null;
        try {
            rigaIva = ivaService.caricaRigaIva(id);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaRigaIva");
        }
        logger.debug("--> Exit caricaRigaIva ");
        return rigaIva;
    }

    @Override
    public List<RigaContabile> caricaRigheContabili(Integer idAreaContabile) {
        logger.debug("--> Enter caricaRigheContabili");
        start("caricaRigheContabili");
        List<RigaContabile> list = null;
        try {
            list = contabilitaService.caricaRigheContabili(idAreaContabile);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaRigheContabili");
        }
        logger.debug("--> Exit caricaRigheContabili");
        return list;
    }

    @Override
    public SituazioneEpDTO caricaSituazioneEconomicaPatrimoniale(
            ParametriRicercaSituazioneEP parametriRicercaSituazioneEP) {
        logger.debug("-->Enter SituazioneEpDTO situazioneEpDTO");
        start("caricaSituazioneEconomicaPatrimoniale");
        SituazioneEpDTO situazioneEpDTO = null;
        try {
            situazioneEpDTO = contabilitaService.caricaSituazioneEconomicoPatrimoniale(parametriRicercaSituazioneEP);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaSituazioneEconomicaPatrimoniale");
        }
        logger.debug("-->Exit SituazioneEpDTO situazioneEpDTO");
        return situazioneEpDTO;
    }

    @Override
    public List<TipoDocumento> caricaTipiDocumentoByTipoRegistro(TipoRegistro tipoRegistro) {
        logger.debug("--> Enter caricaTipiDocumentoByTipoRegistro");
        start("caricaTipiDocumentoByTipoRegistro");

        List<TipoDocumento> listTipoDocumento = new ArrayList<TipoDocumento>();

        try {
            listTipoDocumento = contabilitaService.caricaTipiDocumentoByTipoRegistro(tipoRegistro);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaTipiDocumentoByTipoRegistro");
        }

        logger.debug("--> Exit caricaTipiDocumentoByTipoRegistro");
        return listTipoDocumento;
    }

    @Override
    public TipoAreaPartita caricaTipoAreaPartitaPerTipoDocumento(TipoDocumento tipoDocumento) {
        logger.debug("--> Enter caricaTipoAreaPartitaPerTipoDocumento");
        start("caricaTipoAreaPartitaPerTipoDocumento");
        TipoAreaPartita tipoAreaPartita = null;
        try {
            tipoAreaPartita = contabilitaService.caricaTipoAreaPartitaPerTipoDocumento(tipoDocumento);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaTipoAreaPartitaPerTipoDocumento");
        }
        logger.debug("--> Exit caricaTipoAreaPartitaPerTipoDocumento ");
        return tipoAreaPartita;
    }

    @Override
    public List<String> caricaVariabiliFormulaControPartite() {
        logger.debug("--> Enter caricaVariabiliFormulaControPartite");
        start("caricaVariabiliFormulaControPartite");

        List<String> list = new ArrayList<String>();
        try {
            list = contabilitaService.caricaVariabiliFormulaControPartite();
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaVariabiliFormulaControPartite");
        }
        logger.debug("--> Exit caricaVariabiliFormulaControPartite");
        return list;
    }

    @Override
    public List<String> caricaVariabiliFormulaStrutturaContabile() {
        logger.debug("--> Enter caricaVariabiliFormulaStrutturaContabile");
        start("caricaVariabiliFormulaStrutturaContabile");

        List<String> list = new ArrayList<String>();
        try {
            list = contabilitaService.caricaVariabiliFormulaStrutturaContabile();
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaVariabiliFormulaStrutturaContabile");
        }
        logger.debug("--> Exit caricaVariabiliFormulaStrutturaContabile");
        return list;
    }

    @Override
    public List<RigaContabile> creaRigheContabili(AreaContabile areaContabile, List<ControPartita> list) {
        logger.debug("--> Enter creaRigheContabili");
        start("creaRigheContabili");
        List<RigaContabile> righeContabili = null;
        try {
            righeContabili = contabilitaService.creaRigheContabili(areaContabile, list);

        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("creaRigheContabili");
        }
        logger.debug("--> Exit creaRigheContabili");
        return righeContabili;
    }

    @Override
    public void creaRigheContabiliAutomatiche(AreaContabile areaContabile) {
        logger.debug("--> Enter creaRigheContabiliAutomatiche");
        start("creaRigheContabiliAutomatiche");
        try {
            contabilitaService.creaRigheContabiliAutomatiche(areaContabile);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("creaRigheContabiliAutomatiche");
        }
        logger.debug("--> Exit creaRigheContabiliAutomatiche ");
    }

    @Override
    public void eseguiAperturaContabile(ParametriAperturaContabile parametriAperturaContabile) {
        logger.debug("--> Enter eseguiAperturaContabile");
        start("eseguiAperturaContabile");
        try {
            contabilitaService.eseguiAperturaContabile(parametriAperturaContabile);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("eseguiAperturaContabile");
        }
        logger.debug("--> Exit eseguiAperturaContabile ");
    }

    @Override
    public void eseguiChiusuraContabile(ParametriChiusuraContabile parametriChiusuraContabile) {
        logger.debug("--> Enter eseguiChiusuraContabile");
        start("eseguiChiusuraContabile");
        try {
            contabilitaService.eseguiChiusuraContabile(parametriChiusuraContabile);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("eseguiChiusuraContabile");
        }
        logger.debug("--> Exit eseguiChiusuraContabile ");

    }

    /**
     * @return Returns the contabilitaService.
     */
    public ContabilitaService getContabilitaService() {
        return contabilitaService;
    }

    /**
     * @return IvaService
     */
    public IvaService getIvaService() {
        return ivaService;
    }

    @Override
    public boolean isAreaPresente(Integer idDocumento) {
        logger.debug("--> Enter isAreaPresente");
        start("isAreaPresente");
        boolean isAreaPresente = false;
        try {
            isAreaPresente = contabilitaService.isAreaPresente(idDocumento);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("isAreaPresente");
        }
        logger.debug("--> Exit isAreaPresente " + isAreaPresente);
        return isAreaPresente;
    }

    @Override
    public boolean isRigheContabiliPresenti(AreaContabile areaContabile) {
        logger.debug("--> Enter isRigheContabiliPresenti");
        start("isRigheContabiliPresenti");
        boolean isPresent = false;
        try {
            isPresent = contabilitaService.isRigheContabiliPresenti(areaContabile);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("isRigheContabiliPresenti");
        }
        logger.debug("--> Exit isRigheContabiliPresenti");
        return isPresent;
    }

    @Override
    public boolean isTipoAreaPresente(Integer idTipoDocumento) {
        logger.debug("--> Enter isTipoAreaPresente");
        start("isTipoAreaPresente");
        boolean isTipoAreaPresente = false;
        try {
            isTipoAreaPresente = contabilitaService.isTipoAreaPresente(idTipoDocumento);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("isTipoAreaPresente");
        }
        logger.debug("--> Exit isTipoAreaPresente ");
        return isTipoAreaPresente;
    }

    @Override
    public List<RigaContabileDTO> ricercaControlloAreeContabili(ParametriRicercaMovimentiContabili parametriRicerca) {
        logger.debug("--> Enter ricercaControlloAreeContabili");
        start("ricercaControlloAreeContabili");
        long tempoEsecuzione = Calendar.getInstance().getTimeInMillis();
        List<RigaContabileDTO> righeContabili = new ArrayList<RigaContabileDTO>();
        try {
            righeContabili = contabilitaService.ricercaControlloAreeContabili(parametriRicerca);
            tempoEsecuzione = Calendar.getInstance().getTimeInMillis() - tempoEsecuzione;
            logger.debug("--> tempo esecuzione " + tempoEsecuzione);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("ricercaControlloAreeContabili");
        }
        logger.debug("--> Exit ricercaControlloAreeContabili ");
        return righeContabili;
    }

    @Override
    public List<TotaliCodiceIvaDTO> ricercaRigheIva(ParametriRicercaMovimentiContabili parametriRicerca) {
        logger.debug("--> Enter ricercaRigheIva");
        start("ricercaRigheIva");

        List<TotaliCodiceIvaDTO> list = new ArrayList<TotaliCodiceIvaDTO>();
        try {
            list = contabilitaService.ricercaRigheIva(parametriRicerca);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("ricercaRigheIva");
        }
        logger.debug("--> Exit ricercaRigheIva");
        return list;
    }

    @Override
    public List<RigaIvaRicercaDTO> ricercaRigheIva(ParametriRicercaRigheIva parametriRicercaRigheIva) {
        logger.debug("--> Enter ricercaRigheIva");
        start("ricercaRigheIva");
        List<RigaIvaRicercaDTO> righeIva = null;
        try {
            righeIva = ivaService.ricercaRigheIva(parametriRicercaRigheIva);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("ricercaRigheIva");
        }
        logger.debug("--> Exit ricercaRigheIva ");
        return righeIva;
    }

    @Override
    public List<TotaliCodiceIvaDTO> ricercaRigheRiepilogoCodiciIva(
            ParametriRicercaMovimentiContabili parametriRicercaMovimentiContabili) {
        logger.debug("--> Enter ricercaRigheRiepilogoCodiciIva");
        start("ricercaRigheRiepilogoCodiciIva");

        List<TotaliCodiceIvaDTO> list = new ArrayList<TotaliCodiceIvaDTO>();
        try {
            list = contabilitaService.ricercaRigheRiepilogoCodiciIva(parametriRicercaMovimentiContabili);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("ricercaRigheRiepilogoCodiciIva");
        }
        logger.debug("--> Exit ricercaRigheRiepilogoCodiciIva");
        return list;
    }

    @Override
    public AreaContabile salvaAreaContabile(AreaContabile areaContabile)
            throws AreaContabileDuplicateException, DocumentoDuplicateException {
        logger.debug("--> Enter salvaAreaContabile");
        start("salvaAreaContabile");
        AreaContabile areaContabile2 = null;
        try {
            areaContabile2 = contabilitaService.salvaAreaContabile(areaContabile);
        } catch (AreaContabileDuplicateException e) {
            throw e;
        } catch (DocumentoDuplicateException e) {
            throw e;
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaAreaContabile");
        }
        logger.debug("--> Exit salvaAreaContabile ");
        return areaContabile2;
    }

    @Override
    public AreaContabileFullDTO salvaAreaContabile(AreaContabile areaContabile, AreaRate areaRate)
            throws AreaContabileDuplicateException, DocumentoDuplicateException {
        logger.debug("--> Enter salvaDocumentoContabile");
        start("salvaDocumentoContabile");
        AreaContabileFullDTO areaContabileFullDTO = null;
        try {
            areaContabileFullDTO = contabilitaService.salvaAreaContabile(areaContabile, areaRate);
        } catch (AreaContabileDuplicateException e) {
            throw e;
        } catch (DocumentoDuplicateException e) {
            throw e;
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaDocumentoContabile");
        }
        logger.debug("--> Exit salvaDocumentoContabile ");
        return areaContabileFullDTO;
    }

    @Override
    public AreaContabileFullDTO salvaDocumentoLiquidazione(AreaContabile areaContabile) {
        logger.debug("--> Enter salvaDocumentoContabile");
        start("salvaDocumentoContabile");
        AreaContabileFullDTO areaContabileFullDTO = null;
        try {
            areaContabileFullDTO = contabilitaService.salvaDocumentoLiquidazione(areaContabile);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaDocumentoContabile");
        }
        logger.debug("--> Exit salvaDocumentoContabile ");
        return areaContabileFullDTO;
    }

    @Override
    public GiornaleIva salvaGiornaleIva(GiornaleIva giornaleIva) {
        logger.debug("--> Enter salvaGiornaleIva");
        start("salvaGiornaleIva");
        try {
            giornaleIva = contabilitaService.salvaGiornaleIvaRiepilogativo(giornaleIva);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaGiornaleIva");
        }
        logger.debug("--> Exit salvaGiornaleIva ");
        return giornaleIva;
    }

    @Override
    public RigaContabile salvaRigaContabile(RigaContabile rigaContabile) {
        logger.debug("--> Enter salvaRigaContabile");
        start("salvaRigaContabile");
        RigaContabile rigaContabile2 = null;
        try {
            rigaContabile2 = contabilitaService.salvaRigaContabile(rigaContabile);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaRigaContabile");
        }
        logger.debug("--> Exit salvaRigaContabile ");
        return rigaContabile2;
    }

    @Override
    public RigaIva salvaRigaIva(RigaIva rigaIva, TipoAreaContabile tipoAreaContabile)
            throws CodiceIvaCollegatoAssenteException {
        logger.debug("--> Enter salvaRigaIva");
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
        logger.debug("--> Exit salvaRigaIva ");
        return rigaIvaSave;
    }

    /**
     * @param contabilitaService
     *            The contabilitaService to set.
     */
    public void setContabilitaService(ContabilitaService contabilitaService) {
        this.contabilitaService = contabilitaService;
    }

    /**
     * @param ivaService
     *            the ivaService to set
     */
    public void setIvaService(IvaService ivaService) {
        this.ivaService = ivaService;
    }

    @Override
    public void validaAreaContabile(int idAreaContabile) {
        logger.debug("--> Enter validaAreaContabile");
        start("validaAreaContabile");
        try {
            contabilitaService.validaAreaContabile(idAreaContabile);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("validaAreaContabile");
        }
        logger.debug("--> Exit validaAreaContabile ");

    }

    @Override
    public AreaContabileFullDTO validaAreaIva(AreaIva areaIva, Integer idAreaContabile) {
        logger.debug("--> Enter validaAreaIva");
        start("validaAreaIva");
        AreaContabileFullDTO areaContabileFullDTO = null;
        try {
            areaContabileFullDTO = contabilitaService.validaAreaIva(areaIva, idAreaContabile);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("validaAreaIva");
        }
        logger.debug("--> Exit validaAreaIva");
        return areaContabileFullDTO;
    }

    @Override
    public AreaContabileFullDTO validaRigheContabili(AreaContabile areaContabile) {
        logger.debug("--> Enter validaRigheContabili");
        start("validaRigheContabili");
        AreaContabileFullDTO areaContabileFullDTO = null;
        try {
            areaContabileFullDTO = contabilitaService.validaRigheContabili(areaContabile);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("validaRigheContabili");
        }
        logger.debug("--> Exit validaRigheContabili");
        return areaContabileFullDTO;
    }

    @Override
    public List<AreaContabileDTO> verificaRigheSenzaCentriDiCosto() {
        logger.debug("--> Enter verificaRigheSenzaCentriDiCosto");
        start("verificaRigheSenzaCentriDiCosto");
        List<AreaContabileDTO> result = null;
        try {
            result = contabilitaService.verificaRigheSenzaCentriDiCosto();
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("verificaRigheSenzaCentriDiCosto");
        }
        logger.debug("--> Exit verificaRigheSenzaCentriDiCosto ");
        return result;
    }

}
