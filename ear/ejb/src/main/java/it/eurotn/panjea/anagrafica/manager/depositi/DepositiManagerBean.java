package it.eurotn.panjea.anagrafica.manager.depositi;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.jboss.annotation.IgnoreDependency;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.anagrafica.domain.Deposito;
import it.eurotn.panjea.anagrafica.domain.SedeAzienda;
import it.eurotn.panjea.anagrafica.domain.lite.AziendaLite;
import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.manager.depositi.interfaces.DepositiManager;
import it.eurotn.panjea.anagrafica.manager.interfaces.AziendeManager;
import it.eurotn.panjea.anagrafica.service.exception.AnagraficaServiceException;
import it.eurotn.panjea.exception.GenericException;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.security.JecPrincipal;
import it.eurotn.util.PanjeaEJBUtil;

@Stateless(name = "Panjea.DepositiManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.DepositiManager")
public class DepositiManagerBean implements DepositiManager {

    private static final Logger logger = Logger.getLogger(DepositiManagerBean.class);

    @Resource
    private SessionContext context;

    @EJB
    private PanjeaDAO panjeaDAO;

    @EJB
    @IgnoreDependency
    private AziendeManager aziendeManager;

    @Override
    public void cancellaDeposito(Deposito deposito) throws DAOException {
        logger.debug("--> Enter cancellaDeposito");
        panjeaDAO.delete(deposito);
        logger.debug("--> Exit cancellaDeposito");
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Deposito> caricaDepositi() {
        logger.debug("--> Enter caricaDepositi ");
        Query query = panjeaDAO.prepareQuery("select d from Deposito d ");

        List<Deposito> depositi;
        try {
            depositi = panjeaDAO.getResultList(query);
        } catch (DAOException e) {
            throw new GenericException(e);
        }
        logger.debug("--> Exit caricaDepositi");
        return depositi;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Deposito> caricaDepositi(EntitaLite entita) {
        logger.debug("--> Enter caricaDepositi ");
        Query query = panjeaDAO.prepareQuery("select d from Deposito d where d.entita = :paramEntita");
        query.setParameter("paramEntita", entita);

        List<Deposito> depositi;
        try {
            depositi = panjeaDAO.getResultList(query);
        } catch (DAOException e) {
            throw new GenericException(e);
        }
        logger.debug("--> Exit caricaDepositi");
        return depositi;
    }

    @Override
    public List<Deposito> caricaDepositi(SedeAzienda sedeAzienda) throws AnagraficaServiceException {
        logger.debug("--> Enter caricaDepositi per la sede id " + sedeAzienda.getId());
        if (sedeAzienda.isNew()) {
            logger.error("--> Impossibile caricare i depositi. sedeVO.Id nullo.");
            throw new AnagraficaServiceException("Impossibile caricare i depositi. ID Sede nullo.");
        }

        try {
            SedeAzienda sedeAzienda2 = panjeaDAO.load(SedeAzienda.class, sedeAzienda.getId());
            sedeAzienda2.getDepositi().size();
            List<Deposito> depositi = sedeAzienda2.getDepositi();
            logger.debug("--> Exit caricaDepositi");
            return depositi;
        } catch (ObjectNotFoundException e) {
            logger.error("--> errore, impossibile caricare sedeAzienda e i suoi depositi", e);
            throw new AnagraficaServiceException(
                    "Errore nel caricare i depositi per la sede con id " + sedeAzienda.getId(), e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Deposito> caricaDepositi(String fieldSearch, String valueSearch) {
        logger.debug("--> Enter caricaDepositi ");
        List<Deposito> depositi;

        StringBuilder sb = new StringBuilder(
                "select dep from Deposito dep  where dep.codiceAzienda=:paramCodiceAzienda ");
        if (valueSearch != null) {
            sb.append(" and ").append(fieldSearch).append(" like ").append(PanjeaEJBUtil.addQuote(valueSearch));
        }
        sb.append(" order by ");
        sb.append(fieldSearch);
        Query query = panjeaDAO.prepareQuery(sb.toString());
        query.setParameter("paramCodiceAzienda", getCodiceAzienda());
        try {
            depositi = panjeaDAO.getResultList(query);
        } catch (DAOException e) {
            throw new GenericException(e);
        }
        logger.debug("--> Exit caricaDepositi");
        return depositi;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<DepositoLite> caricaDepositiAzienda() {
        logger.debug("--> Enter ricercaDepositi");
        List<DepositoLite> result = new ArrayList<>();
        try {
            Query query = panjeaDAO.prepareNamedQuery("DepositoLite.caricaDepositiAzienda");
            query.setParameter("paramCodiceAzienda", getCodiceAzienda());
            result = panjeaDAO.getResultList(query);
        } catch (Exception e) {
            throw new GenericException("errore nel caricare i depositi dell'azienda ", e);
        }
        logger.debug("--> Exit ricercaDepositi");
        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<DepositoLite> caricaDepositiAzienda(String fieldSearch, String valueSearch,
            boolean soloConMezziTrasporto) {
        logger.debug("--> Enter ricercaDepositi");
        List<DepositoLite> result;

        String joinMezziTrasporto = " left join ";
        if (soloConMezziTrasporto) {
            joinMezziTrasporto = " inner join ";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("select dep from DepositoLite dep ");
        sb.append(joinMezziTrasporto);
        sb.append("fetch dep.mezziTrasporto  where dep.codiceAzienda=:paramCodiceAzienda ");
        if (valueSearch != null) {
            sb.append(" and dep.").append(fieldSearch).append(" like ").append(PanjeaEJBUtil.addQuote(valueSearch));
        }
        sb.append(" order by ");
        sb.append("dep." + fieldSearch);
        Query query = panjeaDAO.prepareQuery(sb.toString());
        query.setParameter("paramCodiceAzienda", getCodiceAzienda());
        try {
            result = panjeaDAO.getResultList(query);
        } catch (Exception e) {
            throw new GenericException("errore nel caricare i depositi dell'azienda ", e);
        }
        logger.debug("--> Exit ricercaDepositi");
        return result;
    }

    @Override
    public Deposito caricaDeposito(Integer idDeposito) {
        logger.debug("--> Enter caricaDeposito");
        try {
            Deposito deposito = panjeaDAO.load(Deposito.class, idDeposito);
            logger.debug("---> Exit caricaDeposito");
            return deposito;
        } catch (ObjectNotFoundException e) {
            logger.error("--> errore caricare deposito con id " + idDeposito, e);
            throw new GenericException("Errore nel caricare il deposito", e);
        }
    }

    @Override
    public Deposito caricaDepositoPrincipale() {
        logger.debug("--> Enter caricaDepositoPrincipale");

        Deposito deposito = null;
        try {
            Query query = panjeaDAO.prepareNamedQuery("Deposito.caricaDepositoPrincipaleAzienda");

            AziendaLite aziendaLite = aziendeManager.caricaAzienda(getCodiceAzienda());
            query.setParameter("paramIdAzienda", aziendaLite.getId());
            deposito = (Deposito) panjeaDAO.getSingleResult(query);
        } catch (Exception e) {
            throw new GenericException(e);
        }
        logger.debug("--> Exit caricaDepositoPrincipale");
        return deposito;
    }

    /**
     *
     * @return codiceAzienda
     */
    private String getCodiceAzienda() {
        return ((JecPrincipal) context.getCallerPrincipal()).getCodiceAzienda();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Deposito> ricercaDepositi(ParametriRicercaDepositi parametri) {
        logger.debug("--> Enter ricercaDepositi");

        StringBuilder sb = new StringBuilder();
        sb.append("select dep.id as id, ");
        sb.append("          dep.codice as codice, ");
        sb.append("          dep.descrizione as descrizione, ");
        sb.append("          dep.indirizzo as indirizzo, ");
        sb.append("          dep.attivo as attivo, ");
        sb.append("          tipoDep as tipoDeposito, ");
        sb.append("          loc.descrizione as datiGeografici$localita$descrizione, ");
        sb.append("          sedeAnag.descrizione as sedeDeposito$sede$descrizione ");
        sb.append("from Deposito dep inner join dep.tipoDeposito tipoDep ");
        sb.append("                              left join dep.datiGeografici.localita loc ");
        sb.append("                              inner join dep.sedeDeposito sedeDep ");
        sb.append("                              inner join sedeDep.sede sedeAnag ");
        sb.append("where 1=1 ");

        if (!parametri.isLoadDepositiInstallazione()) {
            sb.append(" and tipoDep.codice <> 'DI' ");
        }
        if (parametri.getIdEntita() != null) {
            sb.append(" and dep.entita.id = ");
            sb.append(parametri.getIdEntita());
        }
        if (parametri.getIdSedeAzienda() != null) {
            sb.append(" and dep.sedeDeposito.id = ");
            sb.append(parametri.getIdSedeAzienda());
        }

        Query query = panjeaDAO.prepareQuery(sb.toString(), Deposito.class, null);

        List<Deposito> depositi;
        try {
            depositi = panjeaDAO.getResultList(query);
        } catch (Exception e) {
            logger.error("--> errore durante la ricerca dei depositi.", e);
            throw new GenericException("errore durante la ricerca dei depositi.", e);
        }

        logger.debug("--> Exit ricercaDepositi");
        return depositi;
    }

    @Override
    public Deposito salvaDeposito(Deposito deposito) {
        logger.debug("--> Enter salvaDeposito");
        Deposito depositoSave;
        try {
            if (deposito.getCodiceAzienda() == null) {
                deposito.setCodiceAzienda(getCodiceAzienda());
            }
            depositoSave = panjeaDAO.save(deposito);
        } catch (DAOException e) {
            throw new GenericException(e);
        }
        logger.debug("--> Exit salvaDeposito");
        return depositoSave;
    }
}
