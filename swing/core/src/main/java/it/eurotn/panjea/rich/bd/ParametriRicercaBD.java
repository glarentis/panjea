package it.eurotn.panjea.rich.bd;

import java.util.List;

import org.apache.log4j.Logger;

import it.eurotn.panjea.parametriricerca.domain.AbstractParametriRicerca;
import it.eurotn.panjea.parametriricerca.domain.ParametriRicercaDTO;
import it.eurotn.panjea.parametriricerca.service.interfaces.ParametriRicercaService;
import it.eurotn.panjea.utils.PanjeaSwingUtil;

public class ParametriRicercaBD extends AbstractBaseBD implements IParametriRicercaBD {

    public static final String BEAN_ID = "parametriRicercaBD";

    private static final Logger LOGGER = Logger.getLogger(ParametriRicercaBD.class);
    private ParametriRicercaService parametriRicercaService;

    @Override
    public void cancellaParametro(Class<? extends AbstractParametriRicerca> classeParametro, String nome) {
        LOGGER.debug("--> Enter cancellaParametro");
        start("cancellaParametro");
        try {
            parametriRicercaService.cancellaParametro(classeParametro, nome);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaParametro");
        }
        LOGGER.debug("--> Exit cancellaParametro ");
    }

    @Override
    public List<ParametriRicercaDTO> caricaParametri(Class<? extends AbstractParametriRicerca> classeParametro) {
        LOGGER.debug("--> Enter caricaParametri");
        start("caricaParametri");
        List<ParametriRicercaDTO> result = null;
        try {
            result = parametriRicercaService.caricaParametri(classeParametro);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaParametri");
        }
        LOGGER.debug("--> Exit caricaParametri ");
        return result;
    }

    @Override
    public AbstractParametriRicerca caricaParametro(Class<? extends AbstractParametriRicerca> classeParametro,
            String nome) {
        LOGGER.debug("--> Enter caricaParametro");
        start("caricaParametro");
        AbstractParametriRicerca result = null;
        try {
            result = parametriRicercaService.caricaParametro(classeParametro, nome);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaParametro");
        }
        LOGGER.debug("--> Exit caricaParametro ");
        return result;
    }

    @Override
    public AbstractParametriRicerca salvaParametro(AbstractParametriRicerca parametro) {
        LOGGER.debug("--> Enter salvaParametro");
        start("salvaParametro");
        AbstractParametriRicerca result = null;
        try {
            result = parametriRicercaService.salvaParametro(parametro);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaParametro");
        }
        LOGGER.debug("--> Exit salvaParametro ");
        return result;
    }

    /**
     * @param parametriRicercaService
     *            The parametriRicercaService to set.
     */
    public void setParametriRicercaService(ParametriRicercaService parametriRicercaService) {
        this.parametriRicercaService = parametriRicercaService;
    }

}
