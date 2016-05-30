package it.eurotn.panjea.magazzino.manager.manutenzionelistino;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.log4j.Logger;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.ejb.HibernateQuery;
import org.hibernate.transform.Transformers;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.exception.GenericException;
import it.eurotn.panjea.magazzino.domain.Listino;
import it.eurotn.panjea.magazzino.domain.Listino.ETipoListino;
import it.eurotn.panjea.magazzino.domain.VersioneListino;
import it.eurotn.panjea.magazzino.exception.ArticoliDuplicatiManutenzioneListinoException;
import it.eurotn.panjea.magazzino.exception.ListinoManutenzioneNonValidoException;
import it.eurotn.panjea.magazzino.manager.manutenzionelistino.interfaces.ManutenzioneListinoManager;
import it.eurotn.panjea.magazzino.manager.manutenzionelistino.querybuilder.DecimaliQueryBuilder;
import it.eurotn.panjea.magazzino.manager.manutenzionelistino.querybuilder.ManutenzioneListinoQueryBuilder;
import it.eurotn.panjea.magazzino.manager.manutenzionelistino.querybuilder.ProvenienzaPrezzoQueryBuilder;
import it.eurotn.panjea.magazzino.manager.manutenzionelistino.updater.interfaces.ListinoManutenzioneUpdater;
import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriAggiornaManutenzioneListino;
import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriRicercaManutenzioneListino;
import it.eurotn.panjea.magazzino.util.parametriricerca.RigaManutenzioneListino;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.panjea.util.SqlExecuter;
import it.eurotn.security.JecPrincipal;

/**
 * @author Leonardo
 */
@Stateless(name = "Panjea.ManutenzioneListinoManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.ManutenzioneListinoManager")
public class ManutenzioneListinoManagerBean implements ManutenzioneListinoManager {

    private static final Logger LOGGER = Logger.getLogger(ManutenzioneListinoManagerBean.class);

    /**
     * @uml.property name="panjeaDAO"
     * @uml.associationEnd
     */
    @EJB
    private PanjeaDAO panjeaDAO;
    @Resource
    private SessionContext sessionContext;

    @EJB(beanName = "ListinoNormaleUpdaterBean")
    private ListinoManutenzioneUpdater listinoNormaleUpdater;

    @EJB(beanName = "ListinoScaglioniUpdaterBean")
    private ListinoManutenzioneUpdater listinoScaglioniUpdater;

    @SuppressWarnings("unchecked")
    @Override
    public void aggiornaListinoDaManutenzione(ParametriAggiornaManutenzioneListino parametriAggiornaManutenzioneListino)
            throws ArticoliDuplicatiManutenzioneListinoException {
        LOGGER.debug("--> Enter aggiornaListinoDaManutenzione");
        VersioneListino versioneListino = parametriAggiornaManutenzioneListino.getVersioneListino();

        // verifico se ci sono articoli inseriti più di una volta. In questo caso restituisco un
        // errore
        StringBuilder sb = new StringBuilder();
        sb.append("select CONCAT(art.codice,' ',art.descrizioneLinguaAziendale),count(art.id) ");
        sb.append("from maga_riga_manutenzione_listino as m ");
        sb.append("left join maga_righe_listini as l on m.articolo_id=l.articolo_id and l.versioneListino_id= ");
        sb.append(versioneListino.getId());
        sb.append(" ");
        sb.append("inner join maga_articoli as art on m.articolo_id=art.id ");
        sb.append("where m.userManutenzione='");
        sb.append(getPrincipal().getName());
        sb.append("'");
        sb.append("group by art.id, m.quantita ");
        sb.append("having count(art.id) > 1 ");
        org.hibernate.ejb.QueryImpl queryImpl = (org.hibernate.ejb.QueryImpl) panjeaDAO.getEntityManager()
                .createNativeQuery(sb.toString());
        SQLQuery sqlQuery = (SQLQuery) queryImpl.getHibernateQuery();

        List<Object[]> result = new ArrayList<Object[]>();
        result = sqlQuery.list();
        if (!result.isEmpty()) {
            Map<String, Integer> mapResult = new HashMap<String, Integer>();
            for (Object[] objects : result) {
                mapResult.put((String) objects[0], ((BigInteger) objects[1]).intValue());
            }
            throw new ArticoliDuplicatiManutenzioneListinoException(mapResult);
        }

        ListinoManutenzioneUpdater listinoManutenzioneUpdater = getListinoManutenzioneUpdater(versioneListino);

        // aggiorno le righe esistenti
        listinoManutenzioneUpdater.aggiornaRigheListinoDaManutenzione(versioneListino);

        // inserisco le righe con gli articoli non presenti nella versione listino
        listinoManutenzioneUpdater.aggiungiRigheListinoMancantiDaManutenzione(versioneListino);

        // cancella tutte le righe inserite dall'utente corrente
        cancellaRigheManutenzioneListino(null);
        LOGGER.debug("--> Exit aggiornaListinoDaManutenzione");
    }

