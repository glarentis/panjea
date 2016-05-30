package it.eurotn.panjea.tesoreria.rich.bd;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.domain.RapportoBancarioAzienda;
import it.eurotn.panjea.anagrafica.service.exception.TipoDocumentoBaseException;
import it.eurotn.panjea.contabilita.domain.AreaContabileLite;
import it.eurotn.panjea.pagamenti.service.exception.PagamentiException;
import it.eurotn.panjea.partite.domain.TipoAreaPartita.TipoPartita;
import it.eurotn.panjea.partite.domain.TipoDocumentoBasePartite;
import it.eurotn.panjea.partite.domain.TipoDocumentoBasePartite.TipoOperazione;
import it.eurotn.panjea.partite.util.ParametriRicercaRate;
import it.eurotn.panjea.rate.domain.Rata;
import it.eurotn.panjea.rich.bd.AbstractBaseBD;
import it.eurotn.panjea.tesoreria.domain.AreaAcconto;
import it.eurotn.panjea.tesoreria.domain.AreaAccredito;
import it.eurotn.panjea.tesoreria.domain.AreaAccreditoAssegno;
import it.eurotn.panjea.tesoreria.domain.AreaAnticipo;
import it.eurotn.panjea.tesoreria.domain.AreaAssegno;
import it.eurotn.panjea.tesoreria.domain.AreaBonifico;
import it.eurotn.panjea.tesoreria.domain.AreaChiusure;
import it.eurotn.panjea.tesoreria.domain.AreaDistintaBancaria;
import it.eurotn.panjea.tesoreria.domain.AreaEffetti;
import it.eurotn.panjea.tesoreria.domain.AreaInsoluti;
import it.eurotn.panjea.tesoreria.domain.AreaPagamenti;
import it.eurotn.panjea.tesoreria.domain.AreaTesoreria;
import it.eurotn.panjea.tesoreria.domain.Effetto;
import it.eurotn.panjea.tesoreria.domain.Pagamento;
import it.eurotn.panjea.tesoreria.domain.TesoreriaSettings;
import it.eurotn.panjea.tesoreria.service.exception.DataRichiestaDopoIncassoException;
import it.eurotn.panjea.tesoreria.service.exception.RapportoBancarioPerFlussoAssenteException;
import it.eurotn.panjea.tesoreria.service.interfaces.TesoreriaService;
import it.eurotn.panjea.tesoreria.solleciti.Sollecito;
import it.eurotn.panjea.tesoreria.solleciti.TemplateSolleciti;
import it.eurotn.panjea.tesoreria.util.AssegnoDTO;
import it.eurotn.panjea.tesoreria.util.EffettoDTO;
import it.eurotn.panjea.tesoreria.util.ParametriCreazioneAreaChiusure;
import it.eurotn.panjea.tesoreria.util.ParametriRicercaAreeTesoreria;
import it.eurotn.panjea.tesoreria.util.ParametriRicercaAssegni;
import it.eurotn.panjea.tesoreria.util.ParametriRicercaEffetti;
import it.eurotn.panjea.tesoreria.util.SituazioneEffetto;
import it.eurotn.panjea.tesoreria.util.SituazioneRata;
import it.eurotn.panjea.tesoreria.util.SituazioneRigaAnticipo;
import it.eurotn.panjea.tesoreria.util.parametriricerca.ParametriRicercaAcconti;
import it.eurotn.panjea.utils.PanjeaSwingUtil;

public class TesoreriaBD extends AbstractBaseBD implements ITesoreriaBD {

    private static Logger logger = Logger.getLogger(TesoreriaBD.class);

    public static final String BEAN_ID = "tesoreriaBD";

    private TesoreriaService tesoreriaService;

    @Override
    public void assegnaRapportoBancarioAssegni(RapportoBancarioAzienda rapportoBancarioAzienda,
            List<AreaAssegno> areeAssegno) {
        logger.debug("--> Enter assegnaRapportoBancarioAssegni");
        start("assegnaRapportoBancarioAssegni");
        try {
            tesoreriaService.assegnaRapportoBancarioAssegni(rapportoBancarioAzienda, areeAssegno);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("assegnaRapportoBancarioAssegni");
        }
        logger.debug("--> Exit assegnaRapportoBancarioAssegni ");
    }

