package it.eurotn.panjea.agenti.manager;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.agenti.domain.lite.AgenteLite;
import it.eurotn.panjea.agenti.manager.interfaces.SediAgenteManager;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;

@Stateless(name = "Panjea.SediAgenteManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.SediAgenteManager")
public class SediAgenteManagerBean implements SediAgenteManager {

    private static final Logger LOGGER = Logger.getLogger(SediAgenteManagerBean.class);

    @EJB
    protected PanjeaDAO panjeaDAO;

    @Override
    public Set<SedeEntita> caricaSediAssociate(AgenteLite agente) {
        LOGGER.debug("--> Enter caricaSediAssociate");

        StringBuilder sb = new StringBuilder();

        sb.append(
                "select s from SedeEntita s join fetch s.entita e join fetch e.anagrafica ana join fetch s.sede sedeAnag left join fetch s.agente ag where s.abilitato=true and s.agente.id=:agente");
        Query hql = panjeaDAO.prepareQuery(sb.toString());
        hql.setParameter("agente", agente.getId());
        @SuppressWarnings("unchecked")
        List<SedeEntita> sedi = hql.getResultList();

        Set<SedeEntita> sediResult = new HashSet<SedeEntita>();
        sediResult.addAll(sedi);
        LOGGER.debug("--> Exit caricaSediAssociate");
        return sediResult;
    }

}
