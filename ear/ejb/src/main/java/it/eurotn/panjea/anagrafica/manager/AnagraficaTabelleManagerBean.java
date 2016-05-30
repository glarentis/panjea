package it.eurotn.panjea.anagrafica.manager;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.anagrafica.domain.ConversioneUnitaMisura;
import it.eurotn.panjea.anagrafica.domain.Lingua;
import it.eurotn.panjea.anagrafica.domain.NotaAnagrafica;
import it.eurotn.panjea.anagrafica.domain.UnitaMisura;
import it.eurotn.panjea.anagrafica.domain.ZonaGeografica;
import it.eurotn.panjea.anagrafica.manager.interfaces.AnagraficaTabelleManager;
import it.eurotn.panjea.anagrafica.manager.interfaces.AziendeManager;
import it.eurotn.panjea.anagrafica.service.exception.LinguaAziendaleDeleteException;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.security.JecPrincipal;
import it.eurotn.util.PanjeaEJBUtil;

/**
 * Manager per classi anagrafiche di anagrafica.
 *
 * @author Leonardo
 * @version 1.0, 28/dic/07
 */
@Stateless(name = "Panjea.AnagraficaTabelleManager")
@SecurityDomain("PanjeaLoginModule")
@LocalBinding(jndiBinding = "Panjea.AnagraficaTabelleManager")
public class AnagraficaTabelleManagerBean implements AnagraficaTabelleManager {

    private static Logger logger = Logger.getLogger(AnagraficaTabelleManagerBean.class);

    @EJB
    protected PanjeaDAO panjeaDAO;

    @Resource
    protected SessionContext context;

    @EJB
    protected AziendeManager aziendeManager;

    @Override
    public void cancellaConversioneUnitaMisura(ConversioneUnitaMisura conversioneUnitaMisura) {
        logger.debug("--> Enter cancellaConversioneUnitaMisura");

        try {
            panjeaDAO.delete(conversioneUnitaMisura);
        } catch (Exception e) {
            logger.error("--> errore durante la cancellazione della conversione unità misura "
                    + conversioneUnitaMisura.getId(), e);
            throw new RuntimeException(
                    "errore durante la cancellazione della conversione unità misura " + conversioneUnitaMisura.getId(),
                    e);
        }
        logger.debug("--> Exit cancellaConversioneUnitaMisura");
    }

