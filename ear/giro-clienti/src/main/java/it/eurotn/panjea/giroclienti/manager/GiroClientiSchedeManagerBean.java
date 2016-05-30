package it.eurotn.panjea.giroclienti.manager;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jboss.annotation.IgnoreDependency;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.panjea.giroclienti.domain.GiroSedeCliente;
import it.eurotn.panjea.giroclienti.domain.RigaGiroCliente;
import it.eurotn.panjea.giroclienti.exception.GiroClientiException;
import it.eurotn.panjea.giroclienti.manager.interfaces.GiroClientiAnagraficaManager;
import it.eurotn.panjea.giroclienti.manager.interfaces.GiroClientiSchedeManager;
import it.eurotn.panjea.giroclienti.manager.interfaces.SchedeGiroClientiManager;
import it.eurotn.panjea.giroclienti.util.SchedaGiroClienteStampa;
import it.eurotn.panjea.giroclienti.util.SchedaGiroClientiDTO;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.panjea.util.Giorni;
import it.eurotn.util.PanjeaEJBUtil;

@Stateless(name = "Panjea.GiroClientiSchedeManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.GiroClientiSchedeManager")
public class GiroClientiSchedeManagerBean implements GiroClientiSchedeManager {

    private static final Logger LOGGER = Logger.getLogger(GiroClientiSchedeManagerBean.class);

    @EJB
    private PanjeaDAO panjeaDAO;

    @EJB
    private GiroClientiAnagraficaManager giroClientiAnagraficaManager;

    @EJB
    @IgnoreDependency
    private SchedeGiroClientiManager schedeGiroClientiManager;

    @Override
    public Date[] caricaDateSchedaSettimanale(Integer idUtente) {

        Query query = panjeaDAO
                .prepareQuery("select min(rgc.data) from RigaGiroCliente rgc where rgc.utente.id = :idUtente");
        query.setParameter("idUtente", idUtente);

        Date data = null;
        try {
            data = (Date) panjeaDAO.getSingleResult(query);
        } catch (Exception e) {
            LOGGER.error("--> errore durante il caricamento delle date della scheda settimanale.", e);
            throw new GiroClientiException("errore durante il caricamento delle date della scheda settimanale.", e);
        }
        if (data == null) {
            data = Calendar.getInstance().getTime();
        }

        return PanjeaEJBUtil.getDateOfWeek(data);
    }

    @Override
    public SchedaGiroClientiDTO caricaSchedaSettimana(Integer idUtente, Giorni giorno) {

        Date oggi = Calendar.getInstance().getTime();
        Date[] dates = PanjeaEJBUtil.getDateOfWeek(oggi);

        // carico le righe presenti per il giorno della settimana
        List<RigaGiroCliente> righePresenti = schedeGiroClientiManager.caricaRigheGiroCliente(giorno, idUtente);

        Date dataScheda = dates[giorno.ordinal()];

        if (righePresenti.isEmpty()) {
            // se non ci sono righe creo la nuova scheda del giorno
            schedeGiroClientiManager.cancellaRigheGiroCliente(idUtente, giorno);

            List<GiroSedeCliente> giroSedeCliente = giroClientiAnagraficaManager.caricaGiroSedeCliente(giorno,
                    idUtente);
            for (GiroSedeCliente giro : giroSedeCliente) {
                RigaGiroCliente rigaGiroCliente = schedeGiroClientiManager
                        .salvaRigaGiroCliente(new RigaGiroCliente(giro, dataScheda));
                righePresenti.add(rigaGiroCliente);
            }
        } else {
            dataScheda = righePresenti.get(0).getData();
        }

        SchedaGiroClientiDTO schedaGiro = new SchedaGiroClientiDTO();
        schedaGiro.setData(dataScheda);
        schedaGiro.setGiorno(giorno);
        schedaGiro.setRigheGiroCliente(righePresenti);

        return schedaGiro;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<SchedaGiroClienteStampa> caricaSchedeStampa(Map<Object, Object> params) {
        LOGGER.debug("--> Enter caricaSchedeStampa");

        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
        String dataString = (String) params.get("data");
        Date data = null;
        try {
            data = !StringUtils.isBlank(dataString) ? df.parse(dataString) : null;
        } catch (ParseException e) {
            LOGGER.error("--> Errore durante il parse della data.", e);
        }

        StringBuilder sb = new StringBuilder();
        sb.append("select doc.entita as entita, ");
        sb.append("           ro as rigaArticolo ");
        sb.append("from RigaGiroCliente rgc ");
        sb.append("          inner join rgc.areaOrdine ao ");
        sb.append("          inner join ao.documento doc ");
        sb.append("          inner join ao.righe ro ");
        sb.append("where rgc.data = :paramData ");
        sb.append(" and ro.articolo is not null ");
        sb.append(" and ro.articolo.produzione = true ");
        sb.append("order by doc.entita.anagrafica.denominazione");

        Query query = panjeaDAO.prepareQuery(sb.toString(), SchedaGiroClienteStampa.class, null);
        query.setParameter("paramData", data);

        List<SchedaGiroClienteStampa> schede = new ArrayList<>();
        try {
            schede = panjeaDAO.getResultList(query);
        } catch (DAOException e) {
            LOGGER.error("--> errore durante il caricamento delle schede giro cliente per la stampa.", e);
            throw new GiroClientiException("errore durante il caricamento delle schede giro cliente per la stampa.", e);
        }

        LOGGER.debug("--> Exit caricaSchedeStampa");
        return schede;
    }

}
