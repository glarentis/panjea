package it.eurotn.panjea.manutenzioni.manager.prodotticollegati;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.manager.interfaces.CrudManagerBean;
import it.eurotn.panjea.manutenzioni.domain.ProdottoCollegato;
import it.eurotn.panjea.manutenzioni.manager.prodotticollegati.interfaces.ProdottiCollegatiManager;

@Stateless(name = "Panjea.ProdottiCollegatiManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.ProdottiCollegatiManager")
public class ProdottiCollegatiManagerBean extends CrudManagerBean<ProdottoCollegato>
        implements ProdottiCollegatiManager {

    @Override
    protected Class<ProdottoCollegato> getManagedClass() {
        return ProdottoCollegato.class;
    }
}