    @Override
    public void cancellaLingua(Lingua lingua) throws LinguaAziendaleDeleteException {
        try {
            panjeaDAO.delete(lingua);
        } catch (Exception e) {
            logger.error("--> errore nel cancellare la lingua " + lingua, e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void cancellaNotaAnagrafica(NotaAnagrafica notaAnagrafica) {
        logger.debug("--> Enter cancellaNotaAnagrafica");
        try {
            panjeaDAO.delete(notaAnagrafica);
        } catch (Exception e) {
            logger.error("--> errore durante la cancellazione della nota anagrafica", e);
            throw new RuntimeException("errore durante la cancellazione della nota anagrafica", e);
        }

    }

    @Override
    public void cancellaUnitaMisura(UnitaMisura unitaMisura) {
        logger.debug("--> Enter cancellaUnitaMisura");

        try {
            panjeaDAO.delete(unitaMisura);
        } catch (Exception e) {
            logger.error("--> errore durante la cancellazione dell'unità misura " + unitaMisura.getId(), e);
            throw new RuntimeException("errore durante la cancellazione dell'unità misura " + unitaMisura.getId(), e);
        }
        logger.debug("--> Exit cancellaUnitaMisura");
    }

    @Override
    public void cancellaZonaGeografica(ZonaGeografica zonaGeografica) {
        logger.debug("--> Enter cancellaZonaGeografica");

        try {
            panjeaDAO.delete(zonaGeografica);
        } catch (Exception e) {
            logger.error("--> Errore durante la cancellazione della zona geografica " + zonaGeografica.getId(), e);
            throw new RuntimeException(
                    "--> Errore durante la cancellazione della zona geografica " + zonaGeografica.getId(), e);
        }

        logger.debug("--> Exit cancellaZonaGeografica");
    }

    @Override
    public ConversioneUnitaMisura caricaConversioneUnitaMisura(String unitaMisuraOrigine,
            String unitaMisuraDestinazione) {
        logger.debug("--> Enter caricaConversioneUnitaMisura");

        Query query = panjeaDAO.prepareNamedQuery("ConversioneUnitaMisura.caricaByUnitaMisuraOrigineDestinazione");
        query.setParameter("paramCodiceAzienda", getAzienda());
        query.setParameter("paramCodiceUnitaMisuraOrigine", unitaMisuraOrigine);
        query.setParameter("paramCodiceUnitaMisuraDestinazione", unitaMisuraDestinazione);

        ConversioneUnitaMisura result = null;

        try {
            result = (ConversioneUnitaMisura) panjeaDAO.getSingleResult(query);
        } catch (ObjectNotFoundException e) {
            logger.warn("Nessuna conversione unità di misura trovata per " + unitaMisuraOrigine + "/"
                    + unitaMisuraDestinazione);
        } catch (Exception e) {
            logger.error("--> Errore durante il caricamento delle conversioni unità di misura", e);
            throw new RuntimeException("Errore durante il caricamento delle conversioni unità di misura", e);
        }

        logger.debug("--> Exit caricaConversioneUnitaMisura");
        return result;

    }

    @Override
    @SuppressWarnings("unchecked")
    public List<ConversioneUnitaMisura> caricaConversioniUnitaMisura() {
        logger.debug("--> Enter caricaConversioniUnitaMisura");

        Query query = panjeaDAO.prepareNamedQuery("ConversioneUnitaMisura.caricaAll");
        query.setParameter("paramCodiceAzienda", getAzienda());

        List<ConversioneUnitaMisura> listResult = new ArrayList<ConversioneUnitaMisura>();

        try {
            listResult = panjeaDAO.getResultList(query);
        } catch (DAOException e) {
            logger.error("--> Errore durante il caricamento delle conversioni unità di misura", e);
            throw new RuntimeException("Errore durante il caricamento delle conversioni unità di misura", e);
        }

        logger.debug("--> Exit caricaConversioniUnitaMisura");
        return listResult;
    }

    @Override
    public Lingua caricaLingua(Lingua lingua) {
        logger.debug("--> Enter caricaLingua");
        try {
            lingua = panjeaDAO.load(Lingua.class, lingua.getId());
        } catch (Exception e) {
            logger.error("--> errore nel caricare la lingua " + lingua.getId(), e);
            throw new RuntimeException(e);
        }
        logger.debug("--> Exit caricaLingua");
        return lingua;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Lingua> caricaLingue() {
        Query query = panjeaDAO.prepareNamedQuery("Lingua.caricaLingue");
        query.setParameter("azienda", getAzienda());
        List<Lingua> lingua = query.getResultList();
        return lingua;
    }

    @Override
    public List<NotaAnagrafica> caricaNoteAnagrafica() {
        logger.debug("--> Enter caricaNoteAnagrafica");
        Query query = panjeaDAO.prepareNamedQuery("NotaAnagrafica.caricaAll");
        @SuppressWarnings("unchecked")
        List<NotaAnagrafica> note = query.getResultList();
        return note;
    }

    @Override
    public List<UnitaMisura> caricaUnitaMisura() {
        return caricaUnitaMisura("%");
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<UnitaMisura> caricaUnitaMisura(String codice) {
        logger.debug("--> Enter caricaUnitaMisura");

        if (codice == null) {
            codice = "%";
        }

        StringBuilder sb = new StringBuilder(
                "from UnitaMisura u where u.codiceAzienda = :paramIdAzienda and u.codice like :paramCodice order by u.codice");

        Query query = panjeaDAO.prepareQuery(sb.toString());
        query.setParameter("paramIdAzienda", getAzienda());
        query.setParameter("paramCodice", codice);

        List<UnitaMisura> listResult = new ArrayList<UnitaMisura>();

        try {
            listResult = panjeaDAO.getResultList(query);
        } catch (DAOException e) {
            logger.error("--> Errore durante il caricamento delle unità di misura", e);
            throw new RuntimeException("Errore durante il caricamento delle unità di misura", e);
        }

        logger.debug("--> Exit caricaUnitaMisura");
        return listResult;
    }

    @Override
    public UnitaMisura caricaUnitaMisura(UnitaMisura unitaMisura) {
        logger.debug("--> Enter caricaUnitaMisura");

        try {
            unitaMisura = panjeaDAO.load(UnitaMisura.class, unitaMisura.getId());
        } catch (ObjectNotFoundException e) {
            logger.error("--> errore durante il caricamento dell'unità di misura " + unitaMisura.getId(), e);
            throw new RuntimeException("errore durante il caricamento dell'unità di misura " + unitaMisura.getId(), e);
        }

        logger.debug("--> Exit caricaUnitaMisura");
        return unitaMisura;
    }

    @Override
    public UnitaMisura caricaUnitaMisuraByCodice(String codice) {
        logger.debug("--> Enter caricaUnitaMisura");

        UnitaMisura unitaMisura = null;
        try {
            Query query = panjeaDAO.prepareNamedQuery("UnitaMisura.caricaByCodice");
            query.setParameter("paramCodice", codice);
            unitaMisura = (UnitaMisura) panjeaDAO.getSingleResult(query);
        } catch (ObjectNotFoundException e) {
            return null;
        } catch (DAOException e) {
            logger.error("-->errore ", e);
            throw new RuntimeException("Errore nel caricare l'unità di misura con codice " + codice, e);
        }
        logger.debug("--> Exit caricaUnitaMisura");
        return unitaMisura;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<ZonaGeografica> caricaZoneGeografiche(String fieldSearch, String valueSearch) {
        logger.debug("--> Enter caricaZoneGeografiche");
        List<ZonaGeografica> list = new ArrayList<ZonaGeografica>();
        StringBuilder sb = new StringBuilder("select zg from ZonaGeografica zg where zg.codiceAzienda=")
                .append(PanjeaEJBUtil.addQuote(getAzienda()));
        if (valueSearch != null) {
            sb.append(" and ").append(fieldSearch).append(" like ").append(PanjeaEJBUtil.addQuote(valueSearch));
        }
        sb.append(" order by ");
        sb.append(fieldSearch);
        Query query = panjeaDAO.prepareQuery(sb.toString());
        try {
            list = panjeaDAO.getResultList(query);
        } catch (DAOException e) {
            logger.error("--> Errore durante il caricamento dei mezzi di trasporto", e);
            throw new RuntimeException("Errore durante il caricamento dei mezzi di trasporto", e);
        }
        logger.debug("--> Exit caricaZoneGeografiche");
        return list;
    }

    /**
     * ritorna la descrizione della azienda corrente.
     *
     * @return descrizione della azienda corrente
     */
    private String getAzienda() {
        JecPrincipal jecPrincipal = (JecPrincipal) context.getCallerPrincipal();
        return jecPrincipal.getCodiceAzienda();
    }

    @Override
    public ConversioneUnitaMisura salvaConversioneUnitaMisura(ConversioneUnitaMisura conversioneUnitaMisura) {
        logger.debug("--> Enter salvaConversioneUnitaMisura");

        // se si sta salvando una nuova unità di misura setto l'azienda
        if (conversioneUnitaMisura.isNew()) {
            conversioneUnitaMisura.setCodiceAzienda(getAzienda());
        }

        try {
            conversioneUnitaMisura = panjeaDAO.save(conversioneUnitaMisura);
        } catch (Exception e) {
            logger.error("--> Errore durante il salvataggio dell'unità di misura " + conversioneUnitaMisura, e);
            throw new RuntimeException("Errore durante il salvataggio dell'unità di misura " + conversioneUnitaMisura,
                    e);
        }

        logger.debug("--> Exit salvaUnitaMisura");
        return conversioneUnitaMisura;
    }

    @Override
    public Lingua salvaLingua(Lingua lingua) {
        logger.debug("-->enter salvaLingua " + lingua);
        try {
            // Se non ho impostato l'azienda la imposto
            if (lingua.getAzienda() == null || lingua.getAzienda().equals("")) {
                lingua.setAzienda(getAzienda());
            }
            lingua = panjeaDAO.save(lingua);
        } catch (Exception e) {
            logger.error("--> errore Errore nel salvare la lingua", e);
            throw new RuntimeException(e);
        }
        logger.debug("--> Exit salvaLingua");
        return lingua;
    }

    @Override
    public NotaAnagrafica salvaNotaAnagrafica(NotaAnagrafica notaAnagrafica) {
        logger.debug("--> Enter salvaNotaAnagrafica");
        NotaAnagrafica notaSalvata = null;
        try {
            notaSalvata = panjeaDAO.save(notaAnagrafica);
        } catch (DAOException e) {
            logger.error("--> errore nel salvare la nota anagrafica.", e);
            throw new RuntimeException("errore nel salvare la nota anagrafica.", e);
        }
        logger.debug("--> Exit salvaNotaAnagrafica");
        return notaSalvata;
    }

    @Override
    public UnitaMisura salvaUnitaMisura(UnitaMisura unitaMisura) {
        logger.debug("--> Enter salvaUnitaMisura");

        // se si sta salvando una nuova unità di misura setto l'azienda
        if (unitaMisura.isNew()) {
            unitaMisura.setCodiceAzienda(getAzienda());
        }

        try {
            unitaMisura = panjeaDAO.save(unitaMisura);
        } catch (Exception e) {
            logger.error("--> Errore durante il salvataggio dell'unità di misura " + unitaMisura, e);
            throw new RuntimeException("Errore durante il salvataggio dell'unità di misura " + unitaMisura, e);
        }

        logger.debug("--> Exit salvaUnitaMisura");
        return unitaMisura;
    }

    @Override
    public ZonaGeografica salvaZonaGeografica(ZonaGeografica zonaGeografica) {
        logger.debug("--> Enter salvaZonaGeografica");

        if (zonaGeografica.isNew()) {
            zonaGeografica.setCodiceAzienda(getAzienda());
        }

        ZonaGeografica zonaGeograficaSalvata = null;
        try {
            zonaGeograficaSalvata = panjeaDAO.save(zonaGeografica);
        } catch (Exception e) {
            logger.error("--> Errore durante il salvataggio della zona geografica", e);
            throw new RuntimeException("Errore durante il salvataggio della zona geografica", e);
        }

        logger.debug("--> Exit salvaZonaGeografica");
        return zonaGeograficaSalvata;
    }
}
