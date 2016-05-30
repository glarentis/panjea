package it.eurotn.panjea.giroclienti.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.RemoteBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.giroclienti.domain.RigaGiroCliente;
import it.eurotn.panjea.giroclienti.manager.interfaces.GiroClientiSchedeManager;
import it.eurotn.panjea.giroclienti.manager.interfaces.SchedeGiroClientiManager;
import it.eurotn.panjea.giroclienti.service.interfaces.SchedeGiroClientiService;
import it.eurotn.panjea.giroclienti.util.SchedaGiroClienteStampa;
import it.eurotn.panjea.giroclienti.util.SchedaGiroClientiDTO;
import it.eurotn.panjea.sicurezza.domain.Utente;
import it.eurotn.panjea.util.Giorni;

@Stateless(name = "Panjea.SchedeGiroClientiService")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@RemoteBinding(jndiBinding = "Panjea.SchedeGiroClientiService")
public class SchedeGiroClientiServiceBean implements SchedeGiroClientiService {

    @EJB
    private SchedeGiroClientiManager schedeGiroClientiManager;

    @EJB
    private GiroClientiSchedeManager giroClientiSchedeManager;

    @Override
    public void cancellaSchede(Utente utente) {
        schedeGiroClientiManager.cancellaSchede(utente);

    }

    @Override
    public Date[] caricaDateSchedaSettimanale(Integer idUtente) {
        return giroClientiSchedeManager.caricaDateSchedaSettimanale(idUtente);
    }

    @Override
    public RigaGiroCliente caricaRigaGiroCliente(Integer idRiga) {
        return schedeGiroClientiManager.caricaRigaGiroCliente(idRiga);
    }

    @Override
    public List<RigaGiroCliente> caricaRigheGiroCliente(Giorni giorno, Utente utente) {
        return schedeGiroClientiManager.caricaRigheGiroCliente(giorno, utente.getId());
    }

    @Override
    public List<RigaGiroCliente> caricaRigheGiroCliente(Map<Object, Object> params) {
        return schedeGiroClientiManager.caricaRigheGiroCliente(params);
    }

    @Override
    public SchedaGiroClientiDTO caricaSchedaSettimana(Integer idUtente, Giorni giorno) {
        return giroClientiSchedeManager.caricaSchedaSettimana(idUtente, giorno);
    }

    @Override
    public List<SchedaGiroClienteStampa> caricaSchedeStampa(Map<Object, Object> params) {
        return giroClientiSchedeManager.caricaSchedeStampa(params);
    }

    @Override
    public void creaAreaOrdineGiroCliente(Integer idRigaGiroCliente) {
        schedeGiroClientiManager.creaAreaOrdineGiroCliente(idRigaGiroCliente);
    }

    @Override
    public RigaGiroCliente salvaRigaGiroCliente(RigaGiroCliente rigaGiroCliente) {
        return schedeGiroClientiManager.salvaRigaGiroCliente(rigaGiroCliente);
    }

}
