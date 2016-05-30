/**
 *
 */
package it.eurotn.panjea.magazzino.manager.schedearticolo;

import java.util.Date;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;
import org.joda.time.DateTime;

import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.anagrafica.domain.Preference;
import it.eurotn.panjea.anagrafica.service.exception.PreferenceNotFoundException;
import it.eurotn.panjea.anagrafica.service.interfaces.PreferenceService;
import it.eurotn.panjea.magazzino.domain.RigaArticolo;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.manager.schedearticolo.interfaces.MagazzinoControlloSchedeArticolo;
import it.eurotn.panjea.magazzino.manager.schedearticolo.interfaces.MagazzinoStatoSchedeArticolo;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.security.JecPrincipal;
import it.eurotn.util.PanjeaEJBUtil;

/**
 * @author fattazzo
 *
 */
@Stateless(name = "Panjea.MagazzinoControlloSchedeArticolo")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.MagazzinoControlloSchedeArticolo")
public class MagazzinoControlloSchedeArticoloBean implements MagazzinoControlloSchedeArticolo {

    private static Logger logger = Logger.getLogger(MagazzinoControlloSchedeArticoloBean.class);

    @EJB
    private PanjeaDAO panjeaDAO;

    @Resource
    private SessionContext context;

    @EJB
    private PreferenceService preferenceService;

    @EJB
    private MagazzinoStatoSchedeArticolo magazzinoStatoSchedeArticolo;

    @Override
    public void checkInvalidaSchedeArticolo(AreaMagazzino areaMagazzino) {

        checkInvalidaSchedeArticolo(areaMagazzino, false);
    }

    @Override
    public void checkInvalidaSchedeArticolo(AreaMagazzino areaMagazzino, boolean controllaConAreaPresente) {

        if (!isGestioneSchedaArticoloAbilitata()) {
            return;
        }

        // se non devo fare il controllo lancio subito la invada
        if (!controllaConAreaPresente) {
            magazzinoStatoSchedeArticolo.invalidaSchedeArticolo(areaMagazzino);
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("select am.dataRegistrazione,doc.entita.id ");
            sb.append("from AreaMagazzino am inner join am.documento doc ");
            sb.append("where am.id = :idAreaMagazzino ");
            Query query = panjeaDAO.prepareQuery(sb.toString());
            query.setParameter("idAreaMagazzino", areaMagazzino.getId());
            Object[] result = null;
            try {
                result = (Object[]) panjeaDAO.getSingleResult(query);
            } catch (Exception e) {
                logger.error("--> errore durante il caricamento dell'area magazzino " + areaMagazzino.getId(), e);
                throw new RuntimeException("errore durante il caricamento dell'area magazzino " + areaMagazzino.getId(),
                        e);
            }

            DateTime dataCurr = new DateTime(areaMagazzino.getDataRegistrazione());
            DateTime dataOld = new DateTime(result[0]);
            DateTime dataInizioMese = dataOld.dayOfMonth().withMinimumValue();
            DateTime dataFineMese = dataOld.dayOfMonth().withMaximumValue();
            boolean dataCambiata = dataCurr.isBefore(dataInizioMese) || dataCurr.isAfter(dataFineMese);

            Integer entitaOld = (Integer) result[1];
            Integer entita = areaMagazzino.getDocumento().getEntita() != null
                    ? areaMagazzino.getDocumento().getEntita().getId() : null;
            boolean entitaCambiata = entitaOld != null && !entitaOld.equals(entita);

            if (entitaCambiata || dataCambiata) {
                // invalido le schede articolo usando l'area magazzino con data inferiore
                if (dataCurr.isBefore(dataOld)) {
                    checkInvalidaSchedeArticolo(areaMagazzino);
                } else {
                    AreaMagazzino areaMagazzinoOld = PanjeaEJBUtil.cloneObject(areaMagazzino);
                    areaMagazzinoOld.setDataRegistrazione((Date) result[0]);
                    checkInvalidaSchedeArticolo(areaMagazzinoOld);
                }
            }
        }

    }

