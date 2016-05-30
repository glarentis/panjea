package it.eurotn.panjea.contabilita.rich.bd;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.domain.Entita;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.contabilita.domain.AreaContabile;
import it.eurotn.panjea.contabilita.domain.CodiceIvaCorrispettivo;
import it.eurotn.panjea.contabilita.domain.CodiceIvaPrevalente;
import it.eurotn.panjea.contabilita.domain.ContabilitaSettings;
import it.eurotn.panjea.contabilita.domain.Conto;
import it.eurotn.panjea.contabilita.domain.Conto.SottotipoConto;
import it.eurotn.panjea.contabilita.domain.ContoBase;
import it.eurotn.panjea.contabilita.domain.ControPartita;
import it.eurotn.panjea.contabilita.domain.Mastro;
import it.eurotn.panjea.contabilita.domain.RegistroIva;
import it.eurotn.panjea.contabilita.domain.SottoConto;
import it.eurotn.panjea.contabilita.domain.StrutturaContabile;
import it.eurotn.panjea.contabilita.domain.TipoAreaContabile;
import it.eurotn.panjea.contabilita.domain.TipoDocumentoBase;
import it.eurotn.panjea.contabilita.domain.TipoDocumentoBase.TipoOperazioneTipoDocumento;
import it.eurotn.panjea.contabilita.service.interfaces.ContabilitaAnagraficaService;
import it.eurotn.panjea.contabilita.util.ParametriRicercaSottoConti;
import it.eurotn.panjea.rich.bd.AbstractBaseBD;
import it.eurotn.panjea.utils.PanjeaSwingUtil;

public class ContabilitaAnagraficaBD extends AbstractBaseBD implements IContabilitaAnagraficaBD {
    public static final String BEAN_ID = "contabilitaAnagraficaBD";

    private static final Logger LOGGER = Logger.getLogger(ContabilitaAnagraficaBD.class);

    private ContabilitaAnagraficaService contabilitaAnagraficaService;

    /**
     * Costruttore.
     */
    public ContabilitaAnagraficaBD() {
        super();
    }

    @Override
    public void cancellaCodiceIvaCorrispettivo(CodiceIvaCorrispettivo codiceIvaCorrispettivo) {
        LOGGER.debug("--> Enter cancellaCodiceIvaCorrispettivo");
        start("cancellaCodiceIvaCorrispettivo");

        try {
            contabilitaAnagraficaService.cancellaCodiceIvaCorrispettivo(codiceIvaCorrispettivo);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaCodiceIvaCorrispettivo");
        }

        LOGGER.debug("--> Exit cancellaCodiceIvaCorrispettivo");
    }

    @Override
    public void cancellaCodiceIvaPrevalente(CodiceIvaPrevalente codiceIvaPrevalente) {
        LOGGER.debug("--> Enter cancellaCodiceIvaPrevalente");
        start("cancellaCodiceIvaPrevalente");
        try {
            contabilitaAnagraficaService.cancellaCodiceIvaPrevalente(codiceIvaPrevalente);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaCodiceIvaPrevalente");
        }
        LOGGER.debug("--> Exit cancellaCodiceIvaPrevalente ");
    }

    @Override
    public void cancellaConto(Conto conto) {
        LOGGER.debug("--> Enter cancellaConto");
        start("cancellaConto");
        try {
            contabilitaAnagraficaService.cancellaConto(conto);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaConto");
        }
        LOGGER.debug("--> Exit cancellaConto");
    }

    @Override
    public void cancellaContoBase(ContoBase contoBase) {
        LOGGER.debug("--> Enter cancellaContoBase");
        start("cancellaContoBase");
        try {
            contabilitaAnagraficaService.cancellaContoBase(contoBase);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaContoBase");
        }
        LOGGER.debug("--> Exit cancellaContoBase");
    }

    @Override
    public void cancellaControPartita(ControPartita controPartita) {
        LOGGER.debug("--> Enter cancellaControPartita");
        start("cancellaControPartita");
        try {
            contabilitaAnagraficaService.cancellaControPartita(controPartita);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaControPartita");
        }
        LOGGER.debug("--> Exit cancellaControPartita");
    }

