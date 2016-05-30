package it.eurotn.panjea.manutenzioni.service;

import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.RemoteBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.manutenzioni.domain.ProdottoCollegato;
import it.eurotn.panjea.manutenzioni.manager.prodotticollegati.interfaces.ProdottiCollegatiManager;
import it.eurotn.panjea.manutenzioni.service.interfaces.ProdottiCollegatiService;

@Stateless(name = "Panjea.ProdottiCollegatiService")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@RemoteBinding(jndiBinding = "Panjea.ProdottiCollegatiService")
public class ProdottiCollegatiServiceBean implements ProdottiCollegatiService {

    @EJB
    private ProdottiCollegatiManager prodottiCollegatiManager;

    @Override
    public void cancellaProdottoCollegato(Integer id) {
        prodottiCollegatiManager.cancella(id);
    }

    @Override
    public List<ProdottoCollegato> caricaProdottiCollegati() {
        return prodottiCollegatiManager.caricaAll();
    }

    @Override
    public ProdottoCollegato caricaProdottoCollegatoById(Integer id) {
        return prodottiCollegatiManager.caricaById(id);
    }

    @Override
    public ProdottoCollegato salvaProdottoCollegato(ProdottoCollegato prodottoCollegato) {
        return prodottiCollegatiManager.salva(prodottoCollegato);
    }
}
