package it.eurotn.panjea.giroclienti.manager;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.SQLQuery;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.exception.GenericException;
import it.eurotn.panjea.giroclienti.domain.GiroClientiSettings;
import it.eurotn.panjea.giroclienti.domain.RigaGiroCliente;
import it.eurotn.panjea.giroclienti.exception.GiroClientiException;
import it.eurotn.panjea.giroclienti.manager.interfaces.GiroClientiAreaOrdineManager;
import it.eurotn.panjea.giroclienti.manager.interfaces.GiroClientiSettingsManager;
import it.eurotn.panjea.giroclienti.manager.interfaces.SchedeGiroClientiManager;
import it.eurotn.panjea.ordini.domain.documento.AreaOrdine;
import it.eurotn.panjea.ordini.domain.documento.TipoAreaOrdine;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.panjea.sicurezza.domain.Utente;
import it.eurotn.panjea.util.Giorni;

@Stateless(name = "Panjea.SchedeGiroClientiManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.SchedeGiroClientiManager")
public class SchedeGiroClientiManagerBean implements SchedeGiroClientiManager {

    private static final Logger LOGGER = Logger.getLogger(SchedeGiroClientiManagerBean.class);

    private static final String ID_UTENTE = "idUtente";

    @EJB
    private PanjeaDAO panjeaDAO;

    @EJB
    private GiroClientiSettingsManager giroClientiSettingsManager;

    @EJB
    private GiroClientiAreaOrdineManager giroClientiAreaOrdineManager;

    @Override
    public void cancellaRigheGiroCliente(Integer idUtente, Giorni giorno) {
        Query query = panjeaDAO.prepareNamedQuery("RigaGiroCliente.deleteByUtenteEGiorno");
        query.setParameter("idUtente", idUtente);
        query.setParameter("giorno", giorno);
        query.executeUpdate();
    }

    @Override
    public void cancellaSchede(Utente utente) {
        LOGGER.debug("--> Enter cancellaSchede");

        Query query = panjeaDAO.prepareNamedQuery("RigaGiroCliente.deleteByUtente");
        query.setParameter(ID_UTENTE, utente.getId());
        query.executeUpdate();

        LOGGER.debug("--> Exit cancellaSchede");
    }

