package it.eurotn.panjea.ordini.rich.bd;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.domain.CodiceIva;
import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.lotti.exception.EvasioneLottiException;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.ConfigurazioneDistinta;
import it.eurotn.panjea.magazzino.domain.ProvenienzaPrezzoArticolo;
import it.eurotn.panjea.magazzino.domain.RigaArticoloDistinta;
import it.eurotn.panjea.magazzino.domain.SedeMagazzino.ETipologiaCodiceIvaAlternativo;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino.ProvenienzaPrezzo;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino.TipoMovimento;
import it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.provvigioni.interfaces.RigaDocumentoVariazioneProvvigioneStrategy;
import it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.provvigioni.interfaces.TipoVariazioneProvvigioneStrategy;
import it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.sconti.interfaces.RigaDocumentoVariazioneScontoStrategy;
import it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.sconti.interfaces.TipoVariazioneScontoStrategy;
import it.eurotn.panjea.magazzino.service.exception.ContabilizzazioneException;
import it.eurotn.panjea.magazzino.util.ParametriCreazioneRigaArticolo;
import it.eurotn.panjea.magazzino.util.ParametriRicercaAreaMagazzino;
import it.eurotn.panjea.magazzino.util.RigaDestinazione;
import it.eurotn.panjea.ordini.domain.OrdineImportato;
import it.eurotn.panjea.ordini.domain.RigaArticolo;
import it.eurotn.panjea.ordini.domain.RigaOrdine;
import it.eurotn.panjea.ordini.domain.RigaOrdineImportata;
import it.eurotn.panjea.ordini.domain.documento.AreaOrdine;
import it.eurotn.panjea.ordini.domain.documento.TipoAreaOrdine;
import it.eurotn.panjea.ordini.domain.documento.evasione.DistintaCarico;
import it.eurotn.panjea.ordini.domain.documento.evasione.RigaDistintaCarico;
import it.eurotn.panjea.ordini.domain.documento.evasione.RigaDistintaCaricoProduzione;
import it.eurotn.panjea.ordini.exception.CodicePagamentoAssenteException;
import it.eurotn.panjea.ordini.exception.CodicePagamentoEvasioneAssenteException;
import it.eurotn.panjea.ordini.exception.EntitaSenzaTipoDocumentoEvasioneException;
import it.eurotn.panjea.ordini.manager.documento.evasione.DatiDistintaCaricoVerifica;
import it.eurotn.panjea.ordini.service.interfaces.OrdiniDocumentoService;
import it.eurotn.panjea.ordini.util.AreaOrdineFullDTO;
import it.eurotn.panjea.ordini.util.AreaOrdineRicerca;
import it.eurotn.panjea.ordini.util.ParametriRicercaProduzione;
import it.eurotn.panjea.ordini.util.RigaMovimentazione;
import it.eurotn.panjea.ordini.util.RigaOrdineDTO;
import it.eurotn.panjea.ordini.util.parametriricerca.ParametriRicercaAreaOrdine;
import it.eurotn.panjea.ordini.util.parametriricerca.ParametriRicercaEvasione;
import it.eurotn.panjea.ordini.util.parametriricerca.ParametriRicercaMovimentazione;
import it.eurotn.panjea.ordini.util.parametriricerca.ParametriRicercaOrdiniImportati;
import it.eurotn.panjea.ordini.util.righeinserimento.ParametriRigheOrdineInserimento;
import it.eurotn.panjea.ordini.util.righeinserimento.RigaOrdineInserimento;
import it.eurotn.panjea.ordini.util.righeinserimento.RigheOrdineInserimento;
import it.eurotn.panjea.partite.domain.AreaPartite;
import it.eurotn.panjea.rate.domain.AreaRate;
import it.eurotn.panjea.rich.bd.AbstractBaseBD;
import it.eurotn.panjea.rich.bd.AsyncMethodInvocation;
import it.eurotn.panjea.utils.PanjeaSwingUtil;

public class OrdiniDocumentoBD extends AbstractBaseBD implements IOrdiniDocumentoBD {

    private static Logger LOGGER = Logger.getLogger(OrdiniDocumentoBD.class);

    public static final String BEAN_ID = "ordiniDocumentoBD";

    private OrdiniDocumentoService ordiniDocumentoService;

    @Override
    public void aggiornaDataConsegna(AreaOrdine areaOrdine, Date dataRiferimento) {
        LOGGER.debug("--> Enter aggiornaDataConsegna");
        start("aggiornaDataConsegna");
        try {
            ordiniDocumentoService.aggiornaDataConsegna(areaOrdine, dataRiferimento);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("aggiornaDataConsegna");
        }
        LOGGER.debug("--> Exit aggiornaDataConsegna ");
    }