    @Override
    public void cancellaMastro(Mastro mastro) {
        LOGGER.debug("--> Enter cancellaMastro");
        start("cancellaMastro");
        try {
            contabilitaAnagraficaService.cancellaMastro(mastro);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaMastro");
        }
        LOGGER.debug("--> Exit cancellaMastro");
    }

    /**
     * @see it.eurotn.panjea.contabilita.rich.bd.IContabilitaAnagraficaBD#cancellaRegistroIva(it.eurotn.panjea.contabilita.domain.RegistroIva)
     */
    @Override
    public void cancellaRegistroIva(RegistroIva registroIva) {
        LOGGER.debug("--> Enter cancellaRegistroIva");
        start("cancellaRegistroIva");
        try {
            contabilitaAnagraficaService.cancellaRegistroIva(registroIva);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaRegistroIva");
        }
        LOGGER.debug("--> Exit cancellaRegistroIva");
    }

    @Override
    public void cancellaSottoConto(SottoConto sottoConto) {
        LOGGER.debug("--> Enter cancellaSottoConto");
        start("cancellaSottoConto");
        try {
            contabilitaAnagraficaService.cancellaSottoConto(sottoConto);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaSottoConto");
        }
        LOGGER.debug("--> Exit cancellaSottoConto");
    }

    @Override
    public void cancellaStrutturaContabile(StrutturaContabile strutturaContabile) {
        LOGGER.debug("--> Enter cancellaStrutturaContabile");
        start("cancellaStrutturaContabile");
        try {
            contabilitaAnagraficaService.cancellaStrutturaContabile(strutturaContabile);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaStrutturaContabile");
        }
        LOGGER.debug("--> Exit cancellaStrutturaContabile");
    }

    /**
     * @see it.eurotn.panjea.contabilita.rich.bd.IContabilitaAnagraficaBD#cancellaTipoAreaContabile(it.eurotn.panjea.contabilita.domain.TipoAreaContabile)
     */
    @Override
    public void cancellaTipoAreaContabile(TipoAreaContabile tipoAreaContabile) {
        LOGGER.debug("--> Enter cancellaTipoAreaContabile");
        start("cancellaTipoAreaContabile");
        try {
            contabilitaAnagraficaService.cancellaTipoAreaContabile(tipoAreaContabile);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaTipoAreaContabile");
        }
        LOGGER.debug("--> Exit cancellaTipoAreaContabile ");

    }

    /**
     *
     * @see it.eurotn.panjea.contabilita.rich.bd.IContabilitaAnagraficaBD#cancellaTipoDocumentoBase(TipoDocumentoBase)
     */
    @Override
    public void cancellaTipoDocumentoBase(TipoDocumentoBase tipoDocumentoBase) {
        LOGGER.debug("--> Enter cancellaTipoDocumentoBase");
        start("cancellaTipoDocumentoBase");
        try {
            contabilitaAnagraficaService.cancellaTipoDocumentoBase(tipoDocumentoBase);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaTipoDocumentoBase");
        }
        LOGGER.debug("--> Exit cancellaTipoDocumentoBase ");
    }

    @Override
    public CodiceIvaPrevalente caricaCodiceIvaPrevalente(TipoAreaContabile tipoAreaContabile, EntitaLite entita) {
        LOGGER.debug("--> Enter caricaCodiceIvaPrevalente");
        start("caricaCodiceIvaPrevalente");
        CodiceIvaPrevalente codiceIvaPrevalenteCaricato = null;
        try {
            codiceIvaPrevalenteCaricato = contabilitaAnagraficaService.caricaCodiceIvaPrevalente(tipoAreaContabile,
                    entita);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaCodiceIvaPrevalente");
        }
        LOGGER.debug("--> Exit caricaCodiceIvaPrevalente ");
        return codiceIvaPrevalenteCaricato;
    }

    @Override
    public List<CodiceIvaCorrispettivo> caricaCodiciIvaCorrispettivo(TipoDocumento tipoDocumento) {
        LOGGER.debug("--> Enter caricaCodiciIvaCorrispettivo");
        start("caricaCodiciIvaCorrispettivo");

        List<CodiceIvaCorrispettivo> listCodiceIvaCorrispettivo = null;

        try {
            listCodiceIvaCorrispettivo = contabilitaAnagraficaService.caricaCodiciIvaCorrispettivo(tipoDocumento);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaCodiciIvaCorrispettivo");
        }

        LOGGER.debug("--> Exit caricaCodiciIvaCorrispettivo");
        return listCodiceIvaCorrispettivo;
    }