    @Override
    public RigaGiroCliente caricaRigaGiroCliente(Integer idRiga) {
        LOGGER.debug("--> Enter caricaRigaGiroCliente");

        RigaGiroCliente rigaGiroCliente = null;
        try {
            rigaGiroCliente = panjeaDAO.load(RigaGiroCliente.class, idRiga);
            rigaGiroCliente.getSedeEntita().getId();
            rigaGiroCliente.getSedeEntita().getContatti().size();
        } catch (Exception e) {
            LOGGER.error("--> errore durante il caricamento della riga giro cliente.", e);
            throw new GiroClientiException("errore durante il caricamento della riga giro cliente.", e);
        }

        LOGGER.debug("--> Exit caricaRigaGiroCliente");
        return rigaGiroCliente;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<RigaGiroCliente> caricaRigheGiroCliente(Giorni giorno, Integer idUtente) {

        StringBuilder sb = new StringBuilder();
        sb.append("select distinct rgc ");
        sb.append(
                "from RigaGiroCliente rgc inner join fetch rgc.sedeEntita sed left join fetch sed.contatti as contatti ");
        sb.append("where rgc.giorno = :giorno ");
        sb.append("and rgc.utente.id = :idUtente ");
        sb.append("order by rgc.ora asc ");

        Query query = panjeaDAO.prepareQuery(sb.toString());
        query.setParameter("giorno", giorno);
        query.setParameter(ID_UTENTE, idUtente);

        List<RigaGiroCliente> righe = Collections.emptyList();
        try {
            righe = panjeaDAO.getResultList(query);
            for (RigaGiroCliente rigaGiroCliente : righe) {
                rigaGiroCliente.getSedeEntita().getContatti().size();
            }
        } catch (Exception e) {
            LOGGER.error("--> errore durante il caricamento delle schede giro clienti", e);
            throw new GiroClientiException("errore durante il caricamento delle schede giro clienti", e);
        }

        return righe;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<RigaGiroCliente> caricaRigheGiroCliente(Map<Object, Object> params) {
        LOGGER.debug("--> Enter caricaRigheGiroCliente");

        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
        String dataString = (String) params.get("data");
        Date data = null;
        try {
            data = !StringUtils.isBlank(dataString) ? df.parse(dataString) : null;
        } catch (ParseException e) {
            LOGGER.error("--> Errore durante il parse della data.", e);
        }

        StringBuilder sb = new StringBuilder(300);
        sb.append("select rgc ");
        sb.append(
                "from RigaGiroCliente rgc inner join fetch rgc.sedeEntita sed left join fetch sed.contatti as contatti ");
        sb.append("where rgc.data = :data ");

        boolean daEseguire = Boolean.valueOf((String) params.get("daEseguire"));
        if (daEseguire) {
            sb.append("and rgc.eseguito = false ");
        }

        String idUtenti = (String) params.get("idUtenti");
        sb.append("and rgc.utente.id in ( ");
        sb.append(idUtenti);

        sb.append(") order by rgc.utente.userName,rgc.ora asc");
        Query query = panjeaDAO.prepareQuery(sb.toString());
        query.setParameter("data", data);

        List<RigaGiroCliente> righeGiro;
        try {
            righeGiro = panjeaDAO.getResultList(query);
        } catch (DAOException e) {
            LOGGER.error("--> errore durante il caricamento delle righe giro cliente", e);
            throw new GiroClientiException("errore durante il caricamento delle righe giro cliente", e);
        }

        LOGGER.debug("--> Exit caricaRigheGiroCliente");
        return righeGiro;
    }

    @Override
    public void creaAreaOrdineGiroCliente(Integer idRigaGiroCliente) {
        LOGGER.debug("--> Enter creaAreaOrdineGiroCliente");

        GiroClientiSettings giroClientiSettings = giroClientiSettingsManager.caricaGiroClientiSettings();
        if (giroClientiSettings.getTipoAreaOrdineScheda() == null) {
            throw new GenericException("Tipo documento ordine non definito nelle preferenze");
        }

        RigaGiroCliente rigaGiroCliente = caricaRigaGiroCliente(idRigaGiroCliente);

        // se non esiste già un'area ordine per la riga la creo
        if (rigaGiroCliente.getAreaOrdine() != null) {
            return;
        }

        Documento documento = giroClientiAreaOrdineManager.creaDocumento(rigaGiroCliente,
                giroClientiSettings.getTipoAreaOrdineScheda().getTipoDocumento());

        TipoAreaOrdine tipoAreaOrdine = giroClientiSettings.getTipoAreaOrdineScheda();
        DepositoLite deposito = giroClientiSettings.getTipoAreaOrdineScheda().getDepositoOrigine();
        AreaOrdine areaOrdine = giroClientiAreaOrdineManager.creaAreaOrdine(rigaGiroCliente, documento, tipoAreaOrdine,
                deposito);

        rigaGiroCliente.setEseguito(true);
        rigaGiroCliente.setAreaOrdine(areaOrdine);
        salvaRigaGiroCliente(rigaGiroCliente);

        // setto l'area ordine creata a tutte le righe del giro dell'utente che hanno quel cliente
        StringBuilder sb = new StringBuilder(300);
        sb.append("update gcli_riga_giro ");
        sb.append("set areaOrdine_id = ");
        sb.append(rigaGiroCliente.getAreaOrdine().getId());
        sb.append(" where utente_id = ");
        sb.append(rigaGiroCliente.getUtente().getId());
        sb.append(" and giorno = ");
        sb.append(rigaGiroCliente.getGiorno().ordinal());
        sb.append(" and sedeEntita_id = ");
        sb.append(rigaGiroCliente.getSedeEntita().getId());
        sb.append(" and areaOrdine_id is null");
        SQLQuery query = panjeaDAO.prepareNativeQuery(sb.toString());
        query.executeUpdate();

        LOGGER.debug("--> Exit creaAreaOrdineGiroCliente");
    }

    @Override
    public RigaGiroCliente salvaRigaGiroCliente(RigaGiroCliente rigaGiroCliente) {
        LOGGER.debug("--> Enter salvaRigaGiroCliente");

        RigaGiroCliente rigaGiroClienteSalvata = null;
        try {
            rigaGiroClienteSalvata = panjeaDAO.save(rigaGiroCliente);
            rigaGiroClienteSalvata.getSedeEntita().getId();
            rigaGiroClienteSalvata.getSedeEntita().getContatti().size();
        } catch (Exception e) {
            LOGGER.error("--> errore durante il salvataggio della riga giro cliente.", e);
            throw new GiroClientiException("errore durante il salvataggio della riga giro cliente.", e);
        }

        return rigaGiroClienteSalvata;
    }

}
