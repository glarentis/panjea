package it.eurotn.panjea.vending.manager.sistemielettronici;

import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.exception.GenericException;
import it.eurotn.panjea.manager.interfaces.CrudManagerBean;
import it.eurotn.panjea.vending.domain.SistemaElettronico;
import it.eurotn.panjea.vending.manager.sistemielettronici.interfaces.SistemiElettroniciManager;

@Stateless(name = "Panjea.SistemiElettroniciManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.SistemiElettroniciManager")
public class SistemiElettroniciManagerBean extends CrudManagerBean<SistemaElettronico>
        implements SistemiElettroniciManager {

    private static final Logger LOGGER = Logger.getLogger(SistemiElettroniciManagerBean.class);

    @Override
    protected Class<SistemaElettronico> getManagedClass() {
        return SistemaElettronico.class;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<SistemaElettronico> ricercaSistemiElettronici(ParametriRicercaSistemiElettronici parametri) {
        LOGGER.debug("--> Enter ricercaSistemiElettronici");

        StringBuilder sb = new StringBuilder(1000);
        sb.append("select se.id as id, ");
        sb.append("se.version as version, ");
        sb.append("se.codice as codice, ");
        sb.append("se.descrizione as descrizione ");
        sb.append("from SistemaElettronico se ");
        sb.append("where 1=1 ");

        if (!StringUtils.isBlank(parametri.getCodice())) {
            sb.append(" and se.codice = '");
            sb.append(parametri.getCodice());
            sb.append("%'");
        }

        if (!StringUtils.isBlank(parametri.getDescrizione())) {
            sb.append(" and se.descrizione = '");
            sb.append(parametri.getDescrizione());
            sb.append("%'");
        }

        if (parametri.getTipoSistemaElettronico() != null) {
            sb.append(" and se.tipo = :paramTipo ");
        }

        Query query = panjeaDAO.prepareQuery(sb.toString(), SistemaElettronico.class, null);
        if (parametri.getTipoSistemaElettronico() != null) {
            query.setParameter("paramTipo", parametri.getTipoSistemaElettronico());
        }

        List<SistemaElettronico> result = null;
        try {
            result = panjeaDAO.getResultList(query);
        } catch (Exception e) {
            LOGGER.error("--> errore durante la ricerca dei sistemi elettronici.", e);
            throw new GenericException("errore durante la ricerca dei sistemi elettronici.", e);
        }

        LOGGER.debug("--> Exit enclosing_method");
        return result;
    }

}