    @Override
    public ContabilitaSettings caricaContabilitaSettings() {
        LOGGER.debug("--> Enter caricaContabilitaSettings");
        start("caricaContabilitaSettings");
        ContabilitaSettings contabilitaSettings = contabilitaAnagraficaService.caricaContabilitaSettings();
        end("caricaContabilitaSettings");
        LOGGER.debug("--> Exit caricaContabilitaSettings");
        return contabilitaSettings;
    }

    @Override
    public List<ContoBase> caricaContiBase() {
        LOGGER.debug("--> Enter caricaContiBase");
        start("caricaContiBase");
        List<ContoBase> contiCaricati = new ArrayList<ContoBase>();
        try {
            contiCaricati = contabilitaAnagraficaService.caricaContiBase();
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaContiBase");
        }
        LOGGER.debug("--> Exit caricaContiBase");
        return contiCaricati;
    }

    @Override
    public Conto caricaConto(Integer idConto) {
        LOGGER.debug("--> Enter caricaConto");
        start("caricaConto");
        Conto conto = null;
        try {
            conto = contabilitaAnagraficaService.caricaConto(idConto);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaConto");
        }
        LOGGER.debug("--> Exit caricaConto");
        return conto;
    }

    @Override
    public ControPartita caricaControPartita(ControPartita controPartita) {
        LOGGER.debug("--> Enter caricaControPartita");
        start("caricaControPartita");

        ControPartita controPartitaCaricata = null;
        try {
            controPartitaCaricata = contabilitaAnagraficaService.caricaControPartita(controPartita);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaControPartita");
        }
        LOGGER.debug("--> Exit caricaControPartita");
        return controPartitaCaricata;
    }

    @Override
    public List<ControPartita> caricaControPartite(AreaContabile areaContabile) {
        LOGGER.debug("--> Enter caricaControPartite");
        start("caricaControPartite");

        List<ControPartita> list = new ArrayList<ControPartita>();
        try {
            list = contabilitaAnagraficaService.caricaControPartite(areaContabile);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaControPartite");
        }
        LOGGER.debug("--> Exit caricaControPartite");
        return list;
    }

    @Override
    public List<ControPartita> caricaControPartite(TipoDocumento tipoDocumento, EntitaLite entita) {
        LOGGER.debug("--> Enter caricaControPartite");
        start("caricaControPartite");

        List<ControPartita> list = new ArrayList<ControPartita>();
        try {
            list = contabilitaAnagraficaService.caricaControPartite(tipoDocumento, entita);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaControPartite");
        }
        LOGGER.debug("--> Exit caricaControPartite");
        return list;
    }

    @Override
    public List<ControPartita> caricaControPartiteConImporto(AreaContabile areaContabile) {
        LOGGER.debug("--> Enter caricaControPartiteConImporto");
        start("caricaControPartiteConImporto");
        List<ControPartita> list = new ArrayList<ControPartita>();

        try {
            list = contabilitaAnagraficaService.caricaControPartiteConImporto(areaContabile);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaControPartiteConImporto");
        }
        LOGGER.debug("--> Exit caricaControPartiteConImporto");
        return list;
    }

    @Override
    public List<EntitaLite> caricaEntitaConStrutturaContabile(TipoDocumento tipoDocumento) {
        LOGGER.debug("--> Enter caricaEntitaConStrutturaContabile");
        start("caricaEntitaConStrutturaContabile");

        List<EntitaLite> entita = new ArrayList<>();
        try {
            entita = contabilitaAnagraficaService.caricaEntitaConStrutturaContabile(tipoDocumento);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaEntitaConStrutturaContabile");
        }
        LOGGER.debug("--> Exit caricaEntitaConStrutturaContabile ");
        return entita;
    }