    @Override
    public List<RigaDistintaCarico> aggiornaRigheCaricoConDatiEvasione(
            List<RigaDistintaCarico> righeDistintaDaAggiornare) {
        LOGGER.debug("--> Enter aggiornaRigheCaricoConDatiEvasione");
        start("aggiornaRigheCaricoConDatiEvasione");
        List<RigaDistintaCarico> result = null;
        try {
            result = ordiniDocumentoService.aggiornaRigheCaricoConDatiEvasione(righeDistintaDaAggiornare);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("aggiornaRigheCaricoConDatiEvasione");
        }
        LOGGER.debug("--> Exit aggiornaRigheCaricoConDatiEvasione ");
        return result;
    }

    @Override
    public void aggiungiVariazione(Integer idAreaOrdine, BigDecimal variazione, BigDecimal percProvvigione,
            RigaDocumentoVariazioneScontoStrategy variazioneScontoStrategy,
            TipoVariazioneScontoStrategy tipoVariazioneScontoStrategy,
            RigaDocumentoVariazioneProvvigioneStrategy variazioneProvvigioneStrategy,
            TipoVariazioneProvvigioneStrategy tipoVariazioneProvvigioneStrategy) {
        LOGGER.debug("--> Enter aggiungiVariazione");
        start("aggiungiVariazione");
        try {
            ordiniDocumentoService.aggiungiVariazione(idAreaOrdine, variazione, percProvvigione,
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
    public RigaArticolo associaConfigurazioneDistintaARigaOrdine(RigaArticolo rigaArticolo,
            ConfigurazioneDistinta configurazioneDistintaDaAssociare) {
        RigaArticolo rigaOrdine = null;
        LOGGER.debug("--> Enter associaConfigurazioneDistintaARigaOrdine");
        start("associaConfigurazioneDistintaARigaOrdine");
        try {
            rigaOrdine = ordiniDocumentoService.associaConfigurazioneDistintaARigaOrdine(rigaArticolo,
                    configurazioneDistintaDaAssociare);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("associaConfigurazioneDistintaARigaOrdine");
        }
        LOGGER.debug("--> Exit associaConfigurazioneDistintaARigaOrdine ");
        return rigaOrdine;
    }

    @Override
    public AreaOrdine bloccaOrdine(int idAreaOrdine, boolean blocca) {
        LOGGER.debug("--> Enter bloccaOrdine");
        start("bloccaOrdine");
        AreaOrdine result = null;
        try {
            result = ordiniDocumentoService.bloccaOrdine(idAreaOrdine, blocca);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("bloccaOrdine");
        }
        LOGGER.debug("--> Exit bloccaOrdine ");
        return result;
    }

    @Override
    public List<AreaOrdineRicerca> bloccaOrdini(Collection<Integer> idOrdini, boolean blocca) {
        LOGGER.debug("--> Enter bloccaOrdini");
        start("bloccaOrdine");
        List<AreaOrdineRicerca> aree = null;
        try {
            aree = ordiniDocumentoService.bloccaOrdini(idOrdini, blocca);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("bloccaOrdine");
        }
        LOGGER.debug("--> Exit bloccaOrdini ");
        return aree;
    }

    @Override
    public Map<ArticoloLite, Double> calcolaGiacenze(DepositoLite depositoLite, Date data) {
        LOGGER.debug("--> Enter calcolaGiacenze");
        start("calcolaGiacenze");
        Map<ArticoloLite, Double> mapResult = new HashMap<ArticoloLite, Double>();
        try {
            mapResult = ordiniDocumentoService.calcolaGiacenze(depositoLite, data);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("calcolaGiacenze");
        }
        LOGGER.debug("--> Exit calcolaGiacenze ");
        return mapResult;
    }

    @Override
    public void cancellaAreaOrdine(AreaOrdine areaOrdine) {
        LOGGER.debug("--> Enter cancellaAreaOrdine");
        start("cancellaAreaOrdine");
        try {
            ordiniDocumentoService.cancellaAreaOrdine(areaOrdine);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaAreaOrdine");
        }
        LOGGER.debug("--> Exit cancellaAreaOrdine ");
    }

    @Override
    public void cancellaAreeOrdine(List<AreaOrdine> areeOrdine) {
        LOGGER.debug("--> Enter cancellaAreeOrdine");
        start("cancellaAreeOrdine");
        try {
            ordiniDocumentoService.cancellaAreeOrdine(areeOrdine);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaAreeOrdine");
        }
        LOGGER.debug("--> Exit cancellaAreeOrdine ");

    }

    @Override
    public void cancellaOrdiniImportati(Collection<String> numeroOrdini) {
        LOGGER.debug("--> Enter cancellaOrdiniImportatiPerAgente");
        start("cancellaOrdiniImportatiPerAgente");
        try {
            ordiniDocumentoService.cancellaOrdiniImportati(numeroOrdini);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaOrdiniImportatiPerAgente");
        }
        LOGGER.debug("--> Exit cancellaOrdiniImportatiPerAgente ");
    }

    @Override
    public void cancellaOrdiniImportatiPerAgente(String codiceAgente) {
        LOGGER.debug("--> Enter cancellaOrdiniImportatiPerAgente");
        start("cancellaOrdiniImportatiPerAgente");
        try {
            ordiniDocumentoService.cancellaOrdiniImportatiPerAgente(codiceAgente);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaOrdiniImportatiPerAgente");
        }
        LOGGER.debug("--> Exit cancellaOrdiniImportatiPerAgente ");
    }

    @Override
    public AreaOrdine cancellaRigaOrdine(RigaOrdine rigaOrdine) {
        LOGGER.debug("--> Enter cancellaRigaOrdine");
        start("cancellaRigaOrdine");

        AreaOrdine areaOrdine = null;
        try {
            areaOrdine = ordiniDocumentoService.cancellaRigaOrdine(rigaOrdine);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaRigaOrdine");
        }
        LOGGER.debug("--> Exit cancellaRigaOrdine ");
        return areaOrdine;
    }

    @Override
    public void cancellaRigheCollegate(int rigaOrdineOrigine, int rigaOrdineDestinazione) {
        LOGGER.debug("--> Enter cancellaRigheCollegate");
        start("cancellaRigheCollegate");
        try {
            ordiniDocumentoService.cancellaRigheCollegate(rigaOrdineOrigine, rigaOrdineDestinazione);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaRigheCollegate");
        }
        LOGGER.debug("--> Exit cancellaRigheCollegate ");
    }

    @Override
    public void cancellaRigheDistintaCarico(Set<RigaDistintaCarico> righeDistintaCarico) {
        LOGGER.debug("--> Enter cancellaRigheDistintaCarico");
        start("rimuoviRigheDistintaCarico");
        try {
            ordiniDocumentoService.cancellaRigheDistintaCarico(righeDistintaCarico);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("rimuoviRigheDistintaCarico");
        }
        LOGGER.debug("--> Exit cancellaRigheDistintaCarico");

    }

    @Override
    public void cancellaRigheDistintaCaricoLotti(RigaDistintaCarico rigaDistintaCarico) {
        LOGGER.debug("--> Enter cancellaRigheDistintaCaricoLotti");
        start("cancellaRigheDistintaCaricoLotti");
        try {
            ordiniDocumentoService.cancellaRigheDistintaCaricoLotti(rigaDistintaCarico);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaRigheDistintaCaricoLotti");
        }
        LOGGER.debug("--> Exit cancellaRigheDistintaCaricoLotti ");
    }

    @Override
    public void cancellaTipoAreaOrdine(TipoAreaOrdine tipoAreaOrdine) {
        LOGGER.debug("--> Enter cancellaTipoAreaOrdine");
        start("cancellaTipoAreaOrdine");
        try {
            ordiniDocumentoService.cancellaTipoAreaOrdine(tipoAreaOrdine);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaTipoAreaOrdine");
        }
        LOGGER.debug("--> Exit cancellaTipoAreaOrdine ");

    }

    @Override
    public AreaOrdineFullDTO caricaAreaOrdineFullDTO(AreaOrdine areaOrdine) {
        LOGGER.debug("--> Enter caricaAreaOrdineFullDTO");
        start("caricaAreaOrdineFullDTO");
        AreaOrdineFullDTO areaOrdineFullDTO = null;
        try {
            areaOrdineFullDTO = ordiniDocumentoService.caricaAreaOrdineFullDTO(areaOrdine);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaAreaOrdineFullDTO");
        }
        LOGGER.debug("--> Exit caricaAreaOrdineFullDTO ");
        return areaOrdineFullDTO;
    }

    @Override
    public DatiDistintaCaricoVerifica caricaDatiVerifica(Date dataInizioTrasporto) {
        LOGGER.debug("--> Enter caricaDatiVerifica");
        start("caricaDatiVerifica");
        DatiDistintaCaricoVerifica result = null;
        try {
            result = ordiniDocumentoService.caricaDatiVerifica(dataInizioTrasporto);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaDatiVerifica");
        }
        LOGGER.debug("--> Exit caricaDatiVerifica ");
        return result;
    }

    @Override
    public List<RigaMovimentazione> caricaMovimentazione(ParametriRicercaMovimentazione parametriRicercaMovimentazione,
            int page, int sizeOfPage) {
        LOGGER.debug("--> Enter caricaMovimentazione");
        start("caricaMovimentazione");

        List<RigaMovimentazione> listResult = new ArrayList<RigaMovimentazione>();
        try {
            listResult = ordiniDocumentoService.caricaMovimentazione(parametriRicercaMovimentazione, page, sizeOfPage);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaMovimentazione");
        }
        LOGGER.debug("--> Exit caricaMovimentazione ");
        return listResult;
    }

    @Override
    public RigaOrdine caricaRigaOrdine(RigaOrdine rigaOrdine) {
        LOGGER.debug("--> Enter caricaRigaOrdine");
        start("caricaRigaOrdine");
        RigaOrdine rigaOrdineCaricata = null;
        try {
            rigaOrdineCaricata = ordiniDocumentoService.caricaRigaOrdine(rigaOrdine);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaRigaOrdine");
        }
        LOGGER.debug("--> Exit caricaRigaOrdine ");
        return rigaOrdineCaricata;
    }

    @Override
    public List<RigaDestinazione> caricaRigheCollegate(RigaOrdine rigaOrdine) {
        LOGGER.debug("--> Enter caricaRigheCollegate");
        start("caricaRigheCollegate");
        List<RigaDestinazione> righeDest = null;
        try {
            righeDest = ordiniDocumentoService.caricaRigheCollegate(rigaOrdine);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaRigheCollegate");
        }
        LOGGER.debug("--> Exit caricaRigheCollegate ");
        return righeDest;
    }

    @Override
    public List<RigaDistintaCarico> caricaRigheDistintaCarico(List<DistintaCarico> distinte) {
        LOGGER.debug("--> Enter caricaRigheDistintaCarico");
        start("caricaRigheDistintaCarico");
        List<RigaDistintaCarico> righeDistintaCarico = null;
        try {
            righeDistintaCarico = ordiniDocumentoService.caricaRigheDistintaCarico(distinte);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaRigheDistintaCarico");
        }
        LOGGER.debug("--> Exit caricaRigheDistintaCarico ");
        return righeDistintaCarico;
    }

    @Override
    public List<RigaArticoloDistinta> caricaRigheDistintaMagazzino(ParametriRicercaAreaMagazzino parametri) {
        LOGGER.debug("--> Enter caricaRigheMagazzino");
        start("caricaRigheMagazzino");

        List<RigaArticoloDistinta> righe = null;
        try {
            righe = ordiniDocumentoService.caricaRigheDistintaMagazzino(parametri);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaRigheMagazzino");
        }
        LOGGER.debug("--> Exit caricaRigheMagazzino ");
        return righe;
    }

    @Override
    public List<RigaDistintaCarico> caricaRigheEvasione(ParametriRicercaEvasione parametriRicercaEvasione) {
        LOGGER.debug("--> Enter caricaRigheEvasione");
        start("caricaRigheEvasione");
        List<RigaDistintaCarico> righe = new ArrayList<RigaDistintaCarico>();
        try {
            righe = ordiniDocumentoService.caricaRigheEvasione(parametriRicercaEvasione);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaRigheEvasione");
        }
        LOGGER.debug("--> Exit caricaRigheEvasione ");
        return righe;
    }

    @Override
    public List<RigaDistintaCaricoProduzione> caricaRigheEvasioneProduzione(
            ParametriRicercaProduzione parametriRicercaProduzione) {
        LOGGER.debug("--> Enter caricaRigheOrdineProduzione");
        start("caricaRigheOrdineProduzione");
        List<RigaDistintaCaricoProduzione> listResult = new ArrayList<RigaDistintaCaricoProduzione>();
        try {
            listResult = ordiniDocumentoService.caricaRigheEvasioneProduzione(parametriRicercaProduzione);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaRigheOrdineProduzione");
        }
        LOGGER.debug("--> Exit caricaRigheOrdineProduzione ");
        return listResult;
    }

    @Override
    public List<RigaDistintaCarico> caricaRigheInMagazzino() {
        LOGGER.debug("--> Enter caricaRigheInMagazzino");
        start("caricaRigheInMagazzino");
        List<RigaDistintaCarico> righe = Collections.emptyList();
        try {
            righe = ordiniDocumentoService.caricaRigheInMagazzino();
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaRigheInMagazzino");
        }
        LOGGER.debug("--> Exit caricaRigheInMagazzino ");
        return righe;
    }

    @Override
    public List<RigaOrdine> caricaRigheOrdine(AreaOrdine areaOrdine) {
        LOGGER.debug("--> Enter caricaRigheOrdine");
        start("caricaRigheOrdine");
        List<RigaOrdine> listResult = new ArrayList<RigaOrdine>();
        try {
            listResult = ordiniDocumentoService.caricaRigheOrdine(areaOrdine);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaRigheOrdine");
        }
        LOGGER.debug("--> Exit caricaRigheOrdine ");
        return listResult;
    }

    @Override
    public List<RigaOrdineDTO> caricaRigheOrdineDTO(AreaOrdine areaOrdine) {
        LOGGER.debug("--> Enter caricaRigheOrdineDTO");
        start("caricaRigheOrdineDTO");
        List<RigaOrdineDTO> righeOrdine = new ArrayList<RigaOrdineDTO>();
        try {
            righeOrdine = ordiniDocumentoService.caricaRigheOrdineDTO(areaOrdine);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaRigheOrdineDTO");
        }
        LOGGER.debug("--> Exit caricaRigheOrdineDTO ");
        return righeOrdine;
    }

    @Override
    public List<RigaOrdineImportata> caricaRigheOrdineImportate(ParametriRicercaOrdiniImportati parametri) {
        LOGGER.debug("--> Enter caricaRigheOrdineImportate");
        start("caricaRigheOrdineImportate");
        List<RigaOrdineImportata> righe = Collections.emptyList();
        try {
            righe = ordiniDocumentoService.caricaRigheOrdineImportate(parametri);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaRigheOrdineImportate");
        }
        LOGGER.debug("--> Exit caricaRigheOrdineImportate ");
        return righe;
    }

    @AsyncMethodInvocation
    @Override
    public RigheOrdineInserimento caricaRigheOrdineInserimento(ParametriRigheOrdineInserimento parametri) {
        LOGGER.debug("--> Enter caricaRigheOrdineInserimento");
        start("caricaRigheOrdineInserimento");

        RigheOrdineInserimento righeInserimento = null;
        try {
            righeInserimento = ordiniDocumentoService.caricaRigheOrdineInserimento(parametri);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaRigheOrdineInserimento");
        }
        LOGGER.debug("--> Exit caricaRigheOrdineInserimento ");
        return righeInserimento;
    }

    @Override
    public List<TipoAreaOrdine> caricaTipiAreaOrdine(String fieldSearch, String valueSearch,
            boolean loadTipiDocumentoDisabilitati) {
        LOGGER.debug("--> Enter caricaTipiAreaOrdine");
        start("caricaTipiAreaOrdine");
        List<TipoAreaOrdine> result = null;
        try {
            result = ordiniDocumentoService.caricaTipiAreaOrdine(fieldSearch, valueSearch,
                    loadTipiDocumentoDisabilitati);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaTipiAreaOrdine");
        }
        LOGGER.debug("--> Exit caricaTipiAreaOrdine ");
        return result;
    }

    @Override
    public List<TipoDocumento> caricaTipiDocumentiOrdine() {
        LOGGER.debug("--> Enter caricaTipiDocumentiOrdine");
        start("caricaTipiDocumentiOrdine");
        List<TipoDocumento> result = null;
        try {
            result = ordiniDocumentoService.caricaTipiDocumentiOrdine();
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaTipiDocumentiOrdine");
        }
        LOGGER.debug("--> Exit caricaTipiDocumentiOrdine ");
        return result;
    }

    @Override
    public TipoAreaOrdine caricaTipoAreaOrdine(Integer id) {
        LOGGER.debug("--> Enter caricaTipoAreaOrdine");
        start("caricaTipoAreaOrdine");
        TipoAreaOrdine result = null;
        try {
            result = ordiniDocumentoService.caricaTipoAreaOrdine(id);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaTipoAreaOrdine");
        }
        LOGGER.debug("--> Exit caricaTipoAreaOrdine ");
        return result;
    }

    @Override
    public TipoAreaOrdine caricaTipoAreaOrdinePerTipoDocumento(Integer idTipoDocumento) {
        LOGGER.debug("--> Enter caricaTipoAreaOrdinePerTipoDocumento");
        start("caricaTipoAreaOrdinePerTipoDocumento");
        TipoAreaOrdine result = null;
        try {
            result = ordiniDocumentoService.caricaTipoAreaOrdinePerTipoDocumento(idTipoDocumento);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaTipoAreaOrdinePerTipoDocumento");
        }
        LOGGER.debug("--> Exit caricaTipoAreaOrdinePerTipoDocumento ");
        return result;
    }

    @Override
    public void collegaTestata(Set<Integer> righeOrdineDaCambiare) {
        LOGGER.debug("--> Enter collegaTestata");
        start("collegaTestata");
        try {
            ordiniDocumentoService.collegaTestata(righeOrdineDaCambiare);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("collegaTestata");
        }
        LOGGER.debug("--> Exit collegaTestata ");

    }

    @Override
    public Long confermaOrdiniImportati(Collection<OrdineImportato> ordiniDaConfermare) {
        LOGGER.debug("--> Enter confermaOrdiniImportati");
        start("confermaOrdiniImportati");
        long result = new Long(0);
        try {
            result = ordiniDocumentoService.confermaOrdiniImportati(ordiniDaConfermare);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("confermaOrdiniImportati");
        }
        LOGGER.debug("--> Exit confermaOrdiniImportati ");
        return result;
    }

    @Override
    public AreaOrdineFullDTO copiaOrdine(Integer idAreaOrdine) {
        LOGGER.debug("--> Enter copiaOrdine");
        start("copiaOrdine");
        AreaOrdineFullDTO areaOrdineFullDTO = null;
        try {
            areaOrdineFullDTO = ordiniDocumentoService.copiaOrdine(idAreaOrdine);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("copiaOrdine");
        }
        LOGGER.debug("--> Exit copiaOrdine ");
        return areaOrdineFullDTO;
    }

    @Override
    public List<DistintaCarico> creaDistintadiCarico(List<RigaDistintaCarico> righeEvasione) {
        LOGGER.debug("--> Enter creaDistintadiCarico");
        start("creaDistintadiCarico");
        List<DistintaCarico> distinteDiCarico = null;
        try {
            distinteDiCarico = ordiniDocumentoService.creaDistintadiCarico(righeEvasione);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("creaDistintadiCarico");
        }
        LOGGER.debug("--> Exit creaDistintadiCarico ");
        return distinteDiCarico;
    }

    @Override
    public RigaArticolo creaRigaArticolo(ProvenienzaPrezzo provenienzaPrezzo, Integer idArticolo, Date data,
            Integer idSedeEntita, Integer idListinoAlternativo, Integer idListino, Importo importo,
            CodiceIva codiceIvaAlternativo, Integer idTipoMezzo, Integer idZonaGeografica,
            ProvenienzaPrezzoArticolo provenienzaPrezzoArticolo, boolean noteSuDestinazione, String codiceValuta,
            String codiceLingua, Integer idAgente, ETipologiaCodiceIvaAlternativo tipologiaCodiceIvaAlternativo,
            BigDecimal percentualeScontoCommerciale, boolean gestioneArticoloDistinta, Integer idDeposito,
            boolean calcolaGiacenza) {
        LOGGER.debug("--> Enter creaRigaArticolo");
        start("creaRigaArticolo");

        RigaArticolo rigaArticolo = null;
        try {
            ParametriCreazioneRigaArticolo parametri = new ParametriCreazioneRigaArticolo();
            parametri.setProvenienzaPrezzo(provenienzaPrezzo);
            parametri.setIdArticolo(idArticolo);
            parametri.setData(data);
            parametri.setIdSedeEntita(idSedeEntita);
            parametri.setIdListinoAlternativo(idListinoAlternativo);
            parametri.setIdListino(idListino);
            parametri.setImporto(importo);
            parametri.setCodiceIvaAlternativo(codiceIvaAlternativo);
            parametri.setIdTipoMezzo(idTipoMezzo);
            parametri.setIdZonaGeografica(idZonaGeografica);
            parametri.setProvenienzaPrezzoArticolo(provenienzaPrezzoArticolo);
            parametri.setNoteSuDestinazione(noteSuDestinazione);
            parametri.setTipoMovimento(TipoMovimento.NESSUNO);
            parametri.setGestioneArticoloDistinta(gestioneArticoloDistinta);
            parametri.setCodiceValuta(codiceValuta);
            parametri.setCodiceLingua(codiceLingua);
            parametri.setIdAgente(idAgente);
            parametri.setTipologiaCodiceIvaAlternativo(tipologiaCodiceIvaAlternativo);
            parametri.setPercentualeScontoCommerciale(percentualeScontoCommerciale);
            parametri.setIdDeposito(idDeposito);
            parametri.setCalcolaGiacenza(calcolaGiacenza);
            rigaArticolo = ordiniDocumentoService.creaRigaArticolo(parametri);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("creaRigaArticolo");
        }
        LOGGER.debug("--> Exit creaRigaArticolo ");
        return rigaArticolo;
    }

    @Override
    public boolean creaRigaNoteAutomatica(AreaOrdine areaOrdine, String note) {
        LOGGER.debug("--> Enter creaRigaNoteAutomatica");
        start("creaRigaNoteAutomatica");
        boolean notaCreata = false;
        try {
            notaCreata = ordiniDocumentoService.creaRigaNoteAutomatica(areaOrdine, note);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("creaRigaNoteAutomatica");
        }
        LOGGER.debug("--> Exit creaRigaNoteAutomatica ");
        return notaCreata;
    }

    @Override
    public void dividiRiga(Integer rigaOriginale, List<RigaArticolo> righeDivise) {
        LOGGER.debug("--> Enter dividiRiga");
        start("dividiRiga");
        try {
            ordiniDocumentoService.dividiRiga(rigaOriginale, righeDivise);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("dividiRiga");
        }
        LOGGER.debug("--> Exit dividiRiga ");

    }

    @Override
    public void evadiOrdini(List<RigaDistintaCarico> righeEvasione, AreaMagazzino documentoEvasione)
            throws EvasioneLottiException {
        LOGGER.debug("--> Enter evadiOrdini");
        start("evadiOrdini");
        try {
            ordiniDocumentoService.evadiOrdini(righeEvasione, documentoEvasione);
        } catch (EvasioneLottiException evasioneLottiException) {
            throw evasioneLottiException;
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("evadiOrdini");
        }
        LOGGER.debug("--> Exit evadiOrdini ");
    }

    @Override
    public void evadiOrdini(List<RigaDistintaCarico> righeEvasione, Date dataEvasione)
            throws EntitaSenzaTipoDocumentoEvasioneException, ContabilizzazioneException,
            CodicePagamentoEvasioneAssenteException, CodicePagamentoAssenteException {
        LOGGER.debug("--> Enter evadiOrdini");
        start("evadiOrdini");
        try {
            ordiniDocumentoService.evadiOrdini(righeEvasione, dataEvasione);
        } catch (EntitaSenzaTipoDocumentoEvasioneException e) {
            throw e;
        } catch (CodicePagamentoEvasioneAssenteException e) {
            throw e;
        } catch (CodicePagamentoAssenteException e) {
            throw e;
        } catch (ContabilizzazioneException e1) {
            throw e1;
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("evadiOrdini");
        }
        LOGGER.debug("--> Exit evadiOrdini ");
    }

    @Override
    public void forzaRigheOrdine(List<Integer> righe) {
        LOGGER.debug("--> Enter forzaRigheOrdine");
        start("forzaRigheOrdine");
        try {
            ordiniDocumentoService.forzaRigheOrdine(righe);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("forzaRigheOrdine");
        }
        LOGGER.debug("--> Exit forzaRigheOrdine ");
    }

    @Override
    public void generaRigheOrdine(List<RigaOrdineInserimento> righeInserimento, AreaOrdine areaOrdine) {
        LOGGER.debug("--> Enter generaRigheOrdine");
        start("generaRigheOrdine");
        try {
            ordiniDocumentoService.generaRigheOrdine(righeInserimento, areaOrdine);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("generaRigheOrdine");
        }
        LOGGER.debug("--> Exit generaRigheOrdine ");

    }

    /**
     * @return the ordiniDocumentoService
     */
    public OrdiniDocumentoService getOrdiniDocumentoService() {
        return ordiniDocumentoService;
    }

    @Override
    public void inserisciRaggruppamentoArticoli(Integer idAreaOrdine, ProvenienzaPrezzo provenienzaPrezzo,
            Integer idRaggruppamentoArticoli, Date data, Integer idSedeEntita, Integer idListinoAlternativo,
            Integer idListino, Importo importo, CodiceIva codiceIvaAlternativo, Integer idTipoMezzo,
            Integer idZonaGeografica, boolean noteSuDestinazione, String codiceValuta, String codiceLingua,
            Date dataConsegna, Integer idAgente, ETipologiaCodiceIvaAlternativo tipologiaCodiceIvaAlternativo,
            BigDecimal percentualeScontoCommerciale) {
        LOGGER.debug("--> Enter inserisciRaggruppamentoArticoli");
        start("inserisciRaggruppamentoArticoli");
        try {
            ordiniDocumentoService.inserisciRaggruppamentoArticoli(idAreaOrdine, provenienzaPrezzo,
                    idRaggruppamentoArticoli, data, idSedeEntita, idListinoAlternativo, idListino, importo,
                    codiceIvaAlternativo, idTipoMezzo, idZonaGeografica, noteSuDestinazione, codiceValuta, codiceLingua,
                    dataConsegna, idAgente, tipologiaCodiceIvaAlternativo, percentualeScontoCommerciale);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("inserisciRaggruppamentoArticoli");
        }
        LOGGER.debug("--> Exit inserisciRaggruppamentoArticoli ");
    }

    @Override
    public AreaOrdineFullDTO ricalcolaPrezziOrdine(Integer idAreaOrdine) {
        LOGGER.debug("--> Enter ricalcolaPrezziOrdine");
        start("ricalcolaPrezziOrdine");
        AreaOrdineFullDTO areaOrdineFullDTO = null;
        try {
            areaOrdineFullDTO = ordiniDocumentoService.ricalcolaPrezziOrdine(idAreaOrdine);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("ricalcolaPrezziOrdine");
        }
        LOGGER.debug("--> Exit ricalcolaPrezziOrdine ");
        return areaOrdineFullDTO;
    }

    @Override
    public List<AreaOrdineRicerca> ricercaAreeOrdine(ParametriRicercaAreaOrdine parametriRicercaAreaOrdine) {
        LOGGER.debug("--> Enter ricercaAreeOrdine");
        start("ricercaAreeOrdine");
        List<AreaOrdineRicerca> list = new ArrayList<AreaOrdineRicerca>();
        try {
            list = ordiniDocumentoService.ricercaAreeOrdine(parametriRicercaAreaOrdine);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("ricercaAreeOrdine");
        }
        LOGGER.debug("--> Exit ricercaAreeOrdine ");
        return list;
    }

    @Override
    public AreaOrdineFullDTO salvaAreaOrdine(AreaOrdine areaOrdine, AreaRate areaRate) {
        LOGGER.debug("--> Enter salvaAreaOrdine");
        start("salvaAreaOrdine");
        AreaOrdineFullDTO areaOrdineFullDTO = null;
        try {
            areaOrdineFullDTO = ordiniDocumentoService.salvaAreaOrdine(areaOrdine, areaRate);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaAreaOrdine");
        }
        LOGGER.debug("--> Exit salvaAreaOrdine ");
        return areaOrdineFullDTO;
    }

    @Override
    public RigaOrdine salvaRigaOrdine(RigaOrdine rigaOrdine) {
        return salvaRigaOrdine(rigaOrdine, true);
    }

    @Override
    public RigaOrdine salvaRigaOrdine(RigaOrdine rigaOrdine, boolean check) {
        LOGGER.debug("--> Enter salvaRigaOrdine");
        start("salvaRigaOrdine");
        RigaOrdine rigaOrdineSalvata = null;
        try {
            rigaOrdineSalvata = ordiniDocumentoService.salvaRigaOrdine(rigaOrdine, check);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaRigaOrdine");
        }
        LOGGER.debug("--> Exit salvaRigaOrdine ");
        return rigaOrdineSalvata;
    }

    @Override
    public List<RigaOrdineImportata> salvaRigaOrdineImportata(RigaOrdineImportata rigaOrdine) {
        LOGGER.debug("--> Enter salvaRigaOrdineImportata");
        start("salvaRigaOrdineImportata");

        List<RigaOrdineImportata> righeSalvate = null;
        try {
            righeSalvate = ordiniDocumentoService.salvaRigaOrdineImportata(rigaOrdine);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaRigaOrdineImportata");
        }
        LOGGER.debug("--> Exit salvaRigaOrdineImportata ");
        return righeSalvate;
    }

    @Override
    public void salvaRigheMagazzinoNoCheck(
            Map<AreaMagazzino, List<it.eurotn.panjea.magazzino.domain.RigaArticolo>> righePerArea) {
        LOGGER.debug("--> Enter salvaRigheMagazzinoNoCheck");
        start("salvaRigheMagazzinoNoCheck");
        try {
            ordiniDocumentoService.salvaRigheMagazzinoNoCheck(righePerArea);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaRigheMagazzinoNoCheck");
        }
        LOGGER.debug("--> Exit salvaRigheMagazzinoNoCheck ");
    }

    @Override
    public TipoAreaOrdine salvaTipoAreaOrdine(TipoAreaOrdine tipoAreaOrdine) {
        LOGGER.debug("--> Enter salvaTipoAreaOrdine");
        start("salvaTipoAreaOrdine");
        TipoAreaOrdine result = null;
        try {
            result = ordiniDocumentoService.salvaTipoAreaOrdine(tipoAreaOrdine);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaTipoAreaOrdine");
        }
        LOGGER.debug("--> Exit salvaTipoAreaOrdine ");
        return result;
    }

    /**
     * @param ordiniDocumentoService
     *            the ordiniDocumentoService to set
     */
    public void setOrdiniDocumentoService(OrdiniDocumentoService ordiniDocumentoService) {
        this.ordiniDocumentoService = ordiniDocumentoService;
    }

    @Override
    public void spostaRighe(Set<Integer> righeDaSpostare, Integer idDest) {
        LOGGER.debug("--> Enter spostaRighe");
        start("spostaRighe");
        try {
            ordiniDocumentoService.spostaRighe(righeDaSpostare, idDest);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("spostaRighe");
        }
        LOGGER.debug("--> Exit spostaRighe ");

    }

    @Override
    public AreaOrdine totalizzaDocumento(AreaOrdine areaOrdine, AreaPartite areaPartite) {
        LOGGER.debug("--> Enter totalizzaDocumento");
        start("totalizzaDocumento");
        AreaOrdine areaOrdineTotalizzata = null;
        try {
            areaOrdineTotalizzata = ordiniDocumentoService.totalizzaDocumento(areaOrdine, areaPartite);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("totalizzaDocumento");
        }
        LOGGER.debug("--> Exit totalizzaDocumento ");
        return areaOrdineTotalizzata;
    }

    @Override
    public AreaOrdineFullDTO validaRigheOrdine(AreaOrdine areaOrdine, AreaRate areaRate, boolean cambioStato) {
        LOGGER.debug("--> Enter validaRigheOrdine");
        start("validaRigheOrdine");
        AreaOrdineFullDTO areaOrdineFullDTOValidato = null;
        try {
            areaOrdineFullDTOValidato = ordiniDocumentoService.validaRigheOrdine(areaOrdine, areaRate, cambioStato);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("validaRigheOrdine");
        }
        LOGGER.debug("--> Exit validaRigheOrdine ");
        return areaOrdineFullDTOValidato;
    }

    @Override
    public int verificaNumeroOrdiniDaEvadere() {
        LOGGER.debug("--> Enter verificaNumeroOrdiniDaEvadere");
        int numOrdini = 0;
        try {
            numOrdini = ordiniDocumentoService.verificaNumeroOrdiniDaEvadere();
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        }
        LOGGER.debug("--> Exit verificaNumeroOrdiniDaEvadere ");
        return numOrdini;
    }

}
