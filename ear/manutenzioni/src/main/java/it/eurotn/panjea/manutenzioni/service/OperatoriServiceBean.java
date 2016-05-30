package it.eurotn.panjea.manutenzioni.service;

import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.RemoteBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.manutenzioni.domain.Operatore;
import it.eurotn.panjea.manutenzioni.manager.operatori.ParametriRicercaOperatori;
import it.eurotn.panjea.manutenzioni.manager.operatori.interfaces.OperatoriManager;
import it.eurotn.panjea.manutenzioni.service.interfaces.OperatoriService;

@Stateless(name = "Panjea.OperatoriService")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@RemoteBinding(jndiBinding = "Panjea.OperatoriService")
public class OperatoriServiceBean implements OperatoriService {

    @EJB
    private OperatoriManager operatoriManager;

    @Override
    public void cancellaOperatore(Integer id) {
        operatoriManager.cancella(id);
    }

    @Override
    public Operatore caricaOperatoreById(Integer id) {
        return operatoriManager.caricaById(id);
    }

    @Override
    public List<Operatore> caricaOperatori() {
        return operatoriManager.caricaAll();
    }

    @Override
    public List<Operatore> ricercaOperatori(ParametriRicercaOperatori parametri) {
        return operatoriManager.ricercaOperatori(parametri);
    }

    @Override
    public Operatore salvaOperatore(Operatore operatore) {
        return operatoriManager.salva(operatore);
    }

    @Override
    public void sostituisciOperatore(Integer idOperatoreDaSostituire, Integer idOperatore, boolean sostituisciTecnico,
            boolean sostituisciCaricatore) {
        operatoriManager.sostituisciOperatore(idOperatoreDaSostituire, idOperatore, sostituisciTecnico,
                sostituisciCaricatore);
    }
}