    @Override
    public List<Mastro> caricaMastri() {
        LOGGER.debug("--> Enter caricaMastri");
        start("caricaMastri");
        List<Mastro> mastri = new ArrayList<Mastro>();
        try {
            mastri = contabilitaAnagraficaService.caricaMastri();
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaMastri");
        }
        LOGGER.debug("--> Exit caricaMastri");
        return mastri;
    }

    @Override
    public Mastro caricaMastro(Integer idMastro) {
        LOGGER.debug("--> Enter caricaMastro");
        start("caricaMastro");
        Mastro mastro = null;
        try {
            mastro = contabilitaAnagraficaService.caricaMastro(idMastro);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaMastro");
        }
        LOGGER.debug("--> Exit caricaMastro");
        return mastro;
    }

    @Override
    public List<RegistroIva> caricaRegistriIva(String fieldSearch, String valueSearch) {
        LOGGER.debug("--> Enter caricaRegistriIva");
        start("caricaRegistriIva");
        List<RegistroIva> list = null;
        try {
            list = contabilitaAnagraficaService.caricaRegistriIva(fieldSearch, valueSearch);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaRegistriIva");
        }
        LOGGER.debug("--> Exit caricaRegistriIva ");
        return list;
    }

    /**
     * @see it.eurotn.panjea.contabilita.rich.bd.IContabilitaAnagraficaBD#caricaRegistroIva(java.lang.Integer)
     */
    @Override
    public RegistroIva caricaRegistroIva(Integer id) {
        LOGGER.debug("--> Enter caricaRegistroIva");
        start("caricaRegistroIva");
        RegistroIva registroIva = null;
        try {
            registroIva = contabilitaAnagraficaService.caricaRegistroIva(id);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaRegistroIva");
        }
        LOGGER.debug("--> Exit caricaRegistroIva ");
        return registroIva;
    }

    @Override
    public SottoConto caricaSottoConto(Integer idSottoConto) {
        LOGGER.debug("--> Enter caricaSottoConto");
        start("caricaSottoConto");
        SottoConto sottoConto = null;
        try {
            sottoConto = contabilitaAnagraficaService.caricaSottoConto(idSottoConto);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaSottoConto");
        }
        LOGGER.debug("--> Exit caricaSottoConto");
        return sottoConto;
    }

    /**
     * @see it.eurotn.panjea.contabilita.rich.bd.IContabilitaAnagraficaBD#caricaSottoContoPerEntita(java.lang.Integer)
     */
    @Override
    public SottoConto caricaSottoContoPerEntita(SottotipoConto sottotipoConto, Integer codiceEntita) {
        LOGGER.debug("--> Enter caricaSottoContoPerEntita");
        start("caricaSottoContoPerEntita");
        SottoConto sottoConto = null;
        try {
            sottoConto = contabilitaAnagraficaService.caricaSottoContoPerEntita(sottotipoConto, codiceEntita);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaSottoContoPerEntita");
        }
        LOGGER.debug("--> Exit caricaSottoContoPerEntita ");
        return sottoConto;
    }

    @Override
    public List<StrutturaContabile> caricaStrutturaContabile(TipoDocumento tipoDocumento, EntitaLite entita) {
        LOGGER.debug("--> Enter caricaStrutturaContabile");
        start("caricaStrutturaContabile");
        List<StrutturaContabile> list = new ArrayList<StrutturaContabile>();
        try {
            list = contabilitaAnagraficaService.caricaStrutturaContabile(tipoDocumento, entita);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaStrutturaContabile");
        }
        LOGGER.debug("--> Exit caricaStrutturaContabile");
        return list;
    }

    /**
     * @see it.eurotn.panjea.contabilita.rich.bd.IContabilitaAnagraficaBD#caricaTipiAreaContabile()
     */
    @Override
    public List<TipoAreaContabile> caricaTipiAreaContabile(String fieldSearch, String valueSearch,
            boolean loadTipiDocumentiDisabilitati) {
        LOGGER.debug("--> Enter caricaTipiAreaContabile");
        start("caricaTipiAreaContabile");
        List<TipoAreaContabile> list = null;
        try {

            list = contabilitaAnagraficaService.caricaTipiAreaContabile(fieldSearch, valueSearch,
                    loadTipiDocumentiDisabilitati);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaTipiAreaContabile");
        }
        LOGGER.debug("--> Exit caricaTipiAreaContabile ");
        return list;
    }