    @Override
    public void cancellaRigheManutenzioneListino(List<RigaManutenzioneListino> righeManutenzioneListino) {
        if (righeManutenzioneListino == null) {
            String sqlQuery = new String("delete from maga_riga_manutenzione_listino where userManutenzione='"
                    + getPrincipal().getName() + "'");
            executeUpdate(sqlQuery);
        } else {
            for (RigaManutenzioneListino rigaManutenzioneListino : righeManutenzioneListino) {
                try {
                    panjeaDAO.delete(rigaManutenzioneListino);
                } catch (Exception e) {
                    LOGGER.error("--> Errore nella cancellazione della rigaManutenzioneListino "
                            + rigaManutenzioneListino.getId(), e);
                    throw new GenericException("Errore nella cancellazione della rigaManutenzioneListino "
                            + rigaManutenzioneListino.getId(), e);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<RigaManutenzioneListino> caricaRigheManutenzioneListino() {
        LOGGER.debug("--> Enter caricaRigheManutenzioneListino");

        StringBuilder sb = new StringBuilder();
        sb.append("select ");
        sb.append("rml.id as id, ");
        sb.append("rml.version as version, ");
        sb.append("rml.codiceAzienda as codiceAzienda, ");
        sb.append("rml.userManutenzione as userManutenzione, ");
        sb.append("rml.descrizione as descrizione, ");
        sb.append("rml.numero as numero, ");
        sb.append("rml.numeroDecimali as numeroDecimali, ");
        sb.append("rml.numeroDecimaliOriginale as numeroDecimaliOriginale, ");
        sb.append("rml.valore as valore, ");
        sb.append("rml.valoreOriginale as valoreOriginale, ");
        sb.append("rml.quantita as quantita, ");
        sb.append("art.id as idArticolo, ");
        sb.append("art.version as versionArticolo, ");
        sb.append("art.codice as codiceArticolo, ");
        sb.append("art.numeroDecimaliQta as numeroDecimaliQta, ");
        sb.append("art.descrizioneLinguaAziendale as descrizioneArticolo, ");
        sb.append("l.id as idListino,");
        sb.append("l.version as versioneListino,");
        sb.append("l.tipoListino as tipoListino ");
        sb.append("from maga_riga_manutenzione_listino as rml ");
        sb.append("inner join maga_articoli as art on rml.articolo_id=art.id ");
        sb.append("left join maga_listini l on l.id = rml.listino_id ");
        sb.append("where rml.userManutenzione='");
        sb.append(getPrincipal().getName());
        sb.append("'");

        org.hibernate.ejb.QueryImpl queryImpl = (org.hibernate.ejb.QueryImpl) panjeaDAO.getEntityManager()
                .createNativeQuery(sb.toString());
        SQLQuery sqlQuery = (SQLQuery) queryImpl.getHibernateQuery();
        sqlQuery.setResultTransformer(Transformers.aliasToBean(RigaManutenzioneListino.class));
        List<RigaManutenzioneListino> righeManutenzioneListino = null;
        try {
            sqlQuery.addScalar("id");
            sqlQuery.addScalar("version");
            sqlQuery.addScalar("codiceAzienda");
            sqlQuery.addScalar("userManutenzione");
            sqlQuery.addScalar("descrizione");
            sqlQuery.addScalar("numero");
            sqlQuery.addScalar("numeroDecimali");
            sqlQuery.addScalar("numeroDecimaliOriginale");
            sqlQuery.addScalar("valore");
            sqlQuery.addScalar("valoreOriginale");
            sqlQuery.addScalar("quantita");
            sqlQuery.addScalar("idArticolo");
            sqlQuery.addScalar("versionArticolo");
            sqlQuery.addScalar("codiceArticolo");
            sqlQuery.addScalar("numeroDecimaliQta");
            sqlQuery.addScalar("descrizioneArticolo");
            sqlQuery.addScalar("idListino");
            sqlQuery.addScalar("versioneListino");
            sqlQuery.addScalar("tipoListino");

            righeManutenzioneListino = sqlQuery.list();
        } catch (Exception e) {
            LOGGER.error("--> errore in caricaRigheManutenzioneListino", e);
            throw new GenericException(e);
        }
        LOGGER.debug("--> Exit caricaRigheManutenzioneListino");
        return righeManutenzioneListino;
    }

    private void checkListinoIsValid(ParametriRicercaManutenzioneListino parametri)
            throws ListinoManutenzioneNonValidoException {

        Query query = panjeaDAO.prepareNamedQuery("RigaManutenzioneListino.caricaListinoScaglioni");
        query.setParameter("userManutenzione", parametri.getUserManutenzione());
        Listino listino = null;
        try {
            listino = (Listino) panjeaDAO.getSingleResult(query);
            // se ho delle righe manutenzione di un listino a scaglione i parametri devono usare
            // quel listino altrimenti
            // sollevo un'eccezione
            if (parametri.getVersioneListino() == null
                    || !listino.equals(parametri.getVersioneListino().getListino())) {
                throw new ListinoManutenzioneNonValidoException(listino);
            }
        } catch (ObjectNotFoundException e) {
            LOGGER.trace("-->nessun listino. Lo creo", e);
            // non c'è nessun listino a scaglioni nelle righe manutenzione dell'utente
            // se sono presenti delle righe manutenzione ( non di listini a scaglione ) e sto
            // provando a inserire un
            // listino a scaglione sollevo un'eccezione
            query = panjeaDAO.prepareNamedQuery("RigaManutenzioneListino.count");
            query.setParameter("userManutenzione", parametri.getUserManutenzione());
            Long righePresenti = (Long) query.getSingleResult();
            if (righePresenti > 0 && parametri.getVersioneListino() != null
                    && parametri.getVersioneListino().getListino().getTipoListino() == ETipoListino.SCAGLIONE) {
                throw new ListinoManutenzioneNonValidoException(null);
            }
        } catch (DAOException e) {
            LOGGER.error("--> errore durante il caricamento del listino delle righe manutenzione.", e);
            throw new GenericException("errore durante il caricamento del listino delle righe manutenzione.", e);
        }
    }

    /**
     * Esegue una query sql di update.
     *
     * @param sqlQuery
     *            la query di cui lanciare l'execute update.
     */
    private void executeUpdate(String sqlQuery) {
        SqlExecuter executer = new SqlExecuter();
        executer.setSql(sqlQuery);
        ((Session) panjeaDAO.getEntityManager().getDelegate()).doWork(executer);
    }

    private ListinoManutenzioneUpdater getListinoManutenzioneUpdater(VersioneListino versioneListino) {
        ListinoManutenzioneUpdater updater = null;
        switch (versioneListino.getListino().getTipoListino()) {
        case NORMALE:
            updater = listinoNormaleUpdater;
            break;
        case SCAGLIONE:
            updater = listinoScaglioniUpdater;
            break;
        default:
            break;
        }
        return updater;
    }

    /**
     *
     * @return codice azienda loggata
     */
    private JecPrincipal getPrincipal() {
        return ((JecPrincipal) sessionContext.getCallerPrincipal());
    }

    @Override
    public void inserisciRigheRicercaManutenzioneListino(
            ParametriRicercaManutenzioneListino parametriRicercaManutenzioneListino)
                    throws ListinoManutenzioneNonValidoException {
        LOGGER.debug("--> Enter inserisciRigheRicercaManutenzioneListino");
        parametriRicercaManutenzioneListino.setCodiceAzienda(getPrincipal().getCodiceAzienda());
        parametriRicercaManutenzioneListino.setUserManutenzione(getPrincipal().getName());

        // verifico se i parametri hanno un listino valido in base alle eventuali righe manutenzione
        // esistenti
        checkListinoIsValid(parametriRicercaManutenzioneListino);

        // Calcolo il numero di inserimento per questo utente
        Query queryNumeroInserimento = panjeaDAO.prepareNamedQuery("RigaManutenzioneListino.nuovoNumero");
        queryNumeroInserimento.setParameter("userManutenzione",
                parametriRicercaManutenzioneListino.getUserManutenzione());
        Integer numeroInserimento = (Integer) queryNumeroInserimento.getSingleResult();
        numeroInserimento = ObjectUtils.defaultIfNull(numeroInserimento, 0);
        parametriRicercaManutenzioneListino.setNumeroInserimento(numeroInserimento);

        // *********** INIZIO CARICAMENTO CARRELLO *********
        String sqlQuery = "";
        Query query;
        // Inserisco le righe
        ManutenzioneListinoQueryBuilder queryBuilder = new ManutenzioneListinoQueryBuilder(
                parametriRicercaManutenzioneListino, panjeaDAO);
        sqlQuery = queryBuilder.getSql();
        query = panjeaDAO.getEntityManager().createNativeQuery(sqlQuery);
        ((HibernateQuery) query).getHibernateQuery().setProperties(parametriRicercaManutenzioneListino);
        try {
            panjeaDAO.executeQuery(query);
        } catch (DAOException e) {
            throw new GenericException(e);
        }

        // Aggiorno i decimali solamente se non li ho settati a mano. In caso li avrei già inseriti
        // quando ho inserito
        // gli articoli
        Collection<String> sqlQueries;
        if (parametriRicercaManutenzioneListino.getNumeroDecimali() == null) {
            DecimaliQueryBuilder decimaliQueryBuilder = new DecimaliQueryBuilder();
            sqlQueries = decimaliQueryBuilder.getSql(parametriRicercaManutenzioneListino);
            for (String queryDecimali : sqlQueries) {
                query = panjeaDAO.getEntityManager().createNativeQuery(queryDecimali);
                ((HibernateQuery) query).getHibernateQuery().setProperties(parametriRicercaManutenzioneListino);
                try {
                    panjeaDAO.executeQuery(query);
                } catch (DAOException e) {
                    throw new GenericException(e);
                }
            }
        }

        // Aggiorno i prezzi al carrello
        ProvenienzaPrezzoQueryBuilder prezzoQueryBuilder = ProvenienzaPrezzoQueryBuilder
                .getInstance(parametriRicercaManutenzioneListino);
        sqlQueries = prezzoQueryBuilder.getSql(parametriRicercaManutenzioneListino);

        for (String queryPrezzo : sqlQueries) {
            query = panjeaDAO.getEntityManager().createNativeQuery(queryPrezzo);
            ((HibernateQuery) query).getHibernateQuery().setProperties(parametriRicercaManutenzioneListino);
            try {
                panjeaDAO.executeQuery(query);
            } catch (DAOException e) {
                throw new GenericException(e);
            }
        }
        // }

        LOGGER.debug("--> Exit inserisciRigheRicercaManutenzioneListino");
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<RigaManutenzioneListino> salvaRigaManutenzioneListino(RigaManutenzioneListino rigaManutenzioneListino) {
        LOGGER.debug("--> Enter salvaRigaManutenzioneListino");

        List<RigaManutenzioneListino> righeSalvate = new ArrayList<RigaManutenzioneListino>();

        RigaManutenzioneListino rigaManutenzioneListinoSalvata = null;
        try {
            rigaManutenzioneListinoSalvata = panjeaDAO.save(rigaManutenzioneListino);
        } catch (Exception e) {
            LOGGER.error("--> Errore durante il salvataggio della rigaManutenzioneListino", e);
            throw new GenericException("Errore durante il salvataggio della rigaManutenzioneListino", e);
        }

        if (rigaManutenzioneListino.getListino() == null
                || rigaManutenzioneListino.getListino().getTipoListino() == ETipoListino.NORMALE) {
            righeSalvate.add(rigaManutenzioneListinoSalvata);
        } else {
            // se la riga si riferisce ad uno scaglione vado a riportare il numero dei decimali del
            // prezzo su tutti gli
            // scaglioni presenti per l'articolo.
            StringBuilder sb = new StringBuilder();
            sb.append("update maga_riga_manutenzione_listino ");
            sb.append("set numeroDecimali = ");
            sb.append(rigaManutenzioneListino.getNumeroDecimali());
            sb.append(" where articolo_id = ");
            sb.append(rigaManutenzioneListino.getArticolo().getId());
            sb.append(" and userManutenzione='");
            sb.append(rigaManutenzioneListino.getUserManutenzione());
            sb.append("' and listino_id=");
            sb.append(rigaManutenzioneListino.getListino().getId());
            executeUpdate(sb.toString());

            sb = new StringBuilder();
            sb.append("select rml from RigaManutenzioneListino rml ");
            sb.append("where rml.articolo = :articolo ");
            sb.append("and rml.listino = :listino ");
            sb.append("and rml.userManutenzione = :userManutenzione");

            Query query = panjeaDAO.prepareQuery(sb.toString());
            ((HibernateQuery) query).getHibernateQuery().setProperties(rigaManutenzioneListino);

            try {
                righeSalvate = panjeaDAO.getResultList(query);
            } catch (DAOException e) {
                LOGGER.error("--> errore durante il caricamento delle righe manutenzione salvate", e);
                throw new GenericException("errore durante il caricamento delle righe manutenzione salvate", e);
            }
        }

        LOGGER.debug("--> Exit salvaRigaManutenzioneListino");
        return righeSalvate;
    }

}
