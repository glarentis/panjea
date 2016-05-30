package it.eurotn.panjea.giroclienti.manager;

import java.util.Date;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.log4j.Logger;
import org.hibernate.SQLQuery;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.giroclienti.domain.GiroSedeCliente;
import it.eurotn.panjea.giroclienti.domain.ModalitaCopiaGiroClienti;
import it.eurotn.panjea.giroclienti.exception.GiroClientiException;
import it.eurotn.panjea.giroclienti.manager.interfaces.GiroClientiAnagraficaManager;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.panjea.sicurezza.domain.Utente;
import it.eurotn.panjea.util.Giorni;

@Stateless(name = "Panjea.GiroClientiAnagraficaManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.GiroClientiAnagraficaManager")
public class GiroClientiAnagraficaManagerBean implements GiroClientiAnagraficaManager {

    private static final Logger LOGGER = Logger.getLogger(GiroClientiAnagraficaManagerBean.class);

    @EJB
    private PanjeaDAO panjeaDAO;

    @Override
    public void cancellaGiroSedeCliente(GiroSedeCliente giroSedeCliente) {
        try {
            panjeaDAO.delete(giroSedeCliente);
        } catch (Exception e) {
            LOGGER.error("--> errore durante la cancellazione del giro sede cliente", e);
            throw new GiroClientiException("errore durante la cancellazione del giro sede cliente", e);
        }
    }

    @Override
    public void cancellaGiroSedeCliente(Integer idEntita) {
        LOGGER.debug("--> Enter cancellaGiroSedeCliente");

        StringBuilder sb = new StringBuilder();
        sb.append(
                "delete gcli.* from gcli_giro_sede_clienti gcli inner join anag_sedi_entita sediEntita on gcli.sedeEntita_id = sediEntita.id ");
        sb.append(" inner join anag_entita ent on ent.id = sediEntita.entita_id ");
        sb.append("where ent.id = ");
        sb.append(idEntita);
        SQLQuery query = panjeaDAO.prepareNativeQuery(sb.toString());

        try {
            query.executeUpdate();
        } catch (Exception e) {
            LOGGER.error("--> errore durante la cancellazione dei giri sede cliente.", e);
            throw new GiroClientiException("errore durante la cancellazione dei giri sede cliente.", e);
        }

        LOGGER.debug("--> Exit cancellaGiroSedeCliente");
    }

    /**
     * Cancella il giro cliente per l'utente e il giorno specificato.
     *
     * @param idUtente
     *            id utente
     * @param giorno
     *            giorno
     */
    private void cancellaGiroSedeCliente(Integer idUtente, Giorni giorno) {
        LOGGER.debug("--> Enter cancellaGiroSedeCliente");

        StringBuilder sb = new StringBuilder();
        sb.append("delete gcli.* from gcli_giro_sede_clienti gcli ");
        sb.append("where gcli.giorno = :giorno and ");
        sb.append("gcli.utente_id = :idUtente");
        SQLQuery query = panjeaDAO.prepareNativeQuery(sb.toString());
        query.setParameter("giorno", giorno.ordinal());
        query.setParameter("idUtente", idUtente);

        try {
            query.executeUpdate();
        } catch (Exception e) {
            LOGGER.error("--> errore durante la cancellazione dei giri sede cliente.", e);
            throw new GiroClientiException("errore durante la cancellazione dei giri sede cliente.", e);
        }

        LOGGER.debug("--> Exit cancellaGiroSedeCliente");
    }