    /**
     *
     * @see it.eurotn.panjea.contabilita.rich.bd.IContabilitaAnagraficaBD#caricaTipiDocumentoBase()
     */
    @Override
    public List<TipoDocumentoBase> caricaTipiDocumentoBase() {
        LOGGER.debug("--> Enter caricaTipiDocumentoBase");
        start("caricaTipiDocumentoBase");
        List<TipoDocumentoBase> tipiDocumentoBase = null;
        try {
            tipiDocumentoBase = contabilitaAnagraficaService.caricaTipiDocumentoBase();
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaTipiDocumentoBase");
        }
        LOGGER.debug("--> Exit caricaTipiDocumentoBase");
        return tipiDocumentoBase;
    }

    /**
     *
     * @see it.eurotn.panjea.contabilita.rich.bd.IContabilitaAnagraficaBD#caricaTipiOperazione()
     */
    @Override
    public Map<TipoOperazioneTipoDocumento, TipoAreaContabile> caricaTipiOperazione() {
        LOGGER.debug("--> Enter caricaTipiOperazione");
        start("caricaTipiOperazione");
        Map<TipoOperazioneTipoDocumento, TipoAreaContabile> map = null;
        try {
            map = contabilitaAnagraficaService.caricaTipiOperazione();
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaTipiOperazione");
        }
        LOGGER.debug("--> Exit caricaTipiOperazione ");
        return map;
    }

    /**
     * @see it.eurotn.panjea.contabilita.rich.bd.IContabilitaAnagraficaBD#caricaTipoAreaContabile(java.lang.Integer)
     */
    @Override
    public TipoAreaContabile caricaTipoAreaContabile(Integer id) {
        LOGGER.debug("--> Enter caricaTipoAreaContabile");
        start("caricaTipoAreaContabile");
        TipoAreaContabile tipoAreaContabile = null;
        try {
            tipoAreaContabile = contabilitaAnagraficaService.caricaTipoAreaContabile(id);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaTipoAreaContabile");
        }
        LOGGER.debug("--> Exit caricaTipoAreaContabile ");
        return tipoAreaContabile;
    }

    /**
     * @see it.eurotn.panjea.contabilita.rich.bd.IContabilitaAnagraficaBD#caricaTipoAreaContabilePerTipoDocumento(java.lang.Integer)
     */
    @Override
    public TipoAreaContabile caricaTipoAreaContabilePerTipoDocumento(Integer idTipoDocumento) {
        LOGGER.debug("--> Enter caricaTipoAreaContabilePerTipoDocumento");
        start("caricaTipoAreaContabilePerTipoDocumento");
        TipoAreaContabile tipoAreaContabile = null;
        try {
            tipoAreaContabile = contabilitaAnagraficaService.caricaTipoAreaContabilePerTipoDocumento(idTipoDocumento);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaTipoAreaContabilePerTipoDocumento");
        }
        LOGGER.debug("--> Exit caricaTipoAreaContabilePerTipoDocumento ");
        return tipoAreaContabile;
    }

    /**
     *
     * @see it.eurotn.panjea.contabilita.rich.bd.IContabilitaAnagraficaBD#caricaTipoDocumentoBase(Integer)
     */
    @Override
    public TipoDocumentoBase caricaTipoDocumentoBase(Integer idTipoDocumentoBase) {
        LOGGER.debug("--> Enter caricaTipoDocumentoBase");
        start("caricaTipoDocumentoBase");
        TipoDocumentoBase tipoDocumentoBase = null;
        try {
            tipoDocumentoBase = contabilitaAnagraficaService.caricaTipoDocumentoBase(idTipoDocumentoBase);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaTipoDocumentoBase");
        }
        LOGGER.debug("--> Exit caricaTipoDocumentoBase ");
        return tipoDocumentoBase;
    }

