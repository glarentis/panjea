package it.eurotn.querybuilder.manager;

import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.panjea.exception.GenericException;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.querybuilder.domain.ProprietaEntity;
import it.eurotn.querybuilder.domain.ProprietaEntityPersister;
import it.eurotn.querybuilder.domain.ResultCriteria;
import it.eurotn.querybuilder.manager.interfaces.QueryExecutor;

@Stateless(name = "Panjea.QueryExecutor")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.QueryExecutor")
public class QueryExecutorBean implements QueryExecutor {
    private static final Logger LOGGER = Logger.getLogger(QueryExecutorBean.class);

    @EJB
    private PanjeaDAO panjeaDAO;

    @Override
    public ResultCriteria execute(final ProprietaEntityPersister proprietaBase, List<ProprietaEntity> proprieta) {
        HqlBuilder hqlBuilder = new HqlBuilder(proprietaBase, proprieta);
        String hql = hqlBuilder.build();
        System.out.println(hql);
        Query query = panjeaDAO.prepareQuery(hql, proprietaBase.getType(), null);
        List<?> result;
        try {
            result = panjeaDAO.getResultList(query);
        } catch (DAOException e) {
            LOGGER.error("-->errore nell'eseguire la query " + hql, e);
            throw new GenericException("-->errore nell'eseguire la query " + hql, e);
        }
        return new ResultCriteria(result, proprietaBase.getType(), hqlBuilder.getSelectFieldsName());
    }

}