    @Override
    public void cancellaAcconto(AreaAcconto areaAcconto) {
        logger.debug("--> Enter cancellaAcconto");
        start("cancellaAcconto");
        try {
            tesoreriaService.cancellaAcconto(areaAcconto);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaAcconto");
        }
        logger.debug("--> Exit cancellaAcconto ");
    }

    @Override
    public void cancellaAreaTesoreria(AreaTesoreria areaTesoreria) {
        tesoreriaService.cancellaAreaTesoreria(areaTesoreria);
    }

    @Override
    public void cancellaAreeTesorerie(List<AreaTesoreria> listAree) {
        logger.debug("--> Enter cancellaAreeTesorerie");
        start("cancellaAreeTesorerie");
        try {
            tesoreriaService.cancellaAreeTesorerie(listAree);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaAreeTesorerie");
        }
        logger.debug("--> Exit cancellaAreeTesorerie ");
    }

    @Override
    public void cancellaEffetto(Effetto effetto) {
        logger.debug("--> Enter cancellaEffetto");
        start("cancellaEffetto");
        try {
            tesoreriaService.cancellaEffetto(effetto);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaEffetto");
        }
        logger.debug("--> Exit cancellaEffetto ");
    }

    @Override
    public void cancellaPagamento(Pagamento pagamento) {

    }

    @Override
    public void cancellaPagamentoAccontoLiquidazione(Pagamento pagamento) {
        logger.debug("--> Enter cancellaPagamentoAccontoLiquidazione");
        start("cancellaPagamentoAccontoLiquidazione");
        try {
            tesoreriaService.cancellaPagamentoAccontoLiquidazione(pagamento);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaPagamentoAccontoLiquidazione");
        }
        logger.debug("--> Exit cancellaPagamentoAccontoLiquidazione");

    }

    @Override
    public void cancellaSollecito(Sollecito sollecito) {
        logger.debug("--> Enter cancellaTemplateSolleciti");
        start("cancellaSollecito");
        try {
            tesoreriaService.cancellaSollecito(sollecito);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaSollecito");
        }
        logger.debug("--> Exit cancellaTemplateSolleciti ");
    }

    @Override
    public void cancellaTemplateSolleciti(TemplateSolleciti templateSollecito) {
        logger.debug("--> Enter cancellaTemplateSolleciti");
        start("cancellaTemplateSolleciti");
        try {
            tesoreriaService.cancellaTemplateSolleciti(templateSollecito);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaTemplateSolleciti");
        }
        logger.debug("--> Exit cancellaTemplateSolleciti ");
    }

    @Override
    public List<AreaAcconto> caricaAcconti(ParametriRicercaAcconti parametriRicercaAcconti) {
        logger.debug("--> Enter caricaAcconti");
        start("caricaAcconti");
        List<AreaAcconto> listResult = new ArrayList<AreaAcconto>();
        try {
            listResult = tesoreriaService.caricaAreaAcconti(parametriRicercaAcconti);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaAcconti");
        }
        logger.debug("--> Exit caricaAcconti ");
        return listResult;
    }

    @Override
    public AreaContabileLite caricaAreaContabileLite(AreaTesoreria areaTesoreria) {
        logger.debug("--> Enter caricaAreaContabileLite");
        start("caricaAreaContabileLite");

        AreaContabileLite areaContabileLite = null;
        try {
            areaContabileLite = tesoreriaService.caricaAreaContabileLite(areaTesoreria);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaAreaContabileLite");
        }
        logger.debug("--> Exit caricaAreaContabileLite ");
        return areaContabileLite;
    }

    @Override
    public AreaTesoreria caricaAreaTesoreria(AreaTesoreria areaTesoreria) {
        logger.debug("--> Enter caricaAreaTesoreria");
        start("caricaAreaTesoreria");
        AreaTesoreria areaTesoreriaCaricata = null;
        try {
            areaTesoreriaCaricata = tesoreriaService.caricaAreaTesoreria(areaTesoreria);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaAreaTesoreria");
        }
        logger.debug("--> Exit caricaAreaTesoreria ");
        return areaTesoreriaCaricata;
    }