    /**
     * @see it.eurotn.panjea.contabilita.rich.bd.IContabilitaAnagraficaBD#creaSottoContoPerEntita(java.lang.Integer,
     *      it.eurotn.panjea.contabilita.domain.SottoConto)
     */
    @Override
    public SottoConto creaSottoContoPerEntita(Entita entita) {
        LOGGER.debug("--> Enter creaSottoContoPerEntita");
        start("creaSottoContoPerEntita");
        SottoConto sottoConto2 = null;
        try {
            sottoConto2 = contabilitaAnagraficaService.creaSottoContoPerEntita(entita);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("creaSottoContoPerEntita");
        }
        LOGGER.debug("--> Exit creaSottoContoPerEntita ");
        return sottoConto2;
    }

    /**
     * @return the contabilitaAnagraficaService
     */
    public ContabilitaAnagraficaService getContabilitaAnagraficaService() {
        return contabilitaAnagraficaService;
    }

    /**
     * @see it.eurotn.panjea.contabilita.rich.bd.IContabilitaAnagraficaBD#ricercaConti()
     */
    @Override
    public List<Conto> ricercaConti() {
        LOGGER.debug("--> Enter ricercaConti");
        start("ricercaConti");
        List<Conto> conti = new ArrayList<Conto>();
        try {
            conti = contabilitaAnagraficaService.ricercaConti();
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("ricercaConti");
        }
        LOGGER.debug("--> Exit ricercaConti ");
        return conti;
    }

    /**
     * @see it.eurotn.panjea.contabilita.rich.bd.IContabilitaAnagraficaBD#ricercaConti(it.eurotn.panjea.contabilita.domain.Conto.SottotipoConto)
     */
    @Override
    public List<Conto> ricercaConti(String codiceConto, SottotipoConto sottoTipoConto) {
        LOGGER.debug("--> Enter ricercaConti");
        start("ricercaConti");
        List<Conto> conti = new ArrayList<Conto>();
        try {
            conti = contabilitaAnagraficaService.ricercaConti(codiceConto, sottoTipoConto);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("ricercaConti");
        }
        LOGGER.debug("--> Exit ricercaConti ");
        return conti;
    }

    /**
     * @see it.eurotn.panjea.contabilita.rich.bd.IContabilitaAnagraficaBD#ricercaSottoConti()
     */
    @Override
    public List<SottoConto> ricercaSottoConti() {
        LOGGER.debug("--> Enter ricercaSottoConti");
        start("ricercaSottoConti");
        List<SottoConto> list = new ArrayList<SottoConto>();
        try {
            list = contabilitaAnagraficaService.ricercaSottoConti();
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("ricercaSottoConti");
        }
        LOGGER.debug("--> Exit ricercaSottoConti ");
        return list;
    }

    /**
     * @see it.eurotn.panjea.contabilita.rich.bd.IContabilitaAnagraficaBD#ricercaSottoConti(ParametriRicercaSottoConti)
     */
    @Override
    public List<SottoConto> ricercaSottoConti(ParametriRicercaSottoConti parametriRicercaSottoConti) {
        LOGGER.debug("--> Enter ricercaSottoConti");
        start("ricercaSottoConti");
        List<SottoConto> list = new ArrayList<SottoConto>();
        try {
            list = contabilitaAnagraficaService.ricercaSottoConti(parametriRicercaSottoConti);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("ricercaSottoConti");
        }
        LOGGER.debug("--> Exit ricercaSottoConti ");
        return list;
    }

    /**
     *
     * @see it.eurotn.panjea.contabilita.rich.bd.IContabilitaAnagraficaBD#ricercaSottoConti(java.lang.String)
     */
    @Override
    public List<SottoConto> ricercaSottoConti(String codiceSottoConto) {
        LOGGER.debug("--> Enter ricercaSottoConti");
        start("ricercaSottoConti");
        List<SottoConto> sottoconti = new ArrayList<SottoConto>();
        try {
            sottoconti = contabilitaAnagraficaService.ricercaSottoConti(codiceSottoConto);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("ricercaSottoConti");
        }
        LOGGER.debug("--> Exit ricercaSottoConti");
        return sottoconti;
    }