    @Override
    public void checkInvalidaSchedeArticolo(RigaArticolo rigaArticolo) {

        if (!isGestioneSchedaArticoloAbilitata()) {
            return;
        }

        if (!rigaArticolo.isNew()) {
            // ricarico la riga perchè potrei non avere tutte le proprietà (es: area magazzino)
            try {
                rigaArticolo = panjeaDAO.load(rigaArticolo.getClass(), rigaArticolo.getId());
            } catch (ObjectNotFoundException e) {
                logger.error("--> errore, riga magazzino non trovata.", e);
                throw new RuntimeException("errore, riga magazzino non trovata.", e);
            }
        }

        Date dataRegistrazione = rigaArticolo.getAreaMagazzino().getDataRegistrazione();
        DateTime dateTime = new DateTime(dataRegistrazione);
        Integer idArticolo = rigaArticolo.getArticolo().getId();

        Query query = panjeaDAO.prepareQuery(
                "select count(sa.id) from SchedaArticolo sa where sa.codiceAzienda = :codiceAzienda and sa.stato = 1 and sa.anno > :anno or (sa.anno = :anno and sa.mese >= :mese) and sa.articolo.id = :idArticolo");
        query.setParameter("codiceAzienda", getAzienda());
        query.setParameter("anno", dateTime.getYear());
        query.setParameter("mese", dateTime.getMonthOfYear());
        query.setParameter("idArticolo", idArticolo);

        Long numeroSchedaArticolo = ((Long) query.getSingleResult());

        if (numeroSchedaArticolo.intValue() > 0) {
            magazzinoStatoSchedeArticolo.invalidaSchedeArticolo(dateTime.getYear(), dateTime.getMonthOfYear(),
                    idArticolo);
        }

    }

    @Override
    public void checkInvalidaSchedeArticolo(RigaArticolo rigaArticoloPrecedente, RigaArticolo rigaArticoloSalvata) {

        // controllo se è cambiato l'articolo sulla riga per invalidare le schede del nuovo articolo
        boolean cambioArticoloNuovo = !rigaArticoloSalvata.getArticolo().getId()
                .equals(rigaArticoloPrecedente.getArticolo().getId());
        // se è cambiata la quantità oppure è rimasta uguale ma è cambiato l'articolo devo
        // invalidare le schede
        // del vecchio articolo
        boolean cambioQta = false;
        if (rigaArticoloPrecedente.getQta() != null || rigaArticoloSalvata.getQta() != null) {
            cambioQta = (rigaArticoloPrecedente.getQta() == null && rigaArticoloSalvata.getQta() != null)
                    || (rigaArticoloPrecedente.getQta() != null && rigaArticoloSalvata.getQta() == null)
                    || (rigaArticoloSalvata.getQta().doubleValue() != rigaArticoloPrecedente.getQta().doubleValue());
        }

        if (cambioQta || cambioArticoloNuovo) {
            checkInvalidaSchedeArticolo(rigaArticoloPrecedente);
        }

        if (cambioArticoloNuovo) {
            checkInvalidaSchedeArticolo(rigaArticoloSalvata);
        }

    }

    /**
     *
     * @return codice Azienda loggata
     */
    private String getAzienda() {
        JecPrincipal jecPrincipal = (JecPrincipal) context.getCallerPrincipal();
        return jecPrincipal.getCodiceAzienda();
    }

    /**
     * Carica dalle preference il valore della preferenza "gestioneSchedeArticolo". Se non esiste
     * viene restituito <code>false</code>.
     *
     * @return valore della preference
     */
    private boolean isGestioneSchedaArticoloAbilitata() {
        Boolean gestioneSchedaArticolo = null;
        try {
            Preference preference = preferenceService.caricaPreference("gestioneSchedeArticolo");
            gestioneSchedaArticolo = new Boolean(preference.getValore());
        } catch (PreferenceNotFoundException e1) {
            logger.warn("--> preferenza gestioneSchedeArticolo non trovata.");
            gestioneSchedaArticolo = Boolean.FALSE;
        }

        return gestioneSchedaArticolo;
    }

}