    @Override
    public AreaTesoreria caricaAreaTesoreria(Documento documento) {
        logger.debug("--> Enter caricaAreaTesoreria");
        start("caricaAreaTesoreria");
        AreaTesoreria areaTesoreria = null;
        try {
            areaTesoreria = tesoreriaService.caricaAreaTesoreria(documento);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaAreaTesoreria");
        }
        logger.debug("--> Exit caricaAreaTesoreria ");
        return areaTesoreria;
    }

    @Override
    public AreaTesoreria caricaAreaTesoreria(Pagamento pagamento) {
        logger.debug("--> Enter caricaAreaTesoreria");
        start("caricaAreaTesoreria");

        AreaTesoreria areaTesoreriaCaricata = null;
        try {
            areaTesoreriaCaricata = tesoreriaService.caricaAreaTesoreria(pagamento);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaAreaTesoreria");
        }
        logger.debug("--> Exit caricaAreaTesoreria ");
        return areaTesoreriaCaricata;
    }

    @Override
    public AreaTesoreria caricaAreaTesoreriaByStatoEffetto(Effetto effetto) {
        logger.debug("--> Enter caricaAreaTesoreriaByStatoEffetto");
        start("caricaAreaTesoreriaByStatoEffetto");

        AreaTesoreria areaTesoreria = null;
        try {
            areaTesoreria = tesoreriaService.caricaAreaTesoreriaByStatoEffetto(effetto);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaAreaTesoreriaByStatoEffetto");
        }
        logger.debug("--> Exit caricaAreaTesoreriaByStatoEffetto ");
        return areaTesoreria;
    }

    @Override
    public List<AreaTesoreria> caricaAreeCollegate(AreaTesoreria areaTesoreria) {
        logger.debug("--> Enter caricaAreeCollegate");
        start("caricaAreeCollegate");

        List<AreaTesoreria> list = new ArrayList<AreaTesoreria>();
        try {
            list = tesoreriaService.caricaAreeCollegate(areaTesoreria);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaAreeCollegate");
        }
        logger.debug("--> Exit caricaAreeCollegate ");
        return list;
    }

    @Override
    public List<Object> caricaAreeDocumento(Integer idDocumento) {
        logger.debug("--> Enter caricaAreeDocumento");
        start("caricaAreeDocumento");

        List<Object> listResult = new ArrayList<Object>();
        try {
            listResult = tesoreriaService.caricaAreeDocumento(idDocumento);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaAreeDocumento");
        }
        logger.debug("--> Exit caricaAreeDocumento ");
        return listResult;
    }

    @Override
    public List<AssegnoDTO> caricaAssegni(ParametriRicercaAssegni parametriRicercaAssegni) {
        logger.debug("--> Enter caricaAssegni");
        start("caricaAssegni");
        List<AssegnoDTO> listResult = new ArrayList<AssegnoDTO>();
        try {
            listResult = tesoreriaService.caricaAssegni(parametriRicercaAssegni);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaAssegni");
        }
        logger.debug("--> Exit caricaAssegni ");
        return listResult;
    }

    @Override
    public List<EffettoDTO> caricaEffettiDistintePerStampa(Map<Object, Object> parametri)
            throws RapportoBancarioPerFlussoAssenteException {
        logger.debug("--> Enter caricaEffettiDistintePerStampa");
        start("caricaEffettiDistintePerStampa");

        List<EffettoDTO> result = null;
        try {
            result = tesoreriaService.caricaEffettiDistintePerStampa(parametri);
        } catch (RapportoBancarioPerFlussoAssenteException e) {
            throw e;
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaEffettiDistintePerStampa");
        }
        logger.debug("--> Exit caricaEffettiDistintePerStampa ");
        return result;
    }

    @Override
    public AreaAssegno caricaImmagineAssegno(AreaAssegno areaAssegno) {
        logger.debug("--> Enter caricaAreaTesoreria");
        start("caricaAreaTesoreria");

        AreaAssegno assegnoConImmagine = null;
        try {
            assegnoConImmagine = tesoreriaService.caricaImmagineAssegno(areaAssegno);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaAreaTesoreria");
        }
        logger.debug("--> Exit caricaAreaTesoreria ");
        return assegnoConImmagine;
    }