    /**
     * @see it.eurotn.panjea.contabilita.rich.bd.IContabilitaAnagraficaBD#ricercaSottoConti()
     */
    @Override
    public List<SottoConto> ricercaSottoContiOrdinati() {
        LOGGER.debug("--> Enter ricercaSottoConti");
        start("ricercaSottoConti");
        List<SottoConto> list = new ArrayList<SottoConto>();
        try {
            list = contabilitaAnagraficaService.ricercaSottoContiOrdinati();
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("ricercaSottoConti");
        }
        LOGGER.debug("--> Exit ricercaSottoConti ");
        return list;
    }

    @Override
    public List<SottoConto> ricercaSottoContiSearchObject(ParametriRicercaSottoConti parametriRicercaSottoConti) {
        LOGGER.debug("--> Enter ricercaSottoContiSearchObject");
        start("ricercaSottoConti");
        List<SottoConto> list = new ArrayList<SottoConto>();
        try {
            list = contabilitaAnagraficaService.ricercaSottoContiSearchObject(parametriRicercaSottoConti);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("ricercaSottoContiSearchObject");
        }
        LOGGER.debug("--> Exit ricercaSottoContiSearchObject ");
        return list;
    }

    @Override
    public CodiceIvaCorrispettivo salvaCodiceIvaCorrispettivo(CodiceIvaCorrispettivo codiceIvaCorrispettivo) {
        LOGGER.debug("--> Enter salvaCodiceIvaCorrispettivo");
        start("salvaCodiceIvaCorrispettivo");

        CodiceIvaCorrispettivo codiceIvaCorrispettivoSalvato = null;

        try {
            codiceIvaCorrispettivoSalvato = contabilitaAnagraficaService
                    .salvaCodiceIvaCorrispettivo(codiceIvaCorrispettivo);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaCodiceIvaCorrispettivo");
        }

        LOGGER.debug("--> Exit salvaCodiceIvaCorrispettivo");
        return codiceIvaCorrispettivoSalvato;
    }

    @Override
    public CodiceIvaPrevalente salvaCodiceIvaPrevalente(CodiceIvaPrevalente codiceIvaPrevalente) {
        LOGGER.debug("--> Enter salvaCodiceIvaPrevalente");
        start("salvaCodiceIvaPrevalente");
        CodiceIvaPrevalente codiceIvaPrevalenteSalvato = null;
        try {
            codiceIvaPrevalenteSalvato = contabilitaAnagraficaService.salvaCodiceIvaPrevalente(codiceIvaPrevalente);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaCodiceIvaPrevalente");
        }
        LOGGER.debug("--> Exit salvaCodiceIvaPrevalente ");
        return codiceIvaPrevalenteSalvato;
    }

    @Override
    public ContabilitaSettings salvaContabilitaSettings(ContabilitaSettings contabilitaSettings) {
        LOGGER.debug("--> Enter salvaContabilitaSettings");
        start("salvaContabilitaSettings");

        ContabilitaSettings contabilitaSettingsSalvate = null;
        contabilitaSettingsSalvate = contabilitaAnagraficaService.salvaContabilitaSettings(contabilitaSettings);

        end("salvaContabilitaSettings");
        LOGGER.debug("--> Exit salvaContabilitaSettings");
        return contabilitaSettingsSalvate;
    }

    @Override
    public Conto salvaConto(Conto conto) {
        LOGGER.debug("--> Enter salvaConto");
        start("salvaConto");
        Conto contoSalvato = null;
        try {
            contoSalvato = contabilitaAnagraficaService.salvaConto(conto);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaConto");
        }
        LOGGER.debug("--> Exit salvaConto");
        return contoSalvato;
    }

    @Override
    public ContoBase salvaContoBase(ContoBase contoBase) {
        LOGGER.debug("--> Enter salvaContoBase");
        start("salvaContoBase");
        ContoBase contoBaseSalvato = null;
        try {
            contoBaseSalvato = contabilitaAnagraficaService.salvaContoBase(contoBase);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaContoBase");
        }
        LOGGER.debug("--> Exit salvaContoBase");
        return contoBaseSalvato;
    }

    @Override
    public ControPartita salvaControPartita(ControPartita controPartita) {
        LOGGER.debug("--> Enter salvaControPartita");
        start("salvaControPartita");

        ControPartita controPartitaSalvata = null;
        try {
            controPartitaSalvata = contabilitaAnagraficaService.salvaControPartita(controPartita);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaControPartita");
        }
        LOGGER.debug("--> Exit salvaControPartita");
        return controPartitaSalvata;
    }

