package it.eurotn.panjea.rich.bd;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import it.eurotn.panjea.anagrafica.domain.CambioValuta;
import it.eurotn.panjea.anagrafica.domain.ValutaAzienda;
import it.eurotn.panjea.anagrafica.service.exception.CambioNonPresenteException;
import it.eurotn.panjea.anagrafica.service.interfaces.AnagraficaService;
import it.eurotn.panjea.utils.PanjeaSwingUtil;

/**
 * BD per la gestione delle valuta.<br/>
 * <B>NB:</B>le valute vengono messi in cache che si ripulisce dopo 10 sec. <br>
 * La valuta azienda è sempre il cache. Riavviare se si fanno delle modifiche
 *
 * @author giangi
 * @version 1.0, 27/lug/2011
 *
 */
public class ValutaBD extends AbstractBaseBD implements IValutaBD {

    public static final String BEAN_ID = "valutaBD";

    private static final Logger LOGGER = Logger.getLogger(ValutaBD.class);

    private AnagraficaService anagraficaService;

    @Override
    public void aggiornaTassi(byte[] byteArray) {
        LOGGER.debug("--> Enter aggiornaTassi");
        start("aggiornaTassi");
        try {
            anagraficaService.aggiornaTassi(byteArray);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("aggiornaTassi");
        }
        LOGGER.debug("--> Exit aggiornaTassi ");
    }

    @Override
    public void cancellaCambioValuta(CambioValuta cambioValuta) {
        LOGGER.debug("--> Enter cancellaCambioValuta");
        start("cancellaCambioValuta");
        try {
            anagraficaService.cancellaCambioValuta(cambioValuta);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaCambioValuta");
        }
        LOGGER.debug("--> Exit cancellaCambioValuta ");
    }

    @Override
    public void cancellaValutaAzienda(ValutaAzienda valutaAzienda) {
        LOGGER.debug("--> Enter cancellaValutaAzienda");
        start("cancellaValutaAzienda");
        try {
            anagraficaService.cancellaValutaAzienda(valutaAzienda);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaValutaAzienda");
        }
        LOGGER.debug("--> Exit cancellaValutaAzienda ");
    }

    @Override
    public CambioValuta caricaCambioValuta(String codiceValuta, Date date) throws CambioNonPresenteException {
        LOGGER.debug("--> Enter caricaCambioValuta");
        start("caricaCambioValuta");
        CambioValuta result = null;
        try {
            result = anagraficaService.caricaCambioValuta(codiceValuta, date);
        } catch (CambioNonPresenteException e) {
            throw e;
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaCambioValuta");
        }
        LOGGER.debug("--> Exit caricaCambioValuta ");
        return result;
    }

    @Override
    public List<CambioValuta> caricaCambiValute(Date date) {
        LOGGER.debug("--> Enter caricaCambiValuteUltimi");
        start("caricaCambiValuteUltimi");

        List<CambioValuta> list = new ArrayList<CambioValuta>();
        try {
            list = anagraficaService.caricaCambiValute(date);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaCambiValuteUltimi");
        }
        LOGGER.debug("--> Exit caricaCambiValuteUltimi ");
        return list;
    }

    @Override
    public List<CambioValuta> caricaCambiValute(String codiceValuta, int anno) {
        LOGGER.debug("--> Enter caricaCambiValute");
        start("caricaCambiValute");
        List<CambioValuta> list = new ArrayList<CambioValuta>();
        try {
            list = anagraficaService.caricaCambiValute(codiceValuta, anno);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaCambiValute");
        }
        LOGGER.debug("--> Exit caricaCambiValute ");
        return list;
    }

    @Override
    public ValutaAzienda caricaValutaAzienda(String codiceValuta) {
        LOGGER.debug("--> Enter caricaValutaAzienda");
        start("caricaValutaAzienda");
        ValutaAzienda result = null;
        try {
            result = anagraficaService.caricaValutaAzienda(codiceValuta);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaValutaAzienda");
        }
        LOGGER.debug("--> Exit caricaValutaAzienda ");
        return result;
    }

    @Override
    public ValutaAzienda caricaValutaAziendaCorrente() {
        LOGGER.debug("--> Enter caricaValutaAziendaCorrente");
        start("caricaValutaAziendaCorrente");
        ValutaAzienda valutaAziendale = null;
        try {
            valutaAziendale = anagraficaService.caricaValutaAziendaCorrente();
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaValutaAziendaCorrente");
        }
        LOGGER.debug("--> Exit caricaValutaAziendaCorrente ");
        return valutaAziendale;
    }

    @Override
    public List<ValutaAzienda> caricaValuteAzienda() {
        LOGGER.debug("--> Enter caricaValuteAzienda");
        start("caricaValuteAzienda");
        List<ValutaAzienda> list = new ArrayList<ValutaAzienda>();
        try {
            list = anagraficaService.caricaValuteAzienda();
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaValuteAzienda");
        }
        LOGGER.debug("--> Exit caricaValuteAzienda ");
        return list;
    }

    /**
     * @return Returns the anagraficaService.
     */
    public AnagraficaService getAnagraficaService() {
        return anagraficaService;
    }

    @Override
    public CambioValuta salvaCambioValuta(CambioValuta cambioValuta) {
        LOGGER.debug("--> Enter salvaCambioValuta");
        start("salvaCambioValuta");

        CambioValuta cambioValutaSalvato = null;
        try {
            cambioValutaSalvato = anagraficaService.salvaCambioValuta(cambioValuta);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaCambioValuta");
        }
        LOGGER.debug("--> Exit salvaCambioValuta ");
        return cambioValutaSalvato;
    }

    @Override
    public ValutaAzienda salvaValutaAzienda(ValutaAzienda valutaAzienda) {
        LOGGER.debug("--> Enter salValutaAzienda");
        start("salValutaAzienda");

        ValutaAzienda valutaAziendaSalvata = null;
        try {
            valutaAziendaSalvata = anagraficaService.salvaValutaAzienda(valutaAzienda);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salValutaAzienda");
        }
        LOGGER.debug("--> Exit salValutaAzienda ");
        return valutaAziendaSalvata;
    }

    /**
     * @param anagraficaService
     *            The anagraficaService to set.
     */
    public void setAnagraficaService(AnagraficaService anagraficaService) {
        this.anagraficaService = anagraficaService;
    }

}