    @Override
    public BigDecimal caricaImportoAnticipato(AreaEffetti areaEffetti, RapportoBancarioAzienda rapportoBancarioAzienda,
            Date dataValuta) {
        logger.debug("--> Enter caricaImportoAnticipato");
        start("caricaImportoAnticipato");

        BigDecimal importo = BigDecimal.ZERO;
        try {
            importo = tesoreriaService.caricaImportoAnticipato(areaEffetti, rapportoBancarioAzienda, dataValuta);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaImportoAnticipato");
        }
        logger.debug("--> Exit caricaImportoAnticipato ");
        return importo;
    }

    @Override
    public List<Pagamento> caricaPagamenti(Integer idRata) {
        logger.debug("--> Enter caricaPagamenti");
        start("caricaPagamenti");
        List<Pagamento> pagamenti = new ArrayList<Pagamento>();
        try {
            pagamenti = tesoreriaService.caricaPagamenti(idRata);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaPagamenti");
        }
        logger.debug("--> Exit caricaPagamenti ");
        return pagamenti;
    }

    @Override
    public List<Pagamento> caricaPagamentiByAreaAcconto(AreaAcconto areaAcconto) {
        logger.debug("--> Enter caricaPagamentiByAreaAcconto");
        start("caricaPagamentiByAreaAcconto");
        List<Pagamento> pagamenti = new ArrayList<Pagamento>();
        try {
            pagamenti = tesoreriaService.caricaPagamentiByAreaAcconto(areaAcconto);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaPagamentiByAreaAcconto");
        }
        logger.debug("--> Exit caricaPagamentiByAreaAcconto ");
        return pagamenti;
    }

    @Override
    public Rata caricaRata(Integer idRata) {
        start("caricaRata");
        Rata rata = null;
        try {
            rata = tesoreriaService.caricaRata(idRata);

        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("carica rata");
        }
        return rata;

    }

    @Override
    public TesoreriaSettings caricaSettings() {
        logger.debug("--> Enter caricaSettings");
        start("caricaSettings");
        TesoreriaSettings result = null;
        try {
            result = tesoreriaService.caricaSettings();
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaSettings");
        }
        logger.debug("--> Exit caricaSettings ");
        return result;
    }

    @Override
    public List<SituazioneRata> caricaSituazioneRate(Map<Object, Object> parametri) {
        logger.debug("--> Enter caricaSituazioneRate");
        start("caricaSituazioneRate");

        List<SituazioneRata> situazioneRate = new ArrayList<SituazioneRata>();
        try {
            situazioneRate = tesoreriaService.caricaSituazioneRate(parametri);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaSituazioneRate");
        }
        logger.debug("--> Exit caricaSituazioneRate ");
        return situazioneRate;
    }

    @Override
    public List<SituazioneRata> caricaSituazioneRateDaUtilizzarePerAcconto(Integer idEntita, TipoPartita tipoPartita) {
        logger.debug("--> Enter caricaSituazioneRate");
        start("caricaSituazioneRate");
        List<SituazioneRata> rate = new ArrayList<SituazioneRata>();
        try {
            rate = tesoreriaService.caricaSituazioneRateDaUtilizzarePerAcconto(idEntita, tipoPartita);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaSituazioneRate");
        }
        logger.debug("--> Exit caricaSituazioneRate ");
        return rate;
    }

    @Override
    public List<Sollecito> caricaSolleciti() {
        logger.debug("--> Enter caricaSolleciti");
        start("caricaSolleciti");
        List<Sollecito> solleciti = new ArrayList<Sollecito>();
        try {
            solleciti = tesoreriaService.caricaSolleciti();
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaSolleciti");
        }
        logger.debug("--> Exit caricaSolleciti ");
        return solleciti;
    }

    @Override
    public List<Sollecito> caricaSollecitiByCliente(Integer idCliente) {
        List<Sollecito> solleciti = new ArrayList<Sollecito>();
        logger.debug("--> Enter caricaSollecitiByCliente");
        start("caricaSollecitiByCliente");
        try {
            solleciti = tesoreriaService.caricaSollecitiByCliente(idCliente);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaSollecitiByCliente");
        }
        logger.debug("--> Exit caricaSollecitiByCliente");

        return solleciti;
    }