    @Override
    public void cancellaGiroSedeCliente(Integer idSedeEntita, Giorni giorno, Date ora) {
        LOGGER.debug("--> Enter cancellaGiroSedeCliente");

        StringBuilder sb = new StringBuilder();
        sb.append("delete from gcli_giro_sede_clienti ");
        sb.append("where sedeEntita_id = :idSedeEntita and ");
        sb.append("giorno = :giorno and ");
        sb.append("ora = :ora ");
        sb.append("limit 1");
        SQLQuery query = panjeaDAO.prepareNativeQuery(sb.toString());
        query.setParameter("idSedeEntita", idSedeEntita);
        query.setParameter("giorno", giorno.ordinal());
        query.setParameter("ora", DateFormatUtils.format(ora, "HH:mm:ss"));

        try {
            query.executeUpdate();
        } catch (Exception e) {
            LOGGER.error("--> errore durante la cancellazione dei giri sede cliente.", e);
            throw new GiroClientiException("errore durante la cancellazione dei giri sede cliente.", e);
        }

        LOGGER.debug("--> Exit cancellaGiroSedeCliente");
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<GiroSedeCliente> caricaGiroSedeCliente(Giorni giorno, Integer idUtente) {
        LOGGER.debug("--> Enter caricaGiroSedeCliente");

        Query query = panjeaDAO.prepareNamedQuery("GiroSedeCliente.caricaByUtenteEGiorno");
        query.setParameter("idUtente", idUtente);
        query.setParameter("giorno", giorno);

        List<GiroSedeCliente> giri = null;
        try {
            giri = panjeaDAO.getResultList(query);
        } catch (Exception e) {
            LOGGER.error("--> errore durante il caricamento dei giri cliente.", e);
            throw new GiroClientiException("errore durante il caricamento dei giri cliente.", e);
        }

        LOGGER.debug("--> Exit caricaGiroSedeCliente");
        return giri;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<GiroSedeCliente> caricaGiroSedeCliente(Integer idEntita) {
        LOGGER.debug("--> Enter caricaGiroSedeCliente");

        Query query = panjeaDAO.prepareNamedQuery("GiroSedeCliente.caricaByEntita");
        query.setParameter("idEntita", idEntita);

        List<GiroSedeCliente> giriSede = null;
        try {
            giriSede = panjeaDAO.getResultList(query);
        } catch (Exception e) {
            LOGGER.error("--> errore durante il caricamento dei giri sede cliente.", e);
            throw new GiroClientiException("errore durante il caricamento dei giri sede cliente.", e);
        }

        LOGGER.debug("--> Exit caricaGiroSedeCliente");
        return giriSede;
    }

    @Override
    public void copiaGiroSedeClienti(Integer idUtente, Giorni giorno, Integer idUtenteDestinazione,
            Giorni giornoDestinazione, ModalitaCopiaGiroClienti modalitaCopia) {

        // se devo sovrascrivere vado a cancellare subito i giro del cliente e giorno di destinazione
        if (modalitaCopia == ModalitaCopiaGiroClienti.SOVRASCRIVI) {
            cancellaGiroSedeCliente(idUtenteDestinazione, giornoDestinazione);
        }

        List<GiroSedeCliente> giroSedeClienteOrigine = caricaGiroSedeCliente(giorno, idUtente);
        for (GiroSedeCliente giroOrigine : giroSedeClienteOrigine) {
            GiroSedeCliente giroDestinazione = new GiroSedeCliente();
            giroDestinazione.setOra(giroOrigine.getOra());
            giroDestinazione.setSedeEntita(giroOrigine.getSedeEntita());
            giroDestinazione.setUtente(panjeaDAO.loadLazy(Utente.class, idUtenteDestinazione));
            giroDestinazione.setGiorno(giornoDestinazione);

            salvaGiroSedeCliente(giroDestinazione);
        }

    }

    @Override
    public GiroSedeCliente salvaGiroSedeCliente(GiroSedeCliente giroSedeCliente) {
        LOGGER.debug("--> Enter salvaGiroSedeCliente");

        GiroSedeCliente giroSedeClienteSalvato = null;
        try {
            giroSedeClienteSalvato = panjeaDAO.save(giroSedeCliente);
        } catch (Exception e) {
            LOGGER.error("--> errore durante il salvataggio del giro sede cliente", e);
            throw new GiroClientiException("errore durante il salvataggio del giro sede cliente", e);
        }

        LOGGER.debug("--> Exit salvaGiroSedeCliente");
        return giroSedeClienteSalvato;
    }

    @Override
    public void salvaGiroSedeCliente(List<GiroSedeCliente> giri) {
        for (GiroSedeCliente giroSedeCliente : giri) {
            salvaGiroSedeCliente(giroSedeCliente);
        }
    }

}
