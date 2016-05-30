package it.eurotn.querybuilder.service;

import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.RemoteBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.entity.EntityBase;
import it.eurotn.querybuilder.domain.EntitaQuerableMetaData;
import it.eurotn.querybuilder.domain.ParametriRicercaQueryBuilder;
import it.eurotn.querybuilder.domain.ProprietaEntity;
import it.eurotn.querybuilder.domain.ProprietaEntityPersister;
import it.eurotn.querybuilder.domain.ResultCriteria;
import it.eurotn.querybuilder.manager.interfaces.ParametriRicercaManager;
import it.eurotn.querybuilder.manager.interfaces.QueryBuilder;
import it.eurotn.querybuilder.manager.interfaces.QueryExecutor;
import it.eurotn.querybuilder.service.interfaces.QueryBuilderService;

@Stateless(name = "Panjea.QueryBuilderService")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@RemoteBinding(jndiBinding = "Panjea.QueryBuilderService")
public class QueryBuilderServiceBean implements QueryBuilderService {

    @EJB
    private QueryBuilder queryBuilder;

    @EJB
    private QueryExecutor queryExecutor;

    @EJB
    private ParametriRicercaManager parametriRicercaManager;

    @Override
    public List<String> caricaAllEntityQuerable() {
        return queryBuilder.caricaAllEntityQuerable();
    }

    @Override
    public EntitaQuerableMetaData caricaEntitaQuerableMetaData(ProprietaEntity entity) {
        return queryBuilder.caricaEntitaQuerableMetaData(entity);
    }

    @Override
    public List<ParametriRicercaQueryBuilder> caricaParametriRicerca(Class<? extends EntityBase> clazz) {
        return parametriRicercaManager.caricaParametriRicerca(clazz);
    }

    @Override
    public ResultCriteria execute(ProprietaEntityPersister proprietaBase, List<ProprietaEntity> proprieta) {
        return queryExecutor.execute(proprietaBase, proprieta);
    }

    @Override
    public ParametriRicercaQueryBuilder salvaParametriRicerca(ParametriRicercaQueryBuilder parametri) {
        return parametriRicercaManager.salvaParametriRicerca(parametri);
    }

}