    @Override
    public List<Sollecito> caricaSollecitiByRata(Integer idRata) {
        List<Sollecito> solleciti = new ArrayList<Sollecito>();
        logger.debug("--> Enter caricaSollecitiByRata");
        start("caricaSollecitiByRata");
        try {
            solleciti = tesoreriaService.caricaSollecitiByRata(idRata);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaSollecitiByRata");
        }
        logger.debug("--> Exit caricaSollecitiByRata");
        return solleciti;
    }

    @Override
    public List<TemplateSolleciti> caricaTemplateSolleciti() {
        logger.debug("--> Enter caricaTemplateSolleciti");
        start("caricaTemplateSolleciti");
        List<TemplateSolleciti> template = new ArrayList<TemplateSolleciti>();
        try {
            template = tesoreriaService.caricaTemplateSolleciti();
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaTemplateSolleciti");
        }
        logger.debug("--> Exit caricaTemplateSolleciti ");
        return template;
    }

    @Override
    public TemplateSolleciti caricaTemplateSollecito(int idTemplateSollecito) throws PagamentiException {
        logger.debug("--> Enter caricaTemplateSollecito");
        start("caricaTemplateSollecito");
        TemplateSolleciti templateSollecito = new TemplateSolleciti();
        try {
            templateSollecito = tesoreriaService.caricaTemplateSollecito(idTemplateSollecito);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaTemplateSollecito");
        }
        logger.debug("--> Exit caricaTemplateSollecito ");
        return templateSollecito;
    }

    @Override
    public TipoDocumentoBasePartite caricaTipoDocumentoBase(TipoOperazione tipoOperazione)
            throws TipoDocumentoBaseException {
        logger.debug("--> Enter caricaTipoDocumentoBase");
        start("caricaTipoDocumentoBase");
        TipoDocumentoBasePartite tipoDocumentoBasePartite = null;
        try {
            tipoDocumentoBasePartite = tesoreriaService.caricaTipoDocumentoBase(tipoOperazione);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaTipoDocumentoBase");
        }
        logger.debug("--> Exit caricaTipoDocumentoBase ");
        return tipoDocumentoBasePartite;
    }

    @Override
    public AreaAccreditoAssegno creaAreaAccreditoAssegno(ParametriCreazioneAreaChiusure parametriCreazioneAreaChiusure,
            List<AreaAssegno> assegni) {
        logger.debug("--> Enter creaAreaAccreditoAssegno");
        start("creaAreaAccreditoAssegno");

        AreaAccreditoAssegno areaAccreditoAssegno = new AreaAccreditoAssegno();
        try {
            areaAccreditoAssegno = tesoreriaService.creaAreaAccreditoAssegno(parametriCreazioneAreaChiusure, assegni);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("creaAreaAccreditoAssegno");
        }
        logger.debug("--> Exit creaAreaAccreditoAssegno ");
        return areaAccreditoAssegno;
    }

    @Override
    public AreaAnticipo creaAreaAnticipo(List<SituazioneRigaAnticipo> situazioneRigaAnticipo) {
        logger.debug("--> Enter creaAreaAnticipo");
        start("creaAreaAnticipo");

        AreaAnticipo areaAnticipoSave = null;
        try {
            areaAnticipoSave = tesoreriaService.creaAreaAnticipo(situazioneRigaAnticipo);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("creaAreaAnticipo");
        }
        logger.debug("--> Exit creaAreaAnticipo ");
        return areaAnticipoSave;
    }

    @Override
    public AreaBonifico creaAreaBonifico(Date dataDocumento, String numeroDocumento, AreaPagamenti areaPagamenti,
            BigDecimal spese, Set<Pagamento> pagamenti) {
        start("creaAreaBonifico");
        AreaBonifico result = null;
        try {
            result = tesoreriaService.creaAreaBonifico(dataDocumento, numeroDocumento, areaPagamenti, spese, pagamenti);
        } catch (TipoDocumentoBaseException e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("creaAreaBonifico");
        }
        return result;
    }

