package it.eurotn.panjea.vending.manager.evadts.rilevazioni;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.exception.GenericException;
import it.eurotn.panjea.manager.interfaces.CrudManagerBean;
import it.eurotn.panjea.vending.domain.evadts.RilevazioneEvaDts;
import it.eurotn.panjea.vending.manager.evadts.rilevazioni.interfaces.RilevazioniEvaDtsManager;

@Stateless(name = "Panjea.RilevazioniEvaDtsManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.RilevazioniEvaDtsManager")
public class RilevazioniEvaDtsManagerBean extends CrudManagerBean<RilevazioneEvaDts>
        implements RilevazioniEvaDtsManager {

    private static final Logger LOGGER = Logger.getLogger(RilevazioniEvaDtsManagerBean.class);

    @Override
    public RilevazioneEvaDts caricaById(Integer id) {
        RilevazioneEvaDts ril = super.caricaById(id);
        Hibernate.initialize(ril.getErrori());
        return ril;
    }

    @Override
    protected Class<RilevazioneEvaDts> getManagedClass() {
        return RilevazioneEvaDts.class;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<RilevazioneEvaDts> ricercaRilevazioniEvaDts(ParametriRicercaRilevazioniEvaDts parametri) {
        LOGGER.debug("--> Enter ricercaRilevazioniEvaDts");

        StringBuilder sb = new StringBuilder(500);
        sb.append("select ril.id as id, ");
        sb.append("rif.operatore.id as areaRifornimento$operatore$id, ");
        sb.append("rif.operatore.codice as areaRifornimento$operatore$codice, ");
        sb.append("rif.operatore.nome as areaRifornimento$operatore$nome, ");
        sb.append("rif.operatore.cognome as areaRifornimento$operatore$cognome, ");
        sb.append("rif.distributore.id as areaRifornimento$distributore$id, ");
        sb.append("rif.distributore.codice as areaRifornimento$distributore$codice, ");
        sb.append("rif.distributore.descrizioneLinguaAziendale as areaRifornimento$distributore$descrizione, ");
        sb.append("rif.installazione.id as areaRifornimento$installazione$id, ");
        sb.append("rif.installazione.codice as areaRifornimento$installazione$codice, ");
        sb.append("rif.installazione.descrizione as areaRifornimento$installazione$descrizione, ");
        // doc AM
        sb.append(" docAm.id as areaRifornimento$areaMagazzino$documento$id, ");
        sb.append("docAm.codice as areaRifornimento$areaMagazzino$documento$codice,");
        sb.append("docAm.dataDocumento as areaRifornimento$areaMagazzino$documento$dataDocumento, ");
        sb.append("tipoDocAm.codice as areaRifornimento$areaMagazzino$documento$tipoDocumento$codice, ");
        sb.append("tipoDocAm.descrizione as areaRifornimento$areaMagazzino$documento$tipoDocumento$descrizione, ");
        sb.append("tipoDocAm.tipoEntita as areaRifornimento$areaMagazzino$documento$tipoDocumento$tipoEntita, ");
        // entita AM
        sb.append("entAm.id as areaRifornimento$areaMagazzino$documento$entita$id, ");
        sb.append("entAm.codice as areaRifornimento$areaMagazzino$documento$entita$codice, ");
        sb.append(
                " anagAm.denominazione as areaRifornimento$areaMagazzino$documento$entita$anagrafica$denominazione, ");
        sb.append("sedeEntAm.id as areaRifornimento$areaMagazzino$documento$sedeEntita$id, ");
        sb.append("sedeEntAm.codice as areaRifornimento$areaMagazzino$documento$sedeEntita$codice, ");
        sb.append("sedeAnagAm as areaRifornimento$areaMagazzino$documento$sedeEntita$sede, ");
        // doc AO
        sb.append("docAo.id as areaRifornimento$areaOrdine$documento$id, ");
        sb.append("docAo.codice as areaRifornimento$areaOrdine$documento$codice,");
        sb.append("docAo.dataDocumento as areaRifornimento$areaOrdine$documento$dataDocumento, ");
        sb.append("tipoDocAo.codice as areaRifornimento$areaOrdine$documento$tipoDocumento$codice, ");
        // entita AO
        sb.append("entAo.id as areaRifornimento$areaOrdine$documento$entita$id, ");
        sb.append("entAo.codice as areaRifornimento$areaOrdine$documento$entita$codice, ");
        sb.append("anagAo.denominazione as areaRifornimento$areaOrdine$documento$entita$anagrafica$denominazione, ");
        sb.append("sedeEntAo.id as areaRifornimento$areaOrdine$documento$sedeEntita$id, ");
        sb.append("sedeEntAo.codice as areaRifornimento$areaOrdine$documento$sedeEntita$codice, ");
        sb.append("sedeAnagAo as areaRifornimento$areaOrdine$documento$sedeEntita$sede ");
        sb.append("from RilevazioneEvaDts ril inner join ril.areaRifornimento rif ");
        sb.append(
                "left join rif.areaMagazzino am left join am.documento docAm left join docAm.tipoDocumento tipoDocAm left join docAm.entita entAm left join entAm.anagrafica anagAm left join docAm.sedeEntita sedeEntAm left join sedeEntAm.sede sedeAnagAm ");
        sb.append(
                "left join rif.areaOrdine ao left join ao.documento docAo left join docAo.tipoDocumento tipoDocAo left join docAo.entita entAo left join entAo.anagrafica anagAo left join docAo.sedeEntita sedeEntAo left join sedeEntAo.sede sedeAnagAo ");
        sb.append("where ");
        sb.append(" ((docAm.dataDocumento >= :paramDataIniziale and docAm.dataDocumento <= :paramDataFinale) or ");
        sb.append(" (docAo.dataDocumento >= :paramDataIniziale and docAo.dataDocumento <= :paramDataFinale)) ");

        Map<String, Object> mapParams = new HashMap<>();
        mapParams.put("paramDataIniziale", parametri.getPeriodo().getDataIniziale());
        mapParams.put("paramDataFinale", parametri.getPeriodo().getDataFinale());

        if (parametri.getDistributore() != null && !parametri.getDistributore().isNew()) {
            sb.append(" and rif.distributore = :paramDistributore ");
            mapParams.put("paramDistributore", parametri.getDistributore());
        }

        if (parametri.getInstallazione() != null && !parametri.getInstallazione().isNew()) {
            sb.append(" and rif.installazione = :paramInstallazione ");
            mapParams.put("paramInstallazione", parametri.getInstallazione());
        }

        if (parametri.getOperatore() != null && !parametri.getOperatore().isNew()) {
            sb.append(" and rif.operatore = :paramOperatore ");
            mapParams.put("paramOperatore", parametri.getOperatore());
        }

        if (parametri.getEntita() != null && !parametri.getEntita().isNew()) {
            sb.append(" and (docAm.entita = :paramEntita or docAo.entita = :paramEntita) ");
            mapParams.put("paramEntita", parametri.getEntita());

            if (parametri.getSedeEntita() != null && !parametri.getSedeEntita().isNew()) {
                sb.append(" and (docAm.sedeEntita = :paramSedeEntita or docAo.sedeEntita = :paramSedeEntita) ");
                mapParams.put("paramSedeEntita", parametri.getSedeEntita());
            }
        }

        Query query = panjeaDAO.prepareQuery(sb.toString(), RilevazioneEvaDts.class, null);
        for (Entry<String, Object> entry : mapParams.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }

        List<RilevazioneEvaDts> rilevazioni = null;
        try {
            rilevazioni = panjeaDAO.getResultList(query);
        } catch (Exception e) {
            LOGGER.error("--> errore durante la ricerca delle rilavazioni Eva-Dts.", e);
            throw new GenericException("errore durante la ricerca delle rilavazioni Eva-Dts.", e);
        }

        LOGGER.debug("--> Exit ricercaRilevazioniEvaDts");
        return rilevazioni;
    }

}