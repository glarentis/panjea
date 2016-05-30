package it.eurotn.panjea.manutenzioni.manager.operatori;

import java.util.List;

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
import it.eurotn.panjea.magazzino.domain.MezzoTrasporto;
import it.eurotn.panjea.magazzino.manager.interfaces.MezziTrasportoManager;
import it.eurotn.panjea.manager.interfaces.CrudManagerBean;
import it.eurotn.panjea.manutenzioni.domain.Operatore;
import it.eurotn.panjea.manutenzioni.manager.operatori.interfaces.OperatoriManager;

@Stateless(name = "Panjea.OperatoriManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.OperatoriManager")
public class OperatoriManagerBean extends CrudManagerBean<Operatore>implements OperatoriManager {

    private static final Logger LOGGER = Logger.getLogger(OperatoriManagerBean.class);

    @EJB
    private MezziTrasportoManager mezziTrasportoManager;

    @Override
    public Operatore caricaByCodice(String codiceOperatore) {
        LOGGER.debug("--> Enter caricaByCodice");
        Query query = panjeaDAO.prepareQuery("select o from Operatore o where o.codice=:codiceOperatore");
        query.setParameter("codiceOperatore", codiceOperatore);
        Operatore operatore = null;
        try {
            operatore = (Operatore) panjeaDAO.getSingleResult(query);
        } catch (DAOException e) {
            LOGGER.error("-->errore nel caricare l'operartore con codice " + codiceOperatore, e);
            throw new GenericException("-->errore nel caricare l'operartore con codice " + codiceOperatore, e);
        }
        LOGGER.debug("--> Exit caricaByCodice");
        return operatore;
    }

    @Override
    protected Class<Operatore> getManagedClass() {
        return Operatore.class;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Operatore> ricercaOperatori(ParametriRicercaOperatori parametri) {
        LOGGER.debug("--> Enter ricercaOperatori");

        StringBuilder hql = new StringBuilder(
                "select id as id,version as version,codice as codice ,nome as nome ,cognome as cognome from Operatore o where 1 = 1 ");
        if (parametri.getTecnico() != null) {
            hql.append(" and o.tecnico = " + parametri.getTecnico().booleanValue());
        }
        if (parametri.getCaricatore() != null) {
            hql.append(" and o.caricatore = " + parametri.getCaricatore().booleanValue());
        }
        if (!StringUtils.isEmpty(parametri.getDenominazione())) {
            hql.append(" and (o.nome like '%" + parametri.getDenominazione() + "'");
            hql.append(" or o.cognome like '%" + parametri.getDenominazione() + "') ");
        }
        if (!StringUtils.isEmpty(parametri.getCodice())) {
            hql.append(" and o.codice like '%" + parametri.getCodice() + "'");
        }
        Query query = panjeaDAO.prepareQuery(hql.toString(), Operatore.class, null);

        List<Operatore> operatori = null;
        try {
            operatori = panjeaDAO.getResultList(query);
        } catch (DAOException e) {
            LOGGER.error("-->errore nel caricare la lista di operatori", e);
            throw new GenericException("-->errore nel caricare la lista di operatori", e);
        }
        LOGGER.debug("--> Exit ricercaOperatori");
        return operatori;
    }

    @Override
    public Operatore salva(Operatore operatore) {
        LOGGER.debug("--> Enter salva");

        if (operatore.isNew()) {
            operatore.setCodiceAzienda(getCodiceAzienda());
        }

        Operatore operatoreSave = null;
        try {
            // se il mezzo scelto per l'operatore non ha un deposito lo creo con il codice
            // dell'operatore
            if (operatore.getMezzoTrasporto() != null && operatore.getMezzoTrasporto().getDeposito() == null) {
                MezzoTrasporto mezzoTrasporto = mezziTrasportoManager.creaNuovoDepositoMezzoDiTrasporto(
                        operatore.getMezzoTrasporto(), operatore.getCodice(),
                        operatore.getNome() + " " + operatore.getCognome());
                operatore.setMezzoTrasporto(mezzoTrasporto);
            }

            operatoreSave = panjeaDAO.save(operatore);
        } catch (Exception e) {
            LOGGER.error("--> errore durante il salvataggio dell'operatore", e);
            throw new GenericException("errore durante il salvataggio dell'operatore", e);
        }

        LOGGER.debug("--> Exit salva");
        return operatoreSave;
    }

    @Override
    public void sostituisciOperatore(Integer idOperatoreDaSostituire, Integer idOperatore, boolean sostituisciTecnico,
            boolean sostituisciCaricatore) {

        Operatore operatoreDaSostituire = caricaById(idOperatoreDaSostituire);
        Operatore operatore = caricaById(idOperatore);

        if (sostituisciTecnico && operatore.isTecnico()) {
            StringBuilder sb = new StringBuilder();
            sb.append(
                    "update Installazione set datiInstallazione.tecnico = :paramTecnico where datiInstallazione.tecnico = :paramTecnicoDaSostituire ");
            Query query = panjeaDAO.prepareQuery(sb.toString());
            query.setParameter("paramTecnico", operatore);
            query.setParameter("paramTecnicoDaSostituire", operatoreDaSostituire);
            try {
                panjeaDAO.executeQuery(query);
            } catch (DAOException e) {
                LOGGER.error("--> errore durante il cambiamento del tecnico sull'installazione.", e);
                throw new GenericException("errore durante il cambiamento del tecnico sull'installazione.", e);
            }
        }

        if (sostituisciCaricatore && operatore.isCaricatore()) {
            StringBuilder sb = new StringBuilder();
            sb.append(
                    "update Installazione set datiInstallazione.caricatore = :paramCaricatore where datiInstallazione.caricatore = :paramCaricatoreDaSostituire ");
            Query query = panjeaDAO.prepareQuery(sb.toString());
            query.setParameter("paramCaricatore", operatore);
            query.setParameter("paramCaricatoreDaSostituire", operatoreDaSostituire);
            try {
                panjeaDAO.executeQuery(query);
            } catch (DAOException e) {
                LOGGER.error("--> errore durante il cambiamento del caricatore sull'installazione.", e);
                throw new GenericException("errore durante il cambiamento del caricatore sull'installazione.", e);
            }
        }

        // se non ho il mezzo di trasporto sposto quello presente sull'operatore da sostituire
        if (operatore.getMezzoTrasporto() == null || operatore.getMezzoTrasporto().isNew()) {
            operatore.setMezzoTrasporto(operatoreDaSostituire.getMezzoTrasporto());
            salva(operatore);

            operatoreDaSostituire.setMezzoTrasporto(null);
            salva(operatoreDaSostituire);
        }
    }

}