package it.eurotn.panjea.sicurezza.service;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Objects;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.jboss.annotation.ejb.RemoteBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.dao.exception.VincoloException;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.panjea.sicurezza.domain.DatiMail;
import it.eurotn.panjea.sicurezza.domain.Permesso;
import it.eurotn.panjea.sicurezza.domain.Ruolo;
import it.eurotn.panjea.sicurezza.domain.Utente;
import it.eurotn.panjea.sicurezza.manager.interfaces.PanjeaCryptoManager;
import it.eurotn.panjea.sicurezza.service.exception.SicurezzaServiceException;
import it.eurotn.panjea.sicurezza.service.interfaces.SicurezzaService;
import it.eurotn.security.JecPrincipal;
import it.eurotn.util.PanjeaEJBUtil;

/**
 *
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
@Stateless(name = "Panjea.SicurezzaService")
@SecurityDomain("PanjeaLoginModule")
@RemoteBinding(jndiBinding = "Panjea.SicurezzaService")
public class SicurezzaServiceBean implements SicurezzaService {

    private static Logger logger = Logger.getLogger(SicurezzaServiceBean.class);

    @Resource
    private SessionContext sessionContext;

    @EJB
    private PanjeaCryptoManager panjeaCryptoManager;
    /**
     * @uml.property name="panjeaDAO"
     * @uml.associationEnd
     */
    @EJB
    private PanjeaDAO panjeaDAO;

    @Override
    @RolesAllowed("gestionePermessi")
    public void associaPermessoARuolo(Ruolo ruolo, Permesso permesso) throws SicurezzaServiceException {
        logger.debug("--> Enter associaPermessoARuolo");
        try {
            ruolo = panjeaDAO.load(Ruolo.class, ruolo.getId());
        } catch (ObjectNotFoundException e) {
            logger.error("--> errore impossibile recuperare Ruolo", e);
            throw new SicurezzaServiceException(e);
        }
        ruolo.getPermessi().add(permesso);
        try {
            panjeaDAO.save(ruolo);
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }
        logger.debug("--> Exit associaPermessoARuolo");

    }

    @Override
    @RolesAllowed("gestionePermessi")
    public void associaRuoloAUtente(Utente utente, Ruolo ruolo) throws SicurezzaServiceException {
        logger.debug("--> Enter associaRuoloAUtente");
        try {
            utente = panjeaDAO.load(Utente.class, utente.getId());
        } catch (ObjectNotFoundException e) {
            logger.error("--> errore, impossibile salvare Utente ", e);
            throw new SicurezzaServiceException(e);
        }
        utente.getRuoli().add(ruolo);
        try {
            panjeaDAO.save(utente);
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }
        logger.debug("--> Exit associaRuoloAUtente");
    }

    @Override
    @RolesAllowed("accessoUtenti")
    public void cambiaPasswordUtenteLoggato(String oldPassword, String newPassword) throws SicurezzaServiceException {

        Utente utenteLoggato = null;
        try {
            utenteLoggato = caricaUtente(getJecPrincipal().getUserName());

            // controllo che la vecchia password corrisponda
            String oldPasswordEnc = panjeaCryptoManager.encrypt(oldPassword);
            String newPasswordEnc = panjeaCryptoManager.encrypt(utenteLoggato.getPassword());
            if (!Objects.equals(oldPasswordEnc, newPasswordEnc)) {
                throw new SicurezzaServiceException("La password dell'utente non corrisponde a quella inserita.");
            }

            utenteLoggato.setPassword(panjeaCryptoManager.encrypt(newPassword));
            panjeaDAO.save(utenteLoggato);

            flushCacheJBoss();
        } catch (Exception e) {
            logger.error("--> errore durante il salvataggio della nuova password per l'utente loggato", e);
            throw new RuntimeException("errore durante il salvataggio della nuova password per l'utente loggato", e);
        }
    }

    @Override
    public void cancellaDatiMail(DatiMail datiMail) {
        logger.debug("--> Enter cancellaDatiMail");

        try {
            panjeaDAO.delete(datiMail);
        } catch (Exception e) {
            logger.error("--> errore durante la cancellazione dei dati mail", e);
            throw new RuntimeException("errore durante la cancellazione dei dati mail", e);
        }

        logger.debug("--> Exit cancellaDatiMail");
    }

    @Override
    @RolesAllowed("gestionePermessi")
    public void cancellaRuolo(Ruolo ruolo) {
        logger.debug("--> Enter cancellaRuolo");
        try {
            panjeaDAO.delete(ruolo);
        } catch (VincoloException e) {
            logger.error("--> errore, impossibile eliminare Ruolo ", e);
            throw new RuntimeException(e);
        } catch (DAOException e) {
            logger.error("--> errore, impossibile eliminare Ruolo ", e);
            throw new RuntimeException(e);
        }
        logger.debug("--> Exit cancellaRuolo");

    }

    @Override
    @RolesAllowed("gestionePermessi")
    public void cancellaUtente(Utente utente) {
        try {
            panjeaDAO.delete(utente);
        } catch (VincoloException e) {
            logger.error("--> errore, impossibile cancellare Utente ", e);
            throw new RuntimeException(e);
        } catch (DAOException e) {
            logger.error("--> errore, impossibile cancellare Utente ", e);
            throw new RuntimeException(e);
        }

    }

    @Override
    public DatiMail caricaDatiMail(DatiMail datiMail) {
        logger.debug("--> Enter caricaDatiMail");

        DatiMail datiMailCaricati = null;
        try {
            datiMailCaricati = panjeaDAO.load(DatiMail.class, datiMail.getId());
            if (datiMailCaricati != null) {
                ((Session) panjeaDAO.getEntityManager().getDelegate()).evict(datiMailCaricati);
                datiMailCaricati.setPasswordMail(panjeaCryptoManager.decrypt(datiMailCaricati.getPasswordMail()));
            }
        } catch (Exception e) {
            logger.error("--> errore durante il caricamento dei dati mail", e);
            throw new RuntimeException("errore durante il caricamento dei dati mail", e);
        }

        logger.debug("--> Exit caricaDatiMail");
        return datiMailCaricati;
    }

    @Override
    public List<DatiMail> caricaDatiMail(Integer idUtente) throws SicurezzaServiceException {
        Utente utente = caricaUtente(idUtente);
        List<DatiMail> listDatiMail = utente.getDatiMail();
        for (DatiMail datiMail : listDatiMail) {
            if (datiMail != null) {
                ((Session) panjeaDAO.getEntityManager().getDelegate()).evict(datiMail);
                datiMail.setPasswordMail(panjeaCryptoManager.decrypt(datiMail.getPasswordMail()));
            }
        }

        return listDatiMail;
    }

    @Override
    @SuppressWarnings("unchecked")
    @RolesAllowed("gestionePermessi")
    public List<Permesso> caricaPermessi() throws SicurezzaServiceException {
        logger.debug("--> Enter caricaPermessi");
        List<Permesso> listaPermessi;
        Query query = panjeaDAO.prepareNamedQuery("Permesso.caricaAll");
        try {
            listaPermessi = panjeaDAO.getResultList(query);
        } catch (DAOException e) {
            logger.error("--> Errore durante il caricamento dei permessi.");
            throw new SicurezzaServiceException(e);
        }
        logger.debug("--> Caricati " + listaPermessi.size() + " permessi.");
        logger.debug("--> Exit caricaPermessi");
        return listaPermessi;
    }

    @Override
    @SuppressWarnings("unchecked")
    @RolesAllowed("gestionePermessi")
    public List<Permesso> caricaPermessi(String modulo) throws SicurezzaServiceException {
        logger.debug("--> Enter caricaPermessi");
        List<Permesso> listaPermessi;
        Query query = panjeaDAO.prepareNamedQuery("Permesso.caricaByModulo");
        try {
            listaPermessi = panjeaDAO.getResultList(query);
        } catch (DAOException e) {
            logger.error("--> Errore durante il caricamento dei permessi.");
            throw new SicurezzaServiceException(e);
        }
        logger.debug("--> Caricati " + listaPermessi.size() + " permessi.");
        logger.debug("--> Exit caricaPermessi");
        return listaPermessi;
    }

    @Override
    @SuppressWarnings("unchecked")
    @RolesAllowed("gestionePermessi")
    public List<Ruolo> caricaRuoli() throws RemoteException, SicurezzaServiceException {
        logger.debug("--> Enter caricaRuoli");
        List<Ruolo> listaRuoli;
        Query query = panjeaDAO.prepareNamedQuery("Ruolo.caricaAll");
        try {
            listaRuoli = panjeaDAO.getResultList(query);
        } catch (DAOException e) {
            logger.error("--> Errore durante il caricamento dei ruoli .");
            throw new SicurezzaServiceException(e);
        }
        logger.debug("--> Exit caricaRuoli");
        return listaRuoli;
    }

    @Override
    @SuppressWarnings("unchecked")
    @RolesAllowed("gestionePermessi")
    public List<Ruolo> caricaRuoliAziendaCorrente() throws SicurezzaServiceException {
        logger.debug("---> Enter caricaRuoliAziendaCorrente");
        List<Ruolo> listaRuoli;
        Query query = panjeaDAO.prepareNamedQuery("Ruolo.caricaByAzienda");
        query.setParameter("paramCodiceAzienda", getJecPrincipal().getCodiceAzienda());
        try {
            listaRuoli = panjeaDAO.getResultList(query);
        } catch (DAOException e) {
            logger.error("--> Errore durante il caricamento dei ruoli .");
            throw new SicurezzaServiceException(e);
        }
        logger.debug("--> Exit caricaRuoliAziendaCorrente");
        return listaRuoli;
    }

    @Override
    @RolesAllowed("gestionePermessi")
    public Ruolo caricaRuolo(Integer idRuolo) throws SicurezzaServiceException, RemoteException {
        logger.debug("--> Enter caricaRuolo");
        Ruolo ruolo;
        try {
            ruolo = panjeaDAO.load(Ruolo.class, idRuolo);
            ruolo.getPermessi().size();
        } catch (ObjectNotFoundException e) {
            logger.error("--> errore, impossibile caricare Ruolo ", e);
            throw new SicurezzaServiceException(e);
        }
        logger.debug("--> Exit caricaRuolo");
        return ruolo;
    }

    @Override
    public Utente caricaUtente(Integer idUtente) throws SicurezzaServiceException {
        logger.debug("--> Enter caricaUtente");
        Utente utente;
        try {
            utente = panjeaDAO.load(Utente.class, idUtente);
            utente.getRuoli().size();
            utente.getDatiMail().size();
            ((Session) panjeaDAO.getEntityManager().getDelegate()).evict(utente);
            utente.setPassword(panjeaCryptoManager.decrypt(utente.getPassword()));
            for (DatiMail datiMail : utente.getDatiMail()) {
                if (datiMail != null) {
                    ((Session) panjeaDAO.getEntityManager().getDelegate()).evict(datiMail);
                    datiMail.setPasswordMail(panjeaCryptoManager.decrypt(datiMail.getPasswordMail()));
                }
            }
        } catch (ObjectNotFoundException e) {
            logger.error("--> errore, impossibile caricare Utente ", e);
            throw new SicurezzaServiceException(e);
        }
        logger.debug("--> Exit caricaUtente");
        return utente;
    }

    @Override
    public Utente caricaUtente(String userName) throws SicurezzaServiceException {
        logger.debug("--> Enter caricaUtente");
        Query query = panjeaDAO.prepareNamedQuery("Utente.caricaByUserName");
        query.setParameter("paramUserName", userName);
        Utente utente = null;
        try {
            utente = (Utente) panjeaDAO.getSingleResult(query);
            utente.getRuoli().size();
            utente.getDatiMail().size();
            ((Session) panjeaDAO.getEntityManager().getDelegate()).evict(utente);
            utente.setPassword(panjeaCryptoManager.decrypt(utente.getPassword()));
            for (DatiMail datiMail : utente.getDatiMail()) {
                if (datiMail != null) {
                    ((Session) panjeaDAO.getEntityManager().getDelegate()).evict(datiMail);
                    datiMail.setPasswordMail(panjeaCryptoManager.decrypt(datiMail.getPasswordMail()));
                }
            }
        } catch (DAOException e) {
            throw new SicurezzaServiceException(e);
        }
        return utente;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Utente> caricaUtenti(String fieldSearch, String valueSearch)
            throws RemoteException, SicurezzaServiceException {
        List<Utente> listaUtenti;
        StringBuilder sb = new StringBuilder("select distinct u from Utente u left join fetch u.datiMail ");
        if (valueSearch != null) {
            sb.append(" where  u.");
            sb.append(fieldSearch);
            sb.append(" like ");
            sb.append(PanjeaEJBUtil.addQuote(valueSearch));
        }
        sb.append(" order by u.").append(fieldSearch);
        Query query = panjeaDAO.prepareQuery(sb.toString());
        try {
            listaUtenti = panjeaDAO.getResultList(query);
            for (Utente utente : listaUtenti) {
                ((Session) panjeaDAO.getEntityManager().getDelegate()).evict(utente);
                utente.setPassword(panjeaCryptoManager.decrypt(utente.getPassword()));
                for (DatiMail datiMail : utente.getDatiMail()) {
                    if (datiMail != null) {
                        ((Session) panjeaDAO.getEntityManager().getDelegate()).evict(datiMail);
                        datiMail.setPasswordMail(panjeaCryptoManager.decrypt(datiMail.getPasswordMail()));
                    }
                }
            }
        } catch (DAOException e) {
            logger.error("--> Errore durante il caricamento degli utenti .");
            throw new SicurezzaServiceException(e);
        }
        logger.debug("--> Exit caricaUtenti");
        return listaUtenti;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Utente> caricaUtentiPos() {
        logger.debug("--> Enter caricaUtentiPos");
        Query query = panjeaDAO.prepareNamedQuery("Utente.caricaUtentiPos");
        List<Utente> utenti;
        try {
            utenti = panjeaDAO.getResultList(query);
        } catch (Exception e) {
            logger.error("--> errore durante il caricamento degli utenti pos.", e);
            throw new RuntimeException("errore durante il caricamento degli utenti pos.", e);
        }
        return utenti;
    }

    @Override
    @RolesAllowed("gestionePermessi")
    public void flushCache() throws RemoteException {
        flushCacheJBoss();
    }

    /**
     * Esegue il flush della security cache di JBoss.
     */
    private void flushCacheJBoss() {
        try {
            String domain = "PanjeaLoginModule";
            ObjectName jaasMgr = new ObjectName("jboss.security:service=JaasSecurityManager");
            Object[] params = { domain };
            String[] signature = { "java.lang.String" };
            MBeanServer server = (MBeanServer) MBeanServerFactory.findMBeanServer(null).get(0);
            server.invoke(jaasMgr, "flushAuthenticationCache", params, signature);
        } catch (Exception e) {
            logger.error("---> Errore nel flush della cache di JBoss", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    @RolesAllowed("accessoUtenti")
    public String getCredential() throws SicurezzaServiceException, RemoteException {
        logger.debug("--> Enter getCredential");
        Query query = panjeaDAO.prepareNamedQuery("Utente.caricaByUserName");
        query.setParameter("paramUserName", getJecPrincipal().getName());
        Utente utente;
        try {
            utente = (Utente) panjeaDAO.getSingleResult(query);
            ((Session) panjeaDAO.getEntityManager().getDelegate()).evict(utente);
            utente.setPassword(panjeaCryptoManager.decrypt(utente.getPassword()));
        } catch (DAOException e) {
            logger.error("--> errore, impossibile caricare Utente ", e);
            throw new SicurezzaServiceException(e);
        }
        logger.debug("--> Exit getCredential");
        return utente.getPassword();
    }

    /**
     * Restituisce il principal loggato.
     *
     * @return principal
     */
    private JecPrincipal getJecPrincipal() {
        return (JecPrincipal) sessionContext.getCallerPrincipal();
    }

    @Override
    @RolesAllowed("accessoUtenti")
    public JecPrincipal login() throws RemoteException {
        return (JecPrincipal) sessionContext.getCallerPrincipal();
    }

    private String pulisciFirmaMail(String firma) {
        int idxBody = firma.indexOf("<body>");
        int idxFineBody = firma.indexOf("</body>");

        if (idxBody != -1 && idxFineBody != -1) {
            firma = firma.substring(idxBody + 6, idxFineBody);
        }

        return firma;
    }

    @Override
    @RolesAllowed("gestionePermessi")
    public void rimuoviPermessoDaRuolo(Ruolo ruolo, Permesso permesso) throws SicurezzaServiceException {
        logger.debug("--> Enter rimuoviPermessoDaRuolo");
        try {
            ruolo = panjeaDAO.load(Ruolo.class, ruolo.getId());
        } catch (ObjectNotFoundException e) {
            logger.error("--> errore, impossibile caricare Ruolo ", e);
            throw new SicurezzaServiceException(e);
        }
        ruolo.getPermessi().remove(permesso);
        try {
            panjeaDAO.save(ruolo);
        } catch (DAOException e) {
            logger.error("--> errore, impossibile salvare Ruolo ", e);
            throw new RuntimeException(e);
        }
        logger.debug("--> Exit rimuoviRuoloDaUtente");

    }

    @Override
    @RolesAllowed("gestionePermessi")
    public void rimuoviRuoloDaUtente(Utente utente, Ruolo ruolo) throws SicurezzaServiceException {
        logger.debug("--> Enter rimuoviRuoloDaUtente");
        try {
            utente = panjeaDAO.load(Utente.class, utente.getId());
        } catch (ObjectNotFoundException e) {
            logger.error("--> errore, impossibile caricare Utente ", e);
            throw new SicurezzaServiceException(e);
        }
        utente.getRuoli().remove(ruolo);
        try {
            panjeaDAO.save(utente);
        } catch (DAOException e) {
            logger.error("--> errore, impossibile salvare Utente ", e);
            throw new RuntimeException(e);
        }
        logger.debug("--> Exit rimuoviRuoloDaUtente");
    }

    @Override
    public DatiMail salvaDatiMail(DatiMail datiMail) {
        logger.debug("--> Enter salvaDatiMail");
        DatiMail datiMailSave;
        try {
            datiMail.setFirma(pulisciFirmaMail(datiMail.getFirma()));
            datiMail.setPasswordMail(panjeaCryptoManager.encrypt(datiMail.getPasswordMail()));

            datiMailSave = panjeaDAO.save(datiMail);

            // se i dati mail salvati sono predefiniti tolgo il flag agli altri dati mail
            // dell'utente
            if (datiMailSave.isPredefinito()) {
                Query query = panjeaDAO.prepareQuery(
                        "update DatiMail set predefinito = false where utente = :utente and id != :paramID ");
                query.setParameter("utente", datiMailSave.getUtente());
                query.setParameter("paramID", datiMailSave.getId());
                query.executeUpdate();
            }

        } catch (Exception e) {
            logger.error("--> errore durante il salvataggio dei dati mail", e);
            throw new RuntimeException("errore durante il salvataggio dei dati mail", e);
        }
        logger.debug("--> Exit salvaDatiMail");
        return datiMailSave;
    }

    @Override
    @RolesAllowed("gestionePermessi")
    public Ruolo salvaRuolo(Ruolo ruolo) {
        logger.debug("--> Enter salvaRuolo");
        Ruolo ruoloSave;
        try {
            ruoloSave = panjeaDAO.save(ruolo);
        } catch (DAOException e) {
            logger.error("--> errore, impossibile salvare Ruolo ", e);
            throw new RuntimeException(e);
        }
        logger.debug("--> Exit salvaRuolo");
        return ruoloSave;
    }

    @Override
    @RolesAllowed("gestionePermessi")
    public Ruolo salvaRuolo(Ruolo ruolo, List<Permesso> permessiAggiunti, List<Permesso> permessiRimossi) {
        logger.debug("--> Enter salvaRuolo");
        ruolo.getPermessi().addAll(permessiAggiunti);
        ruolo.getPermessi().removeAll(permessiRimossi);

        Ruolo ruoloSave;
        try {
            ruoloSave = panjeaDAO.save(ruolo);
        } catch (DAOException e) {
            logger.error("--> errore, impossibile salvare Ruolo ", e);
            throw new RuntimeException(e);
        }
        logger.debug("--> Exit salvaRuolo");
        return ruoloSave;
    }

    @Override
    @RolesAllowed("gestionePermessi")
    public Utente salvaUtente(Utente utente) {
        logger.debug("--> Enter salvaUtente");

        Utente utenteSave;
        try {
            utente.setPassword(panjeaCryptoManager.encrypt(utente.getPassword()));
            utenteSave = panjeaDAO.save(utente);
        } catch (DAOException e) {
            logger.error("--> errore, impossibile salvare Utente ", e);
            throw new RuntimeException(e);
        }
        logger.debug("--> Exit salvaUtente");
        return utenteSave;
    }

    @Override
    @RolesAllowed("gestionePermessi")
    public Utente salvaUtente(Utente utente, List<Ruolo> ruoliAggiunti, List<Ruolo> ruoliRimossi) {
        logger.debug("--> Enter salvaUtente");
        utente.getRuoli().addAll(ruoliAggiunti);
        utente.getRuoli().removeAll(ruoliRimossi);
        Utente utenteSave;
        try {
            utente.setPassword(panjeaCryptoManager.encrypt(utente.getPassword()));
            utenteSave = panjeaDAO.save(utente);
        } catch (DAOException e) {
            logger.error("--> errore, impossibile salvare Utente ", e);
            throw new RuntimeException(e);
        }
        logger.debug("--> Exit salvaUtente");
        return utenteSave;
    }

}
