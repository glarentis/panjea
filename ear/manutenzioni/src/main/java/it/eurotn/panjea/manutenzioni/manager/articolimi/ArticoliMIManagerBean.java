package it.eurotn.panjea.manutenzioni.manager.articolimi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.panjea.exception.GenericException;
import it.eurotn.panjea.manager.interfaces.CrudManagerBean;
import it.eurotn.panjea.manutenzioni.domain.ArticoloMI;
import it.eurotn.panjea.manutenzioni.manager.articolimi.interfaces.ArticoliMIManager;
import it.eurotn.panjea.manutenzioni.manager.articolimi.interfaces.ParametriRicercaArticoliMI;
import it.eurotn.panjea.manutenzioni.manager.installazioni.interfaces.InstallazioniManager;

@Stateless(name = "Panjea.ArticoliMIManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.ArticoliMIManager")
public class ArticoliMIManagerBean extends CrudManagerBean<ArticoloMI>implements ArticoliMIManager {

    private static final Logger LOGGER = Logger.getLogger(ArticoliMIManagerBean.class);

    @EJB
    private InstallazioniManager installazioniManager;

    @Override
    public ArticoloMI caricaByIdConInstallazione(Integer id) {
        ArticoloMI articolo = caricaById(id);
        articolo.setInstallazione(installazioniManager.caricaByArticolo(id));
        return articolo;
    }

    @Override
    protected Class<ArticoloMI> getManagedClass() {
        return ArticoloMI.class;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<ArticoloMI> ricercaArticoloMI(ParametriRicercaArticoliMI parametriRicerca) {
        LOGGER.debug("--> Enter ricercaArticoloMi");
        Map<String, Object> paramsMap = new HashMap<>();

        StringBuilder sb = new StringBuilder(1000);
        sb.append(
                "select ami.id as id ,ami.version as version,ami.codice as codice,ami.descrizioneLinguaAziendale as descrizioneLinguaAziendale ");
        if (parametriRicerca.isSoloDisponibili()) {
            sb.append(" from Installazione inst right join inst.articolo ami ");
            sb.append("where ami.class != it.eurotn.panjea.magazzino.domain.Articolo ");
            sb.append(" and inst is null ");
        } else {
            sb.append("from ArticoloMI ami ");
            sb.append("where 1=1 ");
        }
        if (!StringUtils.isEmpty(parametriRicerca.getCodice())) {
            sb.append(" and ami.codice like (:paramCodice)");
            paramsMap.put("paramCodice", parametriRicerca.getCodice() + "%");
        }
        if (!StringUtils.isEmpty(parametriRicerca.getDescrizione())) {
            sb.append(" and ami.descrizioneLinguaAziendale like (:paramDescrizione)");
            paramsMap.put("paramDescrizione", parametriRicerca.getDescrizione() + "%");
        }
        if (parametriRicerca.isSoloProprietaCliente()) {
            sb.append(" and ami.proprietaCliente = :paramProprietaCliente ");
            paramsMap.put("paramProprietaCliente", parametriRicerca.isSoloProprietaCliente());
        }
        Query query = panjeaDAO.prepareQuery(sb.toString(), ArticoloMI.class, null);
        for (Entry<String, Object> entry : paramsMap.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
        List<ArticoloMI> result;
        try {
            result = panjeaDAO.getResultList(query);
        } catch (DAOException e) {
            LOGGER.error("-->errore nel cercare gli articoli", e);
            throw new GenericException("-->errore nel cercare gli articoli", e);
        }
        LOGGER.debug("--> Exit ricercaArticoloMi");
        return result;
    }

}