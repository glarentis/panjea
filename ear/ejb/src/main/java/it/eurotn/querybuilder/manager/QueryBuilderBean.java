package it.eurotn.querybuilder.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.persister.entity.AbstractEntityPersister;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.querybuilder.domain.EntitaQuerableMetaData;
import it.eurotn.querybuilder.domain.ProprietaEntity;
import it.eurotn.querybuilder.manager.interfaces.QueryBuilder;
import it.eurotn.querybuilder.metadata.EntityQuerableMetadataLoader;

@Stateless(name = "Panjea.QueryBuilder")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.QueryBuilder")
public class QueryBuilderBean implements QueryBuilder {

    private static final Logger LOGGER = Logger.getLogger(QueryBuilderBean.class);

    @EJB
    private PanjeaDAO panjeaDAO;

    @Override
    public List<String> caricaAllEntityQuerable() {
        List<String> result = new ArrayList<>();
        @SuppressWarnings("unchecked")
        Map<String, AbstractEntityPersister> classi = ((Session) panjeaDAO.getEntityManager().getDelegate())
                .getSessionFactory().getAllClassMetadata();
        for (Entry<String, AbstractEntityPersister> entity : classi.entrySet()) {
            result.add(entity.getKey());

        }
        return result;
    }

    @Override
    public EntitaQuerableMetaData caricaEntitaQuerableMetaData(ProprietaEntity entity) {
        @SuppressWarnings("unchecked")
        Map<String, AbstractEntityPersister> classi = ((Session) panjeaDAO.getEntityManager().getDelegate())
                .getSessionFactory().getAllClassMetadata();

        EntityQuerableMetadataLoader loader = new EntityQuerableMetadataLoader();
        return loader.caricaAll(entity, classi);
    }

}
