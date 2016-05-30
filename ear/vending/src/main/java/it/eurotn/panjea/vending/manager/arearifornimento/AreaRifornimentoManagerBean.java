package it.eurotn.panjea.vending.manager.arearifornimento;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.anagrafica.documenti.manager.interfaces.DocumentiManager;
import it.eurotn.panjea.exception.GenericException;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.AreaMagazzinoManager;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.TipiAreaMagazzinoManager;
import it.eurotn.panjea.manager.interfaces.CrudManagerBean;
import it.eurotn.panjea.vending.domain.documento.AreaRifornimento;
import it.eurotn.panjea.vending.manager.arearifornimento.interfaces.AreaRifornimentoManager;

@Stateless(name = "Panjea.AreaRifornimentoManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.AreaRifornimentoManager")
public class AreaRifornimentoManagerBean extends CrudManagerBean<AreaRifornimento> implements AreaRifornimentoManager {
    private static final Logger LOGGER = Logger.getLogger(AreaRifornimentoManagerBean.class);

    @EJB
    private AreaMagazzinoManager areaMagazzinoManager;
    @EJB
    private DocumentiManager documentiManager;
    @EJB
    private TipiAreaMagazzinoManager tipiAreaMagazzinoManager;

    @Override
    public AreaRifornimento caricaById(Integer id) {
        AreaRifornimento areaRifornimento = super.caricaById(id);
        Hibernate.initialize(areaRifornimento.getInstallazione());
        Hibernate.initialize(areaRifornimento.getInstallazione().getDeposito());
        Hibernate.initialize(
                areaRifornimento.getInstallazione().getDeposito().getSedeEntita().getEntita().getAnagrafica());
        Hibernate.initialize(areaRifornimento.getInstallazione().getArticolo());
        Hibernate.initialize(areaRifornimento.getOperatore());
        if (areaRifornimento.getAreaMagazzino() != null) {
            Hibernate.initialize(areaRifornimento.getAreaMagazzino().getDepositoOrigine());
        } else if (areaRifornimento.getAreaOrdine() != null) {
            Hibernate.initialize(areaRifornimento.getAreaOrdine().getDepositoOrigine());
        }
        return areaRifornimento;
    }

    @Override
    protected Class<AreaRifornimento> getManagedClass() {
        return AreaRifornimento.class;
    }

    private String getQueryRicercaAreeRifornimento() {
        StringBuilder sb = new StringBuilder(2000);
        sb.append("select ar.id as id, ar.version as version, ");
        sb.append("am.id as areaMagazzino$id, am.version as areaMagazzino$version, ");
        sb.append("doc.id as areaMagazzino$documento$id, ");
        sb.append("doc.codice as areaMagazzino$documento$codice, ");
        sb.append("doc.dataDocumento as areaMagazzino$documento$dataDocumento, ");
        sb.append("tipoDoc.id as areaMagazzino$documento$tipoDocumento$id, ");
        sb.append("tipoDoc.codice as areaMagazzino$documento$tipoDocumento$codice, ");
        sb.append("tipoDoc.descrizione as areaMagazzino$documento$tipoDocumento$descrizione, ");
        sb.append("tipoDoc.tipoEntita as areaMagazzino$documento$tipoDocumento$tipoEntita, ");
        sb.append("ent.id as areaMagazzino$documento$entita$id, ");
        sb.append("ent.codice as areaMagazzino$documento$entita$codice, ");
        sb.append("anag.denominazione as areaMagazzino$documento$entita$anagrafica$denominazione, ");
        sb.append("sedeEnt.id as areaMagazzino$documento$sedeEntita$id, ");
        sb.append("sedeEnt.codice as areaMagazzino$documento$sedeEntita$codice, ");
        sb.append("sedeEnt.sede as areaMagazzino$documento$sedeEntita$sede, ");
        sb.append(
                "distr.id as distributore$id, distr.codice as distributore$codice, distr.descrizioneLinguaAziendale as distributore$descrizione, ");
        sb.append(
                "inst.id as installazione$id, inst.codice as installazione$codice, inst.descrizione as installazione$descrizione, ");
        sb.append("op.id as operatore$id, ");
        sb.append("op.codice as operatore$codice, ");
        sb.append("op.nome as operatore$nome, ");
        sb.append("op.cognome as operatore$cognome ");
        sb.append("from AreaRifornimento ar ");
        sb.append("inner join ar.areaMagazzino am inner join am.documento doc ");
        sb.append("inner join doc.tipoDocumento tipoDoc inner join doc.entita ent ");
        sb.append("inner join ent.anagrafica anag inner join doc.sedeEntita sedeEnt ");
        sb.append("inner join ar.distributore distr inner join ar.installazione inst ");
        sb.append("left join ar.operatore op ");

        return sb.toString();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<AreaRifornimento> ricercaAreeRifornimento(ParametriRicercaAreeRifornimento parametri) { // NOSONAR
        LOGGER.debug("--> Enter ricercaAreeRifornimento");
        Map<String, Object> paramsHQL = new HashMap<>();

        StringBuilder sb = new StringBuilder(getQueryRicercaAreeRifornimento());
        sb.append("where 1=1 ");
        if (parametri.getPeriodo().getDataIniziale() != null) {
            sb.append(" and doc.dataDocumento >= :dataIniziale ");
            paramsHQL.put("dataIniziale", parametri.getPeriodo().getDataIniziale());
        }
        if (parametri.getPeriodo().getDataFinale() != null) {
            sb.append(" and doc.dataDocumento <= :dataFinale ");
            paramsHQL.put("dataFinale", parametri.getPeriodo().getDataFinale());
        }
        if (parametri.getEntita() != null) {
            sb.append(" and doc.entita = :paramEntita ");
            paramsHQL.put("paramEntita", parametri.getEntita());
        }
        if (parametri.getSedeEntita() != null) {
            sb.append(" and doc.sedeEntita = :paramSedeEntita ");
            paramsHQL.put("paramSedeEntita", parametri.getSedeEntita());
        }
        if (parametri.getInstallazione() != null) {
            sb.append(" and ar.installazione = :paramInstallazione ");
            paramsHQL.put("paramInstallazione", parametri.getInstallazione());
        }
        if (parametri.getDistributore() != null) {
            sb.append(" and ar.distributore = :paramDistributore ");
            paramsHQL.put("paramDistributore", parametri.getDistributore());
        }
        if (parametri.getOperatore() != null) {
            sb.append(" and ar.operatore = :paramOperatore ");
            paramsHQL.put("paramOperatore", parametri.getOperatore());
        }

        Query query = panjeaDAO.prepareQuery(sb.toString(), AreaRifornimento.class, null);
        for (Entry<String, Object> entry : paramsHQL.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }

        List<AreaRifornimento> result = null;
        try {
            result = panjeaDAO.getResultList(query);
        } catch (Exception e) {
            LOGGER.error("--> errore durante il caricamento dei rifornimenti.", e);
            throw new GenericException("errore durante il caricamento dei rifornimenti.", e);
        }

        LOGGER.debug("--> Exit ricercaAreeRifornimento");
        return result;
    }

    @Override
    public AreaRifornimento salva(AreaRifornimento areaRifornimento) {
        areaRifornimento.getDatiVendingArea().getBattute();// se null li inizializzo altrimenti
                                                           // genera errore nel
        // salva
        areaRifornimento.getDatiVendingArea().getLettureContatore();
        AreaRifornimento areaRifornimentoSalvata = super.salva(areaRifornimento);
        areaRifornimentoSalvata = caricaById(areaRifornimentoSalvata.getId());

        return areaRifornimentoSalvata;
    }

}