package it.eurotn.panjea.rich.bd;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.querybuilder.domain.EntitaQuerableMetaData;
import it.eurotn.querybuilder.domain.ParametriRicercaQueryBuilder;
import it.eurotn.querybuilder.domain.ProprietaEntity;
import it.eurotn.querybuilder.domain.ProprietaEntityPersister;
import it.eurotn.querybuilder.domain.ResultCriteria;
import it.eurotn.querybuilder.service.interfaces.QueryBuilderService;

public class QueryBuilderBD extends AbstractBaseBD implements IQueryBuilderBD {

    public static final String BEAN_ID = "queryBuilderBD";

    private static final Logger LOGGER = Logger.getLogger(QueryBuilderBD.class);

    private QueryBuilderService queryBuilderService;

    @Override
    public List<Class<?>> caricaAllEntityQuerable() {
        LOGGER.debug("--> Enter caricaAllEntityQuerable");
        start("caricaAllEntityQuerable");
        List<Class<?>> result = new ArrayList<>();
        try {
            List<String> resultString = queryBuilderService.caricaAllEntityQuerable();
            for (String classString : resultString) {
                try {
                    result.add(Class.forName(classString));
                } catch (ClassNotFoundException e) {
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("--> Classe lato swing non presente, non la aggiungo");
                    }
                }
            }
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaAllEntityQuerable");
        }
        LOGGER.debug("--> Exit caricaAllEntityQuerable ");
        return result;
    }

    @Override
    public EntitaQuerableMetaData caricaEntitaQuerableMetaData(ProprietaEntity proprieta) {
        LOGGER.debug("--> Enter caricaEntitaQuerableMetaData");
        start("caricaEntitaQuerableMetaData");
        EntitaQuerableMetaData result = null;
        try {
            result = queryBuilderService.caricaEntitaQuerableMetaData(proprieta);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaEntitaQuerableMetaData");
        }
        LOGGER.debug("--> Exit caricaEntitaQuerableMetaData ");
        return result;
    }

    @Override
    public List<ParametriRicercaQueryBuilder> caricaParametriRicerca(Class<? extends EntityBase> clazz) {
        LOGGER.debug("--> Enter caricaParametriRicerca");
        start("caricaParametriRicerca");

        List<ParametriRicercaQueryBuilder> result = null;
        try {
            result = queryBuilderService.caricaParametriRicerca(clazz);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaParametriRicerca");
        }
        LOGGER.debug("--> Exit caricaParametriRicerca ");
        return result;
    }

    @Override
    public ResultCriteria execute(Class<?> clazz, List<ProprietaEntity> proprieta) {
        LOGGER.debug("--> Enter execute");
        start("execute");
        ResultCriteria result = null;
        try {
            result = queryBuilderService.execute(new ProprietaEntityPersister(clazz), proprieta);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("execute");
        }
        LOGGER.debug("--> Exit execute ");

        return result;
    }

    @Override
    public ParametriRicercaQueryBuilder salvaParametriRicerca(ParametriRicercaQueryBuilder parametri) {
        LOGGER.debug("--> Enter salvaParametriRicerca");
        start("salvaParametriRicerca");

        ParametriRicercaQueryBuilder parametriSalvati = null;
        try {
            parametriSalvati = queryBuilderService.salvaParametriRicerca(parametri);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaParametriRicerca");
        }
        LOGGER.debug("--> Exit salvaParametriRicerca ");

        return parametriSalvati;
    }

    /**
     * @param queryBuilderService
     *            the queryBuilderService to set
     */
    public void setQueryBuilderService(QueryBuilderService queryBuilderService) {
        this.queryBuilderService = queryBuilderService;
    }

}
