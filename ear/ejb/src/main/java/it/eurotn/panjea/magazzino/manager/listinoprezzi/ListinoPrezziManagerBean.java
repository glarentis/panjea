package it.eurotn.panjea.magazzino.manager.listinoprezzi;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.hibernate.SQLQuery;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import commonj.work.WorkException;
import commonj.work.WorkItem;
import commonj.work.WorkManager;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.manager.interfaces.SediEntitaManager;
import it.eurotn.panjea.anagrafica.service.exception.AnagraficaServiceException;
import it.eurotn.panjea.exception.GenericException;
import it.eurotn.panjea.magazzino.domain.ProvenienzaPrezzoArticolo;
import it.eurotn.panjea.magazzino.domain.SedeMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino.ProvenienzaPrezzo;
import it.eurotn.panjea.magazzino.domain.moduloprezzo.ParametriCalcoloPrezzi;
import it.eurotn.panjea.magazzino.manager.interfaces.articolo.ArticoloManager;
import it.eurotn.panjea.magazzino.manager.listinoprezzi.interfaces.ListinoPrezziManager;
import it.eurotn.panjea.magazzino.manager.moduloprezzo.interfaces.PrezzoArticoloCalculator;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.security.JecPrincipal;

@Stateless(name = "Panjea.ListinoPrezziManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.ListinoPrezziManager")
public class ListinoPrezziManagerBean implements ListinoPrezziManager {
    private static final Logger LOGGER = Logger.getLogger(ListinoPrezziManagerBean.class);

    @EJB
    private PanjeaDAO panjeaDAO;

    @Resource
    private SessionContext sessionContext;

    @Resource(mappedName = "java:worker/defaultWorker")
    private WorkManager workManager;

    @EJB
    private PrezzoArticoloCalculator prezzoArticoloCalculator;

    @EJB
    private SediEntitaManager sediEntitaManager;

    @EJB
    private ArticoloManager articoloManager;

    /**
     *
     * @param idArticoloPartenza
     * @param numPagina
     * @param numRecordPerPagina
     * @return lista degli articoli per il quale calcolare i prezzi
     */
    private List<Integer> caricaIdArticoli(int idArticoloPartenza, int numPagina, int numRecordPerPagina) {
        StringBuilder sb = new StringBuilder();
        sb.append("select ");
        sb.append("id ");
        sb.append("from maga_articoli art ");
        sb.append("where codice>=(select codice from maga_articoli art where id=:idArticoloPartenza) ");
        sb.append("and codiceAzienda=:codiceAzienda ");
        sb.append("order by codice limit :startRecord,:numRecord ");
        SQLQuery sql = panjeaDAO.prepareNativeQuery(sb.toString());
        sql.setParameter("idArticoloPartenza", idArticoloPartenza);
        sql.setParameter("codiceAzienda", getAzienda());
        sql.setParameter("startRecord", numPagina * numRecordPerPagina);
        sql.setParameter("numRecord", numRecordPerPagina);
        @SuppressWarnings("unchecked")
        List<Integer> result = sql.list();
        return result;
    }

    @Override
    public List<ListinoPrezziDTO> caricaListinoPrezzi(ParametriListinoPrezzi parametriListinoPrezzi) {
        List<Integer> idArticoli = caricaIdArticoli(parametriListinoPrezzi.getIdArticoloPartenza(),
                parametriListinoPrezzi.getNumPagina(), parametriListinoPrezzi.getNumRecordInPagina());
        List<WorkItem> workers = new ArrayList<WorkItem>();

        SedeEntita sedePrincipale = null;
        try {
            sedePrincipale = sediEntitaManager.caricaSedePrincipaleEntita(parametriListinoPrezzi.getIdEntita());
        } catch (AnagraficaServiceException e1) {
            LOGGER.error("-->errore nel caricare la sede principale", e1);
            throw new GenericException("-->errore nel caricare la sede principale", e1);
        }
        SedeMagazzino sedeMagazzino = sedePrincipale.getSedeMagazzino();

        for (Integer idArticolo : idArticoli) {
            Integer idListino = null;
            if (sedeMagazzino.getListino() != null) {
                idListino = sedeMagazzino.getListino().getId();
            }

            Integer idListinoAlternativo = null;
            if (sedeMagazzino.getListinoAlternativo() != null) {
                idListinoAlternativo = sedeMagazzino.getListinoAlternativo().getId();
            }

            ParametriCalcoloPrezzi parametri = new ParametriCalcoloPrezzi(idArticolo, parametriListinoPrezzi.getData(),
                    idListino, idListinoAlternativo, parametriListinoPrezzi.getIdEntita(), sedePrincipale.getId(), null,
                    null, ProvenienzaPrezzo.LISTINO, null, (Integer) null, ProvenienzaPrezzoArticolo.DOCUMENTO, "EUR",
                    null, BigDecimal.ZERO);
            CalcoloPrezzoWork work = new CalcoloPrezzoWork(articoloManager, prezzoArticoloCalculator, parametri);
            try {
                workers.add(workManager.schedule(work));
            } catch (IllegalArgumentException | WorkException e) {
                LOGGER.error("-->errore nel lanciare lo scheduler per il work del calcolo prezzo per l'articolo con id "
                        + idArticolo, e);
                throw new GenericException(
                        "-->errore nel lanciare lo scheduler per il work del calcolo prezzo per l'articolo con id ", e);
            }
        }

        List<ListinoPrezziDTO> result = new ArrayList<>();
        try {
            workManager.waitForAll(workers, 200000);
            for (WorkItem workItem : workers) {
                result.add(((CalcoloPrezzoWork) workItem.getResult()).getRisultato());
            }
        } catch (Exception e) {
            LOGGER.error("-->errore nell'attesa che tutti i worker terminino", e);
            throw new GenericException("-->errore nell'attesa che tutti i worker terminino", e);
        }
        return result;
    }

    /**
     * @return codice azienda loggata
     */
    private String getAzienda() {
        return ((JecPrincipal) sessionContext.getCallerPrincipal()).getCodiceAzienda();
    }

}