    @Override
    public List<AreaChiusure> creaAreaChiusurePerPagamenti(
            ParametriCreazioneAreaChiusure parametriCreazioneAreaChiusure, List<Pagamento> pagamenti) {
        logger.debug("--> Enter creaAreaChiusurePerPagamenti");
        start("creaAreaChiusurePerPagamenti");
        List<AreaChiusure> listResult = new ArrayList<AreaChiusure>();
        try {
            listResult = tesoreriaService.creaAreaChiusure(parametriCreazioneAreaChiusure, pagamenti);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("creaAreaChiusurePerPagamenti");
        }
        logger.debug("--> Exit creaAreaChiusurePerPagamenti ");
        return listResult;
    }

    @Override
    public AreaInsoluti creaAreaInsoluti(Date dataDocumento, String nDocumento, BigDecimal speseInsoluto,
            Set<Effetto> effetti) {
        logger.debug("--> Enter creaAreaInsoluti");
        start("creaAreaInsoluti");

        AreaInsoluti areaInsoluti = null;
        try {
            areaInsoluti = tesoreriaService.creaAreaInsoluti(dataDocumento, nDocumento, speseInsoluto, effetti);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("creaAreaInsoluti");
        }
        logger.debug("--> Exit creaAreaInsoluti ");
        return areaInsoluti;
    }

    @Override
    public List<AreaAccredito> creaAreeAccredito(List<Effetto> effetti, Date dataScritturaPosticipata)
            throws DataRichiestaDopoIncassoException {
        logger.debug("--> Enter creaAreeAcrredito");
        start("creaAreeAcrredito");

        List<AreaAccredito> areeAccreditoCreate = new ArrayList<AreaAccredito>();
        try {
            areeAccreditoCreate = tesoreriaService.creaAreeAccredito(effetti, dataScritturaPosticipata);
        } catch (DataRichiestaDopoIncassoException e) {
            throw e;
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("creaAreeAcrredito");
        }
        logger.debug("--> Exit creaAreeAcrredito ");
        return areeAccreditoCreate;
    }

    @Override
    public AreaDistintaBancaria creaDistintaBancaria(Date dataDocumento, String numeroDocumento,
            AreaEffetti areaEffetto, BigDecimal spese, BigDecimal speseDistinta, Set<Effetto> effetti) {
        start("creaDistintaBancaria");
        AreaDistintaBancaria result = null;
        try {
            result = tesoreriaService.creaAreaDistintaBancaria(dataDocumento, numeroDocumento, areaEffetto, spese,
                    speseDistinta, effetti);
        } catch (TipoDocumentoBaseException e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("creaDistintaBancaria");
        }
        return result;
    }

    @Override
    public List<AreaChiusure> creaPagamentiConAcconto(List<Pagamento> pagamenti, List<AreaAcconto> acconti) {
        logger.debug("--> Enter creaPagamentiConAcconto");
        List<AreaChiusure> areeChiusura = new ArrayList<AreaChiusure>();
        start("creaPagamentiConAcconto");
        try {
            areeChiusura = tesoreriaService.creaPagamentiConAcconto(pagamenti, acconti);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("creaPagamentiConAcconto");
        }
        logger.debug("--> Exit creaPagamentiConAcconto ");
        return areeChiusura;
    }

    @Override
    public String creaTesto(String testo, Sollecito sollecito) {
        String result = "";
        logger.debug("--> Enter creaTesto");
        start("creaTesto");
        try {
            result = tesoreriaService.creaTesto(testo, sollecito);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("creaTesto");
        }
        logger.debug("--> Exit creaTesto ");
        return result;
    }

    @Override
    public String generaFlusso(Integer idDocumento) {
        start("generaFlusso");
        String pathFile = "";
        try {
            pathFile = tesoreriaService.generaFlusso(idDocumento);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("generaFlusso");
        }
        logger.debug("--> Exit generaFlusso ");
        return pathFile;
    }

    /**
     * @return the tesoreriaService
     */
    public TesoreriaService getTesoreriaService() {
        return tesoreriaService;
    }