    @Override
    public Mastro salvaMastro(Mastro mastro) {
        LOGGER.debug("--> Enter salvaMastro");
        start("salvaMastro");
        Mastro mastroSalvato = null;
        try {
            mastroSalvato = contabilitaAnagraficaService.salvaMastro(mastro);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaMastro");
        }
        LOGGER.debug("--> Exit salvaMastro");
        return mastroSalvato;
    }

    /**
     * @see it.eurotn.panjea.contabilita.rich.bd.IContabilitaAnagraficaBD#salvaRegistroIva(it.eurotn.panjea.contabilita.domain.RegistroIva)
     */
    @Override
    public RegistroIva salvaRegistroIva(RegistroIva registroIva) {
        LOGGER.debug("--> Enter salvaRegistroIva");
        start("salvaRegistroIva");
        RegistroIva registroIva2 = null;
        try {
            registroIva2 = contabilitaAnagraficaService.salvaRegistroIva(registroIva);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaRegistroIva");
        }
        LOGGER.debug("--> Exit salvaRegistroIva ");
        return registroIva2;
    }

    @Override
    public SottoConto salvaSottoConto(SottoConto sottoConto) {
        LOGGER.debug("--> Enter salvaSottoConto");
        start("salvaSottoConto");
        SottoConto sottoContoSalvato = null;
        try {
            sottoContoSalvato = contabilitaAnagraficaService.salvaSottoConto(sottoConto);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaSottoConto");
        }
        LOGGER.debug("--> Exit salvaSottoConto");
        return sottoContoSalvato;
    }

    @Override
    public StrutturaContabile salvaStrutturaContabile(StrutturaContabile strutturaContabile) {
        LOGGER.debug("--> Enter salvaStrutturaContabile");
        start("salvaStrutturaContabile");
        StrutturaContabile strutturaContabileSalvata = null;
        try {
            strutturaContabileSalvata = contabilitaAnagraficaService.salvaStrutturaContabile(strutturaContabile);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaStrutturaContabile");
        }
        LOGGER.debug("--> Exit salvaStrutturaContabile");
        return strutturaContabileSalvata;
    }

    /**
     * @see it.eurotn.panjea.contabilita.rich.bd.IContabilitaAnagraficaBD#salvaTipoAreaContabile(it.eurotn.panjea.contabilita.domain.TipoAreaContabile)
     */
    @Override
    public TipoAreaContabile salvaTipoAreaContabile(TipoAreaContabile tipoAreaContabile) {
        LOGGER.debug("--> Enter salvaTipoAreaContabile");
        start("salvaTipoAreaContabile");
        TipoAreaContabile tipoAreaContabile2 = null;
        try {
            tipoAreaContabile2 = contabilitaAnagraficaService.salvaTipoAreaContabile(tipoAreaContabile);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaTipoAreaContabile");
        }
        LOGGER.debug("--> Exit salvaTipoAreaContabile ");
        return tipoAreaContabile2;
    }

    /**
     *
     * @see it.eurotn.panjea.contabilita.rich.bd.IContabilitaAnagraficaBD#salvaTipoDocumentoBase(TipoDocumentoBase)
     */
    @Override
    public TipoDocumentoBase salvaTipoDocumentoBase(TipoDocumentoBase tipoDocumentoBase) {
        LOGGER.debug("--> Enter salvaTipoDocumentoBase");
        start("salvaTipoDocumentoBase");
        TipoDocumentoBase tipoDocumentoBase2 = null;
        try {
            tipoDocumentoBase2 = contabilitaAnagraficaService.salvaTipoDocumentoBase(tipoDocumentoBase);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaTipoDocumentoBase");
        }
        LOGGER.debug("--> Exit salvaTipoDocumentoBase ");
        return tipoDocumentoBase2;
    }

    /**
     * @param contabilitaAnagraficaService
     *            the contabilitaAnagraficaService to set
     */
    public void setContabilitaAnagraficaService(ContabilitaAnagraficaService contabilitaAnagraficaService) {
        this.contabilitaAnagraficaService = contabilitaAnagraficaService;
    }

}