    @Override
    public List<AreaTesoreria> ricercaAreeTesorerie(ParametriRicercaAreeTesoreria parametriRicercaAreeTesoreria) {
        logger.debug("--> Enter ricercaAreeTesorerie");
        start("ricercaAreeTesorerie");

        List<AreaTesoreria> listResult = new ArrayList<AreaTesoreria>();
        try {
            listResult = tesoreriaService.ricercaAreeTesorerie(parametriRicercaAreeTesoreria);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("ricercaAreeTesorerie");
        }
        logger.debug("--> Exit ricercaAreeTesorerie ");
        return listResult;
    }

    @Override
    public List<SituazioneEffetto> ricercaEffetti(ParametriRicercaEffetti parametriRicercaEffetti) {
        logger.debug("--> Enter ricercaEffetti");
        start("ricercaEffetti");

        List<SituazioneEffetto> effetti = new ArrayList<SituazioneEffetto>();
        try {
            effetti = tesoreriaService.ricercaEffetti(parametriRicercaEffetti);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("ricercaEffetti");
        }
        logger.debug("--> Exit ricercaEffetti ");
        return effetti;
    }

    @Override
    public List<SituazioneRata> ricercaRate(ParametriRicercaRate parametriRicercaRate) {
        logger.debug("--> Enter ricercaRate");
        start("ricercaRate");

        List<SituazioneRata> listRate = new ArrayList<SituazioneRata>();
        try {
            listRate = tesoreriaService.ricercaRate(parametriRicercaRate);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("ricercaRate");
        }
        logger.debug("--> Exit ricercaRate ");
        return listRate;
    }

    @Override
    public TesoreriaSettings salva(TesoreriaSettings tesoreriaSettings) {
        logger.debug("--> Enter salva");
        start("salva");
        TesoreriaSettings result = null;
        try {
            result = tesoreriaService.salvaSettings(tesoreriaSettings);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salva");
        }
        logger.debug("--> Exit salva ");
        return result;
    }

    @Override
    public AreaAcconto salvaAreaAcconto(AreaAcconto areaAcconto) {
        logger.debug("--> Enter salvaAreaAcconto");
        start("salvaAreaAcconto");
        AreaAcconto areaAccontoSalvata = null;
        try {
            areaAccontoSalvata = tesoreriaService.salvaAreaAcconto(areaAcconto);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaAreaAcconto");
        }
        logger.debug("--> Exit salvaAreaAcconto ");
        return areaAccontoSalvata;
    }

    @Override
    public AreaTesoreria salvaAreaTesoreria(AreaTesoreria areaTesoreria) {
        start("salvaAreaTesoreria");
        AreaTesoreria result = null;
        try {
            result = tesoreriaService.salvaAreaTesoreria(areaTesoreria);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaAreaTesoreria");
        }
        return result;
    }

    @Override
    public List<Sollecito> salvaSolleciti(List<Sollecito> solleciti) {
        logger.debug("--> Enter salvaSolleciti");
        start("salvaSolleciti");
        List<Sollecito> sollecitiSalvati = null;
        try {
            sollecitiSalvati = tesoreriaService.salvaSolleciti(solleciti);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaSolleciti");
        }
        logger.debug("--> Exit salvaSolleciti ");
        return sollecitiSalvati;
    }

    @Override
    public Sollecito salvaSollecito(Sollecito sollecito) {
        start("salvaTemplateSolleciti");
        Sollecito solecitoSalvato = null;
        try {
            solecitoSalvato = tesoreriaService.salvaSollecito(sollecito);

        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaTemplateSolleciti");
        }
        return solecitoSalvato;
    }

    @Override
    public TemplateSolleciti salvaTemplateSolleciti(TemplateSolleciti templateSolleciti) {
        start("salvaTemplateSolleciti");
        TemplateSolleciti template = null;
        try {
            template = tesoreriaService.salvaTemplateSolleciti(templateSolleciti);

        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaTemplateSolleciti");
        }
        return template;
    }

    /**
     * @param tesoreriaService
     *            the tesoreriaService to set
     */
    public void setTesoreriaService(TesoreriaService tesoreriaService) {
        this.tesoreriaService = tesoreriaService;
    }
}
